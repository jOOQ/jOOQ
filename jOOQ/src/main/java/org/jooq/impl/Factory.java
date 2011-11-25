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

import static org.jooq.impl.Util.combine;

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
import org.jooq.DatePart;
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
import org.jooq.UDT;
import org.jooq.UDTRecord;
import org.jooq.UpdateQuery;
import org.jooq.UpdateSetStep;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowOverStep;
import org.jooq.exception.InvalidResultException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.JooqLogger;

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
        return function(name, getDataType(type), nullSafe(arguments));
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
        return new Function<T>(name, type, nullSafe(arguments));
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
            throw Util.translate("Factory.fetch", null, e);
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
    public final <R extends TableRecord<R>> InsertSetStep<R> insertInto(Table<R> into) {
        return new InsertImpl<R>(this, into, Collections.<Field<?>>emptyList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> InsertValuesStep<R> insertInto(Table<R> into, Field<?>... fields) {
        return new InsertImpl<R>(this, into, Arrays.asList(fields));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> InsertValuesStep<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields) {
        return new InsertImpl<R>(this, into, fields);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> Insert<R> insertInto(Table<R> into, Select<?> select) {
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
    public final <R extends TableRecord<R>> UpdateSetStep<R> update(Table<R> table) {
        return new UpdateImpl<R>(this, table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <R extends TableRecord<R>> MergeUsingStep<R> mergeInto(Table<R> table) {
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
    public final <R extends TableRecord<R>> DeleteWhereStep<R> delete(Table<R> table) {
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
    public final <R extends TableRecord<R>> Truncate<R> truncate(Table<R> table) {
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
     * @see #decode(Field, Field, Field, Field[])
     */
    public static <Z, T> Field<Z> decode(T value, T search, Z result) {
        return decode(value, search, result, new Object[0]);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see #decode(Field, Field, Field, Field[])
     */
    public static <Z, T> Field<Z> decode(T value, T search, Z result, Object... more) {
        return decode(val(value), val(search), val(result), vals(more).toArray(new Field[0]));
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     *
     * @see #decode(Field, Field, Field, Field[])
     */
    public static <Z, T> Field<Z> decode(Field<T> value, Field<T> search, Field<Z> result) {
        return decode(nullSafe(value), nullSafe(search), nullSafe(result), new Field[0]);
    }

    /**
     * Gets the Oracle-style
     * <code>DECODE(expression, search, result[, search , result]... [, default])</code>
     * function
     * <p>
     * Returns the dialect's equivalent to DECODE:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/decode.php">DECODE</a></li>
     * </ul>
     * <p>
     * Other dialects: <code><pre>
     * CASE WHEN [this = search] THEN [result],
     *     [WHEN more...         THEN more...]
     *     [ELSE more...]
     * END
     * </pre></code>
     *
     * @param value The value to decode
     * @param search the mandatory first search parameter
     * @param result the mandatory first result candidate parameter
     * @param more the optional parameters. If <code>more.length</code> is even,
     *            then it is assumed that it contains more search/result pairs.
     *            If <code>more.length</code> is odd, then it is assumed that it
     *            contains more search/result pairs plus a default at the end.     *
     */
    public static <Z, T> Field<Z> decode(Field<T> value, Field<T> search, Field<Z> result, Field<?>... more) {
        return new Decode<T, Z>(nullSafe(value), nullSafe(search), nullSafe(result), nullSafe(more));
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

    /**
     * Gets the Oracle-style <code>COALESCE(value1, value2, ... , value n)</code>
     * function
     *
     * @see #coalesce(Field, Field...)
     */
    public static <T> Field<T> coalesce(T value, T... values) {
        return coalesce(val(value), vals(values).toArray(new Field[0]));
    }

    /**
     * Gets the Oracle-style <code>COALESCE(field1, field2, ... , field n)</code>
     * function
     * <p>
     * Returns the dialect's equivalent to COALESCE:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/coalesce.php">COALESCE</a>
     * </li>
     * </ul>
     */
    public static <T> Field<T> coalesce(Field<T> field, Field<?>... fields) {
        return function("coalesce", nullSafeDataType(field), nullSafe(combine(field, fields)));
    }

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function
     *
     * @see #nvl(Field, Field)
     */
    public static <T> Field<T> nvl(T value, T defaultValue) {
        return nvl(val(value), val(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function
     *
     * @see #nvl(Field, Field)
     */
    public static <T> Field<T> nvl(T value, Field<T> defaultValue) {
        return nvl(val(value), nullSafe(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function
     *
     * @see #nvl(Field, Field)
     */
    public static <T> Field<T> nvl(Field<T> value, T defaultValue) {
        return nvl(nullSafe(value), val(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL(value, defaultValue) function
     * <p>
     * Returns the dialect's equivalent to NVL:
     * <ul>
     * <li>DB2 <a href=
     * "http://publib.boulder.ibm.com/infocenter/db2luw/v9r7/index.jsp?topic=/com.ibm.db2.luw.sql.ref.doc/doc/r0052627.html"
     * >NVL</a></li>
     * <li>Derby <a
     * href="http://db.apache.org/derby/docs/10.7/ref/rreffunccoalesce.html"
     * >COALESCE</a></li>
     * <li>H2 <a
     * href="http://www.h2database.com/html/functions.html#ifnull">IFNULL</a></li>
     * <li>HSQLDB <a
     * href="http://hsqldb.org/doc/2.0/guide/builtinfunctions-chapt.html"
     * >NVL</a></li>
     * <li>MySQL <a href=
     * "http://dev.mysql.com/doc/refman/5.0/en/control-flow-functions.html"
     * >IFNULL</a></li>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/nvl.php">NVL</a></li>
     * <li>Postgres <a href=
     * "http://www.postgresql.org/docs/8.1/static/functions-conditional.html"
     * >COALESCE</a></li>
     * <li>SQLite <a
     * href="http://www.sqlite.org/lang_corefunc.html#ifnull">IFNULL</a></li>
     * </ul>
     */
    public static <T> Field<T> nvl(Field<T> value, Field<T> defaultValue) {
        return new Nvl<T>(nullSafe(value), nullSafe(defaultValue));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function
     *
     * @see #nvl2(Field, Field, Field)
     */
    public static <Z> Field<Z> nvl2(Field<?> value, Z valueIfNotNull, Z valueIfNull) {
        return nvl2(nullSafe(value), val(valueIfNotNull), val(valueIfNull));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function
     *
     * @see #nvl2(Field, Field, Field)
     */
    public static <Z> Field<Z> nvl2(Field<?> value, Z valueIfNotNull, Field<Z> valueIfNull) {
        return nvl2(nullSafe(value), val(valueIfNotNull), nullSafe(valueIfNull));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function
     *
     * @see #nvl2(Field, Field, Field)
     */
    public static <Z> Field<Z> nvl2(Field<?> value, Field<Z> valueIfNotNull, Z valueIfNull) {
        return nvl2(nullSafe(value), nullSafe(valueIfNotNull), val(valueIfNull));
    }

    /**
     * Gets the Oracle-style NVL2(value, valueIfNotNull, valueIfNull) function
     * <p>
     * Returns the dialect's equivalent to NVL2:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/nvl2.php">NVL2</a></li>
     * </ul>
     * <p>
     * Other dialects:
     * <code>CASE WHEN [value] IS NULL THEN [valueIfNull] ELSE [valueIfNotNull] END</code>
     */
    public static <Z> Field<Z> nvl2(Field<?> value, Field<Z> valueIfNotNull, Field<Z> valueIfNull) {
        return new Nvl2<Z>(nullSafe(value), nullSafe(valueIfNotNull), nullSafe(valueIfNull));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function
     *
     * @see #nullif(Field, Field)
     */
    public static <T> Field<T> nullif(T value, T other) {
        return nullif(val(value), val(other));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function
     *
     * @see #nullif(Field, Field)
     */
    public static <T> Field<T> nullif(T value, Field<T> other) {
        return nullif(val(value), nullSafe(other));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function
     *
     * @see #nullif(Field, Field)
     */
    public static <T> Field<T> nullif(Field<T> value, T other) {
        return nullif(nullSafe(value), val(other));
    }

    /**
     * Gets the Oracle-style NULLIF(value, other) function
     * <p>
     * Returns the dialect's equivalent to NULLIF:
     * <ul>
     * <li>Oracle <a
     * href="http://www.techonthenet.com/oracle/functions/nullif.php">NULLIF</a></li>
     * </ul>
     * <p>
     */
    public static <T> Field<T> nullif(Field<T> value, Field<T> other) {
        return function("nullif", nullSafeDataType(value), nullSafe(value), nullSafe(other));
    }

    // -------------------------------------------------------------------------
    // String function factory
    // -------------------------------------------------------------------------

    /**
     * Get the upper(field) function
     *
     * @see #upper(Field)
     */
    public static Field<String> upper(String value) {
        return upper(val(value));
    }

    /**
     * Get the upper(field) function
     * <p>
     * This renders the upper function in all dialects:
     * <code><pre>upper([field])</pre></code>
     */
    public static Field<String> upper(Field<String> field) {
        return function("upper", SQLDataType.VARCHAR, nullSafe(field));
    }

    /**
     * Get the lower(field) function
     *
     * @see #lower(Field)
     */
    public static Field<String> lower(String value) {
        return lower(val(value));
    }

    /**
     * Get the lower(field) function
     * <p>
     * This renders the lower function in all dialects:
     * <code><pre>lower([field])</pre></code>
     */
    public static Field<String> lower(Field<String> value) {
        return function("lower", SQLDataType.VARCHAR, nullSafe(value));
    }

    /**
     * Get the trim(field) function
     *
     * @see #trim(Field)
     */
    public static Field<String> trim(String value) {
        return trim(val(value));
    }

    /**
     * Get the trim(field) function
     * <p>
     * This renders the trim function where available:
     * <code><pre>trim([field])</pre></code> ... or simulates it elsewhere using
     * rtrim and ltrim: <code><pre>ltrim(rtrim([field]))</pre></code>
     */
    public static Field<String> trim(Field<String> field) {
        return new Trim(nullSafe(field));
    }

    /**
     * Get the rtrim(field) function
     *
     * @see #rtrim(Field)
     */
    public static Field<String> rtrim(String value) {
        return rtrim(val(value));
    }

    /**
     * Get the rtrim(field) function
     * <p>
     * This renders the rtrim function in all dialects:
     * <code><pre>rtrim([field])</pre></code>
     */
    public static Field<String> rtrim(Field<String> field) {
        return function("rtrim", SQLDataType.VARCHAR, nullSafe(field));
    }

    /**
     * Get the ltrim(field) function
     *
     * @see #ltrim(Field)
     */
    public static Field<String> ltrim(String value) {
        return ltrim(val(value));
    }

    /**
     * Get the ltrim(field) function
     * <p>
     * This renders the ltrim function in all dialects:
     * <code><pre>ltrim([field])</pre></code>
     */
    public static Field<String> ltrim(Field<String> value) {
        return function("ltrim", SQLDataType.VARCHAR, nullSafe(value));
    }

    /**
     * Get the rpad(field, length) function
     *
     * @see #rpad(Field, Field)
     */
    public static Field<String> rpad(Field<String> field, int length) {
        return rpad(nullSafe(field), val(length));
    }

    /**
     * Get the rpad(field, length) function
     * <p>
     * This renders the rpad function where available:
     * <code><pre>rpad([field], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat([field], repeat(' ', [length] - length([field])))</pre></code>
     */
    public static Field<String> rpad(Field<String> field, Field<? extends Number> length) {
        return new Rpad(nullSafe(field), nullSafe(length));
    }

    /**
     * Get the rpad(field, length, character) function
     *
     * @see #rpad(Field, Field, Field)
     */
    public static Field<String> rpad(Field<String> field, int length, char character) {
        return rpad(field, length, Character.toString(character));
    }

    /**
     * Get the rpad(field, length, character) function
     *
     * @see #rpad(Field, Field, Field)
     */
    public static Field<String> rpad(Field<String> field, int length, String character) {
        return rpad(nullSafe(field), val(length), val(character));
    }

    /**
     * Get the rpad(field, length, character) function
     * <p>
     * This renders the rpad function where available:
     * <code><pre>rpad([field], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat([field], repeat([character], [length] - length([field])))</pre></code>
     */
    public static Field<String> rpad(Field<String> field, Field<? extends Number> length, Field<String> character) {
        return new Rpad(nullSafe(field), nullSafe(length), nullSafe(character));
    }

    /**
     * Get the lpad(field, length) function
     *
     * @see #lpad(Field, Field)
     */
    public static Field<String> lpad(Field<String> field, int length) {
        return lpad(nullSafe(field), val(length));
    }

    /**
     * Get the lpad(field, length) function
     * <p>
     * This renders the lpad function where available:
     * <code><pre>lpad([field], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat(repeat(' ', [length] - length([field])), [field])</pre></code>
     */
    public static Field<String> lpad(Field<String> field, Field<? extends Number> length) {
        return new Lpad(nullSafe(field), nullSafe(length));
    }

    /**
     * Get the lpad(field, length, character) function
     *
     * @see #lpad(Field, Field, Field)
     */
    public static Field<String> lpad(Field<String> field, int length, char character) {
        return lpad(field, length, Character.toString(character));
    }

    /**
     * Get the lpad(field, length, character) function
     *
     * @see #lpad(Field, Field, Field)
     */
    public static Field<String> lpad(Field<String> field, int length, String character) {
        return lpad(nullSafe(field), val(length), val(character));
    }

    /**
     * Get the lpad(field, length, character) function
     * <p>
     * This renders the lpad function where available:
     * <code><pre>lpad([field], [length])</pre></code> ... or simulates it
     * elsewhere using concat, repeat, and length, which may be simulated as
     * well, depending on the RDBMS:
     * <code><pre>concat(repeat([character], [length] - length([field])), [field])</pre></code>
     */
    public static Field<String> lpad(Field<String> field, Field<? extends Number> length, Field<String> character) {
        return new Lpad(nullSafe(field), nullSafe(length), nullSafe(character));
    }

    /**
     * Get the repeat(field, count) function
     *
     * @see #repeat(Field, Field)
     */
    public static Field<String> repeat(String field, int count) {
        return repeat(val(field), val(count));
    }

    /**
     * Get the repeat(field, count) function
     *
     * @see #repeat(Field, Field)
     */
    public static Field<String> repeat(String field, Field<? extends Number> count) {
        return repeat(val(field), nullSafe(count));
    }

    /**
     * Get the repeat(count) function
     *
     * @see #repeat(Field, Field)
     */
    public static Field<String> repeat(Field<String> field, int count) {
        return repeat(nullSafe(field), val(count));
    }

    /**
     * Get the repeat(field, count) function
     * <p>
     * This renders the repeat or replicate function where available:
     * <code><pre>repeat([field], [count]) or
     * replicate([field], [count])</pre></code> ... or simulates it elsewhere
     * using rpad and length, which may be simulated as well, depending on the
     * RDBMS:
     * <code><pre>rpad([field], length([field]) * [count], [field])</pre></code>
     */
    public static Field<String> repeat(Field<String> field, Field<? extends Number> count) {
        return new Repeat(nullSafe(field), nullSafe(count));
    }

    /**
     * Get the replace(field, search) function
     *
     * @see #replace(Field, Field)
     */
    public static Field<String> replace(Field<String> field, String search) {
        return replace(nullSafe(field), val(search));
    }

    /**
     * Get the replace(field, search) function
     * <p>
     * This renders the replace or str_replace function where available:
     * <code><pre>replace([field], [search]) or
     * str_replace([field], [search])</pre></code> ... or simulates it elsewhere
     * using the three-argument replace function:
     * <code><pre>replace([field], [search], '')</pre></code>
     */
    public static Field<String> replace(Field<String> field, Field<String> search) {
        return new Replace(nullSafe(field), nullSafe(search));
    }

    /**
     * Get the replace(field, search, replace) function
     *
     * @see #replace(Field, Field, Field)
     */
    public static Field<String> replace(Field<String> field, String search, String replace) {
        return replace(nullSafe(field), val(search), val(replace));
    }

    /**
     * Get the replace(field, search, replace) function
     * <p>
     * This renders the replace or str_replace function:
     * <code><pre>replace([field], [search]) or
     * str_replace([field], [search])</pre></code>
     */
    public static Field<String> replace(Field<String> field, Field<String> search, Field<String> replace) {
        return new Replace(nullSafe(field), nullSafe(search), nullSafe(replace));
    }

    /**
     * Get the position(in, search) function
     *
     * @see #position(Field, Field)
     */
    public static Field<Integer> position(String in, String search) {
        return position(val(in), val(search));
    }

    /**
     * Get the position(in, search) function
     *
     * @see #position(Field, Field)
     */
    public static Field<Integer> position(String in, Field<String> search) {
        return position(val(in), nullSafe(search));
    }

    /**
     * Get the position(in, search) function
     *
     * @see #position(Field, Field)
     */
    public static Field<Integer> position(Field<String> in, String search) {
        return position(nullSafe(in), val(search));
    }

    /**
     * Get the position(in, search) function
     * <p>
     * This renders the position or any equivalent function:
     * <code><pre>position([search] in [in]) or
     * locate([in], [search]) or
     * locate([search], [in]) or
     * instr([in], [search]) or
     * charindex([search], [in])</pre></code>
     */
    public static Field<Integer> position(Field<String> in, Field<String> search) {
        return new Position(nullSafe(search), nullSafe(in));
    }

    /**
     * Get the ascii(field) function
     *
     * @see #ascii(Field)
     */
    public static Field<Integer> ascii(String field) {
        return ascii(val(field));
    }

    /**
     * Get the ascii(field) function
     * <p>
     * This renders the ascii function:
     * <code><pre>ascii([field])</pre></code>
     */
    public static Field<Integer> ascii(Field<String> field) {
        return new Ascii(nullSafe(field));
    }

    /**
     * Get the concat(value[, value, ...]) function
     *
     * @see #concat(Field...)
     */
    public static Field<String> concat(String... values) {
        return concat(vals((Object[]) values).toArray(new Field[0]));
    }

    /**
     * Get the concat(field[, field, ...]) function
     * <p>
     * This creates <code>fields[0] || fields[1] || ...</code> as an
     * expression, or <code>concat(fields[0], fields[1], ...)</code>,
     * depending on the dialect.
     * <p>
     * If any of the given fields is not a {@link String} field, they are cast
     * to <code>Field&lt;String&gt;</code> first using {@link #cast(Object, Class)}
     */
    public static Field<String> concat(Field<?>... fields) {
        return new Concat(nullSafe(fields));
    }

    /**
     * Get the substring(field, startingPosition) function
     *
     * @see #substring(Field, Field)
     */
    public static Field<String> substring(Field<String> field, int startingPosition) {
        return substring(nullSafe(field), val(startingPosition));
    }

    /**
     * Get the substring(field, startingPosition) function
     * <p>
     * This renders the substr or substring function:
     * <code><pre>substr([field], [startingPosition]) or
     * substring([field], [startingPosition])</pre></code>
     */
    public static Field<String> substring(Field<String> field, Field<? extends Number> startingPosition) {
        return new Substring(nullSafe(field), nullSafe(startingPosition));
    }

    /**
     * Get the substring(field, startingPosition, length) function
     *
     * @see #substring(Field, Field, Field)
     */
    public static Field<String> substring(Field<String> field, int startingPosition, int length) {
        return substring(nullSafe(field), val(startingPosition), val(length));
    }

    /**
     * Get the substring(field, startingPosition, length) function
     * <p>
     * This renders the substr or substring function:
     * <code><pre>substr([field], [startingPosition], [length]) or
     * substring([field], [startingPosition], [length])</pre></code>
     */
    public static Field<String> substring(Field<String> field, Field<? extends Number> startingPosition, Field<? extends Number> length) {
        return new Substring(nullSafe(field), nullSafe(startingPosition), nullSafe(length));
    }

    /**
     * Get the length of a <code>VARCHAR</code> type. This is a synonym for
     * {@link #charLength(String)}
     *
     * @see #charLength(String)
     */
    public static Field<Integer> length(String value) {
        return length(val(value));
    }

    /**
     * Get the length of a <code>VARCHAR</code> type. This is a synonym for
     * {@link #charLength(Field)}
     *
     * @see #charLength(Field)
     */
    public static Field<Integer> length(Field<String> field) {
        return charLength(field);
    }

    /**
     * Get the char_length(field) function
     * <p>
     * This translates into any dialect
     */
    public static Field<Integer> charLength(String value) {
        return charLength(val(value));
    }

    /**
     * Get the char_length(field) function
     * <p>
     * This translates into any dialect
     */
    public static Field<Integer> charLength(Field<String> field) {
        return new Function<Integer>(Term.CHAR_LENGTH, SQLDataType.INTEGER, nullSafe(field));
    }

    /**
     * Get the bit_length(field) function
     * <p>
     * This translates into any dialect
     */
    public static Field<Integer> bitLength(String value) {
        return bitLength(val(value));
    }

    /**
     * Get the bit_length(field) function
     * <p>
     * This translates into any dialect
     */
    public static Field<Integer> bitLength(Field<String> field) {
        return new Function<Integer>(Term.BIT_LENGTH, SQLDataType.INTEGER, nullSafe(field));
    }

    /**
     * Get the octet_length(field) function
     * <p>
     * This translates into any dialect
     */
    public static Field<Integer> octetLength(String value) {
        return octetLength(val(value));
    }

    /**
     * Get the octet_length(field) function
     * <p>
     * This translates into any dialect
     */
    public static Field<Integer> octetLength(Field<String> field) {
        return new Function<Integer>(Term.OCTET_LENGTH, SQLDataType.INTEGER, nullSafe(field));
    }

    // ------------------------------------------------------------------------
    // Date and time functions
    // ------------------------------------------------------------------------

    /**
     * Get the extract(field, datePart) function
     * <p>
     * This translates into any dialect
     */
    public static Field<Integer> extract(java.util.Date value, DatePart datePart) {
        return extract(val(value), datePart);
    }

    /**
     * Get the extract(field, datePart) function
     * <p>
     * This translates into any dialect
     */
    public static Field<Integer> extract(Field<? extends java.util.Date> field, DatePart datePart) {
        return new Extract(nullSafe(field), datePart);
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
        return new Rollup(nullSafe(fields));
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
        return function("cube", Object.class, nullSafe(fields));
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
        return function("grouping", Integer.class, nullSafe(field));
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
        return function("grouping_id", Integer.class, nullSafe(fields));
    }

    // ------------------------------------------------------------------------
    // Bitwise operations
    // ------------------------------------------------------------------------

    /**
     * The MySQL <code>BIT_COUNT(field)</code> function, counting the number of
     * bits that are set in this number.
     *
     * @see #bitCount(Field)
     */
    public static Field<Integer> bitCount(Number value) {
        return bitCount(val(value));
    }

    /**
     * The MySQL <code>BIT_COUNT(field)</code> function, counting the number of
     * bits that are set in this number.
     * <p>
     * This function is simulated in most other databases like this (for a
     * TINYINT field): <code><pre>
     * ([field] &   1) +
     * ([field] &   2) >> 1 +
     * ([field] &   4) >> 2 +
     * ([field] &   8) >> 3 +
     * ([field] &  16) >> 4 +
     *  ...
     * ([field] & 128) >> 7
     * </pre></code>
     * <p>
     * More efficient algorithms are very welcome
     */
    public static Field<Integer> bitCount(Field<? extends Number> field) {
        return new BitCount(nullSafe(field));
    }

    /**
     * The bitwise not operator.
     *
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitNot(T value) {
        return bitNot(val(value));
    }

    /**
     * The bitwise not operator.
     * <p>
     * Most dialects natively support this using <code>~[field]</code>. jOOQ
     * simulates this operator in some dialects using <code>-[field] - 1</code>
     */
    public static <T extends Number> Field<T> bitNot(Field<T> field) {
        return new Neg<T>(nullSafe(field), ExpressionOperator.BIT_NOT);
    }

    /**
     * The bitwise and operator.
     *
     * @see #bitAnd(Field, Field)
     */
    public static <T extends Number> Field<T> bitAnd(T value1, T value2) {
        return bitAnd(val(value1), val(value2));
    }

    /**
     * The bitwise and operator.
     *
     * @see #bitAnd(Field, Field)
     */
    public static <T extends Number> Field<T> bitAnd(T value1, Field<T> value2) {
        return bitAnd(val(value1), nullSafe(value2));
    }

    /**
     * The bitwise and operator.
     *
     * @see #bitAnd(Field, Field)
     */
    public static <T extends Number> Field<T> bitAnd(Field<T> value1, T value2) {
        return bitAnd(nullSafe(value1), val(value2));
    }

    /**
     * The bitwise and operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the and operation where available:
     * <code><pre>[field1] & [field2]</pre></code>
     * ... or the and function elsewhere:
     * <code><pre>bitand([field1], [field2])</pre></code>
     */
    public static <T extends Number> Field<T> bitAnd(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_AND, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise not and operator.
     *
     * @see #bitNand(Field, Field)
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitNand(T value1, T value2) {
        return bitNand(val(value1), val(value2));
    }

    /**
     * The bitwise not and operator.
     *
     * @see #bitNand(Field, Field)
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitNand(T value1, Field<T> value2) {
        return bitNand(val(value1), nullSafe(value2));
    }

    /**
     * The bitwise not and operator.
     *
     * @see #bitNand(Field, Field)
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitNand(Field<T> value1, T value2) {
        return bitNand(nullSafe(value1), val(value2));
    }

    /**
     * The bitwise not and operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the not and operation where available:
     * <code><pre>~([field1] & [field2])</pre></code>
     * ... or the not and function elsewhere:
     * <code><pre>bitnot(bitand([field1], [field2]))</pre></code>
     *
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitNand(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_NAND, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise or operator.
     *
     * @see #bitOr(Field, Field)
     */
    public static <T extends Number> Field<T> bitOr(T value1, T value2) {
        return bitOr(val(value1), val(value2));
    }

    /**
     * The bitwise or operator.
     *
     * @see #bitOr(Field, Field)
     */
    public static <T extends Number> Field<T> bitOr(T value1, Field<T> value2) {
        return bitOr(val(value1), nullSafe(value2));
    }

    /**
     * The bitwise or operator.
     *
     * @see #bitOr(Field, Field)
     */
    public static <T extends Number> Field<T> bitOr(Field<T> value1, T value2) {
        return bitOr(nullSafe(value1), val(value2));
    }

    /**
     * The bitwise or operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the or operation where available:
     * <code><pre>[field1] | [field2]</pre></code>
     * ... or the or function elsewhere:
     * <code><pre>bitor([field1], [field2])</pre></code>
     */
    public static <T extends Number> Field<T> bitOr(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_OR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise not or operator.
     *
     * @see #bitNor(Field, Field)
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitNor(T value1, T value2) {
        return bitNor(val(value1), val(value2));
    }
    /**
     * The bitwise not or operator.
     *
     * @see #bitNor(Field, Field)
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitNor(T value1, Field<T> value2) {
        return bitNor(val(value1), nullSafe(value2));
    }
    /**
     * The bitwise not or operator.
     *
     * @see #bitNor(Field, Field)
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitNor(Field<T> value1, T value2) {
        return bitNor(nullSafe(value1), val(value2));
    }

    /**
     * The bitwise not or operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the not or operation where available:
     * <code><pre>~([field1] | [field2])</pre></code>
     * ... or the not or function elsewhere:
     * <code><pre>bitnot(bitor([field1], [field2]))</pre></code>
     *
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitNor(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_NOR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise xor operator.
     *
     * @see #bitXor(Field, Field)
     */
    public static <T extends Number> Field<T> bitXor(T value1, T value2) {
        return bitXor(val(value1), val(value2));
    }

    /**
     * The bitwise xor operator.
     *
     * @see #bitXor(Field, Field)
     */
    public static <T extends Number> Field<T> bitXor(T value1, Field<T> value2) {
        return bitXor(val(value1), nullSafe(value2));
    }

    /**
     * The bitwise xor operator.
     *
     * @see #bitXor(Field, Field)
     */
    public static <T extends Number> Field<T> bitXor(Field<T> value1, T value2) {
        return bitXor(nullSafe(value1), val(value2));
    }

    /**
     * The bitwise xor operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the or operation where available:
     * <code><pre>[field1] ^ [field2]</pre></code>
     * ... or the xor function elsewhere:
     * <code><pre>bitxor([field1], [field2])</pre></code>
     */
    public static <T extends Number> Field<T> bitXor(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_XOR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise not xor operator.
     *
     * @see #bitXNor(Field, Field)
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitXNor(T value1, T value2) {
        return bitXNor(val(value1), val(value2));
    }

    /**
     * The bitwise not xor operator.
     *
     * @see #bitXNor(Field, Field)
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitXNor(T value1, Field<T> value2) {
        return bitXNor(val(value1), nullSafe(value2));
    }

    /**
     * The bitwise not xor operator.
     *
     * @see #bitXNor(Field, Field)
     * @see #bitNot(Field)
     */
    public static <T extends Number> Field<T> bitXNor(Field<T> value1, T value2) {
        return bitXNor(nullSafe(value1), val(value2));
    }

    /**
     * The bitwise not xor operator.
     * <p>
     * This is not supported by Derby, Ingres
     * <p>
     * This renders the or operation where available:
     * <code><pre>~([field1] ^ [field2])</pre></code>
     * ... or the not xor function elsewhere:
     * <code><pre>bitnot(bitxor([field1], [field2]))</pre></code>
     */
    public static <T extends Number> Field<T> bitXNor(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.BIT_XNOR, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise left shift operator.
     *
     * @see #shl(Field, Field)
     * @see #power(Field, Number)
     */
    public static <T extends Number> Field<T> shl(T value1, T value2) {
        return shl(val(value1), val(value2));
    }

    /**
     * The bitwise left shift operator.
     *
     * @see #shl(Field, Field)
     * @see #power(Field, Number)
     */
    public static <T extends Number> Field<T> shl(T value1, Field<T> value2) {
        return shl(val(value1), nullSafe(value2));
    }

    /**
     * The bitwise left shift operator.
     *
     * @see #shl(Field, Field)
     * @see #power(Field, Number)
     */
    public static <T extends Number> Field<T> shl(Field<T>value1, T value2) {
        return shl(nullSafe(value1), val(value2));
    }

    /**
     * The bitwise left shift operator.
     * <p>
     * Some dialects natively support this using <code>[field1] << [field2]</code>.
     * jOOQ simulates this operator in some dialects using
     * <code>[field1] * power(2, [field2])</code>, where power might also be simulated.
     *
     * @see #power(Field, Field)
     */
    public static <T extends Number> Field<T> shl(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.SHL, nullSafe(field1), nullSafe(field2));
    }

    /**
     * The bitwise right shift operator.
     *
     * @see #shr(Field, Field)
     * @see #power(Field, Number)
     */
    public static <T extends Number> Field<T> shr(T value1, T value2) {
        return shr(val(value1), val(value2));
    }

    /**
     * The bitwise right shift operator.
     *
     * @see #shr(Field, Field)
     * @see #power(Field, Number)
     */
    public static <T extends Number> Field<T> shr(T value1, Field<T> value2) {
        return shr(val(value1), nullSafe(value2));
    }

    /**
     * The bitwise right shift operator.
     *
     * @see #shr(Field, Field)
     * @see #power(Field, Number)
     */
    public static <T extends Number> Field<T> shr(Field<T> value1, T value2) {
        return shr(nullSafe(value1), val(value2));
    }

    /**
     * The bitwise right shift operator.
     * <p>
     * Some dialects natively support this using <code>[field1] >> [field2]</code>.
     * jOOQ simulates this operator in some dialects using
     * <code>[field1] / power(2, [field2])</code>, where power might also be simulated.
     *
     * @see #power(Field, Field)
     */
    public static <T extends Number> Field<T> shr(Field<T> field1, Field<T> field2) {
        return new Expression<T>(ExpressionOperator.SHR, nullSafe(field1), nullSafe(field2));
    }

    // ------------------------------------------------------------------------
    // Mathematical functions
    // ------------------------------------------------------------------------

    /**
     * Find the greatest among all values
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     *
     * @see #greatest(Field, Field...)
     */
    public static <T> Field<T> greatest(T value, T... values) {
        return greatest(val(value), vals(values).toArray(new Field[0]));
    }

    /**
     * Find the greatest among all values
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     */
    public static <T> Field<T> greatest(Field<T> field, Field<?>... others) {
        return new Greatest<T>(nullSafeDataType(field), nullSafe(combine(field, others)));
    }

    /**
     * Find the least among all values
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     *
     * @see #least(Field, Field...)
     */
    public static <T> Field<T> least(T value, T... values) {
        return least(val(value), vals(values).toArray(new Field[0]));
    }

    /**
     * Find the least among all values
     * <p>
     * This function has no equivalent in Adaptive Server, Derby, SQL Server and
     * Sybase SQL Anywhere. Its current simulation implementation has
     * <code>O(2^n)</code> complexity and should be avoided for
     * <code>n &gt; 5</code>! Better implementation suggestions are very
     * welcome.
     */
    public static <T> Field<T> least(Field<T> field, Field<?>... others) {
        return new Least<T>(nullSafeDataType(field), nullSafe(combine(field, others)));
    }

    /**
     * Get the sign of a numeric field: sign(field)
     *
     * @see #sign(Field)
     */
    public static Field<Integer> sign(Number value) {
        return sign(val(value));
    }

    /**
     * Get the sign of a numeric field: sign(field)
     * <p>
     * This renders the sign function where available:
     * <code><pre>sign([field])</pre></code>
     * ... or simulates it elsewhere (without bind variables on values -1, 0, 1):
     * <code><pre>
     * CASE WHEN [this] > 0 THEN 1
     *      WHEN [this] < 0 THEN -1
     *      ELSE 0
     * END
     */
    public static Field<Integer> sign(Field<? extends Number> field) {
        return new Sign(nullSafe(field));
    }

    /**
     * Get the absolute value of a numeric field: abs(field)
     *
     * @see #abs(Field)
     */
    public static <T extends Number> Field<T> abs(T value) {
        return abs(val(value));
    }

    /**
     * Get the absolute value of a numeric field: abs(field)
     * <p>
     * This renders the same on all dialects:
     * <code><pre>abs([field])</pre></code>
     */
    public static <T extends Number> Field<T> abs(Field<T> field) {
        return function("abs", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get rounded value of a numeric field: round(field)
     *
     * @see #round(Field)
     */
    public static <T extends Number> Field<T> round(T value) {
        return round(val(value));
    }

    /**
     * Get rounded value of a numeric field: round(field)
     * <p>
     * This renders the round function where available:
     * <code><pre>round([field]) or
     * round([field], 0)</pre></code>
     * ... or simulates it elsewhere using floor and ceil
     */
    public static <T extends Number> Field<T> round(Field<T> field) {
        return new Round<T>(nullSafe(field));
    }

    /**
     * Get rounded value of a numeric field: round(field, decimals)
     *
     * @see #round(Field, int)
     */
    public static <T extends Number> Field<T> round(T value, int decimals) {
        return round(val(value), decimals);
    }

    /**
     * Get rounded value of a numeric field: round(field, decimals)
     * <p>
     * This renders the round function where available:
     * <code><pre>round([field], [decimals])</pre></code>
     * ... or simulates it elsewhere using floor and ceil
     */
    public static <T extends Number> Field<T> round(Field<T> field, int decimals) {
        return new Round<T>(nullSafe(field), decimals);
    }

    /**
     * Get the largest integer value not greater than [this]
     *
     * @see #floor(Field)
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
        return new Floor<T>(nullSafe(field));
    }

    /**
     * Get the smallest integer value not less than [this]
     *
     * @see #ceil(Field)
     */
    public static <T extends Number> Field<T> ceil(T value) {
        return ceil(val(value));
    }

    /**
     * Get the smallest integer value not less than [field]
     * <p>
     * This renders the ceil or ceiling function where available:
     * <code><pre>ceil([field]) or
     * ceiling([field])</pre></code>
     * ... or simulates it elsewhere using round:
     * <code><pre>round([field] + 0.499999999999999)</pre></code>
     */
    public static <T extends Number> Field<T> ceil(Field<T> field) {
        return new Ceil<T>(nullSafe(field));
    }

    /**
     * Get the sqrt(field) function
     *
     * @see #sqrt(Field)
     */
    public static Field<BigDecimal> sqrt(Number value) {
        return sqrt(val(value));
    }

    /**
     * Get the sqrt(field) function
     * <p>
     * This renders the sqrt function where available:
     * <code><pre>sqrt([field])</pre></code> ... or simulates it elsewhere using
     * power (which in turn may also be simulated using ln and exp functions):
     * <code><pre>power([field], 0.5)</pre></code>
     */
    public static Field<BigDecimal> sqrt(Field<? extends Number> field) {
        return new Sqrt(nullSafe(field));
    }

    /**
     * Get the exp(field) function, taking this field as the power of e
     *
     * @see #exp(Field)
     */
    public static Field<BigDecimal> exp(Number value) {
        return exp(val(value));
    }

    /**
     * Get the exp(field) function, taking this field as the power of e
     * <p>
     * This renders the same on all dialects:
     * <code><pre>exp([field])</pre></code>
     */
    public static Field<BigDecimal> exp(Field<? extends Number> field) {
        return function("exp", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the ln(field) function, taking the natural logarithm of this field
     *
     * @see #ln(Field)
     */
    public static Field<BigDecimal> ln(Number value) {
        return ln(val(value));
    }

    /**
     * Get the ln(field) function, taking the natural logarithm of this field
     * <p>
     * This renders the ln or log function where available:
     * <code><pre>ln([field]) or
     * log([field])</pre></code>
     */
    public static Field<BigDecimal> ln(Field<? extends Number> field) {
        return new Ln(nullSafe(field));
    }

    /**
     * Get the log(field, base) function
     *
     * @see #log(Field, int)
     */
    public static Field<BigDecimal> log(Number value, int base) {
        return log(val(value), base);
    }

    /**
     * Get the log(field, base) function
     * <p>
     * This renders the log function where available:
     * <code><pre>log([field])</pre></code> ... or simulates it elsewhere (in
     * most RDBMS) using the natural logarithm:
     * <code><pre>ln([field]) / ln([base])</pre></code>
     */
    public static Field<BigDecimal> log(Field<? extends Number> field, int base) {
        return new Ln(nullSafe(field), base);
    }

    /**
     * Get the power(field, exponent) function
     *
     * @see #power(Field, Field)
     */
    public static Field<BigDecimal> power(Number value, Number exponent) {
        return power(val(value), val(exponent));
    }

    /**
     * Get the power(field, exponent) function
     *
     * @see #power(Field, Field)
     */
    public static Field<BigDecimal> power(Field<? extends Number> field, Number exponent) {
        return power(nullSafe(field), val(exponent));
    }

    /**
     * Get the power(field, exponent) function
     *
     * @see #power(Field, Field)
     */
    public static Field<BigDecimal> power(Number value, Field<? extends Number> exponent) {
        return power(val(value), nullSafe(exponent));
    }

    /**
     * Get the power(field, exponent) function
     * <p>
     * This renders the power function where available:
     * <code><pre>power([field], [exponent])</pre></code> ... or simulates it
     * elsewhere using ln and exp:
     * <code><pre>exp(ln([field]) * [exponent])</pre></code>
     */
    public static Field<BigDecimal> power(Field<? extends Number> field, Field<? extends Number> exponent) {
        return new Power(nullSafe(field), nullSafe(exponent));
    }

    /**
     * Get the arc cosine(field) function
     *
     * @see #acos(Field)
     */
    public static Field<BigDecimal> acos(Number value) {
        return acos(val(value));
    }

    /**
     * Get the arc cosine(field) function
     * <p>
     * This renders the acos function where available:
     * <code><pre>acos([field])</pre></code>
     */
    public static Field<BigDecimal> acos(Field<? extends Number> field) {
        return function("acos", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the arc sine(field) function
     *
     * @see #asin(Field)
     */
    public static Field<BigDecimal> asin(Number value) {
        return asin(val(value));
    }

    /**
     * Get the arc sine(field) function
     * <p>
     * This renders the asin function where available:
     * <code><pre>asin([field])</pre></code>
     */
    public static Field<BigDecimal> asin(Field<? extends Number> field) {
        return function("asin", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the arc tangent(field) function
     *
     * @see #atan(Field)
     */
    public static Field<BigDecimal> atan(Number value) {
        return atan(val(value));
    }

    /**
     * Get the arc tangent(field) function
     * <p>
     * This renders the atan function where available:
     * <code><pre>atan([field])</pre></code>
     */
    public static Field<BigDecimal> atan(Field<? extends Number> field) {
        return function("atan", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the atan2(field, y) function
     *
     * @see #atan2(Field, Field)
     */
    public static Field<BigDecimal> atan2(Number x, Number y) {
        return atan2(val(x), val(y));
    }

    /**
     * Get the atan2(field, y) function
     *
     * @see #atan2(Field, Field)
     */
    public static Field<BigDecimal> atan2(Number x, Field<? extends Number> y) {
        return atan2(val(x), nullSafe(y));
    }

    /**
     * Get the atan2(field, y) function
      *
     * @see #atan2(Field, Field)
     */
    public static Field<BigDecimal> atan2(Field<? extends Number> x, Number y) {
        return atan2(nullSafe(x), val(y));
    }

    /**
     * Get the atan2(field, y) function
     * <p>
     * This renders the atan2 or atn2 function where available:
     * <code><pre>atan2([x], [y]) or
     * atn2([x], [y])</pre></code>
     */
    public static Field<BigDecimal> atan2(Field<? extends Number> x, Field<? extends Number> y) {
        return new Function<BigDecimal>(Term.ATAN2, SQLDataType.NUMERIC, nullSafe(x), nullSafe(y));
    }

    /**
     * Get the cosine(field) function
     *
     * @see #cos(Field)
     */
    public static Field<BigDecimal> cos(Number value) {
        return cos(val(value));
    }

    /**
     * Get the cosine(field) function
     * <p>
     * This renders the cos function where available:
     * <code><pre>cos([field])</pre></code>
     */
    public static Field<BigDecimal> cos(Field<? extends Number> field) {
        return function("cos", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the sine(field) function
     *
     * @see #sin(Field)
     */
    public static Field<BigDecimal> sin(Number value) {
        return sin(val(value));
    }

    /**
     * Get the sine(field) function
     * <p>
     * This renders the sin function where available:
     * <code><pre>sin([field])</pre></code>
     */
    public static Field<BigDecimal> sin(Field<? extends Number> field) {
        return function("sin", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the tangent(field) function
     *
     * @see #tan(Field)
     */
    public static Field<BigDecimal> tan(Number value) {
        return tan(val(value));
    }

    /**
     * Get the tangent(field) function
     * <p>
     * This renders the tan function where available:
     * <code><pre>tan([field])</pre></code>
     */
    public static Field<BigDecimal> tan(Field<? extends Number> field) {
        return function("tan", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the cotangent(field) function
     *
     * @see #cot(Field)
     */
    public static Field<BigDecimal> cot(Number value) {
        return cot(val(value));
    }

    /**
     * Get the cotangent(field) function
     * <p>
     * This renders the cot function where available:
     * <code><pre>cot([field])</pre></code> ... or simulates it elsewhere using
     * sin and cos: <code><pre>cos([field]) / sin([field])</pre></code>
     */
    public static Field<BigDecimal> cot(Field<? extends Number> field) {
        return new Cot(nullSafe(field));
    }

    /**
     * Get the hyperbolic sine function: sinh(field)
     *
     * @see #sinh(Field)
     */
    public static Field<BigDecimal> sinh(Number value) {
        return sinh(val(value));
    }

    /**
     * Get the hyperbolic sine function: sinh(field)
     * <p>
     * This renders the sinh function where available:
     * <code><pre>sinh([field])</pre></code> ... or simulates it elsewhere using
     * exp: <code><pre>(exp([field] * 2) - 1) / (exp([field] * 2))</pre></code>
     */
    public static Field<BigDecimal> sinh(Field<? extends Number> field) {
        return new Sinh(nullSafe(field));
    }

    /**
     * Get the hyperbolic cosine function: cosh(field)
     *
     * @see #cosh(Field)
     */
    public static Field<BigDecimal> cosh(Number value) {
        return cosh(val(value));
    }

    /**
     * Get the hyperbolic cosine function: cosh(field)
     * <p>
     * This renders the cosh function where available:
     * <code><pre>cosh([field])</pre></code> ... or simulates it elsewhere using
     * exp: <code><pre>(exp([field] * 2) + 1) / (exp([field] * 2))</pre></code>
     */
    public static Field<BigDecimal> cosh(Field<? extends Number> field) {
        return new Cosh(nullSafe(field));
    }

    /**
     * Get the hyperbolic tangent function: tanh(field)
     *
     * @see #tanh(Field)
     */
    public static Field<BigDecimal> tanh(Number value) {
        return tanh(val(value));
    }

    /**
     * Get the hyperbolic tangent function: tanh(field)
     * <p>
     * This renders the tanh function where available:
     * <code><pre>tanh([field])</pre></code> ... or simulates it elsewhere using
     * exp:
     * <code><pre>(exp([field] * 2) - 1) / (exp([field] * 2) + 1)</pre></code>
     */
    public static Field<BigDecimal> tanh(Field<? extends Number> field) {
        return new Tanh(nullSafe(field));
    }

    /**
     * Get the hyperbolic cotangent function: coth(field)
     *
     * @see #coth(Field)
     */
    public static Field<BigDecimal> coth(Number value) {
        return coth(val(value));
    }

    /**
     * Get the hyperbolic cotangent function: coth(field)
     * <p>
     * This is not supported by any RDBMS, but simulated using exp exp:
     * <code><pre>(exp([field] * 2) + 1) / (exp([field] * 2) - 1)</pre></code>
     */
    public static Field<BigDecimal> coth(Field<? extends Number> field) {
        field = nullSafe(field);
        return exp(field.mul(2)).add(1).div(exp(field.mul(2)).sub(1));
    }

    /**
     * Calculate degrees from radians from this field
     *
     * @see #deg(Field)
     */
    public static Field<BigDecimal> deg(Number value) {
        return deg(val(value));
    }

    /**
     * Calculate degrees from radians from this field
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([field])</pre></code> ... or simulates it elsewhere:
     * <code><pre>[field] * 180 / PI</pre></code>
     */
    public static Field<BigDecimal> deg(Field<? extends Number> field) {
        return new Degrees(nullSafe(field));
    }

    /**
     * Calculate radians from degrees from this field
     *
     * @see #rad(Field)
     */
    public static Field<BigDecimal> rad(Number value) {
        return rad(val(value));
    }

    /**
     * Calculate radians from degrees from this field
     * <p>
     * This renders the degrees function where available:
     * <code><pre>degrees([field])</pre></code> ... or simulates it elsewhere:
     * <code><pre>[field] * PI / 180</pre></code>
     */
    public static Field<BigDecimal> rad(Field<? extends Number> field) {
        return new Radians(nullSafe(field));
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
        return new Count(nullSafe(field), false);
    }

    /**
     * Get the count(distinct field) function
     */
    public static AggregateFunction<Integer> countDistinct(Field<?> field) {
        return new Count(nullSafe(field), true);
    }

    /**
     * Get the max value over a field: max(field)
     */
    public static <T> AggregateFunction<T> max(Field<T> field) {
        return new AggregateFunctionImpl<T>("max", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get the min value over a field: min(field)
     */
    public static <T> AggregateFunction<T> min(Field<T> field) {
        return new AggregateFunctionImpl<T>("min", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * Get the sum over a numeric field: sum(field)
     */
    public static AggregateFunction<BigDecimal> sum(Field<? extends Number> field) {
        return new AggregateFunctionImpl<BigDecimal>("sum", SQLDataType.NUMERIC, nullSafe(field));
    }

    /**
     * Get the average over a numeric field: avg(field)
     */
    public static AggregateFunction<BigDecimal> avg(Field<? extends Number> field) {
        return new AggregateFunctionImpl<BigDecimal>("avg", SQLDataType.NUMERIC, nullSafe(field));
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
        return new AggregateFunctionImpl<BigDecimal>("median", SQLDataType.NUMERIC, nullSafe(field));
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
        return new AggregateFunctionImpl<BigDecimal>(Term.STDDEV_POP, SQLDataType.NUMERIC, nullSafe(field));
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
        return new AggregateFunctionImpl<BigDecimal>(Term.STDDEV_SAMP, SQLDataType.NUMERIC, nullSafe(field));
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
        return new AggregateFunctionImpl<BigDecimal>(Term.VAR_POP, SQLDataType.NUMERIC, nullSafe(field));
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
        return new AggregateFunctionImpl<BigDecimal>(Term.VAR_SAMP, SQLDataType.NUMERIC, nullSafe(field));
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
        return new WindowFunction<T>("first_value", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * The <code>last_value(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lastValue(Field<T> field) {
        return new WindowFunction<T>("last_value", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * The <code>lead(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field) {
        return new WindowFunction<T>("lead", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * The <code>lead(field, offset) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lead(Field<T> field, int offset) {
        return new WindowFunction<T>("lead", nullSafeDataType(field), nullSafe(field), literal(offset));
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
        return lead(nullSafe(field), offset, val(defaultValue));
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
        return new WindowFunction<T>("lead", nullSafeDataType(field), nullSafe(field), literal(offset), nullSafe(defaultValue));
    }

    /**
     * The <code>lag(field) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field) {
        return new WindowFunction<T>("lag", nullSafeDataType(field), nullSafe(field));
    }

    /**
     * The <code>lag(field, offset) over ([analytic clause])</code> function.
     * <p>
     * Window functions are supported in DB2, Postgres, Oracle, SQL Server and
     * Sybase.
     */
    public static <T> WindowIgnoreNullsStep<T> lag(Field<T> field, int offset) {
        return new WindowFunction<T>("lag", nullSafeDataType(field), nullSafe(field), literal(offset));
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
        return lag(nullSafe(field), offset, val(defaultValue));
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
        return new WindowFunction<T>("lag", nullSafeDataType(field), nullSafe(field), literal(offset), nullSafe(defaultValue));
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
        return val(value, nullSafeDataType(field));
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
        FieldList result = new FieldList();

        if (values != null) {
            for (Object value : values) {

                // Fields can be mixed with constant values
                if (value instanceof Field<?>) {
                    result.add((Field<?>) value);
                }
                else {
                    result.add(val(value));
                }
            }
        }

        return result;
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
     * Null-safety of a field
     */
    static <T> Field<T> nullSafe(Field<T> field) {
        return field == null ? val((T) null) : field;
    }

    /**
     * Null-safety of a field
     */
    static Field<?>[] nullSafe(Field<?>... fields) {
        Field<?>[] result = new Field<?>[fields.length];

        for (int i = 0; i < fields.length; i++) {
            result[i] = nullSafe(fields[i]);
        }

        return result;
    }

    /**
     * Get a default data type if a field is null
     */
    @SuppressWarnings("unchecked")
    static <T> DataType<T> nullSafeDataType(Field<T> field) {
        return (DataType<T>) (field == null ? SQLDataType.OTHER : field.getDataType());
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
