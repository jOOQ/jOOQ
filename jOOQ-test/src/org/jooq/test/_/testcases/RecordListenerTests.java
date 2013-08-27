/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

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
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> {

    public RecordListenerTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testRecordListenerLoad() throws Exception {

        // Check if lifecycle is handled correctly when initialising new records.
        SimpleRecordListener listener1 = new SimpleRecordListener();
        create(listener1).newRecord(TBook());
        assertEquals(asList("loadStart", "loadEnd"), listener1.events);

        // Check if lifecycle is handled correctly when loading records from the DB.
        SimpleRecordListener listener2 = new SimpleRecordListener();
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

    @Test
    public void testRecordListenerStore() throws Exception {
        jOOQAbstractTest.reset = false;

        SimpleRecordListener listener1 = new SimpleRecordListener();
        B book1 = newBook(5);
        book1.attach(create(listener1).configuration());
        assertEquals(1, book1.store());
        assertEquals(asList("storeStart", "insertStart", "insertEnd", "storeEnd"), listener1.events);

        listener1.events.clear();
        book1.setValue(TBook_TITLE(), "1234");
        assertEquals(1, book1.store());
        assertEquals(asList("storeStart", "updateStart", "updateEnd", "storeEnd"), listener1.events);

        SimpleRecordListener listener2 = new SimpleRecordListener();
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

    @Test
    public void testRecordListenerBatchStore() throws Exception {
        jOOQAbstractTest.reset = false;
        SimpleRecordListener listener1 = new SimpleRecordListener();

        B book1 = newBook(5);
        B book2 = newBook(6);

        create(listener1).batchStore(book1, book2).execute();

        System.out.println(listener1.events);
        throw new RuntimeException("Support for RecordListener and batch store is not yet implemented");

//        Result<B> books =
//        create().selectFrom(TBook())
//                .orderBy(TBook_ID().asc())
//                .fetch();
//
//        for (int i = 0; i < books.size(); i++) {
//            books.get(i).attach(create(listener1).configuration());
//            books.get(i).setValue(TBook_TITLE(), "Title " + i);
//        }
    }

    @Test
    public void testRecordListenerRefresh() throws Exception {
        B book =
        create()
            .selectFrom(TBook())
            .where(TBook_ID().eq(1))
            .fetchOne();

        SimpleRecordListener listener1 = new SimpleRecordListener();
        book.attach(create(listener1).configuration());

        // TODO: There is an internal load operation involved here. Is that
        // really the desired behaviour?
        book.refresh();
        assertEquals(asList("loadStart", "loadEnd", "refreshStart", "refreshEnd"), listener1.events);
    }

    private static class SimpleRecordListener extends DefaultRecordListener {
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
        public void refreshStart(RecordContext ctx) {
            events.add("refreshStart");
        }

        @Override
        public void refreshEnd(RecordContext ctx) {
            events.add("refreshEnd");
        }
    }
}