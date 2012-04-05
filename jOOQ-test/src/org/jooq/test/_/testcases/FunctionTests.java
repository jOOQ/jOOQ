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
import static junit.framework.Assert.assertTrue;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.impl.Factory.abs;
import static org.jooq.impl.Factory.acos;
import static org.jooq.impl.Factory.ascii;
import static org.jooq.impl.Factory.asin;
import static org.jooq.impl.Factory.atan;
import static org.jooq.impl.Factory.atan2;
import static org.jooq.impl.Factory.bitAnd;
import static org.jooq.impl.Factory.bitCount;
import static org.jooq.impl.Factory.bitLength;
import static org.jooq.impl.Factory.bitNand;
import static org.jooq.impl.Factory.bitNor;
import static org.jooq.impl.Factory.bitNot;
import static org.jooq.impl.Factory.bitOr;
import static org.jooq.impl.Factory.bitXNor;
import static org.jooq.impl.Factory.bitXor;
import static org.jooq.impl.Factory.castNull;
import static org.jooq.impl.Factory.ceil;
import static org.jooq.impl.Factory.charLength;
import static org.jooq.impl.Factory.coalesce;
import static org.jooq.impl.Factory.concat;
import static org.jooq.impl.Factory.cos;
import static org.jooq.impl.Factory.cosh;
import static org.jooq.impl.Factory.cot;
import static org.jooq.impl.Factory.coth;
import static org.jooq.impl.Factory.currentDate;
import static org.jooq.impl.Factory.currentTime;
import static org.jooq.impl.Factory.currentTimestamp;
import static org.jooq.impl.Factory.currentUser;
import static org.jooq.impl.Factory.day;
import static org.jooq.impl.Factory.decode;
import static org.jooq.impl.Factory.deg;
import static org.jooq.impl.Factory.exp;
import static org.jooq.impl.Factory.extract;
import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.floor;
import static org.jooq.impl.Factory.greatest;
import static org.jooq.impl.Factory.hour;
import static org.jooq.impl.Factory.least;
import static org.jooq.impl.Factory.length;
import static org.jooq.impl.Factory.ln;
import static org.jooq.impl.Factory.log;
import static org.jooq.impl.Factory.lower;
import static org.jooq.impl.Factory.lpad;
import static org.jooq.impl.Factory.ltrim;
import static org.jooq.impl.Factory.minute;
import static org.jooq.impl.Factory.month;
import static org.jooq.impl.Factory.nullif;
import static org.jooq.impl.Factory.nvl;
import static org.jooq.impl.Factory.nvl2;
import static org.jooq.impl.Factory.octetLength;
import static org.jooq.impl.Factory.position;
import static org.jooq.impl.Factory.power;
import static org.jooq.impl.Factory.rad;
import static org.jooq.impl.Factory.rand;
import static org.jooq.impl.Factory.repeat;
import static org.jooq.impl.Factory.replace;
import static org.jooq.impl.Factory.round;
import static org.jooq.impl.Factory.rpad;
import static org.jooq.impl.Factory.rtrim;
import static org.jooq.impl.Factory.second;
import static org.jooq.impl.Factory.shl;
import static org.jooq.impl.Factory.shr;
import static org.jooq.impl.Factory.sign;
import static org.jooq.impl.Factory.sin;
import static org.jooq.impl.Factory.sinh;
import static org.jooq.impl.Factory.sqrt;
import static org.jooq.impl.Factory.substring;
import static org.jooq.impl.Factory.tan;
import static org.jooq.impl.Factory.tanh;
import static org.jooq.impl.Factory.trim;
import static org.jooq.impl.Factory.upper;
import static org.jooq.impl.Factory.val;
import static org.jooq.impl.Factory.year;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;

