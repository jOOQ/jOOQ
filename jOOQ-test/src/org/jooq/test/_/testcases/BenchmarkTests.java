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

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.Random;

import org.jooq.Select;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.Factory;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.StopWatch;

import org.junit.Test;

public class BenchmarkTests<
    A    extends UpdatableRecord<A>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> {

    private static final int    REPETITIONS = 4000;
    private static final String RANDOM      = "" + new Random().nextLong();

    public BenchmarkTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testBenchmark() throws Exception {
        // This benchmark is contributed by "jjYBdx4IL" on GitHub:
        // https://github.com/jOOQ/jOOQ/issues/1625


        Factory create = create();
        create.getSettings().setExecuteLogging(false);
        create.getSettings().setExecuteListeners(Collections.<String>emptyList());

        // Dry-run to avoid side-effects
        testBenchmarkFullExecution(create, 1);
        testBenchmarkReuseSelect(create, 1);
        testBenchmarkReuseSQLString(create, 1);

        System.in.read();
        StopWatch watch = new StopWatch();
        watch.splitInfo("Benchmark start");

        testBenchmarkFullExecution(create, REPETITIONS);
        watch.splitInfo("Full re-execution");

        testBenchmarkReuseSelect(create, REPETITIONS);
        watch.splitInfo("Reuse select");

        testBenchmarkReuseSQLString(create, REPETITIONS);
        watch.splitInfo("Reuse SQL String");
    }

    private void testBenchmarkReuseSQLString(Factory create, int repetitions) throws Exception {
        String sql = createSelect(create).getSQL(false);
        PreparedStatement pst = getConnection().prepareStatement(sql);
        pst.setLong(1, 1);
        pst.setString(2, RANDOM);

        for (int i = 0; i < repetitions; i++) {
            pst.executeQuery();
        }

        pst.close();
    }

    private void testBenchmarkReuseSelect(Factory create, int repetitions) {
        Select<?> scs = createSelect(create);

        for (int i = 0; i < repetitions; i++) {
            scs.execute();
        }
    }

    private void testBenchmarkFullExecution(Factory create, int repetitions) {
        for (int i = 0; i < repetitions; i++) {
            createSelect(create).execute();
        }
    }

    private Select<?> createSelect(Factory create) {
        return create.select()
                     .from(TBook())
                     .where(TBook_ID().equal(1))
                     .and(TBook_TITLE().isNull().or(TBook_TITLE().notEqual(RANDOM)));
    }
}
