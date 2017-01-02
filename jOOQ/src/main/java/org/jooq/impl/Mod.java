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
import static org.jooq.impl.ExpressionOperator.MODULO;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Mod<T> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<T>                arg1;
    private final Field<? extends Number> arg2;

    Mod(Field<T> arg1, Field<? extends Number> arg2) {
        super("mod", arg1.getDataType(), arg1, arg2);

        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    final Field<T> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {








            case SQLITE:
                return new Expression<T>(MODULO, arg1, arg2);
        }

        return function("mod", getDataType(), arg1, arg2);
    }
}
