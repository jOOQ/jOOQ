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
final class Substring extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = -7273879239726265322L;

    Substring(Field<?>... arguments) {
        super("substring", SQLDataType.VARCHAR, arguments);
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        String functionName = "substring";

        switch (configuration.family()) {

            // [#430] Firebird has its own syntax
            case FIREBIRD: {
                if (getArguments().length == 2) {
                    return DSL.field("{substring}({0} {from} {1})", SQLDataType.VARCHAR, getArguments());
                }
                else {
                    return DSL.field("{substring}({0} {from} {1} {for} {2})", SQLDataType.VARCHAR, getArguments());
                }
            }








































            case DERBY:
            case SQLITE:
                functionName = "substr";
                break;
        }

        return function(functionName, SQLDataType.VARCHAR, getArguments());
    }
}
