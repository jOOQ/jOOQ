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
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
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
import org.jooq.impl.SQLDataType;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

public class PlainSQLTests<
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

    public PlainSQLTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testQualifiedSQL() throws Exception {
        Result<Record2<Integer, String>> result =
        create().select(
                    field(name(TBook_ID().getName()), Integer.class),
                    field(name(TBook_TITLE().getName()), String.class))
                .from(table(TBook().getName()))
                .orderBy(field(name(TBook().getName(), TBook_ID().getName())))
                .fetch();

        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(0));
        assertEquals(BOOK_TITLES, result.getValues(1));
    }

    public void testPlainSQLExecuteWithResults() throws Exception {
        // [#1829] The Factory.execute() method must be able to handle queries
        // that return results

        assertEquals(0, create().execute(create().render(create().selectOne())));
        assertEquals(0, create().query(create().render(create().selectOne())).execute());
    }

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
        Result<Record1<Integer>> result2 = create().select(ID).from("(select * from t_book) b").orderBy(ID).fetch();
        assertEquals(4, result2.size());
        assertEquals(BOOK_IDS, result2.getValues(ID));

        // [#271] Aliased plain SQL table
        Result<Record> result3 = create().select().from("(select * from t_book) b").orderBy(ID).fetch();
        assertEquals(4, result3.size());
        assertEquals(
            Arrays.asList(1, 2, 3, 4),
            result3.getValues(ID));

        // [#836] Aliased plain SQL table
        Result<Record> result4 = create().select().from(table("t_book").as("b")).orderBy(ID).fetch();
        assertEquals(4, result4.size());
        assertEquals(BOOK_IDS, result4.getValues(ID));

        // [#271] Check for aliased nested selects. The DescribeQuery does not seem to work
        // [#836] Aliased plain SQL nested select
        Result<Record> result5 = create().select().from(table("(select * from t_book)").as("b")).orderBy(ID).fetch();
        assertEquals(4, result5.size());
        assertEquals(
            Arrays.asList(1, 2, 3, 4),
            result5.getValues(ID));


        // Field, Table and Condition
        // --------------------------
        Field<?> LAST_NAME = field(TAuthor_LAST_NAME().getName());
        Field<?> COUNT1 = field("count(*)").as("x");
        Field<?> COUNT2 = field("count(*)", Integer.class).as("y");

        Result<?> result6 = create()
            .select(LAST_NAME, COUNT1, COUNT2)
            .from("t_author a")
            .join("t_book b").on("a.id = b.author_id")
            .where("b.title <> 'Brida'")
            .groupBy(LAST_NAME)
            .orderBy(LAST_NAME).fetch();

        assertEquals(2, result6.size());
        assertEquals("Coelho", result6.getValue(0, LAST_NAME));
        assertEquals("Orwell", result6.getValue(1, LAST_NAME));
        assertEquals("1", result6.get(0).getValue(COUNT1, String.class));
        assertEquals("2", result6.get(1).getValue(COUNT1, String.class));
        assertEquals(Integer.valueOf(1), result6.getValue(0, COUNT2));
        assertEquals(Integer.valueOf(2), result6.getValue(1, COUNT2));

        // Field, Table and Condition
        // --------------------------
        Result<?> result7 = create().select(LAST_NAME, COUNT1, COUNT2)
            .from("t_author a")
            .join("t_book b").on("a.id = b.author_id")
            .where("b.title <> 'Brida'")
            .groupBy(LAST_NAME)
            .having("{count}(*) = ?", 1).fetch();

        assertEquals(1, result7.size());
        assertEquals("Coelho", result7.getValue(0, LAST_NAME));
        assertEquals("1", result7.get(0).getValue(COUNT1, String.class));
        assertEquals(Integer.valueOf(1), result7.getValue(0, COUNT2));

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
        assertEquals("4", create().select(function("max", String.class, field("id"))).from("t_book").fetchOne(0));
        assertEquals("1", create().select(function("min", SQLDataType.VARCHAR, field("id"))).from("t_book").fetchOne(0));

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

    public void testPlainSQLInsert() throws Exception {
        jOOQAbstractTest.reset = false;

        assertEquals(1, create()
            .insertInto(table(name(TAuthor().getName())))
            .columns(
                field(name(TAuthor().getName(), TAuthor_ID().getName())),
                field(name(TAuthor().getName(), TAuthor_LAST_NAME().getName())))
            .values(3, "X")
            .execute()
        );

        assertEquals(3, create().fetchCount(TAuthor()));
    }

    public void testPlainSQLAmbiguousColumnNames() throws Exception {

        // Not all JDBC drivers can access the schema / table names from such statements
        assumeFamilyNotIn(ORACLE, SQLSERVER);

        String schema = TBook().getSchema().getName();
        String book = TBook().getName();
        String bookID = TBook_ID().getName();
        String author = TAuthor().getName();
        String authorID = TAuthor_ID().getName();

        Result<Record> result =
        create().fetch("select b.{0}, a.{1} from t_author a join t_book b on a.id = b.author_id order by b.{0}, a.{1}",
            name(bookID),
            name(authorID));

        assertEquals(BOOK_IDS, result.getValues(0, int.class));
        assertEquals(BOOK_AUTHOR_IDS, result.getValues(1, int.class));

        assertEquals(BOOK_IDS, result.getValues(field(name(book, bookID), int.class)));
        assertEquals(BOOK_IDS, result.getValues(field(name(schema, book, bookID), int.class)));
        assertEquals(BOOK_AUTHOR_IDS, result.getValues(field(name(author, authorID), int.class)));
        assertEquals(BOOK_AUTHOR_IDS, result.getValues(field(name(schema, author, authorID), int.class)));

        assertEquals(BOOK_IDS, result.getValues(result.field(name(book, bookID), int.class)));
        assertEquals(BOOK_IDS, result.getValues(result.field(name(schema, book, bookID), int.class)));
        assertEquals(BOOK_AUTHOR_IDS, result.getValues(result.field(name(author, authorID), int.class)));
        assertEquals(BOOK_AUTHOR_IDS, result.getValues(result.field(name(schema, author, authorID), int.class)));
    }

    public void testPlainSQLWithSelfJoins()  throws Exception {

        // [#1860] In case of ambiguous field names in plain SQL, access by
        // index should still be possible
        Result<Record> result1 =
        create().fetch("select * from (select id from t_author) a1, (select id from t_author) a2 order by a1.id, a2.id");

        assertEquals(asList(1, 1, 2, 2), result1.getValues(0, int.class));
        assertEquals(asList(1, 2, 1, 2), result1.getValues(1, int.class));

        Result<Record> result2 =
        create().fetch("select * from (select id from t_author) a1 left outer join (select id from t_author where 1 = 0) a2 on a1.id = a2.id order by a1.id");

        assertEquals(asList(1, 2), result2.getValues(0, Integer.class));
        assertEquals(asList(null, null), result2.getValues(1, Integer.class));
    }

    public void testPlainSQLAndComments() throws Exception {

        // Skip comments test for most dialects, as the behaviour w.r.t. comments
        // may differ
        if (dialect() != H2) {
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

        Result<Record3<Integer, String, String>> authors1 = create()
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

        Result<Record3<Integer, String, String>> authors2 =
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

        cursor.close();
    }

    public void testPlainSQLFetchValue() throws Exception {
        assertEquals("1", "" + create().fetchValue("select 1 from " + TBook().getName() + " where " + TBook_ID().getName() + " = 1"));
        assertEquals("1", "" + create().fetchValue("select 1 from " + TBook().getName() + " where " + TBook_ID().getName() + " = ?", 1));
        assertEquals("1", "" + create().fetchValue("select {0} from {1} where {2} = {3}", inline(1), TBook(), TBook_ID(), val(1)));
    }

    public void testPlainSQLResultQuery() throws Exception {
        // [#1749] TODO Firebird renders CAST(? as VARCHAR(...)) bind values with sizes
        // pre-calculated. Hence the param needs to have some min length...
        String sql = create().select(param("p", "abc").as("p")).getSQL(INDEXED);
        ResultQuery<Record> q = create().resultQuery(sql, "10");

        Result<Record> fetch1 = q.fetch();
        assertEquals(1, fetch1.size());
        assertEquals(1, fetch1.fieldsRow().size());
        assertEquals("p", fetch1.field(0).getName());
        assertEquals("p", fetch1.field("p").getName());
        assertEquals("10", fetch1.getValue(0, 0));
        assertEquals("10", fetch1.getValue(0, "p"));
        assertEquals("10", fetch1.getValue(0, fetch1.field("p")));

        List<?> fetch2 = q.fetch("p");
        assertEquals(1, fetch2.size());
        assertEquals("10", fetch2.get(0));

        List<Long> fetch3 = q.fetch(0, Long.class);
        assertEquals(1, fetch3.size());
        assertEquals(10L, (long) fetch3.get(0));

        Record fetch4 = q.fetchAny();
        assertEquals(1, fetch4.fieldsRow().size());
        assertEquals("p", fetch4.field(0).getName());
        assertEquals("p", fetch4.field("p").getName());
        assertEquals("10", fetch4.getValue(0));
        assertEquals("10", fetch4.getValue("p"));
        assertEquals("10", fetch4.getValue(fetch4.field("p")));

        // [#1722] Check the actual returned type of arrays, also
        Object[] fetch5 = q.fetchArray("p");
        assertEquals(1, fetch5.length);
        assertEquals("10", fetch5[0]);
        assertTrue(fetch5 instanceof String[]);

        Object[] fetch6 = q.fetchArray(0);
        assertEquals(1, fetch6.length);
        assertEquals("10", fetch6[0]);
        assertTrue(fetch6 instanceof String[]);

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
                assertEquals(1, record.fieldsRow().size());
                assertEquals("10", record.getValue(0));
                count[0] += 1;
            }
        });

        assertEquals(1, (int) count[0]);

        Cursor<Record> fetch10 = q.fetchLazy();
        assertFalse(fetch10.isClosed());
        assertTrue(fetch10.hasNext());
        assertEquals(1, fetch10.fieldsRow().size());
        assertEquals("p", fetch10.field(0).getName());
        assertEquals("10", fetch10.fetchOne().getValue(0));
        assertFalse(fetch10.isClosed());
        assertFalse(fetch10.hasNext());
        assertTrue(fetch10.isClosed());

        assertEquals(fetch1.get(0), q.fetchOne());
    }

    public static class TestPlainSQLResultQuery {
        public int p;
    }

    public void testCustomSQL() throws Exception {
        final Field<Integer> IDx2 = new CustomField<Integer>(TBook_ID().getName(), TBook_ID().getDataType()) {
            private static final long serialVersionUID = 1L;

            @Override
            public void toSQL(RenderContext context) {
                context.configuration().data("Foo-Field", "Baz");

                if (context.paramType() == INLINED) {
                    context.sql(TBook_ID().getName() + " * 2");
                }

                // Firebird is the only dialect that cannot handle type inferral
                // When multiplying an INT by a bind value
                else if (context.configuration().dialect() == FIREBIRD) {
                    context.sql(TBook_ID().getName() + " * cast (? as int)");
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
                    throw translate(null, e);
                }
            }
        };

        Condition c = new CustomCondition() {
            private static final long serialVersionUID = -629253722638033620L;

            @Override
            public void toSQL(RenderContext context) {
                context.configuration().data("Foo-Condition", "Baz");

                context.visit(IDx2);
                context.sql(" > ");

                if (context.paramType() == INLINED) {
                    context.sql("3");
                }
                else {
                    context.sql("?");
                }
            }

            @Override
            public void bind(BindContext context) {
                try {
                    context.visit(IDx2);
                    context.statement().setInt(context.nextIndex(), 3);
                }
                catch (SQLException e) {
                    throw translate(null, e);
                }
            }
        };

        // [#1169] Some additional checks to see if custom data is correctly
        // passed on to custom QueryParts
        DSLContext create = create();
        create.configuration().data("Foo-Field", "Bar");
        create.configuration().data("Foo-Condition", "Bar");

        Result<Record2<Integer, Integer>> result = create
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
        assertEquals("Baz", create.configuration().data("Foo-Field"));
        assertEquals("Baz", create.configuration().data("Foo-Condition"));
    }

    public void testPlainSQLBlobAndClob() throws Exception {
        assumeNotNull(TBook_CONTENT_PDF());

        jOOQAbstractTest.reset = false;

        // [#2534] BLOB and CLOB should be auto-converted to byte[] and Spring
        // [#3923] äöü generate "negative" bytes, which might be serialised badly in PostgreSQL
        for (DSLContext create : new DSLContext[] { create(), create(new Settings().withStatementType(STATIC_STATEMENT)) }) {
            create.update(TBook())
                  .set(TBook_CONTENT_TEXT(), "text")
                  .set(TBook_CONTENT_PDF(), "pdf äöü".getBytes("UTF-8"))
                  .where(TBook_ID().eq(1))
                  .execute();

            Record r1 = create.fetchOne("select content_text, content_pdf from t_book where id = 1");
            assertEquals("text", r1.getValue(0));
            assertEquals("pdf äöü", new String((byte[]) r1.getValue(1), "UTF-8"));

            Record r2 = create.select(
                                  field("content_text", String.class),
                                  field("content_pdf", byte[].class))
                              .from("t_book")
                              .where("id = 1")
                              .fetchOne();
            assertEquals("text", r2.getValue(0));
            assertEquals("pdf äöü", new String((byte[]) r2.getValue(1), "UTF-8"));

            Record r3 = create.select(field("content_text"), field("content_pdf"))
                              .from("t_book")
                              .where("id = 1")
                              .fetchOne();
            assertEquals("text", r3.getValue(0));
            assertEquals("pdf äöü", new String((byte[]) r3.getValue(1), "UTF-8"));
        }
    }

    public void testPlainSQLLimitOffset() throws Exception {
        int titleIndex;

        Result<Record> r1 =
        create().select()
                .from("t_book")
                .orderBy(field("id"))
                .limit(3)
                .fetch();

        titleIndex = titleIndex(r1);

        assertEquals(3, r1.size());
        assertEquals(asList(1, 2, 3), r1.getValues(0, int.class));
        assertEquals(BOOK_TITLES.subList(0, 3), r1.getValues(titleIndex));

        Result<Record> r2 =
        create().select()
                .from("t_book")
                .orderBy(field("id"))
                .limit(3)
                .offset(1)
                .fetch();

        titleIndex = titleIndex(r2);

        assertEquals(3, r2.size());
        assertEquals(asList(2, 3, 4), r2.getValues(0, int.class));
        assertEquals(BOOK_TITLES.subList(1, 4), r2.getValues(titleIndex));
    }

    private int titleIndex(Result<Record> r1) {
        Field<?>[] fields = r1.fieldsRow().fields();

        for (int i = 0; i < fields.length; i++)
            if (fields[i].getName().equalsIgnoreCase("title"))
                return i;

        throw new AssertionError();
    }

    public void testPlainSQLAndJDBCEscapeSyntax() throws Exception {
        assumeFamilyNotIn(SQLITE);

        Record r1 = create()
            .select(
                field("{d '2014-01-01'}", Date.class).as("a"),
                field("{t '19:00:00'}", Time.class).as("b"),
                field("{ts '2014-01-01 19:00:00'}", Timestamp.class).as("c")
            )
            .fetchOne();

        assertEquals(Date.valueOf("2014-01-01"), r1.getValue(0));
        assertEquals(Time.valueOf("19:00:00"), r1.getValue(1));
        assertEquals(Timestamp.valueOf("2014-01-01 19:00:00"), r1.getValue(2));

        // [#3430] Don't let newline characters break the parsing of JDBC escape syntax:
        Record r2 = create()
            .select(one().as("one"))
            .where("{d '2014-01-01'}           < {d '2014-01-02'}\n"
             + "and {t '00:00:00'}             < {t '01:00:00'}\n"
             + "and {ts '2014-01-01 00:00:00'} < {ts '2014-01-02 00:00:00'}")
            .fetchOne();

        assertEquals(1, r2.getValue(0));
    }
}
