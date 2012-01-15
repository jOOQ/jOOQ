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

import static java.util.Arrays.asList;
import static org.jooq.impl.Factory.condition;
import static org.jooq.impl.Factory.exists;
import static org.jooq.impl.Factory.notExists;

import java.util.Collection;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JoinType;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.TableOnConditionStep;
import org.jooq.TableOnStep;
import org.jooq.exception.DataAccessException;

/**
 * A table consisting of two joined tables and possibly a join condition
 *
 * @author Lukas Eder
 */
class JoinTable extends AbstractTable<Record> implements TableOnStep, TableOnConditionStep {

    /**
     * Generated UID
     */
    private static final long           serialVersionUID = 8377996833996498178L;

    private final Table<?>              lhs;
    private final Table<?>              rhs;

    private final JoinType              type;
    private final ConditionProviderImpl condition;
    private final FieldList             using;

    JoinTable(TableLike<?> lhs, TableLike<?> rhs, JoinType type) {
        super("join");

        this.lhs = lhs.asTable();
        this.rhs = rhs.asTable();
        this.type = type;

        this.condition = new ConditionProviderImpl();
        this.using = new FieldList();
    }

    // ------------------------------------------------------------------------
    // Table API
    // ------------------------------------------------------------------------

    @Override
    public final void toSQL(RenderContext context) {
        context.sql(lhs)
               .sql(" ")
               .sql(getType(context).toSQL())
               .sql(" ")
               .sql(rhs);

        switch (getType(context)) {

            // CROSS JOIN does not have any additional clauses
            case CROSS_JOIN:

            // NATURAL JOIN does not have any additional clauses
            case NATURAL_JOIN:
            case NATURAL_LEFT_OUTER_JOIN:
            case NATURAL_RIGHT_OUTER_JOIN:
                break;

            // Regular JOINs
            default: {
                toSQL0(context);
                break;
            }
        }
    }

    final JoinType getType(RenderContext context) {
        if (context.getDialect() == SQLDialect.ASE && type == JoinType.CROSS_JOIN) {
            return JoinType.JOIN;
        }
        else {
            return type;
        }
    }

    private void toSQL0(RenderContext context) {
        if (!using.isEmpty()) {
            context.sql(" using (");
            Util.toSQLNames(context, using);
            context.sql(")");
        }
        else {
            context.sql(" on ").sql(condition);
        }
    }

    @Override
    public final void bind(BindContext context) throws DataAccessException {
        context.bind(lhs).bind(rhs);

        if (!using.isEmpty()) {
            context.bind((QueryPart) using);
        }
        else {
            context.bind(condition);
        }
    }

    @Override
    public final Table<Record> as(String alias) {
        return new TableAlias<Record>(this, alias);
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    protected final FieldList getFieldList() {
        FieldList result = new FieldList();

        result.addAll(lhs.asTable().getFields());
        result.addAll(rhs.asTable().getFields());

        return result;
    }

    @Override
    protected final List<Attachable> getAttachables0() {
        return getAttachables(lhs, rhs, condition, using);
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    // ------------------------------------------------------------------------
    // Join API
    // ------------------------------------------------------------------------

    @Override
    public final JoinTable on(Condition... conditions) {
        condition.addConditions(conditions);
        return this;
    }

    @Override
    public final JoinTable on(String sql) {
        and(sql);
        return this;
    }

    @Override
    public final JoinTable on(String sql, Object... bindings) {
        and(sql, bindings);
        return this;
    }

    @Override
    public final JoinTable using(Field<?>... fields) {
        return using(asList(fields));
    }

    @Override
    public final JoinTable using(Collection<? extends Field<?>> fields) {
        using.addAll(fields);
        return this;
    }

    @Override
    public final JoinTable and(Condition c) {
        condition.addConditions(c);
        return this;
    }

    @Override
    public final JoinTable and(String sql) {
        return and(condition(sql));
    }

    @Override
    public final JoinTable and(String sql, Object... bindings) {
        return and(condition(sql, bindings));
    }

    @Override
    public final JoinTable andNot(Condition c) {
        return and(c.not());
    }

    @Override
    public final JoinTable andExists(Select<?> select) {
        return and(exists(select));
    }

    @Override
    public final JoinTable andNotExists(Select<?> select) {
        return and(notExists(select));
    }

    @Override
    public final JoinTable or(Condition c) {
        condition.addConditions(Operator.OR, c);
        return this;
    }

    @Override
    public final JoinTable or(String sql) {
        return or(condition(sql));
    }

    @Override
    public final JoinTable or(String sql, Object... bindings) {
        return or(condition(sql, bindings));
    }

    @Override
    public final JoinTable orNot(Condition c) {
        return or(c.not());
    }

    @Override
    public final JoinTable orExists(Select<?> select) {
        return or(exists(select));
    }

    @Override
    public final JoinTable orNotExists(Select<?> select) {
        return or(notExists(select));
    }
}
