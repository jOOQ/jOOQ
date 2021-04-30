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

import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;

import org.jooq.Context;
import org.jooq.EmbeddableRecord;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * @author Lukas Eder
 */
final class EmbeddableTableField<R extends Record, E extends EmbeddableRecord<E>>
extends AbstractField<E>
implements TableField<R, E> {
    final Class<E>            recordType;
    final boolean             replacesFields;
    final Table<R>            table;
    final AbstractRow<?>      fieldsRow;
    /**
     * @deprecated - [#11058] - 3.14.5 - This will be removed in the future.
     */
    @Deprecated
    final TableField<R, ?>[]  fields;


    EmbeddableTableField(Name name, Class<E> recordType, boolean replacesFields, Table<R> table, TableField<R, ?>[] fields) {
        super(name, new DefaultDataType<>(SQLDialect.DEFAULT, recordType, name.last()));

        this.recordType = recordType;
        this.replacesFields = replacesFields;
        this.table = table;
        this.fieldsRow = Tools.row0(fields);
        this.fields = fields;





    }

    // -------------------------------------------------------------------------
    // TableField API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.data(DATA_LIST_ALREADY_INDENTED, true, c -> c.visit(wrap(fieldsRow.fields.fields)));
    }

    @Override
    public final Table<R> getTable() {
        return table;
    }
}
