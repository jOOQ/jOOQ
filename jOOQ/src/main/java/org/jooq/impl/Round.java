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
package org.jooq.impl;

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.val;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Round<T extends Number> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    private final Field<T>    argument;
    private final int         decimals;

    Round(Field<T> argument) {
        this(argument, 0);
    }

    Round(Field<T> argument, int decimals) {
        super("round", argument.getDataType(), argument);

        this.argument = argument;
        this.decimals = decimals;
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<T> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {

            // evaluate "round" if unavailable
            case DERBY: {
                if (decimals == 0) {
                    return DSL.decode()
                        .when(argument.sub(DSL.floor(argument))
                        .lessThan((T) Double.valueOf(0.5)), DSL.floor(argument))
                        .otherwise(DSL.ceil(argument));
                }
                else {
                    Field<BigDecimal> factor = DSL.val(BigDecimal.ONE.movePointRight(decimals));
                    Field<T> mul = argument.mul(factor);

                    return DSL.decode()
                        .when(mul.sub(DSL.floor(mul))
                        .lessThan((T) Double.valueOf(0.5)), DSL.floor(mul).div(factor))
                        .otherwise(DSL.ceil(mul).div(factor));
                }
            }

            /* [com] */
            // These dialects have a mandatory decimals argument
            case ASE:
            case INGRES:
            case SQLSERVER:
            case SYBASE: {
                return function("round", getDataType(), argument, val(decimals));
            }

            /* [/com] */
            // There's no function round(double precision, integer) in Postgres
            case POSTGRES: {
                if (decimals == 0) {
                    return function("round", getDataType(), argument);
                }
                else {
                    return function("round", getDataType(), argument.cast(BigDecimal.class), val(decimals));
                }
            }

            // This is the optimal implementation by most RDBMS
            default: {
                if (decimals == 0) {
                    return function("round", getDataType(), argument);
                }
                else {
                    return function("round", getDataType(), argument, val(decimals));
                }
            }
        }
    }
}
