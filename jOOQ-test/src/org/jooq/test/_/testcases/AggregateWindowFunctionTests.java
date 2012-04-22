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
import static java.util.Collections.nCopies;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.Factory.avg;
import static org.jooq.impl.Factory.avgDistinct;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.countDistinct;
import static org.jooq.impl.Factory.cumeDist;
import static org.jooq.impl.Factory.denseRank;
import static org.jooq.impl.Factory.firstValue;
import static org.jooq.impl.Factory.groupConcat;
import static org.jooq.impl.Factory.lag;
import static org.jooq.impl.Factory.lead;
import static org.jooq.impl.Factory.listAgg;
import static org.jooq.impl.Factory.max;
import static org.jooq.impl.Factory.maxDistinct;
import static org.jooq.impl.Factory.median;
import static org.jooq.impl.Factory.min;
import static org.jooq.impl.Factory.minDistinct;
import static org.jooq.impl.Factory.ntile;
import static org.jooq.impl.Factory.percentRank;
import static org.jooq.impl.Factory.rank;
import static org.jooq.impl.Factory.rowNumber;
import static org.jooq.impl.Factory.stddevPop;
import static org.jooq.impl.Factory.stddevSamp;
import static org.jooq.impl.Factory.sum;
import static org.jooq.impl.Factory.sumDistinct;
import static org.jooq.impl.Factory.val;
import static org.jooq.impl.Factory.varPop;
import static org.jooq.impl.Factory.varSamp;

