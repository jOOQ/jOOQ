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

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.tools.reflect.Reflect.on;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import java.sql.Date;
import java.util.Arrays;

import org.jooq.ArrayRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Routine;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.conf.Settings;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;

import org.junit.Assume;

public class RoutineAndUDTTests<
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

    public RoutineAndUDTTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testPackage() throws Exception {
        if (cLibrary() == null) {
            log.info("SKIPPING", "packages test");
            return;
        }

        jOOQAbstractTest.reset = false;

        assertEquals("1", "" + invoke(cLibrary(), "pkgPAuthorExists1", create().configuration(), "Paulo"));
        assertEquals("0", "" + invoke(cLibrary(), "pkgPAuthorExists1", create().configuration(), "Shakespeare"));
        assertEquals("1", "" + invoke(cLibrary(), "pkgFAuthorExists1", create().configuration(), "Paulo"));
        assertEquals("0", "" + invoke(cLibrary(), "pkgFAuthorExists1", create().configuration(), "Shakespeare"));
    }

    public void testStoredProcedure() throws Exception {
        if (cRoutines() == null) {
            log.info("SKIPPING", "procedure test");
            return;
        }

        jOOQAbstractTest.reset = false;

        // P_AUTHOR_EXISTS
        // ---------------------------------------------------------------------
        if (supportsOUTParameters()) {
            Object r1 = invoke(cRoutines(), "pAuthorExists", create().configuration(), null, DUMMY_OUT_INT);
            Object r2 = invoke(cRoutines(), "pAuthorExists", create().configuration(), "Paulo", DUMMY_OUT_INT);
            Object r3 = invoke(cRoutines(), "pAuthorExists", create().configuration(), "Shakespeare", DUMMY_OUT_INT);

            // [#4106] SQL Server always has a RETURN_VALUE in Procedures
            if (family() == SQLSERVER) {
                r1 = on(r1).call("getResult").get();
                r2 = on(r2).call("getResult").get();
                r3 = on(r3).call("getResult").get();
            }

            assertEquals("0", "" + r1);
            assertEquals("1", "" + r2);
            assertEquals("0", "" + r3);
        } else {
            log.info("SKIPPING", "procedure test for OUT parameters");
        }

        // P_CREATE_AUTHOR_*
        // ---------------------------------------------------------------------
        assertEquals(null, create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("William")));
        invoke(cRoutines(), "pCreateAuthor", create().configuration());
        assertEquals("Shakespeare", create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("William")).getValue(TAuthor_LAST_NAME()));

        assertEquals(null, create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("Hermann")));
        invoke(cRoutines(), "pCreateAuthorByName", create().configuration(), "Hermann", "Hesse");
        assertEquals("Hesse", create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("Hermann")).getValue(TAuthor_LAST_NAME()));

        assertEquals(null, create().fetchOne(
            TAuthor(),
            TAuthor_LAST_NAME().equal("Kaestner")));
        invoke(cRoutines(), "pCreateAuthorByName", create().configuration(), null, "Kaestner");
        assertEquals("Kaestner", create().fetchOne(
            TAuthor(),
            TAuthor_LAST_NAME().equal("Kaestner")).getValue(TAuthor_LAST_NAME()));

        // P391, a test for properly binding and treating various IN, OUT, INOUT
        // parameters
        // ---------------------------------------------------------------------
        if (supportsOUTParameters() &&
            dialect() != SQLDialect.FIREBIRD) {

            // TODO: [#396] MySQL seems to have a bug when passing null to IN/OUT
            // parameters. Check back on this, when this is fixed.
            if (dialect() != SQLDialect.MYSQL &&
                dialect() != SQLDialect.MARIADB) {
                Object p391a = invoke(cRoutines(), "p391", create().configuration(), null, null, DUMMY_OUT_INT, DUMMY_OUT_INT, null, null);
                assertEquals(null, invoke(p391a, "getIo1"));
                assertEquals(null, invoke(p391a, "getO1"));
                assertEquals(null, invoke(p391a, "getIo2"));
                assertEquals(null, invoke(p391a, "getO2"));
            }

            // TODO: [#459] Sybase messes up IN/OUT parameter orders.
            // Check back on this, when this is fixed.
            if (true/* [pro] */ && dialect() != SQLDialect.SYBASE/* [/pro] */) {
                Object p391b = invoke(cRoutines(), "p391", create().configuration(), null, 2, DUMMY_OUT_INT, DUMMY_OUT_INT, 3, null);
                assertEquals(null, invoke(p391b, "getIo1"));
                assertEquals("2", "" + invoke(p391b, "getO1"));
                assertEquals(null, invoke(p391b, "getIo2"));
                assertEquals("3", "" + invoke(p391b, "getO2"));

                Object p391c = invoke(cRoutines(), "p391", create().configuration(), 1, 2, DUMMY_OUT_INT, DUMMY_OUT_INT, 3, 4);
                assertEquals("1", "" + invoke(p391c, "getIo1"));
                assertEquals("2", "" + invoke(p391c, "getO1"));
                assertEquals("4", "" + invoke(p391c, "getIo2"));
                assertEquals("3", "" + invoke(p391c, "getO2"));
            }
        }

        // F378, which is a stored function with OUT parameters
        // ---------------------------------------------------------------------
        /* [pro] */
        switch (dialect().family()) {

            // Currently, this is only supported for oracle
            case ORACLE:
                Object result1a = invoke(cRoutines(), "f378", create().configuration(), null, null);
                assertEquals(null, invoke(result1a, "getIo"));
                assertEquals(null, invoke(result1a, "getO"));
                assertEquals(null, invoke(result1a, "getReturnValue"));

                Object result2a = invoke(cRoutines(), "f378", create().configuration(), null, 2);
                assertEquals(null, invoke(result2a, "getIo"));
                assertEquals("2", "" + invoke(result2a, "getO"));
                assertEquals(null, invoke(result2a, "getReturnValue"));

                Object result3a = invoke(cRoutines(), "f378", create().configuration(), 1, 2);
                assertEquals("1", "" + invoke(result3a, "getIo"));
                assertEquals("2", "" + invoke(result3a, "getO"));
                assertEquals("3", "" + invoke(result3a, "getReturnValue"));

                Object result1b = invoke(cLibrary(), "pkgF378", create().configuration(), null, null);
                assertEquals(null, invoke(result1b, "getIo"));
                assertEquals(null, invoke(result1b, "getO"));
                assertEquals(null, invoke(result1b, "getReturnValue"));

                Object result2b = invoke(cLibrary(), "pkgF378", create().configuration(), null, 2);
                assertEquals(null, invoke(result2b, "getIo"));
                assertEquals("2", "" + invoke(result2b, "getO"));
                assertEquals(null, invoke(result2b, "getReturnValue"));

                Object result3b = invoke(cLibrary(), "pkgF378", create().configuration(), 1, 2);
                assertEquals("1", "" + invoke(result3b, "getIo"));
                assertEquals("2", "" + invoke(result3b, "getO"));
                assertEquals("3", "" + invoke(result3b, "getReturnValue"));
                break;
        }
        /* [/pro] */
    }

    public void testStoredProcedureWithResultSets() {
        assumeNotNull(cRoutines());

        Reflect presults;
        Reflect presultsAndOutParameters;
        try {
            presults = Reflect.on(cRoutines().getPackage().getName() + ".routines.PResults");
            presultsAndOutParameters = Reflect.on(cRoutines().getPackage().getName() + ".routines.PResultsAndOutParameters");
            assumeNotNull(presults.create().<Object>get());
            assumeNotNull(presultsAndOutParameters.create().<Object>get());
        }
        catch (ReflectException e) {
            log.info("SKIPPING", "procedure tests with default parameters");
            return;
        }

        Reflect executedPResults = presults.create();

        executedPResults.call("setPResultSets", 0);
        executedPResults.<Routine<?>>get().execute(create().configuration());
        assertEquals(0, executedPResults.<Routine<?>>get().getResults().size());

        executedPResults.call("setPResultSets", 1);
        executedPResults.<Routine<?>>get().execute(create().configuration());
        assertEquals(1, executedPResults.<Routine<?>>get().getResults().size());
        assertEquals("a", executedPResults.<Routine<?>>get().getResults().get(0).field(0).getName());
        assertEquals(Arrays.asList(1), executedPResults.<Routine<?>>get().getResults().get(0).sortAsc(0).getValues(0, int.class));

        executedPResults.call("setPResultSets", 2);
        executedPResults.<Routine<?>>get().execute(create().configuration());
        assertEquals(2, executedPResults.<Routine<?>>get().getResults().size());
        assertEquals("a", executedPResults.<Routine<?>>get().getResults().get(0).field(0).getName());
        assertEquals("b", executedPResults.<Routine<?>>get().getResults().get(1).field(0).getName());
        assertEquals(Arrays.asList(1), executedPResults.<Routine<?>>get().getResults().get(0).sortAsc(0).getValues(0, int.class));
        assertEquals(Arrays.asList(1, 2), executedPResults.<Routine<?>>get().getResults().get(1).sortAsc(0).getValues(0, int.class));

        executedPResults.call("setPResultSets", 3);
        executedPResults.<Routine<?>>get().execute(create().configuration());
        assertEquals(3, executedPResults.<Routine<?>>get().getResults().size());
        assertEquals("a", executedPResults.<Routine<?>>get().getResults().get(0).field(0).getName());
        assertEquals("b", executedPResults.<Routine<?>>get().getResults().get(1).field(0).getName());
        assertEquals("c", executedPResults.<Routine<?>>get().getResults().get(2).field(0).getName());
        assertEquals(Arrays.asList(1), executedPResults.<Routine<?>>get().getResults().get(0).sortAsc(0).getValues(0, int.class));
        assertEquals(Arrays.asList(1, 2), executedPResults.<Routine<?>>get().getResults().get(1).sortAsc(0).getValues(0, int.class));
        assertEquals(Arrays.asList(1, 2, 3), executedPResults.<Routine<?>>get().getResults().get(2).sortAsc(0).getValues(0, int.class));



        Reflect executedPResultsAndOutParameters = presultsAndOutParameters.create();

        executedPResultsAndOutParameters.call("setPResultSets", 0);
        executedPResultsAndOutParameters.<Routine<?>>get().execute(create().configuration());
        assertEquals(0, executedPResultsAndOutParameters.<Routine<?>>get().getResults().size());
        assertEquals(0, (int) executedPResultsAndOutParameters.call("getPCount").get());

        executedPResultsAndOutParameters.call("setPResultSets", 1);
        executedPResultsAndOutParameters.<Routine<?>>get().execute(create().configuration());
        assertEquals(1, executedPResultsAndOutParameters.<Routine<?>>get().getResults().size());
        assertEquals(1, (int) executedPResultsAndOutParameters.call("getPCount").get());
        assertEquals("a", executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(0).field(0).getName());
        assertEquals(Arrays.asList(1), executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(0).sortAsc(0).getValues(0, int.class));

        executedPResultsAndOutParameters.call("setPResultSets", 2);
        executedPResultsAndOutParameters.<Routine<?>>get().execute(create().configuration());
        assertEquals(2, executedPResultsAndOutParameters.<Routine<?>>get().getResults().size());
        assertEquals(2, (int) executedPResultsAndOutParameters.call("getPCount").get());
        assertEquals("a", executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(0).field(0).getName());
        assertEquals("b", executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(1).field(0).getName());
        assertEquals(Arrays.asList(1), executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(0).sortAsc(0).getValues(0, int.class));
        assertEquals(Arrays.asList(1, 2), executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(1).sortAsc(0).getValues(0, int.class));

        executedPResultsAndOutParameters.call("setPResultSets", 3);
        executedPResultsAndOutParameters.<Routine<?>>get().execute(create().configuration());
        assertEquals(3, executedPResultsAndOutParameters.<Routine<?>>get().getResults().size());
        assertEquals(3, (int) executedPResultsAndOutParameters.call("getPCount").get());
        assertEquals("a", executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(0).field(0).getName());
        assertEquals("b", executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(1).field(0).getName());
        assertEquals("c", executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(2).field(0).getName());
        assertEquals(Arrays.asList(1), executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(0).sortAsc(0).getValues(0, int.class));
        assertEquals(Arrays.asList(1, 2), executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(1).sortAsc(0).getValues(0, int.class));
        assertEquals(Arrays.asList(1, 2, 3), executedPResultsAndOutParameters.<Routine<?>>get().getResults().get(2).sortAsc(0).getValues(0, int.class));
    }

    public void testStoredProcedureWithDefaultParameters() {
        assumeNotNull(cRoutines());

        Reflect pdefault;
        try {
            pdefault = Reflect.on(cRoutines().getPackage().getName() + ".routines.PDefault");

            assertTrue(pdefault.field("P_IN_NUMBER").call("isDefaulted").<Boolean>get());
        }
        catch (ReflectException e) {
            log.info("SKIPPING", "procedure tests with default parameters");
            return;
        }

        // Call with defaulted IN parameters only
        Reflect executedWithDefaults = pdefault.create();
        executedWithDefaults.call("execute", create().configuration());
        assertEquals(0, executedWithDefaults.call("getPOutNumber").<Number>get().intValue());
        assertEquals("0", executedWithDefaults.call("getPOutVarchar").get());
        assertEquals(Date.valueOf("1981-07-10"), executedWithDefaults.call("getPOutDate").get());

        // Call with some values provided
        Reflect executedWithSomeDefaults1 = pdefault.create();
        executedWithSomeDefaults1.call("setPInNumber", 456);
        executedWithSomeDefaults1.call("execute", create().configuration());
        assertEquals(456, executedWithSomeDefaults1.call("getPOutNumber").<Number>get().intValue());
        assertEquals("0", executedWithSomeDefaults1.call("getPOutVarchar").get());
        assertEquals(Date.valueOf("1981-07-10"), executedWithSomeDefaults1.call("getPOutDate").get());

        // Call with some values provided
        Reflect executedWithSomeDefaults2 = pdefault.create();
        executedWithSomeDefaults2.call("setPInVarchar", "xyz");
        executedWithSomeDefaults2.call("execute", create().configuration());
        assertEquals(0, executedWithSomeDefaults2.call("getPOutNumber").<Number>get().intValue());
        assertEquals("xyz", executedWithSomeDefaults2.call("getPOutVarchar").get());
        assertEquals(Date.valueOf("1981-07-10"), executedWithSomeDefaults2.call("getPOutDate").get());

        // Call with all values provided
        Reflect executedWithoutDefault = pdefault.create();
        executedWithoutDefault.call("setPInNumber", 123);
        executedWithoutDefault.call("setPInVarchar", "abc");
        executedWithoutDefault.call("setPInDate", Date.valueOf("2012-01-01"));
        executedWithoutDefault.call("execute", create().configuration());
        assertEquals(123, executedWithoutDefault.call("getPOutNumber").<Number>get().intValue());
        assertEquals("abc", executedWithoutDefault.call("getPOutVarchar").get());
        assertEquals(Date.valueOf("2012-01-01"), executedWithoutDefault.call("getPOutDate").get());
    }

    public void testStoredFunctions() throws Exception {
        Assume.assumeNotNull(FOneField());
        jOOQAbstractTest.reset = false;

        // ---------------------------------------------------------------------
        // Standalone calls
        // ---------------------------------------------------------------------
        assertEquals("0", "" + invoke(cRoutines(), "fAuthorExists", create().configuration(), null));
        assertEquals("1", "" + invoke(cRoutines(), "fAuthorExists", create().configuration(), "Paulo"));
        assertEquals("0", "" + invoke(cRoutines(), "fAuthorExists", create().configuration(), "Shakespeare"));
        assertEquals("1", "" + invoke(cRoutines(), "fOne", create().configuration()));
        assertEquals("1", "" + invoke(cRoutines(), "fNumber", create().configuration(), 1));
        assertEquals(null, invoke(cRoutines(), "fNumber", create().configuration(), null));
        assertEquals("1204", "" + invoke(cRoutines(), "f317", create().configuration(), 1, 2, 3, 4));
        assertEquals("1204", "" + invoke(cRoutines(), "f317", create().configuration(), 1, 2, null, 4));
        assertEquals("4301", "" + invoke(cRoutines(), "f317", create().configuration(), 4, 3, 2, 1));
        assertEquals("4301", "" + invoke(cRoutines(), "f317", create().configuration(), 4, 3, null, 1));
        assertEquals("1101", "" + invoke(cRoutines(), "f317", create().configuration(), 1, 1, 1, 1));
        assertEquals("1101", "" + invoke(cRoutines(), "f317", create().configuration(), 1, 1, null, 1));

        // ---------------------------------------------------------------------
        // Embedded calls
        // ---------------------------------------------------------------------
        Field<Integer> f1a = FAuthorExistsField("Paulo").cast(Integer.class);
        Field<Integer> f2a = FAuthorExistsField("Shakespeare").cast(Integer.class);
        Field<Integer> f3a = FOneField().cast(Integer.class);
        Field<Integer> f4a = FNumberField(42).cast(Integer.class);
        Field<Integer> f5a = FNumberField(FNumberField(FOneField())).cast(Integer.class);
        Field<Integer> f6a = F317Field(1, 2, null, 4).cast(Integer.class);
        Field<Integer> f7a = F317Field(4, 3, null, 1).cast(Integer.class);
        Field<Integer> f8a = F317Field(1, 1, null, 1).cast(Integer.class);
        Field<Integer> f9a = F317Field(FNumberField(1), FNumberField(2), FNumberField((Number) null), FNumberField(4)).cast(Integer.class);

        // Repeat fields to check correct fetching from resultset
        Field<Integer> f1b = FAuthorExistsField("Paulo").cast(Integer.class);
        Field<Integer> f2b = FAuthorExistsField("Shakespeare").cast(Integer.class);
        Field<Integer> f3b = FOneField().cast(Integer.class);
        Field<Integer> f4b = FNumberField(42).cast(Integer.class);
        Field<Integer> f5b = FNumberField(FNumberField(FOneField())).cast(Integer.class);
        Field<Integer> f6b = F317Field(1, 2, 3, 4).cast(Integer.class);
        Field<Integer> f7b = F317Field(4, 3, 2, 1).cast(Integer.class);
        Field<Integer> f8b = F317Field(1, 1, 1, 1).cast(Integer.class);
        Field<Integer> f9b = F317Field(FNumberField(1), FNumberField(2), FNumberField(3), FNumberField(4)).cast(Integer.class);

        // Null argument checks
        Field<Integer> f10 = FAuthorExistsField(null).cast(Integer.class);

        SelectQuery<Record> q = create().selectQuery();
        q.addSelect(
            f1a, f2a, f3a, f4a, f5a, f6a, f7a, f8a, f9a,
            f1b, f2b, f3b, f4b, f5b, f6b, f7b, f8b, f9b, f10);
        q.execute();
        Result<Record> result1 = q.getResult();

        assertEquals(1, result1.size());
        assertEquals("1", result1.get(0).getValue(f1a, String.class));
        assertEquals("0", result1.get(0).getValue(f2a, String.class));
        assertEquals("1", result1.get(0).getValue(f3a, String.class));
        assertEquals("42", result1.get(0).getValue(f4a, String.class));
        assertEquals("1", result1.get(0).getValue(f5a, String.class));
        assertEquals("1204", result1.get(0).getValue(f6a, String.class));
        assertEquals("4301", result1.get(0).getValue(f7a, String.class));
        assertEquals("1101", result1.get(0).getValue(f8a, String.class));
        assertEquals("1204", result1.get(0).getValue(f9a, String.class));

        assertEquals("1", result1.get(0).getValue(f1b, String.class));
        assertEquals("0", result1.get(0).getValue(f2b, String.class));
        assertEquals("1", result1.get(0).getValue(f3b, String.class));
        assertEquals("42", result1.get(0).getValue(f4b, String.class));
        assertEquals("1", result1.get(0).getValue(f5b, String.class));
        assertEquals("1204", result1.get(0).getValue(f6b, String.class));
        assertEquals("4301", result1.get(0).getValue(f7b, String.class));
        assertEquals("1101", result1.get(0).getValue(f8b, String.class));
        assertEquals("1204", result1.get(0).getValue(f9b, String.class));

        assertEquals("0", result1.get(0).getValue(f10, String.class));

        // ---------------------------------------------------------------------
        // Functions in conditions
        // ---------------------------------------------------------------------
        assertEquals(Integer.valueOf(1),
            create().selectOne().where(f4b.equal(1)).or(f1b.equal(1)).fetchOne(0));
        assertEquals(null,
            create().selectOne().where(f4b.equal(1)).and(f1b.equal(1)).fetchOne(0));
        assertEquals(null,
            create().selectOne().where(f4b.equal(1)).and(f1b.equal(1)).fetchOne());

        // ---------------------------------------------------------------------
        // Functions in SQL
        // ---------------------------------------------------------------------
        Result<Record3<Integer, Integer, Integer>> result2 = create().select(
                FNumberField(1).cast(Integer.class),
                FNumberField(TAuthor_ID()).cast(Integer.class),
                FNumberField(FNumberField(TAuthor_ID())).cast(Integer.class))
            .from(TAuthor())
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(Integer.valueOf(1), result2.getValue(0, 0));
        assertEquals(Integer.valueOf(1), result2.getValue(0, 1));
        assertEquals(Integer.valueOf(1), result2.getValue(0, 2));
        assertEquals(Integer.valueOf(1), result2.getValue(1, 0));
        assertEquals(Integer.valueOf(2), result2.getValue(1, 1));
        assertEquals(Integer.valueOf(2), result2.getValue(1, 2));
    }

    @SuppressWarnings("unchecked")
    public void testScalarSubqueryCaching() throws Exception {
        Assume.assumeNotNull(FOneField());

        DSLContext create = create(create().settings().withRenderScalarSubqueriesForStoredFunctions(true));

        Field<Number> f = (Field<Number>) FOneField();
        Select<Record1<Number>> select = create.select(f).where(f.eq(f));

        assertEquals(FOneField().getDataType().convert(1), create.fetchValue(select));
        assertTrue(create.render(select).contains("select (select"));
        assertTrue(create.render(select).contains("= (select"));
    }

    public void testStoredFunctionsWithNoSchema() throws Exception {
        Assume.assumeNotNull(FOneField());

        /* [pro] */
        // DB2 seems not to allow unqualified function calls, even in the local schema
        if (dialect().family() == DB2) {
            log.info("SKIPPING", "functions test with no schema");
            return;
        }
        /* [/pro] */

        assertEquals(42, (int) create(new Settings().withRenderSchema(false))
            .select(FNumberField(42).cast(Integer.class))
            .fetchOne(0, Integer.class));
    }

    @SuppressWarnings({ "unchecked" })
    public void testARRAYType() throws Exception {
        if (TArrays() == null) {
            log.info("SKIPPING", "ARRAY type test");
            return;
        }

        jOOQAbstractTest.reset = false;

        /* [pro] */
        if (TArrays_STRING_R() != null) {
            Result<?> arrays = create().select(
                TArrays_STRING_R(),
                TArrays_NUMBER_R(),
                TArrays_DATE_R())
            .from(TArrays())
            .orderBy(TArrays_ID())
            .fetch();

            assertEquals(null, arrays.getValue(0, TArrays_STRING_R()));
            assertEquals(null, arrays.getValue(0, TArrays_NUMBER_R()));
            assertEquals(null, arrays.getValue(0, TArrays_DATE_R()));

            assertEquals(Arrays.asList(), arrays.getValue(1, TArrays_STRING_R()));
            assertEquals(Arrays.asList(), arrays.getValue(1, TArrays_NUMBER_R()));
            assertEquals(Arrays.asList(), arrays.getValue(1, TArrays_DATE_R()));

            assertEquals(Arrays.asList("a"), arrays.getValue(2, TArrays_STRING_R()));
            assertEquals(Arrays.asList(1), arrays.getValue(2, TArrays_NUMBER_R()));
            assertEquals("[1981-07-10]", arrays.getValue(2, TArrays_DATE_R()).toString());

            assertEquals(Arrays.asList("a", "b"), arrays.getValue(3, TArrays_STRING_R()));
            assertEquals(Arrays.asList(1, 2), arrays.getValue(3, TArrays_NUMBER_R()));
            assertEquals("[1981-07-10, 2000-01-01]", arrays.getValue(3, TArrays_DATE_R()).toString());



            InsertQuery<?> insert = create().insertQuery(TArrays());
            insert.addValue(TArrays_ID(), 5);
            insert.addValue((Field<ArrayRecord<Integer>>) TArrays_NUMBER_R(),
                on(TArrays_NUMBER_R().getType()).create(create().configuration(), new Integer[] { 1, 2, 3 }).<ArrayRecord<Integer>>get());
            insert.addValue((Field<ArrayRecord<String>>) TArrays_STRING_R(),
                on(TArrays_STRING_R().getType()).create(create().configuration(), new String[] { "a", "b", "c", "d\"\\d" }).<ArrayRecord<String>>get());
            insert.addValue((Field<ArrayRecord<Date>>) TArrays_DATE_R(),
                on(TArrays_DATE_R().getType()).create(create().configuration(), new Date[] { new Date(0), new Date(84600 * 1000), new Date(84600 * 2000) }).<ArrayRecord<Date>>get());
            insert.execute();

            Record array = create().select(
                TArrays_STRING_R(),
                TArrays_NUMBER_R(),
                TArrays_DATE_R())
            .from(TArrays())
            .where(TArrays_ID().equal(5))
            .fetchOne();

            assertEquals(Arrays.asList("a", "b", "c", "d\"\\d"), array.getValue(TArrays_STRING_R()));
            assertEquals(Arrays.asList(1, 2, 3), array.getValue(TArrays_NUMBER_R()));
            assertEquals("[1970-01-01, 1970-01-02, 1970-01-03]", array.getValue(TArrays_DATE_R()).toString());



            UpdateQuery<X> update = create().updateQuery(TArrays());
            update.addValue((Field<ArrayRecord<Integer>>) TArrays_NUMBER_R(),
                on(TArrays_NUMBER_R().getType()).create(create().configuration(), new Integer[] { 3, 2, 1 }).<ArrayRecord<Integer>>get());
            update.addValue((Field<ArrayRecord<String>>) TArrays_STRING_R(),
                on(TArrays_STRING_R().getType()).create(create().configuration(), new String[] { "d\"\\d", "c", "b", "a" }).<ArrayRecord<String>>get());
            update.addValue((Field<ArrayRecord<Date>>) TArrays_DATE_R(),
                on(TArrays_DATE_R().getType()).create(create().configuration(), new Date[] { new Date(84600 * 2000), new Date(84600 * 1000), new Date(0) }).<ArrayRecord<Date>>get());
            update.addConditions(TArrays_ID().equal(5));
            update.execute();

            array = create().select(
                TArrays_STRING_R(),
                TArrays_NUMBER_R(),
                TArrays_DATE_R())
            .from(TArrays())
            .where(TArrays_ID().equal(5))
            .fetchOne();

            assertEquals(Arrays.asList("d\"\\d", "c", "b", "a"), array.getValue(TArrays_STRING_R()));
            assertEquals(Arrays.asList(3, 2, 1), array.getValue(TArrays_NUMBER_R()));
            assertEquals("[1970-01-03, 1970-01-02, 1970-01-01]", array.getValue(TArrays_DATE_R()).toString());
        }
        /* [/pro] */

        if (TArrays_STRING() != null) {
            Result<?> arrays = create().select(
                TArrays_STRING(),
                TArrays_NUMBER(),
                TArrays_DATE(),
                TArrays_UDT() == null ? val(0) : TArrays_UDT())
            .from(TArrays())
            .orderBy(TArrays_ID())
            .fetch();

            // First record: null
            // -----------------------------------------------------------------
            assertNull(arrays.getValue(0, TArrays_STRING()));
            assertNull(arrays.getValue(0, TArrays_NUMBER()));
            assertNull(arrays.getValue(0, TArrays_DATE()));

            if (TArrays_UDT() != null) {
                assertNull(arrays.getValue(0, TArrays_UDT()));
            }


            // Second record: empty
            // -----------------------------------------------------------------
            // These expressions are a bit verbose. Unfortunately, H2 does not
            // support typed arrays, hence the tests are kept general
            // http://groups.google.com/group/h2-database/browse_thread/thread/42e38afa682d4fc2
            Object[] s = (Object[]) arrays.getValue(1, 0);
            Object[] n = (Object[]) arrays.getValue(1, 1);
            Object[] d = (Object[]) arrays.getValue(1, 2);
            assertEquals(0, s.length);
            assertEquals(0, n.length);
            assertEquals(0, d.length);
            assertEquals(TArrays_STRING().getType(), s.getClass());
            assertEquals(TArrays_NUMBER().getType(), n.getClass());
            assertEquals(TArrays_DATE().getType(), d.getClass());

            if (TArrays_UDT() != null) {
                UDTRecord<?>[] u = (UDTRecord<?>[]) arrays.getValue(1, 3);
                assertEquals(0, u.length);
                assertEquals(TArrays_UDT().getType(), u.getClass());
            }

            // Third record: one element
            // -----------------------------------------------------------------
            s = (Object[]) arrays.getValue(2, 0);
            n = (Object[]) arrays.getValue(2, 1);
            d = (Object[]) arrays.getValue(2, 2);
            assertEquals(1, s.length);
            assertEquals(1, n.length);
            assertEquals(1, d.length);
            assertEquals(TArrays_STRING().getType(), s.getClass());
            assertEquals(TArrays_NUMBER().getType(), n.getClass());
            assertEquals(TArrays_DATE().getType(), d.getClass());
            assertEquals("a", s[0].toString());
            assertEquals("1", n[0].toString());
            assertEquals("1981-07-10", d[0].toString());

            if (TArrays_UDT() != null) {
                UDTRecord<?>[] u = (UDTRecord<?>[]) arrays.getValue(2, 3);
                assertEquals(1, u.length);
                assertEquals(TArrays_UDT().getType(), u.getClass());
                assertEquals("Downing Street", u[0].getValue(0));
                assertEquals("10", u[0].getValue(1));
                assertNull(u[0].getValue(2));
            }

            // Fourth record: two elements
            // -----------------------------------------------------------------
            s = (Object[]) arrays.getValue(3, 0);
            n = (Object[]) arrays.getValue(3, 1);
            d = (Object[]) arrays.getValue(3, 2);
            assertEquals(2, s.length);
            assertEquals(2, n.length);
            assertEquals(2, d.length);
            assertEquals(TArrays_STRING().getType(), s.getClass());
            assertEquals(TArrays_NUMBER().getType(), n.getClass());
            assertEquals(TArrays_DATE().getType(), d.getClass());
            assertEquals("a", s[0].toString());
            assertEquals("b", s[1].toString());
            assertEquals("1", n[0].toString());
            assertEquals("2", n[1].toString());
            assertEquals("1981-07-10", d[0].toString());
            assertEquals("2000-01-01", d[1].toString());

            if (TArrays_UDT() != null) {
                UDTRecord<?>[] u = (UDTRecord<?>[]) arrays.getValue(3, 3);
                assertEquals(2, u.length);
                assertEquals(TArrays_UDT().getType(), u.getClass());

                assertEquals("Downing Street", u[0].getValue(0));
                assertEquals("10", u[0].getValue(1));
                Integer[] floors = (Integer[]) u[0].getValue(2);
                assertEquals(0, floors.length);

                assertEquals("Bahnhofstrasse", u[1].getValue(0));
                assertEquals("12", u[1].getValue(1));
                floors = (Integer[]) u[1].getValue(2);
                assertEquals(2, floors.length);
                assertEquals(1, (int) floors[0]);
                assertEquals(2, (int) floors[1]);
            }


            // Insert again
            // -----------------------------------------------------------------
            InsertQuery<?> insert = create().insertQuery(TArrays());
            insert.addValue(TArrays_ID(), 5);
            insert.addValue(TArrays_NUMBER(), new Integer[] { 1, 2, 3 });
            insert.addValue(TArrays_STRING(), new String[] { "a", "b", "c", "d\"\\d" });
            insert.addValue(TArrays_DATE(), new Date[] { new Date(0), new Date(84600 * 1000), new Date(84600 * 2000)});

            insert.execute();

            Record array = create().select(
                    TArrays_STRING(),
                    TArrays_NUMBER(),
                    TArrays_DATE())
                .from(TArrays())
                .where(TArrays_ID().equal(5))
                .fetchOne();

            s = (Object[]) array.getValue(0);
            n = (Object[]) array.getValue(1);
            d = (Object[]) array.getValue(2);
            assertEquals(4, s.length);
            assertEquals(3, n.length);
            assertEquals(3, d.length);
            assertEquals(String[].class, s.getClass());
            assertEquals(Integer[].class, n.getClass());
            assertEquals(Date[].class, d.getClass());
            assertEquals("a", s[0].toString());
            assertEquals("b", s[1].toString());
            assertEquals("c", s[2].toString());
            assertEquals("d\"\\d", s[3].toString());
            assertEquals("1", n[0].toString());
            assertEquals("2", n[1].toString());
            assertEquals("3", n[2].toString());
            assertEquals(zeroDate(), d[0].toString());
            assertEquals(zeroDatePlusOneDay(), d[1].toString());
            assertEquals(zeroDatePlusTwoDays(), d[2].toString());



            UpdateQuery<X> update = create().updateQuery(TArrays());
            update.addValue(TArrays_NUMBER(), new Integer[] { 3, 2, 1});
            update.addValue(TArrays_STRING(), new String[] { "d\"\\d", "c", "b", "a" });
            update.addValue(TArrays_DATE(), new Date[] { new Date(84600 * 2000), new Date(84600 * 1000), new Date(0) });
            update.addConditions(TArrays_ID().equal(5));
            update.execute();

            array = create().select(
                TArrays_STRING(),
                TArrays_NUMBER(),
                TArrays_DATE())
            .from(TArrays())
            .where(TArrays_ID().equal(5))
            .fetchOne();

            s = (Object[]) array.getValue(0);
            n = (Object[]) array.getValue(1);
            d = (Object[]) array.getValue(2);
            assertEquals(4, s.length);
            assertEquals(3, n.length);
            assertEquals(3, d.length);
            assertEquals(String[].class, s.getClass());
            assertEquals(Integer[].class, n.getClass());
            assertEquals(Date[].class, d.getClass());
            assertEquals("d\"\\d", s[0].toString());
            assertEquals("c", s[1].toString());
            assertEquals("b", s[2].toString());
            assertEquals("a", s[3].toString());
            assertEquals("3", n[0].toString());
            assertEquals("2", n[1].toString());
            assertEquals("1", n[2].toString());
            assertEquals(zeroDatePlusTwoDays(), d[0].toString());
            assertEquals(zeroDatePlusOneDay(), d[1].toString());
            assertEquals(zeroDate(), d[2].toString());
        }
    }

    public void testARRAYProcedure() throws Exception {
        if (cRoutines() == null) {
            log.info("SKIPPING", "ARRAY procedure test (no procedure support)");
            return;
        }

        if (TArrays() == null) {
            log.info("SKIPPING", "ARRAY procedure test (no array support)");
            return;
        }

        jOOQAbstractTest.reset = false;

        /* [pro] */
        if (TArrays_STRING_R() != null) {
            ArrayRecord<Integer> i;
            ArrayRecord<Long> l;
            ArrayRecord<String> s;

            assertEquals(null, invoke(cRoutines(), "pArrays1", create().configuration(), null));
            assertEquals(null, invoke(cRoutines(), "pArrays2", create().configuration(), null));
            assertEquals(null, invoke(cRoutines(), "pArrays3", create().configuration(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays1", create().configuration(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays2", create().configuration(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays3", create().configuration(), null));

            i = newNUMBER_R();
            l = newNUMBER_LONG_R();
            s = newSTRING_R();

            assertEquals(
                Arrays.asList(new Integer[0]),
                invoke(cRoutines(), "pArrays1", create().configuration(), i));
            assertEquals(
                Arrays.asList(new Long[0]),
                invoke(cRoutines(), "pArrays2", create().configuration(), l));
            assertEquals(
                Arrays.asList(new String[0]),
                invoke(cRoutines(), "pArrays3", create().configuration(), s));
            assertEquals(
                Arrays.asList(new Integer[0]),
                invoke(cRoutines(), "fArrays1", create().configuration(), i));
            assertEquals(
                Arrays.asList(new Long[0]),
                invoke(cRoutines(), "fArrays2", create().configuration(), l));
            assertEquals(
                Arrays.asList(new String[0]),
                invoke(cRoutines(), "fArrays3", create().configuration(), s));

            i = newNUMBER_R();
            l = newNUMBER_LONG_R();
            s = newSTRING_R();

            i.add((Integer) null);
            l.add((Long) null);
            s.add((String) null);

            assertEquals(
                Arrays.asList((Integer) null),
                invoke(cRoutines(), "pArrays1", create().configuration(), i));
            assertEquals(
                Arrays.asList((Long) null),
                invoke(cRoutines(), "pArrays2", create().configuration(), l));
            assertEquals(
                Arrays.asList((String) null),
                invoke(cRoutines(), "pArrays3", create().configuration(), s));
            assertEquals(
                Arrays.asList((Integer) null),
                invoke(cRoutines(), "fArrays1", create().configuration(), i));
            assertEquals(
                Arrays.asList((Long) null),
                invoke(cRoutines(), "fArrays2", create().configuration(), l));
            assertEquals(
                Arrays.asList((String) null),
                invoke(cRoutines(), "fArrays3", create().configuration(), s));

            i = newNUMBER_R();
            l = newNUMBER_LONG_R();
            s = newSTRING_R();

            i.addAll(asList(1, 2));
            l.addAll(asList(1L, 2L));
            s.addAll(asList("1", "2"));

            assertEquals(
                Arrays.asList(1, 2),
                invoke(cRoutines(), "pArrays1", create().configuration(), i));
            assertEquals(
                Arrays.asList(1L, 2L),
                invoke(cRoutines(), "pArrays2", create().configuration(), l));
            assertEquals(
                Arrays.asList("1", "2"),
                invoke(cRoutines(), "pArrays3", create().configuration(), s));
            assertEquals(
                Arrays.asList(1, 2),
                invoke(cRoutines(), "fArrays1", create().configuration(), i));
            assertEquals(
                Arrays.asList(1L, 2L),
                invoke(cRoutines(), "fArrays2", create().configuration(), l));
            assertEquals(
                Arrays.asList("1", "2"),
                invoke(cRoutines(), "fArrays3", create().configuration(), s));
        }
        /* [/pro] */

        if (TArrays_STRING() != null) {
            if (supportsOUTParameters()) {
                assertEquals(null, invoke(cRoutines(), "pArrays1", create().configuration(), null));
                assertEquals(null, invoke(cRoutines(), "pArrays2", create().configuration(), null));
                assertEquals(null, invoke(cRoutines(), "pArrays3", create().configuration(), null));
            }

            assertEquals(null, invoke(cRoutines(), "fArrays1", create().configuration(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays2", create().configuration(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays3", create().configuration(), null));

            if (supportsOUTParameters()) {
                assertEquals(
                    Arrays.asList(new Integer[0]),
                    Arrays.asList((Integer[]) invoke(cRoutines(), "pArrays1", create().configuration(), new Integer[0])));
                assertEquals(
                    Arrays.asList(new Long[0]),
                    Arrays.asList((Long[]) invoke(cRoutines(), "pArrays2", create().configuration(), new Long[0])));
                assertEquals(
                    Arrays.asList(new String[0]),
                    Arrays.asList((String[]) invoke(cRoutines(), "pArrays3", create().configuration(), new String[0])));
            }

            assertEquals(
                Arrays.asList(new Integer[0]),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays1", create().configuration(), new Integer[0])));
            assertEquals(
                Arrays.asList(new Long[0]),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays2", create().configuration(), new Long[0])));
            assertEquals(
                Arrays.asList(new String[0]),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays3", create().configuration(), new String[0])));

            if (supportsOUTParameters()) {
                assertEquals(
                    Arrays.asList((Integer) null),
                    Arrays.asList((Integer[]) invoke(cRoutines(), "pArrays1", create().configuration(), new Integer[] { null })));
                assertEquals(
                    Arrays.asList((Long) null),
                    Arrays.asList((Long[]) invoke(cRoutines(), "pArrays2", create().configuration(), new Long[] { null })));
                assertEquals(
                    Arrays.asList((String) null),
                    Arrays.asList((String[]) invoke(cRoutines(), "pArrays3", create().configuration(), new String[] { null })));
            }

            assertEquals(
                Arrays.asList((Integer) null),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays1", create().configuration(), new Integer[] { null })));
            assertEquals(
                Arrays.asList((Long) null),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays2", create().configuration(), new Long[] { null })));
            assertEquals(
                Arrays.asList((String) null),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays3", create().configuration(), new String[] { null })));

            if (supportsOUTParameters()) {
                assertEquals(
                    Arrays.asList(1, 2),
                    Arrays.asList((Integer[]) invoke(cRoutines(), "pArrays1", create().configuration(), new Integer[] {1, 2})));
                assertEquals(
                    Arrays.asList(1L, 2L),
                    Arrays.asList((Long[]) invoke(cRoutines(), "pArrays2", create().configuration(), new Long[] {1L, 2L})));
                assertEquals(
                    Arrays.asList("1", "2"),
                    Arrays.asList((String[]) invoke(cRoutines(), "pArrays3", create().configuration(), new String[] {"1", "2"})));
            }

            assertEquals(
                Arrays.asList(1, 2),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays1", create().configuration(), new Integer[] {1, 2})));
            assertEquals(
                Arrays.asList(1L, 2L),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays2", create().configuration(), new Long[] {1L, 2L})));
            assertEquals(
                Arrays.asList("1", "2"),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays3", create().configuration(), new String[] {"1", "2"})));
        }
    }

    /* [pro] */
    private ArrayRecord<Integer> newNUMBER_R() throws Exception {
        ArrayRecord<Integer> result = TArrays_NUMBER_R().getType().getConstructor(Configuration.class).newInstance(create().configuration());
        return result;
    }

    private ArrayRecord<Long> newNUMBER_LONG_R() throws Exception {
        ArrayRecord<Long> result = TArrays_NUMBER_LONG_R().getType().getConstructor(Configuration.class).newInstance(create().configuration());
        return result;
    }

    private ArrayRecord<String> newSTRING_R() throws Exception {
        ArrayRecord<String> result = TArrays_STRING_R().getType().getConstructor(Configuration.class).newInstance(create().configuration());
        return result;
    }

    /* [/pro] */

    public void testUDTs() throws Exception {
        if (TAuthor_ADDRESS() == null) {
            log.info("SKIPPING", "UDT test");
            return;
        }

        jOOQAbstractTest.reset = false;

        Result<A> authors = create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch();
        UDTRecord<?> a1 = authors.get(0).getValue(TAuthor_ADDRESS());
        UDTRecord<?> a2 = authors.get(1).getValue(TAuthor_ADDRESS());

        Object street1 = on(a1).call("getStreet").get();
        assertEquals("77", on(street1).call("getNo").get());
        assertEquals("Parliament Hill", on(street1).call("getStreet").get());
        assertTrue(Arrays.equals(new byte[] { 0x70, 0x70 }, on(street1).call("getF_1323").<byte[]>get()));
        assertEquals("NW31A9", on(a1).call("getZip").get());
        assertEquals("Hampstead", on(a1).call("getCity").get());
        assertEquals("England", "" + on(a1).call("getCountry").get());
        assertEquals(null, on(a1).call("getCode").get());
        assertTrue(Arrays.equals(new byte[] { 0x71, 0x71 }, on(a1).call("getF_1323").<byte[]>get()));

        /* [pro] */
        if (TArrays_NUMBER_R() != null) {
            assertEquals(Arrays.asList(1, 2, 3), invoke(invoke(street1, "getFloors"), "getList"));
        }
        /* [/pro] */
        if (TArrays_NUMBER() != null) {
            assertEquals(Arrays.asList(1, 2, 3), Arrays.asList((Object[]) invoke(street1, "getFloors")));
        }

        Object street2 = on(a2).call("getStreet").get();
        assertEquals("43.003", on(street2).call("getNo").get());
        assertEquals("Caixa Postal", on(street2).call("getStreet").get());
        assertEquals(null, on(street2).call("getF_1323").<byte[]>get());
        assertEquals(null, on(a2).call("getZip").get());
        assertEquals("Rio de Janeiro", on(a2).call("getCity").get());
        assertEquals("Brazil", "" + on(a2).call("getCountry").get());
        assertEquals(2, (int) on(a2).call("getCode").get());
        assertEquals(null, on(a2).call("getF_1323").<byte[]>get());

        /* [pro] */
        if (TArrays_NUMBER_R() != null) {
            assertEquals(null, invoke(street2, "getFloors"));
        }
        /* [/pro] */
        if (TArrays_NUMBER() != null) {
            assertEquals(null, invoke(street2, "getFloors"));
        }
    }

    public void testUDTProcedure() throws Exception {
        if (cUAddressType() == null) {
            log.info("SKIPPING", "UDT procedure test (no UDT support)");
            return;
        }

        if (cRoutines() == null) {
            log.info("SKIPPING", "UDT procedure test (no procedure support)");
            return;
        }

        if (dialect() == SQLDialect.POSTGRES) {
            log.info("SKIPPING", "UDT procedure test (Postgres JDBC driver flaw)");
            return;
        }

        jOOQAbstractTest.reset = false;

        UDTRecord<?> address = cUAddressType().newInstance();
        UDTRecord<?> street = cUStreetType().newInstance();
        invoke(street, "setNo", "35");
        invoke(address, "setStreet", street);

        // First procedure
        Object result = invoke(cRoutines(), "pEnhanceAddress1", create().configuration(), address);
        assertEquals("35", result);

        // Second procedure
        address = invoke(cRoutines(), "pEnhanceAddress2", create().configuration());
        street = invoke(address, "getStreet");
        assertEquals("Parliament Hill", invoke(street, "getStreet"));
        assertEquals("77", invoke(street, "getNo"));

        /* [pro] */
        if (TArrays_NUMBER_R() != null) {
            assertEquals(Arrays.asList(1, 2, 3), invoke(invoke(street, "getFloors"), "getList"));
        }
        /* [/pro] */
        if (TArrays_NUMBER() != null) {
            assertEquals(Arrays.asList(1, 2, 3), Arrays.asList((Object[]) invoke(street, "getFloors")));
        }

        // Third procedure
        address = (UDTRecord<?>) invoke(cRoutines(), "pEnhanceAddress3", create().configuration(), address);
        street = (UDTRecord<?>) invoke(address, "getStreet");
        assertEquals("Zwinglistrasse", invoke(street, "getStreet"));
        assertEquals("17", invoke(street, "getNo"));
    }

    public void testArrayTables() throws Exception {
        /* [pro] */
        if (TArrays_NUMBER_R() != null) {
            Result<?> result;

            // [#1184] Test data type
            assertTrue(TArrays_NUMBER_R().getDataType().isArray());
            assertFalse(TBook_ID().getDataType().isArray());

            // An empty array
            // --------------
            ArrayRecord<Integer> array = newNUMBER_R();
            result = create().select().from(table(array)).fetch();

            assertEquals(0, result.size());
            assertEquals(1, result.fieldsRow().size());
            // [#523] TODO use ArrayRecord meta data instead
//            assertEquals(array.getDataType(), result.getField(0).getDataType());

            // An array containing null
            // ------------------------
            array.clear();
            array.add((Integer) null);
            result = create().select().from(table(array)).fetch();

            assertEquals(1, result.size());
            assertEquals(1, result.fieldsRow().size());
//            assertEquals(array.getDataType(), result.getField(0).getDataType());
            assertEquals(null, result.getValue(0, 0));

            // An array containing two values
            // ------------------------------
            array.clear();
            array.addAll(asList((Integer) null, 1));
            result = create().select().from(table(array)).fetch();

            assertEquals(2, result.size());
            assertEquals(1, result.fieldsRow().size());
//            assertEquals(array.getDataType(), result.getField(0).getDataType());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));

            // An array containing three values
            // --------------------------------
            array.clear();
            array.addAll(asList((Integer) null, 1, 2));
            result = create().select().from(table(array)).fetch();

            assertEquals(3, result.size());
            assertEquals(1, result.fieldsRow().size());
//            assertEquals(array.getDataType(), result.getField(0).getDataType());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));
            assertEquals("2", "" + result.getValue(2, 0));

            // Joining an unnested array table
            // -------------------------------
            array.set(2, 3);
            Table<?> table = table(array);
            result = create()
                .select(TBook_ID(), TBook_TITLE())
                .from(TBook())
                .join(table)
                .on(table.field(0).cast(Integer.class).equal(TBook_ID()))
                .orderBy(TBook_ID())
                .fetch();

            assertEquals(2, result.size());
            assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
            assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
            assertEquals("Animal Farm", result.getValue(0, TBook_TITLE()));
            assertEquals("O Alquimista", result.getValue(1, TBook_TITLE()));

            // Joining an aliased unnested array table
            // ---------------------------------------
            result = create()
                .select(TBook_ID(), TBook_TITLE())
                .from(TBook())
                .join(table.as("t"))
                .on(table.as("t").field(0).cast(Integer.class).equal(TBook_ID()))
                .orderBy(TBook_ID())
                .fetch();

            assertEquals(2, result.size());
            assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
            assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
            assertEquals("Animal Farm", result.getValue(0, TBook_TITLE()));
            assertEquals("O Alquimista", result.getValue(1, TBook_TITLE()));

            // Functions returning arrays
            // --------------------------
            result = create().select().from(table(FArrays1Field_R(null))).fetch();
            assertEquals(0, result.size());
            assertEquals(1, result.fieldsRow().size());

            array = newNUMBER_R();
            result = create().select().from(table(FArrays1Field_R(val(array)))).fetch();
            assertEquals(0, result.size());
            assertEquals(1, result.fieldsRow().size());

            array.clear();
            array.addAll(asList(null, 1));
            result = create().select().from(table(FArrays1Field_R(val(array)))).fetch();
            assertEquals(2, result.size());
            assertEquals(1, result.fieldsRow().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));

            array.clear();
            array.addAll(asList(null, 1, null, 2));
            result = create().select().from(table(FArrays1Field_R(val(array)))).fetch();
            assertEquals(4, result.size());
            assertEquals(1, result.fieldsRow().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));
            assertEquals(null, result.getValue(2, 0));
            assertEquals("2", "" + result.getValue(3, 0));
        }
        else
        /* [/pro] */
        if (TArrays_NUMBER() != null) {
            Result<?> result;
            Table<?> table;
            Integer[] array;

            // [#1184] Test data type
            assertTrue(TArrays_NUMBER().getDataType().isArray());
            assertFalse(TBook_ID().getDataType().isArray());

            // Cross join the array table with the unnested string array value
            // ---------------------------------------------------------------

            switch (family()) {
                case POSTGRES:
                case H2:
                case HSQLDB:
                    // [#1085] TODO: Is this kind of thing supported in any database??
                    log.info("SKIPPING", "Cross join of table with unnested array is not supported");
                    break;

                default:
                    table = table(TArrays_STRING()).as("t");
                    result = create()
                        .select(TArrays_ID(), table.field(0))
                        .from(TArrays(), table)
                        .orderBy(TArrays_ID())
                        .fetch();

                    assertEquals(3, result.size());
                    assertEquals(Integer.valueOf(3), result.getValue(0, TArrays_ID()));
                    assertEquals(Integer.valueOf(4), result.getValue(1, TArrays_ID()));
                    assertEquals(Integer.valueOf(4), result.getValue(2, TArrays_ID()));

                    assertEquals("a", result.getValue(0, 1));
                    assertEquals("a", result.getValue(1, 1));
                    assertEquals("b", result.getValue(2, 1));
            }


            // Functions returning arrays
            // --------------------------
            result = create().select().from(table(FArrays1Field(null))).fetch();
            assertEquals(0, result.size());
            assertEquals(1, result.fieldsRow().size());

            array = new Integer[0];
            result = create().select().from(table(FArrays1Field(val(array)))).fetch();
            assertEquals(0, result.size());
            assertEquals(1, result.fieldsRow().size());

            array = new Integer[] { null, 1 };
            result = create().select().from(table(FArrays1Field(val(array)))).fetch();
            assertEquals(2, result.size());
            assertEquals(1, result.fieldsRow().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));

            array = new Integer[] { null, 1, null, 2 };
            result = create().select().from(table(FArrays1Field(val(array)))).fetch();
            assertEquals(4, result.size());
            assertEquals(1, result.fieldsRow().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));
            assertEquals(null, result.getValue(2, 0));
            assertEquals("2", "" + result.getValue(3, 0));
        }
        else {
            log.info("SKIPPING", "ARRAY TABLE tests");
        }
    }

    public void testArrayTableEmulation() throws Exception {
        Result<?> result;

        // An empty array
        // --------------
        Integer[] array = new Integer[0];
        result = create().select().from(table(new Integer[0])).fetch();

        assertEquals(0, result.size());
        assertEquals(1, result.fieldsRow().size());

        // An array containing null
        // ------------------------
        array = new Integer[] { null };
        result = create().select().from(table(array)).fetch();

        assertEquals(1, result.size());
        assertEquals(1, result.fieldsRow().size());
        assertEquals(null, result.getValue(0, 0));

        // An array containing two values (some DB's can't guarantee ordering)
        // -------------------------------------------------------------------
        array = new Integer[] { null, 1 };
        result = create().select().from(table(array)).fetch();

        assertEquals(2, result.size());
        assertEquals(1, result.fieldsRow().size());
        assertTrue(asList(array).containsAll(result.getValues(0)));

        // An array containing three values (some DB's can't guarantee ordering)
        // ---------------------------------------------------------------------
        array = new Integer[] { null, 1, 2 };
        result = create().select().from(table(array)).fetch();

        assertEquals(3, result.size());
        assertEquals(1, result.fieldsRow().size());
        assertTrue(asList(array).containsAll(result.getValues(0)));

        // Joining an unnested array table
        // -------------------------------
        array = new Integer[] { 2, 3 };
        Table<?> table = table(array);
        result = create()
            .select(TBook_ID(), TBook_TITLE())
            .from(TBook())
            .join(table)
            .on(table.field(0).cast(Integer.class).equal(TBook_ID()))
            .orderBy(TBook_ID().asc())
            .fetch();

        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
        assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
        assertEquals("Animal Farm", result.getValue(0, TBook_TITLE()));
        assertEquals("O Alquimista", result.getValue(1, TBook_TITLE()));

        // Joining an aliased unnested array table
        // ---------------------------------------
        result = create()
            .select(TBook_ID(), TBook_TITLE())
            .from(TBook())
            .join(table.as("t"))
            .on(table.as("t").field(0).cast(Integer.class).equal(TBook_ID()))
            .orderBy(TBook_ID().asc())
            .fetch();

        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
        assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
        assertEquals("Animal Farm", result.getValue(0, TBook_TITLE()));
        assertEquals("O Alquimista", result.getValue(1, TBook_TITLE()));
    }

    public void testStoredProceduresWithCursorParameters() throws Exception {
        switch (family()) {
            /* [pro] */
            case ORACLE:
            /* [/pro] */
            case H2:
            case HSQLDB:
            case POSTGRES:
                break;

            default:
                log.info("SKIPPING", "Stored procedures tests with CURSOR type parameters");
                return;
        }

        // [#706] [#2324] Postgres JDBC needs two separate queries to fetch a
        // cursor from a result set. This is only possible in a single
        // transaction. This was previously fixed by jOOQ, but jOOQ's the wrong
        // place to fix such things.
        boolean autoCommit = getConnection().getAutoCommit();
        try {
            getConnection().setAutoCommit(false);

            // ---------------------------------------------------------------------
            // The one cursor function
            // ---------------------------------------------------------------------
            {
                Object integerArray = null;

                // Get an empty cursor
                // -------------------
                Result<Record> bFromCursor = invoke(cRoutines(), "fGetOneCursor", create().configuration(), integerArray);

                assertNotNull(bFromCursor);
                assertTrue(bFromCursor.isEmpty());
                assertEquals(0, bFromCursor.size());

                // Get a filled cursor
                // -------------------
                /* [pro] */
                if (TArrays_STRING_R() != null) {
                    ArrayRecord<Integer> i = newNUMBER_R();
                    i.addAll(asList(1, 2, 4, 6));
                    integerArray = i;
                }
                else
                /* [/pro] */
                if (TArrays_STRING() != null) {
                    integerArray = new Integer[] { 1, 2, 4, 6 };
                }

                bFromCursor = invoke(cRoutines(), "fGetOneCursor", create().configuration(), integerArray);

                Result<B> bFromTable = create()
                    .selectFrom(TBook())
                    .where(TBook_ID().in(1, 2, 4))
                    .orderBy(TBook_ID()).fetch();

                assertNotNull(bFromCursor);
                assertFalse(bFromCursor.isEmpty());
                assertEquals(3, bFromCursor.size());

                compareBookResults(bFromCursor, bFromTable);
            }

            // ---------------------------------------------------------------------
            // The one cursor function used in SQL
            // ---------------------------------------------------------------------
            {

                // Get an empty cursor
                // -------------------
                Field<Result<Record>> field = FGetOneCursorField(null);
                Result<Record> bFromCursor;

                switch (dialect()) {
                    case HSQLDB:
                        bFromCursor = create().select().from(table(field)).fetch();
                        break;

                    default:
                        bFromCursor = create().select(field).fetchOne(field);
                        break;
                }

                assertNotNull(bFromCursor);
                assertTrue(bFromCursor.isEmpty());
                assertEquals(0, bFromCursor.size());

                // Get a filled cursor
                // -------------------
                field = FGetOneCursorField(new Integer[] { 1, 2, 4, 6 });

                switch (dialect()) {
                    case HSQLDB:
                        bFromCursor = create().select().from(table(field)).fetch();
                        break;

                    default:
                        bFromCursor = create().select(field).fetchOne(field);
                        break;
                }

                Result<B> bFromTable = create()
                    .selectFrom(TBook())
                    .where(TBook_ID().in(1, 2, 4))
                    .orderBy(TBook_ID()).fetch();

                assertNotNull(bFromCursor);
                assertFalse(bFromCursor.isEmpty());
                assertEquals(3, bFromCursor.size());

                compareBookResults(bFromCursor, bFromTable);
            }

            if (dialect() == SQLDialect.HSQLDB) {
                log.info("SKIPPING", "Cursor OUT parameter tests");
                return;
            }

            // ---------------------------------------------------------------------
            // The one cursor procedure
            // ---------------------------------------------------------------------
            if (supportsOUTParameters()) {
                Object integerArray = null;

                // Get an empty cursor
                // -------------------
                Object result = invoke(cRoutines(), "pGetOneCursor", create().configuration(), integerArray);

                assertNotNull(result);
                assertEquals("0", "" + invoke(result, "getTotal"));

                Result<Record> bFromCursor = invoke(result, "getBooks");
                assertTrue(bFromCursor.isEmpty());
                assertEquals(0, bFromCursor.size());

                // Get a filled cursor
                // -------------------
                /* [pro] */
                if (TArrays_STRING_R() != null) {
                    ArrayRecord<Integer> i = newNUMBER_R();
                    i.addAll(asList(1, 2, 4, 6));
                    integerArray = i;
                }
                else
                /* [/pro] */
                if (TArrays_STRING() != null) {
                    integerArray = new Integer[] { 1, 2, 4, 6 };
                }

                result = invoke(cRoutines(), "pGetOneCursor", create().configuration(), integerArray);

                assertEquals("3", "" + invoke(result, "getTotal"));
                bFromCursor = invoke(result, "getBooks");

                Result<B> bFromTable = create()
                    .selectFrom(TBook())
                    .where(TBook_ID().in(1, 2, 4))
                    .orderBy(TBook_ID()).fetch();

                assertNotNull(bFromCursor);
                assertFalse(bFromCursor.isEmpty());
                assertEquals(3, bFromCursor.size());

                compareBookResults(bFromCursor, bFromTable);
            }
            else {
                log.info("SKIPPING", "One cursor OUT parameter test");
            }

            // ---------------------------------------------------------------------
            // The two cursor procedure
            // ---------------------------------------------------------------------
            if (dialect() == SQLDialect.POSTGRES) {

                // TODO [#707] This fails for Postgres, as UDT's are not correctly
                // deserialised
                log.info("SKIPPING", "UDT/Enum types returned in refcursor (see [#707])");
            }
            else if (supportsOUTParameters()) {
                Object result = invoke(cRoutines(), "pGetTwoCursors", create().configuration());
                assertNotNull(result);

                Result<A> aFromTable = create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch();
                Result<B> bFromTable = create().selectFrom(TBook()).orderBy(TBook_ID()).fetch();

                Result<Record> aFromCursor = invoke(result, "getAuthors");
                Result<Record> bFromCursor = invoke(result, "getBooks");
                assertNotNull(aFromCursor);
                assertNotNull(bFromCursor);
                assertEquals(2, aFromCursor.size());
                assertEquals(4, bFromCursor.size());
                assertFalse(aFromCursor.isEmpty());
                assertFalse(bFromCursor.isEmpty());

                compareAuthorResults(aFromCursor, aFromTable);
                compareBookResults(bFromCursor, bFromTable);
            }
            else {
                log.info("SKIPPING", "Two cursor OUT parameter test");
            }
        }
        finally {
            getConnection().setAutoCommit(autoCommit);
        }
    }

    private void compareAuthorResults(Result<Record> aFromCursor, Result<A> aFromTable) {
        assertEquals(aFromTable.getValues(TAuthor_ID()), aFromCursor.getValues(TAuthor_ID()));
        assertEquals(aFromTable.getValues(TAuthor_ID().getName()), aFromCursor.getValues(TAuthor_ID().getName()));
        assertEquals(aFromTable.getValues(0), aFromCursor.getValues(0));

        assertEquals(aFromTable.getValues(TAuthor_FIRST_NAME()), aFromCursor.getValues(TAuthor_FIRST_NAME()));
        assertEquals(aFromTable.getValues(TAuthor_LAST_NAME()), aFromCursor.getValues(TAuthor_LAST_NAME()));
        assertEquals(aFromTable.getValues(TAuthor_YEAR_OF_BIRTH()), aFromCursor.getValues(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(aFromTable.getValues(TAuthor_DATE_OF_BIRTH()), aFromCursor.getValues(TAuthor_DATE_OF_BIRTH()));
        assertEquals(aFromTable.getValues(TAuthor_ADDRESS()), aFromCursor.getValues(TAuthor_ADDRESS()));
    }

    private void compareBookResults(Result<Record> bFromCursor, Result<B> bFromTable) {
        assertEquals(bFromTable.getValues(TBook_ID()), bFromCursor.getValues(TBook_ID()));
        assertEquals(bFromTable.getValues(TBook_ID().getName()), bFromCursor.getValues(TBook_ID().getName()));
        assertEquals(bFromTable.getValues(0), bFromCursor.getValues(0));

        assertEquals(bFromTable.getValues(TBook_AUTHOR_ID()), bFromCursor.getValues(TBook_AUTHOR_ID()));
        assertEquals(bFromTable.getValues(TBook_CONTENT_PDF()), bFromCursor.getValues(TBook_CONTENT_PDF()));
        assertEquals(bFromTable.getValues(TBook_CONTENT_TEXT()), bFromCursor.getValues(TBook_CONTENT_TEXT()));
        assertEquals(bFromTable.getValues(TBook_TITLE()), bFromCursor.getValues(TBook_TITLE()));
    }
}
