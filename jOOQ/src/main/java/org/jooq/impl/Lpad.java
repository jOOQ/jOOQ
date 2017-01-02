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

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Lpad extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<String>           field;
    private final Field<? extends Number> length;
    private final Field<String>           character;

    Lpad(Field<String> field, Field<? extends Number> length) {
        this(field, length, null);
    }

    Lpad(Field<String> field, Field<? extends Number> length, Field<String> character) {
        super("lpad", SQLDataType.VARCHAR, field, length, character);

        this.field = field;
        this.length = length;
        this.character = (character == null ? inline(" ") : character);
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        switch (configuration.family()) {












            // This beautiful expression was contributed by "Ludo", here:
            // http://stackoverflow.com/questions/6576343/how-to-simulate-lpad-rpad-with-sqlite
            case SQLITE: {
                return DSL.field(
                    "substr(" +
                      "replace(" +
                        "replace(" +
                          "substr(" +
                            "quote(" +
                              "zeroblob((({1} - length({0}) - 1 + length({2})) / length({2}) + 1) / 2)" +
                            "), 3" +
                          "), '\''', ''" +
                        "), '0', {2}" +
                      "), 1, ({1} - length({0}))" +
                    ") || {0}",
                    String.class,
                    field, length, character);
            }

            // According to the Firebird documentation, LPAD outcomes should be
            // cast to truncate large results...
            case FIREBIRD: {
                return DSL.field("cast(lpad({0}, {1}, {2}) as varchar(4000))", SQLDataType.VARCHAR, field, length, character);
            }

            default: {
                return function("lpad", SQLDataType.VARCHAR, field, length, character);
            }
        }
    }
}
