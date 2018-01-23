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
     * Examples of such similar statements include:
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
     * This event is triggered every time a new duplicate is encountered.
     */
    void duplicateStatements(DiagnosticsContext ctx);

}
