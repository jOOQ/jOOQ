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

import static java.util.Arrays.asList;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

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
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

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
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public GeneralTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testLiterals() throws Exception {
        Record record = create().select(zero(), one(), two(), pi(), e(), rad(deg(pi()))).fetchOne();

        assertEquals(0, record.getValue(0));
        assertEquals(1, record.getValue(1));
        assertEquals(2, record.getValue(2));
        assertEquals("3.141", record.getValue(3, String.class).substring(0, 5));
        assertEquals("2.718", record.getValue(4, String.class).substring(0, 5));
        assertEquals("3.141", record.getValue(5, String.class).substring(0, 5));
    }

    public void testSequences() throws Exception {
        testSequences0(SAuthorID());
    }

    public void testSequenceByName() throws Exception {
        Sequence<? extends Number> sequence = SAuthorID();

        if (sequence != null) {
            testSequences0(DSL.sequenceByName(sequence.getSchema().getName(), sequence.getName()));
        }
    }

    private void testSequences0(Sequence<? extends Number> sequence) {
        if (cSequences() == null) {
            log.info("SKIPPING", "sequences test");
            return;
        }

        jOOQAbstractTest.reset = false;

        Field<? extends Number> nextval = sequence.nextval();
        Field<? extends Number> currval = null;

        assertEquals("3", "" + create().select(nextval).fetchOne(nextval));
        assertEquals("4", "" + create().select(nextval).fetchOne(nextval));
        assertEquals("5", "" + create().select(nextval).fetchOne(nextval));

        switch (dialect()) {
            // HSQLDB and DERBY don't support currval, so don't test it
            case HSQLDB:
            case DERBY:

            /* [pro] xx
            xx xxxxxx xxx xxxx xxxxx xxxxxx xxxxxxxx xxxx xxxx xxxxx xxx xxx
            xx xxxxxxxxx xxxxxx xxxxxxx xxxxx
            xxxx xxxxxxx
                xxxxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxxx xxxxxxxx
                xxxxxx

            xx [/pro] */
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

    @SuppressWarnings("unchecked")

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

    public void testEquals() throws Exception {

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
