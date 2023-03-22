/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import static java.lang.Boolean.TRUE;
import static org.jooq.conf.SettingsTools.renderLocale;
import static org.jooq.impl.Tools.EMPTY_INT;
import static org.jooq.impl.Tools.EMPTY_QUERY;
import static org.jooq.impl.Tools.EMPTY_STRING;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.Constants;
import org.jooq.ConverterContext;
import org.jooq.DDLQuery;
import org.jooq.DSLContext;
import org.jooq.Delete;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteType;
import org.jooq.Insert;
import org.jooq.Merge;
// ...
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Routine;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Update;
import org.jooq.conf.DiagnosticsConnection;
import org.jooq.conf.Settings;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;

import org.jetbrains.annotations.NotNull;

// ...

/**
 * A default implementation for the {@link ExecuteContext}.
 *
 * @author Lukas Eder
 */
class DefaultExecuteContext implements ExecuteContext {

    private static final JooqLogger                       log               = JooqLogger.getLogger(DefaultExecuteContext.class);
    private static final JooqLogger                       logVersionSupport = JooqLogger.getLogger(DefaultExecuteContext.class, "logVersionSupport", 1);

    // Persistent attributes (repeatable)
    private final ConverterContext                        converterContext;
    private final Instant                                 creationTime;
    private final Configuration                           originalConfiguration;
    private final Configuration                           derivedConfiguration;
    private final Map<Object, Object>                     data;






    private Query                                         query;
    private final Routine<?>                              routine;
    private String                                        sql;

    private final BatchMode                               batchMode;
    private Query[]                                       batchQueries;
    private String[]                                      batchSQL;
    private int[]                                         batchRows;

    ConnectionProvider                                    connectionProvider;
    private Connection                                    connection;
    private Connection                                    wrappedConnection;
    private PreparedStatement                             statement;
    private int                                           statementExecutionCount;
    private ResultSet                                     resultSet;
    private Record                                        record;
    private Result<?>                                     result;
    int                                                   recordLevel;
    int                                                   resultLevel;
    private int                                           rows      = -1;
    private RuntimeException                              exception;
    private SQLException                                  sqlException;
    private SQLWarning                                    sqlWarning;
    private String[]                                      serverOutput;

    // ------------------------------------------------------------------------
    // XXX: Static utility methods for handling blob / clob lifecycle
    // ------------------------------------------------------------------------

    private static final ThreadLocal<List<AutoCloseable>> RESOURCES = new ThreadLocal<>();

    /**
     * Clean up blobs, clobs and the local configuration.
     * <p>
     * <h5>BLOBS and CLOBS</h5>
     * <p>
     * [#1326] This is necessary in those dialects that have long-lived
     * temporary lob objects, which can cause memory leaks in certain contexts,
     * where the lobs' underlying session / connection is long-lived as well.
     * Specifically, Oracle and ojdbc have some trouble when streaming temporary
     * lobs to UDTs:
     * <ol>
     * <li>The lob cannot have a call-scoped life time with UDTs</li>
     * <li>Freeing the lob after binding will cause an ORA-22275</li>
     * <li>Not freeing the lob after execution will cause an
     * {@link OutOfMemoryError}</li>
     * </ol>
     * <p>
     * <h5>Local configuration</h5>
     * <p>
     * [#1544] There exist some corner-cases regarding the {@link SQLOutput}
     * API, used for UDT serialisation / deserialisation, which have no elegant
     * solutions of obtaining a {@link Configuration} and thus a JDBC
     * {@link Connection} object short of:
     * <ul>
     * <li>Making assumptions about the JDBC driver and using proprietary API,
     * e.g. that of ojdbc</li>
     * <li>Dealing with this problem globally by using such a local
     * configuration</li>
     * </ul>
     *
     * @see <a
     *      href="http://stackoverflow.com/q/11439543/521799">http://stackoverflow.com/q/11439543/521799</a>
     */
    static final void clean() {
        List<AutoCloseable> resources = RESOURCES.get();

        if (resources != null) {
            for (AutoCloseable resource : resources)
                JDBCUtils.safeClose(resource);

            RESOURCES.remove();
        }

        LOCAL_CONNECTION.remove();
    }

