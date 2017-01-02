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

import org.jooq.CaseConditionStep;
import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
final class Decode<T, Z> extends AbstractFunction<Z> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7273879239726265322L;

    private final Field<T>    field;
    private final Field<T>    search;
    private final Field<Z>    result;
    private final Field<?>[]  more;

    public Decode(Field<T> field, Field<T> search, Field<Z> result, Field<?>[] more) {
        super("decode", result.getDataType(), Tools.combine(field, search, result, more));

        this.field = field;
        this.search = search;
        this.result = result;
        this.more = more;
    }

    @SuppressWarnings("unchecked")
    @Override
    final Field<Z> getFunction0(Configuration configuration) {
        switch (configuration.dialect().family()) {












            // Other dialects emulate it with a CASE ... WHEN expression
            default: {
                CaseConditionStep<Z> when = DSL
                    .decode()
                    .when(field.isNotDistinctFrom(search), result);

                for (int i = 0; i < more.length; i += 2) {

                    // search/result pair
                    if (i + 1 < more.length) {
                        when = when.when(field.isNotDistinctFrom((Field<T>) more[i]), (Field<Z>) more[i + 1]);
                    }

                    // trailing default value
                    else {
                        return when.otherwise((Field<Z>) more[i]);
                    }
                }

                return when;
            }
        }
    }
}
