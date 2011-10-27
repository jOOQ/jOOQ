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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.Table;

/**
 * A common base class for all <code>SELECT</code> statements.
 *
 * @author Lukas Eder
 */
abstract class AbstractSelect<R extends Record> extends AbstractResultQuery<R> implements Select<R> {

    private static final long       serialVersionUID = 5432006637149005588L;

    AbstractSelect(Configuration configuration) {
        super(configuration);
    }

    @Override
    public final Select<R> union(Select<R> select) {
        return new Union<R>(getConfiguration(), this, select, CombineOperator.UNION);
    }

    @Override
    public final Select<R> unionAll(Select<R> select) {
        return new Union<R>(getConfiguration(), this, select, CombineOperator.UNION_ALL);
    }

    @Override
    public final Select<R> except(Select<R> select) {
        return new Union<R>(getConfiguration(), this, select, CombineOperator.EXCEPT);
    }

    @Override
    public final Select<R> intersect(Select<R> select) {
        return new Union<R>(getConfiguration(), this, select, CombineOperator.INTERSECT);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> Field<T> asField() {
        if (getSelect().size() != 1) {
            throw new IllegalStateException("Can only use single-column ResultProviderQuery as a field");
        }

        return new SelectQueryAsField<T>(this, (DataType<T>) getSelect().get(0).getDataType());
    }

    @Override
    public final <T> Field<T> asField(String alias) {
        return this.<T> asField().as(alias);
    }

    @Override
    public final <T> Field<T> getField(Field<T> field) {
        return asTable().getField(field);
    }

    @Override
    public final Field<?> getField(String name) {
        return asTable().getField(name);
    }

    @Override
    public final Field<?> getField(int index) {
        return asTable().getField(index);
    }

    @Override
    public final List<Field<?>> getFields() {
        return asTable().getFields();
    }

    @Override
    public final int getIndex(Field<?> field) throws IllegalArgumentException {
        return asTable().getIndex(field);
    }

    @Override
    public final Table<R> asTable() {
        // Its usually better to alias nested selects that are used in
        // the FROM clause of a query
        return new SelectQueryAsTable<R>(this).as("alias_" + Math.abs(hashCode()));
    }

    @Override
    public final Table<R> asTable(String alias) {
        return new SelectQueryAsTable<R>(this).as(alias);
    }

    @Override
    protected final List<Field<?>> getFields(ResultSetMetaData meta) throws SQLException {
    	List<Field<?>> select = getSelect();

    	// If no projection was specified explicitly, create fields from result
    	// set meta data instead. This is typically the case for SELECT * ...
    	if (select.isEmpty()) {
            Configuration configuration = getConfiguration();
            return new MetaDataFieldProvider(configuration, meta).getFields();
        }

        return select;
    }

    @Override
    final boolean isSelectingRefCursor() {
        for (Field<?> field : getSelect()) {
            if (Result.class.isAssignableFrom(field.getType())) {
                return true;
            }
        }

        return false;
    }
}
