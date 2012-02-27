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
package org.jooq.test;

import java.sql.Date;

import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;

/**
 * @author Lukas Eder
 */
public class Table1 extends TableImpl<Table1Record> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7621282509163949636L;

    public static final Table<Table1Record>               TABLE1      = new Table1();

    public static final TableField<Table1Record, Integer> FIELD_ID1   = createField("ID1", TestDataType.INTEGER_TYPE, TABLE1);
    public static final TableField<Table1Record, String>  FIELD_NAME1 = createField("NAME1", TestDataType.STRING_TYPE, TABLE1);
    public static final TableField<Table1Record, Date>    FIELD_DATE1 = createField("DATE1", TestDataType.DATE_TYPE, TABLE1);

    public Table1() {
        super("TABLE1");
    }

    @Override
    public Class<Table1Record> getRecordType() {
        return Table1Record.class;
    }
}