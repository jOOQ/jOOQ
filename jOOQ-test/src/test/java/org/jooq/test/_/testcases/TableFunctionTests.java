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
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.dual;
import static org.jooq.impl.DSL.generateSeries;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.selectOne;
import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.List;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class TableFunctionTests<
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

    public TableFunctionTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testDualImplicit() throws Exception {
        assertEquals(1, (int) create().selectOne().fetchOne(0, Integer.class));
        assertEquals(1, (int) create().selectOne().where(one().equal(1)).fetchOne(0, Integer.class));
    }

    public void testDualExplicit() throws Exception {
        assertEquals("X", create().selectFrom(dual()).fetchOne(0, String.class));
        assertEquals(
            asList("X", "X", "X"),
            asList(create().select()
                           .from(
                               dual().as("a"),
                               dual().as("b"),
                               dual().as("c"))
                           .fetchOne().intoArray()));
    }

    public void testGenerateSeries() throws Exception {
        if (!asList(CUBRID, POSTGRES).contains(dialect().family()))
            return;

        List<Integer> expected = asList(0, 1, 2, 3);
        Table<Record1<Integer>> series = generateSeries(0, 3);
        Table<Record1<Integer>> t = series.as("t", "a");

        assertEquals(
            expected,
            create().select()
                    .from(series)
                    .orderBy(1)
                    .fetch(0, Integer.class));
        assertEquals(
            0, (int)
            create().selectFrom(series)
                    .orderBy(1)
                    .fetch()
                    .get(0)
                    .value1());
        assertEquals(
            expected,
            create().select()
                    .from(t)
                    .orderBy(t.field("a"))
                    .fetch(t.field("a")));

        assertEquals(3, create().fetchCount(
            selectOne()
            .from(t)
            .join(TBook())
            .on(TBook_ID().eq(t.field("a").coerce(Integer.class)))
        ));
    }
}
