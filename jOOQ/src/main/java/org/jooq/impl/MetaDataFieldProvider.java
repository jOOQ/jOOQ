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

import static org.jooq.impl.DSL.field;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
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
            log.warn("Cannot fetch column count for cursor : " + e.getMessage());
            fieldList.add(field("dummy"));
        }

        try {
            for (int i = 1; i <= columnCount; i++) {
                String name = meta.getColumnLabel(i);
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

                // [#650, #667] TODO This should not happen. All types
                // should be known at this point
                catch (SQLDialectNotSupportedException ignore) {
                    log.warn("Not supported by dialect", ignore.getMessage());
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
