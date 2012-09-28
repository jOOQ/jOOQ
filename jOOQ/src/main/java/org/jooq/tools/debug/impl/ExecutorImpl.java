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
import org.jooq.tools.debug.Executor;

/**
 * A local query executor
 *
 * @author Christopher Deckers
 * @author Lukas Eder
 */
class ExecutorImpl implements Executor {

    private final String        name;
    private final Configuration configuration;
    private final Schema[]      schemata;

    ExecutorImpl(String name, Configuration configuration, Schema... schemata) {
        this.name = name;
        this.configuration = configuration;
        this.schemata = schemata != null ? schemata : new Schema[0];
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final Schema[] getSchemata() {
        if (schemata.length != 0) {
            return getGeneratedSchemata();
        }
        else {
            return getJDBCSchemata();
        }
    }

    @Override
    public final Table<?>[] getTables(Schema... filter) {
        if (schemata.length != 0) {
            return getGeneratedTables(filter);
        }
        else {
            return getJDBCTables(filter);
        }
    }

    @Override
    public final Field<?>[] getFields(Table<?>... filter) {
        if (schemata.length != 0) {
            return getGeneratedFields(filter);
        }
        else {
            return getJDBCFields(filter);
        }
    }

    private final Schema[] getJDBCSchemata() {
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

    private final Schema[] getGeneratedSchemata() {
        return schemata.clone();
    }

    private final Table<?>[] getGeneratedTables(Schema[] filter) {
        List<Table<?>> result = new ArrayList<Table<?>>();

        for (Schema schema : nonEmpty(filter, schemata)) {
            result.addAll(schema.getTables());
        }

        return result.toArray(new Table[result.size()]);
    }

    private final Field<?>[] getGeneratedFields(Table<?>[] filter) {
        List<Field<?>> result = new ArrayList<Field<?>>();

        for (Table<?> table : nonEmpty(filter, getGeneratedTables(schemata))) {
            result.addAll(table.getFields());
        }

        return result.toArray(new Field[result.size()]);
    }

    private final Table<?>[] getJDBCTables(Schema... filter) {
        filter = (filter != null && filter.length > 0) ? filter : new Schema[] { null };
        List<Table<?>> result = new ArrayList<Table<?>>();

        try {
            DatabaseMetaData meta = meta();

            for (Schema schema : filter) {
                String schemaName = (schema != null) ? schema.getName() : null;

                for (Record record : create().fetch(meta.getTables(null, schemaName, null, null))) {
                    result.add(tableByName(record.getValue(1, String.class), record.getValue(2, String.class)));
                }
            }

            return result.toArray(new Table[result.size()]);
        }
        catch (SQLException e) {
            throw new DataAccessException("Cannot fetch tables", e);
        }
    }

    private final Field<?>[] getJDBCFields(Table<?>... filter) {
        filter = (filter != null && filter.length > 0) ? filter : new Table[] { null };
        List<Field<?>> result = new ArrayList<Field<?>>();

        try {
            DatabaseMetaData meta = meta();

            for (Table<?> table : filter) {
                String schemaName = (table != null && table.getSchema() != null) ? table.getSchema().getName() : null;
                String tableName = (table != null) ? table.getName() : null;

                for (Record record : create().fetch(meta.getColumns(null, schemaName, tableName, null))) {
                    // Discover SQLDataType, here?
                    result.add(fieldByName(record.getValue(1, String.class), record.getValue(2, String.class),
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

    private final <T> T[] nonEmpty(T[] filter, T[] fallback) {
        return (filter != null && filter.length > 0) ? filter : fallback;
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
