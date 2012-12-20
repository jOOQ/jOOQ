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

import java.sql.Connection;
import java.sql.Date;
import java.util.Arrays;

import org.jooq.Batch;
import org.jooq.ExecuteContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class BatchTests<
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
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T725, T639, T785> {

    public BatchTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    public static class ConnectionProviderListener extends DefaultExecuteListener {

        static Connection c;

        @Override
        public void start(ExecuteContext ctx) {
            ctx.connectionProvider(new DefaultConnectionProvider(c));
        }
    }

    @Test
    public void testBatchSingle() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#1749] TODO Firebird renders CAST(? as VARCHAR(...)) bind values with sizes
        // pre-calculated. Hence the param needs to have some min length...
        Batch batch = create().batch(create().insertInto(TAuthor())
                                             .set(TAuthor_ID(), 8)
                                             .set(TAuthor_LAST_NAME(), "           "))
                              .bind(8, "Gamma")
                              .bind(9, "Helm")
                              .bind(10, "Johnson");

        assertEquals(3, batch.size());

        int[] result = batch.execute();
        assertEquals(3, result.length);
        testBatchAuthors("Gamma", "Helm", "Johnson");
    }

    @Test
    public void testBatchMultiple() throws Exception {
        jOOQAbstractTest.reset = false;

        Batch batch = create().batch(
            create().insertInto(TAuthor())
                    .set(TAuthor_ID(), 8)
                    .set(TAuthor_LAST_NAME(), "Gamma"),

            create().insertInto(TAuthor())
                    .set(TAuthor_ID(), 9)
                    .set(TAuthor_LAST_NAME(), "Helm"),

            create().insertInto(TBook())
                    .set(TBook_ID(), 6)
                    .set(TBook_AUTHOR_ID(), 8)
                    .set(TBook_PUBLISHED_IN(), 1994)
                    .set(TBook_LANGUAGE_ID(), 1)
                    .set(TBook_CONTENT_TEXT(), "Design Patterns are awesome")
                    .set(TBook_TITLE(), "Design Patterns"),

            create().insertInto(TAuthor())
                    .set(TAuthor_ID(), 10)
                    .set(TAuthor_LAST_NAME(), "Johnson"));

        assertEquals(4, batch.size());

        int[] result = batch.execute();
        assertEquals(4, result.length);
        assertEquals(5, create().fetch(TBook()).size());
        assertEquals(1, create().fetch(TBook(), TBook_AUTHOR_ID().equal(8)).size());
        testBatchAuthors("Gamma", "Helm", "Johnson");
    }

    @Test
    public void testBatchStore() throws Exception {
        jOOQAbstractTest.reset = false;

        // First, INSERT two authors and one book
        // --------------------------------------
        A a1 = create().newRecord(TAuthor());
        a1.setValue(TAuthor_ID(), 8);
        a1.setValue(TAuthor_LAST_NAME(), "XX");

        A a2 = create().newRecord(TAuthor());
        a2.setValue(TAuthor_ID(), 9);
        a2.setValue(TAuthor_LAST_NAME(), "YY");

        B b1 = create().newRecord(TBook());
        b1.setValue(TBook_ID(), 80);
        b1.setValue(TBook_AUTHOR_ID(), 8);
        b1.setValue(TBook_TITLE(), "XX 1");
        b1.setValue(TBook_PUBLISHED_IN(), 2000);
        b1.setValue(TBook_LANGUAGE_ID(), 1);

        Batch batch = create().batchStore(a1, b1, a2);
        assertEquals(3, batch.size());

        int[] result1 = batch.execute();
        assertEquals(3, result1.length);
        testBatchAuthors("XX", "YY");
        assertEquals("XX 1", create()
            .select(TBook_TITLE())
            .from(TBook())
            .where(TBook_ID().equal(80))
            .fetchOne(0));

        // Then, update one author and insert another one
        // ----------------------------------------------
        a2.setValue(TAuthor_LAST_NAME(), "ABC");

        A a3 = create().newRecord(TAuthor());
        a3.setValue(TAuthor_ID(), 10);
        a3.setValue(TAuthor_LAST_NAME(), "ZZ");

        int[] result2 = create().batchStore(b1, a1, a2, a3).execute();
        assertEquals(2, result2.length);
        testBatchAuthors("XX", "ABC", "ZZ");
        assertEquals("XX 1", create()
            .select(TBook_TITLE())
            .from(TBook())
            .where(TBook_ID().equal(80))
            .fetchOne(0));
    }

    @Test
    public void testBatchDelete() throws Exception {
        jOOQAbstractTest.reset = false;

        Result<B> books = create().selectFrom(TBook()).where(TBook_ID().in(1, 3, 4)).fetch();
        Batch batch = create().batchDelete(books);
        assertEquals(3, batch.size());

        int[] result = batch.execute();
        assertEquals(3, result.length);
        assertEquals(1, create().selectFrom(TBook()).fetch().size());
    }

    private void testBatchAuthors(String... names) throws Exception {
        assertEquals(names.length == 3 ? 5 : 4, create().fetch(TAuthor()).size());

        assertEquals(
             names.length == 3
                 ? Arrays.asList(8, 9, 10)
                 : Arrays.asList(8, 9),
             create().select(TAuthor_ID())
                     .from(TAuthor())
                     .where(TAuthor_ID().in(8, 9, 10))
                     .orderBy(TAuthor_ID())
                     .fetch(TAuthor_ID()));

        assertEquals(Arrays.asList(names),
            create().select(TAuthor_LAST_NAME())
                    .from(TAuthor())
                    .where(TAuthor_ID().in(8, 9, 10))
                    .orderBy(TAuthor_ID())
                    .fetch(TAuthor_LAST_NAME()));
    }
}
