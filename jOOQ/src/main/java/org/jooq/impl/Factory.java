/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jooq.ArrayRecord;
import org.jooq.Attachable;
import org.jooq.Batch;
import org.jooq.BatchBindStep;
import org.jooq.BindContext;
import org.jooq.Case;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.ConfigurationRegistry;
import org.jooq.DataType;
import org.jooq.DeleteQuery;
import org.jooq.DeleteWhereStep;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.Identity;
import org.jooq.Insert;
import org.jooq.InsertQuery;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStep;
import org.jooq.LoaderOptionsStep;
import org.jooq.MergeUsingStep;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.SQLDialectNotSupportedException;
import org.jooq.Schema;
import org.jooq.SchemaMapping;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.SelectSelectStep;
import org.jooq.Sequence;
import org.jooq.SimpleSelectQuery;
import org.jooq.SimpleSelectWhereStep;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.TableRecord;
import org.jooq.Truncate;
import org.jooq.UDTRecord;
import org.jooq.UpdateQuery;
import org.jooq.UpdateSetStep;
import org.jooq.WindowPartitionByStep;
import org.jooq.exception.DetachedException;

/**
 * A factory providing implementations to the org.jooq interfaces
 * <p>
 * This factory is the main entry point for client code, to access jOOQ classes
 * and functionality. Here, you can instanciate all of those objects that cannot
 * be accessed through other objects. For example, to create a {@link Field}
 * representing a constant value, you can write:
 * <p>
 * <code><pre>
 * Field&lt;String&gt; field = new Factory().val("Hello World")
 * </pre></code>
 * <p>
 * Also, some SQL clauses cannot be expressed easily with DSL, for instance the
 * EXISTS clause, as it is not applied on a concrete object (yet). Hence you
 * should write
 * <p>
 * <code><pre>
 * Condition condition = new Factory().exists(new Factory().select(...));
 * </pre></code>
 * <p>
 * A <code>Factory</code> holds a reference to a JDBC {@link Connection} and
 * operates upon that connection. This means, that a <code>Factory</code> is
 * <i>not</i> thread-safe, since a JDBC Connection is not thread-safe either.
 *
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
public class Factory implements Configuration {

    /**
     * Generated UID
     */
    private static final long       serialVersionUID  = 2681360188806309513L;
    private static final JooqLogger log               = JooqLogger.getLogger(Factory.class);

    private static final Factory[]  DEFAULT_INSTANCES = new Factory[SQLDialect.values().length];

    private transient Connection    connection;
    private final SQLDialect        dialect;
    private final SchemaMapping     mapping;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a factory with connection and dialect configured
     *
     * @param connection The connection to use with objects created from this
     *            factory
     * @param dialect The dialect to use with objects created from this factory
     */
    public Factory(Connection connection, SQLDialect dialect) {
        this(connection, dialect, new SchemaMapping());
    }

    /**
     * Create a factory with connection, a dialect and a schema mapping
     * configured
     *
     * @param connection The connection to use with objects created from this
     *            factory
     * @param dialect The dialect to use with objects created from this factory
     * @param mapping The schema mapping to use with objects created from this
     *            factory
     */
    public Factory(Connection connection, SQLDialect dialect, SchemaMapping mapping) {
        this.connection = connection;
        this.dialect = dialect;
        this.mapping = mapping != null ? mapping : new SchemaMapping();
    }

    // -------------------------------------------------------------------------
    // Configuration API
    // -------------------------------------------------------------------------

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
    public final Connection getConnection() {
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SchemaMapping getSchemaMapping() {
        return mapping;
    }

    // -------------------------------------------------------------------------
    // Access to the loader API
    // -------------------------------------------------------------------------

    /**
     * Create a new <code>Loader</code> object to load data from a CSV or XML
     * source
     */
    public final <R extends TableRecord<R>> LoaderOptionsStep<R> loadInto(Table<R> table) {
        return new LoaderImpl<R>(this, table);
    }

    // -------------------------------------------------------------------------
    // RenderContext and BindContext accessors
    // -------------------------------------------------------------------------

    /**
     * Get a new {@link RenderContext} for the context of this factory
     * <p>
     * This will return an initialised render context as such:
     * <ul>
     * <li> <code>{@link RenderContext#declareFields()} == false</code></li>
     * <li> <code>{@link RenderContext#declareTables()} == false</code></li>
     * <li> <code>{@link RenderContext#inline()} == false</code></li>
     * </ul>
     * <p>
     * RenderContext for JOOQ INTERNAL USE only. Avoid referencing it directly
     */
    public final RenderContext renderContext() {
        return new DefaultRenderContext(this);
    }

    /**
     * Render a QueryPart in the context of this factory
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
     * Render a QueryPart in the context of this factory, inlining all bind
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
    public final int bind(QueryPart part, PreparedStatement stmt) throws SQLException {
        return bindContext(stmt).bind(part).peekIndex();
    }

    // -------------------------------------------------------------------------
    // Attachable and Serializable API
    // -------------------------------------------------------------------------

    /**
     * Attach this <code>Factory</code> to some attachables
     */
    public final void attach(Attachable... attachables) {
        attach(Arrays.asList(attachables));
    }

    /**
     * Attach this <code>Factory</code> to some attachables
     */
    public final void attach(Collection<Attachable> attachables) {
        for (Attachable attachable : attachables) {
            attachable.attach(this);
        }
    }

    // -------------------------------------------------------------------------
    // Conversion of objects into tables
    // -------------------------------------------------------------------------

    /**
     * A synonym for {@link #unnest(List)}
     *
     * @see #unnest(List)
     */
    public final Table<?> table(List<?> list) {
        return table(list.toArray());
    }

    /**
     * A synonym for {@link #unnest(Object[])}
     *
     * @see #unnest(Object[])
     */
    public final Table<?> table(Object[] array) {
        return table(val(array));
    }

    /**
     * A synonym for {@link #unnest(ArrayRecord)}
     *
     * @see #unnest(ArrayRecord)
     */
    public final Table<?> table(ArrayRecord<?> array) {
        return table(val(array));
    }

    /**
     * A synonym for {@link #unnest(Field)}
     *
     * @see #unnest(Field)
     */
    public final Table<?> table(Field<?> cursor) {
        return unnest(cursor);
    }

    /**
     * Create a table from a list of values
     * <p>
     * This is equivalent to the <code>TABLE</code> function for H2, or the
     * <code>UNNEST</code> function in HSQLDB and Postgres
     * <p>
     * For Oracle, use {@link #table(ArrayRecord)} instead, as Oracle knows only
     * typed arrays
     */
    public final Table<?> unnest(List<?> list) {
        return table(list.toArray());
    }

    /**
     * Create a table from an array of values
     * <p>
     * This is equivalent to the <code>TABLE</code> function for H2,
     * or the <code>UNNEST</code> function in HSQLDB and Postgres
     * <p>
     * For Oracle, use {@link #table(ArrayRecord)} instead, as Oracle knows only
     * typed arrays
     */
    public final Table<?> unnest(Object[] array) {
        return table(val(array));
    }

    /**
     * Create a table from an array of values
     * <p>
     * This wraps the argument array in a <code>TABLE</code> function for
     * Oracle. Currently, only Oracle knows typed arrays
     */
    public final Table<?> unnest(ArrayRecord<?> array) {
        return table(val(array));
    }

    /**
     * Create a table from a field. The supplied field can have any of these
     * types:
     * <ul>
     * <li> {@link Result}: For <code>CURSOR</code> or <code>REF CURSOR</code>
     * fields, typically fetched from stored functions or from nested tables</li>
     * <li> {@link ArrayRecord}: For Oracle-style <code>VARRAY</code> types.</li>
     * <li> {@link Object}[]: Array types, for other RDBMS's ARRAY types (e.g.
     * H2, HSQLDB, and Postgres)</li>
     * <li> {@link Object}: Any other type that jOOQ will try to convert in an
     * array first, before converting that array into a table</li>
     * </ul>
     * <p>
     * This functionality has only limited scope when used in H2, as ARRAY types
     * involved with stored functions can only be of type <code>Object[]</code>.
     * Such arrays are converted into <code>VARCHAR</code> arrays by jOOQ.
     */
    public final Table<?> unnest(Field<?> cursor) {
        if (cursor == null) {
            throw new IllegalArgumentException();
        }

        // The field is an actual CURSOR or REF CURSOR returned from a stored
        // procedure or from a NESTED TABLE
        else if (cursor.getType() == Result.class) {
            return new FunctionTable<Record>(cursor);
        }

        // The field is an Oracle-style VARRAY constant
        else if (ArrayConstant.class.isAssignableFrom(cursor.getClass())) {
            return new ArrayTable<Record>(cursor);
        }

        // The field is an Oracle-style VARRAY field
        else if (ArrayRecord.class.isAssignableFrom(cursor.getDataType().getType())) {
            return new ArrayTable<Record>(cursor);
        }

        // The field is a regular array
        else if (cursor.getType().isArray() && cursor.getType() != byte[].class) {
            return new ArrayTable<Record>(cursor);
        }

        // The field has any other type. Try to make it an array
        throw new SQLDialectNotSupportedException("Converting arbitrary types into array tables is currently not supported");
    }

    // -------------------------------------------------------------------------
    // Plain SQL object factory
    // -------------------------------------------------------------------------

    /**
     * A PlainSQLTable is a table that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex, but static subqueries or tables from different schemas.
     * <p>
     * Example
     * <p>
     * <code><pre>
     * String sql = "SELECT * FROM USER_TABLES WHERE OWNER = 'MY_SCHEMA'";
     * </pre></code>
     * <p>
     * The provided SQL must evaluate as a table whose type can be dynamically
     * discovered using JDBC's {@link ResultSetMetaData} methods. That way, you
     * can be sure that calling methods, such as {@link Table#getFields()} will
     * list the actual fields returned from your result set.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A table wrapping the plain SQL
     */
    public final Table<Record> table(String sql) {
        return table(sql, new Object[0]);
    }

    /**
     * A PlainSQLTable is a table that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex, but static subqueries or tables from different schemas.
     * There must be as many binding variables contained in the SQL, as passed
     * in the bindings parameter
     * <p>
     * Example
     * <p>
     * <code><pre>
     * String sql = "SELECT * FROM USER_TABLES WHERE OWNER = ?";
     * Object[] bindings = new Object[] { "MY_SCHEMA" };
     * </pre></code>
     * <p>
     * The provided SQL must evaluate as a table whose type can be dynamically
     * discovered using JDBC's {@link ResultSetMetaData} methods. That way, you
     * can be sure that calling methods, such as {@link Table#getFields()} will
     * list the actual fields returned from your result set.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A table wrapping the plain SQL
     */
    public final Table<Record> table(String sql, Object... bindings) {
        return new SQLTable(this, sql, bindings);
    }

    /**
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must not be any binding
     * variables contained in the SQL.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, 1, 100, 200)";
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A field wrapping the plain SQL
     */
    public final Field<?> field(String sql) {
        return field(sql, new Object[0]);
    }

    /**
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must be as many binding
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, ?, ?, ?)";
     * Object[] bindings = new Object[] { 1, 100, 200 };</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings for the field
     * @return A field wrapping the plain SQL
     */
    public final Field<?> field(String sql, Object... bindings) {
        return field(sql, Object.class, bindings);
    }

    /**
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must not be any binding
     * variables contained in the SQL.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, 1, 100, 200)";
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param type The field type
     * @return A field wrapping the plain SQL
     */
    public final <T> Field<T> field(String sql, Class<T> type) {
        return field(sql, type, new Object[0]);
    }

    /**
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must be as many binding
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, ?, ?, ?)";
     * Object[] bindings = new Object[] { 1, 100, 200 };</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param type The field type
     * @param bindings The bindings for the field
     * @return A field wrapping the plain SQL
     */
    public final <T> Field<T> field(String sql, Class<T> type, Object... bindings) {
        return field(sql, getDataType(type), bindings);
    }

    /**
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must not be any binding
     * variables contained in the SQL.
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, 1, 100, 200)";
     * </pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param type The field type
     * @return A field wrapping the plain SQL
     */
    public final <T> Field<T> field(String sql, DataType<T> type) {
        return field(sql, type, new Object[0]);
    }

    /**
     * A PlainSQLField is a field that can contain user-defined plain SQL,
     * because sometimes it is easier to express things directly in SQL, for
     * instance complex proprietary functions. There must be as many binding
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "DECODE(MY_FIELD, ?, ?, ?)";
     * Object[] bindings = new Object[] { 1, 100, 200 };</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param type The field type
     * @param bindings The bindings for the field
     * @return A field wrapping the plain SQL
     */
    public final <T> Field<T> field(String sql, DataType<T> type, Object... bindings) {
        return new SQLField<T>(sql, type, bindings);
    }

    /**
     * <code>function()</code> can be used to access native functions that are
     * not yet or insufficiently supported by jOOQ
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    public final <T> Field<T> function(String name, Class<T> type, Field<?>... arguments) {
        return function(name, getDataType(type), arguments);
    }

    /**
     * <code>function()</code> can be used to access native functions that are
     * not yet or insufficiently supported by jOOQ
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     */
    public final <T> Field<T> function(String name, DataType<T> type, Field<?>... arguments) {
        return new Function<T>(name, type, arguments);
    }

    /**
     * Create a new condition holding plain SQL. There must not be any binding
     * variables contained in the SQL
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "(X = 1 and Y = 2)";</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @return A condition wrapping the plain SQL
     */
    public final Condition condition(String sql) {
        return condition(sql, new Object[0]);
    }

    /**
     * Create a new condition holding plain SQL. There must be as many binding
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example:
     * <p>
     * <code><pre>
     * String sql = "(X = ? and Y = ?)";
     * Object[] bindings = new Object[] { 1, 2 };</pre></code>
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param sql The SQL
     * @param bindings The bindings
     * @return A condition wrapping the plain SQL
     */
    public final Condition condition(String sql, Object... bindings) {
        return new SQLCondition(sql, bindings);
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
    public final Query query(String sql) {
        return query(sql, new Object[0]);
    }

    /**
     * Create a new query holding plain SQL. There must be as many binding
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
    public final Query query(String sql, Object... bindings) {
        return new SQLQuery(this, sql, bindings);
    }

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code>
     * Example (SQLite):
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
     * @return The results from the executed query
     */
    public final Result<Record> fetch(String sql) throws SQLException {
        return fetch(sql, new Object[0]);
    }

    /**
     * Execute a new query holding plain SQL. There must be as many binding
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code>
     * Example (SQLite):
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
    public final Result<Record> fetch(String sql, Object... bindings) throws SQLException {
        return new SQLResultQuery(this, sql, bindings).fetch();
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
     * @return The results from the executed query
     */
    public final List<Result<Record>> fetchMany(String sql) throws SQLException {
        return fetchMany(sql, new Object[0]);
    }

    /**
     * Execute a new query holding plain SQL, possibly returning several result
     * sets. There must be as many binding variables contained in the SQL, as
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
     * @return A query wrapping the plain SQL
     */
    public final List<Result<Record>> fetchMany(String sql, Object... bindings) throws SQLException {
        return new SQLResultQuery(this, sql, bindings).fetchMany();
    }

    /**
     * Execute a new query holding plain SQL.
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code>
     * Example (SQLite):
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
     * @return The results from the executed query
     * @throws SQLException if more than one record was found
     */
    public final Record fetchOne(String sql) throws SQLException {
        return fetchOne(sql, new Object[0]);
    }

    /**
     * Execute a new query holding plain SQL. There must be as many binding
     * variables contained in the SQL, as passed in the bindings parameter
     * <p>
     * Example (Postgres):
     * <p>
     * <code><pre>
     * String sql = "FETCH ALL IN \"<unnamed cursor 1>\"";</pre></code>
     * Example (SQLite):
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
     * @throws SQLException if more than one record was found
     */
    public final Record fetchOne(String sql, Object... bindings) throws SQLException {
        return new SQLResultQuery(this, sql, bindings).fetchOne();
    }

    /**
     * Fetch all data from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Result}. After fetching all data, the JDBC ResultSet will be
     * closed.
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @return The resulting jOOQ Result
     */
    public final Result<Record> fetch(ResultSet rs) throws SQLException {
        FieldProvider fields = new MetaDataFieldProvider(this, rs.getMetaData());
        return new CursorImpl<Record>(this, fields, rs).fetchResult();
    }

    // -------------------------------------------------------------------------
    // Global Condition factory
    // -------------------------------------------------------------------------

    /**
     * Return a <code>Condition</code> that will always evaluate to true
     */
    public final Condition trueCondition() {
        return new TrueCondition();
    }

    /**
     * Return a <code>Condition</code> that will always evaluate to false
     */
    public final Condition falseCondition() {
        return new FalseCondition();
    }

    /**
     * Create a not exists condition.
     * <p>
     * <code>EXISTS ([query])</code>
     */
    public final Condition exists(Select<?> query) {
        return new SelectQueryAsExistsCondition(query, ExistsOperator.EXISTS);
    }

    /**
     * Create a not exists condition.
     * <p>
     * <code>NOT EXISTS ([query])</code>
     */
    public final Condition notExists(Select<?> query) {
        return new SelectQueryAsExistsCondition(query, ExistsOperator.NOT_EXISTS);
    }

    // -------------------------------------------------------------------------
    // Global Query factory
    // -------------------------------------------------------------------------

    /**
     * Create a new DSL select statement
     * <p>
     * Example: <code><pre>
     * SELECT * FROM [table] WHERE [conditions] ORDER BY [ordering] LIMIT [limit clause]
     * </pre></code>
     */
    public final <R extends Record> SimpleSelectWhereStep<R> selectFrom(Table<R> table) {
        return new SimpleSelectImpl<R>(this, table);
    }

    /**
     * Create a new DSL select statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.select(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2)
     *       .execute();
     * </pre></code>
     */
    public final SelectSelectStep select(Field<?>... fields) {
        return new SelectImpl(this).select(fields);
    }

    /**
     * Create a new DSL select statement for constant <code>0</code> literal
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.selectZero()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2)
     *       .execute();
     * </pre></code>
     *
     * @see #zero()
     */
    public final SelectSelectStep selectZero() {
        return new SelectImpl(this).select(zero());
    }

    /**
     * Create a new DSL select statement for constant <code>1</code> literal
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.selectOne()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2)
     *       .execute();
     * </pre></code>
     *
     * @see #one()
     */
    public final SelectSelectStep selectOne() {
        return new SelectImpl(this).select(one());
    }

    /**
     * Create a new DSL select statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.selectDistinct(field1, field2)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     */
    public final SelectSelectStep selectDistinct(Field<?>... fields) {
        return new SelectImpl(this, true).select(fields);
    }

    /**
     * Create a new DSL select statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.select(fields)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     */
    public final SelectSelectStep select(Collection<? extends Field<?>> fields) {
        return new SelectImpl(this).select(fields);
    }

    /**
     * Create a new DSL select statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.selectDistinct(fields)
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2);
     * </pre></code>
     */
    public final SelectSelectStep selectDistinct(Collection<? extends Field<?>> fields) {
        return new SelectImpl(this, true).select(fields);
    }

    /**
     * Create a new {@link SelectQuery}
     */
    public final SelectQuery selectQuery() {
        return new SelectQueryImpl(this);
    }

    /**
     * Create a new {@link SelectQuery}
     *
     * @param table The table to select data from
     * @return The new {@link SelectQuery}
     */
    public final <R extends Record> SimpleSelectQuery<R> selectQuery(TableLike<R> table) {
        return new SimpleSelectQueryImpl<R>(this, table);
    }

    /**
     * Create a new {@link InsertQuery}
     *
     * @param into The table to insert data into
     * @return The new {@link InsertQuery}
     */
    public final <R extends TableRecord<R>> InsertQuery<R> insertQuery(Table<R> into) {
        return new InsertQueryImpl<R>(this, into);
    }

    /**
     * Create a new DSL insert statement. This type of insert may feel more
     * convenient to some users, as it uses the <code>UPDATE</code> statement's
     * <code>SET a = b</code> syntax.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
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
    public final <R extends TableRecord<R>> InsertSetStep insertInto(Table<R> into) {
        return new InsertImpl<R>(this, into, Collections.<Field<?>>emptyList());
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
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
    public final <R extends TableRecord<R>> InsertValuesStep insertInto(Table<R> into, Field<?>... fields) {
        return new InsertImpl<R>(this, into, Arrays.asList(fields));
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
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
    public final <R extends TableRecord<R>> InsertValuesStep insertInto(Table<R> into, Collection<? extends Field<?>> fields) {
        return new InsertImpl<R>(this, into, fields);
    }

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.insertInto(table, create.select(1))
     *       .onDuplicateKeyUpdate()
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .execute();
     * </pre></code>
     */
    public final <R extends TableRecord<R>> Insert insertInto(Table<R> into, Select<?> select) {
        return new InsertSelectQueryImpl<R>(this, into, select);
    }

    /**
     * Create a new {@link UpdateQuery}
     *
     * @param table The table to update data into
     * @return The new {@link UpdateQuery}
     */
    public final <R extends TableRecord<R>> UpdateQuery<R> updateQuery(Table<R> table) {
        return new UpdateQueryImpl<R>(this, table);
    }

    /**
     * Create a new DSL update statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.update(table)
     *       .set(field1, value1)
     *       .set(field2, value2)
     *       .where(field1.greaterThan(100))
     *       .execute();
     * </pre></code>
     */
    public final <R extends TableRecord<R>> UpdateSetStep update(Table<R> table) {
        return new UpdateImpl<R>(this, table);
    }

    /**
     * Create a new DSL merge statement.
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
     * Factory create = new Factory();
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
     */
    public final <R extends TableRecord<R>> MergeUsingStep mergeInto(Table<R> table) {
        return new MergeImpl<R>(this, table);
    }

    /**
     * Create a new {@link DeleteQuery}
     *
     * @param table The table to delete data from
     * @return The new {@link DeleteQuery}
     */
    public final <R extends TableRecord<R>> DeleteQuery<R> deleteQuery(Table<R> table) {
        return new DeleteQueryImpl<R>(this, table);
    }

    /**
     * Create a new DSL delete statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.delete(table)
     *       .where(field1.greaterThan(100))
     *       .execute();
     * </pre></code>
     */
    public final <R extends TableRecord<R>> DeleteWhereStep delete(Table<R> table) {
        return new DeleteImpl<R>(this, table);
    }

    /**
     * Execute a set of queries in batch mode (without bind values).
     * <p>
     * This essentially runs the following logic: <code><pre>
     * Statement s = connection.createStatement();
     *
     * for (Query query : queries) {
     *     s.addBatch(renderInlined(query));
     * }
     *
     * s.execute();
     * </pre></code>
     *
     * @see Statement#executeBatch()
     */
    public final Batch batch(Query... queries) {
        return new BatchMultiple(this, queries);
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
     * Statement s = connection.prepareStatement(render(query));
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
     *
     * @see Statement#executeBatch()
     */
    public final BatchBindStep batch(Query query) {
        return new BatchSingle(this, query);
    }

    // -------------------------------------------------------------------------
    // DDL Statements
    // -------------------------------------------------------------------------

    /**
     * Create a new DSL truncate statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.truncate(table)
     *       .execute();
     * </pre></code>
     * <p>
     * Note, this statement is only supported in DSL mode. Immediate execution
     * is omitted for future extensibility of this command.
     */
    public final <R extends TableRecord<R>> Truncate truncate(Table<R> table) {
        return new TruncateImpl<R>(this, table);
    }

    // -------------------------------------------------------------------------
    // Other queries for identites and sequences
    // -------------------------------------------------------------------------

    /**
     * Retrieve the last inserted ID.
     * <p>
     * This is poorly supported by {@link SQLDialect#SQLITE}<br/>
     * This is NOT supported by {@link SQLDialect#POSTGRES} and
     * {@link SQLDialect#ORACLE}
     *
     * @return The last inserted ID. This may be <code>null</code> in some
     *         dialects, if no such number is available.
     * @see #lastID(Identity)
     */
    public final BigInteger lastID() throws SQLException {
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
     * Retrieve the last inserted ID for a given {@link Identity}
     * <p>
     * This executes <code>SELECT max([id.field]) FROM [id.table]</code>
     * <p>
     * This is NOT supported by {@link SQLDialect#ORACLE}
     *
     * @return The last inserted ID. This may be <code>null</code> in some
     *         dialects, if no such number is available.
     * @deprecated - 1.6.6 - This is not a precise way of fetching generated
     *             identity values, because it assumes that:
     *             <ul>
     *             <li>identity values are increased by one</li> <li>
     *             transactional integrity is given (no race conditions
     *             occurred)</li>
     *             </ul>
     *             <p>
     *             Use <code>INSERT .. RETURNING</code> instead, in
     *             {@link InsertQuery#getReturned()}, or {@link #lastID()} if
     *             your RDBMS supports such a clause.
     */
    @Deprecated
    public final <T extends Number> T lastID(Identity<?, T> identity) throws SQLException {
        return select(identity.getField().max()).from(identity.getTable()).fetchOne(identity.getField().max());
    }

    /**
     * Convenience method to fetch the NEXTVAL for a sequence directly from this
     * {@link Factory}'s underlying JDBC {@link Connection}
     */
    public final BigInteger nextval(Sequence sequence) throws SQLException {
        Field<BigInteger> nextval = sequence.nextval();
        return select(nextval).fetchOne(nextval);
    }

    /**
     * Convenience method to fetch the CURRVAL for a sequence directly from this
     * {@link Factory}'s underlying JDBC {@link Connection}
     */
    public final BigInteger currval(Sequence sequence) throws SQLException {
        Field<BigInteger> currval = sequence.currval();
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
     * @throws SQLException
     * @throws DetachedException
     */
    public final int use(Schema schema) throws DetachedException, SQLException {
        int result = 0;

        // SQL Server does not support such a syntax
        try {
            String schemaName = schema.internalAPI(QueryPartInternal.class).toSQLReference(this, false);

            switch (dialect) {
                case DB2:
                case DERBY:
                case H2:
                case HSQLDB:
                    result = query("set schema " + schemaName).execute();
                    break;

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
            }
        }
        finally {
            mapping.use(mapping.map(schema));
        }

        return result;
    }

    /**
     * Use a schema as the default schema of the underlying connection.
     *
     * @see #use(Schema)
     */
    public final int use(String schema) throws DetachedException, SQLException {
        return use(new SchemaImpl(schema));
    }

    // -------------------------------------------------------------------------
    // Global Record factory
    // -------------------------------------------------------------------------

    /**
     * Create a new {@link Record} that can be inserted into the corresponding
     * table.
     *
     * @param <R> The generic record type
     * @param table The table holding records of type <R>
     * @return The new record
     */
    public final <R extends Record> R newRecord(Table<R> table) {
        return JooqUtil.newRecord(table, this);
    }

    // -------------------------------------------------------------------------
    // Global Field factory
    // -------------------------------------------------------------------------

    /**
     * Initialise a {@link Case} statement. Decode is used as a method name to
     * avoid name clashes with Java's reserved literal "case"
     *
     * @see Case
     */
    public final Case decode() {
        return new CaseImpl(this);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see Field#decode(Field, Field, Field[])
     */
    public final <Z, T> Field<Z> decode(T value, T search, Z result) {
        return val(value).decode(search, result);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see Field#decode(Field, Field, Field[])
     */
    public final <Z, T> Field<Z> decode(T value, T search, Z result, Object... more) {
        return val(value).decode(search, result, more);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see Field#decode(Field, Field, Field[])
     */
    public final <Z, T> Field<Z> decode(Field<T> value, Field<T> search, Field<Z> result) {
        return value.decode(search, result);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see Field#decode(Field, Field, Field[])
     */
    public final <Z, T> Field<Z> decode(Field<T> value, Field<T> search, Field<Z> result, Field<?>... more) {
        return value.decode(search, result, more);
    }

    /**
     * Cast a value to the type of another field.
     *
     * @param <T> The generic type of the cast field
     * @param value The value to cast
     * @param as The field whose type is used for the cast
     * @return The cast field
     */
    public final <T> Field<T> cast(Object value, Field<T> as) {
        return val(value).cast(as);
    }

    /**
     * Cast null to the type of another field.
     *
     * @param <T> The generic type of the cast field
     * @param as The field whose type is used for the cast
     * @return The cast field
     */
    public final <T> Field<T> castNull(Field<T> as) {
        return NULL().cast(as);
    }

    /**
     * Cast a value to another type
     *
     * @param <T> The generic type of the cast field
     * @param value The value to cast
     * @param type The type that is used for the cast
     * @return The cast field
     */
    public final <T> Field<T> cast(Object value, Class<? extends T> type) {
        return val(value).cast(type);
    }

    /**
     * Cast null to a type
     *
     * @param <T> The generic type of the cast field
     * @param type The type that is used for the cast
     * @return The cast field
     */
    public final <T> Field<T> castNull(DataType<T> type) {
        return NULL().cast(type);
    }

    /**
     * Cast a value to another type
     *
     * @param <T> The generic type of the cast field
     * @param value The value to cast
     * @param type The type that is used for the cast
     * @return The cast field
     */
    public final <T> Field<T> cast(Object value, DataType<T> type) {
        return val(value).cast(type);
    }

    /**
     * Cast null to a type
     *
     * @param <T> The generic type of the cast field
     * @param type The type that is used for the cast
     * @return The cast field
     */
    public final <T> Field<T> castNull(Class<? extends T> type) {
        return NULL().cast(type);
    }

    /**
     * Cast all fields that need casting
     *
     * @param <T> The generic field type
     * @param type The type to cast to
     * @param fields The fields to be cast to a uniform type
     * @return The cast fields
     */
    @SuppressWarnings("unchecked")
    final <T> Field<T>[] castAll(Class<? extends T> type, Field<?>... fields) {
        Field<?>[] castFields = new Field<?>[fields.length];

        for (int i = 0; i < fields.length; i++) {
            castFields[i] = fields[i].cast(type);
        }

        return (Field<T>[]) castFields;
    }

    /**
     * Get a constant value
     * <p>
     * This will be deprecated in the near future, for its verbosity. Use
     * {@link #val(Object, DataType)} instead.
     * <p>
     * jOOQ tries to derive the RDBMS {@link DataType} from the provided Java
     * type <code>&lt;T&gt;</code>. This may not always be accurate, which can
     * lead to problems in some strongly typed RDMBS (namely:
     * {@link SQLDialect#DERBY}, {@link SQLDialect#DB2}, {@link SQLDialect#H2},
     * {@link SQLDialect#HSQLDB}), especially when value is <code>null</code>.
     * <p>
     * If you need more type-safety, please use
     * {@link #constant(Object, DataType)} instead, and provide the precise
     * RDMBS-specific data type, that is needed.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @return A field representing the constant value
     * @deprecated - 1.6.3 [#757] - Use {@link #val(Object)} instead.
     */
    @Deprecated
    public final <T> Field<T> constant(T value) {
        return val(value);
    }

    /**
     * Get a constant value with an associated type, taken from a field
     * <p>
     * This will be deprecated in the near future, for its verbosity. Use
     * {@link #val(Object, DataType)} instead.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @param type The data type to enforce upon the value
     * @return A field representing the constant value
     * @see #constant(Object, DataType)
     * @deprecated - 1.6.3 [#757] - Use {@link #val(Object, Class)} instead.
     */
    @Deprecated
    public final <T> Field<T> constant(Object value, Class<? extends T> type) {
        return val(value, type);
    }

    /**
     * Get a constant value with an associated type, taken from a field
     * <p>
     * This will be deprecated in the near future, for its verbosity. Use
     * {@link #val(Object, DataType)} instead.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @param field The field whose data type to enforce upon the value
     * @return A field representing the constant value
     * @see #constant(Object, DataType)
     * @deprecated - 1.6.3 [#757] - Use {@link #val(Object, Field)} instead.
     */
    @Deprecated
    public final <T> Field<T> constant(Object value, Field<T> field) {
        return val(value, field);
    }

    /**
     * Get a constant value with an associated type
     * <p>
     * This will be deprecated in the near future, for its verbosity. Use
     * {@link #val(Object, DataType)} instead.
     * <p>
     * This will try to bind <code>value</code> as <code>type</code> in a
     * <code>PreparedStatement</code>. If <code>value</code> and
     * <code>type</code> are not compatible, jOOQ will first try to convert and
     * then to cast <code>value</code> to <code>type</code>.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @param type The data type to enforce upon the value
     * @return A field representing the constant value
     * @deprecated - 1.6.3 [#757] - Use {@link #val(Object, DataType)} instead.
     */
    @Deprecated
    public final <T> Field<T> constant(Object value, DataType<T> type) {
        return val(value, type);
    }

    /**
     * Get a list of constant values and fields
     *
     * @deprecated - 1.6.3 - This method causes issues when overloading. Use
     *             {@link #constants(Object...)} instead
     */
    @Deprecated
    public final List<Field<?>> constant(Object... values) {
        return constants(values);
    }

    /**
     * Get a list of constant values and fields
     * @deprecated - 1.6.3 [#757] - Use {@link #vals(Object...)} instead.
     */
    @Deprecated
    public final List<Field<?>> constants(Object... values) {
        return vals(values);
    }

    /**
     * Get a value
     * <p>
     * jOOQ tries to derive the RDBMS {@link DataType} from the provided Java
     * type <code>&lt;T&gt;</code>. This may not always be accurate, which can
     * lead to problems in some strongly typed RDMBS (namely:
     * {@link SQLDialect#DERBY}, {@link SQLDialect#DB2}, {@link SQLDialect#H2},
     * {@link SQLDialect#HSQLDB}), especially when value is <code>null</code>.
     * <p>
     * If you need more type-safety, please use
     * {@link #constant(Object, DataType)} instead, and provide the precise
     * RDMBS-specific data type, that is needed.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @return A field representing the constant value
     */
    @SuppressWarnings("unchecked")
    public final <T> Field<T> val(T value) {

        // null is intercepted immediately
        if (value == null) {
            return (Field<T>) NULL();
        }

        // Prevent errors due to type erasure and unchecked invocation
        else if (value instanceof Field<?>) {
            return (Field<T>) value;
        }

        // Default behaviour
        else {
            return (Field<T>) val(value, getDataType(value.getClass()));
        }
    }

    /**
     * Get a value with an associated type, taken from a field
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @param type The data type to enforce upon the value
     * @return A field representing the constant value
     * @see #constant(Object, DataType)
     */
    public final <T> Field<T> val(Object value, Class<? extends T> type) {
        return val(value, getDataType(type));
    }

    /**
     * Get a value with an associated type, taken from a field
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @param field The field whose data type to enforce upon the value
     * @return A field representing the constant value
     * @see #constant(Object, DataType)
     */
    public final <T> Field<T> val(Object value, Field<T> field) {
        return val(value, field.getDataType());
    }

    /**
     * Get a value with an associated type
     * <p>
     * This will try to bind <code>value</code> as <code>type</code> in a
     * <code>PreparedStatement</code>. If <code>value</code> and
     * <code>type</code> are not compatible, jOOQ will first try to convert and
     * then to cast <code>value</code> to <code>type</code>.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @param type The data type to enforce upon the value
     * @return A field representing the constant value
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final <T> Field<T> val(Object value, DataType<T> type) {

        // Prevent errors due to type erasure and unchecked invocation
        if (value instanceof Field<?>) {
            return (Field<T>) value;
        }

        // Advanced data types have dedicated constant types
        else if (value instanceof UDTRecord) {
            return new UDTConstant((UDTRecord) value);
        }
        else if (value instanceof ArrayRecord) {
            return new ArrayConstant((ArrayRecord) value);
        }

        // The default behaviour
        else {
            return new Constant<T>(type.convert(value), type);
        }
    }

    /**
     * Get a list of values and fields
     */
    public final List<Field<?>> vals(Object... values) {
        if (values == null) {
            throw new IllegalArgumentException("Cannot create a list of constants for null");
        }
        else {
            FieldList result = new FieldList();

            for (Object value : values) {

                // Fields can be mixed with constant values
                if (value instanceof Field<?>) {
                    result.add((Field<?>) value);
                }
                else {
                    result.add(val(value));
                }
            }

            return result;
        }
    }

    // -------------------------------------------------------------------------
    // Literals
    // -------------------------------------------------------------------------

    /**
     * Get a typed <code>Field</code> for a literal.
     * <p>
     * This is similar as calling {@link #field(String)}. A field
     * without bind variables will be generated.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param <T> The generic field type
     * @param literal The literal
     * @return The literal as a field
     */
    @SuppressWarnings("unchecked")
    public final <T> Field<T> literal(T literal) {
        if (literal == null) {
            return (Field<T>) NULL();
        }
        else {
            return literal(literal, (Class<T>) literal.getClass());
        }
    }

    /**
     * Get a typed <code>Field</code> for a literal.
     * <p>
     * This is similar as calling {@link #field(String)}. A field
     * without bind variables will be generated.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param <T> The generic field type
     * @param literal The literal
     * @param type The literal's data type
     * @return The literal as a field
     */
    public final <T> Field<T> literal(Object literal, Class<T> type) {
        return literal(literal, getDataType(type));
    }

    /**
     * Get a typed <code>Field</code> for a literal.
     * <p>
     * This is similar as calling {@link #field(String)}. A field
     * without bind variables will be generated.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @param <T> The generic field type
     * @param literal The literal
     * @param type The literal's data type
     * @return The literal as a field
     */
    @SuppressWarnings("unchecked")
    public final <T> Field<T> literal(Object literal, DataType<T> type) {
        if (literal == null) {
            return (Field<T>) NULL();
        }
        else {
            return field(literal.toString(), type);
        }
    }

    /**
     * Get the null field
     */
    final Field<?> NULL() {
        return field("null");
    }

    /**
     * A <code>0</code> literal.
     * <p>
     * This is useful for mathematical functions or for
     * <code>EXISTS (SELECT 0 ...)</code> or <code>PARTITION BY 0</code> clauses
     * and similar constructs. The <code>0</code> literal will not generate a
     * bind variable.
     *
     * @return A <code>0</code> literal as a <code>Field</code>
     */
    public final Field<Integer> zero() {
        return literal(0);
    }

    /**
     * A <code>1</code> literal.
     * <p>
     * This is useful for mathematical functions or for
     * <code>EXISTS (SELECT 1 ...)</code> or <code>PARTITION BY 1</code> clauses
     * and similar constructs. The <code>1</code> literal will not generate a
     * bind variable.
     *
     * @return A <code>1</code> literal as a <code>Field</code>
     */
    public final Field<Integer> one() {
        return literal(1);
    }

    /**
     * A <code>2</code> literal.
     * <p>
     * This is useful for mathematical functions. The <code>1</code> literal
     * will not generate a bind variable.
     *
     * @return A <code>2</code> literal as a <code>Field</code>
     */
    public final Field<Integer> two() {
        return literal(2);
    }

    /**
     * The <code>PI</code> literal.
     * <p>
     * This will be any of the following:
     * <ul>
     * <li>The underlying RDBMS' <code>PI</code> literal or <code>PI()</code> function</li>
     * <li>{@link Math#PI}</li>
     * </ul>
     */
    public final Field<BigDecimal> pi() {
        return new Pi();
    }

    /**
     * The <code>E</code> literal (Euler number)
     * <p>
     * This will be any of the following:
     * <ul>
     * <li>The underlying RDBMS' <code>E</code> literal or <code>E()</code> function</li>
     * <li>{@link Math#E}</li>
     * </ul>
     */
    public final Field<BigDecimal> e() {
        return new Euler();
    }

    // -------------------------------------------------------------------------
    // Aggregate and window functions
    // -------------------------------------------------------------------------

    /**
     * Get the count(*) function
     *
     * @see Field#count()
     * @see Field#countDistinct()
     */
    public final Field<Integer> count() {
        return field("*", Integer.class).count();
    }

    /**
     * The <code>count(*) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     *
     * @see Field#countOver()
     */
    public final WindowPartitionByStep<Integer> countOver() {
        return field("*", Integer.class).countOver();
    }

    /**
     * The <code>row_number() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public final WindowPartitionByStep<Integer> rowNumberOver() {
        return new WindowFunction<Integer>("row_number", SQLDataType.INTEGER);
    }

    /**
     * The <code>rank_over() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public final WindowPartitionByStep<Integer> rankOver() {
        return new WindowFunction<Integer>("rank", SQLDataType.INTEGER);
    }

    /**
     * The <code>dense_rank() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public final WindowPartitionByStep<Integer> denseRankOver() {
        return new WindowFunction<Integer>("dense_rank", SQLDataType.INTEGER);
    }

    /**
     * The <code>precent_rank() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public final WindowPartitionByStep<BigDecimal> percentRankOver() {
        return new WindowFunction<BigDecimal>("percent_rank", SQLDataType.NUMERIC);
    }

    /**
     * The <code>cume_dist() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public final WindowPartitionByStep<BigDecimal> cumeDistOver() {
        return new WindowFunction<BigDecimal>("cume_dist", SQLDataType.NUMERIC);
    }

    /**
     * The <code>ntile([number]) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public final WindowPartitionByStep<BigDecimal> ntile(int number) {
        return new WindowFunction<BigDecimal>("ntile", SQLDataType.NUMERIC, field("" + number, Integer.class));
    }

    // -------------------------------------------------------------------------
    // Pseudo fields and date time functions
    // -------------------------------------------------------------------------

    /**
     * Get the current_date() function
     * <p>
     * This translates into any dialect
     */
    public final Field<Date> currentDate() throws SQLDialectNotSupportedException {
        switch (getDialect()) {
            case ORACLE:
                return new Function<Date>("sysdate", SQLDataType.DATE);

            case DERBY:    // No break
            case HSQLDB:   // No break
            case INGRES:   // No break
            case POSTGRES: // No break
            case SQLITE:   // No break
                return field("current_date", Date.class);

            case SQLSERVER:
                return field("convert(date, current_timestamp)", Date.class);

            case SYBASE:
                return field("current date", Date.class);
        }

        return new Function<Date>("current_date", SQLDataType.DATE);
    }

    /**
     * Get the current_time() function
     * <p>
     * This translates into any dialect
     */
    public final Field<Time> currentTime() throws SQLDialectNotSupportedException {
        switch (getDialect()) {
            case ORACLE:
                return new Function<Time>("sysdate", SQLDataType.TIME);

            case DERBY:    // No break
            case HSQLDB:   // No break
            case INGRES:   // No break
            case POSTGRES: // No break
            case SQLITE:   // No break
                return field("current_time", Time.class);

            case SQLSERVER:
                return field("convert(time, current_timestamp)", Time.class);

            case SYBASE:
                return field("current time", Time.class);
        }

        return new Function<Time>("current_time", SQLDataType.TIME);
    }

    /**
     * Get the current_timestamp() function
     * <p>
     * This translates into any dialect
     */
    public final Field<Timestamp> currentTimestamp() {
        switch (getDialect()) {
            case ASE:
                return new Function<Timestamp>("current_bigdatetime", SQLDataType.TIMESTAMP);

            case ORACLE:
                return new Function<Timestamp>("sysdate", SQLDataType.TIMESTAMP);

            case DERBY:    // No break
            case HSQLDB:   // No break
            case INGRES:   // No break
            case POSTGRES: // No break
            case SQLITE:   // No break
            case SQLSERVER:
                return field("current_timestamp", Timestamp.class);

            case SYBASE:
                return field("current timestamp", Timestamp.class);
        }

        return new Function<Timestamp>("current_timestamp", SQLDataType.TIMESTAMP);
    }

    /**
     * Get the current_user() function
     * <p>
     * This translates into any dialect
     */
    public final Field<String> currentUser() {
        switch (getDialect()) {
            case ASE:
                return field("user", SQLDataType.VARCHAR);

            case ORACLE:
                return new Function<String>("user", SQLDataType.VARCHAR);

            case DERBY:     // No break
            case HSQLDB:    // No break
            case INGRES:    // No break
            case POSTGRES:  // No break
            case SQLSERVER: // No break
            case SQLITE:    // No break
            case SYBASE:
                return field("current_user", String.class);
        }

        return new Function<String>("current_user", SQLDataType.VARCHAR);
    }

    /**
     * Get the rand() function
     */
    public final Field<BigDecimal> rand() {
        switch (getDialect()) {
            case DERBY:
            case INGRES:
            case POSTGRES:
            case SQLITE:
                return new Function<BigDecimal>("random", SQLDataType.NUMERIC);

            case ORACLE:
                return field("dbms_random.random", BigDecimal.class);
        }

        return new Function<BigDecimal>("rand", SQLDataType.NUMERIC);
    }

    // -------------------------------------------------------------------------
    // Fast querying
    // -------------------------------------------------------------------------

    /**
     * Execute and return all records for
     * <code><pre>SELECT * FROM [table]</pre></code>
     */
    public final <R extends Record> Result<R> fetch(Table<R> table) throws SQLException {
        return fetch(table, trueCondition());
    }

    /**
     * Execute and return all records for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     */
    public final <R extends Record> Result<R> fetch(Table<R> table, Condition condition) throws SQLException {
        return selectFrom(table).where(condition).fetch();
    }

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table]</pre></code>
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws SQLException if more than one record was found
     */
    public final <R extends Record> R fetchOne(Table<R> table) throws SQLException {
        return filterOne(fetch(table));
    }

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws SQLException if more than one record was found
     */
    public final <R extends Record> R fetchOne(Table<R> table, Condition condition) throws SQLException {
        return filterOne(fetch(table, condition));
    }

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table] LIMIT 1</pre></code>
     *
     * @return The record or <code>null</code> if no record was returned
     */
    public final <R extends Record> R fetchAny(Table<R> table) throws SQLException {
        return filterOne(selectFrom(table).limit(1).fetch());
    }

    /**
     * Insert one record
     * <code><pre>INSERT INTO [table] ... VALUES [record] </pre></code>
     *
     * @return The number of inserted records
     */
    public final <R extends TableRecord<R>> int executeInsert(Table<R> table, R record) throws SQLException {
        InsertQuery<R> insert = insertQuery(table);
        insert.setRecord(record);
        return insert.execute();
    }

    /**
     * Update a table
     * <code><pre>UPDATE [table] SET [modified values in record] </pre></code>
     *
     * @return The number of updated records
     */
    public final <R extends TableRecord<R>> int executeUpdate(Table<R> table, R record) throws SQLException {
        return executeUpdate(table, record, trueCondition());
    }

    /**
     * Update a table
     * <code><pre>UPDATE [table] SET [modified values in record] WHERE [condition]</pre></code>
     *
     * @return The number of updated records
     */
    public final <R extends TableRecord<R>, T> int executeUpdate(Table<R> table, R record, Condition condition)
        throws SQLException {
        UpdateQuery<R> update = updateQuery(table);
        update.addConditions(condition);
        update.setRecord(record);
        return update.execute();
    }

    /**
     * Update one record in a table
     * <code><pre>UPDATE [table] SET [modified values in record]</pre></code>
     *
     * @return The number of updated records
     * @throws SQLException if more than one record was updated
     */
    public final <R extends TableRecord<R>> int executeUpdateOne(Table<R> table, R record) throws SQLException {
        return filterUpdateOne(executeUpdate(table, record));
    }

    /**
     * Update one record in a table
     * <code><pre>UPDATE [table] SET [modified values in record] WHERE [condition]</pre></code>
     *
     * @return The number of updated records
     * @throws SQLException if more than one record was updated
     */
    public final <R extends TableRecord<R>, T> int executeUpdateOne(Table<R> table, R record, Condition condition)
        throws SQLException {
        return filterUpdateOne(executeUpdate(table, record, condition));
    }

    /**
     * Delete records from a table <code><pre>DELETE FROM [table]</pre></code>
     *
     * @return The number of deleted records
     */
    public final <R extends TableRecord<R>> int executeDelete(Table<R> table) throws SQLException {
        return executeDelete(table, trueCondition());
    }

    /**
     * Delete records from a table
     * <code><pre>DELETE FROM [table] WHERE [condition]</pre></code>
     *
     * @return The number of deleted records
     */
    public final <R extends TableRecord<R>, T> int executeDelete(Table<R> table, Condition condition)
        throws SQLException {
        DeleteQuery<R> delete = deleteQuery(table);
        delete.addConditions(condition);
        return delete.execute();
    }

    /**
     * Delete one record in a table <code><pre>DELETE FROM [table]</pre></code>
     *
     * @return The number of deleted records
     * @throws SQLException if more than one record was deleted
     */
    public final <R extends TableRecord<R>> int executeDeleteOne(Table<R> table) throws SQLException {
        return executeDeleteOne(table, trueCondition());
    }

    /**
     * Delete one record in a table
     * <code><pre>DELETE FROM [table] WHERE [condition]</pre></code>
     *
     * @return The number of deleted records
     * @throws SQLException if more than one record was deleted
     */
    public final <R extends TableRecord<R>, T> int executeDeleteOne(Table<R> table, Condition condition)
        throws SQLException {
        DeleteQuery<R> delete = deleteQuery(table);
        delete.addConditions(condition);
        return filterDeleteOne(delete.execute());
    }

    /**
     * Get the default data type for the {@link Factory}'s underlying
     * {@link SQLDialect} and a given Java type.
     * <p>
     * This is a convenience method for calling
     * {@link FieldTypeHelper#getDataType(SQLDialect, Class)}
     *
     * @param <T> The generic type
     * @param type The Java type
     * @return The <code>Factory</code>'s underlying default data type.
     */
    public final <T> DataType<T> getDataType(Class<? extends T> type) {
        return FieldTypeHelper.getDataType(getDialect(), type);
    }

    // -------------------------------------------------------------------------
    // Internals
    // -------------------------------------------------------------------------

    private int filterDeleteOne(int i) throws SQLException {
        return filterOne(i, "deleted");
    }

    private int filterUpdateOne(int i) throws SQLException {
        return filterOne(i, "updated");
    }

    private int filterOne(int i, String action) throws SQLException {
        if (i <= 1) {
            return i;
        }
        else {
            throw new SQLException("Too many rows " + action + " : " + i);
        }
    }

    private <R extends Record> R filterOne(List<R> list) throws SQLException {
        if (list.size() == 0) {
            return null;
        }
        else if (list.size() == 1) {
            return list.get(0);
        }
        else {
            throw new SQLException("Too many rows returned : " + list.size());
        }
    }

    @Override
    public String toString() {
        return "Factory [connected=" + (connection != null) + ", dialect=" + dialect + ", mapping=" + mapping + "]";
    }

    static {
        for (SQLDialect dialect : SQLDialect.values()) {
            Factory.DEFAULT_INSTANCES[dialect.ordinal()] = new Factory(null, dialect);
        }
    }

    /**
     * Get a default <code>Factory</code> without a {@link Connection}
     */
    final static Factory getNewFactory(SQLDialect dialect) {
        return getNewFactory(DEFAULT_INSTANCES[dialect.ordinal()]);
    }

    /**
     * Get a default <code>Factory</code> without a {@link Connection}
     */
    final static Factory getStaticFactory(SQLDialect dialect) {
        return DEFAULT_INSTANCES[dialect.ordinal()];
    }

    /**
     * Get a default <code>Factory</code> with a {@link Connection}
     */
    final static Factory getNewFactory(Configuration configuration) {
        if (configuration == null) {
            return getNewFactory(DefaultConfiguration.DEFAULT_CONFIGURATION);
        }
        else {
            return new Factory(configuration.getConnection(), configuration.getDialect(), configuration.getSchemaMapping());
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

    // Serialisation magic.

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        if (log.isDebugEnabled()) {
            log.debug("Deserialising", this);
        }

        Configuration registered = ConfigurationRegistry.provideFor(this);
        if (registered != null) {
            connection = registered.getConnection();
        }

        if (log.isDebugEnabled()) {
            log.debug("Deserialised", this);
        }
    }
}
