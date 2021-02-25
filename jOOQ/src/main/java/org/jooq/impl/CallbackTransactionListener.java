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
package org.jooq.impl;


import java.util.function.Consumer;

import org.jooq.RecordContext;
import org.jooq.RecordListener;
import org.jooq.TransactionContext;
import org.jooq.TransactionListener;

/**
 * A {@link TransactionListener} that allows for functional composition.
 * <p>
 * For example: <code><pre>
 * TransactionListener listener = TransactionListener
 *   .onCommitStart(ctx -&gt; something())
 *   .onCommitEnd(ctx -&gt; something());
 * </pre></code>
 *
 * @author Lukas Eder
 */
public final class CallbackTransactionListener implements TransactionListener {

    private final Consumer<? super TransactionContext> onBeginStart;
    private final Consumer<? super TransactionContext> onBeginEnd;
    private final Consumer<? super TransactionContext> onCommitStart;
    private final Consumer<? super TransactionContext> onCommitEnd;
    private final Consumer<? super TransactionContext> onRollbackStart;
    private final Consumer<? super TransactionContext> onRollbackEnd;

    public CallbackTransactionListener() {
        this(null, null, null, null, null, null);
    }

    private CallbackTransactionListener(
        Consumer<? super TransactionContext> onBeginStart,
        Consumer<? super TransactionContext> onBeginEnd,
        Consumer<? super TransactionContext> onCommitStart,
        Consumer<? super TransactionContext> onCommitEnd,
        Consumer<? super TransactionContext> onRollbackStart,
        Consumer<? super TransactionContext> onRollbackEnd
    ) {
        this.onBeginStart = onBeginStart;
        this.onBeginEnd = onBeginEnd;
        this.onCommitStart = onCommitStart;
        this.onCommitEnd = onCommitEnd;
        this.onRollbackStart = onRollbackStart;
        this.onRollbackEnd = onRollbackEnd;
    }

    @Override
    public final void beginStart(TransactionContext ctx) {
        if (onBeginStart != null)
            onBeginStart.accept(ctx);
    }

    @Override
    public final void beginEnd(TransactionContext ctx) {
        if (onBeginEnd != null)
            onBeginEnd.accept(ctx);
    }

    @Override
    public final void commitStart(TransactionContext ctx) {
        if (onCommitStart != null)
            onCommitStart.accept(ctx);
    }

    @Override
    public final void commitEnd(TransactionContext ctx) {
        if (onCommitEnd != null)
            onCommitEnd.accept(ctx);
    }

    @Override
    public final void rollbackStart(TransactionContext ctx) {
        if (onRollbackStart != null)
            onRollbackStart.accept(ctx);
    }

    @Override
    public final void rollbackEnd(TransactionContext ctx) {
        if (onRollbackEnd != null)
            onRollbackEnd.accept(ctx);
    }

    public final CallbackTransactionListener onBeginStart(Consumer<? super TransactionContext> newOnBeginStart) {
        return new CallbackTransactionListener(
            newOnBeginStart,
            onBeginEnd,
            onCommitStart,
            onCommitEnd,
            onRollbackStart,
            onRollbackEnd
        );
    }

    public final CallbackTransactionListener onBeginEnd(Consumer<? super TransactionContext> newOnBeginEnd) {
        return new CallbackTransactionListener(
            onBeginStart,
            newOnBeginEnd,
            onCommitStart,
            onCommitEnd,
            onRollbackStart,
            onRollbackEnd
        );
    }

    public final CallbackTransactionListener onCommitStart(Consumer<? super TransactionContext> newOnCommitStart) {
        return new CallbackTransactionListener(
            onBeginStart,
            onBeginEnd,
            newOnCommitStart,
            onCommitEnd,
            onRollbackStart,

            onRollbackEnd);
    }

    public final CallbackTransactionListener onCommitEnd(Consumer<? super TransactionContext> newOnCommitEnd) {
        return new CallbackTransactionListener(
            onBeginStart,
            onBeginEnd,
            onCommitStart,
            newOnCommitEnd,
            onRollbackStart,
            onRollbackEnd
        );
    }

    public final CallbackTransactionListener onRollbackStart(Consumer<? super TransactionContext> newOnRollbackStart) {
        return new CallbackTransactionListener(
            onBeginStart,
            onBeginEnd,
            onCommitStart,
            onCommitEnd,
            newOnRollbackStart,
            onRollbackEnd
        );
    }

    public final CallbackTransactionListener onRollbackEnd(Consumer<? super TransactionContext> newOnRollbackEnd) {
        return new CallbackTransactionListener(
            onBeginStart,
            onBeginEnd,
            onCommitStart,
            onCommitEnd,
            onRollbackStart,
            newOnRollbackEnd
        );
    }
}

