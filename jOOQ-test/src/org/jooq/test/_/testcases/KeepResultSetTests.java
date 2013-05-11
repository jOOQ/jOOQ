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
import static org.jooq.KeepResultSetMode.CLOSE_AFTER_FETCH;
import static org.jooq.KeepResultSetMode.KEEP_AFTER_FETCH;
import static org.jooq.KeepResultSetMode.UPDATE_ON_CHANGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class KeepResultSetTests<
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

    public KeepResultSetTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    /**
     * This listener is used to check if a <code>SELECT</code> statement is
     * issued after a call to {@link Record#refresh()}.
     */
    private static class NoSelectAfterRefreshListener extends DefaultExecuteListener {

        /**
         * Default UID
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void start(ExecuteContext ctx) {
            super.start(ctx);

            if (ctx.query() instanceof Select) {
                for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
                    if (e.getMethodName().equals("refresh")) {
                        fail("Record.refresh() should not execute any queries");
                    }
                }
            }
        }
    }

    private void testFailUpdateRow(ResultSet rs) {
        try {
            rs.updateRow();
            fail();
        }
        catch (SQLException expected) {}

    }

    private void testFailRefresh(Record record) {
        try {
            record.refresh();
            fail();
        }
        catch (DataAccessException expected) {}
    }

    @Test
    public void testKeepRSWithCloseAfterFetch() throws Exception {
        DSLContext create = create(new NoSelectAfterRefreshListener());

        Result<B> b1 = create.selectFrom(TBook()).fetch();
        assertNull(b1.resultSet());

        // Use plain SQL to prevent fetching of UpdatableRecord
        Result<Record> b2 = create.select().from(TBook().getName()).keepResultSet(CLOSE_AFTER_FETCH).fetch();
        assertNull(b2.resultSet());

        // Changing a TITLE has no effect
        Record r = b2.get(0);
        r.setValue(TBook_TITLE(), "XX");
        assertTrue(r.changed());
        assertFalse(r.original().equals(r));
        assertEquals(BOOK_TITLES.get(0), getBook(1).getValue(TBook_TITLE()));
        testFailRefresh(r);

        Cursor<Record> c1 = create.select().from(TBook().getName()).keepResultSet(CLOSE_AFTER_FETCH).fetchLazy();
        assertTrue(c1.closesAfterFetch());
        while (c1.hasNext()) {
            Result<Record> result = c1.fetch(1);
            assertNull(result.get(0).resultSet());
            assertNull(result.resultSet());
            assertNotNull(c1.resultSet());
        }

        assertNull(c1.resultSet());
    }

    private void testOriginalBook1(B book) {
        assertEquals(BOOK_TITLES.get(0), book.getValue(TBook_TITLE()));
        assertEquals(BOOK_AUTHOR_IDS.get(0), book.getValue(TBook_AUTHOR_ID()));
        assertFalse(book.changed());
        assertEquals(book.original(), book);
        assertNotNull(book.resultSet());
    }

    private void testModifiedBook1(B book) {
        book.setValue(TBook_TITLE(), "XX");
        book.setValue(TBook_AUTHOR_ID(), 15);
        assertEquals("XX", book.getValue(TBook_TITLE()));
        assertEquals(15, (int) book.getValue(TBook_AUTHOR_ID()));
        assertTrue(book.changed());
        assertFalse(book.original().equals(book));
    }

    @Test
    public void testKeepRSWithKeepAfterFetch() throws Exception {
        switch (dialect()) {
            case SQLITE:
                log.info("SKIPPING", "KeepResultSet tests");
                return;
        }

        DSLContext create = create(new NoSelectAfterRefreshListener());
        Result<B> b2 = create.selectFrom(TBook()).keepResultSet(KEEP_AFTER_FETCH).fetch();
        B r = b2.get(0);
        assertNotNull(b2.resultSet());
        assertNotNull(r.resultSet());
        testFailUpdateRow(b2.resultSet());

        testModifiedBook1(r);
        B dbBook = getBook(1);
        assertEquals(BOOK_TITLES.get(0), dbBook.getValue(TBook_TITLE()));
        assertEquals(BOOK_AUTHOR_IDS.get(0), dbBook.getValue(TBook_AUTHOR_ID()));

        // Refresh the record
        r.refresh(TBook_TITLE());
        assertEquals(BOOK_TITLES.get(0), r.getValue(TBook_TITLE()));
        assertEquals(15, (int) r.getValue(TBook_AUTHOR_ID()));
        assertTrue(r.changed());
        assertFalse(r.original().equals(r));

        r.refresh();
        testOriginalBook1(r);

        b2.close();
        assertNull(b2.resultSet());

        // Changing a TITLE still has no effect
        testModifiedBook1(r);

        // But refreshing should work through a new query (UpdatableRecord)
        // For this, remove the NoSelectAfterRefreshListener
        r.attach(create().configuration());
        r.refresh();
        testOriginalBook1(r);

        // Further refreshing should again not trigger new SQL statements
        r.attach(create.configuration());
        testModifiedBook1(r);

        r.refresh();
        testOriginalBook1(r);
        r.close();

        Cursor<Record> c1 = create.select().from(TBook().getName()).keepResultSet(KEEP_AFTER_FETCH).fetchLazy();
        assertFalse(c1.closesAfterFetch());
        while (c1.hasNext()) {
            Result<Record> result = c1.fetch(1);
            assertNotNull(result.get(0).resultSet());
            assertNotNull(result.resultSet());
            assertNotNull(c1.resultSet());
        }

        assertNotNull(c1.resultSet());
        c1.close();
        assertNull(c1.resultSet());
    }

    @Test
    public void testKeepRSWithUpdateOnChange() throws Exception {
        switch (dialect()) {
            case SQLITE:
                log.info("SKIPPING", "KeepResultSet tests");
                return;
        }

        jOOQAbstractTest.reset = false;
        DSLContext create = create(new NoSelectAfterRefreshListener());

        // Use plain SQL to prevent fetching of UpdatableRecord
        Result<B> books =
        create.selectFrom(TBook())
              .orderBy(TBook_ID())
              .keepResultSet(UPDATE_ON_CHANGE)
              .fetch();

        assertNotNull(books.resultSet());
        for (int i = 0; i < books.size(); i++) {
            assertNotNull(books.get(i).resultSet());
            books.get(i).setValue(TBook_TITLE(), "Title " + i);
        }

        Result<B> booksTest = getBooks();
        assertEquals(
            asList("Title 0", "Title 1", "Title 2", "Title 3"),
            booksTest.getValues(TBook_TITLE()));

        // After closing, setting values to records should no longer have any
        // effect
        assertNotNull(books.resultSet());
        books.close();
        assertNull(books.resultSet());
        books.get(0).setValue(TBook_TITLE(), "XX");
        assertEquals("Title 0", getBook(1).getValue(TBook_TITLE()));
    }

    @Test
    public void testKeepRSWithUpdateOnChangeLazy() throws Exception {
        switch (dialect()) {
            case SQLITE:
                log.info("SKIPPING", "KeepResultSet tests");
                return;
        }

        jOOQAbstractTest.reset = false;
        DSLContext create = create(new NoSelectAfterRefreshListener());

        Cursor<B> books =
        create.selectFrom(TBook())
              .orderBy(TBook_ID())
              .keepResultSet(UPDATE_ON_CHANGE)
              .fetchLazy();

        assertNotNull(books.resultSet());
        assertFalse(books.closesAfterFetch());
        while (books.hasNext()) {
            B book = books.fetchOne();
            assertNotNull(book.resultSet());
            book.setValue(TBook_TITLE(), "Title X");
        }

        Result<B> booksTest = getBooks();
        assertEquals(
            Collections.nCopies(4, "Title X"),
            booksTest.getValues(TBook_TITLE()));

        // After closing, setting values to records should no longer have any
        // effect
        assertNotNull(books.resultSet());
        assertFalse(books.isClosed());
        books.close();
        assertNull(books.resultSet());
        assertTrue(books.isClosed());
    }

    @Test
    public void testKeepRSWithUpdateOnChangeFetchOne() throws Exception {
        switch (dialect()) {
            case SQLITE:
                log.info("SKIPPING", "KeepResultSet tests");
                return;
        }

        jOOQAbstractTest.reset = false;

        DSLContext create = create(new NoSelectAfterRefreshListener());
        Record book =
        create.select()
              .from(TBook().getName())
              .where(TBook_ID().eq(1))
              .keepResultSet(UPDATE_ON_CHANGE)
              .fetchOne();

        assertNotNull(book.resultSet());
        assertFalse(book.changed());
        assertEquals(book, book.original());

        book.setValue(TBook_AUTHOR_ID(), 2);
        assertEquals(2, (int) book.getValue(TBook_AUTHOR_ID()));
        assertFalse(book.changed());
        assertEquals(book, book.original());

        book.refresh();
        assertEquals(2, (int) book.getValue(TBook_AUTHOR_ID()));
        assertFalse(book.changed());
        assertEquals(book, book.original());

        try {
            book.setValue(TBook_AUTHOR_ID(), -1);
            fail();
        }
        catch (DataAccessException expected) {}

        assertEquals(2, (int) book.getValue(TBook_AUTHOR_ID()));
        assertFalse(book.changed());
        assertEquals(book, book.original());

        book.close();
        assertNull(book.resultSet());
    }

    @Test
    public void testKeepRSWithUpdateOnChangeRemove() throws Exception {
        switch (dialect()) {
            case SQLITE:
                log.info("SKIPPING", "KeepResultSet tests");
                return;
        }

        jOOQAbstractTest.reset = false;
    }

    /*
     * TODO: More tests:
     * -----------------
     *
     * [#2265] Pull up store(), delete(), refresh() from UpdatableRecord
     * - store() will perform a scan and update if UPDATE_ON_STORE is set. Otherwise: no-op
     * - delete() will remove the record
     *
     * [#1846] Add ResultQuery.keepResultSet() with UPDATE_ON_CHANGE
     * - Implement all data types from ResultSet.updateXXX() (e.g. updateInt(), etc)
     * - Implement UPDATE_ON_STORE
     */
}
