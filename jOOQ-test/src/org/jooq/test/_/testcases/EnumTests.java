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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import org.jooq.EnumType;
import org.jooq.Field;
import org.jooq.MasterDataType;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class EnumTests<
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

    public EnumTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
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

    public <R extends TableRecord<R>> void testCustomEnums() throws Exception {
        jOOQAbstractTest.reset = false;

        // This does not yet work correctly for Sybase ASE, Postgres
        // Sybase: Is casting enums to unknown enum types
        // ASE: Cannot implicitly cast '1' to 1

        // TODO [#677] [#1013] This doesn't work correctly yet for
        // Ingres, HSQLDB, H2, Derby, Sybase ASE
        // Double-check again for Postgres

        @SuppressWarnings("unchecked")
        Table<R> booleans = (Table<R>) getTable("T_BOOLEANS");

        @SuppressWarnings("unchecked")
        Field<Integer> id = (Field<Integer>) getField(booleans, "ID");

        @SuppressWarnings("unchecked")
        Field<EnumType> e1 = (Field<EnumType>) getField(booleans, "ONE_ZERO");
        EnumType e1False = (EnumType) e1.getType().getField("_0").get(e1.getType());
        EnumType e1True = (EnumType) e1.getType().getField("_1").get(e1.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e2 = (Field<EnumType>) getField(booleans, "TRUE_FALSE_LC");
        EnumType e2False = (EnumType) e2.getType().getField("false_").get(e2.getType());
        EnumType e2True = (EnumType) e2.getType().getField("true_").get(e2.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e3 = (Field<EnumType>) getField(booleans, "TRUE_FALSE_UC");
        EnumType e3False = (EnumType) e3.getType().getField("FALSE").get(e3.getType());
        EnumType e3True = (EnumType) e3.getType().getField("TRUE").get(e3.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e4 = (Field<EnumType>) getField(booleans, "YES_NO_LC");
        EnumType e4False = (EnumType) e4.getType().getField("no").get(e4.getType());
        EnumType e4True = (EnumType) e4.getType().getField("yes").get(e4.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e5 = (Field<EnumType>) getField(booleans, "YES_NO_UC");
        EnumType e5False = (EnumType) e5.getType().getField("NO").get(e5.getType());
        EnumType e5True = (EnumType) e5.getType().getField("YES").get(e5.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e6 = (Field<EnumType>) getField(booleans, "Y_N_LC");
        EnumType e6False = (EnumType) e6.getType().getField("n").get(e6.getType());
        EnumType e6True = (EnumType) e6.getType().getField("y").get(e6.getType());

        @SuppressWarnings("unchecked")
        Field<EnumType> e7 = (Field<EnumType>) getField(booleans, "Y_N_UC");
        EnumType e7False = (EnumType) e7.getType().getField("N").get(e7.getType());
        EnumType e7True = (EnumType) e7.getType().getField("Y").get(e7.getType());

        @SuppressWarnings("unchecked")
        Field<Boolean> b1 = (Field<Boolean>) getField(booleans, "C_BOOLEAN");

        @SuppressWarnings("unchecked")
        Field<Boolean> b2 = (Field<Boolean>) getField(booleans, "VC_BOOLEAN");

        @SuppressWarnings("unchecked")
        Field<Boolean> b3 = (Field<Boolean>) getField(booleans, "N_BOOLEAN");

        assertEquals(1,
        create().insertInto(booleans)
                .set(id, 1)
                .set(e1, e1False)
                .set(e2, e2False)
                .set(e3, e3False)
                .set(e4, e4False)
                .set(e5, e5False)
                .set(e6, e6False)
                .set(e7, e7False)
                .set(b1, false)
                .set(b2, false)
                .set(b3, false)
                .execute());

        assertEquals(1,
        create().insertInto(booleans)
                .set(id, 2)
                .set(e1, e1True)
                .set(e2, e2True)
                .set(e3, e3True)
                .set(e4, e4True)
                .set(e5, e5True)
                .set(e6, e6True)
                .set(e7, e7True)
                .set(b1, true)
                .set(b2, true)
                .set(b3, true)
                .execute());

        Result<?> result =
        create().selectFrom(booleans).orderBy(id.asc()).fetch();

        assertEquals(1, (int) result.getValue(0, id));
        assertEquals(2, (int) result.getValue(1, id));

        assertEquals(e1False, result.getValue(0, e1));
        assertEquals(e1True, result.getValue(1, e1));

        assertEquals(e2False, result.getValue(0, e2));
        assertEquals(e2True, result.getValue(1, e2));

        assertEquals(e3False, result.getValue(0, e3));
        assertEquals(e3True, result.getValue(1, e3));

        assertEquals(e4False, result.getValue(0, e4));
        assertEquals(e4True, result.getValue(1, e4));

        assertEquals(e5False, result.getValue(0, e5));
        assertEquals(e5True, result.getValue(1, e5));

        assertEquals(e6False, result.getValue(0, e6));
        assertEquals(e6True, result.getValue(1, e6));

        assertEquals(e7False, result.getValue(0, e7));
        assertEquals(e7True, result.getValue(1, e7));

        assertFalse(result.getValue(0, b1));
        assertTrue(result.getValue(1, b1));

        assertFalse(result.getValue(0, b2));
        assertTrue(result.getValue(1, b2));

        assertFalse(result.getValue(0, b3));
        assertTrue(result.getValue(1, b3));
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
