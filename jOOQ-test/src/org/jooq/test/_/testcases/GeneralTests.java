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

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.jooq.impl.DSL.castNull;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.deg;
import static org.jooq.impl.DSL.e;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.pi;
import static org.jooq.impl.DSL.rad;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.zero;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.util.Arrays;

import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Sequence;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.conf.Settings;
import org.jooq.exception.DetachedException;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test.sqlite.generatedclasses.Tables;
import org.jooq.test.sqlite.generatedclasses.tables.records.ProjectRecord;

import org.junit.Test;

public class GeneralTests<
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

    public GeneralTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testLiterals() throws Exception {
        Record record = create().select(zero(), one(), two(), pi(), e(), rad(deg(pi()))).fetchOne();

        assertEquals(0, record.getValue(0));
        assertEquals(1, record.getValue(1));
        assertEquals(2, record.getValue(2));
        assertEquals("3.141", record.getValue(3, String.class).substring(0, 5));
        assertEquals("2.718", record.getValue(4, String.class).substring(0, 5));
        assertEquals("3.141", record.getValue(5, String.class).substring(0, 5));
    }

    @Test
    public void testSequences() throws Exception {
        if (cSequences() == null) {
            log.info("SKIPPING", "sequences test");
            return;
        }

        jOOQAbstractTest.reset = false;

        Sequence<? extends Number> sequence = SAuthorID();
        Field<? extends Number> nextval = sequence.nextval();
        Field<? extends Number> currval = null;

        assertEquals("3", "" + create().select(nextval).fetchOne(nextval));
        assertEquals("4", "" + create().select(nextval).fetchOne(nextval));
        assertEquals("5", "" + create().select(nextval).fetchOne(nextval));

        switch (dialect()) {
            // HSQLDB and DERBY don't support currval, so don't test it
            case HSQLDB:
            case DERBY:

            // Ingres has smoe weird issue, claiming that NEXT VALUE was not
            // requested before CURRENT VALUE
            case INGRES:
                log.info("SKIPPING", "Sequence CURRVAL tests");
                break;

            default:
                currval = sequence.currval();
                assertEquals("5", "" + create().select(currval).fetchOne(currval));
                assertEquals("5", "" + create().select(currval).fetchOne(currval));

                assertEquals(5, create().currval(sequence).intValue());
                assertEquals(5, create().currval(sequence).intValue());
        }

        assertEquals("6", "" + create().select(nextval).fetchOne(nextval));

        // Test convenience syntax
        assertEquals(7, create().nextval(sequence).intValue());
        assertEquals(8, create().nextval(sequence).intValue());
    }

    @Test
    public void testAccessInternalRepresentation() throws Exception {
        SelectQuery<Record1<Integer>> query =
        create().select(TBook_ID())
                .from(TBook())
                .where(TBook_ID().in(1, 2, 3))
                .getQuery();

        query.addGroupBy(TBook_ID());
        query.addHaving(count().greaterOrEqual(1));
        query.addOrderBy(TBook_ID());
        query.execute();

        Result<Record1<Integer>> result = query.getResult();

        assertEquals(3, result.size());
        assertEquals(Arrays.asList(1, 2, 3), result.getValues(TBook_ID()));
    }

    @Test
    public void testSerialisation() throws Exception {
        jOOQAbstractTest.reset = false;

        Select<A> q = create().selectFrom(TAuthor()).orderBy(TAuthor_LAST_NAME());

        // Serialising the unexecuted query
        // ---------------------------------------------------------------------
        q = runSerialisation(q);

        try {
            q.execute();
            fail();
        } catch (DetachedException expected) {}

        // Serialising the executed query
        // ---------------------------------------------------------------------
        create().attach(q);
        assertEquals(2, q.execute());
        assertEquals("Coelho", q.getResult().getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Orwell", q.getResult().getValue(1, TAuthor_LAST_NAME()));

        q = runSerialisation(q);
        assertEquals("Coelho", q.getResult().getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Orwell", q.getResult().getValue(1, TAuthor_LAST_NAME()));

        Result<A> result = q.getResult();
        result = runSerialisation(result);
        assertEquals("Coelho", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Orwell", result.getValue(1, TAuthor_LAST_NAME()));

        try {
            result.get(1).setValue(TAuthor_FIRST_NAME(), "Georgie");
            result.get(1).store();
            fail();
        } catch (DetachedException expected) {}

        create().attach(result);
        assertEquals(1, result.get(1).store());
        assertEquals("Georgie", create()
                .fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Orwell"))
                .getValue(TAuthor_FIRST_NAME()));

        // [#1191] Check execution capabilities with new features in ExecuteListener
        ConnectionProviderListener.c = create().configuration().connectionProvider().acquire();
        try {
            DSLContext create = create(new ConnectionProviderListener());
            q = create
                    .selectFrom(TAuthor())
                    .orderBy(TAuthor_LAST_NAME());
            q = runSerialisation(q);
            q.execute();

            result = q.getResult();
            result = runSerialisation(result);
            assertEquals("Coelho", result.getValue(0, TAuthor_LAST_NAME()));
            assertEquals("Orwell", result.getValue(1, TAuthor_LAST_NAME()));

            result.get(1).setValue(TAuthor_FIRST_NAME(), "Gee-Gee");
            result.get(1).store();
        }
        finally {
            create().configuration().connectionProvider().release(ConnectionProviderListener.c);
            ConnectionProviderListener.c = null;
        }

        // [#1071] Check sequences
        if (cSequences() == null) {
            log.info("SKIPPING", "sequences test");
        }
        else {
            Select<?> s;

            s = create().select(SAuthorID().nextval(), SAuthorID().currval());
            s = runSerialisation(s);
        }
    }

    public static class ConnectionProviderListener extends DefaultExecuteListener {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7399239846062763212L;

        static Connection c;

        @Override
        public void start(ExecuteContext ctx) {
            ctx.connectionProvider(new DefaultConnectionProvider(c));
        }
    }

    @SuppressWarnings("unchecked")
    private <Z> Z runSerialisation(Z value) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(out);
        o.writeObject(value);
        o.flush();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream i = new ObjectInputStream(in);
        return (Z) i.readObject();
    }

    @Test
    public void testAttachable() throws Exception {
        jOOQAbstractTest.reset = false;
        DSLContext create = create();

        S store1 = create.newRecord(TBookStore());
        assertNotNull(store1);

        store1.setValue(TBookStore_NAME(), "Barnes and Noble");
        assertEquals(1, store1.store());

        S store2 = create.newRecord(TBookStore());
        store2.setValue(TBookStore_NAME(), "Barnes and Noble");
        store2.attach(null);
        failStoreRefreshDelete(store2);

        store2.attach(create.configuration());
        store2.refresh();
        assertEquals(1, store2.delete());
        assertNull(create.fetchOne(TBookStore(), TBookStore_NAME().equal("Barnes and Noble")));

        // [#1685] Create or fetch detached records
        create = create(new Settings().withAttachRecords(false));

        S store3 = create.newRecord(TBookStore());
        store3.setValue(TBookStore_NAME(), "Barnes and Noble");
        failStoreRefreshDelete(store3);
        store3.attach(create.configuration());
        assertEquals(1, store3.store());

        S store4 = create.newRecord(TBookStore());
        store4.setValue(TBookStore_NAME(), "Barnes and Noble");
        failStoreRefreshDelete(store4);
        store4 = create.fetchOne(TBookStore(), TBookStore_NAME().equal("Barnes and Noble"));
        store4.setValue(TBookStore_NAME(), "ABC");
        failStoreRefreshDelete(store4);
        store4 = create.fetchOne(TBookStore(), TBookStore_NAME().equal("Barnes and Noble"));
        store4.attach(create.configuration());
        assertEquals(1, store4.delete());
        assertNull(create.fetchOne(TBookStore(), TBookStore_NAME().equal("Barnes and Noble")));
        assertNull(create.fetchOne(TBookStore(), TBookStore_NAME().equal("ABC")));
    }

    private void failStoreRefreshDelete(S store) {
        try {
            store.store();
            fail();
        }
        catch (DetachedException expected) {}

        try {
            store.refresh();
            fail();
        }
        catch (DetachedException expected) {}

        try {
            store.delete();
            fail();
        }
        catch (DetachedException expected) {}
    }

    @Test
    public void testDual() throws Exception {
        assertEquals(1, (int) create().selectOne().fetchOne(0, Integer.class));
        assertEquals(1, (int) create().selectOne().where(one().equal(1)).fetchOne(0, Integer.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNULL() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#1083] There is a subtle difference in inlining NULL or binding it
        Field<Integer> n1 = castNull(Integer.class);
        Field<Integer> n2 = val(null, Integer.class);
        Field<Integer> c = val(1);

        for (Field<Integer> n : asList(n1, n2)) {
            assertEquals(null, create().select(n).fetchOne(n));
            assertEquals(Integer.valueOf(1), create().select(c).from(TAuthor()).where(TAuthor_ID().equal(1)).and(n.isNull()).fetchOne(c));
            assertEquals(null, create().selectOne().from(TAuthor()).where(n.isNotNull()).fetchAny());
        }

        UpdateQuery<A> u = create().updateQuery(TAuthor());
        u.addValue(TAuthor_YEAR_OF_BIRTH(), (Integer) null);
        u.execute();

        Result<A> records = create()
            .selectFrom(TAuthor())
            .where(TAuthor_YEAR_OF_BIRTH().isNull())
            .fetch();
        assertEquals(2, records.size());
        assertEquals(null, records.getValue(0, TAuthor_YEAR_OF_BIRTH()));
    }

    @Test
    public void testEquals() throws Exception {
        ProjectRecord record = create().newRecord(Tables.PROJECT);
        record.setCode("billyboytest");
        record.setCompany("code1234");
        record.setName("nca");
        record.insert();

        if (true) return;
        // 'billyboytest', 'code1234', 'nca'

        // Record.equals()
        // ---------------
        assertEquals(create().selectFrom(TBook()).fetchAny(),
                     create().selectFrom(TBook()).fetchAny());
        assertEquals(create().selectFrom   (TBook()).fetchAny(),
                     create().select().from(TBook()).fetchAny());

        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetchAny(),
                     create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetchAny());
        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetchAny(),
                     create().select(TBook_ID(), trim(TBook_TITLE())).from(TBook()).fetchAny());

        assertFalse(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetchAny().equals(
                    create().select(TBook_TITLE(), TBook_ID()).from(TBook()).fetchAny()));

        // Result.equals()
        // ---------------
        assertEquals(create().selectFrom(TBook()).fetch(),
                     create().selectFrom(TBook()).fetch());
        assertEquals(create().selectFrom   (TBook()).fetch(),
                     create().select().from(TBook()).fetch());
        assertEquals(create().selectFrom   (TBook()).limit(1).fetch(),
                     create().select().from(TBook()).limit(1).fetch());

        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetch(),
                     create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetch());
        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetch(),
                     create().select(TBook_ID(), trim(TBook_TITLE())).from(TBook()).fetch());

        assertFalse(create().selectFrom(TBook()).orderBy(TBook_ID().asc()).fetch().equals(
                    create().selectFrom(TBook()).orderBy(TBook_ID().desc()).fetch()));

        assertFalse(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetch().equals(
                    create().select(TBook_TITLE(), TBook_ID()).from(TBook()).fetch()));
    }
}
