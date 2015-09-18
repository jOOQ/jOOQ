/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.impl;

import static org.jooq.impl.Utils.DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT;
import static org.jooq.impl.Utils.DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION;
import static org.jooq.impl.Utils.DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Stack;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.jooq.exception.DataAccessException;

/**
 * A default implementation for the {@link TransactionProvider} SPI.
 * <p>
 * This implementation is entirely based on JDBC transactions and is intended to
 * work with {@link DefaultConnectionProvider} (which is implicitly created when
 * using {@link DSL#using(Connection)}). It supports nested transactions by
 * modeling them implicitly with JDBC {@link Savepoint}s, if supported by the
 * underlying JDBC driver.
 *
 * @author Lukas Eder
 */
public class DefaultTransactionProvider implements TransactionProvider {

    /**
     * This dummy {@link Savepoint} serves as a marker for top level
     * transactions in dialects that do not support Savepoints.
     */
    private static final Savepoint   DUMMY_SAVEPOINT = new DefaultSavepoint();

    private final ConnectionProvider provider;

    public DefaultTransactionProvider(ConnectionProvider provider) {
        this.provider = provider;
    }

    @SuppressWarnings("unchecked")
    private final Stack<Savepoint> savepoints(Configuration configuration) {
        Stack<Savepoint> savepoints = (Stack<Savepoint>) configuration.data(DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS);

        if (savepoints == null) {
            savepoints = new Stack<Savepoint>();
            configuration.data(DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS, savepoints);
        }

        return savepoints;
    }

    private final boolean autoCommit(Configuration configuration) {
        Boolean autoCommit = (Boolean) configuration.data(DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT);

        if (autoCommit == null) {
            autoCommit = connection(configuration).getAutoCommit();
            configuration.data(DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT, autoCommit);
        }

        return autoCommit;
    }

    private final DefaultConnectionProvider connection(Configuration configuration) {
        DefaultConnectionProvider connectionWrapper = (DefaultConnectionProvider) configuration.data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION);

        if (connectionWrapper == null) {
            connectionWrapper = new DefaultConnectionProvider(provider.acquire());
            configuration.data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION, connectionWrapper);
        }

        return connectionWrapper;
    }

    @Override
    public final void begin(TransactionContext ctx) {
        Stack<Savepoint> savepoints = savepoints(ctx.configuration());

        // This is the top-level transaction
        if (savepoints.isEmpty()) {
            brace(ctx.configuration(), true);
        }

        Savepoint savepoint = setSavepoint(ctx.configuration());

        if (savepoint == DUMMY_SAVEPOINT && savepoints.size() > 0)
            throw new DataAccessException("Cannot nest transactions because Savepoints are not supported");

        savepoints.push(savepoint);
    }

    private Savepoint setSavepoint(Configuration configuration) {
        switch (configuration.family()) {
            /* [pro] xx
            xxxx xxxxx
            xx [/pro] */
            case CUBRID:
                return DUMMY_SAVEPOINT;
            default:
                return connection(configuration).setSavepoint();
        }
    }

    @Override
    public final void commit(TransactionContext ctx) {
        Stack<Savepoint> savepoints = savepoints(ctx.configuration());
        Savepoint savepoint = savepoints.pop();

        // [#3489] Explicitly release savepoints prior to commit
        if (savepoint != null && savepoint != DUMMY_SAVEPOINT)
            try {
                connection(ctx.configuration()).releaseSavepoint(savepoint);
            }

            // [#3537] Ignore those cases where the JDBC driver incompletely implements the API
            // See also http://stackoverflow.com/q/10667292/521799
            catch (DataAccessException ignore) {}

        // This is the top-level transaction
        if (savepoints.isEmpty()) {
            connection(ctx.configuration()).commit();
            brace(ctx.configuration(), false);
        }

        // Nested commits have no effect
        else {
        }
    }

    @Override
    public final void rollback(TransactionContext ctx) {
        Stack<Savepoint> savepoints = savepoints(ctx.configuration());
        Savepoint savepoint = null;

        // [#3537] If something went wrong with the savepoints per se
        if (!savepoints.isEmpty())
            savepoint = savepoints.pop();

        try {
            if (savepoint == null || savepoint == DUMMY_SAVEPOINT)
                connection(ctx.configuration()).rollback();
            else
                connection(ctx.configuration()).rollback(savepoint);
        }

        finally {
            if (savepoints.isEmpty())
                brace(ctx.configuration(), false);
        }
    }

    /**
     * Ensure an <code>autoCommit</code> value on the connection, if it was set
     * to <code>true</code>, originally.
     */
    private void brace(Configuration configuration, boolean start) {
        DefaultConnectionProvider connection = connection(configuration);

        try {
            boolean autoCommit = autoCommit(configuration);

            // Transactions cannot run with autoCommit = true. Change the value for
            // the duration of a transaction
            if (autoCommit == true)
                connection.setAutoCommit(!start);
        }

        // [#3718] Chances are that the above JDBC interactions throw additional exceptions
        //         try-finally will ensure that the ConnectionProvider.release() call is made
        finally {
            if (!start) {
                provider.release(connection.connection);
                configuration.data().remove(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION);
            }
        }
    }

    private static class DefaultSavepoint implements Savepoint {
        @Override
        public int getSavepointId() throws SQLException {
            return 0;
        }

        @Override
        public String getSavepointName() throws SQLException {
            return null;
        }
    }
}
