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
import static org.jooq.impl.Factory.select;
import static org.jooq.impl.Factory.val;

import java.sql.Date;

import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Row1;
import org.jooq.Row2;
import org.jooq.Row3;
import org.jooq.Row4;
import org.jooq.Row5;
import org.jooq.Row6;
import org.jooq.Row7;
import org.jooq.Row8;
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
                    .where(row(1, 2, 3).in(select(val(1), val(2), val(3))))
                    .and(row(1, 2, 3).notIn(select(val(3), val(2), val(1))))
                    .fetchOne(0, Integer.class));

            // CUBRID has a bug here http://jira.cubrid.org/browse/ENGINE-61
            if (!asList(CUBRID).contains(getDialect())) {
                assertEquals(1, (int)
                    create().selectOne()
                            .where(row(1, 2, 3).in(select(val(1), val(2), val(3))))
                            .and(row(3, "2").notIn(
                                select(val(2), val("3")).union(
                                select(val(4), val("3")))))
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

    @Test
    public void testRowValueExpressionRecords() {

        // All record types should be assignment-compatible to the general-purpose
        // record type, both at compile-time and at run-time
        Record record;

        // Type-safe record types
        Record1<Integer> r1;
        Record2<Integer, String> r2;
        Record3<Integer, String, Integer> r3;
        Record4<Integer, String, Integer, String> r4;
        Record5<Integer, String, Integer, String, Integer> r5;
        Record6<Integer, String, Integer, String, Integer, String> r6;
        Record7<Integer, String, Integer, String, Integer, String, Integer> r7;
        Record8<Integer, String, Integer, String, Integer, String, Integer, String> r8;
        Record r9;

        record = r1 = create().fetchOne(select(val(1)));
        record = r2 = create().fetchOne(select(val(1), val("2")));
        record = r3 = create().fetchOne(select(val(1), val("2"), val(3)));
        record = r4 = create().fetchOne(select(val(1), val("2"), val(3), val("4")));
        record = r5 = create().fetchOne(select(val(1), val("2"), val(3), val("4"), val(5)));
        record = r6 = create().fetchOne(select(val(1), val("2"), val(3), val("4"), val(5), val("6")));
        record = r7 = create().fetchOne(select(val(1), val("2"), val(3), val("4"), val(5), val("6"), val(7)));
        record = r8 = create().fetchOne(select(val(1), val("2"), val(3), val("4"), val(5), val("6"), val(7), val("8")));
        record = r9 = create().fetchOne(select(val(1), val("2"), val(3), val("4"), val(5), val("6"), val(7), val("8"), val(9)));

        assertEquals(record, r9);

        // Checking value[N]() and field[N]() methods
        // ------------------------------------------

        // Checking value1(), field1()
        assertEquals(1, (int) r1.value1()); assertEquals(val(1), r1.field1());
        assertEquals(1, (int) r2.value1()); assertEquals(val(1), r2.field1());
        assertEquals(1, (int) r3.value1()); assertEquals(val(1), r3.field1());
        assertEquals(1, (int) r4.value1()); assertEquals(val(1), r4.field1());
        assertEquals(1, (int) r5.value1()); assertEquals(val(1), r5.field1());
        assertEquals(1, (int) r6.value1()); assertEquals(val(1), r6.field1());
        assertEquals(1, (int) r7.value1()); assertEquals(val(1), r7.field1());
        assertEquals(1, (int) r8.value1()); assertEquals(val(1), r8.field1());

        // Checking value2(), field2()
        assertEquals("2", r2.value2()); assertEquals(val("2"), r2.field2());
        assertEquals("2", r3.value2()); assertEquals(val("2"), r3.field2());
        assertEquals("2", r4.value2()); assertEquals(val("2"), r4.field2());
        assertEquals("2", r5.value2()); assertEquals(val("2"), r5.field2());
        assertEquals("2", r6.value2()); assertEquals(val("2"), r6.field2());
        assertEquals("2", r7.value2()); assertEquals(val("2"), r7.field2());
        assertEquals("2", r8.value2()); assertEquals(val("2"), r8.field2());

        // Checking value3(), field3()
        assertEquals(3, (int) r3.value3()); assertEquals(val(3), r3.field3());
        assertEquals(3, (int) r4.value3()); assertEquals(val(3), r4.field3());
        assertEquals(3, (int) r5.value3()); assertEquals(val(3), r5.field3());
        assertEquals(3, (int) r6.value3()); assertEquals(val(3), r6.field3());
        assertEquals(3, (int) r7.value3()); assertEquals(val(3), r7.field3());
        assertEquals(3, (int) r8.value3()); assertEquals(val(3), r8.field3());

        // Checking value4(), field4()
        assertEquals("4", r4.value4()); assertEquals(val("4"), r4.field4());
        assertEquals("4", r5.value4()); assertEquals(val("4"), r5.field4());
        assertEquals("4", r6.value4()); assertEquals(val("4"), r6.field4());
        assertEquals("4", r7.value4()); assertEquals(val("4"), r7.field4());
        assertEquals("4", r8.value4()); assertEquals(val("4"), r8.field4());

        // Checking value5(), field5()
        assertEquals(5, (int) r5.value5()); assertEquals(val(5), r5.field5());
        assertEquals(5, (int) r6.value5()); assertEquals(val(5), r6.field5());
        assertEquals(5, (int) r7.value5()); assertEquals(val(5), r7.field5());
        assertEquals(5, (int) r8.value5()); assertEquals(val(5), r8.field5());

        // Checking value6(), field6()
        assertEquals("6", r6.value6()); assertEquals(val("6"), r6.field6());
        assertEquals("6", r7.value6()); assertEquals(val("6"), r7.field6());
        assertEquals("6", r8.value6()); assertEquals(val("6"), r8.field6());

        // Checking value7(), field7()
        assertEquals(7, (int) r7.value7()); assertEquals(val(7), r7.field7());
        assertEquals(7, (int) r8.value7()); assertEquals(val(7), r8.field7());

        // Checking value8(), field8()
        assertEquals("8", r8.value8()); assertEquals(val("8"), r8.field8());

        // Checking fieldsRow() and valuesRow() methods
        // --------------------------------------------
        Row1<Integer> row1;
        Row2<Integer, String> row2;
        Row3<Integer, String, Integer> row3;
        Row4<Integer, String, Integer, String> row4;
        Row5<Integer, String, Integer, String, Integer> row5;
        Row6<Integer, String, Integer, String, Integer, String> row6;
        Row7<Integer, String, Integer, String, Integer, String, Integer> row7;
        Row8<Integer, String, Integer, String, Integer, String, Integer, String> row8;

        for (int i = 0; i < 2; i++) {

            // In the first run, consider the row value expression's fields...
            if (i == 0) {
                row1 = r1.fieldsRow();
                row2 = r2.fieldsRow();
                row3 = r3.fieldsRow();
                row4 = r4.fieldsRow();
                row5 = r5.fieldsRow();
                row6 = r6.fieldsRow();
                row7 = r7.fieldsRow();
                row8 = r8.fieldsRow();
            }

            // ... in this test-case, they should coincide with the values
            else {
                row1 = r1.valuesRow();
                row2 = r2.valuesRow();
                row3 = r3.valuesRow();
                row4 = r4.valuesRow();
                row5 = r5.valuesRow();
                row6 = r6.valuesRow();
                row7 = r7.valuesRow();
                row8 = r8.valuesRow();
            }

            // Checking field1()
            assertEquals(val(1), row1.field1());
            assertEquals(val(1), row2.field1());
            assertEquals(val(1), row3.field1());
            assertEquals(val(1), row4.field1());
            assertEquals(val(1), row5.field1());
            assertEquals(val(1), row6.field1());
            assertEquals(val(1), row7.field1());
            assertEquals(val(1), row8.field1());

            // Checking field2()
            assertEquals(val("2"), row2.field2());
            assertEquals(val("2"), row3.field2());
            assertEquals(val("2"), row4.field2());
            assertEquals(val("2"), row5.field2());
            assertEquals(val("2"), row6.field2());
            assertEquals(val("2"), row7.field2());
            assertEquals(val("2"), row8.field2());

            // Checking field3()
            assertEquals(val(3), row3.field3());
            assertEquals(val(3), row4.field3());
            assertEquals(val(3), row5.field3());
            assertEquals(val(3), row6.field3());
            assertEquals(val(3), row7.field3());
            assertEquals(val(3), row8.field3());

            // Checking field4()
            assertEquals(val("4"), row4.field4());
            assertEquals(val("4"), row5.field4());
            assertEquals(val("4"), row6.field4());
            assertEquals(val("4"), row7.field4());
            assertEquals(val("4"), row8.field4());

            // Checking field5()
            assertEquals(val(5), row5.field5());
            assertEquals(val(5), row6.field5());
            assertEquals(val(5), row7.field5());
            assertEquals(val(5), row8.field5());

            // Checking field6()
            assertEquals(val("6"), row6.field6());
            assertEquals(val("6"), row7.field6());
            assertEquals(val("6"), row8.field6());

            // Checking field7()
            assertEquals(val(7), row7.field7());
            assertEquals(val(7), row8.field7());

            // Checking field8()
            assertEquals(val("8"), row8.field8());
        }
    }
}
