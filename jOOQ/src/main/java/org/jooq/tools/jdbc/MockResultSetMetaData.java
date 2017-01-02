/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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