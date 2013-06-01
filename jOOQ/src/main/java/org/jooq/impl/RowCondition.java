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
import static org.jooq.Comparator.GREATER;
import static org.jooq.Comparator.GREATER_OR_EQUAL;
import static org.jooq.Comparator.LESS;
import static org.jooq.Comparator.LESS_OR_EQUAL;
import static org.jooq.Comparator.NOT_EQUALS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.Row;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
class RowCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -1806139685201770706L;

    private final Row         left;
    private final Row         right;
    private final Comparator  comparator;

    RowCondition(Row left, Row right, Comparator comparator) {
        this.left = left;
        this.right = right;
        this.comparator = comparator;
    }

    @Override
    public final void toSQL(RenderContext context) {
        delegate(context.configuration()).toSQL(context);
    }

    @Override
    public final void bind(BindContext context) {
        delegate(context.configuration()).bind(context);
    }

    private final QueryPartInternal delegate(Configuration configuration) {
        SQLDialect dialect = configuration.dialect();

        // Regular comparison predicate simulation
        if (asList(EQUALS, NOT_EQUALS).contains(comparator) &&
            asList(ASE, DERBY, FIREBIRD, INGRES, SQLSERVER, SQLITE, SYBASE).contains(dialect.family())) {
            List<Condition> conditions = new ArrayList<Condition>();

            Field<?>[] leftFields = left.fields();
            Field<?>[] rightFields = right.fields();

            for (int i = 0; i < leftFields.length; i++) {
                conditions.add(leftFields[i].equal((Field) rightFields[i]));
            }

            Condition result = new CombinedCondition(Operator.AND, conditions);

            if (comparator == NOT_EQUALS) {
                result = result.not();
            }

            return (QueryPartInternal) result;
        }

        // Ordering comparison predicate simulation
        else if (asList(GREATER, GREATER_OR_EQUAL, LESS, LESS_OR_EQUAL).contains(comparator) &&
                 asList(DERBY, CUBRID, FIREBIRD, ORACLE, SQLSERVER, SQLITE, SYBASE).contains(dialect.family())) {

            // The order component of the comparator (stripping the equal component)
            Comparator order
                = (comparator == GREATER) ? GREATER
                : (comparator == GREATER_OR_EQUAL) ? GREATER
                : (comparator == LESS) ? LESS
                : (comparator == LESS_OR_EQUAL) ? LESS
                : null;

            // Whether the comparator has an equal component
            boolean equal
                = (comparator == GREATER_OR_EQUAL)
                ||(comparator == LESS_OR_EQUAL);

            // The following algorithm simulates the equivalency of these expressions:
            // (A, B, C) > (X, Y, Z)
            // (A > X) OR (A = X AND B > Y) OR (A = X AND B = Y AND C > Z)
            List<Condition> outer = new ArrayList<Condition>();

            Field<?>[] leftFields = left.fields();
            Field<?>[] rightFields = right.fields();

            for (int i = 0; i < leftFields.length; i++) {
                List<Condition> inner = new ArrayList<Condition>();

                for (int j = 0; j < i; j++) {
                    inner.add(leftFields[j].equal((Field) rightFields[j]));
                }

                inner.add(leftFields[i].compare(order, (Field) rightFields[i]));
                outer.add(new CombinedCondition(Operator.AND, inner));
            }

            if (equal) {
                outer.add(new RowCondition(left, right, Comparator.EQUALS));
            }

            Condition result = new CombinedCondition(Operator.OR, outer);
            return (QueryPartInternal) result;
        }
        else {
            return new Native();
        }
    }

    private class Native extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -2977241780111574353L;

        @Override
        public final void toSQL(RenderContext context) {

            // Some dialects do not support != comparison with rows
            if (comparator == NOT_EQUALS && asList(DB2).contains(context.configuration().dialect())) {
                context.keyword("not(")
                       .sql(left)
                       .sql(" = ")
                       .sql(right)
                       .sql(")");
            }
            else {
                // Some databases need extra parentheses around the RHS
                boolean extraParentheses = asList(ORACLE).contains(context.configuration().dialect());

                context.sql(left)
                       .sql(" ")
                       .sql(comparator.toSQL())
                       .sql(" ")
                       .sql(extraParentheses ? "(" : "")
                       .sql(right)
                       .sql(extraParentheses ? ")" : "");
            }
        }

        @Override
        public final void bind(BindContext context) {
            context.bind(left).bind(right);
        }
    }
}