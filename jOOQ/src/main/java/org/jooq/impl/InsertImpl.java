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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.Attachable;
import org.jooq.AttachableInternal;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.InsertOnDuplicateSetMoreStep;
import org.jooq.InsertQuery;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStep;
import org.jooq.RenderContext;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.exception.DetachedException;

/**
 * @author Lukas Eder
 */
class InsertImpl<R extends TableRecord<R>> extends AbstractQueryPart implements

    // Cascading interface implementations for Insert behaviour
    InsertValuesStep,
    InsertSetMoreStep,
    InsertOnDuplicateSetMoreStep {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 4222898879771679107L;

    private final InsertQuery<R> delegate;
    private final List<Field<?>> fields;
    private boolean              onDuplicateKeyUpdate;

    InsertImpl(Configuration configuration, Table<R> into, Collection<? extends Field<?>> fields) {
        this.delegate = new InsertQueryImpl<R>(configuration, into);
        this.fields = new ArrayList<Field<?>>(fields);
    }

    // -------------------------------------------------------------------------
    // The QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void toSQL(RenderContext context) {
        context.sql(delegate);
    }

    @Override
    public final void bind(BindContext context) throws SQLException {
        context.bind(delegate);
    }

    @Override
    public final List<Attachable> getAttachables() {
        return internalAPI(AttachableInternal.class).getAttachables();
    }

    @Override
    public final int execute() throws SQLException, DetachedException {
        return delegate.execute();
    }

    // -------------------------------------------------------------------------
    // The DSL API
    // -------------------------------------------------------------------------

    @Override
    public final InsertImpl<R> values(Object... values) {
        return values0(create().vals(values));
    }

    @Override
    public final InsertImpl<R> values(Field<?>... values) {
        return values0(Arrays.asList(values));
    }

    @Override
    public final InsertImpl<R> values(Collection<?> values) {
        return values0(create().vals(values.toArray()));
    }

    private final InsertImpl<R> values0(List<Field<?>> values) {
        if (fields.size() != values.size()) {
            throw new IllegalArgumentException("The number of values must match the number of fields");
        }

        delegate.newRecord();
        for (int i = 0; i < fields.size(); i++) {
            delegate.addValue(fields.get(i), values.get(i));
        }

        return this;
    }

    @Override
    public final InsertImpl<R> onDuplicateKeyUpdate() {
        onDuplicateKeyUpdate = true;
        delegate.onDuplicateKeyUpdate(true);
        return this;
    }

    @Override
    public final InsertImpl<R> set(Field<?> field, Object value) {
        if (onDuplicateKeyUpdate) {
            delegate.addValueForUpdate(field, value);
        }
        else {
            delegate.addValue(field, value);
        }

        return this;
    }

    @Override
    public final InsertImpl<R> set(Field<?> field, Field<?> value) {
        if (onDuplicateKeyUpdate) {
            delegate.addValueForUpdate(field, value);
        }
        else {
            delegate.addValue(field, value);
        }

        return this;
    }

    @Override
    public final InsertImpl<R> set(Map<? extends Field<?>, ?> map) {
        if (onDuplicateKeyUpdate) {
            delegate.addValuesForUpdate(map);
        }
        else {
            delegate.addValues(map);
        }

        return this;
    }

    @Override
    public final InsertSetStep newRecord() {
        delegate.newRecord();
        return this;
    }
}
