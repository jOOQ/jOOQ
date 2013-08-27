/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.jooq.ConnectionProvider;
import org.jooq.exception.DataAccessException;

/**
 * A default implementation for a pooled {@link DataSource}-oriented
 * {@link ConnectionProvider}
 * <p>
 * This implementation wraps a JDBC {@link DataSource}. jOOQ will use that data
 * source for initialising connections, and creating statements.
 * <p>
 * Use this connection provider if you want to run distributed transactions,
 * such as <code>javax.transaction.UserTransaction</code>. jOOQ will
 * {@link Connection#close() close()} all connections after query execution (and
 * result fetching) in order to return the connection to the connection pool. If
 * you do not use distributed transactions, this will produce driver-specific
 * behaviour at the end of query execution at <code>close()</code> invocation
 * (e.g. a transaction rollback). Use a {@link DefaultConnectionProvider}
 * instead, to control the connection's lifecycle, or implement your own
 * {@link ConnectionProvider}.
 *
 * @author Aaron Digulla
 * @author Lukas Eder
 */
public class DataSourceConnectionProvider implements ConnectionProvider {

    private final DataSource dataSource;
    private Connection       connection;

    public DataSourceConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public Connection acquire() {
        if (null == connection) {
            try {
                connection = dataSource.getConnection();
            }
            catch (SQLException e) {
                throw new DataAccessException("Error getting connection from data source " + dataSource, e);
            }
        }

        return connection;
    }

    @Override
    public void release(Connection released) {
        if (this.connection != released) {
            throw new IllegalArgumentException("Expected " + this.connection + " but got " + released);
        }

        try {
            connection.close();
            this.connection = null;
        }
        catch (SQLException e) {
            throw new DataAccessException("Error closing connection " + connection, e);
        }
    }
}
