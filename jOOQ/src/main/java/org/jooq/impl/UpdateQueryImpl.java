/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;
import org.jooq.UpdateQuery;

/**
 * @author Lukas Eder
 */
class UpdateQueryImpl<R extends Record> extends AbstractStoreQuery<R> implements UpdateQuery<R> {

    private static final long           serialVersionUID = -660460731970074719L;

    private final FieldMapForUpdate     updateMap;
    private final ConditionProviderImpl condition;

    UpdateQueryImpl(Configuration configuration, Table<R> table) {
        super(configuration, table);

        this.condition = new ConditionProviderImpl();
        this.updateMap = new FieldMapForUpdate();
    }

    @Override
    protected final List<Attachable> getAttachables0() {
        return getAttachables(updateMap, condition);
    }

    @Override
    protected final FieldMapForUpdate getValues() {
        return updateMap;
    }

    @Override
    public final void setRecord(R record) {
        for (Field<?> field : record.getFields()) {
            if (((AbstractRecord) record).getValue0(field).isChanged()) {
                addValue(record, field);
            }
        }
    }

    @Override
    public final void addValues(Map<? extends Field<?>, ?> map) {
        updateMap.set(map);
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(getInto()).bind(updateMap).bind(condition);
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

    final Condition getWhere() {
        return condition.getWhere();
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.keyword("update ")
               .declareTables(true)
               .sql(getInto())
               .declareTables(false)
               .formatSeparator()
               .keyword("set ")
               .formatIndentLockStart()
               .sql(updateMap)
               .formatIndentLockEnd();

        if (!(getWhere() instanceof TrueCondition)) {
            context.formatSeparator()
                   .keyword("where ")
                   .sql(getWhere());
        }
    }

    @Override
    protected boolean isExecutable() {
        return updateMap.size() > 0;
    }
}
