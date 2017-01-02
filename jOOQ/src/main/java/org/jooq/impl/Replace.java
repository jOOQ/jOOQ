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
import static org.jooq.impl.SQLDataType.VARCHAR;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Replace extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    Replace(Field<?>... arguments) {
        super("replace", SQLDataType.VARCHAR, arguments);
    }

    @Override
    final Field<String> getFunction0(Configuration configuration) {
        Field<?>[] args = getArguments();

        // [#861] Most dialects don't ship with a two-argument replace function:
        switch (configuration.family()) {



















            case FIREBIRD:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE: {
                if (args.length == 2) {
                    return function("replace", VARCHAR, args[0], args[1], val(""));
                }
                else {
                    return function("replace", VARCHAR, args);
                }
            }

            default: {
                return function("replace", VARCHAR, args);
            }
        }
    }
}
