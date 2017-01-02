/*
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
 */
package org.jooq.impl;

import static org.jooq.impl.Tools.EMPTY_FIELD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.jooq.BatchBindStep;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertQuery;
import org.jooq.Loader;
import org.jooq.LoaderCSVOptionsStep;
import org.jooq.LoaderCSVStep;
import org.jooq.LoaderContext;
import org.jooq.LoaderError;
import org.jooq.LoaderFieldMapper;
import org.jooq.LoaderFieldMapper.LoaderFieldContext;
import org.jooq.LoaderJSONOptionsStep;
import org.jooq.LoaderJSONStep;
import org.jooq.LoaderListenerStep;
import org.jooq.LoaderOptionsStep;
import org.jooq.LoaderRowListener;
import org.jooq.LoaderRowsStep;
import org.jooq.LoaderXMLStep;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.LoaderConfigurationException;
import org.jooq.tools.StringUtils;
import org.jooq.tools.csv.CSVParser;
import org.jooq.tools.csv.CSVReader;

import org.xml.sax.InputSource;

/**
 * @author Lukas Eder
 * @author Johannes Bühler
 */
final class LoaderImpl<R extends Record> implements

    // Cascading interface implementations for Loader behaviour
    LoaderOptionsStep<R>,
    LoaderRowsStep<R>,
    LoaderXMLStep<R>,
    LoaderCSVStep<R>,
    LoaderCSVOptionsStep<R>,
    LoaderJSONStep<R>,
    LoaderJSONOptionsStep<R>,
    Loader<R> {

    // Configuration constants
    // -----------------------
    private static final int             ON_DUPLICATE_KEY_ERROR  = 0;
    private static final int             ON_DUPLICATE_KEY_IGNORE = 1;
    private static final int             ON_DUPLICATE_KEY_UPDATE = 2;

    private static final int             ON_ERROR_ABORT          = 0;
    private static final int             ON_ERROR_IGNORE         = 1;

    private static final int             COMMIT_NONE             = 0;
    private static final int             COMMIT_AFTER            = 1;
    private static final int             COMMIT_ALL              = 2;

    private static final int             BATCH_NONE              = 0;
    private static final int             BATCH_AFTER             = 1;
    private static final int             BATCH_ALL               = 2;

    private static final int             BULK_NONE               = 0;
    private static final int             BULK_AFTER              = 1;
    private static final int             BULK_ALL                = 2;

    private static final int             CONTENT_CSV             = 0;
    private static final int             CONTENT_XML             = 1;
    private static final int             CONTENT_JSON            = 2;
    private static final int             CONTENT_ARRAYS          = 3;

    // Configuration data
    // ------------------
    private final DSLContext             create;
    private final Configuration          configuration;
    private final Table<R>               table;
    private int                          onDuplicate             = ON_DUPLICATE_KEY_ERROR;
    private int                          onError                 = ON_ERROR_ABORT;
    private int                          commit                  = COMMIT_NONE;
    private int                          commitAfter             = 1;
    private int                          batch                   = BATCH_NONE;
    private int                          batchAfter              = 1;
    private int                          bulk                    = BULK_NONE;
    private int                          bulkAfter               = 1;
    private int                          content                 = CONTENT_CSV;
    private final InputDelay             data                    = new InputDelay();
    private Iterator<? extends Object[]> arrays;

    // CSV configuration data
    // ----------------------
    private int                          ignoreRows              = 1;
    private char                         quote                   = CSVParser.DEFAULT_QUOTE_CHARACTER;
    private char                         separator               = CSVParser.DEFAULT_SEPARATOR;
    private String                       nullString              = null;
    private Field<?>[]                   source;
    private Field<?>[]                   fields;
    private LoaderFieldMapper            fieldMapper;
    private boolean[]                    primaryKey;

    // Result data
    // -----------
    private LoaderRowListener            listener;
    private LoaderContext                result                  = new DefaultLoaderContext();
    private int                          ignored;
    private int                          processed;
    private int                          stored;
    private int                          executed;
    private int                          buffered;
    private final List<LoaderError>      errors;

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
    public final LoaderImpl<R> batchAll() {
        batch = BATCH_ALL;
        return this;
    }

    @Override
    public final LoaderImpl<R> batchNone() {
        batch = BATCH_NONE;
        return this;
    }

    @Override
    public final LoaderImpl<R> batchAfter(int number) {
        batch = BATCH_AFTER;
        batchAfter = number;
        return this;
    }

    @Override
    public final LoaderImpl<R> bulkAll() {
        bulk = BULK_ALL;
        return this;
    }

    @Override
    public final LoaderImpl<R> bulkNone() {
        bulk = BULK_NONE;
        return this;
    }

    @Override
    public final LoaderImpl<R> bulkAfter(int number) {
        bulk = BULK_AFTER;
        bulkAfter = number;
        return this;
    }

    @Override
    public final LoaderRowsStep<R> loadArrays(Object[]... a) {
        return loadArrays(Arrays.asList(a));
    }

    @Override
    public final LoaderRowsStep<R> loadArrays(Iterable<? extends Object[]> a) {
        return loadArrays(a.iterator());
    }

    @Override
    public final LoaderRowsStep<R> loadArrays(Iterator<? extends Object[]> a) {
        content = CONTENT_ARRAYS;
        this.arrays = a;
        return this;
    }

    @Override
    public final LoaderRowsStep<R> loadRecords(Record... records) {
        return loadRecords(Arrays.asList(records));
    }

    @Override
    public final LoaderRowsStep<R> loadRecords(Iterable<? extends Record> records) {
        return loadRecords(records.iterator());
    }

    @Override
    public final LoaderRowsStep<R> loadRecords(Iterator<? extends Record> records) {
        return loadArrays(new MappingIterator<Record, Object[]>(records, new MappingIterator.Function<Record, Object[]>() {
            @Override
            public final Object[] map(Record value) {
                if (value == null)
                    return null;

                if (source == null)
                    source = value.fields();

                return value.intoArray();
            }
        }));
    }



    @Override
    public final LoaderRowsStep<R> loadArrays(Stream<? extends Object[]> a) {
        return loadArrays(a.iterator());
    }

    @Override
    public final LoaderRowsStep<R> loadRecords(Stream<? extends Record> records) {
        return loadRecords(records.iterator());
    }



    @Override
    public final LoaderImpl<R> loadCSV(File file) {
        content = CONTENT_CSV;
        data.file = file;
        return this;
    }

    @Override
    public final LoaderImpl<R> loadCSV(File file, String charsetName) {
        data.charsetName = charsetName;
        return loadCSV(file);
    }

    @Override
    public final LoaderImpl<R> loadCSV(File file, Charset cs) {
        data.cs = cs;
        return loadCSV(file);
    }

    @Override
    public final LoaderImpl<R> loadCSV(File file, CharsetDecoder dec) {
        data.dec = dec;
        return loadCSV(file);
    }

    @Override
    public final LoaderImpl<R> loadCSV(String csv) {
        return loadCSV(new StringReader(csv));
    }

    @Override
    public final LoaderImpl<R> loadCSV(InputStream stream) {
        return loadCSV(new InputStreamReader(stream));
    }

    @Override
    public final LoaderImpl<R> loadCSV(InputStream stream, String charsetName) throws UnsupportedEncodingException {
        return loadCSV(new InputStreamReader(stream, charsetName));
    }

    @Override
    public final LoaderImpl<R> loadCSV(InputStream stream, Charset cs) {
        return loadCSV(new InputStreamReader(stream, cs));
    }

    @Override
    public final LoaderImpl<R> loadCSV(InputStream stream, CharsetDecoder dec) {
        return loadCSV(new InputStreamReader(stream, dec));
    }

    @Override
    public final LoaderImpl<R> loadCSV(Reader reader) {
        content = CONTENT_CSV;
        data.reader = new BufferedReader(reader);
        return this;
    }

    @Override
    public final LoaderImpl<R> loadXML(File file) {
        content = CONTENT_XML;
        data.file = file;
        return this;
    }

    @Override
    public final LoaderImpl<R> loadXML(File file, String charsetName) {
        data.charsetName = charsetName;
        return loadXML(file);
    }

    @Override
    public final LoaderImpl<R> loadXML(File file, Charset cs) {
        data.cs = cs;
        return loadXML(file);
    }

    @Override
    public final LoaderImpl<R> loadXML(File file, CharsetDecoder dec) {
        data.dec = dec;
        return loadXML(file);
    }

    @Override
    public final LoaderImpl<R> loadXML(String xml) {
        return loadXML(new StringReader(xml));
    }

    @Override
    public final LoaderImpl<R> loadXML(InputStream stream) {
        return loadXML(new InputStreamReader(stream));
    }

    @Override
    public final LoaderImpl<R> loadXML(InputStream stream, String charsetName) throws UnsupportedEncodingException {
        return loadXML(new InputStreamReader(stream, charsetName));
    }

    @Override
    public final LoaderImpl<R> loadXML(InputStream stream, Charset cs) {
        return loadXML(new InputStreamReader(stream, cs));
    }

    @Override
    public final LoaderImpl<R> loadXML(InputStream stream, CharsetDecoder dec) {
        return loadXML(new InputStreamReader(stream, dec));
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

    @Override
    public final LoaderImpl<R> loadJSON(File file) {
        content = CONTENT_JSON;
        data.file = file;
        return this;
    }

    @Override
    public final LoaderImpl<R> loadJSON(File file, String charsetName) {
        data.charsetName = charsetName;
        return loadJSON(file);
    }

    @Override
    public final LoaderImpl<R> loadJSON(File file, Charset cs) {
        data.cs = cs;
        return loadJSON(file);
    }

    @Override
    public final LoaderImpl<R> loadJSON(File file, CharsetDecoder dec) {
        data.dec = dec;
        return loadJSON(file);
    }

    @Override
    public final LoaderImpl<R> loadJSON(String json) {
        return loadJSON(new StringReader(json));
    }

    @Override
    public final LoaderImpl<R> loadJSON(InputStream stream) {
        return loadJSON(new InputStreamReader(stream));
    }

    @Override
    public final LoaderImpl<R> loadJSON(InputStream stream, String charsetName) throws UnsupportedEncodingException {
        return loadJSON(new InputStreamReader(stream, charsetName));
    }

    @Override
    public final LoaderImpl<R> loadJSON(InputStream stream, Charset cs) {
        return loadJSON(new InputStreamReader(stream, cs));
    }

    @Override
    public final LoaderImpl<R> loadJSON(InputStream stream, CharsetDecoder dec) {
        return loadJSON(new InputStreamReader(stream, dec));
    }

    @Override
    public final LoaderImpl<R> loadJSON(Reader reader) {
        content = CONTENT_JSON;
        data.reader = new BufferedReader(reader);
        return this;
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
        return fields(f.toArray(EMPTY_FIELD));
    }

    @Override
    public final LoaderListenerStep<R> fields(LoaderFieldMapper mapper) {
        fieldMapper = mapper;
        return this;
    }

    private final void fields0(Object[] row) {
        Field<?>[] f = new Field[row.length];

        // [#5145] When loading arrays, or when CSV headers are ignored,
        // the source is still null at this stage.
        if (source == null)
            source = Tools.fields(row.length);

        for (int i = 0; i < row.length; i++) {
            final int index = i;

            f[i] = fieldMapper.map(new LoaderFieldContext() {
                @Override
                public int index() {
                    return index;
                }

                @Override
                public Field<?> field() {
                    return source[index];
                }
            });
        }

        fields(f);
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

    // -------------------------------------------------------------------------
    // XML configuration
    // -------------------------------------------------------------------------

    // [...] to be specified

    // -------------------------------------------------------------------------
    // Listening
    // -------------------------------------------------------------------------

    @Override
    public final LoaderImpl<R> onRow(LoaderRowListener l) {
        listener = l;
        return this;
    }

    // -------------------------------------------------------------------------
    // Execution
    // -------------------------------------------------------------------------

    @Override
    public final LoaderImpl<R> execute() throws IOException {
        checkFlags();

        if (content == CONTENT_CSV) {
            executeCSV();
        }
        else if (content == CONTENT_XML) {
            throw new UnsupportedOperationException();
        }
        else if (content == CONTENT_JSON) {
            executeJSON();
        }
        else if (content == CONTENT_ARRAYS) {
            executeRows();
        }
        else {
            throw new IllegalStateException();
        }

        return this;
    }

    private void checkFlags() {
        if (batch != BATCH_NONE && onDuplicate == ON_DUPLICATE_KEY_IGNORE)
            throw new LoaderConfigurationException("Cannot apply batch loading with onDuplicateKeyIgnore flag. Turn off either flag.");

        if (bulk != BULK_NONE && onDuplicate != ON_DUPLICATE_KEY_ERROR)
            throw new LoaderConfigurationException("Cannot apply bulk loading with onDuplicateKey flags. Turn off either flag.");
    }

    private void executeJSON() throws IOException {
        JSONReader reader = null;

        try {
            reader = new JSONReader(data.reader());
            source = Tools.fieldsByName(reader.getFields());

            // The current json format is not designed for streaming. Thats why
            // all records are loaded at once.
            List<String[]> allRecords = reader.readAll();
            executeSQL(allRecords.iterator());
        }

        // SQLExceptions originating from rollbacks or commits are always fatal
        // They are propagated, and not swallowed
        catch (SQLException e) {
            throw Tools.translate(null, e);
        }
        finally {
            if (reader != null)
                reader.close();
        }
    }

    private final void executeCSV() throws IOException {
        CSVReader reader = null;

        try {
            if (ignoreRows == 1) {
                reader = new CSVReader(data.reader(), separator, quote, 0);
                source = Tools.fieldsByName(reader.next());
            }
            else {
                reader = new CSVReader(data.reader(), separator, quote, ignoreRows);
            }

            executeSQL(reader);
        }

        // SQLExceptions originating from rollbacks or commits are always fatal
        // They are propagated, and not swallowed
        catch (SQLException e) {
            throw Tools.translate(null, e);
        }
        finally {
            if (reader != null)
                reader.close();
        }
    }

    private void executeRows() {
        try {
            executeSQL(arrays);
        }

        // SQLExceptions originating from rollbacks or commits are always fatal
        // They are propagated, and not swallowed
        catch (SQLException e) {
            throw Tools.translate(null, e);
        }
    }

    private void executeSQL(Iterator<? extends Object[]> iterator) throws SQLException {
        Object[] row = null;
        BatchBindStep bind = null;
        InsertQuery<R> insert = null;

        execution: {
            rows: while (iterator.hasNext() && ((row = iterator.next()) != null)) {
                try {

                	// [#5145] Lazy initialisation of fields off the first row
                	//         in case LoaderFieldMapper was used.
                    if (fields == null)
                        fields0(row);

                    // [#1627] Handle NULL values
                    for (int i = 0; i < row.length; i++)
                        if (StringUtils.equals(nullString, row[i]))
                            row[i] = null;

                    // TODO: In batch mode, we can probably optimise this by not creating
                    // new statements every time, just to convert bind values to their
                    // appropriate target types. But beware of SQL dialects that tend to
                    // need very explicit casting of bind values (e.g. Firebird)
                    processed++;

                    // TODO: This can be implemented faster using a MERGE statement
                    // in some dialects
                    if (onDuplicate == ON_DUPLICATE_KEY_IGNORE) {
                        SelectQuery<R> select = create.selectQuery(table);

                        for (int i = 0; i < row.length; i++)
                            if (i < fields.length && primaryKey[i])
                                select.addConditions(getCondition(fields[i], row[i]));

                        try {
                            if (create.fetchExists(select)) {
                                ignored++;
                                continue rows;
                            }
                        }
                        catch (DataAccessException e) {
                            errors.add(new LoaderErrorImpl(e, row, processed - 1, select));
                        }
                    }

                    buffered++;

                    if (insert == null)
                        insert = create.insertQuery(table);

                    for (int i = 0; i < row.length; i++)
                        if (i < fields.length && fields[i] != null)
                            addValue0(insert, fields[i], row[i]);

                    // TODO: This is only supported by some dialects. Let other
                    // dialects execute a SELECT and then either an INSERT or UPDATE
                    if (onDuplicate == ON_DUPLICATE_KEY_UPDATE) {
                        insert.onDuplicateKeyUpdate(true);

                        for (int i = 0; i < row.length; i++)
                            if (i < fields.length && fields[i] != null && !primaryKey[i])
                                addValueForUpdate0(insert, fields[i], row[i]);
                    }

                    // Don't do anything. Let the execution fail
                    else if (onDuplicate == ON_DUPLICATE_KEY_ERROR) {}

                    try {
                        if (bulk != BULK_NONE) {
                            if (bulk == BULK_ALL || processed % bulkAfter != 0) {
                                insert.newRecord();
                                continue rows;
                            }
                        }

                        if (batch != BATCH_NONE) {
                            if (bind == null)
                                bind = create.batch(insert);

                            bind.bind(insert.getBindValues().toArray());
                            insert = null;

                            if (batch == BATCH_ALL || processed % (bulkAfter * batchAfter) != 0)
                                continue rows;
                        }

                        if (bind != null)
                            bind.execute();
                        else if (insert != null)
                            insert.execute();

                        stored += buffered;
                        executed++;

                        buffered = 0;
                        bind = null;
                        insert = null;

                        if (commit == COMMIT_AFTER)
                            if ((processed % batchAfter == 0) && ((processed / batchAfter) % commitAfter == 0))
                                commit();
                    }
                    catch (DataAccessException e) {
                        errors.add(new LoaderErrorImpl(e, row, processed - 1, insert));
                        ignored += buffered;
                        buffered = 0;

                        if (onError == ON_ERROR_ABORT)
                            break execution;
                    }

                }
                finally {
                    if (listener != null)
                        listener.row(result);
                }
                // rows:
            }

            // Execute remaining batch
            if (buffered != 0) {
                try {
                    if (bind != null)
                        bind.execute();
                    if (insert != null)
                        insert.execute();

                    stored += buffered;
                    executed++;

                    buffered = 0;
                }
                catch (DataAccessException e) {
                    errors.add(new LoaderErrorImpl(e, row, processed - 1, insert));
                    ignored += buffered;
                    buffered = 0;
                }

                if (onError == ON_ERROR_ABORT)
                    break execution;
            }

            // execution:
        }

        // Rollback on errors in COMMIT_ALL mode
        try {
            if (commit == COMMIT_ALL) {
                if (!errors.isEmpty()) {
                    stored = 0;
                    rollback();
                }
                else {
                    commit();
                }
            }

            // Commit remaining elements in COMMIT_AFTER mode
            else if (commit == COMMIT_AFTER) {
                commit();
            }
        }
        catch (DataAccessException e) {
            errors.add(new LoaderErrorImpl(e, null, processed - 1, null));
        }
    }

    private void commit() throws SQLException {
        Connection connection = configuration.connectionProvider().acquire();

        try {
            connection.commit();
        }
        finally {
            configuration.connectionProvider().release(connection);
        }
    }

    private void rollback() throws SQLException {
        Connection connection = configuration.connectionProvider().acquire();

        try {
            connection.rollback();
        }
        finally {
            configuration.connectionProvider().release(connection);
        }
    }

    /**
     * Type-safety...
     */
    private <T> void addValue0(InsertQuery<R> insert, Field<T> field, Object row) {
        insert.addValue(field, field.getDataType().convert(row));
    }

    /**
     * Type-safety...
     */
    private <T> void addValueForUpdate0(InsertQuery<R> insert, Field<T> field, Object row) {
        insert.addValueForUpdate(field, field.getDataType().convert(row));
    }

    /**
     * Get a type-safe condition
     */
    private <T> Condition getCondition(Field<T> field, Object string) {
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
    public final int executed() {
        return executed;
    }

    @Override
    public final int ignored() {
        return ignored;
    }

    @Override
    public final int stored() {
        return stored;
    }

    @Override
    public final LoaderContext result() {
        return result;
    }

    private class DefaultLoaderContext implements LoaderContext {
        @Override
        public final List<LoaderError> errors() {
            return errors;
        }

        @Override
        public final int processed() {
            return processed;
        }

        @Override
        public final int executed() {
            return executed;
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

    /**
     * An "input delay" type.
     * <p>
     * [#4593] To make sure we do not spill file handles due to improper
     * resource shutdown (e.g. when a loader is created but never executed),
     * this type helps delaying creating resources from input until the input is
     * really needed.
     */
    private class InputDelay {

        // Either, we already have an external Reader resource, in case of which
        // client code is responsible for resource management...
        BufferedReader reader;

        // ... or we create the resource explicitly as late as possible
        File           file;
        String         charsetName;
        Charset        cs;
        CharsetDecoder dec;

        BufferedReader reader() throws IOException {
            if (reader != null)
                return reader;

            if (file != null) {
                try {
                    if (charsetName != null)
                        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
                    else if (cs != null)
                        return new BufferedReader(new InputStreamReader(new FileInputStream(file), cs));
                    else if (dec != null)
                        return new BufferedReader(new InputStreamReader(new FileInputStream(file), dec));
                    else
                        return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                }
                catch (Exception e) {
                    throw new IOException(e);
                }
            }

            return null;
        }
    }
}
