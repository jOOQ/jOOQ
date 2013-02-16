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
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;

import java.sql.SQLException;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
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
                new MockResult(0, create.newResult(Table1.TABLE1))
            };
        }
    }

    @Test
    public void testSingleResult() {
        Executor e = new Executor(new MockConnection(new SingleResult()), SQLDialect.H2);
        Result<Record> result = e.fetch("select ?, ? from dual", 1, 2);

        assertEquals(2, result.size());
        assertEquals(3, result.fields().length);

        for (int i = 0; i < 3; i++) {
            assertEquals(TABLE1.field(i).getName(), result.field(i).getName());
            assertEquals(TABLE1.field(i).getType(), result.field(i).getType());
        }

        assertEquals(1, (int) result.getValue(0, FIELD_ID1));
        assertEquals(2, (int) result.getValue(1, FIELD_ID1));
        assertEquals("1", result.getValue(0, FIELD_NAME1));
        assertEquals("2", result.getValue(1, FIELD_NAME1));
        assertNull(result.getValue(0, Table1.FIELD_DATE1));
        assertNull(result.getValue(1, Table1.FIELD_DATE1));
    }

    class SingleResult extends AbstractResult {
        @Override
        public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
            execute0(ctx);

            Result<Table1Record> result = create.newResult(TABLE1);
            result.add(create.newRecord(TABLE1));
            result.add(create.newRecord(TABLE1));

            result.get(0).setValue(FIELD_ID1, 1);
            result.get(1).setValue(FIELD_ID1, 2);
            result.get(0).setValue(FIELD_NAME1, "1");
            result.get(1).setValue(FIELD_NAME1, "2");

            return new MockResult[] {
                new MockResult(0, result)
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

            Result<Table1Record> result1 = create.newResult(TABLE1);
            Result<Table1Record> result2 = create.newResult(TABLE1);
            result1.add(create.newRecord(TABLE1));
            result2.add(create.newRecord(TABLE1));
            result2.add(create.newRecord(TABLE1));

            result1.get(0).setValue(FIELD_ID1, 1);
            result2.get(0).setValue(FIELD_ID1, 2);
            result2.get(1).setValue(FIELD_ID1, 3);
            result1.get(0).setValue(FIELD_NAME1, "1");
            result2.get(0).setValue(FIELD_NAME1, "2");
            result2.get(1).setValue(FIELD_NAME1, "3");

            return new MockResult[] {
                new MockResult(0, result1),
                new MockResult(0, result2),
            };
        }
    }

    abstract class AbstractResult implements MockDataProvider {
        public void execute0(MockExecuteContext ctx) {
            assertEquals(1, ctx.getBatchSQL().length);
            assertEquals("select ?, ? from dual", ctx.getBatchSQL()[0]);
            assertEquals("select ?, ? from dual", ctx.getSQL());

            assertEquals(1, ctx.getBatchBindings().length);
            assertEquals(asList(1, 2), asList(ctx.getBatchBindings()[0]));
            assertEquals(asList(1, 2), asList(ctx.getBindings()));
        }
    }
}
