/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.test._.testcases;

import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.values;
import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.RowN;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class ValuesConstructorTests<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public ValuesConstructorTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    @SuppressWarnings("unchecked")

    public void testValuesConstructor() throws Exception {
        Result<Record2<Integer, String>> r21 = create().selectFrom(values(row(1, "a"))).fetch();

        assertEquals(1, r21.size());
        assertEquals(2, r21.get(0).size());
        assertEquals("c1", r21.field(0).getName());
        assertEquals("c2", r21.field(1).getName());
        assertEquals(Integer.class, r21.field(0).getType());
        assertEquals(String.class, r21.field(1).getType());
        assertEquals("c1", r21.get(0).field(0).getName());
        assertEquals("c2", r21.get(0).field(1).getName());
        assertEquals(Integer.class, r21.get(0).field(0).getType());
        assertEquals(String.class, r21.get(0).field(1).getType());
        assertEquals(1, r21.getValue(0, 0));
        assertEquals("a", r21.getValue(0, 1));

        Result<Record3<Integer, String, Date>> r1 =
        create().selectFrom(values(
            row(1, "a", Date.valueOf("2013-01-01")),
            row(2, "b", Date.valueOf("2013-01-02"))).as("my_table", "int_col", "string_col", "date_col")).fetch();

        Result<Record> r2 =
        create().selectFrom(values(new RowN[] {
            row(new Object[] { 1, "a", Date.valueOf("2013-01-01")}),
            row(new Object[] { 2, "b", Date.valueOf("2013-01-02")})}).as("my_table", "int_col", "string_col", "date_col")).fetch();

        assertEquals(r1, r2);
    }

    public void testResultConstructor() throws Exception {
        Result<Record2<Integer, String>> booksResult =
        create().select(TBook_ID(), TBook_TITLE())
                .from(TBook())
                .orderBy(TBook_ID())
                .fetch();

        Table<Record2<Integer, String>> booksTable = table(booksResult);

        assertEquals(
            booksResult,
            create().selectFrom(booksTable).fetch());

        assertEquals(
            BOOK_IDS,
            create().select(booksTable.field(TBook_ID()))
                    .from(booksTable)
                    .fetch(TBook_ID()));
    }
}
