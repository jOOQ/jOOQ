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
class Rpad extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<String>           field;
    private final Field<? extends Number> length;
    private final Field<String>           character;

    Rpad(Field<String> field, Field<? extends Number> length) {
        this(field, length, null);
    }

    Rpad(Field<String> field, Field<? extends Number> length, Field<String> character) {
        super("rpad", SQLDataType.VARCHAR, field, length, character);

        this.field = field;
        this.length = length;
        this.character = (character == null ? inline(" ") : character);
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {
            case ASE:
            case SQLSERVER:
            case SYBASE: {
                return DSL.concat(field, DSL.repeat(character, length.sub(DSL.length(field))));
            }

            // This beautiful expression was contributed by "Ludo", here:
            // http://stackoverflow.com/questions/6576343/how-to-simulate-lpad-rpad-with-sqlite
            case SQLITE: {
                return DSL.field(
                    "{0} || substr(" +
                             "replace(" +
                               "replace(" +
                                 "substr(" +
                                   "quote(" +
                                     "zeroblob((({1} - length({0}) - 1 + length({2})) / length({2}) + 1) / 2)" +
                                   "), 3" +
                                 "), '''', ''" +
                               "), '0', {2}" +
                             "), 1, ({1} - length({0}))" +
                           ")",
                    String.class,
                    field, length, character);
            }

            // According to the Firebird documentation, LPAD outcomes should be
            // cast to truncate large results...
            case FIREBIRD: {
                return field("cast(rpad({0}, {1}, {2}) as varchar(4000))", SQLDataType.VARCHAR, field, length, character);
            }

            default: {
                return function("rpad", SQLDataType.VARCHAR, field, length, character);
            }
        }
    }
}
