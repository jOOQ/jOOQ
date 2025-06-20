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

import static java.lang.Boolean.TRUE;
import static java.sql.Connection.TRANSACTION_READ_COMMITTED;
import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;
import static java.sql.Connection.TRANSACTION_REPEATABLE_READ;
import static java.sql.Connection.TRANSACTION_SERIALIZABLE;
import static java.util.Arrays.asList;
import static org.jooq.Isolation.READ_COMMITTED;
import static org.jooq.Isolation.READ_UNCOMMITTED;
import static org.jooq.Isolation.REPEATABLE_READ;
import static org.jooq.Isolation.SERIALIZABLE;
import static org.jooq.Readonly.READONLY;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_READONLY;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_ISOLATION;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.Isolation;
import org.jooq.TransactionContext;
import org.jooq.TransactionProperty;
import org.jooq.TransactionProvider;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.Tools.BooleanDataKey;
import org.jooq.impl.Tools.SimpleDataKey;

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
     * This {@link Savepoint} serves as a marker for top level transactions if
     * {@link #nested()} transactions are deactivated.
     */
    private static final Savepoint         IGNORED_SAVEPOINT = new DefaultSavepoint();

    private final ConnectionProvider       connectionProvider;
    private final boolean                  nested;
    private final Set<TransactionProperty> properties;

    public DefaultTransactionProvider(ConnectionProvider connectionProvider) {
        this(connectionProvider, true);
    }

    /**
     * @param nested Whether nested transactions via {@link Savepoint}s are
     *            supported.
     */
    public DefaultTransactionProvider(ConnectionProvider connectionProvider, boolean nested) {
        this(connectionProvider, nested, new TransactionProperty[0]);
    }

    /**
     * @param nested Whether nested transactions via {@link Savepoint}s are
     *            supported.
     * @param properties The default transaction properties that are used to
     *            create transactions from this provider.
     */
    public DefaultTransactionProvider(ConnectionProvider connectionProvider, boolean nested, TransactionProperty... properties) {
        this.connectionProvider = connectionProvider;
        this.nested = nested;
        this.properties = new LinkedHashSet<>(asList(properties));
    }

    @Override
    public final Set<TransactionProperty> properties() {
        return Collections.unmodifiableSet(properties);
    }

    public final boolean nested() {
        return nested;
    }

    final int nestingLevel(TransactionContext ctx) {
        return savepoints(ctx).size();
    }

    @SuppressWarnings("unchecked")
    private final Deque<Savepoint> savepoints(TransactionContext ctx) {
        Deque<Savepoint> savepoints = (Deque<Savepoint>) ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS);

        if (savepoints == null) {
            savepoints = new ArrayDeque<>();
            ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS, savepoints);
        }

        return savepoints;
    }

    private final Integer isolation(TransactionContext ctx) {
        Integer isolation = (Integer) ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_ISOLATION);

        if (isolation == null) {
            isolation = connection(ctx).getTransactionIsolation();
            ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_ISOLATION, isolation);
        }

        return isolation;
    }

    private final boolean autoCommit(TransactionContext ctx) {
        Boolean autoCommit = (Boolean) ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT);

        if (!TRUE.equals(autoCommit)) {
            autoCommit = connection(ctx).getAutoCommit();
            ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT, autoCommit);
        }

        return autoCommit;
    }

    private final boolean readonly(TransactionContext ctx) {
        Boolean readonly = (Boolean) ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_READONLY);

        if (readonly == null) {
            readonly = connection(ctx).isReadOnly();
            ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_READONLY, readonly);
        }

        return readonly;
    }

    private final DefaultConnectionProvider connection(TransactionContext ctx) {
        DefaultConnectionProvider connectionWrapper = (DefaultConnectionProvider) ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION);

        if (connectionWrapper == null) {
            connectionWrapper = new DefaultConnectionProvider(connectionProvider.acquire());
            ctx.configuration().data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION, connectionWrapper);
        }

        return connectionWrapper;
    }

    @Override
    public final void begin(TransactionContext ctx) {
        Deque<Savepoint> savepoints = savepoints(ctx);

        // This is the top-level transaction
        boolean topLevel = savepoints.isEmpty();
        if (topLevel)
            brace(ctx, true);

        savepoints.push(setSavepoint(ctx, topLevel));
    }

    private final Savepoint setSavepoint(TransactionContext ctx, boolean topLevel) {
        if (topLevel || !nested())
            return IGNORED_SAVEPOINT;
        else
            return connection(ctx).setSavepoint();
    }

    @Override
    public final void commit(TransactionContext ctx) {
        Deque<Savepoint> savepoints = savepoints(ctx);
        Savepoint savepoint = savepoints.pop();

        // [#3489] Explicitly release savepoints prior to commit
        if (savepoint != null && savepoint != IGNORED_SAVEPOINT)
            try {
                connection(ctx).releaseSavepoint(savepoint);
            }

            // [#3537] Ignore those cases where the JDBC driver incompletely implements the API
            // See also http://stackoverflow.com/q/10667292/521799
            catch (DataAccessException ignore) {}

        // This is the top-level transaction
        if (savepoints.isEmpty()) {
            connection(ctx).commit();
            brace(ctx, false);
        }

        // Nested commits have no effect
        else {
        }
    }

    @Override
    public final void rollback(TransactionContext ctx) {
        Deque<Savepoint> savepoints = savepoints(ctx);
        Savepoint savepoint = null;

        // [#3537] If something went wrong with the savepoints per se
        if (!savepoints.isEmpty())
            savepoint = savepoints.pop();

        try {
            if (savepoint == null) {
                connection(ctx).rollback();
            }

            // [#3955] ROLLBACK is only effective if an exception reaches the
            //         top-level transaction.
            else if (savepoint == IGNORED_SAVEPOINT) {
                if (savepoints.isEmpty())
                    connection(ctx).rollback();
            }
            else
                connection(ctx).rollback(savepoint);
        }

        finally {
            if (savepoints.isEmpty())
                brace(ctx, false);
        }
    }

    /**
     * Ensure an <code>autoCommit</code> value on the connection, if it was set
     * to <code>true</code>, originally.
     */
    private final void brace(TransactionContext ctx, boolean start) {
        DefaultConnectionProvider connection = connection(ctx);

        try {

            // [#4836] Reset readonly flag at the end of a transaction
            boolean readonly = readonly(ctx);

            if (ctx.properties().contains(READONLY))
                connection(ctx).setReadOnly(start ? true : readonly);

            int isolation = isolation(ctx);

            if (ctx.properties().contains(READ_COMMITTED))
                connection(ctx).setTransactionIsolation(start ? TRANSACTION_READ_COMMITTED : isolation);
            else if (ctx.properties().contains(READ_UNCOMMITTED))
                connection(ctx).setTransactionIsolation(start ? TRANSACTION_READ_UNCOMMITTED : isolation);
            else if (ctx.properties().contains(REPEATABLE_READ))
                connection(ctx).setTransactionIsolation(start ? TRANSACTION_REPEATABLE_READ : isolation);
            else if (ctx.properties().contains(SERIALIZABLE))
                connection(ctx).setTransactionIsolation(start ? TRANSACTION_SERIALIZABLE : isolation);

            boolean autoCommit = autoCommit(ctx);

            // Transactions cannot run with autoCommit = true. Change the value for
            // the duration of a transaction
            if (autoCommit)
                connection.setAutoCommit(!start);
        }

        // [#3718] Chances are that the above JDBC interactions throw additional exceptions
        //         try-finally will ensure that the ConnectionProvider.release() call is made
        finally {
            if (!start) {
                connectionProvider.release(connection.connection);
                ctx.configuration().data().remove(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION);
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
