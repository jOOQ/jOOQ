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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.falseCondition;
import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.lower;
import static org.jooq.impl.Factory.one;
import static org.jooq.impl.Factory.val;
import static org.jooq.impl.Factory.zero;

import java.util.List;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.SimpleSelectQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class JoinTests<
    A    extends UpdatableRecord<A>,
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
extends BaseTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> {

    public JoinTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testJoinQuery() throws Exception {
        SimpleSelectQuery<L> q1 = create().selectQuery(VLibrary());

        // TODO: Fix this when funny issue is fixed in Derby:
        // https://sourceforge.net/apps/trac/jooq/ticket/238
        q1.addOrderBy(VLibrary_TITLE());

        // Oracle ordering behaviour is a bit different, so exclude "1984"
        q1.addConditions(VLibrary_TITLE().notEqual("1984"));

        Table<A> a = TAuthor().as("a");
        Table<B> b = TBook().as("b");

        Field<Integer> a_authorID = a.getField(TAuthor_ID());
        Field<Integer> b_authorID = b.getField(TBook_AUTHOR_ID());
        Field<String> b_title = b.getField(TBook_TITLE());

        SelectQuery q2 = create().selectQuery();
        q2.addFrom(a);
        q2.addJoin(b, b_authorID.equal(a_authorID));
        q2.addConditions(b_title.notEqual("1984"));
        q2.addOrderBy(lower(b_title));

        int rows1 = q1.execute();
        int rows2 = q2.execute();

        assertEquals(3, rows1);
        assertEquals(3, rows2);

        Result<L> result1 = q1.getResult();
        Result<?> result2 = q2.getResult();

        assertEquals("Animal Farm", result1.get(0).getValue(VLibrary_TITLE()));
        assertEquals("Animal Farm", result2.get(0).getValue(b_title));

        assertEquals("Brida", result1.get(1).getValue(VLibrary_TITLE()));
        assertEquals("Brida", result2.get(1).getValue(b_title));

        assertEquals("O Alquimista", result1.get(2).getValue(VLibrary_TITLE()));
        assertEquals("O Alquimista", result2.get(2).getValue(b_title));

        // DB2 does not allow subselects in join conditions:
        // http://publib.boulder.ibm.com/infocenter/dzichelp/v2r2/index.jsp?topic=/com.ibm.db29.doc.sqlref/db2z_sql_joincondition.htm

        // This query causes a failure in Ingres. Potentially a bug. See E_OP039F_BOOLFACT on
        // http://docs.ingres.com/ingres/9.2/ingres-92-message-guide/1283-errors-from-opf#E_OP039F_BOOLFACT
        if (!asList(CUBRID, DB2, INGRES).contains(getDialect())) {

            // Advanced JOIN usages with single JOIN condition
            Result<Record> result = create().select()
                .from(TAuthor())
                .join(TBook())
                .on(TAuthor_ID().equal(TBook_AUTHOR_ID())
                .and(TBook_LANGUAGE_ID().in(create().select(field("id"))
                                                    .from("t_language")
                                                    .where("upper(cd) in (?, ?)", "DE", "EN")))
                .orExists(create().selectOne().from(TAuthor()).where(falseCondition())))
                .orderBy(TBook_ID()).fetch();

            assertEquals(3, result.size());
            assertEquals("1984", result.getValue(0, TBook_TITLE()));
            assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
            assertEquals("Brida", result.getValue(2, TBook_TITLE()));

            // Advanced JOIN usages with several JOIN condition
            // ------------------------------------------------
            Select<A> author = create().selectFrom(TAuthor());
            result = create().select()
                .from(author)
                .join(TBook())
                .on(author.getField(TAuthor_ID()).equal(TBook_AUTHOR_ID()))
                .and(TBook_LANGUAGE_ID().in(create().select(field("id"))
                                                    .from("t_language")
                                                    .where("upper(cd) in (?, ?)", "DE", "EN")))
                .orExists(create().selectOne().where(falseCondition()))
                .orderBy(TBook_ID()).fetch();

            assertEquals(3, result.size());
            assertEquals("1984", result.getValue(0, TBook_TITLE()));
            assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
            assertEquals("Brida", result.getValue(2, TBook_TITLE()));

            Select<B> book = create().selectFrom(TBook());
            result = create().select()
                .from(TAuthor())
                .join(book)
                .on(TAuthor_ID().equal(book.getField(TBook_AUTHOR_ID())))
                .and(book.getField(TBook_LANGUAGE_ID()).in(create().select(field("id"))
                                                    .from("t_language")
                                                    .where("upper(cd) in (?, ?)", "DE", "EN")))
                .orExists(create().selectOne().where(falseCondition()))
                .orderBy(book.getField(TBook_ID())).fetch();

            assertEquals(3, result.size());
            assertEquals("1984", result.getValue(0, TBook_TITLE()));
            assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
            assertEquals("Brida", result.getValue(2, TBook_TITLE()));

        }
    }

    @Test
    public void testCrossJoin() throws Exception {
        Result<Record> result;

        // Using the CROSS JOIN clause
        assertEquals(Integer.valueOf(8),
        create().select(count())
                .from(TAuthor())
                .crossJoin(TBook())
                .fetchOne(0));

        result =
        create().select()
                .from(create().select(val(1).cast(Integer.class).as("a")))
                .crossJoin(TAuthor())
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(Integer.valueOf(1), result.getValue(0, 0));
        assertEquals(Integer.valueOf(1), result.getValue(0, 1));
        assertEquals(Integer.valueOf(1), result.getValue(1, 0));
        assertEquals(Integer.valueOf(2), result.getValue(1, 1));


        // [#772] Using the FROM clause for regular cartesian products
        assertEquals(Integer.valueOf(8),
        create().select(count())
                .from(TAuthor(), TBook())
                .fetchOne(0));

        result =
        create().select()
                .from(create().select(val(1).cast(Integer.class).as("a")), TAuthor())
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(Integer.valueOf(1), result.getValue(0, 0));
        assertEquals(Integer.valueOf(1), result.getValue(0, 1));
        assertEquals(Integer.valueOf(1), result.getValue(1, 0));
        assertEquals(Integer.valueOf(2), result.getValue(1, 1));
    }

    @Test
    public void testNaturalJoin() throws Exception {
        Result<Record> result =
        create().select(TAuthor_LAST_NAME(), TBook_TITLE())
                .from(TBook())
                .naturalJoin(TAuthor())
                .orderBy(getDialect() == SQLDialect.ORACLE
                        ? field("id")
                        : TBook_ID())
                .fetch();

        assertEquals(2, result.size());
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Orwell", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));

        // TODO [#574] allow for selecting all columns, including
        // the ones making up the join condition!
        result =
        // create().select()
        create().select(TAuthor_LAST_NAME(), TBook_TITLE())
                .from(TBook())
                .naturalLeftOuterJoin(TAuthor())
                .orderBy(getDialect() == SQLDialect.ORACLE
                    ? field("id")
                    : TBook_ID())
                .fetch();

        assertEquals(4, result.size());
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Orwell", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));

        assertEquals("O Alquimista", result.getValue(2, TBook_TITLE()));
        assertNull(result.getValue(2, TAuthor_LAST_NAME()));
        assertEquals("Brida", result.getValue(3, TBook_TITLE()));
        assertNull(result.getValue(3, TAuthor_LAST_NAME()));
    }

    @Test
    public void testJoinUsing() throws Exception {
        Result<Record> result =
        create().select(TAuthor_LAST_NAME(), TBook_TITLE())
                .from(TAuthor())
                .join(TBook())
                .using(TAuthor_ID())
                .orderBy(getDialect() == SQLDialect.ORACLE
                        ? field("id")
                        : TBook_ID())
                .fetch();

        assertEquals(2, result.size());
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Orwell", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));

        // TODO [#574] allow for selecting all columns, including
        // the ones making up the join condition!
        result =
        // create().select()
        create().select(TAuthor_LAST_NAME(), TBook_TITLE())
                .from(TBook())
                .leftOuterJoin(TAuthor())
                .using(TAuthor_ID())
                .orderBy(getDialect() == SQLDialect.ORACLE
                    ? field("id")
                    : TBook_ID())
                .fetch();

        assertEquals(4, result.size());
        assertEquals("1984", result.getValue(0, TBook_TITLE()));
        assertEquals("Orwell", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Animal Farm", result.getValue(1, TBook_TITLE()));
        assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));

        assertEquals("O Alquimista", result.getValue(2, TBook_TITLE()));
        assertNull(result.getValue(2, TAuthor_LAST_NAME()));
        assertEquals("Brida", result.getValue(3, TBook_TITLE()));
        assertNull(result.getValue(3, TAuthor_LAST_NAME()));
    }

    @Test
    public void testJoinOnKey() throws Exception {
        if (!supportsReferences()) {
            log.info("SKIPPING", "JOIN ON KEY tests");
            return;
        }

        try {
            create().select(TAuthor_ID(), TBook_TITLE())
                    .from(TAuthor().join(TBook()).onKey());
            fail();
        }
        catch (DataAccessException expected) {}

        try {
            create().select(TAuthor_ID(), TBook_TITLE())
                    .from(TAuthor().join(TBook()).onKey(TBook_TITLE()));
            fail();
        }
        catch (DataAccessException expected) {}

        try {
            create().select(TAuthor_ID(), TBook_TITLE())
                    .from(TAuthor().join(TBook()).onKey(TBook_AUTHOR_ID(), TBook_ID()));
            fail();
        }
        catch (DataAccessException expected) {}

        // Test the Table API
        // ------------------
        Result<Record> result1 =
        create().select(TAuthor_ID(), TBook_TITLE())
                .from(TAuthor().join(TBook()).onKey(TBook_AUTHOR_ID()))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(4, result1.size());
        assertEquals(BOOK_AUTHOR_IDS, result1.getValues(0));
        assertEquals(BOOK_TITLES, result1.getValues(1));

        Result<Record> result2 =
        create().select(TAuthor_ID(), TBook_TITLE())
                .from(TAuthor()
                    .join(TBook())
                    .onKey(TBook_AUTHOR_ID())
                    .and(TBook_ID().in(1, 2)))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(2, result2.size());
        assertEquals(BOOK_AUTHOR_IDS.subList(0, 2), result2.getValues(0));
        assertEquals(BOOK_TITLES.subList(0, 2), result2.getValues(1));

        // Test the Select API
        // -------------------
        Result<Record> result3 =
        create().select(TAuthor_ID(), TBook_TITLE())
                .from(TAuthor())
                .join(TBook()).onKey(TBook_AUTHOR_ID())
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(4, result3.size());
        assertEquals(BOOK_AUTHOR_IDS, result3.getValues(0));
        assertEquals(BOOK_TITLES, result3.getValues(1));

        // Test using unambiguous keys
        // ---------------------------
        Result<Record> result4 =
        create().select(TBook_ID(), TBookStore_NAME())
                .from(TBook())
                .join(TBookToBookStore()).onKey()
                .join(TBookStore()).onKey()
                .orderBy(TBook_ID(), TBookStore_NAME())
                .fetch();

        assertEquals(6, result4.size());
        assertEquals(asList(1, 1, 2, 3, 3, 3), result4.getValues(0));
        assertEquals(asList(
            "Ex Libris", "Orell Füssli", "Orell Füssli",
            "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli"), result4.getValues(1));

        // [#671] Test inverse join relationship
        // -------------------------------------
        Result<Record> result5 =
        create().select(TBook_ID(), TBookStore_NAME())
                .from(TBook())
                .join(TBookToBookStore()
                    .join(TBookStore()).onKey())
                .onKey()
                .orderBy(TBook_ID(), TBookStore_NAME())
                .fetch();

        assertEquals(result4, result5);
    }

    @Test
    public void testInverseAndNestedJoin() throws Exception {

        // [#1086] TODO: Fix this for SQLite
        // In CUBRID, it is not suupported
        if (getDialect() == SQLITE || getDialect() == CUBRID) {
            log.info("SKIPPING", "Nested JOINs");
            return;
        }

        // Testing joining of nested joins
        // -------------------------------
        Result<Record> result1 = create()
            .select(
                TAuthor_ID(),
                TBook_ID(),
                TBookStore_NAME())
            .from(TAuthor()
                .join(TBook())
                .on(TAuthor_ID().equal(TBook_AUTHOR_ID())))
            .join(TBookToBookStore()
                .join(TBookStore())
                .on(TBookToBookStore_BOOK_STORE_NAME().equal(TBookStore_NAME())))
            .on(TBook_ID().equal(TBookToBookStore_BOOK_ID()))
            .orderBy(TBook_ID(), TBookStore_NAME())
            .fetch();

        assertEquals(6, result1.size());
        assertEquals(asList(1, 1, 1, 2, 2, 2), result1.getValues(0));
        assertEquals(asList(1, 1, 2, 3, 3, 3), result1.getValues(1));
        assertEquals(asList(
            "Ex Libris", "Orell Füssli", "Orell Füssli",
            "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli"), result1.getValues(2));

        // Testing joining of cross products
        Result<Record> result2 = create()
            .select(
                TAuthor_ID(),
                TBook_ID(),
                TBookStore_NAME())
            .from(TAuthor()
                        .join(TBook())
                        .on(TAuthor_ID().equal(TBook_AUTHOR_ID())),
                  TBookToBookStore()
                        .join(TBookStore())
                        .on(TBookToBookStore_BOOK_STORE_NAME().equal(TBookStore_NAME())))
            .where(TBook_ID().equal(TBookToBookStore_BOOK_ID()))
            .orderBy(TBook_ID(), TBookStore_NAME())
            .fetch();

        assertEquals(6, result2.size());
        assertEquals(asList(1, 1, 1, 2, 2, 2), result2.getValues(0));
        assertEquals(asList(1, 1, 2, 3, 3, 3), result2.getValues(1));
        assertEquals(asList(
            "Ex Libris", "Orell Füssli", "Orell Füssli",
            "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli"), result2.getValues(2));

        assertEquals(result1, result2);
    }

    @Test
    public void testOuterJoin() throws Exception {
        // Test LEFT OUTER JOIN
        // --------------------
        Result<Record> result1 =
        create().select(
                    TAuthor_ID(),
                    TBook_ID(),
                    TBookToBookStore_BOOK_STORE_NAME())
                .from(TAuthor())
                .leftOuterJoin(TBook()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                .leftOuterJoin(TBookToBookStore()).on(TBook_ID().equal(TBookToBookStore_BOOK_ID()))
                .orderBy(
                    TAuthor_ID().asc(),
                    TBook_ID().asc(),
                    TBookToBookStore_BOOK_STORE_NAME().asc().nullsLast())
                .fetch();

        assertEquals(
            asList(1, 1, 1, 2, 2, 2, 2),
            result1.getValues(0, Integer.class));
        assertEquals(
            asList(1, 1, 2, 3, 3, 3, 4),
            result1.getValues(1, Integer.class));
        assertEquals(
            asList("Ex Libris", "Orell Füssli", "Orell Füssli", "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli", null),
            result1.getValues(2));

        // Test RIGHT OUTER JOIN
        // ---------------------

        switch (getDialect()) {
            case SQLITE:
                log.info("SKIPPING", "RIGHT OUTER JOIN tests");
                break;

            default: {
                Result<Record> result2 =
                    create().select(
                                TAuthor_ID(),
                                TBook_ID(),
                                TBookToBookStore_BOOK_STORE_NAME())
                            .from(TBookToBookStore())
                            .rightOuterJoin(TBook()).on(TBook_ID().equal(TBookToBookStore_BOOK_ID()))
                            .rightOuterJoin(TAuthor()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                            .orderBy(
                                TAuthor_ID().asc(),
                                TBook_ID().asc(),
                                TBookToBookStore_BOOK_STORE_NAME().asc().nullsLast())
                            .fetch();

                assertEquals(result1, result2);
                assertEquals(
                    asList(1, 1, 1, 2, 2, 2, 2),
                    result2.getValues(0, Integer.class));
                assertEquals(
                    asList(1, 1, 2, 3, 3, 3, 4),
                    result2.getValues(1, Integer.class));
                assertEquals(
                    asList("Ex Libris", "Orell Füssli", "Orell Füssli", "Buchhandlung im Volkshaus", "Ex Libris", "Orell Füssli", null),
                    result2.getValues(2));

                break;
            }
        }

        // Test FULL OUTER JOIN
        // --------------------

        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case DERBY:
            case H2:
            case MYSQL:
            case SQLITE:
                log.info("SKIPPING", "FULL OUTER JOIN tests");
                break;

            default: {
                Select<?> z = create().select(zero().as("z"));
                Select<?> o = create().select(one().as("o"));

                Result<Record> result3 =
                create().select()
                        .from(z)
                        .fullOuterJoin(o).on(z.getField("z").cast(Integer.class).equal(o.getField("o").cast(Integer.class)))
                        .fetch();

                assertEquals("z", result3.getField(0).getName());
                assertEquals("o", result3.getField(1).getName());

                // Interestingly, ordering doesn't work with Oracle, in this
                // example... Seems to be an Oracle bug??
                @SuppressWarnings("unchecked")
                List<List<Integer>> list = asList(asList(0, null), asList(null, 1));
                assertTrue(list.contains(asList(result3.get(0).into(Integer[].class))));
                assertTrue(list.contains(asList(result3.get(1).into(Integer[].class))));
                break;
            }
        }
    }
}
