/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
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
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
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
    private static final long serialVersionUID = -1806139685201770706L;

    private final Row         left;
    private final Select<?>   right;
    private final Comparator  comparator;

    RowSubqueryCondition(Row left, Select<?> right, Comparator comparator) {
        this.left = left;
        this.right = right;
        this.comparator = comparator;
    }

    @Override
    public final void toSQL(RenderContext context) {
        delegate(context.configuration(), context).toSQL(context);
    }

    @Override
    public final void bind(BindContext context) {
        delegate(context.configuration(), null).bind(context);
    }

    private final QueryPartInternal delegate(Configuration configuration, RenderContext context) {
        SQLDialect dialect = configuration.dialect();

        // [#2395] These dialects have full native support for comparison
        // predicates with row value expressions and subqueries:
        if (asList(H2, HSQLDB, MARIADB, MYSQL, POSTGRES).contains(dialect)) {
            return new Native();
        }

        // [#2395] These dialects have native support for = and <>
        else if (
            asList(H2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES).contains(dialect) &&
            asList(EQUALS, NOT_EQUALS).contains(comparator)) {

            return new Native();
        }

        // [#2395] These dialects have native support for IN and NOT IN
        else if (
            asList(H2, DB2, HSQLDB, MARIADB, MYSQL, ORACLE, POSTGRES).contains(dialect) &&
            asList(IN, NOT_IN).contains(comparator)) {

            return new Native();
        }

        // [#2395] All other configurations have to be simulated
        else {
            String table = context == null ? "t" : context.nextAlias();

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
            boolean extraParentheses = asList(ORACLE).contains(context.configuration().dialect());
            boolean subquery = context.subquery();

            context.sql(left)
                   .sql(" ")
                   .keyword(comparator.toSQL())
                   .sql(" (")
                   .sql(extraParentheses ? "(" : "");
            context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, true);
            context.subquery(true)
                   .sql(right)
                   .subquery(subquery);
            context.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, null);
            context.sql(extraParentheses ? ")" : "")
                   .sql(")");
        }

        @Override
        public final void bind(BindContext context) {
            context.bind(left).bind(right);
        }
    }
}