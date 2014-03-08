/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
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
import org.jooq.Truncate;
import org.jooq.Update;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * A default implementation for the {@link ExecuteContext}.
 *
 * @author Lukas Eder
 */
class DefaultExecuteContext implements ExecuteContext {

    // Persistent attributes (repeatable)
    private final Configuration                  configuration;
    private final Map<Object, Object>            data;
    private final Query                          query;
    private final Routine<?>                     routine;
    private String                               sql;

    private final Query[]                        batchQueries;
    private final String[]                       batchSQL;
    private final int[]                          batchRows;

    // Transient attributes (created afresh per execution)
    private transient ConnectionProvider         connectionProvider;
    private transient Connection                 connection;
    private transient PreparedStatement          statement;
    private transient ResultSet                  resultSet;
    private transient Record                     record;
    private transient Result<?>                  result;
    private transient int                        rows  = -1;
    private transient RuntimeException           exception;
    private transient SQLException               sqlException;
    private transient SQLWarning                 sqlWarning;

    // ------------------------------------------------------------------------
    // XXX: Static utility methods for handling blob / clob lifecycle
    // ------------------------------------------------------------------------

    private static final ThreadLocal<List<Blob>> BLOBS            = new ThreadLocal<List<Blob>>();
    private static final ThreadLocal<List<Clob>> CLOBS            = new ThreadLocal<List<Clob>>();

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

        LOCAL_CONFIGURATION.remove();
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

    // ------------------------------------------------------------------------
    // XXX: Static utility methods for handling Configuration lifecycle
    // ------------------------------------------------------------------------

    private static final ThreadLocal<Configuration> LOCAL_CONFIGURATION = new ThreadLocal<Configuration>();

    /**
     * Get the registered configuration
     * <p>
     * It can be safely assumed that such a configuration is available once the
     * {@link ExecuteContext} has been established, until the statement is
     * closed.
     */
    static final Configuration localConfiguration() {
        return LOCAL_CONFIGURATION.get();
    }

    // ------------------------------------------------------------------------
    // XXX: Static utility methods for handling Configuration lifecycle
    // ------------------------------------------------------------------------

    private static final ThreadLocal<Connection> LOCAL_CONNECTION = new ThreadLocal<Connection>();

    /**
     * Get the registered connection
     * <p>
     * It can be safely assumed that such a connection is available once the
     * {@link ExecuteContext} has been established, until the statement is
     * closed.
     */
    static final Connection localConnection() {
        return LOCAL_CONNECTION.get();
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
        this.data = new HashMap<Object, Object>();
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
        LOCAL_CONFIGURATION.set(configuration);
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
            else if (query instanceof Truncate) {
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
            Connection c = provider.acquire();

            if (c != null) {
                LOCAL_CONNECTION.set(c);
                connection = new SettingsEnabledConnection(new ProviderEnabledConnection(provider, c), configuration.settings());
            }
        }

        return connection;
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
    }

    @Override
    public final SQLException sqlException() {
        return sqlException;
    }

    @Override
    public final void sqlException(SQLException e) {
        this.sqlException = e;
        exception(Utils.translate(sql(), e));
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
