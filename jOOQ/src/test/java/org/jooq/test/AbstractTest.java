/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.test;

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.test.data.Table1.FIELD_ID1;
import static org.jooq.test.data.Table1.FIELD_NAME1;
import static org.jooq.test.data.Table1.TABLE1;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Timestamp;

import org.jooq.BindContext;
import org.jooq.DSLContext;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.test.data.Table1Record;
import org.jooq.util.mysql.MySQLDataType;

import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * A base class for unit tests
 *
 * @author Lukas Eder
 */
public abstract class AbstractTest {

    protected Mockery              context;
    protected PreparedStatement    statement;
    protected DSLContext           create;
    protected Result<Table1Record> resultEmpty;
    protected Result<Table1Record> resultOne;
    protected Result<Table1Record> resultTwo;

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
        resultOne.add(create.newRecord(TABLE1));
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
}
