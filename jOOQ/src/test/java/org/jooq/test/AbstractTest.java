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
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;
import static org.jooq.test.data.Table2.FIELD_NAME2;
import static org.jooq.test.data.Table3.FIELD_NAME3;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Timestamp;

import org.jooq.BindContext;
import org.jooq.Constants;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.test.data.Table1Record;
import org.jooq.util.mysql.MySQLDataType;

import org.jmock.Mockery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * A base class for unit tests
 *
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractTest {

    protected Mockery                                 context;
    protected PreparedStatement                       statement;
    protected DSLContext                              create;
    protected Result<Table1Record>                    resultEmpty;
    protected Table1Record                            recordOne;
    protected Result<Table1Record>                    resultOne;
    protected Result<Table1Record>                    resultTwo;
    protected Result<Record3<String, String, String>> resultStrings;

    @BeforeClass
    public static void init() throws Exception {

        // [#650] Due to a lacking data type registry, the types need to be
        // loaded statically
        Class.forName(MySQLDataType.class.getName());
    }

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        statement = context.mock(PreparedStatement.class);
        create = DSL.using(SQLDialect.MYSQL);

        resultEmpty = create.newResult(TABLE1);

        resultOne = create.newResult(TABLE1);
        resultOne.add(recordOne = create.newRecord(TABLE1));
        resultOne.get(0).setValue(FIELD_ID1, 1);
        resultOne.get(0).setValue(FIELD_NAME1, "1");
        resultOne.get(0).changed(false);

        resultTwo = create.newResult(TABLE1);
        resultTwo.add(create.newRecord(TABLE1));
        resultTwo.add(create.newRecord(TABLE1));
        resultTwo.get(0).setValue(FIELD_ID1, 2);
        resultTwo.get(0).setValue(FIELD_NAME1, "2");
        resultTwo.get(0).changed(false);
        resultTwo.get(1).setValue(FIELD_ID1, 3);
        resultTwo.get(1).setValue(FIELD_NAME1, "3");
        resultTwo.get(1).changed(false);

        resultStrings = create.newResult(FIELD_NAME1, FIELD_NAME2, FIELD_NAME3);
        resultStrings.add(create.newRecord(FIELD_NAME1, FIELD_NAME2, FIELD_NAME3));
        resultStrings.add(create.newRecord(FIELD_NAME1, FIELD_NAME2, FIELD_NAME3));
        resultStrings.get(0).setValue(FIELD_NAME1, "A1");
        resultStrings.get(0).setValue(FIELD_NAME2, "B1");
        resultStrings.get(0).setValue(FIELD_NAME3, "C1");
        resultStrings.get(1).setValue(FIELD_NAME1, "A2");
        resultStrings.get(1).setValue(FIELD_NAME2, "B2");
        resultStrings.get(1).setValue(FIELD_NAME3, "C2");
    }

    @After
    public void tearDown() throws Exception {
        statement = null;
        context = null;
    }

    protected final BindContext b_ref() {
        return create.bindContext(statement);
    }

    protected final BindContext b_dec() {
        return b_ref().declareFields(true).declareTables(true);
    }

    protected final BindContext b_decF() {
        return b_ref().declareFields(true);
    }

    protected final BindContext b_decT() {
        return b_ref().declareTables(true);
    }

    protected final RenderContext r_ref() {
        return create.renderContext();
    }

    protected final RenderContext r_dec() {
        return r_ref().declareFields(true).declareTables(true);
    }

    protected final RenderContext r_decF() {
        return r_ref().declareFields(true);
    }

    protected final RenderContext r_decT() {
        return r_ref().declareTables(true);
    }

    protected final RenderContext r_refI() {
        return r_ref().paramType(INLINED);
    }

    protected final RenderContext r_decI() {
        return r_dec().paramType(INLINED);
    }

    protected final RenderContext r_decIF() {
        return r_decF().paramType(INLINED);
    }

    protected final RenderContext r_decIT() {
        return r_decT().paramType(INLINED);
    }

    protected final RenderContext r_refP() {
        return r_ref().paramType(NAMED);
    }

    protected final String zeroDate() {
        return new Date(0).toString();
    }

    protected final String zeroTime() {
        return new Time(0).toString();
    }

    protected final String zeroTimestamp() {
        return new Timestamp(0).toString();
    }

    protected static void assertEquals(int expected, int actual) {
        Assert.assertEquals(expected, actual);
    }

    protected static void assertEquals(Object expected, Object actual) {
        if (actual instanceof String) {
            actual = ((String) actual).replace(" -- SQL rendered with a free trial version of jOOQ " + Constants.FULL_VERSION, "");
        }

        Assert.assertEquals(expected, actual);
    }

    protected static void assertEquals(String message, Object expected, Object actual) {
        if (actual instanceof String) {
            actual = ((String) actual).replace(" -- SQL rendered with a free trial version of jOOQ " + Constants.FULL_VERSION, "");
        }

        Assert.assertEquals(message, expected, actual);
    }
}
