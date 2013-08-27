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

/**
 * An object holding information about an inverse foreign key relationship.
 * <p>
 * This object may either represent a primary key or a unique key. It holds a
 * list of foreign keys referencing this key.
 *
 * @author Lukas Eder
 */
public interface UniqueKeyDefinition extends Definition {

    /**
     * Whether this unique key is the primary key
     */
    boolean isPrimaryKey();

    /**
     * The list of columns making up the primary key.
     */
    List<ColumnDefinition> getKeyColumns();

    /**
     * The foreign keys referencing this primary key
     */
    List<ForeignKeyDefinition> getForeignKeys();

    /**
     * The table holding this key
     */
    TableDefinition getTable();
}
