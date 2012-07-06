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

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.jooq.Attachable;
import org.jooq.Batch;
import org.jooq.BatchBindStep;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DeleteQuery;
import org.jooq.DeleteWhereStep;
import org.jooq.FactoryOperations;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.InsertQuery;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStep;
import org.jooq.LoaderOptionsStep;
import org.jooq.MergeKeyStep;
import org.jooq.MergeUsingStep;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.Schema;
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
import org.jooq.UpdatableRecord;
import org.jooq.UpdateQuery;
import org.jooq.UpdateSetStep;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;

/**
 * Thread safe proxy for {@link Factory}.
 * <p>
 * The <code>FactoryProxy</code> operates as a thread-safe proxy for jOOQ's
 * {@link Factory} objects. Instead of wrapping JDBC {@link Connection} objects,
 * the <code>FactoryProxy</code> wraps a transaction-aware {@link DataSource}
 * and creates a new <code>Factory</code> with a new <code>Connection</code>
 * every time you use any of the operations from {@link FactoryOperations}
 * <p>
 * Refer to the jOOQ manual to see possible operation modes for jOOQ with
 * Spring. Note that this implementation of a <code>FactoryProxy</code> might be
 * re-designed in jOOQ 3.0. Please consider this functionality as being
 * EXPERIMENTAL
 *
 * @author Sergey Epik
 * @author Lukas Eder
 * @deprecated - Use the newly {@link DataSource}-enabled {@link Factory}
 *             constructors instead, e.g.
 *             {@link Factory#Factory(DataSource, SQLDialect)} or
 *             {@link Factory#Factory(DataSource, SQLDialect, Settings)}
 */
@Deprecated
public final class FactoryProxy implements FactoryOperations {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -8475057043526340066L;

    private transient DataSource dataSource;
    private SQLDialect dialect;
    private Settings settings;

    // -------------------------------------------------------------------------
    // Injected configuration
    // -------------------------------------------------------------------------

    public final void setDialect(SQLDialect dialect) {
        this.dialect = dialect;
    }

    public final void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Override
    public final void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public final DataSource getDataSource() {
        return this.dataSource;
    }

    // -------------------------------------------------------------------------
    // Configuration API
    // -------------------------------------------------------------------------

    @Override
    public final SQLDialect getDialect() {
        return dialect;
    }

    @Override
    @Deprecated
    public final org.jooq.SchemaMapping getSchemaMapping() {
        return null;
    }

    @Override
    public final Settings getSettings() {
        return settings;
    }

    @Override
    public final Connection getConnection() {
        return getDelegate().getConnection();
    }

    @Override
    public final void setConnection(Connection connection) {
        getDelegate().setConnection(connection);
    }

    @Override
    public final Map<String, Object> getData() {
        return getDelegate().getData();
    }

    @Override
    public final Object getData(String key) {
        return getDelegate().getData(key);
    }

    @Override
    public final Object setData(String key, Object value) {
        return getDelegate().setData(key, value);
    }

    // -------------------------------------------------------------------------
    // XXX FactoryOperations API
    // -------------------------------------------------------------------------

    @Override
    public final <R extends TableRecord<R>> LoaderOptionsStep<R> loadInto(Table<R> table) {
        return getDelegate().loadInto(table);
    }

    @Override
    public final String render(QueryPart part) {
        return getDelegate().render(part);
    }

    @Override
    public final String renderNamedParams(QueryPart part) {
        return getDelegate().renderNamedParams(part);
    }

    @Override
    public final String renderInlined(QueryPart part) {
        return getDelegate().renderInlined(part);
    }

    @Override
    public final void attach(Attachable... attachables) {
        getDelegate().attach(attachables);
    }

    @Override
    public final void attach(Collection<Attachable> attachables) {
        getDelegate().attach(attachables);
    }

    @Override
    public final Query query(String sql) {
        return getDelegate().query(sql);
    }

    @Override
    public final Query query(String sql, Object... bindings) {
        return getDelegate().query(sql, bindings);
    }

    @Override
    public final Query query(String sql, QueryPart... parts) {
        return getDelegate().query(sql, parts);
    }

