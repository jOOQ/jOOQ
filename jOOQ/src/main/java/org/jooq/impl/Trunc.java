/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
