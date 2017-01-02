/*
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
package org.jooq;

import java.sql.Savepoint;

import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultTransactionProvider;

/**
 * The <code>TransactionProvider</code> SPI can be used to implement custom
 * <code>transaction</code> behaviour that is applied when calling
 * {@link DSLContext#transactionResult(TransactionalCallable)} or
 * {@link DSLContext#transaction(TransactionalRunnable)}.
 * <p>
 * A new {@link Configuration} copy is created from the calling
 * {@link DSLContext} for the scope of a single transactions. Implementors may
 * freely add custom data to {@link Configuration#data()}, in order to share
 * information between {@link #begin(TransactionContext)} and
 * {@link #commit(TransactionContext)} or {@link #rollback(TransactionContext)},
 * as well as to share information with nested transactions.
 * <p>
 * Implementors may freely choose whether they support nested transactions. An
 * example implementation supporting nested transactions is
 * {@link DefaultTransactionProvider}, which implements such transactions using
 * JDBC {@link Savepoint}s.
 *
 * @author Lukas Eder
 */
public interface TransactionProvider {

    /**
     * Begin a new transaction.
     * <p>
     * This method begins a new transaction with a {@link Configuration} scoped
     * for this transaction. The resulting {@link Transaction} object may be
     * used by implementors to identify the transaction when
     * {@link #commit(TransactionContext)} or
     * {@link #rollback(TransactionContext)} is called.
     *
     * @param Configuration the configuration scoped to this transaction and its
     *            nested transactions.
     * @throws DataAccessException Any exception issued by the underlying
     *             database.
     */
    void begin(TransactionContext ctx) throws DataAccessException;

    /**
     * @param Configuration the configuration scoped to this transaction and its
     *            nested transactions.
     * @param transaction The user-defined transaction object returned from
     *            {@link #begin(TransactionContext)}. May be <code>null</code>.
     * @throws DataAccessException Any exception issued by the underlying
     *             database.
     */
    void commit(TransactionContext ctx) throws DataAccessException;

    /**
     * @param Configuration the configuration scoped to this transaction and its
     *            nested transactions.
     * @param transaction The user-defined transaction object returned from
     *            {@link #begin(TransactionContext)}. May be <code>null</code>.
     * @param cause The exception that has caused the rollback.
     * @throws DataAccessException Any exception issued by the underlying
     *             database.
     */
    void rollback(TransactionContext ctx) throws DataAccessException;

}
