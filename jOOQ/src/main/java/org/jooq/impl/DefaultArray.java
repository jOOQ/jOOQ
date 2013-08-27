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

import java.sql.Array;
import java.sql.ResultSet;
import java.util.Map;

import org.jooq.SQLDialect;
import org.jooq.exception.SQLDialectNotSupportedException;

class DefaultArray implements Array {

    private final SQLDialect dialect;
    private final Object[] array;
    private final Class<?> type;

    public DefaultArray(SQLDialect dialect, Object[] array, Class<?> type) {
        this.dialect = dialect;
        this.array = array;
        this.type = type;
    }

    @Override
    public String getBaseTypeName() {
        return DefaultDataType.getDataType(dialect, type.getComponentType()).getTypeName();
    }

    @Override
    public int getBaseType() {
        throw new SQLDialectNotSupportedException("Array.getBaseType()");
    }

    @Override
    public Object getArray() {
        return array;
    }

    @Override
    public Object getArray(Map<String, Class<?>> map) {
        return array;
    }

    @Override
    public Object getArray(long index, int count) {
        throw new SQLDialectNotSupportedException("Array.getArray(long, int)");
    }

    @Override
    public Object getArray(long index, int count, Map<String, Class<?>> map) {
        throw new SQLDialectNotSupportedException("Array.getArray(long, int, Map)");
    }

    @Override
    public ResultSet getResultSet() {
        throw new SQLDialectNotSupportedException("Array.getResultSet()");
    }

    @Override
    public ResultSet getResultSet(Map<String, Class<?>> map) {
        throw new SQLDialectNotSupportedException("Array.getResultSet(Map)");
    }

    @Override
    public ResultSet getResultSet(long index, int count) {
        throw new SQLDialectNotSupportedException("Array.getResultSet(long, int)");
    }

    @Override
    public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) {
        throw new SQLDialectNotSupportedException("Array.getResultSet(long, int, Map)");
    }

    @Override
    public void free() {
    }
}
