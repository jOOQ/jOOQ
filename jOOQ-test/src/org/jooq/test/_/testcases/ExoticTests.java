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
import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.literal;
import static org.jooq.impl.Factory.lower;
import static org.jooq.impl.Factory.max;
import static org.jooq.impl.Factory.substring;
import static org.jooq.impl.Factory.sum;
import static org.jooq.impl.Factory.table;
import static org.jooq.impl.Factory.trueCondition;
import static org.jooq.impl.Factory.val;
import static org.jooq.util.oracle.OracleFactory.connectByIsCycle;
import static org.jooq.util.oracle.OracleFactory.connectByIsLeaf;
import static org.jooq.util.oracle.OracleFactory.level;
import static org.jooq.util.oracle.OracleFactory.prior;
import static org.jooq.util.oracle.OracleFactory.rownum;
import static org.jooq.util.oracle.OracleFactory.sysConnectByPath;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
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

    public ExoticTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testPivotClause() throws Exception {
        switch (getDialect()) {
            case ASE:
            case CUBRID:
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

    @Test
    public void testRelationalDivision() throws Exception {

        // Books and bookstores. There's only one book that is contained in
        // every bookstore:
        // ----------------------------------------------------------------
        int id =
        create().select()
                .from(TBookToBookStore()
                .divideBy(TBookStore())
                .on(TBookToBookStore_BOOK_STORE_NAME().equal(TBookStore_NAME()))
                .returning(TBookToBookStore_BOOK_ID()))
                .fetchOne(0, Integer.class);

        assertEquals(3, id);

        // Test removing some bookstores in nested selects
        Table<?> notAllBookStores =
        create().select()
                .from(TBookStore())
                .where(TBookStore_NAME().notEqual("Buchhandlung im Volkshaus"))
                .asTable("not_all_bookstores");

        Result<?> result =
        create().select()
                .from(TBookToBookStore()
                .divideBy(notAllBookStores)
                .on(TBookToBookStore_BOOK_STORE_NAME().equal(notAllBookStores.getField(TBookStore_NAME())))
                .returning(TBookToBookStore_BOOK_ID(), field("'abc'").as("abc")))
                .orderBy(1)
                .fetch();

        assertEquals(asList((Object) 1, "abc"), asList(result.get(0).intoArray()));
        assertEquals(asList((Object) 3, "abc"), asList(result.get(1).intoArray()));
    }

    @Test
    public void testConnectBySimple() throws Exception {
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
                log.info("SKIPPING", "Connect by tests");
                return;
        }

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            create().select(rownum())
                    .connectBy(level().lessThan(10))
                    .fetch(rownum()));
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            create().select(rownum())
                    .connectByNoCycle(level().lessThan(10))
                    .fetch(rownum()));

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            create().select(rownum())
                    .connectBy(level().lessThan(10))
                    .and("1 = ?", 1)
                    .startWith("? = ?", 1, 1)
                    .fetch(rownum()));
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            create().select(rownum())
                    .connectByNoCycle(level().lessThan(10))
                    .and("1 = ?", 1)
                    .startWith("? = ?", 1, 1)
                    .fetch(rownum()));

        Result<Record> result =
        create().select(rownum(), connectByIsCycle(), connectByIsLeaf())
                .connectByNoCycle(level().lessThan(4))
                .fetch();

        assertEquals(Integer.valueOf(1), result.getValue(0, rownum()));
        assertEquals(Integer.valueOf(2), result.getValue(1, rownum()));
        assertEquals(Integer.valueOf(3), result.getValue(2, rownum()));

        assertEquals(Boolean.FALSE, result.getValue(0, connectByIsLeaf()));
        assertEquals(Boolean.FALSE, result.getValue(1, connectByIsLeaf()));
        assertEquals(Boolean.TRUE, result.getValue(2, connectByIsLeaf()));

        assertEquals(Boolean.FALSE, result.getValue(0, connectByIsCycle()));
        assertEquals(Boolean.FALSE, result.getValue(1, connectByIsCycle()));
        assertEquals(Boolean.FALSE, result.getValue(2, connectByIsCycle()));
    }

    @Test
    public void testConnectByDirectory() throws Exception {
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
                log.info("SKIPPING", "Connect by tests");
                return;
        }

        List<?> paths =
        create().select(substring(sysConnectByPath(lower(TDirectory_NAME()), "/"), 2).as("dir"))
                .from(TDirectory())
                .where(trueCondition())
                .and(trueCondition())
                .connectBy(prior(TDirectory_ID()).equal(TDirectory_PARENT_ID()))
                .startWith(TDirectory_PARENT_ID().isNull())
                .orderBy(1)
                .fetch(0);

        assertEquals(25, paths.size());
        assertEquals(Arrays.asList(
            "c:",
            "c:/eclipse",
            "c:/eclipse/configuration",
            "c:/eclipse/dropins",
            "c:/eclipse/eclipse.exe",
            "c:/eclipse/eclipse.ini",
            "c:/eclipse/features",
            "c:/eclipse/plugins",
            "c:/eclipse/readme",
            "c:/eclipse/readme/readme_eclipse.html",
            "c:/eclipse/src",
            "c:/program files",
            "c:/program files/internet explorer",
            "c:/program files/internet explorer/de-de",
            "c:/program files/internet explorer/ielowutil.exe",
            "c:/program files/internet explorer/iexplore.exe",
            "c:/program files/java",
            "c:/program files/java/jre6",
            "c:/program files/java/jre6/bin",
            "c:/program files/java/jre6/bin/java.exe",
            "c:/program files/java/jre6/bin/javaw.exe",
            "c:/program files/java/jre6/bin/javaws.exe",
            "c:/program files/java/jre6/lib",
            "c:/program files/java/jre6/lib/javaws.jar",
            "c:/program files/java/jre6/lib/rt.jar"), paths);
    }
}
