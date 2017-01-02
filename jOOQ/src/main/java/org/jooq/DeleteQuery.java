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
 */

package org.jooq;

// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.POSTGRES;

import java.util.Collection;

/**
 * A query used for deletion of data
 *
 * @param <R> The record type of the table being deleted from
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
public interface DeleteQuery<R extends Record> extends ConditionProvider, Delete<R> {

    // ------------------------------------------------------------------------
    // Methods from ConditionProvider, OrderProvider, LockProvider
    // ------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Condition... conditions);

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Collection<? extends Condition> conditions);

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Operator operator, Condition... conditions);

    /**
     * {@inheritDoc}
     */
    @Override
    @Support
    void addConditions(Operator operator, Collection<? extends Condition> conditions);

    // ------------------------------------------------------------------------
    // XXX: Methods for the DELETE .. RETURNING syntax
    // ------------------------------------------------------------------------

    /**
     * Configure the <code>DELETE</code> statement to return all fields in
     * <code>R</code>.
     *
     * @see #getReturnedRecords()
     */
    @Support({ FIREBIRD, POSTGRES })
    void setReturning();

    /**
     * Configure the <code>DELETE</code> statement to return a list of fields in
     * <code>R</code>.
     *
     * @param fields Fields to be returned
     * @see #getReturnedRecords()
     */
    @Support({ FIREBIRD, POSTGRES })
    void setReturning(Field<?>... fields);

    /**
     * Configure the <code>DELETE</code> statement to return a list of fields in
     * <code>R</code>.
     *
     * @param fields Fields to be returned
     * @see #getReturnedRecords()
     */
    @Support({ FIREBIRD, POSTGRES })
    void setReturning(Collection<? extends Field<?>> fields);

    /**
     * The record holding returned values as specified by any of the
     * {@link #setReturning()} methods.
     * <p>
     * If the <code>DELETE</code> statement returns several records, this is the
     * same as calling <code>getReturnedRecords().get(0)</code>
     * <p>
     * This implemented differently for every dialect:
     * <ul>
     * <li>Firebird and Postgres have native support for
     * <code>DELETE .. RETURNING</code> clauses</li>
     * </ul>
     *
     * @return The returned value as specified by any of the
     *         {@link #setReturning()} methods. This may return
     *         <code>null</code> in case jOOQ could not retrieve any generated
     *         keys from the JDBC driver.
     * @see #getReturnedRecords()
     */
    @Support({ FIREBIRD, POSTGRES })
    R getReturnedRecord();

    /**
     * The records holding returned values as specified by any of the
     * {@link #setReturning()} methods.
     * <p>
     * If the <code>DELETE</code> statement returns several records, this is the
     * same as calling <code>getReturnedRecords().get(0)</code>
     * <p>
     * This implemented differently for every dialect:
     * <ul>
     * <li>Firebird and Postgres have native support for
     * <code>DELETE .. RETURNING</code> clauses</li>
     * </ul>
     *
     * @return The returned values as specified by any of the
     *         {@link #setReturning()} methods. Note:
     *         <ul>
     *         <li>Not all databases / JDBC drivers support returning several
     *         values on multi-row inserts!</li><li>This may return an empty
     *         <code>Result</code> in case jOOQ could not retrieve any generated
     *         keys from the JDBC driver.</li>
     *         </ul>
     */
    @Support({ FIREBIRD, POSTGRES })
    Result<R> getReturnedRecords();

}
