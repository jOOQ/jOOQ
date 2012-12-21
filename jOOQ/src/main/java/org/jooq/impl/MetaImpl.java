/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.DataType;
import org.jooq.ForeignKey;
import org.jooq.Meta;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.UpdatableTable;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
class MetaImpl implements Meta {

    private final Executor             executor;
    private transient DatabaseMetaData meta;

    MetaImpl(Executor executor) {
        this.executor = executor;
    }

    private final DatabaseMetaData meta() {
        if (meta == null) {
            try {
                meta = executor.getConnectionProvider().acquire().getMetaData();
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while accessing DatabaseMetaData", e);
            }
        }

        return meta;
    }

    @Override
    public final List<Catalog> getCatalogs() {
        try {
            List<Catalog> result = new ArrayList<Catalog>();
            Result<Record> schemas = executor.fetch(meta().getCatalogs());

            for (String name : schemas.getValues(0, String.class)) {
                result.add(new MetaCatalog(name));
            }

            // There should always be at least one (empty) catalog in a database
            if (result.isEmpty()) {
                result.add(new MetaCatalog(""));
            }

            return result;
        }
        catch (SQLException e) {
            throw new DataAccessException("Error while accessing DatabaseMetaData", e);
        }
    }

    @Override
    public final List<Schema> getSchemas() {
        List<Schema> result = new ArrayList<Schema>();

        for (Catalog catalog : getCatalogs()) {
            result.addAll(catalog.getSchemas());
        }

        return result;
    }

    @Override
    public final List<Table<?>> getTables() {
        List<Table<?>> result = new ArrayList<Table<?>>();

        for (Schema schema : getSchemas()) {
            result.addAll(schema.getTables());
        }

        return result;
    }

    private class MetaCatalog extends CatalogImpl {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -2821093577201327275L;

        MetaCatalog(String name) {
            super(name);
        }

        @Override
        public final List<Schema> getSchemas() {
            try {
                List<Schema> result = new ArrayList<Schema>();
                Result<Record> schemas = executor.fetch(meta().getSchemas());

                for (String name : schemas.getValues(0, String.class)) {
                    result.add(new MetaSchema(name));
                }

                return result;
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while accessing DatabaseMetaData", e);
            }
        }
    }

    private class MetaSchema extends SchemaImpl {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -2621899850912554198L;

        MetaSchema(String name) {
            super(name);
        }

        @Override
        public final List<Table<?>> getTables() {
            try {
                List<Table<?>> result = new ArrayList<Table<?>>();
                Result<Record> tables = executor.fetch(meta().getTables(null, getName(), "%", null));

                for (Record table : tables) {
                    String catalog = table.getValue(0, String.class);
                    String schema = table.getValue(1, String.class);
                    String name = table.getValue(2, String.class);

                    Result<Record> pkColumns = executor.fetch(meta().getPrimaryKeys(catalog, schema, name))
                                                       .sortAsc("KEY_SEQ");

                    if (pkColumns.size() == 0) {
                        result.add(new MetaTable(name, this));
                    }
                    else {
                        result.add(new MetaUpdatableTable(name, this));
                    }
                }

                return result;
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while accessing DatabaseMetaData", e);
            }
        }
    }

    private class MetaTable extends TableImpl<Record> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = 4843841667753000233L;

        MetaTable(String name, Schema schema) {
            super(name, schema);

            init();
        }

        @SuppressWarnings("deprecation")
        private final void init() {
            try {
                Result<Record> columns = executor.fetch(meta().getColumns(null, getSchema().getName(), getName(), "%"));

                for (Record column : columns) {
                    String columnName = column.getValue("COLUMN_NAME", String.class);
                    String typeName = column.getValue("TYPE_NAME", String.class);
                    int precision = column.getValue("COLUMN_SIZE", int.class);
                    int scale = column.getValue("DECIMAL_DIGITS", int.class);

                    // TODO: Exception handling should be moved inside SQLDataType
                    DataType<?> type = null;
                    try {
                        type = DefaultDataType.getDataType(SQLDialect.SQL99, typeName, precision, scale);
                    }
                    catch (SQLDialectNotSupportedException e) {
                        type = SQLDataType.OTHER;
                    }

                    createField(columnName, type, this);
                }
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while accessing DatabaseMetaData", e);
            }
        }
    }

    private class MetaUpdatableTable extends MetaTable implements UpdatableTable<Record> {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -4555457095396846609L;

        MetaUpdatableTable(String name, Schema schema) {
            super(name, schema);
        }

        @Override
        public final UniqueKey<Record> getMainKey() {
            return null;
        }

        @Override
        public final List<UniqueKey<Record>> getKeys() {
            return Collections.emptyList();
        }

        @Override
        public final <O extends Record> List<ForeignKey<O, Record>> getReferencesFrom(Table<O> other) {
            return other.getReferencesTo(this);
        }

        @Override
        public final TableField<Record, ? extends Number> getRecordVersion() {
            return null;
        }

        @Override
        public final TableField<Record, ? extends Date> getRecordTimestamp() {
            return null;
        }
    }
}
