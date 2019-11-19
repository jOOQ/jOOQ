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

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.meta.TableDefinition;
import org.jooq.meta.extensions.AbstractInterpretingDatabase;
import org.jooq.tools.Convert;
import org.jooq.tools.JooqLogger;

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
public class LiquibaseDatabase extends AbstractInterpretingDatabase {

    private static final JooqLogger          log = JooqLogger.getLogger(LiquibaseDatabase.class);
    private static final Map<String, Method> SETTERS;
    private boolean                          includeLiquibaseTables;

    static {
        SETTERS = new HashMap<>();

        try {
            for (Method method : Database.class.getMethods()) {
                String name = method.getName();

                if (name.startsWith("set") && method.getParameterCount() == 1)
                    SETTERS.put(name, method);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void export() throws Exception {
        String scripts = getProperties().getProperty("scripts");
        includeLiquibaseTables = Boolean.valueOf(getProperties().getProperty("includeLiquibaseTables", "false"));

        if (isBlank(scripts)) {
            scripts = "";
            log.warn("No scripts defined", "It is recommended that you provide an explicit script directory to scan");
        }

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection()));

        // [#9514] Forward all database.xyz properties to matching Liquibase
        //         Database.setXyz() configuration setter calls
        for (Entry<Object, Object> entry : getProperties().entrySet()) {
            String key = "" + entry.getKey();

            if (key.startsWith("database.")) {
                String property = key.substring("database.".length());
                Method setter = SETTERS.get("set" + Character.toUpperCase(property.charAt(0)) + property.substring(1));

                try {
                    if (setter != null)
                        setter.invoke(database, Convert.convert(entry.getValue(), setter.getParameterTypes()[0]));
                }
                catch (Exception e) {
                    log.warn("Configuration error", e.getMessage(), e);
                }
            }
        }

        Liquibase liquibase = new Liquibase(scripts, new FileSystemResourceAccessor(), database);
        liquibase.update("");
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
}
