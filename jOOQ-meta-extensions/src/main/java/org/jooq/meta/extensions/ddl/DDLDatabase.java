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
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.DSLContext;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.VisitContext;
import org.jooq.conf.ParseUnknownFunctions;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultVisitListener;
import org.jooq.impl.ParserException;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.h2.H2Database;
import org.jooq.meta.tools.FilePattern;
import org.jooq.meta.tools.FilePattern.Loader;
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
public class DDLDatabase extends H2Database {

    private static final JooqLogger log    = JooqLogger.getLogger(DDLDatabase.class);
    private static final Pattern    P_NAME = Pattern.compile("(?s:.*?\"([^\"]*)\".*)");

    private Connection              connection;
    private DSLContext              ctx;
    private boolean                 publicIsDefault;

    @Override
    protected DSLContext create0() {
        if (connection == null) {
            Settings defaultSettings = new Settings();

            String scripts = getProperties().getProperty("scripts");
            String encoding = getProperties().getProperty("encoding", "UTF-8");
            String sort = getProperties().getProperty("sort", "semantic").toLowerCase();
            String unqualifiedSchema = getProperties().getProperty("unqualifiedSchema", "none").toLowerCase();
            final String defaultNameCase = getProperties().getProperty("defaultNameCase", "as_is").toUpperCase();
            boolean parseIgnoreComments = !"false".equalsIgnoreCase(getProperties().getProperty("parseIgnoreComments"));
            String parseIgnoreCommentStart = getProperties().getProperty("parseIgnoreCommentStart", defaultSettings.getParseIgnoreCommentStart());
            String parseIgnoreCommentStop = getProperties().getProperty("parseIgnoreCommentStop", defaultSettings.getParseIgnoreCommentStop());

            publicIsDefault = "none".equals(unqualifiedSchema);
            Comparator<File> fileComparator = FilePattern.fileComparator(sort);

            if (isBlank(scripts)) {
                scripts = "";
                log.warn("No scripts defined", "It is recommended that you provide an explicit script directory to scan");
            }

            try {
                Properties info = new Properties();
                info.put("user", "sa");
                info.put("password", "");
                connection = new org.h2.Driver().connect("jdbc:h2:mem:jooq-meta-extensions-" + UUID.randomUUID(), info);
                ctx = DSL.using(connection, new Settings()
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
                        public void visitStart(VisitContext c) {
                            if (c.queryPart() instanceof Name) {
                                Name[] parts = ((Name) c.queryPart()).parts();
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
                                    c.queryPart(DSL.name(parts));
                            }
                        }
                    });
                }

                FilePattern.load(encoding, scripts, fileComparator, new Loader() {
                    @Override
                    public void load(String e, InputStream in) {
                        DDLDatabase.this.load(e, in);
                    }
                });
            }
            catch (ParserException e) {
                log.error("An exception occurred while parsing script source : " + scripts + ". Please report this error to https://github.com/jOOQ/jOOQ/issues/new", e);
                throw e;
            }
            catch (Exception e) {
                throw new DataAccessException("Error while exporting schema", e);
            }
        }

        return DSL.using(connection);
    }

    private void load(String encoding, InputStream in) {
        try {
            Scanner s = new Scanner(in, encoding).useDelimiter("\\A");
            Queries queries = ctx.parser().parse(s.hasNext() ? s.next() : "");

            for (Query query : queries) {

                repeat:
                for (;;) {
                    try {
                        query.execute();
                        log.info(query);
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
            log.error("DDLDatabase Error", "Your SQL string could not be parsed or simulated. This may have a variety of reasons, including:\n"
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
            if (in != null)
                try {
                    in.close();
                }
                catch (Exception ignore) {}
        }
    }

    @Override
    public void close() {
        JDBCUtils.safeClose(connection);
        connection = null;
        ctx = null;
        super.close();
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<>(super.getSchemata0());

        // [#5608] The H2-specific INFORMATION_SCHEMA is undesired in the DDLDatabase's output
        //         we're explicitly omitting it here for user convenience.
        Iterator<SchemaDefinition> it = result.iterator();
        while (it.hasNext())
            if ("INFORMATION_SCHEMA".equals(it.next().getName()))
                it.remove();

        return result;
    }

    @Override
    @Deprecated
    public String getOutputSchema(String inputSchema) {
        String outputSchema = super.getOutputSchema(inputSchema);

        if (publicIsDefault && "PUBLIC".equals(outputSchema))
            return "";

        return outputSchema;
    }

    @Override
    public String getOutputSchema(String inputCatalog, String inputSchema) {
        String outputSchema = super.getOutputSchema(inputCatalog, inputSchema);

        if (publicIsDefault && "PUBLIC".equals(outputSchema))
            return "";

        return outputSchema;
    }
}
