/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

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
