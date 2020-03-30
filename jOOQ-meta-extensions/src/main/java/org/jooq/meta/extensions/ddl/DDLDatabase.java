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
package org.jooq.meta.extensions.ddl;

import static org.jooq.conf.SettingsTools.renderLocale;
import static org.jooq.impl.DSL.name;
import static org.jooq.tools.StringUtils.isBlank;

import java.io.File;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.DSLContext;
import org.jooq.FilePattern;
import org.jooq.FilePattern.Loader;
import org.jooq.FilePattern.Sort;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.ResultQuery;
import org.jooq.Source;
import org.jooq.VisitContext;
import org.jooq.conf.ParseUnknownFunctions;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultVisitListener;
import org.jooq.impl.ParserException;
import org.jooq.meta.extensions.AbstractInterpretingDatabase;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;

import org.h2.api.ErrorCode;

/**
 * The DDL database.
 * <p>
 * This meta data source parses a set of SQL scripts, translates them to the H2
 * dialect and runs them on an in-memory H2 database before reverse engineering
 * the outcome.
 * <p>
 * The SQL scripts are located in the <code>scripts</code> scripts property
 * available from {@link #getProperties()}.
 *
 * @author Lukas Eder
 */
public class DDLDatabase extends AbstractInterpretingDatabase {

    private static final JooqLogger log                 = JooqLogger.getLogger(DDLDatabase.class);
    private static final Pattern    P_NAME              = Pattern.compile("(?s:.*?\"([^\"]*)\".*)");

    private boolean                 logExecutedQueries  = true;
    private boolean                 logExecutionResults = true;

    @Override
    protected void export() throws Exception {
        Settings defaultSettings = new Settings();
        String scripts = getProperties().getProperty("scripts");
        String encoding = getProperties().getProperty("encoding", "UTF-8");
        String sort = getProperties().getProperty("sort", "semantic").toLowerCase();
        final String defaultNameCase = getProperties().getProperty("defaultNameCase", "as_is").toUpperCase();
        boolean parseIgnoreComments = !"false".equalsIgnoreCase(getProperties().getProperty("parseIgnoreComments"));
        String parseIgnoreCommentStart = getProperties().getProperty("parseIgnoreCommentStart", defaultSettings.getParseIgnoreCommentStart());
        String parseIgnoreCommentStop = getProperties().getProperty("parseIgnoreCommentStop", defaultSettings.getParseIgnoreCommentStop());
        logExecutedQueries = !"false".equalsIgnoreCase(getProperties().getProperty("logExecutedQueries"));
        logExecutionResults = !"false".equalsIgnoreCase(getProperties().getProperty("logExecutionResults"));

        if (isBlank(scripts)) {
            scripts = "";
            log.warn("No scripts defined", "It is recommended that you provide an explicit script directory to scan");
        }

        try {
            final DSLContext ctx = DSL.using(connection(), new Settings()
                .withParseIgnoreComments(parseIgnoreComments)
                .withParseIgnoreCommentStart(parseIgnoreCommentStart)
                .withParseIgnoreCommentStop(parseIgnoreCommentStop)
                .withParseUnknownFunctions(ParseUnknownFunctions.IGNORE)
            );

            // [#7771] [#8011] Ignore all parsed storage clauses when executing the statements
            ctx.data("org.jooq.ddl.ignore-storage-clauses", true);

            // [#8910] Parse things a bit differently for use with the DDLDatabase
            ctx.data("org.jooq.ddl.parse-for-ddldatabase", true);

            if (!"AS_IS".equals(defaultNameCase)) {
                ctx.configuration().set(new DefaultVisitListener() {
                    @Override
                    public void visitStart(VisitContext vc) {
                        if (vc.queryPart() instanceof Name) {
                            Name[] parts = ((Name) vc.queryPart()).parts();
                            boolean changed = false;

                            for (int i = 0; i < parts.length; i++) {
                                if (parts[i].quoted() == Quoted.UNQUOTED) {
                                    parts[i] = DSL.quotedName(
                                        "UPPER".equals(defaultNameCase)
                                      ? parts[i].first().toUpperCase(renderLocale(ctx.settings()))
                                      : parts[i].first().toLowerCase(renderLocale(ctx.settings()))
                                    );
                                    changed = true;
                                }
                            }

                            if (changed)
                                vc.queryPart(DSL.name(parts));
                        }
                    }
                });
            }

            new FilePattern()
                    .encoding(encoding)
                    .basedir(new File(getBasedir()))
                    .pattern(scripts)
                    .sort(Sort.of(sort))
                    .load(new Loader() {
                @Override
                public void load(Source source) {
                    DDLDatabase.this.load(ctx, source);
                }
            });
        }
        catch (ParserException e) {
            log.error("An exception occurred while parsing script source : " + scripts + ". Please report this error to https://github.com/jOOQ/jOOQ/issues/new", e);
            throw e;
        }
    }

    private void load(DSLContext ctx, Source source) {
        Reader r = null;

        try {
            Scanner s = new Scanner(r = source.reader()).useDelimiter("\\A");
            Queries queries = ctx.parser().parse(s.hasNext() ? s.next() : "");

            for (Query query : queries) {

                repeat:
                for (;;) {
                    try {
                        if (logExecutedQueries)
                            log.info(query);

                        if (logExecutedQueries && logExecutionResults)
                            if (query instanceof ResultQuery)
                                log.info("\n" + ((ResultQuery<?>) query).fetch());
                            else
                                log.info("Update count: " + query.execute());

                        // [#10008] Execute all queries. Could have FOR UPDATE or other side effects
                        else
                            query.execute();

                        break repeat;
                    }
                    catch (DataAccessException e) {

                        // [#7039] Auto create missing schemas. We're using the
                        if (Integer.toString(ErrorCode.SCHEMA_NOT_FOUND_1).equals(e.sqlState())) {
                            SQLException cause = e.getCause(SQLException.class);

                            if (cause != null) {
                                Matcher m = P_NAME.matcher(cause.getMessage());

                                if (m.find()) {
                                    Query createSchema = ctx.createSchemaIfNotExists(name(m.group(1)));
                                    createSchema.execute();
                                    log.info(createSchema);
                                    continue repeat;
                                }
                            }
                        }

                        throw e;
                    }
                }
            }
        }
        catch (DataAccessException e) {

            // [#9138] Make users aware of the new parse ignore comment syntax
            log.error("DDLDatabase Error", "Your SQL string could not be parsed or interpreted. This may have a variety of reasons, including:\n"
                + "- The jOOQ parser doesn't understand your SQL\n"
                + "- The jOOQ DDL simulation logic (translating to H2) cannot simulate your SQL\n"
                + "\n"
                + "If you think this is a bug or a feature worth requesting, please report it here: https://github.com/jOOQ/jOOQ/issues/new/choose\n"
                + "\n"
                + "As a workaround, you can use the Settings.parseIgnoreComments syntax documented here:\n"
                + "https://www.jooq.org/doc/latest/manual/sql-building/dsl-context/custom-settings/settings-parser/");

            throw e;
        }
        finally {
            JDBCUtils.safeClose(r);
        }
    }
}
