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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.RecordContext;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultRecordListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class RecordListenerTests<
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

    public RecordListenerTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testRecordListenerLoad() throws Exception {

        // Check if lifecycle is handled correctly when initialising new records.
        ReadListener listener1 = new ReadListener();
        create(listener1).newRecord(TBook());
        assertEquals(asList("loadStart", "loadEnd"), listener1.events);

        // Check if lifecycle is handled correctly when loading records from the DB.
        ReadListener listener2 = new ReadListener();
        B book1 =
        create(listener2)
            .selectFrom(TBook())
            .where(TBook_ID().eq(1))
            .fetchOne();
        assertEquals(asList("loadStart", "loadEnd"), listener2.events);

        // Check if lifecycle is handled correctly when copying, moving records.
        listener2.events.clear();
        B book2 = book1.copy();
        assertEquals(asList("loadStart", "loadEnd"), listener2.events);

        listener2.events.clear();
        book2.into(TBook());
        assertEquals(asList("loadStart", "loadEnd"), listener2.events);
    }

    private static class ReadListener extends DefaultRecordListener {
        List<String> events = new ArrayList<String>();

        @Override
        public void loadStart(RecordContext ctx) {
            events.add("loadStart");
        }

        @Override
        public void loadEnd(RecordContext ctx) {
            events.add("loadEnd");
        }

        @Override
        public void refreshStart(RecordContext ctx) {
            events.add("refreshStart");
        }

        @Override
        public void refreshEnd(RecordContext ctx) {
            events.add("refreshEnd");
        }
    }

    public void testRecordListenerStore() throws Exception {
        jOOQAbstractTest.reset = false;

        WriteListener listener1 = new WriteListener();
        B book1 = newBook(5);
        book1.attach(create(listener1).configuration());
        assertEquals(1, book1.store());
        assertEquals(asList("storeStart", "insertStart", "insertEnd", "storeEnd"), listener1.events);

        listener1.events.clear();
        book1.setValue(TBook_TITLE(), "1234");
        assertEquals(1, book1.store());
        assertEquals(asList("storeStart", "updateStart", "updateEnd", "storeEnd"), listener1.events);

        WriteListener listener2 = new WriteListener();
        B book2 = newBook(6);
        book2.attach(create(listener2).configuration());
        assertEquals(1, book2.insert());
        assertEquals(asList("insertStart", "insertEnd"), listener2.events);

        listener2.events.clear();
        book2.setValue(TBook_TITLE(), "1234");
        assertEquals(1, book2.update());
        assertEquals(asList("updateStart", "updateEnd"), listener2.events);

        listener2.events.clear();
        assertEquals(1, book2.delete());
        assertEquals(asList("deleteStart", "deleteEnd"), listener2.events);
    }

    public void testRecordListenerWithException() throws Exception {
        jOOQAbstractTest.reset = false;
        WriteListener listener1 = new WriteListener();

        B book = create(listener1).fetchOne(TBook(), TBook_ID().eq(1));

        try {
            book.changed(true);
            book.insert();
            fail();
        }
        catch (DataAccessException expected) {}

        assertEquals(asList("insertStart", "insertEnd"), listener1.events);
        assertEquals(1, listener1.exceptions.size());
    }

    public void testRecordListenerBatchStore() throws Exception {
        jOOQAbstractTest.reset = false;
        WriteListener listener1 = new WriteListener();

        B book1 = newBook(5);
        B book2 = newBook(6);

        create(listener1).batchStore(book1, book2).execute();
        assertEquals(asList(
            "storeStart",
            "insertStart",
            "insertEnd",
            "storeEnd",
            "storeStart",
            "insertStart",
            "insertEnd",
            "storeEnd"),
        listener1.events);
    }

    public void testRecordListenerRefresh() throws Exception {
        B book =
        create()
            .selectFrom(TBook())
            .where(TBook_ID().eq(1))
            .fetchOne();

        ReadListener listener1 = new ReadListener();
        book.attach(create(listener1).configuration());

        // TODO: There is an internal load operation involved here. Is that
        // really the desired behaviour?
        book.refresh();
        assertEquals(asList("loadStart", "loadEnd", "refreshStart", "refreshEnd"), listener1.events);
    }

    private static class WriteListener extends DefaultRecordListener {
        List<String> events = new ArrayList<String>();
        List<Exception> exceptions = new ArrayList<Exception>();

        @Override
        public void storeStart(RecordContext ctx) {
            events.add("storeStart");
        }

        @Override
        public void storeEnd(RecordContext ctx) {
            events.add("storeEnd");
        }

        @Override
        public void insertStart(RecordContext ctx) {
            events.add("insertStart");
        }

        @Override
        public void insertEnd(RecordContext ctx) {
            events.add("insertEnd");
        }

        @Override
        public void updateStart(RecordContext ctx) {
            events.add("updateStart");
        }

        @Override
        public void updateEnd(RecordContext ctx) {
            events.add("updateEnd");
        }

        @Override
        public void deleteStart(RecordContext ctx) {
            events.add("deleteStart");
        }

        @Override
        public void deleteEnd(RecordContext ctx) {
            events.add("deleteEnd");
        }

        @Override
        public void exception(RecordContext ctx) {
            exceptions.add(ctx.exception());
        }
    }
}