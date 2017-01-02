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
package org.jooq.impl;

import static org.jooq.impl.Tools.DataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT;
import static org.jooq.impl.Tools.DataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION;
import static org.jooq.impl.Tools.DataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayDeque;
import java.util.Deque;

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
 * using {@link DSL#using(Connection)}).
 * <p>
 * <h3>Nesting of transactions</h3> By default, nested transactions are
 * supported by modeling them implicitly with JDBC {@link Savepoint}s, if
 * supported by the underlying JDBC driver, and if {@link #nested()} is
 * <code>true</code>. To deactivate nested transactions, use
 * {@link #DefaultTransactionProvider(ConnectionProvider, boolean)}.
 *
 * @author Lukas Eder
 */
public class DefaultTransactionProvider implements TransactionProvider {

    /**
     * This {@link Savepoint} serves as a marker for top level
     * transactions in dialects that do not support Savepoints.
     */
    private static final Savepoint UNSUPPORTED_SAVEPOINT = new DefaultSavepoint();

    /**
     * This {@link Savepoint} serves as a marker for top level
     * transactions if {@link #nested()} transactions are deactivated.
     */
    private static final Savepoint IGNORED_SAVEPOINT     = new DefaultSavepoint();

    private final ConnectionProvider connectionProvider;
    private final boolean            nested;

    public DefaultTransactionProvider(ConnectionProvider connectionProvider) {
        this(connectionProvider, true);
    }

    /**
     * @param nested Whether nested transactions via {@link Savepoint}s are
     *            supported.
     */
    public DefaultTransactionProvider(ConnectionProvider connectionProvider, boolean nested) {
        this.connectionProvider = connectionProvider;
        this.nested = nested;
    }

    public final boolean nested() {
        return nested;
    }

    final int nestingLevel(Configuration configuration) {
        return savepoints(configuration).size();
    }

    @SuppressWarnings("unchecked")
    private final Deque<Savepoint> savepoints(Configuration configuration) {
        Deque<Savepoint> savepoints = (Deque<Savepoint>) configuration.data(DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS);

        if (savepoints == null) {
            savepoints = new ArrayDeque<Savepoint>();
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
            connectionWrapper = new DefaultConnectionProvider(connectionProvider.acquire());
            configuration.data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION, connectionWrapper);
        }

        return connectionWrapper;
    }

    @Override
    public final void begin(TransactionContext ctx) {
        Deque<Savepoint> savepoints = savepoints(ctx.configuration());

        // This is the top-level transaction
        if (savepoints.isEmpty())
            brace(ctx.configuration(), true);

        Savepoint savepoint = setSavepoint(ctx.configuration());

        if (savepoint == UNSUPPORTED_SAVEPOINT && !savepoints.isEmpty())
            throw new DataAccessException("Cannot nest transactions because Savepoints are not supported");

        savepoints.push(savepoint);
    }

    private final Savepoint setSavepoint(Configuration configuration) {
        if (!nested())
            return IGNORED_SAVEPOINT;

        switch (configuration.family()) {



            case CUBRID:
                return UNSUPPORTED_SAVEPOINT;
            default:
                return connection(configuration).setSavepoint();
        }
    }

    @Override
    public final void commit(TransactionContext ctx) {
        Deque<Savepoint> savepoints = savepoints(ctx.configuration());
        Savepoint savepoint = savepoints.pop();

        // [#3489] Explicitly release savepoints prior to commit
        if (savepoint != null && savepoint != UNSUPPORTED_SAVEPOINT && savepoint != IGNORED_SAVEPOINT)
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
        Deque<Savepoint> savepoints = savepoints(ctx.configuration());
        Savepoint savepoint = null;

        // [#3537] If something went wrong with the savepoints per se
        if (!savepoints.isEmpty())
            savepoint = savepoints.pop();

        try {
            if (savepoint == null || savepoint == UNSUPPORTED_SAVEPOINT) {
                connection(ctx.configuration()).rollback();
            }

            // [#3955] ROLLBACK is only effective if an exception reaches the
            //         top-level transaction.
            else if (savepoint == IGNORED_SAVEPOINT) {
                if (savepoints.isEmpty())
                    connection(ctx.configuration()).rollback();
            }
            else {
                connection(ctx.configuration()).rollback(savepoint);
            }
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
    private final void brace(Configuration configuration, boolean start) {
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
                connectionProvider.release(connection.connection);
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
