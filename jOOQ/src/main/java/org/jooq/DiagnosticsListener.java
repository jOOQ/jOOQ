/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * A diagnostics listener.
 *
 * @author Lukas Eder
 */
public interface DiagnosticsListener {

    /**
     * The fetched JDBC {@link ResultSet} returned more rows than necessary.
     * <p>
     * An event indicating that a JDBC {@link ResultSet} was fetched with
     * <code>A</code> rows, but only <code>B</code> rows (<code>B &lt; A</code>)
     * were consumed.
     * <p>
     * Typically, this problem can be remedied by applying the appropriate
     * <code>LIMIT</code> clause in SQL, or {@link SelectLimitStep#limit(int)}
     * clause in jOOQ.
     */
    void tooManyRowsFetched(DiagnosticsContext ctx);

    /**
     * The fetched JDBC {@link ResultSet} returned more columns than necessary.
     * <p>
     * An event indicating that a JDBC {@link ResultSet} was fetched with
     * <code>A</code> columns, but only <code>B</code> (<code>B &lt; A</code>)
     * were consumed.
     * <p>
     * Typically, this problem can be remedied by not running a
     * <code>SELECT *</code> query when this isn't strictly required.
     */
    void tooManyColumnsFetched(DiagnosticsContext ctx);

    /**
     * The fetched JDBC {@link ResultSet} returned a value for a column, on
     * which {@link ResultSet#wasNull()} was called unnecessarily (more than
     * once, or for a non-primitive type).
     */
    void unnecessaryWasNullCall(DiagnosticsContext ctx);

    /**
     * The fetched JDBC {@link ResultSet} returned a primitive type value for a
     * column, which could have been null, but {@link ResultSet#wasNull()} was
     * not called.
     */
    void missingWasNullCall(DiagnosticsContext ctx);

    /**
     * The executed JDBC statement has duplicates.
     * <p>
     * Many databases maintain an execution plan cache, which remembers
     * execution plans for a given SQL string. These caches often use the
     * verbatim SQL string (or a hash thereof) as a key, meaning that "similar"
     * but not identical statements will produce different keys. This may be
     * desired in rare cases when querying skewed data, as a hack to force the
     * optimiser to calculate a new plan for a given "similar" but not identical
     * query, but mostly, this is not desirable as calculating execution plans
     * can turn out to be expensive.
     * <p>
     * Examples of such duplicate statements include:
     * <p>
     * <h3>Whitespace differences</h3>
     * <p>
     * <code><pre>
     * SELECT * FROM  actor;
     * SELECT  * FROM actor;
     * </pre></code>
     * <p>
     * <h3>Inline bind values</h3>
     * <p>
     * <code><pre>
     * SELECT * FROM actor WHERE id = 1;
     * SELECT * FROM actor WHERE id = 2;
     * </pre></code>
     * <p>
     * <h3>Aliasing and qualification</h3>
     * <p>
     * <code><pre>
     * SELECT a1.* FROM actor a1 WHERE id = ?;
     * SELECT * FROM actor a2 WHERE a2.id = ?;
     * </pre></code>
     * <p>
     * Examples of identical statements (which are not considered duplicate, but
     * {@link #repeatedStatements(DiagnosticsContext)}, if on the same
     * {@link Connection}) are:
     * <p>
     * <code><pre>
     * SELECT * FROM actor WHERE id = ?;
     * SELECT * FROM actor WHERE id = ?;
     * </pre></code>
     * <p>
     * This is a system-wide diagnostic that is not specific to individual
     * {@link Connection} instances.
     */
    void duplicateStatements(DiagnosticsContext ctx);

    /**
     * The executed JDBC statement is repeated consecutively on the same JDBC
     * {@link Connection}.
     * <p>
     * This problem goes by many names, the most famous one being the <strong>N
     * + 1</strong> problem, when a single (1) query for a parent entity
     * requires many (N) subsequent queries for child entities. This could have
     * been prevented by rewriting the parent query to use a JOIN. If such a
     * rewrite is not possible (or not easy), the subsequent N queries could at
     * least profit (depending on the exact query):
     * <ul>
     * <li>From reusing the {@link PreparedStatement}</li>
     * <li>From being batched</li>
     * <li>From being re-written as a bulk fetch or write query</li>
     * </ul>
     * <p>
     * This problem can be aggravated if combined with the
     * {@link #duplicateStatements(DiagnosticsContext)} problem, in case of
     * which the repeated statements might not be diagnosed as easily.
     * <p>
     * Repeated statements may or may not be "identical". In the following
     * example, there are two repeated <em>and</em> identical statements:
     * <code><pre>
     * SELECT * FROM actor WHERE id = ?;
     * SELECT * FROM actor WHERE id = ?;
     * </pre></code>
     * <p>
     * In this example, we have three repeated statements, only some of which
     * are also identical: <code><pre>
     * SELECT * FROM actor WHERE id = ?;
     * SELECT * FROM actor WHERE id = ?;
     * SELECT * FROM actor WHERE id =  ?;
     * </pre></code>
     * <p>
     * This is a {@link Connection}-specific diagnostic that is reset every time
     * {@link Connection#close()} is called.
     */
    void repeatedStatements(DiagnosticsContext ctx);

}
