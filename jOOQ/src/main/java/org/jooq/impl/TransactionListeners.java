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
package org.jooq.impl;

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
        TransactionListenerProvider[] providers = configuration.transactionListenerProviders();
        listeners = new TransactionListener[providers.length];

        for (int i = 0; i < providers.length; i++)
            listeners[i] = providers[i].provide();
    }

    @Override
    public final void beginStart(TransactionContext ctx) {
        for (TransactionListener listener : listeners)
            listener.beginStart(ctx);
    }

    @Override
    public final void beginEnd(TransactionContext ctx) {
        for (TransactionListener listener : listeners)
            listener.beginEnd(ctx);
    }

    @Override
    public final void commitStart(TransactionContext ctx) {
        for (TransactionListener listener : listeners)
            listener.commitStart(ctx);
    }

    @Override
    public final void commitEnd(TransactionContext ctx) {
        for (TransactionListener listener : listeners)
            listener.commitEnd(ctx);
    }

    @Override
    public final void rollbackStart(TransactionContext ctx) {
        for (TransactionListener listener : listeners)
            listener.rollbackStart(ctx);
    }

    @Override
    public final void rollbackEnd(TransactionContext ctx) {
        for (TransactionListener listener : listeners)
            listener.rollbackEnd(ctx);
    }

}
