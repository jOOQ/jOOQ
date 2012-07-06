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
package org.jooq;

import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;
import org.jooq.impl.Factory;

/**
 * The public API for the jOOQ {@link Factory}
 *
 * @author Sergey Epik
 * @author Lukas Eder
 * @see Factory
 */
public interface FactoryOperations extends Configuration {

    // -------------------------------------------------------------------------
    // XXX RenderContext and BindContext accessors
    // -------------------------------------------------------------------------

    /**
     * Render a QueryPart in the context of this factory
     * <p>
     * This is the same as calling <code>renderContext().render(part)</code>
     *
     * @param part The {@link QueryPart} to be rendered
     * @return The rendered SQL
     */
    String render(QueryPart part);

    /**
     * Render a QueryPart in the context of this factory, rendering bind
     * variables as named parameters.
     * <p>
     * This is the same as calling
     * <code>renderContext().namedParams(true).render(part)</code>
     *
     * @param part The {@link QueryPart} to be rendered
     * @return The rendered SQL
     */
    String renderNamedParams(QueryPart part);

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
    String renderInlined(QueryPart part);

    // -------------------------------------------------------------------------
    // XXX Attachable and Serializable API
    // -------------------------------------------------------------------------

    /**
     * Attach this <code>Factory</code> to some attachables
     */
    void attach(Attachable... attachables);

    /**
     * Attach this <code>Factory</code> to some attachables
     */
    void attach(Collection<Attachable> attachables);

    // -------------------------------------------------------------------------
    // XXX Access to the loader API
    // -------------------------------------------------------------------------

    /**
     * Create a new <code>Loader</code> object to load data from a CSV or XML
     * source
     */
    @Support
    <R extends TableRecord<R>> LoaderOptionsStep<R> loadInto(Table<R> table);

    // -------------------------------------------------------------------------
    // XXX Plain SQL object factory
    // -------------------------------------------------------------------------

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
    Query query(String sql);

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
    Query query(String sql, Object... bindings);

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
    Query query(String sql, QueryPart... parts);

    // -------------------------------------------------------------------------
    // XXX JDBC convenience methods
    // -------------------------------------------------------------------------

    /**
     * Fetch all data from a JDBC {@link ResultSet} and transform it to a jOOQ
     * {@link Result}. After fetching all data, the JDBC ResultSet will be
     * closed.
     *
     * @param rs The JDBC ResultSet to fetch data from
     * @return The resulting jOOQ Result
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    Result<Record> fetch(ResultSet rs) throws DataAccessException;

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
    Result<Record> fetchFromCSV(String string) throws DataAccessException;

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
    Result<Record> fetchFromCSV(String string, char delimiter) throws DataAccessException;

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
    <R extends Record> SimpleSelectWhereStep<R> selectFrom(Table<R> table);

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
    @Support
    SelectSelectStep select(Field<?>... fields);

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
     * @see Factory#zero()
     */
    @Support
    SelectSelectStep selectZero();

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
     * @see Factory#one()
     */
    @Support
    SelectSelectStep selectOne();

    /**
     * Create a new DSL select statement for <code>COUNT(*)</code>
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.selectCount()
     *       .from(table1)
     *       .join(table2).on(field1.equal(field2))
     *       .where(field1.greaterThan(100))
     *       .orderBy(field2)
     *       .execute();
     * </pre></code>
     */
    @Support
    SelectSelectStep selectCount();

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
    @Support
    SelectSelectStep selectDistinct(Field<?>... fields);

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
    @Support
    SelectSelectStep select(Collection<? extends Field<?>> fields);

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
    @Support
    SelectSelectStep selectDistinct(Collection<? extends Field<?>> fields);

    /**
     * Create a new {@link SelectQuery}
     */
    @Support
    SelectQuery selectQuery();

    /**
     * Create a new {@link SelectQuery}
     *
     * @param table The table to select data from
     * @return The new {@link SelectQuery}
     */
    @Support
    <R extends Record> SimpleSelectQuery<R> selectQuery(TableLike<R> table);

