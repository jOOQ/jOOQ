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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.tools.r2dbc;

import org.reactivestreams.Publisher;

import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;

/**
 * A default R2DBC {@link Statement} implementation delegating all R2DBC calls
 * to an internal getDelegate().
 *
 * @author Lukas Eder
 */
public class DefaultStatement implements Statement {

    private final Statement delegate;

    public DefaultStatement(Statement delegate) {
        this.delegate = delegate;
    }

    public Statement getDelegate() {
        return delegate;
    }

    @Override
    public Statement add() {
        return getDelegate().add();
    }

    @Override
    public Statement bind(int index, Object value) {
        return getDelegate().bind(index, value);
    }

    @Override
    public Statement bind(String name, Object value) {
        return getDelegate().bind(name, value);
    }

    @Override
    public Statement bindNull(int index, Class<?> type) {
        return getDelegate().bindNull(index, type);
    }

    @Override
    public Statement bindNull(String name, Class<?> type) {
        return getDelegate().bindNull(name, type);
    }

    @Override
    public Publisher<? extends Result> execute() {
        return getDelegate().execute();
    }

    @Override
    public Statement returnGeneratedValues(String... columns) {
        return getDelegate().returnGeneratedValues(columns);
    }

    @Override
    public Statement fetchSize(int rows) {
        return getDelegate().fetchSize(rows);
    }
}
