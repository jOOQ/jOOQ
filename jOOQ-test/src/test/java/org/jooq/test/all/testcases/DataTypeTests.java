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
import static java.util.stream.Collectors.joining;
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HANA;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.REDSHIFT;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.castNull;
import static org.jooq.impl.DSL.currentDate;
import static org.jooq.impl.DSL.dateAdd;
import static org.jooq.impl.DSL.dateDiff;
import static org.jooq.impl.DSL.dateSub;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.timestampAdd;
import static org.jooq.impl.DSL.timestampDiff;
import static org.jooq.impl.DSL.val;
import static org.jooq.types.Unsigned.ubyte;
import static org.jooq.types.Unsigned.uint;
import static org.jooq.types.Unsigned.ulong;
import static org.jooq.types.Unsigned.ushort;
import static org.joox.JOOX.$;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.jooq.Converter;
import org.jooq.DSLContext;
import org.jooq.DataType;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.InsertSetMoreStep;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record6;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.XMLasDOMBinding;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.test.all.converters.Boolean_YES_NO_LC;
import org.jooq.test.all.converters.Boolean_YES_NO_UC;
import org.jooq.test.all.converters.LocalDateConverter;
import org.jooq.test.all.converters.LocalDateTimeConverter;
import org.jooq.test.all.pojos.jaxb.Author;
import org.jooq.test.all.pojos.jaxb.Book;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.Unsigned;
import org.jooq.types.YearToMonth;

import org.w3c.dom.Node;

