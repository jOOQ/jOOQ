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

/**
 * An <code>FunctionalInterface</code> that wraps transactional code.
 * <p>
 * Transactional code should not depend on any captured scope, but use the
 * argument {@link Configuration} passed to the {@link #run(Configuration)}
 * method to derive its transaction context.
 * <p>
 * If transactional code needs to depend on captured scope ("context"), then
 * {@link ContextTransactionalRunnable} is a better fit.
 *
 * @author Lukas Eder
 */

@FunctionalInterface

public interface TransactionalRunnable {

    /**
     * Run the transactional code.
     * <p>
     * If this method completes normally, and this is not a nested transaction,
     * then the transaction will be committed. If this method completes with an
     * exception, then the transaction is rolled back to the beginning of this
     * <code>TransactionalRunnable</code>.
     *
     * @param configuration The <code>Configuration</code> in whose context the
     *            transaction is run.
     * @throws Exception Any exception that will cause a rollback of the code
     *             contained in this transaction. If this is a nested
     *             transaction, the rollback may be performed only to the state
     *             before executing this <code>TransactionalRunnable</code>.
     */
    void run(Configuration configuration) throws Exception;
}
