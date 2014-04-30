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
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.conf.SettingsTools.executePreparedStatements;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.RenderKeywordStyle;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class RenderAndBindTests<
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

    public RenderAndBindTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

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

        // [#1145] Don't set bind values if not needed
        if (executePreparedStatements(create().configuration().settings())) {
            int i = 0;
            for (Object value : select.getBindValues()) {
                stmt.setObject(++i, value);
            }
        }

        ResultSet rs = stmt.executeQuery();
        Result<Record> result = create().fetch(rs);
        assertEquals(BOOK_IDS, result.getValues(0, Integer.class));
        assertEquals(BOOK_IDS, result.getValues(1, Integer.class));

        try {
            assertEquals(BOOK_IDS, result.getValues(2, Integer.class));
            fail();
        } catch (IllegalArgumentException expected) {}

        stmt.close();
    }

    public void testInsertUpdateGetSQLAndGetBindValues() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#1128] Be sure that NULL values are created as bind variables too.
        // They used to be always inlined for historic reasons.

        // INSERT INTO .. SET syntax
        // ----------------------------
        Insert<A> insert1 =
        create().insertInto(TAuthor())
                .set(TAuthor_ID(), 1)
                .set(TAuthor_FIRST_NAME(), (String) null)
                .set(TAuthor_LAST_NAME(), "Koontz");

        assertEquals(
            asList((Object) 1, null, "Koontz"),
            insert1.getBindValues());

        // INSERT INTO .. VALUES syntax
        // ----------------------------
        Insert<A> insert2 =
        create().insertInto(TAuthor(), TAuthor_ID(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
                .values(1, null, "Hesse");

        assertEquals(
            asList((Object) 1, null, "Hesse"),
            insert2.getBindValues());
    }

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

    public void testManyVarcharBindValues() throws Exception {
        /* [pro] xx
        xx xxxxxxx xx xxxxxxxx xxxxx xxxxx xx xxx xxxxxxxx
        xx [/pro] */
        int n = 255;
        String s = "1234567890";


        // [#1726] Check if "large" amounts of VARCHAR bind values can be handled
        Record record = create().select(Collections.nCopies(n, val(s))).fetchOne();
        assertEquals(n, record.size());
        assertEquals(Collections.nCopies(n, s), asList(record.intoArray()));

        assertEquals(1, create().selectOne()
            .where(val(s).in(Collections.nCopies(n, val(s)).toArray(new Field[0])))
            .fetchOne(0));
    }

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

    public void testSelectBindValuesWithPlainSQL() throws Exception {
        Select<?> select =
        create().select(TAuthor_ID())
                .from(TAuthor())
                .where(TAuthor_ID().in(

                    // [#724] Check for API misuse
                    field("?", Integer.class, (Object[]) null),
                    field("?", Integer.class, (Object[]) null)))
                .and(TAuthor_ID().getName() + " <> ? or 'abc' = '???'", 37)
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

    public void testInlinedBindValues() throws Exception {

        // [#1158] TODO get this working for derby as well
        boolean derby = (dialect() == DERBY);

        // [#1147] Some data types need special care when inlined

        // Selection from DUAL
        // -------------------
        String s1 = "test";
        String s2 = "no SQL 'injection here; <<`'";
        String s3 = "''";
        String s4 = (derby ? s1 : null);

        // [#2669] MySQL uses backslashes for escaping...
        String s5 = "no SQL \\injection\\ in MySQL either\\";

        Byte b1 = Byte.valueOf("1");
        Byte b2 = (derby ? b1 : null);
        Short sh1 = Short.valueOf("2");
        Short sh2 = (derby ? sh1 : null);
        Integer i1 = 3;
        Integer i2 = (derby ? i1 : null);
        Long l1 = 4L;
        Long l2 = (derby ? l1 : null);
        BigInteger bi1 = new BigInteger("5");
        BigInteger bi2 = (derby ? bi1 : null);
        BigDecimal bd1 = new BigDecimal("6.01");
        BigDecimal bd2 = (derby ? bd1 : null);
        Double db1 = 7.25;
        Double db2 = (derby ? db1 : null);
        Float f1 = 8.5f;
        Float f2 = (derby ? f1 : null);

        Date d1 = Date.valueOf("1981-07-10");
        Date d2 = (derby ? d1 : null);
        Time t1 = Time.valueOf("12:01:15");
        Time t2 = (derby ? t1 : null);
        Timestamp ts1 = Timestamp.valueOf("1981-07-10 12:01:15");
        Timestamp ts2 = (derby ? ts1 : null);

        byte[] by1 = "some bytes".getBytes();
        byte[] by2 = (derby ? by1 : null);
        Boolean bool1 = true;
        Boolean bool2 = false;
        Boolean bool3 = (derby ? bool1 : null);

        // Inlining bind values globally, through the factory settings
        // -----------------------------------------------------------
        {
            DSLContext create = create(new Settings()
                .withStatementType(StatementType.STATIC_STATEMENT));

            Object[] array1 = create.select(
                val(s1), val(s2), val(s3), val(s4), val(s5)
            ).fetchOneArray();
            Object[] array2 = create.select(
                val(b1), val(b2),
                val(sh1), val(sh2),
                val(i1), val(i2),
                val(l1), val(l2),
                val(bi1), val(bi2),
                val(bd1), val(bd2),
                val(db1), val(db2),
                val(f1), val(f2)
            ).fetchOneArray();
            Object[] array3 = create.select(
                val(d1), val(d2), val(t1), val(t2), val(ts1), val(ts2)
            ).fetchOneArray();
            Object[] array4 = create.select(
                val(by1), val(by2), val(bool1), val(bool2), val(bool3)
            ).fetchOneArray();

            assertEquals(5, array1.length);
            assertEquals(16, array2.length);
            assertEquals(6, array3.length);
            assertEquals(5, array4.length);

            assertEquals(asList(s1, s2, s3, s4, s5), asList(array1));
            assertEquals(asList((Number) b1, b2, sh1, sh2, i1, i2, l1, l2, bi1, bi2, bd1, bd2, db1, db2, f1, f2), asList(array2));
            assertEquals(asList(d1, d2, t1, t2, ts1, ts2), asList(array3));

            array4[0] = new String((byte[]) array4[0]);
            array4[1] = (derby ? new String((byte[]) array4[1]) : array4[1]);

            assertEquals(asList(new String(by1), (derby ? new String(by2) : by2), bool1, bool2, bool3), asList(array4));
        }

        // Inlining bind values locally, through bind value parameters
        // -----------------------------------------------------------
        {
            DSLContext create = create();

            Object[] array1 = create.select(inline(s1), inline(s2), inline(s3), inline(s4), inline(s5)).fetchOneArray();
            Object[] array2 = create.select(inline(b1), inline(b2), inline(sh1), inline(sh2), inline(i1), inline(i2), inline(l1), inline(l2), inline(bi1), inline(bi2), inline(bd1), inline(bd2), inline(db1), inline(db2), inline(f1), inline(f2)).fetchOneArray();
            Object[] array3 = create.select(inline(d1), inline(d2), inline(t1), inline(t2), inline(ts1), inline(ts2)).fetchOneArray();
            Object[] array4 = create.select(inline(by1), inline(by2), inline(bool1), inline(bool2), inline(bool3)).fetchOneArray();

            assertEquals(5, array1.length);
            assertEquals(16, array2.length);
            assertEquals(6, array3.length);
            assertEquals(5, array4.length);

            assertEquals(asList(s1, s2, s3, s4, s5), asList(array1));
            assertEquals(asList((Number) b1, b2, sh1, sh2, i1, i2, l1, l2, bi1, bi2, bd1, bd2, db1, db2, f1, f2), asList(array2));
            assertEquals(asList(d1, d2, t1, t2, ts1, ts2), asList(array3));

            array4[0] = new String((byte[]) array4[0]);
            array4[1] = (derby ? new String((byte[]) array4[1]) : array4[1]);

            assertEquals(asList(new String(by1), (derby ? new String(by2) : by2), bool1, bool2, bool3), asList(array4));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })

    public void testInlinedBindValuesForNumberTypes() throws Exception {
        jOOQAbstractTest.reset = false;

        Double db1 = 1234.5678E9;
        Float f1 = 1234.5678E9f;

        // Inlining bind values globally, through the factory settings
        // -----------------------------------------------------------
        {
            DSLContext create = create(new Settings()
                .withStatementType(StatementType.STATIC_STATEMENT));

            // [#1557] Check correct inlining of floating point values with
            // exponential notation.
            assertEquals(2,
            create.insertInto(T639(), T639_ID(), (Field<Object>) (Field) T639_BIG_DECIMAL())
                  .values(1, db1)
                  .values(2, f1)
                  .execute());

            Result<T639> result = create.selectFrom(T639()).orderBy(T639_ID()).fetch();
            assertEquals(1, (int) result.getValue(0, T639_ID()));
            assertEquals(2, (int) result.getValue(1, T639_ID()));
            assertEquals(1234, (int) (result.get(0).getValue(T639_BIG_DECIMAL(), Double.class) / 1E9));
            assertEquals(1234, (int) (result.get(0).getValue(T639_BIG_DECIMAL(), Float.class) / 1E9f));
        }
    }

    public void testInlinedBindValuesForDatetime() throws Exception {
        jOOQAbstractTest.reset = false;

        Date d1 = Date.valueOf("1981-07-10");
        Time t1 = Time.valueOf("12:01:15");
        Timestamp ts1 = Timestamp.valueOf("1981-07-10 12:01:15");

        DSLContext create = create(new Settings()
            .withStatementType(StatementType.STATIC_STATEMENT));

        DATE date = create.newRecord(TDates());
        date.setValue(TDates_ID(), 1);
        assertEquals(1, date.store());

        date.setValue(TDates_ID(), 2);
        date.setValue(TDates_D(), d1);
        date.setValue(TDates_T(), t1);
        date.setValue(TDates_TS(), ts1);
        assertEquals(1, date.store());

        Result<Record4<Integer, Date, Time, Timestamp>> dates =
        create.select(TDates_ID(), TDates_D(), TDates_T(), TDates_TS())
              .from(TDates())
              .orderBy(TDates_ID())
              .fetch();

        assertEquals(2, dates.size());
        assertEquals(asList(1, 2), dates.getValues(TDates_ID()));
        assertEquals(asList(1, null, null, null), asList(dates.get(0).intoArray()));
        assertEquals(asList((Object) 2, d1, t1, ts1), asList(dates.get(1).intoArray()));
    }

    public void testRenderNameStyle() throws Exception {
        Select<?> s =
        create(new Settings().withRenderNameStyle(RenderNameStyle.AS_IS))
            .select(TBook_ID(), TBook_TITLE(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
            .from(TBook())
            .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
            .orderBy(TBook_ID());

        Result<?> result = s.fetch();
        assertEquals(BOOK_IDS, result.getValues(TBook_ID()));
        assertEquals(BOOK_TITLES, result.getValues(TBook_TITLE()));
        assertEquals(BOOK_FIRST_NAMES, result.getValues(TAuthor_FIRST_NAME()));
        assertEquals(BOOK_LAST_NAMES, result.getValues(TAuthor_LAST_NAME()));

        // [#521] Ensure that no quote characters are rendered
        assertFalse(s.getSQL().contains("\""));
        assertFalse(s.getSQL().contains("["));
        assertFalse(s.getSQL().contains("]"));
        assertFalse(s.getSQL().contains("`"));

        assertTrue(s.getSQL().toUpperCase().contains("T_BOOK.ID"));
        assertTrue(s.getSQL().toUpperCase().contains("T_BOOK.TITLE"));
        assertTrue(s.getSQL().toUpperCase().contains("T_AUTHOR.FIRST_NAME"));
        assertTrue(s.getSQL().toUpperCase().contains("T_AUTHOR.LAST_NAME"));
    }

    public void testRenderKeywordStyle() throws Exception {
        Select<?> s =
        create(new Settings().withRenderKeywordStyle(RenderKeywordStyle.UPPER))
            .select(TBook_ID(), TBook_TITLE(), TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())
            .from(TBook())
            .join(TAuthor()).on(TBook_AUTHOR_ID().equal(TAuthor_ID()))
            .orderBy(TBook_ID());

        Result<?> result = s.fetch();
        assertEquals(BOOK_IDS, result.getValues(TBook_ID()));
        assertEquals(BOOK_TITLES, result.getValues(TBook_TITLE()));
        assertEquals(BOOK_FIRST_NAMES, result.getValues(TAuthor_FIRST_NAME()));
        assertEquals(BOOK_LAST_NAMES, result.getValues(TAuthor_LAST_NAME()));

        // [#521] Ensure that no quote characters are rendered
        assertTrue(s.getSQL().contains("SELECT"));
        assertTrue(s.getSQL().contains("FROM"));
        assertTrue(s.getSQL().contains("JOIN"));
        assertTrue(s.getSQL().contains("ON"));
        assertTrue(s.getSQL().contains("ORDER BY"));
    }

    public void testRenderFormattedAndInlinedWithNewlines() throws Exception {
        jOOQAbstractTest.reset = false;

        // [#2528] When inlining bind values, formatting SQL might change the
        // values, in case values contain newlines
        DSLContext create = create(new Settings()
            .withRenderFormatted(true)
            .withStatementType(StatementType.STATIC_STATEMENT));

        String value = "foo\nbar\n\n  baz";

        create.update(TBook())
              .set(TBook_TITLE(), value)
              .where(TBook_ID().eq(1))
              .execute();

        assertEquals(value, create.select(TBook_TITLE())
                                  .from(TBook())
                                  .where(TBook_ID().eq(1))
                                  .fetchOne(TBook_TITLE()));
    }
}
