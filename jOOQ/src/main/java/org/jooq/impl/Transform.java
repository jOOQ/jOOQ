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

import java.util.function.Function;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.impl.QOM.MCombinedCondition;
import org.jooq.impl.QOM.MCompareCondition;

/**
 * A simple, preliminary pattern matching implementation for {@link Condition}
 * matching.
 * <p>
 * [#8800] Has been implemented to support transforming ANSI join to pre-ANSI
 * join syntax. For outer join support, the {@link Condition} model needs to be
 * transformed to yield {@link Field#plus()} expressions where applicable.
 * <p>
 * A future jOOQ version will refactor this implementation in favour of much
 * more generic (and efficient) pattern matching of the {@link QueryPart}
 * expression tree.
 *
 * @author Lukas Eder
 */
final class Transform {

    final Function<Field<?>, Field<?>> fieldTransformer;

    Transform(Function<Field<?>, Field<?>> fieldTransformer) {
        this.fieldTransformer = fieldTransformer;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    Condition transform(Condition condition) {
        if (condition instanceof ConditionProviderImpl)
            return transform(((ConditionProviderImpl) condition).getWhere());
        else if (condition instanceof MCombinedCondition)
            return transform((Condition) ((MCombinedCondition) condition).$arg1()).and(transform((Condition) ((MCombinedCondition) condition).$arg2()));
        else if (condition instanceof MCompareCondition)
            return fieldTransformer.apply((Field) ((MCompareCondition<?>) condition).$arg1()).eq((Field) fieldTransformer.apply((Field) ((MCompareCondition<?>) condition).$arg2()));
        else
            return condition;
    }
}
