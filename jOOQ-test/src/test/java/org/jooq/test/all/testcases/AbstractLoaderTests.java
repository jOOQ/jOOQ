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

import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.impl.DSL.count;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

/**
 * @author Johannes Buehler
 * @author Lukas Eder
 */
public abstract class AbstractLoaderTests<
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

    public AbstractLoaderTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testLoader() throws Exception {
        jOOQAbstractTest.reset = false;
        jOOQAbstractTest.connection.setAutoCommit(false);

        Field<Integer> count = count();

        // Empty CSV file
        // --------------
        Loader<A> loader = createForEmptyFile();

        assertEquals(0, loader.processed());
        assertEquals(0, loader.errors().size());
        assertEquals(0, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Constraint violations (LAST_NAME is NOT NULL)
        // Loading is aborted
        // ---------------------------------------------
        loader = createLoaderAbortingOnConstraintViolationOnLAST_NAME();

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
        loader = createLoaderIgnoringConstraintViolationOnLAST_NAME();

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
        loader = createLoaderAbortingOnDuplicateRecords();

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
        loader = createLoaderIgnoringDuplicateRecords();

        assertEquals(2, loader.processed());
        assertEquals(0, loader.errors().size());
        assertEquals(2, loader.ignored());
        assertEquals(2, (int) create().select(count).from(TAuthor()).fetchOne(count));

        // Two records with different NULL representations for FIRST_NAME
        // --------------------------------------------------------------
        loader = createLoaderWithDifferentNulls();

        assertEquals(2, loader.processed());
        assertEquals(2, loader.stored());
        assertEquals(0, loader.ignored());
        assertEquals(0, loader.errors().size());


        boolean oracle = false;
        /* [pro] */
        if (dialect().family() == ORACLE)
            oracle = true;
        /* [/pro] */

        assertEquals(2, (int) create().select(count)
                .from(TAuthor())
                .where(TAuthor_ID().in(3, 4))
                .and(TAuthor_LAST_NAME().in("Hesse", "Frisch"))
                .and(oracle ?
                        TAuthor_FIRST_NAME().isNull() :
                        TAuthor_FIRST_NAME().equal(""))
                .fetchOne(count));

        assertEquals(2, create().delete(TAuthor()).where(TAuthor_ID().in(3, 4)).execute());

        // Two records but don't load one column, and specify a value for NULL
        // -------------------------------------------------------------------
        loader = createLoaderButDontLoadAllColumns();

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
        assertNull(result.getValue(0, TAuthor_FIRST_NAME()));
        assertEquals(oracle ? null : "", result.getValue(1, TAuthor_FIRST_NAME()));

        assertEquals(2, create().delete(TAuthor()).where(TAuthor_ID().in(5, 6)).execute());

        // Update duplicate records
        // ------------------------
        switch (family()) {
            /* [pro] */
            case ACCESS:
            case ASE:
            case INGRES:
            /* [/pro] */
            case DERBY:
            case FIREBIRD:
            case HANA:
            case H2:
            case SQLITE:
                // TODO [#558] Simulate this
                log.info("SKIPPING", "Duplicate record insertion");
                break;

            default: {
                loader = createLoaderUpdatingDuplicateRecords();

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
        loader = createLoaderWithRollbackOnDuplicateKeys();

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
        loader = createLoaderCommittingAndIgnoringDuplicates();

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

    protected abstract Loader<A> createLoaderWithRollbackOnDuplicateKeys() throws java.io.IOException;

    protected abstract Loader<A> createLoaderUpdatingDuplicateRecords() throws java.io.IOException;

    protected abstract Loader<A> createLoaderButDontLoadAllColumns() throws java.io.IOException;

    protected abstract Loader<A> createLoaderWithDifferentNulls() throws java.io.IOException;

    protected abstract Loader<A> createLoaderIgnoringDuplicateRecords() throws java.io.IOException;

    protected abstract Loader<A> createLoaderAbortingOnDuplicateRecords() throws java.io.IOException;

    protected abstract Loader<A> createLoaderIgnoringConstraintViolationOnLAST_NAME() throws java.io.IOException;

    protected abstract Loader<A> createLoaderCommittingAndIgnoringDuplicates() throws java.io.IOException;

    protected abstract Loader<A> createLoaderAbortingOnConstraintViolationOnLAST_NAME() throws java.io.IOException;

    protected abstract Loader<A> createForEmptyFile() throws java.io.IOException;

    private void resetLoaderConnection() throws SQLException {
        jOOQAbstractTest.connection.rollback();
        jOOQAbstractTest.connection.close();
        jOOQAbstractTest.connection = null;
        jOOQAbstractTest.connectionInitialised = false;
        jOOQAbstractTest.connection = delegate.getConnection();
        jOOQAbstractTest.connection.setAutoCommit(false);
    }
}