    @Override
    public final <R extends Record> SimpleSelectWhereStep<R> selectFrom(Table<R> table) {
        return getDelegate().selectFrom(table);
    }

    @Override
    public final SelectSelectStep select(Field<?>... fields) {
        return getDelegate().select(fields);
    }

    @Override
    public final SelectSelectStep selectZero() {
        return getDelegate().selectZero();
    }

    @Override
    public final SelectSelectStep selectOne() {
        return getDelegate().selectOne();
    }

    @Override
    public final SelectSelectStep selectCount() {
        return getDelegate().selectCount();
    }

    @Override
    public final SelectSelectStep selectDistinct(Field<?>... fields) {
        return getDelegate().selectDistinct(fields);
    }

    @Override
    public final SelectSelectStep select(Collection<? extends Field<?>> fields) {
        return getDelegate().select(fields);
    }

    @Override
    public final SelectSelectStep selectDistinct(Collection<? extends Field<?>> fields) {
        return getDelegate().selectDistinct(fields);
    }

    @Override
    public final SelectQuery selectQuery() {
        return getDelegate().selectQuery();
    }

    @Override
    public final <R extends Record> SimpleSelectQuery<R> selectQuery(TableLike<R> table) {
        return getDelegate().selectQuery(table);
    }

    @Override
    public final <R extends Record> InsertQuery<R> insertQuery(Table<R> into) {
        return getDelegate().insertQuery(into);
    }

    @Override
    public final <R extends Record> InsertSetStep<R> insertInto(Table<R> into) {
        return getDelegate().insertInto(into);
    }

    @Override
    public final <R extends Record> InsertValuesStep<R> insertInto(Table<R> into, Field<?>... fields) {
        return getDelegate().insertInto(into, fields);
    }

