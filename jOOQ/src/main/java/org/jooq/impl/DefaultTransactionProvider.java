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

import static org.jooq.impl.Utils.DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT;
import static org.jooq.impl.Utils.DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS;

import java.sql.Connection;
import java.sql.Savepoint;
import java.util.Stack;

import org.jooq.Configuration;
import org.jooq.Transaction;
import org.jooq.TransactionProvider;

/**
 * A default implementation for the {@link TransactionProvider} SPI.
 * <p>
 * This implementation is entirely based on JDBC transactions and is intended to
 * work with {@link DefaultConnectionProvider} (which is implicitly created when
 * using {@link DSL#using(Connection)}). It supports nested transactions by
 * modelling them implicitly with JDBC {@link Savepoint}s, if supported by the
 * underlying JDBC driver.
 *
 * @author Lukas Eder
 */
public class DefaultTransactionProvider implements TransactionProvider {

    private final DefaultConnectionProvider connection;

    public DefaultTransactionProvider(DefaultConnectionProvider connection) {
        this.connection = connection;
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
            autoCommit = connection.getAutoCommit();
            configuration.data(DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT, autoCommit);
        }

        return autoCommit;
    }

    @Override
    public final Transaction begin(Configuration configuration) {
        Stack<Savepoint> savepoints = savepoints(configuration);

        // This is the top-level transaction
        if (savepoints.isEmpty()) {
            autoCommit(configuration, false);
        }

        savepoints.push(connection.setSavepoint());
        return null;
    }

    @Override
    public final void commit(Configuration configuration, Transaction transaction) {
        Stack<Savepoint> savepoints = savepoints(configuration);
        savepoints.pop();

        // This is the top-level transaction
        if (savepoints.isEmpty()) {
            connection.commit();
            autoCommit(configuration, true);
        }

        // Nested commits have no effect
        else {
        }
    }

    @Override
    public final void rollback(Configuration configuration, Transaction transaction, Exception cause) {
        Stack<Savepoint> savepoints = savepoints(configuration);
        Savepoint savepoint = savepoints.pop();

        try {
            connection.rollback(savepoint);
        }

        finally {
            if (savepoints.isEmpty())
                autoCommit(configuration, true);
        }
    }

    /**
     * Ensure an <code>autoCommit</code> value on the connection, if it was set
     * to <code>true</code>, originally.
     */
    private void autoCommit(Configuration configuration, boolean newValue) {
        boolean oldValue = autoCommit(configuration);

        // Transactions cannot run with autoCommit = true. Change the value for
        // the duration of a transaction
        if (oldValue == true) {
            connection.setAutoCommit(newValue);
        }
    }
}
