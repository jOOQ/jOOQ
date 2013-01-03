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
import static org.jooq.SQLDialect.ORACLE;

import org.jooq.BindContext;
import org.jooq.RenderContext;
import org.jooq.Row;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
class RowSubqueryCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long      serialVersionUID = -1806139685201770706L;

    private final Row              left;
    private final Select<?>        right;
    private final SubqueryOperator operator;

    RowSubqueryCondition(Row left, Select<?> right, SubqueryOperator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public final void toSQL(RenderContext context) {

        // Some databases need extra parentheses around the RHS
        boolean extraParentheses = asList(ORACLE).contains(context.getDialect());
        boolean subquery = context.subquery();

        context.sql(left)
               .sql(" ")
               .keyword(operator.toSQL())
               .sql(" (")
               .sql(extraParentheses ? "(" : "");
        context.setData(Utils.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, true);
        context.subquery(true)
               .sql(right)
               .subquery(subquery);
        context.setData(Utils.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY, null);
        context.sql(extraParentheses ? ")" : "")
               .sql(")");
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(left).bind(right);
    }
}