/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */

package org.jooq;

import static org.jooq.SQLDialect.ORACLE;

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
    @Support({ ORACLE })
    boolean isDefaulted();
}
