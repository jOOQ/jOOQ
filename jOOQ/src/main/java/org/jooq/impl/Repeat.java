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

import static org.jooq.impl.DSL.function;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Repeat extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<String>           string;
    private final Field<? extends Number> count;

    Repeat(Field<String> string, Field<? extends Number> count) {
        super("rpad", SQLDataType.VARCHAR, string, count);

        this.string = string;
        this.count = count;
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {
            case FIREBIRD:
            case INGRES:
            case ORACLE:
                return DSL.rpad(string, DSL.length(string).mul(count), string);

            // Simulation of REPEAT() for SQLite currently cannot be achieved
            // using RPAD() above, as RPAD() expects characters, not strings
            // Another option is documented here, though:
            // http://stackoverflow.com/questions/11568496/how-to-simulate-repeat-in-sqlite
            case SQLITE:
                return DSL.field("replace(substr(quote(zeroblob(({0} + 1) / 2)), 3, {0}), '0', {1})", String.class, count, string);

            case ASE:
            case SQLSERVER:
                return function("replicate", SQLDataType.VARCHAR, string, count);

            default:
                return function("repeat", SQLDataType.VARCHAR, string, count);
        }
    }
}
