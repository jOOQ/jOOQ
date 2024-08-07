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
package org.jooq.impl;

import static org.jooq.SQLDialect.DEFAULT;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.jdbc.JDBCUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class DefaultInterpreterConnectionProvider implements ConnectionProvider {

    private final Configuration configuration;

    DefaultInterpreterConnectionProvider(Configuration configuration) {
        this.configuration = configuration;
    }

    @NotNull
    @Override
    public Connection acquire() throws DataAccessException {
        SQLDialect family = defaultIfNull(configuration.settings().getInterpreterDialect(), DEFAULT).family();

        try {
            switch (family) {
                case DERBY:
                    return DriverManager.getConnection("jdbc:derby:memory:db;create=true");

                case H2:
                case DEFAULT:
                    return DriverManager.getConnection("jdbc:h2:mem:jooq-ddl-interpretation-" + UUID.randomUUID(), "sa", "");

                case HSQLDB:
                    // The newer form jdbc:hsqldb:mem:. is not necessarily supported by the driver version yet
                    return DriverManager.getConnection("jdbc:hsqldb:.");

                case SQLITE:
                    return DriverManager.getConnection("jdbc:sqlite::memory:");

                default:
                    throw new DataAccessException("Unsupported interpretation dialect family: " + family);
            }
        }
        catch (SQLException e) {
            if ("08001".equals(e.getSQLState()))
                throw new DataAccessException("The JDBC driver's JAR file was not found on the classpath, which is required for this feature", e);

            throw new DataAccessException("Error while exporting schema", e);
        }
    }

    @Override
    public void release(Connection connection) throws DataAccessException {
        JDBCUtils.safeClose(connection);
    }
}
