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

import org.jooq.CaseConditionStep;
import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Decode<T, Z> extends AbstractFunction<Z> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    private final Field<T>    field;
    private final Field<T>    search;
    private final Field<Z>    result;
    private final Field<?>[]  more;

    public Decode(Field<T> field, Field<T> search, Field<Z> result, Field<?>[] more) {
        super("decode", result.getDataType(), Utils.combine(field, search, result, more));

        this.field = field;
        this.search = search;
        this.result = result;
        this.more = more;
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<Z> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {

            // Oracle actually has this function
            case ORACLE: {
                return function("decode", getDataType(), getArguments());
            }

            // Other dialects simulate it with a CASE ... WHEN expression
            default: {
                CaseConditionStep<Z> when = DSL
                    .decode()
                    .when(field.isNotDistinctFrom(search), result);

                for (int i = 0; i < more.length; i += 2) {

                    // search/result pair
                    if (i + 1 < more.length) {
                        when = when.when(field.isNotDistinctFrom((Field<T>) more[i]), (Field<Z>) more[i + 1]);
                    }

                    // trailing default value
                    else {
                        return when.otherwise((Field<Z>) more[i]);
                    }
                }

                return when;
            }
        }
    }
}
