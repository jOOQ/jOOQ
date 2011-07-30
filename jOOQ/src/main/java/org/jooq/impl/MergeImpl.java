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

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.MergeFinalStep;
import org.jooq.MergeMatchedSetMoreStep;
import org.jooq.MergeNotMatchedValuesStep;
import org.jooq.MergeOnConditionStep;
import org.jooq.MergeOnStep;
import org.jooq.MergeUsingStep;
import org.jooq.Operator;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.TableRecord;

/**
 * @author Lukas Eder
 */
class MergeImpl<R extends TableRecord<R>> extends AbstractQuery
implements

    // Cascading interface implementations for Merge behaviour
    MergeUsingStep,
    MergeOnStep,
    MergeOnConditionStep,
    MergeMatchedSetMoreStep,
    MergeNotMatchedValuesStep,
    MergeFinalStep {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -8835479296876774391L;

    private final Table<R>              table;
    private final ConditionProviderImpl on;
    private final FieldMapForUpdate     updateMap;
    private final FieldMapForInsert     insertMap;
    private TableLike<?>                using;

    MergeImpl(Configuration configuration, Table<R> table) {
        super(configuration);

        this.table = table;
        this.on = new ConditionProviderImpl();
        this.updateMap = new FieldMapForUpdate();
        this.insertMap = new FieldMapForInsert();
    }

    // -------------------------------------------------------------------------
    // QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final MergeImpl<R> using(TableLike<?> u) {
        this.using = u;
        return this;
    }

    @Override
    public final MergeImpl<R> usingDual() {
        this.using = create().selectOne();
        return this;
    }

    @Override
    public final MergeOnConditionStep on(Condition... conditions) {
        on.addConditions(conditions);
        return this;
    }

    @Override
    public final MergeOnConditionStep on(String sql) {
        return on(create().condition(sql));
    }

    @Override
    public final MergeOnConditionStep on(String sql, Object... bindings) {
        return on(create().condition(sql, bindings));
    }

    @Override
    public final MergeImpl<R> and(Condition condition) {
        on.addConditions(condition);
        return this;
    }

    @Override
    public final MergeImpl<R> and(String sql) {
        return and(create().condition(sql));
    }

    @Override
    public final MergeImpl<R> and(String sql, Object... bindings) {
        return and(create().condition(sql, bindings));
    }

    @Override
    public final MergeImpl<R> andNot(Condition condition) {
        return and(condition.not());
    }

    @Override
    public final MergeImpl<R> andExists(Select<?> select) {
        return and(create().exists(select));
    }

    @Override
    public final MergeImpl<R> andNotExists(Select<?> select) {
        return and(create().notExists(select));
    }

    @Override
    public final MergeImpl<R> or(Condition condition) {
        on.addConditions(Operator.OR, condition);
        return this;
    }

    @Override
    public final MergeImpl<R> or(String sql) {
        return or(create().condition(sql));
    }

    @Override
    public final MergeImpl<R> or(String sql, Object... bindings) {
        return or(create().condition(sql, bindings));
    }

    @Override
    public final MergeImpl<R> orNot(Condition condition) {
        return or(condition.not());
    }

    @Override
    public final MergeImpl<R> orExists(Select<?> select) {
        return or(create().exists(select));
    }

    @Override
    public final MergeImpl<R> orNotExists(Select<?> select) {
        return or(create().notExists(select));
    }

    @Override
    public final MergeImpl<R> whenMatchedThenUpdate() {
        return this;
    }

    @Override
    public final MergeImpl<R> set(Field<?> field, Object value) {
        return set(field, val(value));
    }

    @Override
    public final MergeImpl<R> set(Field<?> field, Field<?> value) {
        if (value == null) {
            return set(field, (Object) value);
        }

        updateMap.put(field, value);
        return this;
    }

    @Override
    public final MergeImpl<R> set(Map<? extends Field<?>, ?> map) {
        updateMap.set(map);
        return this;
    }

    @Override
    public final MergeImpl<R> whenNotMatchedThenInsert(Field<?>... fields) {
        return whenNotMatchedThenInsert(Arrays.asList(fields));
    }

    @Override
    public final MergeImpl<R> whenNotMatchedThenInsert(Collection<? extends Field<?>> fields) {
        insertMap.putFields(fields);
        return this;
    }

    @Override
    public final MergeImpl<R> values(Object... values) {
        return values(Arrays.asList(values));
    }

    @Override
    public final MergeImpl<R> values(Field<?>... values) {
        return values(Arrays.asList(values));
    }

    @Override
    public final MergeImpl<R> values(Collection<?> values) {
        insertMap.putValues(vals(values.toArray()));
        return this;
    }

    // -------------------------------------------------------------------------
    // QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void toSQL(RenderContext context) {
        context.sql("merge into ")
               .declareTables(true)
               .sql(table)
               .sql(" using ")
               .sql(wrapInParentheses(context.render(using)))
               .declareTables(false);

        switch (context.getDialect()) {
            case SQLSERVER:
            case SYBASE: {
                if (using instanceof Select) {
                    int hash = Math.abs(using.hashCode());

                    context.sql(" as ")
                           .sql("dummy_")
                           .sql(hash)
                           .sql("(");

                    String separator = "";
                    for (Field<?> field : ((Select<?>) using).getFields()) {

                        // Some fields are unnamed
                        // [#579] Correct this
                        String name = StringUtils.isBlank(field.getName())
                            ? "dummy_" + hash + "_" + Math.abs(field.hashCode())
                            : field.getName();

                        context.sql(separator).literal(name);
                        separator = ", ";
                    }

                    context.sql(")");
                }

                break;
            }
        }

        context.sql(" on ")
               .sql(wrapInParentheses(context.render(on)))
               .sql(" when matched then update set ")
               .sql(updateMap)
               .sql(" when not matched then insert ")
               .sql(insertMap);

        switch (context.getDialect()) {
            case SQLSERVER:
                context.sql(";");
                break;
        }
    }

    @Override
    public final void bind(BindContext context) throws SQLException {
        context.declareTables(true)
               .bind(table)
               .bind(using)
               .declareTables(false)
               .bind(on)
               .bind(updateMap)
               .bind(insertMap);
    }

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(table, using, on, updateMap, insertMap);
    }
}
