/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */

package org.jooq.util;

import java.util.List;

import org.jooq.Record;
import org.jooq.Table;

/**
 * The definition of a table or view.
 *
 * @author Lukas Eder
 */
public interface TableDefinition extends Definition {

    /**
     * All columns in the type, table or view.
     */
    List<ColumnDefinition> getColumns();

    /**
     * Get a column in this type by its name.
     */
    ColumnDefinition getColumn(String columnName);

    /**
     * Get a column in this type by its name.
     */
    ColumnDefinition getColumn(String columnName, boolean ignoreCase);

    /**
     * Get a column in this type by its index (starting at 0).
     */
    ColumnDefinition getColumn(int columnIndex);

    /**
     * Get the primary key for this table.
     */
    UniqueKeyDefinition getPrimaryKey();

    /**
     * Get the unique keys for this table.
     */
    List<UniqueKeyDefinition> getUniqueKeys();

    /**
     * Get the foreign keys for this table.
     */
    List<ForeignKeyDefinition> getForeignKeys();

    /**
     * Get the <code>CHECK</code> constraints for this table.
     */
    List<CheckConstraintDefinition> getCheckConstraints();

    /**
     * Get the <code>IDENTITY</code> column of this table, or <code>null</code>,
     * if no such column exists.
     */
    IdentityDefinition getIdentity();

    /**
     * This TableDefinition as a {@link Table}.
     */
    Table<Record> getTable();

}
