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
package org.jooq.impl;

import org.jooq.Cursor;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Table;
import org.jooq.TableRecord;

/**
 * A base class for custom {@link TableRecord} implementations in client code.
 * <p>
 * Client code may provide proper {@link TableRecord} implementations extending
 * this useful base class. All necessary parts of the {@link TableRecord}
 * interface are already implemented. No methods need further implementation.
 * <p>
 * Use this base class when providing custom tables to any of the following
 * methods:
 * <ul>
 * <li>{@link ResultQuery#fetchInto(Table)}</li>
 * <li>{@link Cursor#fetchInto(Table)}</li>
 * <li>{@link Result#into(Table)}</li>
 * <li>{@link Record#into(Table)}</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public abstract class CustomRecord<R extends TableRecord<R>> extends TableRecordImpl<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 5287021930962460241L;

    protected CustomRecord(Table<R> table) {
        super(table);
    }
}
