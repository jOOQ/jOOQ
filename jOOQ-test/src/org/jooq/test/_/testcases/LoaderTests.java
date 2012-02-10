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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.jooq.impl.Factory.count;

import java.sql.SQLException;
import java.util.Arrays;

import org.jooq.Field;
import org.jooq.Loader;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class LoaderTests<
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

    public LoaderTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
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

        // Two records
        // -----------
        loader =
        create().loadInto(TAuthor())
                .loadCSV(
                    "####Some Data####\n" +
                    "\"ID\",\"Last Qualifier\"\r" +
                    "3,Hesse\n" +
                    "4,Frisch")
                .fields(TAuthor_ID(), TAuthor_LAST_NAME())
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
                                      .fetchOne(count));

        assertEquals(2, create().delete(TAuthor()).where(TAuthor_ID().in(3, 4)).execute());

        // Two records but don't load one column
        // -------------------------------------
        loader =
        create().loadInto(TAuthor())
                .loadCSV(
                    "\"ID\",\"First Qualifier\",\"Last Qualifier\"\r" +
                    "5,Hermann,Hesse\n" +
                    "6,\"Max\",Frisch")
                .fields(TAuthor_ID(), null, TAuthor_LAST_NAME())
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
        assertEquals(null, result.getValue(1, TAuthor_FIRST_NAME()));

        assertEquals(2, create().delete(TAuthor()).where(TAuthor_ID().in(5, 6)).execute());

        // Update duplicate records
        // ------------------------
        switch (getDialect()) {
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
