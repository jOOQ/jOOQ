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
import static junit.framework.Assert.assertTrue;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.countDistinct;
import static org.jooq.impl.Factory.trim;
import static org.jooq.impl.Factory.val;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class SelectTests<
    A    extends UpdatableRecord<A>,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, B, S, B2S, BS, L, X, D, T, U, I, IPK, T658, T725, T639, T785> {

    public SelectTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testSelectSimpleQuery() throws Exception {
        SelectQuery q = create().selectQuery();
        Field<Integer> f1 = val(1).as("f1");
        Field<Double> f2 = val(2d).as("f2");
        Field<String> f3 = val("test").as("f3");

        q.addSelect(f1);
        q.addSelect(f2);
        q.addSelect(f3);

        int i = q.execute();
        Result<?> result = q.getResult();

        assertEquals(1, i);
        assertEquals(1, result.size());
        assertEquals(3, result.getFields().size());
        assertTrue(result.getFields().contains(f1));
        assertTrue(result.getFields().contains(f2));
        assertTrue(result.getFields().contains(f3));

        assertEquals(3, result.get(0).getFields().size());
        assertTrue(result.get(0).getFields().contains(f1));
        assertTrue(result.get(0).getFields().contains(f2));
        assertTrue(result.get(0).getFields().contains(f3));

        assertEquals(Integer.valueOf(1), result.get(0).getValue(f1));
        assertEquals(2d, result.get(0).getValue(f2));
        assertEquals("test", result.get(0).getValue(f3));
    }

    @Test
    public void testSelectQuery() throws Exception {
        SelectQuery q = create().selectQuery();
        q.addFrom(TAuthor());
        q.addSelect(TAuthor().getFields());
        q.addOrderBy(TAuthor_LAST_NAME());

        int rows = q.execute();
        Result<?> result = q.getResult();

        assertEquals(2, rows);
        assertEquals(2, result.size());
        assertEquals("Coelho", result.get(0).getValue(TAuthor_LAST_NAME()));
        assertEquals("Orwell", result.get(1).getValue(TAuthor_LAST_NAME()));
    }

    @Test
    public void testDistinctQuery() throws Exception {
        Result<Record> result = create()
            .selectDistinct(TBook_AUTHOR_ID())
            .from(TBook())
            .orderBy(TBook_AUTHOR_ID())
            .fetch();

        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(1), result.get(0).getValue(TBook_AUTHOR_ID()));
        assertEquals(Integer.valueOf(2), result.get(1).getValue(TBook_AUTHOR_ID()));

        assertEquals(2, create()
            .select(countDistinct(TBook_AUTHOR_ID()))
            .from(TBook())
            .fetchOne(0));
        assertEquals(2, create()
            .selectDistinct(TBook_AUTHOR_ID())
            .from(TBook())
            .fetch()
            .size());
    }

    @Test
    public void testSubSelect() throws Exception {
        // ---------------------------------------------------------------------
        // Testing the IN condition
        // ---------------------------------------------------------------------
        assertEquals(3,
            create().selectFrom(TBook())
                .where(TBook_TITLE().notIn(create()
                    .select(TBook_TITLE())
                    .from(TBook())
                    .where(TBook_TITLE().in("1984"))))
                .execute());

        // ---------------------------------------------------------------------
        // Testing the EXISTS condition
        // ---------------------------------------------------------------------
        assertEquals(3,
            create()
                .selectFrom(TBook())
                .whereNotExists(create()
                    .selectOne()
                    .from(TAuthor())
                    .where(TAuthor_YEAR_OF_BIRTH().greaterOrEqual(TBook_PUBLISHED_IN())))

                // Add additional useless queries to check query correctness
                .orNotExists(create().select())
                .andExists(create().select()).execute());

        // ---------------------------------------------------------------------
        // Testing selecting from a select
        // ---------------------------------------------------------------------
        Table<Record> nested = create().select(TBook_AUTHOR_ID(), count().as("books"))
            .from(TBook())
            .groupBy(TBook_AUTHOR_ID()).asTable("nested");

        Result<Record> records = create().select(nested.getFields())
            .from(nested)
            .orderBy(nested.getField("books"), nested.getField(TBook_AUTHOR_ID())).fetch();

        assertEquals(2, records.size());
        assertEquals(Integer.valueOf(1), records.getValue(0, nested.getField(TBook_AUTHOR_ID())));
        assertEquals(Integer.valueOf(2), records.getValue(0, nested.getField("books")));
        assertEquals(Integer.valueOf(2), records.getValue(1, nested.getField(TBook_AUTHOR_ID())));
        assertEquals(Integer.valueOf(2), records.getValue(1, nested.getField("books")));

        Field<Object> books = create().select(count())
                .from(TBook())
                .where(TBook_AUTHOR_ID().equal(TAuthor_ID())).asField("books");

        records = create().select(TAuthor_ID(), books)
                          .from(TAuthor())
                          .orderBy(books, TAuthor_ID()).fetch();

        assertEquals(2, records.size());
        assertEquals(Integer.valueOf(1), records.getValue(0, TAuthor_ID()));
        assertEquals(Integer.valueOf(2), records.getValue(0, books));
        assertEquals(Integer.valueOf(2), records.getValue(1, TAuthor_ID()));
        assertEquals(Integer.valueOf(2), records.getValue(1, books));

        // ---------------------------------------------------------------------
        // [#493, #632] Testing filtering by a select's outcome
        // ---------------------------------------------------------------------

        // TODO [#632] Handle this for Sybase
        assertEquals("Coelho",
        create().select(TAuthor_LAST_NAME())
                .from(TAuthor())
                .where(val(0).equal(create()
                             .select(count(TBook_ID()))
                             .from(TBook())
                             .where(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                             .and(TBook_TITLE().equal("1984"))))
                .limit(1)
                .fetchOne(TAuthor_LAST_NAME()));
    }

    @Test
    public void testUnaliasedSubqueryProjections() throws Exception {
        // Test whether unaliased literals in subquery projections are correctly
        // handled
        Result<Record> result =
        create().select()
                .from(
                    create().selectOne(),
                    create().select(val(2)),
                    create().select(val(2)),
                    create().select(val(2)),
                    create().select(val(3).add(4)),
                    create().select(val(3).add(4)),
                    create().select(trim(" test ")),
                    create().select(trim(" test ")))
                .fetch();

        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(1), result.getValue(0, 0));
        assertEquals(Integer.valueOf(2), result.getValue(0, 1));
        assertEquals(Integer.valueOf(2), result.getValue(0, val(2)));
        assertEquals(Integer.valueOf(2), result.getValue(0, 3));
        assertEquals(Integer.valueOf(7), result.getValue(0, val(3).add(4)));
        assertEquals(Integer.valueOf(7), result.getValue(0, 5));
        assertEquals("test", result.getValue(0, trim(" test ")));
        assertEquals("test", result.getValue(0, 7));

        result =
        create().select(
                    create().selectOne().asField(),
                    create().select(val(2)).asField(),
                    create().select(val(2)).asField(),
                    create().select(val(2)).asField(),
                    create().select(val(3).add(4)).asField(),
                    create().select(val(3).add(4)).asField(),
                    create().select(trim(" test ")).asField(),
                    create().select(trim(" test ")).asField())
                .fetch();

        assertEquals(1, result.size());
        assertEquals(1, result.getValue(0, 0));
        assertEquals(2, result.getValue(0, 1));
        assertEquals(2, result.getValue(0, 2));
        assertEquals(2, result.getValue(0, 3));
        assertEquals(7, result.getValue(0, 4));
        assertEquals(7, result.getValue(0, 5));
        assertEquals("test", result.getValue(0, 6));
        assertEquals("test", result.getValue(0, 7));
    }

    @Test
    public void testCombinedSelectQuery() throws Exception {
        SelectQuery q1 = create().selectQuery();
        SelectQuery q2 = create().selectQuery();

        q1.addFrom(TBook());
        q2.addFrom(TBook());

        q1.addConditions(TBook_AUTHOR_ID().equal(1));
        q2.addConditions(TBook_TITLE().equal("Brida"));

        // Use union all because of clob's
        Select<?> union = q1.unionAll(q2);
        int rows = union.execute();
        assertEquals(3, rows);

        // Use union all because of clob's
        rows = create().selectDistinct(union.getField(TBook_AUTHOR_ID()), TAuthor_FIRST_NAME())
            .from(union)
            .join(TAuthor())
            .on(union.getField(TBook_AUTHOR_ID()).equal(TAuthor_ID()))
            .orderBy(TAuthor_FIRST_NAME())
            .execute();

        assertEquals(2, rows);
    }

    @Test
    public void testComplexUnions() throws Exception {
        Select<Record> s1 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(1));
        Select<Record> s2 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(2));
        Select<Record> s3 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(3));
        Select<Record> s4 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(4));

        Result<Record> result = create().select().from(s1.union(s2).union(s3).union(s4)).fetch();
        assertEquals(4, result.size());

        result = create().select().from(s1.union(s2).union(s3.union(s4))).fetch();
        assertEquals(4, result.size());

        assertEquals(4, create().select().from(s1.union(
                            create().select().from(s2.unionAll(
                                create().select().from(s3.union(s4))))))
                                    .fetch().size());

        // [#289] Handle bad syntax scenario provided by user Gunther
        Select<Record> q = create().select(val(2008).as("y"));
        for (int year = 2009; year <= 2011; year++) {
            q = q.union(create().select(val(year).as("y")));
        }

        assertEquals(4, q.execute());
    }

    @Test
    public void testForUpdateClauses() throws Exception {
        switch (getDialect()) {
            case SQLITE:
            case SQLSERVER:
                log.info("SKIPPING", "FOR UPDATE tests");
                return;
        }

        // Just checking for syntax correctness. Locking should be OK
        Result<Record> result = create().select(TAuthor_ID())
                                        .from(TAuthor())
                                        .forUpdate()
                                        .fetch();
        assertEquals(2, result.size());
        Result<A> result2 = create().selectFrom(TAuthor())
                .forUpdate()
                .fetch();
        assertEquals(2, result2.size());

        switch (getDialect()) {
            case ASE:
            case DB2:
            case DERBY:
            case HSQLDB:
            case H2:
            case INGRES:
            case MYSQL:
            case POSTGRES:
            case SYBASE:
                log.info("SKIPPING", "FOR UPDATE .. WAIT/NOWAIT tests");
                break;

            default: {
                result = create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()
                        .wait(2)
                        .fetch();
                assertEquals(2, result.size());
                result = create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()
                        .noWait()
                        .fetch();
                assertEquals(2, result.size());
                result = create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()
                        .skipLocked()
                        .fetch();
                assertEquals(2, result.size());


                result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor_LAST_NAME(), TAuthor_FIRST_NAME())
                        .wait(2)
                        .fetch();
                assertEquals(2, result2.size());
                result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor_LAST_NAME(), TAuthor_FIRST_NAME())
                        .noWait()
                        .fetch();
                assertEquals(2, result2.size());
                result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor_LAST_NAME(), TAuthor_FIRST_NAME())
                        .skipLocked()
                        .fetch();
                assertEquals(2, result2.size());
            }
        }

        switch (getDialect()) {
            case MYSQL:
                log.info("SKIPPING", "FOR UPDATE OF tests");
                break;

            // Most dialects support the OF clause
            case DB2:
            case DERBY:
            case H2:
            case HSQLDB:
            case INGRES:
            case ORACLE:
            case SYBASE: {
                result = create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()
                        .of(TAuthor_LAST_NAME(), TAuthor_FIRST_NAME())
                        .fetch();
                assertEquals(2, result.size());

                result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor_LAST_NAME(), TAuthor_FIRST_NAME())
                        .fetch();
                assertEquals(2, result2.size());

                // NO BREAK: Fall through to POSTGRES
            }

            // Postgres only supports the OF clause with tables as parameters
            case POSTGRES: {
                result = create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()
                        .of(TAuthor())
                        .fetch();
                assertEquals(2, result.size());

                result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor())
                        .fetch();
                assertEquals(2, result2.size());

                break;
            }
        }

        // Only few dialects support the FOR SHARE clause:
        switch (getDialect()) {
            case MYSQL:
            case POSTGRES: {
                result = create().select(TAuthor_ID())
                                 .from(TAuthor())
                                 .forShare()
                                 .fetch();
                assertEquals(2, result.size());

                result2 = create().selectFrom(TAuthor())
                                  .forShare()
                                  .fetch();
                assertEquals(2, result2.size());
                break;
            }

            default:
                log.info("SKIPPING", "FOR SHARE clause tests");
        }
    }
}
