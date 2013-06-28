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

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Select;

/**
 * A union is a <code>SELECT</code> statement that combines several sub-selects
 * with a <code>UNION</code> or a similar operator.
 *
 * @author Lukas Eder
 */
class Union<R extends Record> extends AbstractSelect<R> {

    private static final long               serialVersionUID = 7491446471677986172L;

    private final List<Select<? extends R>> queries;
    private final CombineOperator           operator;

    Union(Configuration configuration, Select<R> query1, Select<? extends R> query2, CombineOperator operator) {
        super(configuration);

        this.operator = operator;
        this.queries = new ArrayList<Select<? extends R>>();
        this.queries.add(query1);
        this.queries.add(query2);
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return queries.get(0).getRecordType();
    }

    @Override
    public final List<Field<?>> getSelect() {
        return queries.get(0).getSelect();
    }

    @Override
    public final void toSQL(RenderContext context) {
        for (int i = 0; i < queries.size(); i++) {
            if (i != 0) {
                context.formatSeparator()
                       .keyword(operator.toSQL(context.configuration().dialect()))
                       .formatSeparator();
            }

            wrappingParenthesis(context, "(");
            context.sql(queries.get(i));
            wrappingParenthesis(context, ")");
        }
    }

    private final void wrappingParenthesis(RenderContext context, String parenthesis) {
        switch (context.configuration().dialect()) {
            // Sybase ASE, Derby, Firebird and SQLite have some syntax issues with unions.
            // Check out https://issues.apache.org/jira/browse/DERBY-2374
            case ASE:
            case DERBY:
            case FIREBIRD:
            case SQLITE:

            // [#288] MySQL has a very special way of dealing with UNION's
            // So include it as well
            case MARIADB:
            case MYSQL:
                return;
        }

        if (")".equals(parenthesis)) {
            context.formatIndentEnd()
                   .formatNewLine();
        }

        context.sql(parenthesis);

        if ("(".equals(parenthesis)) {
            context.formatIndentStart()
                   .formatNewLine();
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(queries);
    }

    @Override
    final boolean isForUpdate() {
        return false;
    }
}
