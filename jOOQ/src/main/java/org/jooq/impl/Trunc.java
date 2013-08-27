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
            case ASE:
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

            // SQL Server's round function can be used to truncate.
            case SQLSERVER:
                return field("{round}({0}, {1}, {2})", field.getDataType(), field, decimals, one());

            case SYBASE:
                return field("{truncnum}({0}, {1})", field.getDataType(), field, decimals);

            // Postgres TRUNC() only takes NUMERIC arguments, no
            // DOUBLE PRECISION ones
            case POSTGRES:
                return field("{trunc}({0}, {1})", SQLDataType.NUMERIC, field.cast(BigDecimal.class), decimals).cast(field.getDataType());

            case CUBRID:
            case DB2:
            case HSQLDB:

            // Ingres ships with the TRUNC function, but it seems that it is not
            // implemented correctly in the database engine...
            case INGRES:
            case ORACLE:
            default:
                return field("{trunc}({0}, {1})", field.getDataType(), field, decimals);
        }
    }
}
