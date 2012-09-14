/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.val;
import static org.jooq.tools.reflect.Reflect.on;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jooq.Cursor;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.MappingException;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test._.AuthorWithoutAnnotations;
import org.jooq.test._.BookRecord;
import org.jooq.test._.BookTable;
import org.jooq.test._.BookWithAnnotations;
import org.jooq.test._.BookWithoutAnnotations;
import org.jooq.test._.CharWithAnnotations;
import org.jooq.test._.DatesWithAnnotations;
import org.jooq.test._.FinalWithAnnotations;
import org.jooq.test._.FinalWithoutAnnotations;
import org.jooq.test._.IBookWithAnnotations;
import org.jooq.test._.IBookWithoutAnnotations;
import org.jooq.test._.ImmutableAuthor;
import org.jooq.test._.StaticWithAnnotations;
import org.jooq.test._.StaticWithoutAnnotations;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;

import org.junit.Test;

public class FetchTests<
    A    extends UpdatableRecord<A>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> {

    public FetchTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testFetchMap() throws Exception {
        try {
            create().selectFrom(TBook()).orderBy(TBook_ID()).fetchMap(TBook_AUTHOR_ID());
            fail();
        } catch (InvalidResultException expected) {}

        // Key -> Record Map
        // -----------------
        Map<Integer, B> map1 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchMap(TBook_ID());
        for (Entry<Integer, B> entry : map1.entrySet()) {
            assertEquals(entry.getKey(), entry.getValue().getValue(TBook_ID()));
        }
        assertEquals(BOOK_IDS, new ArrayList<Integer>(map1.keySet()));

        // Key -> Value Map
        // ----------------
        Map<Integer, String> map2 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchMap(TBook_ID(), TBook_TITLE());
        assertEquals(BOOK_IDS, new ArrayList<Integer>(map2.keySet()));
        assertEquals(BOOK_TITLES, new ArrayList<String>(map2.values()));

        // List of Map
        // -----------
        Result<B> books = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();
        List<Map<String, Object>> list =
            create().selectFrom(TBook()).orderBy(TBook_ID()).fetchMaps();
        assertEquals(4, list.size());

        for (int i = 0; i < books.size(); i++) {
            for (Field<?> field : books.getFields()) {
                assertEquals(books.getValue(i, field), list.get(i).get(field.getName()));
            }
        }

        // Single Map
        // ----------
        B book = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOne();
        Map<String, Object> map3 = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOneMap();

        for (Field<?> field : books.getFields()) {
            assertEquals(book.getValue(field), map3.get(field.getName()));
        }

        // Maps with two times the same field
        // ----------------------------------
        try {
            create().select(val("a"), val("a")).fetchMaps();
            fail();
        }
        catch (InvalidResultException expected) {}

        try {
            create().select(val("a"), val("a")).fetchOneMap();
            fail();
        }
        catch (InvalidResultException expected) {}
    }

    @Test
    public void testFetchArray() throws Exception {

        // fetchOne
        // --------
        B book = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOne();
        Object[] bookArray = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOneArray();
        for (int i = 0; i < TBook().getFields().size(); i++) {
            assertEquals(book.getValue(i), bookArray[i]);
        }

        // fetch
        // -----
        Result<B> books = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();
        Object[][] booksArray = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArrays();

        for (int j = 0; j < books.size(); j++) {
            for (int i = 0; i < TBook().getFields().size(); i++) {
                assertEquals(books.getValue(j, i), booksArray[j][i]);
                assertEquals(books.getValue(j, i), books.intoArray()[j][i]);
                assertEquals(books.get(j).getValue(i), books.get(j).intoArray()[i]);
            }
        }

        // fetch single field
        // ------------------
        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetch(TBook_TITLE()),
        Arrays.asList(create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArray(TBook_TITLE())));

        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetch(1),
        Arrays.asList(create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArray(1)));

        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetch(TBook_ID().getName()),
        Arrays.asList(create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArray(TBook_ID().getName())));

    }

    @Test
    public void testFetch() throws Exception {
        SelectQuery q = create().selectQuery();
        q.addFrom(TAuthor());
        q.addSelect(TAuthor().getFields());
        q.addOrderBy(TAuthor_LAST_NAME());

        Result<?> result = q.fetch();

        assertEquals(2, result.size());
        assertEquals("Coelho", result.get(0).getValue(TAuthor_LAST_NAME()));
        assertEquals("Orwell", result.get(1).getValue(TAuthor_LAST_NAME()));

        try {
            q.fetchOne();
            fail();
        }
        catch (InvalidResultException expected) {}

        Record record = q.fetchAny();
        assertEquals("Coelho", record.getValue(TAuthor_LAST_NAME()));
    }

    @Test
    public void testFetchMany() throws Exception {
        switch (getDialect()) {
            case ORACLE:
            case SQLITE:
            case SYBASE:
                log.info("SKIPPING", "Fetch Many tests");
                return;
        }

        List<Result<Record>> results = create().fetchMany(
            "select * from t_book order by " + TBook_ID().getName());

        assertEquals(1, results.size());
        assertEquals(4, results.get(0).size());
        assertEquals(BOOK_IDS, results.get(0).getValues(TBook_ID(), Integer.class));
        assertEquals(BOOK_TITLES, results.get(0).getValues(TBook_TITLE()));
    }

    @Test
    public void testFetchWithoutResults() throws Exception {
        switch (getDialect()) {
            case ASE:
                log.info("SKIPPING", "Fetch without results tests");
                return;
        }

        Result<Record> result =
        create().fetch(
            create().update(TAuthor())
                    .set(TAuthor_FIRST_NAME(), "Hugo")
                    .where(TAuthor_ID().equal(100))
                    .getSQL(true));

        assertNotNull(result);
        assertEquals(0, result.size());

        List<Result<Record>> results =
        create().fetchMany(
            create().update(TAuthor())
                    .set(TAuthor_FIRST_NAME(), "Hugo")
                    .where(TAuthor_ID().equal(100))
                    .getSQL(true));

        assertNotNull(result);
        assertEquals(0, results.size());

    }

    @Test
    public void testFetchIntoWithAnnotations() throws Exception {
        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        Select<?> select =
        create().select(
                    TBook_ID(),
                    TBook_TITLE(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME(),
                    TAuthor_DATE_OF_BIRTH())
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                .orderBy(TBook_ID());

        List<BookWithAnnotations> result1 = select.fetchInto(BookWithAnnotations.class);
        List<IBookWithAnnotations> result2 = select.fetchInto(IBookWithAnnotations.class);

        assertEquals(4, result1.size());
        assertEquals(4, result2.size());

        assertEquals(1, (int) result1.get(0).id);
        assertEquals(2, (int) result1.get(1).id);
        assertEquals(3, (int) result1.get(2).id);
        assertEquals(4, (int) result1.get(3).id);

        assertEquals(1, result1.get(0).id2);
        assertEquals(2, result1.get(1).id2);
        assertEquals(3, result1.get(2).id2);
        assertEquals(4, result1.get(3).id2);

        assertEquals(1, result1.get(0).id3);
        assertEquals(2, result1.get(1).id3);
        assertEquals(3, result1.get(2).id3);
        assertEquals(4, result1.get(3).id3);

        assertEquals(Long.valueOf(1), result1.get(0).id4);
        assertEquals(Long.valueOf(2), result1.get(1).id4);
        assertEquals(Long.valueOf(3), result1.get(2).id4);
        assertEquals(Long.valueOf(4), result1.get(3).id4);

        assertEquals(1L, result1.get(0).id5);
        assertEquals(2L, result1.get(1).id5);
        assertEquals(3L, result1.get(2).id5);
        assertEquals(4L, result1.get(3).id5);

        assertEquals(1, (int) result2.get(0).getId());
        assertEquals(2, (int) result2.get(1).getId());
        assertEquals(3, (int) result2.get(2).getId());
        assertEquals(4, (int) result2.get(3).getId());

        assertEquals("1984", result1.get(0).title);
        assertEquals("Animal Farm", result1.get(1).title);
        assertEquals("O Alquimista", result1.get(2).title);
        assertEquals("Brida", result1.get(3).title);

        assertEquals("George", result1.get(0).firstName);
        assertEquals("George", result1.get(1).firstName);
        assertEquals("Paulo", result1.get(2).firstName);
        assertEquals("Paulo", result1.get(3).firstName);

        assertEquals("George", result1.get(0).firstName2);
        assertEquals("George", result1.get(1).firstName2);
        assertEquals("Paulo", result1.get(2).firstName2);
        assertEquals("Paulo", result1.get(3).firstName2);

        assertEquals("Orwell", result1.get(0).lastName);
        assertEquals("Orwell", result1.get(1).lastName);
        assertEquals("Coelho", result1.get(2).lastName);
        assertEquals("Coelho", result1.get(3).lastName);

        assertEquals("Orwell", result1.get(0).lastName2);
        assertEquals("Orwell", result1.get(1).lastName2);
        assertEquals("Coelho", result1.get(2).lastName2);
        assertEquals("Coelho", result1.get(3).lastName2);

        assertEquals("Orwell", result2.get(0).getLAST_NAME());
        assertEquals("Orwell", result2.get(1).getLAST_NAME());
        assertEquals("Coelho", result2.get(2).getLAST_NAME());
        assertEquals("Coelho", result2.get(3).getLAST_NAME());

        try {
            // Cannot instanciate an abstract class
            create().selectFrom(TAuthor())
                    .fetchInto(AbstractList.class);
            fail();
        }
        catch (MappingException expected) {}

        // [#1340] While useless, this should be possible
        create().selectFrom(TAuthor())
                .fetchInto(Math.class);

        // [#930] Calendar/Date conversion checks
        // --------------------------------------
        List<DatesWithAnnotations> calendars =
        create().select(TAuthor_DATE_OF_BIRTH())
                .from(TAuthor())
                .orderBy(TAuthor_ID())
                .fetchInto(DatesWithAnnotations.class);

        assertEquals(2, calendars.size());

        for (int index : asList(0, 1)) {
            assertEquals(calendars.get(index).cal1, calendars.get(index).cal2);
            assertEquals(calendars.get(index).cal1, calendars.get(index).cal3);

            assertEquals(calendars.get(index).date1, calendars.get(index).date2);
            assertEquals(calendars.get(index).date1, calendars.get(index).date3);

            assertEquals(calendars.get(index).long1, calendars.get(index).long2);
            assertEquals(calendars.get(index).long1, calendars.get(index).long3);

            assertEquals(calendars.get(index).primitiveLong1, calendars.get(index).primitiveLong2);
            assertEquals(calendars.get(index).primitiveLong1, calendars.get(index).primitiveLong3);

            assertEquals(calendars.get(index).cal1.getTime(), calendars.get(index).date1);
            assertEquals(calendars.get(index).cal1.getTime().getTime(), calendars.get(index).date1.getTime());
            assertEquals(calendars.get(index).cal1.getTime().getTime(), calendars.get(index).long1.longValue());
            assertEquals(calendars.get(index).cal1.getTime().getTime(), calendars.get(index).primitiveLong1);
        }

        A author = create().newRecord(TAuthor());
        DatesWithAnnotations dates = author.into(DatesWithAnnotations.class);

        assertNull(dates.cal1);
        assertNull(dates.cal2);
        assertNull(dates.cal3);
        assertNull(dates.date1);
        assertNull(dates.date2);
        assertNull(dates.date3);
        assertNull(dates.long1);
        assertNull(dates.long2);
        assertNull(dates.long3);
        assertEquals(0L, dates.primitiveLong1);
        assertEquals(0L, dates.primitiveLong2);
        assertEquals(0L, dates.primitiveLong3);

        author = create().newRecord(TAuthor());
        author.setValue(TAuthor_DATE_OF_BIRTH(), new Date(1L));
        dates = author.into(DatesWithAnnotations.class);

        assertEquals(1L, dates.cal1.getTime().getTime());
        assertEquals(1L, dates.cal2.getTime().getTime());
        assertEquals(1L, dates.cal3.getTime().getTime());
        assertEquals(1L, dates.date1.getTime());
        assertEquals(1L, dates.date2.getTime());
        assertEquals(1L, dates.date3.getTime());
        assertEquals(1L, (long) dates.long1);
        assertEquals(1L, (long) dates.long2);
        assertEquals(1L, (long) dates.long3);
        assertEquals(1L, dates.primitiveLong1);
        assertEquals(1L, dates.primitiveLong2);
        assertEquals(1L, dates.primitiveLong3);
    }

    @Test
    public void testFetchIntoWithoutAnnotations() throws Exception {
        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        Select<?> select =
        create().select(
                    TBook_ID(),
                    TBook_TITLE(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME(),
                    TAuthor_DATE_OF_BIRTH())
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                .orderBy(TBook_ID());

        List<BookWithoutAnnotations> result1 = select.fetchInto(BookWithoutAnnotations.class);
        List<IBookWithoutAnnotations> result2 = select.fetchInto(IBookWithoutAnnotations.class);

        assertEquals(4, result1.size());
        assertEquals(4, result2.size());

        assertEquals(1, (int) result1.get(0).id);
        assertEquals(2, (int) result1.get(1).id);
        assertEquals(3, (int) result1.get(2).id);
        assertEquals(4, (int) result1.get(3).id);

        assertEquals(1, result1.get(0).id2);
        assertEquals(2, result1.get(1).id2);
        assertEquals(3, result1.get(2).id2);
        assertEquals(4, result1.get(3).id2);

        assertEquals(1, result1.get(0).ID);
        assertEquals(2, result1.get(1).ID);
        assertEquals(3, result1.get(2).ID);
        assertEquals(4, result1.get(3).ID);

        assertEquals(1, (int) result2.get(0).getId());
        assertEquals(2, (int) result2.get(1).getId());
        assertEquals(3, (int) result2.get(2).getId());
        assertEquals(4, (int) result2.get(3).getId());

        assertEquals("1984", result1.get(0).title);
        assertEquals("Animal Farm", result1.get(1).title);
        assertEquals("O Alquimista", result1.get(2).title);
        assertEquals("Brida", result1.get(3).title);

        assertEquals("George", result1.get(0).firstName);
        assertEquals("George", result1.get(1).firstName);
        assertEquals("Paulo", result1.get(2).firstName);
        assertEquals("Paulo", result1.get(3).firstName);

        assertEquals("George", result1.get(0).firstName2);
        assertEquals("George", result1.get(1).firstName2);
        assertEquals("Paulo", result1.get(2).firstName2);
        assertEquals("Paulo", result1.get(3).firstName2);

        assertEquals("Orwell", result1.get(0).lastName);
        assertEquals("Orwell", result1.get(1).lastName);
        assertEquals("Coelho", result1.get(2).lastName);
        assertEquals("Coelho", result1.get(3).lastName);

        assertEquals("Orwell", result1.get(0).lastName2);
        assertEquals("Orwell", result1.get(1).lastName2);
        assertEquals("Coelho", result1.get(2).lastName2);
        assertEquals("Coelho", result1.get(3).lastName2);

        assertEquals("Orwell", result1.get(0).LAST_NAME);
        assertEquals("Orwell", result1.get(1).LAST_NAME);
        assertEquals("Coelho", result1.get(2).LAST_NAME);
        assertEquals("Coelho", result1.get(3).LAST_NAME);

        assertEquals("Orwell", result2.get(0).getLAST_NAME());
        assertEquals("Orwell", result2.get(1).getLAST_NAME());
        assertEquals("Coelho", result2.get(2).getLAST_NAME());
        assertEquals("Coelho", result2.get(3).getLAST_NAME());
    }

    @Test
    public void testRecordFromWithAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        BookWithAnnotations b = new BookWithAnnotations();
        b.firstName = "Edgar Allen";
        b.lastName2 = "Poe";
        b.dateOfBirth = new Date(1);
        b.id = 17;
        b.title = "The Raven";

        // This data shouldn't be considered
        b.id2 = 18;
        b.lastName = "Poet";

        B book = create().newRecord(TBook(), b);
        A author = create().newRecord(TAuthor(), b);

        assertEquals(b.id, author.getValue(TAuthor_ID()));
        assertEquals(b.firstName, author.getValue(TAuthor_FIRST_NAME()));
        assertEquals(b.lastName2, author.getValue(TAuthor_LAST_NAME()));
        assertEquals(b.dateOfBirth, author.getValue(TAuthor_DATE_OF_BIRTH()));
        assertNull(author.getValue(TAuthor_YEAR_OF_BIRTH()));

        assertEquals(b.id, book.getValue(TBook_ID()));
        assertEquals(b.title, book.getValue(TBook_TITLE()));
        assertNull(book.getValue(TBook_AUTHOR_ID()));
        assertNull(book.getValue(TBook_CONTENT_PDF()));
        assertNull(book.getValue(TBook_CONTENT_TEXT()));
        assertNull(book.getValue(TBook_LANGUAGE_ID()));
        assertNull(book.getValue(TBook_PUBLISHED_IN()));
    }

    @Test
    public void testRecordFromWithoutAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        BookWithoutAnnotations b = new BookWithoutAnnotations();
        b.firstName = "Edgar Allen";
        b.lastName = "Poe";
        b.DATE_OF_BIRTH = new Date(1);
        b.id = 17;
        b.title = "The Raven";

        // This data shouldn't be considered
        b.id2 = 18;
        b.ID = 19;
        b.LAST_NAME = "Poet";
        b.dateOfBirth = new Date(2);

        B book = create().newRecord(TBook(), b);
        A author = create().newRecord(TAuthor(), b);

        assertEquals(b.id, author.getValue(TAuthor_ID()));
        assertEquals(b.firstName, author.getValue(TAuthor_FIRST_NAME()));
        assertEquals(b.lastName, author.getValue(TAuthor_LAST_NAME()));
        assertEquals(b.DATE_OF_BIRTH, author.getValue(TAuthor_DATE_OF_BIRTH()));
        assertNull(author.getValue(TAuthor_YEAR_OF_BIRTH()));

        assertEquals(b.id, book.getValue(TBook_ID()));
        assertEquals(b.title, book.getValue(TBook_TITLE()));
        assertNull(book.getValue(TBook_AUTHOR_ID()));
        assertNull(book.getValue(TBook_CONTENT_PDF()));
        assertNull(book.getValue(TBook_CONTENT_TEXT()));
        assertNull(book.getValue(TBook_LANGUAGE_ID()));
        assertNull(book.getValue(TBook_PUBLISHED_IN()));
    }

    @Test
    public void testRecordFromUpdatePK() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "testRecordFromUpdatePK() tests");
                return;
        }

        jOOQAbstractTest.reset = false;

        // [#979] When using Record.from(), and the PK remains unchanged, there
        // must not result an INSERT on a subsequent call to .store()
        A author1 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(1)).fetchOne();
        AuthorWithoutAnnotations into1 = author1.into(AuthorWithoutAnnotations.class);
        into1.yearOfBirth = null;
        author1.from(into1);
        assertEquals(1, author1.store());

        A author2 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(1)).fetchOne();
        assertEquals(author1, author2);
        assertEquals(author1.getValue(TAuthor_ID()), author2.getValue(TAuthor_ID()));
        assertEquals(author1.getValue(TAuthor_FIRST_NAME()), author2.getValue(TAuthor_FIRST_NAME()));
        assertEquals(author1.getValue(TAuthor_LAST_NAME()), author2.getValue(TAuthor_LAST_NAME()));
        assertEquals(author1.getValue(TAuthor_DATE_OF_BIRTH()), author2.getValue(TAuthor_DATE_OF_BIRTH()));
        assertEquals(author1.getValue(TAuthor_YEAR_OF_BIRTH()), author2.getValue(TAuthor_YEAR_OF_BIRTH()));
        assertNull(author2.getValue(TAuthor_YEAR_OF_BIRTH()));

        // But when the PK is modified, be sure an INSERT is executed
        A author3 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(1)).fetchOne();
        AuthorWithoutAnnotations into2 = author3.into(AuthorWithoutAnnotations.class);
        into2.ID = 3;
        author3.from(into2);
        assertEquals(1, author3.store());

        A author4 = create().selectFrom(TAuthor()).where(TAuthor_ID().equal(3)).fetchOne();
        assertEquals(author3, author4);
    }

    @Test
    public void testReflectionWithAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        // [#933] Map values to char / Character
        A author1 = create().newRecord(TAuthor());
        CharWithAnnotations c1 = author1.into(CharWithAnnotations.class);
        assertEquals((char) 0, c1.id1);
        assertEquals(null, c1.id2);
        assertEquals((char) 0, c1.last1);
        assertEquals(null, c1.last2);

        author1.setValue(TAuthor_ID(), 1);
        author1.setValue(TAuthor_LAST_NAME(), "a");
        CharWithAnnotations c2 = author1.into(CharWithAnnotations.class);
        assertEquals('1', c2.id1);
        assertEquals('1', c2.id2.charValue());
        assertEquals('a', c2.last1);
        assertEquals('a', c2.last2.charValue());

        A author2 = create().newRecord(TAuthor(), c2);
        assertEquals('1', author2.getValue(TAuthor_ID(), char.class).charValue());
        assertEquals('1', author2.getValue(TAuthor_ID(), Character.class).charValue());
        assertEquals('a', author2.getValue(TAuthor_LAST_NAME(), char.class).charValue());
        assertEquals('a', author2.getValue(TAuthor_LAST_NAME(), Character.class).charValue());

        // [#934] Static members are not to be considered
        assertEquals(create().newRecord(TBook()), create().newRecord(TBook(), new StaticWithAnnotations()));
        create().newRecord(TBook()).into(StaticWithAnnotations.class);
        assertEquals(13, StaticWithAnnotations.ID);

        // [#935] Final member fields are considered when reading
        B book = create().newRecord(TBook());
        book.setValue(TBook_ID(), new FinalWithAnnotations().ID);
        assertEquals(book, create().newRecord(TBook(), new FinalWithAnnotations()));

        // [#935] ... but not when writing
        FinalWithAnnotations f = create().newRecord(TBook()).into(FinalWithAnnotations.class);
        assertEquals(f.ID, new FinalWithAnnotations().ID);
    }

    @Test
    public void testReflectionWithoutAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        // Arbitrary sources should have no effect
        assertEquals(create().newRecord(TBook()), create().newRecord(TBook(), (Object) null));
        assertEquals(create().newRecord(TBook()), create().newRecord(TBook(), new Object()));

        // [#934] Static members are not to be considered
        assertEquals(create().newRecord(TBook()), create().newRecord(TBook(), new StaticWithoutAnnotations()));
        create().newRecord(TBook()).into(StaticWithoutAnnotations.class);
        assertEquals(13, StaticWithoutAnnotations.ID);

        // [#935] Final member fields are considered when reading
        B book = create().newRecord(TBook());
        book.setValue(TBook_ID(), new FinalWithoutAnnotations().ID);
        assertEquals(book, create().newRecord(TBook(), new FinalWithoutAnnotations()));

        // [#935] ... but not when writing
        FinalWithoutAnnotations f = create().newRecord(TBook()).into(FinalWithoutAnnotations.class);
        assertEquals(f.ID, new FinalWithoutAnnotations().ID);
    }

    @Test
    public void testReflectionWithImmutables() throws Exception {

        // [#1336] Try instanciating "immutable" POJOs
        // -------------------------------------------
        Record author1 =
        create().select(
                    TAuthor_ID(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME(),
                    TAuthor_DATE_OF_BIRTH())
                .from(TAuthor())
                .where(TAuthor_ID().equal(1))
                .fetchOne();

        ImmutableAuthor immutable1 = author1.into(ImmutableAuthor.class);
        assertEquals((int) author1.getValue(TAuthor_ID()), immutable1.ID);
        assertEquals(author1.getValue(TAuthor_FIRST_NAME()), immutable1.firstName);
        assertEquals(author1.getValue(TAuthor_LAST_NAME()), immutable1.lastName);
        assertEquals(author1.getValue(TAuthor_DATE_OF_BIRTH()), immutable1.dateOfBirth);

        // Try again, using a different constructor
        // -------------------------------------------
        Record author2 =
        create().select(
                    TAuthor_ID(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME())
                .from(TAuthor())
                .where(TAuthor_ID().equal(1))
                .fetchOne();

        ImmutableAuthor immutable2 = author2.into(ImmutableAuthor.class);
        assertEquals((int) author2.getValue(TAuthor_ID()), immutable2.ID);
        assertEquals(author2.getValue(TAuthor_FIRST_NAME()), immutable2.firstName);
        assertEquals(author2.getValue(TAuthor_LAST_NAME()), immutable2.lastName);
        assertEquals(null, immutable2.dateOfBirth);

        // Try again, using an inexistent constructor
        // -------------------------------------------
        try {
            create().select(
                        TAuthor_ID(),
                        TAuthor_FIRST_NAME())
                    .from(TAuthor())
                    .where(TAuthor_ID().equal(1))
                    .fetchOne()
                    .into(ImmutableAuthor.class);
            fail();
        }
        catch (MappingException expected) {}

        // Try again, using an invalid constructor
        // -------------------------------------------
        try {
            create().select(
                        TAuthor_FIRST_NAME(),
                        TAuthor_LAST_NAME(),
                        TAuthor_ID())
                    .from(TAuthor())
                    .where(TAuthor_ID().equal(1))
                    .fetchOne()
                    .into(ImmutableAuthor.class);
            fail();
        }
        catch (MappingException expected) {}
    }

    @Test
    public void testFetchIntoTableRecords() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#1819] Check if only applicable setters are used
        // JOIN two tables into a generated UpdatableRecord
        List<B> result1 =
        create().select(
                    TBook_ID(),
                    TBook_TITLE(),
                    TBook_AUTHOR_ID(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME())
                .from(TBook())
                .join(TAuthor())
                .on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                .orderBy(TBook_ID())
                .fetch()
                .into(TBook().getRecordType());

        assertEquals(4, result1.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(BOOK_IDS.get(i), result1.get(i).getValue(TBook_ID()));
            assertEquals(BOOK_TITLES.get(i), result1.get(i).getValue(TBook_TITLE()));
            assertEquals(BOOK_AUTHOR_IDS.get(i), result1.get(i).getValue(TBook_AUTHOR_ID()));
        }
    }

    @Test
    public void testFetchIntoTable() throws Exception {
        jOOQAbstractTest.reset = false;

        // JOIN two tables into a generated UpdatableRecord
        Result<B> result1 =
        create().select(
                    TBook_ID(),
                    TBook_TITLE(),
                    TBook_AUTHOR_ID(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME())
                .from(TBook())
                .join(TAuthor())
                .on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                .orderBy(TBook_ID())
                .fetch()
                .into(TBook());

        // Assure that only book-related fields are actually contained in Result
        assertEquals(4, result1.size());
        assertEquals(BOOK_IDS, result1.getValues(TBook_ID()));
        assertEquals(BOOK_TITLES, result1.getValues(TBook_TITLE()));
        assertEquals(BOOK_AUTHOR_IDS, result1.getValues(TBook_AUTHOR_ID()));
        assertEquals(BOOK_NULLS, result1.getValues(TBook_PUBLISHED_IN()));
        assertNull(result1.getField(TAuthor_FIRST_NAME()));
        assertNull(result1.getField(TAuthor_LAST_NAME()));

        // Ensure that books can be updated using store()
        result1.get(0).setValue(TBook_TITLE(), "Changed");
        assertEquals(1, result1.get(0).store());

        Result<B> books1 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();
        assertEquals(4, books1.size());
        assertEquals(BOOK_IDS, books1.getValues(TBook_ID()));
        assertEquals(1, (int) books1.getValue(0, TBook_ID()));
        assertEquals(1, (int) books1.getValue(0, TBook_AUTHOR_ID()));
        assertEquals("Changed", books1.getValue(0, TBook_TITLE()));

        // Without any fetched primary keys, the resulting records should be
        // inserted using store()
        B book =
        create().select(
                    TBook_TITLE(),
                    TBook_AUTHOR_ID(),
                    TBook_PUBLISHED_IN(),
                    TBook_LANGUAGE_ID())
                .from(TBook())
                .where(TBook_ID().equal(2))
                .fetchOne()
                .into(TBook());

        assertNotNull(book);
        assertEquals("Animal Farm", book.getValue(TBook_TITLE()));
        assertNull(book.getValue(TBook_ID()));

        book.setValue(TBook_ID(), 5);
        assertEquals(1, book.store());
        Result<B> books2 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();
        assertEquals(5, books2.size());
        assertEquals("Animal Farm", books2.getValue(4, TBook_TITLE()));
    }

    @Test
    public void testFetchIntoTables() throws Exception {
        Result<Record> result =
        create().select()
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                .orderBy(TBook_ID())
                .fetch();

        Result<A> a = result.into(TAuthor());
        Result<B> b = result.into(TBook());

        assertEquals(4, a.size());
        assertEquals(4, b.size());

        assertEquals(BOOK_AUTHOR_IDS, a.getValues(TAuthor_ID()));
        assertEquals(BOOK_FIRST_NAMES, a.getValues(TAuthor_FIRST_NAME()));
        assertEquals(BOOK_LAST_NAMES, a.getValues(TAuthor_LAST_NAME()));
        assertTrue(TAuthor().getRecordType().isAssignableFrom(a.get(0).getClass()));

        assertEquals(BOOK_IDS, b.getValues(TBook_ID()));
        assertEquals(BOOK_TITLES, b.getValues(TBook_TITLE()));
        assertTrue(TBook().getRecordType().isAssignableFrom(b.get(0).getClass()));
    }

    @Test
    public void testFetchIntoCustomTable() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case INGRES:
            case POSTGRES:
                log.info("SKIPPING", "fetchInto() tests");
                return;
        }

        Result<BookRecord> result =
            create().select(
                        TBook_ID(),
                        TBook_TITLE(),
                        TAuthor_FIRST_NAME(),
                        TAuthor_LAST_NAME(),
                        TAuthor_DATE_OF_BIRTH())
                    .from(TBook())
                    .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                    .orderBy(TBook_ID())
                    .fetchInto(BookTable.BOOK);

        assertEquals(4, result.size());

        assertEquals(BOOK_IDS_SHORT, result.getValues(3));
        assertEquals(BOOK_IDS_SHORT, result.getValues(TBook_ID()));
        assertEquals(BOOK_IDS_SHORT, result.getValues(BookTable.ID));
        assertEquals(Short.valueOf((short) 1), result.getValue(0, BookTable.ID));
        assertEquals(Short.valueOf((short) 2), result.getValue(1, BookTable.ID));
        assertEquals(Short.valueOf((short) 3), result.getValue(2, BookTable.ID));
        assertEquals(Short.valueOf((short) 4), result.getValue(3, BookTable.ID));

        assertEquals(BOOK_TITLES, result.getValues(4));
        assertEquals(BOOK_TITLES, result.getValues(TBook_TITLE()));
        assertEquals(BOOK_TITLES, result.getValues(BookTable.TITLE));

        assertEquals(BOOK_FIRST_NAMES, result.getValues(0));
        assertEquals(BOOK_FIRST_NAMES, result.getValues(TAuthor_FIRST_NAME()));
        assertEquals(BOOK_FIRST_NAMES, result.getValues(BookTable.FIRST_NAME));

        assertEquals(BOOK_LAST_NAMES, result.getValues(2));
        assertEquals(BOOK_LAST_NAMES, result.getValues(TAuthor_LAST_NAME()));
        assertEquals(BOOK_LAST_NAMES, result.getValues(BookTable.LAST_NAME));

        assertEquals(Collections.nCopies(4, null), result.getValues(1));
        assertEquals(Collections.nCopies(4, null), result.getValues(BookTable.UNMATCHED));
    }

    @Test
    public void testFetchIntoRecordHandler() throws Exception {

        // Test a simple query with typed records
        // --------------------------------------
        final Queue<Integer> ids = new LinkedList<Integer>();
        final Queue<String> titles = new LinkedList<String>();

        ids.addAll(BOOK_IDS);
        titles.addAll(BOOK_TITLES);

        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetchInto(new RecordHandler<B>() {
                    @Override
                    public void next(B record) {
                        assertEquals(ids.poll(), record.getValue(TBook_ID()));
                        assertEquals(titles.poll(), record.getValue(TBook_TITLE()));
                    }
                });

        assertTrue(ids.isEmpty());
        assertTrue(titles.isEmpty());

        // Test lazy fetching
        // --------------------------------------
        ids.addAll(BOOK_IDS);
        titles.addAll(BOOK_TITLES);

        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetchLazy()
                .fetchInto(new RecordHandler<B>() {
                    @Override
                    public void next(B record) {
                        assertEquals(ids.poll(), record.getValue(TBook_ID()));
                        assertEquals(titles.poll(), record.getValue(TBook_TITLE()));
                    }
                });

        assertTrue(ids.isEmpty());
        assertTrue(titles.isEmpty());

        // Test a generic query with any records
        // -------------------------------------
        final Queue<Integer> authorIDs = new LinkedList<Integer>();
        final Queue<Integer> count = new LinkedList<Integer>();

        authorIDs.addAll(Arrays.asList(1, 2));
        count.addAll(Arrays.asList(2, 2));

        create().select(TBook_AUTHOR_ID(), count())
                .from(TBook())
                .groupBy(TBook_AUTHOR_ID())
                .orderBy(TBook_AUTHOR_ID())
                .fetchInto(new RecordHandler<Record>() {
                    @Override
                    public void next(Record record) {
                        assertEquals(authorIDs.poll(), record.getValue(TBook_AUTHOR_ID()));
                        assertEquals(count.poll(), record.getValue(count()));
                    }
                });
    }

    @Test
    public void testFetchLater() throws Exception {
        Future<Result<B>> later;
        Result<B> result;

        int activeCount = Thread.activeCount();

        later = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLater();

        // That's too fast for the query to be done, mostly
        assertFalse(later.isDone());
        assertFalse(later.isCancelled());
        assertEquals(activeCount + 1, Thread.activeCount());

        // Get should make sure the internal thread is terminated
        result = later.get();
        Thread.sleep(500);
        assertEquals(activeCount, Thread.activeCount());

        // Subsequent gets are ok
        result = later.get();
        result = later.get(1000, TimeUnit.MILLISECONDS);

        // Check the data
        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(TBook_ID()));

        // Start new threads
        later = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLater();
        later = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLater();
        later = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLater();
        assertEquals(activeCount + 3, Thread.activeCount());

        // This should be enough to ensure that GC will collect finished threads
        later = null;
        System.gc();
        System.gc();
        Thread.sleep(500);
        assertEquals(activeCount, Thread.activeCount());
    }

    @Test
    public void testFetchResultSet() throws Exception {
        for (int i = 0; i < 2; i++) {
            assertEquals(
                create().fetch("select * from t_author order by id"),
                create().fetch(create().resultQuery("select * from t_author order by id").fetchResultSet()));

            ResultSet rs = create().resultQuery("select * from t_author order by id").fetchResultSet();
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));
            assertEquals(1, rs.getInt(1));
            assertFalse(rs.wasNull());
            assertEquals(1, rs.getInt(TAuthor_ID().getName()));
            assertEquals((short) 1, rs.getShort(TAuthor_ID().getName()));
            assertEquals(1L, rs.getLong(TAuthor_ID().getName()));
            assertEquals(AUTHOR_FIRST_NAMES.get(0), rs.getString(2));
            assertEquals(AUTHOR_FIRST_NAMES.get(0), rs.getString(TAuthor_FIRST_NAME().getName()));
            assertEquals(AUTHOR_LAST_NAMES.get(0), rs.getString(3));
            assertEquals(AUTHOR_LAST_NAMES.get(0), rs.getString(TAuthor_LAST_NAME().getName()));

            assertTrue(rs.next());
            assertEquals(2, rs.getInt(1));
            assertEquals(2, rs.getInt(1));
            assertFalse(rs.wasNull());
            assertEquals(2, rs.getInt(TAuthor_ID().getName()));

            assertFalse(rs.next());
            rs.close();

            // [#1323] Check if Postgres' pre-9.0 encoding of binary data works, too
            if (getDialect() == POSTGRES && i == 0) {
                create().execute("set bytea_output to escape");
            }

            // Otherwise, don't repeat this test
            else {
                break;
            }
        }
    }

    @Test
    public void testFetchLazy() throws Exception {
        for (int fetchSize : Arrays.asList(0, 1)) {

            // ---------------------------------------------------------------------
            // A regular pass through the cursor
            // ---------------------------------------------------------------------
            Cursor<B> cursor = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLazy(fetchSize);

            assertTrue(cursor.hasNext());
            assertTrue(cursor.hasNext());
            assertEquals(Integer.valueOf(1), cursor.fetchOne().getValue(TBook_ID()));
            assertEquals(Integer.valueOf(2), cursor.fetchOne().getValue(TBook_ID()));

            assertTrue(cursor.hasNext());
            assertTrue(cursor.hasNext());
            assertFalse(cursor.isClosed());

            Iterator<B> it = cursor.iterator();
            assertTrue(it.hasNext());
            assertTrue(cursor.hasNext());
            assertTrue(it.hasNext());
            assertTrue(cursor.hasNext());
            assertTrue(it.hasNext());
            assertTrue(cursor.hasNext());
            assertEquals(Integer.valueOf(3), it.next().getValue(TBook_ID()));
            assertEquals(Integer.valueOf(4), it.next().getValue(TBook_ID()));
            assertFalse(cursor.isClosed());

            assertFalse(it.hasNext());
            assertFalse(cursor.hasNext());
            assertFalse(it.hasNext());
            assertFalse(cursor.hasNext());
            assertFalse(it.hasNext());
            assertFalse(cursor.hasNext());
            assertTrue(cursor.isClosed());

            assertEquals(null, it.next());
            assertEquals(null, it.next());
            assertEquals(null, cursor.fetchOne());
            assertEquals(null, cursor.fetchOne());

            cursor.close();
            cursor.close();
            assertTrue(cursor.isClosed());

            // ---------------------------------------------------------------------
            // Prematurely closing the cursor
            // ---------------------------------------------------------------------
            cursor = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLazy(fetchSize);

            assertTrue(cursor.hasNext());
            assertTrue(cursor.hasNext());
            assertEquals(Integer.valueOf(1), cursor.fetchOne().getValue(TBook_ID()));
            assertEquals(Integer.valueOf(2), cursor.fetchOne().getValue(TBook_ID()));
            assertFalse(cursor.isClosed());

            cursor.close();
            assertTrue(cursor.isClosed());
            assertFalse(cursor.hasNext());
            assertNull(cursor.fetchOne());

            // ---------------------------------------------------------------------
            // Fetching several records at once
            // ---------------------------------------------------------------------
            cursor = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchLazy(fetchSize);
            Result<B> fetch0 = cursor.fetch(0);

            assertTrue(fetch0.isEmpty());
            assertFalse(fetch0.isNotEmpty());
            assertTrue(fetch0.isEmpty());
            assertFalse(fetch0.isNotEmpty());

            Result<B> fetch1 = cursor.fetch(1);
            assertEquals(1, fetch1.size());
            assertEquals(Integer.valueOf(1), fetch1.get(0).getValue(TBook_ID()));

            fetch1 = cursor.fetch(2);
            assertEquals(2, fetch1.size());
            assertEquals(Integer.valueOf(2), fetch1.get(0).getValue(TBook_ID()));
            assertEquals(Integer.valueOf(3), fetch1.get(1).getValue(TBook_ID()));

            fetch1 = cursor.fetch(2);
            assertTrue(cursor.isClosed());
            assertEquals(1, fetch1.size());
            assertEquals(Integer.valueOf(4), fetch1.get(0).getValue(TBook_ID()));
        }
    }

    @Test
    public void testFetchIntoGeneratedPojos() throws Exception {
        try {
            Reflect book = on(TBook().getClass().getPackage().getName() + ".pojos." + TBook().getClass().getSimpleName());

            List<Object> books =
            create().selectFrom(TBook())
                    .orderBy(TBook_ID())
                    .fetchInto((Class<?>) book.get());

            assertEquals(4, books.size());
            for (int i = 0; i < 4; i++) {
                assertEquals(BOOK_IDS.get(i), on(books.get(i)).call("getId").get());
                assertEquals(BOOK_AUTHOR_IDS.get(i), on(books.get(i)).call("getAuthorId").get());
                assertEquals(BOOK_TITLES.get(i), on(books.get(i)).call("getTitle").get());
            }
        }
        catch (ReflectException e) {
            log.info("SKIPPING", "Generated POJO tests");
        }
    }

    @Test
    public void testFetchIntoResultSet() throws Exception {
        Result<B> result = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();

        // [#1141] Be careful not to select any Enums or other custom data types
        // for this test case. That sort of type information is lost in
        // Factory.fetch(ResultSet)
//        Result<Record> result1 = create().fetch(result.intoResultSet());
//        Result<Record> result2 = create().fetch(result1.intoResultSet());
//
//        // Check whether an inverse operation yields the same Result object
//        assertEquals(result, result1);
//        assertEquals(result, result2);

        // Check the data correctness
        ResultSet rs = result.intoResultSet();
        check0(rs);

        assertTrue(rs.next());
        check1(rs);

        assertFalse(rs.previous());
        check0(rs);

        assertFalse(rs.absolute(0));
        check0(rs);

        assertTrue(rs.relative(1));
        check1(rs);

        assertFalse(rs.relative(-1));
        check0(rs);

        assertTrue(rs.absolute(-4));
        check1(rs);

        assertFalse(rs.absolute(-5));
        check0(rs);

        assertTrue(rs.absolute(1));
        check1(rs);

        rs.beforeFirst();
        check0(rs);

        assertTrue(rs.last());
        check4(rs);

        rs.afterLast();
        check5(rs);

        assertTrue(rs.previous());
        check4(rs);

        assertFalse(rs.relative(1));
        check5(rs);

        // Check the meta data
        ResultSetMetaData meta = rs.getMetaData();
        assertEquals(result.getFields().size(), meta.getColumnCount());
        assertEquals(Integer.class.getName(), meta.getColumnClassName(1));
        assertEquals(Types.INTEGER, meta.getColumnType(1));
        assertEquals("integer", meta.getColumnTypeName(1));
        assertEquals(TBook_ID().getName(), meta.getColumnLabel(1));
        assertEquals(TBook_ID().getName(), meta.getColumnName(1));
        assertEquals("", meta.getCatalogName(1));

        if (schema() != null)
            assertEquals(schema().getName(), meta.getSchemaName(1));

        assertEquals(TBook().getName(), meta.getTableName(1));
    }

    private void check5(ResultSet rs) throws SQLException {
        assertFalse(rs.isClosed());
        assertFalse(rs.isBeforeFirst());
        assertTrue(rs.isAfterLast());
        assertFalse(rs.isFirst());
        assertFalse(rs.isLast());
        assertEquals(0, rs.getRow());

        try {
            rs.getObject(1);
            fail();
        }
        catch (SQLException expected) {}
    }

    private void check4(ResultSet rs) throws SQLException {
        assertFalse(rs.isClosed());
        assertFalse(rs.isBeforeFirst());
        assertFalse(rs.isAfterLast());
        assertFalse(rs.isFirst());
        assertTrue(rs.isLast());
        assertEquals(4, rs.getRow());

        assertEquals(4, rs.getInt(1));
        assertFalse(rs.wasNull());
        assertEquals(4, rs.getInt(TBook_ID().getName()));
        assertFalse(rs.wasNull());
        assertFalse(rs.wasNull());
        assertEquals("4", rs.getString(1));
        assertEquals((byte) 4, rs.getByte(1));
        assertEquals((short) 4, rs.getShort(1));
        assertEquals(4L, rs.getLong(1));
        assertEquals(4.0f, rs.getFloat(1));
        assertEquals(4.0, rs.getDouble(1));
        assertEquals(new BigDecimal("4"), rs.getBigDecimal(1));

        assertEquals(0, rs.getInt(3));
        assertTrue(rs.wasNull());
        assertTrue(rs.wasNull());
        assertEquals("Brida", rs.getString(5));
        assertFalse(rs.wasNull());

        try {
            rs.getObject(0);
            fail();
        }
        catch (SQLException expected) {}
        try {
            rs.getObject(100);
            fail();
        }
        catch (SQLException expected) {}
    }

    private void check1(ResultSet rs) throws SQLException {
        assertFalse(rs.isClosed());
        assertFalse(rs.isBeforeFirst());
        assertFalse(rs.isAfterLast());
        assertTrue(rs.isFirst());
        assertFalse(rs.isLast());
        assertEquals(1, rs.getRow());

        assertEquals(1, rs.getInt(1));
        assertFalse(rs.wasNull());
        assertEquals(1, rs.getInt(TBook_ID().getName()));
        assertFalse(rs.wasNull());
        assertFalse(rs.wasNull());
        assertEquals("1", rs.getString(1));
        assertEquals((byte) 1, rs.getByte(1));
        assertEquals((short) 1, rs.getShort(1));
        assertEquals(1L, rs.getLong(1));
        assertEquals(1.0f, rs.getFloat(1));
        assertEquals(1.0, rs.getDouble(1));
        assertEquals(BigDecimal.ONE, rs.getBigDecimal(1));

        assertEquals(0, rs.getInt(3));
        assertTrue(rs.wasNull());
        assertTrue(rs.wasNull());
        assertEquals(1984, rs.getInt(5));
        assertFalse(rs.wasNull());

        try {
            rs.getObject(0);
            fail();
        }
        catch (SQLException expected) {}
        try {
            rs.getObject(100);
            fail();
        }
        catch (SQLException expected) {}
    }

    private void check0(ResultSet rs) throws SQLException {
        assertFalse(rs.isClosed());
        assertTrue(rs.isBeforeFirst());
        assertFalse(rs.isAfterLast());
        assertFalse(rs.isFirst());
        assertFalse(rs.isLast());
        assertEquals(0, rs.getRow());

        try {
            rs.getObject(1);
            fail();
        }
        catch (SQLException expected) {}
    }
}