    /**
     * Register a blob for later cleanup with {@link #clean()}
     */
    static final void register(Blob blob) {
        register((AutoCloseable) blob::free);
    }

    /**
     * Register a clob for later cleanup with {@link #clean()}
     */
    static final void register(Clob clob) {
        register((AutoCloseable) clob::free);
    }

    /**
     * Register an xml for later cleanup with {@link #clean()}
     */
    static final void register(SQLXML xml) {
        register((AutoCloseable) xml::free);
    }

    /**
     * Register an array for later cleanup with {@link #clean()}
     */
    static final void register(Array array) {
        register((AutoCloseable) array::free);
    }

    /**
     * Register a closeable for later cleanup with {@link #clean()}
     */
    static final void register(AutoCloseable closeable) {
        List<AutoCloseable> list = RESOURCES.get();

        if (list == null) {
            list = new ArrayList<>();
            RESOURCES.set(list);
        }

        list.add(closeable);
    }

    // ------------------------------------------------------------------------
    // XXX: Static utility methods for handling Configuration lifecycle
    // ------------------------------------------------------------------------

    private static final ThreadLocal<ExecuteContext> LOCAL_EXECUTE_CONTEXT = new ThreadLocal<>();

    /**
     * Get the registered {@link ExecuteContext}.
     * <p>
     * It can be safely assumed that such a configuration is available once the
     * {@link ExecuteContext} has been established, until the statement is
     * closed.
     */
    static final ExecuteContext localExecuteContext() {
        return LOCAL_EXECUTE_CONTEXT.get();
    }

    /**
     * Run a runnable with a new {@link #localExecuteContext()}.
     */
    static final <E extends Exception> void localExecuteContext(ExecuteContext ctx, ThrowingRunnable<E> runnable) throws E {
        localExecuteContext(ctx, () -> { runnable.run(); return null; });
    }

    /**
     * Run a supplier with a new {@link #localExecuteContext()}.
     */
    static final <T, E extends Exception> T localExecuteContext(ExecuteContext ctx, ThrowingSupplier<T, E> supplier) throws E {
        ExecuteContext old = localExecuteContext();

        try {
            LOCAL_EXECUTE_CONTEXT.set(ctx);
            return supplier.get();
        }
        finally {
            LOCAL_EXECUTE_CONTEXT.set(old);
        }
    }

    // ------------------------------------------------------------------------
    // XXX: Static utility methods for handling Configuration lifecycle
    // ------------------------------------------------------------------------

    private static final ThreadLocal<Connection> LOCAL_CONNECTION = new ThreadLocal<>();

    /**
     * Get the registered connection.
     * <p>
     * It can be safely assumed that such a connection is available once the
     * {@link ExecuteContext} has been established, until the statement is
     * closed.
     */
    static final Connection localConnection() {
        return LOCAL_CONNECTION.get();
    }

    /**
     * Get the registered connection's "target connection" through
     * {@link Configuration#unwrapperProvider()} if applicable.
     * <p>
     * It can be safely assumed that such a connection is available once the
     * {@link ExecuteContext} has been established, until the statement is
     * closed.
     */
    static final Connection localTargetConnection(Scope scope) {
        Connection result = localConnection();
















        log.info("Could not unwrap native Connection type. Consider implementing an org.jooq.UnwrapperProvider");
        return result;
    }

    // ------------------------------------------------------------------------
    // XXX: Constructors
    // ------------------------------------------------------------------------

    DefaultExecuteContext(Configuration configuration) {
        this(configuration, BatchMode.NONE, null, null, null);
    }

    DefaultExecuteContext(Configuration configuration, BatchMode batchMode, Query[] batchQueries) {
        this(configuration, batchMode, null, batchQueries, null);
    }

    DefaultExecuteContext(Configuration configuration, Query query) {
        this(configuration, BatchMode.NONE, query, null, null);
    }

