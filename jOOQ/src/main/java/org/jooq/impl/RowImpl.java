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
import static org.jooq.impl.Factory.row;
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
import org.jooq.Row1;
import org.jooq.Row2;
import org.jooq.Row3;
import org.jooq.Row4;
import org.jooq.Row5;
import org.jooq.Row6;
import org.jooq.Row7;
import org.jooq.Row8;
import org.jooq.RowN;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
class RowImpl<T1, T2, T3, T4, T5, T6, T7, T8> extends AbstractNamedQueryPart
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
    RowN {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -929427349071556318L;

    private final Field<?>[]  fields;

    RowImpl(Field<?>... fields) {
        super("row");

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
    // XXX: Row DSL API
    // ------------------------------------------------------------------------

    @Override
    public final Condition equal(Row1<T1> row) {
        return new Compare(row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row2<T1, T2> row) {
        return new Compare(row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row3<T1, T2, T3> row) {
        return new Compare(row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row4<T1, T2, T3, T4> row) {
        return new Compare(row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row5<T1, T2, T3, T4, T5> row) {
        return new Compare(row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new Compare(row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return new Compare(row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return new Compare(row, Comparator.EQUALS);
    }

    @Override
    public final Condition equal(RowN row) {
        return new Compare(row, Comparator.EQUALS);
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
    public final Condition eq(RowN row) {
        return equal(row);
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
    public final Condition notEqual(Row1<T1> row) {
        return new Compare(row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row2<T1, T2> row) {
        return new Compare(row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row3<T1, T2, T3> row) {
        return new Compare(row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row4<T1, T2, T3, T4> row) {
        return new Compare(row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row5<T1, T2, T3, T4, T5> row) {
        return new Compare(row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row6<T1, T2, T3, T4, T5, T6> row) {
        return new Compare(row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row7<T1, T2, T3, T4, T5, T6, T7> row) {
        return new Compare(row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(Row8<T1, T2, T3, T4, T5, T6, T7, T8> row) {
        return new Compare(row, Comparator.NOT_EQUALS);
    }

    @Override
    public final Condition notEqual(RowN row) {
        return new Compare(row, Comparator.NOT_EQUALS);
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
    public final Condition ne(RowN row) {
        return notEqual(row);
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
    public final Condition in(RowN... rows) {
        return in(Arrays.asList(rows));
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
    public final Condition notIn(RowN... rows) {
        return notIn(Arrays.asList(rows));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Condition in(Collection rows) {
        QueryPartList<RowImpl<T1, T2, T3, T4, T5, T6, T7, T8>> list = new QueryPartList<RowImpl<T1, T2, T3, T4, T5, T6, T7, T8>>(rows);
        return new InRows(list, InOperator.IN);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Condition notIn(Collection rows) {
        QueryPartList<RowImpl<T1, T2, T3, T4, T5, T6, T7, T8>> list = new QueryPartList<RowImpl<T1, T2, T3, T4, T5, T6, T7, T8>>(rows);
        return new InRows(list, InOperator.NOT_IN);
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
        return new Overlaps(row);
    }

    // ------------------------------------------------------------------------
    // XXX: Implementation classes
    // ------------------------------------------------------------------------

    private class Overlaps extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long    serialVersionUID = 85887551884667824L;

        private final RowImpl<T1, T2, ?, ?, ?, ?, ?, ?> other;

        @SuppressWarnings("unchecked")
        Overlaps(Row2<T1, T2> other) {
            this.other = (RowImpl<T1, T2, ?, ?, ?, ?, ?, ?>) other;
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
                       .sql(RowImpl.this)
                       .keyword(" overlaps ")
                       .sql(other)
                       .sql(")");
            }

            @Override
            public final void bind(BindContext context) {
                context.bind(RowImpl.this).bind(other);
            }
        }
    }

    private class Compare extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long                               serialVersionUID = -1806139685201770706L;

        private final RowImpl<T1, T2, T3, T4, T5, T6, T7, T8> other;
        private final Comparator                                comparator;

        @SuppressWarnings("unchecked")
        Compare(QueryPart other, Comparator comparator) {
            this.other = (RowImpl<T1, T2, T3, T4, T5, T6, T7, T8>) other;
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

                // Some dialects do not support != comparison with rows
                if (comparator == NOT_EQUALS && asList(DB2).contains(context.getDialect())) {
                    context.keyword("not(")
                           .sql(RowImpl.this)
                           .sql(" = ")
                           .sql(other)
                           .sql(")");
                }
                else {
                    // Some databases need extra parentheses around the RHS
                    boolean extraParentheses = asList(ORACLE).contains(context.getDialect());

                    context.sql(RowImpl.this)
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
                context.bind(RowImpl.this).bind(other);
            }
        }
    }

    private class InRows extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long                                            serialVersionUID = -1806139685201770706L;

        private final QueryPartList<RowImpl<T1, T2, T3, T4, T5, T6, T7, T8>> other;
        private final InOperator                                             operator;

        InRows(QueryPartList<RowImpl<T1, T2, T3, T4, T5, T6, T7, T8>> other, InOperator operator) {
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

                for (RowImpl<T1, T2, T3, T4, T5, T6, T7, T8> row : other) {
                    conditions.add(new Compare(row, EQUALS));
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
                context.sql(RowImpl.this)
                       .sql(" ")
                       .keyword(operator.toSQL())
                       .sql(" (")
                       .sql(other)
                       .sql(")");
            }

            @Override
            public final void bind(BindContext context) {
                context.bind(RowImpl.this).bind((QueryPart) other);
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

            context.sql(RowImpl.this)
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
            context.bind(RowImpl.this).bind(other);
        }
    }
}
