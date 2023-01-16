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

import org.jooq.tools.JooqLogger;

import org.reactivestreams.Publisher;

import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;

/**
 * An R2DBC {@link Statement} proxy that logs all statements that are prepared
 * or executed using it.
 *
 * @author Lukas Eder
 */
public class LoggingStatement extends DefaultStatement {

    private static final JooqLogger log = JooqLogger.getLogger(LoggingStatement.class);

    public LoggingStatement(Statement delegate) {
        super(delegate);
    }

    @Override
    public Statement add() {
        if (log.isDebugEnabled())
            log.debug("Statement::add");

        getDelegate().add();
        return this;
    }

    @Override
    public Statement bind(int index, Object value) {
        if (log.isTraceEnabled())
            log.trace("Statement::bind", "index = " + index + ", value = " + value);

        getDelegate().bind(index, value);
        return this;
    }

    @Override
    public Statement bind(String name, Object value) {
        if (log.isTraceEnabled())
            log.trace("Statement::bind", "name = " + name + ", value = " + value);

        getDelegate().bind(name, value);
        return this;
    }

    @Override
    public Statement bindNull(int index, Class<?> type) {
        if (log.isTraceEnabled())
            log.trace("Statement::bindNull", "index = " + index + ", type = " + type);

        getDelegate().bindNull(index, type);
        return this;
    }

    @Override
    public Statement bindNull(String name, Class<?> type) {
        if (log.isTraceEnabled())
            log.trace("Statement::bindNull", "name = " + name + ", type = " + type);

        getDelegate().bindNull(name, type);
        return this;
    }

    @Override
    public Publisher<? extends Result> execute() {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Statement::execute");

            getDelegate().execute().subscribe(s);
        };
    }
}
