/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.Loader;
import org.jooq.LoaderCSVOptionsStep;
import org.jooq.LoaderCSVStep;
import org.jooq.LoaderError;
import org.jooq.LoaderJSONOptionsStep;
import org.jooq.LoaderJSONStep;
import org.jooq.LoaderOptionsStep;
import org.jooq.LoaderXMLStep;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.StringUtils;
import org.jooq.tools.csv.CSVParser;
import org.jooq.tools.csv.CSVReader;

import org.xml.sax.InputSource;

/**
 * @author Lukas Eder
 * @author Johannes Bühler
 */
class LoaderImpl<R extends TableRecord<R>> implements

    // Cascading interface implementations for Loader behaviour
    LoaderOptionsStep<R>,
    LoaderXMLStep<R>,
    LoaderCSVStep<R>,
    LoaderCSVOptionsStep<R>,
    LoaderJSONStep<R>,
    LoaderJSONOptionsStep<R>,
    Loader<R> {

    // Configuration constants
    // -----------------------
    private static final int        ON_DUPLICATE_KEY_ERROR  = 0;
    private static final int        ON_DUPLICATE_KEY_IGNORE = 1;
    private static final int        ON_DUPLICATE_KEY_UPDATE = 2;

    private static final int        ON_ERROR_ABORT          = 0;
    private static final int        ON_ERROR_IGNORE         = 1;

    private static final int        COMMIT_NONE             = 0;
    private static final int        COMMIT_AFTER            = 1;
    private static final int        COMMIT_ALL              = 2;

    private static final int        CONTENT_CSV             = 0;
    private static final int        CONTENT_XML             = 1;
    private static final int        CONTENT_JSON            = 2;

    // Configuration data
    // ------------------
    private final DSLContext        create;
    private final Configuration     configuration;
    private final Table<R>          table;
    private int                     onDuplicate             = ON_DUPLICATE_KEY_ERROR;
    private int                     onError                 = ON_ERROR_ABORT;
    private int                     commit                  = COMMIT_NONE;
    private int                     commitAfter             = 1;
    private int                     content                 = CONTENT_CSV;
    private BufferedReader          data;
    private Integer markerLimit = Integer.MAX_VALUE;
    private boolean bulkInsert;

    // CSV configuration data
    // ----------------------
    private int                     ignoreRows              = 1;
    private char                    quote                   = CSVParser.DEFAULT_QUOTE_CHARACTER;
    private char                    separator               = CSVParser.DEFAULT_SEPARATOR;
    private String                  nullString              = null;
    private Field<?>[]              fields;
    private boolean[]               primaryKey;

    // Result data
    // -----------
    private int                     ignored;
    private int                     processed;
    private int                     stored;
    private final List<LoaderError> errors;

    LoaderImpl(Configuration configuration, Table<R> table) {
        this.create = DSL.using(configuration);
        this.configuration = configuration;
        this.table = table;
        this.errors = new ArrayList<LoaderError>();
    }

    // -------------------------------------------------------------------------
    // Configuration setup
    // -------------------------------------------------------------------------

    @Override
    public final LoaderImpl<R> onDuplicateKeyError() {
        onDuplicate = ON_DUPLICATE_KEY_ERROR;
        return this;
    }

    @Override
    public final LoaderImpl<R> onDuplicateKeyIgnore() {
        if (table.getPrimaryKey() == null) {
            throw new IllegalStateException("ON DUPLICATE KEY IGNORE only works on tables with explicit primary keys. Table is not updatable : " + table);
        }

        onDuplicate = ON_DUPLICATE_KEY_IGNORE;
        return this;
    }

    @Override
    public final LoaderImpl<R> onDuplicateKeyUpdate() {
        if (table.getPrimaryKey() == null) {
            throw new IllegalStateException("ON DUPLICATE KEY UPDATE only works on tables with explicit primary keys. Table is not updatable : " + table);
        }

        onDuplicate = ON_DUPLICATE_KEY_UPDATE;
        return this;
    }

    @Override
    public final LoaderImpl<R> onErrorIgnore() {
        onError = ON_ERROR_IGNORE;
        return this;
    }

    @Override
    public final LoaderImpl<R> onErrorAbort() {
        onError = ON_ERROR_ABORT;
        return this;
    }

    @Override
    public final LoaderImpl<R> commitEach() {
        commit = COMMIT_AFTER;
        return this;
    }

    @Override
    public final LoaderImpl<R> commitAfter(int number) {
        commit = COMMIT_AFTER;
        commitAfter = number;
        return this;
    }

    @Override
    public final LoaderImpl<R> commitAll() {
        commit = COMMIT_ALL;
        return this;
    }

    @Override
    public final LoaderImpl<R> commitNone() {
        commit = COMMIT_NONE;
        return this;
    }

    @Override
    public LoaderOptionsStep<R> withBulkInsert(int markerLimit) {
        this.bulkInsert = true;
        this.markerLimit = markerLimit;
        return this;
    }

    @Override
    public final LoaderImpl<R> loadCSV(File file) throws FileNotFoundException {
        content = CONTENT_CSV;
        data = new BufferedReader(new FileReader(file));
        return this;
    }

    @Override
    public final LoaderImpl<R> loadCSV(String csv) {
        content = CONTENT_CSV;
        data = new BufferedReader(new StringReader(csv));
        return this;
    }

    @Override
    public final LoaderImpl<R> loadCSV(InputStream stream) {
        content = CONTENT_CSV;
        data = new BufferedReader(new InputStreamReader(stream));
        return this;
    }

    @Override
    public final LoaderImpl<R> loadCSV(Reader reader) {
        content = CONTENT_CSV;
        data = new BufferedReader(reader);
        return this;
    }

    @Override
    public final LoaderImpl<R> loadXML(File file) throws FileNotFoundException {
        content = CONTENT_XML;
        throw new UnsupportedOperationException("This is not yet implemented");
    }

    @Override
    public final LoaderImpl<R> loadXML(String xml) {
        content = CONTENT_XML;
        throw new UnsupportedOperationException("This is not yet implemented");
    }

    @Override
    public final LoaderImpl<R> loadXML(InputStream stream) {
        content = CONTENT_XML;
        throw new UnsupportedOperationException("This is not yet implemented");
    }

    @Override
    public final LoaderImpl<R> loadXML(Reader reader) {
        content = CONTENT_XML;
        throw new UnsupportedOperationException("This is not yet implemented");
    }

    @Override
    public final LoaderImpl<R> loadXML(InputSource source) {
        content = CONTENT_XML;
        throw new UnsupportedOperationException("This is not yet implemented");
    }

    // -------------------------------------------------------------------------
    // CSV configuration
    // -------------------------------------------------------------------------

    @Override
    public final LoaderImpl<R> fields(Field<?>... f) {
        this.fields = f;
        this.primaryKey = new boolean[f.length];

        if (table.getPrimaryKey() != null) {
            for (int i = 0; i < fields.length; i++) {
                if (fields[i] != null) {
                    if (table.getPrimaryKey().getFields().contains(fields[i])) {
                        primaryKey[i] = true;
                    }
                }
            }
        }

        return this;
    }

    @Override
    public final LoaderImpl<R> fields(Collection<? extends Field<?>> f) {
        return fields(f.toArray(new Field[f.size()]));
    }

    @Override
    public final LoaderImpl<R> ignoreRows(int number) {
        ignoreRows = number;
        return this;
    }

    @Override
    public final LoaderImpl<R> quote(char q) {
        this.quote = q;
        return this;
    }

    @Override
    public final LoaderImpl<R> separator(char s) {
        this.separator = s;
        return this;
    }

    @Override
    public final LoaderImpl<R> nullString(String n) {
        this.nullString = n;
        return this;
    }

    @Override
    public final LoaderJSONStep<R> loadJSON(File file) throws FileNotFoundException {
        content = CONTENT_JSON;
        data = new BufferedReader(new FileReader(file));
        return this;
    }

    @Override
    public final LoaderJSONStep<R> loadJSON(String json) {
        content = CONTENT_JSON;
        data = new BufferedReader(new StringReader(json));
        return this;

    }

    @Override
    public final LoaderJSONStep<R> loadJSON(InputStream stream) {
        content = CONTENT_JSON;
        data = new BufferedReader(new InputStreamReader(stream));
        return this;
    }

    @Override
    public final LoaderJSONStep<R> loadJSON(Reader reader) {
        content = CONTENT_JSON;
        data = new BufferedReader(reader);
        return this;
    }

    // -------------------------------------------------------------------------
    // XML configuration
    // -------------------------------------------------------------------------

    // [...] to be specified

    // -------------------------------------------------------------------------
    // Execution
    // -------------------------------------------------------------------------

    @Override
    public final LoaderImpl<R> execute() throws IOException {
        if (content == CONTENT_CSV) {
            executeCSV();
        }
        else if (content == CONTENT_XML) {
            throw new UnsupportedOperationException();
        }
        else if (content == CONTENT_JSON) {
            executeJSON();
        }
        else {
            throw new IllegalStateException();
        }

        return this;
    }

    private void executeJSON() throws IOException {
        JSONReader reader = new JSONReader(data);

        try {

            // The current json format is not designed for streaming. Thats why
            // all records are loaded at once.
            List<String[]> allRecords = reader.readAll();
            executeSQL(allRecords.iterator());
        }

        // SQLExceptions originating from rollbacks or commits are always fatal
        // They are propagated, and not swallowed
        catch (SQLException e) {
            throw Utils.translate(null, e);
        }
        finally {
            reader.close();
        }
    }

    private final void executeCSV() throws IOException {
        CSVReader reader = new CSVReader(data, separator, quote, ignoreRows);

        try {
            executeSQL(reader);
        }

        // SQLExceptions originating from rollbacks or commits are always fatal
        // They are propagated, and not swallowed
        catch (SQLException e) {
            throw Utils.translate(null, e);
        }
        finally {
            reader.close();
        }
    }

    private void executeSQL(Iterator<String[]> reader) throws SQLException {
        String[] row;
        if (bulkInsert) {
            executeBulk(reader);
        } else {
            rowloop:
            while (reader.hasNext() && ((row = reader.next()) != null)) {

                // [#1627] Handle NULL values
                for (int i = 0; i < row.length; i++) {
                    if (StringUtils.equals(nullString, row[i])) {
                        row[i] = null;
                    }
                }

                processed++;
                InsertQuery<R> insert = create.insertQuery(table);

                for (int i = 0; i < row.length; i++) {
                    if (i < fields.length && fields[i] != null) {
                        addValue0(insert, fields[i], row[i]);
                    }
                }

                // TODO: This is only supported by some dialects. Let other
                // dialects execute a SELECT and then either an INSERT or UPDATE
                if (onDuplicate == ON_DUPLICATE_KEY_UPDATE) {
                    insert.onDuplicateKeyUpdate(true);

                    for (int i = 0; i < row.length; i++) {
                        if (i < fields.length && fields[i] != null && !primaryKey[i]) {
                            addValueForUpdate0(insert, fields[i], row[i]);
                        }
                    }
                }

                // TODO: This can be implemented faster using a MERGE statement
                // in some dialects
                else if (onDuplicate == ON_DUPLICATE_KEY_IGNORE) {
                    SelectQuery<R> select = create.selectQuery(table);

                    for (int i = 0; i < row.length; i++) {
                        if (i < fields.length && primaryKey[i]) {
                            select.addConditions(getCondition(fields[i], row[i]));
                        }
                    }

                    try {
                        if (select.execute() > 0) {
                            ignored++;
                            continue rowloop;
                        }
                    } catch (DataAccessException e) {
                        errors.add(new LoaderErrorImpl(e, row, processed - 1, select));
                    }
                }

                // Don't do anything. Let the execution fail
                else if (onDuplicate == ON_DUPLICATE_KEY_ERROR) {
                }

                try {
                    insert.execute();
                    stored++;

                    if (commit == COMMIT_AFTER) {
                        if (processed % commitAfter == 0) {
                            configuration.connectionProvider().acquire().commit();
                        }
                    }
                } catch (DataAccessException e) {
                    errors.add(new LoaderErrorImpl(e, row, processed - 1, insert));
                    ignored++;

                    if (onError == ON_ERROR_ABORT) {
                        break rowloop;
                    }
                }
            }
        }
        // TODO: When running in COMMIT_AFTER > 1
        // or COMMIT_ALL mode, then
        // it might be better to bulk load / merge n records

        // Rollback on errors in COMMIT_ALL mode
        try {
            if (commit == COMMIT_ALL) {
                if (!errors.isEmpty()) {
                    stored = 0;
                    configuration.connectionProvider().acquire().rollback();
                } else {
                    configuration.connectionProvider().acquire().commit();
                }
            }

            // Commit remaining elements in COMMIT_AFTER mode
            else if (commit == COMMIT_AFTER) {
                if (processed % commitAfter != 0) {
                    configuration.connectionProvider().acquire().commit();
                }
            }
        } catch (DataAccessException e) {
            errors.add(new LoaderErrorImpl(e, null, processed - 1, null));
        }
    }

    private void executeBulk(Iterator<String[]> reader) {
        String[] row;
        InsertQuery<R> bulkInsertQuery = create.insertQuery(table);
        int bulkInsertBlockSize = 0;
        while (reader.hasNext()) {
            row = reader.next();
            bulkInsertBlockSize++;
            processed++;
            stored++;
            bulkInsertQuery.newRecord();
            for (int i = 0; i < row.length; i++) {
                if (i < fields.length && fields[i] != null) {
                    addValue0(bulkInsertQuery, fields[i], row[i]);
                }
            }
            if ((bulkInsertBlockSize * fields.length) + fields.length >= markerLimit) {
                bulkInsertBlockSize = 0;
                if (!executeStatement(bulkInsertQuery)) {
                    return;
                }
                bulkInsertQuery = create.insertQuery(table);
            }
        }
        executeStatement(bulkInsertQuery);
    }

    private boolean executeStatement(InsertQuery<R> bulkInsertQuery) {
        try {
            bulkInsertQuery.execute();
        } catch (DataAccessException e) {
            errors.add(new LoaderErrorImpl(e, null, -1, bulkInsertQuery));
            ignored++;

            if (onError == ON_ERROR_ABORT) {
                return false;
            }
        }
        return true;
    }

    /**
     * Type-safety...
     */
    private <T> void addValue0(InsertQuery<R> insert, Field<T> field, String row) {
        insert.addValue(field, field.getDataType().convert(row));
    }

    /**
     * Type-safety...
     */
    private <T> void addValueForUpdate0(InsertQuery<R> insert, Field<T> field, String row) {
        insert.addValueForUpdate(field, field.getDataType().convert(row));
    }

    /**
     * Get a type-safe condition
     */
    private <T> Condition getCondition(Field<T> field, String string) {
        return field.equal(field.getDataType().convert(string));
    }

    // -------------------------------------------------------------------------
    // Outcome
    // -------------------------------------------------------------------------

    @Override
    public final List<LoaderError> errors() {
        return errors;
    }

    @Override
    public final int processed() {
        return processed;
    }

    @Override
    public final int ignored() {
        return ignored;
    }

    @Override
    public final int stored() {
        return stored;
    }
}
