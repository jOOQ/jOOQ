/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_COMPARISON;
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.IN;
import static org.jooq.Comparator.NOT_EQUALS;
import static org.jooq.Comparator.NOT_IN;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Utils.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY;

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
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
class RowSubqueryCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1806139685201770706L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_COMPARISON };

    private final Row             left;
    private final Select<?>       right;
    private final Comparator      comparator;

    RowSubqueryCondition(Row left, Select<?> right, Comparator comparator) {
        this.left = left;
        this.right = right;
        this.comparator = comparator;
    }

    @Override
    public final void toSQL(RenderContext ctx) {
        delegate(ctx.configuration(), ctx).toSQL(ctx);
    }

    @Override
    public final void bind(BindContext ctx) {
        delegate(ctx.configuration(), null).bind(ctx);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return delegate(ctx.configuration(), null).clauses(ctx);
    }

    private final QueryPartInternal delegate(Configuration configuration, RenderContext ctx) {
        SQLDialect family = configuration.dialect().family();

        // [#2395] These dialects have full native support for comparison
        // predicates with row value expressions and subqueries:
        if (asList(H2, HSQLDB, MARIADB, MYSQL, POSTGRES).contains(family)) {
            return new Native();
        }

        // [#2395] These dialects have native support for = and <>
        else if (
            asList(H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES).contains(family) &&
            asList(EQUALS, NOT_EQUALS).contains(comparator)) {

            return new Native();
        }

        // [#2395] These dialects have native support for IN and NOT IN
        else if (
            asList(H2, DB2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES).contains(family) &&
            asList(IN, NOT_IN).contains(comparator)) {

            return new Native();
        }

        // [#2395] All other configurations have to be simulated
        else {
            String table = ctx == null ? "t" : ctx.nextAlias();

            List<String> names = new ArrayList<String>();
            for (int i = 0; i < left.size(); i++) {
                names.add(table + "_" + i);
            }

            Field<?>[] fields = new Field[names.size()];
            for (int i = 0; i < fields.length; i++) {
                fields[i] = fieldByName(table, names.get(i));
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
            select().from(right.asTable(table, names.toArray(new String[0])))
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
        public final void toSQL(RenderContext context) {

            // Some databases need extra parentheses around the RHS
            boolean extraParentheses = asList(ORACLE).contains(context.configuration().dialect().family());
            boolean subquery = context.subquery();

            context.visit(left)
                   .sql(" ")
                   .keyword(comparator.toSQL())
                   .sql(" (")
                   .sql(extraParentheses ? "(" : "");
            context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, true);
            context.subquery(true)
                   .visit(right)
                   .subquery(subquery);
            context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, null);
            context.sql(extraParentheses ? ")" : "")
                   .sql(")");
        }

        @Override
        public final void bind(BindContext context) {
            context.visit(left).visit(right);
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }
    }
}