/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.test.spring;

import org.jooq.DSLContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author Lukas Eder
 */
public class Test {
    public static void main(String[] args) {

        // Configure the Spring application context, loading the bean configuration
        ApplicationContext context =
        new ClassPathXmlApplicationContext(new String[] {
            "jooq-spring-config-minimal.xml"
        });

        // Fetch a DSLContext reference
        DSLContext dsl = context.getBean("dsl", DSLContext.class);

        // Execute a jOOQ query in its isolated "ad-hoc" transaction
        // ---------------------------------------------------------
        System.out.println(dsl.selectOne().fetch());

        // Execute some jOOQ queries in an explicit transaction
        // ----------------------------------------------------
        PlatformTransactionManager transactionManager =
            context.getBean("transactionManager", PlatformTransactionManager.class);

        TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionDefinition());
        dsl.selectOne().fetch();
        dsl.selectOne().fetch();
        dsl.selectOne().fetch();
        transactionManager.commit(tx);
    }
}
