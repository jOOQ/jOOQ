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


import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_BETWEEN;
import static org.jooq.Comparator.LIKE;
import static org.jooq.Comparator.LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_LIKE;
import static org.jooq.Comparator.NOT_LIKE_IGNORE_CASE;
import static org.jooq.Comparator.NOT_SIMILAR_TO;
import static org.jooq.Comparator.SIMILAR_TO;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.isEmbeddable;
import static org.jooq.tools.Convert.convert;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.LikeEscapeStep;
import org.jooq.Operator;
import org.jooq.Param;
import org.jooq.QuantifiedSelect;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class QuantifiedComparisonCondition extends AbstractCondition implements LikeEscapeStep {

    private static final long                serialVersionUID           = -402776705884329740L;
    private static final Clause[]            CLAUSES                    = { CONDITION, CONDITION_BETWEEN };
    private static final EnumSet<Comparator> SYNTHETIC_OPERATORS        = EnumSet.of(LIKE, NOT_LIKE, LIKE_IGNORE_CASE, NOT_LIKE_IGNORE_CASE, SIMILAR_TO, NOT_SIMILAR_TO);
    private static final Set<SQLDialect>     SUPPORTS_QUANTIFIED_ARRAYS = SQLDialect.supportedBy(POSTGRES);

    private final QuantifiedSelectImpl<?>    query;
    private final Field<?>                   field;
    private final Comparator                 comparator;
    private Character                        escape;

    QuantifiedComparisonCondition(QuantifiedSelect<?> query, Field<?> field, Comparator comparator) {
        this.query = (QuantifiedSelectImpl<?>) query;
        this.field = field;
        this.comparator = comparator;
    }

    @Override
    public Condition escape(char c) {
        this.escape = c;
        return this;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (isEmbeddable(field))
            ctx.visit(row(embeddedFields(field)).compare(comparator, query));
        else
            accept0(ctx);
    }

    @SuppressWarnings({ "unchecked" })
    private final void accept0(Context<?> ctx) {
        boolean syntheticOperator = SYNTHETIC_OPERATORS.contains(comparator);
        boolean quantifiedArray = query.array instanceof Param<?>;

        // [#9224] Special case when a SQL dialect actually supports quantified
        //         arrays, such as x = any(?::int[]) in PostgreSQL
        if (quantifiedArray && SUPPORTS_QUANTIFIED_ARRAYS.contains(ctx.family()) && !syntheticOperator) {
            accept1(ctx);
        }
        else if (query.values != null || quantifiedArray) {
            List<Condition> conditions = new ArrayList<>();

            if (query.values != null)
                for (Field<?> value : query.values)
                    conditions.add(comparisonCondition(comparator, (Field<String>) value));
            else
                for (Object value : ((Param<? extends Object[]>) query.array).getValue())
                    conditions.add(value instanceof Field ? comparisonCondition(comparator, (Field<String>) value) : comparisonCondition(comparator, value));

            ctx.visit(CombinedCondition.of(query.quantifier == Quantifier.ALL ? Operator.AND : Operator.OR, conditions));
        }
        else if ((query.array != null || query.query != null) && syntheticOperator) {
            Field<String> pattern = DSL.field(name("pattern"), VARCHAR);
            Condition cond;
            Field<Boolean> lhs;
            switch (comparator) {
                case NOT_LIKE:
                case NOT_SIMILAR_TO:
                case NOT_LIKE_IGNORE_CASE:
                    cond = comparisonCondition(inverse(comparator), pattern);
                    lhs = inline(false);
                    break;
                case LIKE:
                case SIMILAR_TO:
                case LIKE_IGNORE_CASE:
                    cond = comparisonCondition(comparator, pattern);
                    lhs = inline(true);
                    break;
                default:
                    throw new IllegalStateException();
            }

            Table<?> t = (query.array != null ? new ArrayTable(query.array) : query.query).asTable("t", "pattern");
            Select<Record1<Boolean>> select = select(DSL.field(cond)).from(t);
            ctx.visit(lhs.eq(query.quantifier.apply(select)));
        }
        else {
            accept1(ctx);
        }
    }

    private final void accept1(Context<?> ctx) {
        ctx.visit(field)
        .sql(' ')
        .visit(comparator.toKeyword())
        .sql(' ')
        .visit(query);
    }

    private Comparator inverse(Comparator operator) {
        switch (operator) {
            case IN:                   return Comparator.NOT_IN;
            case NOT_IN:               return Comparator.IN;
            case EQUALS:               return Comparator.NOT_EQUALS;
            case NOT_EQUALS:           return Comparator.EQUALS;
            case LESS:                 return Comparator.GREATER_OR_EQUAL;
            case LESS_OR_EQUAL:        return Comparator.GREATER;
            case GREATER:              return Comparator.LESS_OR_EQUAL;
            case GREATER_OR_EQUAL:     return Comparator.LESS;
            case IS_DISTINCT_FROM:     return Comparator.IS_NOT_DISTINCT_FROM;
            case IS_NOT_DISTINCT_FROM: return Comparator.IS_DISTINCT_FROM;
            case LIKE:                 return Comparator.NOT_LIKE;
            case NOT_LIKE:             return Comparator.LIKE;
            case SIMILAR_TO:           return Comparator.NOT_SIMILAR_TO;
            case NOT_SIMILAR_TO:       return Comparator.SIMILAR_TO;
            case LIKE_IGNORE_CASE:     return Comparator.NOT_LIKE_IGNORE_CASE;
            case NOT_LIKE_IGNORE_CASE: return Comparator.LIKE_IGNORE_CASE;
            default:                   throw new IllegalStateException();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Condition comparisonCondition(Comparator operator, Field<String> value) {
        switch (operator) {
            case LIKE:
                return escape != null ? field.like(value, escape) : field.like(value);

            case NOT_LIKE:
                return escape != null ? field.notLike(value, escape) : field.notLike(value);

            case SIMILAR_TO:
                return escape != null ? field.similarTo(value, escape) : field.similarTo(value);

            case NOT_SIMILAR_TO:
                return escape != null ? field.notSimilarTo(value, escape) : field.notSimilarTo(value);

            case LIKE_IGNORE_CASE:
                return escape != null ? field.likeIgnoreCase(value, escape) : field.likeIgnoreCase(value);

            case NOT_LIKE_IGNORE_CASE:
                return escape != null ? field.notLikeIgnoreCase(value, escape) : field.notLikeIgnoreCase(value);

            default:
                return ((Field) field).compare(operator, value);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Condition comparisonCondition(Comparator operator, Object value) {
        switch (operator) {
            case LIKE:
                return escape != null ? field.like(convert(value, String.class), escape) : field.like(convert(value, String.class));

            case NOT_LIKE:
                return escape != null ? field.notLike(convert(value, String.class), escape) : field.notLike(convert(value, String.class));

            case SIMILAR_TO:
                return escape != null ? field.similarTo(convert(value, String.class), escape) : field.similarTo(convert(value, String.class));

            case NOT_SIMILAR_TO:
                return escape != null ? field.notSimilarTo(convert(value, String.class), escape) : field.notSimilarTo(convert(value, String.class));

            case LIKE_IGNORE_CASE:
                return escape != null ? field.likeIgnoreCase(convert(value, String.class), escape) : field.likeIgnoreCase(convert(value, String.class));

            case NOT_LIKE_IGNORE_CASE:
                return escape != null ? field.notLikeIgnoreCase(convert(value, String.class), escape) : field.notLikeIgnoreCase(convert(value, String.class));

            default:
                return ((Field) field).compare(operator, value);
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
