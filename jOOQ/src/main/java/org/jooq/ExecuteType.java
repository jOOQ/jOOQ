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

/**
 * The type of database interaction that is being executed with this context.
 */
public enum ExecuteType {

    /**
     * A <code>SELECT</code> query is being executed
     * <p>
     * This may also apply to plain SQL <code>WITH .. SELECT</code> queries
     * (selections with common table expressions), <code>FETCH</code> queries
     * and other types of vendor-specific queries.
     */
    READ,

    /**
     * An <code>INSERT</code>, <code>UPDATE</code>, <code>DELETE</code>,
     * <code>MERGE</code> query is being executed
     * <p>
     * This may also apply to plain SQL <code>REPLACE</code>,
     * <code>UPSERT</code> and other vendor-specific queries.
     */
    WRITE,

    /**
     * A DDL statement is being executed
     * <p>
     * Currently, this only applies to <code>TRUNCATE</code> statements
     */
    DDL,

    /**
     * A batch statement is being executed (not yet supported)
     */
    BATCH,

    /**
     * A routine (stored procedure or function) is being executed
     */
    ROUTINE,

    /**
     * An other (unknown) type of database interaction is being executed
     */
    OTHER,
}
