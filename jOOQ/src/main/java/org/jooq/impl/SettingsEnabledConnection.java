/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static org.jooq.conf.SettingsTools.executePreparedStatements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.WeakHashMap;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.jdbc.DefaultConnection;

/**
 * A proxy for a JDBC {@link Connection} that handles creation of prepared
 * statements according to the settings' {@link StatementType}
 *
 * @author Lukas Eder
 */
class SettingsEnabledConnection extends DefaultConnection {

    private final Settings settings;
    private static final WeakHashMap<Connection, Boolean> connections = new WeakHashMap<Connection, Boolean>();

    SettingsEnabledConnection(Connection delegate, Configuration configuration) {
        super(delegate);

        this.settings = configuration.settings();

        this.configureBackslashEscapes(configuration);
    }

    /**
     * Ensures that jOOQ and the database agree on how to handle backslash escaping. This is
     * only a concern with MySQL.
     *
     * @throws DataAccessException
     */
    private final void configureBackslashEscapes(Configuration configuration) throws DataAccessException {
        if (configuration.dialect() == SQLDialect.MYSQL) {
            this.configureBackslashEscapesForMySql();
        }
    }

    /**
     * Enables NO_BACKSLASH_ESCAPES in SESSION.sql_mode when applicable.
     *
     * When talking to a MySQL database, we don't know if the database is going to escape
     * backslashes or not. This method looks at the connection string, and if it does not
     * include "&sessionVariables=sql_mode=NO_BACKSLASH_ESCAPES", takes the safe approach
     * of setting NO_BACKSLASH_ESCAPES at the session level.
     *
     * @throws DataAccessException
     */
    private final void configureBackslashEscapesForMySql() {
        Connection connection = getRawConnection();
        try {
            // For performance reasons, we don't parse the URL. We assume that if the URL contains
            // sessionVariables=sql_mode=NO_BACKSLASH_ESCAPES, then we don't need to set
            // NO_BACKSLASH_ESCAPES. This check saves us four round trips to the database). Not formally
            // parsing the URL is obviously not fool proof!
            //
            // Note: .getMetaData().getURL() does not cause a round trip, even though the URL is
            //       read from the metadata.
            if (connection.getMetaData().getURL().contains("sessionVariables=sql_mode=NO_BACKSLASH_ESCAPES")) {
                return;
            }

            // TODO: while we are doing all this, we should perhaps also check that the encoding is UTF-8?

            // Avoid paying the cost of this query when connections are re-used.
            if (SettingsEnabledConnection.connections.containsKey(connection)) {
                return;
            }
            SettingsEnabledConnection.connections.put(connection, true);

            connection
                .prepareStatement(
                    "SET @@SESSION.sql_mode = CONCAT(@@SESSION.sql_mode, ',' ,'NO_BACKSLASH_ESCAPES');")
                .execute();

        } catch (SQLException e) {
            throw Utils.translate("Failed configuring NO_BACKSLASH_ESCAPES", e);
        }
    }

    /**
     * Returns the underlying connection.
     *
     * Mainly useful when dealing with ProviderEnabledConnection, so that we can keep track of which
     * connections have had their backslashes configured.
     */
    private final Connection getRawConnection() {
        Connection r = getDelegate();
        while (r instanceof DefaultConnection) {
            r = ((DefaultConnection) r).getDelegate();
        }
        return r;
    }

    // ------------------------------------------------------------------------
    // XXX Creation of PreparedStatements
    // ------------------------------------------------------------------------

    @Override
    public final PreparedStatement prepareStatement(String sql) throws SQLException {
        if (executePreparedStatements(settings)) {
            return getDelegate().prepareStatement(sql);
        }
        else {
            return new SettingsEnabledPreparedStatement(getDelegate(), sql);
        }
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
        throws SQLException {
        if (executePreparedStatements(settings)) {
            return getDelegate().prepareStatement(sql, resultSetType, resultSetConcurrency);
        }
        else {
            return new SettingsEnabledPreparedStatement(getDelegate(), sql, resultSetType, resultSetConcurrency);
        }
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        if (executePreparedStatements(settings)) {
            return getDelegate().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        else {
            return new SettingsEnabledPreparedStatement(getDelegate(), sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        if (executePreparedStatements(settings)) {
            return getDelegate().prepareStatement(sql, autoGeneratedKeys);
        }
        else {
            return new SettingsEnabledPreparedStatement(getDelegate(), sql, autoGeneratedKeys);
        }
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        if (executePreparedStatements(settings)) {
            return getDelegate().prepareStatement(sql, columnIndexes);
        }
        else {
            return new SettingsEnabledPreparedStatement(getDelegate(), sql, columnIndexes);
        }
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        if (executePreparedStatements(settings)) {
            return getDelegate().prepareStatement(sql, columnNames);
        }
        else {
            return new SettingsEnabledPreparedStatement(getDelegate(), sql, columnNames);
        }
    }
}
