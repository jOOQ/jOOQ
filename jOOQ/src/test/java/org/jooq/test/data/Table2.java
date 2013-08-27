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
package org.jooq.test.data;

import java.sql.Date;

import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

/**
 * @author Lukas Eder
 */
public class Table2 extends TableImpl<Table2Record> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7621282509163949636L;

    public static final Table<Table2Record>               TABLE2      = new Table2();

    public static final TableField<Table2Record, Integer> FIELD_ID2   = createField("ID2", SQLDataType.INTEGER, TABLE2);
    public static final TableField<Table2Record, String>  FIELD_NAME2 = createField("NAME2", SQLDataType.VARCHAR, TABLE2);
    public static final TableField<Table2Record, Date>    FIELD_DATE2 = createField("DATE2", SQLDataType.DATE, TABLE2);

    public Table2() {
        super("TABLE2");
    }

    @Override
    public Class<Table2Record> getRecordType() {
        return Table2Record.class;
    }
}