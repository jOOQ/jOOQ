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

import static java.math.BigDecimal.TEN;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.Utils.extractVal;

import java.math.BigDecimal;
import java.math.MathContext;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class Trunc<T> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 4291348230758816484L;

    private final Field<T>       field;
    private final Field<Integer> decimals;

    Trunc(Field<T> field, Field<Integer> decimals) {
        super("trunc", field.getDataType());

        this.field = field;
        this.decimals = decimals;
    }

    @Override
    final Field<T> getFunction0(Configuration configuration) {
        if (decimals != null) {
            return getNumericFunction(configuration);
        }
        else {
            return getDateTimeFunction(configuration);
        }
    }

    @SuppressWarnings("unused")
    private final Field<T> getDateTimeFunction(Configuration configuration) {
        return null;
    }

    private final Field<T> getNumericFunction(Configuration configuration) {
        switch (configuration.dialect().family()) {
            /* [com] */
            case ASE:
            /* [/com] */
            case DERBY: {
                Field<BigDecimal> power;

                // [#1334] if possible, calculate the power in Java to prevent
                // inaccurate arithmetics in the Derby database
                Integer decimalsVal = extractVal(decimals);
                if (decimalsVal != null) {
                    power = inline(TEN.pow(decimalsVal, MathContext.DECIMAL128));
                }
                else {
                    power = DSL.power(inline(TEN), decimals);
                }

                return DSL.decode()
                    .when(field.sign().greaterOrEqual(zero()),
                          field.mul(power).floor().div(power))
                    .otherwise(
                          field.mul(power).ceil().div(power));
            }

            case H2:
            case MARIADB:
            case MYSQL:
                return field("{truncate}({0}, {1})", field.getDataType(), field, decimals);

            // Postgres TRUNC() only takes NUMERIC arguments, no
            // DOUBLE PRECISION ones
            case POSTGRES:
                return field("{trunc}({0}, {1})", SQLDataType.NUMERIC, field.cast(BigDecimal.class), decimals).cast(field.getDataType());

            /* [com] */
            // SQL Server's round function can be used to truncate.
            case SQLSERVER:
                return field("{round}({0}, {1}, {2})", field.getDataType(), field, decimals, one());

            case SYBASE:
                return field("{truncnum}({0}, {1})", field.getDataType(), field, decimals);

            // Ingres ships with the TRUNC function, but it seems that it is not
            // implemented correctly in the database engine...
            case DB2:
            case INGRES:
            case ORACLE:
            /* [/com] */
            case CUBRID:
            case HSQLDB:
            default:
                return field("{trunc}({0}, {1})", field.getDataType(), field, decimals);
        }
    }
}
