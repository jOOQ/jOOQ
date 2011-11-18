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
package org.jooq.util.spring;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.jooq.Attachable;
import org.jooq.Batch;
import org.jooq.BatchBindStep;
import org.jooq.Condition;
import org.jooq.DeleteQuery;
import org.jooq.DeleteWhereStep;
import org.jooq.FactoryOperations;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.InsertQuery;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStep;
import org.jooq.LoaderOptionsStep;
import org.jooq.MergeUsingStep;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.Record;
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
import org.jooq.exception.DataAccessException;
import org.jooq.impl.Factory;
import org.jooq.tools.JooqLogger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/**
 * A spring-enabled proxy for the jOOQ {@link Factory}
 *
 * @author Sergey Epik
 * @author Lukas Eder
 */
public class FactoryProxy implements FactoryOperations, MethodInterceptor {

    /**
     * Generated UID
     */
    private static final long              serialVersionUID  = -8475057043526340066L;
    private static JooqLogger              log               = JooqLogger.getLogger(FactoryProxy.class);
    private static ThreadLocal<Factory>    currentFactory    = new ThreadLocal<Factory>();
    private static ThreadLocal<Connection> currentConnection = new ThreadLocal<Connection>();

    private transient DataSource           dataSource;
    private SQLDialect                     dialect;
    private SchemaMapping                  schemaMapping;
    private NativeJdbcExtractor            nativeJdbcExtractor;

    // -------------------------------------------------------------------------
    // Spring injection API
    // -------------------------------------------------------------------------

    @Required
    public final void setDialect(SQLDialect dialect) {
        this.dialect = dialect;
    }

    @Required
    public final void setSchemaMapping(SchemaMapping schemaMapping) {
        this.schemaMapping = schemaMapping;
    }

    @Required
    public final void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public final DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * Set a NativeJdbcExtractor to extract native JDBC objects from wrapped
     * handles. Useful if native Statement and/or ResultSet handles are expected
     * for casting to database-specific implementation classes, but a connection
     * pool that wraps JDBC objects is used (note: <i>any</i> pool will return
     * wrapped Connections).
     */
    public final void setNativeJdbcExtractor(NativeJdbcExtractor extractor) {
        this.nativeJdbcExtractor = extractor;
    }

    /**
     * Return the current NativeJdbcExtractor implementation.
     */
    public final NativeJdbcExtractor getNativeJdbcExtractor() {
        return this.nativeJdbcExtractor;
    }

    // -------------------------------------------------------------------------
    // Configuration API
    // -------------------------------------------------------------------------

    @Override
    public final SQLDialect getDialect() {
        return dialect;
    }

    @Override
    public final SchemaMapping getSchemaMapping() {
        return schemaMapping;
    }

    @Override
    public final Connection getConnection() {
        return getDelegate().getConnection();
    }

    // -------------------------------------------------------------------------
    // FactoryOperations API
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
    public final <R extends TableRecord<R>> InsertQuery<R> insertQuery(Table<R> into) {
        return getDelegate().insertQuery(into);
    }

    @Override
    public final <R extends TableRecord<R>> InsertSetStep<R> insertInto(Table<R> into) {
        return getDelegate().insertInto(into);
    }

    @Override
    public final <R extends TableRecord<R>> InsertValuesStep<R> insertInto(Table<R> into, Field<?>... fields) {
        return getDelegate().insertInto(into, fields);
    }

    @Override
    public final <R extends TableRecord<R>> InsertValuesStep<R> insertInto(Table<R> into, Collection<? extends Field<?>> fields) {
        return getDelegate().insertInto(into, fields);
    }

    @Override
    public final <R extends TableRecord<R>> Insert<R> insertInto(Table<R> into, Select<?> select) {
        return getDelegate().insertInto(into, select);
    }

    @Override
    public final <R extends TableRecord<R>> UpdateQuery<R> updateQuery(Table<R> table) {
        return getDelegate().updateQuery(table);
    }

    @Override
    public final <R extends TableRecord<R>> UpdateSetStep<R> update(Table<R> table) {
        return getDelegate().update(table);
    }

    @Override
    public final <R extends TableRecord<R>> MergeUsingStep<R> mergeInto(Table<R> table) {
        return getDelegate().mergeInto(table);
    }

    @Override
    public final <R extends TableRecord<R>> DeleteQuery<R> deleteQuery(Table<R> table) {
        return getDelegate().deleteQuery(table);
    }

    @Override
    public final <R extends TableRecord<R>> DeleteWhereStep<R> delete(Table<R> table) {
        return getDelegate().delete(table);
    }

    @Override
    public final Batch batch(Query... queries) {
        return getDelegate().batch(queries);
    }

    @Override
    public final BatchBindStep batch(Query query) {
        return getDelegate().batch(query);
    }

    @Override
    public <R extends TableRecord<R>> Truncate<R> truncate(Table<R> table) {
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
    public final Result<Record> fetch(String sql) {
        return getDelegate().fetch(sql);
    }

    @Override
    public final Result<Record> fetch(String sql, Object... bindings) {
        return getDelegate().fetch(sql, bindings);
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
    public final Record fetchOne(String sql) {
        return getDelegate().fetchOne(sql);
    }

    @Override
    public final Record fetchOne(String sql, Object... bindings) {
        return getDelegate().fetchOne(sql, bindings);
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
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        }
        finally {
            cleanLocalFactory();
        }
    }

    private final Factory getDelegate() {
        Factory factory = currentFactory.get();

        if (factory == null) {
            factory = createFactory();
        }

        return factory;
    }

    private final Factory createFactory() {
        try {
            Class<? extends Factory> clazz = getDialect().getFactory();
            Constructor<? extends Factory> constructor = clazz.getConstructor(Connection.class, SchemaMapping.class);
            Connection con = DataSourceUtils.getConnection(getDataSource());
            Connection conToUse = con;

            if (nativeJdbcExtractor != null) {
                conToUse = nativeJdbcExtractor.getNativeConnection(con);
            }

            Factory factory = constructor.newInstance(conToUse, getSchemaMapping());
            currentFactory.set(factory);
            currentConnection.set(con);

            return factory;
        }
        catch (Exception exc) {
            throw new DataAccessException("Failed to create jOOQ Factory", exc);
        }
    }

    private final void cleanLocalFactory() {
        Connection conn = currentConnection.get();

        if (conn != null) {
            try {
                conn.close();
            }
            catch (SQLException ignore) {
                log.warn("Could not close JDBC Connection", ignore);
            }
        }

        currentFactory.remove();
        currentConnection.remove();
    }
}
