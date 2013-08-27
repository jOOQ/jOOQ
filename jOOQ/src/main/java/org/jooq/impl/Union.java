/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.Clause.SELECT_EXCEPT;
import static org.jooq.Clause.SELECT_INTERSECT;
import static org.jooq.Clause.SELECT_UNION;
import static org.jooq.Clause.SELECT_UNION_ALL;
import static org.jooq.impl.Utils.visitAll;

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
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
    private final Clause[]                  clauses;

    Union(Configuration configuration, Select<R> query1, Select<? extends R> query2, CombineOperator operator) {
        super(configuration);

        this.queries = new ArrayList<Select<? extends R>>();
        this.queries.add(query1);
        this.queries.add(query2);
        this.operator = operator;

        switch (operator) {
            case EXCEPT:    this.clauses = new Clause[] { SELECT_EXCEPT }    ; break;
            case INTERSECT: this.clauses = new Clause[] { SELECT_INTERSECT } ; break;
            case UNION:     this.clauses = new Clause[] { SELECT_UNION }     ; break;
            case UNION_ALL: this.clauses = new Clause[] { SELECT_UNION_ALL } ; break;
            default:        throw new IllegalArgumentException("Operator not supported : " + operator);
        }
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
            context.visit(queries.get(i));
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
        visitAll(context, queries);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return clauses;
    }

    @Override
    final boolean isForUpdate() {
        return false;
    }
}
