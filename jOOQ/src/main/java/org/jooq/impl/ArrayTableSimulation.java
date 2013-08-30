/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
        ctx.visit(table(ctx.configuration()));
    }

    @Override
    public final void bind(BindContext ctx) throws DataAccessException {
        ctx.visit(table(ctx.configuration()));
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
