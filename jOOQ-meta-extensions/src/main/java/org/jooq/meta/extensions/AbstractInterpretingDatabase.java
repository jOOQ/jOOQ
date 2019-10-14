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
package org.jooq.meta.extensions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.Internal;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.h2.H2Database;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * A common base class for "interpreting" databases, which interpret a third
 * party meta format, applying that to an in-memory H2 database, and reverse
 * engineering that.
 * <p>
 * Future versions of this implementation might switch the H2 database
 * dependency for jOOQ's native DDL interpreting "database", which allows for
 * supporting more native SQL than what H2 supports currently.
 * <p>
 * This class is INTERNAL and should not be implemented directly by users.
 *
 * @author Lukas Eder
 */
@Internal
public abstract class AbstractInterpretingDatabase extends H2Database {

    private Connection connection;
    private boolean    publicIsDefault;

    @Override
    protected DSLContext create0() {
        return DSL.using(connection());
    }

    /**
     * Subclasses should override this to initialise the in-memory H2
     * connection.
     */
    protected abstract void export() throws Exception;

    /**
     * Accessor to the connection that has been initialised by this database.
     */
    protected Connection connection() {
        if (connection == null) {
            try {
                String unqualifiedSchema = getProperties().getProperty("unqualifiedSchema", "none").toLowerCase();
                publicIsDefault = "none".equals(unqualifiedSchema);

                Properties info = new Properties();
                info.put("user", "sa");
                info.put("password", "");
                connection = new org.h2.Driver().connect("jdbc:h2:mem:jooq-meta-extensions-" + UUID.randomUUID(), info);

                export();
            }
            catch (Exception e) {
                throw new DataAccessException("Error while exporting schema", e);
            }
        }

        return connection;
    }

    @Override
    public void close() {
        JDBCUtils.safeClose(connection);
        connection = null;
        super.close();
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<>(super.getSchemata0());

        // [#5608] The H2-specific INFORMATION_SCHEMA is undesired in the output
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
