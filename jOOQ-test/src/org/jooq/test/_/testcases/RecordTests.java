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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> {

    public RecordTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
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

    @Test
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

    @Test
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
