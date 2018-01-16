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
package org.jooq.util.ddl;

import static org.jooq.impl.DSL.name;
import static org.jooq.tools.StringUtils.isBlank;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.DSLContext;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.ParserException;
import org.jooq.tools.JooqLogger;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.h2.H2Database;

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

    @Override
    protected DSLContext create0() {
        if (connection == null) {
            String scripts = getProperties().getProperty("scripts");
            String encoding = getProperties().getProperty("encoding", "UTF-8");

            if (isBlank(scripts)) {
                scripts = "";
                log.warn("No scripts defined", "It is recommended that you provide an explicit script directory to scan");
            }

            try {
                Properties info = new Properties();
                info.put("user", "sa");
                info.put("password", "");
                connection = new org.h2.Driver().connect("jdbc:h2:mem:jooq-meta-extensions-" + UUID.randomUUID(), info);
                ctx = DSL.using(connection);

                InputStream in = null;
                try {
                    in = DDLDatabase.class.getResourceAsStream(scripts);
                    if (in != null) {
                        log.info("Reading from classpath: " + scripts);
                    }
                    else {
                        File file = new File(scripts);

                        if (file.exists()) {
                            if (file.isFile()) {
                                log.info("Reading from file: " + scripts);
                                in = new FileInputStream(file);
                            }
                            else if (file.isDirectory()) {
                                log.warn("Reading from directory not yet supported: " + scripts);
                            }
                        }
                    }

                    if (in != null) {
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
                    else {
                        log.error("Could not find script source : " + scripts);
                    }
                }
                finally {
                    if (in != null)
                        try {
                            in.close();
                        }
                        catch (Exception ignore) {}
                }
            }
            catch (ParserException e) {
                log.error("An exception occurred while parsing script source : " + scripts + ". Please report this error to https://github.com/jOOQ/jOOQ/issues/new", e);
                throw e;
            }
            catch (Exception e) {
                throw new DataAccessException("Error while exporting schema", e);
            }
        }

        return ctx;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>(super.getSchemata0());

        // [#5608] The H2-specific INFORMATION_SCHEMA is undesired in the DDLDatabase's output
        //         we're explicitly omitting it here for user convenience.
        Iterator<SchemaDefinition> it = result.iterator();
        while (it.hasNext())
            if ("INFORMATION_SCHEMA".equals(it.next().getName()))
                it.remove();

        return result;
    }
}
