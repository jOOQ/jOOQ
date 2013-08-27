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
import org.jooq.DataType;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Greatest<T> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    Greatest(DataType<T> type, Field<?>... arguments) {
        super("greatest", type, arguments);
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<T> getFunction0(Configuration configuration) {

        // In any dialect, a single argument is always the greatest
        if (getArguments().length == 1) {
            return (Field<T>) getArguments()[0];
        }

        switch (configuration.dialect().family()) {
            // This implementation has O(2^n) complexity. Better implementations
            // are very welcome
            // [#1049] TODO Fix this!

            case ASE:
            case DERBY:
            case SQLSERVER:
            case SYBASE: {
                Field<T> first = (Field<T>) getArguments()[0];
                Field<T> other = (Field<T>) getArguments()[1];

                if (getArguments().length > 2) {
                    Field<?>[] remaining = new Field[getArguments().length - 2];
                    System.arraycopy(getArguments(), 2, remaining, 0, remaining.length);

                    return DSL.decode()
                        .when(first.greaterThan(other), DSL.greatest(first, remaining))
                        .otherwise(DSL.greatest(other, remaining));
                }
                else {
                    return DSL.decode()
                        .when(first.greaterThan(other), first)
                        .otherwise(other);
                }
            }

            case FIREBIRD:
                return function("maxvalue", getDataType(), getArguments());

            case SQLITE:
                return function("max", getDataType(), getArguments());

            default:
                return function("greatest", getDataType(), getArguments());
        }
    }
}
