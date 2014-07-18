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
package org.jooq.example;

import static java.sql.DriverManager.getConnection;
import static org.jooq.example.db.oracle.Tables.AUTHORS;
import static org.jooq.example.db.oracle.Tables.BOOKS;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.using;

import java.sql.Connection;
import java.util.Properties;

import org.jooq.DSLContext;
import org.jooq.example.db.oracle.packages.Library;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class TransactionTest {

    private static Connection connection;
    private static DSLContext dsl;

    @BeforeClass
    public static void start() throws Exception {
        Properties p = new Properties();
        p.load(TransactionTest.class.getResourceAsStream("/config.properties"));

        connection = getConnection(p.getProperty("db.url"), p.getProperty("db.username"), p.getProperty("db.password"));
        dsl = using(connection);
    }

    @AfterClass
    public static void end() throws Exception {
        connection.close();
    }

    @Before
    public void setup() {
        dsl.transaction(ctx -> {
            using(ctx).delete(BOOKS).execute();
            using(ctx).delete(AUTHORS).execute();

            using(ctx).insertInto(AUTHORS, AUTHORS.ID, AUTHORS.FIRST_NAME, AUTHORS.LAST_NAME)
                      .values(1, "George", "Orwell")
                      .values(2, "Paulo" , "Coelho")
                      .execute();

            using(ctx).insertInto(BOOKS, BOOKS.ID, BOOKS.TITLE, BOOKS.LANGUAGE, BOOKS.AUTHOR_ID)
                      .values(1, "1984"        , "en", 1)
                      .values(2, "Animal Farm" , "en", 1)
                      .values(3, "O Alquimista", "pt", 2)
                      .values(4, "Brida"       , "en", 2)
                      .execute();
        });
    }

    @Test
    public void testProcedures() {
        dsl.selectFrom(table(Library.getAuthors(null, 1)))
           .fetch();

        dsl.select(AUTHORS.FIRST_NAME, AUTHORS.LAST_NAME, Library.getBooks(AUTHORS.ID))
           .from(AUTHORS)
           .fetch()
           .forEach(author -> {

            System.out.println();
            System.out.println("Author " + author.getValue(AUTHORS.FIRST_NAME) + " " + author.getValue(AUTHORS.LAST_NAME) + " wrote: ");
            author.value3().getList().forEach(book -> {
                System.out.println(book.getTitle());
            });
        });


    }
}
