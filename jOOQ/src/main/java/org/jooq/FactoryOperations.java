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
package org.jooq;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

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
    // RenderContext and BindContext accessors
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
    // Attachable and Serializable API
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
    // Access to the loader API
    // -------------------------------------------------------------------------

    /**
     * Create a new <code>Loader</code> object to load data from a CSV or XML
     * source
     */
    <R extends TableRecord<R>> LoaderOptionsStep<R> loadInto(Table<R> table);

    // -------------------------------------------------------------------------
    // Plain SQL object factory
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
    Query query(String sql);

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
    Query query(String sql, Object... bindings);

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
     * @throws DataAccessException if something went wrong executing the query
     */
    Result<Record> fetch(ResultSet rs) throws DataAccessException;

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
    SelectSelectStep selectDistinct(Collection<? extends Field<?>> fields);

    /**
     * Create a new {@link SelectQuery}
     */
    SelectQuery selectQuery();

    /**
     * Create a new {@link SelectQuery}
     *
     * @param table The table to select data from
     * @return The new {@link SelectQuery}
     */
    <R extends Record> SimpleSelectQuery<R> selectQuery(TableLike<R> table);

    /**
     * Create a new {@link InsertQuery}
     *
     * @param into The table to insert data into
     * @return The new {@link InsertQuery}
     */
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
    <R extends Record> InsertValuesStep<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields);

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
    <R extends Record> Insert<R> insertInto(Table<R> into, Select<?> select);

    /**
     * Create a new {@link UpdateQuery}
     *
     * @param table The table to update data into
     * @return The new {@link UpdateQuery}
     */
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
    <R extends Record> UpdateSetStep<R> update(Table<R> table);

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
    <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table);

    /**
     * Create a new {@link DeleteQuery}
     *
     * @param table The table to delete data from
     * @return The new {@link DeleteQuery}
     */
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
    <R extends Record> DeleteWhereStep<R> delete(Table<R> table);

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
    Batch batch(Query... queries);

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
    BatchBindStep batch(Query query);

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
    <R extends TableRecord<R>> Truncate<R> truncate(Table<R> table);

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
     * @throws DataAccessException if something went wrong executing the query
     */
    BigInteger lastID() throws DataAccessException;

    /**
     * Convenience method to fetch the NEXTVAL for a sequence directly from this
     * {@link Factory}'s underlying JDBC {@link Connection}
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    <T extends Number> T nextval(Sequence<T> sequence) throws DataAccessException;

    /**
     * Convenience method to fetch the CURRVAL for a sequence directly from this
     * {@link Factory}'s underlying JDBC {@link Connection}
     *
     * @throws DataAccessException if something went wrong executing the query
     */
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
    int use(Schema schema) throws DataAccessException;

    /**
     * Use a schema as the default schema of the underlying connection.
     *
     * @see #use(Schema)
     * @throws DataAccessException if something went wrong executing the query
     */
    int use(String schema) throws DataAccessException;

    // -------------------------------------------------------------------------
    // Global Record factory
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
     * @throws MappingException wrapping any reflection or data type conversion exception that might
     *             have occurred while mapping records
     * @see Record#from(Object)
     * @see Record#into(Class)
     */
    <R extends TableRecord<R>> R newRecord(Table<R> table, Object source) throws MappingException;

    // -------------------------------------------------------------------------
    // Fast querying
    // -------------------------------------------------------------------------

    /**
     * Execute and return all records for
     * <code><pre>SELECT * FROM [table]</pre></code>
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends Record> Result<R> fetch(Table<R> table) throws DataAccessException;

    /**
     * Execute and return all records for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends Record> Result<R> fetch(Table<R> table, Condition condition) throws DataAccessException;

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table]</pre></code>
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends Record> R fetchOne(Table<R> table) throws DataAccessException;

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table] WHERE [condition] </pre></code>
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends Record> R fetchOne(Table<R> table, Condition condition) throws DataAccessException;

    /**
     * Execute and return zero or one record for
     * <code><pre>SELECT * FROM [table] LIMIT 1</pre></code>
     *
     * @return The record or <code>null</code> if no record was returned
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends Record> R fetchAny(Table<R> table) throws DataAccessException;

    /**
     * Insert one record
     * <code><pre>INSERT INTO [table] ... VALUES [record] </pre></code>
     *
     * @return The number of inserted records
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends TableRecord<R>> int executeInsert(Table<R> table, R record) throws DataAccessException;

    /**
     * Update a table
     * <code><pre>UPDATE [table] SET [modified values in record] </pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends TableRecord<R>> int executeUpdate(Table<R> table, R record) throws DataAccessException;

    /**
     * Update a table
     * <code><pre>UPDATE [table] SET [modified values in record] WHERE [condition]</pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends TableRecord<R>, T> int executeUpdate(Table<R> table, R record, Condition condition)
        throws DataAccessException;

    /**
     * Update one record in a table
     * <code><pre>UPDATE [table] SET [modified values in record]</pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends TableRecord<R>> int executeUpdateOne(Table<R> table, R record) throws DataAccessException;

    /**
     * Update one record in a table
     * <code><pre>UPDATE [table] SET [modified values in record] WHERE [condition]</pre></code>
     *
     * @return The number of updated records
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends TableRecord<R>, T> int executeUpdateOne(Table<R> table, R record, Condition condition)
        throws DataAccessException;

    /**
     * Delete records from a table <code><pre>DELETE FROM [table]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends TableRecord<R>> int executeDelete(Table<R> table) throws DataAccessException;

    /**
     * Delete records from a table
     * <code><pre>DELETE FROM [table] WHERE [condition]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends TableRecord<R>, T> int executeDelete(Table<R> table, Condition condition) throws DataAccessException;

    /**
     * Delete one record in a table <code><pre>DELETE FROM [table]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
    <R extends TableRecord<R>> int executeDeleteOne(Table<R> table) throws DataAccessException;

    /**
     * Delete one record in a table
     * <code><pre>DELETE FROM [table] WHERE [condition]</pre></code>
     *
     * @return The number of deleted records
     * @throws DataAccessException if something went wrong executing the query
     */
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
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    Result<Record> fetch(String sql) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL. There must be as many binding
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
     * @return A query wrapping the plain SQL
     * @throws DataAccessException if something went wrong executing the query
     */
    Result<Record> fetch(String sql, Object... bindings) throws DataAccessException;

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
     * @throws DataAccessException if something went wrong executing the query
     */
    List<Result<Record>> fetchMany(String sql) throws DataAccessException;

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
     * @throws DataAccessException if something went wrong executing the query
     */
    List<Result<Record>> fetchMany(String sql, Object... bindings) throws DataAccessException;

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
     * @return The results from the executed query
     * @throws DataAccessException if something went wrong executing the query
     */
    Record fetchOne(String sql) throws DataAccessException;

    /**
     * Execute a new query holding plain SQL. There must be as many binding
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
     * @return A query wrapping the plain SQL
     * @throws DataAccessException if something went wrong executing the query
     */
    Record fetchOne(String sql, Object... bindings) throws DataAccessException;
}
