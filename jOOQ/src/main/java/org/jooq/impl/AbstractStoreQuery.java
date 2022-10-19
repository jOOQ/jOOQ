/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import java.util.Map;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.FieldOrRow;
import org.jooq.FieldOrRowOrSelect;
import org.jooq.Record;
import org.jooq.StoreQuery;
import org.jooq.Table;
import org.jooq.impl.QOM.UTransient;

/**
 * A default implementation for store queries.
 *
 * @author Lukas Eder
 */
abstract class AbstractStoreQuery<R extends Record, K extends FieldOrRow, V extends FieldOrRowOrSelect>
extends
    AbstractDMLQuery<R>
implements
    StoreQuery<R>
{

    AbstractStoreQuery(Configuration configuration, WithImpl with, Table<R> table) {
        super(configuration, with, table);
    }

    protected abstract Map<K, V> getValues();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final void setRecord(R record) {
        for (int i = 0; i < record.size(); i++)
            if (record.changed(i))
                addValue((Field) record.field(i), record.get(i));
    }

    @Override
    public final <T> void addValue(Field<T> field, T value) {
        addValue(field, -1, value);
    }

    @Override
    public final <T> void addValue(Field<T> field, Field<T> value) {
        addValue(field, -1, value);
    }

    final <T> void addValue(Field<T> field, int index, T value) {
        if (field == null)
            if (index >= 0)
                addValue(new UnknownField<T>(index), value);
            else
                addValue(new UnknownField<T>(getValues().size()), value);
        else
            getValues().put((K) field, (V) Tools.field(value, field));
    }

    final <T> void addValue(Field<T> field, int index, Field<T> value) {
        if (field == null)
            if (index >= 0)
                addValue(new UnknownField<T>(index), value);
            else
                addValue(new UnknownField<T>(getValues().size()), value);
        else
            getValues().put((K) field, (V) Tools.field(value, field));
    }

    static class UnknownField<T> extends AbstractField<T> implements UTransient {
        private final int index;

        @SuppressWarnings({ "rawtypes", "unchecked" })
        UnknownField(int index) {
            super(DSL.name("unknown field " + index), (DataType) SQLDataType.OTHER);

            this.index = index;
        }

        @Override
        public void accept(Context<?> ctx) {
            ctx.visit(getUnqualifiedName());
        }

        @Override
        public int hashCode() {
            return index;
        }

        @Override
        public boolean equals(Object that) {
            if (that instanceof UnknownField<?> f)
                return index == f.index;
            else
                return false;
        }
    }
}