import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.SQLDataType;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class FunctionTests<
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

    public FunctionTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testFunctionPosition() throws Exception {
        // SQLite does not have anything like the position function
        if (getDialect() == SQLDialect.SQLITE) {
            log.info("SKIPPING", "position function test");
            return;
        }

        SelectQuery q = create().selectQuery();
        q.addFrom(VLibrary());

        Field<Integer> position = position(VLibrary_AUTHOR(), "o").as("p");
        q.addSelect(VLibrary_AUTHOR());
        q.addSelect(position);

        // https://issues.apache.org/jira/browse/DERBY-5005
        q.addOrderBy(field(VLibrary_AUTHOR().getName()));

        q.execute();
        Record r1 = q.getResult().get(1); // George Orwell
        Record r2 = q.getResult().get(2); // Paulo Coelho

        assertEquals(Integer.valueOf(3), r1.getValue(position));
        assertEquals(Integer.valueOf(5), r2.getValue(position));

        // Implicit check on the rownum function in oracle dialect
        L library = create().fetchAny(VLibrary());
        assertTrue(library != null);
    }

    @Test
    public void testFunctionsLikeDecode() throws Exception {
        Field<String> sNull = castNull(String.class);
        Field<Integer> iNull = castNull(Integer.class);

        // ---------------------------------------------------------------------
        // NULLIF
        // ---------------------------------------------------------------------
        assertEquals("1", create().select(nullif("1", "2")).fetchOne(0));
        assertEquals(null, create().select(nullif("1", "1")).fetchOne(0));
        assertEquals("1", "" + create().select(nullif(1, 2)).fetchOne(0));
        assertEquals(null, create().select(nullif(1, 1)).fetchOne(0));

        // ---------------------------------------------------------------------
        // NVL
        // ---------------------------------------------------------------------
        assertEquals(null, create().select(nvl(sNull, sNull)).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(nvl(iNull, 1)).fetchOne(0));
        assertEquals("1", create().select(nvl(sNull, "1")).fetchOne(0));
        assertEquals(Integer.valueOf(2), create().select(nvl(2, 1)).fetchOne(0));
        assertEquals("2", create().select(nvl("2", "1")).fetchOne(0));

        // TODO [#831] Fix this for Sybase ASE
        if (getDialect() != SQLDialect.ASE) {
            assertTrue(("" + create()
                .select(nvl(TBook_CONTENT_TEXT(), "abc"))
                .from(TBook())
                .where(TBook_ID().equal(1)).fetchOne(0)).startsWith("To know and"));
            assertEquals("abc", create()
                .select(nvl(TBook_CONTENT_TEXT(), "abc"))
                .from(TBook())
                .where(TBook_ID().equal(2)).fetchOne(0));
        }

        // ---------------------------------------------------------------------
        // NVL2
        // ---------------------------------------------------------------------
        assertEquals(null, create().select(nvl2(sNull, sNull, sNull)).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(nvl2(iNull, 2, 1)).fetchOne(0));
        assertEquals("1", create().select(nvl2(sNull, "2", "1")).fetchOne(0));
        assertEquals(Integer.valueOf(2), create().select(nvl2(val(2), 2, 1)).fetchOne(0));
        assertEquals("2", create().select(nvl2(val("2"), "2", "1")).fetchOne(0));

        // TODO [#831] Fix this for Sybase ASE
        if (getDialect() != SQLDialect.ASE) {
            assertEquals("abc", create()
                .select(nvl2(TBook_CONTENT_TEXT(), "abc", "xyz"))
                .from(TBook())
                .where(TBook_ID().equal(1)).fetchOne(0));
            assertEquals("xyz", create()
                .select(nvl2(TBook_CONTENT_TEXT(), "abc", "xyz"))
                .from(TBook())
                .where(TBook_ID().equal(2)).fetchOne(0));
        }

        // ---------------------------------------------------------------------
        // COALESCE
        // ---------------------------------------------------------------------
        assertEquals(null, create().select(coalesce(sNull, sNull)).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(coalesce(iNull, val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(coalesce(iNull, iNull, val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(coalesce(iNull, iNull, iNull, val(1))).fetchOne(0));

        assertEquals("1", create().select(coalesce(sNull, val("1"))).fetchOne(0));
        assertEquals("1", create().select(coalesce(sNull, sNull, val("1"))).fetchOne(0));
        assertEquals("1", create().select(coalesce(sNull, sNull, sNull, val("1"))).fetchOne(0));

        assertEquals(Integer.valueOf(2), create().select(coalesce(2, 1)).fetchOne(0));
        assertEquals(Integer.valueOf(2), create().select(coalesce(2, 1, 1)).fetchOne(0));
        assertEquals(Integer.valueOf(2), create().select(coalesce(2, 1, 1, 1)).fetchOne(0));

        assertEquals("2", create().select(coalesce("2", "1")).fetchOne(0));
        assertEquals("2", create().select(coalesce("2", "1", "1")).fetchOne(0));
        assertEquals("2", create().select(coalesce("2", "1", "1", "1")).fetchOne(0));

        assertTrue(("" + create()
            .select(coalesce(TBook_CONTENT_TEXT().cast(String.class), sNull, val("abc")))
            .from(TBook())
            .where(TBook_ID().equal(1)).fetchOne(0)).startsWith("To know and"));
        assertEquals("abc", create()
            .select(coalesce(TBook_CONTENT_TEXT().cast(String.class), sNull, val("abc")))
            .from(TBook())
            .where(TBook_ID().equal(2)).fetchOne(0));

        // ---------------------------------------------------------------------
        // DECODE
        // ---------------------------------------------------------------------
        assertEquals(null, create().select(decode(sNull, sNull, sNull)).fetchOne(0));
        assertEquals(null, create().select(decode(iNull, val(2), val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(decode(iNull, val(2), val(1), val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(decode(iNull, iNull, val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(decode(iNull, iNull, val(1), val(2))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(decode(iNull, val(2), val(2), iNull, val(1))).fetchOne(0));
        assertEquals(Integer.valueOf(1), create().select(decode(iNull, val(2), val(2), iNull, val(1), val(3))).fetchOne(0));

        assertEquals(null, create().select(decode(sNull, "2", "1")).fetchOne(0));
        assertEquals("1", create().select(decode(sNull, "2", "1", "1")).fetchOne(0));
        assertEquals("1", create().select(decode(sNull, sNull, val("1"))).fetchOne(0));
        assertEquals("1", create().select(decode(sNull, sNull, val("1"), val("2"))).fetchOne(0));
        assertEquals("1", create().select(decode(sNull, val("2"), val("2"), sNull, val("1"))).fetchOne(0));
        assertEquals("1", create().select(decode(sNull, val("2"), val("2"), sNull, val("1"), val("3"))).fetchOne(0));

        Field<Integer> lang = TBook_LANGUAGE_ID().cast(Integer.class);
        Result<Record> result = create().select(
                decode(lang, 1, "EN"),
                decode(lang, 1, "EN", "Other"),
                decode(lang, 1, "EN", 2, "DE"),
                decode(lang, 1, "EN", 2, "DE", "Other"))
            .from(TBook())
            .orderBy(TBook_ID()).fetch();

        assertEquals("EN", result.getValue(0, 0));
        assertEquals("EN", result.getValue(1, 0));
        assertEquals(null, result.getValue(2, 0));
        assertEquals(null, result.getValue(3, 0));

        assertEquals("EN", result.getValue(0, 1));
        assertEquals("EN", result.getValue(1, 1));
        assertEquals("Other", result.getValue(2, 1));
        assertEquals("Other", result.getValue(3, 1));

        assertEquals("EN", result.getValue(0, 2));
        assertEquals("EN", result.getValue(1, 2));
        assertEquals(null, result.getValue(2, 2));
        assertEquals("DE", result.getValue(3, 2));

        assertEquals("EN", result.getValue(0, 3));
        assertEquals("EN", result.getValue(1, 3));
        assertEquals("Other", result.getValue(2, 3));
        assertEquals("DE", result.getValue(3, 3));
    }

    @Test
    public void testCaseStatement() throws Exception {
        Field<String> case1 = decode()
            .value(TBook_PUBLISHED_IN())
            .when(0, "ancient book")
            .as("case1");

        // Ingres does not allow sub selects in CASE expressions
        Field<?> case2 = getDialect() == SQLDialect.INGRES
            ? decode()
                .value(TBook_AUTHOR_ID())
                .when(1, "Orwell")
                .otherwise("unknown")
            : decode()
                .value(TBook_AUTHOR_ID())
                .when(1, create().select(TAuthor_LAST_NAME())
                    .from(TAuthor())
                    .where(TAuthor_ID().equal(TBook_AUTHOR_ID())).asField())
                .otherwise("unknown");

        Field<?> case3 = decode()
            .value(1)
            .when(1, "A")
            .when(2, "B")
            .otherwise("C");

        SelectQuery query = create().selectQuery();
        query.addSelect(case1, case2, case3);
        query.addFrom(TBook());
        query.addOrderBy(TBook_PUBLISHED_IN());
        query.execute();

        Result<Record> result = query.getResult();
        assertEquals(null, result.getValue(0, case1));
        assertEquals(null, result.getValue(1, case1));
        assertEquals(null, result.getValue(2, case1));
        assertEquals(null, result.getValue(3, case1));

        assertEquals("Orwell", result.getValue(0, case2));
        assertEquals("Orwell", result.getValue(1, case2));
        assertEquals("unknown", result.getValue(2, case2));
        assertEquals("unknown", result.getValue(3, case2));

        assertEquals("A", result.getValue(0, case3));
        assertEquals("A", result.getValue(1, case3));
        assertEquals("A", result.getValue(2, case3));
        assertEquals("A", result.getValue(3, case3));

        Field<String> case4 = decode()
            .when(TBook_PUBLISHED_IN().equal(1948), "probably orwell")
            .when(TBook_PUBLISHED_IN().equal(1988), "probably coelho")
            .otherwise("don't know").as("case3");

        query = create().selectQuery();
        query.addSelect(case4);
        query.addFrom(TBook());
        query.addOrderBy(TBook_PUBLISHED_IN());
        query.execute();

        result = query.getResult();

        // Note: trims are necessary, as certain databases use
        // CHAR datatype here, not VARCHAR
        assertEquals("don't know", result.getValue(0, case4).trim());
        assertEquals("probably orwell", result.getValue(1, case4).trim());
        assertEquals("probably coelho", result.getValue(2, case4).trim());
        assertEquals("don't know", result.getValue(3, case4).trim());
    }

    @Test
    public void testFunctionsOnStrings() throws Exception {

        // [#1241] Casting to CHAR. Some dialects don't like that. They should
        // be casting to VARCHAR instead
        assertEquals("abc",
        create().select(field("cast('abc' as char(3))", SQLDataType.CHAR))
                .where(field("cast('abc' as char(3))", SQLDataType.CHAR).equal("abc"))
                .fetchOne(0, String.class));

        // Trimming
        assertEquals("abc", create().select(trim("abc")).fetchOne(0));
        assertEquals("abc", create().select(trim("abc  ")).fetchOne(0));
        assertEquals("abc", create().select(trim("  abc")).fetchOne(0));
        assertEquals("abc", create().select(trim("  abc  ")).fetchOne(0));
        assertEquals("  abc", create().select(rtrim("  abc  ")).fetchOne(0));
        assertEquals("abc  ", create().select(ltrim("  abc  ")).fetchOne(0));

        // Lower / Upper
        assertEquals("abc", create().select(lower("ABC")).fetchOne(0));
        assertEquals("ABC", create().select(upper("abc")).fetchOne(0));

        // String concatenation
        assertEquals("abc", create().select(concat("a", "b", "c")).fetchOne(0));
        assertEquals("George Orwell", create()
            .select(concat(TAuthor_FIRST_NAME(), val(" "), TAuthor_LAST_NAME()))
            .from(TAuthor())
            .where(TAuthor_FIRST_NAME().equal("George")).fetchOne(0));

        assertEquals("1ab45", create().select(concat(val(1), val("ab"), val(45))).fetchOne(0));

        // Standard String functions
        SelectQuery q = create().selectQuery();
        Field<String> constant = val("abc");

        switch (getDialect()) {

            // DERBY does not have a replace function
            case DERBY:
                log.info("SKIPPING", "replace function test");
                break;

            // These two tests will validate #154
            default: {
                Field<String> x = replace(constant, "b", "x");
                Field<String> y = replace(constant, "b", "y");
                Field<String> z = replace(constant, "b");
                Record record = create().select(x, y, z).fetchOne();

                assertEquals("axc", record.getValue(x));
                assertEquals("ayc", record.getValue(y));
                assertEquals("ac", record.getValue(z));
            }
        }

        Field<Integer> length = length(constant);
        Field<Integer> charLength = charLength(constant);
        Field<Integer> bitLength = bitLength(constant);
        Field<Integer> octetLength = octetLength(constant);
        q.addSelect(length, charLength, bitLength, octetLength);
        q.execute();

        Record record = q.getResult().get(0);

        assertEquals(Integer.valueOf(3), record.getValue(length));
        assertEquals(Integer.valueOf(3), record.getValue(charLength));

        switch (getDialect()) {
            case HSQLDB:
            case H2:
                // HSQLDB and H2 uses Java-style characters (16 bit)
                assertEquals(Integer.valueOf(48), record.getValue(bitLength));
                assertEquals(Integer.valueOf(6), record.getValue(octetLength));
                break;

            default:
                assertEquals(Integer.valueOf(24), record.getValue(bitLength));
                assertEquals(Integer.valueOf(3), record.getValue(octetLength));
                break;
        }

        // RPAD, LPAD
        switch (getDialect()) {
            case DERBY:
            case SQLITE:
                log.info("SKIPPING", "RPAD and LPAD functions");
                break;

            default: {
                Record result = create().select(
                    rpad(val("aa"), 4),
                    rpad(val("aa"), 4, "-"),
                    lpad(val("aa"), 4),
                    lpad(val("aa"), 4, "-")).fetchOne();

                assertEquals("aa  ", result.getValue(0));
                assertEquals("aa--", result.getValue(1));
                assertEquals("  aa", result.getValue(2));
                assertEquals("--aa", result.getValue(3));

                break;
            }
        }

        // SUBSTRING
        Record result = create().select(
            substring(val("abcde"), 1),
            substring(val("abcde"), 1, 2),
            substring(val("abcde"), 3),
            substring(val("abcde"), 3, 2)).fetchOne();

        assertEquals("abcde", result.getValue(0));
        assertEquals("ab", result.getValue(1));
        assertEquals("cde", result.getValue(2));
        assertEquals("cd", result.getValue(3));

        result =
        create().select(
                    substring(TAuthor_FIRST_NAME(), 2),
                    substring(TAuthor_FIRST_NAME(), 2, 2))
                .from(TAuthor())
                .where(TAuthor_ID().equal(1))
                .fetchOne();

        assertEquals("eorge", result.getValue(substring(TAuthor_FIRST_NAME(), 2)));
        assertEquals("eo", result.getValue(substring(TAuthor_FIRST_NAME(), 2, 2)));

        // REPEAT
        switch (getDialect()) {
            case DERBY:
            case SQLITE:
                log.info("SKIPPING", "REPEAT function");
                break;

            default: {
                result = create().select(
                    repeat("a", 1),
                    repeat("ab", 2),
                    repeat("abc", 3)).fetchOne();
                assertEquals("a", result.getValue(0));
                assertEquals("abab", result.getValue(1));
                assertEquals("abcabcabc", result.getValue(2));
                break;
            }
        }

        // ASCII
        switch (getDialect()) {
            case DERBY:
            case INGRES: // TODO [#864]
            case SQLITE: // TODO [#862]
                log.info("SKIPPING", "ASCII function test");
                break;

            default:
                record =
                create().select(
                    ascii("A"),
                    ascii("a"),
                    ascii("-"),
                    ascii(" ")).fetchOne();
                assertEquals((int) 'A', (int) record.getValueAsInteger(0));
                assertEquals((int) 'a', (int) record.getValueAsInteger(1));
                assertEquals((int) '-', (int) record.getValueAsInteger(2));
                assertEquals((int) ' ', (int) record.getValueAsInteger(3));

                break;
        }
    }

    @Test
    public void testFunctionsOnNumbers() throws Exception {

        // The random function
        BigDecimal rand = create().select(rand()).fetchOne(rand());
        assertNotNull(rand);

        // Some rounding functions
        Field<Float> f1a = round(1.111f);
        Field<Float> f2a = round(1.111f, 2);
        Field<Float> f3a = floor(1.111f);
        Field<Float> f4a = ceil(1.111f);
        Field<Double> f1b = round(-1.111);
        Field<Double> f2b = round(-1.111, 2);
        Field<Double> f3b = floor(-1.111);
        Field<Double> f4b = ceil(-1.111);

        Field<Float> f1c = round(2.0f);
        Field<Float> f2c = round(2.0f, 2);
        Field<Float> f3c = floor(2.0f);
        Field<Float> f4c = ceil(2.0f);
        Field<Double> f1d = round(-2.0);
        Field<Double> f2d = round(-2.0, 2);
        Field<Double> f3d = floor(-2.0);
        Field<Double> f4d = ceil(-2.0);

        // Some arbitrary checks on having multiple select clauses
        Record record =
        create().select(f1a)
                .select(f2a, f3a)
                .select(f4a)
                .select(f1b, f2b, f3b, f4b)
                .select(f1c, f2c, f3c, f4c)
                .select(f1d, f2d, f3d, f4d).fetchOne();

        assertNotNull(record);
        assertEquals("1.0", record.getValueAsString(f1a));
        assertEquals("1.11", record.getValueAsString(f2a));
        assertEquals("1.0", record.getValueAsString(f3a));
        assertEquals("2.0", record.getValueAsString(f4a));

        assertEquals("-1.0", record.getValueAsString(f1b));
        assertEquals("-1.11", record.getValueAsString(f2b));
        assertEquals("-2.0", record.getValueAsString(f3b));
        assertEquals("-1.0", record.getValueAsString(f4b));

        assertEquals("2.0", record.getValueAsString(f1c));
        assertEquals("2.0", record.getValueAsString(f2c));
        assertEquals("2.0", record.getValueAsString(f3c));
        assertEquals("2.0", record.getValueAsString(f4c));

        assertEquals("-2.0", record.getValueAsString(f1d));
        assertEquals("-2.0", record.getValueAsString(f2d));
        assertEquals("-2.0", record.getValueAsString(f3d));
        assertEquals("-2.0", record.getValueAsString(f4d));

        // Greatest and least
        record = create().select(
            greatest(1, 2, 3, 4),
            least(1, 2, 3),
            greatest("1", "2", "3", "4"),
            least("1", "2", "3")).fetchOne();

        assertEquals(Integer.valueOf(4), record.getValue(0));
        assertEquals(Integer.valueOf(1), record.getValue(1));
        assertEquals("4", record.getValue(2));
        assertEquals("1", record.getValue(3));

        // Greatest and least with tables. If they're simulated using subqueries
        // there is a risk of breaking this functionality due to limited support
        // for subqueries and derived tables...
        Result<Record> result = create()
            .select(TBook_ID(),
                    greatest(TBook_ID(),
                        TBook_AUTHOR_ID(),
                        TBook_LANGUAGE_ID()),
                    least(TBook_ID(),
                        TBook_AUTHOR_ID(),
                        TBook_LANGUAGE_ID()))
            .from(TBook())
            .orderBy(TBook_ID())
            .fetch();

        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues(TBook_ID()));
        assertEquals(asList(1, 2, 4, 4), result.getValues(1));
        assertEquals(asList(1, 1, 2, 2), result.getValues(2));

        // Mathematical functions
        switch (getDialect()) {
            case SQLITE:
                log.info("SKIPPING", "Tests for mathematical functions");
                break;

            default: {
                // Exponentials, logarithms and roots
                // ----------------------------------
                Field<BigDecimal> m1 = sqrt(2);
                Field<BigDecimal> m2 = round(sqrt(4));
                Field<BigDecimal> m3 = exp(2);
                Field<BigDecimal> m4 = round(exp(0));
                Field<BigDecimal> m5 = exp(-2);
                Field<BigDecimal> m6 = ln(2);
                Field<BigDecimal> m7 = round(log(16, 4));
                Field<BigDecimal> m8 = round(power(2, 4));
                Field<BigDecimal> m9 = round(power(sqrt(power(sqrt(2), 2)), 2));

                record = create().select(m1, m2, m3, m4, m5, m6, m7, m8, m9).fetchOne();

                // Rounding issues are circumvented by using substring()
                assertNotNull(record);
                assertEquals("1.414", record.getValueAsString(m1).substring(0, 5));
                assertEquals("2", record.getValueAsString(m2).substring(0, 1));
                assertEquals("7.389", record.getValueAsString(m3).substring(0, 5));
                assertEquals("1", record.getValueAsString(m4).substring(0, 1));
                assertEquals("0.135", record.getValueAsString(m5).substring(0, 5));
                assertEquals("0.693", record.getValueAsString(m6).substring(0, 5));
                assertEquals("2", record.getValueAsString(m7).substring(0, 1));
                assertEquals("16", record.getValueAsString(m8).substring(0, 2));
                assertEquals("2", record.getValueAsString(m9).substring(0, 1));

                // Trigonometry
                // ------------
                Field<BigDecimal> t1 = sin(Math.PI / 6 + 0.00001);
                Field<BigDecimal> t2 = cos(Math.PI / 6);
                Field<BigDecimal> t3 = tan(Math.PI / 6);
                Field<BigDecimal> t4 = cot(Math.PI / 6);
                Field<BigDecimal> t6 = rad(deg(1.1));
                Field<BigDecimal> t7 = asin(Math.PI / 6);
                Field<BigDecimal> t8 = acos(Math.PI / 6);
                Field<BigDecimal> t9 = atan(Math.PI / 6);
                Field<BigDecimal> ta = round(deg(atan2(1, 1)));

                // Hyperbolic functions
                // --------------------
                Field<BigDecimal> tb = sinh(1.0)
                    .div(cosh(1.0))
                    .mul(tanh(1.0))
                    .mul(power(coth(1.0), 2).add(0.1));

                record = create().select(t1, t2, t3, t4, t6, t7, t8, t9, ta, tb).fetchOne();

                // Rounding issues are circumvented by using substring()
                assertNotNull(record);
                assertEquals("0.5", record.getValueAsString(t1).substring(0, 3));
                assertEquals("0.866", record.getValueAsString(t2).substring(0, 5));
                assertEquals("0.577", record.getValueAsString(t3).substring(0, 5));
                assertEquals("1.732", record.getValueAsString(t4).substring(0, 5));
                assertEquals("1", record.getValueAsString(t6).substring(0, 1));
                assertEquals("0.551", record.getValueAsString(t7).substring(0, 5));
                assertEquals("1.019", record.getValueAsString(t8).substring(0, 5));
                assertEquals("0.482", record.getValueAsString(t9).substring(0, 5));
                assertEquals("45", record.getValueAsString(ta).substring(0, 2));
                assertEquals("1", record.getValueAsString(tb).substring(0, 1));

                break;
            }
        }

        // The sign function
        record = create().select(
            sign(2),
            sign(1),
            sign(0),
            sign(-1),
            sign(-2)).fetchOne();

        assertNotNull(record);
        assertEquals(Integer.valueOf(1), record.getValue(0));
        assertEquals(Integer.valueOf(1), record.getValue(1));
        assertEquals(Integer.valueOf(0), record.getValue(2));
        assertEquals(Integer.valueOf(-1), record.getValue(3));
        assertEquals(Integer.valueOf(-1), record.getValue(4));

        // The abs function
        record = create().select(
            abs(2),
            abs(1),
            abs(0),
            abs(-1),
            abs(-2)).fetchOne();

        assertNotNull(record);
        assertEquals(Integer.valueOf(2), record.getValue(0));
        assertEquals(Integer.valueOf(1), record.getValue(1));
        assertEquals(Integer.valueOf(0), record.getValue(2));
        assertEquals(Integer.valueOf(1), record.getValue(3));
        assertEquals(Integer.valueOf(2), record.getValue(4));
    }

    @Test
    public void testFunctionsOnDates() throws Exception {

        // Some checks on current_timestamp functions
        // ------------------------------------------
        SelectQuery q1 = create().selectQuery();
        Field<Timestamp> now = currentTimestamp();
        Field<Timestamp> ts = now.as("ts");
        Field<Date> date = currentDate().as("d");
        Field<Time> time = currentTime().as("t");

        // ... and the extract function
        // ----------------------------
        Field<Integer> year1 = extract(now, DatePart.YEAR).as("y1");
        Field<Integer> month1 = extract(now, DatePart.MONTH).as("m1");
        Field<Integer> day1 = extract(now, DatePart.DAY).as("dd1");
        Field<Integer> hour1 = extract(now, DatePart.HOUR).as("h1");
        Field<Integer> minute1 = extract(now, DatePart.MINUTE).as("mn1");
        Field<Integer> second1 = extract(now, DatePart.SECOND).as("sec1");

        Field<Integer> year2 = year(now).as("y2");
        Field<Integer> month2 = month(now).as("m2");
        Field<Integer> day2 = day(now).as("dd2");
        Field<Integer> hour2 = hour(now).as("h2");
        Field<Integer> minute2 = minute(now).as("mn2");
        Field<Integer> second2 = second(now).as("sec2");

        q1.addSelect(ts, date, time,
            year1, month1, day1, hour1, minute1, second1,
            year2, month2, day2, hour2, minute2, second2);
        q1.execute();

        Record record = q1.getResult().get(0);
        String timestamp = record.getValue(ts).toString().replaceFirst("\\.\\d+$", "");

        assertEquals(timestamp.split(" ")[0], record.getValue(date).toString());

        // Weird behaviour in postgres
        // See also interesting thread:
        // http://archives.postgresql.org/pgsql-jdbc/2010-09/msg00037.php
        if (getDialect() != SQLDialect.POSTGRES) {
            assertEquals(timestamp.split(" ")[1], record.getValue(time).toString());
        }

        assertEquals(Integer.valueOf(timestamp.split(" ")[0].split("-")[0]), record.getValue(year1));
        assertEquals(Integer.valueOf(timestamp.split(" ")[0].split("-")[1]), record.getValue(month1));
        assertEquals(Integer.valueOf(timestamp.split(" ")[0].split("-")[2]), record.getValue(day1));
        assertEquals(Integer.valueOf(timestamp.split(" ")[1].split(":")[0]), record.getValue(hour1));
        assertEquals(Integer.valueOf(timestamp.split(" ")[1].split(":")[1]), record.getValue(minute1));
        assertEquals(Integer.valueOf(timestamp.split(" ")[1].split(":")[2].split("\\.")[0]), record.getValue(second1));

        assertEquals(record.getValue(year1), record.getValue(year2));
        assertEquals(record.getValue(month1), record.getValue(month2));
        assertEquals(record.getValue(day1), record.getValue(day2));
        assertEquals(record.getValue(hour1), record.getValue(hour2));
        assertEquals(record.getValue(minute1), record.getValue(minute2));
        assertEquals(record.getValue(second1), record.getValue(second2));

        // Timestamp arithmetic
        // --------------------
        Field<Timestamp> tomorrow = now.add(1);
        Field<Timestamp> yesterday = now.sub(1);
        record = create().select(tomorrow, ts, yesterday).fetchOne();

        // Be sure this test doesn't fail when we switch from CET to CEST :-)
        Calendar cal = Calendar.getInstance();
        long tNow = cal.getTimeInMillis();
        cal.add(Calendar.DATE, 1);
        long tTomorrow = cal.getTimeInMillis();
        cal.add(Calendar.DATE, -2);
        long tYesterday = cal.getTimeInMillis();

        // Ingres truncates milliseconds. Ignore this fact
        assertEquals((tNow - tYesterday) / 1000,
            (record.getValue(ts).getTime() / 1000 - record.getValue(yesterday).getTime() / 1000));
        assertEquals((tTomorrow - tNow) / 1000,
            (record.getValue(tomorrow).getTime() / 1000 - record.getValue(ts).getTime() / 1000));
    }

    @Test
    public void testExtractInSubselect() throws Exception {
        Field<Timestamp> now = currentTimestamp();

        Field<Integer> year = extract(now, DatePart.YEAR).as("y");
        Field<Integer> month = extract(now, DatePart.MONTH).as("m");
        Field<Integer> day = extract(now, DatePart.DAY).as("d");

        Select<?> sub = create().select(year, month, day);
        Table<?> subTable = sub.asTable("subselect");

        Record reference = sub.fetchOne();
        Record result;

        result = create().select().from(sub).fetchOne();
        assertEquals(reference, result);

        result = create().select().from(subTable).fetchOne();
        assertEquals(reference, result);

        result = create().select(
            sub.getField("y"),
            sub.getField("m"),
            sub.getField("d")).from(sub).fetchOne();
        assertEquals(reference, result);

        result = create().select(
            subTable.getField("y"),
            subTable.getField("m"),
            subTable.getField("d")).from(subTable).fetchOne();
        assertEquals(reference, result);
    }

    @Test
    public void testSystemFunctions() throws Exception {
        if (getDialect() == SQLDialect.SQLITE) {
            log.info("SKIPPING", "System functions test");
            return;
        }

        Field<?> user = trim(lower(currentUser()));
        Record record = create().select(user).fetchOne();

        assertTrue(Arrays.asList("test", "db2admin", "sa", "root@localhost", "postgres", "dbo", "dba", "dba@lukas-hp")
            .contains(record.getValue(user)));
    }

    @Test
    public void testArithmeticOperations() throws Exception {
        Field<Integer> f1 = val(1).add(2).add(3).div(2);
        Field<Integer> f2 = val(10).div(5).add(val(3).sub(2));
        Field<Integer> f3 = val(10).mod(3);

        SelectQuery q1 = create().selectQuery();
        q1.addSelect(f1, f2, f3);
        q1.execute();

        Result<?> result = q1.getResult();
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(3), result.getValue(0, f1));
        assertEquals(Integer.valueOf(3), result.getValue(0, f2));
        assertEquals(Integer.valueOf(1), result.getValue(0, f3));

        Field<Integer> f4 = TBook_PUBLISHED_IN().add(3).div(7);
        Field<Integer> f5 = TBook_PUBLISHED_IN().sub(4).mul(8).neg();

        SelectQuery q2 = create().selectQuery();
        q2.addSelect(f4);
        q2.addSelect(f5);
        q2.addFrom(TBook());
        q2.addConditions(TBook_TITLE().equal("1984"));
        q2.execute();

        result = q2.getResult();
        assertEquals(Integer.valueOf((1948 + 3) / 7), result.getValue(0, f4));
        assertEquals(Integer.valueOf((1948 - 4) * -8), result.getValue(0, f5));
    }

    @Test
    public void testBitwiseOperations() throws Exception {
        switch (getDialect()) {
            case DERBY:
            case INGRES:
                log.info("SKIPPING", "Tests for bitwise operations");
                return;
        }

        Field<Integer> bitCount = bitCount(3);

        // TODO [#896] This somehow doesn't work on some dialects
        if (asList(ASE, DB2, SQLSERVER).contains(getDialect())) {
            bitCount = val(2);
        }

        Record result =
        create().select(
                    bitCount,
                    bitNot(bitNot(3)),
                    bitAnd(3, 5),
                    bitOr(3, 5),

                    bitXor(3, 5),
                    bitNot(bitNand(3, 5)),
                    bitNot(bitNor(3, 5)),
                    bitNot(bitXNor(3, 5)),

                    shl(333, 3),
                    shr(333, 3))
                .fetchOne();

        int index = 0;
        assertEquals(2, result.getValue(index++));
        assertEquals(~(~3), result.getValue(index++));
        assertEquals(3 & 5, result.getValue(index++));
        assertEquals(3 | 5, result.getValue(index++));

        assertEquals(3 ^ 5, result.getValue(index++));
        assertEquals(~(~(3 & 5)), result.getValue(index++));
        assertEquals(~(~(3 | 5)), result.getValue(index++));
        assertEquals(~(~(3 ^ 5)), result.getValue(index++));

        assertEquals(333 << 3, result.getValue(index++));
        assertEquals(333 >> 3, result.getValue(index++));
    }
}
