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

import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.conf.SettingsTools.getRenderMapping;
import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.fieldByName;
import static org.jooq.impl.Factory.trueCondition;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.jooq.Attachable;
import org.jooq.Batch;
import org.jooq.BatchBindStep;
import org.jooq.BindContext;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.Converter;
import org.jooq.Cursor;
import org.jooq.DeleteQuery;
import org.jooq.DeleteWhereStep;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.InsertQuery;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStep;
import org.jooq.LoaderOptionsStep;
import org.jooq.MergeKeyStep;
import org.jooq.MergeUsingStep;
import org.jooq.Meta;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RecordHandler;
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
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.TableRecord;
import org.jooq.Truncate;
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.UpdateSetFirstStep;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.MappingException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.csv.CSVReader;

/**
 * TODO: Write this Javadoc
 * <p>
 * An <code>Executor</code> holds a reference to a JDBC {@link Connection} and
 * operates upon that connection. This means, that an <code>Executor</code> is
 * <i>not</i> thread-safe, since a JDBC Connection is not thread-safe either.
 *
 * @author Lukas Eder
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Executor implements Configuration {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID  = 2681360188806309513L;
    private final Configuration     configuration;

    // -------------------------------------------------------------------------
    // XXX Constructors
    // -------------------------------------------------------------------------

    /**
     * Create an executor with a dialect configured.
     * <p>
     * Without a connection or data source, this executor cannot execute
     * queries. Use it to render SQL only.
     *
     * @param dialect The dialect to use with objects created from this executor
     */
    public Executor(SQLDialect dialect) {
        this(dialect, null);
    }

    /**
     * Create an executor with a dialect and settings configured
     * <p>
     * Without a connection or data source, this executor cannot execute
     * queries. Use it to render SQL only.
     *
     * @param dialect The dialect to use with objects created from this executor
     * @param settings The runtime settings to apply to objects created from
     *            this executor
     */
    public Executor(SQLDialect dialect, Settings settings) {
        this(new DefaultConfiguration(new NoConnectionProvider(), dialect, settings, null));
    }

    /**
     * Create an executor with a connection and a dialect configured.
     * <p>
     * If you provide a JDBC connection to a jOOQ Executor, jOOQ will use that
     * connection directly for creating statements.
     * <p>
     * This is a convenience constructor for
     * {@link #Executor(ConnectionProvider, SQLDialect, Settings)} using a
     * {@link DefaultConnectionProvider}
     *
     * @param connection The connection to use with objects created from this
     *            executor
     * @param dialect The dialect to use with objects created from this executor
     * @see DefaultConnectionProvider
     */
    public Executor(Connection connection, SQLDialect dialect) {
        this(connection, dialect, null);
    }

    /**
     * Create an executor with a connection, a dialect and settings configured.
     * <p>
     * If you provide a JDBC connection to a jOOQ Executor, jOOQ will use that
     * connection directly for creating statements.
     * <p>
     * This is a convenience constructor for
     * {@link #Executor(ConnectionProvider, SQLDialect, Settings)} using a
     * {@link DefaultConnectionProvider}
     *
     * @param connection The connection to use with objects created from this
     *            executor
     * @param dialect The dialect to use with objects created from this executor
     * @param settings The runtime settings to apply to objects created from
     *            this executor
     * @see DefaultConnectionProvider
     */
    public Executor(Connection connection, SQLDialect dialect, Settings settings) {
        this(new DefaultConfiguration(new DefaultConnectionProvider(connection), dialect, settings, null));
    }

    /**
     * Create an executor with a data source and a dialect configured.
     * <p>
     * If you provide a JDBC data source to a jOOQ Executor, jOOQ will use that
     * data source for initialising connections, and creating statements.
     * <p>
     * This is a convenience constructor for
     * {@link #Executor(ConnectionProvider, SQLDialect)} using a
     * {@link DataSourceConnectionProvider}
     *
     * @param datasource The data source to use with objects created from this
     *            executor
     * @param dialect The dialect to use with objects created from this executor
     * @see DataSourceConnectionProvider
     */
    public Executor(DataSource datasource, SQLDialect dialect) {
        this(datasource, dialect, null);
    }

    /**
     * Create an executor with a data source, a dialect and settings configured.
     * <p>
     * If you provide a JDBC data source to a jOOQ Executor, jOOQ will use that
     * data source for initialising connections, and creating statements.
     * <p>
     * This is a convenience constructor for
     * {@link #Executor(ConnectionProvider, SQLDialect, Settings)} using a
     * {@link DataSourceConnectionProvider}
     *
     * @param datasource The data source to use with objects created from this
     *            executor
     * @param dialect The dialect to use with objects created from this executor
     * @param settings The runtime settings to apply to objects created from
     *            this executor
     * @see DataSourceConnectionProvider
     */
    public Executor(DataSource datasource, SQLDialect dialect, Settings settings) {
        this(new DefaultConfiguration(new DataSourceConnectionProvider(datasource), dialect, settings, null));
    }

    /**
     * Create an executor with a custom connection provider and a dialect
     * configured.
     *
     * @param connectionProvider The connection provider providing jOOQ with
     *            JDBC connections
     * @param dialect The dialect to use with objects created from this executor
     */
    public Executor(ConnectionProvider connectionProvider, SQLDialect dialect) {
        this(connectionProvider, dialect, null);
    }

    /**
     * Create an executor with a custom connection provider, a dialect and settings
     * configured.
     *
     * @param connectionProvider The connection provider providing jOOQ with
     *            JDBC connections
     * @param dialect The dialect to use with objects created from this executor
     * @param settings The runtime settings to apply to objects created from
     *            this executor
     */
    public Executor(ConnectionProvider connectionProvider, SQLDialect dialect, Settings settings) {
        this(new DefaultConfiguration(connectionProvider, dialect, settings, null));
    }

    /**
     * Create an executor from a custom configuration
     *
     * @param configuration The configuration
     */
    public Executor(Configuration configuration) {

        // The Configuration can be null when unattached Query objects are
        // executed or when unattached Records are stored...
        if (configuration == null) {
            configuration = new DefaultConfiguration();
        }

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

    @Override
    public final ConnectionProvider getConnectionProvider() {
        return configuration.getConnectionProvider();
    }

    // -------------------------------------------------------------------------
    // XXX Convenience methods accessing the underlying Connection
    // -------------------------------------------------------------------------

    public final Meta meta() {
        return new MetaImpl(this);
    }


    // -------------------------------------------------------------------------
    // XXX RenderContext and BindContext accessors
    // -------------------------------------------------------------------------

    /**
     * Get a new {@link RenderContext} for the context of this executor
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
     * Render a QueryPart in the context of this executor
     * <p>
     * This is the same as calling <code>renderContext().render(part)</code>
     *
     * @param part The {@link QueryPart} to be rendered
     * @return The rendered SQL
     */
    public final String render(QueryPart part) {
        return renderContext().render(part);
    }

    /**
     * Render a QueryPart in the context of this executor, rendering bind
     * variables as named parameters.
     * <p>
     * This is the same as calling
     * <code>renderContext().namedParams(true).render(part)</code>
     *
     * @param part The {@link QueryPart} to be rendered
     * @return The rendered SQL
     */
    public final String renderNamedParams(QueryPart part) {
        return renderContext().namedParams(true).render(part);
    }

    /**
     * Render a QueryPart in the context of this executor, inlining all bind
     * variables.
     * <p>
     * This is the same as calling
     * <code>renderContext().inline(true).render(part)</code>
     *
     * @param part The {@link QueryPart} to be rendered
     * @return The rendered SQL
     */
    public final String renderInlined(QueryPart part) {
        return renderContext().inline(true).render(part);
    }

    /**
     * Get a new {@link BindContext} for the context of this executor
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
     * Get a new {@link BindContext} for the context of this executor
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
     * Attach this <code>Executor</code> to some attachables
     */
    public final void attach(Attachable... attachables) {
        attach(Arrays.asList(attachables));
    }

    /**
     * Attach this <code>Executor</code> to some attachables
     */
    public final void attach(Collection<? extends Attachable> attachables) {
        for (Attachable attachable : attachables) {
            attachable.attach(this);
        }
    }

    // -------------------------------------------------------------------------
    // XXX Access to the loader API
    // -------------------------------------------------------------------------

    /**
     * Create a new <code>Loader</code> object to load data from a CSV or XML
     * source
     */
    @Support
    public final <R extends TableRecord<R>> LoaderOptionsStep<R> loadInto(Table<R> table) {
        return new LoaderImpl<R>(this, table);
    }

    /**
     * Create a new query holding plain SQL. There must not be any binding
     * variables contained in the SQL
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "SET SCHEMA 'abc'";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A query wrapping the plain SQL
     */
    @Support
    public final Query query(String sql) {
        return query(sql, new Object[0]);
    }

    /**
     * Create a new query holding plain SQL. There must be as many bind
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "SET SCHEMA 'abc'";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return A query wrapping the plain SQL
     */
    @Support
    public final Query query(String sql, Object... bindings) {
        return new SQLQuery(this, sql, bindings);
    }

    /**
     * Create a new query holding plain SQL.
     * <p>
     * Unlike {@link #query(String, Object...)}, the SQL passed to this method
     * should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * query("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will render this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link Factory#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A query wrapping the plain SQL
     */
    @Support
    public final Query query(String sql, QueryPart... parts) {
        return new SQLQuery(this, sql, parts);
    }

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final Result<Record> fetch(String sql) throws DataAccessException {
        return resultQuery(sql).fetch();
    }

    /**
     * Execute a new query holding plain SQL. There must be as many bind
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final Result<Record> fetch(String sql, Object... bindings) throws DataAccessException {
        return resultQuery(sql, bindings).fetch();
    }

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Unlike {@link #fetch(String, Object...)}, the SQL passed to this method
     * should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * fetch("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will execute this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link Factory#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final Result<Record> fetch(String sql, QueryPart... parts) throws DataAccessException {
        return resultQuery(sql, parts).fetch();
    }

    /**
     * Execute a new query holding plain SQL and "lazily" return the generated
     * result.
     * <p>
     * The returned {@link Cursor} holds a reference to the executed
     * {@link PreparedStatement} and the associated {@link ResultSet}. Data can
     * be fetched (or iterated over) lazily, fetching records from the
     * {@link ResultSet} one by one.
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final Cursor<Record> fetchLazy(String sql) throws DataAccessException {
        return resultQuery(sql).fetchLazy();
    }

    /**
     * Execute a new query holding plain SQL and "lazily" return the generated
     * result. There must be as many bind variables contained in the SQL, as
     * passed in the bindings parameter
     * <p>
     * The returned {@link Cursor} holds a reference to the executed
     * {@link PreparedStatement} and the associated {@link ResultSet}. Data can
     * be fetched (or iterated over) lazily, fetching records from the
     * {@link ResultSet} one by one.
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final Cursor<Record> fetchLazy(String sql, Object... bindings) throws DataAccessException {
        return resultQuery(sql, bindings).fetchLazy();
    }

    /**
     * Execute a new query holding plain SQL and "lazily" return the generated
     * result.
     * <p>
     * The returned {@link Cursor} holds a reference to the executed
     * {@link PreparedStatement} and the associated {@link ResultSet}. Data can
     * be fetched (or iterated over) lazily, fetching records from the
     * {@link ResultSet} one by one.
     * <p>
     * Unlike {@link #fetchLazy(String, Object...)}, the SQL passed to this
     * method should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * fetchLazy("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will execute this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link Factory#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final Cursor<Record> fetchLazy(String sql, QueryPart... parts) throws DataAccessException {
        return resultQuery(sql, parts).fetchLazy();
    }

    /**
     * Execute a new query holding plain SQL, possibly returning several result
     * sets
     * <p>
     * Example (Sybase ASE):
     * <p>
     * <code><pre>
     * String sql = "sp_help 'my_table'";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final List<Result<Record>> fetchMany(String sql) throws DataAccessException {
        return resultQuery(sql).fetchMany();
    }

    /**
     * Execute a new query holding plain SQL, possibly returning several result
     * sets. There must be as many bind variables contained in the SQL, as
     * passed in the bindings parameter
     * <p>
     * Example (Sybase ASE):
     * <p>
     * <code><pre>
     * String sql = "sp_help 'my_table'";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final List<Result<Record>> fetchMany(String sql, Object... bindings) throws DataAccessException {
        return resultQuery(sql, bindings).fetchMany();
    }

    /**
     * Execute a new query holding plain SQL, possibly returning several result
     * sets.
     * <p>
     * Unlike {@link #fetchMany(String, Object...)}, the SQL passed to this
     * method should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * fetchMany("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will execute this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link Factory#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final List<Result<Record>> fetchMany(String sql, QueryPart... parts) throws DataAccessException {
        return resultQuery(sql, parts).fetchMany();
    }

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return The results from the executed query. This is never
     *         <code>null</code>, even if the database returns no
     *         {@link ResultSet}
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    public final Record fetchOne(String sql) throws DataAccessException, InvalidResultException {
        return resultQuery(sql).fetchOne();
    }

    /**
     * Execute a new query holding plain SQL. There must be as many bind
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return The results from the executed query. This may be
     *         <code>null</code> if the database returned no records
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    public final Record fetchOne(String sql, Object... bindings) throws DataAccessException, InvalidResultException {
        return resultQuery(sql, bindings).fetchOne();
    }

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Unlike {@link #fetchOne(String, Object...)}, the SQL passed to this
     * method should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * fetchOne("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will execute this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link Factory#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The results from the executed query. This may be
     *         <code>null</code> if the database returned no records
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    public final Record fetchOne(String sql, QueryPart... parts) throws DataAccessException, InvalidResultException {
        return resultQuery(sql, parts).fetchOne();
    }

    /**
     * Execute a query holding plain SQL.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final int execute(String sql) throws DataAccessException {
        return query(sql).execute();
    }

    /**
     * Execute a new query holding plain SQL. There must be as many bind
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final int execute(String sql, Object... bindings) throws DataAccessException {
        return query(sql, bindings).execute();
    }

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Unlike {@link #execute(String, Object...)}, the SQL passed to this method
     * should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * execute("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will execute this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link Factory#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final int execute(String sql, QueryPart... parts) throws DataAccessException {
        return query(sql, parts).execute();
    }

    /**
     * Create a new query holding plain SQL. There must not be any binding
     * variables contained in the SQL
     * <p>
     * Use this method, when you want to take advantage of the many ways to
     * fetch results in jOOQ, using {@link ResultQuery}. Some examples:
     * <p>
     * <table border="1">
     * <tr>
     * <td> {@link ResultQuery#fetchLazy()}</td>
     * <td>Open a cursor and fetch records one by one</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(Class)}</td>
     * <td>Fetch records into a custom POJO (optionally annotated with JPA
     * annotations)</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(RecordHandler)}</td>
     * <td>Fetch records into a custom callback (similar to Spring's RowMapper)</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchLater()}</td>
     * <td>Fetch records of a long-running query asynchronously</td>
     * </tr>
     * </table>
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return An executable query
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final ResultQuery<Record> resultQuery(String sql) {
        return resultQuery(sql, new Object[0]);
    }

    /**
     * Create a new query holding plain SQL. There must be as many bind
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Use this method, when you want to take advantage of the many ways to
     * fetch results in jOOQ, using {@link ResultQuery}. Some examples:
     * <p>
     * <table border="1">
     * <tr>
     * <td> {@link ResultQuery#fetchLazy()}</td>
     * <td>Open a cursor and fetch records one by one</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(Class)}</td>
     * <td>Fetch records into a custom POJO (optionally annotated with JPA
     * annotations)</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchInto(RecordHandler)}</td>
     * <td>Fetch records into a custom callback (similar to Spring's RowMapper)</td>
     * </tr>
     * <tr>
     * <td> {@link ResultQuery#fetchLater()}</td>
     * <td>Fetch records of a long-running query asynchronously</td>
     * </tr>
     * </table>
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code> Example
     * (SQLite):
     * <p>
     * <code><pre>
     * String sql = "pragma table_info('my_table')";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return A query wrapping the plain SQL
     */
    @Support
    public final ResultQuery<Record> resultQuery(String sql, Object... bindings) {
        return new SQLResultQuery(this, sql, bindings);
    }

    /**
     * Create a new query holding plain SQL.
     * <p>
     * Unlike {@link #resultQuery(String, Object...)}, the SQL passed to this
     * method should not contain any bind variables. Instead, you can pass
     * {@link QueryPart} objects to the method which will be rendered at indexed
     * locations of your SQL string as such: <code><pre>
     * // The following query
     * resultQuery("select {0}, {1} from {2}", val(1), inline("test"), name("DUAL"));
     *
     * // Will render this SQL on an Oracle database with RenderNameStyle.QUOTED:
     * select ?, 'test' from "DUAL"
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses! One way to escape
     * literals is to use {@link Factory#name(String...)} and similar methods
     *
     * @param sql The SQL clause, containing {numbered placeholders} where query
     *            parts can be injected
     * @param parts The {@link QueryPart} objects that are rendered at the
     *            {numbered placeholder} locations
     * @return A query wrapping the plain SQL
     */
    @Support
    public final ResultQuery<Record> resultQuery(String sql, QueryPart... parts) {
        return new SQLResultQuery(this, sql, parts);
    }

    // -------------------------------------------------------------------------
    // XXX JDBC convenience methods
    // -------------------------------------------------------------------------

    /**
     * Fetch all data from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Result}. After fetching all data, the JDBC ResultSet will be
     * closed.
     * <p>
     * Use {@link #fetchLazy(ResultSet)}, to fetch one <code>Record</code> at a
     * time, instead of load the entire <code>ResultSet</code> into a jOOQ
     * <code>Result</code> at once.
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final Result<Record> fetch(ResultSet rs) throws DataAccessException {
        return fetchLazy(rs).fetch();
    }

    /**
     * Fetch a record from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Record}. This will internally fetch all records and throw an
     * exception if there was more than one resulting record.
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @return The resulting jOOQ record
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    public final Record fetchOne(ResultSet rs) throws DataAccessException, InvalidResultException {
        return filterOne(fetchLazy(rs).fetch());
    }

    /**
     * Wrap a JDBC {@link ResultSet} into a jOOQ {@link Cursor}.
     * <p>
     * Use {@link #fetch(ResultSet)}, to load the entire <code>ResultSet</code>
     * into a jOOQ <code>Result</code> at once.
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final Cursor<Record> fetchLazy(ResultSet rs) throws DataAccessException {
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
     * Fetch all data from a CSV string.
     * <p>
     * This is the same as calling <code>fetchFromCSV(string, ',')</code> and
     * the inverse of calling {@link Result#formatCSV()}. The first row of the
     * CSV data is required to hold field name information. Subsequent rows may
     * contain data, which is interpreted as {@link String}. Use the various
     * conversion methods to retrieve other data types from the
     * <code>Result</code>:
     * <ul>
     * <li> {@link Result#getValues(Field, Class)}</li>
     * <li> {@link Result#getValues(int, Class)}</li>
     * <li> {@link Result#getValues(String, Class)}</li>
     * <li> {@link Result#getValues(Field, Converter)}</li>
     * <li> {@link Result#getValues(int, Converter)}</li>
     * <li> {@link Result#getValues(String, Converter)}</li>
     * </ul>
     * <p>
     * Missing values result in <code>null</code>. Empty values result in empty
     * <code>Strings</code>
     *
     * @param string The CSV string
     * @return The transformed result
     * @throws DataAccessException If anything went wrong parsing the CSV file
     * @see #fetchFromCSV(String, char)
     */
    @Support
    public final Result<Record> fetchFromCSV(String string) throws DataAccessException {
        return fetchFromCSV(string, ',');
    }

    /**
     * Fetch all data from a CSV string.
     * <p>
     * This is inverse of calling {@link Result#formatCSV(char)}. The first row
     * of the CSV data is required to hold field name information. Subsequent
     * rows may contain data, which is interpreted as {@link String}. Use the
     * various conversion methods to retrieve other data types from the
     * <code>Result</code>:
     * <ul>
     * <li> {@link Result#getValues(Field, Class)}</li>
     * <li> {@link Result#getValues(int, Class)}</li>
     * <li> {@link Result#getValues(String, Class)}</li>
     * <li> {@link Result#getValues(Field, Converter)}</li>
     * <li> {@link Result#getValues(int, Converter)}</li>
     * <li> {@link Result#getValues(String, Converter)}</li>
     * </ul>
     * <p>
     * Missing values result in <code>null</code>. Empty values result in empty
     * <code>Strings</code>
     *
     * @param string The CSV string
     * @param delimiter The delimiter to expect between records
     * @return The transformed result
     * @throws DataAccessException If anything went wrong parsing the CSV file
     * @see #fetchFromCSV(String)
     */
    @Support
    public final Result<Record> fetchFromCSV(String string, char delimiter) throws DataAccessException {
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
                        Utils.setValue(record, fields.get(i), values[i]);
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
     * Create a new DSL select statement
     * <p>
     * Example: <code><pre>
     * SELECT * FROM [table] WHERE [conditions] ORDER BY [ordering] LIMIT [limit clause]
     * </pre></code>
     */
    @Support
    public final <R extends Record> SimpleSelectWhereStep<R> selectFrom(Table<R> table) {
        return new SimpleSelectImpl<R>(this, table);
    }

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link Executor}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link Factory#select(Collection)} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.select(fields)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see Factory#select(Collection)
     */
    @Support
    public final SelectSelectStep<Record> select(Collection<? extends Field<?>> fields) {
        SelectSelectStep<Record> result = Factory.select(fields);
        result.attach(this);
        return result;
    }

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link Executor}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link Factory#select(Field...)} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.select(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2)
     *       .execute();
     * </pre></code>
     *
     * @see Factory#select(Field...)
     */
    @Support
    public final SelectSelectStep<Record> select(Field<?>... fields) {
        SelectSelectStep<Record> result = Factory.select(fields);
        result.attach(this);
        return result;
    }

