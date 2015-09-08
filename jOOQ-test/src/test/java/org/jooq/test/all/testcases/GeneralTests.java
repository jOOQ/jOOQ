/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.test.all.testcases;

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.castNull;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.deg;
import static org.jooq.impl.DSL.e;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.pi;
import static org.jooq.impl.DSL.rad;
import static org.jooq.impl.DSL.sequence;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.zero;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;

import java.sql.Date;
import java.util.Arrays;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.jooq.Sequence;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.conf.Settings;
import org.jooq.exception.DetachedException;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

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
    CS   extends UpdatableRecord<CS>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> {

    public GeneralTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
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
        assumeNotNull(SAuthorID());

        testSequences0(SAuthorID());
    }

    public void testSequenceByName() throws Exception {
        assumeNotNull(SAuthorID());

        testSequences0(sequence(name(SAuthorID().getSchema().getName(), SAuthorID().getName())));
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

            /* [pro] */
            // Ingres has smoe weird issue, claiming that NEXT VALUE was not
            // requested before CURRENT VALUE
            case INGRES:
                log.info("SKIPPING", "Sequence CURRVAL tests");
                break;

            /* [/pro] */
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
        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetchAny(),
                     create().selectFrom(TBook()).orderBy(TBook_ID()).fetchAny());
        assertEquals(create().selectFrom   (TBook()).orderBy(TBook_ID()).fetchAny(),
                     create().select().from(TBook()).orderBy(TBook_ID()).fetchAny());

        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).orderBy(TBook_ID()).fetchAny(),
                     create().select(TBook_ID(), TBook_TITLE()).from(TBook()).orderBy(TBook_ID()).fetchAny());
        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).orderBy(TBook_ID()).fetchAny(),
                     create().select(TBook_ID(), trim(TBook_TITLE())).from(TBook()).orderBy(TBook_ID()).fetchAny());

        assertFalse(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).orderBy(TBook_ID()).fetchAny().equals(
                    create().select(TBook_TITLE(), TBook_ID()).from(TBook()).orderBy(TBook_ID()).fetchAny()));

        // Result.equals()
        // ---------------
        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetch(),
                     create().selectFrom(TBook()).orderBy(TBook_ID()).fetch());
        assertEquals(create().selectFrom   (TBook()).orderBy(TBook_ID()).fetch(),
                     create().select().from(TBook()).orderBy(TBook_ID()).fetch());
        assertEquals(create().selectFrom   (TBook()).orderBy(TBook_ID()).limit(1).fetch(),
                     create().select().from(TBook()).orderBy(TBook_ID()).limit(1).fetch());

        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).orderBy(TBook_ID()).fetch(),
                     create().select(TBook_ID(), TBook_TITLE()).from(TBook()).orderBy(TBook_ID()).fetch());
        assertEquals(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).orderBy(TBook_ID()).fetch(),
                     create().select(TBook_ID(), trim(TBook_TITLE())).from(TBook()).orderBy(TBook_ID()).fetch());

        assertFalse(create().selectFrom(TBook()).orderBy(TBook_ID().asc()).fetch().equals(
                    create().selectFrom(TBook()).orderBy(TBook_ID().desc()).fetch()));

        assertFalse(create().select(TBook_ID(), TBook_TITLE()).from(TBook()).orderBy(TBook_ID()).fetch().equals(
                    create().select(TBook_TITLE(), TBook_ID()).from(TBook()).orderBy(TBook_ID()).fetch()));
    }
}
