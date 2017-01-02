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
package org.jooq.academy.section1;

import static org.jooq.academy.tools.Tools.connection;
import static org.jooq.example.db.h2.Tables.AUTHOR;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.academy.tools.Tools;
import org.jooq.impl.DSL;

import org.junit.Test;

public class Example_1_3_DMLStatements {

    @Test
    public void run() throws SQLException {
        Connection connection = connection();

        DSLContext dsl = DSL.using(connection);

        try {

            // Inserting is just as easy as selecting
            Tools.title("Inserting a new AUTHOR");
            Tools.print(
                dsl.insertInto(AUTHOR, AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
                   .values(3, "Alfred", "Hitchcock")
                   .execute()
            );

            // But the Java compiler will actively check your statements. The
            // following statements will not compile:
            /*
            Tools.title("Not enough arguments to the values() method!");
            Tools.print(
                DSL.using(connection())
                   .insertInto(AUTHOR, AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
                   .values(4, "Alfred")
                   .execute()
            );
            */
            /*
            Tools.title("Wrong order of types of arguments to the values() method!");
            Tools.print(
                DSL.using(connection())
                   .insertInto(AUTHOR, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME, AUTHOR.ID)
                   .values(4, "Alfred", "Hitchcock")
                   .execute()
            );
            */
            Tools.title("Check if our latest record was really created");
            Tools.print(
                dsl.select()
                   .from(AUTHOR)
                   .where(AUTHOR.ID.eq(3))
                   .fetch()
            );

            Tools.title("Update the DATE_OF_BIRTH column");
            Tools.print(
                dsl.update(AUTHOR)
                   .set(AUTHOR.DATE_OF_BIRTH, Date.valueOf("1899-08-13"))
                   .where(AUTHOR.ID.eq(3))
                   .execute()
            );

            Tools.title("Check if our latest record was really updated");
            Tools.print(
                dsl.select()
                   .from(AUTHOR)
                   .where(AUTHOR.ID.eq(3))
                   .fetch()
            );

            Tools.title("Delete the new record again");
            Tools.print(
                dsl.delete(AUTHOR)
                   .where(AUTHOR.ID.eq(3))
                   .execute()
            );

            Tools.title("Check if the record was really deleted");
            Tools.print(
                dsl.select()
                   .from(AUTHOR)
                   .fetch()
            );
        }

        // Don't keep the new data
        finally {
            connection.rollback();
        }
    }
}
