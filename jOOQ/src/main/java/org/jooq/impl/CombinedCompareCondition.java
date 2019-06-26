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

import java.util.ArrayList;
import java.util.Collection;

import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.LikeEscapeStep;
import org.jooq.Operator;

final class CombinedCompareCondition extends AbstractCondition implements LikeEscapeStep {

    private static final long                         serialVersionUID = 1850816878293293314L;

    private final Field<?>                            field;
    private final Comparator                          comparator;
    private final Quantifier                          quantifier;
    private final Collection<? extends Field<String>> values;
    private Character                                 escape;

    public CombinedCompareCondition(Field<?> field, Comparator comparator, Quantifier quantifier, Collection<? extends Field<String>> values) {
        this.field = field;
        this.comparator = comparator;
        this.quantifier = quantifier;
        this.values = values;
    }

    @Override
    public final Condition escape(char c) {
        this.escape = c;
        return this;
    }

    @Override
    public final void accept(Context<?> ctx) {
        Collection<Condition> conditions = new ArrayList<Condition>();

        switch (comparator) {
            case LIKE:
                for (Field<String> value : values)
                    conditions.add(escape != null ? field.like(value, escape) : field.like(value));
                break;

            case NOT_LIKE:
                for (Field<String> value : values)
                    conditions.add(escape != null ? field.notLike(value, escape) : field.notLike(value));
                break;

            case SIMILAR_TO:
                for (Field<String> value : values)
                    conditions.add(escape != null ? field.similarTo(value, escape) : field.similarTo(value));
                break;

            case NOT_SIMILAR_TO:
                for (Field<String> value : values)
                    conditions.add(escape != null ? field.notSimilarTo(value, escape) : field.notSimilarTo(value));
                break;

            case LIKE_IGNORE_CASE:
                for (Field<String> value : values)
                    conditions.add(escape != null ? field.likeIgnoreCase(value, escape) : field.likeIgnoreCase(value));
                break;

            case NOT_LIKE_IGNORE_CASE:
                for (Field<String> value : values)
                    conditions.add(escape != null ? field.notLikeIgnoreCase(value, escape) : field.notLikeIgnoreCase(value));
                break;

            default:
                break;
        }

        Condition combinedCondition = CombinedCondition.of(quantifier == Quantifier.ALL ? Operator.AND : Operator.OR, conditions);
        ctx.visit(combinedCondition);
    }

}
