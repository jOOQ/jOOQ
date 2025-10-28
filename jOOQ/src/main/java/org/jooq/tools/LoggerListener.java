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
package org.jooq.tools;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.val;
import static org.jooq.tools.StringUtils.abbreviate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.ExecuteType;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Parameter;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Routine;
import org.jooq.VisitContext;
import org.jooq.VisitListener;
import org.jooq.impl.DSL;

/**
 * A default {@link ExecuteListener} that just logs events to java.util.logging,
 * log4j, or slf4j using the {@link JooqLogger}
 *
 * @author Lukas Eder
 */
public class LoggerListener implements ExecuteListener {

    private static final JooqLogger log        = JooqLogger.getLogger(LoggerListener.class);
    private static final String     BUFFER     = "org.jooq.tools.LoggerListener.BUFFER";
    private static final String     DO_BUFFER  = "org.jooq.tools.LoggerListener.DO_BUFFER";
    private static final String     BATCH_SIZE = "org.jooq.tools.LoggerListener.BATCH_SIZE";

    @Override
    public void renderEnd(ExecuteContext ctx) {
        if (log.isDebugEnabled()) {
            Configuration configuration = ctx.configuration();
            String newline = TRUE.equals(configuration.settings().isRenderFormatted()) ? "\n" : "";

            // [#2939] Prevent excessive logging of bind variables only in DEBUG mode, not in TRACE mode.
            if (!log.isTraceEnabled())
                configuration = configuration.deriveAppending(new BindValueAbbreviator());

            String[] batchSQL = ctx.batchSQL();
            if (ctx.query() != null) {

                // Actual SQL passed to JDBC
                log.debug("Executing query", newline + ctx.sql());

                // [#1278] DEBUG log also SQL with inlined bind values, if
                // that is not the same as the actual SQL passed to JDBC
                String inlined = DSL.using(configuration).renderInlined(ctx.query());
                if (!ctx.sql().equals(inlined))
                    log.debug("-> with bind values", newline + inlined);
            }

            // [#2987] Log routines
            else if (ctx.routine() != null) {
                log.debug("Calling routine", newline + ctx.sql());

                String inlined = DSL.using(configuration)
                                    .renderInlined(ctx.routine());

                if (!ctx.sql().equals(inlined))
                    log.debug("-> with bind values", newline + inlined);
            }

            else if (!StringUtils.isBlank(ctx.sql())) {

                // [#1529] Batch queries should be logged specially
                if (ctx.type() == ExecuteType.BATCH)
                    log.debug("Executing batch query", newline + ctx.sql());
                else
                    log.debug("Executing query", newline + ctx.sql());
            }
        }
    }

    @Override
    public void bindEnd(ExecuteContext ctx) {
        if (ctx.type() == ExecuteType.BATCH)
            if (log.isDebugEnabled())
                ctx.data().compute(BATCH_SIZE, (k, v) -> v == null ? 1 : ((int) v) + 1);
    }

