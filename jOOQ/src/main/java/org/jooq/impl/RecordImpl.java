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

import java.util.Collection;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
import org.jooq.Record10;
import org.jooq.Record11;
import org.jooq.Record12;
import org.jooq.Record13;
import org.jooq.Record14;
import org.jooq.Record15;
import org.jooq.Record16;
import org.jooq.Record17;
import org.jooq.Record18;
import org.jooq.Record19;
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;

/**
 * A general purpose record, typically used for ad-hoc types.
 * <p>
 * This type implements both the general-purpose, type-unsafe {@link Record}
 * interface, as well as the more specific, type-safe {@link Record1},
 * {@link Record2} through {@link Record22} interfaces
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
@SuppressWarnings({ "unchecked", "rawtypes" })
class RecordImpl<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> extends AbstractRecord
implements

    // This record implementation implements all record types. Type-safety is
    // being checked through the type-safe API. No need for further checks here
    Record1<T1>,
    Record2<T1, T2>,
    Record3<T1, T2, T3>,
    Record4<T1, T2, T3, T4>,
    Record5<T1, T2, T3, T4, T5>,
    Record6<T1, T2, T3, T4, T5, T6>,
    Record7<T1, T2, T3, T4, T5, T6, T7>,
    Record8<T1, T2, T3, T4, T5, T6, T7, T8>,
    Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>,
    Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
    Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
    Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
    Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
    Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
    Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
    Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
    Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
    Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
    Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
    Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>,
    Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>,
    Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2201346180421463830L;

    /**
     * Create a new general purpose record.
     */
    public RecordImpl(Field<?>... fields) {
        super(fields);
    }

    /**
     * Create a new general purpose record.
     */
    public RecordImpl(Collection<? extends Field<?>> fields) {
        super(fields);
    }

    /**
     * Create a new general purpose record.
     */
    RecordImpl(RowImpl fields) {
        super(fields);
    }

    // ------------------------------------------------------------------------
    // XXX: Type-safe Record APIs
    // ------------------------------------------------------------------------

    @Override
    public RowImpl<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> fieldsRow() {
        return fields;
    }

    @Override
    public final RowImpl<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> valuesRow() {
        return new RowImpl(Tools.fields(intoArray(), fields.fields()));
    }

    @Override
    public final Field<T1> field1() {
        return fields.field(0);
    }

    @Override
    public final Field<T2> field2() {
        return fields.field(1);
    }

    @Override
    public final Field<T3> field3() {
        return fields.field(2);
    }

    @Override
    public final Field<T4> field4() {
        return fields.field(3);
    }

    @Override
    public final Field<T5> field5() {
        return fields.field(4);
    }

    @Override
    public final Field<T6> field6() {
        return fields.field(5);
    }

    @Override
    public final Field<T7> field7() {
        return fields.field(6);
    }

    @Override
    public final Field<T8> field8() {
        return fields.field(7);
    }

    @Override
    public final Field<T9> field9() {
        return fields.field(8);
    }

    @Override
    public final Field<T10> field10() {
        return fields.field(9);
    }

    @Override
    public final Field<T11> field11() {
        return fields.field(10);
    }

    @Override
    public final Field<T12> field12() {
        return fields.field(11);
    }

    @Override
    public final Field<T13> field13() {
        return fields.field(12);
    }

    @Override
    public final Field<T14> field14() {
        return fields.field(13);
    }

    @Override
    public final Field<T15> field15() {
        return fields.field(14);
    }

    @Override
    public final Field<T16> field16() {
        return fields.field(15);
    }

    @Override
    public final Field<T17> field17() {
        return fields.field(16);
    }

    @Override
    public final Field<T18> field18() {
        return fields.field(17);
    }

    @Override
    public final Field<T19> field19() {
        return fields.field(18);
    }

    @Override
    public final Field<T20> field20() {
        return fields.field(19);
    }

    @Override
    public final Field<T21> field21() {
        return fields.field(20);
    }

    @Override
    public final Field<T22> field22() {
        return fields.field(21);
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
    public final T7 value7() {
        return (T7) get(6);
    }

    @Override
    public final T8 value8() {
        return (T8) get(7);
    }

    @Override
    public final T9 value9() {
        return (T9) get(8);
    }

    @Override
    public final T10 value10() {
        return (T10) get(9);
    }

    @Override
    public final T11 value11() {
        return (T11) get(10);
    }

    @Override
    public final T12 value12() {
        return (T12) get(11);
    }

    @Override
    public final T13 value13() {
        return (T13) get(12);
    }

    @Override
    public final T14 value14() {
        return (T14) get(13);
    }

    @Override
    public final T15 value15() {
        return (T15) get(14);
    }

    @Override
    public final T16 value16() {
        return (T16) get(15);
    }

    @Override
    public final T17 value17() {
        return (T17) get(16);
    }

    @Override
    public final T18 value18() {
        return (T18) get(17);
    }

    @Override
    public final T19 value19() {
        return (T19) get(18);
    }

    @Override
    public final T20 value20() {
        return (T20) get(19);
    }

    @Override
    public final T21 value21() {
        return (T21) get(20);
    }

    @Override
    public final T22 value22() {
        return (T22) get(21);
    }

    @Override
    public final RecordImpl value1(T1 value) {
        set(0, value);
        return this;
    }

    @Override
    public final RecordImpl value2(T2 value) {
        set(1, value);
        return this;
    }

    @Override
    public final RecordImpl value3(T3 value) {
        set(2, value);
        return this;
    }

    @Override
    public final RecordImpl value4(T4 value) {
        set(3, value);
        return this;
    }

    @Override
    public final RecordImpl value5(T5 value) {
        set(4, value);
        return this;
    }

    @Override
    public final RecordImpl value6(T6 value) {
        set(5, value);
        return this;
    }

    @Override
    public final RecordImpl value7(T7 value) {
        set(6, value);
        return this;
    }

    @Override
    public final RecordImpl value8(T8 value) {
        set(7, value);
        return this;
    }

    @Override
    public final RecordImpl value9(T9 value) {
        set(8, value);
        return this;
    }

    @Override
    public final RecordImpl value10(T10 value) {
        set(9, value);
        return this;
    }

    @Override
    public final RecordImpl value11(T11 value) {
        set(10, value);
        return this;
    }

    @Override
    public final RecordImpl value12(T12 value) {
        set(11, value);
        return this;
    }

    @Override
    public final RecordImpl value13(T13 value) {
        set(12, value);
        return this;
    }

    @Override
    public final RecordImpl value14(T14 value) {
        set(13, value);
        return this;
    }

    @Override
    public final RecordImpl value15(T15 value) {
        set(14, value);
        return this;
    }

    @Override
    public final RecordImpl value16(T16 value) {
        set(15, value);
        return this;
    }

    @Override
    public final RecordImpl value17(T17 value) {
        set(16, value);
        return this;
    }

    @Override
    public final RecordImpl value18(T18 value) {
        set(17, value);
        return this;
    }

    @Override
    public final RecordImpl value19(T19 value) {
        set(18, value);
        return this;
    }

    @Override
    public final RecordImpl value20(T20 value) {
        set(19, value);
        return this;
    }

    @Override
    public final RecordImpl value21(T21 value) {
        set(20, value);
        return this;
    }

    @Override
    public final RecordImpl value22(T22 value) {
        set(21, value);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1) {
        fromArray(t1);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2) {
        fromArray(t1, t2);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3) {
        fromArray(t1, t2, t3);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4) {
        fromArray(t1, t2, t3, t4);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        fromArray(t1, t2, t3, t4, t5);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        fromArray(t1, t2, t3, t4, t5, t6);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        fromArray(t1, t2, t3, t4, t5, t6, t7);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
        return this;
    }

    @Override
    public final RecordImpl values(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        fromArray(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
        return this;
    }
}
