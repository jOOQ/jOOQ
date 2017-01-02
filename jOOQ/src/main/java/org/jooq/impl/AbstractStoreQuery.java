/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
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
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.StoreQuery;
import org.jooq.Table;

/**
 * A default implementation for store queries.
 *
 * @author Lukas Eder
 */
abstract class AbstractStoreQuery<R extends Record> extends AbstractDMLQuery<R> implements StoreQuery<R> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 6864591335823160569L;

    AbstractStoreQuery(Configuration configuration, WithImpl with, Table<R> table) {
        super(configuration, with, table);
    }

    protected abstract Map<Field<?>, Field<?>> getValues();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final void setRecord(R record) {
        for (int i = 0; i < record.size(); i++)
            if (record.changed(i))
                addValue((Field) record.field(i), record.get(i));
    }

    final <T> void addValue(R record, Field<T> field) {
        addValue(field, record.get(field));
    }

    @Override
    public final <T> void addValue(Field<T> field, T value) {
        getValues().put(field, Tools.field(value, field));
    }

    @Override
    public final <T> void addValue(Field<T> field, Field<T> value) {
        getValues().put(field, Tools.field(value, field));
    }
}
