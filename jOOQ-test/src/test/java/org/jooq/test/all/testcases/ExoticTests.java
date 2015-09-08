/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.test.all.testcases;

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HANA;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INFORMIX;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.REDSHIFT;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.SQLDialect.VERTICA;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.connectByIsCycle;
import static org.jooq.impl.DSL.connectByIsLeaf;
import static org.jooq.impl.DSL.connectByRoot;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.level;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.prior;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.sysConnectByPath;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNotNull;

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
import org.jooq.exception.DataAccessException;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

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
    CS   extends UpdatableRecord<CS>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> {

    public ExoticTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testTableWithHint() throws Exception {
        switch (family()) {
            case ACCESS:
            case ASE:
            case DB2:
            case INFORMIX:
            case INGRES:
            case REDSHIFT:
            case ORACLE:
            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case H2:
            case HANA:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case VERTICA:
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

    public void testPivotClause() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, DB2, INGRES, REDSHIFT, SQLSERVER, SYBASE, CUBRID, DERBY, FIREBIRD, H2, HANA,
            HSQLDB, INFORMIX, MARIADB, MYSQL, POSTGRES, SQLITE, VERTICA);

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

    public void testAliasingPivot() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, DB2, INFORMIX, INGRES, REDSHIFT, SQLSERVER, SYBASE, CUBRID, DERBY, FIREBIRD, H2, HANA, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, VERTICA);

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
                    .pivot(max(field(name("lvl"))))
                    .on(field(name("lvl")))
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

    public void testConnectBySimple() throws Exception {
        assumeNotNull(TDirectory());

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            create().select(level())
                    .connectBy(level().lessThan(10))
                    .fetch(level()));
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            create().select(level())
                    .connectByNoCycle(level().lessThan(10))
                    .fetch(level()));

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            create().select(level())
                    .connectBy(level().lessThan(10))
                    .and("1 = ?", 1)
                    .startWith("? = ?", 1, 1)
                    .fetch(level()));
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9),
            create().select(level())
                    .connectByNoCycle(level().lessThan(10))
                    .and("1 = ?", 1)
                    .startWith("? = ?", 1, 1)
                    .fetch(level()));

        Result<Record3<Integer, Boolean, Boolean>> result =
        create().select(level(), connectByIsCycle(), connectByIsLeaf())
                .connectByNoCycle(level().lessThan(4))
                .fetch();

        assertEquals(Integer.valueOf(1), result.getValue(0, level()));
        assertEquals(Integer.valueOf(2), result.getValue(1, level()));
        assertEquals(Integer.valueOf(3), result.getValue(2, level()));

        assertEquals(Boolean.FALSE, result.getValue(0, connectByIsLeaf()));
        assertEquals(Boolean.FALSE, result.getValue(1, connectByIsLeaf()));
        assertEquals(Boolean.TRUE, result.getValue(2, connectByIsLeaf()));

        assertEquals(Boolean.FALSE, result.getValue(0, connectByIsCycle()));
        assertEquals(Boolean.FALSE, result.getValue(1, connectByIsCycle()));
        assertEquals(Boolean.FALSE, result.getValue(2, connectByIsCycle()));
    }

    public void testConnectByDirectory() throws Exception {
        assumeNotNull(TDirectory());

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

    public void testWithCheckOption() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HANA, HSQLDB, INFORMIX, INGRES, MARIADB, MYSQL,
            POSTGRES, REDSHIFT, SQLITE, SQLSERVER, SYBASE, VERTICA);

        jOOQAbstractTest.reset = false;

        create()
            .insertInto(
                table(selectFrom(TAuthor())
                .where(TAuthor_ID().eq(3))
                .withCheckOption()),

                TAuthor_ID(),
                TAuthor_LAST_NAME()
            )
            .values(3, "abc")
            .execute();

        try {
            create()
                .insertInto(
                    table(selectFrom(TAuthor())
                    .where(TAuthor_ID().lt(4))
                    .withCheckOption()),

                    TAuthor_ID(),
                    TAuthor_LAST_NAME()
                )
                .values(4, "abc")
                .execute();

            fail();
        }
        catch (DataAccessException expected) {}
    }

    public void testWithReadOnly() throws Exception {
        assumeFamilyNotIn(ACCESS, ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HANA, HSQLDB, INFORMIX, INGRES, MARIADB, MYSQL, POSTGRES, SQLITE, SQLSERVER, SYBASE);

        try {
            create()
                .insertInto(
                    table(selectFrom(TAuthor())
                    .where(TAuthor_ID().lt(10))
                    .withReadOnly()),

                    TAuthor_ID(),
                    TAuthor_LAST_NAME()
                )
                .values(4, "abc")
                .execute();

            fail();
        }
        catch (DataAccessException expected) {}
    }
}
