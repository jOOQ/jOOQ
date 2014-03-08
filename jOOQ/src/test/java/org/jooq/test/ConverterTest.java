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
}
