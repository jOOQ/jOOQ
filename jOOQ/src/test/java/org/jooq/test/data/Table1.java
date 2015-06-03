/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.test.data;

import java.sql.Date;

import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.SQLDataType;
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

    public static final TableField<Table1Record, Integer> FIELD_ID1   = createField("ID1", SQLDataType.INTEGER, TABLE1);
    public static final TableField<Table1Record, String>  FIELD_NAME1 = createField("NAME1", SQLDataType.VARCHAR, TABLE1);
    public static final TableField<Table1Record, Date>    FIELD_DATE1 = createField("DATE1", SQLDataType.DATE, TABLE1);

    public Table1() {
        super("TABLE1");
    }

    @Override
    public Class<Table1Record> getRecordType() {
        return Table1Record.class;
    }
}