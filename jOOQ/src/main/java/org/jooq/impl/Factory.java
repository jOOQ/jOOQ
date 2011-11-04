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
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jooq.AggregateFunction;
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
import org.jooq.FactoryOperations;
import org.jooq.Field;
import org.jooq.FieldProvider;
import org.jooq.Insert;
import org.jooq.InsertQuery;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStep;
import org.jooq.LoaderOptionsStep;
import org.jooq.MergeUsingStep;
import org.jooq.Query;
import org.jooq.QueryPart;
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
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowOverStep;
import org.jooq.exception.DataAccessException;

/**
 * A factory providing implementations to the org.jooq interfaces
 * <p>
 * This factory is the main entry point for client code, to access jOOQ classes
 * and functionality. Here, you can instanciate all of those objects that cannot
 * be accessed through other objects. For example, to create a {@link Field}
 * representing a constant value, you can write:
 * <p>
 * <code><pre>
 * Field&lt;String&gt; field = Factory.val("Hello World")
 * </pre></code>
 * <p>
 * Also, some SQL clauses cannot be expressed easily with DSL, for instance the
 * EXISTS clause, as it is not applied on a concrete object (yet). Hence you
 * should write
 * <p>
 * <code><pre>
 * Condition condition = Factory.exists(new Factory().select(...));
 * </pre></code>
 * <p>
 * A <code>Factory</code> holds a reference to a JDBC {@link Connection} and
 * operates upon that connection. This means, that a <code>Factory</code> is
 * <i>not</i> thread-safe, since a JDBC Connection is not thread-safe either.
 *
 * @author Lukas Eder
 */
public class Factory implements FactoryOperations {

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
    // Attachable and Serializable API
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
    // Access to the loader API
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> LoaderOptionsStep<R> loadInto(Table<R> table) {
        return new LoaderImpl<R>(this, table);
    }

    // -------------------------------------------------------------------------
    // Conversion of objects into tables
    // -------------------------------------------------------------------------

    /**
     * A synonym for {@link #unnest(List)}
     *
     * @see #unnest(List)
     */
    public static Table<?> table(List<?> list) {
        return table(list.toArray());
    }

    /**
     * A synonym for {@link #unnest(Object[])}
     *
     * @see #unnest(Object[])
     */
    public static Table<?> table(Object[] array) {
        return table(val(array));
    }

    /**
     * A synonym for {@link #unnest(ArrayRecord)}
     *
     * @see #unnest(ArrayRecord)
     */
    public static Table<?> table(ArrayRecord<?> array) {
        return table(val(array));
    }

