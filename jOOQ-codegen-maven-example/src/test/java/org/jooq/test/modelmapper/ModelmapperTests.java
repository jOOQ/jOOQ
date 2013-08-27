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
package org.jooq.test.modelmapper;

import static org.jooq.maven.example.h2.tables.TAuthor.T_AUTHOR;
import static org.jooq.maven.example.h2.tables.TBook.T_BOOK;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

/**
 * @author Lukas Eder
 */
public class ModelmapperTests {
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
    public void testMapping() throws Exception {

        // This is not yet a working example. Just playing around with the API
        // of ModelMapper
        final ModelMapper mapper = new ModelMapper();
        final TypeMap<Record, Book> map = mapper.createTypeMap(Record.class, Book.class);

        List<Book> books =
        create.select()
              .from(T_BOOK
                  .join(T_AUTHOR)
                  .on(T_BOOK.AUTHOR_ID.eq(T_AUTHOR.ID)))
              .orderBy(T_BOOK.ID)
              .fetch(new RecordMapper<Record, Book>() {

            @Override
            public Book map(Record record) {
                return map.map(record);
            }
        });

        System.out.println(books);
    }

    @Test
    public void testMappingWithRecordMapperProvider() throws Exception {

        // This is not yet a working example. Just playing around with the API
        // of ModelMapper
        final ModelMapper mapper = new ModelMapper();
        final TypeMap<Record, Book> map = mapper.createTypeMap(Record.class, Book.class);

        List<Book> books =
        DSL.using(create.configuration().derive(new RecordMapperProvider() {
            @Override
            public <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> rowType, Class<? extends E> type) {
                if (Book.class.isAssignableFrom(type)) {
                    return new RecordMapper<R, E>() {

                        @SuppressWarnings("unchecked")
                        @Override
                        public E map(R record) {
                            return (E) map.map(record);
                        }
                    };
                }

                throw new RuntimeException();
            }
        }))
        .select()
        .from(T_BOOK
            .join(T_AUTHOR)
            .on(T_BOOK.AUTHOR_ID.eq(T_AUTHOR.ID)))
        .orderBy(T_BOOK.ID)
        .fetchInto(Book.class);

        System.out.println(books);
    }

    public static class Book {
        int id;
        String title;
        Author author;

        @Override
        public String toString() {
            return String.format("Book '%s' written by %s", title, author);
        }
    }

    public static class Author {
        int id;
        String firstName;
        String lastName;

        @Override
        public String toString() {
            return String.format("%s %s", firstName, lastName);
        }
    }
}
