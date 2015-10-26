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
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.INFORMIX;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.impl.DSL.abs;
import static org.jooq.impl.DSL.acos;
import static org.jooq.impl.DSL.ascii;
import static org.jooq.impl.DSL.asin;
import static org.jooq.impl.DSL.atan;
import static org.jooq.impl.DSL.atan2;
import static org.jooq.impl.DSL.bitAnd;
import static org.jooq.impl.DSL.bitCount;
import static org.jooq.impl.DSL.bitLength;
import static org.jooq.impl.DSL.bitNand;
import static org.jooq.impl.DSL.bitNor;
import static org.jooq.impl.DSL.bitNot;
import static org.jooq.impl.DSL.bitOr;
import static org.jooq.impl.DSL.bitXNor;
import static org.jooq.impl.DSL.bitXor;
import static org.jooq.impl.DSL.castNull;
import static org.jooq.impl.DSL.ceil;
import static org.jooq.impl.DSL.charLength;
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.cos;
import static org.jooq.impl.DSL.cosh;
import static org.jooq.impl.DSL.cot;
import static org.jooq.impl.DSL.coth;
import static org.jooq.impl.DSL.currentDate;
import static org.jooq.impl.DSL.currentSchema;
import static org.jooq.impl.DSL.currentTime;
import static org.jooq.impl.DSL.currentTimestamp;
import static org.jooq.impl.DSL.currentUser;
import static org.jooq.impl.DSL.date;
import static org.jooq.impl.DSL.day;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.deg;
import static org.jooq.impl.DSL.exp;
import static org.jooq.impl.DSL.extract;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.floor;
import static org.jooq.impl.DSL.greatest;
import static org.jooq.impl.DSL.hour;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.least;
import static org.jooq.impl.DSL.left;
import static org.jooq.impl.DSL.length;
import static org.jooq.impl.DSL.ln;
import static org.jooq.impl.DSL.log;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.lpad;
import static org.jooq.impl.DSL.ltrim;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.md5;
import static org.jooq.impl.DSL.minute;
import static org.jooq.impl.DSL.month;
import static org.jooq.impl.DSL.nullif;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.nvl2;
import static org.jooq.impl.DSL.octetLength;
import static org.jooq.impl.DSL.position;
import static org.jooq.impl.DSL.power;
import static org.jooq.impl.DSL.rad;
import static org.jooq.impl.DSL.rand;
import static org.jooq.impl.DSL.repeat;
import static org.jooq.impl.DSL.replace;
import static org.jooq.impl.DSL.reverse;
import static org.jooq.impl.DSL.right;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.rpad;
import static org.jooq.impl.DSL.rtrim;
import static org.jooq.impl.DSL.second;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectCount;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.shl;
import static org.jooq.impl.DSL.shr;
import static org.jooq.impl.DSL.sign;
import static org.jooq.impl.DSL.sin;
import static org.jooq.impl.DSL.sinh;
import static org.jooq.impl.DSL.space;
import static org.jooq.impl.DSL.sqrt;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.tan;
import static org.jooq.impl.DSL.tanh;
import static org.jooq.impl.DSL.time;
import static org.jooq.impl.DSL.timestamp;
import static org.jooq.impl.DSL.toDate;
import static org.jooq.impl.DSL.toTimestamp;
import static org.jooq.impl.DSL.trim;
import static org.jooq.impl.DSL.trunc;
import static org.jooq.impl.DSL.upper;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.DSL.year;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Assert;

