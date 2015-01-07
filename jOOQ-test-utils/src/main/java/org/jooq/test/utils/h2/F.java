/**
 * Copyright (c) 2009-2015, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test.utils.h2;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.sign;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.SQLDataType.INTEGER;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * Pre-compiled stored functions for H2
 *
 * @author Lukas Eder
 */
public class F {

    public static void pCreateAuthor(Connection connection) {
        pCreateAuthorByName(connection, "William", "Shakespeare");
    }

    public static void pCreateAuthorByName(Connection connection, String firstName, String lastName) {
        DSLContext create = create(connection);

        create.insertInto(table("t_author"))
              .set(field("id"), create.select(max(field("id")).add(1)).from(table("t_author")).<Integer>asField())
              .set(field("first_name"), firstName)
              .set(field("last_name"), lastName)
              .execute();
    }

    public static ResultSet fGetOneCursor(Connection connection, Object[] bookIds) throws SQLException {
        PreparedStatement stmt = null;

        if (bookIds == null) bookIds = new Object[0];

        String sql = create(connection)
            .select()
            .from(table("t_book"))
            .where(field("id", Integer.class).in(INTEGER.convert(bookIds)))
            .orderBy(field("id").asc())
            .getSQL();

        stmt = connection.prepareStatement(sql);
        int i = 1;
        for (Object o : bookIds) {
            stmt.setObject(i++, o);
        }

        return stmt.executeQuery();
    }

    public static Integer fAuthorExists(Connection connection, String authorName) {
        DSLContext create = create(connection);

        Integer result =
        create.select(sign(count()))
              .from(table("t_author"))
              .where(field("first_name").equal(authorName))
              .or(field("last_name").equal(authorName))
              .fetchOne(0, Integer.class);

        return result;
    }

    public static Object[] f_arrays1(Object[] in_array) {
        return in_array;
    }

    public static Object[] f_arrays2(Object[] in_array) {
        return in_array;
    }

    public static Object[] f_arrays3(Object[] in_array) {
        return in_array;
    }

    @SuppressWarnings("unused")
    public static Integer f317(Integer p1, Integer p2, Integer p3, Integer p4) {
        return 1000 * p1 + 100 * p2 + p4;
    }

    public static String f1256(String string) {
        return string;
    }

    public static Integer fNumber(Integer n) {
        return n;
    }

    public static Integer fOne() {
        return 1;
    }

    private static DSLContext create(Connection connection) {
        return DSL.using(connection, SQLDialect.H2);
    }
}
