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

import org.jooq.impl.DefaultTransactionListenerProvider;

/**
 * A provider for {@link TransactionListener} instances.
 * <p>
 * In order to facilitate the lifecycle management of
 * <code>TransactionListener</code> instances that are provided to a jOOQ
 * {@link Configuration}, clients can implement this API. To jOOQ, it is thus
 * irrelevant, if transaction listeners are stateful or stateless, local to an
 * execution, or global to an application.
 *
 * @author Lukas Eder
 * @see TransactionListener
 * @see Configuration
 */

@FunctionalInterface

public interface TransactionListenerProvider {

    /**
     * Provide an <code>TransactionListener</code> instance.
     * <p>
     * Implementations are free to choose whether this method returns new
     * instances at every call or whether the same instance is returned
     * repetitively.
     * <p>
     * A <code>TransactionListener</code> shall be provided exactly once per
     * transaction lifecycle, i.e. per call to
     * {@link DSLContext#transaction(TransactionalRunnable)} or similar API.
     *
     * @return An <code>TransactionListener</code> instance.
     * @see TransactionListener
     * @see TransactionProvider
     * @see DefaultTransactionListenerProvider
     */
    TransactionListener provide();
}
