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

import static org.jooq.impl.Tools.DataKey.DATA_LIST_ALREADY_INDENTED;

import org.jooq.Context;
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.Field;
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
    private final Field<?>[]  emulatedFields;

    RowField(ROW row) {
        this(row, "row");
    }

    @SuppressWarnings({ "serial", "unchecked", "rawtypes" })
    RowField(final ROW row, String as) {
        super(as, (DataType) SQLDataType.RECORD, "", new DefaultBinding<Object, REC>(new Converter<Object, REC>() {
            @Override
            public REC from(final Object t) {
                // So far, this is only supported for PostgreSQL
                return (REC) (t == null ? null : DefaultBinding.pgNewRecord(Record.class, row.fields(), t));
            }

            @Override
            public Object to(REC u) {
                throw new UnsupportedOperationException("Converting from nested records to bind values is not yet supported");
            }

            @Override
            public Class<Object> fromType() {
                return Object.class;
            }

            @Override
            public Class<REC> toType() {
                return (Class<REC>) RecordImpl.class;
            }
        }));

        this.row = row;
        this.emulatedFields = new Field[row.fields().length];

        for (int i = 0; i < emulatedFields.length; i++)
            emulatedFields[i] = row.field(i).as(as + "." + row.field(i).getName());
    }

    Field<?>[] emulatedFields() {
        return emulatedFields;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ctx.declareFields()) {
            Object previous = ctx.data(DATA_LIST_ALREADY_INDENTED);

            ctx.data(DATA_LIST_ALREADY_INDENTED, true);
            ctx.visit(new SelectFieldList(emulatedFields()));
            ctx.data(DATA_LIST_ALREADY_INDENTED, previous);
        }
        else {
            ctx.visit(row);
        }
    }

    @Override
    public Field<REC> as(String alias) {
        return new RowField<ROW, REC>(row, alias);
    }

    @Override
    public boolean declaresFields() {
        return true;
    }
}
