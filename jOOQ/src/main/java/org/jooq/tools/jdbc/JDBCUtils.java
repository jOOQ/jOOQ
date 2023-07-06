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
package org.jooq.tools.jdbc;

import static org.jooq.SQLDialect.*;

import java.io.Closeable;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLXML;
import java.sql.Statement;

// ...
import org.jooq.SQLDialect;
import org.jooq.tools.JooqLogger;

import org.jetbrains.annotations.NotNull;

import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import io.r2dbc.spi.ConnectionMetadata;

/**
 * JDBC-related utility methods.
 *
 * @author Lukas Eder
 */
public class JDBCUtils {

    private static final JooqLogger log = JooqLogger.getLogger(JDBCUtils.class);

    /**
     * "Guess" the {@link SQLDialect} from a {@link Connection} instance.
     * <p>
     * This method tries to guess the <code>SQLDialect</code> of a connection
     * from the its connection URL as obtained by
     * {@link DatabaseMetaData#getURL()}. If the dialect cannot be guessed from
     * the URL (e.g. when using an JDBC-ODBC bridge), further actions may be
     * implemented in the future.
     *
     * @return The appropriate {@link SQLDialect} or {@link SQLDialect#DEFAULT}
     *         if no dialect could be derived from the connection. Never
     *         <code>null</code>.
     * @see #dialect(String)
     */
    @NotNull
    public static final SQLDialect dialect(Connection connection) {
        SQLDialect result = SQLDialect.DEFAULT;

        if (connection != null) {
            try {
                DatabaseMetaData m = connection.getMetaData();







                String url = m.getURL();
                int majorVersion = 0;
                int minorVersion = 0;
                String productVersion = "";

                // [#6814] Better play safe with JDBC API
                try {
                    majorVersion = m.getDatabaseMajorVersion();
                }
                catch (SQLException ignore) {}

                try {
                    minorVersion = m.getDatabaseMinorVersion();
                }
                catch (SQLException ignore) {}

                try {
                    productVersion = m.getDatabaseProductVersion();
                }
                catch (SQLException ignore) {}

                result = dialect(url, majorVersion, minorVersion, productVersion);
            }
            catch (SQLException ignore) {}
        }

        if (result == SQLDialect.DEFAULT) {
            // If the dialect cannot be guessed from the URL, take some other
            // measures, e.g. by querying DatabaseMetaData.getDatabaseProductName()
        }

        return result;
    }

    /**
     * "Guess" the {@link SQLDialect} from a {@link ConnectionFactory} instance.
     * <p>
     * This method tries to guess the <code>SQLDialect</code> of a connection
     * from the its {@link ConnectionFactoryMetadata} as obtained by
     * {@link ConnectionFactory#getMetadata()}. If the dialect cannot be
     * guessed, further actions may be implemented in the future.
     *
     * @return The appropriate {@link SQLDialect} or {@link SQLDialect#DEFAULT}
     *         if no dialect could be derived from the connection. Never
     *         <code>null</code>.
     * @see #dialect(String)
     */
    @NotNull
    public static final SQLDialect dialect(ConnectionFactory connection) {
        SQLDialect result = SQLDialect.DEFAULT;

        if (connection != null)
            result = dialectFromProductName(connection.getMetadata().getName());

        if (result == SQLDialect.DEFAULT) {
            // If the dialect cannot be guessed from the URL, take some other
            // measures, e.g. by querying DatabaseMetaData.getDatabaseProductName()
        }

        return result;
    }

    /**
     * "Guess" the {@link SQLDialect} from an R2DBC
     * {@link io.r2dbc.spi.Connection} instance.
     * <p>
     * This method tries to guess the <code>SQLDialect</code> of a connection
     * from the its {@link ConnectionMetadata} as obtained by
     * {@link io.r2dbc.spi.Connection#getMetadata()}. If the dialect cannot be,
     * further actions may be implemented in the future.
     *
     * @return The appropriate {@link SQLDialect} or {@link SQLDialect#DEFAULT}
     *         if no dialect could be derived from the connection. Never
     *         <code>null</code>.
     * @see #dialect(String)
     */
    @NotNull
    public static final SQLDialect dialect(io.r2dbc.spi.Connection connection) {
        SQLDialect result = SQLDialect.DEFAULT;

        if (connection != null)
            result = dialectFromProductName(connection.getMetadata().getDatabaseProductName());

        if (result == SQLDialect.DEFAULT) {
            // If the dialect cannot be guessed from the URL, take some other
            // measures, e.g. by querying DatabaseMetaData.getDatabaseProductName()
        }

        return result;
    }

