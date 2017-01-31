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
     * @param ctx the configuration scoped to this transaction and its
     *            nested transactions.
     * @throws DataAccessException Any exception issued by the underlying
     *             database.
     */
    void begin(TransactionContext ctx) throws DataAccessException;

    /**
     * Commit a transaction.
     *
     * @param ctx the configuration scoped to this transaction and its nested
     *            transactions.
     * @throws DataAccessException Any exception issued by the underlying
     *             database.
     */
    void commit(TransactionContext ctx) throws DataAccessException;

    /**
     * Rollback a transaction.
     *
     * @param ctx the configuration scoped to this transaction and its nested
     *            transactions.
     * @throws DataAccessException Any exception issued by the underlying
     *             database.
     */
    void rollback(TransactionContext ctx) throws DataAccessException;

}
