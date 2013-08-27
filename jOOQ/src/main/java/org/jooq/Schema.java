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

import java.util.List;

/**
 * An object representing a database schema
 *
 * @author Lukas Eder
 */
public interface Schema extends QueryPart {

    /**
     * The name of this schema
     */
    String getName();

    /**
     * List all tables contained in this schema
     */
    List<Table<?>> getTables();

    /**
     * Get a table by its name (case-sensitive) in this schema, or
     * <code>null</code> if no such table exists
     */
    Table<?> getTable(String name);

    /**
     * List all UDTs contained in this schema
     */
    List<UDT<?>> getUDTs();

    /**
     * Get a UDT by its name (case-sensitive) in this schema, or
     * <code>null</code> if no such UDT exists
     */
    UDT<?> getUDT(String name);

    /**
     * List all sequences contained in this schema
     */
    List<Sequence<?>> getSequences();

    /**
     * Get a sequence by its name (case-sensitive) in this schema, or
     * <code>null</code> if no such sequence exists
     */
    Sequence<?> getSequence(String name);
}
