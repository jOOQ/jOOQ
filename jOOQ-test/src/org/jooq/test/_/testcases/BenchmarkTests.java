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

import static org.jooq.conf.ParamType.INDEXED;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.ExecuteListenerProvider;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.StopWatch;

import org.junit.Test;

public class BenchmarkTests<
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

    private static final int    REPETITIONS_NEW_RECORD   = 1000000;
    private static final int    REPETITIONS_RECORD_INTO  = 2000;
    private static final int    REPETITIONS_FIELD_ACCESS = 1000000;
    private static final int    REPETITIONS_SELECT       = 100;
    private static final String RANDOM                   = "" + new Random().nextLong();

    public BenchmarkTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testBenchmarkNewRecord() throws Exception {
        DSLContext create = create();

        for (int i = 0; i < REPETITIONS_NEW_RECORD; i++) {
            create.newRecord(TBook());
        }
    }

    @Test
    public void testBenchmarkRecordInto() throws Exception {
        Result<B> books = create().fetch(TBook());

        for (int i = 0; i < REPETITIONS_RECORD_INTO; i++) {
            books.into(TBook().getRecordType());
        }
    }

    @Test
    public void testBenchmarkFieldAccess() throws Exception {
        // This benchmark is inspired by a private contribution by Roberto Giacco

        B book = create().newRecord(TBook());

        for (int i = 0; i < REPETITIONS_FIELD_ACCESS; i++) {
            book.setValue(TBook_ID(), i);
            book.setValue(TBook_AUTHOR_ID(), book.getValue(TBook_ID()));

            book.setValue(TBook_PUBLISHED_IN(), i);
            book.setValue(TBook_PUBLISHED_IN(), book.getValue(TBook_PUBLISHED_IN()));
        }
    }

    @Test
    public void testBenchmarkSelect() throws Exception {
        // This benchmark is contributed by "jjYBdx4IL" on GitHub:
        // https://github.com/jOOQ/jOOQ/issues/1625

        Configuration configuration = create().configuration().derive(new ExecuteListenerProvider[0]);
        configuration.settings().setExecuteLogging(false);
        DSLContext create = create(configuration);

        // Dry-run to avoid side-effects
        testBenchmarkFullExecution(create, 1);
        testBenchmarkReuseSelect(create, 1);
        testBenchmarkReuseSQLString(create, 1);

        // System.in.read();
        StopWatch watch = new StopWatch();
        watch.splitInfo("Benchmark start");

        testBenchmarkFullExecution(create, REPETITIONS_SELECT);
        watch.splitInfo("Full re-execution");

        testBenchmarkReuseSelect(create, REPETITIONS_SELECT);
        watch.splitInfo("Reuse select");

        testBenchmarkReuseSQLString(create, REPETITIONS_SELECT);
        watch.splitInfo("Reuse SQL String");
    }

    private void testBenchmarkReuseSQLString(DSLContext create, int repetitions) throws Exception {
        String sql = createSelect(create).getSQL(INDEXED);
        PreparedStatement pst = getConnection().prepareStatement(sql);
        pst.setLong(1, 1);
        pst.setString(2, RANDOM);

        for (int i = 0; i < repetitions; i++) {
            ResultSet rs = pst.executeQuery();
            create.fetch(rs);
            rs.close();
        }

        pst.close();
    }

    private void testBenchmarkReuseSelect(DSLContext create, int repetitions) {
        Select<?> scs = createSelect(create);

        for (int i = 0; i < repetitions; i++) {
            scs.execute();
        }
    }

    private void testBenchmarkFullExecution(DSLContext create, int repetitions) {
        for (int i = 0; i < repetitions; i++) {
            createSelect(create).execute();
        }
    }

    private Select<?> createSelect(DSLContext create) {
        return create.select()
                     .from(TBook())
                     .where(TBook_ID().equal(1))
                     .and(TBook_TITLE().isNull().or(TBook_TITLE().notEqual(RANDOM)));
    }
}
