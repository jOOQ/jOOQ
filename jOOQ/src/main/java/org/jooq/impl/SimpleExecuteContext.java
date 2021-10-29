/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteType;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Routine;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A simple implementation of {@link ExecuteContext} containing
 * {@link #configuration()} and {@link #data()} where no actual context is
 * available.
 *
 * @author Lukas Eder
 */
final class SimpleExecuteContext extends AbstractScope implements ExecuteContext {

    SimpleExecuteContext(Configuration configuration, Map<Object, Object> data) {
        super(configuration, data);
    }

    @Override
    public final Connection connection() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final ExecuteType type() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final Query query() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final Query[] batchQueries() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final Routine<?> routine() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final String sql() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void sql(String sql) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final String[] batchSQL() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void connectionProvider(ConnectionProvider connectionProvider) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final PreparedStatement statement() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void statement(PreparedStatement statement) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final int statementExecutionCount() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final ResultSet resultSet() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void resultSet(ResultSet resultSet) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final int recordLevel() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final Record record() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void record(Record record) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final int rows() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void rows(int rows) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final int[] batchRows() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final int resultLevel() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final Result<?> result() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void result(Result<?> result) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final RuntimeException exception() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void exception(RuntimeException e) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final SQLException sqlException() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void sqlException(SQLException e) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final SQLWarning sqlWarning() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void sqlWarning(SQLWarning e) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final String[] serverOutput() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public final void serverOutput(String[] output) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
