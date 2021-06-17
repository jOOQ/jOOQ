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
 *
 *
 *
 */
package org.jooq.impl;

import static java.lang.Boolean.FALSE;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.combine;
import static org.jooq.tools.jdbc.JDBCUtils.safeClose;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.xml.bind.DatatypeConverter;

import org.jooq.BatchBindStep;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
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
import org.jooq.LoaderOptionsStep;
import org.jooq.LoaderRowListener;
import org.jooq.LoaderRowsStep;
import org.jooq.LoaderXMLStep;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Source;
import org.jooq.Table;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.LoaderConfigurationException;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.tools.csv.CSVParser;
import org.jooq.tools.csv.CSVReader;
import org.jooq.tools.jdbc.DefaultPreparedStatement;

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

    private static final JooqLogger      log                              = JooqLogger.getLogger(LoaderImpl.class);
    private static final Set<SQLDialect> NO_SUPPORT_ROWCOUNT_ON_DUPLICATE = SQLDialect.supportedBy(MARIADB, MYSQL);

    // Configuration constants
    // -----------------------
    private static final int             ON_DUPLICATE_KEY_ERROR           = 0;
    private static final int             ON_DUPLICATE_KEY_IGNORE          = 1;
    private static final int             ON_DUPLICATE_KEY_UPDATE          = 2;

    private static final int             ON_ERROR_ABORT                   = 0;
    private static final int             ON_ERROR_IGNORE                  = 1;

    private static final int             COMMIT_NONE                      = 0;
    private static final int             COMMIT_AFTER                     = 1;
    private static final int             COMMIT_ALL                       = 2;

    private static final int             BATCH_NONE                       = 0;
    private static final int             BATCH_AFTER                      = 1;
    private static final int             BATCH_ALL                        = 2;

    private static final int             BULK_NONE                        = 0;
    private static final int             BULK_AFTER                       = 1;
    private static final int             BULK_ALL                         = 2;

    private static final int             CONTENT_CSV                      = 0;
    private static final int             CONTENT_XML                      = 1;
    private static final int             CONTENT_JSON                     = 2;
    private static final int             CONTENT_ARRAYS                   = 3;

    // Configuration data
    // ------------------
    private final Configuration          configuration;
    private final Table<R>               table;
    private int                          onDuplicate                      = ON_DUPLICATE_KEY_ERROR;
    private int                          onError                          = ON_ERROR_ABORT;
    private int                          commit                           = COMMIT_NONE;
    private int                          commitAfter                      = 1;
    private int                          batch                            = BATCH_NONE;
    private int                          batchAfter                       = 1;
    private int                          bulk                             = BULK_NONE;
    private int                          bulkAfter                        = 1;
    private int                          content                          = CONTENT_CSV;
    private Source                       input;
    private Iterator<? extends Object[]> arrays;

    // CSV configuration data
    // ----------------------
    private int                          ignoreRows                       = 1;
    private char                         quote                            = CSVParser.DEFAULT_QUOTE_CHARACTER;
    private char                         separator                        = CSVParser.DEFAULT_SEPARATOR;
    private String                       nullString                       = null;
    private Field<?>[]                   source;
    private Field<?>[]                   fields;
    private LoaderFieldMapper            fieldMapper;
    private boolean                      fieldsCorresponding;
    private BitSet                       primaryKey;

    // Result data
    // -----------
    private LoaderRowListener            onRowStart;
    private LoaderRowListener            onRowEnd;
    private final LoaderContext          rowCtx                           = new DefaultLoaderContext();
    private int                          ignored;
    private int                          processed;
    private int                          stored;
    private int                          executed;
    private int                          unexecuted;
    private int                          uncommitted;
    private final List<LoaderError>      errors;

    LoaderImpl(Configuration configuration, Table<R> table) {
        this.configuration = configuration;
        this.table = table;
        this.errors = new ArrayList<>();
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
        if (table.getPrimaryKey() == null)
            throw new IllegalStateException("ON DUPLICATE KEY UPDATE only works on tables with explicit primary keys. Table is not updatable : " + table);

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
        return loadArrays(new MappingIterator<Record, Object[]>(records, value -> {
            if (value == null)
                return null;

            if (source == null)
                source = value.fields();

            return value.intoArray();
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
        return loadCSV(Source.of(file));
    }

    @Override
    public final LoaderImpl<R> loadCSV(File file, String charsetName) {
        return loadCSV(Source.of(file, charsetName));
    }

    @Override
    public final LoaderImpl<R> loadCSV(File file, Charset cs) {
        return loadCSV(Source.of(file, cs));
    }

    @Override
    public final LoaderImpl<R> loadCSV(File file, CharsetDecoder dec) {
        return loadCSV(Source.of(file, dec));
    }

    @Override
    public final LoaderImpl<R> loadCSV(String csv) {
        return loadCSV(Source.of(csv));
    }

    @Override
    public final LoaderImpl<R> loadCSV(InputStream stream) {
        return loadCSV(Source.of(stream));
    }

    @Override
    public final LoaderImpl<R> loadCSV(InputStream stream, String charsetName) {
        return loadCSV(Source.of(stream, charsetName));
    }

    @Override
    public final LoaderImpl<R> loadCSV(InputStream stream, Charset cs) {
        return loadCSV(Source.of(stream, cs));
    }

    @Override
    public final LoaderImpl<R> loadCSV(InputStream stream, CharsetDecoder dec) {
        return loadCSV(Source.of(stream, dec));
    }

    @Override
    public final LoaderImpl<R> loadCSV(Reader reader) {
        return loadCSV(Source.of(reader));
    }

    @Override
    public final LoaderImpl<R> loadCSV(Source s) {
        content = CONTENT_CSV;
        input = s;
        return this;
    }

    @Override
    public final LoaderImpl<R> loadXML(File file) {
        return loadXML(Source.of(file));
    }

    @Override
    public final LoaderImpl<R> loadXML(File file, String charsetName) {
        return loadXML(Source.of(file, charsetName));
    }

    @Override
    public final LoaderImpl<R> loadXML(File file, Charset cs) {
        return loadXML(Source.of(file, cs));
    }

    @Override
    public final LoaderImpl<R> loadXML(File file, CharsetDecoder dec) {
        return loadXML(Source.of(file, dec));
    }

    @Override
    public final LoaderImpl<R> loadXML(String xml) {
        return loadXML(Source.of(xml));
    }

    @Override
    public final LoaderImpl<R> loadXML(InputStream stream) {
        return loadXML(Source.of(stream));
    }

    @Override
    public final LoaderImpl<R> loadXML(InputStream stream, String charsetName) {
        return loadXML(Source.of(stream, charsetName));
    }

    @Override
    public final LoaderImpl<R> loadXML(InputStream stream, Charset cs) {
        return loadXML(Source.of(stream, cs));
    }

    @Override
    public final LoaderImpl<R> loadXML(InputStream stream, CharsetDecoder dec) {
        return loadXML(Source.of(stream, dec));
    }

    @Override
    public final LoaderImpl<R> loadXML(Reader reader) {
        return loadXML(Source.of(reader));
    }

    @Override
    public final LoaderImpl<R> loadXML(InputSource s) {
        content = CONTENT_XML;
        throw new UnsupportedOperationException("This is not yet implemented");
    }

    @Override
    public final LoaderImpl<R> loadXML(Source s) {
        content = CONTENT_XML;
        input = s;
        throw new UnsupportedOperationException("This is not yet implemented");
    }

    @Override
    public final LoaderImpl<R> loadJSON(File file) {
        return loadJSON(Source.of(file));
    }

    @Override
    public final LoaderImpl<R> loadJSON(File file, String charsetName) {
        return loadJSON(Source.of(file, charsetName));
    }

    @Override
    public final LoaderImpl<R> loadJSON(File file, Charset cs) {
        return loadJSON(Source.of(file, cs));
    }

    @Override
    public final LoaderImpl<R> loadJSON(File file, CharsetDecoder dec) {
        return loadJSON(Source.of(file, dec));
    }

    @Override
    public final LoaderImpl<R> loadJSON(String json) {
        return loadJSON(Source.of(json));
    }

    @Override
    public final LoaderImpl<R> loadJSON(InputStream stream) {
        return loadJSON(Source.of(stream));
    }

    @Override
    public final LoaderImpl<R> loadJSON(InputStream stream, String charsetName) {
        return loadJSON(Source.of(stream, charsetName));
    }

    @Override
    public final LoaderImpl<R> loadJSON(InputStream stream, Charset cs) {
        return loadJSON(Source.of(stream, cs));
    }

    @Override
    public final LoaderImpl<R> loadJSON(InputStream stream, CharsetDecoder dec) {
        return loadJSON(Source.of(stream, dec));
    }

    @Override
    public final LoaderImpl<R> loadJSON(Reader reader) {
        return loadJSON(Source.of(reader));
    }

    @Override
    public final LoaderImpl<R> loadJSON(Source s) {
        content = CONTENT_JSON;
        input = s;
        return this;
    }

    // -------------------------------------------------------------------------
    // CSV configuration
    // -------------------------------------------------------------------------

    @Override
    public final LoaderImpl<R> fields(Field<?>... f) {
        this.fields = f;
        this.primaryKey = new BitSet(f.length);

        if (table.getPrimaryKey() != null)
            for (int i = 0; i < fields.length; i++)
                if (fields[i] != null && table.getPrimaryKey().getFields().contains(fields[i]))
                    primaryKey.set(i);

        return this;
    }

    @Override
    public final LoaderImpl<R> fields(Collection<? extends Field<?>> f) {
        return fields(f.toArray(EMPTY_FIELD));
    }

    @Override
    public final LoaderImpl<R> fields(LoaderFieldMapper mapper) {
        fieldMapper = mapper;
        return this;
    }

    @Override
    @Deprecated
    public LoaderImpl<R> fieldsFromSource() {
        return fieldsCorresponding();
    }

    @Override
    public LoaderImpl<R> fieldsCorresponding() {
        fieldsCorresponding = true;
        return this;
    }

    private final void fields0(Object[] row) {
        Field<?>[] f = new Field[row.length];

        // [#5145] When loading arrays, or when CSV headers are ignored,
        // the source is still null at this stage.
        if (source == null)
            if (fieldsCorresponding)
                throw new LoaderConfigurationException("Using fieldsCorresponding() requires field names to be available in source.");
            else
                source = Tools.fields(row.length);

        if (fieldMapper != null)
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

        else if (fieldsCorresponding)
            for (int i = 0; i < row.length; i++) {
                f[i] = table.field(source[i]);
                if (f[i] == null)
                    log.info("No column in target table " + table + " found for input field " + source[i]);
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
        return onRowEnd(l);
    }

    @Override
    public final LoaderImpl<R> onRowStart(LoaderRowListener l) {
        onRowStart = l;
        return this;
    }

    @Override
    public final LoaderImpl<R> onRowEnd(LoaderRowListener l) {
        onRowEnd = l;
        return this;
    }

    // -------------------------------------------------------------------------
    // Execution
    // -------------------------------------------------------------------------

    @Override
    public final LoaderImpl<R> execute() throws IOException {
        checkFlags();

        if (content == CONTENT_CSV)
            executeCSV();
        else if (content == CONTENT_XML)
            throw new UnsupportedOperationException();
        else if (content == CONTENT_JSON)
            executeJSON();
        else if (content == CONTENT_ARRAYS)
            executeRows();
        else
            throw new IllegalStateException();

        return this;
    }

    private final void checkFlags() {
        if (bulk != BULK_NONE && onDuplicate != ON_DUPLICATE_KEY_ERROR)
            throw new LoaderConfigurationException("Cannot apply bulk loading with onDuplicateKey flags. Turn off either flag.");
    }

    private final void executeJSON() {
        Reader reader = null;

        try {
            reader = input.reader();
            Result<Record> r = new JSONReader<>(configuration.dsl(), null, null).read(reader);
            source = r.fields();

            // The current json format is not designed for streaming. Thats why
            // all records are loaded at once.
            List<Object[]> allRecords = Arrays.asList(r.intoArrays());
            executeSQL(allRecords.iterator());
        }
        finally {
            safeClose(reader);
        }
    }

    private final void executeCSV() {
        CSVReader reader = null;

        try {
            if (ignoreRows == 1) {
                reader = new CSVReader(input.reader(), separator, quote, 0);
                source = Tools.fieldsByName(reader.next());
            }
            else {
                reader = new CSVReader(input.reader(), separator, quote, ignoreRows);
            }

            executeSQL(reader);
        }
        finally {
            safeClose(reader);
        }
    }

    private final void executeRows() {
        executeSQL(arrays);
    }

    private static final class CachedPSListener extends DefaultExecuteListener implements AutoCloseable {

        final Map<String, CachedPS> map              = new HashMap<>();

        @Override
        public void prepareStart(ExecuteContext ctx) {
            CachedPS ps = map.get(ctx.sql());

            if (ps != null)
                ctx.statement(ps);
        }

        @Override
        public void prepareEnd(ExecuteContext ctx) {
            if (!(ctx.statement() instanceof CachedPS)) {
                CachedPS ps = new CachedPS(ctx.statement());
                map.put(ctx.sql(), ps);
                ctx.statement(ps);
            }
        }

        @Override
        public void close() throws SQLException {
            for (CachedPS ps : map.values())
                safeClose(ps.getDelegate());
        }
    }

    private static class CachedPS extends DefaultPreparedStatement {
        CachedPS(PreparedStatement delegate) {
            super(delegate);
        }

        @Override
        public void close() throws SQLException {}
    }

    private final void executeSQL(final Iterator<? extends Object[]> iterator) {
        configuration.dsl().connection(connection -> {
            Configuration c = configuration.derive(new DefaultConnectionProvider(connection));

            if (FALSE.equals(c.settings().isCachePreparedStatementInLoader())) {
                executeSQL(iterator, c.dsl());
            }

            else {
                try (CachedPSListener cache = new CachedPSListener()) {
                    executeSQL(iterator, c
                        .derive(combine(new DefaultExecuteListenerProvider(cache), c.executeListenerProviders()))
                        .dsl()
                    );
                }
            }
        });
    }

    private final void executeSQL(Iterator<? extends Object[]> iterator, DSLContext ctx) {
        Object[] row = null;
        BatchBindStep bind = null;
        InsertQuery<R> insert = null;
        boolean newRecord = false;

        execution: {
            rows: while (iterator.hasNext() && ((row = iterator.next()) != null)) {
                try {

                    // [#5858] Work with non String[] types from here on (e.g. after CSV import)
                    if (row.getClass() != Object[].class)
                        row = Arrays.copyOf(row, row.length, Object[].class);

                    // [#5145][#8755] Lazy initialisation of fields from the first row
                    // in case fields(LoaderFieldMapper) or fieldsCorresponding() was used
                    if (fields == null)
                        fields0(row);

                    // [#1627] [#5858] Handle NULL values and base64 encodings
                    // [#2741]         TODO: This logic will be externalised in new SPI
                    // [#8829]         JSON binary data has already been decoded at this point
                    for (int i = 0; i < row.length; i++)
                        if (StringUtils.equals(nullString, row[i]))
                            row[i] = null;
                        else if (i < fields.length && fields[i] != null)
                            if (fields[i].getType() == byte[].class && row[i] instanceof String)
                                row[i] = DatatypeConverter.parseBase64Binary((String) row[i]);

                    // [#10583] Pad row to the fields length
                    if (row.length < fields.length)
                        row = Arrays.copyOf(row, fields.length);

                    rowCtx.row(row);
                    if (onRowStart != null) {
                        onRowStart.row(rowCtx);
                        row = rowCtx.row();
                    }

                    // TODO: In batch mode, we can probably optimise this by not creating
                    // new statements every time, just to convert bind values to their
                    // appropriate target types. But beware of SQL dialects that tend to
                    // need very explicit casting of bind values (e.g. Firebird)
                    processed++;
                    unexecuted++;
                    uncommitted++;

                    if (insert == null)
                        insert = ctx.insertQuery(table);

                    if (newRecord) {
                        newRecord = false;
                        insert.newRecord();
                    }

                    for (int i = 0; i < row.length; i++)
                        if (i < fields.length && fields[i] != null)
                            addValue0(insert, fields[i], row[i]);

                    // TODO: This is only supported by some dialects. Let other
                    // dialects execute a SELECT and then either an INSERT or UPDATE
                    if (onDuplicate == ON_DUPLICATE_KEY_UPDATE) {
                        insert.onDuplicateKeyUpdate(true);

                        for (int i = 0; i < row.length; i++)
                            if (i < fields.length && fields[i] != null && !primaryKey.get(i))
                                addValueForUpdate0(insert, fields[i], row[i]);
                    }

                    // [#5200]  When the primary key is not supplied in the data,
                    //          we'll assume it uses an identity, and there will never be duplicates
                    // [#10358] TODO: The above should be moved inside InsertQueryImpl
                    // [#7253]  Use native onDuplicateKeyIgnore() support
                    else if (onDuplicate == ON_DUPLICATE_KEY_IGNORE && primaryKey.cardinality() > 0) {
                        insert.onDuplicateKeyIgnore(true);
                    }

                    // Don't do anything. Let the execution fail
                    else if (onDuplicate == ON_DUPLICATE_KEY_ERROR) {}

                    try {
                        if (bulk != BULK_NONE) {
                            if (bulk == BULK_ALL || processed % bulkAfter != 0) {
                                newRecord = true;
                                continue rows;
                            }
                        }

                        if (batch != BATCH_NONE) {
                            if (bind == null)
                                bind = ctx.batch(insert);

                            bind.bind(insert.getBindValues().toArray());
                            insert = null;

                            if (batch == BATCH_ALL || processed % (bulkAfter * batchAfter) != 0)
                                continue rows;
                        }

                        int[] rowcounts = { 0 };
                        int totalRowCounts = 0;

                        if (bind != null)
                            rowcounts = bind.execute();
                        else if (insert != null)
                            rowcounts = new int[] { insert.execute() };

                        // [#10358] The MySQL dialect category doesn't return rowcounts
                        //          in INSERT .. ON DUPLICATE KEY UPDATE statements, but
                        //          1 = INSERT, 2 = UPDATE, instead
                        if (onDuplicate == ON_DUPLICATE_KEY_UPDATE && NO_SUPPORT_ROWCOUNT_ON_DUPLICATE.contains(ctx.dialect()))
                            totalRowCounts = unexecuted;
                        else
                            for (int rowCount : rowcounts)
                                totalRowCounts += rowCount;

                        stored += totalRowCounts;
                        ignored += unexecuted - totalRowCounts;
                        executed++;

                        unexecuted = 0;
                        bind = null;
                        insert = null;

                        if (commit == COMMIT_AFTER)
                            if ((processed % (bulkAfter * batchAfter) == 0) && ((processed / (bulkAfter * batchAfter)) % commitAfter == 0))
                                commit();
                    }
                    catch (DataAccessException e) {
                        errors.add(new LoaderErrorImpl(e, row, processed - 1, insert));
                        ignored += unexecuted;
                        unexecuted = 0;

                        if (onError == ON_ERROR_ABORT)
                            break execution;
                    }

                }
                finally {
                    if (onRowEnd != null)
                        onRowEnd.row(rowCtx);
                }
                // rows:
            }

            // Execute remaining batch
            if (unexecuted != 0) {
                try {
                    if (bind != null)
                        bind.execute();
                    if (insert != null)
                        insert.execute();

                    stored += unexecuted;
                    executed++;

                    unexecuted = 0;
                }
                catch (DataAccessException e) {
                    errors.add(new LoaderErrorImpl(e, row, processed - 1, insert));
                    ignored += unexecuted;
                    unexecuted = 0;
                }
            }

            // Commit remaining elements in COMMIT_AFTER mode
            if (commit == COMMIT_AFTER && uncommitted != 0)
                commit();

            if (onError == ON_ERROR_ABORT)
                break execution;

            // execution:
        }

        // Rollback on errors in COMMIT_ALL mode
        try {
            if (commit == COMMIT_ALL) {
                if (!errors.isEmpty()) {
                    stored = 0;
                    rollback();
                }
                else
                    commit();
            }
        }
        catch (DataAccessException e) {
            errors.add(new LoaderErrorImpl(e, null, processed - 1, null));
        }
    }

    private final void commit() {
        configuration.dsl().connection(Connection::commit);
        uncommitted = 0;
    }

    private final void rollback() {
        configuration.dsl().connection(Connection::rollback);
    }

    /**
     * Type-safety...
     */
    private final <T> void addValue0(InsertQuery<R> insert, Field<T> field, Object row) {
        insert.addValue(field, field.getDataType().convert(row));
    }

    /**
     * Type-safety...
     */
    private final <T> void addValueForUpdate0(InsertQuery<R> insert, Field<T> field, Object row) {
        insert.addValueForUpdate(field, field.getDataType().convert(row));
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
        return rowCtx;
    }

    private class DefaultLoaderContext implements LoaderContext {
        Object[] row;

        @Override
        public final LoaderContext row(Object[] r) {
            this.row = r;
            return this;
        }

        @Override
        public final Object[] row() {
            return row;
        }

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
}
