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

import static org.jooq.test.data.BoolTable.BOOL_TABLE;

import java.sql.SQLException;

import org.jooq.Converter;
import org.jooq.Converters;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.jooq.test.data.BoolRecord;
import org.jooq.test.data.converter.Bool;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

import org.junit.Test;

/**
 * Unit tests for data type conversion
 *
 * @author Lukas Eder
 */
public class ConverterTest extends AbstractTest {

    @Test
    public void testConverterInMockResult() {
        Result<BoolRecord> result =
        DSL.using(new MockConnection(new MockDataProvider() {

            @Override
            public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
                Result<BoolRecord> r = create.newResult(BOOL_TABLE);

                r.add(create.newRecord(BOOL_TABLE, new Object[] { Bool.TRUE }));
                r.add(create.newRecord(BOOL_TABLE, new Object[] { Bool.FALSE }));
                r.add(create.newRecord(BOOL_TABLE, new Object[] { Bool.NULL }));

                return new MockResult[] { new MockResult(r.size(), r) };
            }
        })).selectFrom(BOOL_TABLE).fetch();

        assertEquals(3, result.size());
        assertEquals(1, result.fields().length);
        assertEquals(Bool.TRUE, result.getValue(0, 0));
        assertEquals(Bool.FALSE, result.getValue(1, 0));
        assertEquals(Bool.NULL, result.getValue(2, 0));
    }

    @Test
    public void testConverterInTableField() {
        assertEquals(Bool.NULL, BOOL_TABLE.BOOL.getDataType().convert((Boolean) null));
        assertEquals(Bool.TRUE, BOOL_TABLE.BOOL.getDataType().convert(true));
        assertEquals(Bool.TRUE, BOOL_TABLE.BOOL.getDataType().convert(1));
        assertEquals(Bool.TRUE, BOOL_TABLE.BOOL.getDataType().convert("true"));
        assertEquals(Bool.FALSE, BOOL_TABLE.BOOL.getDataType().convert(false));
        assertEquals(Bool.FALSE, BOOL_TABLE.BOOL.getDataType().convert(0));
        assertEquals(Bool.FALSE, BOOL_TABLE.BOOL.getDataType().convert("false"));
    }

    @SuppressWarnings("serial")
    @Test
    public void testChainedConverters() {
        Converter<Integer, Integer> c1 = Converters.of();

        assertEquals(1, (int) c1.from(1));
        assertEquals(1, (int) c1.to(1));
        assertEquals(1, (int) c1.from(c1.to(1)));

        Converter<Integer, Integer> add = new Converter<Integer, Integer>() {
            @Override
            public Integer from(Integer t) {
                return t + 1;
            }

            @Override
            public Integer to(Integer u) {
                return u - 1;
            }

            @Override
            public Class<Integer> fromType() {
                return Integer.class;
            }

            @Override
            public Class<Integer> toType() {
                return Integer.class;
            }
        };

        Converter<Integer, Integer> c2 = Converters.of(add);
        Converter<Integer, Integer> c3 = Converters.of(add, add);
        Converter<Integer, Integer> c4 = Converters.of(add, add, add);
        Converter<Integer, Integer> c5 = Converters.of(add, add, add, add);

        assertEquals(2, (int) c2.from(1));
        assertEquals(3, (int) c3.from(1));
        assertEquals(4, (int) c4.from(1));
        assertEquals(5, (int) c5.from(1));

        assertEquals(1, (int) c2.to(2));
        assertEquals(1, (int) c3.to(3));
        assertEquals(1, (int) c4.to(4));
        assertEquals(1, (int) c5.to(5));

        assertEquals(1, (int) c2.from(c2.to(1)));
        assertEquals(1, (int) c3.from(c3.to(1)));
        assertEquals(1, (int) c4.from(c4.to(1)));
        assertEquals(1, (int) c5.from(c5.to(1)));
    }
}