    private static SQLDialect dialectFromProductName(String product) {
        String p = product.toLowerCase().replace(" ", "");

        if (p.contains("h2"))
            return H2;
        else if (p.contains("mariadb"))
            return MARIADB;
        else if (p.contains("mysql"))
            return MYSQL;
        else if (p.contains("postgres"))
            return POSTGRES;






        else
            return DEFAULT;
    }

    @NotNull
    private static final SQLDialect dialect(String url, int majorVersion, int minorVersion, String productVersion) {
        SQLDialect family = dialect(url).family();

        // [#6814] If the driver can't report the version, fall back to the dialect family
        if (majorVersion == 0)
            return family;

        switch (family) {








            case FIREBIRD:
                return firebirdDialect(majorVersion);
            case H2:
                return h2Dialect(majorVersion, minorVersion, productVersion);
            case MARIADB:
                return mariadbDialect(majorVersion, minorVersion);
            case MYSQL:
                return mysqlDialect(majorVersion, minorVersion, productVersion);
            case POSTGRES:
                return postgresDialect(majorVersion, minorVersion);
        }

        return family;
    }























































    private static final SQLDialect postgresDialect(int majorVersion, int minorVersion) {


























        return POSTGRES;
    }

    private static final SQLDialect mariadbDialect(int majorVersion, int minorVersion) {




















        return MARIADB;
    }

    private static final SQLDialect mysqlDialect(int majorVersion, int minorVersion, String productVersion) {


















        return MYSQL;
    }

    private static final SQLDialect firebirdDialect(int majorVersion) {











        return FIREBIRD;
    }

    private static final SQLDialect h2Dialect(int majorVersion, int minorVersion, String productVersion) {





























        return H2;
    }

    /**
     * "Guess" the {@link SQLDialect} from a connection URL.
     *
     * @return The appropriate {@link SQLDialect} or {@link SQLDialect#DEFAULT}
     *         if no dialect could be derived from the connection. Never
     *         <code>null</code>.
     */
    @NotNull
    public static final SQLDialect dialect(String url) {
        if (url == null)
            return DEFAULT;

        // The below list might not be accurate or complete. Feel free to
        // contribute fixes related to new / different JDBC driver configurations

        // [#6035] Third-party JDBC proxies (e.g. www.testcontainers.org) often work
        //         by inserting their names into the JDBC URL, e.g. jdbc:tc:mysql://...
        //         This is why we no longer check for a URL to start with jdbc:mysql:
        //         but to simply contain :mysql:









        else if (url.contains(":cubrid:"))
            return CUBRID;
        else if (url.contains(":derby:"))
            return DERBY;
        else if (url.contains(":duckdb:"))
            return DUCKDB;
        else if (url.contains(":firebirdsql:"))
            return FIREBIRD;
        else if (url.contains(":h2:"))
            return H2;
        else if (url.contains(":hsqldb:"))
            return HSQLDB;
        else if (url.contains(":ignite:"))
            return IGNITE;
        else if (url.contains(":mariadb:"))
            return MARIADB;
        else if (url.contains(":mysql:")
              || url.contains(":google:"))
            return MYSQL;
        else if (url.contains(":postgresql:")
              || url.contains(":pgsql:"))
            return POSTGRES;
        else if (url.contains(":sqlite:")
              || url.contains(":sqldroid:"))
            return SQLITE;
        else if (url.contains(":trino:"))
            return TRINO;
        else if (url.contains(":yugabytedb:"))
            return YUGABYTEDB;

































        return DEFAULT;
    }

