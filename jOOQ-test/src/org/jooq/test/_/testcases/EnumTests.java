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
import static junit.framework.Assert.assertNotNull;
import static org.jooq.tools.reflect.Reflect.on;

import java.util.List;

import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.MasterDataType;
import org.jooq.Result;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test._.converters.Boolean_10;
import org.jooq.test._.converters.Boolean_TF_LC;
import org.jooq.test._.converters.Boolean_TF_UC;
import org.jooq.test._.converters.Boolean_YES_NO_LC;
import org.jooq.test._.converters.Boolean_YES_NO_UC;
import org.jooq.test._.converters.Boolean_YN_LC;
import org.jooq.test._.converters.Boolean_YN_UC;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;

import org.junit.Test;

public class EnumTests<
    A    extends UpdatableRecord<A>,
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
extends BaseTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> {

    public EnumTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testEnums() throws Exception {
        if (TBook_STATUS() == null) {
            log.info("SKIPPING", "enums test");
            return;
        }

        jOOQAbstractTest.reset = false;

        B book = create()
            .selectFrom(TBook())
            .where(TBook_PUBLISHED_IN().equal(1990))
            .fetchOne();
        Enum<?> value = book.getValue(TBook_STATUS());
        assertEquals("SOLD_OUT", value.name());
        assertEquals("SOLD OUT", ((EnumType) value).getLiteral());

        // Another copy of the original record
        book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));
        book.setValue((Field) TBook_STATUS(), Enum.valueOf(value.getClass(), "ON_STOCK"));
        book.store();

        book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));
        value = book.getValue(TBook_STATUS());
        assertEquals("ON_STOCK", value.name());
        assertEquals("ON STOCK", ((EnumType) value).getLiteral());
    }

    @Test
    public <R extends TableRecord<R>> void testCustomEnums() throws Exception {
        jOOQAbstractTest.reset = false;

        // This does not yet work correctly for Sybase ASE, Postgres
        // Sybase: Is casting enums to unknown enum types
        // ASE: Cannot implicitly cast '1' to 1

        // TODO [#677] [#1013] This doesn't work correctly yet for
        // Ingres, HSQLDB, H2, Derby, Sybase ASE
        // Double-check again for Postgres

        // Insertion
        // --------------------------------------------------------------------
        assertEquals(1,
        create().insertInto(TBooleans())
                .set(TBooleans_ID(), 1)
                .set(TBooleans_BOOLEAN_10(), Boolean_10.ZERO)
                .set(TBooleans_Boolean_TF_LC(), Boolean_TF_LC.FALSE)
                .set(TBooleans_Boolean_TF_UC(), Boolean_TF_UC.FALSE)
                .set(TBooleans_Boolean_YES_NO_LC(), Boolean_YES_NO_LC.no)
                .set(TBooleans_Boolean_YES_NO_UC(), Boolean_YES_NO_UC.NO)
                .set(TBooleans_Boolean_YN_LC(), Boolean_YN_LC.n)
                .set(TBooleans_Boolean_YN_UC(), Boolean_YN_UC.N)
//                .set(TBooleans_C(), false)
//                .set(TBooleans_VC(), false)
//                .set(TBooleans_N(), false)
                .execute());

        assertEquals(1,
        create().insertInto(TBooleans())
                .set(TBooleans_ID(), 2)
                .set(TBooleans_BOOLEAN_10(), Boolean_10.ONE)
                .set(TBooleans_Boolean_TF_LC(), Boolean_TF_LC.TRUE)
                .set(TBooleans_Boolean_TF_UC(), Boolean_TF_UC.TRUE)
                .set(TBooleans_Boolean_YES_NO_LC(), Boolean_YES_NO_LC.yes)
                .set(TBooleans_Boolean_YES_NO_UC(), Boolean_YES_NO_UC.YES)
                .set(TBooleans_Boolean_YN_LC(), Boolean_YN_LC.y)
                .set(TBooleans_Boolean_YN_UC(), Boolean_YN_UC.Y)
//                .set(TBooleans_C(), true)
//                .set(TBooleans_VC(), true)
//                .set(TBooleans_N(), true)
                .execute());

        // Selection
        // --------------------------------------------------------------------
        Result<?> result =
        create().selectFrom(TBooleans())
                .where(TBooleans_ID().in(1, 2))
                .and(TBooleans_BOOLEAN_10().in(Boolean_10.ONE, Boolean_10.ZERO))
                .and(TBooleans_Boolean_TF_LC().in(Boolean_TF_LC.TRUE, Boolean_TF_LC.FALSE))
                .and(TBooleans_Boolean_TF_UC().in(Boolean_TF_UC.TRUE, Boolean_TF_UC.FALSE))
                .and(TBooleans_Boolean_YES_NO_LC().in(Boolean_YES_NO_LC.yes, Boolean_YES_NO_LC.no))
                .and(TBooleans_Boolean_YES_NO_UC().in(Boolean_YES_NO_UC.YES, Boolean_YES_NO_UC.NO))
                .and(TBooleans_Boolean_YN_LC().in(Boolean_YN_LC.y, Boolean_YN_LC.n))
                .and(TBooleans_Boolean_YN_UC().in(Boolean_YN_UC.Y, Boolean_YN_UC.N))
                .orderBy(TBooleans_ID().asc())
                .fetch();

        assertEquals(1, (int) result.getValue(0, TBooleans_ID()));
        assertEquals(2, (int) result.getValue(1, TBooleans_ID()));

        assertEquals(Boolean_10.ZERO, result.getValue(0, TBooleans_BOOLEAN_10()));
        assertEquals(Boolean_10.ONE, result.getValue(1, TBooleans_BOOLEAN_10()));

        assertEquals(Boolean_TF_LC.FALSE, result.getValue(0, TBooleans_Boolean_TF_LC()));
        assertEquals(Boolean_TF_LC.TRUE, result.getValue(1, TBooleans_Boolean_TF_LC()));

        assertEquals(Boolean_TF_UC.FALSE, result.getValue(0, TBooleans_Boolean_TF_UC()));
        assertEquals(Boolean_TF_UC.TRUE, result.getValue(1, TBooleans_Boolean_TF_UC()));

        assertEquals(Boolean_YES_NO_LC.no, result.getValue(0, TBooleans_Boolean_YES_NO_LC()));
        assertEquals(Boolean_YES_NO_LC.yes, result.getValue(1, TBooleans_Boolean_YES_NO_LC()));

        assertEquals(Boolean_YES_NO_UC.NO, result.getValue(0, TBooleans_Boolean_YES_NO_UC()));
        assertEquals(Boolean_YES_NO_UC.YES, result.getValue(1, TBooleans_Boolean_YES_NO_UC()));

        assertEquals(Boolean_YN_LC.n, result.getValue(0, TBooleans_Boolean_YN_LC()));
        assertEquals(Boolean_YN_LC.y, result.getValue(1, TBooleans_Boolean_YN_LC()));

        assertEquals(Boolean_YN_UC.N, result.getValue(0, TBooleans_Boolean_YN_UC()));
        assertEquals(Boolean_YN_UC.Y, result.getValue(1, TBooleans_Boolean_YN_UC()));

//        assertFalse(result.getValue(0, TBooleans_C()));
//        assertTrue(result.getValue(1, TBooleans_C()));
//
//        assertFalse(result.getValue(0, TBooleans_VC()));
//        assertTrue(result.getValue(1, TBooleans_VC()));
//
//        assertFalse(result.getValue(0, TBooleans_N()));
//        assertTrue(result.getValue(1, TBooleans_N()));

        // Conversion to custom POJOs
        // --------------------------------------------------------------------
        try {
            Reflect booleans = on(TBooleans().getClass().getPackage().getName() + ".pojos." + TBooleans().getClass().getSimpleName());

            List<Object> b =
            create().selectFrom(TBooleans())
                    .orderBy(TBooleans_ID().asc())
                    .fetchInto((Class<?>) booleans.get());

            assertEquals(2, b.size());
            assertEquals(1, on(b.get(0)).call("getId").get());
            assertEquals(2, on(b.get(1)).call("getId").get());

            assertEquals(Boolean_10.ZERO, on(b.get(0)).call("getOneZero").get());
            assertEquals(Boolean_10.ONE, on(b.get(1)).call("getOneZero").get());

            assertEquals(Boolean_TF_LC.FALSE, on(b.get(0)).call("getTrueFalseLc").get());
            assertEquals(Boolean_TF_LC.TRUE, on(b.get(1)).call("getTrueFalseLc").get());

            assertEquals(Boolean_TF_UC.FALSE, on(b.get(0)).call("getTrueFalseUc").get());
            assertEquals(Boolean_TF_UC.TRUE, on(b.get(1)).call("getTrueFalseUc").get());

            assertEquals(Boolean_YES_NO_LC.no, on(b.get(0)).call("getYesNoLc").get());
            assertEquals(Boolean_YES_NO_LC.yes, on(b.get(1)).call("getYesNoLc").get());

            assertEquals(Boolean_YES_NO_UC.NO, on(b.get(0)).call("getYesNoUc").get());
            assertEquals(Boolean_YES_NO_UC.YES, on(b.get(1)).call("getYesNoUc").get());

            assertEquals(Boolean_YN_LC.n, on(b.get(0)).call("getYNLc").get());
            assertEquals(Boolean_YN_LC.y, on(b.get(1)).call("getYNLc").get());

            assertEquals(Boolean_YN_UC.N, on(b.get(0)).call("getYNUc").get());
            assertEquals(Boolean_YN_UC.Y, on(b.get(1)).call("getYNUc").get());
        }
        catch (ReflectException e) {
            log.info("SKIPPING", "Generated POJO tests");
        }

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testMasterData() throws Exception {
        if (!supportsReferences()) {
            log.info("SKIPPING", "master data test");
            return;
        }

        jOOQAbstractTest.reset = false;

        B book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        Enum<?> value = (Enum<?>) book.getValue(TBook_LANGUAGE_ID());
        assertEquals(Integer.valueOf(1), ((MasterDataType<?>) value).getPrimaryKey());
        assertEquals("en", value.name());

        book.setValue((Field) TBook_LANGUAGE_ID(), Enum.valueOf(value.getClass(), "de"));
        book.store();

        book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));
        value = (Enum<?>) book.getValue(TBook_LANGUAGE_ID());
        assertEquals(Integer.valueOf(2), ((MasterDataType<?>) value).getPrimaryKey());
        assertEquals("de", value.name());

        // [#658] - General master data test
        T658 master = create().fetchOne(T658());
        assertNotNull(master);
        assertEquals("A", invoke(master.getValue(0), "getPrimaryKey").toString().trim());
        assertEquals("A", invoke(master.getValue(0), "getId").toString().trim());
        assertEquals(1, invoke(master.getValue(1), "getPrimaryKey"));
        assertEquals(1, invoke(master.getValue(1), "getId"));
        assertEquals(1L, invoke(master.getValue(2), "getPrimaryKey"));
        assertEquals(1L, invoke(master.getValue(2), "getId"));

        assertEquals("B", invoke(master.getValue(3), "getPrimaryKey").toString().trim());
        assertEquals("B", invoke(master.getValue(3), "getId").toString().trim());
        assertEquals("B", invoke(master.getValue(3), "getCd").toString().trim());
        assertEquals(2, invoke(master.getValue(4), "getPrimaryKey"));
        assertEquals(2, invoke(master.getValue(4), "getId"));
        assertEquals(2, invoke(master.getValue(4), "getCd"));
        assertEquals(2L, invoke(master.getValue(5), "getPrimaryKey"));
        assertEquals(2L, invoke(master.getValue(5), "getId"));
        assertEquals(2L, invoke(master.getValue(5), "getCd"));
    }
}
