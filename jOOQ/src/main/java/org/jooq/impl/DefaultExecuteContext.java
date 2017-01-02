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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.Constants;
import org.jooq.DDLQuery;
import org.jooq.Delete;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteType;
import org.jooq.Insert;
import org.jooq.Merge;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Routine;
import org.jooq.SQLDialect;
import org.jooq.Update;
import org.jooq.conf.Settings;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;
import org.jooq.tools.reflect.Reflect;
import org.jooq.tools.reflect.ReflectException;

/**
 * A default implementation for the {@link ExecuteContext}.
 *
 * @author Lukas Eder
 */
class DefaultExecuteContext implements ExecuteContext {

    private static final JooqLogger                log     = JooqLogger.getLogger(DefaultExecuteContext.class);

    // Persistent attributes (repeatable)
    private final Configuration                    configuration;
    private final Map<Object, Object>              data;
    private final Query                            query;
    private final Routine<?>                       routine;
    private String                                 sql;

    private final Query[]                          batchQueries;
    private final String[]                         batchSQL;
    private final int[]                            batchRows;

    // Transient attributes (created afresh per execution)
    private transient ConnectionProvider           connectionProvider;
    private transient Connection                   connection;
    private transient PreparedStatement            statement;
    private transient ResultSet                    resultSet;
    private transient Record                       record;
    private transient Result<?>                    result;
    private transient int                          rows    = -1;
    private transient RuntimeException             exception;
    private transient SQLException                 sqlException;
    private transient SQLWarning                   sqlWarning;

    // ------------------------------------------------------------------------
    // XXX: Static utility methods for handling blob / clob lifecycle
    // ------------------------------------------------------------------------

    private static final ThreadLocal<List<Blob>>   BLOBS   = new ThreadLocal<List<Blob>>();
    private static final ThreadLocal<List<Clob>>   CLOBS   = new ThreadLocal<List<Clob>>();
    private static final ThreadLocal<List<SQLXML>> SQLXMLS = new ThreadLocal<List<SQLXML>>();
    private static final ThreadLocal<List<Array>>  ARRAYS  = new ThreadLocal<List<Array>>();

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
        List<Blob> blobs = BLOBS.get();
        List<Clob> clobs = CLOBS.get();
        List<SQLXML> xmls = SQLXMLS.get();
        List<Array> arrays = ARRAYS.get();

        if (blobs != null) {
            for (Blob blob : blobs) {
                JDBCUtils.safeFree(blob);
            }

            BLOBS.remove();
        }

        if (clobs != null) {
            for (Clob clob : clobs) {
                JDBCUtils.safeFree(clob);
            }

            CLOBS.remove();
        }

        if (xmls != null) {
            for (SQLXML xml : xmls) {
                JDBCUtils.safeFree(xml);
            }

            SQLXMLS.remove();
        }

        if (arrays != null) {
            for (Array array : arrays) {
                JDBCUtils.safeFree(array);
            }

            SQLXMLS.remove();
        }

