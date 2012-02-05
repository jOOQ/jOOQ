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
import static junit.framework.Assert.assertTrue;
import static org.jooq.impl.Factory.avg;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.literal;
import static org.jooq.impl.Factory.max;
import static org.jooq.impl.Factory.sum;
import static org.jooq.impl.Factory.table;
import static org.jooq.impl.Factory.val;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class ExoticTests<
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

    public ExoticTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testPivotClause() throws Exception {
        switch (getDialect()) {
            case ASE:
            case DB2:
            case DERBY:
            case H2:
            case HSQLDB:
            case INGRES:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case SQLSERVER:
            case SYBASE:
                log.info("SKIPPING", "PIVOT clause tests");
                return;
        }

        // Simple pivoting, no aliasing
        // ----------------------------

        Result<Record> result1 =
        create().select()
                .from(TBookToBookStore()
                .pivot(count())
                .on(TBookToBookStore_BOOK_STORE_NAME())
                .in("Orell Füssli",
                    "Ex Libris",
                    "Buchhandlung im Volkshaus"))
                .orderBy(
                    literal(1).asc(),
                    literal(2).asc())
                .fetch();

        assertEquals(6, result1.size());
        assertEquals(TBookToBookStore_BOOK_ID().getName(), result1.getField(0).getName());
        assertEquals(TBookToBookStore_STOCK().getName(), result1.getField(1).getName());
        assertTrue(result1.getField(2).getName().contains("Orell Füssli"));
        assertTrue(result1.getField(3).getName().contains("Ex Libris"));
        assertTrue(result1.getField(4).getName().contains("Buchhandlung im Volkshaus"));
        assertEquals(
            asList(1, 1, 0, 1, 0),
            asList(result1.get(0).into(Integer[].class)));
        assertEquals(
            asList(1, 10, 1, 0, 0),
            asList(result1.get(1).into(Integer[].class)));
        assertEquals(
            asList(2, 10, 1, 0, 0),
            asList(result1.get(2).into(Integer[].class)));
        assertEquals(
            asList(3, 1, 0, 0, 1),
            asList(result1.get(3).into(Integer[].class)));
        assertEquals(
            asList(3, 2, 0, 1, 0),
            asList(result1.get(4).into(Integer[].class)));
        assertEquals(
            asList(3, 10, 1, 0, 0),
            asList(result1.get(5).into(Integer[].class)));

        // Pivoting with plenty of aliasing and several aggregate functions
        // ----------------------------------------------------------------

        Result<Record> result2 =
        create().select()
                .from(TBookToBookStore()
                .pivot(avg(TBookToBookStore_STOCK()).as("AVG"),
                       max(TBookToBookStore_STOCK()).as("MAX"),
                       sum(TBookToBookStore_STOCK()).as("SUM"),
                       count(TBookToBookStore_STOCK()).as("CNT"))
                .on(TBookToBookStore_BOOK_STORE_NAME())
                .in(val("Orell Füssli").as("BS1"),
                    val("Ex Libris").as("BS2"),
                    val("Buchhandlung im Volkshaus").as("BS3")))
                .orderBy(val(1).asc())
                .fetch();

        assertEquals(3, result2.size());
        assertEquals(TBookToBookStore_BOOK_ID().getName(), result2.getField(0).getName());
        assertEquals("BS1_AVG", result2.getField(1).getName());
        assertEquals("BS1_MAX", result2.getField(2).getName());
        assertEquals("BS1_SUM", result2.getField(3).getName());
        assertEquals("BS1_CNT", result2.getField(4).getName());
        assertEquals("BS2_AVG", result2.getField(5).getName());
        assertEquals("BS2_MAX", result2.getField(6).getName());
        assertEquals("BS2_SUM", result2.getField(7).getName());
        assertEquals("BS2_CNT", result2.getField(8).getName());
        assertEquals("BS3_AVG", result2.getField(9).getName());
        assertEquals("BS3_MAX", result2.getField(10).getName());
        assertEquals("BS3_SUM", result2.getField(11).getName());
        assertEquals("BS3_CNT", result2.getField(12).getName());
        assertEquals(
            asList(1,
                   10, 10, 10, 1,
                   1, 1, 1, 1,
                   null, null, null, 0),
            asList(result2.get(0).into(Integer[].class)));
        assertEquals(
            asList(2,
                   10, 10, 10, 1,
                   null, null, null, 0,
                   null, null, null, 0),
            asList(result2.get(1).into(Integer[].class)));
        assertEquals(
            asList(3,
                   10, 10, 10, 1,
                   2, 2, 2, 1,
                   1, 1, 1, 1),
            asList(result2.get(2).into(Integer[].class)));


        // Check aliasing of fields in source table
        Field<Integer> lang = TBook_LANGUAGE_ID().cast(Integer.class).as("lang");
        Result<Record> result3 =
        create().select()
                .from(table(create().select(TBook_AUTHOR_ID(), lang)
                                    .from(TBook()))
                .pivot(count())
                .on(lang)
                .in(1, 2, 3, 4))
                .fetch();

        assertEquals(2, result3.size());
        assertEquals(5, result3.getFields().size());
        assertEquals(AUTHOR_IDS, result3.getValues(0));
        assertEquals(
            asList(1, 2, 0, 0, 0),
            asList(result3.get(0).into(Integer[].class)));
    }
}
