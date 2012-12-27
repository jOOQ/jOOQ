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
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.inline;
import static org.jooq.impl.Factory.lower;
import static org.jooq.impl.Factory.param;
import static org.jooq.impl.Factory.table;
import static org.jooq.impl.Factory.val;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class OrderByTests<
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

    public OrderByTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testOrderByInSubquery() throws Exception {
        // TODO: [#780] Fix this for Ingres and Sybase ASE
        switch (getDialect()) {
            case ASE:
            case INGRES:
                log.info("SKIPPING", "Ordered subqueries");
                return;
        }

        // Some RDBMS don't accept ORDER BY clauses in subqueries without
        // TOP clause (e.g. SQL Server). jOOQ will synthetically add a
        // TOP 100 PERCENT clause, if necessary

        Select<?> nested =
        create().select(TBook_ID())
                .from(TBook())
                .orderBy(TBook_ID().asc());

        List<Integer> result =
        create().select(nested.getField(TBook_ID()))
                .from(nested)
                .orderBy(nested.getField(TBook_ID()).desc())
                .fetch(nested.getField(TBook_ID()));

        assertEquals(Arrays.asList(4, 3, 2, 1), result);
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
    public void testLimit() throws Exception {

        // Some dialects don't support LIMIT 0 / TOP 0
        int lower = asList(DB2, DERBY, HSQLDB, INGRES, SYBASE).contains(getDialect()) ? 1 : 0;

        for (int i = lower; i < 6; i++) {
            assertEquals(Math.min(i, 4),
                create().selectFrom(TBook()).limit(i).fetch().size());
            assertEquals(Math.min(i, 4),
                create().select().from(TBook()).limit(i).fetch().size());
        }

        if (getDialect() == SQLDialect.ASE) {
            log.info("SKIPPING", "LIMIT .. OFFSET tests");
            return;
        }

        for (int i = lower; i < 6; i++) {
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

    @SuppressWarnings("unchecked")
    @Test
    public void testLimitBindValues() throws Exception {
        Select<?> select = create().select().limit(1).offset(2);
        assertSame(asList(1, 2), select.getBindValues());
        assertSame(asList(val(1), val(2)), select.getParams().values());
    }

    @Test
    public void testLimitNamedParams() throws Exception {
        if (asList(ASE, INGRES).contains(getDialect())) {
            log.info("SKIPPING", "Parameterised LIMIT .. OFFSET tests");
            return;
        }

        // Some dialects don't support LIMIT 0 / TOP 0
        int lower = asList(DB2, DERBY, HSQLDB, SYBASE).contains(getDialect()) ? 1 : 0;

        for (int i = lower; i < 6; i++) {
            Select<?> s1 = create().selectFrom(TBook()).limit(param("limit", i));
            Select<?> s2 = create().select().from(TBook()).limit(param("limit", i));

            assertEquals(Math.min(i, 4), s1.fetch().size());
            assertEquals(Math.min(i, 4), s2.fetch().size());
            assertEquals(Math.min(i + 1, 4), s1.bind("limit", i + 1).fetch().size());
            assertEquals(Math.min(i + 1, 4), s2.bind("limit", i + 1).fetch().size());
        }

        for (int i = lower; i < 6; i++) {
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

    @Test
    public void testLimitNested() throws Exception {
        // TODO [#780] This is not supported in Ingres
        if (getDialect() == SQLDialect.INGRES ||
            getDialect() == SQLDialect.ASE) {

            log.info("SKIPPING", "LIMIT clauses in nested SELECTs");
            return;
        }

        Table<B> nested = table(create()
            .selectFrom(TBook())
            .orderBy(TBook_ID().desc())
            .limit(2))
            .as("nested");

        Field<Integer> nestedID = nested.getField(TBook_AUTHOR_ID());
        Record record = create().select(nestedID, count())
            .from(nested)
            .groupBy(nestedID)
            .orderBy(nestedID)
            .fetchOne();

        assertEquals(Integer.valueOf(2), record.getValue(nestedID));
        assertEquals(Integer.valueOf(2), record.getValue(1));

        Result<Record> result = create().select(nestedID, count())
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
}
