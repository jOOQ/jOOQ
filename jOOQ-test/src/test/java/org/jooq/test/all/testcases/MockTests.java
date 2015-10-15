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

import static org.jooq.impl.DSL.count;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.jdbc.MockConfiguration;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

/**
 * @author Lukas Eder
 */
public class MockTests<
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

    public MockTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testResultCache() {
        jOOQAbstractTest.reset = false;

        ResultCache cache = new ResultCache(getConnection());
        DSLContext cached = DSL.using(new MockConfiguration(
            create().configuration(),
            cache
        ));

        assertEquals(AUTHOR_IDS, cached
            .select(TAuthor_ID())
            .from(TAuthor())
            .orderBy(TAuthor_ID())
            .fetch()
            .map(Record1::value1));

        assertEquals(AUTHOR_FIRST_NAMES, cached
            .select(TAuthor_FIRST_NAME())
            .from(TAuthor())
            .orderBy(TAuthor_FIRST_NAME())
            .fetch()
            .map(Record1::value1));

        assertEquals(2, cached.fetchCount(TAuthor()));

        // Non-cached connection:
        assertEquals(1, newAuthor(5).insert());
        assertEquals(2, cached.fetchCount(TAuthor()));
        assertEquals(3, cached.select(count(), count()).from(TAuthor()).fetchOne().value1());
        assertEquals(3, create().fetchCount(TAuthor()));

        assertEquals(1, newAuthor(6).insert());
        assertEquals(2, cached.fetchCount(TAuthor()));
        assertEquals(3, cached.select(count(), count()).from(TAuthor()).fetchOne().value1());
        assertEquals(4, create().fetchCount(TAuthor()));

        // Still the same values in the cache
        assertEquals(AUTHOR_IDS, cached
            .select(TAuthor_ID())
            .from(TAuthor())
            .orderBy(TAuthor_ID())
            .fetch()
            .map(Record1::value1));

        assertEquals(AUTHOR_FIRST_NAMES, cached
            .select(TAuthor_FIRST_NAME())
            .from(TAuthor())
            .orderBy(TAuthor_FIRST_NAME())
            .fetch()
            .map(Record1::value1));
    }

    public void testResultCacheWithMockAPI() {
        ResultCache cache = new ResultCache(getConnection());

        assertEquals(AUTHOR_IDS, create().mockResult(cache, c -> DSL.using(c)
            .select(TAuthor_ID())
            .from(TAuthor())
            .orderBy(TAuthor_ID())
            .fetch()
            .map(Record1::value1)));

        create().mock(cache, c -> {
            assertEquals(AUTHOR_FIRST_NAMES, DSL.using(c)
                .select(TAuthor_FIRST_NAME())
                .from(TAuthor())
                .orderBy(TAuthor_FIRST_NAME())
                .fetch()
                .map(Record1::value1));

            assertEquals(2, DSL.using(c).fetchCount(TAuthor()));
        });

        // Non-cached connection:
        assertEquals(1, newAuthor(5).insert());

        assertEquals(2, (int) create().mockResult(cache, c -> DSL.using(c).fetchCount(TAuthor())));
        assertEquals(3, (int) create().mockResult(cache, c -> DSL.using(c).select(count(), count()).from(TAuthor()).fetchOne().value1()));
        assertEquals(3, create().fetchCount(TAuthor()));

        assertEquals(1, newAuthor(6).insert());

        create().mock(cache, c -> {
            assertEquals(2, DSL.using(c).fetchCount(TAuthor()));
            assertEquals(3, DSL.using(c).select(count(), count()).from(TAuthor()).fetchOne().value1());

            // Original configuration unmodified...
            assertEquals(4, create().fetchCount(TAuthor()));

            // Still the same values in the cache
            assertEquals(AUTHOR_IDS, DSL.using(c)
                .select(TAuthor_ID())
                .from(TAuthor())
                .orderBy(TAuthor_ID())
                .fetch()
                .map(Record1::value1));

            assertEquals(AUTHOR_FIRST_NAMES, DSL.using(c)
                .select(TAuthor_FIRST_NAME())
                .from(TAuthor())
                .orderBy(TAuthor_FIRST_NAME())
                .fetch()
                .map(Record1::value1));
        });
    }

    class ResultCache implements MockDataProvider {
        final Map<String, Result<?>> cache = new ConcurrentHashMap<>();
        final Connection connection;

        ResultCache(Connection connection) {
            this.connection = connection;
        }

        @Override
        public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
            Result<?> result;

            if (ctx.sql().matches("select.*?" + TAuthor().getName() + ".*?")) {
                result = cache.computeIfAbsent(
                    ctx.sql(),
                    sql -> DSL.using(connection).fetch(
                        ctx.sql(),
                        ctx.bindings()
                    )
                );
            }
            else {

                result = DSL.using(connection).fetch(ctx.sql(), ctx.bindings());
            }

            return new MockResult[] { new MockResult(result.size(), result) };
        }
    }
}
