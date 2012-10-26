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

import static org.jooq.conf.SettingsTools.getRenderMapping;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.fieldByName;
import static org.jooq.impl.Factory.one;
import static org.jooq.impl.Factory.trueCondition;
import static org.jooq.impl.Factory.zero;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.xml.bind.JAXB;

import org.jooq.Attachable;
import org.jooq.Batch;
import org.jooq.BatchBindStep;
import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.DeleteQuery;
import org.jooq.DeleteWhereStep;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.FactoryOperations;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.InsertQuery;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStep;
import org.jooq.LoaderOptionsStep;
import org.jooq.MergeKeyStep;
import org.jooq.MergeUsingStep;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.SelectQuery;
import org.jooq.SelectSelectStep;
import org.jooq.Sequence;
import org.jooq.SimpleSelectQuery;
import org.jooq.SimpleSelectWhereStep;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.TableRecord;
import org.jooq.Truncate;
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.UpdateSetStep;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.csv.CSVReader;

/**
 * A factory providing implementations to the org.jooq interfaces
 * <p>
 * This factory is the main entry point for client code, to access jOOQ classes
 * and functionality. Here, you can instanciate all of those objects that cannot
 * be accessed through other objects. For example, to create a {@link Field}
 * representing a constant value, you can write:
 * <p>
 * <code><pre>
 * Field&lt;String&gt; field = SQL.val("Hello World")
 * </pre></code>
 * <p>
 * Also, some SQL clauses cannot be expressed easily with DSL, for instance the
 * EXISTS clause, as it is not applied on a concrete object (yet). Hence you
 * should write
 * <p>
 * <code><pre>
 * Condition condition = SQL.exists(new Factory().select(...));
 * </pre></code>
 * <p>
 * <h3>Factory and static imports</h3> For increased fluency and readability of
 * your jOOQ client code, it is recommended that you static import all methods
 * from the <code>Factory</code>. For example: <code><pre>
 * import static org.jooq.impl.SQL.*;
 *
 * public class Main {
 *   public static void main(String[] args) {
 *     new Factory(dialect).select(val("Hello"), inline("World"));
 *     //              SQL.val ^^^           ^^^^^^ SQL.inline
 *   }
 * }
 * </pre></code>
 * <p>
 * A <code>Factory</code> holds a reference to a JDBC {@link Connection} and
 * operates upon that connection. This means, that a <code>Factory</code> is
 * <i>not</i> thread-safe, since a JDBC Connection is not thread-safe either.
 *
 * @author Lukas Eder
 */
public class Executor implements FactoryOperations {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID  = 2681360188806309513L;
    private static final JooqLogger log               = JooqLogger.getLogger(Factory.class);

    private static final Executor[] DEFAULT_INSTANCES = new Executor[SQLDialect.values().length];
    private final Configuration     configuration;

