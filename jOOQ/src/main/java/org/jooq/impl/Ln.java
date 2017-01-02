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
import static org.jooq.impl.DSL.inline;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Ln extends AbstractFunction<BigDecimal> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<? extends Number> argument;
    private final Integer                 base;

    Ln(Field<? extends Number> argument) {
        this(argument, null);
    }

    Ln(Field<? extends Number> argument, Integer base) {
        super("ln", SQLDataType.NUMERIC, argument);

        this.argument = argument;
        this.base = base;
    }

    @Override
    final Field<BigDecimal> getFunction0(Configuration configuration) {
        if (base == null) {
            switch (configuration.dialect().family()) {








                case H2:
                    return function("log", SQLDataType.NUMERIC, argument);

                default:
                    return function("ln", SQLDataType.NUMERIC, argument);
            }
        }
        else {
            switch (configuration.dialect().family()) {









                case DERBY:
                case H2:
                case HSQLDB:
                    return DSL.ln(argument).div(DSL.ln(inline(base)));

                default:
                    return function("log", SQLDataType.NUMERIC, inline(base), argument);
            }
        }
    }
}
