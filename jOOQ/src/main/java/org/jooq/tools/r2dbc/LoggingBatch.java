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
package org.jooq.tools.r2dbc;

import org.jooq.tools.JooqLogger;

import org.reactivestreams.Publisher;

import io.r2dbc.spi.Batch;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Result;

/**
 * An R2DBC {@link Batch} proxy that logs all statements that are prepared
 * or executed using it.
 *
 * @author Lukas Eder
 */
public class LoggingBatch implements Batch {

    private static final JooqLogger log = JooqLogger.getLogger(LoggingBatch.class);

    private final Batch delegate;

    public LoggingBatch(Batch delegate) {
        this.delegate = delegate;
    }

    public Batch getDelegate() {
        return delegate;
    }

    @Override
    public Batch add(String sql) {
        if (log.isDebugEnabled())
            log.debug("Batch::add", sql);

        getDelegate().add(sql);
        return this;
    }

    @Override
    public Publisher<? extends Result> execute() {
        return s -> {
            if (log.isDebugEnabled())
                log.debug("Batch::execute");

            getDelegate().execute().subscribe(s);
        };
    }
}