    // -------------------------------------------------------------------------
    // XXX Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a factory with a connection and a dialect configured.
     * <p>
     * If you provide a JDBC connection to a jOOQ Factory, jOOQ will use that
     * connection for creating statements, but it will never call any of these
     * methods:
     * <ul>
     * <li> {@link Connection#commit()}</li>
     * <li> {@link Connection#rollback()}</li>
     * <li> {@link Connection#close()}</li>
     * </ul>
     * Use this constructor if you want to handle transactions directly on the
     * connection.
     *
     * @param connection The connection to use with objects created from this
     *            factory
     * @param dialect The dialect to use with objects created from this factory
     */
    public Executor(Connection connection, SQLDialect dialect) {
        this(null, connection, dialect, null, null, null);
    }

    /**
     * Create a factory with a data source and a dialect configured.
     * <p>
     * If you provide a JDBC data source to a jOOQ Factory, jOOQ will use that
     * data source for initialising connections, and creating statements.
     * <p>
     * Use this constructor if you want to run distributed transactions, such as
     * <code>javax.transaction.UserTransaction</code>. If you provide jOOQ
     * factories with a data source, jOOQ will {@link Connection#close()
     * close()} all connections after query execution in order to return the
     * connection to the connection pool. If you do not use distributed
     * transactions, this will produce driver-specific behaviour at the end of
     * query execution at <code>close()</code> invocation (e.g. a transaction
     * rollback). Use {@link #Executor(Connection, SQLDialect)} instead, to
     * control the connection's lifecycle.
     *
     * @param datasource The data source to use with objects created from this
     *            factory
     * @param dialect The dialect to use with objects created from this factory
     */
    public Executor(DataSource datasource, SQLDialect dialect) {
        this(datasource, null, dialect, null, null, null);
    }

    /**
     * Create a factory with a dialect configured.
     * <p>
     * Without a connection or data source, this factory cannot execute queries.
     * Use it to render SQL only.
     *
     * @param dialect The dialect to use with objects created from this factory
     */
    public Executor(SQLDialect dialect) {
        this(null, null, dialect, null, null, null);
    }

    /**
     * Create a factory with a connection, a dialect and settings configured.
     * <p>
     * If you provide a JDBC connection to a jOOQ Factory, jOOQ will use that
     * connection for creating statements, but it will never call any of these
     * methods:
     * <ul>
     * <li> {@link Connection#commit()}</li>
     * <li> {@link Connection#rollback()}</li>
     * <li> {@link Connection#close()}</li>
     * </ul>
     * Use this constructor if you want to handle transactions directly on the
     * connection.
     *
     * @param connection The connection to use with objects created from this
     *            factory
     * @param dialect The dialect to use with objects created from this factory
     * @param settings The runtime settings to apply to objects created from
     *            this factory
     */
    @SuppressWarnings("deprecation")
    public Executor(Connection connection, SQLDialect dialect, Settings settings) {
        this(null, connection, dialect, settings, new org.jooq.SchemaMapping(settings), null);
    }

    /**
     * Create a factory with a data source, a dialect and settings configured.
     * <p>
     * If you provide a JDBC data source to a jOOQ Factory, jOOQ will use that
     * data source for initialising connections, and creating statements.
     * <p>
     * Use this constructor if you want to run distributed transactions, such as
     * <code>javax.transaction.UserTransaction</code>. If you provide jOOQ
     * factories with a data source, jOOQ will {@link Connection#close()
     * close()} all connections after query execution in order to return the
     * connection to the connection pool. If you do not use distributed
     * transactions, this will produce driver-specific behaviour at the end of
     * query execution at <code>close()</code> invocation (e.g. a transaction
     * rollback). Use {@link #Executor(Connection, SQLDialect, Settings)}
     * instead, to control the connection's lifecycle.
     *
     * @param datasource The data source to use with objects created from this
     *            factory
     * @param dialect The dialect to use with objects created from this factory
     * @param settings The runtime settings to apply to objects created from
     *            this factory
     */
    @SuppressWarnings("deprecation")
    public Executor(DataSource datasource, SQLDialect dialect, Settings settings) {
        this(datasource, null, dialect, settings, new org.jooq.SchemaMapping(settings), null);
    }

    /**
     * Create a factory with a dialect and settings configured
     * <p>
     * Without a connection or data source, this factory cannot execute queries.
     * Use it to render SQL only.
     *
     * @param dialect The dialect to use with objects created from this factory
     * @param settings The runtime settings to apply to objects created from
     *            this factory
     */
    @SuppressWarnings("deprecation")
    public Executor(SQLDialect dialect, Settings settings) {
        this(null, null, dialect, settings, new org.jooq.SchemaMapping(settings), null);
    }

    /**
     * Do the instanciation
     */
    @SuppressWarnings("deprecation")
    private Executor(DataSource datasource, Connection connection, SQLDialect dialect, Settings settings, org.jooq.SchemaMapping mapping, Map<String, Object> data) {
        this(new ExecutorConfiguration(datasource, connection, dialect, settings, mapping, data));
    }

    /**
     * Create an executor from a custom configuration
     *
     * @param configuration The configuration
     */
    public Executor(Configuration configuration) {
        this.configuration = configuration;
    }

    // -------------------------------------------------------------------------
    // XXX Configuration API
    // -------------------------------------------------------------------------

    @Override
    public final SQLDialect getDialect() {
        return configuration.getDialect();
    }

    @Override
    public final DataSource getDataSource() {
        return configuration.getDataSource();
    }

    @Override
    public final void setDataSource(DataSource datasource) {
        configuration.setDataSource(datasource);
    }

    @Override
    public final Connection getConnection() {
        return configuration.getConnection();
    }

    @Override
    public final void setConnection(Connection connection) {
        configuration.setConnection(connection);
    }

    @Override
    @Deprecated
    public final org.jooq.SchemaMapping getSchemaMapping() {
        return configuration.getSchemaMapping();
    }

    @Override
    public final Settings getSettings() {
        return configuration.getSettings();
    }

    @Override
    public final Map<String, Object> getData() {
        return configuration.getData();
    }

    @Override
    public final Object getData(String key) {
        return configuration.getData(key);
    }

    @Override
    public final Object setData(String key, Object value) {
        return configuration.setData(key, value);
    }

    private static class ExecutorConfiguration implements Configuration {

        /**
         * Serial version UID
         */
        private static final long            serialVersionUID = 8193158984283234708L;

        private transient Connection         connection;
        private transient DataSource         datasource;
        private final SQLDialect             dialect;

        @SuppressWarnings("deprecation")
        private final org.jooq.SchemaMapping mapping;
        private final Settings               settings;
        private final Map<String, Object>    data;

        @SuppressWarnings("deprecation")
        ExecutorConfiguration(DataSource datasource, Connection connection, SQLDialect dialect, Settings settings, org.jooq.SchemaMapping mapping, Map<String, Object> data) {
            this.connection = connection;
            this.datasource = datasource;
            this.dialect = dialect;
            this.settings = settings != null ? settings : SettingsTools.defaultSettings();
            this.mapping = mapping != null ? mapping : new org.jooq.SchemaMapping(this.settings);
            this.data = data != null ? data : new HashMap<String, Object>();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final SQLDialect getDialect() {
            return dialect;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final DataSource getDataSource() {
            return datasource;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setDataSource(DataSource datasource) {
            this.datasource = datasource;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final Connection getConnection() {

            // SQL-builder only Factory
            if (connection == null && datasource == null) {
                return null;
            }

            // [#1424] DataSource-enabled Factory with no Connection yet
            else if (connection == null && datasource != null) {
                return new DataSourceConnection(datasource, null, settings);
            }

            // Factory clone
            else if (connection.getClass() == DataSourceConnection.class) {
                return connection;
            }

            // Factory clone
            else if (connection.getClass() == ConnectionProxy.class) {
                return connection;
            }

            // [#1424] Connection-based Factory
            else {
                return new DataSourceConnection(null, new ConnectionProxy(connection, settings), settings);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final void setConnection(Connection connection) {
            this.connection = connection;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Deprecated
        public final org.jooq.SchemaMapping getSchemaMapping() {
            return mapping;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final Settings getSettings() {
            return settings;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final Map<String, Object> getData() {
            return data;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final Object getData(String key) {
            return data.get(key);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final Object setData(String key, Object value) {
            return data.put(key, value);
        }

        @Override
        public String toString() {
            StringWriter writer = new StringWriter();
            JAXB.marshal(settings, writer);

            return "ExecutorConfiguration [\n\tconnected=" + (connection != null) +
                ",\n\tdialect=" + dialect +
                ",\n\tdata=" + data +
                ",\n\tsettings=\n\t\t" + writer.toString().trim().replace("\n", "\n\t\t") +
                "\n]";        }
    }

    // -------------------------------------------------------------------------
    // XXX Convenience methods accessing the underlying Connection
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final void commit() throws DataAccessException {
        try {
            log.debug("commit");
            getConnection().commit();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot commit transaction", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void rollback() throws DataAccessException {
        try {
            log.debug("rollback");
            getConnection().rollback();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot rollback transaction", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void rollback(Savepoint savepoint) throws DataAccessException {
        try {
            log.debug("rollback to savepoint");
            getConnection().rollback(savepoint);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot rollback transaction", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Savepoint setSavepoint() throws DataAccessException {
        try {
            log.debug("set savepoint");
            return getConnection().setSavepoint();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set savepoint", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Savepoint setSavepoint(String name) throws DataAccessException {
        try {
            log.debug("set savepoint", name);
            return getConnection().setSavepoint(name);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set savepoint", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void releaseSavepoint(Savepoint savepoint) throws DataAccessException {
        try {
            log.debug("release savepoint");
            getConnection().releaseSavepoint(savepoint);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot release savepoint", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setAutoCommit(boolean autoCommit) throws DataAccessException {
        try {
            log.debug("setting auto commit", autoCommit);
            getConnection().setAutoCommit(autoCommit);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set autoCommit", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean getAutoCommit() throws DataAccessException {
        try {
            return getConnection().getAutoCommit();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot get autoCommit", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setHoldability(int holdability) throws DataAccessException {
        try {
            log.debug("setting holdability", holdability);
            getConnection().setHoldability(holdability);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set holdability", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getHoldability() throws DataAccessException {
        try {
            return getConnection().getHoldability();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot get holdability", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setTransactionIsolation(int level) throws DataAccessException {
        try {
            log.debug("setting tx isolation", level);
            getConnection().setTransactionIsolation(level);
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot set transactionIsolation", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getTransactionIsolation() throws DataAccessException {
        try {
            return getConnection().getTransactionIsolation();
        }
        catch (Exception e) {
            throw new DataAccessException("Cannot get transactionIsolation", e);
        }
    }

    // -------------------------------------------------------------------------
    // XXX RenderContext and BindContext accessors
    // -------------------------------------------------------------------------

    /**
     * Get a new {@link RenderContext} for the context of this factory
     * <p>
     * This will return an initialised render context as such:
     * <ul>
     * <li> <code>{@link RenderContext#declareFields()} == false</code></li>
     * <li> <code>{@link RenderContext#declareTables()} == false</code></li>
     * <li> <code>{@link RenderContext#inline()} == false</code></li>
     * <li> <code>{@link RenderContext#namedParams()} == false</code></li>
     * </ul>
     * <p>
     * RenderContext for JOOQ INTERNAL USE only. Avoid referencing it directly
     */
    public final RenderContext renderContext() {
        return new DefaultRenderContext(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String render(QueryPart part) {
        return renderContext().render(part);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String renderNamedParams(QueryPart part) {
        return renderContext().namedParams(true).render(part);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String renderInlined(QueryPart part) {
        return renderContext().inline(true).render(part);
    }

    /**
     * Get a new {@link BindContext} for the context of this factory
     * <p>
     * This will return an initialised bind context as such:
     * <ul>
     * <li> <code>{@link RenderContext#declareFields()} == false</code></li>
     * <li> <code>{@link RenderContext#declareTables()} == false</code></li>
     * </ul>
     * <p>
     * RenderContext for JOOQ INTERNAL USE only. Avoid referencing it directly
     */
    public final BindContext bindContext(PreparedStatement stmt) {
        return new DefaultBindContext(this, stmt);
    }

    /**
     * Get a new {@link BindContext} for the context of this factory
     * <p>
     * This will return an initialised bind context as such:
     * <ul>
     * <li> <code>{@link RenderContext#declareFields()} == false</code></li>
     * <li> <code>{@link RenderContext#declareTables()} == false</code></li>
     * </ul>
     * <p>
     * RenderContext for JOOQ INTERNAL USE only. Avoid referencing it directly
     */
    public final int bind(QueryPart part, PreparedStatement stmt) {
        return bindContext(stmt).bind(part).peekIndex();
    }

    // -------------------------------------------------------------------------
    // XXX Attachable and Serializable API
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final void attach(Attachable... attachables) {
        attach(Arrays.asList(attachables));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void attach(Collection<Attachable> attachables) {
        for (Attachable attachable : attachables) {
            attachable.attach(this);
        }
    }

    // -------------------------------------------------------------------------
    // XXX Access to the loader API
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> LoaderOptionsStep<R> loadInto(Table<R> table) {
        return new LoaderImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Query query(String sql) {
        return query(sql, new Object[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Query query(String sql, Object... bindings) {
        return new SQLQuery(this, sql, bindings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Query query(String sql, QueryPart... parts) {
        return new SQLQuery(this, sql, parts);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Result<Record> fetch(String sql) throws DataAccessException {
        return resultQuery(sql).fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Result<Record> fetch(String sql, Object... bindings) throws DataAccessException {
        return resultQuery(sql, bindings).fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Result<Record> fetch(String sql, QueryPart... parts) throws DataAccessException {
        return resultQuery(sql, parts).fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Cursor<Record> fetchLazy(String sql) throws DataAccessException {
        return resultQuery(sql).fetchLazy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Cursor<Record> fetchLazy(String sql, Object... bindings) throws DataAccessException {
        return resultQuery(sql, bindings).fetchLazy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Cursor<Record> fetchLazy(String sql, QueryPart... parts) throws DataAccessException {
        return resultQuery(sql, parts).fetchLazy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Result<Record>> fetchMany(String sql) throws DataAccessException {
        return resultQuery(sql).fetchMany();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Result<Record>> fetchMany(String sql, Object... bindings) throws DataAccessException {
        return resultQuery(sql, bindings).fetchMany();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Result<Record>> fetchMany(String sql, QueryPart... parts) throws DataAccessException {
        return resultQuery(sql, parts).fetchMany();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Record fetchOne(String sql) throws DataAccessException {
        return resultQuery(sql).fetchOne();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Record fetchOne(String sql, Object... bindings) throws DataAccessException {
        return resultQuery(sql, bindings).fetchOne();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Record fetchOne(String sql, QueryPart... parts) throws DataAccessException {
        return resultQuery(sql, parts).fetchOne();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int execute(String sql) throws DataAccessException {
        return query(sql).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int execute(String sql, Object... bindings) throws DataAccessException {
        return query(sql, bindings).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int execute(String sql, QueryPart... parts) throws DataAccessException {
        return query(sql, parts).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ResultQuery<Record> resultQuery(String sql) {
        return resultQuery(sql, new Object[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ResultQuery<Record> resultQuery(String sql, Object... bindings) {
        return new SQLResultQuery(this, sql, bindings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ResultQuery<Record> resultQuery(String sql, QueryPart... parts) {
        return new SQLResultQuery(this, sql, parts);
    }

    // -------------------------------------------------------------------------
    // XXX JDBC convenience methods
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final Result<Record> fetch(ResultSet rs) {
        return fetchLazy(rs).fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Cursor<Record> fetchLazy(ResultSet rs) {
        ExecuteContext ctx = new DefaultExecuteContext(this);
        ExecuteListener listener = new ExecuteListeners(ctx);

        try {
            FieldProvider fields = new MetaDataFieldProvider(this, rs.getMetaData());

            ctx.resultSet(rs);
            return new CursorImpl<Record>(ctx, listener, fields, false);
        }
        catch (SQLException e) {
            ctx.sqlException(e);
            listener.exception(ctx);
            throw ctx.exception();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Result<Record> fetchFromCSV(String string) {
        return fetchFromCSV(string, ',');
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Result<Record> fetchFromCSV(String string, char delimiter) {
        CSVReader reader = new CSVReader(new StringReader(string), delimiter);
        List<String[]> all = null;

        try {
            all = reader.readAll();
        }
        catch (IOException e) {
            throw new DataAccessException("Could not read the CSV string", e);
        }
        finally {
            try {
                reader.close();
            }
            catch (IOException ignore) {}
        }

        FieldList fields = new FieldList();

        if (all.size() == 0) {
            return new ResultImpl<Record>(this, fields);
        }
        else {
            for (String name : all.get(0)) {
                fields.add(fieldByName(String.class, name));
            }

            Result<Record> result = new ResultImpl<Record>(this, fields);

            if (all.size() > 1) {
                for (String[] values : all.subList(1, all.size())) {
                    Record record = new RecordImpl(fields);

                    for (int i = 0; i < Math.min(values.length, fields.size()); i++) {
                        Util.setValue(record, fields.get(i), values[i]);
                    }

                    result.add(record);
                }
            }

            return result;
        }
    }

    // -------------------------------------------------------------------------
    // XXX Global Query factory
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> SimpleSelectWhereStep<R> selectFrom(Table<R> table) {
        return new SimpleSelectImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SelectSelectStep select(Field<?>... fields) {
        return new SelectImpl(this).select(fields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SelectSelectStep selectZero() {
        return new SelectImpl(this).select(zero());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SelectSelectStep selectOne() {
        return new SelectImpl(this).select(one());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SelectSelectStep selectCount() {
        return new SelectImpl(this).select(count());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SelectSelectStep selectDistinct(Field<?>... fields) {
        return new SelectImpl(this, true).select(fields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SelectSelectStep select(Collection<? extends Field<?>> fields) {
        return new SelectImpl(this).select(fields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SelectSelectStep selectDistinct(Collection<? extends Field<?>> fields) {
        return new SelectImpl(this, true).select(fields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SelectQuery selectQuery() {
        return new SelectQueryImpl(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> SimpleSelectQuery<R> selectQuery(TableLike<R> table) {
        return new SimpleSelectQueryImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> InsertQuery<R> insertQuery(Table<R> into) {
        return new InsertQueryImpl<R>(this, into);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> InsertSetStep<R> insertInto(Table<R> into) {
        return new InsertImpl<R>(this, into, Collections.<Field<?>>emptyList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> InsertValuesStep<R> insertInto(Table<R> into, Field<?>... fields) {
        return new InsertImpl<R>(this, into, Arrays.asList(fields));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> InsertValuesStep<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields) {
        return new InsertImpl<R>(this, into, fields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> UpdateQuery<R> updateQuery(Table<R> table) {
        return new UpdateQueryImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> UpdateSetStep<R> update(Table<R> table) {
        return new UpdateImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table) {
        return new MergeImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> MergeKeyStep<R> mergeInto(Table<R> table, Field<?>... fields) {
        return mergeInto(table, Arrays.asList(fields));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> MergeKeyStep<R> mergeInto(Table<R> table, Collection<? extends Field<?>> fields) {
        return new MergeImpl<R>(this, table, fields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> DeleteQuery<R> deleteQuery(Table<R> table) {
        return new DeleteQueryImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> DeleteWhereStep<R> delete(Table<R> table) {
        return new DeleteImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Batch batch(Query... queries) {
        return new BatchMultiple(this, queries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Batch batch(Collection<? extends Query> queries) {
        return batch(queries.toArray(new Query[queries.size()]));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final BatchBindStep batch(Query query) {
        return new BatchSingle(this, query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Batch batchStore(UpdatableRecord<?>... records) {
        return new BatchStore(this, records);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Batch batchStore(Collection<? extends UpdatableRecord<?>> records) {
        return batchStore(records.toArray(new UpdatableRecord[records.size()]));
    }

    // -------------------------------------------------------------------------
    // XXX DDL Statements
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> Truncate<R> truncate(Table<R> table) {
        return new TruncateImpl<R>(this, table);
    }

    // -------------------------------------------------------------------------
    // XXX Other queries for identites and sequences
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final BigInteger lastID() {
        switch (getDialect()) {
            case DERBY: {
                Field<BigInteger> field = field("identity_val_local()", BigInteger.class);
                return select(field).fetchOne(field);
            }

            case H2:
            case HSQLDB: {
                Field<BigInteger> field = field("identity()", BigInteger.class);
                return select(field).fetchOne(field);
            }

            case INGRES: {
                Field<BigInteger> field = field("last_identity()", BigInteger.class);
                return select(field).fetchOne(field);
            }

            case CUBRID:
            case MYSQL: {
                Field<BigInteger> field = field("last_insert_id()", BigInteger.class);
                return select(field).fetchOne(field);
            }

            case SQLITE: {
                Field<BigInteger> field = field("last_insert_rowid()", BigInteger.class);
                return select(field).fetchOne(field);
            }

            case ASE:
            case SQLSERVER:
            case SYBASE: {
                Field<BigInteger> field = field("@@identity", BigInteger.class);
                return select(field).fetchOne(field);
            }

            default:
                throw new SQLDialectNotSupportedException("identity functionality not supported by " + getDialect());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends Number> T nextval(Sequence<T> sequence) {
        Field<T> nextval = sequence.nextval();
        return select(nextval).fetchOne(nextval);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <T extends Number> T currval(Sequence<T> sequence) {
        Field<T> currval = sequence.currval();
        return select(currval).fetchOne(currval);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("deprecation")
    @Override
    public final int use(Schema schema) {
        int result = 0;

        try {
            String schemaName = render(schema);

            switch (getDialect()) {
                case DB2:
                case DERBY:
                case H2:
                case HSQLDB:
                    result = query("set schema " + schemaName).execute();
                    break;

                case ASE:
                case MYSQL:
                case SYBASE:
                    result = query("use " + schemaName).execute();
                    break;

                case ORACLE:
                    result = query("alter session set current_schema = " + schemaName).execute();
                    break;

                case POSTGRES:
                    result = query("set search_path = " + schemaName).execute();
                    break;

                // SQL Server do not support such a syntax for selecting
                // schemata, only for selecting databases
                case SQLSERVER:
                    break;

                // CUBRID and SQLite don't have any schemata
                case CUBRID:
                case SQLITE:
                    break;
            }
        }
        finally {
            getRenderMapping(getSettings()).setDefaultSchema(schema.getName());
            getSchemaMapping().use(schema);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int use(String schema) {
        return use(new SchemaImpl(schema));
    }

    // -------------------------------------------------------------------------
    // XXX Global Record factory
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends UDTRecord<R>> R newRecord(UDT<R> type) {
        return Util.newRecord(type, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> R newRecord(Table<R> table) {
        return Util.newRecord(table, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> R newRecord(Table<R> table, Object source) {
        R result = newRecord(table);
        result.from(source);
        return result;
    }
    // -------------------------------------------------------------------------
    // XXX Fast querying
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> Result<R> fetch(Table<R> table) {
        return fetch(table, trueCondition());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> Result<R> fetch(Table<R> table, Condition condition) {
        return selectFrom(table).where(condition).fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> R fetchOne(Table<R> table) {
        return filterOne(fetch(table));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> R fetchOne(Table<R> table, Condition condition) {
        return filterOne(fetch(table, condition));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> R fetchAny(Table<R> table) {
        return filterOne(selectFrom(table).limit(1).fetch());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> int executeInsert(R record) {
        InsertQuery<R> insert = insertQuery(record.getTable());
        insert.setRecord(record);
        return insert.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends UpdatableRecord<R>> int executeUpdate(R record) {
        UpdateQuery<R> update = updateQuery(record.getTable());
        Util.addConditions(update, record, record.getTable().getMainKey().getFieldsArray());
        update.setRecord(record);
        return update.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>, T> int executeUpdate(R record, Condition condition) {
        UpdateQuery<R> update = updateQuery(record.getTable());
        update.addConditions(condition);
        update.setRecord(record);
        return update.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends UpdatableRecord<R>> int executeDelete(R record) {
        DeleteQuery<R> delete = deleteQuery(record.getTable());
        Util.addConditions(delete, record, record.getTable().getMainKey().getFieldsArray());
        return delete.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>, T> int executeDelete(R record, Condition condition) {
        DeleteQuery<R> delete = deleteQuery(record.getTable());
        delete.addConditions(condition);
        return delete.execute();
    }

    // -------------------------------------------------------------------------
    // XXX Static initialisation of dialect-specific data types
    // -------------------------------------------------------------------------

    static {
        // Load all dialect-specific data types
        // TODO [#650] Make this more reliable using a data type registry

        try {
            Class.forName(SQLDataType.class.getName());
        } catch (Exception ignore) {}
    }

    // -------------------------------------------------------------------------
    // XXX Internals
    // -------------------------------------------------------------------------

    private static int filterOne(int i, String action) {
        if (i <= 1) {
            return i;
        }
        else {
            throw new InvalidResultException("Too many rows " + action + " : " + i);
        }
    }

    private static <R extends Record> R filterOne(List<R> list) {
        if (filterOne(list.size(), "selected") == 1) {
            return list.get(0);
        }

        return null;
    }

    @Override
    public String toString() {
        return configuration.toString();
    }

    static {
        for (SQLDialect dialect : SQLDialect.values()) {
            DEFAULT_INSTANCES[dialect.ordinal()] = new Executor(dialect);
        }
    }

    /**
     * Get a default <code>Factory</code> without a {@link Connection}
     */
    final static Executor getNewFactory(SQLDialect dialect) {
        return getNewFactory(DEFAULT_INSTANCES[dialect.ordinal()]);
    }

    /**
     * Get a default <code>Factory</code> without a {@link Connection}
     */
    final static Executor getStaticFactory(SQLDialect dialect) {
        return DEFAULT_INSTANCES[dialect.ordinal()];
    }

    /**
     * Get a default <code>Factory</code> with a {@link Connection}
     */
    final static Executor getNewFactory(Configuration configuration) {
        if (configuration == null) {
            return getNewFactory(DefaultConfiguration.DEFAULT_CONFIGURATION);
        }
        else {
            return new Executor(configuration);
        }
    }

    /**
     * Whether the supplied {@link Configuration} can be obtained with
     * {@link #getStaticFactory(SQLDialect)}
     */
    final static boolean isStaticFactory(Configuration configuration) {
        if (configuration == null) {
            return false;
        }
        else if (configuration instanceof DefaultConfiguration) {
            return true;
        }
        else {
            return getStaticFactory(configuration.getDialect()) == configuration;
        }
    }
}
