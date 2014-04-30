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
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.connectByIsCycle;
import static org.jooq.impl.DSL.connectByIsLeaf;
import static org.jooq.impl.DSL.connectByRoot;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.level;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.prior;
import static org.jooq.impl.DSL.rownum;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.sysConnectByPath;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public ExoticTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testTableWithHint() throws Exception {
        /* [pro] xx
        xxxxxx xxxxxxxxxxxxxxxxxxxx x
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxx
            xxxx xxxxxxx
            xxxx xxxxxx
            xxxx xxxxxxxxx
            xxxx xxx
            xxxx xxxxxxx
            xxxx xxxxxxxx
            xxxx xxxxxx
            xxxx xxxxxxxxx
            xxxx xxxxxxx
                xxxxxxxxxxxxxxxxxxxx xx xxxxx x xxxx x xxxx x xxxxxxxx
                xxxxxxx
        x

        xxxxxxxx x x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxx x x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxx xxxxxx x
        xxxxxxxxxxxxxxxxx
                xxxxxxxx
                xxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxx

        xxxxxxxxxxxxxxx xxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xx [/pro] */
    }

    public void testPivotClause() throws Exception {
        /* [pro] xx
        xxxxxx xxxxxxxxxxxxxxxxxxxx x
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxx
            xxxx xxxxxxx
            xxxx xxxxxxx
            xxxx xxxxxx
            xxxx xxxxxxxxx
            xxxx xxx
            xxxx xxxxxxx
            xxxx xxxxxxxx
            xxxx xxxxxx
            xxxx xxxxxxxxx
            xxxx xxxxxxx
                xxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxx xxxxxxxx
                xxxxxxx
        x

        xx xxxxxx xxxxxxxxx xx xxxxxxxx
        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxx xxxxxxx x
        xxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxx xxxxxxxx
                    xxx xxxxxxxx
                    xxxxxxxxxxxxx xx xxxxxxxxxxxx
                xxxxxxxxx
                    xxxxxxxxxxxx
                    xxxxxxxxxxxx
                xxxxxxxxx

        xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxx
        xxxxxxxxxxxxx
            xxxxxxxxx xx xx xx xxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx
            xxxxxxxxx xxx xx xx xxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx
            xxxxxxxxx xxx xx xx xxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx
            xxxxxxxxx xx xx xx xxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx
            xxxxxxxxx xx xx xx xxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx
            xxxxxxxxx xxx xx xx xxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xx xxxxxxxx xxxx xxxxxx xx xxxxxxxx xxx xxxxxxx xxxxxxxxx xxxxxxxxx
        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xxxxxxxxxxxxxx xxxxxxx x
        xxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxx
                    xxxxxxx xxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxx

        xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx
            xxxxxxxxx
                   xxx xxx xxx xx
                   xx xx xx xx
                   xxxxx xxxxx xxxxx xxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx
            xxxxxxxxx
                   xxx xxx xxx xx
                   xxxxx xxxxx xxxxx xx
                   xxxxx xxxxx xxxxx xxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx
            xxxxxxxxx
                   xxx xxx xxx xx
                   xx xx xx xx
                   xx xx xx xxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx


        xx xxxxx xxxxxxxx xx xxxxxx xx xxxxxx xxxxx
        xxxxxxxxxxxxxx xxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxx xxxxxxx x
        xxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx
                           xxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxx
                xxxxxxxxx
                xxxxxx xx xx xxx
                xxxxxxxxx

        xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxx
            xxxxxxxxx xx xx xx xxx
            xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xx [/pro] */
    }

    public void testAliasingPivot() throws Exception {
        /* [pro] xx
        xxxxxx xxxxxxxxxxxxxxxxxxxx x
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxx
            xxxx xxxxxxx
            xxxx xxxxxxx
            xxxx xxxxxx
            xxxx xxxxxxxxx
            xxxx xxx
            xxxx xxxxxxx
            xxxx xxxxxxxx
            xxxx xxxxxx
            xxxx xxxxxxxxx
            xxxx xxxxxxx
                xxxxxxxxxxxxxxxxxxxx xxxxxx xxxxxx xxxxxxxx
                xxxxxxx
        x

        xxxxxxxxx xx x
        xxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxx xxxxxxxx
                    xxx xxxxxxxx
                    xxxxxxxxxxxxx xx xxxxxxxxxxx
                xxxxxxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxx xxxxxxxxx
                                              xxxxxxxxx xxxxxxxxx
                                              xxxxxxxxx xxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxx

        xxxxxxxxxxxxxxx xxxxxxxxxxx
        xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxx xx xxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxx xxx xxxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxx xx xxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxx xxxxx xxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxx xx xxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxxx xxxxx xxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxx xx xxx xxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx

        xxxxxxxxx xx x
        xxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxx xx xx xx xx
                    xxxxxxxx xxxx xxxx xxxx xxxx xxxxx
                xxxxxxxxx

        xxxxxxxxxxxxxxx xxxxxxxxxxx
        xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xxxxxxxxxxxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxx
        xx [/pro] */
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
        switch (dialect().family()) {
            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxx
            xxxx xxxxxxx
            xx [/pro] */
            case DERBY:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
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

    public void testConnectByDirectory() throws Exception {
        switch (dialect().family()) {
            /* [pro] xx
            xxxx xxxxxxx
            xxxx xxxx
            xxxx xxxx
            xxxx xxxxxxx
            xxxx xxxxxxxxxx
            xxxx xxxxxxx
            xx [/pro] */
            case DERBY:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
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
