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

            // These dialects have a mandatory decimals argument
            case ASE:
            case INGRES:
            case SQLSERVER:
            case SYBASE: {
                return function("round", getDataType(), argument, val(decimals));
            }

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
