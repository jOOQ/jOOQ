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
import static java.util.Collections.nCopies;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.avgDistinct;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.cumeDist;
import static org.jooq.impl.DSL.denseRank;
import static org.jooq.impl.DSL.firstValue;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.lag;
import static org.jooq.impl.DSL.lead;
import static org.jooq.impl.DSL.listAgg;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.maxDistinct;
import static org.jooq.impl.DSL.median;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.minDistinct;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.ntile;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.percentRank;
import static org.jooq.impl.DSL.rank;
import static org.jooq.impl.DSL.regrAvgX;
import static org.jooq.impl.DSL.regrAvgY;
import static org.jooq.impl.DSL.regrCount;
import static org.jooq.impl.DSL.regrIntercept;
import static org.jooq.impl.DSL.regrR2;
import static org.jooq.impl.DSL.regrSXX;
import static org.jooq.impl.DSL.regrSXY;
import static org.jooq.impl.DSL.regrSYY;
import static org.jooq.impl.DSL.regrSlope;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.stddevPop;
import static org.jooq.impl.DSL.stddevSamp;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.sumDistinct;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.varPop;
import static org.jooq.impl.DSL.varSamp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record9;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.WindowDefinition;
import org.jooq.WindowSpecification;
import org.jooq.impl.DSL;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class AggregateWindowFunctionTests<
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
    UU   extends UpdatableRecord<UU>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public AggregateWindowFunctionTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testSelectCountQuery() throws Exception {
        assertEquals(4, create().selectCount().from(TBook()).fetchOne(0));
        assertEquals(2, create().selectCount().from(TAuthor()).fetchOne(0));
    }

    public void testUserDefinedAggregateFunctions() throws Exception {
        if (secondMax(null) == null) {
            log.info("SKIPPING", "User-defined aggregate function tests");
            return;
        }

        // Check the correctness of the aggregate function
        List<Integer> result1 =
        create().select(secondMax(TBook_ID()))
                .from(TBook())
                .groupBy(TBook_AUTHOR_ID())
                .orderBy(TBook_AUTHOR_ID().asc())
                .fetch(0, Integer.class);

        assertEquals(asList(1, 3), result1);

        /* [pro] xx
        xxxxxx xxxxxxxxxxxxxxxxxxxx x
            xxxx xxxxxxx x
                xx xxxxx xxx xxxxxxxxxxx xx xxx xxxxxxxxxx xxxxxxxx
                xxxxxxxxxxxxx xxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxx xxxxxxxxxxxxxxx

                xxxxxxxxxxxxxxxxxxxxxx xx xx xxx xxxxxxxxx

                xx xxxxxxx xxxxx xx xxxxx xxxxxxxxxx xxx xxxxxxxxx xxxxxxxx xxxxxx xxx
                xxxxxxxxxx xxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx

                xxxxxxxxxxxxx xxxxxxx x
                xxxxxx  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                        xxxxxxxxx xxxxxxxxxxxxxxx

                xxxxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxx
                xxxxxx
            x
        x
        xx xxxxx xx
    x

    xxxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxxx x

        xx xxxxxxxx xxxxxxxxx xxxxxxxxxx xxxxxxxxx xx xxx xxxxxxxxx
        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxx xxxxxx x xxxxxxxxxxxxxxxxxxx

        xx xxxx xxxxxxxx xxxxx xxxxxxx x xxxxxx xxxxxxxx xx x xxxxxxxxxx xxxxxxx
        xx xxx xxx xxxxxxxx xx xx xxxx xxxxxxx xxx xxxxxx xx xxxxxx xxx xxx
        xx xxx xxx xxxx
        xxxxxx xxxxxxxxxxxxxxxxxxxx x
            xx xxxxx xx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxxxxxxxx
            xx [/pro] */

            case DERBY:
            case FIREBIRD:
            case H2:
            case MARIADB:
            case MYSQL:
            case SQLITE:

            // TODO [#871] This could be simulated
            case POSTGRES:
                median = avg(TBook_ID());
                break;
        }

        Result<Record9<Integer, Integer, Integer, Integer, BigDecimal, BigDecimal, Integer, Integer, BigDecimal>> result1 = create()
            .select(
                TBook_AUTHOR_ID(),
                count(),
                count(TBook_ID()),
                countDistinct(TBook_AUTHOR_ID()),
                sum(TBook_ID()),
                avg(TBook_ID()),
                min(TBook_ID()),
                max(TBook_ID()),
                median)
            .from(TBook())
            .groupBy(TBook_AUTHOR_ID())
            .orderBy(TBook_AUTHOR_ID())
            .fetch();

        assertEquals(2, (int) result1.get(0).value2());
        assertEquals(2, (int) result1.get(0).value3());
        assertEquals(1, (int) result1.get(0).value4());
        assertEquals(3d, result1.get(0).getValue(4, Double.class), 0.0);
        assertEquals(1, (int) result1.get(0).value7());
        assertEquals(2, (int) result1.get(0).value8());

        assertEquals(2, (int) result1.get(1).value2());
        assertEquals(2, (int) result1.get(1).value3());
        assertEquals(1, (int) result1.get(1).value4());
        assertEquals(7d, result1.get(1).getValue(4, Double.class), 0.0);
        assertEquals(3, (int) result1.get(1).value7());
        assertEquals(4, (int) result1.get(1).value8());

        // TODO [#868] Derby, HSQLDB, and SQL Server perform rounding/truncation
        // This may need to be corrected by jOOQ
        assertTrue(asList(1.0, 1.5, 2.0).contains(result1.get(0).getValue(5, Double.class)));
        assertTrue(asList(1.0, 1.5, 2.0).contains(result1.get(0).getValue(8, Double.class)));
        assertTrue(asList(3.0, 3.5, 4.0).contains(result1.get(1).getValue(5, Double.class)));
        assertTrue(asList(3.0, 3.5, 4.0).contains(result1.get(1).getValue(8, Double.class)));

        // [#1042] DISTINCT keyword
        // ------------------------

        // DB2 doesn't support multiple DISTINCT keywords in the same query...
        int distinct1 = create().select(countDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        int distinct2 = create().select(minDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        int distinct3 = create().select(maxDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        int distinct4 = create().select(sumDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Integer.class);
        double distinct5 = create().select(avgDistinct(TBook_AUTHOR_ID())).from(TBook()).fetchOne(0, Double.class);

        assertEquals(2, distinct1);
        assertEquals(1, distinct2);
        assertEquals(2, distinct3);
        assertEquals(3, distinct4);
        // TODO [#868] Derby, HSQLDB, and SQL Server perform rounding/truncation
        // This may need to be corrected by jOOQ
        assertTrue(asList(1.0, 1.5, 2.0).contains(distinct5));

        // Statistical aggregate functions, available in some dialects:
        // ------------------------------------------------------------
        switch (dialect()) {
            case DERBY:
            case FIREBIRD:
            case SQLITE:
                log.info("SKIPPING", "Statistical aggregate functions");
                break;

            default: {
                Result<Record5<Integer, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> result2 = create()
                    .select(
                        TBook_AUTHOR_ID(),
                        stddevPop(TBook_ID()),
                        stddevSamp(TBook_ID()),
                        varPop(TBook_ID()),
                        varSamp(TBook_ID()))
                    .from(TBook())
                    .groupBy(TBook_AUTHOR_ID())
                    .orderBy(TBook_AUTHOR_ID())
                    .fetch();

                assertEquals(0.5, result2.get(0).getValue(1, Double.class), 0.0);
                assertEquals(0.25, result2.get(0).getValue(3, Double.class), 0.0);
                assertEquals(0.5, result2.get(1).getValue(1, Double.class), 0.0);
                assertEquals(0.25, result2.get(1).getValue(3, Double.class), 0.0);

                // DB2 only knows STDDEV_POP / VAR_POP
                if (true/* [pro] xx xx xxxxxxxxx xx xxxxxxxxxxxxxxxx [/pro] */) {
                    assertEquals("0.707", result2.get(0).getValue(2, String.class).substring(0, 5));
                    assertEquals(0.5, result2.get(0).getValue(4, Double.class), 0.0);
                    assertEquals("0.707", result2.get(1).getValue(2, String.class).substring(0, 5));
                    assertEquals(0.5, result2.get(1).getValue(4, Double.class), 0.0);
                }
            }
        }

        // [#873] Duplicate functions
        // --------------------------
        Result<Record3<Integer, Integer, Integer>> result3 =
        create().select(
                    TBook_AUTHOR_ID(),
                    max(TBook_ID()),
                    max(TBook_ID()))
                .from(TBook())
                .groupBy(TBook_AUTHOR_ID())
                .orderBy(TBook_AUTHOR_ID())
                .fetch();

        assertEquals(2, (int) result3.get(0).getValue(1, Integer.class));
        assertEquals(2, (int) result3.get(0).getValue(2, Integer.class));
        assertEquals(4, (int) result3.get(1).getValue(1, Integer.class));
        assertEquals(4, (int) result3.get(1).getValue(2, Integer.class));
    }

    public void testFetchCount() throws Exception {
        assertEquals(1, create().fetchCount(select(one().as("x"))));
        assertEquals(1, create().select(one().as("x")).fetchCount());

        // [#2759] T-SQL databases need to render derived column lists, here!
        assertEquals(1, create().fetchCount(selectOne()));
        assertEquals(1, create().selectOne().fetchCount());

        assertEquals(4, create().fetchCount(select(TBook_ID(), TBook_TITLE()).from(TBook())));
        assertEquals(4, create().select(TBook_ID(), TBook_TITLE()).from(TBook()).fetchCount());

        assertEquals(3, create().fetchCount(selectDistinct(TBook_ID(), TBook_TITLE()).from(TBook()).where(TBook_ID().in(1, 2, 3))));

        assertEquals(2, create().fetchCount(
            select(TBook_TITLE()).from(TBook()).where(TBook_ID().eq(1))
            .union(
            select(inline("abc")))));
    }

    public void testFetchCountWithLimitOffset() throws Exception {

        /* [pro] xx
        xxxxxx xxxxxxxxxxxxxxxxxxxx x
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxxxxx
                xxxxxxxxxxxxxxxxxxxx xxxxxxx xxxxx xx xxxxxx xxxxxxxx
                xxxxxxx
        x
        xx [/pro] */

        // Some databases don't allow for LIMIT .. OFFSET in nested selects or derived tables.
        if (!asList().contains(dialect().family())) {
            assertEquals(2, create().fetchCount(selectFrom(TBook()).limit(2)));
            assertEquals(2, create().fetchCount(selectFrom(TBook()).limit(2).offset(1)));
        }
    }

    public void testCountDistinct() throws Exception {

        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxx xx xxxxxxx x
            xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx xxxx xxxxxxxx
            xxxxxxx
        x
        xx [/pro] */

        assertEquals(2, create()
            .select(countDistinct(TBook_AUTHOR_ID()))
            .from(TBook())
            .fetchOne(0));

        // [#1728] COUNT(DISTINCT expr1, expr2, ...)
        // -----------------------------------------
        if (asList(CUBRID, DERBY, FIREBIRD, H2, SQLITE).contains(dialect().family())) {
            log.info("SKIPPING", "Multi-expression COUNT(DISTINCT) test");
        }
        else {
            assertEquals(3, (int)
            create().select(countDistinct(TBook_AUTHOR_ID(), TBook_LANGUAGE_ID()))
                    .from(TBook())
                    .fetchOne(0, Integer.class));
        }
    }

    public void testLinearRegressionFunctions() throws Exception {
        switch (dialect().family()) {
            /* [pro] xx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxx
            xx [/pro] */
            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case SQLITE:
                log.info("SKIPPING", "Skipping linear regression function tests");
                return;
        }

        // [#600] As aggregate functions
        Record record =
        create().select(
                    regrAvgX(TBook_ID(), TBook_AUTHOR_ID()),
                    regrAvgY(TBook_ID(), TBook_AUTHOR_ID()),
                    regrCount(TBook_ID(), TBook_AUTHOR_ID()),
                    regrIntercept(TBook_ID(), TBook_AUTHOR_ID()),
                    regrR2(TBook_ID(), TBook_AUTHOR_ID()),
                    regrSlope(TBook_ID(), TBook_AUTHOR_ID()),
                    regrSXX(TBook_ID(), TBook_AUTHOR_ID()),
                    regrSXY(TBook_ID(), TBook_AUTHOR_ID()),
                    regrSYY(TBook_ID(), TBook_AUTHOR_ID()))
                .from(TBook())
                .fetchOne();

        List<String> values = Arrays.asList("1.5", "2.5", "4.0", "-0.5", "0.8", "2.0", "1.0", "2.0", "5.0");
        assertEquals(values, Arrays.asList(roundStrings(1, record.into(String[].class))));

        /* [pro] xx
        xxxxxx xxxxxxxxxxx x
            xxxx xxxx
                xxxxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxx xxxxxxxxxx xxxxxx xxxxxxxx xxxxxxxx
                xxxxxxx
        x
        xx [/pro] */

        // [#600] As window functions
        Result<Record9<BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal, BigDecimal>> result =
        create().select(
                    regrAvgX(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrAvgY(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrCount(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrIntercept(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrR2(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrSlope(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrSXX(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrSXY(TBook_ID(), TBook_AUTHOR_ID()).over(),
                    regrSYY(TBook_ID(), TBook_AUTHOR_ID()).over())
                .from(TBook())
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(values, Arrays.asList(roundStrings(1, result.get(0).into(String[].class))));
        assertEquals(values, Arrays.asList(roundStrings(1, result.get(1).into(String[].class))));
        assertEquals(values, Arrays.asList(roundStrings(1, result.get(2).into(String[].class))));
        assertEquals(values, Arrays.asList(roundStrings(1, result.get(3).into(String[].class))));
    }

    public void testWindowFunctions() throws Exception {
        switch (dialect()) {
            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxxxxx
            xx [/pro] */
            case FIREBIRD:
            case MARIADB:
            case MYSQL:
            case SQLITE:
                log.info("SKIPPING", "Window function tests");
                return;
        }

        switch (dialect()) {
            case DERBY:
            case H2:
            case HSQLDB:

                // [#1535] TODO: Move this out of the switch statement. Oracle
                // and other databases should be able to support ORDER-BY-less
                // OVER() clauses for ranking functions

                // [#1523] Derby, H2 now support the ROW_NUMBER() OVER() window function
                // without any window clause, though. HSQLDB can simulate it using ROWNUM()
                List<Integer> rows =
                create().select(rowNumber().over()).from(TBook()).orderBy(TBook_ID()).fetch(0, Integer.class);
                assertEquals(asList(1, 2, 3, 4), rows);

                log.info("SKIPPING", "Advanced window function tests");
                return;
        }

        int column = 0;

        // ROW_NUMBER()
        Result<?> result =
        create().select(TBook_ID(),
// [#1535] TODO:        rowNumber().over(),

                        // [#1958] Check if expressions in ORDER BY clauess work
                        // correctly for all databases
                        rowNumber().over()
                                   .partitionByOne()
                                   .orderBy(
                                       TBook_ID().mul(2).desc(),
                                       TBook_ID().add(1).desc()),
                        rowNumber().over()
                                   .partitionBy(TBook_AUTHOR_ID())
                                   .orderBy(TBook_ID().desc()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

//        // [#1535] No ORDER BY clause
//        column++;
//        assertEquals(BOOK_IDS, result.getValues(column));

        // Ordered ROW_NUMBER()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(3), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Partitioned and ordered ROW_NUMBER()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(1), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        column = 0;

        // COUNT()
        result =
        create().select(TBook_ID(),
                        count().over(),
                        count().over().partitionBy(TBook_AUTHOR_ID()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Partitioned and ordered COUNT()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(4), result.getValue(1, column));
        assertEquals(Integer.valueOf(4), result.getValue(2, column));
        assertEquals(Integer.valueOf(4), result.getValue(3, column));

        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(2), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(2), result.getValue(3, column));

        column = 0;

        // RANK(), DENSE_RANK()
        result =
        create().select(TBook_ID(),
                        rank().over().orderBy(TBook_ID().desc()),
                        rank().over().partitionBy(TBook_AUTHOR_ID())
                                     .orderBy(TBook_ID().desc()),
                        denseRank().over().orderBy(TBook_ID().desc()),
                        denseRank().over().partitionBy(TBook_AUTHOR_ID())
                                          .orderBy(TBook_ID().desc()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Ordered RANK()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(3), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Partitioned and ordered RANK()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(1), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Ordered DENSE_RANK()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(3), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        // Partitioned and ordered DENSE_RANK()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(1), result.getValue(1, column));
        assertEquals(Integer.valueOf(2), result.getValue(2, column));
        assertEquals(Integer.valueOf(1), result.getValue(3, column));

        switch (dialect()) {
            /* [pro] xx
            xxxx xxxx
                xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxx xxx xxxxxxxxxxx xxxxxx xxxxxxxx xxxxxxxx
                xxxxxx
            xx [/pro] */

            default: {
                column = 0;

                // PERCENT_RANK() and CUME_DIST()
                result =
                create().select(TBook_ID(),
                                percentRank().over().orderBy(TBook_ID().desc()),
                                percentRank().over().partitionBy(TBook_AUTHOR_ID())
                                                    .orderBy(TBook_ID().desc()),
                                cumeDist().over().orderBy(TBook_ID().desc()),
                                cumeDist().over().partitionBy(TBook_AUTHOR_ID())
                                                 .orderBy(TBook_ID().desc()))
                        .from(TBook())
                        .orderBy(TBook_ID().asc())
                        .fetch();

                // Ordered PERCENT_RANK()
                column++;
                assertEquals("1", result.get(0).getValue(column, String.class).substring(0, 1));
                assertEquals("0.6", result.get(1).getValue(column, String.class).substring(0, 3));
                assertEquals("0.3", result.get(2).getValue(column, String.class).substring(0, 3));
                assertEquals("0", result.get(3).getValue(column, String.class).substring(0, 1));

                // Partitioned and ordered PERCENT_RANK()
                column++;
                assertEquals("1", result.get(0).getValue(column, String.class).substring(0, 1));
                assertEquals("0", result.get(1).getValue(column, String.class).substring(0, 1));
                assertEquals("1", result.get(2).getValue(column, String.class).substring(0, 1));
                assertEquals("0", result.get(3).getValue(column, String.class).substring(0, 1));

                // Ordered CUME_DIST()
                column++;
                assertEquals("1", result.get(0).getValue(column, String.class).substring(0, 1));
                assertEquals("0.75", result.get(1).getValue(column, String.class));
                assertEquals("0.5", result.get(2).getValue(column, String.class));
                assertEquals("0.25", result.get(3).getValue(column, String.class));

                // Partitioned and ordered CUME_DIST()
                column++;
                assertEquals("1", result.get(0).getValue(column, String.class).substring(0, 1));
                assertEquals("0.5", result.get(1).getValue(column, String.class));
                assertEquals("1", result.get(2).getValue(column, String.class).substring(0, 1));
                assertEquals("0.5", result.get(3).getValue(column, String.class));

                break;
            }
        }

        column = 0;

        // MAX()
        result =
        create().select(TBook_ID(),
                        max(TBook_ID()).over()
                                       .partitionByOne(),
                        max(TBook_ID()).over()
                                       .partitionBy(TBook_AUTHOR_ID()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Overall MAX()
        column++;
        assertEquals(Integer.valueOf(4), result.getValue(0, column));
        assertEquals(Integer.valueOf(4), result.getValue(1, column));
        assertEquals(Integer.valueOf(4), result.getValue(2, column));
        assertEquals(Integer.valueOf(4), result.getValue(3, column));

        // Partitioned MAX()
        column++;
        assertEquals(Integer.valueOf(2), result.getValue(0, column));
        assertEquals(Integer.valueOf(2), result.getValue(1, column));
        assertEquals(Integer.valueOf(4), result.getValue(2, column));
        assertEquals(Integer.valueOf(4), result.getValue(3, column));

        column = 0;

        // STDDEV_POP(), STDDEV_SAMP(), VAR_POP(), VAR_SAMP()
        result =
        create().select(TBook_ID(),
                        stddevPop(TBook_ID()).over().partitionByOne(),
                        stddevSamp(TBook_ID()).over().partitionByOne(),
                        varPop(TBook_ID()).over().partitionByOne(),
                        varSamp(TBook_ID()).over().partitionByOne(),

                        stddevPop(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        stddevSamp(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        varPop(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                        varSamp(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

        // Overall STDDEV_POP(), STDDEV_SAMP(), VAR_POP(), VAR_SAMP()
        assertEquals("1.118", result.get(0).getValue(1, String.class).substring(0, 5));
        assertEquals(1.25, result.get(0).getValue(3, Double.class), 0.0);

        // Partitioned STDDEV_POP(), STDDEV_SAMP(), VAR_POP(), VAR_SAMP()
        assertEquals(0.5, result.get(0).getValue(5, Double.class), 0.0);
        assertEquals(0.25, result.get(0).getValue(7, Double.class), 0.0);

        // DB2 only knows STDDEV_POP / VAR_POP
        if (true/* [pro] xx xx xxxxxxxxx xx xxxxxxxxxxxxxxxx [/pro] */) {
            assertEquals("1.290", result.get(0).getValue(2, String.class).substring(0, 5));
            assertEquals("1.666", result.get(0).getValue(4, String.class).substring(0, 5));
            assertEquals("0.707", result.get(0).getValue(6, String.class).substring(0, 5));
            assertEquals(0.5, result.get(0).getValue(8, Double.class), 0.0);
        }

        // NTILE()
        /* [pro] xx
        xx xxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxxxx
        x
        xxxx
        xx [/pro] */
        {
            result =
            create().select(TBook_ID(),
                            ntile(1).over().orderBy(TBook_ID()),
                            ntile(1).over().partitionBy(TBook_AUTHOR_ID()).orderBy(TBook_ID()),
                            ntile(2).over().orderBy(TBook_ID()),
                            ntile(2).over().partitionBy(TBook_AUTHOR_ID()).orderBy(TBook_ID()))
                    .from(TBook())
                    .orderBy(TBook_ID().asc())
                    .fetch();

            assertEquals(BOOK_IDS, result.getValues(0));
            assertEquals(nCopies(4, 1), result.getValues(1));
            assertEquals(nCopies(4, 1), result.getValues(2));
            assertEquals(asList(1, 1, 2, 2), result.getValues(3));
            assertEquals(asList(1, 2, 1, 2), result.getValues(4));
        }

        column = 0;
        if (asList(CUBRID).contains(dialect())) {
            log.info("SKIPPING", "ROWS UNBOUNDED PRECEDING and similar tests");
        }
        else {

            // SUM()
            result =
            create().select(TBook_ID(),
                            sum(TBook_ID()).over().partitionByOne(),
                            sum(TBook_ID()).over().partitionBy(TBook_AUTHOR_ID()),
                            sum(TBook_ID()).over().orderBy(TBook_ID().asc())
                                                  .rowsBetweenUnboundedPreceding()
                                                  .andPreceding(1))
                    .from(TBook())
                    .orderBy(TBook_ID().asc())
                    .fetch();

            // Overall SUM()
            column++;
            assertEquals(new BigDecimal("10"), result.getValue(0, column));
            assertEquals(new BigDecimal("10"), result.getValue(1, column));
            assertEquals(new BigDecimal("10"), result.getValue(2, column));
            assertEquals(new BigDecimal("10"), result.getValue(3, column));

            // Partitioned SUM()
            column++;
            assertEquals(new BigDecimal("3"), result.getValue(0, column));
            assertEquals(new BigDecimal("3"), result.getValue(1, column));
            assertEquals(new BigDecimal("7"), result.getValue(2, column));
            assertEquals(new BigDecimal("7"), result.getValue(3, column));

            // Ordered SUM() with ROWS
            column++;
            assertEquals(null, result.getValue(0, column));
            assertEquals(new BigDecimal("1"), result.getValue(1, column));
            assertEquals(new BigDecimal("3"), result.getValue(2, column));
            assertEquals(new BigDecimal("6"), result.getValue(3, column));

            column = 0;

            // FIRST_VALUE()
            result =
            create().select(TBook_ID(),
                            firstValue(TBook_ID()).over()
                                                  .partitionBy(TBook_AUTHOR_ID())
                                                  .orderBy(TBook_PUBLISHED_IN().asc())
                                                  .rowsBetweenUnboundedPreceding()
                                                  .andUnboundedFollowing())
                    .from(TBook())
                    .orderBy(TBook_ID().asc())
                    .fetch();

            // Partitioned and ordered FIRST_VALUE() with ROWS
            column++;
            assertEquals(Integer.valueOf(2), result.getValue(0, column));
            assertEquals(Integer.valueOf(2), result.getValue(1, column));
            assertEquals(Integer.valueOf(3), result.getValue(2, column));
            assertEquals(Integer.valueOf(3), result.getValue(3, column));
        }

        /* [pro] xx
        xxxxxx xxxxxxxxxxxxxxxxxxxx x
            xxxx xxxxxxxxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxx
                xxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx xxxxxx xxxxxx xxxxxx xxxxxxxx xxxxxxx
                xxxxxx

            xxxxxxxx x
                xxxxxx x xx

                xx xxxxxxxxxxxxxxx xxxxxx xxxxxx
                xxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxx
                                         xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                                               xxxxxxx
                                                               xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                                               xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                                               xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                                               xxxxxxxxxxxxxxxxxxxxxxxxx
                                 xxxxxxxxxxxxxx
                                 xxxxxxxxxxxxxxxxxxxxxxxxxx
                                 xxxxxxxxx

                xx xxxxxxxxxxx xxx xxxxxxx xxxxxxxxxxxxxxx xxxxxx xxxxxx xxxx xxxx
                xxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxx xxxxxxxxx

                xxxxxx
            x
        x
        xx [/pro] */

        switch (dialect()) {
            /* [pro] xx
            xxxx xxxxxxx
                xxxxxxxxxxxxxxxxxxxx xxxxxxxxx xxxxxxxx
                xxxxxx

            xx [/pro] */
            default: {
                column = 0;

                // LEAD() and LAG()
                result =
                create().select(TBook_ID(),
                                lead(TBook_ID()).over()
                                                .partitionByOne()
                                                .orderBy(TBook_ID().asc()),
                                lead(TBook_ID()).over()
                                                .partitionBy(TBook_AUTHOR_ID())
                                                .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2).over()
                                                   .partitionByOne()
                                                   .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2).over()
                                                   .partitionBy(TBook_AUTHOR_ID())
                                                   .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2, 55).over()
                                                       .partitionByOne()
                                                       .orderBy(TBook_ID().asc()),
                                lead(TBook_ID(), 2, 55).over()
                                                       .partitionBy(TBook_AUTHOR_ID())
                                                       .orderBy(TBook_ID().asc()),

                                lag(TBook_ID()).over()
                                               .partitionByOne()
                                               .orderBy(TBook_ID().asc()),
                                lag(TBook_ID()).over()
                                               .partitionBy(TBook_AUTHOR_ID())
                                               .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2).over()
                                                  .partitionByOne()
                                                  .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2).over()
                                                  .partitionBy(TBook_AUTHOR_ID())
                                                  .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2, val(55)).over()
                                                           .partitionByOne()
                                                           .orderBy(TBook_ID().asc()),
                                lag(TBook_ID(), 2, val(55)).over()
                                                           .partitionBy(TBook_AUTHOR_ID())
                                                           .orderBy(TBook_ID().asc()))
                        .from(TBook())
                        .orderBy(TBook_ID().asc())
                        .fetch();

                // Overall LEAD()
                column++;
                assertEquals(2, result.getValue(0, column));
                assertEquals(3, result.getValue(1, column));
                assertEquals(4, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Partitioned LEAD()
                column++;
                assertEquals(2, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(4, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Overall LEAD(2)
                column++;
                assertEquals(3, result.getValue(0, column));
                assertEquals(4, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Partitioned LEAD(2)
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Overall LEAD(2, 55)
                column++;
                assertEquals(3, result.getValue(0, column));
                assertEquals(4, result.getValue(1, column));
                assertEquals(55, result.getValue(2, column));
                assertEquals(55, result.getValue(3, column));

                // Partitioned LEAD(2, 55)
                column++;
                assertEquals(55, result.getValue(0, column));
                assertEquals(55, result.getValue(1, column));
                assertEquals(55, result.getValue(2, column));
                assertEquals(55, result.getValue(3, column));


                // Overall LAG()
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(1, result.getValue(1, column));
                assertEquals(2, result.getValue(2, column));
                assertEquals(3, result.getValue(3, column));

                // Partitioned LAG()
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(1, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(3, result.getValue(3, column));

                // Overall LAG(2)
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(1, result.getValue(2, column));
                assertEquals(2, result.getValue(3, column));

                // Partitioned LAG(2)
                column++;
                assertEquals(null, result.getValue(0, column));
                assertEquals(null, result.getValue(1, column));
                assertEquals(null, result.getValue(2, column));
                assertEquals(null, result.getValue(3, column));

                // Overall LAG(2, 55)
                column++;
                assertEquals(55, result.getValue(0, column));
                assertEquals(55, result.getValue(1, column));
                assertEquals(1, result.getValue(2, column));
                assertEquals(2, result.getValue(3, column));

                // Partitioned LAG(2, 55)
                column++;
                assertEquals(55, result.getValue(0, column));
                assertEquals(55, result.getValue(1, column));
                assertEquals(55, result.getValue(2, column));
                assertEquals(55, result.getValue(3, column));

                break;
            }
        }
    }

    public void testListAgg() throws Exception {
        switch (dialect().family()) {
            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxx
            xx [/pro] */
            case DERBY:
                log.info("SKIPPING", "LISTAGG tests");
                return;
        }

        // [#3045] Skip this test for the time being
        if (!asList().contains(dialect().family())) {
            Result<?> result1 = create().select(
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME(),
                    groupConcat(TBook_ID(), ", ")
                        .as("books"))
                .from(TAuthor())
                .join(TBook()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
                .groupBy(
                    TAuthor_ID(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME())
                .orderBy(TAuthor_ID())
                .fetch();

            assertEquals(2, result1.size());
            assertEquals(AUTHOR_FIRST_NAMES, result1.getValues(TAuthor_FIRST_NAME()));
            assertEquals(AUTHOR_LAST_NAMES, result1.getValues(TAuthor_LAST_NAME()));

            // [#2944] SQLite cannot guarantee any order among aggregated values...
            assertTrue(asList("1, 2", "2, 1").contains(result1.getValue(0, "books")));
            assertTrue(asList("3, 4", "4, 3").contains(result1.getValue(1, "books")));
        }

        switch (dialect().family()) {
            case SQLITE:
                log.info("SKIPPING", "LISTAGG ordered tests");
                return;
        }

        Result<?> result2 = create().select(
                TAuthor_FIRST_NAME(),
                TAuthor_LAST_NAME(),
                listAgg(TBook_ID(), ", ")
                    .withinGroupOrderBy(TBook_ID().desc())
                    .as("books1"),
                groupConcat(TBook_ID())
                    .orderBy(TBook_ID().desc())
                    .separator(", ")
                    .as("books2"))
            .from(TAuthor())
            .join(TBook()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
            .groupBy(
                TAuthor_ID(),
                TAuthor_FIRST_NAME(),
                TAuthor_LAST_NAME())
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(2, result2.size());
        assertEquals(AUTHOR_FIRST_NAMES, result2.getValues(TAuthor_FIRST_NAME()));
        assertEquals(AUTHOR_LAST_NAMES, result2.getValues(TAuthor_LAST_NAME()));
        assertEquals("2, 1", result2.getValue(0, "books1"));
        assertEquals("2, 1", result2.getValue(0, "books2"));
        assertEquals("4, 3", result2.getValue(1, "books1"));
        assertEquals("4, 3", result2.getValue(1, "books2"));

        switch (dialect()) {
            /* [pro] xx
            xxxx xxxx
            xxxx xxxxxxx
            xx [/pro] */
            case CUBRID:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
                log.info("SKIPPING", "LISTAGG window function tests");
                return;
        }

        Result<?> result3 = create().select(
                TAuthor_FIRST_NAME(),
                TAuthor_LAST_NAME(),
                listAgg(TBook_TITLE())
                   .withinGroupOrderBy(TBook_ID().asc())
                   .over().partitionBy(TAuthor_ID()))
           .from(TAuthor())
           .join(TBook()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
           .orderBy(TBook_ID())
           .fetch();

        assertEquals(4, result3.size());
        assertEquals(BOOK_FIRST_NAMES, result3.getValues(TAuthor_FIRST_NAME()));
        assertEquals(BOOK_LAST_NAMES, result3.getValues(TAuthor_LAST_NAME()));
        assertEquals("1984Animal Farm", result3.getValue(0, 2));
        assertEquals("1984Animal Farm", result3.getValue(1, 2));
        assertEquals("O AlquimistaBrida", result3.getValue(2, 2));
        assertEquals("O AlquimistaBrida", result3.getValue(3, 2));
    }

    public void testWindowClause() throws Exception {
        switch (dialect()) {
            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxxxx
            xxxx xxxxxxx
            xx [/pro] */
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case SQLITE:
                log.info("SKIPPING", "Window function tests");
                return;
        }


        Name a = name("a");
        Name b = name("b");

        TableField<B, Integer> aField = TBook_ID();
        TableField<B, Integer> bField = TBook_AUTHOR_ID();

        WindowSpecification aSpec = partitionBy(aField);
        WindowSpecification bSpec = partitionBy(bField);

        WindowDefinition aDef = a.as(aSpec);
        WindowDefinition bDef = b.as(bSpec);

        Result<?> result =
        create().select(
                    count().over().partitionBy(aField),
                    count().over(a),
                    count().over("a"),
                    count().over(aSpec),
                    count().over(aDef),
                    count().over().partitionBy(bField),
                    count().over(b),
                    count().over("b"),
                    count().over(bSpec),
                    count().over(bDef))
                .from(TBook())
                .window(aDef, bDef)
                .orderBy(aField)
                .fetch();

        assertEquals(asList(1, 1, 1, 1), result.getValues(0));
        assertEquals(asList(1, 1, 1, 1), result.getValues(1));
        assertEquals(asList(1, 1, 1, 1), result.getValues(2));
        assertEquals(asList(1, 1, 1, 1), result.getValues(3));
        assertEquals(asList(1, 1, 1, 1), result.getValues(4));

        assertEquals(asList(2, 2, 2, 2), result.getValues(5));
        assertEquals(asList(2, 2, 2, 2), result.getValues(6));
        assertEquals(asList(2, 2, 2, 2), result.getValues(7));
        assertEquals(asList(2, 2, 2, 2), result.getValues(8));
        assertEquals(asList(2, 2, 2, 2), result.getValues(9));
    }
}