    /**
     * A synonym for {@link #unnest(Field)}
     *
     * @see #unnest(Field)
     */
    public static Table<?> table(Field<?> cursor) {
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
    public static Table<?> unnest(List<?> list) {
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
    public static Table<?> unnest(Object[] array) {
        return table(val(array));
    }

    /**
     * Create a table from an array of values
     * <p>
     * This wraps the argument array in a <code>TABLE</code> function for
     * Oracle. Currently, only Oracle knows typed arrays
     */
    public static Table<?> unnest(ArrayRecord<?> array) {
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
    public static Table<?> unnest(Field<?> cursor) {
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
    public static Table<Record> table(String sql) {
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
    public static Table<Record> table(String sql, Object... bindings) {
        return new SQLTable(sql, bindings);
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
    public static Field<?> field(String sql) {
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
    public static Field<?> field(String sql, Object... bindings) {
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
    public static <T> Field<T> field(String sql, Class<T> type) {
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
    public static <T> Field<T> field(String sql, Class<T> type, Object... bindings) {
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
    public static <T> Field<T> field(String sql, DataType<T> type) {
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
    public static <T> Field<T> field(String sql, DataType<T> type, Object... bindings) {
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
    public static <T> Field<T> function(String name, Class<T> type, Field<?>... arguments) {
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
    public static <T> Field<T> function(String name, DataType<T> type, Field<?>... arguments) {
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
    public static Condition condition(String sql) {
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
    public static Condition condition(String sql, Object... bindings) {
        return new SQLCondition(sql, bindings);
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
    public final Result<Record> fetch(String sql) {
        return fetch(sql, new Object[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Result<Record> fetch(String sql, Object... bindings) {
        return new SQLResultQuery(this, sql, bindings).fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Result<Record>> fetchMany(String sql) {
        return fetchMany(sql, new Object[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Result<Record>> fetchMany(String sql, Object... bindings) {
        return new SQLResultQuery(this, sql, bindings).fetchMany();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Record fetchOne(String sql) {
        return fetchOne(sql, new Object[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Record fetchOne(String sql, Object... bindings) {
        return new SQLResultQuery(this, sql, bindings).fetchOne();
    }

    // -------------------------------------------------------------------------
    // JDBC convenience methods
    // -------------------------------------------------------------------------

    /**
     * Fetch all data from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Result}. After fetching all data, the JDBC ResultSet will be
     * closed.
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @return The resulting jOOQ Result
     */
    @Override
    public final Result<Record> fetch(ResultSet rs) {
        try {
            FieldProvider fields = new MetaDataFieldProvider(this, rs.getMetaData());
            return new CursorImpl<Record>(this, fields, rs).fetch();
        }
        catch (SQLException e) {
            throw JooqUtil.translate("Factory.fetch", null, e);
        }
    }

    // -------------------------------------------------------------------------
    // Global Condition factory
    // -------------------------------------------------------------------------

    /**
     * Return a <code>Condition</code> that will always evaluate to true
     */
    public static Condition trueCondition() {
        return new TrueCondition();
    }

    /**
     * Return a <code>Condition</code> that will always evaluate to false
     */
    public static Condition falseCondition() {
        return new FalseCondition();
    }

    /**
     * Create a not exists condition.
     * <p>
     * <code>EXISTS ([query])</code>
     */
    public static Condition exists(Select<?> query) {
        return new SelectQueryAsExistsCondition(query, ExistsOperator.EXISTS);
    }

    /**
     * Create a not exists condition.
     * <p>
     * <code>NOT EXISTS ([query])</code>
     */
    public static Condition notExists(Select<?> query) {
        return new SelectQueryAsExistsCondition(query, ExistsOperator.NOT_EXISTS);
    }

    // -------------------------------------------------------------------------
    // Global Query factory
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
    public final <R extends TableRecord<R>> InsertQuery<R> insertQuery(Table<R> into) {
        return new InsertQueryImpl<R>(this, into);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> InsertSetStep insertInto(Table<R> into) {
        return new InsertImpl<R>(this, into, Collections.<Field<?>>emptyList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> InsertValuesStep insertInto(Table<R> into, Field<?>... fields) {
        return new InsertImpl<R>(this, into, Arrays.asList(fields));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> InsertValuesStep insertInto(Table<R> into, Collection<? extends Field<?>> fields) {
        return new InsertImpl<R>(this, into, fields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> Insert insertInto(Table<R> into, Select<?> select) {
        return new InsertSelectQueryImpl<R>(this, into, select);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> UpdateQuery<R> updateQuery(Table<R> table) {
        return new UpdateQueryImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> UpdateSetStep update(Table<R> table) {
        return new UpdateImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> MergeUsingStep mergeInto(Table<R> table) {
        return new MergeImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> DeleteQuery<R> deleteQuery(Table<R> table) {
        return new DeleteQueryImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> DeleteWhereStep delete(Table<R> table) {
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
    public final BatchBindStep batch(Query query) {
        return new BatchSingle(this, query);
    }

    // -------------------------------------------------------------------------
    // DDL Statements
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> Truncate truncate(Table<R> table) {
        return new TruncateImpl<R>(this, table);
    }

    // -------------------------------------------------------------------------
    // Other queries for identites and sequences
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
    @Override
    public final int use(Schema schema) {
        int result = 0;

        // SQL Server does not support such a syntax
        try {
            String schemaName = render(schema);

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
     * {@inheritDoc}
     */
    @Override
    public final int use(String schema) {
        return use(new SchemaImpl(schema));
    }

    // -------------------------------------------------------------------------
    // Global Record factory
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends Record> R newRecord(Table<R> table) {
        return JooqUtil.newRecord(table, this);
    }

    // -------------------------------------------------------------------------
    // Global Field and Function factory
    // -------------------------------------------------------------------------

    /**
     * Initialise a {@link Case} statement. Decode is used as a method name to
     * avoid name clashes with Java's reserved literal "case"
     *
     * @see Case
     */
    public static Case decode() {
        return new CaseImpl();
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see Field#decode(Field, Field, Field[])
     */
    public static <Z, T> Field<Z> decode(T value, T search, Z result) {
        return val(value).decode(search, result);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see Field#decode(Field, Field, Field[])
     */
    public static <Z, T> Field<Z> decode(T value, T search, Z result, Object... more) {
        return val(value).decode(search, result, more);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see Field#decode(Field, Field, Field[])
     */
    public static <Z, T> Field<Z> decode(Field<T> value, Field<T> search, Field<Z> result) {
        return value.decode(search, result);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see Field#decode(Field, Field, Field[])
     */
    public static <Z, T> Field<Z> decode(Field<T> value, Field<T> search, Field<Z> result, Field<?>... more) {
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
    public static <T> Field<T> cast(Object value, Field<T> as) {
        return val(value).cast(as);
    }

    /**
     * Cast null to the type of another field.
     *
     * @param <T> The generic type of the cast field
     * @param as The field whose type is used for the cast
     * @return The cast field
     */
    public static <T> Field<T> castNull(Field<T> as) {
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
    public static <T> Field<T> cast(Object value, Class<? extends T> type) {
        return val(value).cast(type);
    }

    /**
     * Cast null to a type
     *
     * @param <T> The generic type of the cast field
     * @param type The type that is used for the cast
     * @return The cast field
     */
    public static <T> Field<T> castNull(DataType<T> type) {
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
    public static <T> Field<T> cast(Object value, DataType<T> type) {
        return val(value).cast(type);
    }

    /**
     * Cast null to a type
     *
     * @param <T> The generic type of the cast field
     * @param type The type that is used for the cast
     * @return The cast field
     */
    public static <T> Field<T> castNull(Class<? extends T> type) {
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
    static <T> Field<T>[] castAll(Class<? extends T> type, Field<?>... fields) {
        Field<?>[] castFields = new Field<?>[fields.length];

        for (int i = 0; i < fields.length; i++) {
            castFields[i] = fields[i].cast(type);
        }

        return (Field<T>[]) castFields;
    }

    // ------------------------------------------------------------------------
    // Construction of special grouping functions
    // ------------------------------------------------------------------------

    /**
     * Create a ROLLUP(field1, field2, .., fieldn) grouping field
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>MySQL (simulated using the GROUP BY .. WITH ROLLUP clause)</li>
     * <li>Oracle</li>
     * <li>SQL Server</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     * <p>
     * Please check the SQL Server documentation for a very nice explanation of
     * <code>CUBE</code>, <code>ROLLUP</code>, and <code>GROUPING SETS</code>
     * clauses in grouping contexts: <a
     * href="http://msdn.microsoft.com/en-US/library/bb522495.aspx"
     * >http://msdn.microsoft.com/en-US/library/bb522495.aspx</a>
     *
     * @param fields The fields that are part of the <code>ROLLUP</code>
     *            function
     * @return A field to be used in a <code>GROUP BY</code> clause
     */
    public static Field<?> rollup(Field<?>... fields) {
        return new Rollup(fields);
    }

    /**
     * Create a CUBE(field1, field2, .., fieldn) grouping field
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>Oracle</li>
     * <li>SQL Server</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     * <p>
     * Please check the SQL Server documentation for a very nice explanation of
     * <code>CUBE</code>, <code>ROLLUP</code>, and <code>GROUPING SETS</code>
     * clauses in grouping contexts: <a
     * href="http://msdn.microsoft.com/en-US/library/bb522495.aspx"
     * >http://msdn.microsoft.com/en-US/library/bb522495.aspx</a>
     *
     * @param fields The fields that are part of the <code>CUBE</code>
     *            function
     * @return A field to be used in a <code>GROUP BY</code> clause
     */
    public static Field<?> cube(Field<?>... fields) {
        return function("cube", Object.class, fields);
    }

    /**
     * Create a GROUPING SETS(field1, field2, .., fieldn) grouping field where
     * each grouping set only consists of a single field.
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>Oracle</li>
     * <li>SQL Server</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     * <p>
     * Please check the SQL Server documentation for a very nice explanation of
     * <code>CUBE</code>, <code>ROLLUP</code>, and <code>GROUPING SETS</code>
     * clauses in grouping contexts: <a
     * href="http://msdn.microsoft.com/en-US/library/bb522495.aspx"
     * >http://msdn.microsoft.com/en-US/library/bb522495.aspx</a>
     *
     * @param fields The fields that are part of the <code>GROUPING SETS</code>
     *            function
     * @return A field to be used in a <code>GROUP BY</code> clause
     */
    @SuppressWarnings("unchecked")
    public static Field<?> groupingSets(Field<?>... fields) {
        List<Field<?>>[] array = new List[fields.length];

        for (int i = 0; i < fields.length; i++) {
            array[i] = Arrays.<Field<?>>asList(fields[i]);
        }

        return groupingSets(array);
    }

    /**
     * Create a GROUPING SETS((field1a, field1b), (field2a), .., (fieldna,
     * fieldnb)) grouping field
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>Oracle</li>
     * <li>SQL Server</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     * <p>
     * Please check the SQL Server documentation for a very nice explanation of
     * <code>CUBE</code>, <code>ROLLUP</code>, and <code>GROUPING SETS</code>
     * clauses in grouping contexts: <a
     * href="http://msdn.microsoft.com/en-US/library/bb522495.aspx"
     * >http://msdn.microsoft.com/en-US/library/bb522495.aspx</a>
     *
     * @param fieldSets The fields that are part of the <code>GROUPING SETS</code>
     *            function
     * @return A field to be used in a <code>GROUP BY</code> clause
     */
    @SuppressWarnings("unchecked")
    public static Field<?> groupingSets(Field<?>[]... fieldSets) {
        List<Field<?>>[] array = new List[fieldSets.length];

        for (int i = 0; i < fieldSets.length; i++) {
            array[i] = Arrays.asList(fieldSets[i]);
        }

        return groupingSets(array);
    }

    /**
     * Create a GROUPING SETS((field1a, field1b), (field2a), .., (fieldna,
     * fieldnb)) grouping field
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>Oracle</li>
     * <li>SQL Server</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     * <p>
     * Please check the SQL Server documentation for a very nice explanation of
     * <code>CUBE</code>, <code>ROLLUP</code>, and <code>GROUPING SETS</code>
     * clauses in grouping contexts: <a
     * href="http://msdn.microsoft.com/en-US/library/bb522495.aspx"
     * >http://msdn.microsoft.com/en-US/library/bb522495.aspx</a>
     *
     * @param fieldSets The fields that are part of the <code>GROUPING SETS</code>
     *            function
     * @return A field to be used in a <code>GROUP BY</code> clause
     */
    public static Field<?> groupingSets(Collection<Field<?>>... fieldSets) {
        WrappedList[] array = new WrappedList[fieldSets.length];

        for (int i = 0; i < fieldSets.length; i++) {
            array[i] = new WrappedList(new FieldList(fieldSets[i]));
        }

        return new Function<Object>("grouping sets", SQLDataType.OTHER, array);
    }

    /**
     * Create a GROUPING(field) aggregation field to be used along with
     * <code>CUBE</code>, <code>ROLLUP</code>, and <code>GROUPING SETS</code>
     * groupings
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>DB2</li>
     * <li>Oracle</li>
     * <li>SQL Server</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     *
     * @param field The function argument
     * @return The <code>GROUPING</code> aggregation field
     * @see #cube(Field...)
     * @see #rollup(Field...)
     */
    public static Field<Integer> grouping(Field<?> field) {
        return function("grouping", Integer.class, field);
    }

    /**
     * Create a GROUPING_ID(field1, field2, .., fieldn) aggregation field to be
     * used along with <code>CUBE</code>, <code>ROLLUP</code>, and
     * <code>GROUPING SETS</code> groupings
     * <p>
     * This has been observed to work with the following databases:
     * <ul>
     * <li>Oracle</li>
     * <li>SQL Server</li>
     * </ul>
     *
     * @param fields The function arguments
     * @return The <code>GROUPING_ID</code> aggregation field
     * @see #cube(Field...)
     * @see #rollup(Field...)
     */
    public static Field<Integer> groupingId(Field<?>... fields) {
        return function("grouping_id", Integer.class, fields);
    }

    // ------------------------------------------------------------------------
    // Mathematical functions
    // ------------------------------------------------------------------------

    /**
     * Get the sign of a numeric field: sign(field)
     * <p>
     * This renders the sign function where available:
     * <code><pre>sign([this])</pre></code>
     * ... or simulates it elsewhere (without bind variables on values -1, 0, 1):
     * <code><pre>
     * CASE WHEN [this] > 0 THEN 1
     *      WHEN [this] < 0 THEN -1
     *      ELSE 0
     * END
     */
    public static Field<Integer> sign(Number value) {
        return sign(val(value));
    }

    /**
     * Get the sign of a numeric field: sign(field)
     * <p>
     * This renders the sign function where available:
     * <code><pre>sign([this])</pre></code>
     * ... or simulates it elsewhere (without bind variables on values -1, 0, 1):
     * <code><pre>
     * CASE WHEN [this] > 0 THEN 1
     *      WHEN [this] < 0 THEN -1
     *      ELSE 0
     * END
     */
    public static Field<Integer> sign(Field<? extends Number> field) {
        return new Sign(field);
    }

    /**
     * Get the absolute value of a numeric field: abs(field)
     * <p>
     * This renders the same on all dialects:
     * <code><pre>abs([this])</pre></code>
     */
    public static <T extends Number> Field<T> abs(T value) {
        return abs(val(value));
    }

    /**
     * Get the absolute value of a numeric field: abs(field)
     * <p>
     * This renders the same on all dialects:
     * <code><pre>abs([this])</pre></code>
     */
    public static <T extends Number> Field<T> abs(Field<T> field) {
        return function("abs", field.getDataType(), field);
    }

    /**
     * Get rounded value of a numeric field: round(field)
     * <p>
     * This renders the round function where available:
     * <code><pre>round([this]) or
     * round([this], 0)</pre></code>
     * ... or simulates it elsewhere using floor and ceil
     */
    public static <T extends Number> Field<T> round(T value) {
        return round(val(value));
    }

    /**
     * Get rounded value of a numeric field: round(field)
     * <p>
     * This renders the round function where available:
     * <code><pre>round([this]) or
     * round([this], 0)</pre></code>
     * ... or simulates it elsewhere using floor and ceil
     */
    public static <T extends Number> Field<T> round(Field<T> field) {
        return new Round<T>(field);
    }

    /**
     * Get rounded value of a numeric field: round(field, decimals)
     * <p>
     * This renders the round function where available:
     * <code><pre>round([this], [decimals])</pre></code>
     * ... or simulates it elsewhere using floor and ceil
     */
    public static <T extends Number> Field<T> round(T value, int decimals) {
        return round(val(value), decimals);
    }

    /**
     * Get rounded value of a numeric field: round(field, decimals)
     * <p>
     * This renders the round function where available:
     * <code><pre>round([this], [decimals])</pre></code>
     * ... or simulates it elsewhere using floor and ceil
     */
    public static <T extends Number> Field<T> round(Field<T> field, int decimals) {
        return new Round<T>(field, decimals);
    }

    /**
     * Get the largest integer value not greater than [this]
     * <p>
     * This renders the floor function where available:
     * <code><pre>floor([this])</pre></code>
     * ... or simulates it elsewhere using round:
     * <code><pre>round([this] - 0.499999999999999)</pre></code>
     */
    public static <T extends Number> Field<T> floor(T value) {
        return floor(val(value));
    }

    /**
     * Get the largest integer value not greater than [this]
     * <p>
     * This renders the floor function where available:
     * <code><pre>floor([this])</pre></code>
     * ... or simulates it elsewhere using round:
     * <code><pre>round([this] - 0.499999999999999)</pre></code>
     */
    public static <T extends Number> Field<T> floor(Field<T> field) {
        return new Floor<T>(field);
    }

    /**
     * Get the smallest integer value not less than [this]
     * <p>
     * This renders the ceil or ceiling function where available:
     * <code><pre>ceil([this]) or
     * ceiling([this])</pre></code>
     * ... or simulates it elsewhere using round:
     * <code><pre>round([this] + 0.499999999999999)</pre></code>
     */
    public static <T extends Number> Field<T> ceil(T value) {
        return ceil(val(value));
    }

    /**
     * Get the smallest integer value not less than [this]
     * <p>
     * This renders the ceil or ceiling function where available:
     * <code><pre>ceil([this]) or
     * ceiling([this])</pre></code>
     * ... or simulates it elsewhere using round:
     * <code><pre>round([this] + 0.499999999999999)</pre></code>
     */
    public static <T extends Number> Field<T> ceil(Field<T> field) {
        return new Ceil<T>(field);
    }

    /**
     * Get the sqrt(field) function
     * <p>
     * This renders the sqrt function where available:
     * <code><pre>sqrt([this])</pre></code> ... or simulates it elsewhere using
     * power (which in turn may also be simulated using ln and exp functions):
     * <code><pre>power([this], 0.5)</pre></code>
     */
    public static Field<BigDecimal> sqrt(Number value) {
        return sqrt(val(value));
    }

    /**
     * Get the sqrt(field) function
     * <p>
     * This renders the sqrt function where available:
     * <code><pre>sqrt([this])</pre></code> ... or simulates it elsewhere using
     * power (which in turn may also be simulated using ln and exp functions):
     * <code><pre>power([this], 0.5)</pre></code>
     */
    public static Field<BigDecimal> sqrt(Field<? extends Number> field) {
        return new Sqrt(field);
    }

    /**
     * Get the exp(field) function, taking this field as the power of e
     * <p>
     * This renders the same on all dialects:
     * <code><pre>exp([this])</pre></code>
     */
    public static Field<BigDecimal> exp(Number value) {
        return exp(val(value));
    }

    /**
     * Get the exp(field) function, taking this field as the power of e
     * <p>
     * This renders the same on all dialects:
     * <code><pre>exp([this])</pre></code>
     */
    public static Field<BigDecimal> exp(Field<? extends Number> field) {
        return function("exp", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the ln(field) function, taking the natural logarithm of this field
     * <p>
     * This renders the ln or log function where available:
     * <code><pre>ln([this]) or
     * log([this])</pre></code>
     */
    public static Field<BigDecimal> ln(Number value) {
        return ln(val(value));
    }

    /**
     * Get the ln(field) function, taking the natural logarithm of this field
     * <p>
     * This renders the ln or log function where available:
     * <code><pre>ln([this]) or
     * log([this])</pre></code>
     */
    public static Field<BigDecimal> ln(Field<? extends Number> field) {
        return new Ln(field);
    }

    /**
     * Get the log(field, base) function
     * <p>
     * This renders the log function where available:
     * <code><pre>log([this])</pre></code> ... or simulates it elsewhere (in
     * most RDBMS) using the natural logarithm:
     * <code><pre>ln([this]) / ln([base])</pre></code>
     */
    public static Field<BigDecimal> log(Number value, int base) {
        return log(val(value), base);
    }

    /**
     * Get the log(field, base) function
     * <p>
     * This renders the log function where available:
     * <code><pre>log([this])</pre></code> ... or simulates it elsewhere (in
     * most RDBMS) using the natural logarithm:
     * <code><pre>ln([this]) / ln([base])</pre></code>
     */
    public static Field<BigDecimal> log(Field<? extends Number> field, int base) {
        return new Ln(field, base);
    }

    /**
     * Get the power(field, exponent) function
     * <p>
     * This renders the power function where available:
     * <code><pre>power([this], [exponent])</pre></code> ... or simulates it
     * elsewhere using ln and exp:
     * <code><pre>exp(ln([this]) * [exponent])</pre></code>
     */
    public static Field<BigDecimal> power(Number value, Number exponent) {
        return power(val(value), val(exponent));
    }

    /**
     * Get the power(field, exponent) function
     * <p>
     * This renders the power function where available:
     * <code><pre>power([this], [exponent])</pre></code> ... or simulates it
     * elsewhere using ln and exp:
     * <code><pre>exp(ln([this]) * [exponent])</pre></code>
     */
    public static Field<BigDecimal> power(Field<? extends Number> field, Number exponent) {
        return power(field, val(exponent));
    }

    /**
     * Get the power(field, exponent) function
     * <p>
     * This renders the power function where available:
     * <code><pre>power([this], [exponent])</pre></code> ... or simulates it
     * elsewhere using ln and exp:
     * <code><pre>exp(ln([this]) * [exponent])</pre></code>
     */
    public static Field<BigDecimal> power(Number value, Field<? extends Number> exponent) {
        return power(val(value), exponent);
    }

    /**
     * Get the power(field, exponent) function
     * <p>
     * This renders the power function where available:
     * <code><pre>power([this], [exponent])</pre></code> ... or simulates it
     * elsewhere using ln and exp:
     * <code><pre>exp(ln([this]) * [exponent])</pre></code>
     */
    public static Field<BigDecimal> power(Field<? extends Number> field, Field<? extends Number> exponent) {
        return new Power(field, exponent);
    }

    /**
     * Get the arc cosine(field) function
     * <p>
     * This renders the acos function where available:
     * <code><pre>acos([this])</pre></code>
     */
    public static Field<BigDecimal> acos(Number value) {
        return acos(val(value));
    }

    /**
     * Get the arc cosine(field) function
     * <p>
     * This renders the acos function where available:
     * <code><pre>acos([this])</pre></code>
     */
    public static Field<BigDecimal> acos(Field<? extends Number> field) {
        return function("acos", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the arc sine(field) function
     * <p>
     * This renders the asin function where available:
     * <code><pre>asin([this])</pre></code>
     */
    public static Field<BigDecimal> asin(Number value) {
        return asin(val(value));
    }

    /**
     * Get the arc sine(field) function
     * <p>
     * This renders the asin function where available:
     * <code><pre>asin([this])</pre></code>
     */
    public static Field<BigDecimal> asin(Field<? extends Number> field) {
        return function("asin", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the arc tangent(field) function
     * <p>
     * This renders the atan function where available:
     * <code><pre>atan([this])</pre></code>
     */
    public static Field<BigDecimal> atan(Number value) {
        return atan(val(value));
    }

    /**
     * Get the arc tangent(field) function
     * <p>
     * This renders the atan function where available:
     * <code><pre>atan([this])</pre></code>
     */
    public static Field<BigDecimal> atan(Field<? extends Number> field) {
        return function("atan", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the arc tangent 2(field, y) function
     * <p>
     * This renders the atan2 or atn2 function where available:
     * <code><pre>atan2([this]) or
     * atn2([this])</pre></code>
     */
    public static Field<BigDecimal> atan2(Number x, Number y) {
        return atan2(val(x), val(y));
    }

    /**
     * Get the arc tangent 2(field, y) function
     * <p>
     * This renders the atan2 or atn2 function where available:
     * <code><pre>atan2([this]) or
     * atn2([this])</pre></code>
     */
    public static Field<BigDecimal> atan2(Field<? extends Number> x, Number y) {
        return atan2(x, val(y));
    }

    /**
     * Get the arc tangent 2(field, y) function
     * <p>
     * This renders the atan2 or atn2 function where available:
     * <code><pre>atan2([this]) or
     * atn2([this])</pre></code>
     */
    public static Field<BigDecimal> atan2(Number x, Field<? extends Number> y) {
        return atan2(val(x), y);
    }

    /**
     * Get the arc tangent 2(field, y) function
     * <p>
     * This renders the atan2 or atn2 function where available:
     * <code><pre>atan2([this]) or
     * atn2([this])</pre></code>
     */
    public static Field<BigDecimal> atan2(Field<? extends Number> x, Field<? extends Number> y) {
        if (y == null) {
            return atan2(x, (Number) null);
        }

        return new Function<BigDecimal>(Term.ATAN2, SQLDataType.NUMERIC, x, y);
    }

    /**
     * Get the cosine(field) function
     * <p>
     * This renders the cos function where available:
     * <code><pre>cos([this])</pre></code>
     */
    public static Field<BigDecimal> cos(Number value) {
        return cos(val(value));
    }

    /**
     * Get the cosine(field) function
     * <p>
     * This renders the cos function where available:
     * <code><pre>cos([this])</pre></code>
     */
    public static Field<BigDecimal> cos(Field<? extends Number> field) {
        return function("cos", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the sine(field) function
     * <p>
     * This renders the sin function where available:
     * <code><pre>sin([this])</pre></code>
     */
    public static Field<BigDecimal> sin(Number value) {
        return sin(val(value));
    }

    /**
     * Get the sine(field) function
     * <p>
     * This renders the sin function where available:
     * <code><pre>sin([this])</pre></code>
     */
    public static Field<BigDecimal> sin(Field<? extends Number> field) {
        return function("sin", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the tangent(field) function
     * <p>
     * This renders the tan function where available:
     * <code><pre>tan([this])</pre></code>
     */
    public static Field<BigDecimal> tan(Number value) {
        return tan(val(value));
    }

    /**
     * Get the tangent(field) function
     * <p>
     * This renders the tan function where available:
     * <code><pre>tan([this])</pre></code>
     */
    public static Field<BigDecimal> tan(Field<? extends Number> field) {
        return function("tan", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the cotangent(field) function
     * <p>
     * This renders the cot function where available:
     * <code><pre>cot([this])</pre></code> ... or simulates it elsewhere using
     * sin and cos: <code><pre>cos([this]) / sin([this])</pre></code>
     */
    public static Field<BigDecimal> cot(Number value) {
        return cot(val(value));
    }

    /**
     * Get the cotangent(field) function
     * <p>
     * This renders the cot function where available:
     * <code><pre>cot([this])</pre></code> ... or simulates it elsewhere using
     * sin and cos: <code><pre>cos([this]) / sin([this])</pre></code>
     */
    public static Field<BigDecimal> cot(Field<? extends Number> field) {
        return new Cot(field);
    }

    /**
     * Get the hyperbolic sine function: sinh(field)
     * <p>
     * This renders the sinh function where available:
     * <code><pre>sinh([this])</pre></code> ... or simulates it elsewhere using
     * exp: <code><pre>(exp([this] * 2) - 1) / (exp([this] * 2))</pre></code>
     */
    public static Field<BigDecimal> sinh(Number value) {
        return sinh(val(value));
    }

    /**
     * Get the hyperbolic sine function: sinh(field)
     * <p>
     * This renders the sinh function where available:
     * <code><pre>sinh([this])</pre></code> ... or simulates it elsewhere using
     * exp: <code><pre>(exp([this] * 2) - 1) / (exp([this] * 2))</pre></code>
     */
    public static Field<BigDecimal> sinh(Field<? extends Number> field) {
        return new Sinh(field);
    }

    /**
     * Get the hyperbolic cosine function: cosh(field)
     * <p>
     * This renders the cosh function where available:
     * <code><pre>cosh([this])</pre></code> ... or simulates it elsewhere using
     * exp: <code><pre>(exp([this] * 2) + 1) / (exp([this] * 2))</pre></code>
     */
    public static Field<BigDecimal> cosh(Number value) {
        return cosh(val(value));
    }

    /**
     * Get the hyperbolic cosine function: cosh(field)
     * <p>
     * This renders the cosh function where available:
     * <code><pre>cosh([this])</pre></code> ... or simulates it elsewhere using
     * exp: <code><pre>(exp([this] * 2) + 1) / (exp([this] * 2))</pre></code>
     */
    public static Field<BigDecimal> cosh(Field<? extends Number> field) {
        return new Cosh(field);
    }

    /**
     * Get the hyperbolic tangent function: tanh(field)
     * <p>
     * This renders the tanh function where available:
     * <code><pre>tanh([this])</pre></code> ... or simulates it elsewhere using
     * exp:
     * <code><pre>(exp([this] * 2) - 1) / (exp([this] * 2) + 1)</pre></code>
     */
    public static Field<BigDecimal> tanh(Number value) {
        return tanh(val(value));
    }

    /**
     * Get the hyperbolic tangent function: tanh(field)
     * <p>
     * This renders the tanh function where available:
     * <code><pre>tanh([this])</pre></code> ... or simulates it elsewhere using
     * exp:
     * <code><pre>(exp([this] * 2) - 1) / (exp([this] * 2) + 1)</pre></code>
     */
    public static Field<BigDecimal> tanh(Field<? extends Number> field) {
        return new Tanh(field);
    }

    /**
     * Get the hyperbolic cotangent function: coth(field)
     * <p>
     * This is not supported by any RDBMS, but simulated using exp exp:
     * <code><pre>(exp([this] * 2) + 1) / (exp([this] * 2) - 1)</pre></code>
     */
    public static Field<BigDecimal> coth(Number value) {
        return coth(val(value));
    }

    /**
     * Get the hyperbolic cotangent function: coth(field)
     * <p>
     * This is not supported by any RDBMS, but simulated using exp exp:
     * <code><pre>(exp([this] * 2) + 1) / (exp([this] * 2) - 1)</pre></code>
     */
    public static Field<BigDecimal> coth(Field<? extends Number> field) {
        return exp(field.mul(2)).add(1).div(exp(field.mul(2)).sub(1));
    }

    /**
     * Calculate degrees from radians from this field
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([this])</pre></code> ... or simulates it elsewhere:
     * <code><pre>[this] * 180 / PI</pre></code>
     */
    public static Field<BigDecimal> deg(Number value) {
        return deg(val(value));
    }

    /**
     * Calculate degrees from radians from this field
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([this])</pre></code> ... or simulates it elsewhere:
     * <code><pre>[this] * 180 / PI</pre></code>
     */
    public static Field<BigDecimal> deg(Field<? extends Number> field) {
        return new Degrees(field);
    }

    /**
     * Calculate radians from degrees from this field
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([this])</pre></code> ... or simulates it elsewhere:
     * <code><pre>[this] * PI / 180</pre></code>
     */
    public static Field<BigDecimal> rad(Number value) {
        return rad(val(value));
    }

    /**
     * Calculate radians from degrees from this field
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([this])</pre></code> ... or simulates it elsewhere:
     * <code><pre>[this] * PI / 180</pre></code>
     */
    public static Field<BigDecimal> rad(Field<? extends Number> field) {
        return new Radians(field);
    }

    // -------------------------------------------------------------------------
    // Aggregate functions
    // -------------------------------------------------------------------------

    /**
     * Get the count(*) function
     */
    public static AggregateFunction<Integer> count() {
        return new Count(field("*", Integer.class), false);
    }

    /**
     * Get the count(field) function
     */
    public static AggregateFunction<Integer> count(Field<?> field) {
        return new Count(field, false);
    }

    /**
     * Get the count(distinct field) function
     */
    public static AggregateFunction<Integer> countDistinct(Field<?> field) {
        return new Count(field, true);
    }

    /**
     * Get the max value over a field: max(field)
     */
    public static <T> AggregateFunction<T> max(Field<T> field) {
        return new AggregateFunctionImpl<T>("max", field.getDataType(), field);
    }

    /**
     * Get the min value over a field: min(field)
     */
    public static <T> AggregateFunction<T> min(Field<T> field) {
        return new AggregateFunctionImpl<T>("min", field.getDataType(), field);
    }

    /**
     * Get the sum over a numeric field: sum(field)
     */
    public static AggregateFunction<BigDecimal> sum(Field<? extends Number> field) {
        return new AggregateFunctionImpl<BigDecimal>("sum", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the average over a numeric field: avg(field)
     */
    public static AggregateFunction<BigDecimal> avg(Field<? extends Number> field) {
        return new AggregateFunctionImpl<BigDecimal>("avg", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the median over a numeric field: median(field)
     * <p>
     * This is known to be supported in any of these RDBMS:
     * <ul>
     * <li>HSQLDB</li>
     * <li>Oracle</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     */
    public static AggregateFunction<BigDecimal> median(Field<? extends Number> field) {
        return new AggregateFunctionImpl<BigDecimal>("median", SQLDataType.NUMERIC, field);
    }

    /**
     * Get the population standard deviation of a numeric field: stddev_pop(field)
     * <p>
     * This is known to be supported in any of these RDBMS:
     * <ul>
     * <li>DB2</li>
     * <li>H2</li>
     * <li>HSQLDB</li>
     * <li>Ingres</li>
     * <li>MySQL</li>
     * <li>Oracle</li>
     * <li>Postgres</li>
     * <li>SQL Server (stdev)</li>
     * <li>Sybase ASE</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     */
    public static AggregateFunction<BigDecimal> stddevPop(Field<? extends Number> field) {
        return new AggregateFunctionImpl<BigDecimal>(Term.STDDEV_POP, SQLDataType.NUMERIC, field);
    }

    /**
     * Get the sample standard deviation of a numeric field: stddev_samp(field)
     * <p>
     * This is known to be supported in any of these RDBMS:
     * <ul>
     * <li>DB2</li>
     * <li>H2</li>
     * <li>HSQLDB</li>
     * <li>Ingres</li>
     * <li>MySQL</li>
     * <li>Oracle</li>
     * <li>Postgres</li>
     * <li>SQL Server (stdev)</li>
     * <li>Sybase ASE</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     */
    public static AggregateFunction<BigDecimal> stddevSamp(Field<? extends Number> field) {
        return new AggregateFunctionImpl<BigDecimal>(Term.STDDEV_SAMP, SQLDataType.NUMERIC, field);
    }

    /**
     * Get the population variance of a numeric field: var_pop(field)
     * <p>
     * This is known to be supported in any of these RDBMS:
     * <ul>
     * <li>DB2</li>
     * <li>H2</li>
     * <li>HSQLDB</li>
     * <li>Ingres</li>
     * <li>MySQL</li>
     * <li>Oracle</li>
     * <li>Postgres</li>
     * <li>SQL Server (stdev)</li>
     * <li>Sybase ASE</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     */
    public static AggregateFunction<BigDecimal> varPop(Field<? extends Number> field) {
        return new AggregateFunctionImpl<BigDecimal>(Term.VAR_POP, SQLDataType.NUMERIC, field);
    }

    /**
     * Get the sample variance of a numeric field: var_samp(field)
     * <p>
     * This is known to be supported in any of these RDBMS:
     * <ul>
     * <li>H2</li>
     * <li>HSQLDB</li>
     * <li>Ingres</li>
     * <li>MySQL</li>
     * <li>Oracle</li>
     * <li>Postgres</li>
     * <li>SQL Server (var)</li>
     * <li>Sybase SQL Anywhere</li>
     * </ul>
     */
    public static AggregateFunction<BigDecimal> varSamp(Field<? extends Number> field) {
        return new AggregateFunctionImpl<BigDecimal>(Term.VAR_SAMP, SQLDataType.NUMERIC, field);
    }

    // -------------------------------------------------------------------------
    // Window functions
    // -------------------------------------------------------------------------

    /**
     * The <code>row_number() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static WindowOverStep<Integer> rowNumber() {
        return new WindowFunction<Integer>("row_number", SQLDataType.INTEGER);
    }

    /**
     * The <code>rank_over() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static WindowOverStep<Integer> rank() {
        return new WindowFunction<Integer>("rank", SQLDataType.INTEGER);
    }

    /**
     * The <code>dense_rank() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static WindowOverStep<Integer> denseRank() {
        return new WindowFunction<Integer>("dense_rank", SQLDataType.INTEGER);
    }

    /**
     * The <code>precent_rank() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static WindowOverStep<BigDecimal> percentRank() {
        return new WindowFunction<BigDecimal>("percent_rank", SQLDataType.NUMERIC);
    }

    /**
     * The <code>cume_dist() over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static WindowOverStep<BigDecimal> cumeDist() {
        return new WindowFunction<BigDecimal>("cume_dist", SQLDataType.NUMERIC);
    }

    /**
     * The <code>ntile([number]) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static WindowOverStep<BigDecimal> ntile(int number) {
        return new WindowFunction<BigDecimal>("ntile", SQLDataType.NUMERIC, field("" + number, Integer.class));
    }

    /**
     * The <code>first_value(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> firstValue(Field<T> field) {
        return new WindowFunction<T>("first_value", field.getDataType(), field);
    }

    /**
     * The <code>last_value(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lastValue(Field<T> field) {
        return new WindowFunction<T>("last_value", field.getDataType(), field);
    }

    /**
     * The <code>lead(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field) {
        return new WindowFunction<T>("lead", field.getDataType(), field);
    }

    /**
     * The <code>lead(field, offset) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field, int offset) {
        return new WindowFunction<T>("lead", field.getDataType(), field, literal(offset));
    }

    /**
     * The
     * <code>lead(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field, int offset, T defaultValue) {
        return lead(field, offset, val(defaultValue));
    }

    /**
     * The
     * <code>lead(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field, int offset, Field<T> defaultValue) {
        if (defaultValue == null) {
            return lead(field, offset, (T) null);
        }

        return new WindowFunction<T>("lead", field.getDataType(), field, literal(offset), defaultValue);
    }

    /**
     * The <code>lag(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field) {
        return new WindowFunction<T>("lag", field.getDataType(), field);
    }

    /**
     * The <code>lag(field, offset) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field, int offset) {
        return new WindowFunction<T>("lag", field.getDataType(), field, literal(offset));
    }

    /**
     * The
     * <code>lag(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field, int offset, T defaultValue) {
        return lag(field, offset, val(defaultValue));
    }

    /**
     * The
     * <code>lag(field, offset, defaultValue) over ([analytic clause])</code>
     * function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field, int offset, Field<T> defaultValue) {
        if (defaultValue == null) {
            return lag(field, offset, (T) null);
        }

        return new WindowFunction<T>("lag", field.getDataType(), field, literal(offset), defaultValue);
    }

    // -------------------------------------------------------------------------
    // Bind values
    // -------------------------------------------------------------------------

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
     * {@link #val(Object, DataType)} instead, and provide the precise
     * RDMBS-specific data type, that is needed.
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @return A field representing the constant value
     */
    @SuppressWarnings("unchecked")
    public static <T> Field<T> val(T value) {

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
     * @see #val(Object, DataType)
     */
    public static <T> Field<T> val(Object value, Class<? extends T> type) {
        return val(value, getDataType(type));
    }

    /**
     * Get a value with an associated type, taken from a field
     *
     * @param <T> The generic value type
     * @param value The constant value
     * @param field The field whose data type to enforce upon the value
     * @return A field representing the constant value
     * @see #val(Object, DataType)
     */
    public static <T> Field<T> val(Object value, Field<T> field) {
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
    public static <T> Field<T> val(Object value, DataType<T> type) {

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
    public static List<Field<?>> vals(Object... values) {
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
    public static <T> Field<T> literal(T literal) {
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
    public static <T> Field<T> literal(Object literal, Class<T> type) {
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
    public static <T> Field<T> literal(Object literal, DataType<T> type) {
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
    static Field<?> NULL() {
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
    public static Field<Integer> zero() {
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
    public static Field<Integer> one() {
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
    public static Field<Integer> two() {
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
    public static Field<BigDecimal> pi() {
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
    public static Field<BigDecimal> e() {
        return new Euler();
    }

    // -------------------------------------------------------------------------
    // Pseudo fields and date time functions
    // -------------------------------------------------------------------------

    /**
     * Get the current_date() function
     * <p>
     * This translates into any dialect
     */
    public static Field<Date> currentDate() {
        return new CurrentDate();
    }

    /**
     * Get the current_time() function
     * <p>
     * This translates into any dialect
     */
    public static Field<Time> currentTime() {
        return new CurrentTime();
    }

    /**
     * Get the current_timestamp() function
     * <p>
     * This translates into any dialect
     */
    public static Field<Timestamp> currentTimestamp() {
        return new CurrentTimestamp();
    }

    /**
     * Get the current_user() function
     * <p>
     * This translates into any dialect
     */
    public static Field<String> currentUser() {
        return new CurrentUser();
    }

    /**
     * Get the rand() function
     */
    public static Field<BigDecimal> rand() {
        return new Rand();
    }

    // -------------------------------------------------------------------------
    // Fast querying
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
    public final <R extends TableRecord<R>> int executeInsert(Table<R> table, R record) {
        InsertQuery<R> insert = insertQuery(table);
        insert.setRecord(record);
        return insert.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> int executeUpdate(Table<R> table, R record) {
        return executeUpdate(table, record, trueCondition());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>, T> int executeUpdate(Table<R> table, R record, Condition condition) {
        UpdateQuery<R> update = updateQuery(table);
        update.addConditions(condition);
        update.setRecord(record);
        return update.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> int executeUpdateOne(Table<R> table, R record) {
        return filterUpdateOne(executeUpdate(table, record));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>, T> int executeUpdateOne(Table<R> table, R record, Condition condition) {
        return filterUpdateOne(executeUpdate(table, record, condition));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> int executeDelete(Table<R> table) {
        return executeDelete(table, trueCondition());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>, T> int executeDelete(Table<R> table, Condition condition)
        {
        DeleteQuery<R> delete = deleteQuery(table);
        delete.addConditions(condition);
        return delete.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> int executeDeleteOne(Table<R> table) {
        return executeDeleteOne(table, trueCondition());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>, T> int executeDeleteOne(Table<R> table, Condition condition) {
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
    @SuppressWarnings("deprecation")
    public static <T> DataType<T> getDataType(Class<? extends T> type) {
        return FieldTypeHelper.getDataType(SQLDialect.SQL99, type);
    }

    // -------------------------------------------------------------------------
    // Static initialisation of dialect-specific data types
    // -------------------------------------------------------------------------

    static {
        // Load all dialect-specific data types
        // TODO [#650] Make this more reliable using a data type registry

        try {
            Class.forName(SQLDataType.class.getName());
        } catch (Exception ignore) {}
    }

    // -------------------------------------------------------------------------
    // Internals
    // -------------------------------------------------------------------------

    private static int filterDeleteOne(int i) {
        return filterOne(i, "deleted");
    }

    private static int filterUpdateOne(int i) {
        return filterOne(i, "updated");
    }

    private static int filterOne(int i, String action) {
        if (i <= 1) {
            return i;
        }
        else {
            throw new DataAccessException("Too many rows " + action + " : " + i);
        }
    }

    private static <R extends Record> R filterOne(List<R> list) {
        if (list.size() == 0) {
            return null;
        }
        else if (list.size() == 1) {
            return list.get(0);
        }
        else {
            throw new DataAccessException("Too many rows returned : " + list.size());
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
