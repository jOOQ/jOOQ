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

import static java.util.Arrays.asList;
import static org.jooq.Clause.DELETE;
import static org.jooq.Clause.DELETE_DELETE;
import static org.jooq.Clause.DELETE_WHERE;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;

import java.util.Collection;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DeleteQuery;
import org.jooq.Operator;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class DeleteQueryImpl<R extends Record> extends AbstractQuery implements DeleteQuery<R> {

    private static final long           serialVersionUID = -1943687511774150929L;
    private static final Clause[]       CLAUSES          = { DELETE };

    private final Table<R>              table;
    private final ConditionProviderImpl condition;

    DeleteQueryImpl(Configuration configuration, Table<R> table) {
        super(configuration);

        this.table = table;
        this.condition = new ConditionProviderImpl();
    }

    final Table<R> getFrom() {
        return table;
    }

    final Condition getWhere() {
        return condition.getWhere();
    }

    @Override
    public final void addConditions(Collection<Condition> conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Condition... conditions) {
        condition.addConditions(conditions);
    }

    @Override
    public final void addConditions(Operator operator, Condition... conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void addConditions(Operator operator, Collection<Condition> conditions) {
        condition.addConditions(operator, conditions);
    }

    @Override
    public final void toSQL(RenderContext context) {
        boolean declare = context.declareTables();

        context.start(DELETE_DELETE)
               .keyword("delete").sql(" ");

        // [#2464] MySQL supports a peculiar multi-table DELETE syntax for aliased tables:
        // DELETE t1 FROM my_table AS t1
        if (asList(MARIADB, MYSQL).contains(context.configuration().dialect())) {

            // [#2579] TODO: Improve Table API to discover aliased tables more
            // reliably instead of resorting to instanceof:
            if (getFrom() instanceof TableAlias ||
               (getFrom() instanceof TableImpl && ((TableImpl<R>)getFrom()).getAliasedTable() != null)) {
                context.visit(getFrom())
                       .sql(" ");
            }
        }

        context.keyword("from").sql(" ")
               .declareTables(true)
               .visit(getFrom())
               .declareTables(declare)
               .end(DELETE_DELETE)
               .start(DELETE_WHERE);

        if (!(getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("where").sql(" ")
                   .visit(getWhere());
        }

        context.end(DELETE_WHERE);
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(getFrom()).visit(getWhere());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
