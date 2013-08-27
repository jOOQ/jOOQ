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


/**
 * A definition for a data type object.
 *
 * @author Lukas Eder
 */
public interface DataTypeDefinition {

    /**
     * The dialect-specific column type.
     */
    String getType();

    /**
     * The type's length.
     */
    int getLength();

    /**
     * The type's precision.
     */
    int getPrecision();

    /**
     * The type's scale.
     */
    int getScale();

    /**
     * The user type, if applicable.
     */
    String getUserType();

    /**
     * Whether this data type is nullable.
     */
    boolean isNullable();

    /**
     * Whether this data type is defaultable.
     */
    boolean isDefaulted();

    /**
     * Whether this data type represents a udt.
     */
    boolean isUDT();

    /**
     * Whether this data type is a NUMBER type without precision and scale.
     */
    boolean isGenericNumberType();

    /**
     * The underlying database.
     */
    Database getDatabase();

    /**
     * The underlying schema.
     */
    SchemaDefinition getSchema();

}
