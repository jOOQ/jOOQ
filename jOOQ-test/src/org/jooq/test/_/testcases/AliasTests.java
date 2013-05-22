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
package org.jooq.test._.testcases;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.selectZero;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.zero;

import java.sql.Date;
import java.util.List;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class AliasTests<
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
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> {

    public AliasTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testAliasingSimple() throws Exception {
        Table<B> b = TBook().as("b");
        Field<Integer> b_ID = b.field(TBook_ID());

        List<Integer> ids = create().select(b_ID).from(b).orderBy(b_ID).fetch(b_ID);
        assertEquals(4, ids.size());
        assertEquals(BOOK_IDS, ids);

        Result<Record> books = create().select().from(b).orderBy(b_ID).fetch();
        assertEquals(4, books.size());
        assertEquals(BOOK_IDS, books.getValues(b_ID));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAliasingTablesAndFields() throws Exception {
        Table<B2S> b2s = TBookToBookStore().as("b2s", "b2s_1", "b2s_2", "b2s_3");
        Field<String> b2s1 = (Field<String>) b2s.field(0);
        Field<Integer> b2s2 = (Field<Integer>) b2s.field(1);
        Field<Integer> b2s3 = (Field<Integer>) b2s.field(2);

        assertEquals("b2s", b2s.getName());
        assertEquals("b2s_1", b2s1.getName());
        assertEquals("b2s_2", b2s2.getName());
        assertEquals("b2s_3", b2s3.getName());

        assertEquals("b2s_1", b2s.field("b2s_1").getName());
        assertEquals("b2s_2", b2s.field("b2s_2").getName());
        assertEquals("b2s_3", b2s.field("b2s_3").getName());

        // TODO: What happens with typed records?
        // B2S record1 = create().selectFrom(b2s).where(b2s3.eq(2)).fetchOne();
        // Record record1 = create().select().from(b2s).where(b2s3.eq(2)).fetchOne();
        Record record1 = create()
            .select(b2s1, b2s2, b2s3)
            .from(b2s)
            .where(b2s3.eq(2))
            .fetchOne();

        assertEquals("Ex Libris", record1.getValue("b2s_1"));
        assertEquals("Ex Libris", record1.getValue(b2s1));
        assertEquals(3, record1.getValue("b2s_2"));
        assertEquals(3, (int) record1.getValue(b2s2));
        assertEquals(2, record1.getValue("b2s_3"));
        assertEquals(2, (int) record1.getValue(b2s3));
    }

    @Test
    public void testAliasingSelectAndFields() throws Exception {
        Record r1 = create().select().from(table(selectOne()).as("t", "v")).fetchOne();
        assertEquals("v", r1.field(0).getName());
        assertEquals("v", r1.field("v").getName());
        assertEquals(1, r1.getValue(0));
        assertEquals(1, r1.getValue("v"));
        assertEquals(1, r1.getValue(r1.field(0)));

        Record r2 = create()
            .select()
            .from(table(selectOne()).as("t1", "v1"))
            .crossJoin(table(select(two(), zero())).as("t2", "v2a", "v2b"))
            .leftOuterJoin(table(selectOne()).as("t3", "v3"))
            .on("1 = 0")
            .fetchOne();
        assertEquals("v1", r2.field(0).getName());
        assertEquals("v2a", r2.field(1).getName());
        assertEquals("v2b", r2.field(2).getName());
        assertEquals("v3", r2.field(3).getName());
        assertEquals("v1", r2.field("v1").getName());
        assertEquals("v2a", r2.field("v2a").getName());
        assertEquals("v2b", r2.field("v2b").getName());
        assertEquals("v3", r2.field("v3").getName());

        assertEquals(1, r2.getValue(0));
        assertEquals(1, r2.getValue("v1"));
        assertEquals(2, r2.getValue(1));
        assertEquals(2, r2.getValue("v2a"));
        assertEquals(0, r2.getValue(2));
        assertEquals(0, r2.getValue("v2b"));
        assertNull(r2.getValue(3));
        assertNull(r2.getValue("v3"));
    }

    @Test
    public void testAliasingJoins() throws Exception {
        Record r1 = create()
            .select()
            .from(table(selectOne())
                .crossJoin(table(selectZero())).as("t", "a", "b"))
            .fetchOne();

        assertEquals("a", r1.field(0).getName());
        assertEquals("b", r1.field(1).getName());
        assertEquals("a", r1.field("a").getName());
        assertEquals("b", r1.field("b").getName());
        assertEquals(1, r1.getValue(0));
        assertEquals(0, r1.getValue(1));
        assertEquals(1, r1.getValue("a"));
        assertEquals(0, r1.getValue("b"));

    }

    @Test
    public void testAliasingDelete() throws Exception {
        switch (dialect()) {
            case SQLITE:
            case SQLSERVER:
                log.info("SKIPPING", "Aliasing DELETE tests");
                return;
        }

        jOOQAbstractTest.reset = false;
        Table<B2S> b = TBookToBookStore().as("b");

        assertEquals(2, create().delete(b).where(b.field(TBookToBookStore_BOOK_ID()).eq(1)).execute());
        assertEquals(4, create().fetchCount(selectFrom(TBookToBookStore())));

        assertEquals(4, create().delete(b).execute());
        assertEquals(0, create().fetchCount(selectFrom(TBookToBookStore())));
    }
}
