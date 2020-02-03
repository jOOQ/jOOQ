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


/**
 * A parameter to a stored procedure or function.
 * <p>
 * Instances of this type cannot be created directly. They are available from
 * generated code.
 *
 * @param <T> The parameter type
 * @author Lukas Eder
 */
public interface Parameter<T> extends Named, Typed<T> {

    /**
     * Whether this parameter has a default value
     * <p>
     * Procedures and functions with defaulted parameters behave slightly
     * different from ones without defaulted parameters. In PL/SQL and other
     * procedural languages, it is possible to pass parameters by name,
     * reordering names and omitting defaulted parameters: <code><pre>
     * CREATE PROCEDURE MY_PROCEDURE (P_DEFAULTED IN NUMBER := 0
     *                                P_MANDATORY IN NUMBER);
     *
     * -- The above procedure can be called as such:
     * BEGIN
     *   -- Assign parameters by index
     *   MY_PROCEDURE(1, 2);
     *
     *   -- Assign parameters by name
     *   MY_PROCEDURE(P_DEFAULTED =&gt; 1,
     *                P_MANDATORY =&gt; 2);
     *
     *   -- Omitting defaulted parameters
     *   MY_PROCEDURE(P_MANDATORY =&gt; 2);
     * END;
     * </pre></code>
     * <p>
     * If a procedure has defaulted parameters, jOOQ binds them by name, rather
     * than by index.
     * <p>
     * Currently, this is only supported for Oracle 11g
     */
    boolean isDefaulted();

    /**
     * Whether this parameter has a name or not.
     * <p>
     * Some databases (e.g. {@link SQLDialect#POSTGRES}) allow for using unnamed
     * parameters. In this case, {@link #getName()} will return a synthetic name
     * created from the parameter index.
     */
    boolean isUnnamed();
}