public class DataTypeTests<
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

    public DataTypeTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testCharCasts() throws Exception {
        if (asList(ACCESS).contains(dialect().family())) {
            log.info("SKIPPING", "Char cast tests");
            return;
        }

        // [#1241] Casting to CHAR. Some dialects don't like that. They should
        // be casting to VARCHAR instead
        assertEquals("abc",
        create().select(field("cast('abc' as char(3))", SQLDataType.CHAR))
                .where(field("cast('abc' as char(3))", SQLDataType.CHAR).equal("abc"))
                .fetchOne(0, String.class));
    }

    public void testBlobAndClob() throws Exception {
        assumeFamilyNotIn(REDSHIFT);

        jOOQAbstractTest.reset = false;

        // Superficial tests in T_BOOK table
        // ---------------------------------
        DSLContext create = create();

        // Avoid excessive LOB logging
        create.configuration().settings().setExecuteLogging(false);

        B book = create.fetchOne(TBook(), TBook_TITLE().equal("1984"));

        assertTrue(book.getValue(TBook_CONTENT_TEXT()).contains("doublethink"));
        assertEquals(null, book.getValue(TBook_CONTENT_PDF()));

        String text = IntStream.range(0, 1000).mapToObj(i -> "Blah blah").collect(joining(", "));

        book.setValue(TBook_CONTENT_TEXT(), text);
        book.setValue(TBook_CONTENT_PDF(), text.getBytes());
        book.store();

        book = create.fetchOne(TBook(), TBook_TITLE().equal("1984"));

        assertEquals(text, book.getValue(TBook_CONTENT_TEXT()));
        assertEquals(text, new String(book.getValue(TBook_CONTENT_PDF())));

        // More in-depth tests in T_725_LOB_TEST table
        // -------------------------------------------
        T725 record = create.newRecord(T725());

        // Store and fetch NULL value
        record.setValue(T725_ID(), 1);
        assertEquals(1, record.store());
        record.refresh();
        assertNull(record.getValue(T725_LOB()));

        // Store and fetch empty byte[]. In some RDBMS, this is the same as null
        record.setValue(T725_LOB(), new byte[0]);
        assertEquals(1, record.store());
        record.refresh();

        switch (dialect().family()) {

            /* [pro] */
            // In ASE, there don't seem to be any empty byte[]
            case ASE:
                assertEquals(1, record.getValue(T725_LOB()).length);
                assertEquals(0, record.getValue(T725_LOB())[0]);
                break;

            // These don't make a difference between an empty byte[] and null
            case ORACLE:
            /* [/pro] */
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

        String query = "insert into "
            + T725().getName()
            + " ("
            + T725_ID().getName()
            + ", "
            + T725_LOB().getName()
            + ") values (?, ?)";
        assertEquals(1, create.query(query, 2, (Object) null).execute());
        assertEquals(1, create.query(query, 3, new byte[0]).execute());
        assertEquals(1, create.query(query, 4, "abc".getBytes()).execute());

        record.setValue(T725_ID(), 2);
        record.refresh();
        assertNull(record.getValue(T725_LOB()));

        record.setValue(T725_ID(), 3);
        record.refresh();

        switch (dialect().family()) {
            /* [pro] */
            case ASE:
                assertEquals(1, record.getValue(T725_LOB()).length);
                assertEquals(0, record.getValue(T725_LOB())[0]);
                break;

            case ORACLE:
            /* [/pro] */
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

        Result<Record> result = create.fetch(
            "select " + T725_ID().getName() + ", " + T725_LOB().getName() +
            " from " + T725().getName() +
            " order by " + T725_ID().getName());
        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(0));
        assertNull(result.getValue(1, 1));

        switch (dialect().family()) {
            /* [pro] */
            case ASE:
                assertEquals(1, result.getValue(2, T725_LOB()).length);
                assertEquals(0, result.getValue(2, T725_LOB())[0]);
                break;

            case ORACLE:
            /* [/pro] */
            case SQLITE:
                assertNull(result.getValue(2, T725_LOB()));
                break;

            default:
                assertEquals(0, result.getValue(2, T725_LOB()).length);
                break;
        }

        assertEquals("abc", new String((byte[]) result.getValue(3, T725_LOB().getName())));
    }

    public void testTypeConversions() throws Exception {
        Record record = create().fetchOne(TAuthor(), TAuthor_LAST_NAME().equal("Orwell"));

        assertEquals("George", record.getValue(TAuthor_FIRST_NAME()));
        assertEquals("George", record.getValue(TAuthor_FIRST_NAME(), String.class));
        assertEquals("George", record.getValue(1, String.class));

        assertEquals(Integer.valueOf("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH()));
        assertEquals(Integer.valueOf("1903"), record.getValue(4));

        assertEquals(Short.valueOf("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH(), Short.class));
        assertEquals(Short.valueOf("1903"), record.getValue(4, Short.class));

        assertEquals(Long.valueOf("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH(), Long.class));
        assertEquals(Long.valueOf("1903"), record.getValue(4, Long.class));

        assertEquals(new BigInteger("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH(), BigInteger.class));
        assertEquals(new BigInteger("1903"), record.getValue(4, BigInteger.class));

        assertEquals(Float.valueOf("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH(), Float.class));
        assertEquals(Float.valueOf("1903"), record.getValue(4, Float.class));

        assertEquals(Double.valueOf("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH(), Double.class));
        assertEquals(Double.valueOf("1903"), record.getValue(4, Double.class));

        assertEquals(new BigDecimal("1903"), record.getValue(TAuthor_YEAR_OF_BIRTH(), BigDecimal.class));
        assertEquals(new BigDecimal("1903"), record.getValue(4, BigDecimal.class));


        long dateOfBirth = record.getValue(TAuthor_DATE_OF_BIRTH()).getTime();
        assertEquals(dateOfBirth, record.getValue(TAuthor_DATE_OF_BIRTH(), Date.class).getTime());
        assertEquals(dateOfBirth, record.getValue(TAuthor_DATE_OF_BIRTH(), Timestamp.class).getTime());
        assertEquals(dateOfBirth, record.getValue(TAuthor_DATE_OF_BIRTH(), Time.class).getTime());
    }

    @SuppressWarnings("serial")
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
        Result<Record1<String>> result =
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

    public void testJava8TimeWithConverter() {
        Field<LocalDate> d = field(
            name(TDates().getName(), TDates_D().getName()),
            SQLDataType.DATE.asConvertedDataType(new LocalDateConverter())
        );

        Field<LocalDateTime> ts = field(
            name(TDates().getName(), TDates_TS().getName()),
            SQLDataType.TIMESTAMP.asConvertedDataType(new LocalDateTimeConverter())
        );

        testJava8Time0(d, ts);
    }

    public void testJava8TimeWithBinding() {

    }

    private void testJava8Time0(Field<LocalDate> d, Field<LocalDateTime> ts) {
        clean(TDates());

        assertEquals(1,
        create().insertInto(TDates())
                .columns(TDates_ID(), d, ts)
                .values(1, null, null)
                .execute());

        assertEquals(1,
        create().insertInto(TDates(), TDates_ID(), d, ts)
                .values(2, LocalDate.parse("2000-01-01"), LocalDateTime.parse("2000-01-01T00:01:02"))
                .execute());

        Result<?> result =
        create().select(TDates_ID(), d, ts)
                .from(TDates())
                .orderBy(TDates_ID())
                .fetch();

        assertNull(result.get(0).getValue(d));
        assertNull(result.get(0).getValue(ts));

        assertEquals(
            LocalDate.parse("2000-01-01"),
            result.get(1).getValue(d));
        assertEquals(
            LocalDateTime.parse("2000-01-01T00:01:02"),
            result.get(1).getValue(ts));
    }

    public void testCastingToDialectDataType() throws Exception {
        for (DataType<?> type : getCastableDataTypes()) {
            /* [pro] */
            if (dialect() == SQLDialect.ASE ||
                dialect() == SQLDialect.DB2 ||
                dialect() == SQLDialect.SYBASE) {
                if (type.getType() == Boolean.class) {
                    log.info("SKIPPING", "Casting to bit type in Sybase ASE / SQL Anywhere");
                    continue;
                }
            }

            /* [/pro] */
            assertEquals(null, create().select(val(null, type).cast(type)).fetchOne(0));
        }
    }

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

            /* [pro] */
            if (dialect().family() == SQLDialect.ORACLE) {
                if (type.getType() == byte[].class ||
                    type == SQLDataType.CLOB ||
                    type == SQLDataType.NCLOB) {

                    log.info("SKIPPING", "Casting to lob type in Oracle");
                    continue;
                }
            }

            if (dialect().family() == SQLDialect.ASE ||
                dialect().family() == SQLDialect.DB2 ||
                dialect().family() == SQLDialect.ORACLE ||
                dialect().family() == SQLDialect.SYBASE) {
                if (type.getType() == Boolean.class) {
                    log.info("SKIPPING", "Casting to bit type in Sybase ASE / SQL Anywhere");
                    continue;
                }
            }

            /* [/pro] */
            assertEquals(null, create().select(val(null, type).cast(type)).fetchOne(0));
        }
    }

    public void testCastingToJavaClass() throws Exception {
        if (dialect() != SQLDialect.HSQLDB) {
            assertEquals(true, create().select(cast(1, Boolean.class)).fetchOne(0));

            if (true/* [pro] */ && dialect() != SQLDialect.INGRES/* [/pro] */) {
                assertEquals(true, create().select(cast("1", Boolean.class)).fetchOne(0));
            }
        }

        assertEquals(BigInteger.ONE, create().select(cast("1", BigInteger.class)).fetchOne(0));
        assertEquals(BigInteger.ONE, create().select(cast(1, BigInteger.class)).fetchOne(0));

        /* [pro] */
        // Sybase applies the wrong scale when casting. Force scale before comparing (Sybase returns 1.0000 when we expect 1)
        if (dialect() == SQLDialect.SYBASE) {
            BigDecimal result = (BigDecimal)create().select(cast("1", BigDecimal.class)).fetchOne(0);
            result = result.setScale(0);
            assertEquals(BigDecimal.ONE, result);

            result = (BigDecimal)create().select(cast(1, BigDecimal.class)).fetchOne(0);
            result = result.setScale(0);
            assertEquals(BigDecimal.ONE, result);
        } else
        /* [/pro] */
        {
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
        if (true/* [pro] */ && dialect() != SQLDialect.ASE/* [/pro] */) {
            assertEquals(null, create().select(castNull(Boolean.class)).fetchOne(0));
        }

        assertEquals(null, create().select(castNull(Byte.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Short.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Integer.class)).fetchOne(0));
        assertEquals(null, create().select(castNull(Long.class)).fetchOne(0));

        // Not implemented by the driver
        if (dialect() != SQLDialect.SQLITE) {
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
        testConversionFetchArrayByIndex(
            Byte.class, Arrays.asList((byte) 1, (byte) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            Short.class, Arrays.asList((short) 1, (short) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            Integer.class, Arrays.asList(1, 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            Long.class, Arrays.asList(1L, 2L),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            UByte.class, Arrays.asList(ubyte((byte) 1), ubyte((byte) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            UShort.class, Arrays.asList(ushort((short) 1), ushort((short) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            UInteger.class, Arrays.asList(uint(1), uint(2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            ULong.class, Arrays.asList(ulong(1L), ulong(2L)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            Float.class, Arrays.asList(1.0f, 2.0f),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            Double.class, Arrays.asList(1.0, 2.0),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            BigInteger.class, Arrays.asList(new BigInteger("1"), new BigInteger("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByIndex(
            BigDecimal.class, Arrays.asList(new BigDecimal("1"), new BigDecimal("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));


        testConversionFetchArrayByName(
            Byte.class, Arrays.asList((byte) 1, (byte) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            Short.class, Arrays.asList((short) 1, (short) 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            Integer.class, Arrays.asList(1, 2),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            Long.class, Arrays.asList(1L, 2L),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            UByte.class, Arrays.asList(ubyte((byte) 1), ubyte((byte) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            UShort.class, Arrays.asList(ushort((short) 1), ushort((short) 2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            UInteger.class, Arrays.asList(uint(1), uint(2)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            ULong.class, Arrays.asList(ulong(1L), ulong(2L)),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            Float.class, Arrays.asList(1.0f, 2.0f),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            Double.class, Arrays.asList(1.0, 2.0),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            BigInteger.class, Arrays.asList(new BigInteger("1"), new BigInteger("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));
        testConversionFetchArrayByName(
            BigDecimal.class, Arrays.asList(new BigDecimal("1"), new BigDecimal("2")),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()));


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
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Float.class), 0.0f);
        assertEquals(
            1.0,
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(0, Double.class), 0.0);
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
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Float.class), 0.0f);
        assertEquals(
            1.0,
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), Double.class), 0.0);
        assertEquals(
            new BigInteger("1"),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), BigInteger.class));
        assertEquals(
            new BigDecimal("1"),
            create().selectFrom(TAuthor()).orderBy(TAuthor_ID()).limit(1).fetchOne(TAuthor_ID().getName(), BigDecimal.class));

    }

    private <Z> void testConversionFetchArrayByIndex(Class<Z> type, List<Z> expected, ResultQuery<?> select) {
        assertEquals(expected, Arrays.asList(select.fetchArray(0, type)));
        assertEquals(expected, Arrays.asList(select.fetch().intoArray(0, type)));
    }

    private <Z> void testConversionFetchArrayByName(Class<Z> type, List<Z> expected, ResultQuery<?> select) {
        assertEquals(expected, Arrays.asList(select.fetchArray(TAuthor_ID().getName(), type)));
        assertEquals(expected, Arrays.asList(select.fetch().intoArray(TAuthor_ID().getName(), type)));
    }

    public void testConversion() throws Exception {

        // Converting NULL
        // ---------------
        assertEquals(null, SQLDataType.TINYINT.convert((Object) null));
        assertEquals(null, SQLDataType.TINYINTUNSIGNED.convert((Object) null));
        assertEquals(null, SQLDataType.SMALLINT.convert((Object) null));
        assertEquals(null, SQLDataType.SMALLINTUNSIGNED.convert((Object) null));
        assertEquals(null, SQLDataType.INTEGER.convert((Object) null));
        assertEquals(null, SQLDataType.INTEGERUNSIGNED.convert((Object) null));
        assertEquals(null, SQLDataType.BIGINT.convert((Object) null));
        assertEquals(null, SQLDataType.BIGINTUNSIGNED.convert((Object) null));
        assertEquals(null, SQLDataType.REAL.convert((Object) null));
        assertEquals(null, SQLDataType.DOUBLE.convert((Object) null));
        assertEquals(null, SQLDataType.DECIMAL_INTEGER.convert((Object) null));
        assertEquals(null, SQLDataType.NUMERIC.convert((Object) null));
        assertEquals(null, SQLDataType.BOOLEAN.convert((Object) null));
        assertEquals(null, SQLDataType.VARCHAR.convert((Object) null));
        assertEquals(null, SQLDataType.DATE.convert((Object) null));
        assertEquals(null, SQLDataType.TIME.convert((Object) null));
        assertEquals(null, SQLDataType.TIMESTAMP.convert((Object) null));

        // Converting NULLs
        // ----------------
        List<Object> list = asList(null, null, null);
        assertEquals(list, asList(SQLDataType.TINYINT.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.TINYINTUNSIGNED.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.SMALLINT.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.SMALLINTUNSIGNED.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.INTEGER.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.INTEGERUNSIGNED.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.BIGINT.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.BIGINTUNSIGNED.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.REAL.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.DOUBLE.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.DECIMAL_INTEGER.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.NUMERIC.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.BOOLEAN.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.VARCHAR.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.DATE.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.TIME.convert(null, null, null)));
        assertEquals(list, asList(SQLDataType.TIMESTAMP.convert(null, null, null)));

        // Converting char
        // ---------------
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

        // Converting String
        // -----------------
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

        // Converting "any"
        // ----------------
        assertEquals(
            asList(Byte.valueOf("1"), Byte.valueOf("2"), Byte.valueOf("3")),
            asList(SQLDataType.TINYINT.convert('1', "2", 3)));
        assertEquals(
            asList(UByte.valueOf("1"), UByte.valueOf("2"), UByte.valueOf("3")),
            asList(SQLDataType.TINYINTUNSIGNED.convert('1', "2", 3)));
        assertEquals(
            asList(Short.valueOf("1"), Short.valueOf("2"), Short.valueOf("3")),
            asList(SQLDataType.SMALLINT.convert('1', "2", 3)));
        assertEquals(
            asList(UShort.valueOf("1"), UShort.valueOf("2"), UShort.valueOf("3")),
            asList(SQLDataType.SMALLINTUNSIGNED.convert('1', "2", 3)));
        assertEquals(
            asList(Integer.valueOf("1"), Integer.valueOf("2"), Integer.valueOf("3")),
            asList(SQLDataType.INTEGER.convert('1', "2", 3)));
        assertEquals(
            asList(UInteger.valueOf("1"), UInteger.valueOf("2"), UInteger.valueOf("3")),
            asList(SQLDataType.INTEGERUNSIGNED.convert('1', "2", 3)));
        assertEquals(
            asList(Long.valueOf("1"), Long.valueOf("2"), Long.valueOf("3")),
            asList(SQLDataType.BIGINT.convert('1', "2", 3)));
        assertEquals(
            asList(ULong.valueOf("1"), ULong.valueOf("2"), ULong.valueOf("3")),
            asList(SQLDataType.BIGINTUNSIGNED.convert('1', "2", 3)));
        assertEquals(
            asList(Float.valueOf("1"), Float.valueOf("2"), Float.valueOf("3")),
            asList(SQLDataType.REAL.convert('1', "2", 3)));
        assertEquals(
            asList(Double.valueOf("1"), Double.valueOf("2"), Double.valueOf("3")),
            asList(SQLDataType.DOUBLE.convert('1', "2", 3)));
        assertEquals(
            asList(new BigInteger("1"), new BigInteger("2"), new BigInteger("3")),
            asList(SQLDataType.DECIMAL_INTEGER.convert('1', "2", 3)));
        assertEquals(
            asList(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3")),
            asList(SQLDataType.NUMERIC.convert('1', "2", 3)));
        assertEquals(
            asList("1", "2", "3"),
            asList(SQLDataType.VARCHAR.convert('1', "2", 3)));

        // Converting byte
        // ---------------
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

        // Converting short
        // ----------------
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

        // Converting int
        // --------------
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

        // Converting long
        // ---------------
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

        // Converting float
        // ----------------
        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(1.1f));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(1.1f));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(1.1f));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(1.1f));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(1.1f));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(1.1f));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(1.1f));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(1.1f));
        assertEquals(Float.valueOf("1.1"), SQLDataType.REAL.convert(1.1f), 0.0001f);
        assertEquals(Double.valueOf("1.1"), SQLDataType.DOUBLE.convert(1.1f), 0.0001);
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(1.1f));
        assertEquals(new BigDecimal("1.1"), SQLDataType.NUMERIC.convert(1.1f));
        assertEquals(null, SQLDataType.BOOLEAN.convert(1.1f));
        assertEquals("1.1", SQLDataType.VARCHAR.convert(1.1f));

        // Converting double
        // -----------------
        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(1.1));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(1.1));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(1.1));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(1.1));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(1.1));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(1.1));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(1.1));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(1.1));
        assertEquals(Float.valueOf("1.1"), SQLDataType.REAL.convert(1.1), 0.0001f);
        assertEquals(Double.valueOf("1.1"), SQLDataType.DOUBLE.convert(1.1), 0.0001);
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(1.1));
        assertEquals(new BigDecimal("1.1"), SQLDataType.NUMERIC.convert(1.1));
        assertEquals(null, SQLDataType.BOOLEAN.convert(1.1));
        assertEquals("1.1", SQLDataType.VARCHAR.convert(1.1));

        // Converting BigInteger
        // ---------------------
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

        // Converting BigDecimal
        // ---------------------
        assertEquals(Byte.valueOf("1"), SQLDataType.TINYINT.convert(new BigDecimal("1.1")));
        assertEquals(UByte.valueOf("1"), SQLDataType.TINYINTUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Short.valueOf("1"), SQLDataType.SMALLINT.convert(new BigDecimal("1.1")));
        assertEquals(UShort.valueOf("1"), SQLDataType.SMALLINTUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Integer.valueOf("1"), SQLDataType.INTEGER.convert(new BigDecimal("1.1")));
        assertEquals(UInteger.valueOf("1"), SQLDataType.INTEGERUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Long.valueOf("1"), SQLDataType.BIGINT.convert(new BigDecimal("1.1")));
        assertEquals(ULong.valueOf("1"), SQLDataType.BIGINTUNSIGNED.convert(new BigDecimal("1.1")));
        assertEquals(Float.valueOf("1.1"), SQLDataType.REAL.convert(new BigDecimal("1.1")), 0.0001f);
        assertEquals(Double.valueOf("1.1"), SQLDataType.DOUBLE.convert(new BigDecimal("1.1")), 0.0001);
        assertEquals(new BigInteger("1"), SQLDataType.DECIMAL_INTEGER.convert(new BigDecimal("1.1")));
        assertEquals(new BigDecimal("1.1"), SQLDataType.NUMERIC.convert(new BigDecimal("1.1")));
        assertEquals(null, SQLDataType.BOOLEAN.convert(new BigDecimal("1.1")));
        assertEquals("1.1", SQLDataType.VARCHAR.convert(new BigDecimal("1.1")));

        // Date time conversions
        // ---------------------
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

        // [#1501] String parsing conversions for date time data types
        // -----------------------------------------------------------

        // Use toString() to avoid timezone mess
        assertEquals("1970-01-01", SQLDataType.DATE.convert("1970-01-01").toString());
        assertEquals("00:00:00", SQLDataType.TIME.convert("00:00:00").toString());
        assertEquals("1970-01-01 00:00:00.0", SQLDataType.TIMESTAMP.convert("1970-01-01 00:00:00").toString());

        // [#936] Primitive type conversion
        // --------------------------------
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
        assertEquals('1', (char) author2.getValue(TAuthor_ID(), Character.class));
        assertEquals('1', (char) author2.getValue(TAuthor_ID(), char.class));
        assertEquals('a', (char) author2.getValue(TAuthor_LAST_NAME(), Character.class));
        assertEquals('a', (char) author2.getValue(TAuthor_LAST_NAME(), char.class));

        // [#1448] Check conversion from String to Enum
        Record record =
        create().select(inline("YES"), inline("NO"))
                .fetchOne();

        assertEquals(Boolean_YES_NO_UC.YES, record.getValue(0, Boolean_YES_NO_UC.class));
        assertEquals(Boolean_YES_NO_UC.NO, record.getValue(1, Boolean_YES_NO_UC.class));

        assertNull(record.getValue(0, Boolean_YES_NO_LC.class));
        assertNull(record.getValue(1, Boolean_YES_NO_LC.class));
    }

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
                .values((UByte) null, null, null, null)
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

    public void testNumbers() throws Exception {
        jOOQAbstractTest.reset = false;

        // Insert some numbers
        // -------------------
        InsertSetMoreStep<T639> set =
        create().insertInto(T639())
                .set(T639_ID(), 1)
                .set(T639_BIG_DECIMAL(), new BigDecimal("1234.5670"))
                .set(T639_BYTE_DECIMAL(), (byte) 2)
                .set(T639_INTEGER(), 3)
                .set(T639_INTEGER_DECIMAL(), 4)
                .set(T639_LONG(), 5L)
                .set(T639_LONG_DECIMAL(), 6L)
                .set(T639_SHORT(), (short) 7)
                .set(T639_SHORT_DECIMAL(), (short) 8);

        if (T639_BIG_INTEGER() != null) set.set(T639_BIG_INTEGER(), new BigInteger("1234567890"));
        if (T639_BYTE() != null) set.set(T639_BYTE(), (byte) 9);
        if (T639_DOUBLE() != null) set.set(T639_DOUBLE(), 10.125);
        if (T639_FLOAT() != null) set.set(T639_FLOAT(), 11.375f);

        assertEquals(1, set.execute());

        T639 record = create().fetchOne(T639());
        assertEquals(1, (int) record.getValue(T639_ID()));
        // In some test databases, DECIMAL types are emulated using DOUBLE
        assertTrue(new BigDecimal("1234.567").compareTo(record.getValue(T639_BIG_DECIMAL()).movePointRight(3).setScale(0, RoundingMode.DOWN).movePointLeft(3)) == 0);
        assertEquals(2, (byte) record.getValue(T639_BYTE_DECIMAL()));
        assertEquals(3, (int) record.getValue(T639_INTEGER()));
        assertEquals(4, (int) record.getValue(T639_INTEGER_DECIMAL()));
        assertEquals(5L, (long) record.getValue(T639_LONG()));
        assertEquals(6L, (long) record.getValue(T639_LONG_DECIMAL()));
        assertEquals(7, (short) record.getValue(T639_SHORT()));
        assertEquals(8, (short) record.getValue(T639_SHORT_DECIMAL()));

        if (T639_BIG_INTEGER() != null) assertEquals(new BigInteger("1234567890"), record.getValue(T639_BIG_INTEGER()));
        if (T639_BYTE() != null) assertEquals(9, (byte) record.getValue(T639_BYTE()));
        if (T639_DOUBLE() != null) assertEquals(10.125, record.getValue(T639_DOUBLE()), 0.0);
        if (T639_FLOAT() != null) assertEquals(11.375f, record.getValue(T639_FLOAT()), 0.0f);

        // Various BigDecimal tests
        // ------------------------
        if (asList(ACCESS, SQLITE).contains(dialect().family())) {
            log.info("SKIPPING", "Advanced BigDecimal tests");
        }
        else {
            create().insertInto(T639(), T639_ID(), T639_BIG_DECIMAL())
                    .values(2, new BigDecimal("1234567890.67899"))
                    .values(3, new BigDecimal("9999999999.99999"))
                    .values(4, new BigDecimal("1.00001"))
                    .values(5, new BigDecimal("0.00001"))
                    .values(6, new BigDecimal("0.00001"))
                    .execute();

            Result<Record2<Integer, BigDecimal>> result =
            create().select(T639_ID(), T639_BIG_DECIMAL())
                    .from(T639())
                    .where(T639_ID().between(2, 6))
                    .orderBy(T639_ID())
                    .fetch();

            assertEquals(Arrays.asList(2, 3, 4, 5, 6), result.getValues(0));
            assertEquals(new BigDecimal("1234567890.67899"), result.getValue(0, 1));
            assertEquals(new BigDecimal("9999999999.99999"), result.getValue(1, 1));
            assertEquals(new BigDecimal("1.00001"), result.getValue(2, 1));
            assertEquals(new BigDecimal("0.00001"), result.getValue(3, 1));
            assertEquals(new BigDecimal("0.00001"), result.getValue(4, 1));
        }
    }

    public void testDateTime() throws Exception {
        Record record =
        create().select(
            val(Date.valueOf(zeroDate())).as("d"),
            val(Time.valueOf("00:00:00")).as("t"),
            val(Timestamp.valueOf(zeroTimestamp())).as("ts")
        ).fetchOne();

        // ... (except for SQLite)
        if (dialect() != SQLITE)
            assertEquals(Date.valueOf(zeroDate()), record.getValue("d"));

        assertEquals(Time.valueOf("00:00:00"), record.getValue("t"));
        assertEquals(Timestamp.valueOf(zeroTimestamp()), record.getValue("ts"));

        // Interval tests
        // --------------
        if (/* [pro] */
            dialect() == ACCESS ||
            dialect() == ASE ||
            dialect() == DB2 ||
            dialect() == INGRES || // [#1285] TODO: Fix this for Ingres
            dialect().family() == SQLSERVER ||
            dialect() == SYBASE ||
            /* [/pro] */
            dialect() == CUBRID ||
            dialect() == DERBY ||
            dialect() == FIREBIRD ||
            dialect() == H2 ||
            dialect() == HANA ||
            dialect() == MARIADB ||
            dialect() == MYSQL ||
            dialect() == SQLITE) {

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

    public void testDateTimeFractionalSeconds() throws Exception {
        Record record =
        create().select(
            val(Timestamp.valueOf("2015-02-02 15:30:45")).as("ts0"),
            val(Timestamp.valueOf("2015-02-02 15:30:45.0")).as("ts1"),
            val(Timestamp.valueOf("2015-02-02 15:30:45.100")).as("ts2"),
            val(Timestamp.valueOf("2015-02-02 15:30:45.123")).as("ts3")
        ).fetchOne();

        assertEquals(Timestamp.valueOf("2015-02-02 15:30:45.0"), record.getValue(0));
        assertEquals(Timestamp.valueOf("2015-02-02 15:30:45.0"), record.getValue(1));
        assertEquals(Timestamp.valueOf("2015-02-02 15:30:45.100"), record.getValue(2));
        assertEquals(Timestamp.valueOf("2015-02-02 15:30:45.123"), record.getValue(3));
    }

    public void testDateTimeArithmetic() throws Exception {

        /* [pro] */
        // [#1285] TODO: Fix this for INGRES
        if (dialect() == INGRES) {
            log.info("SKIPPING", "Date time arithmetic tests");
            return;
        }

        /* [/pro] */
        // [#1009] SQL DATE doesn't have a time zone. SQL TIMESTAMP does
        long tsShift = -3600000;

        // [#566] INTERVAL arithmetic: addition
        // ------------------------------------
        Record record =
        create().select(

            // Extra care needs to be taken with Postgres negative DAY TO SECOND
            // intervals. Postgres allows for having several signs in intervals
            val(new Date(0)).add(1).as("d1"),
            dateAdd(new Date(0), 1).as("d1a"),
            val(new Date(0)).add(-1).as("d2a"),
            val(new Date(0)).sub(1).as("d2b"),

            val(new Date(0)).add(new YearToMonth(1, 6)).as("d3"),
            dateAdd(new Date(0), new YearToMonth(1, 6)).as("d3a"),
            val(new Date(0)).add(new YearToMonth(1, 6).neg()).as("d4a"),
            val(new Date(0)).sub(new YearToMonth(1, 6)).as("d4b"),

            val(new Date(0)).add(new DayToSecond(2)).as("d5"),
            dateAdd(new Date(0), new DayToSecond(2)).as("d5a"),
            val(new Date(0)).add(new DayToSecond(2).neg()).as("d6a"),
            val(new Date(0)).sub(new DayToSecond(2)).as("d6b"),

            val(new Timestamp(0)).add(1).as("ts1"),
            timestampAdd(new Timestamp(0), 1).as("ts1a"),
            val(new Timestamp(0)).add(-1).as("ts2a"),
            val(new Timestamp(0)).sub(1).as("ts2b"),

            val(new Timestamp(0)).add(new YearToMonth(1, 6)).as("ts3"),
            timestampAdd(new Timestamp(0), new YearToMonth(1, 6)).as("ts3a"),
            val(new Timestamp(0)).add(new YearToMonth(1, 6).neg()).as("ts4a"),
            val(new Timestamp(0)).sub(new YearToMonth(1, 6)).as("ts4b"),

            val(new Timestamp(0)).add(new DayToSecond(2)).as("ts5"),
            timestampAdd(new Timestamp(0), new DayToSecond(2)).as("ts5a"),
            val(new Timestamp(0)).add(new DayToSecond(2).neg()).as("ts6a"),
            val(new Timestamp(0)).sub(new DayToSecond(2)).as("ts6b"),
            val(new Timestamp(0)).add(new DayToSecond(2, 6)).as("ts7"),
            val(new Timestamp(0)).add(new DayToSecond(2, 6).neg()).as("ts8a"),
            val(new Timestamp(0)).sub(new DayToSecond(2, 6)).as("ts8b"),

            // Dummy field for simpler testing
            inline("dummy")
        ).fetchOne();

        Calendar cal;

        cal = cal();
        cal.add(Calendar.DATE, 1);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d1"));
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d1a"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts1"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts1a"));

        cal = cal();
        cal.add(Calendar.DATE, -1);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d2a"));
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d2b"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts2a"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts2b"));

        cal = cal();
        cal.add(Calendar.MONTH, 18);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d3"));
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d3a"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts3"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts3a"));

        cal = cal();
        cal.add(Calendar.MONTH, -18);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d4a"));
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d4b"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts4a"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts4b"));

        cal = cal();
        cal.add(Calendar.DATE, 2);
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d5"));
        assertEquals(new Date(cal.getTimeInMillis()), record.getValue("d5a"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts5"));
        assertEquals(new Timestamp(cal.getTimeInMillis() - tsShift), record.getValue("ts5a"));

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
        record = create()
        .select(
            dateDiff(new Date(0), new Date(30 * 60 * 60 * 1000L)).as("d1"),
            dateDiff(new Date(30 * 60 * 60 * 1000L), new Date(0)).as("d2"),
            // TODO [#566] Make this work!
            // timeDiff(new Time(0), new Time(60 * 60 * 1000L)).as("t1"),
            // timeDiff(new Time(60 * 60 * 1000L), new Time(0)).as("t2"),
            timestampDiff(new Timestamp(0), new Timestamp(30 * 60 * 60 * 1000L)).as("ts1"),
            timestampDiff(new Timestamp(30 * 60 * 60 * 1000L), new Timestamp(0)).as("ts2"),

            // Dummy field for simpler testing
            inline("dummy")
        )
        // TODO [#2006] INTERVAL data types seem to be still quite "experimental"
        // their interoperability within jOOQ isn't well supported
        // .where(timestampDiff(Timestamp.valueOf("2012-12-21 15:30:00.0"), Timestamp.valueOf("2012-12-21 15:40:00.0")).eq(new DayToSecond(0, 0, 10, 0)))
        // .and(dateDiff(Date.valueOf("2012-12-21"), Date.valueOf("2012-12-23")).eq(2))
        .fetchOne();

        assertEquals(-1, record.getValue("d1"));
        assertEquals(1, record.getValue("d2"));
        //assertEquals(new DayToSecond(0, 1).neg(), record.getValue("t1"));
        //assertEquals(new DayToSecond(0, 1), record.getValue("t2"));
        assertEquals(new DayToSecond(1, 6).neg(), record.getValue("ts1"));
        assertEquals(new DayToSecond(1, 6), record.getValue("ts2"));
    }

    public void testFunctionsOnDates_DATE_DIFF_AND_DATE_ADD() throws Exception {

        // [#3824] Be sure that DATE types aren't converted to TIMESTAMP by any functions
        Record6<Integer, Integer, Integer, Integer, Integer, Integer> d1 = create()
        .select(
            dateDiff(new Date(0), dateAdd(new Date(0), 2, DatePart.DAY)).as("d1"),
            dateDiff(new Date(0), val(new Date(0)).add(2)).as("d2"),

            dateDiff(dateAdd(new Date(0), 2, DatePart.DAY), new Date(0)).as("d3"),
            dateDiff(val(new Date(0)).add(2), new Date(0)).as("d4"),

            dateDiff(dateSub(new Date(0), 2, DatePart.DAY), new Date(0)).as("d5"),
            dateDiff(val(new Date(0)).sub(2), new Date(0)).as("d6")
        )
        .fetchOne();

        assertEquals(-2, d1.value1());
        assertEquals(-2, d1.value2());
        assertEquals(2,  d1.value3());
        assertEquals(2,  d1.value4());
        assertEquals(-2, d1.value5());
        assertEquals(-2, d1.value6());

        // [#4160] Some edge cases may appear when the calculated date does "not exist"
        Record8<Date, Date, Date, Date, Timestamp, Timestamp, Timestamp, Timestamp> d2 = create()
        .select(
            dateAdd(Date.valueOf("2012-02-29"), 1, DatePart.YEAR),
            dateAdd(Date.valueOf("2015-01-31"), 1, DatePart.MONTH),
            dateAdd(Date.valueOf("2015-03-30"), -1, DatePart.MONTH),
            dateAdd(Date.valueOf("2015-03-31"), 1, DatePart.MONTH),

            timestampAdd(Timestamp.valueOf("2012-02-29 00:00:00"), 1, DatePart.YEAR),
            timestampAdd(Timestamp.valueOf("2015-01-31 00:00:00"), 1, DatePart.MONTH),
            timestampAdd(Timestamp.valueOf("2015-03-30 00:00:00"), -1, DatePart.MONTH),
            timestampAdd(Timestamp.valueOf("2015-03-31 00:00:00"), 1, DatePart.MONTH)
        )
        .fetchOne();

        assertEquals(Date.valueOf("2013-02-28"), d2.value1());
        assertEquals(Date.valueOf("2015-02-28"), d2.value2());
        assertEquals(Date.valueOf("2015-02-28"), d2.value3());
        assertEquals(Date.valueOf("2015-04-30"), d2.value4());
        assertEquals(Timestamp.valueOf("2013-02-28 00:00:00"), d2.value5());
        assertEquals(Timestamp.valueOf("2015-02-28 00:00:00"), d2.value6());
        assertEquals(Timestamp.valueOf("2015-02-28 00:00:00"), d2.value7());
        assertEquals(Timestamp.valueOf("2015-04-30 00:00:00"), d2.value8());
    }

    public void testFunctionsOnDates_DATE_ADD() throws Exception {
        Calendar cal;

        // Adding
        cal = cal();

        Record6<Timestamp, Timestamp, Timestamp, Timestamp, Timestamp, Timestamp> r1 = create().select(
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), 2, DatePart.YEAR)  .as("yy"),
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), 2, DatePart.MONTH) .as("mm"),
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), 2, DatePart.DAY)   .as("dd"),
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), 2, DatePart.HOUR)  .as("hh"),
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), 2, DatePart.MINUTE).as("mi"),
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), 2, DatePart.SECOND).as("ss")
        ).fetchOne();

        // This test fails for Vertica but the jOOQ-generated SQL is correct.
        // https://community.dev.hp.com/t5/Vertica-Forum/Date-time-arithmetic-bug/m-p/229329#U229329

        cal = cal(); cal.add(Calendar.YEAR       , 2); assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value1());
        cal = cal(); cal.add(Calendar.MONTH      , 2); assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value2());
        cal = cal(); cal.add(Calendar.DAY_OF_YEAR, 2); assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value3());
        cal = cal(); cal.add(Calendar.HOUR       , 2); assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value4());
        cal = cal(); cal.add(Calendar.MINUTE     , 2); assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value5());
        cal = cal(); cal.add(Calendar.SECOND     , 2); assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value6());

        // Subtracting
        cal = cal();

        Record6<Timestamp, Timestamp, Timestamp, Timestamp, Timestamp, Timestamp> r2 = create().select(
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), -2, DatePart.YEAR)  .as("yy"),
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), -2, DatePart.MONTH) .as("mm"),
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), -2, DatePart.DAY)   .as("dd"),
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), -2, DatePart.HOUR)  .as("hh"),
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), -2, DatePart.MINUTE).as("mi"),
            DSL.timestampAdd(new Timestamp(cal.getTimeInMillis()), -2, DatePart.SECOND).as("ss")
        ).fetchOne();

        cal = cal(); cal.add(Calendar.YEAR       , -2); assertEquals(new Timestamp(cal.getTimeInMillis()), r2.value1());
        cal = cal(); cal.add(Calendar.MONTH      , -2); assertEquals(new Timestamp(cal.getTimeInMillis()), r2.value2());
        cal = cal(); cal.add(Calendar.DAY_OF_YEAR, -2); assertEquals(new Timestamp(cal.getTimeInMillis()), r2.value3());
        cal = cal(); cal.add(Calendar.HOUR       , -2); assertEquals(new Timestamp(cal.getTimeInMillis()), r2.value4());
        cal = cal(); cal.add(Calendar.MINUTE     , -2); assertEquals(new Timestamp(cal.getTimeInMillis()), r2.value5());
        cal = cal(); cal.add(Calendar.SECOND     , -2); assertEquals(new Timestamp(cal.getTimeInMillis()), r2.value6());
    }

    private Calendar cal() {
        return cal(0);
    }

    private Calendar cal(long offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(-3600000 + offset);
        return cal;
    }

    public void testFunctionsOnDates_DATE_ADD_WithCast() throws Exception {

        // Adding
        Record6<Date, Date, Date, Timestamp, Timestamp, Timestamp> r1 = create().select(
                 DSL.dateAdd(Date.valueOf("2000-01-01"), 2, DatePart.YEAR)                    .as("d-yy"),
                 DSL.dateAdd(Date.valueOf("2000-01-01"), 2, DatePart.MONTH)                   .as("d-mm"),
                 DSL.dateAdd(Date.valueOf("2000-01-01"), 2, DatePart.DAY)                     .as("d-dd"),
            cast(DSL.dateAdd(Date.valueOf("2000-01-01"), 2, DatePart.YEAR)  , Timestamp.class).as("ts-yy"),
            cast(DSL.dateAdd(Date.valueOf("2000-01-01"), 2, DatePart.MONTH) , Timestamp.class).as("ts-mm"),
            cast(DSL.dateAdd(Date.valueOf("2000-01-01"), 2, DatePart.DAY)   , Timestamp.class).as("ts-dd")
        ).fetchOne();

        assertEquals(Date.valueOf("2002-01-01"), r1.value1());
        assertEquals(Date.valueOf("2000-03-01"), r1.value2());
        assertEquals(Date.valueOf("2000-01-03"), r1.value3());
        assertEquals(Timestamp.valueOf("2002-01-01 00:00:00"), r1.value4());
        assertEquals(Timestamp.valueOf("2000-03-01 00:00:00"), r1.value5());
        assertEquals(Timestamp.valueOf("2000-01-03 00:00:00"), r1.value6());

        // Subtracting
        Record6<Date, Date, Date, Timestamp, Timestamp, Timestamp> r2 = create().select(
                 DSL.dateAdd(Date.valueOf("2000-01-01"), -2, DatePart.YEAR)                    .as("d-yy"),
                 DSL.dateAdd(Date.valueOf("2000-01-01"), -2, DatePart.MONTH)                   .as("d-mm"),
                 DSL.dateAdd(Date.valueOf("2000-01-01"), -2, DatePart.DAY)                     .as("d-dd"),
            cast(DSL.dateAdd(Date.valueOf("2000-01-01"), -2, DatePart.YEAR)  , Timestamp.class).as("ts-yy"),
            cast(DSL.dateAdd(Date.valueOf("2000-01-01"), -2, DatePart.MONTH) , Timestamp.class).as("ts-mm"),
            cast(DSL.dateAdd(Date.valueOf("2000-01-01"), -2, DatePart.DAY)   , Timestamp.class).as("ts-dd")
        ).fetchOne();

        assertEquals(Date.valueOf("1998-01-01"), r2.value1());
        assertEquals(Date.valueOf("1999-11-01"), r2.value2());
        assertEquals(Date.valueOf("1999-12-30"), r2.value3());
        assertEquals(Timestamp.valueOf("1998-01-01 00:00:00"), r2.value4());
        assertEquals(Timestamp.valueOf("1999-11-01 00:00:00"), r2.value5());
        assertEquals(Timestamp.valueOf("1999-12-30 00:00:00"), r2.value6());
    }

    public void testFunctionsOnDates_TRUNC() throws Exception {
        switch (dialect().family()) {
            /* [pro] */
            case ACCESS:
            case ASE:
            case HANA:
            case INGRES:
            case SQLSERVER:
            case SYBASE:
            /* [/pro] */

            case DERBY:
            case FIREBIRD:
            case MARIADB:
            case MYSQL:
            case SQLITE:
                log.info("SKIPPING", "TRUNC(datetime) tests");
                return;
        }

        Calendar cal;

        cal = cal(-1);

        Record6<Timestamp, Timestamp, Timestamp, Timestamp, Timestamp, Timestamp> r1 = create().select(
            DSL.trunc(new Timestamp(cal.getTimeInMillis()), DatePart.YEAR)  .as("yy"),
            DSL.trunc(new Timestamp(cal.getTimeInMillis()), DatePart.MONTH) .as("mm"),
            DSL.trunc(new Timestamp(cal.getTimeInMillis()), DatePart.DAY)   .as("dd"),
            DSL.trunc(new Timestamp(cal.getTimeInMillis()), DatePart.HOUR)  .as("hh"),
            DSL.trunc(new Timestamp(cal.getTimeInMillis()), DatePart.MINUTE).as("mi"),
            DSL.trunc(new Timestamp(cal.getTimeInMillis()), DatePart.SECOND).as("ss")
        ).fetchOne();

        cal = cal(-1);
        cal.set(Calendar.MONTH       , 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY , 0);
        cal.set(Calendar.MINUTE      , 0);
        cal.set(Calendar.SECOND      , 0);
        cal.set(Calendar.MILLISECOND , 0);
        assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value1());

        cal = cal(-1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY , 0);
        cal.set(Calendar.MINUTE      , 0);
        cal.set(Calendar.SECOND      , 0);
        cal.set(Calendar.MILLISECOND , 0);
        assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value2());

        cal = cal(-1);
        cal.set(Calendar.HOUR_OF_DAY , 0);
        cal.set(Calendar.MINUTE      , 0);
        cal.set(Calendar.SECOND      , 0);
        cal.set(Calendar.MILLISECOND , 0);
        assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value3());

        cal = cal(-1);
        cal.set(Calendar.MINUTE      , 0);
        cal.set(Calendar.SECOND      , 0);
        cal.set(Calendar.MILLISECOND , 0);
        assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value4());

        cal = cal(-1);
        cal.set(Calendar.SECOND      , 0);
        cal.set(Calendar.MILLISECOND , 0);
        assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value5());

        cal = cal(-1);
        cal.set(Calendar.MILLISECOND , 0);
        assertEquals(new Timestamp(cal.getTimeInMillis()), r1.value6());
    }

    public void testCurrentDate() throws Exception {

        // Skip this test for Derby
        // https://issues.apache.org/jira/browse/DERBY-896
        switch (dialect().family()) {
            case DERBY:
                log.info("SKIPPING", "Unsupported cast in Derby");
                return;
        }

        Field<Date> d = currentDate().as("d");

        Record1<Date> record =
        create().select(d)
                .fetchOne();

        assertEquals(Date.valueOf(LocalDate.now()), record.value1());
    }

    public void testUUIDDataType() throws Exception {
        assumeNotNull(TExoticTypes());

        jOOQAbstractTest.reset = false;

        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        assertEquals(1,
        create().insertInto(TExoticTypes(),
                    TExoticTypes_ID(),
                    TExoticTypes_UU())
                .values(1, uuid1)
                .execute());
        UU uu1 = create().fetchOne(TExoticTypes());
        assertEquals(uuid1, uu1.getValue(TExoticTypes_UU()));

        assertEquals(1,
        create().update(TExoticTypes())
                .set(TExoticTypes_UU(), uuid2)
                .where(TExoticTypes_UU().eq(uuid1))
                .execute());

        UU uu2 = create().fetchOne(TExoticTypes());
        assertEquals(uuid2, uu2.getValue(TExoticTypes_UU()));
    }

    public void testUUIDArrayDataType() throws Exception {
        assumeNotNull(TArrays_STRING());

        // [#2278] Run the subsequent test twice, once with bind values and once
        // with inlined bind values
        for (DSLContext create : asList(
            create(),
            create(new Settings().withStatementType(STATIC_STATEMENT)))) {

            UUID uuid1 = UUID.randomUUID();
            UUID uuid2 = UUID.randomUUID();

            UUID[] array = new UUID[] { uuid1, uuid2 };
            Field<UUID[]> val = val(array).as("array");

            Record1<UUID[]> record = create
                .select(val)
                .fetchOne();

            assertEquals(UUID[].class, record.getValue(val).getClass());
            assertEquals(2, record.getValue(val).length);
            assertEquals(uuid1, record.getValue(val)[0]);
            assertEquals(uuid2, record.getValue(val)[1]);
        }
    }

    public void testXMLasJAXB() throws Exception {
        assumeNotNull(TExoticTypes());
        assumeNotNull(TExoticTypes_UNTYPED_XML_AS_JAXB());
        clean(TExoticTypes());

        assertEquals(1,
        create().insertInto(TExoticTypes(), TExoticTypes_ID(), TExoticTypes_UNTYPED_XML_AS_JAXB())
                .values(1, null)
                .execute());

        assertEquals(1,
        create().insertInto(TExoticTypes(), TExoticTypes_ID(), TExoticTypes_UNTYPED_XML_AS_JAXB())
                .values(2, new Book())
                .execute());

        assertEquals(1,
        create().insertInto(TExoticTypes(), TExoticTypes_ID(), TExoticTypes_UNTYPED_XML_AS_JAXB())
                .values(3, new Book("1984", new Author("George", "Orwell")))
                .execute());

        Result<UU> result =
        create().selectFrom(TExoticTypes())
                .orderBy(TExoticTypes_ID())
                .fetch();

        assertNull(result.get(0).getValue(TExoticTypes_UNTYPED_XML_AS_JAXB()));
        assertEquals(
            new Book(),
            result.get(1).getValue(TExoticTypes_UNTYPED_XML_AS_JAXB()));
        assertEquals(
            new Book("1984", new Author("George", "Orwell")),
            result.get(2).getValue(TExoticTypes_UNTYPED_XML_AS_JAXB()));
    }

    public void testXMLasDOM() throws Exception {
        assumeNotNull(TExoticTypes());
        assumeNotNull(TExoticTypes_UNTYPED_XML_AS_DOM());
        testXMLasDOM0(TExoticTypes(), TExoticTypes_ID(), TExoticTypes_UNTYPED_XML_AS_DOM());
    }

    public void testXMLusingPlainSQLConverters() {
        assumeNotNull(TExoticTypes());
        assumeNotNull(TExoticTypes_PLAIN_SQL_CONVERTER_XML());
        clean(TExoticTypes());

        XMLasDOMBinding binding = new XMLasDOMBinding();
        Converter<Object, Node> converter = binding.converter();

        Table<?> table = table(name(TExoticTypes().getName()));
        Field<Integer> id = field(name(table.getName(), TExoticTypes_ID().getName()), Integer.class);
        Field<Node> xml = field(
            name(table.getName(), TExoticTypes_PLAIN_SQL_CONVERTER_XML().getName()),
            SQLDataType.CLOB.asConvertedDataType(converter)
        );

        testXMLasDOM0(table, id, xml);
    }

    public void testXMLusingPlainSQLBindings() {
        assumeNotNull(TExoticTypes());
        assumeNotNull(TExoticTypes_PLAIN_SQL_CONVERTER_XML());
        clean(TExoticTypes());

        XMLasDOMBinding binding = new XMLasDOMBinding();

        Table<?> table = table(name(TExoticTypes().getName()));
        Field<Integer> id = field(name(table.getName(), TExoticTypes_ID().getName()), Integer.class);
        Field<Node> xml = field(
            name(table.getName(), TExoticTypes_PLAIN_SQL_CONVERTER_XML().getName()),
            SQLDataType.CLOB.asConvertedDataType(binding)
        );

        testXMLasDOM0(table, id, xml);
    }

    private void testXMLasDOM0(Table<?> table, Field<Integer> id, Field<Node> xml) {
        assumeNotNull(TExoticTypes());
        clean(TExoticTypes());

        assertEquals(1,
        create().insertInto(TExoticTypes(), id, xml)
                .values(1, null)
                .execute());

        assertEquals(1,
        create().insertInto(table, id, xml)
                .values(2, $("<empty/>").document())
                .execute());

        assertEquals(1,
        create().insertInto(table, id, xml)
                .values(3, $("<a><b/></a>").document())
                .execute());

        Result<?> result =
        create().select(id, xml)
                .from(table)
                .orderBy(id)
                .fetch();

        assertNull(result.get(0).getValue(xml));
        assertEquals(
            $("<empty/>").toString(),
            $(result.get(1).getValue(xml)).toString());
        assertEquals(
            $("<a><b/></a>").toString(),
            $(result.get(2).getValue(xml)).toString());
    }

    public void testCoercion() throws Exception {
        Field<Long> id1 = TBook_ID().coerce(Long.class);
        Field<String> id2 = TBook_ID().coerce(String.class);

        Result<Record4<Long, String, Short, String>> result =
        create().select(
                    id1.as("id1"),
                    id2.as("id2"),
                    val("1").coerce(Short.class),
                    val(2).coerce(String.class))
                .from(TBook())
                .where(id1.in(val(1).coerce(Long.class), val(2).coerce(Long.class)))
                .and(id2.in(val(1).coerce(String.class), val(2).coerce(String.class)))
                .orderBy(1, 2)
                .fetch();

        assertEquals(2, result.size());
        assertEquals(1L, result.getValue(0, 0));
        assertEquals(2L, result.getValue(1, 0));
        assertEquals("1", result.getValue(0, 1));
        assertEquals("2", result.getValue(1, 1));
        assertEquals((short) 1, result.getValue(0, 2));
        assertEquals((short) 1, result.getValue(1, 2));
        assertEquals("2", result.getValue(0, 3));
        assertEquals("2", result.getValue(1, 3));
    }
 }
