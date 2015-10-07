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
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

public class OrderByTests<
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

    public OrderByTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testOrderByInSubquery() throws Exception {
        /* [pro] */
        // TODO: [#780] Fix this for Ingres and Sybase ASE
        switch (dialect()) {
            case ASE:
            case INGRES:
                log.info("SKIPPING", "Ordered subqueries");
                return;
        }

        /* [/pro] */
        // Some RDBMS don't accept ORDER BY clauses in subqueries without
        // TOP clause (e.g. SQL Server). jOOQ will synthetically add a
        // TOP 100 PERCENT clause, if necessary

        Select<?> nested =
        create().select(TBook_ID())
                .from(TBook())
                .orderBy(TBook_ID().asc());

        List<Integer> result =
        create().select(nested.field(TBook_ID()))
                .from(nested)
                .orderBy(nested.field(TBook_ID()).desc())
                .fetch(nested.field(TBook_ID()));

        assertEquals(Arrays.asList(4, 3, 2, 1), result);
    }

    public void testOrderByNulls() throws Exception {
        jOOQAbstractTest.reset = false;

        // Make data a bit more meaningful, first
        create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME())
                .values(3, "Döblin")
                .execute();

        Result<A> authors1 =
        create().selectFrom(TAuthor())
                .orderBy(
                    TAuthor_FIRST_NAME().asc().nullsFirst())
                .fetch();

        assertNull(authors1.getValue(0, TAuthor_FIRST_NAME()));
        assertEquals("George", authors1.getValue(1, TAuthor_FIRST_NAME()));
        assertEquals("Paulo", authors1.getValue(2, TAuthor_FIRST_NAME()));

        Result<A> authors2 =
        create().selectFrom(TAuthor())
                .orderBy(
                    TAuthor_FIRST_NAME().asc().nullsLast())
                .fetch();

        assertEquals("George", authors2.getValue(0, TAuthor_FIRST_NAME()));
        assertEquals("Paulo", authors2.getValue(1, TAuthor_FIRST_NAME()));
        assertNull(authors2.getValue(2, TAuthor_FIRST_NAME()));

        // [#1667] Check correct behaviour when bind values are involved
        Result<A> authors3 =
        create().selectFrom(TAuthor())
                .orderBy(
                    TAuthor_FIRST_NAME().substring(3).asc().nullsLast())
                .fetch();

        assertEquals("George", authors3.getValue(0, TAuthor_FIRST_NAME()));
        assertEquals("Paulo", authors3.getValue(1, TAuthor_FIRST_NAME()));
        assertNull(authors3.getValue(2, TAuthor_FIRST_NAME()));
    }

    public void testOrderByWithDual() throws Exception {
        assertEquals(1, (int) create().selectOne().orderBy(1).fetchOne(0, int.class));
    }

    public void testOrderByIndexes() throws Exception {
        assertEquals(Arrays.asList(1, 2, 3, 4),
            create().selectFrom(TBook())
                    .orderBy(1)
                    .fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 2, 3, 4),
            create().select(TBook_ID(), TBook_TITLE())
                    .from(TBook())
                    .orderBy(1)
                    .fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 2, 3, 4),
            create().select(TBook_TITLE(), TBook_ID())
                    .from(TBook())
                    .orderBy(2)
                    .fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 1, 2, 2),
            create().select(TBook_AUTHOR_ID(), TBook_ID())
                    .from(TBook())
                    .orderBy(2, 1)
                    .fetch(TBook_AUTHOR_ID()));
    }

    public void testOrderByIndirection() throws Exception {
        assertEquals(BOOK_IDS,
            create().selectFrom(TBook())
            .orderBy(TBook_ID().sortAsc(), TBook_ID().asc())
            .fetch(TBook_ID()));

        assertEquals(Arrays.asList(3, 2, 4, 1),
            create().selectFrom(TBook())
                    .orderBy(TBook_ID().sortAsc(3, 2, 4, 1))
                    .fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 4, 2, 3),
            create().selectFrom(TBook())
                    .orderBy(TBook_ID().sortDesc(3, 2, 4, 1))
                    .fetch(TBook_ID()));

