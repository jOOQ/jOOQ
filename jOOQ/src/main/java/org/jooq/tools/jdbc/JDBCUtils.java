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
 */
package org.jooq.tools.jdbc;

// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DEFAULT;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...

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

import org.jooq.SQLDialect;
import org.jooq.tools.JooqLogger;

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
     * @see #dialect(String)
     */
    public static final SQLDialect dialect(Connection connection) {
        SQLDialect result = SQLDialect.DEFAULT;

        if (connection != null) {
            try {
                DatabaseMetaData m = connection.getMetaData();








                String url = m.getURL();
                result = dialect(url);
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
     * "Guess" the {@link SQLDialect} from a connection URL.
     */
    public static final SQLDialect dialect(String url) {
        if (url == null) {
            return DEFAULT;
        }

        // The below list might not be accurate or complete. Feel free to
        // contribute fixes related to new / different JDBC driver configurations










        else if (url.startsWith("jdbc:cubrid:")) {
            return CUBRID;
        }
        else if (url.startsWith("jdbc:derby:")) {
            return DERBY;
        }
        else if (url.startsWith("jdbc:firebirdsql:")) {
            return FIREBIRD;
        }
        else if (url.startsWith("jdbc:h2:")) {
            return H2;
        }
        else if (url.startsWith("jdbc:hsqldb:")) {
            return HSQLDB;
        }
        else if (url.startsWith("jdbc:mariadb:")) {
            return MARIADB;
        }
        else if (url.startsWith("jdbc:mysql:")
              || url.startsWith("jdbc:google:")) {
            return MYSQL;
        }
        else if (url.startsWith("jdbc:postgresql:")
              || url.startsWith("jdbc:pgsql:")) {
            return POSTGRES;
        }
        else if (url.startsWith("jdbc:sqlite:")
              || url.startsWith("jdbc:sqldroid:")) {
            return SQLITE;
        }




































        return DEFAULT;
    }

    /**
     * "Guess" the JDBC driver from a connection URL.
     */
    public static final String driver(String url) {
        switch (dialect(url).family()) {
            case CUBRID:
                return "cubrid.jdbc.driver.CUBRIDDriver";
            case DERBY:
                return "org.apache.derby.jdbc.ClientDriver";
            case FIREBIRD:
                return "org.firebirdsql.jdbc.FBDriver";
            case H2:
                return "org.h2.Driver";
            case HSQLDB:
                return "org.hsqldb.jdbcDriver";
            case MARIADB:
                return "org.mariadb.jdbc.Driver";
            case MYSQL:
                return "com.mysql.jdbc.Driver";
            case POSTGRES:
                return "org.postgresql.Driver";
            case SQLITE:
                return "org.sqlite.JDBC";

























        }

        return "java.sql.Driver";
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
            catch (Exception ignore) {}
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
            catch (Exception ignore) {}
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
            catch (Exception ignore) {}
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
            catch (Exception ignore) {
                log.warn("Error while freeing resource", ignore);
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
            catch (Exception ignore) {
                log.warn("Error while freeing resource", ignore);
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
            catch (Exception ignore) {
                log.warn("Error while freeing resource", ignore);
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
            catch (Exception ignore) {
                log.warn("Error while freeing resource", ignore);
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
        return (value == null || (value.booleanValue() == false && stream.wasNull())) ? null : value;
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
        return (value == null || (value.booleanValue() == false && rs.wasNull())) ? null : value;
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
        return (value == null || (value.booleanValue() == false && statement.wasNull())) ? null : value;
    }

    /**
     * No instances.
     */
    private JDBCUtils() {}
}
