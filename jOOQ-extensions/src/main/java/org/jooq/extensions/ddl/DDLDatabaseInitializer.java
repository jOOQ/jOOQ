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
package org.jooq.extensions.ddl;

import static org.jooq.conf.SettingsTools.renderLocale;
import static org.jooq.impl.DSL.name;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.DSLContext;
import org.jooq.Internal;
import org.jooq.Name;
import org.jooq.Name.Quoted;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.VisitContext;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultVisitListener;
import org.jooq.impl.ParserException;
import org.jooq.tools.JooqLogger;

import org.h2.api.ErrorCode;

/**
 * Utility to create an in-memory H2 database, which can then be
 * {@link DDLDatabaseInitializer#loadScript(String, InputStream) loaded} with
 * DDL scripts.
 * <p>
 * Instead of directly executing the DDL scripts against the H2 database, they
 * are first parsed and translated to the H2 dialect, and only then executed
 * against the H2 database.
 * <p>
 * This is INTERNAL API. Please do not use directly as API may change
 * incompatibly.
 *
 * @author Knut Wannheden
 */
@Internal
public final class DDLDatabaseInitializer {

    private static final JooqLogger log    = JooqLogger.getLogger(DDLDatabaseInitializer.class);
    private static final Pattern    P_NAME = Pattern.compile("(?s:.*?\"([^\"]*)\".*)");

    private Connection connection;
    private DSLContext ctx;

    private DDLDatabaseInitializer(final Settings settings) {
        try {
            Properties info = new Properties();
            info.put("user", "sa");
            info.put("password", "");
            connection = new org.h2.Driver().connect("jdbc:h2:mem:jooq-extensions-" + UUID.randomUUID(), info);
            ctx = DSL.using(connection, settings);

            // [#7771] [#8011] Ignore all parsed storage clauses when executing the statements
            ctx.data("org.jooq.extensions.ddl.ignore-storage-clauses", true);

            // [#8910] Parse things a bit differently for use with the DDLDatabase
            ctx.data("org.jooq.extensions.ddl.parse-for-ddldatabase", true);

            final RenderNameCase nameCase = settings.getRenderNameCase();
            final Locale locale = renderLocale(ctx.settings());
            if (nameCase != null && nameCase != RenderNameCase.AS_IS) {
                ctx.configuration().set(new DefaultVisitListener() {
                    @Override
                    public void visitStart(VisitContext c) {
                        if (c.queryPart() instanceof Name) {
                            Name[] parts = ((Name) c.queryPart()).parts();
                            boolean changed = false;

                            for (int i = 0; i < parts.length; i++) {
                                switch (nameCase) {
                                    case LOWER_IF_UNQUOTED:
                                        if (parts[i].quoted() == Quoted.QUOTED) break;
                                    case LOWER:
                                        parts[i] = DSL.quotedName(parts[i].first().toLowerCase(locale));
                                        changed = true;
                                        break;

                                    case UPPER_IF_UNQUOTED:
                                        if (parts[i].quoted() == Quoted.QUOTED) break;
                                    case UPPER:
                                        parts[i] = DSL.quotedName(parts[i].first().toUpperCase(locale));
                                        changed = true;
                                        break;

                                    default:
                                        break;
                                }
                            }

                            if (changed)
                                c.queryPart(DSL.name(parts));
                        }
                    }
                });
            }
        }
        catch (Exception e) {
            throw new DataAccessException("Error while exporting schema", e);
        }
    }

    public static DDLDatabaseInitializer using(final Settings settings) {
        return new DDLDatabaseInitializer(settings);
    }

    /**
     * Parses and executes the script represented by {@code in} against the H2
     * database. If the script references a schema which doesn't exist, it will
     * be automatically created first.
     * <p>
     * Any parser errors will be thrown. It is however possible to delimit
     * sections which cannot be parsed using special comments.
     *
     * @see Settings#getParseIgnoreCommentStart()
     * @see Settings#getParseIgnoreCommentStop()
     */
    public final void loadScript(String encoding, InputStream in) {
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
        catch (ParserException e) {
            log.error("An exception occurred while parsing a DDL script: " + e.getMessage()
                + ". Please report this error to https://github.com/jOOQ/jOOQ/issues/new", e);
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

    public final Connection connection() {
        return connection;
    }

}
