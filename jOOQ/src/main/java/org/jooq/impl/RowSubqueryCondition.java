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
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.GREATER;
import static org.jooq.Comparator.GREATER_OR_EQUAL;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.LESS;
import static org.jooq.Comparator.LESS_OR_EQUAL;
import static org.jooq.Comparator.NOT_EQUALS;
import static org.jooq.Comparator.NOT_IN;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.*;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Quantifier.ALL;
import static org.jooq.impl.Quantifier.ANY;
import static org.jooq.impl.Tools.embeddedFieldsRow;
import static org.jooq.impl.Tools.fieldNames;
import static org.jooq.impl.Tools.fieldsByName;
import static org.jooq.impl.Tools.visitSubquery;
import static org.jooq.impl.Transformations.transformInConditionSubqueryWithLimitToDerivedTable;
import static org.jooq.impl.Transformations.subqueryWithLimit;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Context;
import org.jooq.Name;
// ...
import org.jooq.QuantifiedSelect;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Row;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectOrderByStep;
import org.jooq.impl.Tools.BooleanDataKey;

/**
 * @author Lukas Eder
 */
final class RowSubqueryCondition extends AbstractCondition {
    private static final Clause[]        CLAUSES                                    = { CONDITION, CONDITION_COMPARISON };
    private static final Set<SQLDialect> NO_SUPPORT_NATIVE                          = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD);
    private static final Set<SQLDialect> NO_SUPPORT_QUANTIFIED                      = SQLDialect.supportedBy(DERBY, FIREBIRD, SQLITE);
    // See https://bugs.mysql.com/bug.php?id=103494
    private static final Set<SQLDialect> NO_SUPPORT_QUANTIFIED_OTHER_THAN_IN_NOT_IN = SQLDialect.supportedBy(MARIADB, MYSQL);






    private final Row                    left;
    private final Select<?>              right;
    private final QuantifiedSelect<?>    rightQuantified;
    private final Comparator             comparator;

    RowSubqueryCondition(Row left, Select<?> right, Comparator comparator) {
        this.left = left;
        this.right = right;
        this.rightQuantified = null;
        this.comparator = comparator;
    }

    RowSubqueryCondition(Row left, QuantifiedSelect<?> right, Comparator comparator) {
        this.left = left;
        this.right = null;
        this.rightQuantified = right;
        this.comparator = comparator;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate(ctx));
    }

    @Override // Avoid AbstractCondition implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    private static final boolean inOrNotIn(Comparator comparator, Quantifier quantifier) {
        return comparator == EQUALS && quantifier == ANY
            || comparator == NOT_EQUALS && quantifier == ALL;
    }

    private final QueryPartInternal delegate(Context<?> ctx) {

        // [#3505] TODO: Emulate this where it is not supported
        if (rightQuantified != null) {

            // TODO: Handle all cases, not just the query one
            QuantifiedSelectImpl<?> q = (QuantifiedSelectImpl<?>) rightQuantified;
            boolean inOrNotIn = inOrNotIn(comparator, q.quantifier);

            if (NO_SUPPORT_QUANTIFIED.contains(ctx.dialect()) ||
                NO_SUPPORT_QUANTIFIED_OTHER_THAN_IN_NOT_IN.contains(ctx.dialect()) && !inOrNotIn) {

                switch (comparator) {
                    case EQUALS:
                    case NOT_EQUALS: {
                        if (inOrNotIn)
                            return new RowSubqueryCondition(left, q.query, comparator == EQUALS ? IN : NOT_IN);
                        else
                            return emulationUsingExists(ctx, left, q.query, comparator == EQUALS ? NOT_EQUALS : EQUALS, comparator == EQUALS);
                    }

                    case GREATER:
                    case GREATER_OR_EQUAL:
                    case LESS:
                    case LESS_OR_EQUAL:
                    default:
                        return emulationUsingExists(ctx, left, q.query, q.quantifier == ALL ? comparator.inverse() : comparator, q.quantifier == ALL);
                }
            }
            else
                return new Native();
        }









        else if (NO_SUPPORT_NATIVE.contains(ctx.dialect()))
            return emulationUsingExists(ctx, left, right,
                comparator == GREATER
             || comparator == GREATER_OR_EQUAL
             || comparator == LESS
             || comparator == LESS_OR_EQUAL ? comparator : EQUALS,
                comparator == NOT_IN || comparator == NOT_EQUALS
            );
        else
            return new Native();
    }

    private static final QueryPartInternal emulationUsingExists(Context<?> ctx, Row row, Select<?> select, Comparator comparator, boolean notExists) {
        Select<Record> subselect = emulatedSubselect(ctx, row, select, comparator);
        return (QueryPartInternal) (notExists ? notExists(subselect) : exists(subselect));
    }

    private static final SelectOrderByStep<Record> emulatedSubselect(Context<?> ctx, Row row, Select<?> s, Comparator c) {
        RenderContext render = ctx instanceof RenderContext ? (RenderContext) ctx : null;
        Row l = embeddedFieldsRow(row);
        Name table = name(render == null ? "t" : render.nextAlias());
        Name[] names = fieldNames(l.size());

        return select()
              .from(new AliasedSelect<>(s, true, true, names).as(table))
              .where(c == null
                  ? noCondition()
                  : new RowCondition(l, row(fieldsByName(table, names)), c));
    }

    private class Native extends AbstractCondition {

        @Override
        public final void accept(Context<?> ctx) {
            SelectQueryImpl<?> s;

            if ((comparator == IN || comparator == NOT_IN)
                    && right != null
                    && (s = subqueryWithLimit(right)) != null
                    && transformInConditionSubqueryWithLimitToDerivedTable(ctx.configuration())) {



            }
            else if ((comparator == EQUALS || comparator == NOT_EQUALS)
                    && rightQuantified != null
                    && (s = subqueryWithLimit(rightQuantified)) != null
                    && transformInConditionSubqueryWithLimitToDerivedTable(ctx.configuration())) {



            }
            else
                accept0(ctx);
        }

        final void accept0(Context<?> ctx) {
            switch (ctx.family()) {















                default:
                    ctx.visit(left)
                       .sql(' ')
                       .visit(comparator.toKeyword())
                       .sql(' ');

                    if (rightQuantified == null) {

                        // Some databases need extra parentheses around the RHS
                        boolean extraParentheses = false ;

                        ctx.sql(extraParentheses ? "((" : "(")
                           .data(BooleanDataKey.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, true, c -> visitSubquery(c, right, false))
                           .sql(extraParentheses ? "))" : ")");
                    }

                    // [#2054] Quantified row value expression comparison predicates shouldn't have parentheses before ANY or ALL
                    else
                        ctx.data(BooleanDataKey.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, true, c -> c.subquery(true).visit(rightQuantified).subquery(false));

                    break;
            }
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }
    }
}
