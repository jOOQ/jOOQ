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

import static org.jooq.impl.Factory.vals;

import java.util.Collection;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.PivotForStep;
import org.jooq.PivotInStep;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;

/**
 * A pivot table implementation
 * <p>
 * Future versions of jOOQ could simulate Oracle's <code>PIVOT</code> clause by
 * rendering a subquery instead.
 *
 * @author Lukas Eder
 */
class Pivot<T>
extends AbstractTable<Record>
implements
    PivotForStep,
    PivotInStep<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7918219502110473521L;

    private final Table<?>    table;
    private final FieldList   aggregateFunctions;
    private Field<T>          on;
    private FieldList         in;

    Pivot(Table<?> table, Field<?>... aggregateFunctions) {
        super("pivot");

        this.table = table;
        this.aggregateFunctions = new SelectFieldList(aggregateFunctions);
    }

    // ------------------------------------------------------------------------
    // Table API
    // ------------------------------------------------------------------------

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final void toSQL(RenderContext context) {

        // Bind variables are not allowed inside of PIVOT clause
        boolean inline = context.inline();
        boolean declare = context.declareFields();

        context.sql(table)
               .sql(" pivot(")
               .inline(true)
               .declareFields(true)
               .sql(aggregateFunctions)
               .sql(" for ")
               .literal(on.getName())
               .sql(" in (")
               .sql(in)
               .declareFields(declare)
               .inline(inline)
               .sql("))");
    }

    @Override
    public final boolean declaresTables() {
        return table.internalAPI(QueryPartInternal.class).declaresTables();
    }

    @Override
    public final void bind(BindContext context) throws DataAccessException {
        context.bind(table);
    }

    @Override
    public final Table<Record> as(String alias) {
        return new TableAlias<Record>(this, alias);
    }

    @Override
    protected final FieldList getFieldList() {
        return new FieldList();
    }

    @Override
    protected final List<Attachable> getAttachables0() {
        return getAttachables(table, aggregateFunctions, on, in);
    }

    // ------------------------------------------------------------------------
    // Pivot API
    // ------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final <Z> Pivot<Z> on(Field<Z> field) {
        // The previous bound of <T> is Object, but that's irrelevant
        this.on = (Field<T>) field;
        return (Pivot<Z>) this;
    }

    @Override
    public final Table<Record> in(Object... values) {
        return in(vals(values).toArray(new Field<?>[0]));
    }

    @Override
    public final Table<Record> in(Field<?>... f) {
        this.in = new SelectFieldList(f);
        return this;
    }

    @Override
    public final Table<Record> in(Collection<? extends Field<T>> f) {
        return in(f.toArray(new Field[0]));
    }
}
