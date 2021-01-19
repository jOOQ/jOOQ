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

import static org.jooq.impl.DefaultBinding.binding;
import static org.jooq.impl.DefaultBinding.DefaultRecordBinding.pgNewRecord;
import static org.jooq.impl.Names.N_ROW;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row;

/**
 * @author Lukas Eder
 */
final class RowField<ROW extends Row, REC extends Record> extends AbstractField<REC> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2065258332642911588L;

    private final ROW         row;
    private final AbstractRow emulatedFields;

    RowField(ROW row) {
        this(row, N_ROW);
    }

    @SuppressWarnings({ "serial", "unchecked", "rawtypes" })
    RowField(final ROW row, Name as) {
        super(as, (DataType) SQLDataType.RECORD, CommentImpl.NO_COMMENT, binding(new AbstractConverter<Object, REC>(
                Object.class, (Class<REC>) Tools.recordType(row.size())
        ) {
            @Override
            public REC from(final Object t) {
                // So far, this is only supported for PostgreSQL
                return (REC) (t == null ? null : pgNewRecord(Record.class, (AbstractRow) row, t));
            }

            @Override
            public Object to(REC u) {
                throw new UnsupportedOperationException("Converting from nested records to bind values is not yet supported");
            }
        }));

        Field<?>[] f = new Field[row.size()];
        for (int i = 0; i < f.length; i++)
            f[i] = row.field(i).as(as + "." + row.field(i).getName());

        this.row = row;
        this.emulatedFields = Tools.row0(f);
    }

    AbstractRow emulatedFields() {
        return emulatedFields;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ctx.declareFields())
            ctx.data(DATA_LIST_ALREADY_INDENTED, true, c -> c.visit(new SelectFieldList<>(emulatedFields.fields.fields)));
        else
            ctx.visit(row);
    }

    @Override
    public Field<REC> as(Name alias) {
        return new RowField<>(row, alias);
    }

    @Override
    public boolean declaresFields() {
        return true;
    }
}
