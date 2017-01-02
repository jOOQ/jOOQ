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

import static java.util.Arrays.asList;
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.NOT_EQUALS;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Tools.EMPTY_STRING;
import static org.jooq.impl.Tools.DataKey.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QuantifiedSelect;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Row;
import org.jooq.RowN;
import org.jooq.SQLDialect;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class RowSubqueryCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long         serialVersionUID = -1806139685201770706L;
    private static final Clause[]     CLAUSES          = { CONDITION, CONDITION_COMPARISON };

    private final Row                 left;
    private final Select<?>           right;
    private final QuantifiedSelect<?> rightQuantified;
    private final Comparator          comparator;

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

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    private final QueryPartInternal delegate(Context<?> ctx) {
        final Configuration configuration = ctx.configuration();
        final RenderContext render = ctx instanceof RenderContext ? (RenderContext) ctx : null;

        SQLDialect family = configuration.dialect().family();

        // [#3505] TODO: Emulate this where it is not supported
        if (rightQuantified != null) {
            return new Native();
        }

        // [#2395] These dialects have full native support for comparison
        // predicates with row value expressions and subqueries:
        else if (asList(H2, HSQLDB, MARIADB, MYSQL, POSTGRES).contains(family)) {
            return new Native();
        }



















        // [#2395] All other configurations have to be emulated
        else {
            String table = render == null ? "t" : render.nextAlias();

            List<String> names = new ArrayList<String>();
            for (int i = 0; i < left.size(); i++) {
                names.add(table + "_" + i);
            }

            Field<?>[] fields = new Field[names.size()];
            for (int i = 0; i < fields.length; i++) {
                fields[i] = field(name(table, names.get(i)));
            }

            Condition condition;
            switch (comparator) {
                case GREATER:
                    condition = ((RowN) left).gt(row(fields));
                    break;

                case GREATER_OR_EQUAL:
                    condition = ((RowN) left).ge(row(fields));
                    break;

                case LESS:
                    condition = ((RowN) left).lt(row(fields));
                    break;

                case LESS_OR_EQUAL:
                    condition = ((RowN) left).le(row(fields));
                    break;

                case IN:
                case EQUALS:
                case NOT_IN:
                case NOT_EQUALS:
                default:
                    condition = ((RowN) left).eq(row(fields));
                    break;
            }

            Select<Record> subselect =
            select().from(right.asTable(table, names.toArray(EMPTY_STRING)))
                    .where(condition);

            switch (comparator) {
                case NOT_IN:
                case NOT_EQUALS:
                    return (QueryPartInternal) notExists(subselect);

                default:
                    return (QueryPartInternal) exists(subselect);
            }
        }
    }

    private class Native extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -1552476981094856727L;

        @Override
        public final void accept(Context<?> ctx) {
            boolean subquery = ctx.subquery();

            ctx.visit(left)
               .sql(' ')
               .keyword(comparator.toSQL())
               .sql(' ');

            if (rightQuantified == null) {

                // Some databases need extra parentheses around the RHS
                boolean extraParentheses = asList().contains(ctx.family());

                ctx.sql(extraParentheses ? "((" : "(");
                ctx.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, true);
                ctx.subquery(true)
                   .formatIndentStart()
                   .formatNewLine()
                   .visit(right)
                   .formatIndentEnd()
                   .formatNewLine()
                   .subquery(subquery);
                ctx.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, null);
                ctx.sql(extraParentheses ? "))" : ")");
            }

            // [#2054] Quantified row value expression comparison predicates shouldn't have parentheses before ANY or ALL
            else {
                ctx.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, true);
                ctx.subquery(true)
                   .visit(rightQuantified)
                   .subquery(subquery);
                ctx.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, null);
            }
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }
    }
}