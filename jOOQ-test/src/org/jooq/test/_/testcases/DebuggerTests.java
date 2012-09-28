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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.jooq.impl.Factory.inline;
import static org.jooq.tools.debug.impl.DebuggerAPI.localDebugger;
import static org.jooq.tools.debug.impl.DebuggerAPI.remoteDebugger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.debug.Action;
import org.jooq.tools.debug.Breakpoint;
import org.jooq.tools.debug.BreakpointListener;
import org.jooq.tools.debug.Debugger;
import org.jooq.tools.debug.Executor;
import org.jooq.tools.debug.HitContext;
import org.jooq.tools.debug.Logger;
import org.jooq.tools.debug.LoggerListener;
import org.jooq.tools.debug.Matcher;
import org.jooq.tools.debug.Processor;
import org.jooq.tools.debug.QueryLog;
import org.jooq.tools.debug.ResultLog;
import org.jooq.tools.debug.Step;
import org.jooq.tools.debug.impl.DebuggerAPI;

import org.junit.Test;

public class DebuggerTests<
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

    public DebuggerTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    private static boolean initialiseOnce = false;

    void run(DebugTestRunnable runnable) throws Exception {
        System.setProperty("communication.interface.syncmessages", "true");

        // Be sure that DebugListener has successfully registered a server instance
        if (!initialiseOnce) {
            initialiseOnce = true;

            create().selectOne().fetch();
            Thread.sleep(1000);
        }

        Debugger remote = remoteDebugger("localhost", jOOQAbstractTest.DEBUGGER_PORT);
        Debugger local = localDebugger();

        try {
            runnable.run(remote, local);
        }
        finally {
            remote.remove();
            local.remove();
        }
    }

    interface DebugTestRunnable {
        void run(Debugger remote, Debugger local) throws Exception;
    }

    @Test
    public void testDebuggerMatchCount() throws Exception {
        class Count234 implements LoggerListener {

            List<String> queries = new ArrayList<String>(Arrays.asList("0, 1", "0, 1, 2", "0, 1, 2, 3, 4"));
            List<Integer> columns = new ArrayList<Integer>(Arrays.asList(2, 3, 5));

            @Override
            public void logQuery(QueryLog l) {
                assertTrue(l.getQuery().getSQL().contains(queries.remove(0)));
            }

            @Override
            public void logResult(ResultLog l) {
                assertEquals((int) columns.remove(0), l.columns());
            }

        }

        run(new DebugTestRunnable() {

            @Override
            public void run(Debugger d1, Debugger d2) throws Exception {
                Matcher matcher = d1.newMatcher();
                matcher.matchCount(2, 3, 5);
                matcher.newLogger().listener(new Count234());

                List<Field<?>> fields = new ArrayList<Field<?>>();
                for (int i = 0; i < 10; i++) {
                    fields.add(inline(i));
                    create().select(fields).fetch();
                }
            }
        });
    }

    @Test
    public void testDebuggerBreakpoint() throws Exception {
        run(new DebugTestRunnable() {

            @Override
            public void run(Debugger d1, Debugger d2) throws Exception {
                Matcher d1m1 = d1.newMatcher();
                Matcher d2m1 = d2.newMatcher();

                d1m1.newLogger().listener(new LListener(null));

                Breakpoint d1m1b1 = d1m1.newBreakpoint();
                Breakpoint d2m1b1 = d2m1.newBreakpoint();

                d1m1b1.listener(new BListener());
                d2m1b1.listener(new BListener());

                create().select().from(TAuthor()).fetch();
            }
        });
    }

    @Test
    public void testDebuggerProcessor() throws Exception {
        jOOQAbstractTest.reset = false;

        run(new DebugTestRunnable() {

            @Override
            public void run(Debugger d1, Debugger d2) throws Exception {
                Matcher d1m1 = d1.newMatcher();
                Matcher d2m1 = d2.newMatcher();

                d1m1.newLogger().listener(new LListener(null));

                Processor d1m1p1 = d1m1.newProcessor();
                Processor d2m1p1 = d2m1.newProcessor();

                Action d1m1p1before = d1m1p1.newBefore();
                Action d2m1p1after = d2m1p1.newAfter();

                d1m1p1before.query(create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_LAST_NAME()).values(3, "Hesse"));
                d2m1p1after.query(create().delete(TAuthor()).where(TAuthor_ID().equal(3)));

                Result<A> result1 =
                    create().selectFrom(TAuthor())
                    .orderBy(TAuthor_ID())
                    .fetch();

                assertEquals(3, result1.size());

                d1m1p1.remove();
                d2m1p1.remove();

                Result<A> result2 =
                    create().selectFrom(TAuthor())
                    .orderBy(TAuthor_ID())
                    .fetch();

                assertEquals(2, result2.size());
            }
        });
    }

    @Test
    public void testDebuggerProcessorInstead() throws Exception {
        jOOQAbstractTest.reset = false;

        run(new DebugTestRunnable() {

            @Override
            public void run(Debugger d1, Debugger d2) throws Exception {
                Matcher matcher = d1.newMatcher();
                matcher.matchSQL("^(?i:delete.*book.*)$");
                matcher.newLogger().listener(new LListener(null));

                Processor processor = matcher.newProcessor();
                Action action = processor.newInstead();

                action.query(create().selectOne());

                assertEquals(0, create().delete(TBook()).execute());
                assertEquals(4, (int) create().selectCount().from(TBook()).fetchOne(0, Integer.class));

                action.remove();
                assertEquals(4, create().delete(TBook()).execute());
                assertEquals(0, (int) create().selectCount().from(TBook()).fetchOne(0, Integer.class));
            }
        });
    }

    @Test
    public void testDebuggerExecutor() throws Exception {

        // This is local to the server-side!
        if (schema() != null) {
            DebuggerAPI.setExecutor("GENERATED", create(), schema());
        }
        DebuggerAPI.setExecutor("META", create());

        try {
            run(new DebugTestRunnable() {

                @Override
                public void run(Debugger remote, Debugger local) throws Exception {
                    Executor[] generated = { remote.executor("GENERATED"), local.executor("GENERATED") };
                    Executor[] meta = { remote.executor("META"), local.executor("META") };

                    for (Executor exe : generated) {
                        assertEquals("GENERATED", exe.getName());
                        executorTests(exe);
                    }

                    for (Executor exe : meta) {
                        assertEquals("META", exe.getName());
                        executorTests(exe);
                    }
                }
            });
        }
        finally {
            DebuggerAPI.removeExecutor("GENERATED");
            DebuggerAPI.removeExecutor("META");
        }
    }

    private void executorTests(Executor executor) {
        if (schema() != null) {
            List<Schema> schemata = Arrays.asList(executor.getSchemata());
            assertTrue(schemata.contains(schema()));
        }

        List<Table<?>> tables1 = Arrays.asList(executor.getTables());
        assertTrue(tables1.contains(TBook()));
        assertTrue(tables1.contains(TAuthor()));

        if (schema() != null) {
            List<Table<?>> tables2 = Arrays.asList(executor.getTables(schema()));
            assertTrue(tables2.contains(TBook()));
            assertTrue(tables2.contains(TAuthor()));
        }

        List<Field<?>> fields1 = Arrays.asList(executor.getFields());
        assertTrue(fields1.contains(TBook_ID()));
        assertTrue(fields1.contains(TBook_TITLE()));
        assertTrue(fields1.contains(TAuthor_ID()));

        List<Field<?>> fields2 = Arrays.asList(executor.getFields(TBook()));
        assertTrue(fields2.contains(TBook_ID()));
        assertTrue(fields2.contains(TBook_TITLE()));
        assertFalse(fields2.contains(TAuthor_ID()));

        Result<Record> result =
        executor.fetch(create().selectCount().from(TAuthor()));

        assertEquals(1, result.size());
        assertEquals(2, (int) result.get(0).getValue(0, int.class));
    }

    @Test
    public void testDebuggerLogger() throws Exception {
        run(new DebugTestRunnable() {

            @Override
            public void run(Debugger d1, Debugger d2) throws Exception {
                Matcher d1m1 = d1.newMatcher();
                Matcher d1m2 = d1.newMatcher();
                Matcher d2m1 = d2.newMatcher();

                d1m1.matchSQL("(?i:.*book.*)");
                d1m2.matchSQL("(?i:.*id.*)");
                d2m1.matchSQL("(?i:^select.*)");

                Logger d1m1l1 = d1m1.newLogger();
                Logger d1m1l2 = d1m1.newLogger();
                LListener d1m1l1listener = new LListener(LListenerType.BOOK);
                LListener d1m1l2listener = new LListener(LListenerType.BOOK);
                d1m1l1.listener(d1m1l1listener);
                d1m1l2.listener(d1m1l2listener);

                Logger d1m2l1 = d1m2.newLogger();
                Logger d1m2l2 = d1m2.newLogger();
                LListener d1m2l1listener = new LListener(LListenerType.ID);
                LListener d1m2l2listener = new LListener(LListenerType.ID);
                d1m2l1.listener(d1m2l1listener);
                d1m2l2.listener(d1m2l2listener);

                Logger d2m1l1 = d2m1.newLogger();
                Logger d2m1l2 = d2m1.newLogger();
                LListener d2m1l1listener = new LListener(LListenerType.SELECT);
                LListener d2m1l2listener = new LListener(LListenerType.SELECT);
                d2m1l1.listener(d2m1l1listener);
                d2m1l2.listener(d2m1l2listener);

                create().select().from(TBook()).where(TBook_ID().in(1, 3, 4)).fetch();
                create().select().from(TAuthor()).fetch();
            }
        });
    }

    class BListener implements BreakpointListener {

        @Override
        public Step before(HitContext context) {
            executorTests(context.executor());
            return Step.STEP;
        }

        @Override
        public Step after(HitContext context) throws Exception {
            executorTests(context.executor());
            return null;
        }
    }

    enum LListenerType {
        BOOK,
        ID,
        SELECT
    }

    class LListener implements LoggerListener {
        private final LListenerType type;

        LListener(LListenerType type) {
            this.type = type;
        }

        @Override
        public void logResult(ResultLog result) {
            System.out.println(result);
        }

        @Override
        public void logQuery(QueryLog query) {
            System.out.println(query);
        }
    }
}