import java.math.BigDecimal;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class AggregateWindowFunctionTests<
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

    public AggregateWindowFunctionTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testSelectCountQuery() throws Exception {
        assertEquals(4, create().selectCount().from(TBook()).fetchOne(0));
        assertEquals(2, create().selectCount().from(TAuthor()).fetchOne(0));
    }

    @Test
    public void testAggregateFunctions() throws Exception {

        // Standard aggregate functions, available in all dialects:
        // --------------------------------------------------------
        Field<BigDecimal> median = median(TBook_ID());

        // Some dialects don't support a median function or a simulation thereof
        // Use AVG instead, as in this example the values of MEDIAN and AVG
        // are the same
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case DERBY:
            case H2:
            case INGRES:
            case MYSQL:
            case SQLITE:

            // TODO [#871] This could be simulated
            case SQLSERVER:
            case POSTGRES:
            case DB2:
                median = avg(TBook_ID());
                break;
        }

        Result<Record> result = create()
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

        assertEquals(2, (int) result.getValueAsInteger(0, 1));
        assertEquals(2, (int) result.getValueAsInteger(0, 2));
        assertEquals(1, (int) result.getValueAsInteger(0, 3));
        assertEquals(3d, result.getValueAsDouble(0, 4));
        assertEquals(1, (int) result.getValueAsInteger(0, 6));
        assertEquals(2, (int) result.getValueAsInteger(0, 7));

        assertEquals(2, (int) result.getValueAsInteger(1, 1));
        assertEquals(2, (int) result.getValueAsInteger(1, 2));
        assertEquals(1, (int) result.getValueAsInteger(1, 3));
        assertEquals(7d, result.getValueAsDouble(1, 4));
        assertEquals(3, (int) result.getValueAsInteger(1, 6));
        assertEquals(4, (int) result.getValueAsInteger(1, 7));

        // TODO [#868] Derby, HSQLDB, and SQL Server perform rounding/truncation
        // This may need to be corrected by jOOQ
        assertTrue(asList(1.0, 1.5, 2.0).contains(result.getValueAsDouble(0, 5)));
        assertTrue(asList(1.0, 1.5, 2.0).contains(result.getValueAsDouble(0, 8)));
        assertTrue(asList(3.0, 3.5, 4.0).contains(result.getValueAsDouble(1, 5)));
        assertTrue(asList(3.0, 3.5, 4.0).contains(result.getValueAsDouble(1, 8)));

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
        switch (getDialect()) {
            case DERBY:
            case SQLITE:
                log.info("SKIPPING", "Statistical aggregate functions");
                break;

            default: {
                result = create()
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

                assertEquals(0.5, result.getValueAsDouble(0, 1));
                assertEquals(0.25, result.getValueAsDouble(0, 3));
                assertEquals(0.5, result.getValueAsDouble(1, 1));
                assertEquals(0.25, result.getValueAsDouble(1, 3));

                // DB2 only knows STDDEV_POP / VAR_POP
                if (getDialect() != SQLDialect.DB2) {
                    assertEquals("0.707", result.getValueAsString(0, 2).substring(0, 5));
                    assertEquals(0.5, result.getValueAsDouble(0, 4));
                    assertEquals("0.707", result.getValueAsString(1, 2).substring(0, 5));
                    assertEquals(0.5, result.getValueAsDouble(1, 4));
                }
            }
        }

        // [#873] Duplicate functions
        // --------------------------
        result =
        create().select(
                    TBook_AUTHOR_ID(),
                    max(TBook_ID()),
                    max(TBook_ID()))
                .from(TBook())
                .groupBy(TBook_AUTHOR_ID())
                .orderBy(TBook_AUTHOR_ID())
                .fetch();

        assertEquals(2, (int) result.getValueAsInteger(0, 1));
        assertEquals(2, (int) result.getValueAsInteger(0, 2));
        assertEquals(4, (int) result.getValueAsInteger(1, 1));
        assertEquals(4, (int) result.getValueAsInteger(1, 2));
    }

    @Test
    public void testWindowFunctions() throws Exception {
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case DERBY:
            case H2:
            case HSQLDB:
            case INGRES:
            case MYSQL:
            case SQLITE:
                log.info("SKIPPING", "Window function tests");
                return;
        }

        int column = 0;

        // ROW_NUMBER()
        Result<Record> result =
        create().select(TBook_ID(),
                        rowNumber().over()
                                   .partitionByOne()
                                   .orderBy(TBook_ID().desc()),
                        rowNumber().over()
                                   .partitionBy(TBook_AUTHOR_ID())
                                   .orderBy(TBook_ID().desc()))
                .from(TBook())
                .orderBy(TBook_ID().asc())
                .fetch();

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

        switch (getDialect()) {
            case DB2:
            case SQLSERVER:
                log.info("SKIPPING", "PERCENT_RANK() and CUME_DIST() window function tests");
                break;

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
                assertEquals("1", result.getValueAsString(0, column));
                assertEquals("0.6", result.getValueAsString(1, column).substring(0, 3));
                assertEquals("0.3", result.getValueAsString(2, column).substring(0, 3));
                assertEquals("0", result.getValueAsString(3, column));

                // Partitioned and ordered PERCENT_RANK()
                column++;
                assertEquals("1", result.getValueAsString(0, column));
                assertEquals("0", result.getValueAsString(1, column));
                assertEquals("1", result.getValueAsString(2, column));
                assertEquals("0", result.getValueAsString(3, column));

                // Ordered CUME_DIST()
                column++;
                assertEquals("1", result.getValueAsString(0, column));
                assertEquals("0.75", result.getValueAsString(1, column));
                assertEquals("0.5", result.getValueAsString(2, column));
                assertEquals("0.25", result.getValueAsString(3, column));

                // Partitioned and ordered CUME_DIST()
                column++;
                assertEquals("1", result.getValueAsString(0, column));
                assertEquals("0.5", result.getValueAsString(1, column));
                assertEquals("1", result.getValueAsString(2, column));
                assertEquals("0.5", result.getValueAsString(3, column));

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
        assertEquals("1.118", result.getValueAsString(0, 1).substring(0, 5));
        assertEquals(1.25, result.getValueAsDouble(0, 3));

        // Partitioned STDDEV_POP(), STDDEV_SAMP(), VAR_POP(), VAR_SAMP()
        assertEquals(0.5, result.getValueAsDouble(0, 5));
        assertEquals(0.25, result.getValueAsDouble(0, 7));

        // DB2 only knows STDDEV_POP / VAR_POP
        if (getDialect() != SQLDialect.DB2) {
            assertEquals("1.290", result.getValueAsString(0, 2).substring(0, 5));
            assertEquals("1.666", result.getValueAsString(0, 4).substring(0, 5));
            assertEquals("0.707", result.getValueAsString(0, 6).substring(0, 5));
            assertEquals(0.5, result.getValueAsDouble(0, 8));
        }

        // NTILE()
        if (asList(SYBASE, DB2).contains(getDialect())) {
            log.info("SKIPPING", "NTILE tests");
        }
        else {
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
        if (getDialect() == SQLDialect.SQLSERVER) {
            log.info("SKIPPING", "ROWS UNBOUNDED PRECEDING and similar tests");
            return;
        }

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

        switch (getDialect()) {
            case POSTGRES:
                log.info("SKIPPING", "FIRST_VALUE(... IGNORE NULLS) window function test");
                break;

            default: {
                column = 0;

                // FIRST_VALUE(... IGNORE NULLS)
                result = create().select(TBook_ID(),
                                         firstValue(TBook_ID()).ignoreNulls()
                                                               .over()
                                                               .partitionBy(TBook_AUTHOR_ID())
                                                               .orderBy(TBook_PUBLISHED_IN().asc())
                                                               .rowsBetweenUnboundedPreceding()
                                                               .andUnboundedFollowing())
                                 .from(TBook())
                                 .orderBy(TBook_ID().asc())
                                 .fetch();

                // Partitioned and ordered FIRST_VALUE(... IGNORE NULLS) with ROWS
                column++;
                assertEquals(Integer.valueOf(2), result.getValue(0, column));
                assertEquals(Integer.valueOf(2), result.getValue(1, column));
                assertEquals(Integer.valueOf(3), result.getValue(2, column));
                assertEquals(Integer.valueOf(3), result.getValue(3, column));

                break;
            }
        }

        switch (getDialect()) {
            case SYBASE:
                log.info("SKIPPING", "LEAD/LAG tests");
                break;

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

    @Test
    public void testListAgg() throws Exception {
        switch (getDialect()) {
            case ASE:
            case DERBY:
            case INGRES:
            case SQLITE:
            case SQLSERVER:
                log.info("SKIPPING", "LISTAGG tests");
                return;
        }

        Result<?> result1 = create().select(
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

        assertEquals(2, result1.size());
        assertEquals(AUTHOR_FIRST_NAMES, result1.getValues(TAuthor_FIRST_NAME()));
        assertEquals(AUTHOR_LAST_NAMES, result1.getValues(TAuthor_LAST_NAME()));
        assertEquals("2, 1", result1.getValue(0, "books1"));
        assertEquals("2, 1", result1.getValue(0, "books2"));
        assertEquals("4, 3", result1.getValue(1, "books1"));
        assertEquals("4, 3", result1.getValue(1, "books2"));

        switch (getDialect()) {
            case CUBRID:
            case DB2:
            case H2:
            case HSQLDB:
            case MYSQL:
            case POSTGRES:
            case SYBASE:
                log.info("SKIPPING", "LISTAGG window function tests");
                return;
        }

        Result<?> result2 = create().select(
                TAuthor_FIRST_NAME(),
                TAuthor_LAST_NAME(),
                listAgg(TBook_TITLE())
                   .withinGroupOrderBy(TBook_ID().asc())
                   .over().partitionBy(TAuthor_ID()))
           .from(TAuthor())
           .join(TBook()).on(TAuthor_ID().equal(TBook_AUTHOR_ID()))
           .orderBy(TBook_ID())
           .fetch();

        assertEquals(4, result2.size());
        assertEquals(BOOK_FIRST_NAMES, result2.getValues(TAuthor_FIRST_NAME()));
        assertEquals(BOOK_LAST_NAMES, result2.getValues(TAuthor_LAST_NAME()));
        assertEquals("1984Animal Farm", result2.getValue(0, 2));
        assertEquals("1984Animal Farm", result2.getValue(1, 2));
        assertEquals("O AlquimistaBrida", result2.getValue(2, 2));
        assertEquals("O AlquimistaBrida", result2.getValue(3, 2));
    }
}
