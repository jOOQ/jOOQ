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
        assertEquals(asList("storeStart", "storeEnd"), listener1.events);

        SimpleRecordListener listener2 = new SimpleRecordListener();
        B book2 = newBook(6);
        book2.attach(create(listener2).configuration());
        assertEquals(1, book2.insert());
        assertEquals(asList("insertStart", "insertEnd"), listener2.events);

        listener2.events.clear();
        book2.setValue(TBook_TITLE(), "1234");
        assertEquals(1, book2.update());
        assertEquals(asList("updateStart", "updateEnd"), listener2.events);
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