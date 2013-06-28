/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.DSL.row;

import javax.annotation.Generated;

import org.jooq.BetweenAndStep1;
import org.jooq.BetweenAndStep10;
import org.jooq.BetweenAndStep11;
import org.jooq.BetweenAndStep12;
import org.jooq.BetweenAndStep13;
import org.jooq.BetweenAndStep14;
import org.jooq.BetweenAndStep15;
import org.jooq.BetweenAndStep16;
import org.jooq.BetweenAndStep17;
import org.jooq.BetweenAndStep18;
import org.jooq.BetweenAndStep19;
import org.jooq.BetweenAndStep2;
import org.jooq.BetweenAndStep20;
import org.jooq.BetweenAndStep21;
import org.jooq.BetweenAndStep22;
import org.jooq.BetweenAndStep3;
import org.jooq.BetweenAndStep4;
import org.jooq.BetweenAndStep5;
import org.jooq.BetweenAndStep6;
import org.jooq.BetweenAndStep7;
import org.jooq.BetweenAndStep8;
import org.jooq.BetweenAndStep9;
import org.jooq.BetweenAndStepN;
import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.Record1;
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
import org.jooq.Record2;
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
import org.jooq.RenderContext;
import org.jooq.Row;
import org.jooq.Row1;
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
import org.jooq.Row2;
import org.jooq.Row20;
import org.jooq.Row21;
import org.jooq.Row22;
import org.jooq.Row3;
import org.jooq.Row4;
import org.jooq.Row5;
import org.jooq.Row6;
import org.jooq.Row7;
import org.jooq.Row8;
import org.jooq.Row9;
import org.jooq.RowN;

