/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
import org.jooq.util.oracle.OracleDataType;

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
        Class.forName(OracleDataType.class.getName());
    }

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        statement = context.mock(PreparedStatement.class);
        create = DSL.using(SQLDialect.ORACLE);

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
