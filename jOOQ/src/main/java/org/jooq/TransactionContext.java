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
package org.jooq;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A context object that is used to pass arguments to the various methods of
 * {@link TransactionProvider}.
 * <p>
 * This type is a {@link Scope} with independent lifecycle and its own
 * {@link #data()} map.
 *
 * @author Lukas Eder
 */
public interface TransactionContext extends Scope {

    /**
     * The transaction properties that the transaction was created with.
     */
    Set<TransactionProperty> properties();

    /**
     * A user-defined transaction object, possibly obtained from
     * {@link TransactionProvider#begin(TransactionContext)}.
     *
     * @return The transaction object. May be <code>null</code>.
     */
    @Nullable
    Transaction transaction();

    /**
     * Set the user-defined transaction object to the current transaction
     * context.
     */
    @NotNull
    TransactionContext transaction(Transaction transaction);

    /**
     * The result of a {@link TransactionalCallable} or
     * {@link ContextTransactionalCallable}, if the transaction has completed
     * successfully.
     *
     * @return The result. May be <code>null</code> if the transaction hasn't
     *         completed yet, or if there was no result (e.g. in a
     *         {@link TransactionalRunnable} or
     *         {@link ContextTransactionalRunnable}), or if the result is
     *         <code>null</code>.
     */
    @Nullable
    Object result();

    /**
     * Set the result of the {@link TransactionalCallable} or
     * {@link ContextTransactionalCallable}.
     */
    @NotNull
    TransactionContext result(Object result);

    /**
     * The exception that has caused the rollback.
     *
     * @return The exception. May be <code>null</code>, in particular if the
     *         cause is a {@link Throwable}, in case of which
     *         {@link #causeThrowable()} should be called.
     */
    @Nullable
    Exception cause();

    /**
     * The throwable that has caused the rollback.
     *
     * @return The throwable. May be <code>null</code>.
     */
    @Nullable
    Throwable causeThrowable();

    /**
     * Set the exception that has caused the rollback to the current transaction
     * context.
     */
    @NotNull
    TransactionContext cause(Exception cause);

    /**
     * Set the throwable that has caused the rollback to the current transaction
     * context.
     */
    @NotNull
    TransactionContext causeThrowable(Throwable cause);
}
