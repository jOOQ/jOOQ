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

package org.jooq.test;

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.TABLE1;
import static org.jooq.test.data.Table2.FIELD_ID2;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Row1;
import org.jooq.Row2;
import org.jooq.Row3;
import org.jooq.Row4;
import org.jooq.Row5;
import org.jooq.Row6;
import org.jooq.Row7;
import org.jooq.Row8;
import org.jooq.RowN;
import org.jooq.Table;
import org.jooq.conf.RenderKeywordStyle;
import org.jooq.conf.RenderNameStyle;
import org.jooq.impl.CustomCondition;
import org.jooq.impl.CustomField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import org.jmock.Expectations;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * A test suite for basic jOOQ functionality
 *
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicTest extends AbstractTest {

    @Test
    public void testNullPointerExceptionSafety() throws Exception {
        // Functions created from a field
        // ------------------------------
        assertEquals(
            FIELD_ID1.add((Integer) null),
            FIELD_ID1.add((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.between((Integer) null, null),
            FIELD_ID1.between((Field<Integer>) null, null));
        assertEquals(
            FIELD_ID1.betweenSymmetric((Integer) null, null),
            FIELD_ID1.betweenSymmetric((Field<Integer>) null, null));
        assertEquals(
            FIELD_ID1.div((Integer) null),
            FIELD_ID1.div((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.equal((Integer) null),
            FIELD_ID1.equal((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.greaterOrEqual((Integer) null),
            FIELD_ID1.greaterOrEqual((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.greaterThan((Integer) null),
            FIELD_ID1.greaterThan((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.in((Integer) null),
            FIELD_ID1.in((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.isDistinctFrom((Integer) null),
            FIELD_ID1.isDistinctFrom((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.isNotDistinctFrom((Integer) null),
            FIELD_ID1.isNotDistinctFrom((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.lessOrEqual((Integer) null),
            FIELD_ID1.lessOrEqual((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.lessThan((Integer) null),
            FIELD_ID1.lessThan((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.mod((Integer) null),
            FIELD_ID1.mod((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.mul((Integer) null),
            FIELD_ID1.mul((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.notBetween((Integer) null, null),
            FIELD_ID1.notBetween((Field<Integer>) null, null));
        assertEquals(
            FIELD_ID1.notBetweenSymmetric((Integer) null, null),
            FIELD_ID1.notBetweenSymmetric((Field<Integer>) null, null));
        assertEquals(
            FIELD_ID1.notEqual((Integer) null),
            FIELD_ID1.notEqual((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.notIn((Integer) null),
            FIELD_ID1.notIn((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.sub((Integer) null),
            FIELD_ID1.sub((Field<Integer>) null));
        assertEquals(
            FIELD_ID1.shl((Long) null),
            FIELD_ID1.shl((Field<Long>) null));
        assertEquals(
            FIELD_ID1.shr((Long) null),
            FIELD_ID1.shr((Field<Long>) null));

        // Standalone functions created from the factory
        // ---------------------------------------------
        assertEquals(
            DSL.abs((Integer) null),
            DSL.abs((Field<Integer>) null));
        assertEquals(
            DSL.acos((Integer) null),
            DSL.acos((Field<Integer>) null));
        assertEquals(
            DSL.asin((Integer) null),
            DSL.asin((Field<Integer>) null));
        assertEquals(
            DSL.atan((Integer) null),
            DSL.atan((Field<Integer>) null));
        assertEquals(
            DSL.atan2((Integer) null, (Integer) null),
            DSL.atan2((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.atan2((Integer) null, (Integer) null),
            DSL.atan2((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.atan2((Integer) null, (Integer) null),
            DSL.atan2((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitAnd((Integer) null, (Integer) null),
            DSL.bitAnd((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitAnd((Integer) null, (Integer) null),
            DSL.bitAnd((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.bitAnd((Integer) null, (Integer) null),
            DSL.bitAnd((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitCount((Integer) null),
            DSL.bitCount((Field<Integer>) null));
        assertEquals(
            DSL.bitLength((String) null),
            DSL.bitLength((Field<String>) null));
        assertEquals(
            DSL.bitNand((Integer) null, (Integer) null),
            DSL.bitNand((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitNand((Integer) null, (Integer) null),
            DSL.bitNand((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.bitNand((Integer) null, (Integer) null),
            DSL.bitNand((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitNor((Integer) null, (Integer) null),
            DSL.bitNor((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitNor((Integer) null, (Integer) null),
            DSL.bitNor((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.bitNor((Integer) null, (Integer) null),
            DSL.bitNor((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitOr((Integer) null, (Integer) null),
            DSL.bitOr((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitOr((Integer) null, (Integer) null),
            DSL.bitOr((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.bitOr((Integer) null, (Integer) null),
            DSL.bitOr((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitXNor((Integer) null, (Integer) null),
            DSL.bitXNor((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitXNor((Integer) null, (Integer) null),
            DSL.bitXNor((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.bitXNor((Integer) null, (Integer) null),
            DSL.bitXNor((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitXor((Integer) null, (Integer) null),
            DSL.bitXor((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.bitXor((Integer) null, (Integer) null),
            DSL.bitXor((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.bitXor((Integer) null, (Integer) null),
            DSL.bitXor((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.ceil((Integer) null),
            DSL.ceil((Field<Integer>) null));
        assertEquals(
            DSL.charLength((String) null),
            DSL.charLength((Field<String>) null));
        assertEquals(
            DSL.coalesce((Integer) null, (Integer) null),
            DSL.coalesce((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.coalesce((Integer) null, (Integer) null, (Integer) null),
            DSL.coalesce((Field<Integer>) null, (Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.coalesce((Integer) null, (Integer) null, (Integer[]) null),
            DSL.coalesce((Field<Integer>) null, (Field<Integer>) null, (Field<?>[]) null));
        assertEquals(
            DSL.coalesce((Integer) null, (Integer) null, (Integer) null, (Integer) null),
            DSL.coalesce((Field<Integer>) null, (Field<Integer>) null, (Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.concat((String) null, (String) null),
            DSL.concat((Field<String>) null, (Field<String>) null));
        assertEquals(
            DSL.cos((Integer) null),
            DSL.cos((Field<Integer>) null));
        assertEquals(
            DSL.cosh((Integer) null),
            DSL.cosh((Field<Integer>) null));
        assertEquals(
            DSL.cot((Integer) null),
            DSL.cot((Field<Integer>) null));
        assertEquals(
            DSL.coth((Integer) null),
            DSL.coth((Field<Integer>) null));
        assertEquals(
            DSL.dateAdd((Date) null, (Integer) null),
            DSL.dateAdd((Field<Date>) null, (Field<Integer>) null));
        assertEquals(
            DSL.dateDiff((Date) null, (Date) null),
            DSL.dateDiff((Field<Date>) null, (Field<Date>) null));
        assertEquals(
            DSL.day((java.util.Date) null),
            DSL.day((Field<java.util.Date>) null));
        assertEquals(
            DSL.decode((Integer) null, null, null),
            DSL.decode((Field<Integer>) null, null, null));
        assertEquals(
            DSL.decode((Integer) null, null, null),
            DSL.decode((Field<Integer>) null, null, null));
        assertEquals(
            DSL.deg((Integer) null),
            DSL.deg((Field<Integer>) null));
        assertEquals(
            DSL.exp((Integer) null),
            DSL.exp((Field<Integer>) null));
        assertEquals(
            DSL.extract((java.util.Date) null, DatePart.DAY),
            DSL.extract((Field<java.util.Date>) null, DatePart.DAY));
        assertEquals(
            DSL.floor((Integer) null),
            DSL.floor((Field<Integer>) null));
        assertEquals(
            DSL.greatest((Integer) null),
            DSL.greatest((Field<Integer>) null));
        assertEquals(
            DSL.greatest((Integer) null, (Integer[]) null),
            DSL.greatest((Field<Integer>) null, (Field[]) null));
        assertEquals(
            DSL.greatest((Integer) null, (Integer) null, (Integer) null),
            DSL.greatest((Field<Integer>) null, (Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.greatest((Integer) null, (Integer) null),
            DSL.greatest((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.hour((java.util.Date) null),
            DSL.hour((Field<java.util.Date>) null));
        assertEquals(
            DSL.lag((Field<Integer>) null, 1, (Integer) null),
            DSL.lag((Field<Integer>) null, 1, (Field<Integer>) null));
        assertEquals(
            DSL.lead((Field<Integer>) null, 1, (Integer) null),
            DSL.lead((Field<Integer>) null, 1, (Field<Integer>) null));
        assertEquals(
            DSL.least((Integer) null),
            DSL.least((Field<Integer>) null));
        assertEquals(
            DSL.least((Integer) null, (Integer) null),
            DSL.least((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.least((Integer) null, (Integer[]) null),
            DSL.least((Field<Integer>) null, (Field[]) null));
        assertEquals(
            DSL.least((Integer) null, (Integer) null, (Integer) null),
            DSL.least((Field<Integer>) null, (Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.length((String) null),
            DSL.length((Field<String>) null));
        assertEquals(
            DSL.ln((Integer) null),
            DSL.ln((Field<Integer>) null));
        assertEquals(
            DSL.log((Integer) null, 2),
            DSL.log((Field<Integer>) null, 2));
        assertEquals(
            DSL.lower((String) null),
            DSL.lower((Field<String>) null));
        assertEquals(
            DSL.ltrim((String) null),
            DSL.ltrim((Field<String>) null));
        assertEquals(
            DSL.minute((java.util.Date) null),
            DSL.minute((Field<java.util.Date>) null));
        assertEquals(
            DSL.month((java.util.Date) null),
            DSL.month((Field<java.util.Date>) null));
        assertEquals(
            DSL.nullif((Integer) null, (Integer) null),
            DSL.nullif((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.nullif((Integer) null, (Integer) null),
            DSL.nullif((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.nullif((Integer) null, (Integer) null),
            DSL.nullif((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.nvl((Integer) null, (Integer) null),
            DSL.nvl((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.nvl((Integer) null, (Integer) null),
            DSL.nvl((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.nvl((Integer) null, (Integer) null),
            DSL.nvl((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.nvl2((Field<Integer>) null, (Integer) null, (Integer) null),
            DSL.nvl2((Field<Integer>) null, (Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.nvl2((Field<Integer>) null, (Integer) null, (Integer) null),
            DSL.nvl2((Field<Integer>) null, (Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.nvl2((Field<Integer>) null, (Integer) null, (Integer) null),
            DSL.nvl2((Field<Integer>) null, (Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.octetLength((String) null),
            DSL.octetLength((Field<String>) null));
        assertEquals(
            DSL.position((String) null, (String) null),
            DSL.position((String) null, (Field<String>) null));
        assertEquals(
            DSL.position((String) null, (String) null),
            DSL.position((Field<String>) null, (String) null));
        assertEquals(
            DSL.position((String) null, (String) null),
            DSL.position((Field<String>) null, (Field<String>) null));
        assertEquals(
            DSL.power((Integer) null, (Integer) null),
            DSL.power((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.power((Integer) null, (Integer) null),
            DSL.power((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.power((Integer) null, (Integer) null),
            DSL.power((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.rad((Integer) null),
            DSL.rad((Field<Integer>) null));
        assertEquals(
            DSL.repeat((String) null, (Field<Integer>) null),
            DSL.repeat((Field<String>) null, (Field<Integer>) null));
        assertEquals(
            DSL.replace((Field<String>) null, (String) null),
            DSL.replace((Field<String>) null, (Field<String>) null));
        assertEquals(
            DSL.replace((Field<String>) null, (String) null, (String) null),
            DSL.replace((Field<String>) null, (Field<String>) null, (Field<String>) null));
        assertEquals(
            DSL.round((Integer) null),
            DSL.round((Field<Integer>) null));
        assertEquals(
            DSL.round((Integer) null, 1),
            DSL.round((Field<Integer>) null, 1));
        assertEquals(
            DSL.rtrim((String) null),
            DSL.rtrim((Field<String>) null));
        assertEquals(
            DSL.second((java.util.Date) null),
            DSL.second((Field<java.util.Date>) null));
        assertEquals(
            DSL.shl((Long) null, (Integer) null),
            DSL.shl((Long) null, (Field<Integer>) null));
        assertEquals(
            DSL.shl((Long) null, (Integer) null),
            DSL.shl((Field<Long>) null, (Integer) null));
        assertEquals(
            DSL.shl((Long) null, (Integer) null),
            DSL.shl((Field<Long>) null, (Field<Integer>) null));
        assertEquals(
            DSL.shr((Long) null, (Integer) null),
            DSL.shr((Long) null, (Field<Integer>) null));
        assertEquals(
            DSL.shr((Long) null, (Integer) null),
            DSL.shr((Field<Long>) null, (Integer) null));
        assertEquals(
            DSL.shr((Long) null, (Integer) null),
            DSL.shr((Field<Long>) null, (Field<Integer>) null));
        assertEquals(
            DSL.sign((Integer) null),
            DSL.sign((Field<Integer>) null));
        assertEquals(
            DSL.sin((Integer) null),
            DSL.sin((Field<Integer>) null));
        assertEquals(
            DSL.sinh((Integer) null),
            DSL.sinh((Field<Integer>) null));
        assertEquals(
            DSL.sqrt((Integer) null),
            DSL.sqrt((Field<Integer>) null));
        assertEquals(
            DSL.tan((Integer) null),
            DSL.tan((Field<Integer>) null));
        assertEquals(
            DSL.tanh((Integer) null),
            DSL.tanh((Field<Integer>) null));
        assertEquals(
            DSL.timestampAdd((Timestamp) null, (Integer) null),
            DSL.timestampAdd((Field<Timestamp>) null, (Field<Integer>) null));
        assertEquals(
            DSL.timestampDiff((Timestamp) null, (Timestamp) null),
            DSL.timestampDiff((Field<Timestamp>) null, (Field<Timestamp>) null));
//        assertEquals(
//            SQL.trunc((Timestamp) null, null),
//            SQL.trunc((Field<Timestamp>) null, null));
        assertEquals(
            DSL.trunc((Integer) null, null),
            DSL.trunc((Field<Integer>) null, null));
        assertEquals(
            DSL.trim((String) null),
            DSL.trim((Field<String>) null));
        assertEquals(
            DSL.upper((String) null),
            DSL.upper((Field<String>) null));
        assertEquals(
            DSL.year((java.util.Date) null),
            DSL.year((Field<java.util.Date>) null));
    }

    @Test
    public void testRowValueExpressions() throws Exception {
        Row1<Integer> t1 = row(1);
        Row2<Integer, String> t2 = row(1, "2");
        Row3<Integer, String, Integer> t3 = row(1, "2", 3);
        Row4<Integer, String, Integer, String> t4 = row(1, "2", 3, "4");
        Row5<Integer, String, Integer, String, Integer> t5 = row(1, "2", 3, "4", 5);
        Row6<Integer, String, Integer, String, Integer, String> t6 = row(1, "2", 3, "4", 5, "6");
        Row7<Integer, String, Integer, String, Integer, String, Integer> t7 = row(1, "2", 3, "4", 5, "6", 7);
        Row8<Integer, String, Integer, String, Integer, String, Integer, String> t8 = row(1, "2", 3, "4", 5, "6", 7, "8");
        RowN t23 = row(1, "2", 3, "4", 5, "6", 7, "8", 9, "10", 11, "12", 13, "14", 15, "16", 17, "18", 19, "20", 21, "22", 23);

        // General info
        assertEquals(1, t1.size());
        assertEquals(2, t2.size());
        assertEquals(3, t3.size());
        assertEquals(4, t4.size());
        assertEquals(5, t5.size());
        assertEquals(6, t6.size());
        assertEquals(7, t7.size());
        assertEquals(8, t8.size());
        assertEquals(23, t23.size());

        // Accessors
        assertEquals(val(1), t1.field1());

        assertEquals(val(1), t2.field1());
        assertEquals(val("2"), t2.field2());

        assertEquals(val(1), t3.field1());
        assertEquals(val("2"), t3.field2());
        assertEquals(val(3), t3.field3());

        assertEquals(val(1), t4.field1());
        assertEquals(val("2"), t4.field2());
        assertEquals(val(3), t4.field3());
        assertEquals(val("4"), t4.field4());

        assertEquals(val(1), t5.field1());
        assertEquals(val("2"), t5.field2());
        assertEquals(val(3), t5.field3());
        assertEquals(val("4"), t5.field4());
        assertEquals(val(5), t5.field5());

        assertEquals(val(1), t6.field1());
        assertEquals(val("2"), t6.field2());
        assertEquals(val(3), t6.field3());
        assertEquals(val("4"), t6.field4());
        assertEquals(val(5), t6.field5());
        assertEquals(val("6"), t6.field6());

        assertEquals(val(1), t7.field1());
        assertEquals(val("2"), t7.field2());
        assertEquals(val(3), t7.field3());
        assertEquals(val("4"), t7.field4());
        assertEquals(val(5), t7.field5());
        assertEquals(val("6"), t7.field6());
        assertEquals(val(7), t7.field7());

        assertEquals(val(1), t8.field1());
        assertEquals(val("2"), t8.field2());
        assertEquals(val(3), t8.field3());
        assertEquals(val("4"), t8.field4());
        assertEquals(val(5), t8.field5());
        assertEquals(val("6"), t8.field6());
        assertEquals(val(7), t8.field7());
        assertEquals(val("8"), t8.field8());

        // Rendering
        assertEquals("(?)", r_ref().render(t1));
        assertEquals("(1)", r_refI().render(t1));
        assertEquals("(?, ?)", r_ref().render(t2));
        assertEquals("(1, '2')", r_refI().render(t2));
        assertEquals("(?, ?, ?)", r_ref().render(t3));
        assertEquals("(1, '2', 3)", r_refI().render(t3));
        assertEquals("(?, ?, ?, ?)", r_ref().render(t4));
        assertEquals("(1, '2', 3, '4')", r_refI().render(t4));
        assertEquals("(?, ?, ?, ?, ?)", r_ref().render(t5));
        assertEquals("(1, '2', 3, '4', 5)", r_refI().render(t5));
        assertEquals("(?, ?, ?, ?, ?, ?)", r_ref().render(t6));
        assertEquals("(1, '2', 3, '4', 5, '6')", r_refI().render(t6));
        assertEquals("(?, ?, ?, ?, ?, ?, ?)", r_ref().render(t7));
        assertEquals("(1, '2', 3, '4', 5, '6', 7)", r_refI().render(t7));
        assertEquals("(?, ?, ?, ?, ?, ?, ?, ?)", r_ref().render(t8));
        assertEquals("(1, '2', 3, '4', 5, '6', 7, '8')", r_refI().render(t8));
        assertEquals("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", r_ref().render(t23));
        assertEquals("(1, '2', 3, '4', 5, '6', 7, '8', 9, '10', 11, '12', 13, '14', 15, '16', 17, '18', 19, '20', 21, '22', 23)", r_refI().render(t23));

        context.checking(new Expectations() {{
            for (int i = 1; i < 22; i += 2) {
                oneOf(statement).setInt(i, i);
                oneOf(statement).setString(i + 1, "" + (i + 1));
            }

            oneOf(statement).setInt(23, 23);
        }});

        assertEquals(24, b_ref().visit(t23).peekIndex());
        context.assertIsSatisfied();
    }

    @Test
    public void testRowCompareConditions() throws Exception {
        assertEquals("(?) = (?)", r_ref().render(row(1).eq(row(1))));
        assertEquals("(1) = (1)", r_refI().render(row(1).eq(row(1))));
        assertEquals("(?, ?) = (?, ?)", r_ref().render(row(1, "2").eq(row(1, "2"))));
        assertEquals("(1, '2') = (1, '2')", r_refI().render(row(1, "2").eq(row(1, "2"))));
        assertEquals("(?, ?, ?) = (?, ?, ?)", r_ref().render(row(1, "2", 3).eq(row(1, "2", 3))));
        assertEquals("(1, '2', 3) = (1, '2', 3)", r_refI().render(row(1, "2", 3).eq(row(1, "2", 3))));
        assertEquals("(?, ?, ?, ?) = (?, ?, ?, ?)", r_ref().render(row(1, "2", 3, "4").eq(row(1, "2", 3, "4"))));
        assertEquals("(1, '2', 3, '4') = (1, '2', 3, '4')", r_refI().render(row(1, "2", 3, "4").eq(row(1, "2", 3, "4"))));
        assertEquals("(?, ?, ?, ?, ?) = (?, ?, ?, ?, ?)", r_ref().render(row(1, "2", 3, "4", 5).eq(row(1, "2", 3, "4", 5))));
        assertEquals("(1, '2', 3, '4', 5) = (1, '2', 3, '4', 5)", r_refI().render(row(1, "2", 3, "4", 5).eq(row(1, "2", 3, "4", 5))));
        assertEquals("(?, ?, ?, ?, ?, ?) = (?, ?, ?, ?, ?, ?)", r_ref().render(row(1, "2", 3, "4", 5, "6").eq(row(1, "2", 3, "4", 5, "6"))));
        assertEquals("(1, '2', 3, '4', 5, '6') = (1, '2', 3, '4', 5, '6')", r_refI().render(row(1, "2", 3, "4", 5, "6").eq(row(1, "2", 3, "4", 5, "6"))));
        assertEquals("(?, ?, ?, ?, ?, ?, ?) = (?, ?, ?, ?, ?, ?, ?)", r_ref().render(row(1, "2", 3, "4", 5, "6", 7).eq(row(1, "2", 3, "4", 5, "6", 7))));
        assertEquals("(1, '2', 3, '4', 5, '6', 7) = (1, '2', 3, '4', 5, '6', 7)", r_refI().render(row(1, "2", 3, "4", 5, "6", 7).eq(row(1, "2", 3, "4", 5, "6", 7))));
        assertEquals("(?, ?, ?, ?, ?, ?, ?, ?) = (?, ?, ?, ?, ?, ?, ?, ?)", r_ref().render(row(1, "2", 3, "4", 5, "6", 7, "8").eq(row(1, "2", 3, "4", 5, "6", 7, "8"))));
        assertEquals("(1, '2', 3, '4', 5, '6', 7, '8') = (1, '2', 3, '4', 5, '6', 7, '8')", r_refI().render(row(1, "2", 3, "4", 5, "6", 7, "8").eq(row(1, "2", 3, "4", 5, "6", 7, "8"))));
        assertEquals("(?, ?, ?, ?, ?, ?, ?, ?, ?) = (?, ?, ?, ?, ?, ?, ?, ?, ?)", r_ref().render(row(1, "2", 3, "4", 5, "6", 7, "8", 9).eq(row(1, "2", 3, "4", 5, "6", 7, "8", 9))));
        assertEquals("(1, '2', 3, '4', 5, '6', 7, '8', 9) = (1, '2', 3, '4', 5, '6', 7, '8', 9)", r_refI().render(row(1, "2", 3, "4", 5, "6", 7, "8", 9).eq(row(1, "2", 3, "4", 5, "6", 7, "8", 9))));

        context.checking(new Expectations() {{
            int i = 0;

            oneOf(statement).setInt(++i, i);
            oneOf(statement).setString(++i, "" + i);
            oneOf(statement).setInt(++i, i);
            oneOf(statement).setString(++i, "" + i);
            oneOf(statement).setInt(++i, i);
            oneOf(statement).setString(++i, "" + i);
            oneOf(statement).setInt(++i, i);
            oneOf(statement).setString(++i, "" + i);
            oneOf(statement).setInt(++i, i);

            oneOf(statement).setInt(++i, (i - 9));
            oneOf(statement).setString(++i, "" + (i - 9));
            oneOf(statement).setInt(++i, (i - 9));
            oneOf(statement).setString(++i, "" + (i - 9));
            oneOf(statement).setInt(++i, (i - 9));
            oneOf(statement).setString(++i, "" + (i - 9));
            oneOf(statement).setInt(++i, (i - 9));
            oneOf(statement).setString(++i, "" + (i - 9));
            oneOf(statement).setInt(++i, (i - 9));
        }});

        assertEquals(19, b_ref().visit(row(1, "2", 3, "4", 5, "6", 7, "8", 9).eq(1, "2", 3, "4", 5, "6", 7, "8", 9)).peekIndex());
        context.assertIsSatisfied();
    }

    @Test
    public void testAliasing() throws Exception {
        assertEquals("`TABLE1`", r_decT().render(TABLE1));
        assertEquals("`TABLE1`", r_decF().render(TABLE1));
        assertEquals("`TABLE1`", r_ref().render(TABLE1));

        assertEquals("`TABLE1` as `t1`", r_decT().render(TABLE1.as("t1")));
        assertEquals("`t1`",            r_decF().render(TABLE1.as("t1")));
        assertEquals("`t1`",            r_ref().render(TABLE1.as("t1")));

        assertEquals("`TABLE1`.`ID1`", r_decF().render(TABLE1.field(FIELD_ID1)));
        assertEquals("`TABLE1`.`ID1`", r_decT().render(TABLE1.field(FIELD_ID1)));
        assertEquals("`TABLE1`.`ID1`", r_ref().render(TABLE1.field(FIELD_ID1)));

        assertEquals("`TABLE1`.`ID1` as `f1`", r_decF().render(TABLE1.field(FIELD_ID1).as("f1")));
        assertEquals("`f1`",                    r_decT().render(TABLE1.field(FIELD_ID1).as("f1")));
        assertEquals("`f1`",                    r_ref().render(TABLE1.field(FIELD_ID1).as("f1")));

        assertEquals("`t1`.`ID1`", r_decF().render(TABLE1.as("t1").field(FIELD_ID1)));
        assertEquals("`t1`.`ID1`", r_decT().render(TABLE1.as("t1").field(FIELD_ID1)));
        assertEquals("`t1`.`ID1`", r_ref().render(TABLE1.as("t1").field(FIELD_ID1)));

        assertEquals("`t1`.`ID1` as `f1`", r_decF().render(TABLE1.as("t1").field(FIELD_ID1).as("f1")));
        assertEquals("`f1`",                r_decT().render(TABLE1.as("t1").field(FIELD_ID1).as("f1")));
        assertEquals("`f1`",                r_ref().render(TABLE1.as("t1").field(FIELD_ID1).as("f1")));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 2);
            oneOf(statement).setInt(1, 4);
        }});

        int f1 = b_ref().visit(val(1).as("c1")).peekIndex();
        int f2 = b_decF().visit(val(2).as("c2")).peekIndex();
        int f3 = b_decT().visit(val(2).as("c2")).peekIndex();
        int t1 = b_ref().visit(create.select(val(3)).asTable("t1")).peekIndex();
        int t2 = b_decF().visit(create.select(val(4)).asTable("t2")).peekIndex();
        int t3 = b_decT().visit(create.select(val(4)).asTable("t2")).peekIndex();

        assertEquals(1, f1);
        assertEquals(2, f2);
        assertEquals(1, f3);

        assertEquals(1, t1);
        assertEquals(1, t2);
        assertEquals(2, t3);

        context.assertIsSatisfied();
    }

    @Test
    public void testMultipleCombinedCondition() throws Exception {
        Condition c1 = FIELD_ID1.equal(10);
        Condition c2 = FIELD_ID2.equal(20);
        Condition c3 = FIELD_ID1.equal(30);
        Condition c4 = FIELD_ID2.equal(40);

        Condition c = c1.and(c2).or(c3.and(c4));
        assertEquals("((`TABLE1`.`ID1` = 10 and `TABLE2`.`ID2` = 20) or (`TABLE1`.`ID1` = 30 and `TABLE2`.`ID2` = 40))", r_refI().render(c));
        assertEquals("((`TABLE1`.`ID1` = ? and `TABLE2`.`ID2` = ?) or (`TABLE1`.`ID1` = ? and `TABLE2`.`ID2` = ?))", r_ref().render(c));

        c = c1.and(c2).or(c3).and(c4);
        assertEquals("(((`TABLE1`.`ID1` = 10 and `TABLE2`.`ID2` = 20) or `TABLE1`.`ID1` = 30) and `TABLE2`.`ID2` = 40)", r_refI().render(c));
        assertEquals("(((`TABLE1`.`ID1` = ? and `TABLE2`.`ID2` = ?) or `TABLE1`.`ID1` = ?) and `TABLE2`.`ID2` = ?)", r_ref().render(c));

        c = c1.and(c2).and(c3).or(c4);
        assertEquals("((`TABLE1`.`ID1` = 10 and `TABLE2`.`ID2` = 20 and `TABLE1`.`ID1` = 30) or `TABLE2`.`ID2` = 40)", r_refI().render(c));
        assertEquals("((`TABLE1`.`ID1` = ? and `TABLE2`.`ID2` = ? and `TABLE1`.`ID1` = ?) or `TABLE2`.`ID2` = ?)", r_ref().render(c));

        c = c1.and(c2).and(c3).and(c4);
        assertEquals("(`TABLE1`.`ID1` = 10 and `TABLE2`.`ID2` = 20 and `TABLE1`.`ID1` = 30 and `TABLE2`.`ID2` = 40)", r_refI().render(c));
        assertEquals("(`TABLE1`.`ID1` = ? and `TABLE2`.`ID2` = ? and `TABLE1`.`ID1` = ? and `TABLE2`.`ID2` = ?)", r_ref().render(c));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setInt(2, 20);
            oneOf(statement).setInt(3, 30);
            oneOf(statement).setInt(4, 40);
        }});

        int i = b_ref().visit(c).peekIndex();
        assertEquals(5, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testQueryPartByName() throws Exception {
        Field<Object> field = field(name("A", "b", "';`"));
        Table<Record> table = table(name("A", "b", "';`"));

        assertEquals("`A`.`b`.`';```", r_ref().render(field));
        assertEquals("`A`.`b`.`';```", r_refI().render(field));
        assertEquals("`A`.`b`.`';```", r_ref().render(table));
        assertEquals("`A`.`b`.`';```", r_refI().render(table));
    }

    @Test
    public void testQueryPartByNameAndConditions() throws Exception {
        List<String> v1 = Arrays.asList("1", "2");
        Condition c1 = field(name("A", "b"), String.class).in(v1);

        assertEquals("`A`.`b` in (?, ?)", r_ref().render(c1));
        assertEquals("`A`.`b` in ('1', '2')", r_refI().render(c1));

        Set<String> v2 = new TreeSet<String>(Arrays.asList("1", "2"));
        Condition c2 = field(name("A", "b"), String.class).in(v2);

        assertEquals("`A`.`b` in (?, ?)", r_ref().render(c2));
        assertEquals("`A`.`b` in ('1', '2')", r_refI().render(c2));
    }

    @Test
    public void testPlainSQLInPredicate() throws Exception {
        List<String> v1 = Arrays.asList("1", "2");
        Condition c1 = field("f").in(v1);

        assertEquals("f in (?, ?)", r_ref().render(c1));
        assertEquals("f in ('1', '2')", r_refI().render(c1));

        Set<String> v2 = new TreeSet<String>(Arrays.asList("1", "2"));
        Condition c2 = field("f").in(v2);

        assertEquals("f in (?, ?)", r_ref().render(c2));
        assertEquals("f in ('1', '2')", r_refI().render(c2));
    }

    @Test
    public void testPlainSQLCondition() throws Exception {
        Condition c1 = condition("TABLE1.ID = 10");
        Condition c2 = condition("TABLE1.ID = ? and TABLE2.ID = ?", 10, "20");

        assertEquals("(TABLE1.ID = 10)", r_refI().render(c1));
        assertEquals("(TABLE1.ID = 10)", r_ref().render(c1));

        assertEquals("(TABLE1.ID = 10 and TABLE2.ID = '20')", r_refI().render(c2));
        assertEquals("(TABLE1.ID = ? and TABLE2.ID = ?)", r_ref().render(c2));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setString(2, "20");
        }});

        int i = b_ref().visit(c2).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testPlainSQLDebugLogging() throws Exception {
        // Some corner case for correct tokenisation of string literals and bind variables
        Field<?> f = field("Hello ? 'Hello ?' Is there anybody '' ? ' out there '' ? '", "A", "B");
        assertEquals("Hello 'A' 'Hello ?' Is there anybody '' 'B' ' out there '' ? '", r_refI().render(f));
    }

    @Test
    public void testCustomCondition() throws Exception {
        Condition c = new CustomCondition() {
            private static final long serialVersionUID = 6302350477408137757L;

            @Override
            public void toSQL(RenderContext ctx) {
                if (ctx.paramType() == INLINED) {
                    ctx.sql("1 = 1");
                } else {
                    ctx.sql("? = ?");
                }
            }

            @Override
            public void bind(BindContext ctx) {
                try {
                    ctx.statement().setInt(ctx.nextIndex(), 1);
                    ctx.bindValue(1, DSL.val(1));
                }
                catch (SQLException ignore) {}
            }
        };

        assertEquals("1 = 1", r_refI().render(c));
        assertEquals("? = ?", r_ref().render(c));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 1);
        }});

        int i = b_ref().visit(c).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testPlainSQLComments() throws Exception {
        assertEquals("SELECT 1 FROM DUAL; -- comment without newline",
            r_refI().render(create.resultQuery("SELECT 1 FROM DUAL; -- comment without newline")));

        Field<?> f = field(
            "-- comment ? '\n" +
            "/* another comment ? '\n" +
            "   continuing -- */" +
            "a bind value : ? /* a comment : ? */ another bind value : ?", "a", "b");

        assertEquals(
            "-- comment ? '\n" +
            "/* another comment ? '\n" +
            "   continuing -- */" +
            "a bind value : 'a' /* a comment : ? */ another bind value : 'b'", r_refI().render(f));
        assertEquals(
            "-- comment ? '\n" +
            "/* another comment ? '\n" +
            "   continuing -- */" +
            "a bind value : ? /* a comment : ? */ another bind value : ?", r_ref().render(f));

        context.checking(new Expectations() {{
            oneOf(statement).setString(1, "a");
            oneOf(statement).setString(2, "b");
        }});

        int i = b_ref().visit(f).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testPlainSQLField() throws Exception {
        Field<?> f1 = field("DECODE(TABLE1.ID, 1, 'a', 'b')");
        Field<?> f2 = field("DECODE(TABLE1.ID, 1, ?, ?)", "a", "b");

        assertEquals("DECODE(TABLE1.ID, 1, 'a', 'b')", r_refI().render(f1));
        assertEquals("DECODE(TABLE1.ID, 1, 'a', 'b')", r_ref().render(f1));

        assertEquals("DECODE(TABLE1.ID, 1, 'a', 'b')", r_refI().render(f2));
        assertEquals("DECODE(TABLE1.ID, 1, ?, ?)", r_ref().render(f2));

        context.checking(new Expectations() {{
            oneOf(statement).setString(1, "a");
            oneOf(statement).setString(2, "b");
        }});

        int i = b_ref().visit(f2).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testCustomField() throws Exception {
        Field<?> f = new CustomField<Integer>("test", SQLDataType.INTEGER) {
            private static final long serialVersionUID = 1L;

            @Override
            public void toSQL(RenderContext ctx) {
                if (ctx.paramType() == INLINED) {
                    ctx.sql('1');
                } else {
                    ctx.sql('?');
                }
            }

            @Override
            public void bind(BindContext ctx) {
                ctx.bindValue(1, DSL.val(1));
            }
        };

        assertEquals("1", r_refI().render(f));
        assertEquals("?", r_ref().render(f));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
        }});

        int i = b_ref().visit(f).peekIndex();
        assertEquals(2, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testConstantFunction() throws Exception {
        Field<Integer> f1 = val(Integer.valueOf(1));
        assertEquals(Integer.class, f1.getType());
        assertEquals("1", r_refI().render(f1));
        assertEquals("?", r_ref().render(f1));
        assertEquals("1", r_decI().render(f1));
        assertEquals("?", r_dec().render(f1));

        Field<String> f2 = val("test's");
        assertEquals(String.class, f2.getType());
        assertEquals("'test''s'", r_refI().render(f2));
        assertEquals("?", r_ref().render(f2));
        assertEquals("'test''s'", r_decI().render(f2));
        assertEquals("?", r_dec().render(f2));

        Field<Integer> f3 = val(Integer.valueOf(1)).as("value");
        assertEquals(Integer.class, f3.getType());
        assertEquals("`value`", r_refI().render(f3));
        assertEquals("`value`", r_ref().render(f3));
        assertEquals("1 as `value`", r_decI().render(f3));
        assertEquals("? as `value`", r_dec().render(f3));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setString(1, "test's");
            oneOf(statement).setInt(1, 1);
        }});

        int i = b_decF().visit(f1).peekIndex();
        int j = b_decF().visit(f2).peekIndex();
        int k = b_decF().visit(f3).peekIndex();

        assertEquals(2, i);
        assertEquals(2, j);
        assertEquals(2, k);

        context.assertIsSatisfied();
    }

    @Test
    public void testNamedParams() throws Exception {
        Query q1 = create.select(val(1)).from(TABLE1).where(FIELD_ID1.equal(val(2)));
        Query q2 = create.select(param("p1", 1)).from(TABLE1).where(FIELD_ID1.equal(param("p2", 2)));
        Query q3 = create.select(param("p1", 1)).from(TABLE1).where(FIELD_ID1.equal(2));
        Query q4 = create.select(val(1)).from(TABLE1).where(FIELD_ID1.equal(param("p2", 2)));

        assertEquals("select 1 from `TABLE1` where `TABLE1`.`ID1` = 2", r_refI().render(q1));
        assertEquals("select :1 from `TABLE1` where `TABLE1`.`ID1` = :2", r_refP().render(q1));
        assertEquals("select ? from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref().render(q1));

        assertEquals("select 1 from `TABLE1` where `TABLE1`.`ID1` = 2", r_refI().render(q2));
        assertEquals("select :p1 from `TABLE1` where `TABLE1`.`ID1` = :p2", r_refP().render(q2));
        assertEquals("select ? from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref().render(q2));

        assertEquals("select 1 from `TABLE1` where `TABLE1`.`ID1` = 2", r_refI().render(q3));
        assertEquals("select :p1 from `TABLE1` where `TABLE1`.`ID1` = :2", r_refP().render(q3));
        assertEquals("select ? from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref().render(q3));

        assertEquals("select 1 from `TABLE1` where `TABLE1`.`ID1` = 2", r_refI().render(q4));
        assertEquals("select :1 from `TABLE1` where `TABLE1`.`ID1` = :p2", r_refP().render(q4));
        assertEquals("select ? from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref().render(q4));

        // Param / Val queries should be equal as toString() doesn't consider params
        assertEquals(q1, q2);
        assertEquals(q1, q3);
        assertEquals(q1, q4);

        // Params
        Param<?> p11 = q1.getParam("1");
        Param<?> p21 = q2.getParam("p1");
        Param<?> p31 = q3.getParam("p1");
        Param<?> p41 = q4.getParam("1");

        Param<?> p12 = q1.getParam("2");
        Param<?> p22 = q2.getParam("p2");
        Param<?> p32 = q3.getParam("2");
        Param<?> p42 = q4.getParam("p2");

        assertEquals(p11, create.extractParam(q1, "1"));
        assertEquals(p21, create.extractParam(q2, "p1"));
        assertEquals(p31, create.extractParam(q3, "p1"));
        assertEquals(p41, create.extractParam(q4, "1"));

        assertEquals(p12, create.extractParam(q1, "2"));
        assertEquals(p22, create.extractParam(q2, "p2"));
        assertEquals(p32, create.extractParam(q3, "2"));
        assertEquals(p42, create.extractParam(q4, "p2"));

        assertEquals(Arrays.asList("1", "2"), new ArrayList<String>(create.extractParams(q1).keySet()));
        assertEquals(Arrays.asList("p1", "p2"), new ArrayList<String>(create.extractParams(q2).keySet()));
        assertEquals(Arrays.asList("p1", "2"), new ArrayList<String>(create.extractParams(q3).keySet()));
        assertEquals(Arrays.asList("1", "p2"), new ArrayList<String>(create.extractParams(q4).keySet()));

        assertEquals(Arrays.asList("1", "2"), new ArrayList<String>(create.extractParams(q1).keySet()));
        assertEquals(Arrays.asList("p1", "p2"), new ArrayList<String>(create.extractParams(q2).keySet()));
        assertEquals(Arrays.asList("p1", "2"), new ArrayList<String>(create.extractParams(q3).keySet()));
        assertEquals(Arrays.asList("1", "p2"), new ArrayList<String>(create.extractParams(q4).keySet()));

        // Types
        assertEquals(Integer.class, p11.getType());
        assertEquals(Integer.class, p21.getType());
        assertEquals(Integer.class, p31.getType());
        assertEquals(Integer.class, p41.getType());

        assertEquals(Integer.class, p12.getType());
        assertEquals(Integer.class, p22.getType());
        assertEquals(Integer.class, p32.getType());
        assertEquals(Integer.class, p42.getType());

        // Values
        assertEquals(Integer.valueOf(1), p11.getValue());
        assertEquals(Integer.valueOf(1), p21.getValue());
        assertEquals(Integer.valueOf(1), p31.getValue());
        assertEquals(Integer.valueOf(1), p41.getValue());

        assertEquals(Integer.valueOf(2), p12.getValue());
        assertEquals(Integer.valueOf(2), p22.getValue());
        assertEquals(Integer.valueOf(2), p32.getValue());
        assertEquals(Integer.valueOf(2), p42.getValue());

        // Param replacement
        p11.setConverted(3);
        p21.setConverted(3);
        p31.setConverted(3);
        p41.setConverted(3);

        p12.setConverted(4);
        p22.setConverted(4);
        p32.setConverted(4);
        p42.setConverted(4);

        assertEquals("select 3 from `TABLE1` where `TABLE1`.`ID1` = 4", r_refI().render(q1));
        assertEquals("select :1 from `TABLE1` where `TABLE1`.`ID1` = :2", r_refP().render(q1));
        assertEquals("select ? from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref().render(q1));

        assertEquals("select 3 from `TABLE1` where `TABLE1`.`ID1` = 4", r_refI().render(q2));
        assertEquals("select :p1 from `TABLE1` where `TABLE1`.`ID1` = :p2", r_refP().render(q2));
        assertEquals("select ? from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref().render(q2));

        assertEquals("select 3 from `TABLE1` where `TABLE1`.`ID1` = 4", r_refI().render(q3));
        assertEquals("select :p1 from `TABLE1` where `TABLE1`.`ID1` = :2", r_refP().render(q3));
        assertEquals("select ? from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref().render(q3));

        assertEquals("select 3 from `TABLE1` where `TABLE1`.`ID1` = 4", r_refI().render(q4));
        assertEquals("select :1 from `TABLE1` where `TABLE1`.`ID1` = :p2", r_refP().render(q4));
        assertEquals("select ? from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref().render(q4));
    }

    @Test
    public void testInlinedBindValues() {
        Param<String> i1 = inline("abc'def");
        Param<Integer> i2 = inline(123);
        Param<Double> i3 = inline(123.0);
        Param<Date> i4 = inline(Date.valueOf("1981-07-10"));

        RenderContext r_refI = r_refI();
        RenderContext r_refP = r_refP();
        RenderContext r_ref = r_ref();

        assertEquals("'abc''def'", r_refI.render(i1));
        assertEquals("'abc''def'", r_refP.render(i1));
        assertEquals("'abc''def'", r_ref.render(i1));

        assertEquals("123", r_refI.render(i2));
        assertEquals("123", r_refP.render(i2));
        assertEquals("123", r_ref.render(i2));

        assertEquals("123.0", r_refI.render(i3));
        assertEquals("123.0", r_refP.render(i3));
        assertEquals("123.0", r_ref.render(i3));

        assertEquals("{d '1981-07-10'}", r_refI.render(i4));
        assertEquals("{d '1981-07-10'}", r_refP.render(i4));
        assertEquals("{d '1981-07-10'}", r_ref.render(i4));
    }

    @Test
    public void testPlaceholders1() throws Exception {

        // [#1593] Check if reordering is possible
        Field<Object> f1 = field("{1} + {0}", val(1), val(2));

        assertEquals("? + ?", r_ref().render(f1));
        assertEquals("2 + 1", r_refI().render(f1));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 2);
            oneOf(statement).setInt(2, 1);
        }});

        int i = b_ref().visit(f1).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testPlaceholders2() throws Exception {

        // [#1593] Check if repetition is possible
        Field<Object> f1 = field("{0} + {0}", val(1));

        assertEquals("? + ?", r_ref().render(f1));
        assertEquals("1 + 1", r_refI().render(f1));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 1);
        }});

        int i = b_ref().visit(f1).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testRenderNameStyle() {
        Query q = create.select(val(1)).from(TABLE1).where(FIELD_ID1.equal(2));

        RenderContext r_refI = r_refI();
        RenderContext r_refP = r_refP();
        RenderContext r_ref = r_ref();

        r_refI.configuration().settings().setRenderNameStyle(RenderNameStyle.AS_IS);
        r_refP.configuration().settings().setRenderNameStyle(RenderNameStyle.AS_IS);
        r_ref.configuration().settings().setRenderNameStyle(RenderNameStyle.AS_IS);

        assertEquals("select 1 from TABLE1 where TABLE1.ID1 = 2", r_refI.render(q));
        assertEquals("select :1 from TABLE1 where TABLE1.ID1 = :2", r_refP.render(q));
        assertEquals("select ? from TABLE1 where TABLE1.ID1 = ?", r_ref.render(q));

        r_refI.configuration().settings().setRenderNameStyle(RenderNameStyle.LOWER);
        r_refP.configuration().settings().setRenderNameStyle(RenderNameStyle.LOWER);
        r_ref.configuration().settings().setRenderNameStyle(RenderNameStyle.LOWER);

        assertEquals("select 1 from table1 where table1.id1 = 2", r_refI.render(q));
        assertEquals("select :1 from table1 where table1.id1 = :2", r_refP.render(q));
        assertEquals("select ? from table1 where table1.id1 = ?", r_ref.render(q));

        r_refI.configuration().settings().setRenderNameStyle(RenderNameStyle.UPPER);
        r_refP.configuration().settings().setRenderNameStyle(RenderNameStyle.UPPER);
        r_ref.configuration().settings().setRenderNameStyle(RenderNameStyle.UPPER);

        assertEquals("select 1 from TABLE1 where TABLE1.ID1 = 2", r_refI.render(q));
        assertEquals("select :1 from TABLE1 where TABLE1.ID1 = :2", r_refP.render(q));
        assertEquals("select ? from TABLE1 where TABLE1.ID1 = ?", r_ref.render(q));

        r_refI.configuration().settings().setRenderNameStyle(RenderNameStyle.QUOTED);
        r_refP.configuration().settings().setRenderNameStyle(RenderNameStyle.QUOTED);
        r_ref.configuration().settings().setRenderNameStyle(RenderNameStyle.QUOTED);

        assertEquals("select 1 from `TABLE1` where `TABLE1`.`ID1` = 2", r_refI.render(q));
        assertEquals("select :1 from `TABLE1` where `TABLE1`.`ID1` = :2", r_refP.render(q));
        assertEquals("select ? from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref.render(q));
    }

    @Test
    public void testRenderNameStyleWithSpecialCharacters() {
        Query q = create.select(val(1).as("Aa `Bb` Cc")).from(TABLE1.as("Xx ''Yy''\\ Zz"));

        RenderContext r_refI = r_refI();

        r_refI.configuration().settings().setRenderNameStyle(RenderNameStyle.AS_IS);
        assertEquals("select 1 as Aa `Bb` Cc from TABLE1 as Xx ''Yy''\\ Zz", r_refI.render(q));

        r_refI.configuration().settings().setRenderNameStyle(RenderNameStyle.LOWER);
        assertEquals("select 1 as aa `bb` cc from table1 as xx ''yy''\\ zz", r_refI.render(q));

        r_refI.configuration().settings().setRenderNameStyle(RenderNameStyle.UPPER);
        assertEquals("select 1 as AA `BB` CC from TABLE1 as XX ''YY''\\ ZZ", r_refI.render(q));

        r_refI.configuration().settings().setRenderNameStyle(RenderNameStyle.QUOTED);
        assertEquals("select 1 as `Aa ``Bb`` Cc` from `TABLE1` as `Xx ''Yy''\\ Zz`", r_refI.render(q));
    }

    @Test
    public void testRenderKeywordStyle() {
        Query q = create.select(val(1)).from(TABLE1).where(FIELD_ID1.equal(2));

        RenderContext r_refI = r_refI();
        RenderContext r_refP = r_refP();
        RenderContext r_ref = r_ref();

        r_refI.configuration().settings().setRenderKeywordStyle(RenderKeywordStyle.UPPER);
        r_refP.configuration().settings().setRenderKeywordStyle(RenderKeywordStyle.UPPER);
        r_ref.configuration().settings().setRenderKeywordStyle(RenderKeywordStyle.UPPER);

        assertEquals("SELECT 1 FROM `TABLE1` WHERE `TABLE1`.`ID1` = 2", r_refI.render(q));
        assertEquals("SELECT :1 FROM `TABLE1` WHERE `TABLE1`.`ID1` = :2", r_refP.render(q));
        assertEquals("SELECT ? FROM `TABLE1` WHERE `TABLE1`.`ID1` = ?", r_ref.render(q));

        r_refI.configuration().settings().setRenderKeywordStyle(RenderKeywordStyle.LOWER);
        r_refP.configuration().settings().setRenderKeywordStyle(RenderKeywordStyle.LOWER);
        r_ref.configuration().settings().setRenderKeywordStyle(RenderKeywordStyle.LOWER);

        assertEquals("select 1 from `TABLE1` where `TABLE1`.`ID1` = 2", r_refI.render(q));
        assertEquals("select :1 from `TABLE1` where `TABLE1`.`ID1` = :2", r_refP.render(q));
        assertEquals("select ? from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref.render(q));
    }
}
