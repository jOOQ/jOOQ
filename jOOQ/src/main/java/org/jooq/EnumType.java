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
 * A SQL enum type. This can be any of the following:
 * <ul>
 * <li>In {@link SQLDialect#MARIADB}, this can be a column-scope enum type</li>
 * <li>In {@link SQLDialect#MYSQL}, this can be a column-scope enum type</li>
 * <li>In {@link SQLDialect#POSTGRES}, this can be a schema-scope enum type</li>
 * <li>In all other dialects, this can be an enum type as defined in the code
 * generation configuration [#968]</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface EnumType {

    /**
     * The literal as defined in the database
     */
    String getLiteral();

    /**
     * The schema of the enum type, if applicable (Postgres schema-scope enum
     * type only). Otherwise, this returns <code>null</code>
     */
    Schema getSchema();

    /**
     * The type name as registered in the database, if applicable (Postgres
     * schema-scope enum type only). Otherwise, this returns <code>null</code>
     */
    String getName();
}
