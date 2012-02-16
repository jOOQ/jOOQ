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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.param;
import static org.jooq.impl.Factory.vals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;

import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.Execution;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.Factory;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class RenderAndBindTests<
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

    public RenderAndBindTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testSelectGetSQLAndGetBindValues() throws Exception {
        Select<?> select =
        create().select(TBook_ID(), TBook_ID().mul(6).div(2).div(3))
                .from(TBook())
                .orderBy(TBook_ID(), TBook_ID().mod(2));

        assertEquals(
            asList(6, 2, 3, 2),
            select.getBindValues());

        log.info("Executing", select.getSQL());
        PreparedStatement stmt = jOOQAbstractTest.connection.prepareStatement(select.getSQL());
        int i = 0;
        for (Object value : select.getBindValues()) {
            stmt.setObject(++i, value);
        }

        ResultSet rs = stmt.executeQuery();
        Result<Record> result = create().fetch(rs);
        assertEquals(BOOK_IDS, result.getValues(TBook_ID(), Integer.class));
        assertEquals(BOOK_IDS, result.getValues(1, Integer.class));

        try {
            assertEquals(BOOK_IDS, result.getValues(2, Integer.class));
            fail();
        } catch (IllegalArgumentException expected) {}

        stmt.close();
    }

    @Test
    public void testInsertUpdateGetSQLAndGetBindValues() throws Exception {
        jOOQAbstractTest.reset = false;

        // INSERT INTO .. SET syntax
        // ----------------------------
        Insert<A> insert1 =
        create().insertInto(TAuthor())
                .set(TAuthor_ID(), 1)
                .set(TAuthor_FIRST_NAME(), null)
                .set(TAuthor_LAST_NAME(), "Koontz");

        assertEquals(
            Arrays.<Object>asList(1, null, "Koontz"),
            insert1.getBindValues());

        // INSERT INTO .. VALUES syntax
        // ----------------------------
        Insert<A> insert2 =
        create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
                .values(1, null, "Hesse");

        assertEquals(
            Arrays.<Object>asList(1, null, "Hesse"),
            insert2.getBindValues());
    }

    @Test
    public void testNamedParams() throws Exception {
        Select<?> select =
        create().select(
                    TAuthor_ID(),
                    param("p1", String.class))
                .from(TAuthor())
                .where(TAuthor_ID().in(
                    param("p2", Integer.class),
                    param("p3", Integer.class)))
                .orderBy(TAuthor_ID().asc());

        // Should execute fine, but no results due to IN (null, null) filter
        assertEquals(0, select.fetch().size());

        // Set both parameters to the same value
        select.getParam("p2").setConverted(1L);
        select.getParam("p3").setConverted("1");
        Result<?> result1 = select.fetch();
        assertEquals(1, result1.size());
        assertEquals(1, result1.getValue(0, 0));
        assertNull(result1.getValue(0, 1));

        // Set more parameters
        select.getParam("p1").setConverted("asdf");
        select.getParam("p3").setConverted("2");
        Result<?> result2 = select.fetch();
        assertEquals(2, result2.size());
        assertEquals(1, result2.getValue(0, 0));
        assertEquals(2, result2.getValue(1, 0));
        assertEquals("asdf", result2.getValue(0, 1));
        assertEquals("asdf", result2.getValue(1, 1));
    }

    @Test
    public void testUnknownBindTypes() throws Exception {

        // [#1028] [#1029] Named params without any associated type information
        Select<?> select = create().select(
            param("p1"),
            param("p2"));

        select.bind(1, "10");
        select.bind(2, null);
        Result<?> result3 = select.fetch();

        assertEquals(1, result3.size());
        assertEquals("10", result3.getValue(0, 0));
        assertEquals(null, result3.getValue(0, 1));
    }

    @Test
    public void testSelectBindValues() throws Exception {
        Select<?> select =
        create().select(
                    TAuthor_ID(),
                    param("p1", String.class))
                .from(TAuthor())
                .where(TAuthor_ID().in(
                    param("p2", Integer.class),
                    param("p3", Integer.class)))
                .orderBy(TAuthor_ID().asc());

        // Should execute fine, but no results due to IN (null, null) filter
        assertEquals(0, select.fetch().size());

        // Set both condition parameters to the same value
        Result<?> result1 =
        select.bind("p2", 1L)
              .bind(3, "1")
              .fetch();
        assertEquals(1, result1.size());
        assertEquals(1, result1.getValue(0, 0));
        assertNull(result1.getValue(0, 1));

        // Set selection parameter, too
        Result<?> result2 =
        select.bind(1, "asdf")
              .bind("p3", "2")
              .fetch();
        assertEquals(2, result2.size());
        assertEquals(1, result2.getValue(0, 0));
        assertEquals(2, result2.getValue(1, 0));
        assertEquals("asdf", result2.getValue(0, 1));
        assertEquals("asdf", result2.getValue(1, 1));
    }

    @Test
    public void testSelectBindValuesWithPlainSQL() throws Exception {
        Select<?> select =
        create().select(TAuthor_ID())
                .from(TAuthor())
                .where(TAuthor_ID().in(

                    // [#724] Check for API misuse
                    field("?", Integer.class, (Object[]) null),
                    field("?", Integer.class, (Object[]) null)))
                .and(TAuthor_ID().getName() + " != ? or 'abc' = '???'", 37)
                .orderBy(TAuthor_ID().asc());

        // Should execute fine, but no results due to IN (null, null) filter
        assertEquals(0, select.fetch().size());

        // Set both parameters to the same value
        Result<?> result1 =
        select.bind(1, 1L)
              .bind(2, 1)
              .fetch();
        assertEquals(1, result1.size());
        assertEquals(1, result1.getValue(0, 0));

        // Set selection parameter, too
        Result<?> result2 =
        select.bind(2, 2)
              .fetch();
        assertEquals(2, result2.size());
        assertEquals(1, result2.getValue(0, 0));
        assertEquals(2, result2.getValue(1, 0));
    }

    @Test
    public void testInlinedBindValues() throws Exception {
        // [#1147] Some data types need special care when inlined

        String s1 = "test";
        String s2 = "no SQL 'injection here; <<`'";
        String s3 = "''";
        String s4 = null;

        Byte b1 = Byte.valueOf("1");
        Byte b2 = null;
        Short sh1 = Short.valueOf("2");
        Short sh2 = null;
        Integer i1 = 3;
        Integer i2 = null;
        Long l1 = 4L;
        Long l2 = null;
        BigInteger bi1 = new BigInteger("5");
        BigInteger bi2 = null;
        BigDecimal bd1 = new BigDecimal("6.01");
        BigDecimal bd2 = null;
        Double db1 = 7.25;
        Double db2 = null;
        Float f1 = 8.5f;
        Float f2 = null;

        Date d1 = Date.valueOf("1981-07-10");
        Date d2 = null;
        Time t1 = Time.valueOf("12:01:15");
        Time t2 = null;
        Timestamp ts1 = Timestamp.valueOf("1981-07-10 12:01:15");
        Timestamp ts2 = null;

        byte[] by1 = "some bytes".getBytes();
        byte[] by2 = null;
        Boolean bool1 = true;
        Boolean bool2 = false;
        Boolean bool3 = null;

        Factory create = create(new Settings()
            .withExecution(new Execution()
            .withStatementType(StatementType.STATEMENT)));

        Object[] array1 = create.select(vals(s1, s2, s3, s4)).fetchOneArray();
        Object[] array2 = create.select(vals(b1, b2, sh1, sh2, i1, i2, l1, l2, bi1, bi2, bd1, bd2, db1, db2, f1, f2)).fetchOneArray();
        Object[] array3 = create.select(vals(d1, d2, t1, t2, ts1, ts2)).fetchOneArray();
        Object[] array4 = create.select(vals(by1, by2, bool1, bool2, bool3)).fetchOneArray();

        assertEquals(4, array1.length);
        assertEquals(16, array2.length);
        assertEquals(6, array3.length);
        assertEquals(5, array4.length);

        assertEquals(asList(s1, s2, s3, s4), asList(array1));
        assertEquals(asList((Number) b1, b2, sh1, sh2, i1, i2, l1, l2, bi1, bi2, bd1, bd2, db1, db2, f1, f2), asList(array2));
        assertEquals(asList(d1, d2, t1, t2, ts1, ts2), asList(array3));

        array4[0] = new String((byte[]) array4[0]);
        assertEquals(asList(new String(by1), by2, bool1, bool2, bool3), asList(array4));
    }
}
