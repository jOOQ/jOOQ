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

import static org.jooq.impl.Factory.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jooq.DataType;
import org.jooq.DivideByOnStep;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.JoinType;
import org.jooq.PivotForStep;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnStep;

abstract class AbstractTable<R extends Record> extends AbstractType<R> implements Table<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 3155496238969274871L;

    AbstractTable(String name) {
        this(name, null);
    }

    AbstractTable(String name, Schema schema) {
        super(name, schema);
    }

    // ------------------------------------------------------------------------
    // XXX: TableLike API
    // ------------------------------------------------------------------------

    @Override
    public final Table<R> asTable() {
        return this;
    }

    @Override
    public final Table<R> asTable(String alias) {
        return as(alias);
    }

    // ------------------------------------------------------------------------
    // XXX: Table API
    // ------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public Identity<R, ? extends Number> getIdentity() {
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Subclasses should override this method
     */
    @Override
    public List<ForeignKey<R, ?>> getReferences() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <O extends Record> List<ForeignKey<R, O>> getReferencesTo(Table<O> other) {
        List<ForeignKey<R, O>> result = new ArrayList<ForeignKey<R, O>>();

        for (ForeignKey<R, ?> reference : getReferences()) {
            if (other.equals(reference.getKey().getTable())) {
                result.add((ForeignKey<R, O>) reference);
            }
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Subclasses may call this method to create {@link TableField} objects that
     * are linked to this table.
     *
     * @param name The name of the field (case-sensitive!)
     * @param type The data type of the field
     */
    protected static final <R extends Record, T> TableField<R, T> createField(String name, DataType<T> type, Table<R> table) {
        return new TableFieldImpl<R, T>(name, type, table);
    }

    // ------------------------------------------------------------------------
    // XXX: PIVOT API
    // ------------------------------------------------------------------------

    @Override
    public final PivotForStep pivot(Field<?>... aggregateFunctions) {
        return new Pivot<Object>(this, aggregateFunctions);
    }

    @Override
    public final PivotForStep pivot(Collection<? extends Field<?>> aggregateFunctions) {
        return pivot(aggregateFunctions.toArray(new Field[0]));
    }

    // ------------------------------------------------------------------------
    // XXX: DIVISION API
    // ------------------------------------------------------------------------

    @Override
    public final DivideByOnStep divideBy(Table<?> divisor) {
        return new DivideBy(this, divisor);
    }

    // ------------------------------------------------------------------------
    // XXX: JOIN API
    // ------------------------------------------------------------------------

    @Override
    public final TableOnStep join(TableLike<?> table) {
        return new JoinTable(this, table, JoinType.JOIN);
    }

    @Override
    public final TableOnStep join(String sql) {
        return join(table(sql));
    }

    @Override
    public final TableOnStep join(String sql, Object... bindings) {
        return join(table(sql, bindings));
    }

    @Override
    public final TableOnStep leftOuterJoin(TableLike<?> table) {
        return new JoinTable(this, table, JoinType.LEFT_OUTER_JOIN);
    }

    @Override
    public final TableOnStep leftOuterJoin(String sql) {
        return leftOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep leftOuterJoin(String sql, Object... bindings) {
        return leftOuterJoin(table(sql, bindings));
    }

    @Override
    public final TableOnStep rightOuterJoin(TableLike<?> table) {
        return new JoinTable(this, table, JoinType.RIGHT_OUTER_JOIN);
    }

    @Override
    public final TableOnStep rightOuterJoin(String sql) {
        return rightOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep rightOuterJoin(String sql, Object... bindings) {
        return rightOuterJoin(table(sql, bindings));
    }

    @Override
    public final TableOnStep fullOuterJoin(TableLike<?> table) {
        return new JoinTable(this, table, JoinType.FULL_OUTER_JOIN);
    }

    @Override
    public final TableOnStep fullOuterJoin(String sql) {
        return fullOuterJoin(table(sql));
    }

    @Override
    public final TableOnStep fullOuterJoin(String sql, Object... bindings) {
        return fullOuterJoin(table(sql, bindings));
    }

    @Override
    public final Table<Record> crossJoin(TableLike<?> table) {
        return new JoinTable(this, table, JoinType.CROSS_JOIN);
    }

    @Override
    public final Table<Record> crossJoin(String sql) {
        return crossJoin(table(sql));
    }

    @Override
    public final Table<Record> crossJoin(String sql, Object... bindings) {
        return crossJoin(table(sql, bindings));
    }

    @Override
    public final Table<Record> naturalJoin(TableLike<?> table) {
        return new JoinTable(this, table, JoinType.NATURAL_JOIN);
    }

    @Override
    public final Table<Record> naturalJoin(String sql) {
        return naturalJoin(table(sql));
    }

    @Override
    public final Table<Record> naturalJoin(String sql, Object... bindings) {
        return naturalJoin(table(sql, bindings));
    }

    @Override
    public final Table<Record> naturalLeftOuterJoin(TableLike<?> table) {
        return new JoinTable(this, table, JoinType.NATURAL_LEFT_OUTER_JOIN);
    }

    @Override
    public final Table<Record> naturalLeftOuterJoin(String sql) {
        return naturalLeftOuterJoin(table(sql));
    }

    @Override
    public final Table<Record> naturalLeftOuterJoin(String sql, Object... bindings) {
        return naturalLeftOuterJoin(table(sql, bindings));
    }

    @Override
    public final Table<Record> naturalRightOuterJoin(TableLike<?> table) {
        return new JoinTable(this, table, JoinType.NATURAL_RIGHT_OUTER_JOIN);
    }

    @Override
    public final Table<Record> naturalRightOuterJoin(String sql) {
        return naturalRightOuterJoin(table(sql));
    }

    @Override
    public final Table<Record> naturalRightOuterJoin(String sql, Object... bindings) {
        return naturalRightOuterJoin(table(sql, bindings));
    }
}
