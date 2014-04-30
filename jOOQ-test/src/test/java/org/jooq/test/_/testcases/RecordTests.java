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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.reflect.Reflect;

import org.junit.Test;

public class RecordTests<
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

    public RecordTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testRecordOriginals() throws Exception {
        B book = create().selectFrom(TBook()).where(TBook_ID().eq(1)).fetchOne();
        B orig = book.original();

        assertEquals(book, orig);
        assertEquals(book.getValue(TBook_ID()), orig.getValue(TBook_ID()));
        assertEquals(book.getValue(TBook_TITLE()), orig.getValue(TBook_TITLE()));
        testOriginalMethods(book, orig);

        book.setValue(TBook_TITLE(), "abc");
        assertFalse(book.equals(orig));
        assertFalse(book.equals(book.original()));
        assertEquals("abc", book.getValue(TBook_TITLE()));
        assertEquals(BOOK_TITLES.get(0), orig.getValue(TBook_TITLE()));
        testOriginalMethods(book, orig);

        book = orig;
        orig = orig.original();
        book.setValue(TBook_TITLE(), "abc");
        assertFalse(book.equals(orig));
        assertFalse(book.equals(book.original()));
        assertEquals("abc", book.getValue(TBook_TITLE()));
        assertEquals(BOOK_TITLES.get(0), orig.getValue(TBook_TITLE()));
        testOriginalMethods(book, orig);
    }

    private void testOriginalMethods(B changed, B original) {
        for (Field<?> field : changed.fields()) {
            assertEquals(changed.original(field), original.getValue(field));
        }
    }

    public void testRecordChanged() throws Exception {
        B book = create().selectFrom(TBook()).where(TBook_ID().eq(1)).fetchOne();

        assertFalse(book.changed());
        assertFalse(book.changed(TBook_TITLE()));
        assertFalse(book.changed(TBook_TITLE().getName()));
        book.setValue(TBook_TITLE(), "abc");
        assertTrue(book.changed());
        assertTrue(book.changed(TBook_TITLE()));
        assertTrue(book.changed(TBook_TITLE().getName()));

        book.changed(false);
        assertFalse(book.changed());
        assertFalse(book.changed(TBook_TITLE()));
        assertFalse(book.changed(TBook_TITLE().getName()));
        assertEquals("abc", book.original().getValue(TBook_TITLE()));
        assertEquals("abc", book.original(TBook_TITLE()));

        book.changed(true);
        assertTrue(book.changed());
        assertTrue(book.changed(TBook_TITLE()));
        assertTrue(book.changed(TBook_TITLE().getName()));
        assertEquals("abc", book.original().getValue(TBook_TITLE()));
        assertEquals("abc", book.original(TBook_TITLE()));

        book.changed(false);
        book.changed(TBook_TITLE(), true);
        assertTrue(book.changed());
        assertTrue(book.changed(TBook_TITLE()));
        assertTrue(book.changed(TBook_TITLE().getName()));
        assertEquals("abc", book.original().getValue(TBook_TITLE()));
        assertEquals("abc", book.original(TBook_TITLE()));
    }

    public void testRecordChangedOnGeneratedMethods() throws Exception {

        // [#2798] Generated methods might show a different behaviour with respect to changed flags,
        // compared to regular API.
        B b1 = create().selectFrom(TBook()).where(TBook_ID().eq(1)).fetchOne();
        B b2 = create().selectFrom(TBook()).where(TBook_ID().eq(1)).fetchOne();

        b1.setValue(TBook_ID(), 1);
        Reflect.on(b2).call("setId", 1);

        assertEquals(b1, b2);
        assertFalse(b1.changed());
        assertFalse(b2.changed());
        assertFalse(b1.changed(TBook_ID()));
        assertFalse(b2.changed(TBook_ID()));

        b1.setValue(TBook_ID(), 2);
        Reflect.on(b2).call("setId", 2);

        assertEquals(b1, b2);
        assertTrue(b1.changed());
        assertTrue(b2.changed());
        assertTrue(b1.changed(TBook_ID()));
        assertTrue(b2.changed(TBook_ID()));
    }

    public void testRecordReset() throws Exception {
        B book = create().selectFrom(TBook()).where(TBook_ID().eq(1)).fetchOne();

        book.setValue(TBook_TITLE(), "abc");
        book.reset();
        assertFalse(book.changed());
        assertFalse(book.changed(TBook_TITLE()));
        assertFalse(book.changed(TBook_TITLE().getName()));
        assertEquals("1984", book.getValue(TBook_TITLE()));

        book.setValue(TBook_TITLE(), "abc");
        book.reset(TBook_TITLE());
        assertFalse(book.changed());
        assertFalse(book.changed(TBook_TITLE()));
        assertFalse(book.changed(TBook_TITLE().getName()));
        assertEquals("1984", book.getValue(TBook_TITLE()));

        book.setValue(TBook_TITLE(), "abc");
        book.reset(TBook_TITLE().getName());
        assertFalse(book.changed());
        assertFalse(book.changed(TBook_TITLE()));
        assertFalse(book.changed(TBook_TITLE().getName()));
        assertEquals("1984", book.getValue(TBook_TITLE()));
    }
}