        LOCAL_CONFIGURATION.remove();
        LOCAL_DATA.remove();
        LOCAL_CONNECTION.remove();
    }

    /**
     * Register a blob for later cleanup with {@link #clean()}
     */
    static final void register(Blob blob) {
        BLOBS.get().add(blob);
    }

    /**
     * Register a clob for later cleanup with {@link #clean()}
     */
    static final void register(Clob clob) {
        CLOBS.get().add(clob);
    }

    /**
     * Register an xml for later cleanup with {@link #clean()}
     */
    static final void register(SQLXML xml) {
        SQLXMLS.get().add(xml);
    }

    /**
     * Register an array for later cleanup with {@link #clean()}
     */
    static final void register(Array array) {
        ARRAYS.get().add(array);
    }

    // ------------------------------------------------------------------------
    // XXX: Static utility methods for handling Configuration lifecycle
    // ------------------------------------------------------------------------

    private static final ThreadLocal<Configuration>       LOCAL_CONFIGURATION = new ThreadLocal<Configuration>();
    private static final ThreadLocal<Map<Object, Object>> LOCAL_DATA          = new ThreadLocal<Map<Object, Object>>();

    /**
     * Get the registered configuration.
     * <p>
     * It can be safely assumed that such a configuration is available once the
     * {@link ExecuteContext} has been established, until the statement is
     * closed.
     */
    static final Configuration localConfiguration() {
        return LOCAL_CONFIGURATION.get();
    }

    /**
     * Get the registered data.
     * <p>
     * It can be safely assumed that such a configuration is available once the
     * {@link ExecuteContext} has been established, until the statement is
     * closed.
     */
    static final Map<Object, Object> localData() {
        return LOCAL_DATA.get();
    }

    // ------------------------------------------------------------------------
    // XXX: Static utility methods for handling Configuration lifecycle
    // ------------------------------------------------------------------------

    private static final ThreadLocal<Connection> LOCAL_CONNECTION = new ThreadLocal<Connection>();

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
     * [#3696] We shouldn't infinitely attempt to unwrap connections.
     */
    private static int            maxUnwrappedConnections = 256;

    /**
     * Get the registered connection's "target connection" if applicable.
     * <p>
     * This will try to unwrap any native connection if it has been wrapped with
     * any of:
     * <ul>
     * <li><code>org.springframework.jdbc.datasource.ConnectionProxy</code></li>
     * <li><code>org.apache.commons.dbcp.DelegatingConnection</code></li>
     * <li>...</li>
     * </ul>
     * <p>
     * It can be safely assumed that such a connection is available once the
     * {@link ExecuteContext} has been established, until the statement is
     * closed.
     */
    static final Connection localTargetConnection() {
        Connection result = localConnection();

        unwrappingLoop:
        for (int i = 0; i < maxUnwrappedConnections; i++) {





























            // Unwrap nested Spring org.springframework.jdbc.datasource.ConnectionProxy objects
            try {
                Connection r = Reflect.on(result).call("getTargetConnection").get();
                if (result != r && r != null) {
                    result = r;
                    continue unwrappingLoop;
                }
            }
            catch (ReflectException ignore) {}

            // Unwrap nested DBCP org.apache.commons.dbcp.DelegatingConnection
            try {
                Connection r = Reflect.on(result).call("getDelegate").get();
                if (result != r && r != null) {
                    result = r;
                    continue unwrappingLoop;
                }
            }
            catch (ReflectException ignore) {}

            // No unwrapping method was found.
            break;
        }

        return result;
    }

    // ------------------------------------------------------------------------
    // XXX: Constructors
    // ------------------------------------------------------------------------

    DefaultExecuteContext(Configuration configuration) {
        this(configuration, null, null, null);
    }

    DefaultExecuteContext(Configuration configuration, Query[] batchQueries) {
        this(configuration, null, batchQueries, null);
    }

    DefaultExecuteContext(Configuration configuration, Query query) {
        this(configuration, query, new Query[] { query }, null);
    }

    DefaultExecuteContext(Configuration configuration, Routine<?> routine) {
        this(configuration, null, null, routine);
    }

    private DefaultExecuteContext(Configuration configuration, Query query, Query[] batchQueries, Routine<?> routine) {
        this.configuration = configuration;
        this.data = new DataMap();
        this.query = query;
        this.batchQueries = (batchQueries == null ? new Query[0] : batchQueries);
        this.routine = routine;

        if (this.batchQueries.length > 0) {
            this.batchSQL = new String[this.batchQueries.length];
            this.batchRows = new int[this.batchQueries.length];

            for (int i = 0; i < this.batchQueries.length; i++)
                this.batchRows[i] = -1;
        }
        else if (routine != null) {
            this.batchSQL = new String[1];
            this.batchRows = new int[] { -1 };
        }
        else {
            this.batchSQL = new String[0];
            this.batchRows = new int[0];
        }

        clean();
        BLOBS.set(new ArrayList<Blob>());
        CLOBS.set(new ArrayList<Clob>());
        SQLXMLS.set(new ArrayList<SQLXML>());
        ARRAYS.set(new ArrayList<Array>());
        LOCAL_CONFIGURATION.set(configuration);
        LOCAL_DATA.set(this.data);
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

        // This can only be a BatchSingle execution
        else if (batchQueries.length == 1 && query == null) {
            return ExecuteType.BATCH;
        }

        // This can only be a BatchMultiple execution
        else if (batchQueries.length > 1) {
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
                String s = query.getSQL().toLowerCase();

                // TODO: Use a simple lexer to parse SQL here. Potentially, the
                // SQL Console's SQL formatter could be used...?
                if (s.matches("^(with\\b.*?\\bselect|select|explain)\\b.*?")) {
                    return ExecuteType.READ;
                }

                // These are sample DML statements. There may be many more
                else if (s.matches("^(insert|update|delete|merge|replace|upsert|lock)\\b.*?")) {
                    return ExecuteType.WRITE;
                }

                // These are only sample DDL statements. There may be many more
                else if (s.matches("^(create|alter|drop|truncate|grant|revoke|analyze|comment|flashback|enable|disable)\\b.*?")) {
                    return ExecuteType.DDL;
                }

                // JDBC escape syntax for routines
                else if (s.matches("^\\s*\\{\\s*(\\?\\s*=\\s*)call.*?")) {
                    return ExecuteType.ROUTINE;
                }

                // Vendor-specific calling of routines / procedural blocks
                else if (s.matches("^(call|begin|declare)\\b.*?")) {
                    return ExecuteType.ROUTINE;
                }
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
    public final Query[] batchQueries() {
        return batchQueries;
    }

    @Override
    public final Routine<?> routine() {
        return routine;
    }

    @Override
    public final void sql(String s) {
        this.sql = s;

        // If this isn't a batch query
        if (batchSQL.length == 1) {
            batchSQL[0] = s;
        }
    }

    @Override
    public final String sql() {
        return sql;
    }

    @Override
    public final String[] batchSQL() {
        return batchSQL;
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
    public final void resultSet(ResultSet rs) {
        this.resultSet = rs;
    }

    @Override
    public final ResultSet resultSet() {
        return resultSet;
    }

    @Override
    public final Configuration configuration() {
        return configuration;
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

        ConnectionProvider provider = connectionProvider != null ? connectionProvider : configuration.connectionProvider();
        if (connection == null && provider != null) {
            connection(provider, provider.acquire());
        }

        return connection;
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
            LOCAL_CONNECTION.set(c);
            connection = new SettingsEnabledConnection(new ProviderEnabledConnection(provider, c), configuration.settings());
        }
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
    public final int rows() {
        return rows;
    }

    @Override
    public final void rows(int r) {
        this.rows = r;

        // If this isn't a batch query
        if (batchRows.length == 1) {
            batchRows[0] = r;
        }
    }

    @Override
    public final int[] batchRows() {
        return batchRows;
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
    public final RuntimeException exception() {
        return exception;
    }

    @Override
    public final void exception(RuntimeException e) {
        this.exception = e;

        if (Boolean.TRUE.equals(settings().isDebugInfoOnStackTrace())) {

            // [#5570] Add jOOQ version and SQL Dialect info on the stack trace
            //         to help users write better bug reports.
            //         See http://stackoverflow.com/q/39712695/521799
            StackTraceElement[] oldStack = e.getStackTrace();
            if (oldStack != null) {
                StackTraceElement[] newStack = new StackTraceElement[oldStack.length + 1];
                System.arraycopy(oldStack, 0, newStack, 1, oldStack.length);
                newStack[0] = new StackTraceElement(
                    "org.jooq_" + Constants.VERSION + "." + dialect(),
                    "debug", null, -1);
                e.setStackTrace(newStack);
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

}