    /**
     * "Guess" the JDBC driver from a {@link SQLDialect}.
     *
     * @return The appropriate JDBC driver class or
     *         <code>"java.sql.Driver"</code> if no driver class could be
     *         derived from the URL. Never <code>null</code>.
     */
    @NotNull
    public static final String driver(SQLDialect dialect) {
        switch (dialect.family()) {
            case CUBRID:
                return "cubrid.jdbc.driver.CUBRIDDriver";
            case DERBY:
                return "org.apache.derby.jdbc.ClientDriver";
            case DUCKDB:
                return "org.duckdb.DuckDBDriver";
            case FIREBIRD:
                return "org.firebirdsql.jdbc.FBDriver";
            case H2:
                return "org.h2.Driver";
            case HSQLDB:
                return "org.hsqldb.jdbcDriver";
            case IGNITE:
                return "org.apache.ignite.IgniteJdbcThinDriver";
            case MARIADB:
                return "org.mariadb.jdbc.Driver";
            case MYSQL:
                return "com.mysql.cj.jdbc.Driver";
            case POSTGRES:
                return "org.postgresql.Driver";
            case SQLITE:
                return "org.sqlite.JDBC";
            case YUGABYTEDB:
                return "com.yugabyte.Driver";

































        }

        return "java.sql.Driver";
    }

    /**
     * "Guess" the JDBC driver from a connection URL.
     *
     * @return The appropriate JDBC driver class or
     *         <code>"java.sql.Driver"</code> if no driver class could be
     *         derived from the URL. Never <code>null</code>.
     */
    @NotNull
    public static final String driver(String url) {
        return driver(dialect(url).family());
    }

