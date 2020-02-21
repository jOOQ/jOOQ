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
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;

/**
 * A general purpose record, typically used for ad-hoc types.
 * <p>
 * This type implements both the general-purpose, type-unsafe {@link Record}
 * interface, as well as the more specific, type-safe {@link Record1}
 * interfaces
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "unchecked" })
class RecordImpl1<T1> extends AbstractRecord implements InternalRecord, Record1<T1> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2201346180421463830L;
    
    RecordImpl1(Field<T1> t1) {
        super(t1);
    }

    // ------------------------------------------------------------------------
    // XXX: Type-safe Record APIs
    // ------------------------------------------------------------------------

    @Override
    public RowImpl1<T1> fieldsRow() {
        return new RowImpl1<>(field1());
    }

    @Override
    public final RowImpl1<T1> valuesRow() {
        return new RowImpl1<>(Tools.field(value1(), field1()));
    }

    @Override
    public final Field<T1> field1() {
        return (Field<T1>) fields.field(0);
    }

    @Override
    public final T1 value1() {
        return (T1) get(0);
    }

    @Override
    public final Record1<T1> value1(T1 value) {
        set(0, value);
        return this;
    }

    @Override
    public final Record1<T1> values(T1 t1) {
        fromArray(t1);
        return this;
    }

    @Override
    public <T> Record1<T1> with(Field<T> field, T value) {
        return (Record1<T1>) super.with(field, value);
    }

    @Override
    public <T, U> Record1<T1> with(Field<T> field, U value, Converter<? extends T, ? super U> converter) {
        return (Record1<T1>) super.with(field, value, converter);
    }

    @Override
    public final T1 component1() {
        return value1();
    }
}
