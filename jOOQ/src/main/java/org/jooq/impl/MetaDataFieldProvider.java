/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.SQLDialectNotSupportedException;

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
class MetaDataFieldProvider implements FieldProvider, Serializable {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID = -8482521025536063609L;
    private static final JooqLogger log              = JooqLogger.getLogger(MetaDataFieldProvider.class);

    private final Configuration     configuration;
    private final FieldList         fields;
    private ResultSetMetaData       meta;

    MetaDataFieldProvider(Configuration configuration, ResultSetMetaData meta) {
        this.configuration = configuration;
        this.meta = meta;
        this.fields = new FieldList();

        init();
    }

    private final void init() {
        Factory create = Factory.getNewFactory(configuration);

        int columnCount = 0;

        try {
            columnCount = meta.getColumnCount();
        }

        // This happens in Oracle for empty cursors returned from stored
        // procedures / functions
        catch (SQLException e) {
            log.warn("Cannot fetch column count for cursor : " + e.getMessage());
            fields.add(create.field("dummy", SQLDataType.OTHER));
        }

        try {
            for (int i = 1; i <= columnCount; i++) {
                String name = meta.getColumnLabel(i);
                int precision = meta.getPrecision(i);
                int scale = meta.getScale(i);
                DataType<?> dataType = SQLDataType.OTHER;

                // TODO: [#650] Find a more intelligent way to strip the
                // schema from Oracle UDT names!
                String type = meta.getColumnTypeName(i).replaceAll(".*\\.", "");

                try {
                    dataType = FieldTypeHelper.getDialectDataType(configuration.getDialect(), type, precision, scale);
                }

                // [#650, #667] TODO This should not happen. All types
                // should be known at this point
                catch (SQLDialectNotSupportedException ignore) {
                    log.warn("Not supported by dialect", ignore.getMessage());
                }

                fields.add(create.field(name, dataType));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        meta = null;
    }

    @Override
    public final <T> Field<T> getField(Field<T> field) {
        return fields.getField(field);
    }

    @Override
    public final Field<?> getField(String name) {
        return fields.getField(name);
    }

    @Override
    public final Field<?> getField(int index) {
        return fields.getField(index);
    }

    @Override
    public final List<Field<?>> getFields() {
        return fields.getFields();
    }

    @Override
    public final int getIndex(Field<?> field) throws IllegalArgumentException {
        return fields.getIndex(field);
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return fields.toString();
    }
}
