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
import org.jooq.Record6;

/**
 * A general purpose record, typically used for ad-hoc types.
 * <p>
 * This type implements both the general-purpose, type-unsafe {@link Record}
 * interface, as well as the more specific, type-safe {@link Record6}
 * interfaces
 *
 * @author Lukas Eder
 */
class RecordImpl6<T1, T2, T3, T4, T5, T6> extends AbstractRecord implements InternalRecord, Record6<T1, T2, T3, T4, T5, T6> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2201346180421463830L;
    
    RecordImpl6(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        super(t1, t2, t3, t4, t5, t6);
    }

    // ------------------------------------------------------------------------
    // XXX: Type-safe Record APIs
    // ------------------------------------------------------------------------

    @Override
    public RowImpl6<T1, T2, T3, T4, T5, T6> fieldsRow() {
        return new RowImpl6<>(field1(), field2(), field3(), field4(), field5(), field6());
    }

    @Override
    public final RowImpl6<T1, T2, T3, T4, T5, T6> valuesRow() {
        return new RowImpl6<>(Tools.field(value1(), field1()), Tools.field(value2(), field2()), Tools.field(value3(), field3()), Tools.field(value4(), field4()), Tools.field(value5(), field5()), Tools.field(value6(), field6()));
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
    public final Field<T3> field3() {
        return (Field<T3>) fields.field(2);
    }

    @Override
    public final Field<T4> field4() {
        return (Field<T4>) fields.field(3);
    }

    @Override
    public final Field<T5> field5() {
        return (Field<T5>) fields.field(4);
    }

    @Override
    public final Field<T6> field6() {
        return (Field<T6>) fields.field(5);
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
    public final T3 value3() {
        return (T3) get(2);
    }

    @Override
    public final T4 value4() {
        return (T4) get(3);
    }

    @Override
    public final T5 value5() {
        return (T5) get(4);
    }

    @Override
    public final T6 value6() {
        return (T6) get(5);
    }

    @Override
    public final Record6<T1, T2, T3, T4, T5, T6> value1(T1 value) {
        set(0, value);
        return this;
    }

    @Override
    public final Record6<T1, T2, T3, T4, T5, T6> value2(T2 value) {
        set(1, value);
        return this;
    }

    @Override
    public final Record6<T1, T2, T3, T4, T5, T6> value3(T3 value) {
        set(2, value);
        return this;
    }

    @Override
    public final Record6<T1, T2, T3, T4, T5, T6> value4(T4 value) {
        set(3, value);
        return this;
    }

    @Override
    public final Record6<T1, T2, T3, T4, T5, T6> value5(T5 value) {
        set(4, value);
        return this;
    }

    @Override
    public final Record6<T1, T2, T3, T4, T5, T6> value6(T6 value) {
        set(5, value);
        return this;
    }

    @Override
    public final Record6<T1, T2, T3, T4, T5, T6> values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        fromArray(t1, t2, t3, t4, t5, t6);
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

    @Override
    public final T3 component3() {
        return value3();
    }

    @Override
    public final T4 component4() {
        return value4();
    }

    @Override
    public final T5 component5() {
        return value5();
    }

    @Override
    public final T6 component6() {
        return value6();
    }
}
