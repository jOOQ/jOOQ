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
package org.jooq.test._;

import org.jooq.TableField;
import org.jooq.impl.CustomTable;
import org.jooq.impl.SQLDataType;

/**
 * @author Lukas Eder
 */
public class BookTable extends CustomTable<BookRecord> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -1124714471434439420L;

    public static final BookTable                      BOOK       = new BookTable();

    public static final TableField<BookRecord, String> FIRST_NAME = createField("FIRST_NAME", SQLDataType.VARCHAR, BOOK);
    public static final TableField<BookRecord, String> UNMATCHED  = createField("UNMATCHED", SQLDataType.VARCHAR, BOOK);
    public static final TableField<BookRecord, String> LAST_NAME  = createField("LAST_NAME", SQLDataType.VARCHAR, BOOK);
    public static final TableField<BookRecord, Short>  ID         = createField("ID", SQLDataType.SMALLINT, BOOK);
    public static final TableField<BookRecord, String> TITLE      = createField("TITLE", SQLDataType.VARCHAR, BOOK);

    protected BookTable() {
        super(null);
    }

    @Override
    public Class<? extends BookRecord> getRecordType() {
        return BookRecord.class;
    }
}
