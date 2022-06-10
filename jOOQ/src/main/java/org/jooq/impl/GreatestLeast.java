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

import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.values;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
// ...
import org.jooq.Row1;

/**
 * @author Lukas Eder
 */
final class GreatestLeast<T> {

















    static final <T> void acceptCaseEmulation(
        Context<?> ctx,
        QueryPartListView<Field<T>> args,
        BiFunction<? super Field<T>, ? super Field<?>[], ? extends Field<T>> greatestLeast,
        BiFunction<? super Field<T>, ? super Field<T>, ? extends Condition> gtLt
    ) {

        // This implementation has O(2^n) complexity. Better implementations
        // are very welcome
        Field<T> first = args.get(0);
        Field<T> other = args.get(1);

        if (args.size() > 2) {
            Field<?>[] remaining = args.subList(2, args.size()).toArray(Tools.EMPTY_FIELD);

            ctx.visit(DSL
               .when(gtLt.apply(first, other), greatestLeast.apply(first, remaining))
               .otherwise(greatestLeast.apply(other, remaining)));
        }
        else
            ctx.visit(DSL
               .when(gtLt.apply(first, other), first)
               .otherwise(other));
    }
}
