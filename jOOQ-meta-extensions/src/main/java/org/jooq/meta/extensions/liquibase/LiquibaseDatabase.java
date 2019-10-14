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
package org.jooq.meta.extensions.liquibase;

import static org.jooq.tools.StringUtils.isBlank;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.h2.H2Database;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;

/**
 * The Liquibase database.
 * <p>
 * This meta data source parses a set of XML files that follow the Liquibase
 * migration DSL, runs them on an in-memory H2 database before reverse
 * engineering the outcome.
 * <p>
 * The XML scripts are located in the <code>scripts</code> scripts property
 * available from {@link #getProperties()}.
 *
 * @author Lukas Eder
 */
public class LiquibaseDatabase extends H2Database {

    private static final JooqLogger log    = JooqLogger.getLogger(LiquibaseDatabase.class);

    private Connection              connection;
    private boolean                 publicIsDefault;
    private boolean                 includeLiquibaseTables;

    @Override
    protected DSLContext create0() {
        if (connection == null) {
            String scripts = getProperties().getProperty("scripts");
            String unqualifiedSchema = getProperties().getProperty("unqualifiedSchema", "none").toLowerCase();
            includeLiquibaseTables = Boolean.valueOf(getProperties().getProperty("includeLiquibaseTables", "false"));

            publicIsDefault = "none".equals(unqualifiedSchema);


            if (isBlank(scripts)) {
                scripts = "";
                log.warn("No scripts defined", "It is recommended that you provide an explicit script directory to scan");
            }

            try {
                Properties info = new Properties();
                info.put("user", "sa");
                info.put("password", "");
                connection = new org.h2.Driver().connect("jdbc:h2:mem:jooq-meta-extensions-" + UUID.randomUUID(), info);

                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                Liquibase liquibase = new Liquibase(scripts, new FileSystemResourceAccessor(), database);
                liquibase.update("");
            }
            catch (Exception e) {
                throw new DataAccessException("Error while exporting schema", e);
            }
        }

        return DSL.using(connection);
    }

    @Override
    public void close() {
        JDBCUtils.safeClose(connection);
        connection = null;
        super.close();
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<>(super.getTables0());

        if (!includeLiquibaseTables) {
            List<String> liquibaseTables = Arrays.asList("DATABASECHANGELOG", "DATABASECHANGELOGLOCK");

            Iterator<TableDefinition> it = result.iterator();
            while (it.hasNext())
                if (liquibaseTables.contains(it.next().getName()))
                    it.remove();
        }

        return result;
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
