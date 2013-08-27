/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
