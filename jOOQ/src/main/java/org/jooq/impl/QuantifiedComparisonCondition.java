/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.NOT_EQUALS;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.characterLiteral;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.quantify;
import static org.jooq.impl.Transformations.transformInConditionSubqueryWithLimitToDerivedTable;
import static org.jooq.impl.Transformations.subqueryWithLimit;
import static org.jooq.tools.Convert.convert;

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
import org.jooq.impl.QOM.Array;
import org.jooq.impl.QOM.Quantifier;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * @author Lukas Eder
 */
final class QuantifiedComparisonCondition extends AbstractCondition implements LikeEscapeStep, UNotYetImplemented {

    private static final Clause[]        CLAUSES                          = { CONDITION, CONDITION_BETWEEN };
    private static final Set<SQLDialect> NO_SUPPORT_QUANTIFIED_LIKE       = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE);
    private static final Set<SQLDialect> NO_SUPPORT_QUANTIFIED_SIMILAR_TO = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, YUGABYTEDB);
    private static final Set<SQLDialect> SUPPORTS_QUANTIFIED_ARRAYS       = SQLDialect.supportedBy(POSTGRES);

    private final QuantifiedSelect<?>    query;
    private final Field<?>               field;
    private final Comparator             comparator;
    private Character                    escape;

    QuantifiedComparisonCondition(QuantifiedSelect<?> query, Field<?> field, Comparator comparator) {
        this.query = query;
        this.field = field;
        this.comparator = comparator;
    }

    @Override
    public Condition escape(char c) {
        this.escape = c;
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final void accept(Context<?> ctx) {
        SelectQueryImpl<?> s;

        if (field.getDataType().isEmbeddable()) {
            ctx.visit(row(embeddedFields(field)).compare(comparator, query));
        }
        else if ((comparator == EQUALS || comparator == NOT_EQUALS)
                && (query instanceof QOM.QuantifiedSelect)
                && (s = subqueryWithLimit(((QOM.QuantifiedSelect<?>) query).$select())) != null
                && transformInConditionSubqueryWithLimitToDerivedTable(ctx.configuration())) {






        }










        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        boolean quantifiedArrayParam = query instanceof QOM.QuantifiedArray<?> a ? a.$arg2() instanceof Param : false;
        boolean quantifiedArray = query instanceof QOM.QuantifiedArray<?> a ? a.$arg2() instanceof Array : false;
        boolean emulateOperator;

        switch (comparator) {
            case LIKE:
            case NOT_LIKE:
            case LIKE_IGNORE_CASE:
            case NOT_LIKE_IGNORE_CASE:
                emulateOperator = escape != null || NO_SUPPORT_QUANTIFIED_LIKE.contains(ctx.dialect());
                break;
            case SIMILAR_TO:
            case NOT_SIMILAR_TO:
                emulateOperator = escape != null || NO_SUPPORT_QUANTIFIED_SIMILAR_TO.contains(ctx.dialect());
                break;
            default:
                emulateOperator = false;
                break;
        }

        // [#9224] Special case when a SQL dialect actually supports quantified
        //         arrays, such as x = any(?::int[]) in PostgreSQL
        if (quantifiedArrayParam && SUPPORTS_QUANTIFIED_ARRAYS.contains(ctx.dialect()) && !emulateOperator) {
            accept1(ctx);
        }
        else if (quantifiedArrayParam || quantifiedArray) {
            QOM.QuantifiedArray<?> a = (org.jooq.impl.QOM.QuantifiedArray<?>) query;
            ctx.visit(DSL.condition(
                a.$quantifier() == Quantifier.ALL ? Operator.AND : Operator.OR,
                a.$array() instanceof Array
                    ? map(((Array) a.$array()).$elements(), v -> comparisonCondition(comparator, (Field<String>) v))
                    : map(((Param<? extends Object[]>) a.$array()).getValue(), v -> v instanceof Field ? comparisonCondition(comparator, (Field<String>) v) : comparisonCondition(comparator, v))
            ));
        }
        else if (emulateOperator) {
            Field<String> pattern = DSL.field(name("pattern"), VARCHAR);
            Condition condition;
            Field<Boolean> lhs;

            switch (comparator) {
                case NOT_LIKE:
                case NOT_SIMILAR_TO:
                case NOT_LIKE_IGNORE_CASE:
                    condition = comparisonCondition(inverse(comparator), pattern);
                    lhs = inline(false);
                    break;
                case LIKE:
                case SIMILAR_TO:
                case LIKE_IGNORE_CASE:
                    condition = comparisonCondition(comparator, pattern);
                    lhs = inline(true);
                    break;
                default:
                    throw new IllegalStateException();
            }

            Table<?> t;
            Quantifier q;

            if (query instanceof QuantifiedArray<?> a) {
                t = new ArrayTable(a.$array()).asTable("t", "pattern");
                q = a.$quantifier();
            }
            else {
                QOM.QuantifiedSelect<?> s = (QOM.QuantifiedSelect<?>) query;
                t = new AliasedSelect<>(s.$select(), true, true, false, name("pattern")).as("t");
                q = s.$quantifier();
            }

            ctx.visit(lhs.eq(quantify(q, select(condition).from(t))));
        }
        else {
            accept1(ctx);
        }
    }

    private final void accept1(Context<?> ctx) {
        switch (ctx.family()) {








            default:
                ctx.visit(field)
                   .sql(' ')
                   .visit(comparator.toKeyword())
                   .sql(' ')
                   .visit(query);

                break;
        }
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
                return escape != null ? field.like(Convert.convert(value, String.class), escape) : field.like(Convert.convert(value, String.class));

            case NOT_LIKE:
                return escape != null ? field.notLike(Convert.convert(value, String.class), escape) : field.notLike(Convert.convert(value, String.class));

            case SIMILAR_TO:
                return escape != null ? field.similarTo(Convert.convert(value, String.class), escape) : field.similarTo(Convert.convert(value, String.class));

            case NOT_SIMILAR_TO:
                return escape != null ? field.notSimilarTo(Convert.convert(value, String.class), escape) : field.notSimilarTo(Convert.convert(value, String.class));

            case LIKE_IGNORE_CASE:
                return escape != null ? field.likeIgnoreCase(Convert.convert(value, String.class), escape) : field.likeIgnoreCase(Convert.convert(value, String.class));

            case NOT_LIKE_IGNORE_CASE:
                return escape != null ? field.notLikeIgnoreCase(Convert.convert(value, String.class), escape) : field.notLikeIgnoreCase(Convert.convert(value, String.class));

            default:
                return ((Field) field).compare(operator, value);
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
