/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
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
final class Round<T extends Number> extends AbstractFunction<T> {

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
        switch (configuration.family()) {

            // evaluate "round" if unavailable
            case DERBY: {
                if (decimals == 0) {
                    return DSL
                        .when(argument.sub(DSL.floor(argument))
                        .lessThan((T) Double.valueOf(0.5)), DSL.floor(argument))
                        .otherwise(DSL.ceil(argument));
                }
                else {
                    Field<BigDecimal> factor = DSL.val(BigDecimal.ONE.movePointRight(decimals));
                    Field<T> mul = argument.mul(factor);

                    return DSL
                        .when(mul.sub(DSL.floor(mul))
                        .lessThan((T) Double.valueOf(0.5)), DSL.floor(mul).div(factor))
                        .otherwise(DSL.ceil(mul).div(factor));
                }
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
