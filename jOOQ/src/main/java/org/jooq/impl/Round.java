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
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.SQLDataType.NUMERIC;
import static org.jooq.impl.Tools.castIfNeeded;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Param;

/**
 * @author Lukas Eder
 */
final class Round<T extends Number> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = -7273879239726265322L;

    private final Field<T>       argument;
    private final Field<Integer> decimals;

    Round(Field<T> argument) {
        this(argument, null);
    }

    Round(Field<T> argument, Field<Integer> decimals) {
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
                if (decimals == null) {
                    return DSL
                        .when(argument.sub(DSL.floor(argument))
                        .lessThan((T) Double.valueOf(0.5)), DSL.floor(argument))
                        .otherwise(DSL.ceil(argument));
                }
                else if (decimals instanceof Param) {
                    Integer decimalsValue = ((Param<Integer>) decimals).getValue();
                    Field<BigDecimal> factor = DSL.val(BigDecimal.ONE.movePointRight(decimalsValue));
                    Field<T> mul = argument.mul(factor);

                    return DSL
                        .when(mul.sub(DSL.floor(mul))
                        .lessThan((T) Double.valueOf(0.5)), DSL.floor(mul).div(factor))
                        .otherwise(DSL.ceil(mul).div(factor));
                }
                // fall-through
            }
























            // There's no function round(double precision, integer) in Postgres
            case POSTGRES: {
                if (decimals == null)
                    return function("round", getDataType(), argument);
                else
                    return function("round", getDataType(), castIfNeeded(argument, BigDecimal.class), decimals);
            }

            // This is the optimal implementation by most RDBMS
            default: {
                if (decimals == null)
                    return function("round", getDataType(), argument);
                else
                    return function("round", getDataType(), argument, decimals);
            }
        }
    }
}
