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

import static org.jooq.impl.DSL.castAll;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.ExpressionOperator.ADD;
import static org.jooq.impl.ExpressionOperator.BIT_AND;
import static org.jooq.impl.ExpressionOperator.CONCAT;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Concat extends AbstractFunction<String> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    Concat(Field<?>... arguments) {
        super("concat", SQLDataType.VARCHAR, arguments);
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<String> getFunction0(Configuration configuration) {

        // [#461] Type cast the concat expression, if this isn't a VARCHAR field
        Field<String>[] cast = castAll(String.class, getArguments());

        // If there is only one argument, return it immediately
        if (cast.length == 1) {
            return cast[0];
        }

        Field<String> first = cast[0];
        Field<String>[] others = new Field[cast.length - 1];
        System.arraycopy(cast, 1, others, 0, others.length);

        switch (configuration.family()) {
            case MARIADB:
            case MYSQL:
                return function("concat", SQLDataType.VARCHAR, cast);










            default:
                return new Expression<String>(CONCAT, first, others);
        }
    }
}
