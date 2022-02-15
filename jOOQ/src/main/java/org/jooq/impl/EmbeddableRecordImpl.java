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

import static org.jooq.impl.Tools.fieldsArray;
import static org.jooq.impl.Tools.row0;

import org.jooq.Converter;
import org.jooq.EmbeddableRecord;
import org.jooq.Field;
import org.jooq.Row;
import org.jooq.TableField;

/**
 * A record implementation for a record originating from a single table
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class EmbeddableRecordImpl<R extends EmbeddableRecord<R>> extends AbstractRecord implements EmbeddableRecord<R> {

    /**
     * @deprecated - [#11058] - 3.14.5 - Please re-generate your code.
     */
    @Deprecated
    public EmbeddableRecordImpl(Field<?>... fields) {
        super(fields);
    }

    /**
     * @deprecated - [#11058] - 3.14.5 - Please re-generate your code.
     */
    @Deprecated
    public EmbeddableRecordImpl(TableField<?, ?>... fields) {
        super(fields);
    }

    public EmbeddableRecordImpl(Row fields) {
        super((AbstractRow<?>) fields);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> R with(Field<T> field, T value) {
        return (R) super.with(field, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T, U> R with(Field<T> field, U value, Converter<? extends T, ? super U> converter) {
        return (R) super.with(field, value, converter);
    }

    /*
     * Subclasses may override this method
     */
    @Override
    public Row fieldsRow() {
        return fields;
    }

    /*
     * Subclasses may override this method
     */
    @Override
    public Row valuesRow() {
        return row0(fieldsArray(intoArray(), fields.fields.fields()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final R original() {
        return (R) super.original();
    }
}
