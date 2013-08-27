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
package org.jooq;

import java.io.Serializable;

/**
 * An <code>Identity</code> is an object representing an <code>IDENTITY</code>
 * column as understood by the SQL:2003 standard. In most RDBMS, this is
 * actually called an <code>IDENTITY</code> column. Some RDMBS such as MySQL
 * call it <code>AUTO_INCREMENT</code> columns.
 *
 * @param <R> The <code>IDENTITY</code>'s owner table record
 * @param <T> The <code>IDENTITY</code>'s field type
 * @author Lukas Eder
 */
public interface Identity<R extends Record, T> extends Serializable {

    /**
     * The <code>IDENTITY</code>'s owner table
     */
    Table<R> getTable();

    /**
     * The <code>IDENTITY</code> column.
     */
    TableField<R, T> getField();
}