    @Override
    public void executeStart(ExecuteContext ctx) {
        if (ctx.type() == ExecuteType.BATCH)
            if (log.isDebugEnabled())
                log.debug("Batch size", ctx.data().getOrDefault(BATCH_SIZE, ctx.batchSQL().length));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void recordEnd(ExecuteContext ctx) {

        // [#12564] Make sure we don't log any nested-level results or records
        if (ctx.recordLevel() > 0)
            return;

        if (log.isTraceEnabled() && ctx.record() != null)
            logMultiline("Record fetched", ctx.record().toString(), Level.FINER);

        // [#10019] Buffer the first few records
        if (log.isDebugEnabled() && ctx.record() != null && !FALSE.equals(ctx.data(DO_BUFFER))) {
            Result<Record> buffer = (Result<Record>) ctx.data(BUFFER);

            if (buffer == null)
                ctx.data(BUFFER, buffer = ctx.dsl().newResult(ctx.record().fields()));

            if (buffer.size() < maxRows())
                buffer.add(ctx.record());
        }
    }

    @Override
    public void resultStart(ExecuteContext ctx) {

        // [#10019] Don't buffer any records if it isn't needed
        ctx.data(DO_BUFFER, false);
    }

    @Override
    public void resultEnd(ExecuteContext ctx) {

        // [#12564] Make sure we don't log any nested-level results or records
        if (ctx.resultLevel() > 0)
            return;

        if (ctx.result() != null && log.isDebugEnabled()) {
            log(ctx.configuration(), ctx.result());
            log.debug("Fetched row(s)", ctx.result().size());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fetchEnd(ExecuteContext ctx) {
        Result<Record> buffer = (Result<Record>) ctx.data(BUFFER);

        if (buffer != null && !buffer.isEmpty() && log.isDebugEnabled()) {
            log(ctx.configuration(), buffer);
            log.debug("Fetched row(s)", buffer.size() + (buffer.size() < maxRows() ? "" : " (or more)"));
        }
    }

    private void log(Configuration configuration, Result<?> result) {
        logMultiline("Fetched result", result.format(configuration.formattingProvider().txtFormat().maxRows(maxRows()).maxColWidth(maxColWidth())), Level.FINE);
    }

    private int maxRows() {
        if (log.isTraceEnabled())
            return 500;
        else if (log.isDebugEnabled())
            return 5;
        else
            return 0;
    }

    private int maxColWidth() {
        if (log.isTraceEnabled())
            return 500;
        else if (log.isDebugEnabled())
            return 50;
        else
            return 0;
    }

    @Override
    public void executeEnd(ExecuteContext ctx) {
        if (ctx.rowsLarge() >= 0L)
            if (log.isDebugEnabled())
                log.debug("Affected row(s)", ctx.rowsLarge());
    }

    @Override
    public void outEnd(ExecuteContext ctx) {
        if (ctx.routine() != null)
            if (log.isDebugEnabled())
                logMultiline("Fetched OUT parameters", "" + StringUtils.defaultIfNull(record(ctx.configuration(), ctx.routine()), "N/A"), Level.FINE);
    }

    @Override
    public void exception(ExecuteContext ctx) {

        // [#9506] An internal, undocumented flag that allows for muting exception logging of
        //         "expected" exceptions.
        if (log.isDebugEnabled() && ctx.configuration().data("org.jooq.tools.LoggerListener.exception.mute") == null)
            log.debug("Exception", ctx.exception());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Record record(Configuration configuration, Routine<?> routine) {
        Record result = null;

        List<Field<?>> fields = new ArrayList<>(1 + routine.getOutParameters().size());
        Parameter<?> returnParam = routine.getReturnParameter();
        if (returnParam != null)
            fields.add(field(name(returnParam.getName()), returnParam.getDataType()));

        for (Parameter<?> param : routine.getOutParameters())
            fields.add(field(name(param.getName()), param.getDataType()));

        if (fields.size() > 0) {
            result = DSL.using(configuration).newRecord(fields.toArray(new Field[0]));

            int i = 0;
            if (returnParam != null)
                result.setValue((Field) fields.get(i++), routine.getValue(returnParam));

            for (Parameter<?> param : routine.getOutParameters())
                result.setValue((Field) fields.get(i++), routine.getValue(param));

            result.touched(false);
        }

        return result;
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

    private static class BindValueAbbreviator implements VisitListener {

        private boolean anyAbbreviations = false;

        @Override
        public void visitStart(VisitContext context) {
            if (context.renderContext() != null) {
                QueryPart part = context.queryPart();

                if (part instanceof Param<?> param) {
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
            if (anyAbbreviations && context.queryPartsLength() == 1)
                context.renderContext().sql(" -- Bind values may have been abbreviated for DEBUG logging. Use TRACE logging for very large bind variables.");
        }
    }
}
