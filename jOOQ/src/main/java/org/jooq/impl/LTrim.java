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

import static org.jooq.impl.SQLDataType.VARCHAR;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class LTrim extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -7273879239726265322L;

    private final Field<String> argument;
    private final Field<String> characters;

    LTrim(Field<String> argument) {
        super("ltrim", SQLDataType.VARCHAR, argument);

        this.argument = argument;
        this.characters = null;
    }

    LTrim(Field<String> argument, Field<String> characters) {
        super("ltrim", SQLDataType.VARCHAR, argument, characters);

        this.argument = argument;
        this.characters = characters;
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        if (characters == null) {
            switch (configuration.family()) {
                case FIREBIRD:
                    return DSL.field("{trim}({leading} {from} {0})", VARCHAR, argument);

                default:
                    return DSL.function("ltrim", VARCHAR, argument);
            }
        }
        else {
            switch (configuration.family()) {





                case SQLITE:
                    return DSL.function("ltrim", VARCHAR, argument, characters);

                default:
                    return DSL.field("{trim}({leading} {0} {from} {1})", VARCHAR, characters, argument);
            }
        }
    }
}
