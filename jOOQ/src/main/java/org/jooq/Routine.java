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

import java.util.List;

import org.jooq.exception.DataAccessException;

/**
 * A routine is a callable object in your RDBMS.
 * <p>
 * Callable objects are mainly stored procedures and stored functions. The
 * distinction between those two object types is very subtle and not well
 * defined across various RDBMS. In general, this can be said:
 * <p>
 * Procedures:
 * <p>
 * <ul>
 * <li>Are called as callable statements</li>
 * <li>Have no return value</li>
 * <li>Support OUT parameters</li>
 * </ul>
 * Functions
 * <p>
 * <ul>
 * <li>Can be used in SQL statements</li>
 * <li>Have a return value</li>
 * <li>Don't support OUT parameters</li>
 * </ul>
 * But there are exceptions to these rules:
 * <p>
 * <ul>
 * <li>DB2, H2, and HSQLDB don't allow for JDBC escape syntax when calling
 * functions. Functions must be used in a SELECT statement</li>
 * <li>H2 only knows functions (without OUT parameters)</li>
 * <li>Oracle functions may have OUT parameters</li>
 * <li>Oracle knows functions that mustn't be used in SQL statements</li>
 * <li>Oracle parameters can have default values (to support this, jOOQ renders
 * PL/SQL instead of the JDBC escape syntax)</li>
 * <li>Postgres only knows functions (with all features combined)</li>
 * <li>The Sybase JDBC driver doesn't handle null values correctly when using
 * the JDBC escape syntax on functions</li>
 * <li>etc...</li>
 * </ul>
 * <p>
 * Hence, with #852, jOOQ 1.6.8, the distinction between procedures and
 * functions becomes obsolete. All stored routines are simply referred to as
 * "Routine".
 *
 * @author Lukas Eder
 */
public interface Routine<T> extends QueryPart {

    // -------------------------------------------------------------------------
    // XXX: Meta information
    // -------------------------------------------------------------------------

    /**
     * Get the routine catalog.
     */
    Catalog getCatalog();

    /**
     * Get the routine schema
     */
    Schema getSchema();

    /**
     * The name of this routine
     */
    String getName();

    /**
     * The container package of this stored procedure or function.
     * <p>
     * This is only supported in the {@link SQLDialect#ORACLE} dialect.
     *
     * @return The container package of this object, or <code>null</code> if
     *         there is no such container.
     */
    Package getPackage();

    /**
     * The parameter representing this routine's {@link #getReturnValue()}
     *
     * @return The return parameter or <code>null</code> if this routine doesn't
     *         have a return value.
     * @see #getParameters()
     */
    Parameter<T> getReturnParameter();

    /**
     * A list of OUT parameters passed to the stored procedure as argument. This
     * list contains all parameters that are either OUT or INOUT in their
     * respective order of appearance in {@link #getParameters()}.
     *
     * @return The list of out parameters
     * @see #getParameters()
     */
    List<Parameter<?>> getOutParameters();

    /**
     * A list of IN parameters passed to the stored procedure as argument. This
     * list contains all parameters that are either IN or INOUT in their
     * respective order of appearance in {@link #getParameters()}.
     *
     * @return The list of in parameters
     * @see #getParameters()
     */
    List<Parameter<?>> getInParameters();

    /**
     * @return A list of parameters passed to the stored object as argument
     */
    List<Parameter<?>> getParameters();

    // -------------------------------------------------------------------------
    // XXX: Call API
    // -------------------------------------------------------------------------

    /**
     * Execute the stored object using a {@link Configuration} object
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    int execute(Configuration configuration) throws DataAccessException;

    /**
     * Execute the stored object on an underlying connection
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    int execute() throws DataAccessException;

    // -------------------------------------------------------------------------
    // XXX: Call data
    // -------------------------------------------------------------------------

    /**
     * Set the routine's IN value for an IN parameter.
     */
    <Z> void setValue(Parameter<Z> parameter, Z value);

    /**
     * Set the routine's IN value for an IN parameter.
     */
    <Z> void set(Parameter<Z> parameter, Z value);

    /**
     * @return The routine's OUT value for an OUT parameter.
     */
    <Z> Z getValue(Parameter<Z> parameter);

    /**
     * @return The routine's OUT value for an OUT parameter.
     */
    <Z> Z get(Parameter<Z> parameter);

    /**
     * @return The routine's return value (if it is a function)
     */
    T getReturnValue();

    /**
     * @return The routine's results (if available)
     */
    Results getResults();
}
