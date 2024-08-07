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

import static org.jooq.conf.InvocationOrder.REVERSE;
import static org.jooq.impl.Tools.map;

import java.util.Arrays;

import org.jooq.Configuration;
import org.jooq.TransactionContext;
import org.jooq.TransactionListener;
import org.jooq.TransactionListenerProvider;

/**
 * @author Lukas Eder
 */
class TransactionListeners implements TransactionListener {

    private final TransactionListener[] listeners;

    TransactionListeners(Configuration configuration) {
        listeners = map(configuration.transactionListenerProviders(), p -> p.provide(), TransactionListener[]::new);
    }

    @Override
    public final void beginStart(TransactionContext ctx) {
        for (TransactionListener listener : ctx.settings().getTransactionListenerStartInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.beginStart(ctx);
    }

    @Override
    public final void beginEnd(TransactionContext ctx) {
        for (TransactionListener listener : ctx.settings().getTransactionListenerEndInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.beginEnd(ctx);
    }

    @Override
    public final void commitStart(TransactionContext ctx) {
        for (TransactionListener listener : ctx.settings().getTransactionListenerStartInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.commitStart(ctx);
    }

    @Override
    public final void commitEnd(TransactionContext ctx) {
        for (TransactionListener listener : ctx.settings().getTransactionListenerEndInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.commitEnd(ctx);
    }

    @Override
    public final void rollbackStart(TransactionContext ctx) {
        for (TransactionListener listener : ctx.settings().getTransactionListenerStartInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.rollbackStart(ctx);
    }

    @Override
    public final void rollbackEnd(TransactionContext ctx) {
        for (TransactionListener listener : ctx.settings().getTransactionListenerEndInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.rollbackEnd(ctx);
    }

}
