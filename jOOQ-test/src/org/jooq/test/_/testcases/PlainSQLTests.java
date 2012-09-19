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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.jooq.SQLDialect.H2;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.fieldByName;
import static org.jooq.impl.Factory.function;
import static org.jooq.impl.Factory.inline;
import static org.jooq.impl.Factory.name;
import static org.jooq.impl.Factory.param;
import static org.jooq.impl.Factory.table;
import static org.jooq.impl.Factory.tableByName;
import static org.jooq.impl.Factory.val;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.Field;
import org.jooq.FutureResult;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.Settings;
import org.jooq.impl.CustomCondition;
import org.jooq.impl.CustomField;
import org.jooq.impl.Factory;
import org.jooq.impl.SQLDataType;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class PlainSQLTests<
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

    public PlainSQLTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testQualifiedSQL() throws Exception {
        Result<Record> result =
        create().select(
                    fieldByName(Integer.class, TBook_ID().getName()),
                    fieldByName(String.class, TBook_TITLE().getName()))
                .from(tableByName(TBook().getName()))
                .orderBy(fieldByName(TBook().getName(), TBook_ID().getName()))
                .fetch();

        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(0));
        assertEquals(BOOK_TITLES, result.getValues(1));
    }

    @Test
    public void testPlainSQLExecuteWithResults() throws Exception {
        // [#1829] The Factory.execute() method must be able to handle queries
        // that return results

        assertEquals(0, create().execute(create().render(create().selectOne())));
        assertEquals(0, create().query(create().render(create().selectOne())).execute());
    }

    @Test
    public void testPlainSQL() throws Exception {
        jOOQAbstractTest.reset = false;

        // Field and Table
        // ---------------
        Field<Integer> ID = field(TBook_ID().getName(), Integer.class);
        Result<Record> result = create().select().from("t_book").orderBy(ID).fetch();

        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(ID));
        assertEquals(BOOK_TITLES, result.getValues(TBook_TITLE()));

        // [#271] Aliased plain SQL table
        result = create().select(ID).from("(select * from t_book) b").orderBy(ID).fetch();
        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(ID));

        // [#271] Aliased plain SQL table
        result = create().select().from("(select * from t_book) b").orderBy(ID).fetch();
        assertEquals(4, result.size());
        assertEquals(
            Arrays.asList(1, 2, 3, 4),
            result.getValues(ID));

        // [#836] Aliased plain SQL table
        result = create().select().from(table("t_book").as("b")).orderBy(ID).fetch();
        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(ID));

        // [#271] Check for aliased nested selects. The DescribeQuery does not seem to work
        // [#836] Aliased plain SQL nested select
        result = create().select().from(table("(select * from t_book)").as("b")).orderBy(ID).fetch();
        assertEquals(4, result.size());
        assertEquals(
            Arrays.asList(1, 2, 3, 4),
            result.getValues(ID));


        // Field, Table and Condition
        // --------------------------
        Field<?> LAST_NAME = field(TAuthor_LAST_NAME().getName());
        Field<?> COUNT1 = field("count(*) x");
        Field<?> COUNT2 = field("count(*) y", Integer.class);

        result = create()
            .select(LAST_NAME, COUNT1, COUNT2)
            .from("t_author a")
            .join("t_book b").on("a.id = b.author_id")
            .where("b.title != 'Brida'")
            .groupBy(LAST_NAME)
            .orderBy(LAST_NAME).fetch();

        assertEquals(2, result.size());
        assertEquals("Coelho", result.getValue(0, LAST_NAME));
        assertEquals("Orwell", result.getValue(1, LAST_NAME));
        assertEquals("1", result.getValueAsString(0, COUNT1));
        assertEquals("2", result.getValueAsString(1, COUNT1));
        assertEquals(Integer.valueOf(1), result.getValue(0, COUNT2));
        assertEquals(Integer.valueOf(2), result.getValue(1, COUNT2));

        // Field, Table and Condition
        // --------------------------
        result = create().select(LAST_NAME, COUNT1, COUNT2)
            .from("t_author a")
            .join("t_book b").on("a.id = b.author_id")
            .where("b.title != 'Brida'")
            .groupBy(LAST_NAME)
            .having("{count}(*) = ?", 1).fetch();

        assertEquals(1, result.size());
        assertEquals("Coelho", result.getValue(0, LAST_NAME));
        assertEquals("1", result.getValueAsString(0, COUNT1));
        assertEquals(Integer.valueOf(1), result.getValue(0, COUNT2));

        // Query
        // -----
        assertEquals(1, create()
            .query("insert into t_author (id, first_name, last_name) values (?, ?, ?)", 3, "Michèle", "Roten")
            .execute());
        A author = create().fetchOne(TAuthor(), TAuthor_ID().equal(3));
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals("Michèle", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Roten", author.getValue(TAuthor_LAST_NAME()));

        // [#724] Check for correct binding when passing
        // ---------------------------------------------
        // - (Object[]) null: API misuse
        // - (Object) null: Single null bind value
        assertEquals(1, create()
            .query("update t_author set first_name = ? where id = 3", (Object[]) null)
            .execute());
        author.refresh();
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals(null, author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Roten", author.getValue(TAuthor_LAST_NAME()));

        // Reset name
        assertEquals(1, create()
            .query("update t_author set first_name = ? where id = 3", "Michèle")
            .execute());
        author.refresh();
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals("Michèle", author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Roten", author.getValue(TAuthor_LAST_NAME()));

        // [#724] Check for correct binding when passing (Object) null
        assertEquals(1, create()
            .query("update t_author set first_name = ? where id = 3", (Object) null)
            .execute());
        author.refresh();
        assertEquals(Integer.valueOf(3), author.getValue(TAuthor_ID()));
        assertEquals(null, author.getValue(TAuthor_FIRST_NAME()));
        assertEquals("Roten", author.getValue(TAuthor_LAST_NAME()));

        // Function
        // --------
        assertEquals("ABC", create().select(function("upper", String.class, val("aBc"))).fetchOne(0));
        assertEquals("abc", create().select(function("lower", SQLDataType.VARCHAR, val("aBc"))).fetchOne(0));

        // Fetch
        // -----
        Result<Record> books = create().fetch("select * from t_book where id in (?, ?) order by id", 1, 2);
        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals(Integer.valueOf(1), books.getValue(0, TBook_ID()));
        assertEquals(Integer.valueOf(2), books.getValue(1, TBook_ID()));
        assertEquals(Integer.valueOf(1), books.getValue(0, TBook_AUTHOR_ID()));
        assertEquals(Integer.valueOf(1), books.getValue(1, TBook_AUTHOR_ID()));
    }

    @Test
    public void testPlainSQLAndComments() throws Exception {

        // Skip comments test for most dialects, as the behaviour w.r.t. comments
        // may differ
        if (getDialect() != H2) {
            log.info("SKIPPING", "Skip comments tests");
            return;
        }

        // [#1797] Plain SQL should be allowed to contain comments. Special care
        // must be taken when comments contain ' or ? characters

        // Single-line comments
        // --------------------

        // Render bind values
        Record record1 = create()
            .fetchOne("select 1 x -- what's this ?'? \n" +
            		  ", '-- no comment' y from t_book \n" +
            		  "       -- what's this ?'?\r" +
            		  "where id = ?", 1);
        assertEquals(1, record1.getValue(0));
        assertEquals("-- no comment", record1.getValue(1));

        // Inline bind values
        Record record2 = create(new Settings().withStatementType(STATIC_STATEMENT))
            .fetchOne("select 1 x -- what's this ?'? \n" +
                ", '-- no comment' y from t_book \n" +
                "       -- what's this ?'?\r" +
                "where id = ?", 1);
        assertEquals(1, record2.getValue(0));
        assertEquals("-- no comment", record2.getValue(1));

        // Multi-line comments
        // -------------------

        // Render bind values
        Record record3 = create()
            .fetchOne("select /* what's this ?'?\n\r?'? */ 1 x, '/* no comment */' y from t_book where id = ?", 1);
        assertEquals(1, record3.getValue(0));
        assertEquals("/* no comment */", record3.getValue(1));

        // Inline bind values
        Record record4 = create(new Settings().withStatementType(STATIC_STATEMENT))
            .fetchOne("select /* what's this ?'?\n\r?'? */ 1 x, '/* no comment */' y from t_book where id = ?", 1);
        assertEquals(1, record4.getValue(0));
        assertEquals("/* no comment */", record4.getValue(1));
    }

    @Test
    public void testPlainSQLCRUD() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#989] CRUD with plain SQL
        Table<Record> table = table(TAuthor().getName());
        Field<Integer> id = field("id", Integer.class);
        Field<String> firstName = field("first_name", String.class);
        Field<String> lastName = field(TAuthor_LAST_NAME().getName(), String.class);

        assertEquals(2,
        create().insertInto(table, id, firstName, lastName)
                .values(10, "Herbert", "Meier")
                .values(11, "Friedrich", "Glauser")
                .execute());

        Result<Record> authors1 = create()
                .select(id, firstName, lastName)
                .from(table)
                .where(id.in(10, 11))
                .orderBy(id)
                .fetch();

        assertEquals(2, authors1.size());
        assertEquals(10, (int) authors1.getValue(0, id));
        assertEquals(11, (int) authors1.getValue(1, id));
        assertEquals("Herbert", authors1.getValue(0, firstName));
        assertEquals("Friedrich", authors1.getValue(1, firstName));
        assertEquals("Meier", authors1.getValue(0, lastName));
        assertEquals("Glauser", authors1.getValue(1, lastName));

        assertEquals(2,
        create().update(table)
                .set(firstName, "Friedrich")
                .set(lastName, "Schiller")
                .where(id.in(10, 11))
                .execute());

        Result<Record> authors2 =
        create().select(id, firstName, lastName)
                .from(table)
                .where(id.in(10, 11))
                .orderBy(id)
                .fetch();

        assertEquals(2, authors2.size());
        assertEquals(10, (int) authors2.getValue(0, id));
        assertEquals(11, (int) authors2.getValue(1, id));
        assertEquals("Friedrich", authors2.getValue(0, firstName));
        assertEquals("Friedrich", authors2.getValue(1, firstName));
        assertEquals("Schiller", authors2.getValue(0, lastName));
        assertEquals("Schiller", authors2.getValue(1, lastName));

        assertEquals(2,
        create().delete(table)
                .where(id.in(10, 11))
                .execute());

        assertEquals(0,
        create().selectCount()
                .from(table)
                .where(id.in(10, 11))
                .fetchOne(0));
    }

    @Test
    public void testPlainSQLWithQueryParts() throws Exception {
        // Mix {keywords} with {numbered placeholders}
        String sql = "{select} {0}, a.{1} {from} {2} a {where} {3} = {4}";
        QueryPart[] parts = {
            val("a"), name(TAuthor_LAST_NAME().getName()), name(TAuthor().getName()), name(TAuthor_ID().getName()), inline(1)
        };

        Record author = create()
                .select(val("a"), TAuthor_LAST_NAME())
                .from(TAuthor())
                .where(TAuthor_ID().equal(1))
                .fetchOne();

        Record record = create().fetchOne(sql, parts);
        Result<Record> result = create().fetch(sql, parts);
        Cursor<Record> cursor = create().fetchLazy(sql, parts);

        assertEquals(author, record);
        assertEquals(author, result.get(0));
        assertEquals(author, cursor.fetchOne());
    }

    @Test
    public void testPlainSQLResultQuery() throws Exception {
        String sql = create().select(param("p", String.class).as("p")).getSQL(false);
        ResultQuery<Record> q = create().resultQuery(sql, "10");

        Result<Record> fetch1 = q.fetch();
        assertEquals(1, fetch1.size());
        assertEquals(1, fetch1.getFields().size());
        assertEquals("p", fetch1.getField(0).getName());
        assertEquals("p", fetch1.getField("p").getName());
        assertEquals("10", fetch1.getValue(0, 0));
        assertEquals("10", fetch1.getValue(0, "p"));
        assertEquals("10", fetch1.getValue(0, fetch1.getField("p")));

        List<?> fetch2 = q.fetch("p");
        assertEquals(1, fetch2.size());
        assertEquals("10", fetch2.get(0));

        List<Long> fetch3 = q.fetch(0, Long.class);
        assertEquals(1, fetch3.size());
        assertEquals(10L, (long) fetch3.get(0));

        Record fetch4 = q.fetchAny();
        assertEquals(1, fetch4.getFields().size());
        assertEquals("p", fetch4.getField(0).getName());
        assertEquals("p", fetch4.getField("p").getName());
        assertEquals("10", fetch4.getValue(0));
        assertEquals("10", fetch4.getValue("p"));
        assertEquals("10", fetch4.getValue(fetch4.getField("p")));

        Object[] fetch5 = q.fetchArray("p");
        assertEquals(1, fetch5.length);
        assertEquals("10", fetch5[0]);

        Object[] fetch6 = q.fetchArray(0);
        assertEquals(1, fetch6.length);
        assertEquals("10", fetch6[0]);

        Long[] fetch7 = q.fetchArray(0, Long.class);
        assertEquals(1, fetch7.length);
        assertEquals(10L, (long) fetch7[0]);

        List<TestPlainSQLResultQuery> fetch8 = q.fetchInto(TestPlainSQLResultQuery.class);
        assertEquals(1, fetch8.size());
        assertEquals(10, fetch8.get(0).p);

        final Integer[] count = new Integer[] { 0 };
        q.fetchInto(new RecordHandler<Record>() {
            @Override
            public void next(Record record) {
                assertEquals(1, record.getFields().size());
                assertEquals("10", record.getValue(0));
                count[0] += 1;
            }
        });

        assertEquals(1, (int) count[0]);

        FutureResult<Record> fetch9 = q.fetchLater();
        Thread.sleep(50);
        assertTrue(fetch9.isDone());
        assertEquals(1, fetch9.get().size());
        assertEquals("10", fetch9.get().getValue(0, 0));

        Cursor<Record> fetch10 = q.fetchLazy();
        assertFalse(fetch10.isClosed());
        assertTrue(fetch10.hasNext());
        assertEquals(1, fetch10.getFields().size());
        assertEquals("p", fetch10.getField(0).getName());
        assertEquals("10", fetch10.fetchOne().getValue(0));
        assertFalse(fetch10.isClosed());
        assertFalse(fetch10.hasNext());
        assertTrue(fetch10.isClosed());

        assertEquals(fetch1.get(0), q.fetchOne());
    }

    public static class TestPlainSQLResultQuery {
        public int p;
    }

    @Test
    public void testCustomSQL() throws Exception {
        final Field<Integer> IDx2 = new CustomField<Integer>(TBook_ID().getName(), TBook_ID().getDataType()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void toSQL(RenderContext context) {
                context.setData("Foo-Field", "Baz");

                if (context.inline()) {
                    context.sql(TBook_ID().getName() + " * 2");
                }
                else {
                    context.sql(TBook_ID().getName() + " * ?");
                }
            }

            @Override
            public void bind(BindContext context) {
                try {
                    context.statement().setInt(context.nextIndex(), 2);
                }
                catch (SQLException e) {
                    throw translate(getSQL(), e);
                }
            }
        };

        Condition c = new CustomCondition() {
            private static final long serialVersionUID = -629253722638033620L;

            @Override
            public void toSQL(RenderContext context) {
                context.setData("Foo-Condition", "Baz");

                context.sql(IDx2);
                context.sql(" > ");

                if (context.inline()) {
                    context.sql("3");
                }
                else {
                    context.sql("?");
                }
            }

            @Override
            public void bind(BindContext context) {
                try {
                    context.bind(IDx2);
                    context.statement().setInt(context.nextIndex(), 3);
                }
                catch (SQLException e) {
                    throw translate(getSQL(), e);
                }
            }
        };

        // [#1169] Some additional checks to see if custom data is correctly
        // passed on to custom QueryParts
        Factory create = create();
        create.setData("Foo-Field", "Bar");
        create.setData("Foo-Condition", "Bar");

        Result<Record> result = create
            .select(TBook_ID(), IDx2)
            .from(TBook())
            .where(c)
            .orderBy(IDx2)
            .fetch();

        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
        assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
        assertEquals(Integer.valueOf(4), result.getValue(2, TBook_ID()));

        assertEquals(Integer.valueOf(4), result.getValue(0, IDx2));
        assertEquals(Integer.valueOf(6), result.getValue(1, IDx2));
        assertEquals(Integer.valueOf(8), result.getValue(2, IDx2));

        // [#1169] Check again
        assertEquals("Baz", create.getData("Foo-Field"));
        assertEquals("Baz", create.getData("Foo-Condition"));
    }
}
