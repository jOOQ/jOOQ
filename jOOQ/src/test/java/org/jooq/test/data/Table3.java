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
package org.jooq.test.data;

import java.sql.Date;

import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

/**
 * @author Lukas Eder
 */
public class Table3 extends TableImpl<Table3Record> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7621282509163949636L;

    public static final Table<Table3Record>               TABLE3      = new Table3();

    public static final TableField<Table3Record, Integer> FIELD_ID3   = createField("ID3", SQLDataType.INTEGER, TABLE3);
    public static final TableField<Table3Record, String>  FIELD_NAME3 = createField("NAME3", SQLDataType.VARCHAR, TABLE3);
    public static final TableField<Table3Record, Date>    FIELD_DATE3 = createField("DATE3", SQLDataType.DATE, TABLE3);

    public Table3() {
        super("TABLE3");
    }

    @Override
    public Class<Table3Record> getRecordType() {
        return Table3Record.class;
    }
}