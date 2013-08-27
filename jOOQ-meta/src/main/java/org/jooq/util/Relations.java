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
 * A model for all known Referenced Key / Referencing Key relations in the
 * {@link Database}'s schema.
 *
 * @author Lukas Eder
 */
public interface Relations {

    /**
     * Get the the primary for a given column, or <code>null</code> if that
     * column is not part of the primary key.
     */
    UniqueKeyDefinition getPrimaryKey(ColumnDefinition column);

    /**
     * Get a list of referenced keys (primary or unique) for a given table, that
     * the column participates in. Returns an empty list if the given column is
     * not part of any primary key or unique key.
     */
    List<UniqueKeyDefinition> getUniqueKeys(ColumnDefinition column);

    /**
     * Get a list of referenced keys (primary or unique) for a given table.
     * Returns an empty list if the given table has no primary or unique keys.
     */
    List<UniqueKeyDefinition> getUniqueKeys(TableDefinition table);

    /**
     * Get a list of foreign keys for a given table, that the column
     * participates in. Returns an empty list if the given column is not part of
     * any foreign key.
     */
    List<ForeignKeyDefinition> getForeignKeys(ColumnDefinition column);

    /**
     * Get a list of foreign keys for a given table. Returns an empty list if
     * the given table has no foreign keys.
     */
    List<ForeignKeyDefinition> getForeignKeys(TableDefinition table);

    /**
     * Get a list of <code>CHECK</code> constraints for a given table. Returns
     * an empty list if the given table has no <code>CHECK</code> constraints.
     */
    List<CheckConstraintDefinition> getCheckConstraints(TableDefinition table);
}
