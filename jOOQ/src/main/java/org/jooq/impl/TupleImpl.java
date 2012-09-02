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

import static java.util.Arrays.asList;
import static org.jooq.Comparator.EQUALS;
import static org.jooq.Comparator.NOT_EQUALS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.Factory.tuple;
import static org.jooq.impl.InOperator.NOT_IN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Operator;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.Tuple1;
import org.jooq.Tuple2;
import org.jooq.Tuple3;
import org.jooq.Tuple4;
import org.jooq.Tuple5;
import org.jooq.Tuple6;
import org.jooq.Tuple7;
import org.jooq.Tuple8;
import org.jooq.TupleN;

/**
 * @author Lukas Eder
 */
class TupleImpl<T1, T2, T3, T4, T5, T6, T7, T8> extends AbstractNamedQueryPart
implements

    // This tuple implementation implements all tuple types. Type-safety is
    // being checked through the type-safe API. No need for further checks here
    Tuple1<T1>,
    Tuple2<T1, T2>,
    Tuple3<T1, T2, T3>,
    Tuple4<T1, T2, T3, T4>,
    Tuple5<T1, T2, T3, T4, T5>,
    Tuple6<T1, T2, T3, T4, T5, T6>,
    Tuple7<T1, T2, T3, T4, T5, T6, T7>,
    Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>,
    TupleN {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -929427349071556318L;

    private final Field<?>[]  fields;

    TupleImpl(Field<?>... fields) {
        super("tuple");

        this.fields = fields;
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
    // XXX: Tuple accessor API
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

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T1> field1() {
        return (Field<T1>) fields[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T2> field2() {
        return (Field<T2>) fields[1];
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T3> field3() {
        return (Field<T3>) fields[2];
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T4> field4() {
        return (Field<T4>) fields[3];
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T5> field5() {
        return (Field<T5>) fields[4];
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T6> field6() {
        return (Field<T6>) fields[5];
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T7> field7() {
        return (Field<T7>) fields[6];
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T8> field8() {
        return (Field<T8>) fields[7];
    }

    // ------------------------------------------------------------------------
    // XXX: Tuple DSL API
    // ------------------------------------------------------------------------

    @Override
    public final Condition equal(Tuple1<T1> tuple) {
        return new Compare(tuple, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Tuple2<T1, T2> tuple) {
        return new Compare(tuple, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Tuple3<T1, T2, T3> tuple) {
        return new Compare(tuple, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Tuple4<T1, T2, T3, T4> tuple) {
        return new Compare(tuple, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Tuple5<T1, T2, T3, T4, T5> tuple) {
        return new Compare(tuple, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Tuple6<T1, T2, T3, T4, T5, T6> tuple) {
        return new Compare(tuple, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple) {
        return new Compare(tuple, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> tuple) {
        return new Compare(tuple, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(TupleN tuple) {
        return new Compare(tuple, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(T1 t1) {
        return equal(tuple(t1));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2) {
        return equal(tuple(t1, t2));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3) {
        return equal(tuple(t1, t2, t3));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4) {
        return equal(tuple(t1, t2, t3, t4));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return equal(tuple(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return equal(tuple(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return equal(tuple(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition equal(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return equal(tuple(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition equal(Object... values) {
        return equal(tuple(values));
    }

    @Override
    public final Condition equal(Field<T1> t1) {
        return equal(tuple(t1));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2) {
        return equal(tuple(t1, t2));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return equal(tuple(t1, t2, t3));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return equal(tuple(t1, t2, t3, t4));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return equal(tuple(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return equal(tuple(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return equal(tuple(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition equal(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return equal(tuple(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition equal(Field<?>... f) {
        return equal(tuple(f));
    }

    @Override
    public final Condition eq(Tuple1<T1> tuple) {
        return equal(tuple);
    }

    @Override
    public final Condition eq(Tuple2<T1, T2> tuple) {
        return equal(tuple);
    }

    @Override
    public final Condition eq(Tuple3<T1, T2, T3> tuple) {
        return equal(tuple);
    }

    @Override
    public final Condition eq(Tuple4<T1, T2, T3, T4> tuple) {
        return equal(tuple);
    }

    @Override
    public final Condition eq(Tuple5<T1, T2, T3, T4, T5> tuple) {
        return equal(tuple);
    }

    @Override
    public final Condition eq(Tuple6<T1, T2, T3, T4, T5, T6> tuple) {
        return equal(tuple);
    }

    @Override
    public final Condition eq(Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple) {
        return equal(tuple);
    }

    @Override
    public final Condition eq(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> tuple) {
        return equal(tuple);
    }

    @Override
    public final Condition eq(TupleN tuple) {
        return equal(tuple);
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
    public final Condition eq(Field<?>... values) {
        return equal(values);
    }

    @Override
    public final Condition notEqual(Tuple1<T1> tuple) {
        return new Compare(tuple, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Tuple2<T1, T2> tuple) {
        return new Compare(tuple, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Tuple3<T1, T2, T3> tuple) {
        return new Compare(tuple, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Tuple4<T1, T2, T3, T4> tuple) {
        return new Compare(tuple, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Tuple5<T1, T2, T3, T4, T5> tuple) {
        return new Compare(tuple, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Tuple6<T1, T2, T3, T4, T5, T6> tuple) {
        return new Compare(tuple, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple) {
        return new Compare(tuple, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> tuple) {
        return new Compare(tuple, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(TupleN tuple) {
        return new Compare(tuple, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(T1 t1) {
        return notEqual(tuple(t1));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2) {
        return notEqual(tuple(t1, t2));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3) {
        return notEqual(tuple(t1, t2, t3));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4) {
        return notEqual(tuple(t1, t2, t3, t4));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return notEqual(tuple(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return notEqual(tuple(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return notEqual(tuple(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition notEqual(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return notEqual(tuple(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition notEqual(Object... values) {
        return notEqual(tuple(values));
    }

    @Override
    public final Condition notEqual(Field<T1> t1) {
        return notEqual(tuple(t1));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2) {
        return notEqual(tuple(t1, t2));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3) {
        return notEqual(tuple(t1, t2, t3));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4) {
        return notEqual(tuple(t1, t2, t3, t4));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5) {
        return notEqual(tuple(t1, t2, t3, t4, t5));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6) {
        return notEqual(tuple(t1, t2, t3, t4, t5, t6));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7) {
        return notEqual(tuple(t1, t2, t3, t4, t5, t6, t7));
    }

    @Override
    public final Condition notEqual(Field<T1> t1, Field<T2> t2, Field<T3> t3, Field<T4> t4, Field<T5> t5, Field<T6> t6, Field<T7> t7, Field<T8> t8) {
        return notEqual(tuple(t1, t2, t3, t4, t5, t6, t7, t8));
    }

    @Override
    public final Condition notEqual(Field<?>... f) {
        return notEqual(tuple(f));
    }

    @Override
    public final Condition ne(Tuple1<T1> tuple) {
        return notEqual(tuple);
    }

    @Override
    public final Condition ne(Tuple2<T1, T2> tuple) {
        return notEqual(tuple);
    }

    @Override
    public final Condition ne(Tuple3<T1, T2, T3> tuple) {
        return notEqual(tuple);
    }

    @Override
    public final Condition ne(Tuple4<T1, T2, T3, T4> tuple) {
        return notEqual(tuple);
    }

    @Override
    public final Condition ne(Tuple5<T1, T2, T3, T4, T5> tuple) {
        return notEqual(tuple);
    }

    @Override
    public final Condition ne(Tuple6<T1, T2, T3, T4, T5, T6> tuple) {
        return notEqual(tuple);
    }

    @Override
    public final Condition ne(Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple) {
        return notEqual(tuple);
    }

    @Override
    public final Condition ne(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> tuple) {
        return notEqual(tuple);
    }

    @Override
    public final Condition ne(TupleN tuple) {
        return notEqual(tuple);
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
    public final Condition ne(Field<?>... values) {
        return notEqual(values);
    }

    @Override
    public final Condition in(Tuple1<T1>... tuples) {
        return in(Arrays.asList(tuples));
    }

    @Override
    public final Condition in(Tuple2<T1, T2>... tuples) {
        return in(Arrays.asList(tuples));
    }

    @Override
    public final Condition in(Tuple3<T1, T2, T3>... tuples) {
        return in(Arrays.asList(tuples));
    }

    @Override
    public final Condition in(Tuple4<T1, T2, T3, T4>... tuples) {
        return in(Arrays.asList(tuples));
    }

    @Override
    public final Condition in(Tuple5<T1, T2, T3, T4, T5>... tuples) {
        return in(Arrays.asList(tuples));
    }

    @Override
    public final Condition in(Tuple6<T1, T2, T3, T4, T5, T6>... tuples) {
        return in(Arrays.asList(tuples));
    }

    @Override
    public final Condition in(Tuple7<T1, T2, T3, T4, T5, T6, T7>... tuples) {
        return in(Arrays.asList(tuples));
    }

    @Override
    public final Condition in(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>... tuples) {
        return in(Arrays.asList(tuples));
    }

    @Override
    public final Condition in(TupleN... tuples) {
        return in(Arrays.asList(tuples));
    }

    @Override
    public final Condition notIn(Tuple1<T1>... tuples) {
        return notIn(Arrays.asList(tuples));
    }

    @Override
    public final Condition notIn(Tuple2<T1, T2>... tuples) {
        return notIn(Arrays.asList(tuples));
    }

    @Override
    public final Condition notIn(Tuple3<T1, T2, T3>... tuples) {
        return notIn(Arrays.asList(tuples));
    }

    @Override
    public final Condition notIn(Tuple4<T1, T2, T3, T4>... tuples) {
        return notIn(Arrays.asList(tuples));
    }

    @Override
    public final Condition notIn(Tuple5<T1, T2, T3, T4, T5>... tuples) {
        return notIn(Arrays.asList(tuples));
    }

    @Override
    public final Condition notIn(Tuple6<T1, T2, T3, T4, T5, T6>... tuples) {
        return notIn(Arrays.asList(tuples));
    }

    @Override
    public final Condition notIn(Tuple7<T1, T2, T3, T4, T5, T6, T7>... tuples) {
        return notIn(Arrays.asList(tuples));
    }

    @Override
    public final Condition notIn(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>... tuples) {
        return notIn(Arrays.asList(tuples));
    }

    @Override
    public final Condition notIn(TupleN... tuples) {
        return notIn(Arrays.asList(tuples));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Condition in(Collection tuples) {
        QueryPartList<TupleImpl<T1, T2, T3, T4, T5, T6, T7, T8>> list = new QueryPartList<TupleImpl<T1, T2, T3, T4, T5, T6, T7, T8>>(tuples);
        return new InTuples(list, InOperator.IN);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Condition notIn(Collection tuples) {
        QueryPartList<TupleImpl<T1, T2, T3, T4, T5, T6, T7, T8>> list = new QueryPartList<TupleImpl<T1, T2, T3, T4, T5, T6, T7, T8>>(tuples);
        return new InTuples(list, InOperator.NOT_IN);
    }

    @Override
    public final Condition in(Select<?> select) {
        return new InSubquery(select, InOperator.IN);
    }

    @Override
    public final Condition notIn(Select<?> select) {
        return new InSubquery(select, InOperator.NOT_IN);
    }

    // ------------------------------------------------------------------------
    // XXX: Tuple2 API
    // ------------------------------------------------------------------------

    @Override
    public final Condition overlaps(T1 t1, T2 t2) {
        return overlaps(tuple(t1, t2));
    }

    @Override
    public final Condition overlaps(Field<T1> t1, Field<T2> t2) {
        return overlaps(tuple(t1, t2));
    }

    @Override
    public final Condition overlaps(Tuple2<T1, T2> tuple) {
        return new Overlaps(tuple);
    }

    // ------------------------------------------------------------------------
    // XXX: Implementation classes
    // ------------------------------------------------------------------------

    private class Overlaps extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long    serialVersionUID = 85887551884667824L;

        private final TupleImpl<T1, T2, ?, ?, ?, ?, ?, ?> other;

        @SuppressWarnings("unchecked")
        Overlaps(Tuple2<T1, T2> other) {
            this.other = (TupleImpl<T1, T2, ?, ?, ?, ?, ?, ?>) other;
        }

        @Override
        public final void toSQL(RenderContext context) {
            delegate(context).toSQL(context);
        }

        @Override
        public final void bind(BindContext context) {
            delegate(context).bind(context);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private final QueryPartInternal delegate(Configuration configuration) {
            DataType<?> type0 = fields[0].getDataType();
            DataType<?> type1 = fields[1].getDataType();

            // The SQL standard only knows temporal OVERLAPS predicates:
            // (DATE, DATE)     OVERLAPS (DATE, DATE)
            // (DATE, INTERVAL) OVERLAPS (DATE, INTERVAL)
            boolean standardOverlaps = type0.isDateTime() && type1.isTemporal();
            boolean intervalOverlaps = type0.isDateTime() && (type1.isInterval() || type1.isNumeric());

            // The non-standard OVERLAPS predicate is always simulated
            if (!standardOverlaps || asList(ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, INGRES, MYSQL, SQLSERVER, SQLITE, SYBASE).contains(configuration.getDialect())) {

                // Interval OVERLAPS predicates need some additional arithmetic
                if (intervalOverlaps) {
                    return (QueryPartInternal)
                           other.fields[0].le((Field) fields[0].add(fields[1])).and(
                           fields[0].le((Field) other.fields[0].add(other.fields[1])));
                }

                // All other OVERLAPS predicates can be simulated simply
                else {
                    return (QueryPartInternal)
                           other.fields[0].le((Field) fields[1]).and(
                           fields[0].le((Field) other.fields[1]));
                }
            }

            // These dialects seem to have trouble with INTERVAL OVERLAPS predicates
            else if (intervalOverlaps && asList(HSQLDB).contains(configuration.getDialect())) {
                    return (QueryPartInternal)
                            other.fields[0].le((Field) fields[0].add(fields[1])).and(
                            fields[0].le((Field) other.fields[0].add(other.fields[1])));
            }

            // Everyone else can handle OVERLAPS (Postgres, Oracle)
            else {
                return new Native();
            }
        }

        private class Native extends AbstractCondition {

            /**
             * Generated UID
             */
            private static final long serialVersionUID = -1552476981094856727L;

            @Override
            public final void toSQL(RenderContext context) {
                context.sql("(")
                       .sql(TupleImpl.this)
                       .keyword(" overlaps ")
                       .sql(other)
                       .sql(")");
            }

            @Override
            public final void bind(BindContext context) {
                context.bind(TupleImpl.this).bind(other);
            }
        }
    }

    private class Compare extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long                               serialVersionUID = -1806139685201770706L;

        private final TupleImpl<T1, T2, T3, T4, T5, T6, T7, T8> other;
        private final Comparator                                comparator;

        @SuppressWarnings("unchecked")
        Compare(QueryPart other, Comparator comparator) {
            this.other = (TupleImpl<T1, T2, T3, T4, T5, T6, T7, T8>) other;
            this.comparator = comparator;
        }

        @Override
        public final void toSQL(RenderContext context) {
            delegate(context).toSQL(context);
        }

        @Override
        public final void bind(BindContext context) {
            delegate(context).bind(context);
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        private final QueryPartInternal delegate(Configuration configuration) {
            if (asList(ASE, DERBY, FIREBIRD, INGRES, SQLSERVER, SQLITE, SYBASE).contains(configuration.getDialect())) {
                List<Condition> conditions = new ArrayList<Condition>();

                for (int i = 0; i < fields.length; i++) {
                    conditions.add(fields[i].equal((Field) other.fields[i]));
                }

                Condition result = new CombinedCondition(Operator.AND, conditions);

                if (comparator == NOT_EQUALS) {
                    result = result.not();
                }

                return (QueryPartInternal) result;
            }
            else {
                return new Native();
            }
        }

        private class Native extends AbstractCondition {

            /**
             * Generated UID
             */
            private static final long serialVersionUID = -2977241780111574353L;

            @Override
            public final void toSQL(RenderContext context) {

                // Some dialects do not support != comparison with tuples
                if (comparator == NOT_EQUALS && asList(DB2).contains(context.getDialect())) {
                    context.keyword("not(")
                           .sql(TupleImpl.this)
                           .sql(" = ")
                           .sql(other)
                           .sql(")");
                }
                else {
                    // Some databases need extra parentheses around the RHS
                    boolean extraParentheses = asList(ORACLE).contains(context.getDialect());

                    context.sql(TupleImpl.this)
                           .sql(" ")
                           .sql(comparator.toSQL())
                           .sql(" ")
                           .sql(extraParentheses ? "(" : "")
                           .sql(other)
                           .sql(extraParentheses ? ")" : "");
                }
            }

            @Override
            public final void bind(BindContext context) {
                context.bind(TupleImpl.this).bind(other);
            }
        }
    }

    private class InTuples extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long                                              serialVersionUID = -1806139685201770706L;

        private final QueryPartList<TupleImpl<T1, T2, T3, T4, T5, T6, T7, T8>> other;
        private final InOperator                                               operator;

        InTuples(QueryPartList<TupleImpl<T1, T2, T3, T4, T5, T6, T7, T8>> other, InOperator operator) {
            this.other = other;
            this.operator = operator;
        }

        @Override
        public final void toSQL(RenderContext context) {
            delegate(context).toSQL(context);
        }

        @Override
        public final void bind(BindContext context) {
            delegate(context).bind(context);
        }

        private final QueryPartInternal delegate(Configuration configuration) {
            if (asList(ASE, DB2, DERBY, FIREBIRD, INGRES, SQLSERVER, SQLITE, SYBASE).contains(configuration.getDialect())) {
                List<Condition> conditions = new ArrayList<Condition>();

                for (TupleImpl<T1, T2, T3, T4, T5, T6, T7, T8> tuple : other) {
                    conditions.add(new Compare(tuple, EQUALS));
                }

                Condition result = new CombinedCondition(Operator.OR, conditions);

                if (operator == NOT_IN) {
                    result = result.not();
                }

                return (QueryPartInternal) result;
            }
            else {
                return new Native();
            }
        }

        private class Native extends AbstractCondition {

            /**
             * Generated UID
             */
            private static final long serialVersionUID = -7019193803316281371L;

            @Override
            public final void toSQL(RenderContext context) {
                context.sql(TupleImpl.this)
                       .sql(" ")
                       .keyword(operator.toSQL())
                       .sql(" (")
                       .sql(other)
                       .sql(")");
            }

            @Override
            public final void bind(BindContext context) {
                context.bind(TupleImpl.this).bind((QueryPart) other);
            }

        }
    }

    private class InSubquery extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -1806139685201770706L;

        private final Select<?>   other;
        private final InOperator  operator;

        InSubquery(Select<?> other, InOperator operator) {
            this.other = other;
            this.operator = operator;
        }

        @Override
        public final void toSQL(RenderContext context) {

            // Some databases need extra parentheses around the RHS
            boolean extraParentheses = asList(ORACLE).contains(context.getDialect());
            boolean subquery = context.subquery();

            context.sql(TupleImpl.this)
                   .sql(" ")
                   .keyword(operator.toSQL())
                   .sql(" (")
                   .sql(extraParentheses ? "(" : "")
                   .subquery(true)
                   .sql(other)
                   .subquery(subquery)
                   .sql(extraParentheses ? ")" : "")
                   .sql(")");
        }

        @Override
        public final void bind(BindContext context) {
            context.bind(TupleImpl.this).bind(other);
        }
    }
}
