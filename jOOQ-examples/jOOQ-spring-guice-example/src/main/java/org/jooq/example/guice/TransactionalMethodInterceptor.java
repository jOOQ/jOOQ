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
package org.jooq.example.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.inject.Inject;

/**
 * A {@link MethodInterceptor} that implements nested transactions.
 * <p>
 * Only the outermost transactional method will <code>commit()</code> or
 * <code>rollback()</code> the contextual transaction. This can be verified
 * through {@link TransactionStatus#isNewTransaction()}, which returns
 * <code>true</code> only for the outermost transactional method call.
 * <p>
 * This code is tested only for simple {@link Transactional} methods, without
 * any {@link Propagation} flags.
 *
 * @author Lukas Eder
 */
class TransactionalMethodInterceptor implements MethodInterceptor {

    @Inject
    private DataSourceTransactionManager transactionManager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);

        try {
            Object result = invocation.proceed();

            try {
                if (transaction.isNewTransaction())
                    transactionManager.commit(transaction);
            }
            catch (UnexpectedRollbackException ignore) {}

            return result;
        }
        catch (Exception e) {
            if (transaction.isNewTransaction())
                transactionManager.rollback(transaction);

            throw e;
        }
    }
}
