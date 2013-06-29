/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.using;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;

/**
 * Essentially, this is the same as <code>ArrayTable</code>, except that it simulates
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
    private final Fields<Record>    field;
    private final String            alias;
    private final String            fieldAlias;

    private transient Table<Record> table;

    ArrayTableSimulation(Object[] array) {
        this(array, "array_table", null);
    }

    ArrayTableSimulation(Object[] array, String alias) {
        this(array, alias, null);
    }

    ArrayTableSimulation(Object[] array, String alias, String fieldAlias) {
        super(alias);

        this.array = array;
        this.alias = alias;
        this.fieldAlias = fieldAlias == null ? "COLUMN_VALUE" : fieldAlias;
        this.field = new Fields<Record>(fieldByName(DSL.getDataType(array.getClass().getComponentType()), alias, this.fieldAlias));
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
    public final Table<Record> as(String as, String... fieldAliases) {
        if (fieldAliases == null) {
            return new ArrayTableSimulation(array, as);
        }
        else if (fieldAliases.length == 1) {
            return new ArrayTableSimulation(array, as, fieldAliases[0]);
        }

        throw new IllegalArgumentException("Array table simulations can only have a single field alias");
    }

    @Override
    public final boolean declaresTables() {

        // [#1055] Always true, because unnested tables are always aliased.
        // This is particularly important for simulated unnested arrays
        return true;
    }

    @Override
    public final void toSQL(RenderContext ctx) {
        ctx.sql(table(ctx.configuration()));
    }

    @Override
    public final void bind(BindContext ctx) throws DataAccessException {
        ctx.bind(table(ctx.configuration()));
    }

    @Override
    final Fields<Record> fields0() {
        return field;
    }

    private final Table<Record> table(Configuration configuration) {
        if (table == null) {
            Select<Record> select = null;

            for (Object element : array) {

                // [#1081] Be sure to get the correct cast type also for null
                Field<?> val = DSL.val(element, field.fields[0].getDataType());
                Select<Record> subselect = using(configuration).select(val.as("COLUMN_VALUE")).select();

                if (select == null) {
                    select = subselect;
                }
                else {
                    select = select.unionAll(subselect);
                }
            }

            // Empty arrays should result in empty tables
            if (select == null) {
                select = using(configuration).select(one().as("COLUMN_VALUE")).select().where(falseCondition());
            }

            table = select.asTable(alias);
        }

        return table;
    }
}
