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

package org.jooq.test;

import static org.jooq.JoinType.LEFT_OUTER_JOIN;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.decode;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.fieldByName;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.param;
import static org.jooq.impl.DSL.replace;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.tableByName;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.val;
import static org.jooq.test.data.Table1.FIELD_DATE1;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;
import static org.jooq.test.data.Table2.FIELD_ID2;
import static org.jooq.test.data.Table2.TABLE2;
import static org.jooq.test.data.Table3.FIELD_ID3;
import static org.jooq.test.data.Table3.TABLE3;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.BindContext;
import org.jooq.Case;
import org.jooq.CaseConditionStep;
import org.jooq.CaseValueStep;
import org.jooq.CaseWhenStep;
import org.jooq.Condition;
import org.jooq.DatePart;
import org.jooq.DeleteQuery;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.InsertQuery;
import org.jooq.Merge;
import org.jooq.Operator;
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
import org.jooq.Select;
import org.jooq.SelectFinalStep;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.Truncate;
import org.jooq.UpdateQuery;
import org.jooq.conf.RenderKeywordStyle;
import org.jooq.conf.RenderNameStyle;
import org.jooq.impl.CustomCondition;
import org.jooq.impl.CustomField;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.test.data.Table1Record;

import org.jmock.Expectations;
import org.junit.Test;

