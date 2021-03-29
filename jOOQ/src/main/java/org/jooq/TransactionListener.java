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
 *
 *
 *
 */
package org.jooq;

import java.util.function.Consumer;

import org.jooq.impl.CallbackTransactionListener;

/**
 * The <code>TransactionListener</code> SPI is used to intercept the
 * {@link TransactionProvider}'s transaction events.
 *
 * @author Lukas Eder
 */
public interface TransactionListener {

    /**
     * Called before {@link TransactionProvider#begin(TransactionContext)}.
     */
    void beginStart(TransactionContext ctx);

    /**
     * Called after {@link TransactionProvider#begin(TransactionContext)}.
     */
    void beginEnd(TransactionContext ctx);

    /**
     * Called before {@link TransactionProvider#commit(TransactionContext)}.
     */
    void commitStart(TransactionContext ctx);

    /**
     * Called after {@link TransactionProvider#commit(TransactionContext)}.
     */
    void commitEnd(TransactionContext ctx);

    /**
     * Called before {@link TransactionProvider#rollback(TransactionContext)}.
     */
    void rollbackStart(TransactionContext ctx);

    /**
     * Called after {@link TransactionProvider#rollback(TransactionContext)}.
     */
    void rollbackEnd(TransactionContext ctx);

    /**
     * Create a {@link TransactionListener} with a
     * {@link #onBeginStart(Consumer)} implementation.
     */
    static CallbackTransactionListener onBeginStart(Consumer<? super TransactionContext> onBeginStart) {
        return new CallbackTransactionListener().onBeginStart(onBeginStart);
    }

    /**
     * Create a {@link TransactionListener} with a {@link #onBeginEnd(Consumer)}
     * implementation.
     */
    static CallbackTransactionListener onBeginEnd(Consumer<? super TransactionContext> onBeginEnd) {
        return new CallbackTransactionListener().onBeginEnd(onBeginEnd);
    }

    /**
     * Create a {@link TransactionListener} with a
     * {@link #onCommitStart(Consumer)} implementation.
     */
    static CallbackTransactionListener onCommitStart(Consumer<? super TransactionContext> onCommitStart) {
        return new CallbackTransactionListener().onCommitStart(onCommitStart);
    }

    /**
     * Create a {@link TransactionListener} with a
     * {@link #onCommitEnd(Consumer)} implementation.
     */
    static CallbackTransactionListener onCommitEnd(Consumer<? super TransactionContext> onCommitEnd) {
        return new CallbackTransactionListener().onCommitEnd(onCommitEnd);
    }

    /**
     * Create a {@link TransactionListener} with a
     * {@link #onRollbackStart(Consumer)} implementation.
     */
    static CallbackTransactionListener onRollbackStart(Consumer<? super TransactionContext> onRollbackStart) {
        return new CallbackTransactionListener().onRollbackStart(onRollbackStart);
    }

    /**
     * Create a {@link TransactionListener} with a
     * {@link #onRollbackEnd(Consumer)} implementation.
     */
    static CallbackTransactionListener onRollbackEnd(Consumer<? super TransactionContext> onRollbackEnd) {
        return new CallbackTransactionListener().onRollbackEnd(onRollbackEnd);
    }
}
