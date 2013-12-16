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
