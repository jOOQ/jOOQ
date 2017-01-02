/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.academy.section3;

import static org.jooq.academy.tools.Tools.connection;
import static org.jooq.example.db.h2.Tables.AUTHOR;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jooq.academy.tools.Tools;
import org.jooq.impl.DSL;

import org.junit.Test;

public class Example_3_1_CheckedExceptions {

    @Test
    public void run() throws SQLException {
        Connection connection = connection();

        Tools.title("JDBC throws lots of checked exceptions");

        // These two calls can throw a SQLException
        try (PreparedStatement stmt = connection.prepareStatement("SELECT FIRST_NAME FROM AUTHOR");
             ResultSet rs = stmt.executeQuery()) {

            // This can throw a SQLException
            while (rs.next()) {

                // This can throw a SQLException
                System.out.println(rs.getString(1));
            }
        }

        Tools.title("jOOQ doesn't throw any checked exceptions");
        DSL.using(connection)
           .select(AUTHOR.FIRST_NAME)
           .from(AUTHOR)
           .fetch()
           .forEach(record -> System.out.println(record.get(AUTHOR.FIRST_NAME)));
    }
}