// [jooq-tools] START [select]

// [jooq-tools] END [select]

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link Executor}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link Factory#selectDistinct(Collection)} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.selectDistinct(fields)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see Factory#selectDistinct(Collection)
     */
    @Support
    public final SelectSelectStep<Record> selectDistinct(Collection<? extends Field<?>> fields) {
        SelectSelectStep<Record> result = Factory.selectDistinct(fields);
        result.attach(this);
        return result;
    }

    /**
     * Create a new DSL select statement.
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link Executor}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link Factory#selectDistinct(Field...)} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.selectDistinct(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     *
     * @see Factory#selectDistinct(Field...)
     */
    @Support
    public final SelectSelectStep<Record> selectDistinct(Field<?>... fields) {
        SelectSelectStep<Record> result = Factory.selectDistinct(fields);
        result.attach(this);
        return result;
    }

// [jooq-tools] START [selectDistinct]

// [jooq-tools] END [selectDistinct]

    /**
     * Create a new DSL select statement for constant <code>0</code> literal
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link Executor}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link Factory#selectZero()} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.selectZero()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2)
     *       .execute();
     * </pre></code>
     *
     * @see Factory#zero()
     * @see Factory#selectZero()
     */
    @Support
    public final SelectSelectStep<Record> selectZero() {
        SelectSelectStep<Record> result = (SelectSelectStep) Factory.selectZero();
        result.attach(this);
        return result;
    }

    /**
     * Create a new DSL select statement for constant <code>1</code> literal
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link Executor}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link Factory#selectOne()} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.selectOne()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2)
     *       .execute();
     * </pre></code>
     *
     * @see Factory#one()
     * @see Factory#selectOne()
     */
    @Support
    public final SelectSelectStep<Record> selectOne() {
        SelectSelectStep<Record> result = (SelectSelectStep) Factory.selectOne();
        result.attach(this);
        return result;
    }

    /**
     * Create a new DSL select statement for <code>COUNT(*)</code>
     * <p>
     * This creates an attached, renderable and executable <code>SELECT</code>
     * statement from this {@link Executor}. If you don't need to render or
     * execute this <code>SELECT</code> statement (e.g. because you want to
     * create a subselect), consider using the static
     * {@link Factory#selectCount()} instead.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.selectCount()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2)
     *       .execute();
     * </pre></code>
     *
     * @see Factory#selectCount()
     */
    @Support
    public final SelectSelectStep<Record> selectCount() {
        SelectSelectStep<Record> result = (SelectSelectStep) Factory.selectCount();
        result.attach(this);
        return result;
    }

    /**
     * Create a new {@link SelectQuery}
     */
    @Support
    public final SelectQuery selectQuery() {
        return new SelectQueryImpl(this);
    }

    /**
     * Create a new {@link SelectQuery}
     *
     * @param table The table to select data from
     * @return The new {@link SelectQuery}
     */
    @Support
    public final <R extends Record> SimpleSelectQuery<R> selectQuery(TableLike<R> table) {
        return new SimpleSelectQueryImpl<R>(this, table);
    }

    /**
     * Create a new {@link InsertQuery}
     *
     * @param into The table to insert data into
     * @return The new {@link InsertQuery}
     */
    @Support
    public final <R extends Record> InsertQuery<R> insertQuery(Table<R> into) {
        return new InsertQueryImpl<R>(this, into);
    }

    /**
     * Create a new DSL insert statement. This type of insert may feel more
     * convenient to some users, as it uses the <code>UPDATE</code> statement's
     * <code>SET a = b</code> syntax.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.insertInto(table)
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .newRecord()
     *       .set(field1, value3)
     *       .set(field2, value4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Support
    public final <R extends Record> InsertSetStep<R> insertInto(Table<R> into) {
        return new InsertImpl<R>(this, into, Collections.<Field<?>>emptyList());
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.insertInto(table, field1, field2)
     *       .values(value1, value2)
     *       .values(value3, value4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Support
    public final <R extends Record> InsertValuesStep<R> insertInto(Table<R> into, Field<?>... fields) {
        return new InsertImpl<R>(this, into, Arrays.asList(fields));
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.insertInto(table, field1, field2)
     *       .values(value1, value2)
     *       .values(value3, value4)
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    @Support
    public final <R extends Record> InsertValuesStep<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields) {
        return new InsertImpl<R>(this, into, fields);
    }

    /**
     * Create a new {@link UpdateQuery}
     *
     * @param table The table to update data into
     * @return The new {@link UpdateQuery}
     */
    @Support
    public final <R extends Record> UpdateQuery<R> updateQuery(Table<R> table) {
        return new UpdateQueryImpl<R>(this, table);
    }

    /**
     * Create a new DSL update statement.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.update(table)
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .where(field1.greaterThan(100))
     *       .execute();
     * </pre></code>
     */
    @Support
    public final <R extends Record> UpdateSetFirstStep<R> update(Table<R> table) {
        return new UpdateImpl<R>(this, table);
    }

    /**
     * Create a new DSL SQL standard MERGE statement.
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <th>dialect</th>
     * <th>support type</th>
     * <th>documentation</th>
     * </tr>
     * <tr>
     * <td>CUBRID</td>
     * <td>SQL:2008 standard and some enhancements</td>
     * <td><a href="http://www.cubrid.org/manual/90/en/MERGE"
     * >http://www.cubrid.org/manual/90/en/MERGE</a></td>
     * </tr>
     * <tr>
     * <tr>
     * <td>DB2</td>
     * <td>SQL:2008 standard and major enhancements</td>
     * <td><a href=
     * "http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.ibm.db2.udb.admin.doc/doc/r0010873.htm"
     * >http://publib.boulder.ibm.com/infocenter/db2luw/v9/index.jsp?topic=/com.
     * ibm.db2.udb.admin.doc/doc/r0010873.htm</a></td>
     * </tr>
     * <tr>
     * <td>HSQLDB</td>
     * <td>SQL:2008 standard</td>
     * <td><a
     * href="http://hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#N129BA"
     * >http://hsqldb.org/doc/2.0/guide/dataaccess-chapt.html#N129BA</a></td>
     * </tr>
     * <tr>
     * <td>Oracle</td>
     * <td>SQL:2008 standard and minor enhancements</td>
     * <td><a href=
     * "http://download.oracle.com/docs/cd/B28359_01/server.111/b28286/statements_9016.htm"
     * >http://download.oracle.com/docs/cd/B28359_01/server.111/b28286/
     * statements_9016.htm</a></td>
     * </tr>
     * <tr>
     * <td>SQL Server</td>
     * <td>Similar to SQL:2008 standard with some major enhancements</td>
     * <td><a href= "http://msdn.microsoft.com/de-de/library/bb510625.aspx"
     * >http://msdn.microsoft.com/de-de/library/bb510625.aspx</a></td>
     * </tr>
     * <tr>
     * <td>Sybase</td>
     * <td>Similar to SQL:2008 standard with some major enhancements</td>
     * <td><a href=
     * "http://dcx.sybase.com/1100/en/dbreference_en11/merge-statement.html"
     * >http://dcx.sybase.com/1100/en/dbreference_en11/merge-statement.html</a></td>
     * </tr>
     * </table>
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.mergeInto(table)
     *       .using(select)
     *       .on(condition)
     *       .whenMatchedThenUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .whenNotMatchedThenInsert(field1, field2)
     *       .values(value1, value2)
     *       .execute();
     * </pre></code>
     * <p>
     * Note: Using this method, you can also create an H2-specific MERGE
     * statement without field specification. See also
     * {@link #mergeInto(Table, Field...)}
     */
    @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    public final <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table) {
        return new MergeImpl<R>(this, table);
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     * <p>
     * This statement is available from DSL syntax only. It is known to be
     * supported in some way by any of these dialects:
     * <table border="1">
     * <tr>
     * <td>H2</td>
     * <td>H2 natively supports this special syntax</td>
     * <td><a href= "www.h2database.com/html/grammar.html#merge"
     * >www.h2database.com/html/grammar.html#merge</a></td>
     * </tr>
     * <tr>
     * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
     * <td>These databases can simulate the H2-specific MERGE statement using a
     * standard SQL MERGE statement, without restrictions</td>
     * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
     * </tr>
     * </table>
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    public final <R extends Record> MergeKeyStep<R> mergeInto(Table<R> table, Field<?>... fields) {
        return mergeInto(table, Arrays.asList(fields));
    }

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     *
     * @see #mergeInto(Table, Field...)
     */
    @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    public final <R extends Record> MergeKeyStep<R> mergeInto(Table<R> table, Collection<? extends Field<?>> fields) {
        return new MergeImpl<R>(this, table, fields);
    }

    /**
     * Create a new {@link DeleteQuery}
     *
     * @param table The table to delete data from
     * @return The new {@link DeleteQuery}
     */
    @Support
    public final <R extends Record> DeleteQuery<R> deleteQuery(Table<R> table) {
        return new DeleteQueryImpl<R>(this, table);
    }

    /**
     * Create a new DSL delete statement.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.delete(table)
     *       .where(field1.greaterThan(100))
     *       .execute();
     * </pre></code>
     */
    @Support
    public final <R extends Record> DeleteWhereStep<R> delete(Table<R> table) {
        return new DeleteImpl<R>(this, table);
    }

    // -------------------------------------------------------------------------
    // XXX Batch query execution
    // -------------------------------------------------------------------------

    /**
     * Execute a set of queries in batch mode (without bind values).
     * <p>
     * This essentially runs the following logic: <code><pre>
     * Statement s = connection.createStatement();
     *
     * for (Query query : queries) {
     *     s.addBatch(query.getSQL(true));
     * }
     *
     * s.execute();
     * </pre></code>
     *
     * @see Statement#executeBatch()
     */
    @Support
    public final Batch batch(Query... queries) {
        return new BatchMultiple(this, queries);
    }

    /**
     * Execute a set of queries in batch mode (without bind values).
     * <p>
     * This essentially runs the following logic: <code><pre>
     * Statement s = connection.createStatement();
     *
     * for (Query query : queries) {
     *     s.addBatch(query.getSQL(true));
     * }
     *
     * s.execute();
     * </pre></code>
     *
     * @see Statement#executeBatch()
     */
    @Support
    public final Batch batch(Collection<? extends Query> queries) {
        return batch(queries.toArray(new Query[queries.size()]));
    }

    /**
     * Execute a set of queries in batch mode (with bind values).
     * <p>
     * When running <code><pre>
     * create.batch(query)
     *       .bind(valueA1, valueA2)
     *       .bind(valueB1, valueB2)
     *       .execute();
     * </pre></code>
     * <p>
     * This essentially runs the following logic: <code><pre>
     * Statement s = connection.prepareStatement(query.getSQL(false));
     *
     * for (Object[] bindValues : allBindValues) {
     *     for (Object bindValue : bindValues) {
     *         s.setXXX(bindValue);
     *     }
     *
     *     s.addBatch();
     * }
     *
     * s.execute();
     * </pre></code>
     * <p>
     * Note: bind values will be inlined to a static batch query as in
     * {@link #batch(Query...)}, if you choose to execute queries with
     * <code>{@link Settings#getStatementType()} == {@link StatementType#STATIC_STATEMENT}</code>
     *
     * @see Statement#executeBatch()
     */
    @Support
    public final BatchBindStep batch(Query query) {
        return new BatchSingle(this, query);
    }

    /**
     * Execute a set of <code>INSERT</code> and <code>UPDATE</code> queries in
     * batch mode (with bind values).
     * <p>
     * This batch operation can be executed in two modes:
     * <h3>With
     * <code>{@link Settings#getStatementType()} == {@link StatementType#PREPARED_STATEMENT}</code>
     * (the default)</h3> In this mode, record order is preserved as much as
     * possible, as long as two subsequent records generate the same SQL (with
     * bind variables). The number of executed batch operations corresponds to
     * <code>[number of distinct rendered SQL statements]</code>. In the worst
     * case, this corresponds to the number of total records.
     * <p>
     * The record type order is preserved in the way they are passed to this
     * method. This is an example of how statements will be ordered: <code><pre>
     * // Let's assume, odd numbers result in INSERTs and even numbers in UPDATES
     * // Let's also assume a[n] are all of the same type, just as b[n], c[n]...
     * int[] result = create.batchStore(a1, a2, a3, b1, a4, c1, b3, a5)
     *                      .execute();
     * </pre></code> The above results in <code>result.length == 8</code> and
     * the following 4 separate batch statements:
     * <ol>
     * <li>INSERT a1, a3, a5</li>
     * <li>UPDATE a2, a4</li>
     * <li>INSERT b1, b3</li>
     * <li>INSERT c1</li>
     * </ol>
     * <h3>With
     * <code>{@link Settings#getStatementType()} == {@link StatementType#STATIC_STATEMENT}</code>
     * </h3> This mode may be better for large and complex batch store
     * operations, as the order of records is preserved entirely, and jOOQ can
     * guarantee that only a single batch statement is serialised to the
     * database.
     *
     * @see Statement#executeBatch()
     */
    @Support
    public final Batch batchStore(UpdatableRecord<?>... records) {
        return new BatchStore(this, records);
    }

    /**
     * Execute a set of <code>INSERT</code> and <code>UPDATE</code> queries in
     * batch mode (with bind values).
     *
     * @see #batchStore(UpdatableRecord...)
     * @see Statement#executeBatch()
     */
    @Support
    public final Batch batchStore(Collection<? extends UpdatableRecord<?>> records) {
        return batchStore(records.toArray(new UpdatableRecord[records.size()]));
    }

    // -------------------------------------------------------------------------
    // XXX DDL Statements
    // -------------------------------------------------------------------------

    /**
     * Create a new DSL truncate statement.
     * <p>
     * Example: <code><pre>
     * Executor create = new Executor();
     *
     * create.truncate(table)
     *       .execute();
     * </pre></code>
     * <p>
     * Most dialects implement the <code>TRUNCATE</code> statement. If it is not
     * supported, it is simulated using an equivalent <code>DELETE</code>
     * statement. This is particularly true for these dialects:
     * <ul>
     * <li> {@link SQLDialect#FIREBIRD}</li>
     * <li> {@link SQLDialect#INGRES}</li>
     * <li> {@link SQLDialect#SQLITE}</li>
     * </ul>
     * <p>
     * Note, this statement is only supported in DSL mode. Immediate execution
     * is omitted for future extensibility of this command.
     */
    @Support
    public final <R extends Record> Truncate<R> truncate(Table<R> table) {
        return new TruncateImpl<R>(this, table);
    }

    // -------------------------------------------------------------------------
    // XXX Other queries for identites and sequences
    // -------------------------------------------------------------------------

    /**
     * Retrieve the last inserted ID.
     * <p>
     * Note, there are some restrictions to the following dialects:
     * <ul>
     * <li> {@link SQLDialect#DB2} doesn't support this</li>
     * <li> {@link SQLDialect#ORACLE} doesn't support this</li>
     * <li> {@link SQLDialect#POSTGRES} doesn't support this</li>
     * <li> {@link SQLDialect#SQLITE} supports this, but its support is poorly
     * documented.</li>
     * </ul>
     *
     * @return The last inserted ID. This may be <code>null</code> in some
     *         dialects, if no such number is available.
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ ASE, CUBRID, DERBY, H2, HSQLDB, INGRES, MYSQL, SQLITE, SQLSERVER, SYBASE })
    public final BigInteger lastID() throws DataAccessException {
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
     * Convenience method to fetch the NEXTVAL for a sequence directly from this
     * {@link Executor}'s underlying JDBC {@link Connection}
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, ORACLE, POSTGRES, SYBASE })
    public final <T extends Number> T nextval(Sequence<T> sequence) {
        Field<T> nextval = sequence.nextval();
        return select(nextval).fetchOne(nextval);
    }

    /**
     * Convenience method to fetch the CURRVAL for a sequence directly from this
     * {@link Executor}'s underlying JDBC {@link Connection}
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ CUBRID, DB2, FIREBIRD, H2, INGRES, ORACLE, POSTGRES, SYBASE })
    public final <T extends Number> T currval(Sequence<T> sequence) throws DataAccessException {
        Field<T> currval = sequence.currval();
        return select(currval).fetchOne(currval);
    }

    /**
     * Use a schema as the default schema of the underlying connection.
     * <p>
     * This has two effects.
     * <ol>
     * <li>The <code>USE [schema]</code> statement is executed on those RDBMS
     * that support this</li>
     * <li>The supplied {@link Schema} is used as the default schema resulting
     * in omitting that schema in rendered SQL.</li>
     * </ol>
     * <p>
     * The <code>USE [schema]</code> statement translates to the various
     * dialects as follows:
     * <table>
     * <tr>
     * <th>Dialect</th>
     * <th>Command</th>
     * </tr>
     * <tr>
     * <td>DB2</td>
     * <td><code>SET SCHEMA [schema]</code></td>
     * </tr>
     * <tr>
     * <td>Derby:</td>
     * <td><code>SET SCHEMA [schema]</code></td>
     * </tr>
     * <tr>
     * <td>H2:</td>
     * <td><code>SET SCHEMA [schema]</code></td>
     * </tr>
     * <tr>
     * <td>HSQLDB:</td>
     * <td><code>SET SCHEMA [schema]</code></td>
     * </tr>
     * <tr>
     * <td>MySQL:</td>
     * <td><code>USE [schema]</code></td>
     * </tr>
     * <tr>
     * <td>Oracle:</td>
     * <td><code>ALTER SESSION SET CURRENT_SCHEMA = [schema]</code></td>
     * </tr>
     * <tr>
     * <td>Postgres:</td>
     * <td><code>SET SEARCH_PATH = [schema]</code></td>
     * </tr>
     * <tr>
     * <td>Sybase:</td>
     * <td><code>USE [schema]</code></td>
     * </tr>
     * </table>
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @SuppressWarnings("deprecation")
    @Support({ DB2, DERBY, H2, HSQLDB, MYSQL, SYBASE, ORACLE, POSTGRES, SYBASE })
    public final int use(Schema schema) throws DataAccessException {
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
     * Use a schema as the default schema of the underlying connection.
     *
     * @see #use(Schema)
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ DB2, DERBY, H2, HSQLDB, MYSQL, SYBASE, ORACLE, POSTGRES, SYBASE })
    public final int use(String schema) throws DataAccessException {
        return use(new SchemaImpl(schema));
    }

    // -------------------------------------------------------------------------
    // XXX Global Record factory
    // -------------------------------------------------------------------------

    /**
     * Create a new {@link UDTRecord}.
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @param <R> The generic record type
     * @param type The UDT describing records of type &lt;R&gt;
     * @return The new record
     */
    public final <R extends UDTRecord<R>> R newRecord(UDT<R> type) {
        return Utils.newRecord(type, this);
    }

    /**
     * Create a new {@link Record} that can be inserted into the corresponding
     * table.
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @param <R> The generic record type
     * @param table The table holding records of type &lt;R&gt;
     * @return The new record
     */
    public final <R extends TableRecord<R>> R newRecord(Table<R> table) {
        return Utils.newRecord(table, this);
    }

    /**
     * Create a new pre-filled {@link Record} that can be inserted into the
     * corresponding table.
     * <p>
     * This performs roughly the inverse operation of {@link Record#into(Class)}
     * <p>
     * The resulting record will have its internal "changed" flags set to true
     * for all values. This means that {@link UpdatableRecord#store()} will
     * perform an <code>INSERT</code> statement. If you wish to store the record
     * using an <code>UPDATE</code> statement, use
     * {@link #executeUpdate(UpdatableRecord)} instead.
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @param <R> The generic record type
     * @param table The table holding records of type &lt;R&gt;
     * @param source The source to be used to fill the new record
     * @return The new record
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see Record#from(Object)
     * @see Record#into(Class)
     */
    public final <R extends TableRecord<R>> R newRecord(Table<R> table, Object source) {
        R result = newRecord(table);
        result.from(source);
        return result;
    }

    // -------------------------------------------------------------------------
    // XXX Executing queries
    // -------------------------------------------------------------------------

    /**
     * Execute a {@link ResultQuery} in the context of this executor and return
     * results.
     *
     * @param query The query to execute
     * @return The result
     * @throws DataAccessException if something went wrong executing the query
     * @see ResultQuery#fetch()
     */
    public final <R extends Record> Result<R> fetch(ResultQuery<R> query) throws DataAccessException {
        final Configuration previous = Utils.getConfiguration(query);

        try {
            query.attach(this);
            return query.fetch();
        }
        finally {
            query.attach(previous);
        }
    }

    /**
     * Execute a {@link ResultQuery} in the context of this executor and return
     * a cursor.
     *
     * @param query The query to execute
     * @return The cursor
     * @throws DataAccessException if something went wrong executing the query
     * @see ResultQuery#fetchLazy()
     */
    public final <R extends Record> Cursor<R> fetchLazy(ResultQuery<R> query) throws DataAccessException {
        final Configuration previous = Utils.getConfiguration(query);

        try {
            query.attach(this);
            return query.fetchLazy();
        }
        finally {
            query.attach(previous);
        }
    }

    /**
     * Execute a {@link ResultQuery} in the context of this executor and return
     * a cursor.
     *
     * @param query The query to execute
     * @return The results
     * @throws DataAccessException if something went wrong executing the query
     * @see ResultQuery#fetchMany()
     */
    public final <R extends Record> List<Result<Record>> fetchMany(ResultQuery<R> query) throws DataAccessException {
        final Configuration previous = Utils.getConfiguration(query);

        try {
            query.attach(this);
            return query.fetchMany();
        }
        finally {
            query.attach(previous);
        }
    }

    /**
     * Execute a {@link ResultQuery} in the context of this executor and return
     * a record.
     *
     * @param query The query to execute
     * @return The record
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     * @see ResultQuery#fetchOne()
     */
    public final <R extends Record> R fetchOne(ResultQuery<R> query) throws DataAccessException, InvalidResultException {
        final Configuration previous = Utils.getConfiguration(query);

        try {
            query.attach(this);
            return query.fetchOne();
        }
        finally {
            query.attach(previous);
        }
    }

    /**
     * Execute a {@link Query} in the context of this executor.
     *
     * @param query The query to execute
     * @return The number of affected rows
     * @throws DataAccessException if something went wrong executing the query
     * @see Query#execute()
     */
    public final int execute(Query query) throws DataAccessException {
        final Configuration previous = Utils.getConfiguration(query);

        try {
            query.attach(this);
            return query.execute();
        }
        finally {
            query.attach(previous);
        }
    }

    // -------------------------------------------------------------------------
    // XXX Fast querying
    // -------------------------------------------------------------------------

    /**
     * Execute and return all records for
     * <code><pre>SELECT * FROM [table]</pre></code>
     * <p>
     * The result and its contained records are attached to this
     * {@link Configuration} by default. Use {@link Settings#isAttachRecords()}
     * to override this behaviour.
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final <R extends Record> Result<R> fetch(Table<R> table) throws DataAccessException {
        return fetch(table, trueCondition());
    }

    /**
     * Execute and return all records for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     * <p>
     * The result and its contained records are attached to this
     * {@link Configuration} by default. Use {@link Settings#isAttachRecords()}
     * to override this behaviour.
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final <R extends Record> Result<R> fetch(Table<R> table, Condition condition) throws DataAccessException {
        return selectFrom(table).where(condition).fetch();
    }

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table]</pre></code>
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    public final <R extends Record> R fetchOne(Table<R> table) throws DataAccessException, InvalidResultException {
        return filterOne(fetch(table));
    }

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     * @throws InvalidResultException if the query returned more than one record
     */
    @Support
    public final <R extends Record> R fetchOne(Table<R> table, Condition condition) throws DataAccessException, InvalidResultException {
        return filterOne(fetch(table, condition));
    }

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table] LIMIT 1</pre></code>
     * <p>
     * The resulting record is attached to this {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final <R extends Record> R fetchAny(Table<R> table) throws DataAccessException {
        return filterOne(selectFrom(table).limit(1).fetch());
    }

    /**
     * Insert one record
     * <p>
     * This executes something like the following statement:
     * <code><pre>INSERT INTO [table] ... VALUES [record] </pre></code>
     * <p>
     * Unlike {@link UpdatableRecord#store()}, this does not change any of the
     * argument <code>record</code>'s internal "changed" flags, such that a
     * subsequent call to {@link UpdatableRecord#store()} might lead to another
     * <code>INSERT</code> statement being executed.
     *
     * @return The number of inserted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final <R extends TableRecord<R>> int executeInsert(R record) throws DataAccessException {
        InsertQuery<R> insert = insertQuery(record.getTable());
        insert.setRecord(record);
        return insert.execute();
    }

    /**
     * Update a table
     * <code><pre>UPDATE [table] SET [modified values in record] WHERE [record is supplied record] </pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final <R extends UpdatableRecord<R>> int executeUpdate(R record) throws DataAccessException {
        UpdateQuery<R> update = updateQuery(record.getTable());
        Utils.addConditions(update, record, record.getTable().getMainKey().getFieldsArray());
        update.setRecord(record);
        return update.execute();
    }

    /**
     * Update a table
     * <code><pre>UPDATE [table] SET [modified values in record] WHERE [condition]</pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final <R extends TableRecord<R>, T> int executeUpdate(R record, Condition condition) throws DataAccessException {
        UpdateQuery<R> update = updateQuery(record.getTable());
        update.addConditions(condition);
        update.setRecord(record);
        return update.execute();
    }

    /**
     * Delete a record from a table
     * <code><pre>DELETE FROM [table] WHERE [record is supplied record]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final <R extends UpdatableRecord<R>> int executeDelete(R record) throws DataAccessException {
        DeleteQuery<R> delete = deleteQuery(record.getTable());
        Utils.addConditions(delete, record, record.getTable().getMainKey().getFieldsArray());
        return delete.execute();
    }

    /**
     * Delete a record from a table
     * <code><pre>DELETE FROM [table] WHERE [condition]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    public final <R extends TableRecord<R>, T> int executeDelete(R record, Condition condition) throws DataAccessException {
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

    private static <R extends Record> R filterOne(List<R> list) throws InvalidResultException {
        int size = list.size();

        if (size == 1) {
            return list.get(0);
        }
        else if (size > 1) {
            throw new InvalidResultException("Too many rows selected : " + size);
        }

        return null;
    }

    @Override
    public String toString() {
        return configuration.toString();
    }
}
