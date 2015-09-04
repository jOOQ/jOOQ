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
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.INFORMIX;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.REDSHIFT;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.val;
import static org.jooq.lambda.Seq.seq;
import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.jooq.tools.reflect.Reflect.on;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.AttachableInternal;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Results;
import org.jooq.Row;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.MappingException;
import org.jooq.exception.TooManyRowsException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.SQLDataType;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test.all.AuthorWithoutAnnotations;
import org.jooq.test.all.BookRecord;
import org.jooq.test.all.BookTable;
import org.jooq.test.all.BookWithAnnotations;
import org.jooq.test.all.BookWithoutAnnotations;
import org.jooq.test.all.CharWithAnnotations;
import org.jooq.test.all.DatesWithAnnotations;
import org.jooq.test.all.FinalWithAnnotations;
import org.jooq.test.all.FinalWithoutAnnotations;
import org.jooq.test.all.IBookWithAnnotations;
import org.jooq.test.all.IBookWithoutAnnotations;
import org.jooq.test.all.ImmutableAuthor;
import org.jooq.test.all.ImmutableAuthorWithConstructorProperties;
import org.jooq.test.all.ImmutableAuthorWithConstructorPropertiesAndJPAAnnotations;
import org.jooq.test.all.ImmutableAuthorWithConstructorPropertiesAndJPAAnnotationsAndPublicFields;
import org.jooq.test.all.ImmutableAuthorWithConstructorPropertiesAndPublicFields;
import org.jooq.test.all.ImmutableAuthorWithConstructorPropertiesButNoMatchingGetters;
import org.jooq.test.all.StaticWithAnnotations;
import org.jooq.test.all.StaticWithoutAnnotations;
import org.jooq.tools.jdbc.DefaultConnection;
import org.jooq.tools.jdbc.DefaultPreparedStatement;
import org.jooq.tools.jdbc.DefaultResultSet;
import org.jooq.tools.jdbc.JDBCUtils;

import org.junit.Assert;