    DefaultExecuteContext(Configuration configuration, Routine<?> routine) {
        this(configuration, BatchMode.NONE, null, null, routine);
    }

    private DefaultExecuteContext(Configuration configuration, BatchMode batchMode, Query query, Query[] batchQueries, Routine<?> routine) {

        // [#4277] The ExecuteContext's Configuration will always return the same Connection,
        //         e.g. when running statements from sub-ExecuteContexts
        // [#7569] The original configuration is attached to Record and Result instances
        this.creationTime = configuration.clock().instant();
        this.connectionProvider = configuration.connectionProvider();
        this.originalConfiguration = configuration;
        this.derivedConfiguration = configuration.derive(new ExecuteContextConnectionProvider());
        this.data = new DataMap();
        this.batchMode = batchMode;
        this.query = query;




        this.routine = routine;
        this.converterContext = new DefaultConverterContext(derivedConfiguration, data);

        batchQueries0(batchQueries);
        clean();
    }

    @Override
    public final ConverterContext converterContext() {
        return converterContext;
    }

    @Override
    public final Instant creationTime() {
        return creationTime;
    }

    @Override
    public final Map<Object, Object> data() {
        return data;
    }

    @Override
    public final Object data(Object key) {
        return data.get(key);
    }

    @Override
    public final Object data(Object key, Object value) {
        return data.put(key, value);
    }

    @Override
    public final ExecuteType type() {

        // This can only be a routine
        if (routine != null) {
            return ExecuteType.ROUTINE;
        }

        // This can only be a BatchSingle or BatchMultiple execution
        else if (batchMode != BatchMode.NONE) {
            return ExecuteType.BATCH;
        }

        // Any other type of query
        else if (query != null) {
            if (query instanceof ResultQuery) {
                return ExecuteType.READ;
            }
            else if (query instanceof Insert
                  || query instanceof Update
                  || query instanceof Delete
                  || query instanceof Merge) {

                return ExecuteType.WRITE;
            }
            else if (query instanceof DDLQuery) {
                return ExecuteType.DDL;
            }

            // Analyse SQL in plain SQL queries:
            else {
                String s = query.getSQL().toLowerCase(renderLocale(configuration().settings()));

                // TODO: Use a simple lexer to parse SQL here. Potentially, the
                // SQL Console's SQL formatter could be used...?
                if (s.matches("^(with\\b.*?\\bselect|select|explain)\\b.*?"))
                    return ExecuteType.READ;

                // These are sample DML statements. There may be many more
                else if (s.matches("^(insert|update|delete|merge|replace|upsert|lock)\\b.*?"))
                    return ExecuteType.WRITE;

                // These are only sample DDL statements. There may be many more
                else if (s.matches("^(create|alter|drop|truncate|grant|revoke|analyze|comment|flashback|enable|disable)\\b.*?"))
                    return ExecuteType.DDL;

                // JDBC escape syntax for routines
                else if (s.matches("^\\s*\\{\\s*(\\?\\s*=\\s*)call.*?"))
                    return ExecuteType.ROUTINE;

                // Vendor-specific calling of routines / procedural blocks
                else if (s.matches("^(call|begin|declare)\\b.*?"))
                    return ExecuteType.ROUTINE;
            }
        }

        // Fetching JDBC result sets, e.g. with SQL.fetch(ResultSet)
        else if (resultSet != null) {
            return ExecuteType.READ;
        }

        // No query available
        return ExecuteType.OTHER;
    }

    @Override
    public final Query query() {
        return query;
    }




















    @Override
    public final BatchMode batchMode() {
        return batchMode;
    }

    @Override
    public final Query[] batchQueries() {
        return batchMode != BatchMode.NONE
             ? batchQueries
             : query() != null
             ? new Query[] { query() }
             : EMPTY_QUERY;
    }




















