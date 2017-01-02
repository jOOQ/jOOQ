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

import org.jooq.impl.ThreadLocalTransactionProvider;

/**
 * An <code>FunctionalInterface</code> that wraps transactional code.
 * <p>
 * This callable may depend on captured scope ("context") in order to discover a
 * contextual {@link Configuration} to be used to create new SQL statements.
 * Clients are responsible to implement such context state in appropriate
 * {@link ConnectionProvider} and {@link TransactionProvider} implementations.
 * <p>
 * An out-of-the-box implementation for a fitting {@link TransactionProvider} is
 * available through {@link ThreadLocalTransactionProvider}.
 *
 * @author Lukas Eder
 */

@FunctionalInterface

public interface ContextTransactionalCallable<T> {

    /**
     * Run the transactional code.
     * <p>
     * If this method completes normally, and this is not a nested transaction,
     * then the transaction will be committed. If this method completes with an
     * exception, then the transaction is rolled back to the beginning of this
     * <code>ContextTransactionalCallable</code>.
     *
     * @return The outcome of the transaction.
     * @throws Exception Any exception that will cause a rollback of the code
     *             contained in this transaction. If this is a nested
     *             transaction, the rollback may be performed only to the state
     *             before executing this
     *             <code>ContextTransactionalCallable</code>.
     */
    T run() throws Exception;
}
