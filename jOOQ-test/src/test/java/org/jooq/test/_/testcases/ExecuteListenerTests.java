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
import static org.jooq.conf.SettingsTools.executePreparedStatements;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteType;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

@SuppressWarnings("serial")
public class ExecuteListenerTests<
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

    public ExecuteListenerTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testExecuteListenerRows() throws Exception {
        RowsListener listener1 = new RowsListener();
        DSLContext create1 = create(listener1);

        create1.selectFrom(TBook()).fetch();
        assertEquals(-1, listener1.executeRows);
        assertEquals(4, listener1.fetchRows);


        RowsListener listener2 = new RowsListener();
        DSLContext create2 = create(listener2);

        create2.update(TBook()).set(TBook_TITLE(), "abc").where(falseCondition()).execute();
        assertEquals(0, listener2.executeRows);
        assertEquals(-2, listener2.fetchRows);
    }

    public static class RowsListener extends DefaultExecuteListener {

        int executeRows = -2;
        int fetchRows = -2;

        @Override
        public void executeEnd(ExecuteContext ctx) {
            executeRows = ctx.rows();
        }

        @Override
        public void fetchEnd(ExecuteContext ctx) {
            fetchRows = ctx.rows();
        }
    }

    public void testExecuteListenerWithData() throws Exception {
        DSLContext create = create(new DataListener());
        create.selectOne().fetch();
    }

    public static class DataListener extends DefaultExecuteListener {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7399239846062763212L;

        @Override
        public void start(ExecuteContext ctx) {
            assertTrue(ctx.data().isEmpty());
            assertNull(ctx.data("X"));
            assertNull(ctx.data("X", "start"));
        }

        private void assertThings(ExecuteContext ctx, String oldValue, String newValue) {
            assertFalse(ctx.data().isEmpty());
            assertEquals(1, ctx.data().size());
            assertEquals(oldValue, ctx.data("X"));
            assertEquals(oldValue, ctx.data("X", newValue));
        }

        @Override
        public void renderStart(ExecuteContext ctx) {
            assertThings(ctx, "start", "renderStart");
        }

        @Override
        public void renderEnd(ExecuteContext ctx) {
            assertThings(ctx, "renderStart", "renderEnd");
        }

        @Override
        public void prepareStart(ExecuteContext ctx) {
            assertThings(ctx, "renderEnd", "prepareStart");
        }

        @Override
        public void prepareEnd(ExecuteContext ctx) {
            assertThings(ctx, "prepareStart", "prepareEnd");
        }

        @Override
        public void bindStart(ExecuteContext ctx) {
            assertThings(ctx, "prepareEnd", "bindStart");
        }

        @Override
        public void bindEnd(ExecuteContext ctx) {
            assertThings(ctx, "bindStart", "bindEnd");
        }

        @Override
        public void executeStart(ExecuteContext ctx) {
            if (SettingsTools.executePreparedStatements(ctx.configuration().settings())) {
                assertThings(ctx, "bindEnd", "executeStart");
            }
            else {
                assertThings(ctx, "prepareEnd", "executeStart");
            }
        }

        @Override
        public void executeEnd(ExecuteContext ctx) {
            assertThings(ctx, "executeStart", "executeEnd");
        }

        @Override
        public void fetchStart(ExecuteContext ctx) {
            assertThings(ctx, "executeEnd", "fetchStart");
        }

        @Override
        public void resultStart(ExecuteContext ctx) {
            assertThings(ctx, "fetchStart", "resultStart");
        }

        @Override
        public void recordStart(ExecuteContext ctx) {
            assertThings(ctx, "resultStart", "recordStart");
        }

        @Override
        public void recordEnd(ExecuteContext ctx) {
            assertThings(ctx, "recordStart", "recordEnd");
        }

        @Override
        public void resultEnd(ExecuteContext ctx) {
            assertThings(ctx, "recordEnd", "resultEnd");
        }

        @Override
        public void fetchEnd(ExecuteContext ctx) {
            assertThings(ctx, "resultEnd", "fetchEnd");
        }

        @Override
        public void end(ExecuteContext ctx) {
            assertThings(ctx, "fetchEnd", "end");
        }

        @Override
        public void exception(ExecuteContext ctx) {
            fail();
        }
    }

    public void testExecuteListenerException() throws Exception {
        ExecuteListenerEvents events = new ExecuteListenerEvents();
        DSLContext create = create(events);

        try {
            create.fetch("invalid sql");
            fail();
        }
        catch (DataAccessException e) {

            // Some databases may fail at "prepareStart", some may fail at "executeStart"
            // [#2385] But all should terminate on "exception" and "end"
            assertEquals("exception", events.events.get(events.events.size() - 2));
            assertEquals("end", events.events.get(events.events.size() - 1));
        }

        events.events.clear();

        try {
            create.fetchLazy("invalid sql");
            fail();
        }
        catch (DataAccessException e) {

            // Some databases may fail at "prepareStart", some may fail at "executeStart"
            // [#2385] But all should terminate on "exception" and "end"
            assertEquals("exception", events.events.get(events.events.size() - 2));
            assertEquals("end", events.events.get(events.events.size() - 1));
        }
    }

    public static class ExecuteListenerEvents implements ExecuteListener {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -731317256251936182L;

        public final List<String> events = new ArrayList<String>();

        @Override
        public void start(ExecuteContext ctx) {
            events.add("start");
        }

        @Override
        public void renderStart(ExecuteContext ctx) {
            events.add("renderStart");
        }

        @Override
        public void renderEnd(ExecuteContext ctx) {
            events.add("renderEnd");
        }

        @Override
        public void prepareStart(ExecuteContext ctx) {
            events.add("prepareStart");
        }

        @Override
        public void prepareEnd(ExecuteContext ctx) {
            events.add("prepareEnd");
        }

        @Override
        public void bindStart(ExecuteContext ctx) {
            events.add("bindStart");
        }

        @Override
        public void bindEnd(ExecuteContext ctx) {
            events.add("bindEnd");
        }

        @Override
        public void executeStart(ExecuteContext ctx) {
            events.add("executeStart");
        }

        @Override
        public void executeEnd(ExecuteContext ctx) {
            events.add("executeEnd");
        }

        @Override
        public void fetchStart(ExecuteContext ctx) {
            events.add("fetchStart");
        }

        @Override
        public void resultStart(ExecuteContext ctx) {
            events.add("resultStart");
        }

        @Override
        public void recordStart(ExecuteContext ctx) {
            events.add("recordStart");
        }

        @Override
        public void recordEnd(ExecuteContext ctx) {
            events.add("recordEnd");
        }

        @Override
        public void resultEnd(ExecuteContext ctx) {
            events.add("resultEnd");
        }

        @Override
        public void fetchEnd(ExecuteContext ctx) {
            events.add("fetchEnd");
        }

        @Override
        public void end(ExecuteContext ctx) {
            events.add("end");
        }

        @Override
        public void exception(ExecuteContext ctx) {
            events.add("exception");
        }

        @Override
        public void warning(ExecuteContext ctx) {
            events.add("warning");
        }
    }

    public void testExecuteListenerCustomException() throws Exception {
        DSLContext create = create(new CustomExceptionListener());

        try {
            create.fetch("invalid sql");
            fail();
        }
        catch (E e) {
            assertEquals("ERROR", e.getMessage());
        }
    }

    public static class CustomExceptionListener extends DefaultExecuteListener {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7399239846062763212L;

        @Override
        public void exception(ExecuteContext ctx) {
            ctx.exception(new E("ERROR"));
        }
    }

    public void testExecuteListenerOnResultQuery() throws Exception {
        DSLContext create = create(new ResultQueryListener());

        create.configuration().data("Foo", "Bar");
        create.configuration().data("Bar", "Baz");

        Result<?> result =
        create.select(TBook_ID(), val("Hello"))
              .from(TBook())
              .where(TBook_ID().in(1, 2))
              .fetch();

        // [#1145] When inlining variables, no bind events are triggered
        int plus = (SettingsTools.executePreparedStatements(create.configuration().settings()) ? 2 : 0);

        // Check correct order of listener method invocation
        assertEquals(1, ResultQueryListener.start);
        assertEquals(2, ResultQueryListener.renderStart);
        assertEquals(3, ResultQueryListener.renderEnd);
        assertEquals(4, ResultQueryListener.prepareStart);
        assertEquals(5, ResultQueryListener.prepareEnd);
        assertEquals(plus > 0 ? 6 : 0, ResultQueryListener.bindStart);
        assertEquals(plus > 0 ? 7 : 0, ResultQueryListener.bindEnd);
        assertEquals(6 + plus, ResultQueryListener.executeStart);
        assertEquals(7 + plus, ResultQueryListener.executeEnd);
        assertEquals(8 + plus, ResultQueryListener.fetchStart);
        assertEquals(9 + plus, ResultQueryListener.resultStart);
        assertEquals(asList(10 + plus, 12 + plus), ResultQueryListener.recordStart);
        assertEquals(asList(11 + plus, 13 + plus), ResultQueryListener.recordEnd);
        assertEquals(14 + plus, ResultQueryListener.resultEnd);
        assertEquals(15 + plus, ResultQueryListener.fetchEnd);
        assertEquals(16 + plus, ResultQueryListener.end);
        assertEquals(2, result.size());
    }

    public static class ResultQueryListener extends DefaultExecuteListener {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7399239846062763212L;

        // A counter that is incremented in callback methods
        private static int           callbackCount = 0;

        // Fields that are used to check whether callback methods were called
        // in the expected order
        public static int            start;
        public static int            renderStart;
        public static int            renderEnd;
        public static int            prepareStart;
        public static int            prepareEnd;
        public static int            bindStart;
        public static int            bindEnd;
        public static int            executeStart;
        public static int            executeEnd;
        public static int            fetchStart;
        public static int            resultStart;
        public static List<Integer>  recordStart   = new ArrayList<Integer>();
        public static List<Integer>  recordEnd     = new ArrayList<Integer>();
        public static int            resultEnd;
        public static int            fetchEnd;
        public static int            end;

        public static Queue<Integer> ids = new LinkedList<Integer>(asList(1, 2));

        private void checkBase(ExecuteContext ctx) {
            assertNotNull(ctx.query());
            assertNotNull(ctx.batchQueries());
            assertTrue(ctx.query().toString().toLowerCase().contains("select"));
            assertTrue(ctx.batchQueries()[0].toString().toLowerCase().contains("select"));
            assertEquals(ctx.query(), ctx.batchQueries()[0]);
            assertEquals(1, ctx.batchSQL().length);

            assertEquals("Bar", ctx.configuration().data("Foo"));
            assertEquals("Baz", ctx.configuration().data("Bar"));
            assertEquals(new HashMap<String, String>() {{
                put("Foo", "Bar");
                put("Bar", "Baz");
            }}, ctx.configuration().data());

            assertNull(ctx.routine());
            assertEquals(ExecuteType.READ, ctx.type());
        }

        private void checkSQL(ExecuteContext ctx, boolean patched) {
            assertTrue(ctx.batchSQL()[0].toLowerCase().contains("select"));
            assertTrue(ctx.sql().toLowerCase().contains("select"));
            assertEquals(ctx.sql(), ctx.batchSQL()[0]);

            if (patched) {
                assertTrue(ctx.sql().toLowerCase().contains("as my_field"));
            }
        }

        private void checkStatement(ExecuteContext ctx, boolean patched) {
            checkStatement(ctx, patched, false);
        }

        @SuppressWarnings("unused")
        private void checkStatement(ExecuteContext ctx, boolean patched, boolean isNull) {
            if (isNull) {
                assertNull(ctx.statement());
            }
            else {
                assertNotNull(ctx.statement());
            }
        }

        private void checkResultSet(ExecuteContext ctx, boolean patched) {
            checkResultSet(ctx, patched, false);
        }

        @SuppressWarnings("unused")
        private void checkResultSet(ExecuteContext ctx, boolean patched, boolean isNull) {
            if (isNull) {
                assertNull(ctx.resultSet());
            }
            else {
                assertNotNull(ctx.resultSet());
            }
        }

        @Override
        public void start(ExecuteContext ctx) {
            start = ++callbackCount;
            checkBase(ctx);

            assertNull(ctx.batchSQL()[0]);
            assertNull(ctx.sql());
            assertNull(ctx.statement());
            assertNull(ctx.resultSet());
            assertNull(ctx.record());
            assertNull(ctx.result());
        }

        @Override
        public void renderStart(ExecuteContext ctx) {
            renderStart = ++callbackCount;
            checkBase(ctx);

            assertNull(ctx.batchSQL()[0]);
            assertNull(ctx.sql());
            assertNull(ctx.statement());
            assertNull(ctx.resultSet());
            assertNull(ctx.record());
            assertNull(ctx.result());
        }

        @Override
        public void renderEnd(ExecuteContext ctx) {
            renderEnd = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, false);

            assertNull(ctx.statement());
            assertNull(ctx.resultSet());
            assertNull(ctx.record());
            assertNull(ctx.result());

            ctx.sql(ctx.sql().replaceFirst("(?i:from)", "as my_field from"));
            checkSQL(ctx, true);
        }

        @Override
        public void prepareStart(ExecuteContext ctx) {
            prepareStart = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);

            assertNull(ctx.statement());
            assertNull(ctx.resultSet());
            assertNull(ctx.record());
            assertNull(ctx.result());
        }

        @Override
        public void prepareEnd(ExecuteContext ctx) {
            prepareEnd = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);

            checkStatement(ctx, false);
            // TODO Patch statement
            checkStatement(ctx, true);

            assertNull(ctx.resultSet());
            assertNull(ctx.record());
            assertNull(ctx.result());
        }

        @Override
        public void bindStart(ExecuteContext ctx) {
            bindStart = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);

            assertNull(ctx.resultSet());
            assertNull(ctx.record());
            assertNull(ctx.result());
        }

        @Override
        public void bindEnd(ExecuteContext ctx) {
            bindEnd = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);

            assertNull(ctx.resultSet());
            assertNull(ctx.record());
            assertNull(ctx.result());
        }

        @Override
        public void executeStart(ExecuteContext ctx) {
            executeStart = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);

            assertNull(ctx.resultSet());
            assertNull(ctx.record());
            assertNull(ctx.result());
        }

        @Override
        public void executeEnd(ExecuteContext ctx) {
            executeEnd = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);

            checkResultSet(ctx, false);
            // TODO patch result set
            checkResultSet(ctx, true);

            assertNull(ctx.record());
            assertNull(ctx.result());
        }

        @Override
        public void fetchStart(ExecuteContext ctx) {
            fetchStart = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);
            checkResultSet(ctx, true);

            assertNull(ctx.record());
            assertNull(ctx.result());
        }

        @Override
        public void resultStart(ExecuteContext ctx) {
            resultStart = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);
            checkResultSet(ctx, true);

            assertNull(ctx.record());
            assertNotNull(ctx.result());
            assertTrue(ctx.result().isEmpty());
        }

        @Override
        public void recordStart(ExecuteContext ctx) {
            recordStart.add(++callbackCount);
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);
            checkResultSet(ctx, true);

            assertNotNull(ctx.record());
            assertEquals(2, ctx.record().fieldsRow().size());

            assertNull(ctx.record().getValue(0));
            assertNull(ctx.record().getValue(1));
        }

        @Override
        public void recordEnd(ExecuteContext ctx) {
            recordEnd.add(++callbackCount);
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);
            checkResultSet(ctx, true);

            assertNotNull(ctx.record());
            assertEquals(2, ctx.record().fieldsRow().size());

            assertEquals(ids.remove(), ctx.record().getValue(0));
            assertEquals("Hello", ctx.record().getValue(1));
        }

        @Override
        public void resultEnd(ExecuteContext ctx) {
            resultEnd = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true, true);
            checkResultSet(ctx, true, true);
            assertNotNull(ctx.record());
            assertEquals(2, ctx.record().fieldsRow().size());

            assertNotNull(ctx.result());
            assertEquals(2, ctx.result().size());
        }

        @Override
        public void fetchEnd(ExecuteContext ctx) {
            fetchEnd = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true, true);
            checkResultSet(ctx, true, true);
            assertNotNull(ctx.record());
            assertEquals(2, ctx.record().fieldsRow().size());

            assertNotNull(ctx.result());
            assertEquals(2, ctx.result().size());
        }

        @Override
        public void end(ExecuteContext ctx) {
            end = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true, true);
            checkResultSet(ctx, true, true);
            assertNotNull(ctx.record());
            assertEquals(2, ctx.record().fieldsRow().size());

            assertNotNull(ctx.result());
            assertEquals(2, ctx.result().size());
        }
    }

    static class E extends RuntimeException {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 594781555404278995L;

        public E(String message) {
            super(message);
        }
    }

    public void testExecuteListenerOnBatchSingle() {
        if (!executePreparedStatements(create().configuration().settings())) {
            log.info("SKIPPINT", "Single batch tests with statement type = STATEMENT");
            return;
        }

        jOOQAbstractTest.reset = false;

        DSLContext create = create(new BatchSingleListener());

        create.configuration().data("Foo", "Bar");
        create.configuration().data("Bar", "Baz");

        int[] result = create.batch(create().insertInto(TAuthor())
                                            .set(TAuthor_ID(), param("id", Integer.class))
                                            .set(TAuthor_LAST_NAME(), param("name", String.class)))
                             .bind(8, "Gamma")
                             .bind(9, "Helm")
                             .bind(10, "Johnson")
                             .execute();

        assertEquals(3, result.length);

        // Check correct order of listener method invocation
        assertEquals(1, BatchSingleListener.start);
        assertEquals(2, BatchSingleListener.renderStart);
        assertEquals(3, BatchSingleListener.renderEnd);
        assertEquals(4, BatchSingleListener.prepareStart);
        assertEquals(5, BatchSingleListener.prepareEnd);
        assertEquals(asList(6, 8, 10), BatchSingleListener.bindStart);
        assertEquals(asList(7, 9, 11), BatchSingleListener.bindEnd);
        assertEquals(12, BatchSingleListener.executeStart);
        assertEquals(13, BatchSingleListener.executeEnd);
        assertEquals(14, BatchSingleListener.end);
    }

    public static class BatchSingleListener extends DefaultExecuteListener {

        /**
         * Generated UID
         */
        private static final long   serialVersionUID = 7399239846062763212L;

        // A counter that is incremented in callback methods
        private static int          callbackCount    = 0;

        // Fields that are used to check whether callback methods were called
        // in the expected order
        public static int           start;
        public static int           renderStart;
        public static int           renderEnd;
        public static int           prepareStart;
        public static int           prepareEnd;
        public static List<Integer> bindStart        = new ArrayList<Integer>();
        public static List<Integer> bindEnd          = new ArrayList<Integer>();
        public static int           executeStart;
        public static int           executeEnd;
        public static int           end;

        private void checkBase(ExecuteContext ctx) {
            assertNull(ctx.query());
            assertNotNull(ctx.batchQueries());
            assertTrue(ctx.batchQueries()[0].toString().toLowerCase().contains("insert"));
            assertEquals(1, ctx.batchSQL().length);

            assertEquals("Bar", ctx.configuration().data("Foo"));
            assertEquals("Baz", ctx.configuration().data("Bar"));
            assertEquals(new HashMap<String, String>() {{
                put("Foo", "Bar");
                put("Bar", "Baz");
            }}, ctx.configuration().data());

            assertNull(ctx.routine());
            assertNull(ctx.resultSet());
            assertNull(ctx.record());
            assertNull(ctx.result());

            assertEquals(ExecuteType.BATCH, ctx.type());
        }

        private void checkSQL(ExecuteContext ctx, boolean patched) {
            assertTrue(ctx.batchSQL()[0].toLowerCase().contains("insert"));

            if (patched) {
                assertTrue(ctx.batchSQL()[0].toLowerCase().contains("values    ("));
            }
        }

        private void checkStatement(ExecuteContext ctx, boolean patched) {
            checkStatement(ctx, patched, false);
        }

        @SuppressWarnings("unused")
        private void checkStatement(ExecuteContext ctx, boolean patched, boolean isNull) {
            if (isNull) {
                assertNull(ctx.statement());
            }
            else {
                assertNotNull(ctx.statement());
            }
        }

        @Override
        public void start(ExecuteContext ctx) {
            start = ++callbackCount;
            checkBase(ctx);

            assertNull(ctx.batchSQL()[0]);
            assertNull(ctx.sql());
            assertNull(ctx.statement());
        }

        @Override
        public void renderStart(ExecuteContext ctx) {
            renderStart = ++callbackCount;
            checkBase(ctx);

            assertNull(ctx.batchSQL()[0]);
            assertNull(ctx.sql());
            assertNull(ctx.statement());
        }

        @Override
        public void renderEnd(ExecuteContext ctx) {
            renderEnd = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, false);

            assertNull(ctx.statement());

            ctx.sql(ctx.sql().replaceFirst("(?i:values\\s+)", "values    "));
            checkSQL(ctx, true);
        }

        @Override
        public void prepareStart(ExecuteContext ctx) {
            prepareStart = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);

            assertNull(ctx.statement());
        }

        @Override
        public void prepareEnd(ExecuteContext ctx) {
            prepareEnd = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);

            checkStatement(ctx, false);
            // TODO Patch statement
            checkStatement(ctx, true);
        }

        @Override
        public void bindStart(ExecuteContext ctx) {
            bindStart.add(++callbackCount);
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);
        }

        @Override
        public void bindEnd(ExecuteContext ctx) {
            bindEnd.add(++callbackCount);
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);
        }

        @Override
        public void executeStart(ExecuteContext ctx) {
            executeStart = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);
        }

        @Override
        public void executeEnd(ExecuteContext ctx) {
            executeEnd = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);
        }

        @Override
        public void fetchStart(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void resultStart(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void recordStart(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void recordEnd(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void resultEnd(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void fetchEnd(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void end(ExecuteContext ctx) {
            end = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true, true);
        }
    }

    public void testExecuteListenerOnBatchMultiple() {
        jOOQAbstractTest.reset = false;

        DSLContext create = create(new BatchMultipleListener());

        create.configuration().data("Foo", "Bar");
        create.configuration().data("Bar", "Baz");

        int[] result = create.batch(
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
                    .set(TAuthor_LAST_NAME(), "Johnson")).execute();

        assertEquals(4, result.length);
        assertEquals(5, create().fetch(TBook()).size());
        assertEquals(1, create().fetch(TBook(), TBook_AUTHOR_ID().equal(8)).size());

        // Check correct order of listener method invocation
        assertEquals(1, BatchMultipleListener.start);
        assertEquals(asList(2, 4, 6, 8), BatchMultipleListener.renderStart);
        assertEquals(asList(3, 5, 7, 9), BatchMultipleListener.renderEnd);
        assertEquals(asList(10, 12, 14, 16), BatchMultipleListener.prepareStart);
        assertEquals(asList(11, 13, 15, 17), BatchMultipleListener.prepareEnd);
        assertEquals(18, BatchMultipleListener.executeStart);
        assertEquals(19, BatchMultipleListener.executeEnd);
        assertEquals(20, BatchMultipleListener.end);
    }

    public static class BatchMultipleListener extends DefaultExecuteListener {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7399239846062763212L;

        // A counter that is incremented in callback methods
        private static int           callbackCount = 0;
        private static int           rendered      = 0;
        private static int           prepared      = 0;

        // Fields that are used to check whether callback methods were called
        // in the expected order
        public static int            start;
        public static List<Integer>  renderStart   = new ArrayList<Integer>();
        public static List<Integer>  renderEnd     = new ArrayList<Integer>();
        public static List<Integer>  prepareStart  = new ArrayList<Integer>();
        public static List<Integer>  prepareEnd    = new ArrayList<Integer>();
        public static int            executeStart;
        public static int            executeEnd;
        public static int            end;

        public static Queue<Integer> ids = new LinkedList<Integer>(asList(1, 2));

        private void checkBase(ExecuteContext ctx) {
            assertNull(ctx.query());
            assertNotNull(ctx.batchQueries());
            assertTrue(ctx.batchQueries()[0].toString().toLowerCase().contains("insert"));
            assertTrue(ctx.batchQueries()[1].toString().toLowerCase().contains("insert"));
            assertTrue(ctx.batchQueries()[2].toString().toLowerCase().contains("insert"));
            assertTrue(ctx.batchQueries()[3].toString().toLowerCase().contains("insert"));
            assertEquals(4, ctx.batchSQL().length);

            assertEquals("Bar", ctx.configuration().data("Foo"));
            assertEquals("Baz", ctx.configuration().data("Bar"));
            assertEquals(new HashMap<String, String>() {{
                put("Foo", "Bar");
                put("Bar", "Baz");
            }}, ctx.configuration().data());

            assertNull(ctx.routine());
            assertNull(ctx.resultSet());
            assertNull(ctx.record());
            assertNull(ctx.result());

            assertEquals(ExecuteType.BATCH, ctx.type());
        }

        private void checkSQL(ExecuteContext ctx, boolean patched) {
            for (int i = 0; i < rendered; i++) {
                assertTrue(ctx.batchQueries()[i].toString().toLowerCase().contains("insert"));

                if (patched) {
                    assertTrue(ctx.batchSQL()[i].toLowerCase().contains("values    ("));
                }
            }
        }

        private void checkStatement(ExecuteContext ctx, boolean patched) {
            checkStatement(ctx, patched, false);
        }

        @SuppressWarnings("unused")
        private void checkStatement(ExecuteContext ctx, boolean patched, boolean isNull) {
            if (isNull) {
                assertNull(ctx.statement());
            }
            else {
                assertNotNull(ctx.statement());
            }
        }

        @Override
        public void start(ExecuteContext ctx) {
            start = ++callbackCount;
            checkBase(ctx);

            assertNull(ctx.batchSQL()[0]);
            assertNull(ctx.batchSQL()[1]);
            assertNull(ctx.batchSQL()[2]);
            assertNull(ctx.batchSQL()[3]);
            assertNull(ctx.sql());
            assertNull(ctx.statement());
        }

        @Override
        public void renderStart(ExecuteContext ctx) {
            renderStart.add(++callbackCount);
            checkBase(ctx);
            checkStatement(ctx, false);
            checkSQL(ctx, false);

            assertNull(ctx.sql());
        }

        @Override
        public void renderEnd(ExecuteContext ctx) {
            renderEnd.add(++callbackCount);
            rendered++;
            checkBase(ctx);
            checkStatement(ctx, false);
            checkSQL(ctx, false);

            ctx.batchSQL()[rendered - 1] = ctx.batchSQL()[rendered - 1].replaceFirst("(?i:values\\s+)", "values    ");
            checkSQL(ctx, true);
        }

        @Override
        public void prepareStart(ExecuteContext ctx) {
            prepareStart.add(++callbackCount);
            checkBase(ctx);
            checkStatement(ctx, false);
            checkSQL(ctx, true);
        }

        @Override
        public void prepareEnd(ExecuteContext ctx) {
            prepareEnd.add(++callbackCount);
            prepared++;
            checkBase(ctx);
            checkStatement(ctx, false);
            checkSQL(ctx, true);
        }

        @Override
        public void bindStart(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void bindEnd(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void executeStart(ExecuteContext ctx) {
            executeStart = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);
        }

        @Override
        public void executeEnd(ExecuteContext ctx) {
            executeEnd = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true);
        }

        @Override
        public void fetchStart(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void resultStart(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void recordStart(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void recordEnd(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void resultEnd(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void fetchEnd(ExecuteContext ctx) {
            fail();
        }

        @Override
        public void end(ExecuteContext ctx) {
            end = ++callbackCount;
            checkBase(ctx);
            checkSQL(ctx, true);
            checkStatement(ctx, true, true);
        }
    }

    public void testExecuteListenerFetchLazyTest() throws Exception {
        DSLContext create = create(new FetchLazyListener());
        FetchLazyListener.reset();

        create.selectFrom(TAuthor()).fetch();
        assertEquals(1, FetchLazyListener.countStart);
        assertEquals(1, FetchLazyListener.countRenderStart);
        assertEquals(1, FetchLazyListener.countRenderEnd);
        assertEquals(1, FetchLazyListener.countPrepareStart);
        assertEquals(1, FetchLazyListener.countPrepareEnd);
        assertEquals(1, FetchLazyListener.countBindStart);
        assertEquals(1, FetchLazyListener.countBindEnd);
        assertEquals(1, FetchLazyListener.countExecuteStart);
        assertEquals(1, FetchLazyListener.countExecuteEnd);
        assertEquals(1, FetchLazyListener.countFetchStart);
        assertEquals(1, FetchLazyListener.countResultStart);
        assertEquals(2, FetchLazyListener.countRecordStart);
        assertEquals(2, FetchLazyListener.countRecordEnd);
        assertEquals(1, FetchLazyListener.countResultEnd);
        assertEquals(1, FetchLazyListener.countFetchEnd);
        assertEquals(1, FetchLazyListener.countEnd);
        assertEquals(0, FetchLazyListener.countException);

        // [#1868] fetchLazy should behave almost the same as fetch
        FetchLazyListener.reset();
        Cursor<A> cursor = create.selectFrom(TAuthor()).fetchLazy();
        assertEquals(1, FetchLazyListener.countStart);
        assertEquals(1, FetchLazyListener.countRenderStart);
        assertEquals(1, FetchLazyListener.countRenderEnd);
        assertEquals(1, FetchLazyListener.countPrepareStart);
        assertEquals(1, FetchLazyListener.countPrepareEnd);
        assertEquals(1, FetchLazyListener.countBindStart);
        assertEquals(1, FetchLazyListener.countBindEnd);
        assertEquals(1, FetchLazyListener.countExecuteStart);
        assertEquals(1, FetchLazyListener.countExecuteEnd);
        assertEquals(0, FetchLazyListener.countFetchStart);
        assertEquals(0, FetchLazyListener.countResultStart);
        assertEquals(0, FetchLazyListener.countRecordStart);
        assertEquals(0, FetchLazyListener.countRecordEnd);
        assertEquals(0, FetchLazyListener.countResultEnd);
        assertEquals(0, FetchLazyListener.countFetchEnd);
        assertEquals(0, FetchLazyListener.countEnd);
        assertEquals(0, FetchLazyListener.countException);

        cursor.fetchOne();
        assertEquals(1, FetchLazyListener.countStart);
        assertEquals(1, FetchLazyListener.countRenderStart);
        assertEquals(1, FetchLazyListener.countRenderEnd);
        assertEquals(1, FetchLazyListener.countPrepareStart);
        assertEquals(1, FetchLazyListener.countPrepareEnd);
        assertEquals(1, FetchLazyListener.countBindStart);
        assertEquals(1, FetchLazyListener.countBindEnd);
        assertEquals(1, FetchLazyListener.countExecuteStart);
        assertEquals(1, FetchLazyListener.countExecuteEnd);
        assertEquals(1, FetchLazyListener.countFetchStart);
        assertEquals(1, FetchLazyListener.countResultStart);
        assertEquals(1, FetchLazyListener.countRecordStart);
        assertEquals(1, FetchLazyListener.countRecordEnd);
        assertEquals(1, FetchLazyListener.countResultEnd);
        assertEquals(0, FetchLazyListener.countFetchEnd);
        assertEquals(0, FetchLazyListener.countEnd);
        assertEquals(0, FetchLazyListener.countException);

        cursor.fetchOne();
        assertEquals(1, FetchLazyListener.countStart);
        assertEquals(1, FetchLazyListener.countRenderStart);
        assertEquals(1, FetchLazyListener.countRenderEnd);
        assertEquals(1, FetchLazyListener.countPrepareStart);
        assertEquals(1, FetchLazyListener.countPrepareEnd);
        assertEquals(1, FetchLazyListener.countBindStart);
        assertEquals(1, FetchLazyListener.countBindEnd);
        assertEquals(1, FetchLazyListener.countExecuteStart);
        assertEquals(1, FetchLazyListener.countExecuteEnd);
        assertEquals(1, FetchLazyListener.countFetchStart);
        assertEquals(2, FetchLazyListener.countResultStart);
        assertEquals(2, FetchLazyListener.countRecordStart);
        assertEquals(2, FetchLazyListener.countRecordEnd);
        assertEquals(2, FetchLazyListener.countResultEnd);
        assertEquals(0, FetchLazyListener.countFetchEnd);
        assertEquals(0, FetchLazyListener.countEnd);
        assertEquals(0, FetchLazyListener.countException);

        cursor.fetchOne();
        assertEquals(1, FetchLazyListener.countStart);
        assertEquals(1, FetchLazyListener.countRenderStart);
        assertEquals(1, FetchLazyListener.countRenderEnd);
        assertEquals(1, FetchLazyListener.countPrepareStart);
        assertEquals(1, FetchLazyListener.countPrepareEnd);
        assertEquals(1, FetchLazyListener.countBindStart);
        assertEquals(1, FetchLazyListener.countBindEnd);
        assertEquals(1, FetchLazyListener.countExecuteStart);
        assertEquals(1, FetchLazyListener.countExecuteEnd);
        assertEquals(1, FetchLazyListener.countFetchStart);
        assertEquals(3, FetchLazyListener.countResultStart);
        assertEquals(2, FetchLazyListener.countRecordStart);
        assertEquals(2, FetchLazyListener.countRecordEnd);
        assertEquals(3, FetchLazyListener.countResultEnd);
        assertEquals(1, FetchLazyListener.countFetchEnd);
        assertEquals(1, FetchLazyListener.countEnd);
        assertEquals(0, FetchLazyListener.countException);
    }

    public static class FetchLazyListener implements ExecuteListener {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 7399239846062763212L;

        static int countStart;
        static int countRenderStart;
        static int countRenderEnd;
        static int countPrepareStart;
        static int countPrepareEnd;
        static int countBindStart;
        static int countBindEnd;
        static int countExecuteStart;
        static int countExecuteEnd;
        static int countFetchStart;
        static int countResultStart;
        static int countRecordStart;
        static int countRecordEnd;
        static int countResultEnd;
        static int countFetchEnd;
        static int countEnd;
        static int countException;
        static int countWarning;

        static void reset() {
            for (java.lang.reflect.Field f : FetchLazyListener.class.getDeclaredFields()) {
                f.setAccessible(true);

                try {
                    f.set(FetchLazyListener.class, 0);
                }
                catch (Exception ignore) {}
            }
        }

        @Override
        public void start(ExecuteContext ctx) {
            countStart++;
        }

        @Override
        public void renderStart(ExecuteContext ctx) {
            countRenderStart++;
        }

        @Override
        public void renderEnd(ExecuteContext ctx) {
            countRenderEnd++;
        }

        @Override
        public void prepareStart(ExecuteContext ctx) {
            countPrepareStart++;
        }

        @Override
        public void prepareEnd(ExecuteContext ctx) {
            countPrepareEnd++;
        }

        @Override
        public void bindStart(ExecuteContext ctx) {
            countBindStart++;
        }

        @Override
        public void bindEnd(ExecuteContext ctx) {
            countBindEnd++;
        }

        @Override
        public void executeStart(ExecuteContext ctx) {
            countExecuteStart++;
        }

        @Override
        public void executeEnd(ExecuteContext ctx) {
            countExecuteEnd++;
        }

        @Override
        public void fetchStart(ExecuteContext ctx) {
            countFetchStart++;
        }

        @Override
        public void resultStart(ExecuteContext ctx) {
            countResultStart++;
        }

        @Override
        public void recordStart(ExecuteContext ctx) {
            countRecordStart++;
        }

        @Override
        public void recordEnd(ExecuteContext ctx) {
            countRecordEnd++;
        }

        @Override
        public void resultEnd(ExecuteContext ctx) {
            countResultEnd++;
        }

        @Override
        public void fetchEnd(ExecuteContext ctx) {
            countFetchEnd++;
        }

        @Override
        public void exception(ExecuteContext ctx) {
            countException++;
        }

        @Override
        public void warning(ExecuteContext ctx) {
            countWarning++;
        }

        @Override
        public void end(ExecuteContext ctx) {
            countEnd++;
        }
    }

    public void testExecuteListenerDELETEorUPDATEwithoutWHERE() throws Exception {
        try {
            create(new DELETEorUPDATEwithoutWHERElistener())
                .update(TBook())
                .set(TBook_TITLE(), "abc")
                .execute();
            fail();
        }
        catch (DELETEorUPDATEwithoutWHEREException expected) {}

        try {
            create(new DELETEorUPDATEwithoutWHERElistener())
                .delete(TBook())
                .execute();
            fail();
        }
        catch (DELETEorUPDATEwithoutWHEREException expected) {}

        assertEquals(0,
        create(new DELETEorUPDATEwithoutWHERElistener())
            .update(TBook())
            .set(TBook_TITLE(), "abc")
            .where(TBook_ID().eq(5))
            .execute());

        assertEquals(0,
        create(new DELETEorUPDATEwithoutWHERElistener())
            .delete(TBook())
            .where(TBook_ID().eq(5))
            .execute());
    }

    public static class DELETEorUPDATEwithoutWHERElistener extends DefaultExecuteListener {

        @Override
        public void renderEnd(ExecuteContext ctx) {
            if (ctx.sql().matches("^(?i:(UPDATE|DELETE)(?!.* WHERE ).*)$")) {
                throw new DELETEorUPDATEwithoutWHEREException();
            }
        }
    }

    public static class DELETEorUPDATEwithoutWHEREException extends RuntimeException {

    }
}