    /**
     * Create a new {@link InsertQuery}
     *
     * @param into The table to insert data into
     * @return The new {@link InsertQuery}
     */
    @Support
    <R extends Record> InsertQuery<R> insertQuery(Table<R> into);

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
    @Support
    <R extends Record> InsertSetStep<R> insertInto(Table<R> into);

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
    @Support
    <R extends Record> InsertValuesStep<R> insertInto(Table<R> into, Field<?>... fields);

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
    @Support
    <R extends Record> InsertValuesStep<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields);

    /**
     * Create a new DSL insert statement.
     * <p>
     * Example: <code><pre>
     * Factory create = new Factory();
     *
     * create.insertInto(table, create.select(1))
     *       .execute();
     * </pre></code>
     *
     * @deprecated - 2.0.3 - Use any of these methods instead:
     *             <ul>
     *             <li>{@link #insertInto(Table)} and
     *             {@link InsertSetStep#select(Select)}</li> <li>
     *             {@link #insertInto(Table, Field...)} and
     *             {@link InsertValuesStep#select(Select)}</li> <li>
     *             {@link #insertInto(Table, Collection)} and
     *             {@link InsertValuesStep#select(Select)}</li>
     *             </ul>
     */
    @Deprecated
    @Support
    <R extends Record> Insert<R> insertInto(Table<R> into, Select<?> select);

    /**
     * Create a new {@link UpdateQuery}
     *
     * @param table The table to update data into
     * @return The new {@link UpdateQuery}
     */
    @Support
    <R extends Record> UpdateQuery<R> updateQuery(Table<R> table);

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
    @Support
    <R extends Record> UpdateSetStep<R> update(Table<R> table);

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
     * <p>
     * Note: Using this method, you can also create an H2-specific MERGE
     * statement without field specification. See also
     * {@link #mergeInto(Table, Field...)}
     */
    @Support({ DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table);

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
    @Support({ DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record> MergeKeyStep<R> mergeInto(Table<R> table, Field<?>... fields);

    /**
     * Create a new DSL merge statement (H2-specific syntax)
     *
     * @see #mergeInto(Table, Field...)
     */
    @Support({ DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
    <R extends Record> MergeKeyStep<R> mergeInto(Table<R> table, Collection<? extends Field<?>> fields);

    /**
     * Create a new {@link DeleteQuery}
     *
     * @param table The table to delete data from
     * @return The new {@link DeleteQuery}
     */
    @Support
    <R extends Record> DeleteQuery<R> deleteQuery(Table<R> table);

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
    @Support
    <R extends Record> DeleteWhereStep<R> delete(Table<R> table);

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
    Batch batch(Query... queries);

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
    Batch batch(Collection<? extends Query> queries);

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
    BatchBindStep batch(Query query);

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
    Batch batchStore(UpdatableRecord<?>... records);

    /**
     * Execute a set of <code>INSERT</code> and <code>UPDATE</code> queries in
     * batch mode (with bind values).
     *
     * @see #batchStore(UpdatableRecord...)
     * @see Statement#executeBatch()
     */
    Batch batchStore(Collection<? extends UpdatableRecord<?>> records);

    // -------------------------------------------------------------------------
    // XXX DDL Statements
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
     * Most dialects implement the <code>TRUNCATE</code> statement. If it is not
     * supported, it is simulated using an equivalent <code>DELETE</code>
     * statement. This is particularly true for these dialects:
     * <ul>
     * <li> {@link SQLDialect#INGRES}</li>
     * <li> {@link SQLDialect#SQLITE}</li>
     * </ul>
     * <p>
     * Note, this statement is only supported in DSL mode. Immediate execution
     * is omitted for future extensibility of this command.
     */
    @Support
    <R extends Record> Truncate<R> truncate(Table<R> table);

    // -------------------------------------------------------------------------
    // XXX Other queries for identities and sequences
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
    BigInteger lastID() throws DataAccessException;

    /**
     * Convenience method to fetch the NEXTVAL for a sequence directly from this
     * {@link Factory}'s underlying JDBC {@link Connection}
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ CUBRID, DB2, DERBY, H2, HSQLDB, INGRES, ORACLE, POSTGRES, SYBASE })
    <T extends Number> T nextval(Sequence<T> sequence) throws DataAccessException;

    /**
     * Convenience method to fetch the CURRVAL for a sequence directly from this
     * {@link Factory}'s underlying JDBC {@link Connection}
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ CUBRID, DB2, H2, INGRES, ORACLE, POSTGRES, SYBASE })
    <T extends Number> T currval(Sequence<T> sequence) throws DataAccessException;

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
    @Support({ DB2, DERBY, H2, HSQLDB, MYSQL, SYBASE, ORACLE, POSTGRES, SYBASE })
    int use(Schema schema) throws DataAccessException;

    /**
     * Use a schema as the default schema of the underlying connection.
     *
     * @see #use(Schema)
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support({ DB2, DERBY, H2, HSQLDB, MYSQL, SYBASE, ORACLE, POSTGRES, SYBASE })
    int use(String schema) throws DataAccessException;

    // -------------------------------------------------------------------------
    // XXX Global Record factory
    // -------------------------------------------------------------------------

    /**
     * Create a new attached {@link UDTRecord}.
     *
     * @param <R> The generic record type
     * @param type The UDT describing records of type &lt;R&gt;
     * @return The new record
     */
    <R extends UDTRecord<R>> R newRecord(UDT<R> type);

    /**
     * Create a new {@link Record} that can be inserted into the corresponding
     * table.
     *
     * @param <R> The generic record type
     * @param table The table holding records of type &lt;R&gt;
     * @return The new record
     */
    <R extends TableRecord<R>> R newRecord(Table<R> table);

    /**
     * Create a new pre-filled {@link Record} that can be inserted into the
     * corresponding table.
     * <p>
     * This performs roughly the inverse operation of {@link Record#into(Class)}
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
    <R extends TableRecord<R>> R newRecord(Table<R> table, Object source) throws MappingException;

    // -------------------------------------------------------------------------
    // XXX Fast querying
    // -------------------------------------------------------------------------

    /**
     * Execute and return all records for
     * <code><pre>SELECT * FROM [table]</pre></code>
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends Record> Result<R> fetch(Table<R> table) throws DataAccessException;

    /**
     * Execute and return all records for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends Record> Result<R> fetch(Table<R> table, Condition condition) throws DataAccessException;

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table]</pre></code>
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends Record> R fetchOne(Table<R> table) throws DataAccessException;

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends Record> R fetchOne(Table<R> table, Condition condition) throws DataAccessException;

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table] LIMIT 1</pre></code>
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends Record> R fetchAny(Table<R> table) throws DataAccessException;

    /**
     * Insert one record
     * <code><pre>INSERT INTO [table] ... VALUES [record] </pre></code>
     *
     * @return The number of inserted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>> int executeInsert(Table<R> table, R record) throws DataAccessException;

    /**
     * Update a table
     * <code><pre>UPDATE [table] SET [modified values in record] </pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>> int executeUpdate(Table<R> table, R record) throws DataAccessException;

    /**
     * Update a table
     * <code><pre>UPDATE [table] SET [modified values in record] WHERE [condition]</pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>, T> int executeUpdate(Table<R> table, R record, Condition condition)
        throws DataAccessException;

    /**
     * Update one record in a table
     * <code><pre>UPDATE [table] SET [modified values in record]</pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>> int executeUpdateOne(Table<R> table, R record) throws DataAccessException;

    /**
     * Update one record in a table
     * <code><pre>UPDATE [table] SET [modified values in record] WHERE [condition]</pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>, T> int executeUpdateOne(Table<R> table, R record, Condition condition)
        throws DataAccessException;

    /**
     * Delete records from a table <code><pre>DELETE FROM [table]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>> int executeDelete(Table<R> table) throws DataAccessException;

    /**
     * Delete records from a table
     * <code><pre>DELETE FROM [table] WHERE [condition]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>, T> int executeDelete(Table<R> table, Condition condition) throws DataAccessException;

    /**
     * Delete one record in a table <code><pre>DELETE FROM [table]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>> int executeDeleteOne(Table<R> table) throws DataAccessException;

    /**
     * Delete one record in a table
     * <code><pre>DELETE FROM [table] WHERE [condition]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    @Support
    <R extends TableRecord<R>, T> int executeDeleteOne(Table<R> table, Condition condition) throws DataAccessException;

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
    Result<Record> fetch(String sql) throws DataAccessException;

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
    Result<Record> fetch(String sql, Object... bindings) throws DataAccessException;

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
    Result<Record> fetch(String sql, QueryPart... parts) throws DataAccessException;

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
    Cursor<Record> fetchLazy(String sql) throws DataAccessException;

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
    Cursor<Record> fetchLazy(String sql, QueryPart... parts) throws DataAccessException;

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
    Cursor<Record> fetchLazy(String sql, Object... bindings) throws DataAccessException;

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
    List<Result<Record>> fetchMany(String sql) throws DataAccessException;

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
    List<Result<Record>> fetchMany(String sql, Object... bindings) throws DataAccessException;

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
    List<Result<Record>> fetchMany(String sql, QueryPart... parts) throws DataAccessException;

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
    Record fetchOne(String sql) throws DataAccessException;

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
     */
    @Support
    Record fetchOne(String sql, Object... bindings) throws DataAccessException;

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
     */
    @Support
    Record fetchOne(String sql, QueryPart... parts) throws DataAccessException;

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
    int execute(String sql) throws DataAccessException;

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
    int execute(String sql, Object... bindings) throws DataAccessException;

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
    int execute(String sql, QueryPart... parts) throws DataAccessException;

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
    ResultQuery<Record> resultQuery(String sql) throws DataAccessException;

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
    ResultQuery<Record> resultQuery(String sql, Object... bindings);

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
    ResultQuery<Record> resultQuery(String sql, QueryPart... parts);

    // -------------------------------------------------------------------------
    // XXX Convenience methods accessing the underlying Connection
    // -------------------------------------------------------------------------

    /**
     * Convenience method to access {@link Connection#getTransactionIsolation()}
     */
    int getTransactionIsolation() throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#setTransactionIsolation(int)}
     */
    void setTransactionIsolation(int level) throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#getHoldability()}
     */
    int getHoldability() throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#setHoldability(int)}
     */
    void setHoldability(int holdability) throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#getAutoCommit()}
     */
    boolean getAutoCommit() throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#setAutoCommit(boolean)}
     */
    void setAutoCommit(boolean autoCommit) throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#releaseSavepoint(Savepoint)}
     */
    void releaseSavepoint(Savepoint savepoint) throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#setSavepoint(String)}
     */
    Savepoint setSavepoint(String name) throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#setSavepoint()}
     */
    Savepoint setSavepoint() throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#rollback(Savepoint)}
     */
    void rollback(Savepoint savepoint) throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#rollback()}
     */
    void rollback() throws DataAccessException;

    /**
     * Convenience method to access {@link Connection#commit()}
     */
    void commit() throws DataAccessException;
}
