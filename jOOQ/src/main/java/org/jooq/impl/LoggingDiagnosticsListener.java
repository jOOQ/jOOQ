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
package org.jooq.impl;

import static java.util.stream.Collectors.toList;

import java.util.stream.Collectors;

import org.jooq.DiagnosticsContext;
import org.jooq.DiagnosticsListener;
import org.jooq.tools.JooqLogger;

/**
 * A default implementation of a {@link DiagnosticsListener} that logs
 * diagnostics.
 *
 * @author Lukas Eder
 */
public class LoggingDiagnosticsListener implements DiagnosticsListener {

    private static final JooqLogger log = JooqLogger.getLogger(LoggingDiagnosticsListener.class);

    private void log(String text, DiagnosticsContext ctx) {
        log(text, ctx, null);
    }

    private void log(String text, DiagnosticsContext ctx, String additionalContext) {
        if (log.isInfoEnabled()) {
            if (additionalContext != null)
                text += "\n" + additionalContext;

            if (ctx.actualStatement().equals(ctx.normalisedStatement()))
                text += "\nStatement: " + ctx.actualStatement();
            else
                text += "\nActual statement    : " + ctx.actualStatement()
                      + "\nNormalised statement: " + ctx.normalisedStatement();

            log.info("Diagnostics", text);
        }
    }

    @Override
    public void duplicateStatements(DiagnosticsContext ctx) {
        log("""
            Duplicate statements were encountered. Why is it bad? See: https://www.jooq.org/doc/latest/manual/sql-execution/diagnostics/diagnostics-duplicate-statements/
            """,
            ctx,
            "Recent statements include: " + ctx.duplicateStatements().stream().limit(5).map(s -> "\n  " + s).collect(toList())
        );
    }

    @Override
    public void repeatedStatements(DiagnosticsContext ctx) {
        log("""
            Repeated statements were encountered. Why is it bad? See: https://www.jooq.org/doc/latest/manual/sql-execution/diagnostics/diagnostics-repeated-statements/
            """,
            ctx,
            "Recent statements include: " + ctx.repeatedStatements().stream().limit(5).map(s -> "\n  " + s).collect(toList())
        );
    }


    @Override
    public void tooManyColumnsFetched(DiagnosticsContext ctx) {
        log("""
            Too many columns were fetched and never read. Why is it bad? See: https://www.jooq.org/doc/latest/manual/sql-execution/diagnostics/diagnostics-too-many-columns/
            """,
            ctx,
            "Fetched columns : " + ctx.resultSetFetchedColumnNames()
        + "\nConsumed columns: " + ctx.resultSetConsumedColumnNames()
        );
    }

    @Override
    public void tooManyRowsFetched(DiagnosticsContext ctx) {
        log("""
            Too many rows were fetched and never read. Why is it bad? See: https://www.jooq.org/doc/latest/manual/sql-execution/diagnostics/diagnostics-too-many-rows/
            """,
            ctx,
            "Fetched rows : " + ctx.resultSetFetchedRows()
        + "\nConsumed rows: " + ctx.resultSetConsumedRows()
        );
    }









































}
