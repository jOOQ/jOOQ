/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
import static org.jooq.impl.Factory.connectByIsCycle;
import static org.jooq.impl.Factory.connectByIsLeaf;
import static org.jooq.impl.Factory.connectByRoot;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.fieldByName;
import static org.jooq.impl.Factory.level;
import static org.jooq.impl.Factory.lower;
import static org.jooq.impl.Factory.max;
import static org.jooq.impl.Factory.one;
import static org.jooq.impl.Factory.prior;
import static org.jooq.impl.Factory.select;
import static org.jooq.impl.Factory.substring;
import static org.jooq.impl.Factory.sum;
import static org.jooq.impl.Factory.sysConnectByPath;
import static org.jooq.impl.Factory.table;
import static org.jooq.impl.Factory.trueCondition;
import static org.jooq.impl.Factory.two;
import static org.jooq.impl.Factory.val;
import static org.jooq.util.oracle.OracleFactory.rownum;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class ExoticTests<
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
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> {

    public ExoticTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testTableWithHint() throws Exception {
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case DB2:
            case DERBY:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case INGRES:
            case MYSQL:
            case ORACLE:
            case POSTGRES:
            case SQLITE:
                log.info("SKIPPING", "[ table ] WITH [ hint ] tests");
                return;
        }

        Table<B> b = TBook().as("b").with("READUNCOMMITTED");
        Table<A> a = TAuthor().with("READUNCOMMITTED").as("a");

        Result<?> result =
        create().select()
                .from(b)
                .join(a)
                .on(b.field(TBook_AUTHOR_ID()).eq(a.field(TAuthor_ID())))
                .orderBy(b.field(TBook_ID()))
                .fetch();

        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(TBook_ID()));
    }

    @Test
    public void testPivotClause() throws Exception {
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case DB2:
            case DERBY:
            case FIREBIRD:
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
                    one().asc(),
                    two().asc())
                .fetch();

        assertEquals(6, result1.size());
        assertEquals(TBookToBookStore_BOOK_ID().getName(), result1.field(0).getName());
        assertEquals(TBookToBookStore_STOCK().getName(), result1.field(1).getName());
        assertTrue(result1.field(2).getName().contains("Orell Füssli"));
        assertTrue(result1.field(3).getName().contains("Ex Libris"));
        assertTrue(result1.field(4).getName().contains("Buchhandlung im Volkshaus"));
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
        assertEquals(TBookToBookStore_BOOK_ID().getName(), result2.field(0).getName());
        assertEquals("BS1_AVG", result2.field(1).getName());
        assertEquals("BS1_MAX", result2.field(2).getName());
        assertEquals("BS1_SUM", result2.field(3).getName());
        assertEquals("BS1_CNT", result2.field(4).getName());
        assertEquals("BS2_AVG", result2.field(5).getName());
        assertEquals("BS2_MAX", result2.field(6).getName());
        assertEquals("BS2_SUM", result2.field(7).getName());
        assertEquals("BS2_CNT", result2.field(8).getName());
        assertEquals("BS3_AVG", result2.field(9).getName());
        assertEquals("BS3_MAX", result2.field(10).getName());
        assertEquals("BS3_SUM", result2.field(11).getName());
        assertEquals("BS3_CNT", result2.field(12).getName());
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
                .from(table(select(TBook_AUTHOR_ID(), lang)
                           .from(TBook()))
                .pivot(count())
                .on(lang)
                .in(1, 2, 3, 4))
                .fetch();

        assertEquals(2, result3.size());
        assertEquals(5, result3.fieldsRow().size());
        assertEquals(AUTHOR_IDS, result3.getValues(0));
        assertEquals(
            asList(1, 2, 0, 0, 0),
            asList(result3.get(0).into(Integer[].class)));
    }

    @Test
    public void testAliasingPivot() throws Exception {
        switch (getDialect()) {
            case ASE:
            case CUBRID:
            case DB2:
            case DERBY:
            case FIREBIRD:
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

        Result<?> r1 =
        create().select()
                .from(TBookToBookStore()
                .pivot(max(TBookToBookStore_STOCK()).as("max"),
                       count(TBookToBookStore_STOCK()).as("cnt"))
                .on(TBookToBookStore_BOOK_STORE_NAME())
                .in("Orell Füssli",
                    "Ex Libris",
                    "Buchhandlung im Volkshaus")
                .as("pivot_table", "book_id", "of_max", "of_cnt",
                                              "ex_max", "ex_cnt",
                                              "bv_max", "bv_cnt"))
                .orderBy(val(1).asc())
                .fetch();

        assertEquals(3, r1.size());
        assertEquals(7, r1.fieldsRow().size());
        assertEquals(asList(1, 2, 3), r1.getValues("book_id", Integer.class));
        assertEquals(asList(10, 10, 10), r1.getValues("of_max", Integer.class));
        assertEquals(asList(1, 1, 1), r1.getValues("of_cnt", Integer.class));
        assertEquals(asList(1, null, 2), r1.getValues("ex_max", Integer.class));
        assertEquals(asList(1, 0, 1), r1.getValues("ex_cnt", Integer.class));
        assertEquals(asList(null, null, 1), r1.getValues("bv_max", Integer.class));
        assertEquals(asList(0, 0, 1), r1.getValues("bv_cnt", Integer.class));

        Result<?> r2 =
        create().select()
                .from(table(select(level().as("lvl")).connectBy(level().le(5)))
                    .pivot(max(fieldByName("lvl")))
                    .on(fieldByName("lvl"))
                    .in(1, 2, 3, 4, 5)
                    .as("t", "a", "b", "c", "d", "e"))
                .fetch();

        assertEquals(1, r2.size());
        assertEquals(5, r2.fieldsRow().size());
        assertEquals(1, (int) r2.get(0).getValue("a", Integer.class));
        assertEquals(2, (int) r2.get(0).getValue("b", Integer.class));
        assertEquals(3, (int) r2.get(0).getValue("c", Integer.class));
        assertEquals(4, (int) r2.get(0).getValue("d", Integer.class));
        assertEquals(5, (int) r2.get(0).getValue("e", Integer.class));
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
                .on(TBookToBookStore_BOOK_STORE_NAME().equal(notAllBookStores.field(TBookStore_NAME())))
                .returning(TBookToBookStore_BOOK_ID(), field("'abc'").as("abc")))
                .orderBy(1)
                .fetch();

        assertEquals(asList((Object) 1, "abc"), asList(result.get(0).intoArray()));
        assertEquals(asList((Object) 3, "abc"), asList(result.get(1).intoArray()));
    }

    @Test
    public void testAliasingRelationalDivision() throws Exception {

        // Books and bookstores. There's only one book that is contained in
        // every bookstore:
        // ----------------------------------------------------------------
        Record record =
        create().select()
                .from(TBookToBookStore()
                .divideBy(TBookStore())
                .on(TBookToBookStore_BOOK_STORE_NAME().equal(TBookStore_NAME()))
                .returning(TBookToBookStore_BOOK_ID())
                .as("division", "x"))
                .fetchOne();

        assertEquals(3, record.getValue("x"));
    }

    @Test
    public void testConnectBySimple() throws Exception {
        switch (getDialect()) {
            case ASE:
            case DB2:
            case DERBY:
            case FIREBIRD:
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

        Result<Record3<Integer, Boolean, Boolean>> result =
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
            case FIREBIRD:
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

        Result<Record4<String, Boolean, Boolean, String>> paths =
        create().select(
                    lower(connectByRoot(TDirectory_NAME())),
                    connectByIsLeaf(),
                    connectByIsCycle(),
                    substring(sysConnectByPath(lower(TDirectory_NAME()), "/"), 2).as("dir"))
                .from(TDirectory())
                .where(trueCondition())
                .and(trueCondition())
                .connectByNoCycle(prior(TDirectory_ID()).equal(TDirectory_PARENT_ID()))
                .startWith(TDirectory_PARENT_ID().isNull())
                .orderSiblingsBy(TDirectory_NAME().lower())
                .fetch();

        assertEquals(25, paths.size());
        assertEquals(Collections.nCopies(25, "c:"), paths.getValues(0));
        assertEquals(Arrays.asList(
            false, // c:
            false, // c:/eclipse
            true,
            true,
            true,
            true,
            true,
            true,
            false, // c:/eclipse/readme
            true,
            true,
            false, // c:/program files
            false, // c:/program files/internet explorer
            true,
            true,
            true,
            false, // c:/program files/java
            false, // c:/program files/java/jre6
            false, // c:/program files/java/jre6/bin,
            true,
            true,
            true,
            false, // c:/program files/java/jre6/lib
            true,
            true), paths.getValues(1));
        assertEquals(Collections.nCopies(25, false), paths.getValues(2));
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
            "c:/program files/java/jre6/lib/rt.jar"), paths.getValues(3));
    }
}
