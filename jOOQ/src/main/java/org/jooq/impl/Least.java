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

import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Least<T> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    Least(DataType<T> type, Field<?>... arguments) {
        super("least", type, arguments);
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<T> getFunction0(Configuration configuration) {

        // In any dialect, a single argument is always the least
        if (getArguments().length == 1) {
            return (Field<T>) getArguments()[0];
        }

        switch (configuration.family()) {
            // This implementation has O(2^n) complexity. Better implementations
            // are very welcome








            case DERBY: {
                Field<T> first = (Field<T>) getArguments()[0];
                Field<T> other = (Field<T>) getArguments()[1];

                if (getArguments().length > 2) {
                    Field<?>[] remaining = new Field<?>[getArguments().length - 2];
                    System.arraycopy(getArguments(), 2, remaining, 0, remaining.length);

                    return DSL
                        .when(first.lessThan(other), DSL.least(first, remaining))
                        .otherwise(DSL.least(other, remaining));
                }
                else {
                    return DSL
                        .when(first.lessThan(other), first)
                        .otherwise(other);
                }
            }

            case FIREBIRD:
                return function("minvalue", getDataType(), getArguments());

            case SQLITE:
                return function("min", getDataType(), getArguments());

            default:
                return function("least", getDataType(), getArguments());
        }
    }
}