/**
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
@SuppressWarnings({ "rawtypes", "unchecked" })
class RowBetweenCondition<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> extends AbstractCondition
implements

    // This BetweenAndStep implementation implements all types. Type-safety is
    // being checked through the type-safe API. No need for further checks here
    BetweenAndStep1<T1>,
    BetweenAndStep2<T1, T2>,
    BetweenAndStep3<T1, T2, T3>,
    BetweenAndStep4<T1, T2, T3, T4>,
    BetweenAndStep5<T1, T2, T3, T4, T5>,
    BetweenAndStep6<T1, T2, T3, T4, T5, T6>,
    BetweenAndStep7<T1, T2, T3, T4, T5, T6, T7>,
    BetweenAndStep8<T1, T2, T3, T4, T5, T6, T7, T8>,
    BetweenAndStep9<T1, T2, T3, T4, T5, T6, T7, T8, T9>,
    BetweenAndStep10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>,
    BetweenAndStep11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>,
    BetweenAndStep12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>,
    BetweenAndStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>,
    BetweenAndStep14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>,
    BetweenAndStep15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>,
    BetweenAndStep16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>,
    BetweenAndStep17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>,
    BetweenAndStep18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>,
    BetweenAndStep19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>,
    BetweenAndStep20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>,
    BetweenAndStep21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>,
    BetweenAndStep22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>,
    BetweenAndStepN {

    private static final long serialVersionUID = -4666251100802237878L;

    private final boolean     symmetric;
    private final boolean     not;
    private final Row         row;
    private final Row         minValue;
    private Row               maxValue;

    RowBetweenCondition(Row row, Row minValue, boolean not, boolean symmetric) {
        this.row = row;
        this.minValue = minValue;
        this.not = not;
        this.symmetric = symmetric;
    }

    // ------------------------------------------------------------------------
    // XXX: BetweenAndStep API
    // ------------------------------------------------------------------------

    @Override
    public final Condition and(Field f) {
        if (maxValue == null) {
            return and(row(f));
        }
        else {
            return super.and(f);
        }
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2) {
        return and(row(t1, t2));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return and(row(t1, t2, t3));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return and(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return and(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return and(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return and(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition and(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8, Field<T9> t9, Field<T10> t10, Field<T11> t11, Field<T12> t12, Field<T13> t13, Field<T14> t14, Field<T15> t15, Field<T16> t16, Field<T17> t17, Field<T18> t18, Field<T19> t19, Field<T20> t20, Field<T21> t21, Field<T22> t22) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition and(Field<?>... fields) {
        return and(row(fields));
    }

    @Override
    public final Condition and(T1 t1) {
        return and(row(t1));
    }

    @Override
    public final Condition and(T1 t1, T2 t2) {
        return and(row(t1, t2));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3) {
        return and(row(t1, t2, t3));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4) {
        return and(row(t1, t2, t3, t4));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return and(row(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return and(row(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return and(row(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
    }

    @Override
    public final Condition and(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return and(row(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
    }

    @Override
    public final Condition and(Object... values) {
        return and(row(values));
    }

    @Override
    public final Condition and(Row1<T1> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row2<T1, T2> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row3<T1, T2, T3> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row4<T1, T2, T3, T4> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row5<T1, T2, T3, T4, T5> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row6<T1, T2, T3, T4, T5, T6> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row7<T1, T2, T3, T4, T5, T6, T7> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row8<T1, T2, T3, T4, T5, T6, T7, T8> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row9<T1, T2, T3, T4, T5, T6, T7, T8, T9> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Row22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(RowN r) {
        this.maxValue = r;
        return this;
    }

    @Override
    public final Condition and(Record1<T1> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record2<T1, T2> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record3<T1, T2, T3> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record4<T1, T2, T3, T4> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record5<T1, T2, T3, T4, T5> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record6<T1, T2, T3, T4, T5, T6> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record7<T1, T2, T3, T4, T5, T6, T7> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record8<T1, T2, T3, T4, T5, T6, T7, T8> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> record) {
        return and(record.valuesRow());
    }

    @Override
    public final Condition and(Record record) {
        RowN r = new RowImpl(Utils.fields(record.intoArray(), record.fields()));
        return and(r);
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void bind(BindContext context) {
        delegate(context.configuration()).bind(context);
    }

    @Override
    public final void toSQL(RenderContext context) {
        delegate(context.configuration()).toSQL(context);
    }

    private final QueryPartInternal delegate(Configuration configuration) {
        // These casts are safe for RowImpl
        RowN r = (RowN) row;
        RowN min = (RowN) minValue;
        RowN max = (RowN) maxValue;

        // These dialects don't support the SYMMETRIC keyword at all
        if (symmetric && asList(ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, MARIADB, MYSQL, ORACLE, SQLITE, SQLSERVER, SYBASE).contains(configuration.dialect().family())) {
            if (not) {
                return (QueryPartInternal) r.notBetween(min, max).and(r.notBetween(max, min));
            }
            else {
                return (QueryPartInternal) r.between(min, max).or(r.between(max, min));
            }
        }

        // These dialects either don't support row value expressions, or they
        // Can't handle row value expressions with the BETWEEN predicate
        else if (row.size() > 1 && asList(CUBRID, DERBY, FIREBIRD, MARIADB, MYSQL, ORACLE, SQLITE, SQLSERVER, SYBASE).contains(configuration.dialect().family())) {
            Condition result = r.ge(min).and(r.le(max));

            if (not) {
                result = result.not();
            }

            return (QueryPartInternal) result;
        }
        else {
            return new Native();
        }
    }

    private class Native extends AbstractQueryPart {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2915703568738921575L;

        @Override
        public final void toSQL(RenderContext context) {
            context.sql(row)
                   .keyword(not ? " not" : "")
                   .keyword(" between ")
                   .keyword(symmetric ? "symmetric " : "")
                   .sql(minValue)
                   .keyword(" and ")
                   .sql(maxValue);
        }

        @Override
        public final void bind(BindContext context) {
            context.bind(row).bind(minValue).bind(maxValue);
        }
    }
}
