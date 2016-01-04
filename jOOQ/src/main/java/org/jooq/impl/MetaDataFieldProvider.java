/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.JooqLogger;

/**
 * A <code>FieldProvider</code> providing fields for a JDBC
 * {@link ResultSetMetaData} object.
 * <p>
 * This can be used when a <code>Cursor</code> doesn't know its fields before
 * actually fetching data from the database. Use-cases for this are:
 * <ul>
 * <li>Plain SQL tables</li>
 * <li>CURSOR type OUT parameters from stored procedures</li>
 * <li>CURSOR type return values from stored functions</li>
 * </ul>
 *
 * @author Lukas Eder
 */
class MetaDataFieldProvider implements Serializable {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = -8482521025536063609L;
    private static final JooqLogger log              = JooqLogger.getLogger(MetaDataFieldProvider.class);

    private final Fields<Record>    fields;

    MetaDataFieldProvider(Configuration configuration, ResultSetMetaData meta) {
        this.fields = init(configuration, meta);
    }

    private Fields<Record> init(Configuration configuration, ResultSetMetaData meta) {
        List<Field<?>> fieldList = new ArrayList<Field<?>>();
        int columnCount = 0;

        try {
            columnCount = meta.getColumnCount();
        }

        // This happens in Oracle for empty cursors returned from stored
        // procedures / functions
        catch (SQLException e) {
            log.info("Cannot fetch column count for cursor : " + e.getMessage());
            fieldList.add(field("dummy"));
        }

        try {
            for (int i = 1; i <= columnCount; i++) {
                Name name;

                String columnLabel = meta.getColumnLabel(i);
                String columnName = meta.getColumnName(i);

                if (columnName.equals(columnLabel)) {
                    String columnSchema = meta.getSchemaName(i);
                    String columnTable = meta.getTableName(i);

                    name = name(columnSchema, columnTable, columnName);
                }
                else {
                    name = name(columnLabel);
                }

                int precision = meta.getPrecision(i);
                int scale = meta.getScale(i);
                DataType<?> dataType = SQLDataType.OTHER;
                String type = meta.getColumnTypeName(i);

                try {
                    dataType = DefaultDataType.getDataType(configuration.dialect().family(), type, precision, scale);

                    if (dataType.hasPrecision()) {
                        dataType = dataType.precision(precision);
                    }
                    if (dataType.hasScale()) {
                        dataType = dataType.scale(scale);
                    }
                    if (dataType.hasLength()) {

                        // JDBC doesn't distinguish between precision and length
                        dataType = dataType.length(precision);
                    }
                }

                // [#650, #667] All types should be known at this point, but in plain
                // SQL environments, it is possible that user-defined types, or vendor-specific
                // types (e.g. such as PostgreSQL's json type) will cause this exception.
                catch (SQLDialectNotSupportedException ignore) {
                    log.debug("Not supported by dialect", ignore.getMessage());
                }

                fieldList.add(field(name, dataType));
            }
        }
        catch (SQLException e) {
            throw Utils.translate(null, e);
        }

        return new Fields<Record>(fieldList);
    }

    final Field<?>[] getFields() {
        return fields.fields();
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return fields.toString();
    }
}
