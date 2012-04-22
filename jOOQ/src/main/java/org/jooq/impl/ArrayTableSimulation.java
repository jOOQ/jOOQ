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

import static org.jooq.impl.Factory.falseCondition;
import static org.jooq.impl.Factory.one;

import java.util.Collections;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;

/**
 * Essentially, this is the same as {@link ArrayTable}, except that it simulates
 * unnested arrays using <code>UNION ALL</code>
 *
 * @author Lukas Eder
 */
class ArrayTableSimulation extends AbstractTable<Record> {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = 2392515064450536343L;

    private final Object[]          array;
    private final FieldList         field;
    private final String            alias;

    private transient Table<Record> table;

    ArrayTableSimulation(Object[] array) {
        this(array, "array_table");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    ArrayTableSimulation(Object[] array, String alias) {
        super(alias);

        this.array = array;
        this.field = new FieldList();
        this.alias = alias;
        this.field.add(new Qualifier(Factory.getDataType(array.getClass().getComponentType()), alias, "COLUMN_VALUE"));
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final Table<Record> as(String as) {
        return new ArrayTableSimulation(array, as);
    }

    @Override
    public final boolean declaresTables() {

        // [#1055] Always true, because unnested tables are always aliased.
        // This is particularly important for simulated unnested arrays
        return true;
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.sql(table(context));
    }

    @Override
    public final void bind(BindContext context) throws DataAccessException {
        context.bind(table(context));
    }

    @Override
    protected final FieldList getFieldList() {
        return field;
    }

    @Override
    protected final List<Attachable> getAttachables0() {
        return Collections.emptyList();
    }

    private final Table<Record> table(Configuration configuration) {
        if (table == null) {
            Select<Record> select = null;

            for (Object element : array) {

                // [#1081] Be sure to get the correct cast type also for null
                Field<?> val = Factory.val(element, field.get(0).getDataType());
                Select<Record> subselect = create(configuration).select(val.as("COLUMN_VALUE"));

                if (select == null) {
                    select = subselect;
                }
                else {
                    select = select.unionAll(subselect);
                }
            }

            // Empty arrays should result in empty tables
            if (select == null) {
                select = create(configuration).select(one().as("COLUMN_VALUE")).where(falseCondition());
            }

            table = select.asTable(alias);
        }

        return table;
    }
}