    @Override
    public final <R extends Record> InsertValuesStep<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields) {
        return getDelegate().insertInto(into, fields);
    }

    @Override
    @Deprecated
    public final <R extends Record> Insert<R> insertInto(Table<R> into, Select<?> select) {
        return getDelegate().insertInto(into, select);
    }

    @Override
    public final <R extends Record> UpdateQuery<R> updateQuery(Table<R> table) {
        return getDelegate().updateQuery(table);
    }

    @Override
    public final <R extends Record> UpdateSetStep<R> update(Table<R> table) {
        return getDelegate().update(table);
    }

    @Override
    public final <R extends Record> MergeUsingStep<R> mergeInto(Table<R> table) {
        return getDelegate().mergeInto(table);
    }

    @Override
    public final <R extends Record> MergeKeyStep<R> mergeInto(Table<R> table, Field<?>... fields) {
        return getDelegate().mergeInto(table, fields);
    }

    @Override
    public final <R extends Record> MergeKeyStep<R> mergeInto(Table<R> table, Collection<? extends Field<?>> fields) {
        return getDelegate().mergeInto(table, fields);
    }

    @Override
    public final <R extends Record> DeleteQuery<R> deleteQuery(Table<R> table) {
        return getDelegate().deleteQuery(table);
    }

    @Override
    public final <R extends Record> DeleteWhereStep<R> delete(Table<R> table) {
        return getDelegate().delete(table);
    }

    @Override
    public final Batch batch(Query... queries) {
        return getDelegate().batch(queries);
    }

    @Override
    public final Batch batch(Collection<? extends Query> queries) {
        return getDelegate().batch(queries);
    }

    @Override
    public final BatchBindStep batch(Query query) {
        return getDelegate().batch(query);
    }

    @Override
    public final Batch batchStore(UpdatableRecord<?>... records) {
        return getDelegate().batchStore(records);
    }

    @Override
    public final Batch batchStore(Collection<? extends UpdatableRecord<?>> records) {
        return getDelegate().batchStore(records);
    }

    @Override
    public final <R extends Record> Truncate<R> truncate(Table<R> table) {
        return getDelegate().truncate(table);
    }

    @Override
    public final <R extends UDTRecord<R>> R newRecord(UDT<R> type) {
        return getDelegate().newRecord(type);
    }

    @Override
    public final <R extends TableRecord<R>> R newRecord(Table<R> table) {
        return getDelegate().newRecord(table);
    }

    @Override
    public final <R extends TableRecord<R>> R newRecord(Table<R> table, Object source) {
        return getDelegate().newRecord(table, source);
    }

    @Override
    public final Result<Record> fetch(ResultSet rs) {
        return getDelegate().fetch(rs);
    }

    @Override
    public final Result<Record> fetchFromCSV(String string) {
        return null;
    }

    @Override
    public final Result<Record> fetchFromCSV(String string, char delimiter) {
        return null;
    }

    @Override
    public final Result<Record> fetch(String sql) {
        return getDelegate().fetch(sql);
    }

    @Override
    public final Result<Record> fetch(String sql, Object... bindings) {
        return getDelegate().fetch(sql, bindings);
    }

    @Override
    public final Result<Record> fetch(String sql, QueryPart... parts) {
        return getDelegate().fetch(sql, parts);
    }

    @Override
    public final Cursor<Record> fetchLazy(String sql) throws DataAccessException {
        return getDelegate().fetchLazy(sql);
    }

    @Override
    public final Cursor<Record> fetchLazy(String sql, Object... bindings) throws DataAccessException {
        return getDelegate().fetchLazy(sql, bindings);
    }

    @Override
    public final Cursor<Record> fetchLazy(String sql, QueryPart... parts) throws DataAccessException {
        return getDelegate().fetchLazy(sql, parts);
    }

    @Override
    public final List<Result<Record>> fetchMany(String sql) {
        return getDelegate().fetchMany(sql);
    }

    @Override
    public final List<Result<Record>> fetchMany(String sql, Object... bindings) {
        return getDelegate().fetchMany(sql, bindings);
    }

    @Override
    public final List<Result<Record>> fetchMany(String sql, QueryPart... parts) {
        return getDelegate().fetchMany(sql, parts);
    }

    @Override
    public final Record fetchOne(String sql) {
        return getDelegate().fetchOne(sql);
    }

    @Override
    public final Record fetchOne(String sql, Object... bindings) {
        return getDelegate().fetchOne(sql, bindings);
    }

    @Override
    public final Record fetchOne(String sql, QueryPart... parts) {
        return getDelegate().fetchOne(sql, parts);
    }

    @Override
    public final int execute(String sql) throws DataAccessException {
        return getDelegate().execute(sql);
    }

    @Override
    public final int execute(String sql, Object... bindings) throws DataAccessException {
        return getDelegate().execute(sql, bindings);
    }

    @Override
    public final int execute(String sql, QueryPart... parts) throws DataAccessException {
        return getDelegate().execute(sql, parts);
    }

    @Override
    public final ResultQuery<Record> resultQuery(String sql) throws DataAccessException {
        return getDelegate().resultQuery(sql);
    }

    @Override
    public final ResultQuery<Record> resultQuery(String sql, Object... bindings) throws DataAccessException {
        return getDelegate().resultQuery(sql, bindings);
    }

    @Override
    public final ResultQuery<Record> resultQuery(String sql, QueryPart... parts) {
        return getDelegate().resultQuery(sql, parts);
    }

    @Override
    public final BigInteger lastID() {
        return getDelegate().lastID();
    }

    @Override
    public final <T extends Number> T nextval(Sequence<T> sequence) {
        return getDelegate().nextval(sequence);
    }

    @Override
    public final <T extends Number> T currval(Sequence<T> sequence) {
        return getDelegate().currval(sequence);
    }

    @Override
    public final int use(Schema schema) {
        return getDelegate().use(schema);
    }

    @Override
    public final int use(String schema) {
        return getDelegate().use(schema);
    }

    @Override
    public final <R extends Record> Result<R> fetch(Table<R> table) {
        return getDelegate().fetch(table);
    }

    @Override
    public final <R extends Record> Result<R> fetch(Table<R> table, Condition condition) {
        return getDelegate().fetch(table, condition);
    }

    @Override
    public final <R extends Record> R fetchOne(Table<R> table) {
        return getDelegate().fetchOne(table);
    }

    @Override
    public final <R extends Record> R fetchOne(Table<R> table, Condition condition) {
        return getDelegate().fetchOne(table, condition);
    }

    @Override
    public final <R extends Record> R fetchAny(Table<R> table) {
        return getDelegate().fetchAny(table);
    }

    @Override
    public final <R extends TableRecord<R>> int executeInsert(Table<R> table, R record) {
        return getDelegate().executeInsert(table, record);
    }

    @Override
    public final <R extends TableRecord<R>> int executeUpdate(Table<R> table, R record) {
        return getDelegate().executeUpdate(table, record);
    }

    @Override
    public final <R extends TableRecord<R>, T> int executeUpdate(Table<R> table, R record, Condition condition) {
        return getDelegate().executeUpdate(table, record, condition);
    }

    @Override
    public final <R extends TableRecord<R>> int executeUpdateOne(Table<R> table, R record) {
        return getDelegate().executeUpdateOne(table, record);
    }

    @Override
    public final <R extends TableRecord<R>, T> int executeUpdateOne(Table<R> table, R record, Condition condition) {
        return getDelegate().executeUpdateOne(table, record, condition);
    }

    @Override
    public final <R extends TableRecord<R>> int executeDelete(Table<R> table) {
        return getDelegate().executeDelete(table);
    }

    @Override
    public final <R extends TableRecord<R>, T> int executeDelete(Table<R> table, Condition condition) {
        return getDelegate().executeDelete(table, condition);
    }

    @Override
    public final <R extends TableRecord<R>> int executeDeleteOne(Table<R> table) {
        return getDelegate().executeDeleteOne(table);
    }

    @Override
    public final <R extends TableRecord<R>, T> int executeDeleteOne(Table<R> table, Condition condition) {
        return getDelegate().executeDeleteOne(table, condition);
    }

    @Override
    public final int getTransactionIsolation() throws DataAccessException {
        return getDelegate().getTransactionIsolation();
    }

    @Override
    public final void setTransactionIsolation(int level) throws DataAccessException {
        getDelegate().setTransactionIsolation(level);
    }

    @Override
    public final int getHoldability() throws DataAccessException {
        return getDelegate().getHoldability();
    }

    @Override
    public final void setHoldability(int holdability) throws DataAccessException {
        getDelegate().setHoldability(holdability);
    }

    @Override
    public final boolean getAutoCommit() throws DataAccessException {
        return getDelegate().getAutoCommit();
    }

    @Override
    public final void setAutoCommit(boolean autoCommit) throws DataAccessException {
        getDelegate().setAutoCommit(autoCommit);
    }

    @Override
    public final void releaseSavepoint(Savepoint savepoint) throws DataAccessException {
        getDelegate().releaseSavepoint(savepoint);
    }

    @Override
    public final Savepoint setSavepoint(String name) throws DataAccessException {
        return getDelegate().setSavepoint(name);
    }

    @Override
    public final Savepoint setSavepoint() throws DataAccessException {
        return getDelegate().setSavepoint();
    }

    @Override
    public final void rollback(Savepoint savepoint) throws DataAccessException {
        getDelegate().rollback(savepoint);
    }

    @Override
    public final void rollback() throws DataAccessException {
        getDelegate().rollback();
    }

    @Override
    public final void commit() throws DataAccessException {
        getDelegate().commit();
    }

    private FactoryOperations getDelegate() {
        if (dataSource == null || dialect == null) {
            throw new DataAccessException("Both dataSource and dialect properties should be set");
        }
        try {
            Class<? extends Factory> clazz = getDialect().getFactory();
            Connection con = getDataSource().getConnection();

            Constructor<? extends Factory> constructor;
            if (settings == null) {
                constructor = clazz.getConstructor(Connection.class);
                return constructor.newInstance(con);
            } else {
                constructor = clazz.getConstructor(Connection.class, Settings.class);
                return constructor.newInstance(con, settings);
            }
        } catch (Exception exc) {
            throw new DataAccessException("Failed to create jOOQ Factory", exc);
        }
    }
}
