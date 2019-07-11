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
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;

import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * @author Lukas Eder
 */
final class EmbeddableTableField<R extends Record, T extends Record> extends AbstractField<T> implements TableField<R, T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7105430856294526440L;
    final Table<R>            table;
    final TableField<R, ?>[]  fields;
    final Class<T>            recordType;

    EmbeddableTableField(Name name, Class<T> recordType, Table<R> table, TableField<R, ?>[] fields) {
        super(name, new DefaultDataType<>(SQLDialect.DEFAULT, recordType, name.last()));

        this.table = table;
        this.recordType = recordType;
        this.fields = fields;
    }

    // -------------------------------------------------------------------------
    // TableField API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        Object previous = ctx.data(DATA_LIST_ALREADY_INDENTED);

        ctx.data(DATA_LIST_ALREADY_INDENTED, true);
        ctx.visit(new QueryPartList<>(fields));
        ctx.data(DATA_LIST_ALREADY_INDENTED, previous);
    }

    @Override
    public final Table<R> getTable() {
        return table;
    }
}
