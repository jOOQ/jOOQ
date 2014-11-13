/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq;


/**
 * A parameter to a stored procedure or function.
 *
 * @param <T> The parameter type
 * @author Lukas Eder
 */
public interface Parameter<T> extends QueryPart {

    /**
     * The name of this parameter
     */
    String getName();

    /**
     * The Java type of the parameter.
     */
    Class<T> getType();

    /**
     * The parameter's underlying {@link Converter}.
     * <p>
     * By default, all parameters reference an identity-converter
     * <code>Converter&lt;T, T></code>. Custom data types may be obtained by a
     * custom {@link Converter} placed on the generated {@link Parameter}.
     */
    Converter<?, T> getConverter();

    /**
     * The parameter's underlying {@link Binding}.
     */
    Binding<?, T> getBinding();

    /**
     * The type of this parameter (might not be dialect-specific)
     */
    DataType<T> getDataType();

    /**
     * The dialect-specific type of this parameter
     */
    DataType<T> getDataType(Configuration configuration);

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
     *   MY_PROCEDURE(P_DEFAULTED => 1,
     *                P_MANDATORY => 2);
     *
     *   -- Omitting defaulted parameters
     *   MY_PROCEDURE(P_MANDATORY => 2);
     * END;
     * </pre></code>
     * <p>
     * If a procedure has defaulted parameters, jOOQ binds them by name, rather
     * than by index.
     * <p>
     * Currently, this is only supported for Oracle 11g
     */
    boolean isDefaulted();
}
