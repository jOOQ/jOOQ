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
package org.jooq.test;

import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.val;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;
import static org.jooq.test.data.Table2.FIELD_ID2;
import static org.jooq.test.data.Table2.FIELD_NAME2;
import static org.jooq.test.data.Table2.TABLE2;
import static org.jooq.test.data.Table3.FIELD_NAME3;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.List;

import org.jooq.Constants;
import org.jooq.DSLContext;
import org.jooq.InsertResultStep;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.test.data.Table1;
import org.jooq.test.data.Table1Record;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockFileDatabase;
import org.jooq.tools.jdbc.MockResult;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test suite contains tests for the JDBC mock implementations.
 *
 * @author Lukas Eder
 */
public class MockTest extends AbstractTest {

    private static DSLContext MOCK;

    @BeforeClass
    public static void before() throws Exception {
        RandomAccessFile f = new RandomAccessFile(new File(MockTest.class.getResource("/org/jooq/test/data/db.txt").toURI()), "r");
        byte[] b = new byte[(int) f.length()];
        f.readFully(b);
        String s = new String(b);
        s = s.replace("{version}", Constants.FULL_VERSION);

        MOCK = DSL.using(new MockConnection(new MockFileDatabase(s)), SQLDialect.POSTGRES);
    }

    @Test
    public void testEmptyResult() {
        DSLContext e = DSL.using(new MockConnection(new EmptyResult()), SQLDialect.H2);
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
        DSLContext e = DSL.using(new MockConnection(new SingleResult()), SQLDialect.H2);
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
    public void testTripleResult() {
        DSLContext e = DSL.using(new MockConnection(new TripleResult()), SQLDialect.H2);
        List<Result<Record>> result = e.fetchMany("select ?, ? from dual", 1, 2);

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(2, result.get(1).size());
        assertEquals(3, result.get(0).fields().length);
        assertEquals(3, result.get(1).fields().length);

        // Metadata
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 3; i++) {
                assertEquals(TABLE1.field(i).getName(), result.get(j).field(i).getName());
                assertEquals(TABLE1.field(i).getType(), result.get(j).field(i).getType());
            }
        }

        assertEquals(FIELD_NAME1.getName(), result.get(2).field(0).getName());
        assertEquals(FIELD_NAME2.getName(), result.get(2).field(1).getName());
        assertEquals(FIELD_NAME3.getName(), result.get(2).field(2).getName());
        assertEquals(FIELD_NAME1.getType(), result.get(2).field(0).getType());
        assertEquals(FIELD_NAME2.getType(), result.get(2).field(1).getType());
        assertEquals(FIELD_NAME3.getType(), result.get(2).field(2).getType());

        // Data
        assertEquals(1, (int) result.get(0).getValue(0, FIELD_ID1));
        assertEquals(2, (int) result.get(1).getValue(0, FIELD_ID1));
        assertEquals(3, (int) result.get(1).getValue(1, FIELD_ID1));
        assertEquals("1", result.get(0).getValue(0, FIELD_NAME1));
        assertEquals("2", result.get(1).getValue(0, FIELD_NAME1));
        assertEquals("3", result.get(1).getValue(1, FIELD_NAME1));
        assertNull(result.get(0).getValue(0, Table1.FIELD_DATE1));
        assertNull(result.get(1).getValue(0, Table1.FIELD_DATE1));
        assertNull(result.get(1).getValue(1, Table1.FIELD_DATE1));

        assertEquals("A1", result.get(2).getValue(0, FIELD_NAME1));
        assertEquals("B1", result.get(2).getValue(0, FIELD_NAME2));
        assertEquals("C1", result.get(2).getValue(0, FIELD_NAME3));
        assertEquals("A2", result.get(2).getValue(1, FIELD_NAME1));
        assertEquals("B2", result.get(2).getValue(1, FIELD_NAME2));
        assertEquals("C2", result.get(2).getValue(1, FIELD_NAME3));
    }

    class TripleResult extends AbstractResult {
        @Override
        public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
            execute0(ctx);

            return new MockResult[] {
                new MockResult(0, resultOne),
                new MockResult(0, resultTwo),
                new MockResult(0, resultStrings)
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
        DSLContext e = DSL.using(new MockConnection(new BatchSingle()), SQLDialect.H2);

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
        DSLContext e = DSL.using(new MockConnection(new BatchMultiple()), SQLDialect.H2);

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
        DSLContext e = DSL.using(new MockConnection(new Exceptional()), SQLDialect.H2);

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
        DSLContext e = DSL.using(new MockConnection(new InsertReturning()), SQLDialect.POSTGRES);

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

    @Test
    public void testFileDatabase_SELECT_A_FROM_DUAL() throws Exception {
        Result<Record> r1 = MOCK.fetch("select 'A'");
        Result<Record> r2 = MOCK.fetch("select ?", "A");
        Result<Record1<String>> r3 = MOCK.select(val("A")).fetch();

        assertEquals(1, r1.size());
        assertEquals(1, r1.fields().length);
        assertEquals("A", r1.field(0).getName());
        assertEquals("A", r1.get(0).getValue(0));
        assertEquals(r1, r2);
        assertEquals(r1, r3);
    }

    @Test
    public void testFileDatabase_SELECT_ID1_NAME1_FROM_TABLE1() throws Exception {
        Result<Record2<Integer, String>> r = MOCK.select(FIELD_ID1, FIELD_NAME1).from(TABLE1).fetch();

        assertEquals(2, r.size());
        assertEquals("ID1", r.field(0).getName());
        assertEquals("NAME1", r.field(1).getName());
        assertEquals(asList(1, 2), r.getValues(0));
        assertEquals(asList("X", "Y"), r.getValues(1));
    }

    @Test
    public void testFileDatabase_SELECT_ID2_NAME2_FROM_TABLE2() throws Exception {
        Result<Record2<Integer, String>> r = MOCK.select(FIELD_ID2, FIELD_NAME2).from(TABLE2).fetch();

        assertEquals(2, r.size());
        assertEquals("ID2", r.field(0).getName());
        assertEquals("NAME2", r.field(1).getName());
        assertEquals(asList(1, 2), r.getValues(0));
        assertEquals(asList("X", "Y"), r.getValues(1));
    }

    @Test
    public void testFileDatabase_SELECT_COMPLEX_DATA() throws Exception {
        List<Result<Record>> results = MOCK.fetchMany("select complex_data");
        assertEquals(2, results.size());

        Result<Record> r1 = results.get(0);
        Result<Record> r2 = results.get(1);

        // Result 1
        // --------
        assertEquals(2, r1.size());

        // Header
        assertEquals(3, r1.fields().length);
        assertEquals("F1", r1.field(0).getName());
        assertEquals("F2", r1.field(1).getName());
        assertEquals("F3 is a bit more complex", r1.field(2).getName());

        // Data
        assertEquals("1", r1.getValue(0, 0));
        assertEquals("2", r1.getValue(0, 1));
        assertEquals("and a string containing data", r1.getValue(0, 2));
        assertEquals("1.1", r1.getValue(1, 0));
        assertEquals("x", r1.getValue(1, 1));
        assertEquals("another string", r1.getValue(1, 2));

        // Result 1
        // --------
        assertEquals(1, r2.size());

        // Header
        assertEquals(3, r2.fields().length);
        assertEquals("A", r2.field(0).getName());
        assertEquals("B", r2.field(1).getName());
        assertEquals("\"C D\"", r2.field(2).getName());

        // Data
        assertEquals("x", r2.getValue(0, 0));
        assertEquals("y", r2.getValue(0, 1));
        assertEquals("z", r2.getValue(0, 2));
    }
}
