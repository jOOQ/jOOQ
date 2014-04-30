/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
// ...
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.selectZero;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.Vector;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class SelectTests<
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

    public SelectTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testSelectSimpleQuery() throws Exception {
        SelectQuery<?> q = create().selectQuery();
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
        assertEquals(3, result.fieldsRow().size());
        assertTrue(asList(result.fields()).contains(f1));
        assertTrue(asList(result.fields()).contains(f2));
        assertTrue(asList(result.fields()).contains(f3));

        assertEquals(3, result.get(0).fieldsRow().size());
        assertTrue(asList(result.get(0).fields()).contains(f1));
        assertTrue(asList(result.get(0).fields()).contains(f2));
        assertTrue(asList(result.get(0).fields()).contains(f3));

        assertEquals(Integer.valueOf(1), result.get(0).getValue(f1));
        assertEquals(2d, result.get(0).getValue(f2), 0.0);
        assertEquals("test", result.get(0).getValue(f3));
    }

    public void testSelectQuery() throws Exception {
        SelectQuery<?> q = create().selectQuery();
        q.addFrom(TAuthor());
        q.addSelect(TAuthor().fields());
        q.addOrderBy(TAuthor_LAST_NAME());

        int rows = q.execute();
        Result<?> result = q.getResult();

        assertEquals(2, rows);
        assertEquals(2, result.size());
        assertEquals("Coelho", result.get(0).getValue(TAuthor_LAST_NAME()));
        assertEquals("Orwell", result.get(1).getValue(TAuthor_LAST_NAME()));
    }

    public void testDistinctQuery() throws Exception {
        Result<Record1<Integer>> result = create()
            .selectDistinct(TBook_AUTHOR_ID())
            .from(TBook())
            .orderBy(TBook_AUTHOR_ID())
            .fetch();

        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(1), result.get(0).getValue(TBook_AUTHOR_ID()));
        assertEquals(Integer.valueOf(2), result.get(1).getValue(TBook_AUTHOR_ID()));

        assertEquals(2, create()
            .selectDistinct(TBook_AUTHOR_ID())
            .from(TBook())
            .fetch()
            .size());
    }

    public void testSubSelect() throws Exception {

        // ---------------------------------------------------------------------
        // [#493, #632] Testing filtering by a select's outcome
        // ---------------------------------------------------------------------

        // TODO [#632] Handle this for Sybase
        assertEquals("Coelho",
        create().select(TAuthor_LAST_NAME())
                .from(TAuthor())
                .where(val(0).equal(
                              select(count(TBook_ID()))
                             .from(TBook())
                             .where(TBook_AUTHOR_ID().equal(TAuthor_ID()))
                             .and(TBook_TITLE().equal("1984"))))

                // SQLite doesn't support {=|<>|<|>|<=|>=} {ANY|ALL|SOME}, which
                // is checked in PredicateTests. But do check simpler subqueries

                .and(val(100).notEqual(selectOne()))
                .and(val(0).lessThan(selectOne()))
                .and(val(1).lessOrEqual(selectOne()))
                .and(val(1).greaterThan(selectZero()))
                .and(val(0).greaterOrEqual(selectZero()))
                .limit(1)
                .fetchOne(TAuthor_LAST_NAME()));
    }

    public void testSelectWithINPredicate() throws Exception {
        assertEquals(3,
            create().selectFrom(TBook())
                .where(TBook_TITLE().notIn(
                    select(TBook_TITLE())
                    .from(TBook())
                    .where(TBook_TITLE().in("1984"))))
                .execute());
    }

    public void testSelectWithExistsPredicate() throws Exception {
        assertEquals(3,
            create()
                .selectFrom(TBook())
                .whereNotExists(
                    selectOne()
                    .from(TAuthor())
                    .where(TAuthor_YEAR_OF_BIRTH().greaterOrEqual(TBook_PUBLISHED_IN())))

                // Add additional useless queries to check query correctness
                .orNotExists(select())
                .andExists(select()).execute());
    }

    public void testSelectFromSelect() throws Exception {
        Table<Record2<Integer, Integer>> nested = create().select(TBook_AUTHOR_ID(), count().as("books"))
            .from(TBook())
            .groupBy(TBook_AUTHOR_ID()).asTable("nested");

        Result<Record> records = create().select(nested.fields())
            .from(nested)
            .orderBy(nested.field("books"), nested.field(TBook_AUTHOR_ID())).fetch();

        assertEquals(2, records.size());
        assertEquals(Integer.valueOf(1), records.getValue(0, nested.field(TBook_AUTHOR_ID())));
        assertEquals(Integer.valueOf(2), records.getValue(0, nested.field("books")));
        assertEquals(Integer.valueOf(2), records.getValue(1, nested.field(TBook_AUTHOR_ID())));
        assertEquals(Integer.valueOf(2), records.getValue(1, nested.field("books")));
    }

    public void testSelectWithSubselectProjection() throws Exception {
        Field<Object> books = create().select(count())
                .from(TBook())
                .where(TBook_AUTHOR_ID().equal(TAuthor_ID())).asField("books");

        Result<Record2<Integer, Object>> records = create().select(TAuthor_ID(), books)
                          .from(TAuthor())
                          .orderBy(books, TAuthor_ID()).fetch();

        assertEquals(2, records.size());
        assertEquals(Integer.valueOf(1), records.getValue(0, TAuthor_ID()));
        assertEquals(Integer.valueOf(2), records.getValue(0, books));
        assertEquals(Integer.valueOf(2), records.getValue(1, TAuthor_ID()));
        assertEquals(Integer.valueOf(2), records.getValue(1, books));
    }

    public void testUnaliasedSubqueryProjections() throws Exception {
        // TODO [#579] re-enable this test when fixing this bug

        // Test whether unaliased literals in subquery projections are correctly
        // handled
        Result<Record> result1 =
        create().select()
                .from(
                    selectOne(),
                    select(val(2)),
                    select(val(2)),
                    select(val(2)),
                    select(val(3).add(4)),
                    select(val(3).add(4)),
                    select(trim(" test ")),
                    select(trim(" test ")))
                .fetch();

        assertEquals(1, result1.size());
        assertEquals(Integer.valueOf(1), result1.getValue(0, 0));
        assertEquals(Integer.valueOf(2), result1.getValue(0, 1));
        assertEquals(Integer.valueOf(2), result1.getValue(0, val(2)));
        assertEquals(Integer.valueOf(2), result1.getValue(0, 3));
        assertEquals(Integer.valueOf(7), result1.getValue(0, val(3).add(4)));
        assertEquals(Integer.valueOf(7), result1.getValue(0, 5));
        assertEquals("test", result1.getValue(0, trim(" test ")));
        assertEquals("test", result1.getValue(0, 7));

        Result<Record8<Object, Object, Object, Object, Object, Object, Object, Object>> result2 =
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

        assertEquals(1, result2.size());
        assertEquals(1, result2.getValue(0, 0));
        assertEquals(2, result2.getValue(0, 1));
        assertEquals(2, result2.getValue(0, 2));
        assertEquals(2, result2.getValue(0, 3));
        assertEquals(7, result2.getValue(0, 4));
        assertEquals(7, result2.getValue(0, 5));
        assertEquals("test", result2.getValue(0, 6));
        assertEquals("test", result2.getValue(0, 7));
    }

    public void testCombinedSelectQuery() throws Exception {
        SelectQuery<B> q1 = create().selectQuery(TBook());
        SelectQuery<B> q2 = create().selectQuery(TBook());

        q1.addConditions(TBook_AUTHOR_ID().equal(1));
        q2.addConditions(TBook_TITLE().equal("Brida"));

        // Use union all because of clob's
        Select<?> union = q1.unionAll(q2);
        int rows = union.execute();
        assertEquals(3, rows);

        // Use union all because of clob's
        rows = create().selectDistinct(union.field(TBook_AUTHOR_ID()), TAuthor_FIRST_NAME())
            .from(union)
            .join(TAuthor())
            .on(union.field(TBook_AUTHOR_ID()).equal(TAuthor_ID()))
            .orderBy(TAuthor_FIRST_NAME())
            .execute();

        assertEquals(2, rows);
    }

    public void testComplexUnions() throws Exception {
        Select<Record1<String>> s1 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(1));
        Select<Record1<String>> s2 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(2));
        Select<Record1<String>> s3 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(3));
        Select<Record1<String>> s4 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(4));

        Result<Record> result = create().select().from(s1.union(s2).union(s3).union(s4)).fetch();
        assertEquals(4, result.size());

        result = create().select().from(s1.union(s2).union(s3.union(s4))).fetch();
        assertEquals(4, result.size());

        assertEquals(4, create().selectFrom(s1.union(
                            create().selectFrom(s2.unionAll(
                                create().selectFrom(s3.union(s4).asTable())
                            ).asTable())
                        ).asTable())
                                    .fetch().size());

        // [#289] Handle bad syntax scenario provided by user Gunther
        Select<Record1<Integer>> q = create().select(val(2008).as("y"));
        for (int year = 2009; year <= 2011; year++) {
            q = q.union(create().select(val(year).as("y")));
        }

        assertEquals(4, q.execute());
    }

    public void testForUpdateClauses() throws Exception {
        switch (dialect().family()) {
            /* [pro] xx
            xxxx xxxxxxx
            xx [/pro] */
            case SQLITE:
                log.info("SKIPPING", "FOR UPDATE tests");
                return;
        }

        // Checking for syntax correctness and locking behaviour
        // -----------------------------------------------------
        final DSLContext create1 = create();
        final DSLContext create2 = create();

        ((DefaultConnectionProvider) create2.configuration().connectionProvider()).setConnection(getNewConnection());
        create2.configuration().connectionProvider().acquire().setAutoCommit(false);

        final Vector<String> execOrder = new Vector<String>();

        try {
            final Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(2000);
                    execOrder.add("t1-block");
                    try {
                        create1
                            .select(TAuthor_ID())
                            .from(TAuthor())
                            .forUpdate()
                            .fetch();
                    }

                    // Some databases fail on locking, others lock for a while
                    catch (DataAccessException ignore) {
                    }
                    finally {
                        execOrder.add("t1-fail-or-t2-commit");
                    }
                }
            });

            final Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    execOrder.add("t2-exec");
                    Result<A> result2 = create2
                        .selectFrom(TAuthor())
                        .forUpdate()
                        .fetch();
                    assertEquals(2, result2.size());

                    execOrder.add("t2-signal");
                    sleep(4000);
                    execOrder.add("t1-fail-or-t2-commit");

                    try {
                        create2.configuration().connectionProvider().acquire().commit();
                        create2.configuration().connectionProvider().acquire().close();
                    }
                    catch (Exception e) {}
                }
            });

            // This is the test case:
            // 0.0s: Both threads start
            // 0.0s: t1 sleeps for 2s
            // 0.0s: t2 locks the T_AUTHOR table
            // 0.1s: t2 sleeps for 4s
            // 2.0s: t1 blocks on the T_AUTHOR table
            // ???s: t1 fails
            // 4.0s: t2 commits and unlocks T_AUTHOR
            t1.start();
            t2.start();

            t1.join();
            t2.join();

            assertEquals(asList("t2-exec", "t2-signal", "t1-block", "t1-fail-or-t2-commit", "t1-fail-or-t2-commit"), execOrder);
        }
        finally {
            try {
                create2.configuration().connectionProvider().acquire().close();
            }
            catch (Exception e) {}
        }

        // Check again with limit / offset clauses
        // ---------------------------------------
        switch (dialect().family()) {
            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxxxxx
                xxxxxxxxxxxxxxxxxxxx xxxxxx xx xxxxxx xx xxx xxxxxxxxx
                xxxxxx

            xx [/pro] */
            default: {
                Result<Record1<Integer>> result3 = create()
                    .select(TAuthor_ID())
                    .from(TAuthor())
                    .limit(5)
                    .offset(0)
                    .forUpdate()
                    .fetch();
                assertEquals(2, result3.size());
                Result<A> result4 = create().selectFrom(TAuthor())
                                            .limit(5)
                                            .offset(0)
                                            .forUpdate()
                                            .fetch();
                assertEquals(2, result4.size());
            }
        }

        switch (dialect().family()) {
            /* [pro] xx
            xxxx xxxx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxx
            xx [/pro] */
            case DERBY:
            case FIREBIRD:
            case HSQLDB:
            case H2:
            case MARIADB:
            case MYSQL:
                log.info("SKIPPING", "FOR UPDATE .. WAIT/NOWAIT tests");
                break;

            /* [pro] xx
            xxxx xxxxxxx
            xx [/pro] */
            case POSTGRES:
            default: {
                Result<Record1<Integer>> r1a = create()
                    .select(TAuthor_ID())
                    .from(TAuthor())
                    .forUpdate()
                    .noWait()
                    .fetch();
                assertEquals(2, r1a.size());

                /* [pro] xx
                xx xxxxxxxxxxxxxxxxxxx xx xxxxxxx x
                    xxxxxxxxxxxxxxxxxxxxxxxx xxx x xxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxx
                        xxxxxxxxxxxx
                        xxxxxxxx
                        xxxxxxxxx
                    xxxxxxxxxxxxxxx xxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxx xxx x xxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxx
                        xxxxxxxxxxxx
                        xxxxxxxxxxxxx
                        xxxxxxxxx
                    xxxxxxxxxxxxxxx xxxxxxxxxxxx

                    xxxxxxxxx xxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                            xxxxxxxxxxxx
                            xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxx
                            xxxxxxxx
                            xxxxxxxxx
                    xxxxxxxxxxxxxxx xxxxxxxxxxxx
                    xxxxxxxxx xxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                            xxxxxxxxxxxx
                            xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxx
                            xxxxxxxxx
                            xxxxxxxxx
                    xxxxxxxxxxxxxxx xxxxxxxxxxxx
                    xxxxxxxxx xxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                            xxxxxxxxxxxx
                            xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxx
                            xxxxxxxxxxxxx
                            xxxxxxxxx
                    xxxxxxxxxxxxxxx xxxxxxxxxxxx
                x
                xx [/pro] */
            }
        }

        switch (dialect().family()) {
            case MARIADB:
            case MYSQL:
            case POSTGRES:
                log.info("SKIPPING", "FOR UPDATE OF tests");
                break;

            // Most dialects support the OF clause
            default: {
                Result<Record1<Integer>> result =
                create().select(TAuthor_ID())
                        .from(TAuthor())
                        .forUpdate()

                        // DB2 requires a key column to be contained in the
                        // FOR UPDATE OF ... clause
                        .of(TAuthor_ID(),
                            TAuthor_LAST_NAME(),
                            TAuthor_FIRST_NAME())
                        .fetch();
                assertEquals(2, result.size());

                Result<A> result2 =
                create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor_ID(),
                            TAuthor_LAST_NAME(),
                            TAuthor_FIRST_NAME())
                        .fetch();
                assertEquals(2, result2.size());

                // NO BREAK: Fall through to POSTGRES
            }
        }

        switch (dialect()) {
            /* [pro] xx
            xxxx xxxx xx xxxx xxxxxx xxxxxxxx xxxxx xxx xxxxxxx xxx
            xx [/pro] */
            case MARIADB:
            case MYSQL:
                log.info("SKIPPING", "FOR UPDATE OF tests");
                break;

            // Postgres only supports the OF clause with tables as parameters
            case POSTGRES:
            default: {
                Result<Record1<Integer>> result = create()
                    .select(TAuthor_ID())
                    .from(TAuthor())
                    .forUpdate()
                    .of(TAuthor())
                    .fetch();
                assertEquals(2, result.size());

                Result<A> result2 = create().selectFrom(TAuthor())
                        .forUpdate()
                        .of(TAuthor())
                        .fetch();
                assertEquals(2, result2.size());

                break;
            }
        }

        // Only few dialects support the FOR SHARE clause:
        switch (dialect()) {
            case MARIADB:
            case MYSQL:
            case POSTGRES: {
                Result<Record1<Integer>> result = create()
                    .select(TAuthor_ID())
                    .from(TAuthor())
                    .forShare()
                    .fetch();
                assertEquals(2, result.size());

                Result<A> result2 = create().selectFrom(TAuthor())
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
