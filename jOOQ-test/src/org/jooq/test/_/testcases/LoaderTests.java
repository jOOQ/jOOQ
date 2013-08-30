/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.test._.testcases;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.impl.DSL.count;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import org.jooq.Field;
import org.jooq.Loader;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class LoaderTests<
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

    public LoaderTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testLoader() throws Exception {
        jOOQAbstractTest.reset = false;
        jOOQAbstractTest.connection.setAutoCommit(false);

        Field<Integer> count = count();

        // Empty CSV file
        // --------------
        Loader<A> loader =
        create().loadInto(TAuthor())
                .loadCSV("")
                .fields(TAuthor_ID())
                .execute();

        assertEquals(0, loader.processed());
        assertEquals(0, loader.errors().size());
        assertEquals(0, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (LAST_NAME is NOT NULL)
        // Loading is aborted
        // ---------------------------------------------
        loader =
        create().loadInto(TAuthor())
                .loadCSV(
                    "3\n" +
                    "4")
                .fields(TAuthor_ID())
                .ignoreRows(0)
                .execute();

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        assertEquals(1, loader.processed());
        assertEquals(1, loader.errors().size());
        assertNotNull(loader.errors().get(0));
        assertEquals(0, loader.stored());
        assertEquals(1, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (LAST_NAME is NOT NULL)
        // Errors are ignored
        // ---------------------------------------------
        loader =
        create().loadInto(TAuthor())
                .onErrorIgnore()
                .loadCSV(
                    "3\n" +
                    "4")
                .fields(TAuthor_ID())
                .ignoreRows(0)
                .execute();

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        assertEquals(2, loader.processed());
        assertEquals(2, loader.errors().size());
        assertNotNull(loader.errors().get(0));
        assertNotNull(loader.errors().get(1));
        assertEquals(0, loader.stored());
        assertEquals(2, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (Duplicate records)
        // Loading is aborted
        // -----------------------------------------
        loader =
        create().loadInto(TAuthor())
                .onDuplicateKeyError()
                .onErrorAbort()
                .loadCSV(
                    "1;'Kafka'\n" +
                    "2;Frisch")
                .fields(TAuthor_ID(), TAuthor_LAST_NAME())
                .quote('\'')
                .separator(';')
                .ignoreRows(0)
                .execute();

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        assertEquals(1, loader.processed());
        assertEquals(1, loader.errors().size());
        assertNotNull(loader.errors().get(0));
        assertEquals(0, loader.stored());
        assertEquals(1, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (Duplicate records)
        // Errors are ignored
        // -----------------------------------------
        loader =
        create().loadInto(TAuthor())
                .onDuplicateKeyIgnore()
                .onErrorAbort()
                .loadCSV(
                    "1,\"Kafka\"\n" +
                    "2,Frisch")
                .fields(TAuthor_ID(), TAuthor_LAST_NAME())
                .ignoreRows(0)
                .execute();

        assertEquals(2, loader.processed());
        assertEquals(0, loader.errors().size());
        assertEquals(2, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Two records with different NULL representations for FIRST_NAME
        // --------------------------------------------------------------
        loader =
        create().loadInto(TAuthor())
                .loadCSV(
                    "####Some Data####\n" +
                    "\"ID\",\"Last Qualifier\"\r" +
                    "3,\"\",Hesse\n" +
                    "4,,Frisch")
                .fields(TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
                .quote('"')
                .separator(',')
                .ignoreRows(2)
                .execute();

        assertEquals(2, loader.processed());
        assertEquals(2, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());
        assertEquals(2, (int) create().select(count)
                                      .from(TAuthor())
                                      .where(TAuthor_ID().in(3, 4))
                                      .and(TAuthor_LAST_NAME().in("Hesse", "Frisch"))
                                      .and(dialect().family() == ORACLE ?
                                           TAuthor_FIRST_NAME().isNull() :
                                           TAuthor_FIRST_NAME().equal(""))
                                      .fetchOne(count));

        assertEquals(2, create().delete(TAuthor()).where(TAuthor_ID().in(3, 4)).execute());

        // Two records but don't load one column, and specify a value for NULL
        // -------------------------------------------------------------------
        loader =
        create().loadInto(TAuthor())
                .loadCSV(
                    "\"ID\",ignore,\"First Qualifier\",\"Last Qualifier\"\r" +
                    "5,asdf,{null},Hesse\n" +
                    "6,asdf,\"\",Frisch")
                .fields(TAuthor_ID(), null, TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
                .nullString("{null}")
                .execute();

        assertEquals(2, loader.processed());
        assertEquals(2, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());

        Result<A> result =
        create().selectFrom(TAuthor())
                .where(TAuthor_ID().in(5, 6))
                .and(TAuthor_LAST_NAME().in("Hesse", "Frisch"))
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(2, result.size());
        assertEquals(5, (int) result.getValue(0, TAuthor_ID()));
        assertEquals(6, (int) result.getValue(1, TAuthor_ID()));
        assertEquals("Hesse", result.getValue(0, TAuthor_LAST_NAME()));
        assertEquals("Frisch", result.getValue(1, TAuthor_LAST_NAME()));
        assertEquals(null, result.getValue(0, TAuthor_FIRST_NAME()));
        assertEquals(dialect().family() == ORACLE ? null : "", result.getValue(1, TAuthor_FIRST_NAME()));

        assertEquals(2, create().delete(TAuthor()).where(TAuthor_ID().in(5, 6)).execute());

        // Update duplicate records
        // ------------------------
        switch (dialect()) {
            case ASE:
            case DERBY:
            case H2:
            case INGRES:
            case POSTGRES:
            case SQLITE:
                // TODO [#558] Simulate this
                log.info("SKIPPING", "Duplicate record insertion");
                break;

            default: {
                loader =
                create().loadInto(TAuthor())
                        .onDuplicateKeyUpdate()
                        .loadCSV(
                            "\"ID\",\"First Qualifier\",\"Last Qualifier\"\r" +
                            "1,Hermann,Hesse\n" +
                            "7,\"Max\",Frisch")
                        .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                        .execute();

                assertEquals(2, loader.processed());
                assertEquals(2, loader.stored());
                assertEquals(0, loader.ignored());
                assertEquals(0, loader.errors().size());

                result =
                create().selectFrom(TAuthor())
                        .where(TAuthor_LAST_NAME().in("Hesse", "Frisch"))
                        .orderBy(TAuthor_ID())
                        .fetch();

                assertEquals(2, result.size());
                assertEquals(1, (int) result.getValue(0, TAuthor_ID()));
                assertEquals(7, (int) result.getValue(1, TAuthor_ID()));
                assertEquals("Hesse", result.getValue(0, TAuthor_LAST_NAME()));
                assertEquals("Frisch", result.getValue(1, TAuthor_LAST_NAME()));
                assertEquals("George", result.getValue(0, TAuthor_FIRST_NAME()));
                assertEquals(null, result.getValue(1, TAuthor_FIRST_NAME()));

                assertEquals(1, create().delete(TAuthor()).where(TAuthor_ID().in(7)).execute());
            }
        }

        // [#812] Reset stale connection. Seems to be necessary in Postgres
        resetLoaderConnection();

        // Rollback on duplicate keys
        // --------------------------
        loader =
        create().loadInto(TAuthor())
                .commitAll()
                .onDuplicateKeyError()
                .onErrorAbort()
                .loadCSV(
                    "\"ID\",\"First Qualifier\",\"Last Qualifier\"\r" +
                    "8,Hermann,Hesse\n" +
                    "1,\"Max\",Frisch\n" +
                    "2,Friedrich,Dürrenmatt")
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();

        assertEquals(2, loader.processed());
        assertEquals(0, loader.stored());
        assertEquals(1, loader.ignored());
        assertEquals(1, loader.errors().size());
        assertEquals(1, loader.errors().get(0).rowIndex());
        assertEquals(
            Arrays.asList("1", "Max", "Frisch"),
            Arrays.asList(loader.errors().get(0).row()));

        result =
        create().selectFrom(TAuthor())
                .where(TAuthor_ID().in(8))
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(0, result.size());

        // Commit and ignore duplicates
        // ----------------------------
        loader =
        create().loadInto(TAuthor())
                .commitAll()
                .onDuplicateKeyIgnore()
                .onErrorAbort()
                .loadCSV(
                    "\"ID\",\"First Qualifier\",\"Last Qualifier\"\r" +
                    "8,Hermann,Hesse\n" +
                    "1,\"Max\",Frisch\n" +
                    "2,Friedrich,Dürrenmatt")
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
                .execute();

        assertEquals(3, loader.processed());
        assertEquals(1, loader.stored());
        assertEquals(2, loader.ignored());
        assertEquals(0, loader.errors().size());

        result =
        create().selectFrom(TAuthor())
                .where(TAuthor_ID().in(1, 2, 8))
                .orderBy(TAuthor_ID())
                .fetch();

        assertEquals(3, result.size());
        assertEquals(8, (int) result.getValue(2, TAuthor_ID()));
        assertNull(result.getValue(2, TAuthor_FIRST_NAME()));
        assertEquals("Hesse", result.getValue(2, TAuthor_LAST_NAME()));
        assertEquals("Coelho", result.getValue(1, TAuthor_LAST_NAME()));
    }


    private void resetLoaderConnection() throws SQLException {
        jOOQAbstractTest.connection.rollback();
        jOOQAbstractTest.connection.close();
        jOOQAbstractTest.connection = null;
        jOOQAbstractTest.connectionInitialised = false;
        jOOQAbstractTest.connection = delegate.getConnection();
        jOOQAbstractTest.connection.setAutoCommit(false);
    }
}
