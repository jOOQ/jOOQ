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

import org.jooq.Converter;
import org.jooq.EmbeddableRecord;
import org.jooq.Field;
import org.jooq.Row;

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
     * Generated UID
     */
    private static final long serialVersionUID = -1260149220986944763L;

    public EmbeddableRecordImpl(Field<?>... fields) {
        super(fields);
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Row valuesRow() {
        return new RowImpl(Tools.fields(intoArray(), fields.fields.fields()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final R original() {
        return (R) super.original();
    }
}
