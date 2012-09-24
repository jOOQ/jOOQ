/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
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
package org.jooq.tools.debug.impl;

import static org.jooq.impl.Factory.fieldByName;
import static org.jooq.impl.Factory.schemaByName;
import static org.jooq.impl.Factory.tableByName;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.Factory;
import org.jooq.tools.debug.QueryExecutor;

/**
 * A local query executor
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
class QueryExecutorImpl implements QueryExecutor {

    private final Configuration configuration;

    QueryExecutorImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public final Schema[] schemata() {
        try {
            DatabaseMetaData meta = meta();
            String[] names = create().fetch(meta.getSchemas()).intoArray(0, String.class);
            Schema[] result = new Schema[names.length];

            for (int i = 0; i < names.length; i++) {
                result[i] = schemaByName(names[i]);
            }

            return result;
        }
        catch (SQLException e) {
            throw new DataAccessException("Cannot fetch schemata", e);
        }
    }

    @Override
    public final Table<?>[] tables(Schema... schemata) {
        schemata = (schemata != null && schemata.length > 0) ? schemata : new Schema[] { null };
        List<Table<?>> result = new ArrayList<Table<?>>();

        try {
            DatabaseMetaData meta = meta();

            for (Schema schema : schemata) {
                String schemaName = (schema != null) ? schema.getName() : null;

                for (Record record : create().fetch(meta.getTables(null, schemaName, null, null))) {
                    result.add(tableByName(
                        record.getValue(1, String.class),
                        record.getValue(2, String.class)));
                }
            }

            return result.toArray(new Table[result.size()]);
        }
        catch (SQLException e) {
            throw new DataAccessException("Cannot fetch tables", e);
        }
    }

    @Override
    public final Field<?>[] fields(Table<?>... tables) {
        tables = (tables != null && tables.length > 0) ? tables : new Table[] { null };
        List<Field<?>> result = new ArrayList<Field<?>>();

        try {
            DatabaseMetaData meta = meta();

            for (Table<?> table : tables) {
                String schemaName = (table != null && table.getSchema() != null) ? table.getSchema().getName() : null;
                String tableName = (table != null) ? table.getName() : null;

                for (Record record : create().fetch(meta.getColumns(null, schemaName, tableName, null))) {
                    // Discover SQLDataType, here?
                    result.add(fieldByName(
                        record.getValue(1, String.class),
                        record.getValue(2, String.class),
                        record.getValue(3, String.class)));
                }
            }

            return result.toArray(new Field[result.size()]);
        }
        catch (SQLException e) {
            throw new DataAccessException("Cannot fetch fields", e);
        }
    }

    @Override
    public final int execute(Query query) {
        create().attach(query);
        return query.execute();
    }

    @Override
    public final <R extends Record> Result<R> fetch(ResultQuery<R> query) {
        create().attach(query);
        return query.fetch();
    }

    private final DatabaseMetaData meta() {
        try {
            return create().getConnection().getMetaData();
        }
        catch (SQLException e) {
            throw new DataAccessException("Cannot fetch meta data", e);
        }
    }

    private final Factory create() {
        return new Factory(configuration.getConnection(), configuration.getDialect(), configuration.getSettings());
    }
}
