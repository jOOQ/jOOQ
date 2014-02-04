/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.tools;

import static org.jooq.impl.DSL.val;
import static org.jooq.tools.StringUtils.abbreviate;

import java.util.Arrays;
import java.util.logging.Level;

import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteType;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.VisitContext;
import org.jooq.VisitListener;
import org.jooq.VisitListenerProvider;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultVisitListener;
import org.jooq.impl.DefaultVisitListenerProvider;

/**
 * A default {@link ExecuteListener} that just logs events to java.util.logging,
 * log4j, or slf4j using the {@link JooqLogger}
 *
 * @author Lukas Eder
 */
public class LoggerListener extends DefaultExecuteListener {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7399239846062763212L;

    private static final JooqLogger log   = JooqLogger.getLogger(LoggerListener.class);

    @Override
    public void renderEnd(ExecuteContext ctx) {
        if (log.isDebugEnabled()) {
            Configuration configuration = ctx.configuration();

            // [#2939] Prevent excessive logging of bind variables only in DEBUG mode, not in TRACE mode.
            if (!log.isTraceEnabled()) {
                configuration = abbreviateBindVariables(configuration);
            }

            String[] batchSQL = ctx.batchSQL();
            if (ctx.query() != null) {

                // Actual SQL passed to JDBC
                log.debug("Executing query", ctx.sql());

                // [#1278] DEBUG log also SQL with inlined bind values, if
                // that is not the same as the actual SQL passed to JDBC
                String inlined = DSL.using(configuration).renderInlined(ctx.query());
                if (!ctx.sql().equals(inlined)) {
                    log.debug("-> with bind values", inlined);
                }
            }

            // [#2987] Log routines
            else if (ctx.routine() != null) {
                log.debug("Calling routine", ctx.sql());

                String inlined = DSL.using(configuration)
                                    .renderInlined(ctx.routine());

                if (!ctx.sql().equals(inlined)) {
                    log.debug("-> with bind values", inlined);
                }
            }

            else if (!StringUtils.isBlank(ctx.sql())) {

                // [#1529] Batch queries should be logged specially
                if (ctx.type() == ExecuteType.BATCH) {
                    log.debug("Executing batch query", ctx.sql());
                }
                else {
                    log.debug("Executing query", ctx.sql());
                }
            }

            // [#2532] Log a complete BatchMultiple query
            else if (batchSQL.length > 0) {
                if (batchSQL[batchSQL.length - 1] != null) {
                    for (String sql : batchSQL) {
                        log.debug("Executing batch query", sql);
                    }
                }
            }
        }
    }

    @Override
    public void recordEnd(ExecuteContext ctx) {
        if (log.isTraceEnabled() && ctx.record() != null)
            logMultiline("Record fetched", ctx.record().toString(), Level.FINER);
    }

    @Override
    public void resultEnd(ExecuteContext ctx) {
        if (ctx.result() != null) {
            if (log.isTraceEnabled()) {
                logMultiline("Fetched result", ctx.result().format(500), Level.FINE);
            }
            else if (log.isDebugEnabled()) {
                logMultiline("Fetched result", ctx.result().format(5), Level.FINE);
            }
        }
    }

    @Override
    public void executeEnd(ExecuteContext ctx) {
        if (log.isDebugEnabled() && ctx.rows() >= 0) {
            log.debug("Affected row(s)", ctx.rows());
        }
    }

    private void logMultiline(String comment, String message, Level level) {
        for (String line : message.split("\n")) {
            if (level == Level.FINE) {
                log.debug(comment, line);
            }
            else {
                log.trace(comment, line);
            }

            comment = "";
        }
    }

    private static final int maxLength = 2000;

    /**
     * Add a {@link VisitListener} that transforms all bind variables by abbreviating them.
     */
    private final Configuration abbreviateBindVariables(Configuration configuration) {
        VisitListenerProvider[] oldProviders = configuration.visitListenerProviders();
        VisitListenerProvider[] newProviders = new VisitListenerProvider[oldProviders.length + 1];
        System.arraycopy(oldProviders, 0, newProviders, 0, oldProviders.length);
        newProviders[newProviders.length - 1] = new DefaultVisitListenerProvider(new BindValueAbbreviator());

        return configuration.derive(newProviders);
    }

    private static class BindValueAbbreviator extends DefaultVisitListener {

        private boolean anyAbbreviations = false;

        @Override
        public void visitStart(VisitContext context) {
            if (context.renderContext() != null) {
                QueryPart part = context.queryPart();

                if (part instanceof Param<?>) {
                    Param<?> param = (Param<?>) part;
                    Object value = param.getValue();

                    if (value instanceof String && ((String) value).length() > maxLength) {
                        anyAbbreviations = true;
                        context.queryPart(val(abbreviate((String) value, maxLength)));
                    }
                    else if (value instanceof byte[] && ((byte[]) value).length > maxLength) {
                        anyAbbreviations = true;
                        context.queryPart(val(Arrays.copyOf((byte[]) value, maxLength)));
                    }
                }
            }
        }

        @Override
        public void visitEnd(VisitContext context) {
            if (anyAbbreviations) {
                if (context.queryPartsLength() == 1) {
                    context.renderContext().sql(" -- Bind values may have been abbreviated for DEBUG logging. Use TRACE logging for very large bind variables.");
                }
            }
        }
    }
}
