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
package org.jooq.test.util.maven;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.maven.example.h2.tables.TAuthor.T_AUTHOR;
import static org.jooq.maven.example.h2.tables.TBook.T_BOOK;
import static org.jooq.maven.example.h2.tables.TBookStore.T_BOOK_STORE;
import static org.jooq.maven.example.h2.tables.TBookToBookStore.T_BOOK_TO_BOOK_STORE;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.maven.example.h2.tables.TAuthor;
import org.jooq.maven.example.h2.tables.TBook;
import org.jooq.maven.example.h2.tables.TBookStore;
import org.jooq.maven.example.h2.tables.TBookToBookStore;
import org.jooq.maven.example.h2.tables.records.TBookRecord;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class TestH2 {

    private static DSLContext create;
    private static Connection connection;

    @BeforeClass
    public static void start() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:~/maven-test", "sa", "");
        create = DSL.using(connection, SQLDialect.H2);
    }

    @AfterClass
    public static void stop() throws Exception {
        connection.close();
    }

    @Test
    public void testInstanceModel() throws Exception {
        TBook b = T_BOOK.as("b");
        TAuthor a = T_AUTHOR.as("a");
        TBookStore s = T_BOOK_STORE.as("s");
        TBookToBookStore t = T_BOOK_TO_BOOK_STORE.as("t");

        Result<Record3<String, String, Integer>> result =
        create.select(a.FIRST_NAME, a.LAST_NAME, countDistinct(s.NAME))
              .from(a)
              .join(b).on(b.AUTHOR_ID.equal(a.ID))
              .join(t).on(t.BOOK_ID.equal(b.ID))
              .join(s).on(t.BOOK_STORE_NAME.equal(s.NAME))
              .groupBy(a.FIRST_NAME, a.LAST_NAME)
              .orderBy(countDistinct(s.NAME).desc())
              .fetch();

        assertEquals(2, result.size());
        assertEquals("Paulo", result.getValue(0, a.FIRST_NAME));
        assertEquals("George", result.getValue(1, a.FIRST_NAME));

        assertEquals("Coelho", result.getValue(0, a.LAST_NAME));
        assertEquals("Orwell", result.getValue(1, a.LAST_NAME));

        assertEquals(Integer.valueOf(3), result.getValue(0, countDistinct(s.NAME)));
        assertEquals(Integer.valueOf(2), result.getValue(1, countDistinct(s.NAME)));
    }

    @Test
    public void testTypedRecords() throws Exception {
        Result<TBookRecord> result = create.selectFrom(T_BOOK).orderBy(T_BOOK.ID).fetch();

        assertEquals(4, result.size());
        assertEquals(asList(1, 2, 3, 4), result.getValues(0));
    }
}
