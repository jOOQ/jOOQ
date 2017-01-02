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
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Repeat extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    private final Field<String>           string;
    private final Field<? extends Number> count;

    Repeat(Field<String> string, Field<? extends Number> count) {
        super("rpad", SQLDataType.VARCHAR, string, count);

        this.string = string;
        this.count = count;
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        switch (configuration.family()) {






            case FIREBIRD:
                return DSL.rpad(string, DSL.length(string).mul(count), string);

            // Emulation of REPEAT() for SQLite currently cannot be achieved
            // using RPAD() above, as RPAD() expects characters, not strings
            // Another option is documented here, though:
            // http://stackoverflow.com/questions/11568496/how-to-simulate-repeat-in-sqlite
            case SQLITE:
                return DSL.field("replace(substr(quote(zeroblob(({0} + 1) / 2)), 3, {0}), '0', {1})", String.class, count, string);







            default:
                return function("repeat", SQLDataType.VARCHAR, string, count);
        }
    }
}
