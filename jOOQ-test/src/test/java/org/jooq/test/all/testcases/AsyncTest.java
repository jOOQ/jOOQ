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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

/**
 * This test suite checks jOOQ's Java 8 interaction readiness
 *
 * @author Lukas Eder
 */
public class AsyncTest<
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

    public AsyncTest(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testCompletableFuture() {
        jOOQAbstractTest.reset = false;

        assertEquals(1, (int)
        CompletableFuture

            // This will supply an int value indicating the number of inserted rows
            .supplyAsync(() -> create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME()).values(3, "Hitchcock").execute())

            // This will supply a TAuthorRecord value for the newly inserted author
            .handleAsync((rows, throwable) -> {
                assertNull(throwable);
                assertEquals(1, rows.intValue());

                return create().fetchOne(TAuthor(), TAuthor_ID().eq(3));
            })

            // This should supply an int value indicating the number of rows,
            // but in fact it'll throw a constraint violation exception
            .handleAsync((record, throwable) -> {
                assertNull(throwable);
                assertNotNull(record);

                assertEquals(3, (int) record.getValue(TAuthor_ID()));
                assertNull(record.getValue(TAuthor_FIRST_NAME()));
                assertEquals("Hitchcock", record.getValue(TAuthor_LAST_NAME()));

                record.changed(true);
                return record.insert();
            })

            // This will supply an int value indicating the number of deleted rows
            .handleAsync((rows, throwable) -> {
                assertNotNull(throwable);
                assertTrue(throwable instanceof CompletionException);
                assertTrue(throwable.getCause() instanceof DataAccessException);

                return create().delete(TAuthor()).where(TAuthor_ID().eq(3)).execute();
            })

            .join());
    }
}