/**
 * A test suite for basic jOOQ functionality
 *
 * @author Lukas Eder
 */
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
            DSL.shl((Integer) null, (Integer) null),
            DSL.shl((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.shl((Integer) null, (Integer) null),
            DSL.shl((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.shl((Integer) null, (Integer) null),
            DSL.shl((Field<Integer>) null, (Field<Integer>) null));
        assertEquals(
            DSL.shr((Integer) null, (Integer) null),
            DSL.shr((Integer) null, (Field<Integer>) null));
        assertEquals(
            DSL.shr((Integer) null, (Integer) null),
            DSL.shr((Field<Integer>) null, (Integer) null));
        assertEquals(
            DSL.shr((Integer) null, (Integer) null),
            DSL.shr((Field<Integer>) null, (Field<Integer>) null));
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
    public void testTruncate() throws Exception {
        Truncate<Table1Record> t = create.truncate(TABLE1);

        assertEquals("truncate table `TABLE1`", r_dec().render(t));
        assertEquals("truncate table `TABLE1`", r_ref().render(t));
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
    public void testBetweenCondition() throws Exception {
        Condition c = FIELD_ID1.between(1, 10);
        assertEquals("`TABLE1`.`ID1` between 1 and 10", r_refI().render(c));
        assertEquals("`TABLE1`.`ID1` between ? and ?", r_ref().render(c));

        c = FIELD_ID1.notBetween(1, 10);
        assertEquals("`TABLE1`.`ID1` not between 1 and 10", r_refI().render(c));
        assertEquals("`TABLE1`.`ID1` not between ? and ?", r_ref().render(c));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 10);
        }});

        int i = b_ref().visit(c).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testInCondition() throws Exception {
        Condition c = FIELD_ID1.in(new Integer[0]);
        assertEquals(falseCondition(), c);

        c = FIELD_ID1.notIn(new Integer[0]);
        assertEquals(trueCondition(), c);

        c = FIELD_ID1.in(1, 10);
        assertEquals("`TABLE1`.`ID1` in (1, 10)", r_refI().render(c));
        assertEquals("`TABLE1`.`ID1` in (?, ?)", r_ref().render(c));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 10);
        }});

        int i = b_ref().visit(c).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testInSelectCondition() throws Exception {
        Condition c = FIELD_ID1.in(select(FIELD_ID1).from(TABLE1).where(FIELD_NAME1.equal("x")));
        assertEquals("`TABLE1`.`ID1` in (select `TABLE1`.`ID1` from `TABLE1` where `TABLE1`.`NAME1` = 'x')", r_refI().render(c));
        assertEquals("`TABLE1`.`ID1` in (select `TABLE1`.`ID1` from `TABLE1` where `TABLE1`.`NAME1` = ?)", r_ref().render(c));

        c = FIELD_ID1.notIn(select(FIELD_ID1).from(TABLE1).where(FIELD_NAME1.equal("x")));
        assertEquals("`TABLE1`.`ID1` not in (select `TABLE1`.`ID1` from `TABLE1` where `TABLE1`.`NAME1` = 'x')", r_refI().render(c));
        assertEquals("`TABLE1`.`ID1` not in (select `TABLE1`.`ID1` from `TABLE1` where `TABLE1`.`NAME1` = ?)", r_ref().render(c));

        context.checking(new Expectations() {{
            oneOf(statement).setString(1, "x");
        }});

        int i = b_ref().visit(c).peekIndex();
        assertEquals(2, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testCompareCondition() throws Exception {
        Condition c = FIELD_ID1.equal(10);
        assertEquals("`TABLE1`.`ID1` = 10", r_refI().render(c));
        assertEquals("`TABLE1`.`ID1` = ?", r_ref().render(c));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
        }});

        int i = b_ref().visit(c).peekIndex();
        assertEquals(2, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testNotCondition() throws Exception {
        Condition c1 = FIELD_ID1.equal(10).not();
        Condition c2 = not(FIELD_ID1.equal(10));
        assertEquals(c1, c2);

        for (Condition c : Arrays.asList(c1, c2)) {
            assertEquals("not(`TABLE1`.`ID1` = 10)", r_refI().render(c));
            assertEquals("not(`TABLE1`.`ID1` = ?)", r_ref().render(c));

            assertEquals("not(not(`TABLE1`.`ID1` = 10))", r_refI().render(c.not()));
            assertEquals("not(not(`TABLE1`.`ID1` = ?))", r_ref().render(c.not()));

            context.checking(new Expectations() {{
                oneOf(statement).setInt(1, 10);
            }});

            int i = b_ref().visit(c).peekIndex();
            assertEquals(2, i);

            context.assertIsSatisfied();
        }

        // [#1771] Negated combined conditions shouldn't render extra parentheses
        Condition c = not(val(1).eq(1).and(val(2).eq(2)));
        assertEquals("not((1 = 1 and 2 = 2))", r_refI().render(c));
        assertEquals("not((? = ? and ? = ?))", r_ref().render(c));
    }

    @Test
    public void testLikeCondition() throws Exception {
        Condition c1 = FIELD_NAME1.like("%a%");
        assertEquals("`TABLE1`.`NAME1` like '%a%'", r_refI().render(c1));
        assertEquals("`TABLE1`.`NAME1` like ?", r_ref().render(c1));

        Condition c2 = FIELD_NAME1.notLike("%a%");
        assertEquals("`TABLE1`.`NAME1` not like '%a%'", r_refI().render(c2));
        assertEquals("`TABLE1`.`NAME1` not like ?", r_ref().render(c2));

        Condition c3 = FIELD_NAME1.like("%a%", '!');
        assertEquals("`TABLE1`.`NAME1` like '%a%' escape '!'", r_refI().render(c3));
        assertEquals("`TABLE1`.`NAME1` like ? escape '!'", r_ref().render(c3));

        Condition c4 = FIELD_NAME1.notLike("%a%", '!');
        assertEquals("`TABLE1`.`NAME1` not like '%a%' escape '!'", r_refI().render(c4));
        assertEquals("`TABLE1`.`NAME1` not like ? escape '!'", r_ref().render(c4));
    }

    @Test
    public void testQueryPartByName() throws Exception {
        Field<Object> field = fieldByName("A", "b", "';`");
        Table<Record> table = tableByName("A", "b", "';`");

        assertEquals("`A`.`b`.`';```", r_ref().render(field));
        assertEquals("`A`.`b`.`';```", r_refI().render(field));
        assertEquals("`A`.`b`.`';```", r_ref().render(table));
        assertEquals("`A`.`b`.`';```", r_refI().render(table));
    }

    @Test
    public void testQueryPartByNameAndConditions() throws Exception {
        List<String> v1 = Arrays.asList("1", "2");
        Condition c1 = fieldByName(String.class, "A", "b").in(v1);

        assertEquals("`A`.`b` in (?, ?)", r_ref().render(c1));
        assertEquals("`A`.`b` in ('1', '2')", r_refI().render(c1));

        Set<String> v2 = new TreeSet<String>(Arrays.asList("1", "2"));
        Condition c2 = fieldByName(String.class, "A", "b").in(v2);

        assertEquals("`A`.`b` in (?, ?)", r_ref().render(c2));
        assertEquals("`A`.`b` in ('1', '2')", r_refI().render(c2));
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
                    ctx.sql("1");
                } else {
                    ctx.sql("?");
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
    public void testIsNullCondition() throws Exception {
        Condition c1 = FIELD_ID1.isNull();
        assertEquals("`TABLE1`.`ID1` is null", r_refI().render(c1));
        assertEquals("`TABLE1`.`ID1` is null", r_ref().render(c1));

        Condition c2 = FIELD_ID1.isNotNull();
        assertEquals("`TABLE1`.`ID1` is not null", r_refI().render(c2));
        assertEquals("`TABLE1`.`ID1` is not null", r_ref().render(c2));

        int i = b_ref().visit(c1).peekIndex();
        assertEquals(1, i);

        int j = b_ref().visit(c2).peekIndex();
        assertEquals(1, j);
    }

    @Test
    public void testCaseValueFunction() throws Exception {
        Case decode = decode();
        CaseValueStep<Integer> value = decode.value(FIELD_ID1);
        CaseWhenStep<Integer, String> c = value.when(1, "one");

        assertEquals("case `TABLE1`.`ID1` when 1 then 'one' end", r_refI().render(c));
        assertEquals("case `TABLE1`.`ID1` when ? then ? end", r_ref().render(c));
        assertEquals("case `TABLE1`.`ID1` when 1 then 'one' end", r_decI().render(c));
        assertEquals("case `TABLE1`.`ID1` when ? then ? end", r_dec().render(c));

        c.otherwise("nothing");
        assertEquals("case `TABLE1`.`ID1` when 1 then 'one' else 'nothing' end", r_refI().render(c));
        assertEquals("case `TABLE1`.`ID1` when ? then ? else ? end", r_ref().render(c));
        assertEquals("case `TABLE1`.`ID1` when 1 then 'one' else 'nothing' end", r_decI().render(c));
        assertEquals("case `TABLE1`.`ID1` when ? then ? else ? end", r_dec().render(c));

        c.when(2, "two").when(3, "three");
        assertEquals("case `TABLE1`.`ID1` when 1 then 'one' when 2 then 'two' when 3 then 'three' else 'nothing' end", r_refI().render(c));
        assertEquals("case `TABLE1`.`ID1` when ? then ? when ? then ? when ? then ? else ? end", r_ref().render(c));
        assertEquals("case `TABLE1`.`ID1` when 1 then 'one' when 2 then 'two' when 3 then 'three' else 'nothing' end", r_decI().render(c));
        assertEquals("case `TABLE1`.`ID1` when ? then ? when ? then ? when ? then ? else ? end", r_dec().render(c));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setString(2, "one");
            oneOf(statement).setInt(3, 2);
            oneOf(statement).setString(4, "two");
            oneOf(statement).setInt(5, 3);
            oneOf(statement).setString(6, "three");
            oneOf(statement).setString(7, "nothing");
        }});

        int i = b_ref().visit(c).peekIndex();
        assertEquals(8, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testCaseConditionFunction() throws Exception {
        Case decode = decode();
        CaseConditionStep<String> c = decode.when(FIELD_ID1.equal(1), "one");

        assertEquals("case when `TABLE1`.`ID1` = 1 then 'one' end", r_refI().render(c));
        assertEquals("case when `TABLE1`.`ID1` = ? then ? end", r_ref().render(c));
        assertEquals("case when `TABLE1`.`ID1` = 1 then 'one' end", r_decI().render(c));
        assertEquals("case when `TABLE1`.`ID1` = ? then ? end", r_dec().render(c));

        c.otherwise("nothing");
        assertEquals("case when `TABLE1`.`ID1` = 1 then 'one' else 'nothing' end", r_refI().render(c));
        assertEquals("case when `TABLE1`.`ID1` = ? then ? else ? end", r_ref().render(c));
        assertEquals("case when `TABLE1`.`ID1` = 1 then 'one' else 'nothing' end", r_decI().render(c));
        assertEquals("case when `TABLE1`.`ID1` = ? then ? else ? end", r_dec().render(c));

        c.when(FIELD_ID1.equal(2), "two").when(FIELD_ID1.equal(3), "three");
        assertEquals("case when `TABLE1`.`ID1` = 1 then 'one' when `TABLE1`.`ID1` = 2 then 'two' when `TABLE1`.`ID1` = 3 then 'three' else 'nothing' end", r_refI().render(c));
        assertEquals("case when `TABLE1`.`ID1` = ? then ? when `TABLE1`.`ID1` = ? then ? when `TABLE1`.`ID1` = ? then ? else ? end", r_ref().render(c));
        assertEquals("case when `TABLE1`.`ID1` = 1 then 'one' when `TABLE1`.`ID1` = 2 then 'two' when `TABLE1`.`ID1` = 3 then 'three' else 'nothing' end", r_decI().render(c));
        assertEquals("case when `TABLE1`.`ID1` = ? then ? when `TABLE1`.`ID1` = ? then ? when `TABLE1`.`ID1` = ? then ? else ? end", r_dec().render(c));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setString(2, "one");
            oneOf(statement).setInt(3, 2);
            oneOf(statement).setString(4, "two");
            oneOf(statement).setInt(5, 3);
            oneOf(statement).setString(6, "three");
            oneOf(statement).setString(7, "nothing");
        }});

        int i = b_ref().visit(c).peekIndex();
        assertEquals(8, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testNullFunction() throws Exception {
        Field<?> f = val((Object) null);
        assertEquals("null", r_refI().render(f));
        assertEquals("?", r_ref().render(f));

        context.checking(new Expectations() {{
            oneOf(statement).setObject(1, null);
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
    public void testArithmeticSumExpressions() throws Exception {
        Field<Integer> sum1 = FIELD_ID1.add(FIELD_ID1).add(1).add(2);
        assertEquals(Integer.class, sum1.getType());
        assertEquals("(`TABLE1`.`ID1` + `TABLE1`.`ID1` + 1 + 2)", r_refI().render(sum1));
        assertEquals("(`TABLE1`.`ID1` + `TABLE1`.`ID1` + ? + ?)", r_ref().render(sum1));
        assertEquals("(`TABLE1`.`ID1` + `TABLE1`.`ID1` + 1 + 2)", r_decI().render(sum1));
        assertEquals("(`TABLE1`.`ID1` + `TABLE1`.`ID1` + ? + ?)", r_dec().render(sum1));

        Field<Integer> sum2 = sum1.as("s");
        assertEquals(Integer.class, sum2.getType());
        assertEquals("`s`", r_refI().render(sum2));
        assertEquals("`s`", r_ref().render(sum2));

        assertEquals("(`TABLE1`.`ID1` + `TABLE1`.`ID1` + 1 + 2) as `s`", r_decI().render(sum2));
        assertEquals("(`TABLE1`.`ID1` + `TABLE1`.`ID1` + ? + ?) as `s`", r_dec().render(sum2));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 2);
        }});

        int i = b_decF().visit(sum2).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testArithmeticDifferenceExpressions() throws Exception {
        Field<Integer> difference1 = FIELD_ID1.sub(FIELD_ID1).sub(1).sub(2);
        assertEquals(Integer.class, difference1.getType());
        assertEquals("(((`TABLE1`.`ID1` - `TABLE1`.`ID1`) - 1) - 2)", r_refI().render(difference1));
        assertEquals("(((`TABLE1`.`ID1` - `TABLE1`.`ID1`) - ?) - ?)", r_ref().render(difference1));
        assertEquals("(((`TABLE1`.`ID1` - `TABLE1`.`ID1`) - 1) - 2)", r_decI().render(difference1));
        assertEquals("(((`TABLE1`.`ID1` - `TABLE1`.`ID1`) - ?) - ?)", r_dec().render(difference1));

        Field<Integer> difference2 = difference1.as("d");
        assertEquals(Integer.class, difference2.getType());
        assertEquals("`d`", r_refI().render(difference2));
        assertEquals("`d`", r_ref().render(difference2));
        assertEquals("(((`TABLE1`.`ID1` - `TABLE1`.`ID1`) - 1) - 2) as `d`", r_decI().render(difference2));
        assertEquals("(((`TABLE1`.`ID1` - `TABLE1`.`ID1`) - ?) - ?) as `d`", r_dec().render(difference2));


        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 2);
        }});

        int i = b_decF().visit(difference2).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testArithmeticProductExpressions() throws Exception {
        Field<Integer> product1 = FIELD_ID1.mul(FIELD_ID1).mul(1).mul(2);
        assertEquals(Integer.class, product1.getType());
        assertEquals("(`TABLE1`.`ID1` * `TABLE1`.`ID1` * 1 * 2)", r_refI().render(product1));
        assertEquals("(`TABLE1`.`ID1` * `TABLE1`.`ID1` * ? * ?)", r_ref().render(product1));
        assertEquals("(`TABLE1`.`ID1` * `TABLE1`.`ID1` * 1 * 2)", r_decI().render(product1));
        assertEquals("(`TABLE1`.`ID1` * `TABLE1`.`ID1` * ? * ?)", r_dec().render(product1));

        Field<Integer> product2 = product1.as("p");
        assertEquals(Integer.class, product2.getType());
        assertEquals("`p`", r_refI().render(product2));
        assertEquals("`p`", r_ref().render(product2));
        assertEquals("(`TABLE1`.`ID1` * `TABLE1`.`ID1` * 1 * 2) as `p`", r_decI().render(product2));
        assertEquals("(`TABLE1`.`ID1` * `TABLE1`.`ID1` * ? * ?) as `p`", r_dec().render(product2));


        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 2);
        }});

        int i = b_decF().visit(product2).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testArithmeticDivisionExpressions() throws Exception {
        Field<Integer> division1 = FIELD_ID1.div(FIELD_ID1).div(1).div(2);
        assertEquals(Integer.class, division1.getType());
        assertEquals("(((`TABLE1`.`ID1` / `TABLE1`.`ID1`) / 1) / 2)", r_refI().render(division1));
        assertEquals("(((`TABLE1`.`ID1` / `TABLE1`.`ID1`) / ?) / ?)", r_ref().render(division1));
        assertEquals("(((`TABLE1`.`ID1` / `TABLE1`.`ID1`) / 1) / 2)", r_decI().render(division1));
        assertEquals("(((`TABLE1`.`ID1` / `TABLE1`.`ID1`) / ?) / ?)", r_dec().render(division1));

        Field<Integer> division2 = division1.as("d");
        assertEquals(Integer.class, division2.getType());
        assertEquals("`d`", r_refI().render(division2));
        assertEquals("`d`", r_ref().render(division2));
        assertEquals("(((`TABLE1`.`ID1` / `TABLE1`.`ID1`) / 1) / 2) as `d`", r_decI().render(division2));
        assertEquals("(((`TABLE1`.`ID1` / `TABLE1`.`ID1`) / ?) / ?) as `d`", r_dec().render(division2));


        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 2);
        }});

        int i = b_decF().visit(division2).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testFunctions() {
        Field<String> f = replace(FIELD_NAME1, "a", "b");
        assertEquals("replace(`TABLE1`.`NAME1`, 'a', 'b')", r_refI().render(f));
        assertEquals("replace(`TABLE1`.`NAME1`, ?, ?)", r_ref().render(f));
    }

    @Test
    public void testArithmeticExpressions() {
        Field<? extends Number> f;

        f = FIELD_ID1.add(1).sub(2).add(3);
        assertEquals("(((`TABLE1`.`ID1` + 1) - 2) + 3)", r_refI().render(f));
        assertEquals("(((`TABLE1`.`ID1` + ?) - ?) + ?)", r_ref().render(f));

        f = FIELD_ID1.add(1).add(2).sub(3);
        assertEquals("((`TABLE1`.`ID1` + 1 + 2) - 3)", r_refI().render(f));
        assertEquals("((`TABLE1`.`ID1` + ? + ?) - ?)", r_ref().render(f));

        f = FIELD_ID1.add(1).sub(val(2).add(3));
        assertEquals("((`TABLE1`.`ID1` + 1) - (2 + 3))", r_refI().render(f));
        assertEquals("((`TABLE1`.`ID1` + ?) - (? + ?))", r_ref().render(f));

        f = FIELD_ID1.mul(1).div(2).mul(3);
        assertEquals("(((`TABLE1`.`ID1` * 1) / 2) * 3)", r_refI().render(f));
        assertEquals("(((`TABLE1`.`ID1` * ?) / ?) * ?)", r_ref().render(f));

        f = FIELD_ID1.mul(1).mul(2).div(3);
        assertEquals("((`TABLE1`.`ID1` * 1 * 2) / 3)", r_refI().render(f));
        assertEquals("((`TABLE1`.`ID1` * ? * ?) / ?)", r_ref().render(f));

        f = FIELD_ID1.mul(1).div(val(2).mul(3));
        assertEquals("((`TABLE1`.`ID1` * 1) / (2 * 3))", r_refI().render(f));
        assertEquals("((`TABLE1`.`ID1` * ?) / (? * ?))", r_ref().render(f));
    }

    @Test
    public void testArithmeticFunctions() throws Exception {
        Field<BigDecimal> sum1 = sum(FIELD_ID1);
        assertEquals(BigDecimal.class, sum1.getType());
        assertEquals("sum(`TABLE1`.`ID1`)", r_refI().render(sum1));
        assertEquals("sum(`TABLE1`.`ID1`)", r_ref().render(sum1));
        assertEquals("sum(`TABLE1`.`ID1`)", r_decI().render(sum1));
        assertEquals("sum(`TABLE1`.`ID1`)", r_dec().render(sum1));
        assertEquals(1, b_ref().visit(sum1).peekIndex());

        Field<BigDecimal> sum2 = sum(FIELD_ID1).as("value");
        assertEquals(BigDecimal.class, sum2.getType());
        assertEquals("`value`", r_refI().render(sum2));
        assertEquals("`value`", r_ref().render(sum2));
        assertEquals("sum(`TABLE1`.`ID1`) as `value`", r_decI().render(sum2));
        assertEquals("sum(`TABLE1`.`ID1`) as `value`", r_dec().render(sum2));
        assertEquals(1, b_ref().visit(sum2).peekIndex());

        Field<BigDecimal> avg1 = avg(FIELD_ID1);
        assertEquals(BigDecimal.class, avg1.getType());
        assertEquals("avg(`TABLE1`.`ID1`)", r_refI().render(avg1));
        assertEquals("avg(`TABLE1`.`ID1`)", r_ref().render(avg1));
        assertEquals("avg(`TABLE1`.`ID1`)", r_decI().render(avg1));
        assertEquals("avg(`TABLE1`.`ID1`)", r_dec().render(avg1));
        assertEquals(1, b_ref().visit(avg1).peekIndex());

        Field<BigDecimal> avg2 = avg(FIELD_ID1).as("value");
        assertEquals(BigDecimal.class, avg2.getType());
        assertEquals("`value`", r_refI().render(avg2));
        assertEquals("`value`", r_ref().render(avg2));
        assertEquals("avg(`TABLE1`.`ID1`) as `value`", r_decI().render(avg2));
        assertEquals("avg(`TABLE1`.`ID1`) as `value`", r_dec().render(avg2));
        assertEquals(1, b_ref().visit(avg2).peekIndex());

        Field<Integer> min1 = min(FIELD_ID1);
        assertEquals(Integer.class, min1.getType());
        assertEquals("min(`TABLE1`.`ID1`)", r_refI().render(min1));
        assertEquals("min(`TABLE1`.`ID1`)", r_ref().render(min1));
        assertEquals("min(`TABLE1`.`ID1`)", r_decI().render(min1));
        assertEquals("min(`TABLE1`.`ID1`)", r_dec().render(min1));
        assertEquals(1, b_ref().visit(min1).peekIndex());

        Field<Integer> min2 = min(FIELD_ID1).as("value");
        assertEquals(Integer.class, min2.getType());
        assertEquals("`value`", r_refI().render(min2));
        assertEquals("`value`", r_ref().render(min2));
        assertEquals("min(`TABLE1`.`ID1`) as `value`", r_decI().render(min2));
        assertEquals("min(`TABLE1`.`ID1`) as `value`", r_dec().render(min2));
        assertEquals(1, b_ref().visit(min2).peekIndex());

        Field<Integer> max1 = max(FIELD_ID1);
        assertEquals(Integer.class, max1.getType());
        assertEquals("max(`TABLE1`.`ID1`)", r_refI().render(max1));
        assertEquals("max(`TABLE1`.`ID1`)", r_ref().render(max1));
        assertEquals("max(`TABLE1`.`ID1`)", r_decI().render(max1));
        assertEquals("max(`TABLE1`.`ID1`)", r_dec().render(max1));
        assertEquals(1, b_ref().visit(max1).peekIndex());

        Field<Integer> max2 = max(FIELD_ID1).as("value");
        assertEquals(Integer.class, max2.getType());
        assertEquals("`value`", r_refI().render(max2));
        assertEquals("`value`", r_ref().render(max2));
        assertEquals("max(`TABLE1`.`ID1`) as `value`", r_decI().render(max2));
        assertEquals("max(`TABLE1`.`ID1`) as `value`", r_dec().render(max2));
        assertEquals(1, b_ref().visit(max2).peekIndex());

        Field<Integer> count1 = count();
        assertEquals(Integer.class, count1.getType());
        assertEquals("count(*)", r_refI().render(count1));
        assertEquals("count(*)", r_ref().render(count1));
        assertEquals("count(*)", r_decI().render(count1));
        assertEquals("count(*)", r_dec().render(count1));
        assertEquals(1, b_ref().visit(count1).peekIndex());

        Field<Integer> count1a = count().as("cnt");
        assertEquals(Integer.class, count1a.getType());
        assertEquals("`cnt`", r_refI().render(count1a));
        assertEquals("`cnt`", r_ref().render(count1a));
        assertEquals("count(*) as `cnt`", r_decI().render(count1a));
        assertEquals("count(*) as `cnt`", r_dec().render(count1a));
        assertEquals(1, b_ref().visit(count1a).peekIndex());

        Field<Integer> count2 = count(FIELD_ID1);
        assertEquals(Integer.class, count2.getType());
        assertEquals("count(`TABLE1`.`ID1`)", r_refI().render(count2));
        assertEquals("count(`TABLE1`.`ID1`)", r_ref().render(count2));
        assertEquals("count(`TABLE1`.`ID1`)", r_decI().render(count2));
        assertEquals("count(`TABLE1`.`ID1`)", r_dec().render(count2));
        assertEquals(1, b_ref().visit(count2).peekIndex());

        Field<Integer> count2a = count(FIELD_ID1).as("cnt");
        assertEquals(Integer.class, count2a.getType());
        assertEquals("`cnt`", r_refI().render(count2a));
        assertEquals("`cnt`", r_ref().render(count2a));
        assertEquals("count(`TABLE1`.`ID1`) as `cnt`", r_decI().render(count2a));
        assertEquals("count(`TABLE1`.`ID1`) as `cnt`", r_dec().render(count2a));
        assertEquals(1, b_ref().visit(count2a).peekIndex());

        Field<Integer> count3 = countDistinct(FIELD_ID1);
        assertEquals(Integer.class, count3.getType());
        assertEquals("count(distinct `TABLE1`.`ID1`)", r_refI().render(count3));
        assertEquals("count(distinct `TABLE1`.`ID1`)", r_ref().render(count3));
        assertEquals("count(distinct `TABLE1`.`ID1`)", r_decI().render(count3));
        assertEquals("count(distinct `TABLE1`.`ID1`)", r_dec().render(count3));
        assertEquals(1, b_ref().visit(count3).peekIndex());

        Field<Integer> count3a = countDistinct(FIELD_ID1).as("cnt");
        assertEquals(Integer.class, count3a.getType());
        assertEquals("`cnt`", r_refI().render(count3a));
        assertEquals("`cnt`", r_ref().render(count3a));
        assertEquals("count(distinct `TABLE1`.`ID1`) as `cnt`", r_decI().render(count3a));
        assertEquals("count(distinct `TABLE1`.`ID1`) as `cnt`", r_dec().render(count3a));
        assertEquals(1, b_ref().visit(count3a).peekIndex());
    }

    @Test
    public void testInsertQuery1() throws Exception {
        InsertQuery<Table1Record> q = create.insertQuery(TABLE1);

        q.addValue(FIELD_ID1, 10);
        assertEquals("insert into `TABLE1` (`ID1`) values (10)", r_refI().render(q));
        assertEquals("insert into `TABLE1` (`ID1`) values (?)", r_ref().render(q));
        assertEquals(q, create.insertInto(TABLE1, FIELD_ID1).values(10));
        assertEquals(q, create.insertInto(TABLE1).set(FIELD_ID1, 10));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(2, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testInsertQuery2() throws Exception {
        InsertQuery<Table1Record> q = create.insertQuery(TABLE1);

        q.addValue(FIELD_ID1, 10);
        q.addValue(FIELD_NAME1, "ABC");
        q.addValue(FIELD_DATE1, new Date(0));
        assertEquals("insert into `TABLE1` (`ID1`, `NAME1`, `DATE1`) values (10, 'ABC', date '" + zeroDate() + "')", r_refI().render(q));
        assertEquals("insert into `TABLE1` (`ID1`, `NAME1`, `DATE1`) values (?, ?, ?)", r_ref().render(q));
        assertEquals(q, create.insertInto(TABLE1, FIELD_ID1, FIELD_NAME1, FIELD_DATE1).values(10, "ABC", new Date(0)));
        assertEquals(q, create.insertInto(TABLE1).set(FIELD_ID1, 10).set(FIELD_NAME1, "ABC").set(FIELD_DATE1, new Date(0)));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setString(2, "ABC");
            oneOf(statement).setDate(3, new Date(0));
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(4, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testInsertSelect1() throws Exception {
        InsertQuery<Table1Record> q = create.insertQuery(TABLE1);

        q.addValue(FIELD_ID1, round(val(10)));
        q.addValue(FIELD_NAME1, create.select(FIELD_NAME1).from(TABLE1).where(FIELD_ID1.equal(1)).<String> asField());
        assertEquals("insert into `TABLE1` (`ID1`, `NAME1`) values (round(10), (select `TABLE1`.`NAME1` from `TABLE1` where `TABLE1`.`ID1` = 1))", r_refI().render(q));
        assertEquals("insert into `TABLE1` (`ID1`, `NAME1`) values (round(?), (select `TABLE1`.`NAME1` from `TABLE1` where `TABLE1`.`ID1` = ?))", r_ref().render(q));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setInt(2, 1);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testInsertSelect2() throws Exception {
        Insert<Table1Record> q = create.insertInto(TABLE1).select(create.selectQuery());

        assertEquals("insert into `TABLE1` (`ID1`, `NAME1`, `DATE1`) select 1 from dual", r_refI().render(q));
        assertEquals("insert into `TABLE1` (`ID1`, `NAME1`, `DATE1`) select 1 from dual", r_ref().render(q));

        // [#1069] Allow for specifying custom fields
        q = create.insertInto(TABLE1, FIELD_ID1).select(selectOne());

        assertEquals("insert into `TABLE1` (`ID1`) select 1 from dual", r_refI().render(q));
        assertEquals("insert into `TABLE1` (`ID1`) select 1 from dual", r_ref().render(q));

        // [#1069] Allow for specifying custom fields
        q = create.insertInto(TABLE1, FIELD_ID1, FIELD_NAME1).select(select(one(), inline("a")));

        assertEquals("insert into `TABLE1` (`ID1`, `NAME1`) select 1, 'a' from dual", r_refI().render(q));
        assertEquals("insert into `TABLE1` (`ID1`, `NAME1`) select 1, 'a' from dual", r_ref().render(q));

        q = create.insertInto(TABLE1).select(create.select(val(1), FIELD_NAME1).from(TABLE1).where(FIELD_NAME1.equal("abc")));

        assertEquals("insert into `TABLE1` (`ID1`, `NAME1`, `DATE1`) select 1, `TABLE1`.`NAME1` from `TABLE1` where `TABLE1`.`NAME1` = 'abc'", r_refI().render(q));
        assertEquals("insert into `TABLE1` (`ID1`, `NAME1`, `DATE1`) select ?, `TABLE1`.`NAME1` from `TABLE1` where `TABLE1`.`NAME1` = ?", r_ref().render(q));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setString(2, "abc");
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testUpdateQuery1() throws Exception {
        UpdateQuery<Table1Record> q = create.updateQuery(TABLE1);

        q.addValue(FIELD_ID1, 10);
        assertEquals("update `TABLE1` set `TABLE1`.`ID1` = 10", r_refI().render(q));
        assertEquals("update `TABLE1` set `TABLE1`.`ID1` = ?", r_ref().render(q));
        assertEquals(q, create.update(TABLE1).set(FIELD_ID1, 10));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(2, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testUpdateQuery2() throws Exception {
        UpdateQuery<Table1Record> q = create.updateQuery(TABLE1);

        q.addValue(FIELD_ID1, 10);
        q.addValue(FIELD_NAME1, "ABC");
        assertEquals("update `TABLE1` set `TABLE1`.`ID1` = 10, `TABLE1`.`NAME1` = 'ABC'", r_refI().render(q));
        assertEquals("update `TABLE1` set `TABLE1`.`ID1` = ?, `TABLE1`.`NAME1` = ?", r_ref().render(q));
        assertEquals(q, create.update(TABLE1).set(FIELD_ID1, 10).set(FIELD_NAME1, "ABC"));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setString(2, "ABC");
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testUpdateQuery3() throws Exception {
        UpdateQuery<Table1Record> q = create.updateQuery(TABLE1);
        Condition c = FIELD_ID1.equal(10);

        q.addValue(FIELD_ID1, 10);
        q.addValue(FIELD_NAME1, "ABC");
        q.addConditions(c);
        assertEquals("update `TABLE1` set `TABLE1`.`ID1` = 10, `TABLE1`.`NAME1` = 'ABC' where `TABLE1`.`ID1` = 10", r_refI().render(q));
        assertEquals("update `TABLE1` set `TABLE1`.`ID1` = ?, `TABLE1`.`NAME1` = ? where `TABLE1`.`ID1` = ?", r_ref().render(q));
        assertEquals(q, create.update(TABLE1).set(FIELD_ID1, 10).set(FIELD_NAME1, "ABC").where(c));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setString(2, "ABC");
            oneOf(statement).setInt(3, 10);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(4, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testUpdateQuery4() throws Exception {
        UpdateQuery<Table1Record> q = create.updateQuery(TABLE1);
        Condition c1 = FIELD_ID1.equal(10);
        Condition c2 = FIELD_ID1.equal(20);

        q.addValue(FIELD_ID1, 10);
        q.addValue(FIELD_NAME1, "ABC");
        q.addConditions(c1);
        q.addConditions(c2);
        assertEquals("update `TABLE1` set `TABLE1`.`ID1` = 10, `TABLE1`.`NAME1` = 'ABC' where (`TABLE1`.`ID1` = 10 and `TABLE1`.`ID1` = 20)", r_refI().render(q));
        assertEquals("update `TABLE1` set `TABLE1`.`ID1` = ?, `TABLE1`.`NAME1` = ? where (`TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ?)", r_ref().render(q));
        assertEquals(q, create.update(TABLE1).set(FIELD_ID1, 10).set(FIELD_NAME1, "ABC").where(c1, c2));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setString(2, "ABC");
            oneOf(statement).setInt(3, 10);
            oneOf(statement).setInt(4, 20);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(5, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testUpdateQuery5() throws Exception {
        UpdateQuery<Table1Record> q = create.updateQuery(TABLE1);
        Condition c1 = FIELD_ID1.equal(10);
        Condition c2 = FIELD_ID1.equal(20);

        q.addValue(FIELD_ID1, 10);
        q.addValue(FIELD_NAME1, "ABC");
        q.addConditions(c1);
        q.addConditions(c2);
        q.addConditions(c2, c1);
        assertEquals("update `TABLE1` set `TABLE1`.`ID1` = 10, `TABLE1`.`NAME1` = 'ABC' where (`TABLE1`.`ID1` = 10 and `TABLE1`.`ID1` = 20 and `TABLE1`.`ID1` = 20 and `TABLE1`.`ID1` = 10)", r_refI().render(q));
        assertEquals("update `TABLE1` set `TABLE1`.`ID1` = ?, `TABLE1`.`NAME1` = ? where (`TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ?)", r_ref().render(q));
        assertEquals(q, create.update(TABLE1).set(FIELD_ID1, 10).set(FIELD_NAME1, "ABC").where(c1).and(c2).and(c2).and(c1));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setString(2, "ABC");
            oneOf(statement).setInt(3, 10);
            oneOf(statement).setInt(4, 20);
            oneOf(statement).setInt(5, 20);
            oneOf(statement).setInt(6, 10);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(7, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testMergeQuery() throws Exception {
        Merge<Table1Record> q =
        create.mergeInto(TABLE1)
              .using(create.select(FIELD_ID2).from(TABLE2))
              .on(FIELD_ID2.equal(FIELD_ID1))
              .and(FIELD_ID1.equal(1))
              .or(FIELD_ID2.equal(2))
              .whenMatchedThenUpdate()
              .set(FIELD_NAME1, "name")
              .set(FIELD_DATE1, new Date(0))
              .whenNotMatchedThenInsert(FIELD_ID1, FIELD_NAME1, FIELD_DATE1)
              .values(1, "name", new Date(0));

        assertEquals("merge into `TABLE1` using (select `TABLE2`.`ID2` from `TABLE2`) on ((`TABLE2`.`ID2` = `TABLE1`.`ID1` and `TABLE1`.`ID1` = 1) or `TABLE2`.`ID2` = 2) when matched then update set `TABLE1`.`NAME1` = 'name', `TABLE1`.`DATE1` = date '" + zeroDate() + "' when not matched then insert (`ID1`, `NAME1`, `DATE1`) values (1, 'name', date '" + zeroDate() + "')", r_refI().render(q));
        assertEquals("merge into `TABLE1` using (select `TABLE2`.`ID2` from `TABLE2`) on ((`TABLE2`.`ID2` = `TABLE1`.`ID1` and `TABLE1`.`ID1` = ?) or `TABLE2`.`ID2` = ?) when matched then update set `TABLE1`.`NAME1` = ?, `TABLE1`.`DATE1` = ? when not matched then insert (`ID1`, `NAME1`, `DATE1`) values (?, ?, ?)", r_ref().render(q));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 2);
            oneOf(statement).setString(3, "name");
            oneOf(statement).setDate(4, new Date(0));
            oneOf(statement).setInt(5, 1);
            oneOf(statement).setString(6, "name");
            oneOf(statement).setDate(7, new Date(0));
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(8, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testDeleteQuery1() throws Exception {
        DeleteQuery<Table1Record> q = create.deleteQuery(TABLE1);

        assertEquals("delete from `TABLE1`", r_refI().render(q));
        assertEquals("delete from `TABLE1`", r_ref().render(q));
        assertEquals(q, create.delete(TABLE1));
    }

    @Test
    public void testDeleteQuery2() throws Exception {
        DeleteQuery<Table1Record> q = create.deleteQuery(TABLE1);

        q.addConditions(falseCondition());
        assertEquals("delete from `TABLE1` where 1 = 0", r_refI().render(q));
        assertEquals("delete from `TABLE1` where 1 = 0", r_ref().render(q));
        assertEquals(q, create.delete(TABLE1).where(falseCondition()));
    }

    @Test
    public void testDeleteQuery3() throws Exception {
        DeleteQuery<Table1Record> q = create.deleteQuery(TABLE1);
        Condition c1 = FIELD_ID1.equal(10);
        Condition c2 = FIELD_ID1.equal(20);

        q.addConditions(c1);
        q.addConditions(c2);
        assertEquals("delete from `TABLE1` where (`TABLE1`.`ID1` = 10 and `TABLE1`.`ID1` = 20)", r_refI().render(q));
        assertEquals("delete from `TABLE1` where (`TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ?)", r_ref().render(q));
        assertEquals(q, create.delete(TABLE1).where(c1, c2));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setInt(2, 20);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testDeleteQuery4() throws Exception {
        DeleteQuery<Table1Record> q = create.deleteQuery(TABLE1);
        Condition c1 = FIELD_ID1.equal(10);
        Condition c2 = FIELD_ID1.equal(20);

        q.addConditions(c1);
        q.addConditions(c2);
        q.addConditions(c2, c1);
        assertEquals("delete from `TABLE1` where (`TABLE1`.`ID1` = 10 and `TABLE1`.`ID1` = 20 and `TABLE1`.`ID1` = 20 and `TABLE1`.`ID1` = 10)", r_refI().render(q));
        assertEquals("delete from `TABLE1` where (`TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ?)", r_ref().render(q));
        assertEquals(q, create.delete(TABLE1).where(c1, c2).and(c2).and(c1));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setInt(2, 20);
            oneOf(statement).setInt(3, 20);
            oneOf(statement).setInt(4, 10);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(5, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testConditionalSelectQuery1() throws Exception {
        Select<?> q = create.selectQuery();
        Select<?> s = create.select();

        assertEquals("select 1 from dual", r_refI().render(q));
        assertEquals("select 1 from dual", r_ref().render(q));
        assertEquals(q, s);
    }

    @Test
    public void testConditionalSelectQuery2() throws Exception {
        SelectQuery<?> q = create.selectQuery();

        q.addConditions(falseCondition());
        assertEquals("select 1 from dual where 1 = 0", r_refI().render(q));
        assertEquals("select 1 from dual where 1 = 0", r_ref().render(q));
        assertEquals(q, create.select().where(falseCondition()));
    }

    @Test
    public void testConditionalSelectQuery3() throws Exception {
        SelectQuery<?> q = create.selectQuery();

        q.addConditions(falseCondition());
        q.addConditions(trueCondition());
        assertEquals("select 1 from dual where (1 = 0 and 1 = 1)", r_refI().render(q));
        assertEquals("select 1 from dual where (1 = 0 and 1 = 1)", r_ref().render(q));
        assertEquals(q, create.select().where(falseCondition().and(trueCondition())));
    }

    @Test
    public void testConditionalSelectQuery4() throws Exception {
        SelectQuery<?> q = create.selectQuery();
        Condition c1 = FIELD_ID1.equal(10);
        Condition c2 = FIELD_ID1.equal(20);

        q.addConditions(c1);
        q.addConditions(c2);
        q.addConditions(c2, c1);
        assertEquals("select 1 from dual where (`TABLE1`.`ID1` = 10 and `TABLE1`.`ID1` = 20 and `TABLE1`.`ID1` = 20 and `TABLE1`.`ID1` = 10)", r_refI().render(q));
        assertEquals("select 1 from dual where (`TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ? and `TABLE1`.`ID1` = ?)", r_ref().render(q));
        assertEquals(q, create.select().where(c1.and(c2).and(c2.and(c1))));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 10);
            oneOf(statement).setInt(2, 20);
            oneOf(statement).setInt(3, 20);
            oneOf(statement).setInt(4, 10);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(5, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testConditionalSelectQuery5() throws Exception {
        SelectQuery<?> q = create.selectQuery();
        Condition c1 = condition("`TABLE1`.`ID1` = ?", "10");
        Condition c2 = condition("`TABLE2`.`ID2` = 20 or `TABLE2`.`ID2` = ?", 30);

        q.addConditions(c1);
        q.addConditions(c2);
        assertEquals("select 1 from dual where ((`TABLE1`.`ID1` = '10') and (`TABLE2`.`ID2` = 20 or `TABLE2`.`ID2` = 30))", r_refI().render(q));
        assertEquals("select 1 from dual where ((`TABLE1`.`ID1` = ?) and (`TABLE2`.`ID2` = 20 or `TABLE2`.`ID2` = ?))", r_ref().render(q));
        assertEquals(q, create.select().where(c1, c2));

        context.checking(new Expectations() {{
            oneOf(statement).setString(1, "10");
            oneOf(statement).setInt(2, 30);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testDistinctSelectQuery() throws Exception {
        SelectQuery<?> q = create.selectQuery();
        q.addSelect(FIELD_ID1, FIELD_ID2);
        q.setDistinct(true);

        assertEquals("select distinct `TABLE1`.`ID1`, `TABLE2`.`ID2` from dual", r_refI().render(q));
        assertEquals("select distinct `TABLE1`.`ID1`, `TABLE2`.`ID2` from dual", r_ref().render(q));
        assertEquals(q, create.selectDistinct(FIELD_ID1, FIELD_ID2));

        int i = b_ref().visit(q).peekIndex();
        assertEquals(1, i);
    }

    @Test
    public void testProductSelectQuery() throws Exception {
        SelectQuery<?> q = create.selectQuery();

        q.addFrom(TABLE1);
        q.addFrom(TABLE2);
        q.addFrom(TABLE3);
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2`, `TABLE3`.`ID3`, `TABLE3`.`NAME3`, `TABLE3`.`DATE3` from `TABLE1`, `TABLE2`, `TABLE3`", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2`, `TABLE3`.`ID3`, `TABLE3`.`NAME3`, `TABLE3`.`DATE3` from `TABLE1`, `TABLE2`, `TABLE3`", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1, TABLE2, TABLE3));

        int i = b_ref().visit(q).peekIndex();
        assertEquals(1, i);
    }

    @Test
    public void testJoinSelectQuery() throws Exception {
        SelectQuery<?> q = create.selectQuery();

        q.addFrom(TABLE1);
        q.addJoin(TABLE2);
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2` from `TABLE1` join `TABLE2` on 1 = 1", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2` from `TABLE1` join `TABLE2` on 1 = 1", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1).join(TABLE2).on());

        int i = b_ref().visit(q).peekIndex();
        assertEquals(1, i);
    }

    @Test
    public void testJoinOnConditionSelectQuery() throws Exception {
        SelectQuery<?> q = create.selectQuery();
        q.addFrom(TABLE1);
        q.addJoin(TABLE2, FIELD_ID1.equal(FIELD_ID2));

        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2` from `TABLE1` join `TABLE2` on `TABLE1`.`ID1` = `TABLE2`.`ID2`", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2` from `TABLE1` join `TABLE2` on `TABLE1`.`ID1` = `TABLE2`.`ID2`", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1).join(TABLE2).on(FIELD_ID1.equal(FIELD_ID2)));

        q.addJoin(TABLE3, FIELD_ID2.equal(FIELD_ID3));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2`, `TABLE3`.`ID3`, `TABLE3`.`NAME3`, `TABLE3`.`DATE3` from `TABLE1` join `TABLE2` on `TABLE1`.`ID1` = `TABLE2`.`ID2` join `TABLE3` on `TABLE2`.`ID2` = `TABLE3`.`ID3`", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2`, `TABLE3`.`ID3`, `TABLE3`.`NAME3`, `TABLE3`.`DATE3` from `TABLE1` join `TABLE2` on `TABLE1`.`ID1` = `TABLE2`.`ID2` join `TABLE3` on `TABLE2`.`ID2` = `TABLE3`.`ID3`", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1)
                                      .join(TABLE2).on(FIELD_ID1.equal(FIELD_ID2))
                                      .join(TABLE3).on(FIELD_ID2.equal(FIELD_ID3)));

        int i = b_ref().visit(q).peekIndex();
        assertEquals(1, i);
    }

    @Test
    public void testJoinComplexSelectQuery() throws Exception {
        SelectQuery<?> q = create.selectQuery();

        q.addFrom(TABLE1);
        q.addJoin(TABLE2,
                FIELD_ID1.equal(FIELD_ID2),
                FIELD_ID1.equal(1),
                FIELD_ID2.in(1, 2, 3));
        q.addConditions(FIELD_ID1.equal(5));

        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2` from `TABLE1` join `TABLE2` on (`TABLE1`.`ID1` = `TABLE2`.`ID2` and `TABLE1`.`ID1` = 1 and `TABLE2`.`ID2` in (1, 2, 3)) where `TABLE1`.`ID1` = 5", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2` from `TABLE1` join `TABLE2` on (`TABLE1`.`ID1` = `TABLE2`.`ID2` and `TABLE1`.`ID1` = ? and `TABLE2`.`ID2` in (?, ?, ?)) where `TABLE1`.`ID1` = ?", r_ref().render(q));

        // Join using a single condition
        assertEquals(q, create.select().from(TABLE1)
                                       .join(TABLE2)
                                       .on(FIELD_ID1.equal(FIELD_ID2)
                                       .and(FIELD_ID1.equal(1))
                                       .and(FIELD_ID2.in(1, 2, 3)))
                                       .where(FIELD_ID1.equal(5)));

        // Join using several conditions
        assertEquals(q, create.select().from(TABLE1)
                                       .join(TABLE2)
                                       .on(FIELD_ID1.equal(FIELD_ID2))
                                       .and(FIELD_ID1.equal(1))
                                       .and(FIELD_ID2.in(1, 2, 3))
                                       .where(FIELD_ID1.equal(5)));

        q.addJoin(TABLE3, FIELD_ID2.equal(FIELD_ID3));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2`, `TABLE3`.`ID3`, `TABLE3`.`NAME3`, `TABLE3`.`DATE3` from `TABLE1` join `TABLE2` on (`TABLE1`.`ID1` = `TABLE2`.`ID2` and `TABLE1`.`ID1` = 1 and `TABLE2`.`ID2` in (1, 2, 3)) join `TABLE3` on `TABLE2`.`ID2` = `TABLE3`.`ID3` where `TABLE1`.`ID1` = 5", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2`, `TABLE3`.`ID3`, `TABLE3`.`NAME3`, `TABLE3`.`DATE3` from `TABLE1` join `TABLE2` on (`TABLE1`.`ID1` = `TABLE2`.`ID2` and `TABLE1`.`ID1` = ? and `TABLE2`.`ID2` in (?, ?, ?)) join `TABLE3` on `TABLE2`.`ID2` = `TABLE3`.`ID3` where `TABLE1`.`ID1` = ?", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1)
                                .join(TABLE2).on(FIELD_ID1.equal(FIELD_ID2)
                                            .and(FIELD_ID1.equal(1))
                                            .and(FIELD_ID2.in(1, 2, 3)))
                                .join(TABLE3).on(FIELD_ID2.equal(FIELD_ID3))
                                .where(FIELD_ID1.equal(5)));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 1);
            oneOf(statement).setInt(3, 2);
            oneOf(statement).setInt(4, 3);
            oneOf(statement).setInt(5, 5);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(6, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testJoinSelf() throws Exception {
        Table<Table1Record> t1 = TABLE1.as("t1");
        Table<Table1Record> t2 = TABLE1.as("t2");

        SelectQuery<?> q = create.selectQuery();
        q.addFrom(t1);
        q.addJoin(t2, t1.field(FIELD_ID1).equal(t2.field(FIELD_ID1)));

        assertEquals("select `t1`.`ID1`, `t1`.`NAME1`, `t1`.`DATE1`, `t2`.`ID1`, `t2`.`NAME1`, `t2`.`DATE1` from `TABLE1` as `t1` join `TABLE1` as `t2` on `t1`.`ID1` = `t2`.`ID1`", r_refI().render(q));
        assertEquals("select `t1`.`ID1`, `t1`.`NAME1`, `t1`.`DATE1`, `t2`.`ID1`, `t2`.`NAME1`, `t2`.`DATE1` from `TABLE1` as `t1` join `TABLE1` as `t2` on `t1`.`ID1` = `t2`.`ID1`", r_ref().render(q));
        assertEquals(q, create.select().from(t1)
                                .join(t2).on(t1.field(FIELD_ID1).equal(
                                             t2.field(FIELD_ID1))));

        int i = b_ref().visit(q).peekIndex();
        assertEquals(1, i);
    }

    @Test
    public void testJoinTypeSelectQuery() throws Exception {
        SelectQuery<?> q = create.selectQuery();
        q.addFrom(TABLE1);
        q.addJoin(TABLE2, LEFT_OUTER_JOIN, FIELD_ID1.equal(FIELD_ID2));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2` from `TABLE1` left outer join `TABLE2` on `TABLE1`.`ID1` = `TABLE2`.`ID2`", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2` from `TABLE1` left outer join `TABLE2` on `TABLE1`.`ID1` = `TABLE2`.`ID2`", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1).leftOuterJoin(TABLE2).on(FIELD_ID1.equal(FIELD_ID2)));

        int i = b_ref().visit(q).peekIndex();
        assertEquals(1, i);
    }

    @Test
    public void testGroupSelectQuery() throws Exception {
        SelectQuery<?> q = create.selectQuery();
        q.addFrom(TABLE1);

        q.addGroupBy();
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by 1", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by 1", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1).groupBy());

        q.addGroupBy(FIELD_ID1);
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1).groupBy(FIELD_ID1));

        q.addGroupBy(FIELD_ID2, FIELD_ID3);
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`, `TABLE2`.`ID2`, `TABLE3`.`ID3`", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`, `TABLE2`.`ID2`, `TABLE3`.`ID3`", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1).groupBy(FIELD_ID1, FIELD_ID2, FIELD_ID3));

        q.addHaving(FIELD_ID1.equal(1));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`, `TABLE2`.`ID2`, `TABLE3`.`ID3` having `TABLE1`.`ID1` = 1", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`, `TABLE2`.`ID2`, `TABLE3`.`ID3` having `TABLE1`.`ID1` = ?", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1)
                                  .groupBy(FIELD_ID1, FIELD_ID2, FIELD_ID3)
                                  .having(FIELD_ID1.equal(1)));

        q.addHaving(Operator.OR, FIELD_ID1.equal(2));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`, `TABLE2`.`ID2`, `TABLE3`.`ID3` having (`TABLE1`.`ID1` = 1 or `TABLE1`.`ID1` = 2)", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`, `TABLE2`.`ID2`, `TABLE3`.`ID3` having (`TABLE1`.`ID1` = ? or `TABLE1`.`ID1` = ?)", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1)
                                  .groupBy(FIELD_ID1, FIELD_ID2, FIELD_ID3)
                                  .having(FIELD_ID1.equal(1))
                                  .or(FIELD_ID1.equal(2)));

        q.addHaving(Operator.OR, FIELD_ID1.equal(3));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`, `TABLE2`.`ID2`, `TABLE3`.`ID3` having (`TABLE1`.`ID1` = 1 or `TABLE1`.`ID1` = 2 or `TABLE1`.`ID1` = 3)", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`, `TABLE2`.`ID2`, `TABLE3`.`ID3` having (`TABLE1`.`ID1` = ? or `TABLE1`.`ID1` = ? or `TABLE1`.`ID1` = ?)", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1)
                                  .groupBy(FIELD_ID1, FIELD_ID2, FIELD_ID3)
                                  .having(FIELD_ID1.equal(1))
                                  .or(FIELD_ID1.equal(2))
                                  .or(FIELD_ID1.equal(3)));

        q.addHaving(FIELD_ID1.in(1, 2, 3));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`, `TABLE2`.`ID2`, `TABLE3`.`ID3` having ((`TABLE1`.`ID1` = 1 or `TABLE1`.`ID1` = 2 or `TABLE1`.`ID1` = 3) and `TABLE1`.`ID1` in (1, 2, 3))", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` group by `TABLE1`.`ID1`, `TABLE2`.`ID2`, `TABLE3`.`ID3` having ((`TABLE1`.`ID1` = ? or `TABLE1`.`ID1` = ? or `TABLE1`.`ID1` = ?) and `TABLE1`.`ID1` in (?, ?, ?))", r_ref().render(q));
        assertEquals(q, create.select().from(TABLE1)
                                  .groupBy(FIELD_ID1, FIELD_ID2, FIELD_ID3)
                                  .having(FIELD_ID1.equal(1))
                                  .or(FIELD_ID1.equal(2))
                                  .or(FIELD_ID1.equal(3))
                                  .and(FIELD_ID1.in(1, 2, 3)));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 2);
            oneOf(statement).setInt(3, 3);
            oneOf(statement).setInt(4, 1);
            oneOf(statement).setInt(5, 2);
            oneOf(statement).setInt(6, 3);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(7, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testOrderSelectQuery() throws Exception {
        SelectQuery<Table1Record> q = create.selectQuery(TABLE1);

        q.addOrderBy(FIELD_ID1);
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` order by `TABLE1`.`ID1` asc", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` order by `TABLE1`.`ID1` asc", r_ref().render(q));
        assertEquals(q, create.selectFrom(TABLE1).orderBy(FIELD_ID1));

        q.addOrderBy(FIELD_ID2.desc());
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` order by `TABLE1`.`ID1` asc, `TABLE2`.`ID2` desc", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` order by `TABLE1`.`ID1` asc, `TABLE2`.`ID2` desc", r_ref().render(q));
        assertEquals(q, create.selectFrom(TABLE1).orderBy(
                                    FIELD_ID1.asc(),
                                    FIELD_ID2.desc()));

        int i = b_ref().visit(q).peekIndex();
        assertEquals(1, i);
    }

    @Test
    public void testCompleteSelectQuery() throws Exception {
        SelectQuery<?> q = create.selectQuery();
        q.addFrom(TABLE1);
        q.addJoin(TABLE2, FIELD_ID1.equal(FIELD_ID2));
        q.addSelect(FIELD_ID1, FIELD_ID2);
        q.addGroupBy(FIELD_ID1, FIELD_ID2);
        q.addHaving(FIELD_ID1.equal(1));
        q.addOrderBy(FIELD_ID1.asc());
        q.addOrderBy(FIELD_ID2.desc());

        assertEquals("select `TABLE1`.`ID1`, `TABLE2`.`ID2` from `TABLE1` join `TABLE2` on `TABLE1`.`ID1` = `TABLE2`.`ID2` group by `TABLE1`.`ID1`, `TABLE2`.`ID2` having `TABLE1`.`ID1` = 1 order by `TABLE1`.`ID1` asc, `TABLE2`.`ID2` desc", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE2`.`ID2` from `TABLE1` join `TABLE2` on `TABLE1`.`ID1` = `TABLE2`.`ID2` group by `TABLE1`.`ID1`, `TABLE2`.`ID2` having `TABLE1`.`ID1` = ? order by `TABLE1`.`ID1` asc, `TABLE2`.`ID2` desc", r_ref().render(q));
        assertEquals(q, create.select(FIELD_ID1, FIELD_ID2)
                          .from(TABLE1)
                          .join(TABLE2).on(FIELD_ID1.equal(FIELD_ID2))
                          .groupBy(FIELD_ID1, FIELD_ID2)
                          .having(FIELD_ID1.equal(1))
                          .orderBy(
                              FIELD_ID1.asc(),
                              FIELD_ID2.desc()));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
        }});

        int i = b_ref().visit(q).peekIndex();
        assertEquals(2, i);

        context.assertIsSatisfied();
    }

    @Test
    public void testCombinedSelectQuery() throws Exception {
        Select<?> combine = createCombinedSelectQuery();

        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = 1 union select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = 2", r_refI().render(combine));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = ? union select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = ?", r_ref().render(combine));
        assertEquals(combine, createCombinedSelect());

        combine = create
            .select(createCombinedSelectQuery().field(FIELD_ID1))
            .from(createCombinedSelectQuery())
            .orderBy(FIELD_ID1);

        Pattern p = Pattern.compile("`alias_\\d+`");
        Matcher m = p.matcher(r_ref().render(combine));
        m.find();
        String match = m.group();

        assertEquals("select " + match + ".`ID1` from (select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = 1 union select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = 2) as " + match + " order by `TABLE1`.`ID1` asc", r_refI().render(combine));
        assertEquals("select " + match + ".`ID1` from (select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = ? union select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = ?) as " + match + " order by `TABLE1`.`ID1` asc", r_ref().render(combine));

        combine = createCombinedSelectQuery();
        combine = create.select()
            .from(createCombinedSelectQuery())
            .join(TABLE2).on(FIELD_ID1.equal(FIELD_ID2))
            .orderBy(FIELD_ID1);
        assertEquals("select " + match + ".`ID1`, " + match + ".`NAME1`, " + match + ".`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2` from (select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = 1 union select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = 2) as " + match + " join `TABLE2` on `TABLE1`.`ID1` = `TABLE2`.`ID2` order by `TABLE1`.`ID1` asc", r_refI().render(combine));
        assertEquals("select " + match + ".`ID1`, " + match + ".`NAME1`, " + match + ".`DATE1`, `TABLE2`.`ID2`, `TABLE2`.`NAME2`, `TABLE2`.`DATE2` from (select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = ? union select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = ?) as " + match + " join `TABLE2` on `TABLE1`.`ID1` = `TABLE2`.`ID2` order by `TABLE1`.`ID1` asc", r_ref().render(combine));

        context.checking(new Expectations() {{
            oneOf(statement).setInt(1, 1);
            oneOf(statement).setInt(2, 2);
        }});

        int i = b_ref().visit(combine).peekIndex();
        assertEquals(3, i);

        context.assertIsSatisfied();
    }

    private Select<?> createCombinedSelectQuery() {
        SelectQuery<Record> q1 = create.selectQuery();
        SelectQuery<Record> q2 = create.selectQuery();

        q1.addFrom(TABLE1);
        q2.addFrom(TABLE1);

        q1.addConditions(FIELD_ID1.equal(1));
        q2.addConditions(FIELD_ID1.equal(2));

        return q1.union(q2);
    }

    private Select<?> createCombinedSelect() {
        SelectFinalStep<Record> q1 = create.select().from(TABLE1).where(FIELD_ID1.equal(1));
        SelectFinalStep<Record> q2 = create.select().from(TABLE1).where(FIELD_ID1.equal(2));

        return q1.union(q2);
    }

    @Test
    public void testInnerSelect1() throws Exception {
        SelectQuery<Table1Record> q1 = create.selectQuery(TABLE1);
        SelectQuery<Table1Record> q2 = create.selectQuery(q1.asTable().as("inner_temp_table"));
        SelectQuery<Table1Record> q3 = create.selectQuery(q2.asTable().as("outer_temp_table"));

        assertEquals("select `inner_temp_table`.`ID1`, `inner_temp_table`.`NAME1`, `inner_temp_table`.`DATE1` from (select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1`) as `inner_temp_table`", r_refI().render(q2));
        assertEquals("select `inner_temp_table`.`ID1`, `inner_temp_table`.`NAME1`, `inner_temp_table`.`DATE1` from (select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1`) as `inner_temp_table`", r_ref().render(q2));

        assertEquals("select `outer_temp_table`.`ID1`, `outer_temp_table`.`NAME1`, `outer_temp_table`.`DATE1` from (select `inner_temp_table`.`ID1`, `inner_temp_table`.`NAME1`, `inner_temp_table`.`DATE1` from (select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1`) as `inner_temp_table`) as `outer_temp_table`", r_refI().render(q3));
        assertEquals("select `outer_temp_table`.`ID1`, `outer_temp_table`.`NAME1`, `outer_temp_table`.`DATE1` from (select `inner_temp_table`.`ID1`, `inner_temp_table`.`NAME1`, `inner_temp_table`.`DATE1` from (select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1`) as `inner_temp_table`) as `outer_temp_table`", r_ref().render(q3));
    }

    @Test
    public void testInnerSelect2() throws Exception {
        SelectQuery<?> q1 = create.selectQuery();
        SelectQuery<?> q2 = create.selectQuery();

        q1.addFrom(TABLE1);
        q2.addFrom(TABLE2);

        q1.addSelect(FIELD_ID1.as("inner_id1"));
        q2.addSelect(FIELD_ID2.as("outer_id2"));
        q2.addSelect(q1.asField().as("outer_id1"));

        assertEquals("select `TABLE2`.`ID2` as `outer_id2`, (select `TABLE1`.`ID1` as `inner_id1` from `TABLE1`) as `outer_id1` from `TABLE2`", r_refI().render(q2));
        assertEquals("select `TABLE2`.`ID2` as `outer_id2`, (select `TABLE1`.`ID1` as `inner_id1` from `TABLE1`) as `outer_id1` from `TABLE2`", r_ref().render(q2));
    }

    @Test
    public void testInnerSelect3() throws Exception {
        SelectQuery<?> q = create.selectQuery();

        q.addFrom(TABLE1);
        q.addConditions(FIELD_ID1.in(select(FIELD_ID2).from(TABLE2)));

        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` in (select `TABLE2`.`ID2` from `TABLE2`)", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` in (select `TABLE2`.`ID2` from `TABLE2`)", r_ref().render(q));
    }

    @Test
    public void testInnerSelect4() throws Exception {
        SelectQuery<?> q = create.selectQuery();

        q.addFrom(TABLE1);
        q.addConditions(FIELD_ID1.equal(select(FIELD_ID2).from(TABLE2)));

        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = (select `TABLE2`.`ID2` from `TABLE2`)", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` = (select `TABLE2`.`ID2` from `TABLE2`)", r_ref().render(q));
    }

    @Test
    public void testInnerSelect5() throws Exception {
        SelectQuery<?> q = create.selectQuery();

        q.addFrom(TABLE1);
        q.addConditions(FIELD_ID1.greaterThan(any(select(FIELD_ID2).from(TABLE2))));

        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` > any (select `TABLE2`.`ID2` from `TABLE2`)", r_refI().render(q));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where `TABLE1`.`ID1` > any (select `TABLE2`.`ID2` from `TABLE2`)", r_ref().render(q));
    }

    @Test
    public void testInnerSelect6() throws Exception {
        SelectQuery<?> q1 = create.selectQuery();
        SelectQuery<?> q2 = create.selectQuery();

        q1.addFrom(TABLE1);
        q2.addFrom(TABLE2);

        q2.addSelect(FIELD_ID2);
        q1.addConditions(exists(q2));

        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where exists (select `TABLE2`.`ID2` from `TABLE2`)", r_refI().render(q1));
        assertEquals("select `TABLE1`.`ID1`, `TABLE1`.`NAME1`, `TABLE1`.`DATE1` from `TABLE1` where exists (select `TABLE2`.`ID2` from `TABLE2`)", r_ref().render(q1));
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

        assertEquals("date '1981-07-10'", r_refI.render(i4));
        assertEquals("date '1981-07-10'", r_refP.render(i4));
        assertEquals("date '1981-07-10'", r_ref.render(i4));
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
