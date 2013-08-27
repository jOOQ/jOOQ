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

import org.jooq.Configuration;
import org.jooq.DataType;

/**
 * A wrapper for anonymous array data types
 *
 * @author Lukas Eder
 */
class ArrayDataType<T> extends DefaultDataType<T[]> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7883229760246533448L;

    private final DataType<T> elementType;

    public ArrayDataType(DataType<T> elementType) {
        super(null, elementType.getArrayType(), elementType.getTypeName(), elementType.getCastTypeName());

        this.elementType = elementType;
    }

    @Override
    public final String getTypeName(Configuration configuration) {
        String typeName = elementType.getTypeName(configuration);
        return getArrayType(configuration, typeName);
    }

    @Override
    public final String getCastTypeName(Configuration configuration) {
        String castTypeName = elementType.getCastTypeName(configuration);
        return getArrayType(configuration, castTypeName);
    }

    private static String getArrayType(Configuration configuration, String dataType) {
        switch (configuration.dialect()) {
            case HSQLDB:
                return dataType + " array";
            case POSTGRES:
                return dataType + "[]";
            case H2:
                return "array";

            // Default implementation is needed for hash-codes and toString()
            default:
                return dataType + "[]";
        }
    }
}
