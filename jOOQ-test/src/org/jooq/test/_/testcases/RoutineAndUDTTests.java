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

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.jooq.impl.Factory.table;
import static org.jooq.impl.Factory.val;

import java.sql.Date;
import java.util.Arrays;

import org.jooq.ArrayRecord;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class RoutineAndUDTTests<
    A    extends UpdatableRecord<A>,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, B, S, B2S, BS, L, X, D, T, U, I, IPK, T658, T725, T639, T785> {

    public RoutineAndUDTTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }
    @Test
    public void testPackage() throws Exception {
        if (cLibrary() == null) {
            log.info("SKIPPING", "packages test");
            return;
        }

        jOOQAbstractTest.reset = false;

        assertEquals("1", "" + invoke(cLibrary(), "pkgPAuthorExists1", create(), "Paulo"));
        assertEquals("0", "" + invoke(cLibrary(), "pkgPAuthorExists1", create(), "Shakespeare"));
        assertEquals("1", "" + invoke(cLibrary(), "pkgFAuthorExists1", create(), "Paulo"));
        assertEquals("0", "" + invoke(cLibrary(), "pkgFAuthorExists1", create(), "Shakespeare"));
    }

    @Test
    public void testStoredProcedure() throws Exception {
        if (cRoutines() == null) {
            log.info("SKIPPING", "procedure test");
            return;
        }

        jOOQAbstractTest.reset = false;

        // P_AUTHOR_EXISTS
        // ---------------------------------------------------------------------
        if (supportsOUTParameters()) {
            assertEquals("0", "" + invoke(cRoutines(), "pAuthorExists", create(), null, DUMMY_OUT_INT));
            assertEquals("1", "" + invoke(cRoutines(), "pAuthorExists", create(), "Paulo", DUMMY_OUT_INT));
            assertEquals("0", "" + invoke(cRoutines(), "pAuthorExists", create(), "Shakespeare", DUMMY_OUT_INT));
        } else {
            log.info("SKIPPING", "procedure test for OUT parameters");
        }

        // P_CREATE_AUTHOR_*
        // ---------------------------------------------------------------------
        assertEquals(null, create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("William")));
        invoke(cRoutines(), "pCreateAuthor", create());
        assertEquals("Shakespeare", create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("William")).getValue(TAuthor_LAST_NAME()));

        assertEquals(null, create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("Hermann")));
        invoke(cRoutines(), "pCreateAuthorByName", create(), "Hermann", "Hesse");
        assertEquals("Hesse", create().fetchOne(
            TAuthor(),
            TAuthor_FIRST_NAME().equal("Hermann")).getValue(TAuthor_LAST_NAME()));

        assertEquals(null, create().fetchOne(
            TAuthor(),
            TAuthor_LAST_NAME().equal("Kaestner")));
        invoke(cRoutines(), "pCreateAuthorByName", create(), null, "Kaestner");
        assertEquals("Kaestner", create().fetchOne(
            TAuthor(),
            TAuthor_LAST_NAME().equal("Kaestner")).getValue(TAuthor_LAST_NAME()));

        // P391, a test for properly binding and treating various IN, OUT, INOUT
        // parameters
        // ---------------------------------------------------------------------
        if (supportsOUTParameters()) {

            // TODO: [#396] MySQL seems to have a bug when passing null to IN/OUT
            // parameters. Check back on this, when this is fixed.
            if (getDialect() != SQLDialect.MYSQL) {
                Object p391a = invoke(cRoutines(), "p391", create(), null, null, DUMMY_OUT_INT, DUMMY_OUT_INT, null, null);
                assertEquals(null, invoke(p391a, "getIo1"));
                assertEquals(null, invoke(p391a, "getO1"));
                assertEquals(null, invoke(p391a, "getIo2"));
                assertEquals(null, invoke(p391a, "getO2"));
            }

            // TODO: [#459] Sybase messes up IN/OUT parameter orders.
            // Check back on this, when this is fixed.
            if (getDialect() != SQLDialect.SYBASE) {
                Object p391b = invoke(cRoutines(), "p391", create(), null, 2, DUMMY_OUT_INT, DUMMY_OUT_INT, 3, null);
                assertEquals(null, invoke(p391b, "getIo1"));
                assertEquals("2", "" + invoke(p391b, "getO1"));
                assertEquals(null, invoke(p391b, "getIo2"));
                assertEquals("3", "" + invoke(p391b, "getO2"));

                Object p391c = invoke(cRoutines(), "p391", create(), 1, 2, DUMMY_OUT_INT, DUMMY_OUT_INT, 3, 4);
                assertEquals("1", "" + invoke(p391c, "getIo1"));
                assertEquals("2", "" + invoke(p391c, "getO1"));
                assertEquals("4", "" + invoke(p391c, "getIo2"));
                assertEquals("3", "" + invoke(p391c, "getO2"));
            }
        }

        // F378, which is a stored function with OUT parameters
        // ---------------------------------------------------------------------
        switch (getDialect()) {

            // Currently, this is only supported for oracle
            case ORACLE:
                Object result1a = invoke(cRoutines(), "f378", create(), null, null);
                assertEquals(null, invoke(result1a, "getIo"));
                assertEquals(null, invoke(result1a, "getO"));
                assertEquals(null, invoke(result1a, "getReturnValue"));

                Object result2a = invoke(cRoutines(), "f378", create(), null, 2);
                assertEquals(null, invoke(result2a, "getIo"));
                assertEquals("2", "" + invoke(result2a, "getO"));
                assertEquals(null, invoke(result2a, "getReturnValue"));

                Object result3a = invoke(cRoutines(), "f378", create(), 1, 2);
                assertEquals("1", "" + invoke(result3a, "getIo"));
                assertEquals("2", "" + invoke(result3a, "getO"));
                assertEquals("3", "" + invoke(result3a, "getReturnValue"));

                Object result1b = invoke(cLibrary(), "pkgF378", create(), null, null);
                assertEquals(null, invoke(result1b, "getIo"));
                assertEquals(null, invoke(result1b, "getO"));
                assertEquals(null, invoke(result1b, "getReturnValue"));

                Object result2b = invoke(cLibrary(), "pkgF378", create(), null, 2);
                assertEquals(null, invoke(result2b, "getIo"));
                assertEquals("2", "" + invoke(result2b, "getO"));
                assertEquals(null, invoke(result2b, "getReturnValue"));

                Object result3b = invoke(cLibrary(), "pkgF378", create(), 1, 2);
                assertEquals("1", "" + invoke(result3b, "getIo"));
                assertEquals("2", "" + invoke(result3b, "getO"));
                assertEquals("3", "" + invoke(result3b, "getReturnValue"));
                break;
        }
    }

    @Test
    public void testStoredFunctions() throws Exception {
        if (cRoutines() == null) {
            log.info("SKIPPING", "functions test");
            return;
        }

        jOOQAbstractTest.reset = false;

        // ---------------------------------------------------------------------
        // Standalone calls
        // ---------------------------------------------------------------------
        assertEquals("0", "" + invoke(cRoutines(), "fAuthorExists", create(), null));
        assertEquals("1", "" + invoke(cRoutines(), "fAuthorExists", create(), "Paulo"));
        assertEquals("0", "" + invoke(cRoutines(), "fAuthorExists", create(), "Shakespeare"));
        assertEquals("1", "" + invoke(cRoutines(), "fOne", create()));
        assertEquals("1", "" + invoke(cRoutines(), "fNumber", create(), 1));
        assertEquals(null, invoke(cRoutines(), "fNumber", create(), null));
        assertEquals("1204", "" + invoke(cRoutines(), "f317", create(), 1, 2, 3, 4));
        assertEquals("1204", "" + invoke(cRoutines(), "f317", create(), 1, 2, null, 4));
        assertEquals("4301", "" + invoke(cRoutines(), "f317", create(), 4, 3, 2, 1));
        assertEquals("4301", "" + invoke(cRoutines(), "f317", create(), 4, 3, null, 1));
        assertEquals("1101", "" + invoke(cRoutines(), "f317", create(), 1, 1, 1, 1));
        assertEquals("1101", "" + invoke(cRoutines(), "f317", create(), 1, 1, null, 1));

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

        SelectQuery q = create().selectQuery();
        q.addSelect(
            f1a, f2a, f3a, f4a, f5a, f6a, f7a, f8a, f9a,
            f1b, f2b, f3b, f4b, f5b, f6b, f7b, f8b, f9b, f10);
        q.execute();
        Result<Record> result = q.getResult();

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getValueAsString(f1a));
        assertEquals("0", result.get(0).getValueAsString(f2a));
        assertEquals("1", result.get(0).getValueAsString(f3a));
        assertEquals("42", result.get(0).getValueAsString(f4a));
        assertEquals("1", result.get(0).getValueAsString(f5a));
        assertEquals("1204", result.get(0).getValueAsString(f6a));
        assertEquals("4301", result.get(0).getValueAsString(f7a));
        assertEquals("1101", result.get(0).getValueAsString(f8a));
        assertEquals("1204", result.get(0).getValueAsString(f9a));

        assertEquals("1", result.get(0).getValueAsString(f1b));
        assertEquals("0", result.get(0).getValueAsString(f2b));
        assertEquals("1", result.get(0).getValueAsString(f3b));
        assertEquals("42", result.get(0).getValueAsString(f4b));
        assertEquals("1", result.get(0).getValueAsString(f5b));
        assertEquals("1204", result.get(0).getValueAsString(f6b));
        assertEquals("4301", result.get(0).getValueAsString(f7b));
        assertEquals("1101", result.get(0).getValueAsString(f8b));
        assertEquals("1204", result.get(0).getValueAsString(f9b));

        assertEquals("0", result.get(0).getValueAsString(f10));

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
        result = create().select(
                FNumberField(1).cast(Integer.class),
                FNumberField(TAuthor_ID()).cast(Integer.class),
                FNumberField(FNumberField(TAuthor_ID())).cast(Integer.class))
            .from(TAuthor())
            .orderBy(TAuthor_ID())
            .fetch();

        assertEquals(Integer.valueOf(1), result.getValue(0, 0));
        assertEquals(Integer.valueOf(1), result.getValue(0, 1));
        assertEquals(Integer.valueOf(1), result.getValue(0, 2));
        assertEquals(Integer.valueOf(1), result.getValue(1, 0));
        assertEquals(Integer.valueOf(2), result.getValue(1, 1));
        assertEquals(Integer.valueOf(2), result.getValue(1, 2));
    }


    @Test
    public void testARRAYType() throws Exception {
        if (TArrays() == null) {
            log.info("SKIPPING", "ARRAY type test");
            return;
        }

        jOOQAbstractTest.reset = false;

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

            assertEquals(Arrays.asList(), Arrays.asList(arrays.getValueAsArray(1, TArrays_STRING_R())));
            assertEquals(Arrays.asList(), Arrays.asList(arrays.getValueAsArray(1, TArrays_NUMBER_R())));
            assertEquals(Arrays.asList(), Arrays.asList(arrays.getValueAsArray(1, TArrays_DATE_R())));

            assertEquals(Arrays.asList("a"), Arrays.asList(arrays.getValueAsArray(2, TArrays_STRING_R())));
            assertEquals(Arrays.asList(1), Arrays.asList(arrays.getValueAsArray(2, TArrays_NUMBER_R())));
            assertEquals("[1981-07-10]", Arrays.asList(arrays.getValueAsArray(2, TArrays_DATE_R())).toString());

            assertEquals(Arrays.asList("a", "b"), Arrays.asList(arrays.getValueAsArray(3, TArrays_STRING_R())));
            assertEquals(Arrays.asList(1, 2), Arrays.asList(arrays.getValueAsArray(3, TArrays_NUMBER_R())));
            assertEquals("[1981-07-10, 2000-01-01]", Arrays.asList(arrays.getValueAsArray(3, TArrays_DATE_R())).toString());



            InsertQuery<?> insert = create().insertQuery(TArrays());
            insert.addValue(TArrays_ID(), 5);
            insert.addValueAsArray(TArrays_NUMBER_R(), 1, 2, 3);
            insert.addValueAsArray(TArrays_STRING_R(), "a", "b", "c");
            insert.addValueAsArray(TArrays_DATE_R(), new Date(0), new Date(84600 * 1000), new Date(84600 * 2000));
            insert.execute();

            Record array = create().select(
                TArrays_STRING_R(),
                TArrays_NUMBER_R(),
                TArrays_DATE_R())
            .from(TArrays())
            .where(TArrays_ID().equal(5))
            .fetchOne();

            assertEquals(Arrays.asList("a", "b", "c"), Arrays.asList(array.getValueAsArray(TArrays_STRING_R())));
            assertEquals(Arrays.asList(1, 2, 3), Arrays.asList(array.getValueAsArray(TArrays_NUMBER_R())));
            assertEquals("[1970-01-01, 1970-01-02, 1970-01-03]", Arrays.asList(array.getValueAsArray(TArrays_DATE_R())).toString());



            UpdateQuery<X> update = create().updateQuery(TArrays());
            update.addValueAsArray(TArrays_NUMBER_R(), 3, 2, 1);
            update.addValueAsArray(TArrays_STRING_R(), "c", "b", "a");
            update.addValueAsArray(TArrays_DATE_R(), new Date(84600 * 2000), new Date(84600 * 1000), new Date(0));
            update.addConditions(TArrays_ID().equal(5));
            update.execute();

            array = create().select(
                TArrays_STRING_R(),
                TArrays_NUMBER_R(),
                TArrays_DATE_R())
            .from(TArrays())
            .where(TArrays_ID().equal(5))
            .fetchOne();

            assertEquals(Arrays.asList("c", "b", "a"), Arrays.asList(array.getValueAsArray(TArrays_STRING_R())));
            assertEquals(Arrays.asList(3, 2, 1), Arrays.asList(array.getValueAsArray(TArrays_NUMBER_R())));
            assertEquals("[1970-01-03, 1970-01-02, 1970-01-01]", Arrays.asList(array.getValueAsArray(TArrays_DATE_R())).toString());
        }

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
            assertEquals(null, arrays.getValue(0, TArrays_STRING()));
            assertEquals(null, arrays.getValue(0, TArrays_NUMBER()));
            assertEquals(null, arrays.getValue(0, TArrays_DATE()));

            if (TArrays_UDT() != null) {
                assertEquals(null, arrays.getValue(0, TArrays_UDT()));
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
            insert.addValue(TArrays_STRING(), new String[] { "a", "b", "c" });
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
            assertEquals(3, s.length);
            assertEquals(3, n.length);
            assertEquals(3, d.length);
            assertEquals(TArrays_STRING().getType(), s.getClass());
            assertEquals(TArrays_NUMBER().getType(), n.getClass());
            assertEquals(TArrays_DATE().getType(), d.getClass());
            assertEquals("a", s[0].toString());
            assertEquals("b", s[1].toString());
            assertEquals("c", s[2].toString());
            assertEquals("1", n[0].toString());
            assertEquals("2", n[1].toString());
            assertEquals("3", n[2].toString());
            assertEquals("1970-01-01", d[0].toString());
            assertEquals("1970-01-02", d[1].toString());
            assertEquals("1970-01-03", d[2].toString());



            UpdateQuery<X> update = create().updateQuery(TArrays());
            update.addValue(TArrays_NUMBER(), new Integer[] { 3, 2, 1});
            update.addValue(TArrays_STRING(), new String[] { "c", "b", "a" });
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
            assertEquals(3, s.length);
            assertEquals(3, n.length);
            assertEquals(3, d.length);
            assertEquals(TArrays_STRING().getType(), s.getClass());
            assertEquals(TArrays_NUMBER().getType(), n.getClass());
            assertEquals(TArrays_DATE().getType(), d.getClass());
            assertEquals("c", s[0].toString());
            assertEquals("b", s[1].toString());
            assertEquals("a", s[2].toString());
            assertEquals("3", n[0].toString());
            assertEquals("2", n[1].toString());
            assertEquals("1", n[2].toString());
            assertEquals("1970-01-03", d[0].toString());
            assertEquals("1970-01-02", d[1].toString());
            assertEquals("1970-01-01", d[2].toString());
        }
    }

    @Test
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

        if (TArrays_STRING_R() != null) {
            ArrayRecord<Integer> i;
            ArrayRecord<Long> l;
            ArrayRecord<String> s;

            assertEquals(null, invoke(cRoutines(), "pArrays1", create(), null));
            assertEquals(null, invoke(cRoutines(), "pArrays2", create(), null));
            assertEquals(null, invoke(cRoutines(), "pArrays3", create(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays1", create(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays2", create(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays3", create(), null));

            i = newNUMBER_R();
            l = newNUMBER_LONG_R();
            s = newSTRING_R();

            assertEquals(
                Arrays.asList(new Integer[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList(new Long[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList(new String[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays3", create(), s)).get()));
            assertEquals(
                Arrays.asList(new Integer[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList(new Long[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList(new String[0]),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays3", create(), s)).get()));

            i = newNUMBER_R();
            l = newNUMBER_LONG_R();
            s = newSTRING_R();

            i.set((Integer) null);
            l.set((Long) null);
            s.set((String) null);

            assertEquals(
                Arrays.asList((Integer) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList((Long) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList((String) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays3", create(), s)).get()));
            assertEquals(
                Arrays.asList((Integer) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList((Long) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList((String) null),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays3", create(), s)).get()));

            i = newNUMBER_R();
            l = newNUMBER_LONG_R();
            s = newSTRING_R();

            i.set(1, 2);
            l.set(1L, 2L);
            s.set("1", "2");

            assertEquals(
                Arrays.asList(1, 2),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList(1L, 2L),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList("1", "2"),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "pArrays3", create(), s)).get()));
            assertEquals(
                Arrays.asList(1, 2),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays1", create(), i)).get()));
            assertEquals(
                Arrays.asList(1L, 2L),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays2", create(), l)).get()));
            assertEquals(
                Arrays.asList("1", "2"),
                Arrays.asList(((ArrayRecord<?>) invoke(cRoutines(), "fArrays3", create(), s)).get()));
        }

        if (TArrays_STRING() != null) {
            if (supportsOUTParameters()) {
                assertEquals(null, invoke(cRoutines(), "pArrays1", create(), null));
                assertEquals(null, invoke(cRoutines(), "pArrays2", create(), null));
                assertEquals(null, invoke(cRoutines(), "pArrays3", create(), null));
            }

            assertEquals(null, invoke(cRoutines(), "fArrays1", create(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays2", create(), null));
            assertEquals(null, invoke(cRoutines(), "fArrays3", create(), null));

            if (supportsOUTParameters()) {
                assertEquals(
                    Arrays.asList(new Integer[0]),
                    Arrays.asList((Integer[]) invoke(cRoutines(), "pArrays1", create(), new Integer[0])));
                assertEquals(
                    Arrays.asList(new Long[0]),
                    Arrays.asList((Long[]) invoke(cRoutines(), "pArrays2", create(), new Long[0])));
                assertEquals(
                    Arrays.asList(new String[0]),
                    Arrays.asList((String[]) invoke(cRoutines(), "pArrays3", create(), new String[0])));
            }

            assertEquals(
                Arrays.asList(new Integer[0]),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays1", create(), new Integer[0])));
            assertEquals(
                Arrays.asList(new Long[0]),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays2", create(), new Long[0])));
            assertEquals(
                Arrays.asList(new String[0]),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays3", create(), new String[0])));

            if (supportsOUTParameters()) {
                assertEquals(
                    Arrays.asList((Integer) null),
                    Arrays.asList((Integer[]) invoke(cRoutines(), "pArrays1", create(), new Integer[] { null })));
                assertEquals(
                    Arrays.asList((Long) null),
                    Arrays.asList((Long[]) invoke(cRoutines(), "pArrays2", create(), new Long[] { null })));
                assertEquals(
                    Arrays.asList((String) null),
                    Arrays.asList((String[]) invoke(cRoutines(), "pArrays3", create(), new String[] { null })));
            }

            assertEquals(
                Arrays.asList((Integer) null),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays1", create(), new Integer[] { null })));
            assertEquals(
                Arrays.asList((Long) null),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays2", create(), new Long[] { null })));
            assertEquals(
                Arrays.asList((String) null),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays3", create(), new String[] { null })));

            if (supportsOUTParameters()) {
                assertEquals(
                    Arrays.asList(1, 2),
                    Arrays.asList((Integer[]) invoke(cRoutines(), "pArrays1", create(), new Integer[] {1, 2})));
                assertEquals(
                    Arrays.asList(1L, 2L),
                    Arrays.asList((Long[]) invoke(cRoutines(), "pArrays2", create(), new Long[] {1L, 2L})));
                assertEquals(
                    Arrays.asList("1", "2"),
                    Arrays.asList((String[]) invoke(cRoutines(), "pArrays3", create(), new String[] {"1", "2"})));
            }

            assertEquals(
                Arrays.asList(1, 2),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays1", create(), new Integer[] {1, 2})));
            assertEquals(
                Arrays.asList(1L, 2L),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays2", create(), new Long[] {1L, 2L})));
            assertEquals(
                Arrays.asList("1", "2"),
                Arrays.asList((Object[]) invoke(cRoutines(), "fArrays3", create(), new String[] {"1", "2"})));
        }
    }

    private ArrayRecord<Integer> newNUMBER_R() throws Exception {
        ArrayRecord<Integer> result = TArrays_NUMBER_R().getType().getConstructor(Configuration.class).newInstance(create());
        return result;
    }

    private ArrayRecord<Long> newNUMBER_LONG_R() throws Exception {
        ArrayRecord<Long> result = TArrays_NUMBER_LONG_R().getType().getConstructor(Configuration.class).newInstance(create());
        return result;
    }

    private ArrayRecord<String> newSTRING_R() throws Exception {
        ArrayRecord<String> result = TArrays_STRING_R().getType().getConstructor(Configuration.class).newInstance(create());
        return result;
    }

    @Test
    public void testUDTs() throws Exception {
        if (TAuthor_ADDRESS() == null) {
            log.info("SKIPPING", "UDT test");
            return;
        }

        jOOQAbstractTest.reset = false;

        Result<A> authors = create().selectFrom(TAuthor()).fetch();
        UDTRecord<?> a1 = authors.get(0).getValue(TAuthor_ADDRESS());
        UDTRecord<?> a2 = authors.get(1).getValue(TAuthor_ADDRESS());

        Object street1 = a1.getClass().getMethod("getStreet").invoke(a1);
        assertEquals("77", street1.getClass().getMethod("getNo").invoke(street1));
        assertEquals("Parliament Hill", street1.getClass().getMethod("getStreet").invoke(street1));
        assertEquals("NW31A9", a1.getClass().getMethod("getZip").invoke(a1));
        assertEquals("Hampstead", a1.getClass().getMethod("getCity").invoke(a1));
        assertEquals("England", "" + a1.getClass().getMethod("getCountry").invoke(a1));
        assertEquals(null, a1.getClass().getMethod("getCode").invoke(a1));

        if (TArrays_NUMBER_R() != null) {
            assertEquals(Arrays.asList(1, 2, 3), invoke(invoke(street1, "getFloors"), "getList"));
        }
        if (TArrays_NUMBER() != null) {
            assertEquals(Arrays.asList(1, 2, 3), Arrays.asList((Object[]) invoke(street1, "getFloors")));
        }

        Object street2 = a2.getClass().getMethod("getStreet").invoke(a2);
        assertEquals("43.003", street1.getClass().getMethod("getNo").invoke(street2));
        assertEquals("Caixa Postal", street1.getClass().getMethod("getStreet").invoke(street2));
        assertEquals(null, a2.getClass().getMethod("getZip").invoke(a2));
        assertEquals("Rio de Janeiro", a2.getClass().getMethod("getCity").invoke(a2));
        assertEquals("Brazil", "" + a1.getClass().getMethod("getCountry").invoke(a2));
        assertEquals(2, a1.getClass().getMethod("getCode").invoke(a2));

        if (TArrays_NUMBER_R() != null) {
            assertEquals(null, invoke(street2, "getFloors"));
        }
        if (TArrays_NUMBER() != null) {
            assertEquals(null, invoke(street2, "getFloors"));
        }
    }

    @Test
    public void testUDTProcedure() throws Exception {
        if (cUAddressType() == null) {
            log.info("SKIPPING", "UDT procedure test (no UDT support)");
            return;
        }

        if (cRoutines() == null) {
            log.info("SKIPPING", "UDT procedure test (no procedure support)");
            return;
        }

        if (getDialect() == SQLDialect.POSTGRES) {
            log.info("SKIPPING", "UDT procedure test (Postgres JDBC driver flaw)");
            return;
        }

        jOOQAbstractTest.reset = false;

        UDTRecord<?> address = cUAddressType().newInstance();
        UDTRecord<?> street = cUStreetType().newInstance();
        invoke(street, "setNo", "35");
        invoke(address, "setStreet", street);

        // First procedure
        Object result = invoke(cRoutines(), "pEnhanceAddress1", create(), address);
        assertEquals("35", result);

        // Second procedure
        address = invoke(cRoutines(), "pEnhanceAddress2", create());
        street = invoke(address, "getStreet");
        assertEquals("Parliament Hill", invoke(street, "getStreet"));
        assertEquals("77", invoke(street, "getNo"));

        if (TArrays_NUMBER_R() != null) {
            assertEquals(Arrays.asList(1, 2, 3), invoke(invoke(street, "getFloors"), "getList"));
        }
        if (TArrays_NUMBER() != null) {
            assertEquals(Arrays.asList(1, 2, 3), Arrays.asList((Object[]) invoke(street, "getFloors")));
        }

        // Third procedure
        address = (UDTRecord<?>) invoke(cRoutines(), "pEnhanceAddress3", create(), address);
        street = (UDTRecord<?>) invoke(address, "getStreet");
        assertEquals("Zwinglistrasse", invoke(street, "getStreet"));
        assertEquals("17", invoke(street, "getNo"));
    }

    @Test
    public void testArrayTables() throws Exception {
        if (TArrays_NUMBER_R() != null) {
            Result<Record> result;

            // An empty array
            // --------------
            ArrayRecord<Integer> array = newNUMBER_R();
            result = create().select().from(table(array)).fetch();

            assertEquals(0, result.size());
            assertEquals(1, result.getFields().size());
            // [#523] TODO use ArrayRecord meta data instead
//            assertEquals(array.getDataType(), result.getField(0).getDataType());

            // An array containing null
            // ------------------------
            array.set((Integer) null);
            result = create().select().from(table(array)).fetch();

            assertEquals(1, result.size());
            assertEquals(1, result.getFields().size());
//            assertEquals(array.getDataType(), result.getField(0).getDataType());
            assertEquals(null, result.getValue(0, 0));

            // An array containing two values
            // ------------------------------
            array.set((Integer) null, 1);
            result = create().select().from(table(array)).fetch();

            assertEquals(2, result.size());
            assertEquals(1, result.getFields().size());
//            assertEquals(array.getDataType(), result.getField(0).getDataType());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));

            // An array containing three values
            // --------------------------------
            array.set((Integer) null, 1, 2);
            result = create().select().from(table(array)).fetch();

            assertEquals(3, result.size());
            assertEquals(1, result.getFields().size());
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
                .on(table.getField(0).cast(Integer.class).equal(TBook_ID()))
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
                .on(table.as("t").getField(0).cast(Integer.class).equal(TBook_ID()))
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
            assertEquals(1, result.getFields().size());

            array = newNUMBER_R();
            result = create().select().from(table(FArrays1Field_R(val(array)))).fetch();
            assertEquals(0, result.size());
            assertEquals(1, result.getFields().size());

            array.set(null, 1);
            result = create().select().from(table(FArrays1Field_R(val(array)))).fetch();
            assertEquals(2, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));

            array.set(null, 1, null, 2);
            result = create().select().from(table(FArrays1Field_R(val(array)))).fetch();
            assertEquals(4, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));
            assertEquals(null, result.getValue(2, 0));
            assertEquals("2", "" + result.getValue(3, 0));
        }
        else if (TArrays_NUMBER() != null) {
            Result<Record> result;
            Table<?> table;
            Integer[] array;

            // Cross join the array table with the unnested string array value
            // ---------------------------------------------------------------

            switch (getDialect()) {
                case POSTGRES:
                case H2:
                case HSQLDB:
                    // [#1085] TODO: Is this kind of thing supported in any database??
                    log.info("SKIPPING", "Cross join of table with unnested array is not supported");
                    break;

                default:
                    table = table(TArrays_STRING()).as("t");
                    result = create()
                        .select(TArrays_ID(), table.getField(0))
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
            assertEquals(1, result.getFields().size());

            array = new Integer[0];
            result = create().select().from(table(FArrays1Field(val(array)))).fetch();
            assertEquals(0, result.size());
            assertEquals(1, result.getFields().size());

            array = new Integer[] { null, 1 };
            result = create().select().from(table(FArrays1Field(val(array)))).fetch();
            assertEquals(2, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));

            array = new Integer[] { null, 1, null, 2 };
            result = create().select().from(table(FArrays1Field(val(array)))).fetch();
            assertEquals(4, result.size());
            assertEquals(1, result.getFields().size());
            assertEquals(null, result.getValue(0, 0));
            assertEquals("1", "" + result.getValue(1, 0));
            assertEquals(null, result.getValue(2, 0));
            assertEquals("2", "" + result.getValue(3, 0));
        }
        else {
            log.info("SKIPPING", "ARRAY TABLE tests");
        }
    }

    @Test
    public void testArrayTableSimulation() throws Exception {
        Result<Record> result;

        // An empty array
        // --------------
        Integer[] array = new Integer[0];
        result = create().select().from(table(new Integer[0])).fetch();

        assertEquals(0, result.size());
        assertEquals(1, result.getFields().size());

        // An array containing null
        // ------------------------
        array = new Integer[] { null };
        result = create().select().from(table(array)).fetch();

        assertEquals(1, result.size());
        assertEquals(1, result.getFields().size());
        assertEquals(null, result.getValue(0, 0));

        // An array containing two values (some DB's can't guarantee ordering)
        // -------------------------------------------------------------------
        array = new Integer[] { null, 1 };
        result = create().select().from(table(array)).fetch();

        assertEquals(2, result.size());
        assertEquals(1, result.getFields().size());
        assertTrue(asList(array).containsAll(result.getValues(0)));

        // An array containing three values (some DB's can't guarantee ordering)
        // ---------------------------------------------------------------------
        array = new Integer[] { null, 1, 2 };
        result = create().select().from(table(array)).fetch();

        assertEquals(3, result.size());
        assertEquals(1, result.getFields().size());
        assertTrue(asList(array).containsAll(result.getValues(0)));

        // Joining an unnested array table
        // -------------------------------
        array = new Integer[] { 2, 3 };
        Table<?> table = table(array);
        result = create()
            .select(TBook_ID(), TBook_TITLE())
            .from(TBook())
            .join(table)
            .on(table.getField(0).cast(Integer.class).equal(TBook_ID()))
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
            .on(table.as("t").getField(0).cast(Integer.class).equal(TBook_ID()))
            .orderBy(TBook_ID().asc())
            .fetch();

        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(2), result.getValue(0, TBook_ID()));
        assertEquals(Integer.valueOf(3), result.getValue(1, TBook_ID()));
        assertEquals("Animal Farm", result.getValue(0, TBook_TITLE()));
        assertEquals("O Alquimista", result.getValue(1, TBook_TITLE()));
    }

    @Test
    public void testStoredProceduresWithCursorParameters() throws Exception {
        switch (getDialect()) {
            case H2:
            case HSQLDB:
            case ORACLE:
            case POSTGRES:
                break;

            default:
                log.info("SKIPPING", "Stored procedures tests with CURSOR type parameters");
                return;
        }

        // ---------------------------------------------------------------------
        // The one cursor function
        // ---------------------------------------------------------------------
        {
            Object integerArray = null;

            // Get an empty cursor
            // -------------------
            Result<Record> bFromCursor = invoke(cRoutines(), "fGetOneCursor", create(), integerArray);

            assertNotNull(bFromCursor);
            assertTrue(bFromCursor.isEmpty());
            assertEquals(0, bFromCursor.size());

            // Get a filled cursor
            // -------------------
            if (TArrays_STRING_R() != null) {
                ArrayRecord<Integer> i = newNUMBER_R();
                i.set(1, 2, 4, 6);
                integerArray = i;
            }
            else if (TArrays_STRING() != null) {
                integerArray = new Integer[] { 1, 2, 4, 6 };
            }

            bFromCursor = invoke(cRoutines(), "fGetOneCursor", create(), integerArray);

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

            switch (getDialect()) {
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

            switch (getDialect()) {
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

        if (getDialect() == SQLDialect.HSQLDB) {
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
            Object result = invoke(cRoutines(), "pGetOneCursor", create(), integerArray);

            assertNotNull(result);
            assertEquals("0", "" + invoke(result, "getTotal"));

            Result<Record> bFromCursor = invoke(result, "getBooks");
            assertTrue(bFromCursor.isEmpty());
            assertEquals(0, bFromCursor.size());

            // Get a filled cursor
            // -------------------
            if (TArrays_STRING_R() != null) {
                ArrayRecord<Integer> i = newNUMBER_R();
                i.set(1, 2, 4, 6);
                integerArray = i;
            }
            else if (TArrays_STRING() != null) {
                integerArray = new Integer[] { 1, 2, 4, 6 };
            }

            result = invoke(cRoutines(), "pGetOneCursor", create(), integerArray);

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
        if (getDialect() == SQLDialect.POSTGRES) {

            // TODO [#707] This fails for Postgres, as UDT's are not correctly
            // deserialised
            log.info("SKIPPING", "UDT/Enum types returned in refcursor (see [#707])");
        }
        else if (supportsOUTParameters()) {
            Object result = invoke(cRoutines(), "pGetTwoCursors", create());
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
