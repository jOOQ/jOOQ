/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;

import java.sql.SQLException;
import java.util.List;

import org.jooq.InsertResultStep;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.Executor;
import org.jooq.test.data.Table1;
import org.jooq.test.data.Table1Record;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

import org.junit.Test;

/**
 * This test suite contains tests for the JDBC mock implementations.
 *
 * @author Lukas Eder
 */
public class MockTest extends AbstractTest {

    @Test
    public void testEmptyResult() {
        Executor e = new Executor(new MockConnection(new EmptyResult()), SQLDialect.H2);
        Result<Record> result = e.fetch("select ?, ? from dual", 1, 2);

        assertEquals(0, result.size());
        assertEquals(3, result.fields().length);
        for (int i = 0; i < 3; i++) {
            assertEquals(TABLE1.field(i).getName(), result.field(i).getName());
            assertEquals(TABLE1.field(i).getType(), result.field(i).getType());
        }
    }

    class EmptyResult extends AbstractResult {
        @Override
        public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
            execute0(ctx);

            return new MockResult[] {
                new MockResult(0, resultEmpty)
            };
        }
    }

    @Test
    public void testSingleResult() {
        Executor e = new Executor(new MockConnection(new SingleResult()), SQLDialect.H2);
        Result<Record> result = e.fetch("select ?, ? from dual", 1, 2);

        assertEquals(1, result.size());
        assertEquals(3, result.fields().length);

        for (int i = 0; i < 3; i++) {
            assertEquals(TABLE1.field(i).getName(), result.field(i).getName());
            assertEquals(TABLE1.field(i).getType(), result.field(i).getType());
        }

        assertEquals(1, (int) result.getValue(0, FIELD_ID1));
        assertEquals("1", result.getValue(0, FIELD_NAME1));
        assertNull(result.getValue(0, Table1.FIELD_DATE1));
    }

    class SingleResult extends AbstractResult {
        @Override
        public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
            execute0(ctx);

            return new MockResult[] {
                new MockResult(0, resultOne)
            };
        }
    }

    @Test
    public void testDoubleResult() {
        Executor e = new Executor(new MockConnection(new DoubleResult()), SQLDialect.H2);
        List<Result<Record>> result = e.fetchMany("select ?, ? from dual", 1, 2);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(2, result.get(1).size());
        assertEquals(3, result.get(0).fields().length);
        assertEquals(3, result.get(1).fields().length);

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 3; i++) {
                assertEquals(TABLE1.field(i).getName(), result.get(j).field(i).getName());
                assertEquals(TABLE1.field(i).getType(), result.get(j).field(i).getType());
            }
        }

        assertEquals(1, (int) result.get(0).getValue(0, FIELD_ID1));
        assertEquals(2, (int) result.get(1).getValue(0, FIELD_ID1));
        assertEquals(3, (int) result.get(1).getValue(1, FIELD_ID1));
        assertEquals("1", result.get(0).getValue(0, FIELD_NAME1));
        assertEquals("2", result.get(1).getValue(0, FIELD_NAME1));
        assertEquals("3", result.get(1).getValue(1, FIELD_NAME1));
        assertNull(result.get(0).getValue(0, Table1.FIELD_DATE1));
        assertNull(result.get(1).getValue(0, Table1.FIELD_DATE1));
        assertNull(result.get(1).getValue(1, Table1.FIELD_DATE1));
    }

    class DoubleResult extends AbstractResult {
        @Override
        public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
            execute0(ctx);

            return new MockResult[] {
                new MockResult(0, resultOne),
                new MockResult(0, resultTwo),
            };
        }
    }

    abstract class AbstractResult implements MockDataProvider {
        public void execute0(MockExecuteContext ctx) {
            assertEquals(1, ctx.batchSQL().length);
            assertEquals("select ?, ? from dual", ctx.batchSQL()[0]);
            assertEquals("select ?, ? from dual", ctx.sql());

            assertEquals(1, ctx.batchBindings().length);
            assertEquals(asList(1, 2), asList(ctx.batchBindings()[0]));
            assertEquals(asList(1, 2), asList(ctx.bindings()));
        }
    }

    @Test
    public void testBatchSingle() {
        Executor e = new Executor(new MockConnection(new BatchSingle()), SQLDialect.H2);

        int[] result =
        e.batch(
            e.query("insert into x values(1)"),
            e.query("insert into x values(2)")
        ).execute();

        assertEquals(2, result.length);
        assertEquals(0, result[0]);
        assertEquals(1, result[1]);
    }

    class BatchSingle implements MockDataProvider {

        @Override
        public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
            assertEquals(2, ctx.batchSQL().length);
            assertEquals("insert into x values(1)", ctx.batchSQL()[0]);
            assertEquals("insert into x values(2)", ctx.batchSQL()[1]);
            assertEquals("insert into x values(1)", ctx.sql());

            assertEquals(0, ctx.batchBindings().length);
            assertEquals(asList(), asList(ctx.bindings()));

            return new MockResult[] {
                new MockResult(0, null),
                new MockResult(1, null)
            };
        }
    }

    @Test
    public void testBatchMultiple() {
        Executor e = new Executor(new MockConnection(new BatchMultiple()), SQLDialect.H2);

        Query query = e.query("insert into x values(?, ?)", null, null);

        int[] result =
        e.batch(query)
         .bind(1, 2)
         .bind(3, 4)
         .execute();

        assertEquals(2, result.length);
        assertEquals(0, result[0]);
        assertEquals(1, result[1]);
    }

    class BatchMultiple implements MockDataProvider {

        @Override
        public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
            assertEquals(1, ctx.batchSQL().length);
            assertEquals("insert into x values(?, ?)", ctx.batchSQL()[0]);
            assertEquals("insert into x values(?, ?)", ctx.sql());

            assertEquals(2, ctx.batchBindings().length);
            assertEquals(asList(1, 2), asList(ctx.batchBindings()[0]));
            assertEquals(asList(3, 4), asList(ctx.batchBindings()[1]));
            assertEquals(asList(1, 2), asList(ctx.bindings()));

            return new MockResult[] {
                new MockResult(0, null),
                new MockResult(1, null)
            };
        }
    }

    @Test
    public void testException() {
        Executor e = new Executor(new MockConnection(new Exceptional()), SQLDialect.H2);

        Query query = e.query("insert into x values(1)");

        try {
            query.execute();
            fail();
        }
        catch (DataAccessException expected) {
            assertEquals("Expected", expected.getCause().getMessage());
        }
    }

    class Exceptional implements MockDataProvider {

        @Override
        public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
            throw new SQLException("Expected");
        }
    }

    @Test
    public void testInsertReturning() {

        // Note: INSERT .. RETURNING is hard to mock for all dialects...
        Executor e = new Executor(new MockConnection(new InsertReturning()), SQLDialect.ORACLE);

        InsertResultStep<Table1Record> query = e
            .insertInto(TABLE1, FIELD_ID1)
            .values(1)
            .returning();

        assertEquals(1, query.execute());
        Table1Record record = query.fetchOne();

        assertEquals(1, (int) record.getValue(FIELD_ID1));
        assertEquals("1", record.getValue(FIELD_NAME1));
    }

    class InsertReturning implements MockDataProvider {

        @Override
        public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
            assertEquals(1, ctx.batchSQL().length);
            assertEquals(1, ctx.batchBindings().length);
            assertEquals(asList(1), asList(ctx.batchBindings()[0]));
            assertEquals(asList(1), asList(ctx.bindings()));

            return new MockResult[] {
                new MockResult(1, resultOne)
            };
        }
    }
}