//        assertEquals(Arrays.asList(3, 2, 1, 4),
//            create().selectFrom(TBook())
//                    .orderBy(TBook_ID().sortAsc(3, 2).nullsLast(), TBook_ID().asc())
//                    .fetch(TBook_ID()));
//
//        assertEquals(Arrays.asList(1, 4, 3, 2),
//            create().selectFrom(TBook())
//                    .orderBy(TBook_ID().sortAsc(3, 2).nullsFirst(), TBook_ID().asc())
//                    .fetch(TBook_ID()));

        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(1, 3);
        map.put(2, 4);
        map.put(3, 1);
        map.put(4, 2);
        assertEquals(Arrays.asList(3, 4, 1, 2),
            create().selectFrom(TBook())
                    .orderBy(TBook_ID().sort(map))
                    .fetch(TBook_ID()));
    }

    public void testOrderByAndLimit() throws Exception {

        // [#1954] The combination of ORDER BY and LIMIT clauses has some
        // implications on the ROW_NUMBER() window function in certain databases
        List<Integer> result1 =
        create().select(TBook_ID())
                .from(TBook())

                // Avoid fetching 1984 as ordering differs between DBs
                .where(TBook_ID().between(2).and(4))
                .orderBy(
                    TBook_AUTHOR_ID().mul(2).asc(),
                    lower(TBook_TITLE()).asc())
                .limit(2)
                .fetch(TBook_ID());

        assertEquals(asList(2, 4), result1);

        /* [pro] */
        // [#2812] TODO: Implement this also for MS Access and Sybase ASE
        if (asList(ACCESS, ASE).contains(dialect().family())) {
            log.info("SKIPPING", "OFFSET tests");
            return;
        }
        /* [/pro] */

        // [#1954] LIMIT .. OFFSET is more trouble than LIMIT, as it is
        // simulated using ROW_NUMBER() in DB2, SQL Server
        List<Integer> result2 =
        create().select(TBook_ID())
                .from(TBook())

                // Avoid fetching 1984 as ordering differs between DBs
                .where(TBook_ID().between(2).and(4))
                .orderBy(
                    TBook_AUTHOR_ID().mul(2).asc(),
                    lower(TBook_TITLE()).asc())

                // Force Sybase to simulate TOP .. START AT
                .limit(inline(2))
                .offset(1)
                .fetch(TBook_ID());

        assertEquals(asList(4, 3), result2);
    }

    public void testOrderByAndSeek() throws Exception {

        // Single ORDER BY column, no LIMIT
        // --------------------------------
        assertEquals(
            asList(3, 4),
            create().select(TBook_ID())
                    .from(TBook())
                    .orderBy(TBook_ID())
                    .seek(2)
                    .fetch(TBook_ID()));

        assertEquals(
            asList(2, 1),
            create().select(TBook_ID())
                    .from(TBook())
                    .orderBy(TBook_ID().desc())
                    .seekAfter(3)
                    .fetch(TBook_ID()));

        assertEquals(
            asList(1, 2),
            create().select(TBook_ID())
                    .from(TBook())
                    .orderBy(TBook_ID().asc())
                    .seekBefore(3)
                    .fetch(TBook_ID()));

        // Single ORDER BY column, with LIMIT
        // ----------------------------------
        assertEquals(
            asList(3),
            create().select(TBook_ID())
                    .from(TBook())
                    .orderBy(TBook_ID())
                    .seekAfter(2)
                    .limit(1)
                    .fetch(TBook_ID()));

        assertEquals(
            asList(2, 3),
            create().select(TBook_ID())
                    .from(TBook())
                    .orderBy(TBook_ID())
                    .seekAfter(1)
                    .limit(2)
                    .fetch(TBook_ID()));

        assertEquals(
            asList(2, 1),
            create().select(TBook_ID())
                    .from(TBook())
                    .orderBy(TBook_ID().desc())
                    .seekAfter(3)
                    .limit(4)
                    .fetch(TBook_ID()));

        assertEquals(
            asList(1, 2),
            create().select(TBook_ID())
                    .from(TBook())
                    .orderBy(TBook_ID().asc())
                    .seekBefore(3)
                    .limit(4)
                    .fetch(TBook_ID()));

        // Several aligned ORDER BY columns (same sort direction) with or without LIMIT
        // ----------------------------------------------------------------------------

        // For optimisation purposes, jOOQ will render row value expression predicates in case sort directions are aligned

        assertEquals(
            asList(1, 3, 3, 1),
            create().select(TBookToBookStore_BOOK_ID())
                    .from(TBookToBookStore())
                    .orderBy(TBookToBookStore_STOCK().asc(), TBookToBookStore_BOOK_ID().asc())
                    .seek(0, 0)
                    .limit(4)
                    .fetch(TBookToBookStore_BOOK_ID()));

        assertEquals(
            asList(3, 3, 1, 2),
            create().select(TBookToBookStore_BOOK_ID())
                    .from(TBookToBookStore())
                    .orderBy(TBookToBookStore_STOCK().asc(), TBookToBookStore_BOOK_ID().asc())
                    .seek(1, 1)
                    .limit(4)
                    .fetch(TBookToBookStore_BOOK_ID()));

        assertEquals(
            asList(3, 1, 2, 3),
            create().select(TBookToBookStore_BOOK_ID())
                    .from(TBookToBookStore())
                    .orderBy(TBookToBookStore_STOCK().asc(), TBookToBookStore_BOOK_ID().asc())
                    .seek(1, 3)
                    .fetch(TBookToBookStore_BOOK_ID()));

        // Several non-aligned ORDER BY columns (different sort direction) with or without LIMIT
        // -------------------------------------------------------------------------------------

        assertEquals(
            asList(1, 3, 3, 1),
            create().select(TBookToBookStore_BOOK_ID())
                    .from(TBookToBookStore())
                    .orderBy(
                        TBookToBookStore_STOCK().desc(),
                        TBookToBookStore_BOOK_STORE_NAME().asc(),
                        TBookToBookStore_BOOK_ID().desc()
                    )
                    .seek(10, "Orell Füssli", 2)
                    .fetch(TBookToBookStore_BOOK_ID()));

        // Complete random sorting with NULLS FIRST and NULLS LAST
        // -------------------------------------------------------
    }

    public void testLimit() throws Exception {

        // Some dialects don't support LIMIT 0 / TOP 0
        for (int i = 1; i < 6; i++) {
            assertEquals(Math.min(i, 4),
                create().selectFrom(TBook()).limit(i).fetch().size());
            assertEquals(Math.min(i, 4),
                create().select().from(TBook()).limit(i).fetch().size());
        }

        /* [pro] */
        // [#2812] TODO: Implement this also for MS Access and Sybase ASE
        if (asList(ACCESS, ASE).contains(dialect().family())) {
            log.info("SKIPPING", "LIMIT .. OFFSET tests");
            return;
        }

        /* [/pro] */
        for (int i = 1; i < 6; i++) {
            assertEquals(Math.min(i, 3),
                create().selectFrom(TBook()).limit(1, i).fetch().size());
            assertEquals(Math.min(i, 3),
                create().selectFrom(TBook()).limit(i).offset(1).fetch().size());
            assertEquals(Math.min(i, 3),
                create().select().from(TBook()).limit(1, i).fetch().size());
            assertEquals(Math.min(i, 3),
                create().select().from(TBook()).limit(i).offset(1).fetch().size());
        }

        Result<B> result = create()
            .selectFrom(TBook())
            .orderBy(TBook_ID(), TBook_AUTHOR_ID())
            .limit(1, 2)
            .fetch();

        assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
        assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
    }

    public void testLimitWithAmbiguousColumnNames() throws Exception {
        // [#2335] In those databases that do not support LIMIT .. OFFSET, emulations
        // using ROW_NUMBER() or ROWNUM may cause additional issues

        Result<Record2<Integer, Integer>> result1 =
        create().select(TAuthor_ID(), TBook_ID())
                .from(TAuthor())
                .join(TBook()).on(TAuthor_ID().eq(TBook_AUTHOR_ID()))
                .orderBy(TBook_ID())
                .limit(2)
                .fetch();

        assertEquals(2, result1.size());
        assertEquals(asList(1, 1), result1.getValues(TAuthor_ID()));
        assertEquals(asList(1, 2), result1.getValues(TBook_ID()));

        /* [pro] */
        // [#2812] TODO: Implement this also for MS Access and Sybase ASE
        if (asList(ACCESS, ASE).contains(dialect().family())) {
            log.info("SKIPPING", "LIMIT .. OFFSET tests");
            return;
        }

        /* [/pro] */

        Result<Record2<Integer, Integer>> result2 =
        create().select(TAuthor_ID(), TBook_ID())
                .from(TAuthor())
                .join(TBook()).on(TAuthor_ID().eq(TBook_AUTHOR_ID()))
                .orderBy(TBook_ID())
                .limit(2)
                .offset(2)
                .fetch();

        assertEquals(2, result2.size());
        assertEquals(asList(2, 2), result2.getValues(TAuthor_ID()));
        assertEquals(asList(3, 4), result2.getValues(TBook_ID()));
    }

    public void testLimitDistinct() throws Exception {

        /* [pro] */
        if (dialect() == SQLDialect.ACCESS ||
            dialect() == SQLDialect.ASE) {

            log.info("SKIPPING", "LIMIT clauses in nested SELECTs");
            return;
        }
        /* [/pro] */

        assertEquals(asList(1, 2),
        create().selectDistinct(TBookToBookStore_BOOK_ID())
                .from(TBookToBookStore())
                .orderBy(TBookToBookStore_BOOK_ID())
                .limit(2)
                .fetch(TBookToBookStore_BOOK_ID()));

        assertEquals(asList(2, 3),
        create().selectDistinct(TBookToBookStore_BOOK_ID())
                .from(TBookToBookStore())
                .orderBy(TBookToBookStore_BOOK_ID())
                .limit(2)
                .offset(1)
                .fetch(TBookToBookStore_BOOK_ID()));

        assertSame(asList(2, 1),
        create().selectDistinct(TBook_LANGUAGE_ID(), TBook_AUTHOR_ID())
                .from(TBook())
                .orderBy(TBook_AUTHOR_ID().desc())
                .limit(2)
                .offset(1)
                .fetch(TBook_AUTHOR_ID()));
    }

    public void testLimitAliased() throws Exception {
    	assumeFamilyNotIn(ACCESS, ASE);

        // [#2080] Some databases generate ORDER BY clauses within their ranking
        // functions. There are some syntax problems, when selectable columns
        // have aliases

        // Arbitrary ordering
        // ------------------

        // Inline LIMIT .. OFFSET with arbitrary ordering
        Result<Record2<Integer, String>> r1 =
        create().select(TBook_ID().as("xx"), TBook_TITLE().as("yy"))
                .from(TBook())
                .limit(1, 2)
                .fetch();

        assertEquals(2, r1.size());

        // Bind values for LIMIT .. OFFSET with arbitrary ordering
        Result<Record2<Integer, String>> r2 =
        create().select(TBook_ID().as("xx"), TBook_TITLE().as("yy"))
                .from(TBook())
                .limit(param("x", 1), param("y", 2))
                .fetch();

        assertEquals(2, r2.size());

        // Explicit ordering
        // -----------------

        // Inline LIMIT .. OFFSET with explicit ordering
        Result<Record2<String, Integer>> r3 =
        create().select(TBook_TITLE().as("yy"), TBook_ID().as("xx"))
                .from(TBook())
                .orderBy(TBook_ID().as("xx").desc())
                .limit(1, 2)
                .fetch();

        assertEquals(2, r3.size());
        assertEquals(asList(3, 2), r3.getValues("xx"));

        // Bind values for LIMIT .. OFFSET with arbitrary ordering
        Result<Record2<String, Integer>> r4 =
        create().select(TBook_TITLE().as("yy"), TBook_ID().as("xx"))
                .from(TBook())
                .orderBy(TBook_ID().as("xx").desc())
                .limit(param("x", 1), param("y", 2))
                .fetch();

        assertEquals(2, r4.size());
        assertEquals(asList(3, 2), r4.getValues("xx"));

        // Nested expressions
        switch (family()) {
            case DERBY:
            case H2:
            case POSTGRES:
            /* [pro] */
            case REDSHIFT:
            case SQLSERVER:
            case VERTICA:
                log.info("SKIPPING", "[#3575] Emulated OFFSET pagination with expressions using aliases in ORDER BY");
                break;
            /* [/pro] */

            default: {
                Result<Record2<String, Integer>> r5 =
                create().select(TBook_TITLE().as("yy"), TBook_ID().as("xx"))
                        .from(TBook())
                        .orderBy(TBook_ID().as("xx").sortAsc(4, 1, 3, 2))
                        .limit(param("x", 1), param("y", 2))
                        .fetch();

                assertEquals(2, r5.size());
                assertEquals(asList(1, 3), r5.getValues("xx"));
            }
        }

        // Subqueries
        switch (family()) {
            // These dialects do not really have full support for ORDER BY (SELECT ...)
            case H2:
            case DERBY:
            /* [pro] */
            case DB2:
            case REDSHIFT:
            case SYBASE:
                log.info("SKIPPING", "Emulated OFFSET pagination with ORDER BY (SELECT)");
                break;
            /* [/pro] */

            default: {
                Result<Record2<String, Integer>> r6 =
                create().select(TBook_TITLE().as("yy"), TBook_ID().as("xx"))
                        .from(TBook())
                        .orderBy(
                            select(TAuthor_LAST_NAME().as("xx")).from(TAuthor()).where(TAuthor_ID().eq(TBook_AUTHOR_ID())).asField(),
                            TBook_ID()
                        )
                        .limit(param("x", 1), param("y", 2))
                        .fetch();

                assertEquals(2, r6.size());
                assertEquals(asList(4, 1), r6.getValues("xx"));
            }
        }
    }

    public void testLimitBindValues() throws Exception {
        Select<?> s2 = DSL.select().limit(1).offset(2);
        assertSame(asList(1, 2), s2.getBindValues());
        assertSame(asList(val(1), val(2)), s2.getParams().values());
    }

    public void testLimitNamedParams() throws Exception {
        /* [pro] */
        if (asList(ASE, INGRES).contains(dialect())) {
            log.info("SKIPPING", "Parameterised LIMIT .. OFFSET tests");
            return;
        }

        /* [/pro] */
        // Some dialects don't support LIMIT 0 / TOP 0
        for (int i = 1; i < 6; i++) {
            Select<?> s1 = create().selectFrom(TBook()).limit(param("limit", i));
            Select<?> s2 = create().select().from(TBook()).limit(param("limit", i));

            assertEquals(Math.min(i, 4), s1.fetch().size());
            assertEquals(Math.min(i, 4), s2.fetch().size());
            assertEquals(Math.min(i + 1, 4), s1.bind("limit", i + 1).fetch().size());
            assertEquals(Math.min(i + 1, 4), s2.bind("limit", i + 1).fetch().size());
        }

        for (int i = 1; i < 6; i++) {
            Select<?> s1a = create().selectFrom(TBook()).limit(param("offset", 1), i);
            Select<?> s1b = create().selectFrom(TBook()).limit(1, param("limit", i));
            Select<?> s1c = create().selectFrom(TBook()).limit(param("offset", 1), param("limit", i));

            Select<?> s2a = create().selectFrom(TBook()).limit(i).offset(param("offset", 1));
            Select<?> s2b = create().selectFrom(TBook()).limit(param("limit", i)).offset(1);
            Select<?> s2c = create().selectFrom(TBook()).limit(param("limit", i)).offset(param("offset", 1));

            Select<?> s3a = create().select().from(TBook()).limit(param("offset", 1), i);
            Select<?> s3b = create().select().from(TBook()).limit(1, param("limit", i));
            Select<?> s3c = create().select().from(TBook()).limit(param("offset", 1), param("limit", i));

            Select<?> s4a = create().select().from(TBook()).limit(i).offset(param("offset", 1));
            Select<?> s4b = create().select().from(TBook()).limit(param("limit", i)).offset(1);
            Select<?> s4c = create().select().from(TBook()).limit(param("limit", i)).offset(param("offset", 1));

            assertEquals(Math.min(i, 3), s1a.fetch().size());
            assertEquals(Math.min(i, 3), s1b.fetch().size());
            assertEquals(Math.min(i, 3), s1c.fetch().size());
            assertEquals(Math.min(i + 1, 2), s1c.bind("limit", i + 1).bind("offset", 2).fetch().size());

            assertEquals(Math.min(i, 3), s2a.fetch().size());
            assertEquals(Math.min(i, 3), s2b.fetch().size());
            assertEquals(Math.min(i, 3), s2c.fetch().size());
            assertEquals(Math.min(i + 1, 2), s2c.bind("limit", i + 1).bind("offset", 2).fetch().size());

            assertEquals(Math.min(i, 3), s3a.fetch().size());
            assertEquals(Math.min(i, 3), s3b.fetch().size());
            assertEquals(Math.min(i, 3), s3c.fetch().size());
            assertEquals(Math.min(i + 1, 2), s3c.bind("limit", i + 1).bind("offset", 2).fetch().size());

            assertEquals(Math.min(i, 3), s4a.fetch().size());
            assertEquals(Math.min(i, 3), s4b.fetch().size());
            assertEquals(Math.min(i, 3), s4c.fetch().size());
            assertEquals(Math.min(i + 1, 2), s4c.bind("limit", i + 1).bind("offset", 2).fetch().size());
        }

        Result<B> result = create()
            .selectFrom(TBook())
            .orderBy(TBook_ID(), TBook_AUTHOR_ID())
            .limit(
                param("offset", 1),
                param("limit", 2))
            .fetch();

        assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
        assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
    }

    public void testLimitNested() throws Exception {
        /* [pro] */
        // TODO [#780] This is not supported in Ingres
        if (dialect() == SQLDialect.ACCESS ||
            dialect() == SQLDialect.INGRES ||
            dialect() == SQLDialect.ASE) {

            log.info("SKIPPING", "LIMIT clauses in nested SELECTs");
            return;
        }

        /* [/pro] */
        Table<B> nested = table(create()
            .selectFrom(TBook())
            .orderBy(TBook_ID().desc())
            .limit(2))
            .as("nested");

        Field<Integer> nestedID = nested.field(TBook_AUTHOR_ID());
        Record record = create().select(nestedID, count())
            .from(nested)
            .groupBy(nestedID)
            .orderBy(nestedID)
            .fetchOne();

        assertEquals(Integer.valueOf(2), record.getValue(nestedID));
        assertEquals(Integer.valueOf(2), record.getValue(1));

        Result<Record2<Integer, Integer>> result = create().select(nestedID, count())
            .from(create().selectFrom(TBook())
                          .orderBy(TBook_ID().desc())
                          .limit(1, 2).asTable("nested"))
            .groupBy(nestedID)
            .orderBy(nestedID)
            .fetch();

        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(1), result.getValue(0, nestedID));
        assertEquals(Integer.valueOf(1), result.getValue(0, 1));
        assertEquals(Integer.valueOf(2), result.getValue(1, nestedID));
        assertEquals(Integer.valueOf(1), result.getValue(1, 1));
    }

    public void testLimitWithLOBs() throws Exception {
        // [#2646] Emulated LIMIT .. OFFSET clauses must not synthetically sort on
        // LOB columns
        Result<Record1<String>> result =
        create().select(TBook_CONTENT_TEXT())
                .from(TBook())
                .limit(2)
                .fetch();

        assertEquals(2, result.size());

        result =
        create().select(TBook_CONTENT_TEXT())
                .from(TBook())
                .limit(2)
                .offset(1)
                .fetch();

        assertEquals(2, result.size());
    }
}
