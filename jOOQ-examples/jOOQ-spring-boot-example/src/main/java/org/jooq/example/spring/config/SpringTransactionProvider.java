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
package org.jooq.example.spring.config;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;

import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.jooq.tools.JooqLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * An example <code>TransactionProvider</code> implementing the {@link TransactionProvider} contract for use with
 * Spring.
 *
 * @author Lukas Eder
 */
public class SpringTransactionProvider implements TransactionProvider {

	private static final JooqLogger log = JooqLogger.getLogger(SpringTransactionProvider.class);

	@Autowired DataSourceTransactionManager txMgr;

	@Override
	public void begin(TransactionContext ctx) {
		log.info("Begin transaction");

		// This TransactionProvider behaves like jOOQ's DefaultTransactionProvider,
		// which supports nested transactions using Savepoints
		TransactionStatus tx = txMgr.getTransaction(new DefaultTransactionDefinition(PROPAGATION_NESTED));
		ctx.transaction(new SpringTransaction(tx));
	}

	@Override
	public void commit(TransactionContext ctx) {
		log.info("commit transaction");

		txMgr.commit(((SpringTransaction) ctx.transaction()).tx);
	}

	@Override
	public void rollback(TransactionContext ctx) {
		log.info("rollback transaction");

		txMgr.rollback(((SpringTransaction) ctx.transaction()).tx);
	}
}
