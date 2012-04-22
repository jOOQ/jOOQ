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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.Factory.cast;
import static org.jooq.impl.Factory.castNull;
import static org.jooq.impl.Factory.dateDiff;
import static org.jooq.impl.Factory.literal;
import static org.jooq.impl.Factory.timestampDiff;
import static org.jooq.impl.Factory.val;
import static org.jooq.tools.unsigned.Unsigned.ubyte;
import static org.jooq.tools.unsigned.Unsigned.uint;
import static org.jooq.tools.unsigned.Unsigned.ulong;
import static org.jooq.tools.unsigned.Unsigned.ushort;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.InsertSetMoreStep;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.SQLDataType;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.tools.unsigned.UByte;
import org.jooq.tools.unsigned.UInteger;
import org.jooq.tools.unsigned.ULong;
import org.jooq.tools.unsigned.UShort;
import org.jooq.tools.unsigned.Unsigned;
import org.jooq.types.DayToSecond;
import org.jooq.types.YearToMonth;

import org.junit.Test;

public class DataTypeTests<
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

    public DataTypeTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testBlobAndClob() throws Exception {
        jOOQAbstractTest.reset = false;

        // Superficial tests in T_BOOK table
        // ---------------------------------
        B book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        assertTrue(book.getValue(TBook_CONTENT_TEXT()).contains("doublethink"));
        assertEquals(null, book.getValue(TBook_CONTENT_PDF()));

        book.setValue(TBook_CONTENT_TEXT(), "Blah blah");
        book.setValue(TBook_CONTENT_PDF(), "Blah blah".getBytes());
        book.store();

        book = create().fetchOne(TBook(), TBook_TITLE().equal("1984"));

        assertEquals("Blah blah", book.getValue(TBook_CONTENT_TEXT()));
        assertEquals("Blah blah", new String(book.getValue(TBook_CONTENT_PDF())));

        // More in-depth tests in T_725_LOB_TEST table
        // -------------------------------------------
        T725 record = create().newRecord(T725());

        // Store and fetch NULL value
        record.setValue(T725_ID(), 1);
        assertEquals(1, record.store());
        record.refresh();
        assertNull(record.getValue(T725_LOB()));

        // Store and fetch empty byte[]. In some RDBMS, this is the same as null
        record.setValue(T725_LOB(), new byte[0]);
        assertEquals(1, record.store());
        record.refresh();

        switch (getDialect()) {

            // In ASE, there don't seem to be any empty byte[]
            case ASE:
                assertEquals(1, record.getValue(T725_LOB()).length);
                assertEquals(0, record.getValue(T725_LOB())[0]);
                break;

            // These don't make a difference between an empty byte[] and null
            case ORACLE:
            case SQLITE:
                assertNull(record.getValue(T725_LOB()));
                break;

            default:
                assertEquals(0, record.getValue(T725_LOB()).length);
                break;
        }

        // Store and fetch a filled byte[]
        record.setValue(T725_LOB(), "Blah".getBytes());
        assertEquals(1, record.store());
        record.refresh();
        assertEquals("Blah", new String(record.getValue(T725_LOB())));

        assertEquals(1, create().query("insert into " + T725().getName() + " values (?, ?)", 2, (Object) null).execute());
        assertEquals(1, create().query("insert into " + T725().getName() + " values (?, ?)", 3, new byte[0]).execute());
        assertEquals(1, create().query("insert into " + T725().getName() + " values (?, ?)", 4, "abc".getBytes()).execute());

        record.setValue(T725_ID(), 2);
        record.refresh();
        assertNull(record.getValue(T725_LOB()));

        record.setValue(T725_ID(), 3);
        record.refresh();

        switch (getDialect()) {
            case ASE:
                assertEquals(1, record.getValue(T725_LOB()).length);
                assertEquals(0, record.getValue(T725_LOB())[0]);
                break;

            case ORACLE:
            case SQLITE:
                assertNull(record.getValue(T725_LOB()));
                break;

            default:
                assertEquals(0, record.getValue(T725_LOB()).length);
                break;
        }

        record.setValue(T725_ID(), 4);
        record.refresh();
        assertEquals("abc", new String(record.getValue(T725_LOB())));

        Result<Record> result = create().fetch(
            "select " + T725_ID().getName() + ", " + T725_LOB().getName() +
            " from " + T725().getName() +
            " order by " + T725_ID().getName());
        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(0));
        assertNull(result.getValue(1, 1));

        switch (getDialect()) {
            case ASE:
                assertEquals(1, result.getValue(2, T725_LOB()).length);
                assertEquals(0, result.getValue(2, T725_LOB())[0]);
                break;

            case ORACLE:
            case SQLITE:
                assertNull(result.getValue(2, T725_LOB()));
                break;

            default:
                assertEquals(0, result.getValue(2, T725_LOB()).length);
                break;
        }

        assertEquals("abc", new String((byte[]) result.getValue(3, T725_LOB().getName())));
    }

    @Test
    public void testTypeConversions() throws Exception {
        Record record = create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Orwell"));

        assertEquals("George", record.getValue(TAuthor_FIRST_NAME()));
        assertEquals("George", record.getValueAsString(TAuthor_FIRST_NAME()));
        assertEquals("George", record.getValueAsString(TAuthor_FIRST_NAME(), "gnarf"));
        assertEquals("George", record.getValueAsString(1));
        assertEquals("George", record.getValueAsString(1, "gnarf"));

        assertEquals(Integer.valueOf("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Integer.valueOf("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH(), 123));
        assertEquals(Integer.valueOf("1903"), record.getValue(4));
        assertEquals(Integer.valueOf("1903"), record.getValue(4, 123));

        assertEquals(Short.valueOf("1903"), record.getValueAsShort(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Short.valueOf("1903"), record.getValueAsShort(TAuthor_YEAR_OF_BIRTH(), (short) 123));
        assertEquals(Short.valueOf("1903"), record.getValueAsShort(4));
        assertEquals(Short.valueOf("1903"), record.getValueAsShort(4, (short) 123));

        assertEquals(Long.valueOf("1903"), record.getValueAsLong(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Long.valueOf("1903"), record.getValueAsLong(TAuthor_YEAR_OF_BIRTH(), 123L));
        assertEquals(Long.valueOf("1903"), record.getValueAsLong(4));
        assertEquals(Long.valueOf("1903"), record.getValueAsLong(4, 123L));

        assertEquals(new BigInteger("1903"), record.getValueAsBigInteger(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(new BigInteger("1903"), record.getValueAsBigInteger(TAuthor_YEAR_OF_BIRTH(), new BigInteger("123")));
        assertEquals(new BigInteger("1903"), record.getValueAsBigInteger(4));
        assertEquals(new BigInteger("1903"), record.getValueAsBigInteger(4, new BigInteger("123")));

        assertEquals(Float.valueOf("1903"), record.getValueAsFloat(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Float.valueOf("1903"), record.getValueAsFloat(TAuthor_YEAR_OF_BIRTH(), 123f));
        assertEquals(Float.valueOf("1903"), record.getValueAsFloat(4));
        assertEquals(Float.valueOf("1903"), record.getValueAsFloat(4, 123f));

        assertEquals(Double.valueOf("1903"), record.getValueAsDouble(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Double.valueOf("1903"), record.getValueAsDouble(TAuthor_YEAR_OF_BIRTH(), 123d));
        assertEquals(Double.valueOf("1903"), record.getValueAsDouble(4));
        assertEquals(Double.valueOf("1903"), record.getValueAsDouble(4, 123d));

        assertEquals(new BigDecimal("1903"), record.getValueAsBigDecimal(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(new BigDecimal("1903"), record.getValueAsBigDecimal(TAuthor_YEAR_OF_BIRTH(), new BigDecimal("123")));
        assertEquals(new BigDecimal("1903"), record.getValueAsBigDecimal(4));
        assertEquals(new BigDecimal("1903"), record.getValueAsBigDecimal(4, new BigDecimal("123")));


        long dateOfBirth = record.getValue(TAuthor_DATE_OF_BIRTH()).getTime();
        assertEquals(dateOfBirth, record.getValueAsDate(TAuthor_DATE_OF_BIRTH()).getTime());
        assertEquals(dateOfBirth, record.getValueAsTimestamp(TAuthor_DATE_OF_BIRTH()).getTime());
        assertEquals(dateOfBirth, record.getValueAsTime(TAuthor_DATE_OF_BIRTH()).getTime());
    }

    @SuppressWarnings("serial")
    @Test
    public void testCustomConversion() {
        Converter<String, StringBuilder> converter = new Converter<String, StringBuilder>() {
            @Override
            public StringBuilder from(String databaseObject) {
                return new StringBuilder("prefix_" + databaseObject);
            }
            @Override
            public String to(StringBuilder userObject) {
                return userObject.toString().replace("prefix_", "");
            }
            @Override
            public Class<String> fromType() {
                return String.class;
            }
            @Override
            public Class<StringBuilder> toType() {
                return StringBuilder.class;
            }
        };

        List<StringBuilder> prefixed = asList(
            new StringBuilder("prefix_1984"),
            new StringBuilder("prefix_Animal Farm"),
            new StringBuilder("prefix_O Alquimista"),
            new StringBuilder("prefix_Brida"));

        // Check various Result, Record methods
        Result<Record> result =
        create().select(TBook_TITLE())
                .from(TBook())
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(strings(prefixed), strings(result.getValues(TBook_TITLE(), converter)));
        assertEquals(strings(prefixed), strings(result.getValues(TBook_TITLE().getName(), converter)));
        assertEquals(strings(prefixed), strings(result.getValues(0, converter)));

        for (int i = 0; i < 4; i++) {
            assertEquals(strings(prefixed.subList(i, i + 1)), strings(asList(result.get(i).getValue(TBook_TITLE(), converter))));
            assertEquals(strings(prefixed.subList(i, i + 1)), strings(asList(result.get(i).getValue(TBook_TITLE().getName(), converter))));
            assertEquals(strings(prefixed.subList(i, i + 1)), strings(asList(result.get(i).getValue(0, converter))));
        }

        // Check various fetch methods
        assertEquals(strings(prefixed),
                     strings(create().select(TBook_TITLE())
                                     .from(TBook())
                                     .orderBy(TBook_ID())
                                     .fetch(TBook_TITLE(), converter)));

        assertEquals(strings(prefixed),
                     strings(create().select(TBook_TITLE())
                                     .from(TBook())
                                     .orderBy(TBook_ID())
                                     .fetch(TBook_TITLE().getName(), converter)));

        assertEquals(strings(prefixed),
                     strings(create().select(TBook_TITLE())
                                     .from(TBook())
                                     .orderBy(TBook_ID())
                                     .fetch(0, converter)));

         // Check various fetchOne methods
        for (int i = 0; i < 4; i++) {
            assertEquals(strings(prefixed.subList(i, i + 1)),
                         strings(asList(create().select(TBook_TITLE())
                                                .from(TBook())
                                                .where(TBook_ID().equal(i + 1))
                                                .fetchOne(TBook_TITLE(), converter))));
        }

        // Check various fetchArray methods
        StringBuilder[] array =
        create().select(TBook_TITLE())
                .from(TBook())
                .orderBy(TBook_ID())
                .fetchArray(TBook_TITLE(), converter);

        assertEquals(strings(prefixed), strings(asList(array)));
    }

    private List<String> strings(List<StringBuilder> prefixed) {
        List<String> result = new ArrayList<String>();

        for (StringBuilder sb : prefixed) {
            result.add(sb.toString());
        }

        return result;
    }

    @Test
    public void testCastingToDialectDataType() throws Exception {
        for (DataType<?> type : getCastableDataTypes()) {
            if (getDialect() == SQLDialect.ASE ||
                getDialect() == SQLDialect.DB2 ||
                getDialect() == SQLDialect.SYBASE) {
                if (type.getType() == Boolean.class) {
                    log.info("SKIPPING", "Casting to bit type in Sybase ASE / SQL Anywhere");
                    continue;
                }
            }

            assertEquals(null, create().select(val(null, type).cast(type)).fetchOne(0));
        }
    }

    @Test
    public void testCastingToSQLDataType() throws Exception {
        for (DataType<?> type : Arrays.<DataType<?>> asList(
            SQLDataType.BIGINT,
            SQLDataType.BINARY,
            SQLDataType.BIT,
            SQLDataType.BLOB,
            SQLDataType.BOOLEAN,
            SQLDataType.CHAR,
            SQLDataType.CLOB,
            SQLDataType.DATE,
            SQLDataType.DECIMAL,
            SQLDataType.DECIMAL_INTEGER,
            SQLDataType.DOUBLE,
            SQLDataType.FLOAT,
            SQLDataType.INTEGER,
            SQLDataType.LONGNVARCHAR,
            SQLDataType.LONGVARBINARY,
            SQLDataType.LONGVARCHAR,
            SQLDataType.NCHAR,
            SQLDataType.NCLOB,
            SQLDataType.NUMERIC,
            SQLDataType.NVARCHAR,

//          TODO: is there any meaningful cast for OTHER?
//          SQLDataType.OTHER,

            SQLDataType.REAL,
            SQLDataType.SMALLINT,
            SQLDataType.TIME,
            SQLDataType.TIMESTAMP,
            SQLDataType.TINYINT,
            SQLDataType.VARBINARY,
            SQLDataType.VARCHAR)) {

            if (getDialect() == SQLDialect.ORACLE) {
                if (type.getType() == byte[].class ||
                    type == SQLDataType.CLOB ||
                    type == SQLDataType.NCLOB) {

                    log.info("SKIPPING", "Casting to lob type in Oracle");
                    continue;
                }
            }

            if (getDialect() == SQLDialect.ASE ||
                getDialect() == SQLDialect.DB2 ||
                getDialect() == SQLDialect.ORACLE ||
                getDialect() == SQLDialect.SYBASE) {
                if (type.getType() == Boolean.class) {
                    log.info("SKIPPING", "Casting to bit type in Sybase ASE / SQL Anywhere");
                    continue;
                }
            }

            assertEquals(null, create().select(val(null, type).cast(type)).fetchOne(0));
        }
    }

    @Test
    public void testCastingToJavaClass() throws Exception {
        if (getDialect() != SQLDialect.HSQLDB) {
            assertEquals(true, create().select(cast(1, Boolean.class)).fetchOne(0));

            if (getDialect() != SQLDialect.INGRES) {
                assertEquals(true, create().select(cast("1", Boolean.class)).fetchOne(0));
            }
        }

        assertEquals(BigInteger.ONE, create().select(cast("1", BigInteger.class)).fetchOne(0));
        assertEquals(BigInteger.ONE, create().select(cast(1, BigInteger.class)).fetchOne(0));

        // Sybase applies the wrong scale when casting. Force scale before comparing (Sybase returns 1.0000 when we expect 1)
        if (getDialect() == SQLDialect.SYBASE) {
            BigDecimal result = (BigDecimal)create().select(cast("1", BigDecimal.class)).fetchOne(0);
            result = result.setScale(0);
            assertEquals(BigDecimal.ONE, result);

            result = (BigDecimal)create().select(cast(1, BigDecimal.class)).fetchOne(0);
            result = result.setScale(0);
            assertEquals(BigDecimal.ONE, result);
        } else {
            assertEquals(0, BigDecimal.ONE.compareTo((BigDecimal) create().select(cast("1", BigDecimal.class)).fetchOne(0)));
            assertEquals(0, BigDecimal.ONE.compareTo((BigDecimal) create().select(cast(1, BigDecimal.class)).fetchOne(0)));
        }

        assertEquals((byte) 1, create().select(cast("1", Byte.class)).fetchOne(0));
        assertEquals((short) 1, create().select(cast("1", Short.class)).fetchOne(0));
        assertEquals(1, create().select(cast("1", Integer.class)).fetchOne(0));
        assertEquals(1L, create().select(cast("1", Long.class)).fetchOne(0));

        assertEquals(1.0f, create().select(cast("1", Float.class)).fetchOne(0));
        assertEquals(1.0, create().select(cast("1", Double.class)).fetchOne(0));
        assertEquals("1", create().select(cast("1", String.class)).fetchOne(0));

        assertEquals((byte) 1, create().select(cast(1, Byte.class)).fetchOne(0));
        assertEquals((short) 1, create().select(cast(1, Short.class)).fetchOne(0));
        assertEquals(1, create().select(cast(1, Integer.class)).fetchOne(0));
        assertEquals(1L, create().select(cast(1, Long.class)).fetchOne(0));
        assertEquals(1.0f, create().select(cast(1, Float.class)).fetchOne(0));
        assertEquals(1.0, create().select(cast(1, Double.class)).fetchOne(0));
        assertEquals("1", create().select(cast(1, String.class)).fetchOne(0));

        // Sybase ASE does not know null bits
        if (getDialect() != SQLDialect.ASE) {
            assertEquals(null, create().select(castNull(Boolean.class)).fetchOne(0));
        }

        assertEquals(null, create().select(castNull(Byte.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Short.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Integer.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Long.class)).fetchOne(0));

        // Not implemented by the driver
        if (getDialect() != SQLDialect.SQLITE) {
            assertEquals(null, create().select(castNull(BigInteger.class)).fetchOne(0));
            assertEquals(null, create().select(castNull(BigDecimal.class)).fetchOne(0));
        }

        assertEquals(null, create().select(castNull(Float.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Double.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(String.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Date.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Time.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Timestamp.class)).fetchOne(0));

        assertEquals(1984, create()
            .select(TBook_TITLE().cast(Integer.class))
            .from(TBook())
            .where(TBook_ID().equal(1))
            .fetch().getValue(0, 0));

        assertEquals(1984L, create()
            .select(TBook_TITLE().cast(Long.class))
            .from(TBook())
            .where(TBook_ID().equal(1))
            .fetch().getValue(0, 0));
    }

    @Test
    public void testNestedCasting() throws Exception {
        // TODO: These tests fail on some dialects. Investigate in some post-2.2.0 release
        if (true) return;

        assertEquals(1,
            create().select(val(1).cast(Long.class).cast(Integer.class)).fetchOne(0));
        assertEquals(3,
            create().select(val(1).cast(Long.class).add(val(2).cast(String.class).cast(Integer.class)).cast(Integer.class)).fetchOne(0));
        assertEquals("3",
            create().select(val(1).add(val("2")).cast(String.class)).fetchOne(0));
    }

    @Test
    public void testConversionResult() throws Exception {
        // .fetch(..., Class)
        // ------------------
        assertEquals(
            Arrays.asList((byte) 1, (byte) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Byte.class));
        assertEquals(
            Arrays.asList((short) 1, (short) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Short.class));
        assertEquals(
            Arrays.asList(1, 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Integer.class));
        assertEquals(
            Arrays.asList(1L, 2L),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Long.class));
        assertEquals(
            Arrays.asList(ubyte((byte) 1), ubyte((byte) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, UByte.class));
        assertEquals(
            Arrays.asList(ushort((short) 1), ushort((short) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, UShort.class));
        assertEquals(
            Arrays.asList(uint(1), uint(2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, UInteger.class));
        assertEquals(
            Arrays.asList(ulong(1L), ulong(2L)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, ULong.class));
        assertEquals(
            Arrays.asList(1.0f, 2.0f),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Float.class));
        assertEquals(
            Arrays.asList(1.0, 2.0),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, Double.class));
        assertEquals(
            Arrays.asList(new BigInteger("1"), new BigInteger("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, BigInteger.class));
        assertEquals(
            Arrays.asList(new BigDecimal("1"), new BigDecimal("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(0, BigDecimal.class));


        assertEquals(
            Arrays.asList((byte) 1, (byte) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Byte.class));
        assertEquals(
            Arrays.asList((short) 1, (short) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Short.class));
        assertEquals(
            Arrays.asList(1, 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Integer.class));
        assertEquals(
            Arrays.asList(1L, 2L),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Long.class));
        assertEquals(
            Arrays.asList(ubyte((byte) 1), ubyte((byte) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), UByte.class));
        assertEquals(
            Arrays.asList(ushort((short) 1), ushort((short) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), UShort.class));
        assertEquals(
            Arrays.asList(uint(1), uint(2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), UInteger.class));
        assertEquals(
            Arrays.asList(ulong(1L), ulong(2L)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), ULong.class));
        assertEquals(
            Arrays.asList(1.0f, 2.0f),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Float.class));
        assertEquals(
            Arrays.asList(1.0, 2.0),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), Double.class));
        assertEquals(
            Arrays.asList(new BigInteger("1"), new BigInteger("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), BigInteger.class));
        assertEquals(
            Arrays.asList(new BigDecimal("1"), new BigDecimal("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetch(TAuthor_ID().getName(), BigDecimal.class));


        // .fetchArray(..., Class)
        // ------------------
        assertEquals(
            Arrays.asList((byte) 1, (byte) 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Byte.class)));
        assertEquals(
            Arrays.asList((short) 1, (short) 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Short.class)));
        assertEquals(
            Arrays.asList(1, 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Integer.class)));
        assertEquals(
            Arrays.asList(1L, 2L),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Long.class)));
        assertEquals(
            Arrays.asList(ubyte((byte) 1), ubyte((byte) 2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, UByte.class)));
        assertEquals(
            Arrays.asList(ushort((short) 1), ushort((short) 2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, UShort.class)));
        assertEquals(
            Arrays.asList(uint(1), uint(2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, UInteger.class)));
        assertEquals(
            Arrays.asList(ulong(1L), ulong(2L)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, ULong.class)));
        assertEquals(
            Arrays.asList(1.0f, 2.0f),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Float.class)));
        assertEquals(
            Arrays.asList(1.0, 2.0),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, Double.class)));
        assertEquals(
            Arrays.asList(new BigInteger("1"), new BigInteger("2")),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, BigInteger.class)));
        assertEquals(
            Arrays.asList(new BigDecimal("1"), new BigDecimal("2")),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(0, BigDecimal.class)));


        assertEquals(
            Arrays.asList((byte) 1, (byte) 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Byte.class)));
        assertEquals(
            Arrays.asList((short) 1, (short) 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Short.class)));
        assertEquals(
            Arrays.asList(1, 2),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Integer.class)));
        assertEquals(
            Arrays.asList(1L, 2L),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Long.class)));
        assertEquals(
            Arrays.asList(ubyte((byte) 1), ubyte((byte) 2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), UByte.class)));
        assertEquals(
            Arrays.asList(ushort((short) 1), ushort((short) 2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), UShort.class)));
        assertEquals(
            Arrays.asList(uint(1), uint(2)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), UInteger.class)));
        assertEquals(
            Arrays.asList(ulong(1L), ulong(2L)),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), ULong.class)));
        assertEquals(
            Arrays.asList(1.0f, 2.0f),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Float.class)));
        assertEquals(
            Arrays.asList(1.0, 2.0),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), Double.class)));
        assertEquals(
            Arrays.asList(new BigInteger("1"), new BigInteger("2")),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), BigInteger.class)));
        assertEquals(
            Arrays.asList(new BigDecimal("1"), new BigDecimal("2")),
            Arrays.asList(create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).fetchArray(TAuthor_ID().getName(), BigDecimal.class)));


        // .fetchOne(..., Class)
        // ---------------------
        assertEquals(
            (byte) 1,
            (byte) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Byte.class));
        assertEquals(
            (short) 1,
            (short) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Short.class));
        assertEquals(
            1,
            (int) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Integer.class));
        assertEquals(
            1L,
            (long) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Long.class));
        assertEquals(
            ubyte((byte) 1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, UByte.class));
        assertEquals(
            ushort((short) 1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, UShort.class));
        assertEquals(
            uint(1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, UInteger.class));
        assertEquals(
            ulong(1L),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, ULong.class));
        assertEquals(
            1.0f,
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Float.class));
        assertEquals(
            1.0,
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Double.class));
        assertEquals(
            new BigInteger("1"),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, BigInteger.class));
        assertEquals(
            new BigDecimal("1"),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, BigDecimal.class));


        assertEquals(
            (byte) 1,
            (byte) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Byte.class));
        assertEquals(
            (short) 1,
            (short) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Short.class));
        assertEquals(
            1,
            (int) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Integer.class));
        assertEquals(
            1L,
            (long) create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Long.class));
        assertEquals(
            ubyte((byte) 1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), UByte.class));
        assertEquals(
            ushort((short) 1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), UShort.class));
        assertEquals(
            uint(1),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), UInteger.class));
        assertEquals(
            ulong(1L),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), ULong.class));
        assertEquals(
            1.0f,
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Float.class));
        assertEquals(
            1.0,
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Double.class));
        assertEquals(
            new BigInteger("1"),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), BigInteger.class));
        assertEquals(
            new BigDecimal("1"),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), BigDecimal.class));

    }

    @Test
    public void testConversion() throws Exception {
        assertEquals(null, SQLDataType.TINYINT.convert(null));
        assertEquals(null, SQLDataType.TINYINTUNSIGNED.convert(null));
        assertEquals(null, SQLDataType.SMALLINT.convert(null));
        assertEquals(null, SQLDataType.SMALLINTUNSIGNED.convert(null));
        assertEquals(null, SQLDataType.INTEGER.convert(null));
        assertEquals(null, SQLDataType.INTEGERUNSIGNED.convert(null));
        assertEquals(null, SQLDataType.BIGINT.convert(null));
        assertEquals(null, SQLDataType.BIGINTUNSIGNED.convert(null));
        assertEquals(null, SQLDataType.REAL.convert(null));
        assertEquals(null, SQLDataType.DOUBLE.convert(null));
        assertEquals(null, SQLDataType.DECIMAL_INTEGER.convert(null));
        assertEquals(null, SQLDataType.NUMERIC.convert(null));
        assertEquals(null, SQLDataType.BOOLEAN.convert(null));
        assertEquals(null, SQLDataType.VARCHAR.convert(null));
        assertEquals(null, SQLDataType.DATE.convert(null));
        assertEquals(null, SQLDataType.TIME.convert(null));
        assertEquals(null, SQLDataType.TIMESTAMP.convert(null));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert('1'));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert('1'));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert('1'));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert('1'));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert('1'));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert('1'));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert('1'));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert('1'));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert('1'));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert('1'));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert('1'));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert('1'));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert('1'));
        assertEquals("1", SQLDataType.VARCHAR.convert('1'));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert("1"));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert("1"));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert("1"));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert("1"));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert("1"));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert("1"));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert("1"));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert("1"));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert("1"));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert("1"));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert("1"));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert("1"));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert("1"));
        assertEquals("1", SQLDataType.VARCHAR.convert("1"));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert("  1"));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert("  1"));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert("  1"));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert("  1"));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert("  1"));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert("  1"));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert("  1"));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert("  1"));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert("  1"));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert("  1"));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert("  1"));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert("  1"));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert("  1"));
        assertEquals("  1", SQLDataType.VARCHAR.convert("  1"));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert((byte) 1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert((byte) 1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert((byte) 1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert((byte) 1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert((byte) 1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert((byte) 1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert((byte) 1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert((byte) 1));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert((byte) 1));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert((byte) 1));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert((byte) 1));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert((byte) 1));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert((byte) 1));
        assertEquals("1", SQLDataType.VARCHAR.convert((byte) 1));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert((short) 1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert((short) 1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert((short) 1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert((short) 1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert((short) 1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert((short) 1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert((short) 1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert((short) 1));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert((short) 1));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert((short) 1));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert((short) 1));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert((short) 1));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert((short) 1));
        assertEquals("1", SQLDataType.VARCHAR.convert((short) 1));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(1));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert(1));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert(1));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(1));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert(1));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert(1));
        assertEquals("1", SQLDataType.VARCHAR.convert(1));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert((long) 1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert((long) 1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert((long) 1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert((long) 1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert((long) 1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert((long) 1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert((long) 1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert((long) 1));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert((long) 1));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert((long) 1));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert((long) 1));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert((long) 1));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert((long) 1));
        assertEquals("1", SQLDataType.VARCHAR.convert((long) 1));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(1.1f));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(1.1f));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(1.1f));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(1.1f));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(1.1f));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(1.1f));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(1.1f));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(1.1f));
        assertEquals(Float.valueOf("1.1"), SQLDataType.REAL.convert(1.1f));
        assertEquals(Double.valueOf("1.1"), SQLDataType.DOUBLE.convert(1.1f));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(1.1f));
        assertEquals(new BigDecimal("1.1"), SQLDataType.NUMERIC.convert(1.1f));
        assertEquals(null, SQLDataType.BOOLEAN.convert(1.1f));
        assertEquals("1.1", SQLDataType.VARCHAR.convert(1.1f));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(1.1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(1.1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(1.1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(1.1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(1.1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(1.1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(1.1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(1.1));
        assertEquals(Float.valueOf("1.1"), SQLDataType.REAL.convert(1.1));
        assertEquals(Double.valueOf("1.1"), SQLDataType.DOUBLE.convert(1.1));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(1.1));
        assertEquals(new BigDecimal("1.1"), SQLDataType.NUMERIC.convert(1.1));
        assertEquals(null, SQLDataType.BOOLEAN.convert(1.1));
        assertEquals("1.1", SQLDataType.VARCHAR.convert(1.1));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(new BigInteger("1")));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(new BigInteger("1")));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(new BigInteger("1")));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(new BigInteger("1")));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(new BigInteger("1")));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(new BigInteger("1")));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(new BigInteger("1")));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(new BigInteger("1")));
        assertEquals(Float.valueOf("1"), SQLDataType.REAL.convert(new BigInteger("1")));
        assertEquals(Double.valueOf("1"), SQLDataType.DOUBLE.convert(new BigInteger("1")));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(new BigInteger("1")));
        assertEquals(new BigDecimal("1"), SQLDataType.NUMERIC.convert(new BigInteger("1")));
        assertEquals(Boolean.TRUE, SQLDataType.BOOLEAN.convert(new BigInteger("1")));
        assertEquals("1", SQLDataType.VARCHAR.convert(new BigInteger("1")));

        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(new BigDecimal("1.1")));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(new BigDecimal("1.1")));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(new BigDecimal("1.1")));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(new BigDecimal("1.1")));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Float.valueOf("1.1"), SQLDataType.REAL.convert(new BigDecimal("1.1")));
        assertEquals(Double.valueOf("1.1"), SQLDataType.DOUBLE.convert(new BigDecimal("1.1")));
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(new BigDecimal("1.1")));
        assertEquals(new BigDecimal("1.1"), SQLDataType.NUMERIC.convert(new BigDecimal("1.1")));
        assertEquals(null, SQLDataType.BOOLEAN.convert(new BigDecimal("1.1")));
        assertEquals("1.1", SQLDataType.VARCHAR.convert(new BigDecimal("1.1")));

        assertEquals(new Date(1), SQLDataType.DATE.convert(new Date(1)));
        assertEquals(new Time(1), SQLDataType.TIME.convert(new Date(1)));
        assertEquals(new Timestamp(1), SQLDataType.TIMESTAMP.convert(new Date(1)));

        assertEquals(new Date(1), SQLDataType.DATE.convert(new Time(1)));
        assertEquals(new Time(1), SQLDataType.TIME.convert(new Time(1)));
        assertEquals(new Timestamp(1), SQLDataType.TIMESTAMP.convert(new Time(1)));

        assertEquals(new Date(1), SQLDataType.DATE.convert(new Timestamp(1)));
        assertEquals(new Time(1), SQLDataType.TIME.convert(new Timestamp(1)));
        assertEquals(new Timestamp(1), SQLDataType.TIMESTAMP.convert(new Timestamp(1)));

        assertEquals(new Date(1), SQLDataType.DATE.convert(1L));
        assertEquals(new Time(1), SQLDataType.TIME.convert(1L));
        assertEquals(new Timestamp(1), SQLDataType.TIMESTAMP.convert(1L));

        // [#936] Primitive type conversion
        A author1 = create().newRecord(TAuthor());
        assertEquals(Byte.valueOf("0"), author1.getValue(TAuthor_ID(), byte.class));
        assertEquals(Short.valueOf("0"), author1.getValue(TAuthor_ID(), short.class));
        assertEquals(Integer.valueOf("0"), author1.getValue(TAuthor_ID(), int.class));
        assertEquals(Long.valueOf("0"), author1.getValue(TAuthor_ID(), long.class));
        assertEquals(Float.valueOf("0"), author1.getValue(TAuthor_ID(), float.class));
        assertEquals(Double.valueOf("0"), author1.getValue(TAuthor_ID(), double.class));
        assertEquals(Boolean.FALSE, author1.getValue(TAuthor_ID(), boolean.class));
        assertEquals(Character.valueOf((char) 0), author1.getValue(TAuthor_ID(), char.class));

        author1.setValue(TAuthor_ID(), 1);
        assertEquals(Byte.valueOf("1"), author1.getValue(TAuthor_ID(), byte.class));
        assertEquals(Short.valueOf("1"), author1.getValue(TAuthor_ID(), short.class));
        assertEquals(Integer.valueOf("1"), author1.getValue(TAuthor_ID(), int.class));
        assertEquals(Long.valueOf("1"), author1.getValue(TAuthor_ID(), long.class));
        assertEquals(Float.valueOf("1"), author1.getValue(TAuthor_ID(), float.class));
        assertEquals(Double.valueOf("1"), author1.getValue(TAuthor_ID(), double.class));
        assertEquals(Boolean.TRUE, author1.getValue(TAuthor_ID(), boolean.class));
        assertEquals(Character.valueOf('1'), author1.getValue(TAuthor_ID(), char.class));

        // [#926] Some additional date conversion checks
        // ---------------------------------------------
        A author2 = create().newRecord(TAuthor());
        author2.setValue(TAuthor_DATE_OF_BIRTH(), new Date(1));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(1);

        assertEquals(new Date(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), Date.class));
        assertEquals(new Time(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), Time.class));
        assertEquals(new Timestamp(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), Timestamp.class));
        assertEquals(new java.util.Date(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), java.util.Date.class));
        assertEquals(calendar, author2.getValue(TAuthor_DATE_OF_BIRTH(), Calendar.class));
        assertEquals(Long.valueOf(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), Long.class));
        assertEquals(Long.valueOf(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), long.class));
        assertEquals(ULong.valueOf(1), author2.getValue(TAuthor_DATE_OF_BIRTH(), ULong.class));

        // [#933] Character conversion checks
        // ----------------------------------
        author2.setValue(TAuthor_ID(), 1);
        author2.setValue(TAuthor_LAST_NAME(), "a");
        assertEquals(Character.valueOf('1'), author2.getValue(TAuthor_ID(), Character.class));
        assertEquals(Character.valueOf('1'), author2.getValue(TAuthor_ID(), char.class));
        assertEquals(Character.valueOf('a'), author2.getValue(TAuthor_LAST_NAME(), Character.class));
        assertEquals(Character.valueOf('a'), author2.getValue(TAuthor_LAST_NAME(), char.class));
        assertEquals(null, author2.getValue(TAuthor_FIRST_NAME(), Character.class));
        assertEquals(Character.valueOf((char) 0), author2.getValue(TAuthor_FIRST_NAME(), char.class));

        author2.setValue(TAuthor_ID(), 12);
        author2.setValue(TAuthor_LAST_NAME(), "ab");
        try {
            author2.getValue(TAuthor_ID(), Character.class);
            fail();
        }
        catch (DataTypeException expected) {}
        try {
            author2.getValue(TAuthor_ID(), char.class);
            fail();
        }
        catch (DataTypeException expected) {}
        try {
            author2.getValue(TAuthor_LAST_NAME(), Character.class);
            fail();
        }
        catch (DataTypeException expected) {}
        try {
            author2.getValue(TAuthor_LAST_NAME(), char.class);
            fail();
        }
        catch (DataTypeException expected) {}
    }

    @Test
    public void testUnsignedDataTypes() throws Exception {
        if (TUnsigned() == null) {
            log.info("SKIPPING", "Unsigned tests");
            return;
        }

        jOOQAbstractTest.reset = false;

        // unsigned null values
        // --------------------
        assertEquals(1,
        create().insertInto(TUnsigned(),
                            TUnsigned_U_BYTE(),
                            TUnsigned_U_SHORT(),
                            TUnsigned_U_INT(),
                            TUnsigned_U_LONG())
                .values(null, null, null, null)
                .execute());

        assertEquals(1, create().selectCount().from(TUnsigned()).fetchOne(0));
        U u = create().selectFrom(TUnsigned()).fetchOne();
        assertNotNull(u);
        assertNull(u.getValue(TUnsigned_U_BYTE()));
        assertNull(u.getValue(TUnsigned_U_SHORT()));
        assertNull(u.getValue(TUnsigned_U_INT()));
        assertNull(u.getValue(TUnsigned_U_LONG()));

        // unsigned 1
        // ----------
        assertEquals(1,
        create().insertInto(TUnsigned())
                .set(TUnsigned_U_BYTE(), Unsigned.ubyte((byte) 1))
                .set(TUnsigned_U_SHORT(), Unsigned.ushort((short) 1))
                .set(TUnsigned_U_INT(), Unsigned.uint(1))
                .set(TUnsigned_U_LONG(), Unsigned.ulong(1L))
                .execute());

        assertEquals(2, create().selectCount().from(TUnsigned()).fetchOne(0));
        u = create().selectFrom(TUnsigned()).where(TUnsigned_U_INT().equal(Unsigned.uint(1))).fetchOne();
        assertNotNull(u);
        assertEquals(Unsigned.ubyte("1"), u.getValue(TUnsigned_U_BYTE()));
        assertEquals(Unsigned.ushort("1"), u.getValue(TUnsigned_U_SHORT()));
        assertEquals(Unsigned.uint("1"), u.getValue(TUnsigned_U_INT()));
        assertEquals(Unsigned.ulong("1"), u.getValue(TUnsigned_U_LONG()));

        assertEquals("1", u.getValue(TUnsigned_U_BYTE(), String.class));
        assertEquals("1", u.getValue(TUnsigned_U_SHORT(), String.class));
        assertEquals("1", u.getValue(TUnsigned_U_INT(), String.class));
        assertEquals("1", u.getValue(TUnsigned_U_LONG(), String.class));

        assertEquals(Unsigned.ubyte("1"), u.getValue(TUnsigned_U_BYTE()));
        assertEquals(Unsigned.ushort("1"), u.getValue(TUnsigned_U_SHORT()));
        assertEquals(Unsigned.uint("1"), u.getValue(TUnsigned_U_INT()));
        assertEquals(Unsigned.ulong("1"), u.getValue(TUnsigned_U_LONG()));

        // unsigned max-values
        // -------------------
        assertEquals(1,
        create().insertInto(TUnsigned())
                .set(TUnsigned_U_BYTE(), Unsigned.ubyte((byte) -1))
                .set(TUnsigned_U_SHORT(), Unsigned.ushort((short) -1))
                .set(TUnsigned_U_INT(), Unsigned.uint(-1))
                .set(TUnsigned_U_LONG(), Unsigned.ulong(-1L))
                .execute());

        assertEquals(3, create().selectCount().from(TUnsigned()).fetchOne(0));
        u = create().selectFrom(TUnsigned()).where(TUnsigned_U_INT().equal(Unsigned.uint(-1))).fetchOne();
        assertNotNull(u);
        assertEquals(Unsigned.ubyte(UByte.MAX_VALUE), u.getValue(TUnsigned_U_BYTE()));
        assertEquals(Unsigned.ushort(UShort.MAX_VALUE), u.getValue(TUnsigned_U_SHORT()));
        assertEquals(Unsigned.uint(UInteger.MAX_VALUE), u.getValue(TUnsigned_U_INT()));
        assertEquals(Unsigned.ulong(ULong.MAX_VALUE), u.getValue(TUnsigned_U_LONG()));

        assertEquals((byte) -1, u.getValue(TUnsigned_U_BYTE()).byteValue());
        assertEquals((short) -1, u.getValue(TUnsigned_U_SHORT()).shortValue());
        assertEquals(-1, u.getValue(TUnsigned_U_INT()).intValue());
        assertEquals(-1L, u.getValue(TUnsigned_U_LONG()).longValue());
    }

    @Test
    public void testNumbers() throws Exception {
        jOOQAbstractTest.reset = false;

        // Insert some numbers
        // -------------------
        InsertSetMoreStep<T639> set =
        create().insertInto(T639())
                .set(T639_ID(), 1)
                .set(T639_BIG_DECIMAL(), new BigDecimal("1234.5670"))
                .set(T639_BIG_INTEGER(), new BigInteger("1234567890"))
                .set(T639_BYTE_DECIMAL(), (byte) 2)
                .set(T639_INTEGER(), 3)
                .set(T639_INTEGER_DECIMAL(), 4)
                .set(T639_LONG(), 5L)
                .set(T639_LONG_DECIMAL(), 6L)
                .set(T639_SHORT(), (short) 7)
                .set(T639_SHORT_DECIMAL(), (short) 8);

        if (T639_BYTE() != null) set.set(T639_BYTE(), (byte) 9);
        if (T639_DOUBLE() != null) set.set(T639_DOUBLE(), 10.125);
        if (T639_FLOAT() != null) set.set(T639_FLOAT(), 11.375f);

        assertEquals(1, set.execute());

        T639 record = create().fetchOne(T639());
        assertEquals(1, (int) record.getValue(T639_ID()));
        assertTrue(new BigDecimal("1234.567").compareTo(record.getValue(T639_BIG_DECIMAL())) == 0);
        assertEquals(new BigInteger("1234567890"), record.getValue(T639_BIG_INTEGER()));
        assertEquals(2, (byte) record.getValue(T639_BYTE_DECIMAL()));
        assertEquals(3, (int) record.getValue(T639_INTEGER()));
        assertEquals(4, (int) record.getValue(T639_INTEGER_DECIMAL()));
        assertEquals(5L, (long) record.getValue(T639_LONG()));
        assertEquals(6L, (long) record.getValue(T639_LONG_DECIMAL()));
        assertEquals(7, (short) record.getValue(T639_SHORT()));
        assertEquals(8, (short) record.getValue(T639_SHORT_DECIMAL()));

        if (T639_BYTE() != null) assertEquals(9, (byte) record.getValue(T639_BYTE()));
        if (T639_DOUBLE() != null) assertEquals(10.125, (double) record.getValue(T639_DOUBLE()));
        if (T639_FLOAT() != null) assertEquals(11.375f, (float) record.getValue(T639_FLOAT()));

        // Various BigDecimal tests
        // ------------------------
        if (getDialect() == SQLDialect.SQLITE) {
            log.info("SKIPPING", "Advanced BigDecimal tests");
        }
        else {
            create().insertInto(T639(), T639_ID(), T639_BIG_DECIMAL())
                    .values(2, new BigDecimal("123456789012345.67899"))
                    .values(3, new BigDecimal("999999999999999.99999"))
                    .values(4, new BigDecimal("1.00001"))
                    .values(5, new BigDecimal("0.00001"))
                    .values(6, new BigDecimal("0.00001"))
                    .execute();

            Result<Record> result =
            create().select(T639_ID(), T639_BIG_DECIMAL())
                    .from(T639())
                    .where(T639_ID().between(2, 6))
                    .orderBy(T639_ID())
                    .fetch();

            assertEquals(Arrays.asList(2, 3, 4, 5, 6), result.getValues(0));
            assertEquals(new BigDecimal("123456789012345.67899"), result.getValue(0, 1));
            assertEquals(new BigDecimal("999999999999999.99999"), result.getValue(1, 1));
            assertEquals(new BigDecimal("1.00001"), result.getValue(2, 1));
            assertEquals(new BigDecimal("0.00001"), result.getValue(3, 1));
            assertEquals(new BigDecimal("0.00001"), result.getValue(4, 1));
        }
    }

    @Test
    public void testDateTime() throws Exception {

        // [#1009] SQL DATE doesn't have a time zone. SQL TIMESTAMP does
        long tsShift = -3600000;

        Record record =
        create().select(
            val(new Date(0)).as("d"),
            val(new Time(0)).as("t"),
            val(new Timestamp(0)).as("ts")
        ).fetchOne();

        // ... (except for SQLite)
        if (getDialect() != SQLITE)
            assertEquals(new Date(tsShift), record.getValue("d"));

        assertEquals(new Time(0), record.getValue("t"));
        assertEquals(new Timestamp(0), record.getValue("ts"));

        // Interval tests
        // --------------
        if (getDialect() == ASE ||
            getDialect() == DB2 ||
            getDialect() == DERBY ||
            getDialect() == CUBRID ||
            getDialect() == H2 ||
            getDialect() == MYSQL ||
            getDialect() == SQLSERVER ||
            getDialect() == SQLITE ||
            getDialect() == SYBASE) {

            log.info("SKIPPING", "Interval tests");
        }

        // Other dialects support actual interval data types
        else {
            record =
            create().select(
                val(new YearToMonth(1, 1)).as("iyplus"),
                val(new YearToMonth(0)).as("iy"),
                val(new YearToMonth(1, 1).neg()).as("iyminus"),

                val(new DayToSecond(1, 1, 1, 1)).as("idplus"),
                val(new DayToSecond(0)).as("id"),
                val(new DayToSecond(1, 1, 1, 1).neg()).as("idminus")
            ).fetchOne();

            assertEquals(new YearToMonth(1, 1), record.getValue("iyplus"));
            assertEquals(new YearToMonth(0), record.getValue("iy"));
            assertEquals(new YearToMonth(1, 1).neg(), record.getValue("iyminus"));

            assertEquals(new DayToSecond(1, 1, 1, 1), record.getValue("idplus"));
            assertEquals(new DayToSecond(0), record.getValue("id"));
            assertEquals(new DayToSecond(1, 1, 1, 1).neg(), record.getValue("idminus"));
            // TODO: Add tests for reading date / time / interval types into pojos

            // [#566] INTERVAL arithmetic: multiplication
            // ------------------------------------------
            record =
            create().select(
                val(new YearToMonth(1)).div(2).as("y1"),
                val(new YearToMonth(1)).mul(2).as("y2"),
                val(new YearToMonth(1)).div(2).mul(2).as("y3"),

                val(new DayToSecond(1)).div(2).as("d1"),
                val(new DayToSecond(1)).mul(2).as("d2"),
                val(new DayToSecond(1)).div(2).mul(2).as("d3")
            ).fetchOne();

            assertEquals(new YearToMonth(0, 6), record.getValue("y1"));
            assertEquals(new YearToMonth(2), record.getValue("y2"));
            assertEquals(new YearToMonth(1), record.getValue("y3"));

            assertEquals(new DayToSecond(0, 12), record.getValue("d1"));
            assertEquals(new DayToSecond(2), record.getValue("d2"));
            assertEquals(new DayToSecond(1), record.getValue("d3"));
        }
    }

    @Test
    public void testDateTimeArithmetic() throws Exception {

        // [#1009] SQL DATE doesn't have a time zone. SQL TIMESTAMP does
        long tsShift = -3600000;

        // [#566] INTERVAL arithmetic: addition
        // ------------------------------------
        Record record =
        create().select(

            // Extra care needs to be taken with Postgres negative DAY TO SECOND
            // intervals. Postgres allows for having several signs in intervals
            val(new Date(0)).add(1).as("d1"),
            val(new Date(0)).add(-1).as("d2a"),
            val(new Date(0)).sub(1).as("d2b"),
            val(new Date(0)).add(new YearToMonth(1, 6)).as("d3"),
            val(new Date(0)).add(new YearToMonth(1, 6).neg()).as("d4a"),
            val(new Date(0)).sub(new YearToMonth(1, 6)).as("d4b"),
            val(new Date(0)).add(new DayToSecond(2)).as("d5"),
            val(new Date(0)).add(new DayToSecond(2).neg()).as("d6a"),
            val(new Date(0)).sub(new DayToSecond(2)).as("d6b"),

            val(new Timestamp(0)).add(1).as("ts1"),
            val(new Timestamp(0)).add(-1).as("ts2a"),
            val(new Timestamp(0)).sub(1).as("ts2b"),
            val(new Timestamp(0)).add(new YearToMonth(1, 6)).as("ts3"),
            val(new Timestamp(0)).add(new YearToMonth(1, 6).neg()).as("ts4a"),
            val(new Timestamp(0)).sub(new YearToMonth(1, 6)).as("ts4b"),
            val(new Timestamp(0)).add(new DayToSecond(2)).as("ts5"),
            val(new Timestamp(0)).add(new DayToSecond(2).neg()).as("ts6a"),
            val(new Timestamp(0)).sub(new DayToSecond(2)).as("ts6b"),
            val(new Timestamp(0)).add(new DayToSecond(2, 6)).as("ts7"),
            val(new Timestamp(0)).add(new DayToSecond(2, 6).neg()).as("ts8a"),
            val(new Timestamp(0)).sub(new DayToSecond(2, 6)).as("ts8b"),

            // Dummy field for simpler testing
            literal("'dummy'")
        ).fetchOne();

        Calendar cal;

        cal = cal();
        cal.add(Calendar.DATE, 1);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d1"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts1"));

        cal = cal();
        cal.add(Calendar.DATE, -1);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d2a"));
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d2b"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts2a"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts2b"));

        cal = cal();
        cal.add(Calendar.MONTH, 18);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d3"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts3"));

        cal = cal();
        cal.add(Calendar.MONTH, -18);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d4a"));
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d4b"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts4a"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts4b"));

        cal = cal();
        cal.add(Calendar.DATE, 2);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d5"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts5"));

        cal = cal();
        cal.add(Calendar.DATE, -2);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d6a"));
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d6b"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts6a"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts6b"));

        cal = cal();
        cal.add(Calendar.DATE, 2);
        cal.add(Calendar.HOUR, 6);
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts7"));

        cal = cal();
        cal.add(Calendar.DATE, -2);
        cal.add(Calendar.HOUR, -6);
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts8a"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts8b"));

        // [#566] INTERVAL arithmetic: difference
        // --------------------------------------
        record =
        create().select(
            dateDiff(new Date(0), new Date(30 * 60 * 60 * 1000L)).as("d1"),
            dateDiff(new Date(30 * 60 * 60 * 1000L), new Date(0)).as("d2"),
            //TODO [#566] Make this work!
            //timeDiff(new Time(0), new Time(60 * 60 * 1000L)).as("t1"),
            //timeDiff(new Time(60 * 60 * 1000L), new Time(0)).as("t2"),
            timestampDiff(new Timestamp(0), new Timestamp(30 * 60 * 60 * 1000L)).as("ts1"),
            timestampDiff(new Timestamp(30 * 60 * 60 * 1000L), new Timestamp(0)).as("ts2"),

            // Dummy field for simpler testing
            literal("'dummy'")
        ).fetchOne();

        assertEquals(-1, record.getValue("d1"));
        assertEquals(1, record.getValue("d2"));
        //assertEquals(new DayToSecond(0, 1).neg(), record.getValue("t1"));
        //assertEquals(new DayToSecond(0, 1), record.getValue("t2"));
        assertEquals(new DayToSecond(1, 6).neg(), record.getValue("ts1"));
        assertEquals(new DayToSecond(1, 6), record.getValue("ts2"));
    }

    private Calendar cal() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(-3600000);
        return cal;
    }
 }
