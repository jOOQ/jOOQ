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

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.UpdatableTable;

/**
 * A record implementation for a record holding a primary key
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class UpdatableRecordImpl<R extends UpdatableRecord<R>> extends TableRecordImpl<R> implements UpdatableRecord<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -1012420583600561579L;

    public UpdatableRecordImpl(UpdatableTable<R> table) {
        super(table);
    }

    @Override
    public final UpdatableTable<R> getTable() {
        return (UpdatableTable<R>) super.getTable();
    }

    @Override
    final UniqueKey<R> getMainKey() {
        return getTable().getMainKey();
    }

    @Override
    public final int store() {
        return storeUsing(getMainKey().getFieldsArray());
    }

    @Override
    public final int delete() {
        return deleteUsing(getMainKey().getFieldsArray());
    }

    @Override
    public final void refresh() {
        refreshUsing(getMainKey().getFieldsArray());
    }

    @Override
    Collection<Field<?>> getReturning() {
        Collection<Field<?>> result = super.getReturning();
        result.addAll(getMainKey().getFields());
        return result;
    }

    @Override
    public final R copy() {
        R copy = create().newRecord(getTable());

        // Copy all fields. This marks them all as isChanged, which is important
        for (Field<?> field : getFields()) {
            setValue(copy, field);
        }

        // Remove key values again
        for (Field<?> field : getMainKey().getFields()) {
            copy.setValue(field, null);
        }

        return copy;
    }

    /**
     * Extracted method to ensure generic type safety.
     */
    private final <T> void setValue(Record record, Field<T> field) {
        record.setValue(field, getValue(field));
    }
}
