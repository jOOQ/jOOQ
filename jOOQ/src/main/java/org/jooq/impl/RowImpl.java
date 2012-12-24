/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import static org.jooq.impl.Factory.row;
import static org.jooq.impl.Factory.vals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.BindContext;
import org.jooq.Comparator;
import org.jooq.Condition;
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
import org.jooq.RenderContext;
import org.jooq.Row;
import org.jooq.Row1;
import org.jooq.Row2;
import org.jooq.Row3;
import org.jooq.Row4;
import org.jooq.Row5;
import org.jooq.Row6;
import org.jooq.Row7;
import org.jooq.Row8;
import org.jooq.Row9;
import org.jooq.Row10;
import org.jooq.Row11;
import org.jooq.Row12;
import org.jooq.Row13;
import org.jooq.Row14;
import org.jooq.Row15;
import org.jooq.Row16;
import org.jooq.Row17;
import org.jooq.Row18;
import org.jooq.Row19;
import org.jooq.Row20;
import org.jooq.Row21;
import org.jooq.Row22;
import org.jooq.RowN;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
@SuppressWarnings({ "rawtypes", "unchecked" })
class RowImpl<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> extends AbstractQueryPart
implements

    // This row implementation implements all row types. Type-safety is
    // being checked through the type-safe API. No need for further checks here
    Row1<T1>,
    Row2<T1, T2>,
    Row3<T1, T2, T3>,
    Row4<T1, T2, T3, T4>,
    Row5<T1, T2, T3, T4, T5>,
    Row6<T1, T2, T3, T4, T5, T6>,
    Row7<T1, T2, T3, T4, T5, T6, T7>,
    Row8<T1, T2, T3, T4, T5, T6, T7, T8>,
    Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9>,
    Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
    Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
    Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
    Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
    Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
    Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
    Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
    Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
    Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
    Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
    Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>,
    Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>,
    Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>,
    RowN {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -929427349071556318L;

    private final Field<?>[]  fields;

    RowImpl(Field<?>... fields) {
        super();

        this.fields = fields;
    }

    RowImpl(Collection<? extends Field<?>> fields) {
        this(fields.toArray(new Field[fields.size()]));
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void toSQL(RenderContext context) {
        context.sql("(");

        String separator = "";
        for (Field<?> field : fields) {
            context.sql(separator);
            context.sql(field);

            separator = ", ";
        }

        context.sql(")");
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(fields);
    }

    // ------------------------------------------------------------------------
    // XXX: Row accessor API
    // ------------------------------------------------------------------------

    @Override
    public final int getDegree() {
        return fields.length;
    }

    @Override
    public final Field<?> getField(int index) {
        return fields[index];
    }

    @Override
    public final Field<?>[] getFields() {
        Field<?>[] result = new Field[fields.length];
        System.arraycopy(fields, 0, result, 0, fields.length);
        return result;
    }

    @Override
    public final Field<T1> field1() {
        return (Field<T1>) fields[0];
    }

    @Override
    public final Field<T2> field2() {
        return (Field<T2>) fields[1];
    }

    @Override
    public final Field<T3> field3() {
        return (Field<T3>) fields[2];
    }

    @Override
    public final Field<T4> field4() {
        return (Field<T4>) fields[3];
    }

    @Override
    public final Field<T5> field5() {
        return (Field<T5>) fields[4];
    }

    @Override
    public final Field<T6> field6() {
        return (Field<T6>) fields[5];
    }

    @Override
    public final Field<T7> field7() {
        return (Field<T7>) fields[6];
    }

    @Override
    public final Field<T8> field8() {
        return (Field<T8>) fields[7];
    }

    @Override
    public final Field<T9> field9() {
        return (Field<T9>) fields[8];
    }

    @Override
    public final Field<T10> field10() {
        return (Field<T10>) fields[9];
    }

    @Override
    public final Field<T11> field11() {
        return (Field<T11>) fields[10];
    }

    @Override
    public final Field<T12> field12() {
        return (Field<T12>) fields[11];
    }

    @Override
    public final Field<T13> field13() {
        return (Field<T13>) fields[12];
    }

    @Override
    public final Field<T14> field14() {
        return (Field<T14>) fields[13];
    }

    @Override
    public final Field<T15> field15() {
        return (Field<T15>) fields[14];
    }

    @Override
    public final Field<T16> field16() {
        return (Field<T16>) fields[15];
    }

    @Override
    public final Field<T17> field17() {
        return (Field<T17>) fields[16];
    }

    @Override
    public final Field<T18> field18() {
        return (Field<T18>) fields[17];
    }

    @Override
    public final Field<T19> field19() {
        return (Field<T19>) fields[18];
    }

    @Override
    public final Field<T20> field20() {
        return (Field<T20>) fields[19];
    }

    @Override
    public final Field<T21> field21() {
        return (Field<T21>) fields[20];
    }

    @Override
    public final Field<T22> field22() {
        return (Field<T22>) fields[21];
    }

    // ------------------------------------------------------------------------
    // [NOT] NULL predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition isNull() {
    	return new RowIsNull(this, true);
    }
    
    @Override
    public final Condition isNotNull() {
    	return new RowIsNull(this, false);
	}

    // ------------------------------------------------------------------------
    // Equal / Not equal comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition equal(Row1<T1> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row2<T1, T2> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row3<T1, T2, T3> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row4<T1, T2, T3, T4> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row5<T1, T2, T3, T4, T5> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(RowN row) {
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record1<T1> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record2<T1, T2> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record3<T1, T2, T3> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record4<T1, T2, T3, T4> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record5<T1, T2, T3, T4, T5> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record6<T1, T2, T3, T4, T5, T6> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Record record) {
        List<Field<?>> f = record.getFields();
        Row row = new RowImpl(vals(record.intoArray(), f.toArray(new Field[f.size()])));
        return new RowCondition(this, row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(T1 t1) {
        return equal(row(t1));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2) {
        return equal(row(t1, t2));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3) {
        return equal(row(t1, t2, t3));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4) {
        return equal(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return equal(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return equal(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition equal(Object... values) {
        return equal(row(values));
    }

    @Override
    public final Condition equal(Field<T1> t1) {
        return equal(row(t1));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2) {
        return equal(row(t1, t2));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return equal(row(t1, t2, t3));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return equal(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return equal(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return equal(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return equal(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition equal(Field<?>... f) {
        return equal(row(f));
    }

    @Override
    public final Condition eq(Row1<T1> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row2<T1, T2> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row3<T1, T2, T3> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row4<T1, T2, T3, T4> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row5<T1, T2, T3, T4, T5> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row6<T1, T2, T3, T4, T5, T6> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return equal(row);
    }

    @Override
    public final Condition eq(RowN row) {
        return equal(row);
    }

    @Override
    public final Condition eq(Record1<T1> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record2<T1, T2> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record3<T1, T2, T3> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record4<T1, T2, T3, T4> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record5<T1, T2, T3, T4, T5> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record6<T1, T2, T3, T4, T5, T6> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return equal(record);
    }

    @Override
    public final Condition eq(Record record) {
        return equal(record);
    }

    @Override
    public final Condition eq(T1 t1) {
        return equal(t1);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2) {
        return equal(t1, t2);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3) {
        return equal(t1, t2, t3);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4) {
        return equal(t1, t2, t3, t4);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return equal(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return equal(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return equal(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition eq(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition eq(Object... values) {
        return equal(values);
    }

    @Override
    public final Condition eq(Field<T1> t1) {
        return equal(t1);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2) {
        return equal(t1, t2);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return equal(t1, t2, t3);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return equal(t1, t2, t3, t4);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return equal(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return equal(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return equal(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition eq(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return equal(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition eq(Field<?>... values) {
        return equal(values);
    }

    @Override
    public final Condition notEqual(Row1<T1> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row2<T1, T2> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row3<T1, T2, T3> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row4<T1, T2, T3, T4> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row5<T1, T2, T3, T4, T5> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(RowN row) {
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record1<T1> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record2<T1, T2> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record3<T1, T2, T3> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record4<T1, T2, T3, T4> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record5<T1, T2, T3, T4, T5> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record6<T1, T2, T3, T4, T5, T6> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Record record) {
        List<Field<?>> f = record.getFields();
        Row row = new RowImpl(vals(record.intoArray(), f.toArray(new Field[f.size()])));
        return new RowCondition(this, row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(T1 t1) {
        return notEqual(row(t1));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2) {
        return notEqual(row(t1, t2));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3) {
        return notEqual(row(t1, t2, t3));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4) {
        return notEqual(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return notEqual(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return notEqual(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition notEqual(Object... values) {
        return notEqual(row(values));
    }

    @Override
    public final Condition notEqual(Field<T1> t1) {
        return notEqual(row(t1));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2) {
        return notEqual(row(t1, t2));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return notEqual(row(t1, t2, t3));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return notEqual(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return notEqual(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return notEqual(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return notEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition notEqual(Field<?>... f) {
        return notEqual(row(f));
    }

    @Override
    public final Condition ne(Row1<T1> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row2<T1, T2> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row3<T1, T2, T3> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row4<T1, T2, T3, T4> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row5<T1, T2, T3, T4, T5> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row6<T1, T2, T3, T4, T5, T6> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(RowN row) {
        return notEqual(row);
    }

    @Override
    public final Condition ne(Record1<T1> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record2<T1, T2> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record3<T1, T2, T3> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record4<T1, T2, T3, T4> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record5<T1, T2, T3, T4, T5> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record6<T1, T2, T3, T4, T5, T6> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(Record record) {
        return notEqual(record);
    }

    @Override
    public final Condition ne(T1 t1) {
        return notEqual(t1);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2) {
        return notEqual(t1, t2);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3) {
        return notEqual(t1, t2, t3);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4) {
        return notEqual(t1, t2, t3, t4);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return notEqual(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return notEqual(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition ne(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition ne(Object... values) {
        return notEqual(values);
    }

    @Override
    public final Condition ne(Field<T1> t1) {
        return notEqual(t1);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2) {
        return notEqual(t1, t2);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return notEqual(t1, t2, t3);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return notEqual(t1, t2, t3, t4);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return notEqual(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return notEqual(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition ne(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return notEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition ne(Field<?>... values) {
        return notEqual(values);
    }

    // ------------------------------------------------------------------------
    // Ordering comparison predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition lessThan(Row1<T1> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row2<T1, T2> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row3<T1, T2, T3> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row4<T1, T2, T3, T4> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row5<T1, T2, T3, T4, T5> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(RowN row) {
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record1<T1> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record2<T1, T2> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record3<T1, T2, T3> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record4<T1, T2, T3, T4> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record5<T1, T2, T3, T4, T5> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record6<T1, T2, T3, T4, T5, T6> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS);
    }

    @Override
    public final Condition lessThan(Record record) {
        List<Field<?>> f = record.getFields();
        Row row = new RowImpl(vals(record.intoArray(), f.toArray(new Field[f.size()])));
        return new RowCondition(this, row, Comparator.LESS);
    }

    @Override
    public final Condition lessThan(T1 t1) {
        return lessThan(row(t1));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2) {
        return lessThan(row(t1, t2));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3) {
        return lessThan(row(t1, t2, t3));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4) {
        return lessThan(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return lessThan(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return lessThan(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition lessThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition lessThan(Object... values) {
        return lessThan(row(values));
    }

    @Override
    public final Condition lessThan(Field<T1> t1) {
        return lessThan(row(t1));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2) {
        return lessThan(row(t1, t2));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return lessThan(row(t1, t2, t3));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return lessThan(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return lessThan(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return lessThan(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition lessThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return lessThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition lessThan(Field<?>... f) {
        return lessThan(row(f));
    }

    @Override
    public final Condition lt(Row1<T1> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row2<T1, T2> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row3<T1, T2, T3> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row4<T1, T2, T3, T4> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row5<T1, T2, T3, T4, T5> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row6<T1, T2, T3, T4, T5, T6> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(RowN row) {
        return lessThan(row);
    }

    @Override
    public final Condition lt(Record1<T1> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record2<T1, T2> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record3<T1, T2, T3> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record4<T1, T2, T3, T4> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record5<T1, T2, T3, T4, T5> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record6<T1, T2, T3, T4, T5, T6> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(Record record) {
        return lessThan(record);
    }

    @Override
    public final Condition lt(T1 t1) {
        return lessThan(t1);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2) {
        return lessThan(t1, t2);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3) {
        return lessThan(t1, t2, t3);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4) {
        return lessThan(t1, t2, t3, t4);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return lessThan(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return lessThan(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition lt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition lt(Object... values) {
        return lessThan(values);
    }

    @Override
    public final Condition lt(Field<T1> t1) {
        return lessThan(t1);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2) {
        return lessThan(t1, t2);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return lessThan(t1, t2, t3);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return lessThan(t1, t2, t3, t4);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return lessThan(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return lessThan(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition lt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return lessThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition lt(Field<?>... values) {
        return lessThan(values);
    }

    @Override
    public final Condition lessOrEqual(Row1<T1> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row2<T1, T2> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row3<T1, T2, T3> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row4<T1, T2, T3, T4> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row5<T1, T2, T3, T4, T5> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(RowN row) {
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record1<T1> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record2<T1, T2> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record3<T1, T2, T3> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record4<T1, T2, T3, T4> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record5<T1, T2, T3, T4, T5> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record6<T1, T2, T3, T4, T5, T6> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(Record record) {
        List<Field<?>> f = record.getFields();
        Row row = new RowImpl(vals(record.intoArray(), f.toArray(new Field[f.size()])));
        return new RowCondition(this, row, Comparator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition lessOrEqual(T1 t1) {
        return lessOrEqual(row(t1));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2) {
        return lessOrEqual(row(t1, t2));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3) {
        return lessOrEqual(row(t1, t2, t3));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4) {
        return lessOrEqual(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return lessOrEqual(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition lessOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition lessOrEqual(Object... values) {
        return lessOrEqual(row(values));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1) {
        return lessOrEqual(row(t1));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2) {
        return lessOrEqual(row(t1, t2));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return lessOrEqual(row(t1, t2, t3));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return lessOrEqual(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return lessOrEqual(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition lessOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return lessOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition lessOrEqual(Field<?>... f) {
        return lessOrEqual(row(f));
    }

    @Override
    public final Condition le(Row1<T1> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row2<T1, T2> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row3<T1, T2, T3> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row4<T1, T2, T3, T4> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row5<T1, T2, T3, T4, T5> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row6<T1, T2, T3, T4, T5, T6> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(RowN row) {
        return lessOrEqual(row);
    }

    @Override
    public final Condition le(Record1<T1> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record2<T1, T2> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record3<T1, T2, T3> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record4<T1, T2, T3, T4> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record5<T1, T2, T3, T4, T5> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record6<T1, T2, T3, T4, T5, T6> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(Record record) {
        return lessOrEqual(record);
    }

    @Override
    public final Condition le(T1 t1) {
        return lessOrEqual(t1);
    }

    @Override
    public final Condition le(T1 t1, T2 t2) {
        return lessOrEqual(t1, t2);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3) {
        return lessOrEqual(t1, t2, t3);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4) {
        return lessOrEqual(t1, t2, t3, t4);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return lessOrEqual(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition le(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition le(Object... values) {
        return lessOrEqual(values);
    }

    @Override
    public final Condition le(Field<T1> t1) {
        return lessOrEqual(t1);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2) {
        return lessOrEqual(t1, t2);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return lessOrEqual(t1, t2, t3);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return lessOrEqual(t1, t2, t3, t4);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return lessOrEqual(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition le(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return lessOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition le(Field<?>... values) {
        return lessOrEqual(values);
    }

    @Override
    public final Condition greaterThan(Row1<T1> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row2<T1, T2> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row3<T1, T2, T3> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row4<T1, T2, T3, T4> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row5<T1, T2, T3, T4, T5> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(RowN row) {
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record1<T1> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record2<T1, T2> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record3<T1, T2, T3> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record4<T1, T2, T3, T4> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record5<T1, T2, T3, T4, T5> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record6<T1, T2, T3, T4, T5, T6> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(Record record) {
        List<Field<?>> f = record.getFields();
        Row row = new RowImpl(vals(record.intoArray(), f.toArray(new Field[f.size()])));
        return new RowCondition(this, row, Comparator.GREATER);
    }

    @Override
    public final Condition greaterThan(T1 t1) {
        return greaterThan(row(t1));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2) {
        return greaterThan(row(t1, t2));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3) {
        return greaterThan(row(t1, t2, t3));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4) {
        return greaterThan(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return greaterThan(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition greaterThan(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition greaterThan(Object... values) {
        return greaterThan(row(values));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1) {
        return greaterThan(row(t1));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2) {
        return greaterThan(row(t1, t2));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return greaterThan(row(t1, t2, t3));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return greaterThan(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return greaterThan(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition greaterThan(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return greaterThan(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition greaterThan(Field<?>... f) {
        return greaterThan(row(f));
    }

    @Override
    public final Condition gt(Row1<T1> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row2<T1, T2> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row3<T1, T2, T3> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row4<T1, T2, T3, T4> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row5<T1, T2, T3, T4, T5> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row6<T1, T2, T3, T4, T5, T6> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(RowN row) {
        return greaterThan(row);
    }

    @Override
    public final Condition gt(Record1<T1> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record2<T1, T2> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record3<T1, T2, T3> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record4<T1, T2, T3, T4> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record5<T1, T2, T3, T4, T5> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record6<T1, T2, T3, T4, T5, T6> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(Record record) {
        return greaterThan(record);
    }

    @Override
    public final Condition gt(T1 t1) {
        return greaterThan(t1);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2) {
        return greaterThan(t1, t2);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3) {
        return greaterThan(t1, t2, t3);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4) {
        return greaterThan(t1, t2, t3, t4);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return greaterThan(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return greaterThan(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition gt(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition gt(Object... values) {
        return greaterThan(values);
    }

    @Override
    public final Condition gt(Field<T1> t1) {
        return greaterThan(t1);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2) {
        return greaterThan(t1, t2);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return greaterThan(t1, t2, t3);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return greaterThan(t1, t2, t3, t4);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return greaterThan(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return greaterThan(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition gt(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return greaterThan(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition gt(Field<?>... values) {
        return greaterThan(values);
    }

    @Override
    public final Condition greaterOrEqual(Row1<T1> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row2<T1, T2> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row3<T1, T2, T3> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row4<T1, T2, T3, T4> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row5<T1, T2, T3, T4, T5> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(RowN row) {
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record1<T1> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record2<T1, T2> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record3<T1, T2, T3> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record4<T1, T2, T3, T4> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record5<T1, T2, T3, T4, T5> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record6<T1, T2, T3, T4, T5, T6> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return new RowCondition(this, record.valuesRow(), Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(Record record) {
        List<Field<?>> f = record.getFields();
        Row row = new RowImpl(vals(record.intoArray(), f.toArray(new Field[f.size()])));
        return new RowCondition(this, row, Comparator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition greaterOrEqual(T1 t1) {
        return greaterOrEqual(row(t1));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2) {
        return greaterOrEqual(row(t1, t2));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3) {
        return greaterOrEqual(row(t1, t2, t3));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4) {
        return greaterOrEqual(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition greaterOrEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition greaterOrEqual(Object... values) {
        return greaterOrEqual(row(values));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1) {
        return greaterOrEqual(row(t1));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2) {
        return greaterOrEqual(row(t1, t2));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return greaterOrEqual(row(t1, t2, t3));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return greaterOrEqual(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition greaterOrEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return greaterOrEqual(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition greaterOrEqual(Field<?>... f) {
        return greaterOrEqual(row(f));
    }

    @Override
    public final Condition ge(Row1<T1> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row2<T1, T2> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row3<T1, T2, T3> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row4<T1, T2, T3, T4> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row5<T1, T2, T3, T4, T5> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row6<T1, T2, T3, T4, T5, T6> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(RowN row) {
        return greaterOrEqual(row);
    }

    @Override
    public final Condition ge(Record1<T1> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record2<T1, T2> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record3<T1, T2, T3> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record4<T1, T2, T3, T4> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record5<T1, T2, T3, T4, T5> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record6<T1, T2, T3, T4, T5, T6> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(Record record) {
        return greaterOrEqual(record);
    }

    @Override
    public final Condition ge(T1 t1) {
        return greaterOrEqual(t1);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2) {
        return greaterOrEqual(t1, t2);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3) {
        return greaterOrEqual(t1, t2, t3);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4) {
        return greaterOrEqual(t1, t2, t3, t4);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return greaterOrEqual(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition ge(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition ge(Object... values) {
        return greaterOrEqual(values);
    }

    @Override
    public final Condition ge(Field<T1> t1) {
        return greaterOrEqual(t1);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2) {
        return greaterOrEqual(t1, t2);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return greaterOrEqual(t1, t2, t3);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return greaterOrEqual(t1, t2, t3, t4);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return greaterOrEqual(t1, t2, t3, t4, t5);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    @Override
    public final Condition ge(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return greaterOrEqual(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    @Override
    public final Condition ge(Field<?>... values) {
        return greaterOrEqual(values);
    }

    // ------------------------------------------------------------------------
    // [NOT] DISTINCT predicates
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // [NOT] IN predicates
    // ------------------------------------------------------------------------

    @Override
    public final Condition in(Row1<T1>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row2<T1, T2>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row3<T1, T2, T3>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row4<T1, T2, T3, T4>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row5<T1, T2, T3, T4, T5>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row6<T1, T2, T3, T4, T5, T6>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row7<T1, T2, T3, T4, T5, T6, T7>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row8<T1, T2, T3, T4, T5, T6, T7, T8>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(RowN... rows) {
        return in(Arrays.asList(rows));
    }

    @Override
    public final Condition in(Record1<T1>... records) {
        Row1<T1>[] rows = new Row1[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record2<T1, T2>... records) {
        Row2<T1, T2>[] rows = new Row2[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record3<T1, T2, T3>... records) {
        Row3<T1, T2, T3>[] rows = new Row3[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record4<T1, T2, T3, T4>... records) {
        Row4<T1, T2, T3, T4>[] rows = new Row4[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record5<T1, T2, T3, T4, T5>... records) {
        Row5<T1, T2, T3, T4, T5>[] rows = new Row5[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record6<T1, T2, T3, T4, T5, T6>... records) {
        Row6<T1, T2, T3, T4, T5, T6>[] rows = new Row6[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record7<T1, T2, T3, T4, T5, T6, T7>... records) {
        Row7<T1, T2, T3, T4, T5, T6, T7>[] rows = new Row7[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record8<T1, T2, T3, T4, T5, T6, T7, T8>... records) {
        Row8<T1, T2, T3, T4, T5, T6, T7, T8>[] rows = new Row8[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>... records) {
        Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9>[] rows = new Row9[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>... records) {
        Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>[] rows = new Row10[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>... records) {
        Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>[] rows = new Row11[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>... records) {
        Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>[] rows = new Row12[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... records) {
        Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>[] rows = new Row13[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>... records) {
        Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>[] rows = new Row14[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>... records) {
        Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>[] rows = new Row15[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>... records) {
        Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>[] rows = new Row16[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>... records) {
        Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>[] rows = new Row17[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>... records) {
        Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>[] rows = new Row18[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>... records) {
        Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>[] rows = new Row19[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>... records) {
        Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>[] rows = new Row20[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>... records) {
        Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>[] rows = new Row21[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>... records) {
        Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>[] rows = new Row22[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return in(rows);
    }

    @Override
    public final Condition in(Record... records) {
        RowN[] rows = new RowN[records.length];

        for (int i = 0; i < records.length; i++) {
            List<Field<?>> f = records[i].getFields();
            rows[i] = new RowImpl(vals(records[i].intoArray(), f.toArray(new Field[f.size()])));
        }

        return in(rows);
    }

    @Override
    public final Condition notIn(Row1<T1>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row2<T1, T2>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row3<T1, T2, T3>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row4<T1, T2, T3, T4>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row5<T1, T2, T3, T4, T5>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row6<T1, T2, T3, T4, T5, T6>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row7<T1, T2, T3, T4, T5, T6, T7>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row8<T1, T2, T3, T4, T5, T6, T7, T8>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(RowN... rows) {
        return notIn(Arrays.asList(rows));
    }

    @Override
    public final Condition notIn(Record1<T1>... records) {
        Row1<T1>[] rows = new Row1[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record2<T1, T2>... records) {
        Row2<T1, T2>[] rows = new Row2[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record3<T1, T2, T3>... records) {
        Row3<T1, T2, T3>[] rows = new Row3[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record4<T1, T2, T3, T4>... records) {
        Row4<T1, T2, T3, T4>[] rows = new Row4[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record5<T1, T2, T3, T4, T5>... records) {
        Row5<T1, T2, T3, T4, T5>[] rows = new Row5[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record6<T1, T2, T3, T4, T5, T6>... records) {
        Row6<T1, T2, T3, T4, T5, T6>[] rows = new Row6[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record7<T1, T2, T3, T4, T5, T6, T7>... records) {
        Row7<T1, T2, T3, T4, T5, T6, T7>[] rows = new Row7[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record8<T1, T2, T3, T4, T5, T6, T7, T8>... records) {
        Row8<T1, T2, T3, T4, T5, T6, T7, T8>[] rows = new Row8[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>... records) {
        Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9>[] rows = new Row9[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>... records) {
        Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>[] rows = new Row10[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>... records) {
        Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>[] rows = new Row11[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>... records) {
        Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>[] rows = new Row12[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>... records) {
        Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>[] rows = new Row13[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>... records) {
        Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>[] rows = new Row14[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>... records) {
        Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>[] rows = new Row15[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>... records) {
        Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>[] rows = new Row16[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>... records) {
        Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>[] rows = new Row17[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>... records) {
        Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>[] rows = new Row18[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>... records) {
        Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>[] rows = new Row19[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>... records) {
        Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>[] rows = new Row20[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>... records) {
        Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>[] rows = new Row21[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>... records) {
        Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>[] rows = new Row22[records.length];

        for (int i = 0; i < records.length; i++) {
            rows[i] = records[i].valuesRow();
        }

        return notIn(rows);
    }

    @Override
    public final Condition notIn(Record... records) {
        RowN[] rows = new RowN[records.length];

        for (int i = 0; i < records.length; i++) {
            List<Field<?>> f = records[i].getFields();
            rows[i] = new RowImpl(vals(records[i].intoArray(), f.toArray(new Field[f.size()])));
        }

        return notIn(rows);
    }

    @Override
    public final Condition in(Collection rows) {
        QueryPartList<Row> list = new QueryPartList<Row>(rows);
        return new RowInCondition(this, list, SubqueryOperator.IN);
    }

    @Override
    public final Condition notIn(Collection rows) {
        QueryPartList<Row> list = new QueryPartList<Row>(rows);
        return new RowInCondition(this, list, SubqueryOperator.NOT_IN);
    }

    // ------------------------------------------------------------------------
    // Predicates involving subqueries
    // ------------------------------------------------------------------------
    
    @Override
    public final Condition equal(Select select) {
        return new RowSubqueryCondition(this, select, SubqueryOperator.EQUALS);
    }

    @Override
    public final Condition eq(Select select) {
        return equal(select);
    }

    @Override
    public final Condition notEqual(Select select) {
        return new RowSubqueryCondition(this, select, SubqueryOperator.NOT_EQUALS);
    }

    @Override
    public final Condition ne(Select select) {
        return notEqual(select);
    }

    @Override
    public final Condition greaterThan(Select select) {
        return new RowSubqueryCondition(this, select, SubqueryOperator.GREATER);
    }

    @Override
    public final Condition gt(Select select) {
        return greaterThan(select);
    }

    @Override
    public final Condition greaterOrEqual(Select select) {
        return new RowSubqueryCondition(this, select, SubqueryOperator.GREATER_OR_EQUAL);
    }

    @Override
    public final Condition ge(Select select) {
        return greaterOrEqual(select);
    }

    @Override
    public final Condition lessThan(Select select) {
        return new RowSubqueryCondition(this, select, SubqueryOperator.LESS);
    }

    @Override
    public final Condition lt(Select select) {
        return lessThan(select);
    }

    @Override
    public final Condition lessOrEqual(Select select) {
        return new RowSubqueryCondition(this, select, SubqueryOperator.LESS_OR_EQUAL);
    }

    @Override
    public final Condition le(Select select) {
        return lessOrEqual(select);
    }

    @Override
    public final Condition in(Select select) {
        return new RowSubqueryCondition(this, select, SubqueryOperator.IN);
    }

    @Override
    public final Condition notIn(Select select) {
        return new RowSubqueryCondition(this, select, SubqueryOperator.NOT_IN);
    }

    // ------------------------------------------------------------------------
    // XXX: Row2 API
    // ------------------------------------------------------------------------

    @Override
    public final Condition overlaps(T1 t1, T2 t2) {
        return overlaps(row(t1, t2));
    }

    @Override
    public final Condition overlaps(Field<T1> t1, Field<T2> t2) {
        return overlaps(row(t1, t2));
    }

    @Override
    public final Condition overlaps(Row2<T1, T2> row) {
        return new RowOverlapsCondition(this, row);
    }
}
