/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.Tools.EMPTY_FIELD;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import org.jooq.CloseableResultQuery;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Delete;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Table;
import org.jooq.Update;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;

import org.jetbrains.annotations.NotNull;

/**
 * A wrapped DML query ({@link Insert}, {@link Update}, {@link Delete}) that
 * works like a {@link ResultQuery}.
 *
 * @author Lukas Eder
 */
abstract class AbstractDMLQueryAsResultQuery<R extends Record, Q extends AbstractDMLQuery<R>>
extends
    AbstractQueryPart
implements
    ResultQueryTrait<R>
{
    final Q                        delegate;
    final boolean                  returningResult;
    Table<?>                       coerceTable;
    Collection<? extends Field<?>> coerceFields;

    AbstractDMLQueryAsResultQuery(Q delegate, boolean returningResult) {
        this.delegate = delegate;
        this.returningResult = returningResult;
    }

    final Q getDelegate() {
        return delegate;
    }

    // TODO: Refactor this coercion, share logic with AbstractResultQuery

    @Override
    public final Field<?>[] getFields(ThrowingSupplier<? extends ResultSetMetaData, SQLException> rs) throws SQLException {
        Field<?>[] f = getFields();
        return f != null ? f : delegate.getFields(rs);
    }

    @Override
    public final Field<?>[] getFields() {
        if (coerceFields != null && !coerceFields.isEmpty())
            return coerceFields.toArray(EMPTY_FIELD);
        else
            return delegate.returningResolvedAsterisks.toArray(EMPTY_FIELD);
    }

    @Override
    public final <X extends Record> CloseableResultQuery<X> coerce(Table<X> table) {
        this.coerceTable = table;
        return (CloseableResultQuery<X>) coerce(Arrays.asList(table.fields()));
    }

    @Override
    public final CloseableResultQuery<Record> coerce(Collection<? extends Field<?>> fields) {
        this.coerceFields = fields;
        return (CloseableResultQuery<Record>) this;
    }

    @Override
    public final Class<? extends R> getRecordType() {
        if (coerceTable != null)
            return (Class<? extends R>) coerceTable.getRecordType();
        else if (returningResult)
            return (Class<? extends R>) delegate.getRecordType();
        else
            return delegate.table().getRecordType();
    }

    @Override
    public final Result<R> fetch() {
        delegate.execute();
        return (@NotNull Result<R>) getResult();
    }

    @Override
    public final int execute() throws DataAccessException {
        return delegate.execute();
    }

    @Override
    public final CompletionStage<Integer> executeAsync() {
        return delegate.executeAsync();
    }

    @Override
    public final CompletionStage<Integer> executeAsync(Executor executor) {
        return delegate.executeAsync(executor);
    }

    @Override
    public final long executeLarge() throws DataAccessException {
        return delegate.executeLarge();
    }

    @Override
    public final CompletionStage<Long> executeLargeAsync() {
        return delegate.executeLargeAsync();
    }

    @Override
    public final CompletionStage<Long> executeLargeAsync(Executor executor) {
        return delegate.executeLargeAsync(executor);
    }

    @Override
    public final boolean isExecutable() {
        return delegate.isExecutable();
    }

    @Override
    public final String getSQL() {
        return delegate.getSQL();
    }

    @Override
    public final String getSQL(ParamType paramType) {
        return delegate.getSQL(paramType);
    }

    @Override
    public final List<Object> getBindValues() {
        return delegate.getBindValues();
    }

    @Override
    public final Map<String, Param<?>> getParams() {
        return delegate.getParams();
    }

    @Override
    public final Param<?> getParam(String name) {
        return delegate.getParam(name);
    }

    @Override
    public final void close() throws DataAccessException {
        delegate.close();
    }

    @Override
    public final void cancel() throws DataAccessException {
        delegate.cancel();
    }

    @Override
    public final void attach(Configuration configuration) {
        delegate.attach(configuration);
    }

    @Override
    public final void detach() {
        delegate.detach();
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate);
    }

    @Override
    public final Configuration configuration() {
        return delegate.configuration();
    }

    @Override
    public final Result<R> getResult() {
        return (Result<R>) (returningResult ? delegate.getResult() : delegate.getReturnedRecords());
    }

    @Override
    public final CloseableResultQuery<R> bind(String param, Object value) throws IllegalArgumentException, DataTypeException {
        delegate.bind(param, value);
        return this;
    }

    @Override
    public final CloseableResultQuery<R> bind(int index, Object value) throws IllegalArgumentException, DataTypeException {
        delegate.bind(index, value);
        return this;
    }

    @Override
    public final CloseableResultQuery<R> poolable(boolean poolable) {
        delegate.poolable(poolable);
        return this;
    }

    @Override
    public final CloseableResultQuery<R> queryTimeout(int timeout) {
        delegate.queryTimeout(timeout);
        return this;
    }

    @Override
    public final CloseableResultQuery<R> keepStatement(boolean keepStatement) {
        delegate.keepStatement(keepStatement);
        return this;
    }

    @Override
    public final CloseableResultQuery<R> maxRows(int rows) {
        return this;
    }

    @Override
    public final CloseableResultQuery<R> largeMaxRows(long rows) {
        return this;
    }

    @Override
    public final CloseableResultQuery<R> fetchSize(int rows) {
        return this;
    }

    @Override
    public final CloseableResultQuery<R> resultSetConcurrency(int resultSetConcurrency) {
        return this;
    }

    @Override
    public final CloseableResultQuery<R> resultSetType(int resultSetType) {
        return this;
    }

    @Override
    public final CloseableResultQuery<R> resultSetHoldability(int resultSetHoldability) {
        return this;
    }
}