    /**
     * Safely close a connection.
     * <p>
     * This method will silently ignore if <code>connection</code> is
     * <code>null</code>, or if {@link Connection#close()} throws an exception.
     */
    public static final void safeClose(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (Exception ignore) {
                log.debug("Error when closing connection", ignore);
            }
        }
    }

    /**
     * Safely close a statement.
     * <p>
     * This method will silently ignore if <code>statement</code> is
     * <code>null</code>, or if {@link Statement#close()} throws an exception.
     */
    public static final void safeClose(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            }
            catch (Exception ignore) {
                log.debug("Error when closing statement", ignore);
            }
        }
    }

    /**
     * Safely close a result set.
     * <p>
     * This method will silently ignore if <code>resultSet</code> is
     * <code>null</code>, or if {@link ResultSet#close()} throws an exception.
     */
    public static final void safeClose(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            }
            catch (Exception ignore) {
                log.debug("Error when closing result set", ignore);
            }
        }
    }

    /**
     * Safely close a result set and / or a statement.
     * <p>
     * This method will silently ignore if <code>resultSet</code> or
     * <code>statement</code> is <code>null</code>, or if
     * {@link ResultSet#close()} or {@link Statement#close()} throws an
     * exception.
     */
    public static final void safeClose(ResultSet resultSet, PreparedStatement statement) {
        safeClose(resultSet);
        safeClose(statement);
    }

    /**
     * Safely close a closeable.
     * <p>
     * This method will silently ignore if <code>closeable</code> is
     * <code>null</code>, or if {@link Closeable#close()} throws an exception.
     */
    public static final void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (Exception ignore) {
                log.debug("Error when closing closeable", ignore);
            }
        }
    }

    /**
     * Safely close a closeable.
     * <p>
     * This method will silently ignore if <code>closeable</code> is
     * <code>null</code>, or if {@link AutoCloseable#close()} throws an exception.
     */
    public static final void safeClose(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (Exception ignore) {
                log.debug("Error when closing closeable", ignore);
            }
        }
    }

    /**
     * Safely free a blob.
     * <p>
     * This method will silently ignore if <code>blob</code> is
     * <code>null</code>, or if {@link Blob#free()} throws an exception.
     */
    public static final void safeFree(Blob blob) {
        if (blob != null) {
            try {
                blob.free();
            }
            catch (Exception e) {
                log.warn("Error while freeing resource", e);
            }

            // [#3069] The free() method was added only in JDBC 4.0 / Java 1.6
            catch (AbstractMethodError ignore) {}
        }
    }

    /**
     * Safely free a clob.
     * <p>
     * This method will silently ignore if <code>clob</code> is
     * <code>null</code>, or if {@link Clob#free()} throws an exception.
     */
    public static final void safeFree(Clob clob) {
        if (clob != null) {
            try {
                clob.free();
            }
            catch (Exception e) {
                log.warn("Error while freeing resource", e);
            }

            // [#3069] The free() method was added only in JDBC 4.0 / Java 1.6
            catch (AbstractMethodError ignore) {}
        }
    }

    /**
     * Safely free an XML object.
     * <p>
     * This method will silently ignore if <code>xml</code> is
     * <code>null</code>, or if {@link SQLXML#free()} throws an exception.
     */
    public static final void safeFree(SQLXML xml) {
        if (xml != null) {
            try {
                xml.free();
            }
            catch (Exception e) {
                log.warn("Error while freeing resource", e);
            }

            // [#3069] The free() method was added only in JDBC 4.0 / Java 1.6
            catch (AbstractMethodError ignore) {}
        }
    }

    /**
     * Safely free an Array object.
     * <p>
     * This method will silently ignore if <code>array</code> is
     * <code>null</code>, or if {@link Array#free()} throws an exception.
     */
    public static final void safeFree(Array array) {
        if (array != null) {
            try {
                array.free();
            }
            catch (Exception e) {
                log.warn("Error while freeing resource", e);
            }

            // [#3069] The free() method was added only in JDBC 4.0 / Java 1.6
            catch (AbstractMethodError ignore) {}
        }
    }

    /**
     * Convenient way to check if a JDBC-originated record was <code>null</code>.
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param stream The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link SQLInput#wasNull()} is <code>true</code>
     */
    public static final <T> T wasNull(SQLInput stream, T value) throws SQLException {
        return (value == null || stream.wasNull()) ? null : value;
    }

    /**
     * Convenient way to check if a JDBC-originated record was <code>null</code>.
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param stream The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link SQLInput#wasNull()} is <code>true</code>
     */
    public static final <T extends Number> T wasNull(SQLInput stream, T value) throws SQLException {
        return (value == null || (value.intValue() == 0 && stream.wasNull())) ? null : value;
    }

    /**
     * Convenient way to check if a JDBC-originated record was <code>null</code>.
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param stream The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link SQLInput#wasNull()} is <code>true</code>
     */
    public static final Boolean wasNull(SQLInput stream, Boolean value) throws SQLException {
        return (value == null || (!value && stream.wasNull())) ? null : value;
    }

    /**
     * Convenient way to check if a JDBC-originated record was <code>null</code>.
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param rs The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link ResultSet#wasNull()} is <code>true</code>
     */
    public static final <T> T wasNull(ResultSet rs, T value) throws SQLException {
        return (value == null || rs.wasNull()) ? null : value;
    }

    /**
     * Convenient way to check if a JDBC-originated record was <code>null</code>.
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param rs The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link ResultSet#wasNull()} is <code>true</code>
     */
    public static final <T extends Number> T wasNull(ResultSet rs, T value) throws SQLException {
        return (value == null || (value.intValue() == 0 && rs.wasNull())) ? null : value;
    }

    /**
     * Convenient way to check if a JDBC-originated record was <code>null</code>.
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param rs The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link ResultSet#wasNull()} is <code>true</code>
     */
    public static final Boolean wasNull(ResultSet rs, Boolean value) throws SQLException {
        return (value == null || (!value && rs.wasNull())) ? null : value;
    }

    /**
     * Convenient way to check if a JDBC-originated record was <code>null</code>.
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param statement The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link CallableStatement#wasNull()} is <code>true</code>
     */
    public static final <T> T wasNull(CallableStatement statement, T value) throws SQLException {
        return (value == null || statement.wasNull()) ? null : value;
    }

    /**
     * Convenient way to check if a JDBC-originated record was <code>null</code>.
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param statement The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link CallableStatement#wasNull()} is <code>true</code>
     */
    public static final <T extends Number> T wasNull(CallableStatement statement, T value) throws SQLException {
        return (value == null || (value.intValue() == 0 && statement.wasNull())) ? null : value;
    }

    /**
     * Convenient way to check if a JDBC-originated record was <code>null</code>.
     * <p>
     * This is useful to check if primitive types obtained from the JDBC API
     * were actually SQL NULL values.
     *
     * @param statement The data source from which a value was read
     * @param value The value that was read
     * @return The <code>value</code> or <code>null</code> if the
     *         {@link CallableStatement#wasNull()} is <code>true</code>
     */
    public static final Boolean wasNull(CallableStatement statement, Boolean value) throws SQLException {
        return (value == null || (!value && statement.wasNull())) ? null : value;
    }

    /**
     * No instances.
     */
    private JDBCUtils() {}
}
