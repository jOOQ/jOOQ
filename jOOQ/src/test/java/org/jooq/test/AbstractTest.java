/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test;

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
        return r_ref().inline(true);
    }

    protected final RenderContext r_decI() {
        return r_dec().inline(true);
    }

    protected final RenderContext r_decIF() {
        return r_decF().inline(true);
    }

    protected final RenderContext r_decIT() {
        return r_decT().inline(true);
    }

    protected final RenderContext r_refP() {
        return r_ref().namedParams(true);
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