    private final void batchQueries0(Query... newQueries) {
        if (newQueries != null) {
            this.batchQueries = newQueries.clone();
            this.batchSQL = new String[newQueries.length];
            this.batchRows = new int[newQueries.length];

            Arrays.fill(this.batchRows, -1);
        }
        else {
            this.batchQueries = null;
            this.batchSQL = null;
            this.batchRows = null;
        }
    }

    @Override
    public final Routine<?> routine() {
        return routine;
    }

    @Override
    public final void sql(String s) {
        this.sql = s;

        // If this isn't a BatchMultiple query
        if (batchSQL != null && batchSQL.length == 1)
            batchSQL[0] = s;
    }

    @Override
    public final String sql() {
        return sql;
    }

    @Override
    public final String[] batchSQL() {
        return batchMode != BatchMode.NONE
             ? batchSQL
             : routine != null || query() != null
             ? new String[] { sql }
             : EMPTY_STRING;
    }

    @Override
    public final void statement(PreparedStatement s) {
        this.statement = s;
    }

    @Override
    public final PreparedStatement statement() {
        return statement;
    }

    @Override
    public final int statementExecutionCount() {
        return statementExecutionCount;
    }

    @Override
    public final void resultSet(ResultSet rs) {
        this.resultSet = rs;
    }

    @Override
    public final ResultSet resultSet() {
        return resultSet;
    }

    @Override
    public final Configuration configuration() {
        return derivedConfiguration;
    }

    // [#4277] [#7569] The original configuration that was used to create the
    //                 derived configuration in this ExecuteContext
    final Configuration originalConfiguration() {
        return originalConfiguration;
    }

    @Override
    public final DSLContext dsl() {
        return configuration().dsl();
    }

    @Override
    public final Settings settings() {
        return Tools.settings(configuration());
    }

    @Override
    public final SQLDialect dialect() {
        return Tools.configuration(configuration()).dialect();
    }

    @Override
    public final SQLDialect family() {
        return dialect().family();
    }

    @Override
    public final void connectionProvider(ConnectionProvider provider) {
        this.connectionProvider = provider;
    }

    @Override
    public final Connection connection() {
        // All jOOQ internals are expected to get a connection through this
        // single method. It can thus be guaranteed, that every connection is
        // wrapped by a ConnectionProxy, transparently, in order to implement
        // Settings.getStatementType() correctly.
        if (wrappedConnection == null && connectionProvider != null)
            connection(connectionProvider, connectionProvider.acquire());

        return wrappedConnection;
    }

    /**
     * Initialise this {@link DefaultExecuteContext} with a pre-existing
     * {@link Connection}.
     * <p>
     * [#3191] This is needed, e.g. when using
     * {@link Query#keepStatement(boolean)}.
     */
    final void connection(ConnectionProvider provider, Connection c) {
        if (c != null) {

            // [#11355] Check configured dialect version vs. JDBC Connection server version.
            if (dialect().isVersioned() && logVersionSupport.isWarnEnabled()) {
                try {
                    int majorVersion = c.getMetaData().getDatabaseMajorVersion();
                    int minorVersion = c.getMetaData().getDatabaseMinorVersion();
                    String productVersion = c.getMetaData().getDatabaseProductVersion();

                    if (!dialect().supportsDatabaseVersion(majorVersion, minorVersion, productVersion))
                        logVersionSupport.warn("Version mismatch", "Database version is older than what dialect " + dialect() + " supports: " + productVersion + ". Consider https://www.jooq.org/download/support-matrix to see what jOOQ version and edition supports which RDBMS versions.");
                    else
                        logVersionSupport.info("Version", "Database version is supported by dialect " + dialect() + ": " + productVersion);
                }

                // [#14833] There are various reasons why the version can't be read, which we can ignore
                catch (SQLException e) {
                    logVersionSupport.info("Version", "Database version cannot be read: " + e.getMessage());
                }

                // [#14791] Could also be NumberFormatException when reading non-standard version numbers
                catch (Exception e) {
                    logVersionSupport.error("Error reading database version", e);
                }
            }

            LOCAL_CONNECTION.set(c);
            connection = c;
            wrappedConnection = wrap(provider, c);
        }
    }

