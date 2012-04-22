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
import static org.jooq.impl.Factory.countDistinct;
import static org.jooq.util.maven.example.ase.Tables.T_AUTHOR;
import static org.jooq.util.maven.example.ase.Tables.T_BOOK;
import static org.jooq.util.maven.example.ase.Tables.T_BOOK_STORE;
import static org.jooq.util.maven.example.ase.Tables.T_BOOK_TO_BOOK_STORE;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.Record;
import org.jooq.Result;
import org.jooq.util.maven.example.ase.DboFactory;
import org.jooq.util.maven.example.ase.tables.TAuthor;
import org.jooq.util.maven.example.ase.tables.TBook;
import org.jooq.util.maven.example.ase.tables.TBookStore;
import org.jooq.util.maven.example.ase.tables.TBookToBookStore;

import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class TestASE {

    @Test
    public void testInstanceModel() throws Exception {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:jtds:sybase://lukas-hp:5000/TEST", "sa", "");
        DboFactory create = new DboFactory(connection);

        TBook b = T_BOOK.as("b");
        TAuthor a = T_AUTHOR.as("a");
        TBookStore s = T_BOOK_STORE.as("s");
        TBookToBookStore t = T_BOOK_TO_BOOK_STORE.as("t");

        Result<Record> result =
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
}