public class FetchTests<
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

    public FetchTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

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

        // Keys -> Record
        // --------------
        Map<Record, B> map3 = create().selectFrom(TBook()).orderBy(TBook_ID())
            .fetchMap(new Field<?>[] { TBook_ID(), TBook_LANGUAGE_ID(), TBook_TITLE() });
        assertEquals(4, map3.keySet().size());

        for (Record key : map3.keySet()) {
            B record = map3.get(key);
            assertEquals(key.getValue(0), record.getValue(TBook_ID()));
            assertEquals(key.getValue(1), record.getValue(TBook_LANGUAGE_ID()));
            assertEquals(key.getValue(2), record.getValue(TBook_TITLE()));

            // Check if the key can be constructed synthetically
            // See also http://stackoverflow.com/q/19938815/521799
            Record3<Integer, Integer, String> k = create().newRecord(TBook_ID(), TBook_LANGUAGE_ID(), TBook_TITLE());
            k.setValue(TBook_ID(), record.getValue(TBook_ID()));
            k.setValue(TBook_LANGUAGE_ID(), record.getValue(TBook_LANGUAGE_ID()));
            k.setValue(TBook_TITLE(), record.getValue(TBook_TITLE()));
            assertEquals(key, k);
            assertEquals(record, map3.get(k));
        }

        // List of Map
        // -----------
        Result<B> books = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();
        List<Map<String, Object>> list =
            create().selectFrom(TBook()).orderBy(TBook_ID()).fetchMaps();
        assertEquals(4, list.size());

        for (int i = 0; i < books.size(); i++) {
            for (Field<?> field : books.fields()) {
                assertEquals(books.getValue(i, field), list.get(i).get(field.getName()));
            }
        }

        // Single Map
        // ----------
        B book = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOne();
        Map<String, Object> map4 = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOneMap();
        B book2 = create().newRecord(TBook());
        B book3 = create().newRecord(TBook());

        book2.fromMap(map4);
        book3.from(map4);

        assertEquals(book, book2);
        assertEquals(book, book3);

        for (Field<?> field : books.fields()) {
            assertEquals(book.getValue(field), map4.get(field.getName()));
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

    public void testFetchMapTable() throws Exception {
        Map<B, Record> result =
        create().select()
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                .orderBy(TBook_ID())
                .fetchMap(TBook());

        assertEquals(4, result.size());
        assertEquals(BOOK_IDS,    seq(result.keySet()).map(b -> b.getValue(TBook_ID())).toList());
        assertEquals(BOOK_TITLES, seq(result.keySet()).map(b -> b.getValue(TBook_TITLE())).toList());
        result.keySet().forEach(b -> assertEquals(TBook().getRecordType(), b.getClass()));

        assertEquals(BOOK_IDS,         seq(result.values()).map(b -> b.getValue(TBook_ID())).toList());
        assertEquals(BOOK_TITLES,      seq(result.values()).map(b -> b.getValue(TBook_TITLE())).toList());
        assertEquals(BOOK_AUTHOR_IDS,  seq(result.values()).map(a -> a.getValue(TAuthor_ID())).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result.values()).map(a -> a.getValue(TAuthor_FIRST_NAME())).toList());
        assertEquals(BOOK_LAST_NAMES,  seq(result.values()).map(a -> a.getValue(TAuthor_LAST_NAME())).toList());
    }

    public void testFetchGroupsTable() throws Exception {
        Map<A, Result<Record>> result =
        create().select()
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                .orderBy(TBook_ID())
                .fetchGroups(TAuthor());

        assertEquals(2, result.size());
        assertEquals(AUTHOR_IDS,         seq(result.keySet()).map(a -> a.getValue(TAuthor_ID())).toList());
        assertEquals(AUTHOR_FIRST_NAMES, seq(result.keySet()).map(a -> a.getValue(TAuthor_FIRST_NAME())).toList());
        assertEquals(AUTHOR_LAST_NAMES,  seq(result.keySet()).map(a -> a.getValue(TAuthor_LAST_NAME())).toList());
        result.keySet().forEach(b -> assertEquals(TAuthor().getRecordType(), b.getClass()));

        assertEquals(BOOK_IDS,         seq(result.values()).flatMap(r -> r.stream()).map(b -> b.getValue(TBook_ID())).toList());
        assertEquals(BOOK_TITLES,      seq(result.values()).flatMap(r -> r.stream()).map(b -> b.getValue(TBook_TITLE())).toList());
        assertEquals(BOOK_AUTHOR_IDS,  seq(result.values()).flatMap(r -> r.stream()).map(a -> a.getValue(TAuthor_ID())).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result.values()).flatMap(r -> r.stream()).map(a -> a.getValue(TAuthor_FIRST_NAME())).toList());
        assertEquals(BOOK_LAST_NAMES,  seq(result.values()).flatMap(r -> r.stream()).map(a -> a.getValue(TAuthor_LAST_NAME())).toList());
    }

    public void testFetchGroups() throws Exception {
        // Key -> Record Map
        // -----------------

        // Grouping by BOOK.ID
        Map<Integer, Result<B>> map1 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchGroups(TBook_ID());
        for (Entry<Integer, Result<B>> entry : map1.entrySet()) {
            assertEquals(1, entry.getValue().size());
            assertEquals(1, entry.getValue().getValues(TBook_ID()).size());
            assertEquals(1, entry.getValue().getValues(TBook_TITLE()).size());

            assertEquals(entry.getKey(), entry.getValue().get(0).getValue(TBook_ID()));
        }
        assertEquals(4, map1.size());
        assertEquals(BOOK_IDS, new ArrayList<Integer>(map1.keySet()));

        // Grouping by BOOK.AUTHOR_ID
        Map<Integer, Result<B>> map2 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchGroups(TBook_AUTHOR_ID());
        assertEquals(2, map2.size());
        assertEquals(AUTHOR_IDS, new ArrayList<Integer>(map2.keySet()));

        Iterator<Entry<Integer, Result<B>>> it = map2.entrySet().iterator();
        Entry<Integer, Result<B>> entry21 = it.next();
        assertEquals(2, entry21.getValue().size());
        assertEquals(1, (int) entry21.getValue().get(0).getValue(TBook_ID()));
        assertEquals(2, (int) entry21.getValue().get(1).getValue(TBook_ID()));
        assertEquals(1, (int) entry21.getValue().getValues(TBook_ID()).get(0));
        assertEquals(2, (int) entry21.getValue().getValues(TBook_ID()).get(1));

        Entry<Integer, Result<B>> entry22 = it.next();
        assertEquals(2, entry22.getValue().size());
        assertEquals(3, (int) entry22.getValue().get(0).getValue(TBook_ID()));
        assertEquals(4, (int) entry22.getValue().get(1).getValue(TBook_ID()));
        assertEquals(3, (int) entry22.getValue().getValues(TBook_ID()).get(0));
        assertEquals(4, (int) entry22.getValue().getValues(TBook_ID()).get(1));

        assertFalse(it.hasNext());

        // Key -> Value Map
        // ----------------

        // Grouping by BOOK.ID
        Map<Integer, List<String>> map3 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchGroups(TBook_ID(), TBook_TITLE());
        ArrayList<List<String>> map3Values = new ArrayList<List<String>>(map3.values());

        assertEquals(4, map3.size());
        assertEquals(BOOK_IDS, new ArrayList<Integer>(map3.keySet()));
        for (int i = 0; i < 4; i++) {
            assertEquals(1, map3Values.get(i).size());
            assertEquals(BOOK_TITLES.get(i), map3Values.get(i).get(0));
        }

        // Grouping by BOOK.AUTHOR_ID
        Map<Integer, List<String>> map4 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchGroups(TBook_AUTHOR_ID(), TBook_TITLE());
        ArrayList<List<String>> map4Values = new ArrayList<List<String>>(map4.values());

        assertEquals(2, map4.size());
        assertEquals(AUTHOR_IDS, new ArrayList<Integer>(map4.keySet()));
        for (int i = 0; i < 2; i++) {
            assertEquals(2, map4Values.get(i).size());
            assertEquals(BOOK_TITLES.get(i * 2 + 0), map4Values.get(i).get(0));
            assertEquals(BOOK_TITLES.get(i * 2 + 1), map4Values.get(i).get(1));
        }

        // Keys -> Record
        // --------------
        // Grouping by BOOK.AUTHOR_ID, BOOK.LANGUAGE_ID
        Map<Record, Result<B>> map5 = create().selectFrom(TBook()).orderBy(TBook_ID())
            .fetchGroups(new Field<?>[] { TBook_AUTHOR_ID(), TBook_LANGUAGE_ID() });

        Iterator<Entry<Record, Result<B>>> iterator = map5.entrySet().iterator();
        Entry<Record, Result<B>> entry1_en = iterator.next();
        assertFalse(entry1_en.getKey().changed());
        assertEquals(2, entry1_en.getValue().size());
        assertEquals(entry1_en.getKey().getValue(0), entry1_en.getValue().get(0).getValue(TBook_AUTHOR_ID()));
        assertEquals(entry1_en.getKey().getValue(0), entry1_en.getValue().get(1).getValue(TBook_AUTHOR_ID()));
        assertEquals(entry1_en.getKey().getValue(1), entry1_en.getValue().get(0).getValue(TBook_LANGUAGE_ID()));
        assertEquals(entry1_en.getKey().getValue(1), entry1_en.getValue().get(1).getValue(TBook_LANGUAGE_ID()));

        Entry<Record, Result<B>> entry2_pt = iterator.next();
        assertFalse(entry2_pt.getKey().changed());
        assertEquals(1, entry2_pt.getValue().size());
        assertEquals(entry2_pt.getKey().getValue(0), entry2_pt.getValue().get(0).getValue(TBook_AUTHOR_ID()));
        assertEquals(entry2_pt.getKey().getValue(1), entry2_pt.getValue().get(0).getValue(TBook_LANGUAGE_ID()));

        Entry<Record, Result<B>> entry2_de = iterator.next();
        assertFalse(entry2_de.getKey().changed());
        assertEquals(1, entry2_de.getValue().size());
        assertEquals(entry2_de.getKey().getValue(0), entry2_de.getValue().get(0).getValue(TBook_AUTHOR_ID()));
        assertEquals(entry2_de.getKey().getValue(1), entry2_de.getValue().get(0).getValue(TBook_LANGUAGE_ID()));

        assertFalse(iterator.hasNext());

        // Grouping by BOOK.AUTHOR_ID, BOOK.LANGUAGE_ID, BOOK.TITLE
        Map<Record, Result<B>> map6 = create().selectFrom(TBook()).orderBy(TBook_ID())
            .fetchGroups(new Field<?>[] { TBook_ID(), TBook_LANGUAGE_ID(), TBook_TITLE() });
        assertEquals(4, map6.size());

        for (Record key : map6.keySet()) {
            assertFalse(key.changed());
            Result<B> result = map6.get(key);
            assertEquals(1, result.size());
            assertEquals(key.getValue(0), result.get(0).getValue(TBook_ID()));
            assertEquals(key.getValue(1), result.get(0).getValue(TBook_LANGUAGE_ID()));
            assertEquals(key.getValue(2), result.get(0).getValue(TBook_TITLE()));
        }
    }

    public void testFetchArray() throws Exception {

        // fetchOne
        // --------
        B book = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOne();
        Object[] bookArray = create().selectFrom(TBook()).where(TBook_ID().equal(1)).fetchOneArray();
        for (int i = 0; i < TBook().fieldsRow().size(); i++) {
            assertEquals(book.getValue(i), bookArray[i]);
        }

        // fetch
        // -----
        Result<B> books = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();
        B[] booksArray = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArray();
        Object[][] booksMatrix = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArrays();

        for (int j = 0; j < books.size(); j++) {
            B bookJ1 = books.get(j);
            B bookJ2 = booksArray[j];
            Object[] array = bookJ1.intoArray();

            B book2 = create().newRecord(TBook());
            B book3 = create().newRecord(TBook());
            book2.fromArray(array);
            book3.from(array);
            assertEquals(bookJ1, book2);
            assertEquals(bookJ1, book3);
            assertEquals(bookJ2, book2);
            assertEquals(bookJ2, book3);

            for (int i = 0; i < TBook().fieldsRow().size(); i++) {
                assertEquals(books.getValue(j, i), booksMatrix[j][i]);
                assertEquals(books.getValue(j, i), books.intoArrays()[j][i]);
                assertEquals(books.get(j).getValue(i), books.get(j).intoArray()[i]);
            }
        }

        // fetch single field
        // ------------------

        // [#1722] Check the actual returned type of arrays, also
        String[] array1 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArray(TBook_TITLE());
        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetch(TBook_TITLE()),
        Arrays.asList(array1));

        Object[] array2 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArray(1);
        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetch(1), Arrays.asList(array2));
        assertTrue(array2 instanceof Integer[]);

        Object[] array3 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetchArray(TBook_ID().getName());
        assertEquals(create().selectFrom(TBook()).orderBy(TBook_ID()).fetch(TBook_ID().getName()),
            Arrays.asList(array3));
        assertTrue(array3 instanceof Integer[]);
    }

    public void testFetchSet() throws Exception {
        assertEquals(
            new LinkedHashSet<>(AUTHOR_IDS),
            create().select(TBook_AUTHOR_ID()).from(TBook()).fetchSet(TBook_AUTHOR_ID())
        );
    }

    public void testFetch() throws Exception {
        SelectQuery<?> q = create().selectQuery();
        q.addFrom(TAuthor());
        q.addSelect(TAuthor().fields());
        q.addOrderBy(TAuthor_LAST_NAME());

        Result<?> result = q.fetch();

        assertEquals(2, result.size());
        assertEquals("Coelho", result.get(0).getValue(TAuthor_LAST_NAME()));
        assertEquals("Orwell", result.get(1).getValue(TAuthor_LAST_NAME()));

        try {
            q.fetchOne();
            fail();
        }
        catch (TooManyRowsException expected) {}

        Record record = q.fetchAny();
        assertEquals("Coelho", record.getValue(TAuthor_LAST_NAME()));
    }

    public void testFetchOptional() throws Exception {
        ResultQuery<B> q1 =
        create().selectFrom(TBook())
                .where(TBook_ID().eq(1));

        Optional<B> b1 = q1.fetchOptional();
        assertTrue(b1.isPresent());
        assertEquals(BOOK_TITLES.get(0), b1.map(b -> b.getValue(TBook_TITLE())).get());

        ResultQuery<B> q2 =
        create().selectFrom(TBook())
                .where(TBook_ID().eq(5));

        Optional<B> b2 = q2.fetchOptional();
        assertFalse(b2.isPresent());
    }

    public void testFetchStream() throws Exception {
        assertSame(BOOK_IDS,
            create().fetchStream(TBook())
                    .map(b -> b.getValue(TBook_ID()))
                    .collect(Collectors.toList()));
    }

    public void testFetchValue() throws Exception {
        assertEquals(1, (int) create().fetchValue(selectOne()));
        assertSame(asList(1, 2, 3, 4), create().fetchValues(TBook_ID()));
    }

    public void testFetchExists() throws Exception {
        assertTrue(create().fetchExists(TAuthor()));
        assertTrue(create().fetchExists(TAuthor(), TAuthor_ID().eq(1)));
        assertFalse(create().fetchExists(TAuthor(), TAuthor_ID().eq(0)));
        assertTrue(create().fetchExists(DSL.select(TBook_ID()).from(TBook()).groupBy(TBook_ID())));
    }

    public void testFetchAny() throws Exception {
        A a1 = create().fetchAny(TAuthor());
        assertTrue(asList(1, 2).contains(a1.getValue(TAuthor_ID())));

        A a2 = create().fetchAny(TAuthor(), TAuthor_ID().eq(1));
        assertEquals(1, (int) a2.getValue(TAuthor_ID()));

        A a3 = create().fetchAny(TAuthor(), TAuthor_ID().eq(3));
        assertNull(a3);
    }

    public void testFetchMany() throws Exception {
        assumeFamilyNotIn(ACCESS, INFORMIX, ORACLE, SYBASE, SQLITE);
        Results results = create().fetchMany(
            "select * from t_book order by " + TBook_ID().getName());

        assertEquals(1, results.size());
        assertEquals(4, results.get(0).size());
        assertEquals(BOOK_IDS, results.get(0).getValues(TBook_ID(), Integer.class));
        assertEquals(BOOK_TITLES, results.get(0).getValues(TBook_TITLE()));
    }

    public void testFetchWithoutResults() throws Exception {
        /* [pro] */
        switch (dialect()) {
            case ASE:
                log.info("SKIPPING", "Fetch without results tests");
                return;
        }

        /* [/pro] */
        Result<Record> result =
        create().fetch(
            create().update(TAuthor())
                    .set(TAuthor_FIRST_NAME(), "Hugo")
                    .where(TAuthor_ID().equal(100))
                    .getSQL(INLINED));

        assertNotNull(result);
        assertEquals(0, result.size());

        Results results =
        create().fetchMany(
            create().update(TAuthor())
                    .set(TAuthor_FIRST_NAME(), "Hugo")
                    .where(TAuthor_ID().equal(100))
                    .getSQL(INLINED));

        assertNotNull(result);
        assertEquals(0, results.size());

    }

    public void testFetchIntoWithAnnotations() throws Exception {
        // TODO [#791] Fix test data and have all upper case columns everywhere
        assumeFamilyNotIn(ACCESS, ASE, INFORMIX, INGRES, CUBRID, POSTGRES);

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

//      TODO [#3614] reactivate this once we know how to implement it efficiently and securely
//        assertEquals(1, result1.get(0).getId6());
//        assertEquals(2, result1.get(1).getId6());
//        assertEquals(3, result1.get(2).getId6());
//        assertEquals(4, result1.get(3).getId6());
//
//        assertEquals(1, result1.get(0).id7);
//        assertEquals(2, result1.get(1).id7);
//        assertEquals(3, result1.get(2).id7);
//        assertEquals(4, result1.get(3).id7);
//
//        assertEquals(1, result1.get(0).id8);
//        assertEquals(2, result1.get(1).id8);
//        assertEquals(3, result1.get(2).id8);
//        assertEquals(4, result1.get(3).id8);

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

        A author1 = create().newRecord(TAuthor());
        A author2 = create().newRecord(TAuthor());
        author2.setValue(TAuthor_DATE_OF_BIRTH(), new Date(1L));

        DatesWithAnnotations dates1 = author1.into(DatesWithAnnotations.class);
        DatesWithAnnotations dates2 = author2.into(DatesWithAnnotations.class);

        checkDatesWithAnnotations(dates1, dates2);

        // [#1688] Check both types of into() methods
        DatesWithAnnotations dates3a = new DatesWithAnnotations();
        DatesWithAnnotations dates4a = new DatesWithAnnotations();
        DatesWithAnnotations dates3b = author1.into(dates3a);
        DatesWithAnnotations dates4b = author2.into(dates4a);

        assertTrue(dates3a == dates3b);
        assertTrue(dates4a == dates4b);
        checkDatesWithAnnotations(dates3b, dates4b);
    }

    private void checkDatesWithAnnotations(DatesWithAnnotations dates1, DatesWithAnnotations dates2) {
        assertNull(dates1.cal1);
        assertNull(dates1.cal2);
        assertNull(dates1.cal3);
        assertNull(dates1.date1);
        assertNull(dates1.date2);
        assertNull(dates1.date3);
        assertNull(dates1.long1);
        assertNull(dates1.long2);
        assertNull(dates1.long3);
        assertEquals(0L, dates1.primitiveLong1);
        assertEquals(0L, dates1.primitiveLong2);
        assertEquals(0L, dates1.primitiveLong3);

        assertEquals(1L, dates2.cal1.getTime().getTime());
        assertEquals(1L, dates2.cal2.getTime().getTime());
        assertEquals(1L, dates2.cal3.getTime().getTime());
        assertEquals(1L, dates2.date1.getTime());
        assertEquals(1L, dates2.date2.getTime());
        assertEquals(1L, dates2.date3.getTime());
        assertEquals(1L, (long) dates2.long1);
        assertEquals(1L, (long) dates2.long2);
        assertEquals(1L, (long) dates2.long3);
        assertEquals(1L, dates2.primitiveLong1);
        assertEquals(1L, dates2.primitiveLong2);
        assertEquals(1L, dates2.primitiveLong3);
    }

    public void testFetchIntoWithoutAnnotations() throws Exception {
        // TODO [#791] Fix test data and have all upper case columns everywhere
        assumeFamilyNotIn(ACCESS, ASE, INFORMIX, INGRES, CUBRID, POSTGRES);

        Select<?> select =
        create().select(
                    TBook_ID(),
                    TBook_TITLE(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME(),
                    TAuthor_DATE_OF_BIRTH(),
                    TAuthor_ID().as("THE_AUTHOR.ID"),
                    TAuthor_FIRST_NAME().as("THE_AUTHOR.FIRST_NAME"),
                    TAuthor_LAST_NAME().as("THE_AUTHOR.LAST_NAME"),
                    TAuthor_FIRST_NAME().as("THE_AUTHOR.FULL_NAME.FIRST_NAME"),
                    TAuthor_LAST_NAME().as("THE_AUTHOR.FULL_NAME.LAST_NAME"),
                    TAuthor_DATE_OF_BIRTH().as("THE_AUTHOR.DATE_OF_BIRTH")
                )
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                .orderBy(TBook_ID());

        List<BookWithoutAnnotations> result1 = select.fetchInto(BookWithoutAnnotations.class);
        List<IBookWithoutAnnotations> result2 = select.fetchInto(IBookWithoutAnnotations.class);

        assertEquals(4, result1.size());
        assertEquals(4, result2.size());

        assertEquals(BOOK_IDS, seq(result1).map(b -> b.id).toList());
        assertEquals(BOOK_IDS, seq(result1).map(b -> b.id2).toList());
        assertEquals(BOOK_IDS, seq(result1).map(b -> b.ID).toList());
        assertEquals(BOOK_IDS, seq(result2).map(b -> (int) b.getId()).toList());

        assertEquals(BOOK_AUTHOR_IDS , seq(result1).map(b -> b.THE_AUTHOR.ID).toList());
        assertEquals(BOOK_AUTHOR_IDS , seq(result1).map(b -> b.theAuthor.ID).toList());
        assertEquals(BOOK_AUTHOR_IDS , seq(result1).map(b -> b.a.ID).toList());

        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.THE_AUTHOR.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.THE_AUTHOR.FULL_NAME.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.THE_AUTHOR.fullName.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.THE_AUTHOR.f.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.theAuthor.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.theAuthor.FULL_NAME.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.theAuthor.fullName.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.theAuthor.f.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.a.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.a.FULL_NAME.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.a.fullName.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.a.f.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result2).map(b -> b.getTheAuthor().getFullName().firstName).toList());

        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.THE_AUTHOR.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.THE_AUTHOR.FULL_NAME.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.THE_AUTHOR.fullName.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.THE_AUTHOR.f.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.theAuthor.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.theAuthor.FULL_NAME.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.theAuthor.fullName.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.theAuthor.f.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.a.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.a.FULL_NAME.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.a.fullName.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result1).map(b -> b.a.f.lastName).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result2).map(b -> b.getLAST_NAME()).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result2).map(b -> b.getTheAuthor().getLAST_NAME()).toList());
        assertEquals(BOOK_LAST_NAMES , seq(result2).map(b -> b.getTheAuthor().getFullName().lastName).toList());


        assertEquals(BOOK_TITLES, seq(result1).map(b -> b.title).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.firstName).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(result1).map(b -> b.firstName2).toList());
        assertEquals(BOOK_LAST_NAMES, seq(result1).map(b -> b.lastName).toList());
        assertEquals(BOOK_LAST_NAMES, seq(result1).map(b -> b.lastName2).toList());
        assertEquals(BOOK_LAST_NAMES, seq(result1).map(b -> b.LAST_NAME).toList());
    }

    public void testRecordFromWithAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        assumeFamilyNotIn(ACCESS, ASE, INFORMIX, INGRES, CUBRID, POSTGRES);

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

    public void testRecordFromWithoutAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        assumeFamilyNotIn(ACCESS, ASE, INFORMIX, INGRES, CUBRID, POSTGRES);

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

    public void testRecordFromWithIdentity() throws Exception {
        if (TIdentityPK() == null) {
            log.info("SKIPPING", "Reflection with IDENTITY tests");
            return;
        }

        jOOQAbstractTest.reset = false;

        // [#1818] SQL Server doesn't like inserting IDENTITY values...
        IPK record = create().newRecord(TIdentityPK(), new Identity());
        assertEquals(10, (int) record.getValue(TIdentityPK_ID()));
        assertEquals(11, (int) record.getValue(TIdentityPK_VAL()));
        assertEquals(1, record.store());
    }

    static class Identity {
        public int id = 10;
        public int val = 11;
    }

    public void testRecordFromUpdatePK() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        assumeFamilyNotIn(ACCESS, ASE, INFORMIX, INGRES, CUBRID, POSTGRES);

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

    public void testRecordFrom() throws Exception {
        A author;

        // Mapping through Objects
        // -----------------------
        AuthorWithoutAnnotations object = new AuthorWithoutAnnotations();
        object.firstName = "A";
        object.lastName = "B";
        object.ID = 13;

        author = create().newRecord(TAuthor());
        author.from(object, TAuthor_FIRST_NAME(), TAuthor_LAST_NAME());
        testRecordFromAssertions(object, author);

        author = create().newRecord(TAuthor());
        author.from(object, TAuthor_FIRST_NAME().getName(), TAuthor_LAST_NAME().getName());
        testRecordFromAssertions(object, author);

        // Mapping through Maps
        // --------------------
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(TAuthor_FIRST_NAME().getName(), "A");
        map.put(TAuthor_LAST_NAME().getName(), "B");
        map.put(TAuthor_ID().getName(), 13);

        author = create().newRecord(TAuthor());
        author.fromMap(map, TAuthor_FIRST_NAME(), TAuthor_LAST_NAME());
        testRecordFromAssertions(object, author);

        author = create().newRecord(TAuthor());
        author.fromMap(map, TAuthor_FIRST_NAME().getName(), TAuthor_LAST_NAME().getName());
        testRecordFromAssertions(object, author);

        // Mapping through Arrays
        // ----------------------
        Object[] array = { 13, "A", "B" };

        author = create().newRecord(TAuthor());
        author.fromArray(array, new Field[] { TAuthor_FIRST_NAME(), TAuthor_LAST_NAME() });
        testRecordFromAssertions(object, author);

        author = create().newRecord(TAuthor());
        author.fromArray(array, new String[] { TAuthor_FIRST_NAME().getName(), TAuthor_LAST_NAME().getName() });
        testRecordFromAssertions(object, author);
    }

    private void testRecordFromAssertions(AuthorWithoutAnnotations object, A author) {
        assertNull(author.getValue(TAuthor_ID()));
        assertEquals(object.firstName, author.getValue(TAuthor_FIRST_NAME()));
        assertEquals(object.lastName, author.getValue(TAuthor_LAST_NAME()));
    }

    public void testReflectionWithAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        assumeFamilyNotIn(ACCESS, ASE, INFORMIX, INGRES, CUBRID, POSTGRES);

        // [#933] Map values to char / Character
        A author1 = create().newRecord(TAuthor());

        // [#1688] Check both types of into() methods
        for (CharWithAnnotations c1 : asList(
                author1.into(CharWithAnnotations.class),
                author1.into(new CharWithAnnotations()))) {

            assertEquals((char) 0, c1.id1);
            assertEquals(null, c1.id2);
            assertEquals((char) 0, c1.last1);
            assertEquals(null, c1.last2);
        }

        A author2 = create().newRecord(TAuthor());
        author2.setValue(TAuthor_ID(), 1);
        author2.setValue(TAuthor_LAST_NAME(), "a");

        // [#1688] Check both types of into() methods
        for (CharWithAnnotations c2 : asList(
                author2.into(CharWithAnnotations.class),
                author2.into(new CharWithAnnotations()))) {

            assertEquals('1', c2.id1);
            assertEquals('1', c2.id2.charValue());
            assertEquals('a', c2.last1);
            assertEquals('a', c2.last2.charValue());

            A author3 = create().newRecord(TAuthor(), c2);
            assertEquals('1', author3.getValue(TAuthor_ID(), char.class).charValue());
            assertEquals('1', author3.getValue(TAuthor_ID(), Character.class).charValue());
            assertEquals('a', author3.getValue(TAuthor_LAST_NAME(), char.class).charValue());
            assertEquals('a', author3.getValue(TAuthor_LAST_NAME(), Character.class).charValue());
        }

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

    public void testReflectionWithoutAnnotations() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        assumeFamilyNotIn(ACCESS, ASE, INFORMIX, INGRES, CUBRID, POSTGRES);

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

    public void testReflectionWithImmutablesAndConstructorProperties() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        assumeFamilyNotIn(ACCESS, ASE, INFORMIX, INGRES, CUBRID, POSTGRES);

        Record author =
        create().select(
                    TAuthor_LAST_NAME(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_ID(),
                    TAuthor_YEAR_OF_BIRTH())
                .from(TAuthor())
                .where(TAuthor_ID().equal(1))
                .fetchOne();

        ImmutableAuthorWithConstructorProperties into1 =
            author.into(ImmutableAuthorWithConstructorProperties.class);
        assertNull(into1.getDateOfBirth());
        assertEquals(1, into1.getId());
        assertEquals(AUTHOR_FIRST_NAMES.get(0), into1.getFirstName());
        assertEquals(AUTHOR_LAST_NAMES.get(0), into1.getLastName());

        ImmutableAuthorWithConstructorPropertiesAndPublicFields into2 =
            author.into(ImmutableAuthorWithConstructorPropertiesAndPublicFields.class);
        assertNull(into2.dateOfBirth);
        assertEquals(1, into2.id);
        assertEquals(AUTHOR_FIRST_NAMES.get(0), into2.firstName);
        assertEquals(AUTHOR_LAST_NAMES.get(0), into2.lastName);

        ImmutableAuthorWithConstructorPropertiesAndJPAAnnotations into3 =
            author.into(ImmutableAuthorWithConstructorPropertiesAndJPAAnnotations.class);
        assertNull(into3.getF4());
        assertEquals(1, into3.getF3());
        assertEquals(AUTHOR_FIRST_NAMES.get(0), into3.getF1());
        assertEquals(AUTHOR_LAST_NAMES.get(0), into3.getF2());

        ImmutableAuthorWithConstructorPropertiesAndJPAAnnotationsAndPublicFields into4 =
            author.into(ImmutableAuthorWithConstructorPropertiesAndJPAAnnotationsAndPublicFields.class);
        assertNull(into4.f4);
        assertEquals(1, into4.f3);
        assertEquals(AUTHOR_FIRST_NAMES.get(0), into4.f1);
        assertEquals(AUTHOR_LAST_NAMES.get(0), into4.f2);

        ImmutableAuthorWithConstructorPropertiesButNoMatchingGetters into5 =
            author.into(ImmutableAuthorWithConstructorPropertiesButNoMatchingGetters.class);
        assertNull(into5.get_date_of_birth());
        assertEquals(1, into5.get_id());
        assertEquals(AUTHOR_FIRST_NAMES.get(0), into5.get_first_name());
        assertEquals(AUTHOR_LAST_NAMES.get(0), into5.get_last_name());

    }

    public void testFetchIntoTableRecords() throws Exception {

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

    public void testFetchIntoTableRecordsWithColumnAmbiguities() throws Exception {

        // [#2836] When fetching into TableRecord types, the "expected" behaviour would
        // be to map "obvious" columns onto their corresponding values
        // Fixing this might be a "dangerous" change.
        B book =
        create().select()
                .from(TBook())
                .join(TAuthor())
                .on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                .where(TBook_ID().eq(4))
                .fetchOneInto(TBook().getRecordType());

        assertEquals(4, (int) book.getValue(TBook_ID()));
    }

    public void testFetchAttachables() throws Exception {
        // [#2869] DefaultRecordMapper should recognise Attachable types and attach them
        // according to the Settings.

        B b1 = create().selectFrom(TBook()).where(TBook_ID().eq(1)).fetchOne();
        B b2 = b1.into(TBook().getRecordType());

        assertNotNull(((AttachableInternal) b1).configuration());
        assertNotNull(((AttachableInternal) b2).configuration());

        B b3 = create(new Settings().withAttachRecords(false))
                    .selectFrom(TBook()).where(TBook_ID().eq(1)).fetchOne();
        B b4 = b3.into(TBook().getRecordType());

        assertNull(((AttachableInternal) b3).configuration());
        assertNull(((AttachableInternal) b4).configuration());
    }

    public void testFetchIntoTableRecordsWithUDTs() throws Exception {
        if (cUAddressType() == null) {
            log.info("SKIPPING", "Skipping batch store with UDT tests");
            return;
        }

        // [#2137] Fetch a record containing a UDT into its TableRecord type
        List<A> authors =
        create().selectFrom(TAuthor())
                .orderBy(TAuthor_ID())
                .fetch()
                .into(TAuthor().getRecordType());

        assertEquals(2, authors.size());
        assertNotNull(authors.get(0).getValue(TAuthor_ADDRESS()));
        assertNotNull(authors.get(1).getValue(TAuthor_ADDRESS()));

        assertEquals("Hampstead", on(authors.get(0).getValue(TAuthor_ADDRESS())).call("getCity").get());
        assertEquals("Rio de Janeiro", on(authors.get(1).getValue(TAuthor_ADDRESS())).call("getCity").get());
    }

    public void testFetchIntoRecordClass() throws Exception {
        B b1 = create().selectFrom(TBook()).where(TBook_ID().eq(1)).fetchOne();
        B b2 = b1.into(TBook().getRecordType());

        assertEquals(b1, b2);
        assertEquals(b1.getValue(TBook_TITLE()), b2.getValue(TBook_TITLE()));
        assertEquals(b1.changed(), b2.changed());
        assertEquals(b1.changed(TBook_TITLE()), b2.changed(TBook_TITLE()));
        assertEquals(b1.original(), b2.original());
        assertEquals(b1.original(TBook_TITLE()), b2.original(TBook_TITLE()));

        b1.setValue(TBook_TITLE(), "abc");
        b2 = b1.into(TBook().getRecordType());
        assertEquals("abc", b1.getValue(TBook_TITLE()));
        assertEquals("abc", b2.getValue(TBook_TITLE()));
        assertEquals(b1.changed(), b2.changed());
        assertTrue(b1.changed());
        assertTrue(b2.changed());
        assertFalse(b1.changed(TBook_ID()));
        assertFalse(b2.changed(TBook_ID()));
        assertTrue(b1.changed(TBook_TITLE()));
        assertTrue(b2.changed(TBook_TITLE()));
        assertEquals(b1.original(), b2.original());
        assertEquals("1984", b1.original(TBook_TITLE()));
        assertEquals("1984", b2.original(TBook_TITLE()));
    }

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
        assertNull(result1.field(TAuthor_FIRST_NAME()));
        assertNull(result1.field(TAuthor_LAST_NAME()));

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
        book.changed(true);
        assertEquals(1, book.store());
        Result<B> books2 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();
        assertEquals(5, books2.size());
        assertEquals("Animal Farm", books2.getValue(4, TBook_TITLE()));
    }

    public void testFetchIntoTables() throws Exception {
        Result<Record> result =
        create().select()
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
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

    public void testFetchIntoAliasedTables() throws Exception {
        Table<B> b1 = TBook().as("b1");
        Table<B> b2 = TBook().as("b2");

        Result<Record> result =
        create().select()
                .from(b1)
                .leftOuterJoin(b2).on(b1.field(TBook_ID()).eq(b2.field(TBook_AUTHOR_ID())))
                .orderBy(b1.field(TBook_ID()), b2.field(TBook_ID()))
                .fetch();

        List<Tuple2<B, B>> r1 = result.map(r -> tuple(
            r.into(b1),
            r.into(b2)
        ));

        List<Tuple2<Integer, Integer>> r2 = result.map(r -> tuple(
            r.into(b1.field(TBook_ID())).value1(),
            r.into(b2.field(TBook_ID())).value1()
        ));

        assertEquals(asList(1, 1, 2, 2, 3, 4), seq(r1).map(t -> t.v1.getValue(TBook_ID())).toList());
        assertEquals(asList(1, 1, 1, 1, 2, 2), seq(r1).map(t -> t.v1.getValue(TBook_AUTHOR_ID())).toList());
        assertEquals(asList(1, 2, 3, 4, null, null), seq(r1).map(t -> t.v2.getValue(TBook_ID())).toList());
        assertEquals(asList(1, 1, 2, 2, null, null), seq(r1).map(t -> t.v2.getValue(TBook_AUTHOR_ID())).toList());

        assertEquals(asList(1, 1, 2, 2, 3, 4), seq(r2).map(Tuple2::v1).toList());
        assertEquals(asList(1, 2, 3, 4, null, null), seq(r2).map(Tuple2::v2).toList());
    }

    public void testFetchIntoCustomTable() throws Exception {

        // TODO [#791] Fix test data and have all upper case columns everywhere
        assumeFamilyNotIn(ACCESS, ASE, INFORMIX, INGRES, CUBRID, POSTGRES, REDSHIFT);

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

    public void testFetchResultSet() throws Exception {
        for (int i = 0; i < 2; i++) {
            // Fetching ResultSets into Results
            assertEquals(
                create().fetch("select * from t_author order by id"),
                create().fetch(create().resultQuery("select * from t_author order by id").fetchResultSet()));

            // [#1723] Fetching ResultSets into Cursors (Firebird can't have two
            // open Cursors in parallel...
            Result<Record> c1 = create().fetch("select * from t_author order by id");
            Cursor<Record> c2 = create().fetchLazy(create().resultQuery("select * from t_author order by id").fetchResultSet());

            for (int j = 0; j < 2; j++) {
                assertTrue(c2.hasNext());
                assertEquals(c1.remove(0), c2.fetchOne());
            }

            assertFalse(c2.hasNext());

            // Fetching ResultSets
            ResultSet rs = create().resultQuery("select * from t_author order by id").fetchResultSet();
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));
            assertFalse(rs.wasNull());
            assertEquals(AUTHOR_FIRST_NAMES.get(0), rs.getString(2));
            assertEquals(AUTHOR_LAST_NAMES.get(0), rs.getString(3));

            // Some JDBC drivers don't cache ResultSet. The same value cannot be fetched twice, then.
            if (!asList(ACCESS).contains(dialect().family())) {
                assertEquals(1, rs.getInt(1));
                assertEquals(1, rs.getInt(TAuthor_ID().getName()));
                assertEquals((short) 1, rs.getShort(TAuthor_ID().getName()));
                assertEquals(1L, rs.getLong(TAuthor_ID().getName()));
                assertEquals(AUTHOR_FIRST_NAMES.get(0), rs.getString(TAuthor_FIRST_NAME().getName()));
                assertEquals(AUTHOR_LAST_NAMES.get(0), rs.getString(TAuthor_LAST_NAME().getName()));
            }

            assertTrue(rs.next());
            assertEquals(2, rs.getInt(1));
            assertFalse(rs.wasNull());

            // Some JDBC drivers don't cache ResultSet. The same value cannot be fetched twice, then.
            if (!asList(ACCESS).contains(dialect().family())) {
                assertEquals(2, rs.getInt(1));
                assertEquals(2, rs.getInt(TAuthor_ID().getName()));
            }

            assertFalse(rs.next());
            rs.close();

            // [#1323] Check if Postgres' pre-9.0 encoding of binary data works, too
            if (family() == POSTGRES && i == 0) {
                create().execute("set bytea_output to escape");
            }

            // Otherwise, don't repeat this test
            else {
                break;
            }
        }
    }

    public void testFetchResultSetValue() throws Exception {
        assertEquals("1", "" + create().fetchValue(
            create().fetchLazy("select 1 from " + TBook().getName() + " where " + TBook_ID().getName() + " = 1").resultSet()));
        assertEquals("1", create().fetchValue(
            create().fetchLazy("select 1 from " + TBook().getName() + " where " + TBook_ID().getName() + " = 1").resultSet(),
            String.class));
        assertEquals("1", create().fetchValue(
            create().fetchLazy("select 1 from " + TBook().getName() + " where " + TBook_ID().getName() + " = 1").resultSet(),
            SQLDataType.VARCHAR));
        assertEquals("1", create().fetchValue(
            create().fetchLazy("select 1 from " + TBook().getName() + " where " + TBook_ID().getName() + " = 1").resultSet(),
            TBook_TITLE()));
    }

    public void testFetchResultSetWithCoercedTypes() throws Exception {
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        try {

            // Coerce classes
            rs1 = getConnection().createStatement().executeQuery("select id, year_of_birth from t_author order by id");
            Result<Record> r1 = create().fetch(rs1, String.class, Long.class);

            assertEquals(2, r1.size());
            assertEquals(String.class, r1.field(0).getType());
            assertEquals(Long.class, r1.field(1).getType());
            assertEquals("id", r1.field(0).getName().toLowerCase());
            assertEquals("year_of_birth", r1.field(1).getName().toLowerCase());
            assertEquals(asList("1", "2"), r1.getValues(0));
            assertEquals(asList(1903L, 1947L), r1.getValues(1));

            // Coerce data types
            rs2 = getConnection().createStatement().executeQuery("select id, year_of_birth from t_author order by id");
            Result<Record> r2 = create().fetch(rs2, SQLDataType.VARCHAR, SQLDataType.BIGINT);

            assertEquals(2, r2.size());
            assertEquals(String.class, r2.field(0).getType());
            assertEquals(Long.class, r2.field(1).getType());
            assertEquals("id", r2.field(0).getName().toLowerCase());
            assertEquals("year_of_birth", r2.field(1).getName().toLowerCase());
            assertEquals(asList("1", "2"), r2.getValues(0));
            assertEquals(asList(1903L, 1947L), r2.getValues(1));

            // Coerce fields
            rs3 = getConnection().createStatement().executeQuery("select id, year_of_birth from t_author order by id");
            Result<Record> r3 = create().fetch(rs3, field("x", String.class), field("y", Long.class));

            assertEquals(2, r3.size());
            assertEquals(String.class, r3.field(0).getType());
            assertEquals(Long.class, r3.field(1).getType());
            assertEquals("x", r3.field(0).getName().toLowerCase());
            assertEquals("y", r3.field(1).getName().toLowerCase());
            assertEquals(asList("1", "2"), r3.getValues(0));
            assertEquals(asList(1903L, 1947L), r3.getValues(1));
        }
        finally {
            JDBCUtils.safeClose(rs1);
            JDBCUtils.safeClose(rs2);
            JDBCUtils.safeClose(rs3);
        }
    }

    public void testResultQueryStream() throws Exception {
        List<Tuple2<A, B>> list =
        create().selectFrom(TAuthor())
                .orderBy(TAuthor_ID())
                .stream()
                .flatMap(a -> create()
                    .selectFrom(TBook())
                    .where(TBook_AUTHOR_ID().eq(a.getValue(TAuthor_ID())))
                    .orderBy(TBook_ID())
                    .stream()
                    .map(b -> tuple(a, b))
                )
                .collect(Collectors.toList());

        assertEquals(4, list.size());
        assertEquals(BOOK_AUTHOR_IDS, seq(list).map(t -> t.v1.getValue(TAuthor_ID())).toList());
        assertEquals(BOOK_FIRST_NAMES, seq(list).map(t -> t.v1.getValue(TAuthor_FIRST_NAME())).toList());
        assertEquals(BOOK_LAST_NAMES, seq(list).map(t -> t.v1.getValue(TAuthor_LAST_NAME())).toList());
        assertEquals(BOOK_IDS, seq(list).map(t -> t.v2.getValue(TBook_ID())).toList());
        assertEquals(BOOK_TITLES, seq(list).map(t -> t.v2.getValue(TBook_TITLE())).toList());
    }

    public void testResultQueryStreamWithAutoCloseable() throws Exception {
        ResultSetCloseListener l = new ResultSetCloseListener();

        // Even if a stream's consumption produces an exception, the close()
        // method must be called via AutoCloseable
        assertThrows(
            RuntimeException.class,
            () -> {
                try (Stream<A> stream = create(l)
                    .selectFrom(TAuthor())
                    .orderBy(TAuthor_ID())
                    .stream()
                    .peek(a -> { throw new RuntimeException("abort"); })) {

                    stream.findAny();
                }
            },
            e -> assertEquals("abort", e.getMessage())
        );
        assertTrue(l.rsclosed);
    }

    public void testFetchLazy() throws Exception {
        FetchSizeListener listener = new FetchSizeListener();
        DSLContext create = create(listener);

        List<Integer> sizes = family() == MYSQL
            ? Arrays.asList(Integer.MIN_VALUE, 0, 1)
            : Arrays.asList(                   0, 1);

        for (int fetchSize : sizes) {

            // ---------------------------------------------------------------------
            // A regular pass through the cursor
            // ---------------------------------------------------------------------
            Cursor<B> cursor = create.selectFrom(TBook()).orderBy(TBook_ID()).fetchSize(fetchSize).fetchLazy();
            assertFetchSize(fetchSize, listener);

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

            try {
                it.next();
                fail();
            }
            catch (NoSuchElementException expected) {}

            try {
                it.next();
                fail();
            }
            catch (NoSuchElementException expected) {}

            assertEquals(null, cursor.fetchOne());
            assertEquals(null, cursor.fetchOne());

            cursor.close();
            cursor.close();
            assertTrue(cursor.isClosed());

            // ---------------------------------------------------------------------
            // Prematurely closing the cursor
            // ---------------------------------------------------------------------
            cursor = create.selectFrom(TBook()).orderBy(TBook_ID()).fetchSize(fetchSize).fetchLazy();
            assertFetchSize(fetchSize, listener);

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
            cursor = create.selectFrom(TBook()).orderBy(TBook_ID()).fetchSize(fetchSize).fetchLazy();
            assertFetchSize(fetchSize, listener);
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

    public void testFetchLazyWithAutoCloseable() throws Exception {
        ResultSetCloseListener l = new ResultSetCloseListener();
        try (Cursor<B> c = create(l).fetchLazy(TBook())) {
            // Don't do anything
        }
        assertTrue(l.rsclosed);
    }

    @SuppressWarnings("serial")
    class ResultSetCloseListener extends DefaultExecuteListener {
        boolean rsclosed;

        @Override
        public void executeEnd(ExecuteContext ctx) {
            ctx.resultSet(new DefaultResultSet(ctx.resultSet()) {
                @Override
                public void close() throws SQLException {
                    rsclosed = true;
                    super.close();
                }
            });
        }
    }

    private void assertFetchSize(int fetchSize, FetchSizeListener listener) {
        assertEquals(listener.stmtSize[0], fetchSize);
        listener.stmtSize[0] = 0;
    }

    @SuppressWarnings("serial")
    class FetchSizeListener extends DefaultExecuteListener {
        int[] stmtSize = new int[1];

        @Override
        public void start(ExecuteContext ctx) {
            ctx.connectionProvider(new DefaultConnectionProvider(new DefaultConnection(getConnection()) {

                @Override
                public PreparedStatement prepareStatement(String sql) throws SQLException {
                    return new DefaultPreparedStatement(super.prepareStatement(sql)) {

                        @Override
                        public void setFetchSize(int rows) throws SQLException {
                            super.setFetchSize(rows);
                            stmtSize[0] = rows;
                        }
                    };
                }
            }));
        }
    }

    public void testFetchViaIterable() throws Exception {
        int i = 0;

        for (B b : create().selectFrom(TBook()).orderBy(TBook_ID())) {
            assertEquals(BOOK_IDS.get(i++), b.getValue(TBook_ID()));
        }

        assertEquals(4, i);
    }

    public void testFetchIntoGeneratedPojos() throws Exception {
        if (TAuthorPojo() == null) {
            log.info("SKIPPING", "Generated POJO tests");
            return;
        }

        List<Object> books =
        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetchInto(TBookPojo());

        assertEquals(4, books.size());
        for (int i = 0; i < 4; i++) {
            assertEquals(BOOK_IDS.get(i), on(books.get(i)).call("getId").get());
            assertEquals(BOOK_AUTHOR_IDS.get(i), on(books.get(i)).call("getAuthorId").get());
            assertEquals(BOOK_TITLES.get(i), on(books.get(i)).call("getTitle").get());
        }
    }

    public void testFetchIntoValueType() throws Exception {
        Result<Record1<Integer>> result =
        create().select(TBook_ID())
                .from(TBook())
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(BOOK_IDS, result.into(int.class));
        assertEquals(BOOK_IDS, result.into(Integer.class));
        assertEquals(BOOK_IDS_SHORT, result.into(short.class));
        assertEquals(BOOK_IDS_SHORT, result.into(Short.class));
        assertEquals(BOOK_IDS_STRING, result.into(String.class));

        try {
            create().selectFrom(TBook())
                    .fetchInto(int.class);
            fail();
        }
        catch (MappingException expected) {}
    }

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
        ResultSet rs1 = result.intoResultSet();
        check0(rs1);

        assertTrue(rs1.next());
        check1(rs1, false);

        assertFalse(rs1.previous());
        check0(rs1);

        assertFalse(rs1.absolute(0));
        check0(rs1);

        assertTrue(rs1.relative(1));
        check1(rs1, false);

        assertFalse(rs1.relative(-1));
        check0(rs1);

        assertTrue(rs1.absolute(-4));
        check1(rs1, false);

        assertFalse(rs1.absolute(-5));
        check0(rs1);

        assertTrue(rs1.absolute(1));
        check1(rs1, false);

        rs1.beforeFirst();
        check0(rs1);

        assertTrue(rs1.last());
        check4(rs1);

        rs1.afterLast();
        check5(rs1);

        assertTrue(rs1.previous());
        check4(rs1);

        assertFalse(rs1.relative(1));
        check5(rs1);

        checkMetaData(result.fieldsRow(), rs1);

        // [#1923] Record.intoResultSet() is similar
        // -----------------------------------------
        B book = create().selectFrom(TBook()).where(TBook_ID().eq(1)).fetchOne();

        ResultSet rs2 = book.intoResultSet();
        check0(rs2);

        assertTrue(rs2.next());
        check1(rs2, true);

        assertFalse(rs2.previous());
        check0(rs2);

        assertFalse(rs2.absolute(0));
        check0(rs2);

        assertTrue(rs2.relative(1));
        check1(rs2, true);

        assertFalse(rs2.relative(-1));
        check0(rs2);

        assertTrue(rs2.absolute(-1));
        check1(rs2, true);

        assertFalse(rs2.absolute(-2));
        check0(rs2);

        assertTrue(rs2.absolute(1));
        check1(rs2, true);

        rs2.beforeFirst();
        check0(rs2);

        assertTrue(rs2.last());
        check1(rs2, true);

        rs2.afterLast();
        check5(rs2);

        assertTrue(rs2.previous());
        check1(rs2, true);

        assertFalse(rs2.relative(1));
        check5(rs2);

        checkMetaData(book.fieldsRow(), rs2);
    }

    private void checkMetaData(Row row, ResultSet rs) throws SQLException {
        // Check the meta data
        ResultSetMetaData meta = rs.getMetaData();
        assertEquals(row.size(), meta.getColumnCount());
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
        assertEquals(4.0f, rs.getFloat(1), 0.0f);
        assertEquals(4.0, rs.getDouble(1), 0.0);
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

    private void check1(ResultSet rs, boolean last) throws SQLException {
        assertFalse(rs.isClosed());
        assertFalse(rs.isBeforeFirst());
        assertFalse(rs.isAfterLast());
        assertTrue(rs.isFirst());
        assertEquals(last, rs.isLast());
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
        assertEquals(1.0f, rs.getFloat(1), 0.0f);
        assertEquals(1.0, rs.getDouble(1), 0.0);
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

    public void testFetchMapPOJO() throws Exception {
        if (TBookPojo() == null) {
            log.info("SKIPPING", "Generated POJO tests");
            return;
        }

        // key -> POJO
        // Group by BOOK.ID
        Map<Integer, Object> map1 =
        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetchMap(TBook_ID(), TBookPojo());

        assertEquals(4, map1.size());
        assertEquals(BOOK_IDS, new ArrayList<Integer>(map1.keySet()));

        List<Entry<Integer, Object>> entries =
            new ArrayList<Map.Entry<Integer,Object>>(map1.entrySet());

        for (int i = 0; i < map1.size(); i++) {
            Entry<Integer, Object> entry = entries.get(i);

            assertEquals(BOOK_IDS.get(i), on(entry.getValue()).call("getId").get());
            assertEquals(BOOK_AUTHOR_IDS.get(i), on(entry.getValue()).call("getAuthorId").get());
            assertEquals(BOOK_TITLES.get(i), on(entry.getValue()).call("getTitle").get());
        }

        try {
            // Group by BOOK.AUTHOR_ID
            create().selectFrom(TBook()).orderBy(TBook_ID()).fetchMap(TBook_AUTHOR_ID(), TBookPojo());

            fail("Fetching map with the non-unique key - InvalidResultException not thrown.");
        }
        catch (Throwable t) {
            assertEquals(InvalidResultException.class, t.getClass());
        }

        // keys -> POJO
        // Grouping by BOOK.ID, BOOK.LANGUAGE_ID, BOOK.TITLE
        Map<List<?>, Object> map4 = create().selectFrom(TBook()).orderBy(TBook_ID())
            .fetchMap(new Field<?>[] { TBook_ID(), TBook_LANGUAGE_ID(), TBook_TITLE() }, TBookPojo());
        assertEquals(4, map4.keySet().size());

        for (List<?> keys : map4.keySet()) {
            Object pojo = map4.get(keys);
            assertEquals(keys.get(0), on(pojo).call("getId").get());
            assertEquals(keys.get(1), on(pojo).call("getLanguageId").get());
            assertEquals(keys.get(2), on(pojo).call("getTitle").get());
        }

        try {
         // Grouping by BOOK.AUTHOR_ID
            create().selectFrom(TBook()).orderBy(TBook_ID())
                .fetchMap(new Field<?>[] { TBook_AUTHOR_ID(), }, TBookPojo());
            fail("Fetching map with the non-unique key - InvalidResultException not thrown.");
        }
        catch (Throwable t) {
            assertEquals(InvalidResultException.class, t.getClass());
        }
    }

    public void testFetchGroupsPOJO() throws Exception {
        if (TBookPojo() == null) {
            log.info("SKIPPING", "Generated POJO tests");
            return;
        }

        // key -> POJO
        // Group by BOOK.ID
        Map<Integer, List<Object>> map1 =
        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetchGroups(TBook_ID(), TBookPojo());

        assertEquals(4, map1.size());
        assertEquals(BOOK_IDS, new ArrayList<Integer>(map1.keySet()));

        List<Entry<Integer, List<Object>>> entries =
            new ArrayList<Map.Entry<Integer,List<Object>>>(map1.entrySet());

        for (int i = 0; i < map1.size(); i++) {
            Entry<Integer, List<Object>> entry = entries.get(i);
            assertEquals(1, entry.getValue().size());

            assertEquals(BOOK_IDS.get(i), on(entry.getValue().get(0)).call("getId").get());
            assertEquals(BOOK_AUTHOR_IDS.get(i), on(entry.getValue().get(0)).call("getAuthorId").get());
            assertEquals(BOOK_TITLES.get(i), on(entry.getValue().get(0)).call("getTitle").get());
        }

        // Group by BOOK.AUTHOR_ID
        Map<Integer, List<Object>> map2 =
        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetchGroups(TBook_AUTHOR_ID(), TBookPojo());

        assertEquals(2, map2.size());
        assertEquals(AUTHOR_IDS, new ArrayList<Integer>(map2.keySet()));

        Iterator<Entry<Integer, List<Object>>> it = map2.entrySet().iterator();
        Entry<Integer, List<Object>> entry21 = it.next();
        assertEquals(2, entry21.getValue().size());
        assertEquals(BOOK_IDS.get(0), on(entry21.getValue().get(0)).call("getId").get());
        assertEquals(BOOK_AUTHOR_IDS.get(0), on(entry21.getValue().get(0)).call("getAuthorId").get());
        assertEquals(BOOK_TITLES.get(0), on(entry21.getValue().get(0)).call("getTitle").get());
        assertEquals(BOOK_IDS.get(1), on(entry21.getValue().get(1)).call("getId").get());
        assertEquals(BOOK_AUTHOR_IDS.get(1), on(entry21.getValue().get(1)).call("getAuthorId").get());
        assertEquals(BOOK_TITLES.get(1), on(entry21.getValue().get(1)).call("getTitle").get());

        Entry<Integer, List<Object>> entry22 = it.next();
        assertEquals(2, entry22.getValue().size());
        assertEquals(BOOK_IDS.get(2), on(entry22.getValue().get(0)).call("getId").get());
        assertEquals(BOOK_AUTHOR_IDS.get(2), on(entry22.getValue().get(0)).call("getAuthorId").get());
        assertEquals(BOOK_TITLES.get(2), on(entry22.getValue().get(0)).call("getTitle").get());
        assertEquals(BOOK_IDS.get(3), on(entry22.getValue().get(1)).call("getId").get());
        assertEquals(BOOK_AUTHOR_IDS.get(3), on(entry22.getValue().get(1)).call("getAuthorId").get());
        assertEquals(BOOK_TITLES.get(3), on(entry22.getValue().get(1)).call("getTitle").get());

        assertFalse(it.hasNext());

        // keys -> POJO
        // Grouping by BOOK.AUTHOR_ID, BOOK.LANGUAGE_ID
        Map<Record, List<Object>> map3 = create().selectFrom(TBook()).orderBy(TBook_ID())
            .fetchGroups(new Field<?>[] { TBook_AUTHOR_ID(), TBook_LANGUAGE_ID() }, TBookPojo());

        Iterator<Entry<Record, List<Object>>> iterator = map3.entrySet().iterator();
        Entry<Record, List<Object>> entry1_en = iterator.next();
        assertEquals(2, entry1_en.getValue().size());
        assertEquals(entry1_en.getKey().getValue(0), on(entry1_en.getValue().get(0)).call("getAuthorId").get());
        assertEquals(entry1_en.getKey().getValue(0), on(entry1_en.getValue().get(1)).call("getAuthorId").get());
        assertEquals(entry1_en.getKey().getValue(1), on(entry1_en.getValue().get(0)).call("getLanguageId").get());
        assertEquals(entry1_en.getKey().getValue(1), on(entry1_en.getValue().get(1)).call("getLanguageId").get());

        Entry<Record, List<Object>> entry2_pt = iterator.next();
        assertEquals(1, entry2_pt.getValue().size());
        assertEquals(entry2_pt.getKey().getValue(0), on(entry2_pt.getValue().get(0)).call("getAuthorId").get());
        assertEquals(entry2_pt.getKey().getValue(1), on(entry2_pt.getValue().get(0)).call("getLanguageId").get());

        Entry<Record, List<Object>> entry2_de = iterator.next();
        assertEquals(1, entry2_de.getValue().size());
        assertEquals(entry2_de.getKey().getValue(0), on(entry2_de.getValue().get(0)).call("getAuthorId").get());
        assertEquals(entry2_de.getKey().getValue(1), on(entry2_de.getValue().get(0)).call("getLanguageId").get());

        assertFalse(iterator.hasNext());

        // Grouping by BOOK.AUTHOR_ID, BOOK.LANGUAGE_ID, BOOK.TITLE
        Map<Record, List<Object>> map4 = create().selectFrom(TBook()).orderBy(TBook_ID())
            .fetchGroups(new Field<?>[] { TBook_ID(), TBook_LANGUAGE_ID(), TBook_TITLE() }, TBookPojo());
        assertEquals(4, map4.size());

        for (Record keyList : map4.keySet()) {
            List<Object> result = map4.get(keyList);
            assertEquals(1, result.size());
            assertEquals(keyList.getValue(0), on(result.get(0)).call("getId").get());
            assertEquals(keyList.getValue(1), on(result.get(0)).call("getLanguageId").get());
            assertEquals(keyList.getValue(2), on(result.get(0)).call("getTitle").get());
        }
    }

    public void testFetchWithMaxRows() throws Exception {
        Result<B> books =
        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .maxRows(2)
                .fetch();

        assertEquals(2, books.size());
        assertEquals(Arrays.asList(1, 2), books.getValues(TBook_ID()));
    }

    public void testFetchWithTimeout() throws Exception {

        // Some dialects do not really implement the timeout well. In those
        // dialects, this query will run forever
        if (dialect() != H2) {
            log.info("SKIPPING", "Dangerous timeout query");
            return;
        }

        try {

            // [#1856] The below query is *likely* to run into a timeout
            create().selectOne()
                    .from(
                        TBook(), TBook(), TBook(), TBook(),
                        TBook(), TBook(), TBook(), TBook(),
                        TBook(), TBook(), TBook(), TBook(),
                        TBook(), TBook(), TBook(), TBook(),
                        TBook(), TBook(), TBook(), TBook(),
                        TBook(), TBook(), TBook(), TBook(),
                        TBook(), TBook(), TBook(), TBook())
                    .queryTimeout(1)
                    .fetch();
            fail();
        }
        catch (DataAccessException expected) {}
    }

    public void testInterning() throws Exception {
        jOOQAbstractTest.reset = false;

        assertEquals(1,
        create().update(TBook())
                .set(TBook_TITLE(), "1984")
                .where(TBook_ID().eq(2))
                .execute());

        Result<B> r1 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();
        Result<B> r2 = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch().intern(TBook_AUTHOR_ID(), TBook_TITLE());
        Result<B> r3 = create().selectFrom(TBook()).orderBy(TBook_ID()).intern(TBook_AUTHOR_ID(), TBook_TITLE()).fetch();

        assertEquals(r1, r2);
        assertEquals(r1, r3);

        assertEquals(r1.get(0).getValue(TBook_TITLE()), r1.get(1).getValue(TBook_TITLE()));
        assertEquals(r2.get(0).getValue(TBook_TITLE()), r2.get(1).getValue(TBook_TITLE()));
        assertEquals(r3.get(0).getValue(TBook_TITLE()), r3.get(1).getValue(TBook_TITLE()));

        /* [pro] */
        // Some JDBC drivers already perform string interning...
        if (dialect().family() == ORACLE) {
            Assert.assertNotSame(r1.get(0).getValue(TBook_TITLE()), r1.get(1).getValue(TBook_TITLE()));
        }

        /* [/pro] */
        Assert.assertSame(r2.get(0).getValue(TBook_TITLE()), r2.get(1).getValue(TBook_TITLE()));
        Assert.assertSame(r3.get(0).getValue(TBook_TITLE()), r3.get(1).getValue(TBook_TITLE()));

    }
}
