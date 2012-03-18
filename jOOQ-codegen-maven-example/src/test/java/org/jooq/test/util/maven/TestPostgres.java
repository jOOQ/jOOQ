/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test.util.maven;

import static junit.framework.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.util.maven.example.postgres.PublicFactory;
import org.jooq.util.maven.example.postgres.tables.TBook;
import org.jooq.util.maven.example.postgres.tables.records.TBookRecord;

import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class TestPostgres {

    @Test
    public void testFetch() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:postgresql:postgres", "postgres", "test");
        PublicFactory create = new PublicFactory(connection);

        Result<Record> books =
        create.select(TBook.ID, TBook.TITLE)
              .from(TBook.T_BOOK)
              .orderBy(TBook.ID)
              .fetch();

        assertEquals(Data.BOOK_IDS, books.getValues(0));
        assertEquals(Data.BOOK_TITLES, books.getValues(1));
    }

    @Test
    public void testFetchInto() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:postgresql:postgres", "postgres", "test");
        PublicFactory create = new PublicFactory(connection);

        List<TBookRecord> books =
        create.select(TBook.ID, TBook.TITLE)
              .from(TBook.T_BOOK)
              .orderBy(TBook.ID)
              .fetchInto(TBookRecord.class);

        for (int i = 0; i < Data.BOOK_IDS.size(); i++) {
            assertEquals(Data.BOOK_IDS.get(i), books.get(i).getId());
            assertEquals(Data.BOOK_TITLES.get(i), books.get(i).getTitle());
        }
    }
}
