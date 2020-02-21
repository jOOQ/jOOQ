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

import java.util.Collection;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record2;

/**
 * A general purpose record, typically used for ad-hoc types.
 * <p>
 * This type implements both the general-purpose, type-unsafe {@link Record}
 * interface, as well as the more specific, type-safe {@link Record2}
 * interfaces
 *
 * @author Lukas Eder
 */
class RecordImpl2<T1, T2> extends AbstractRecord implements InternalRecord, Record2<T1, T2> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2201346180421463830L;
    
    RecordImpl2(Field<T1> t1, Field<T2> t2) {
        super(t1, t2);
    }

    // ------------------------------------------------------------------------
    // XXX: Type-safe Record APIs
    // ------------------------------------------------------------------------

    @Override
    public RowImpl2<T1, T2> fieldsRow() {
        return new RowImpl2<>(field1(), field2());
    }

    @Override
    public final RowImpl2<T1, T2> valuesRow() {
        return new RowImpl2<>(Tools.field(value1(), field1()), Tools.field(value2(), field2()));
    }

    @Override
    public final Field<T1> field1() {
        return (Field<T1>) fields.field(0);
    }

    @Override
    public final Field<T2> field2() {
        return (Field<T2>) fields.field(1);
    }

    @Override
    public final T1 value1() {
        return (T1) get(0);
    }

    @Override
    public final T2 value2() {
        return (T2) get(1);
    }

    @Override
    public final Record2<T1, T2> value1(T1 value) {
        set(0, value);
        return this;
    }

    @Override
    public final Record2<T1, T2> value2(T2 value) {
        set(1, value);
        return this;
    }

    @Override
    public final Record2<T1, T2> values(T1 t1, T2 t2) {
        fromArray(t1, t2);
        return this;
    }

    @Override
    public final T1 component1() {
        return value1();
    }

    @Override
    public final T2 component2() {
        return value2();
    }
}
