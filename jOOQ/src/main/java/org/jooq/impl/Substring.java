/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Substring extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    Substring(Field<?>... arguments) {
        super("substring", SQLDataType.VARCHAR, arguments);
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        String functionName = "substring";

        switch (configuration.dialect().family()) {

            // Sybase ASE and SQL Server requires 3 arguments
            case ASE:
            case SQLSERVER: {
                if (getArguments().length == 2) {
                    return function(functionName,
                        SQLDataType.VARCHAR, getArguments()[0],
                        getArguments()[1],
                        inline(Integer.MAX_VALUE));
                }

                // Default behaviour
                else {
                    break;
                }
            }

            // [#430] Firebird has its own syntax
            case FIREBIRD: {
                if (getArguments().length == 2) {
                    return field("{substring}({0} {from} {1})", SQLDataType.VARCHAR, getArguments());
                }
                else {
                    return field("{substring}({0} {from} {1} {for} {2})", SQLDataType.VARCHAR, getArguments());
                }
            }

            // [#722] For undocumented reasons, Ingres needs explicit casting
            case INGRES: {
                if (getArguments().length == 2) {
                    return field("{substring}({0}, {cast}({1} {as integer}))",
                        SQLDataType.VARCHAR,
                        getArguments());
                }
                else {
                    return field("{substring}({0}, {cast}({1} {as integer}), {cast}({2} {as integer}))",
                        SQLDataType.VARCHAR,
                        getArguments());
                }
            }

            case DB2:
            case DERBY:
            case ORACLE:
            case SQLITE:
                functionName = "substr";
                break;
        }

        return function(functionName, SQLDataType.VARCHAR, getArguments());
    }
}