    private final Connection wrap(ConnectionProvider provider, Connection c) {
        return wrap0(new SettingsEnabledConnection(new ProviderEnabledConnection(provider, c), derivedConfiguration.settings(), this));
    }

    private final Connection wrap0(Connection c) {
        if (derivedConfiguration.settings().getDiagnosticsConnection() == DiagnosticsConnection.ON)
            return new org.jooq.impl.DiagnosticsConnection(derivedConfiguration, c);
        else
            return c;
    }

    final void incrementStatementExecutionCount() {
        statementExecutionCount++;
    }

    final DefaultExecuteContext withStatementExecutionCount(int count) {
        statementExecutionCount = count;
        return this;
    }

    @Override
    public final void record(Record r) {
        this.record = r;
    }

    @Override
    public final Record record() {
        return record;
    }

    @Override
    public final int recordLevel() {
        return recordLevel;
    }

    @Override
    public final int rows() {
        return rows;
    }

    @Override
    public final void rows(int r) {
        this.rows = r;

        // If this isn't a BatchMultiple query
        if (batchRows != null && batchRows.length == 1)
            batchRows[0] = r;
    }

    @Override
    public final int[] batchRows() {
        return batchMode != BatchMode.NONE
             ? batchRows
             : routine != null || query() != null
             ? new int[] { rows }
             : EMPTY_INT;
    }

    @Override
    public final void result(Result<?> r) {
        this.result = r;
    }

    @Override
    public final Result<?> result() {
        return result;
    }

    @Override
    public final int resultLevel() {
        return resultLevel;
    }

    @Override
    public final RuntimeException exception() {
        return exception;
    }

    @Override
    public final void exception(RuntimeException e) {
        this.exception = Tools.translate(sql(), e);

        if (Boolean.TRUE.equals(settings().isDebugInfoOnStackTrace())) {

            // [#5570] Add jOOQ version and SQL Dialect info on the stack trace
            //         to help users write better bug reports.
            //         See http://stackoverflow.com/q/39712695/521799
            StackTraceElement[] oldStack = exception.getStackTrace();
            if (oldStack != null) {
                StackTraceElement[] newStack = new StackTraceElement[oldStack.length + 1];
                System.arraycopy(oldStack, 0, newStack, 1, oldStack.length);
                newStack[0] = new StackTraceElement(
                    "org.jooq_" + Constants.VERSION + "." + dialect(),
                    "debug", null, -1);
                exception.setStackTrace(newStack);
            }
        }
    }

    @Override
    public final SQLException sqlException() {
        return sqlException;
    }

    @Override
    public final void sqlException(SQLException e) {
        this.sqlException = e;
        exception(Tools.translate(sql(), e));
    }

    @Override
    public final SQLWarning sqlWarning() {
        return sqlWarning;
    }

    @Override
    public final void sqlWarning(SQLWarning e) {
        this.sqlWarning = e;
    }

    @Override
    public final String[] serverOutput() {
        return serverOutput == null ? EMPTY_STRING : serverOutput;
    }

    @Override
    public final void serverOutput(String[] output) {
        this.serverOutput = output;
    }

    final class ExecuteContextConnectionProvider implements ConnectionProvider {

        @NotNull
        @Override
        public final Connection acquire() {

            // [#4277] Connections are acquired lazily in parent ExecuteContext. A child ExecuteContext
            //         may well need a Connection earlier than the parent, in case of which acquisition is
            //         forced in the parent as well.
            if (connection == null)
                DefaultExecuteContext.this.connection();

            return wrap(this, connection);
        }

        @Override
        public final void release(Connection c) {}
    }


    final void transformQueries(ExecuteListener listener) {




        if (TRUE.equals(settings().isTransformPatterns()) && configuration().requireCommercial(() -> "SQL transformations are a commercial only feature. Please consider upgrading to the jOOQ Professional Edition or jOOQ Enterprise Edition.")) {




        }





    }
}
