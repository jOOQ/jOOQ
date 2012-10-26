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
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.Factory.currentDate;
import static org.jooq.impl.Factory.inline;
import static org.jooq.impl.Factory.not;
import static org.jooq.impl.Factory.row;
import static org.jooq.impl.Factory.val;

import java.sql.Date;

import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.types.DayToSecond;

import org.junit.Test;

public class RowValueExpressionTests<
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
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T725, T639, T785> {

    public RowValueExpressionTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRowValueExpressionConditions() throws Exception {

        // Simple equality tests
        assertEquals(1, (int)
        create().selectOne()
                .where(row(1, 2, 3).equal(1, 2, 3))
                .and(row(1, 2, 3).ne(3, 2, 1))
                .fetchOne(0, Integer.class));

        // IN-condition tests
        assertEquals(1, (int)
        create().selectOne()
                .where(row(1, 2, 3).in(row(3, 3, 3), row(2, 3, 1), row(1, 2, 3)))
                .and(row(1, 2, 3).notIn(row(3, 3, 3), row(2, 3, 1)))
                .fetchOne(0, Integer.class));

        // Tuples with actual data
        assertEquals(asList(1, 2),
        create().select(TBook_ID())
                .from(TBook())
                .where(row(TBook_ID()).equal(1))
                .or(row(TBook_ID(), inline(2)).equal(2, 2))
                .or(row(TBook_ID(), inline(2), val(3)).equal(1, 2, 3))
                .or(row("1", "2", "3", "4").equal(TBook_TITLE(), TBook_TITLE(), TBook_TITLE(), TBook_TITLE()))
                .or(row(TBook_ID(), TBook_ID(), TBook_ID(), TBook_ID(), TBook_ID()).notEqual(row(TBook_ID(), TBook_ID(), TBook_ID(), TBook_ID(), TBook_ID())))
                .or(row(1, 2, 3, 4, 5, 6).eq(6, 5, 4, 3, 2, 1))
                .or(row(1, 2, 3, 4, 5, 6, 7).eq(1, 2, 3, 4, 5, 6, 0))
                .or(row(1, 2, 3, 4, 5, 6, 7, 8).eq(1, 2, 3, 4, 5, 6, 7, 0))
                .or(row(1, 2, 3, 4, 5, 6, 7, 8, 9).eq(1, 2, 3, 4, 5, 6, 7, 8, 0))
                .orderBy(TBook_ID())
                .fetch(TBook_ID()));

        // IN with subselects - not supported by all DBs
        // TODO [#1772] Simulate this for all dialects
        if (asList(ASE, DERBY, FIREBIRD, INGRES, SQLSERVER, SQLITE, SYBASE, H2).contains(getDialect())) {
            log.info("SKIPPING", "Tuples and subselects");
        }
        else {
            assertEquals(1, (int)
            create().selectOne()
                    .where(row(1, 2, 3).in(create().select(val(1), val(2), val(3))))
                    .and(row(1, 2, 3).notIn(create().select(val(3), val(2), val(1))))
                    .fetchOne(0, Integer.class));

            // CUBRID has a bug here http://jira.cubrid.org/browse/ENGINE-61
            if (!asList(CUBRID).contains(getDialect())) {
                assertEquals(1, (int)
                    create().selectOne()
                            .where(row(1, 2, 3).in(create().select(val(1), val(2), val(3))))
                            .and(row(3, 2).notIn(
                                create().select(val(2), val(3)).union(
                                create().select(val(4), val(3)))))
                            .fetchOne(0, Integer.class));
            }
        }
    }

    @Test
    public void testRowValueExpressionOverlapsCondition() throws Exception {
        // 1903-06-25
        // 1947-08-24

        long now = System.currentTimeMillis();
        long day = 1000L * 60 * 60 * 24;

        // SQL standard (DATE, DATE) OVERLAPS (DATE, DATE) predicate
        // ---------------------------------------------------------
        assertEquals(2, (int)
        create().selectCount()
                .from(TAuthor())
                .where(row(TAuthor_DATE_OF_BIRTH(), currentDate())
                    .overlaps(new Date(now - day), new Date(now + day)))
                .fetchOne(0, Integer.class));

        // SQL standard (DATE, INTERVAL) OVERLAPS (DATE, INTERVAL) predicate
        // -----------------------------------------------------------------
        if (asList(INGRES).contains(getDialect())) {
            log.info("SKIPPING", "Ingres INTERVAL OVERLAPS tests");
        }
        else {
            assertEquals(1, (int)
            create().selectOne()
                    .where(row(new Date(now), new DayToSecond(3))
                        .overlaps(new Date(now + day), new DayToSecond(3)))
                    .fetchOne(0, Integer.class));

            // jOOQ should recognise these as a (DATE, INTERVAL) tuple
            assertEquals(1, (int)
            create().selectOne()
                    .where(row(new Date(now), 3)
                        .overlaps(new Date(now + day), 3))
                    .fetchOne(0, Integer.class));
        }

        // jOOQ's convenience for letting arbitrary data types "overlap"
        // -------------------------------------------------------------
        assertEquals(1, (int)
        create().selectOne()
                .where(row(1, 3).overlaps(2, 4))
                .and(row(1, 4).overlaps(2, 3))
                .and(row(1, 4).overlaps(3, 2))
                .and(not(row(1, 2).overlaps(3, 4)))
                .fetchOne(0, Integer.class));
    }
}
