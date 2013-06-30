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
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;

import java.util.Collection;

import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
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

    private final Table<R>              table;
    private final ConditionProviderImpl condition;

    DeleteQueryImpl(Configuration configuration, Table<R> table) {
        super(configuration);

        this.table = table;
        this.condition = new ConditionProviderImpl();
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(getFrom()).bind(getWhere());
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

        context.keyword("delete ");

        // [#2464] MySQL supports a peculiar multi-table DELETE syntax for aliased tables:
        // DELETE t1 FROM my_table AS t1
        if (asList(MARIADB, MYSQL).contains(context.configuration().dialect())) {

            // [#2579] TODO: Improve Table API to discover aliased tables more
            // reliably instead of resorting to instanceof:
            if (getFrom() instanceof TableAlias ||
               (getFrom() instanceof TableImpl && ((TableImpl<R>)getFrom()).getAliasedTable() != null)) {
                context.sql(getFrom())
                       .sql(" ");
            }
        }

        context.keyword("from ");
        context.declareTables(true)
               .sql(getFrom())
               .declareTables(declare);

        if (!(getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("where ")
                   .sql(getWhere());
        }
    }
}
