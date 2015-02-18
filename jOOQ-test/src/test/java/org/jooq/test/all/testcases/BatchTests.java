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
import static java.util.Collections.nCopies;
import static java.util.Collections.singletonMap;
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HANA;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.impl.DSL.delete;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.insertInto;
import static org.jooq.impl.DSL.mergeInto;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.update;
import static org.jooq.lambda.tuple.Tuple.tuple;
import static org.jooq.tools.reflect.Reflect.on;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import org.jooq.Batch;
import org.jooq.BatchBindStep;
import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.lambda.Seq;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

public class BatchTests<
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

    public BatchTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
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

    public void testBatchSingle() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#1749] TODO Firebird renders CAST(? as VARCHAR(...)) bind values with sizes
        // pre-calculated. Hence the param needs to have some min length...
        Batch batch1 = create().batch(create().insertInto(TAuthor())
                                              .set(TAuthor_ID(), 8)
                                              .set(TAuthor_LAST_NAME(), "           "))
                               .bind(8, "Gamma")
                               .bind(9, "Helm")
                               .bind(10, "Johnson");
        assertEquals(3, batch1.size());
        int[] result1 = batch1.execute();
        assertEquals(3, result1.length);
        testBatchAuthors("Gamma", "Helm", "Johnson");


        Batch batch2 = create().batch(delete(TAuthor()).where(TAuthor_ID().eq((Integer) null)))
                               .bind(8)
                               .bind(9)
                               .bind(10);
        assertEquals(3, batch2.size());
        int[] result2 = batch2.execute();
        assertEquals(3, result2.length);
        assertEquals(2, create().fetchCount(selectOne().from(TAuthor())));


        Batch batch3 = create().batch(insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                                      .values((Integer) null, "           "))
                               .bind(8, "Gamma")
                               .bind(9, "Helm")
                               .bind(10, "Johnson");
        assertEquals(3, batch3.size());
        int[] result3 = batch3.execute();
        assertEquals(3, result3.length);
        testBatchAuthors("Gamma", "Helm", "Johnson");
    }

    public void testBatchSingleNamedParameters() throws Exception {
        jOOQAbstractTest.reset = false;

        // Declare a :first_name bind value, but don't bind any variables to it
        Batch batch1 = create().batch(create().insertInto(TAuthor())

                                              // [#3940] Add some inline values just to be sure
                                              .set(TAuthor_YEAR_OF_BIRTH(), inline(2000))
                                              .set(TAuthor_ID(), param("id", Integer.class))
                                              .set(TAuthor_FIRST_NAME(), param("first_name", String.class))
                                              .set(TAuthor_LAST_NAME(), param("last_name", String.class)))
                               .bind(Seq.toMap(Seq.of(tuple("id", (Object)  8), tuple("last_name", "Gamma"  ))))
                               .bind(Seq.toMap(Seq.of(tuple("id", (Object)  9), tuple("last_name", "Helm"   ))))
                               .bind(Seq.toMap(Seq.of(tuple("id", (Object) 10), tuple("last_name", "Johnson"))))
                               ;
        assertEquals(3, batch1.size());
        int[] result1 = batch1.execute();
        assertEquals(3, result1.length);
        testBatchAuthors("Gamma", "Helm", "Johnson");


        Batch batch2 = create().batch(delete(TAuthor()).where(TAuthor_ID().eq(param("id", Integer.class))))
                               .bind(singletonMap("id", 8))
                               .bind(singletonMap("id", 9))
                               .bind(singletonMap("id", 10));
        assertEquals(3, batch2.size());
        int[] result2 = batch2.execute();
        assertEquals(3, result2.length);
        assertEquals(2, create().fetchCount(selectOne().from(TAuthor())));

        Batch batch3 = create().batch(insertInto(TAuthor(),
                                            TAuthor_ID(),
                                            TAuthor_FIRST_NAME(),
                                            TAuthor_LAST_NAME(),
                                            TAuthor_YEAR_OF_BIRTH())
                                      .values(
                                            param("id", Integer.class),
                                            param("first_name", String.class),
                                            param("last_name", String.class),
                                            inline(2000)))
                               .bind(Seq.toMap(Seq.of(tuple("id", (Object)  8), tuple("first_name", "A"), tuple("last_name", "Gamma"  ))))
                               .bind(Seq.toMap(Seq.of(tuple("id", (Object)  9), tuple("first_name", "B"), tuple("last_name", "Helm"   ))))
                               .bind(Seq.toMap(Seq.of(tuple("id", (Object) 10), tuple("first_name", "C"), tuple("last_name", "Johnson"))));
        assertEquals(3, batch3.size());
        int[] result3 = batch3.execute();
        assertEquals(3, result3.length);
        assertEquals(asList("A", "B", "C"), create().fetchValues(
            select(TAuthor_FIRST_NAME())
            .from(TAuthor())
            .where(TAuthor_ID().in(8, 9, 10))
            .orderBy(TAuthor_FIRST_NAME())));
        assertEquals(nCopies(3, 2000), create().fetchValues(
            select(TAuthor_YEAR_OF_BIRTH())
            .from(TAuthor())
            .where(TAuthor_ID().in(8, 9, 10))
            .orderBy(TAuthor_FIRST_NAME())));
        testBatchAuthors("Gamma", "Helm", "Johnson");
    }

    public void testBatchSingleMerge() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, DERBY, FIREBIRD, H2, HANA, INGRES, MARIADB, MYSQL, POSTGRES, SQLITE);
        jOOQAbstractTest.reset = false;

        BatchBindStep batch = create().batch(
            mergeInto(TBook())
            .usingDual()
            .on(TBook_ID().eq((Integer) null))
            .whenMatchedThenUpdate()
            .set(TBook_TITLE(), (String) null)
        );

        for (int i = 1; i < 5; i++)
            batch.bind(i, "abc");

        assertEquals(4, batch.execute().length);
        assertEquals(nCopies(4, "abc"), create().select(TBook_TITLE()).from(TBook()).fetch(TBook_TITLE()));
    }

    public void testBatchSingleWithInlineVariables() throws Exception {
        jOOQAbstractTest.reset = false;

        BatchBindStep batch = create().batch(
            update(TBook())
            .set(TBook_TITLE(), inline("abc"))
            .where(TBook_ID().eq((Integer) null))
        );

        for (int i = 1; i < 5; i++)
            batch.bind(i);

        assertEquals(4, batch.execute().length);
        assertEquals(nCopies(4, "abc"), create().select(TBook_TITLE()).from(TBook()).fetch(TBook_TITLE()));
    }

    public void testBatchSinglePlainSQL() throws Exception {
        jOOQAbstractTest.reset = false;

        String author = TAuthor().getName();
        String id = TAuthor_ID().getName();
        String last_name = TAuthor_LAST_NAME().getName();

        int[] r1 =
        create().batch(String.format("insert into %s (%s, %s) values (?, ?)", author, id, last_name))
                .bind(3, "X")
                .bind(4, "Y")
                .execute();

        assertEquals(2, r1.length);

        Result<A> authors = create().fetch(TAuthor()).sortAsc(TAuthor_ID());
        assertEquals(4, authors.size());
        assertEquals(3, (int) authors.get(2).getValue(TAuthor_ID()));
        assertEquals(4, (int) authors.get(3).getValue(TAuthor_ID()));
        assertEquals("X", authors.get(2).getValue(TAuthor_LAST_NAME()));
        assertEquals("Y", authors.get(3).getValue(TAuthor_LAST_NAME()));
    }

    public void testBatchSingleWithNulls() throws Exception {
        Batch batch = create().batch(insertInto(TDates(), TDates_ID(), TDates_D(), TDates_T(), TDates_TS())
                                     .values(1, null, null, null))
                              .bind(1, null, null, null)
                              .bind(2, null, null, null)
                              .bind(3, null, null, null);

        assertEquals(3, batch.size());
        int[] result = batch.execute();
        assertEquals(3, result.length);

        Result<DATE> dates = create().fetch(TDates());
        assertEquals(3, dates.size());
        assertEquals(asList(1, 2, 3), dates.getValues(TDates_ID()));
        assertEquals(nCopies(3, null), dates.getValues(TDates_D()));
        assertEquals(nCopies(3, null), dates.getValues(TDates_T()));
        assertEquals(nCopies(3, null), dates.getValues(TDates_TS()));

    }

    public void testBatchMultiple() throws Exception {
        jOOQAbstractTest.reset = false;

        Batch batch = create().batch(
            create().insertInto(TAuthor())
                    .set(TAuthor_ID(), 8)
                    .set(TAuthor_LAST_NAME(), "Gamma"),

            create().insertInto(TAuthor())
                    .set(TAuthor_ID(), 9)
                    .set(TAuthor_LAST_NAME(), "Helm"),

            create().insertInto(TBook())
                    .set(TBook_ID(), 6)
                    .set(TBook_AUTHOR_ID(), 8)
                    .set(TBook_PUBLISHED_IN(), 1994)
                    .set(TBook_LANGUAGE_ID(), 1)
                    .set(TBook_CONTENT_TEXT(), "Design Patterns are awesome")
                    .set(TBook_TITLE(), "Design Patterns"),

            create().insertInto(TAuthor())
                    .set(TAuthor_ID(), 10)
                    .set(TAuthor_LAST_NAME(), "Johnson"));

        assertEquals(4, batch.size());

        int[] result = batch.execute();
        assertEquals(4, result.length);
        assertEquals(5, create().fetch(TBook()).size());
        assertEquals(1, create().fetch(TBook(), TBook_AUTHOR_ID().equal(8)).size());
        testBatchAuthors("Gamma", "Helm", "Johnson");
    }

    public void testBatchStore() throws Exception {
        jOOQAbstractTest.reset = false;

        // First, INSERT two authors and one book
        // --------------------------------------
        A a1 = create().newRecord(TAuthor());
        a1.setValue(TAuthor_ID(), 8);
        a1.setValue(TAuthor_LAST_NAME(), "XX");

        A a2 = create().newRecord(TAuthor());
        a2.setValue(TAuthor_ID(), 9);
        a2.setValue(TAuthor_LAST_NAME(), "YY");

        B b1 = create().newRecord(TBook());
        b1.setValue(TBook_ID(), 80);
        b1.setValue(TBook_AUTHOR_ID(), 8);
        b1.setValue(TBook_TITLE(), "XX 1");
        b1.setValue(TBook_PUBLISHED_IN(), 2000);
        b1.setValue(TBook_LANGUAGE_ID(), 1);

        Batch batch = create().batchStore(a1, b1, a2);
        assertEquals(3, batch.size());

        int[] result1 = batch.execute();
        assertEquals(3, result1.length);
        testBatchAuthors("XX", "YY");
        assertEquals("XX 1", create()
            .select(TBook_TITLE())
            .from(TBook())
            .where(TBook_ID().equal(80))
            .fetchOne(0));

        // Then, update one author and insert another one
        // ----------------------------------------------
        a2.setValue(TAuthor_LAST_NAME(), "ABC");

        A a3 = create().newRecord(TAuthor());
        a3.setValue(TAuthor_ID(), 10);
        a3.setValue(TAuthor_LAST_NAME(), "ZZ");

        int[] result2 = create().batchStore(b1, a1, a2, a3).execute();
        assertEquals(2, result2.length);
        testBatchAuthors("XX", "ABC", "ZZ");
        assertEquals("XX 1", create()
            .select(TBook_TITLE())
            .from(TBook())
            .where(TBook_ID().equal(80))
            .fetchOne(0));
    }

    public void testBatchInsertUpdate() throws Exception {
        jOOQAbstractTest.reset = false;

        // First, INSERT two authors and one book
        // --------------------------------------
        A a1 = create().newRecord(TAuthor());
        a1.setValue(TAuthor_ID(), 8);
        a1.setValue(TAuthor_LAST_NAME(), "XX");

        A a2 = create().newRecord(TAuthor());
        a2.setValue(TAuthor_ID(), 9);
        a2.setValue(TAuthor_LAST_NAME(), "YY");

        B b1 = create().newRecord(TBook());
        b1.setValue(TBook_ID(), 80);
        b1.setValue(TBook_AUTHOR_ID(), 8);
        b1.setValue(TBook_TITLE(), "XX 1");
        b1.setValue(TBook_PUBLISHED_IN(), 2000);
        b1.setValue(TBook_LANGUAGE_ID(), 1);

        Batch batch1 = create().batchInsert(a1, b1, a2);
        assertEquals(3, batch1.size());

        int[] result1 = batch1.execute();
        assertEquals(3, result1.length);
        assertCountAuthors(4);
        assertCountBooks(5);

        testBatchAuthors("XX", "YY");
        assertEquals("XX 1", create()
            .select(TBook_TITLE())
            .from(TBook())
            .where(TBook_ID().equal(80))
            .fetchOne(0));

        // Then, update one author
        // -----------------------
        a2.setValue(TAuthor_LAST_NAME(), "ABC");

        try {
            create().batchInsert(a2).execute();
            fail();
        }
        catch (DataAccessException expected) {}

        int[] result3 = create().batchUpdate(b1, a1, a2).execute();
        assertEquals(1, result3.length);
        testBatchAuthors("XX", "ABC");
        assertEquals("XX 1", create()
            .select(TBook_TITLE())
            .from(TBook())
            .where(TBook_ID().equal(80))
            .fetchOne(0));
    }

    @SuppressWarnings("unchecked")

    public void testBatchStoreWithUDTs() throws Exception {
        if (cUAddressType() == null) {
            log.info("SKIPPING", "Skipping batch store with UDT tests");
            return;
        }

        jOOQAbstractTest.reset = false;

        // [#2139] Check for correct binding of UDT values in batch operations
        UDTRecord<?> addr1 = cUAddressType().newInstance();
        UDTRecord<?> addr2 = cUAddressType().newInstance();

        on(addr1).call("setCity", "City X");
        on(addr2).call("setCity", "City Y");

        A a1 = create().newRecord(TAuthor());
        A a2 = create().newRecord(TAuthor());

        a1.setValue(TAuthor_ID(), 3);
        a2.setValue(TAuthor_ID(), 4);

        a1.setValue(TAuthor_LAST_NAME(), "X");
        a2.setValue(TAuthor_LAST_NAME(), "Y");

        a1.setValue((Field<UDTRecord<?>>) TAuthor_ADDRESS(), addr1);
        a2.setValue((Field<UDTRecord<?>>) TAuthor_ADDRESS(), addr2);

        Batch batch = create().batchStore(a1, a2);
        assertEquals(2, batch.size());

        int[] result = batch.execute();
        assertEquals(2, result.length);

        Result<A> authors = create()
            .selectFrom(TAuthor())
            .where(TAuthor_ID().in(3, 4))
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(2, authors.size());
        assertEquals(asList(3, 4), authors.getValues(TAuthor_ID()));
        assertEquals(asList("X", "Y"), authors.getValues(TAuthor_LAST_NAME()));
        assertEquals(asList(addr1, addr2), authors.getValues(TAuthor_ADDRESS()));
        assertEquals("City X", on(authors.get(0).getValue(TAuthor_ADDRESS())).call("getCity").get());
        assertEquals("City Y", on(authors.get(1).getValue(TAuthor_ADDRESS())).call("getCity").get());
    }

    public void testBatchDelete() throws Exception {
        jOOQAbstractTest.reset = false;

        Result<B2S> books = create().selectFrom(TBookToBookStore()).where(TBookToBookStore_BOOK_ID().in(1, 3, 4)).fetch();
        Batch batch = create().batchDelete(books);
        assertEquals(5, batch.size());

        int[] result = batch.execute();
        assertEquals(5, result.length);
        assertEquals(1, create().selectFrom(TBookToBookStore()).fetch().size());
    }

    public void testBatchDeleteWithExecuteListener() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#3427] Internally, jOOQ uses org.jooq.exception.ControlFlowSignal to abort rendering of bind values
        // This "exception" must not escape to client ExecuteListeners
        NoControlFlowSignals listener = new NoControlFlowSignals();

        Result<B2S> books = create().selectFrom(TBookToBookStore()).where(TBookToBookStore_BOOK_ID().in(1, 3, 4)).fetch();
        Batch batch = create(listener).batchDelete(books);
        assertEquals(5, batch.size());

        int[] result = batch.execute();
        assertEquals(5, result.length);
        assertEquals(1, create().selectFrom(TBookToBookStore()).fetch().size());
        assertNull(listener.e1);
        assertNull(listener.e2);
    }

    @SuppressWarnings("serial")
    static class NoControlFlowSignals extends DefaultExecuteListener {

        RuntimeException e1;
        SQLException e2;

        @Override
        public void exception(ExecuteContext ctx) {
            super.exception(ctx);

            e1 = ctx.exception();
            e2 = ctx.sqlException();
        }
    }

    private void testBatchAuthors(String... names) throws Exception {
        assertEquals(names.length == 3 ? 5 : 4, create().fetch(TAuthor()).size());

        assertEquals(
             names.length == 3
                 ? Arrays.asList(8, 9, 10)
                 : Arrays.asList(8, 9),
             create().select(TAuthor_ID())
                     .from(TAuthor())
                     .where(TAuthor_ID().in(8, 9, 10))
                     .orderBy(TAuthor_ID())
                     .fetch(TAuthor_ID()));

        assertEquals(Arrays.asList(names),
            create().select(TAuthor_LAST_NAME())
                    .from(TAuthor())
                    .where(TAuthor_ID().in(8, 9, 10))
                    .orderBy(TAuthor_ID())
                    .fetch(TAuthor_LAST_NAME()));
    }
}
