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
package org.jooq.tools.jdbc;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.jooq.AttachableInternal;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.types.UNumber;

/**
 * A mock result set meta data object.
 *
 * @author Lukas Eder
 * @see MockConnection
 */
public class MockResultSetMetaData implements ResultSetMetaData, Serializable {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -6859273409631070434L;

    /**
     * The result set reference.
     */
    private final MockResultSet rs;

    /**
     * Create a new mock result set meta data object
     */
    public MockResultSetMetaData(MockResultSet rs) {
        this.rs = rs;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public int getColumnCount() throws SQLException {
        rs.checkNotClosed();

        return rs.result.fieldsRow().size();
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        rs.checkNotClosed();

        return false;
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        rs.checkNotClosed();

        return true;
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        rs.checkNotClosed();

        return true;
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        rs.checkNotClosed();

        return false;
    }

    @Override
    public int isNullable(int column) throws SQLException {
        rs.checkNotClosed();

        // TODO: Check generated JSR-303 or JPA annotations for nullability
        return ResultSetMetaData.columnNullableUnknown;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        rs.checkNotClosed();

        Field<?> field = rs.result.field(column - 1);
        Class<?> type = field.getType();

        return Number.class.isAssignableFrom(type) && !UNumber.class.isAssignableFrom(type);
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        return 0;
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return getColumnName(column);
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        rs.checkNotClosed();

        return rs.result.field(column - 1).getName();
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        rs.checkNotClosed();

        Field<?> field = rs.result.field(column - 1);
        if (field instanceof TableField) {
            Table<?> table = ((TableField<?, ?>) field).getTable();

            if (table != null) {
                Schema schema = table.getSchema();

                if (schema != null) {
                    Configuration configuration = ((AttachableInternal) rs.result).configuration();
                    Schema mapped = null;

                    if (configuration != null) {
                        mapped = DSL.using(configuration).map(schema);
                    }

                    if (mapped != null) {
                        return mapped.getName();
                    }
                    else {
                        return schema.getName();
                    }
                }
            }
        }

        // By default, no schema is available
        return "";
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        rs.checkNotClosed();

        // TODO: Check generated JSR-303 or JPA annotations for precision
        return 0;
    }

    @Override
    public int getScale(int column) throws SQLException {
        rs.checkNotClosed();

        // TODO: Check generated JSR-303 or JPA annotations for scale
        return 0;
    }

    @Override
    public String getTableName(int column) throws SQLException {
        rs.checkNotClosed();

        Field<?> field = rs.result.field(column - 1);
        if (field instanceof TableField) {
            Table<?> table = ((TableField<?, ?>) field).getTable();

            if (table != null) {
                return table.getName();
            }
        }

        // By default, no table is available
        return "";
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        rs.checkNotClosed();

        // jOOQ doesn't support catalogs yet
        return "";
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        rs.checkNotClosed();

        return rs.result.field(column - 1).getDataType().getSQLType();
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        rs.checkNotClosed();

        return rs.result.field(column - 1).getDataType().getTypeName();
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        rs.checkNotClosed();

        return true;
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        rs.checkNotClosed();

        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        rs.checkNotClosed();

        return false;
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        rs.checkNotClosed();

        return rs.result.field(column - 1).getType().getName();
    }
}