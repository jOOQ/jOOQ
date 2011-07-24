/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import java.util.Arrays;
import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.JoinType;
import org.jooq.Operator;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.TableLike;

/**
 * @author Lukas Eder
 */
class SelectQueryImpl extends AbstractSubSelect<Record> implements SelectQuery {

    private static final long serialVersionUID = 1555503854543561285L;

    SelectQueryImpl(Configuration configuration) {
        this(configuration, null);
    }

    SelectQueryImpl(Configuration configuration, boolean distinct) {
        this(configuration, null, distinct);
    }

    SelectQueryImpl(Configuration configuration, TableLike<?> from) {
        this(configuration, from, false);
    }

    SelectQueryImpl(Configuration configuration, TableLike<?> from, boolean distinct) {
        super(configuration, from, distinct);
    }

    @Override
    public final void addFrom(Collection<TableLike<?>> from) {
        for (TableLike<?> provider : from) {
            getFrom().add(provider.asTable());
        }
    }

    @Override
    public final void addFrom(TableLike<?>... from) {
        addFrom(Arrays.asList(from));
    }

    @Override
    public final void addConnectBy(Condition condition) {
        getConnectBy().addConditions(condition);
    }

    @Override
    public final void addConnectByNoCycle(Condition condition) {
        getConnectBy().addConditions(condition);
        setConnectByNoCycle(true);
    }

    @Override
    public final void setConnectByStartWith(Condition condition) {
        setStartWith(condition);
    }

    @Override
    public final void addGroupBy(Collection<? extends Field<?>> fields) {
        getGroupBy().addAll(fields);
    }

    @Override
    public final void addGroupBy(Field<?>... fields) {
        addGroupBy(Arrays.asList(fields));
    }

    @Override
    public final void addHaving(Condition... conditions) {
        addHaving(Arrays.asList(conditions));
    }

    @Override
    public final void addHaving(Collection<Condition> conditions) {
        getHaving().addConditions(conditions);
    }

    @Override
    public final void addHaving(Operator operator, Condition... conditions) {
        getHaving().addConditions(operator, conditions);
    }

    @Override
    public final void addHaving(Operator operator, Collection<Condition> conditions) {
        getHaving().addConditions(operator, conditions);
    }

    @Override
    public final void addJoin(TableLike<?> table, Condition... conditions) {
        getJoin().add(new Join(table, JoinType.JOIN, conditions));
    }

    @Override
    public final void addJoin(TableLike<?> table, JoinType type, Condition... conditions) {
        getJoin().add(new Join(table, type, conditions));
    }

    @Override
    public final void addJoinUsing(TableLike<?> table, Collection<? extends Field<?>> fields) {
        getJoin().add(new Join(table, JoinType.JOIN, fields));
    }

    @Override
    public final void addJoinUsing(TableLike<?> table, JoinType type, Collection<? extends Field<?>> fields) {
        getJoin().add(new Join(table, type, fields));
    }

    @Override
    public final void addHint(String hint) {
        setHint(hint);
    }
}