public class FunctionTests<
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

    public FunctionTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testFunctionPosition() throws Exception {
        assumeFamilyNotIn(INFORMIX);

        SelectQuery<?> q = create().selectQuery();
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
        if (true/* [pro] */ && dialect() != SQLDialect.ASE/* [/pro] */) {
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
        if (true/* [pro] */ && dialect() != SQLDialect.ASE/* [/pro] */) {
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
        Result<Record4<String, String, String, String>> result = create().select(
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

    public void testCaseExpression() throws Exception {
        Field<String> case1 =
             choose(TBook_PUBLISHED_IN())
            .when(0, "ancient book")
            .as("case1");

        boolean noSubselects = false;

        /* [pro] */
        // Ingres does not allow sub selects in CASE expressions
        if (dialect() == INGRES)
            noSubselects = true;

        /* [/pro] */
        Field<?> case2 = noSubselects
            ? choose(TBook_AUTHOR_ID())
                .when(1, "Orwell")
                .otherwise("unknown")
            : choose(TBook_AUTHOR_ID())
                .when(1, create().select(TAuthor_LAST_NAME())
                    .from(TAuthor())
                    .where(TAuthor_ID().equal(TBook_AUTHOR_ID())).asField())
                .otherwise("unknown");

        Field<?> case3 = choose(1)
                        .when(1, "A")
                        .when(2, "B")
                        .otherwise("C");

        SelectQuery<?> query = create().selectQuery();
        query.addSelect(case1, case2, case3);
        query.addFrom(TBook());
        query.addOrderBy(TBook_PUBLISHED_IN());
        query.execute();

        Result<?> result = query.getResult();
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

        Field<String> case4 =
             when(TBook_PUBLISHED_IN().equal(1948), "probably orwell")
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

    public void testCaseExpressionWithSubquery() throws Exception {
        Result<Record1<Integer>> result =
        create().select(
            when(TAuthor_ID().eq(0), selectCount().from(TBook()).where(TBook_AUTHOR_ID().eq(TAuthor_ID())))
            .when(TAuthor_ID().eq(1), select(max(TBook_ID())).from(TBook()).where(TBook_AUTHOR_ID().eq(TAuthor_ID())))
            .otherwise(selectOne())
        )
        .from(TAuthor())
        .orderBy(TAuthor_ID())
        .fetch();

        assertEquals(2, result.size());
        assertEquals(asList(2, 1), result.getValues(0, int.class));
    }

    public void testFunctionsOnStrings_TRIM() throws Exception {

        // Trimming
        assertEquals("abc", create().select(trim("abc")).fetchOne(0));
        assertEquals("abc", create().select(trim("abc  ")).fetchOne(0));
        assertEquals("abc", create().select(trim("  abc")).fetchOne(0));
        assertEquals("abc", create().select(trim("  abc  ")).fetchOne(0));
        assertEquals("  abc", create().select(rtrim("  abc  ")).fetchOne(0));
        assertEquals("abc  ", create().select(ltrim("  abc  ")).fetchOne(0));
    }

    public void testFunctionsOnStrings_UPPER_LOWER() throws Exception {

        // Lower / Upper
        assertEquals("abc", create().select(lower("ABC")).fetchOne(0));
        assertEquals("ABC", create().select(upper("abc")).fetchOne(0));
    }

    public void testFunctionsOnStrings_CONCAT() throws Exception {

        // String concatenation
        assertEquals("abc", create().select(concat("a", "b", "c")).fetchOne(0));
        assertEquals("George Orwell", create()
            .select(concat(TAuthor_FIRST_NAME(), val(" "), TAuthor_LAST_NAME()))
            .from(TAuthor())
            .where(TAuthor_FIRST_NAME().equal("George")).fetchOne(0));

        assertEquals("1ab45", create().select(concat(val(1), val("ab"), val(45))).fetchOne(0));
    }

    public void testFunctionsOnStrings_REPLACE() throws Exception {
        assumeFamilyNotIn(DERBY);

        // Standard String functions
        Field<String> constant = val("abc");
        Field<String> x = replace(constant, "b", "x");
        Field<String> y = replace(constant, "b", "y");
        Field<String> z = replace(constant, "b");
        Record record = create().select(x, y, z).fetchOne();

        assertEquals("axc", record.getValue(x));
        assertEquals("ayc", record.getValue(y));
        assertEquals("ac", record.getValue(z));
    }

    public void testFunctionsOnStrings_LENGTH() throws Exception {
        // Standard String functions
        SelectQuery<?> q = create().selectQuery();
        Field<String> constant = val("abc");

        Field<Integer> length = length(constant);
        Field<Integer> charLength = charLength(constant);
        Field<Integer> bitLength = bitLength(constant);
        Field<Integer> octetLength = octetLength(constant);
        q.addSelect(length, charLength, bitLength, octetLength);
        q.execute();

        Record record = q.getResult().get(0);

        assertEquals(Integer.valueOf(3), record.getValue(length));
        assertEquals(Integer.valueOf(3), record.getValue(charLength));

        switch (dialect()) {
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
    }

    public void testFunctionsOnStrings_RPAD_LPAD() throws Exception {

        // RPAD, LPAD
        switch (dialect()) {
            case DERBY:
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
    }

    public void testFunctionsOnStrings_SUBSTRING() throws Exception {

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
    }

    public void testFunctionsOnStrings_LEFT_RIGHT() throws Exception {

        // LEFT and RIGHT
        Record2<String, String> result = create().select(
            left(val("abcde"), 3),
            right(val("abcde"), 3)).fetchOne();

        assertEquals("abc", result.value1());
        assertEquals("cde", result.value2());

        result =
        create().select(
                    left(TAuthor_FIRST_NAME(), 3),
                    right(TAuthor_FIRST_NAME(), 3))
                .from(TAuthor())
                .where(TAuthor_ID().equal(1))
                .fetchOne();

        assertEquals("Geo", result.value1());
        assertEquals("rge", result.value2());
    }

    public void testFunctionsOnStrings_REPEAT() throws Exception {

        // REPEAT
        switch (dialect()) {
            /* [pro] */
            case ACCESS:
            /* [/pro] */
            case DERBY:
                log.info("SKIPPING", "REPEAT function");
                break;

            default: {
                Record result = create().select(
                    repeat("a", 1),
                    repeat("ab", 2),
                    repeat("abc", 3)).fetchOne();
                assertEquals("a", result.getValue(0));
                assertEquals("abab", result.getValue(1));
                assertEquals("abcabcabc", result.getValue(2));
                break;
            }
        }
    }

    public void testFunctionsOnStrings_SPACE() throws Exception {

        switch (dialect()) {
            case DERBY:
                log.info("SKIPPING", "SPACE function");
                break;

            default: {
                Result<Record2<String, String>> result =
                create().select(
                            space(1),
                            space(TAuthor_ID().mul(2)))
                        .from(TAuthor())
                        .orderBy(TAuthor_ID())
                        .fetch();

                assertEquals(" ", result.get(0).value1());
                assertEquals("  ", result.get(0).value2());
                assertEquals(" ", result.get(1).value1());
                assertEquals("    ", result.get(1).value2());
                break;
            }
        }
    }

    public void testFunctionsOnStrings_REVERSE() throws Exception {

        // REVERSE
        switch (dialect().family()) {
            /* [pro] */
            case DB2:
            case HANA:
            case INGRES:
            case SYBASE:
            case VERTICA:
            /* [/pro] */

            case DERBY:
            case FIREBIRD:
            case H2:
            case SQLITE:
                log.info("SKIPPING", "REVERSE function");
                break;

            default: {
                Record result = create().select(
                    reverse("a"),
                    reverse("ab"),
                    reverse("abc")).fetchOne();
                assertEquals("a", result.getValue(0));
                assertEquals("ba", result.getValue(1));
                assertEquals("cba", result.getValue(2));
                break;
            }
        }
    }

    public void testFunctionsOnStrings_ASCII() throws Exception {

        // ASCII
        switch (dialect()) {
            /* [pro] */
            case INGRES: // TODO [#864]
            /* [/pro] */
            case DERBY:
            case SQLITE: // TODO [#862]
                log.info("SKIPPING", "ASCII function test");
                break;

            default:
                Record record =
                create().select(
                    ascii("A"),
                    ascii("a"),
                    ascii("-"),
                    ascii(" ")).fetchOne();
                assertEquals('A', (int) record.getValue(0, Integer.class));
                assertEquals('a', (int) record.getValue(1, Integer.class));
                assertEquals('-', (int) record.getValue(2, Integer.class));
                assertEquals(' ', (int) record.getValue(3, Integer.class));

                break;
        }
    }

    public void testFunctionsOnStrings_HashFunctions() throws Exception {

        // MD5
        switch (family()) {
            /* [pro] */
            case ACCESS:
            case ASE:
            case DB2:
            case INGRES:
            case SQLSERVER:
            case SYBASE:
            /* [/pro] */
            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case H2:
            case HANA:
            case HSQLDB:
            case POSTGRES:
            case SQLITE:
                log.info("SKIPPING", "MD5 function test");
                break;

            /* [pro] */
            case ORACLE:
            /* [/pro] */
            case MARIADB:
            case MYSQL:
            default:
                assertEquals("900150983cd24fb0d6963f7d28e17f72", create().select(md5("abc")).fetchOne(0));
                break;
        }
    }

    public void testFunctionsOnNumbers_RAND() throws Exception {
        assumeFamilyNotIn(INFORMIX);

        // The random function
        BigDecimal rand = create().select(rand()).fetchOne(rand());
        assertNotNull(rand);
    }

    public void testFunctionsOnNumbers_ROUND_FLOOR_CEIL_TRUNC() throws Exception {

        // Some databases are limited or buggy
        boolean sqlite = (dialect() == SQLITE);
        boolean ingres = false/* [pro] */ || (dialect() == INGRES)/* [/pro] */;

        // Some rounding functions
        Field<Float> f1a = round(1.111f);
        Field<Float> f2a = round(1.111f, 2);
        Field<Float> f3a = floor(1.111f);
        Field<Float> f4a = ceil(1.111f);
        Field<Float> f5a = sqlite || ingres ? inline(1.0f) : trunc(1.111f);
        Field<Float> f6a = sqlite || ingres ? inline(1.11f) : trunc(1.111f, 2);
        Field<Float> f7a = sqlite || ingres ? inline(10.0f) : trunc(11.111f, -1);

        Record r1 =
        create().select(f1a)
                .select(f2a, f3a)
                .select(f4a)
                .select(f5a, f6a, f7a)
                .fetchOne();

        assertNotNull(r1);
        assertEquals("1.0", r1.getValue(f1a, String.class));
        assertEquals("1.11", r1.getValue(f2a, String.class));
        assertEquals("1.0", r1.getValue(f3a, String.class));
        assertEquals("2.0", r1.getValue(f4a, String.class));
        assertEquals("1.0", r1.getValue(f5a, String.class));
        assertEquals("1.11", r1.getValue(f6a, String.class));
        assertEquals("10.0", r1.getValue(f7a, String.class));



        Field<Double> f1b = round(-1.111);
        Field<Double> f2b = round(-1.111, 2);
        Field<Double> f3b = floor(-1.111);
        Field<Double> f4b = ceil(-1.111);
        Field<Double> f5b = sqlite || ingres ? inline(1.0) : trunc(1.111);
        Field<Double> f6b = sqlite || ingres ? inline(1.11) : trunc(1.111, 2);
        Field<Double> f7b = sqlite || ingres ? inline(10.0) : trunc(11.111, -1);

        Record r2 =
        create().select(f1b, f2b, f3b, f4b, f5b, f6b, f7b)
                .fetchOne();

        assertEquals("-1.0", r2.getValue(f1b, String.class));
        assertEquals("-1.11", r2.getValue(f2b, String.class));
        assertEquals("-2.0", r2.getValue(f3b, String.class));
        assertEquals("-1.0", r2.getValue(f4b, String.class));
        assertEquals("1.0", r2.getValue(f5b, String.class));
        assertEquals("1.11", r2.getValue(f6b, String.class));
        assertEquals("10.0", r2.getValue(f7b, String.class));



        Field<Float> f1c = round(2.0f);
        Field<Float> f2c = round(2.0f, 2);
        Field<Float> f3c = floor(2.0f);
        Field<Float> f4c = ceil(2.0f);
        Field<Double> f1d = round(-2.0);
        Field<Double> f2d = round(-2.0, 2);
        Field<Double> f3d = floor(-2.0);
        Field<Double> f4d = ceil(-2.0);

        Field<Float> f1e = ingres ? inline(0.0f) : round(0.0f);
        Field<Float> f2e = ingres ? inline(0.0f) : round(0.0f, 2);
        Field<Float> f3e = ingres ? inline(0.0f) : floor(0.0f);
        Field<Float> f4e = ingres ? inline(0.0f) : ceil(0.0f);
        Field<Double> f1f = round(0.0);
        Field<Double> f2f = round(0.0, 2);
        Field<Double> f3f = floor(0.0);
        Field<Double> f4f = ceil(0.0);

        Record record =
        create().select(f1c, f2c, f3c, f4c)
                .select(f1d, f2d, f3d, f4d)
                .select(f1e, f2e, f3e, f4e)
                .select(f1f, f2f, f3f, f4f)
                .fetchOne();

        assertEquals("2.0", record.getValue(f1c, String.class));
        assertEquals("2.0", record.getValue(f2c, String.class));
        assertEquals("2.0", record.getValue(f3c, String.class));
        assertEquals("2.0", record.getValue(f4c, String.class));

        assertEquals("-2.0", record.getValue(f1d, String.class));
        assertEquals("-2.0", record.getValue(f2d, String.class));
        assertEquals("-2.0", record.getValue(f3d, String.class));
        assertEquals("-2.0", record.getValue(f4d, String.class));

        assertEquals("0.0", record.getValue(f1e, String.class));
        assertEquals("0.0", record.getValue(f2e, String.class));
        assertEquals("0.0", record.getValue(f3e, String.class));
        assertEquals("0.0", record.getValue(f4e, String.class));

        assertEquals("0.0", record.getValue(f1f, String.class));
        assertEquals("0.0", record.getValue(f2f, String.class));
        assertEquals("0.0", record.getValue(f3f, String.class));
        assertEquals("0.0", record.getValue(f4f, String.class));
    }

    public void testFunctionsOnNumbers_GREATEST_LEAST() throws Exception {

        // Greatest and least
        Record record = create().select(
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
        Result<Record3<Integer, Integer, Integer>> result = create()
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
    }

    public void testFunctionsOnNumbers_TRIGONOMETRY() throws Exception {

        // Mathematical functions
        switch (dialect()) {
            case SQLITE:
                log.info("SKIPPING", "Tests for trigonometric functions");
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

                Record record = create().select(m1, m2, m3, m4, m5, m6, m7, m8, m9).fetchOne();

                // Rounding issues are circumvented by using substring()
                assertNotNull(record);
                assertEquals("1.414", record.getValue(m1, String.class).substring(0, 5));
                assertEquals("2", record.getValue(m2, String.class).substring(0, 1));
                assertEquals("7.389", record.getValue(m3, String.class).substring(0, 5));
                assertEquals("1", record.getValue(m4, String.class).substring(0, 1));
                assertEquals("0.135", record.getValue(m5, String.class).substring(0, 5));
                assertEquals("0.693", record.getValue(m6, String.class).substring(0, 5));
                assertEquals("2", record.getValue(m7, String.class).substring(0, 1));
                assertEquals("16", record.getValue(m8, String.class).substring(0, 2));
                assertEquals("2", record.getValue(m9, String.class).substring(0, 1));

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
                assertEquals("0.5", record.getValue(t1, String.class).substring(0, 3));
                assertEquals("0.866", record.getValue(t2, String.class).substring(0, 5));
                assertEquals("0.577", record.getValue(t3, String.class).substring(0, 5));
                assertEquals("1.732", record.getValue(t4, String.class).substring(0, 5));
                assertEquals("1", record.getValue(t6, String.class).substring(0, 1));
                assertEquals("0.551", record.getValue(t7, String.class).substring(0, 5));
                assertEquals("1.019", record.getValue(t8, String.class).substring(0, 5));
                assertEquals("0.482", record.getValue(t9, String.class).substring(0, 5));
                assertEquals("45", record.getValue(ta, String.class).substring(0, 2));
                assertEquals("1", record.getValue(tb, String.class).substring(0, 1));

                break;
            }
        }
    }

    public void testFunctionsOnNumbers_SIGN() throws Exception {

        // The sign function
        Record record = create().select(
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
    }

    public void testFunctionsOnNumbers_ABS() throws Exception {

        // The abs function
        Record record = create().select(
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

    public void testDateOrTimeFunction() throws Exception {

        // [#3041] TODO
        switch (dialect().family()) {
            case DERBY:
                log.info("SKIPPING", "Derby date or time function");
                return;
        }

        Field<Date> d1 = date(inline(Timestamp.valueOf("1970-01-01 02:00:00.0")));
        Field<Time> t1 = time(inline(Timestamp.valueOf("1970-01-01 02:00:00.0")));
        Field<Timestamp> ts1 = timestamp(inline(Date.valueOf("1970-01-01")));

        Field<Date> d2 = date("1970-01-01");
        Field<Time> t2 = time("02:00:00");
        Field<Timestamp> ts2 = timestamp("1970-01-01 02:00:00.0");

        Record record =
        create().select(d1, t1, ts1)
                .select(d2, t2, ts2)
                .fetchOne();

        assertEquals("1970-01-01 00:00:00.0", record.getValue(ts1).toString());
        assertEquals("1970-01-01", record.getValue(d1).toString());
        assertEquals("02:00:00", record.getValue(t1).toString());

        assertEquals("1970-01-01 02:00:00.0", record.getValue(ts2).toString());
        assertEquals("1970-01-01", record.getValue(d2).toString());
        assertEquals("02:00:00", record.getValue(t2).toString());
    }

    public void testToDateToTimestamp() {
        Record2<Date, Timestamp> result =
        create().select(
                    toDate("20000401", "YYYYMMDD"),
                    toTimestamp("20000401 121537", "YYYYMMDD HH24MISS"))
                .fetchOne();

        assertEquals(Date.valueOf("2000-04-01"), result.value1());
        assertEquals(Timestamp.valueOf("2000-04-01 12:15:37"), result.value2());
    }

    public void testCurrentDateTime() {
        Record3<Timestamp, Date, Time> record = create().select(currentTimestamp(), currentDate(), currentTime()).fetchOne();

        // Let's not compare fractional seconds, though...
        assertEquals(
            record.value1().toString().replaceAll("\\..*", ""),
            record.value2().toString() + " " + record.value3().toString().replaceAll("\\..*", ""));
    }

    public void testFunctionsOnDates_EXTRACT() throws Exception {

        // Some checks on current_timestamp functions
        // ------------------------------------------
        SelectQuery<?> q1 = create().selectQuery();
        Field<Timestamp> now = val(new Timestamp(0));
        Field<Timestamp> ts = now.as("ts");
        Field<Date> date = val(new Date(0)).as("d");
        Field<Time> time = val(new Time(0)).as("t");

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
        if (family() != SQLDialect.POSTGRES) {
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
    }

    public void testFunctionsOnDates_ARITHMETIC() throws Exception {

        // Timestamp arithmetic
        // --------------------
        Field<Timestamp> now = currentTimestamp();
        Field<Timestamp> ts = now.as("ts");
        Field<Timestamp> tomorrow = now.add(1);
        Field<Timestamp> yesterday = now.sub(1);
        Record record = create().select(tomorrow, ts, yesterday).fetchOne();

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
            sub.field("y"),
            sub.field("m"),
            sub.field("d")).from(sub).fetchOne();
        assertEquals(reference, result);

        result = create().select(
            subTable.field("y"),
            subTable.field("m"),
            subTable.field("d")).from(subTable).fetchOne();
        assertEquals(reference, result);
    }

    public void testSystemFunctions() throws Exception {
        Field<String> user = trim(lower(currentUser()));
        Field<String> schema = trim(lower(currentSchema()));
        Record2<String, String> record = create().select(user, schema).fetchOne();

        switch (family()) {
            case CUBRID:
                assertEquals("dba@ubuntu", record.value1());
                assertEquals("", record.value2());
                break;

            case DERBY:
                assertEquals("test", record.value1());
                assertEquals("test", record.value2());
                break;

            case FIREBIRD:
                assertEquals("test", record.value1());
                assertEquals("", record.value2());
                break;

            case H2:
            case HSQLDB:
                assertEquals("sa", record.value1());
                assertEquals("public", record.value2());
                break;

            case MARIADB:
            case MYSQL:
                assertEquals("root@localhost", record.value1());
                assertEquals("test", record.value2());
                break;

            case ORACLE:
                assertEquals("test", record.value1());
                assertEquals("test", record.value2());
                break;

            case POSTGRES:
                assertEquals("postgres", record.value1());
                assertEquals("public", record.value2());
                break;

            case SQLITE:
                assertEquals("", record.value1());
                assertEquals("", record.value2());
                break;

            case SQLSERVER:
                assertEquals("dbo", record.value1());
                assertEquals("dbo", record.value2());
                break;

            case VERTICA:
                assertEquals("test", record.value1());
                assertEquals("public", record.value2());
                break;

            default:
                Assert.fail();
        }
    }

    public void testArithmeticOperations() throws Exception {
        Field<Integer> f1 = val(1).add(2).add(3).div(2);
        Field<Integer> f2 = val(10).div(5).add(val(3).sub(2));
        Field<Integer> f3 = val(10).mod(3);

        SelectQuery<?> q1 = create().selectQuery();
        q1.addSelect(f1, f2, f3);
        q1.execute();

        Result<?> result = q1.getResult();
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(3), result.getValue(0, f1));
        assertEquals(Integer.valueOf(3), result.getValue(0, f2));
        assertEquals(Integer.valueOf(1), result.getValue(0, f3));

        Field<Integer> f4 = TBook_PUBLISHED_IN().add(3).div(7);
        Field<Integer> f5 = TBook_PUBLISHED_IN().sub(4).mul(8).neg();

        SelectQuery<?> q2 = create().selectQuery();
        q2.addSelect(f4);
        q2.addSelect(f5);
        q2.addFrom(TBook());
        q2.addConditions(TBook_TITLE().equal("1984"));
        q2.execute();

        result = q2.getResult();
        assertEquals(Integer.valueOf((1948 + 3) / 7), result.getValue(0, f4));
        assertEquals(Integer.valueOf((1948 - 4) * -8), result.getValue(0, f5));
    }

    public void testBitwiseOperations() throws Exception {
        switch (dialect().family()) {
            /* [pro] */
            case ACCESS:
            case HANA:
            case INGRES:
            /* [/pro] */
            case DERBY:
                log.info("SKIPPING", "Tests for bitwise operations");
                return;
        }

        Field<Integer> bitCount = bitCount(3);

        /* [pro] */
        // TODO [#896] This somehow doesn't work on some dialects
        if (asList(ASE, DB2, SQLSERVER).contains(dialect().family())) {
            bitCount = val(2);
        }

        /* [/pro] */
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
