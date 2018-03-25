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
package org.jooq.tools;

import static java.lang.Boolean.TRUE;
import static java.util.Objects.requireNonNull;
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
import org.jooq.Log;
import org.jooq.Param;
import org.jooq.Parameter;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Routine;
import org.jooq.TXTFormat;
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

    private final Log.Level executeLoggingBindVariables;

    private final boolean   executeLoggingAbbreviatedBindVariables;

    private final Log.Level executeLoggingSqlString;

    private final Log.Level executeLoggingResult;

    private final int       executeLoggingResultNumberOfRows;

    private final int       executeLoggingResultNumberOfColumns;

    private final Log.Level executeLoggingRoutine;

    private final Log.Level executeLoggingException;

    public LoggerListener(final Log.Level executeLoggingBindVariables,
                          final Boolean executeLoggingAbbreviatedBindVariables,
                          final Log.Level executeLoggingSqlString,
                          final Log.Level executeLoggingResult,
                          final Integer executeLoggingResultNumberOfRows,
                          final Integer executeLoggingResultNumberOfColumns,
                          final Log.Level executeLoggingRoutine,
                          final Log.Level executeLoggingException) {
        this.executeLoggingBindVariables = requireNonNull(executeLoggingBindVariables);
        this.executeLoggingAbbreviatedBindVariables = requireNonNull(executeLoggingAbbreviatedBindVariables);
        this.executeLoggingSqlString = requireNonNull(executeLoggingSqlString);
        this.executeLoggingResult = requireNonNull(executeLoggingResult);
        this.executeLoggingResultNumberOfRows = requireNonNull(executeLoggingResultNumberOfRows);
        this.executeLoggingResultNumberOfColumns = requireNonNull(executeLoggingResultNumberOfColumns);
        this.executeLoggingRoutine = requireNonNull(executeLoggingRoutine);
        this.executeLoggingException = requireNonNull(executeLoggingException);
    }

    @Override
    public void renderEnd(ExecuteContext ctx) {
        Configuration configuration = ctx.configuration();
        String newline = TRUE.equals(configuration.settings().isRenderFormatted()) ? "\n" : "";

        // [#2939] Prevent excessive logging of bind variables only in DEBUG mode, not in TRACE mode.
        if (executeLoggingAbbreviatedBindVariables)
            configuration = abbreviateBindVariables(configuration);

        String[] batchSQL = ctx.batchSQL();
        if (log.isEnabled(executeLoggingSqlString) && ctx.query() != null) {

            // Actual SQL passed to JDBC
            log.log("Executing query", newline + ctx.sql(), executeLoggingSqlString);

            // [#1278] DEBUG log also SQL with inlined bind values, if
            // that is not the same as the actual SQL passed to JDBC
            if (log.isEnabled(executeLoggingBindVariables)) {
                String inlined = DSL.using(configuration).renderInlined(ctx.query());
                if (!ctx.sql().equals(inlined))
                    log.log("-> with bind values", newline + inlined, executeLoggingBindVariables);
            }
        }

        // [#2987] Log routines
        else if (log.isEnabled(executeLoggingRoutine) && ctx.routine() != null) {

            log.log("Calling routine", newline + ctx.sql(), executeLoggingRoutine);

            if (log.isEnabled(executeLoggingBindVariables)) {
                String inlined = DSL.using(configuration)
                        .renderInlined(ctx.routine());

                if (!ctx.sql().equals(inlined))
                    log.log("-> with bind values", newline + inlined, executeLoggingBindVariables);
            }
        } else if (log.isEnabled(executeLoggingSqlString) && !StringUtils.isBlank(ctx.sql())) {

            // [#1529] Batch queries should be logged specially
            if (ctx.type() == ExecuteType.BATCH)
                log.log("Executing batch query", newline + ctx.sql(), executeLoggingSqlString);
            else
                log.log("Executing query", newline + ctx.sql(), executeLoggingSqlString);
        }

        // [#2532] Log a complete BatchMultiple query
        else if (log.isEnabled(executeLoggingSqlString) && batchSQL.length > 0) {
            if (batchSQL[batchSQL.length - 1] != null)
                for (String sql : batchSQL)
                    log.log("Executing batch query", newline + sql, executeLoggingSqlString);
        }
    }

    @Override
    public void recordEnd(ExecuteContext ctx) {
        if (log.isEnabled(executeLoggingResult) && ctx.record() != null)
            logMultiline("Record fetched", ctx.record().toString(), executeLoggingResult);
    }

    @Override
    public void resultEnd(ExecuteContext ctx) {
        if (log.isEnabled(executeLoggingResult) && ctx.result() != null)
            logMultiline("Fetched result", ctx.result().format(TXTFormat.DEFAULT.maxRows(executeLoggingResultNumberOfRows).maxColWidth(executeLoggingResultNumberOfColumns)), executeLoggingResult);
    }

    @Override
    public void executeEnd(ExecuteContext ctx) {
        if (log.isEnabled(executeLoggingResult) && ctx.rows() >= 0)
            log.log("Affected row(s)", ctx.rows(), executeLoggingResult);
    }

    @Override
    public void outEnd(ExecuteContext ctx) {
        if (log.isEnabled(executeLoggingRoutine) && ctx.routine() != null)
            logMultiline("Fetched OUT parameters", "" + StringUtils.defaultIfNull(record(ctx.configuration(), ctx.routine()), "N/A"), executeLoggingRoutine);
    }

    @Override
    public void exception(ExecuteContext ctx) {
        if (log.isEnabled(executeLoggingException))
            log.log("Exception", ctx.exception(), executeLoggingException);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Record record(Configuration configuration, Routine<?> routine) {
        Record result = null;

        List<Field<?>> fields = new ArrayList<Field<?>>();
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

            result.changed(false);
        }

        return result;
    }

    private void logMultiline(String comment, String message, Log.Level level) {
        for (String line : message.split("\n")) {
            log.log(comment, line, level);

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
