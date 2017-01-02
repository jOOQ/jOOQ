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
package org.jooq.example;

import static org.jooq.example.db.oracle.sp.Tables.AUTHORS;
import static org.jooq.example.db.oracle.sp.Tables.BOOKS;
import static org.jooq.impl.DSL.using;

import org.jooq.example.db.oracle.sp.packages.Library;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class OracleProcedureExamples extends Utils {

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
        // TODO: Work on this nice table unnesting syntax, which currently doesn't work.
        // dsl.selectFrom(table(Library.getAuthors(null, 1)))
        //    .fetch();

        dsl.select(AUTHORS.FIRST_NAME, AUTHORS.LAST_NAME, Library.getBooks(AUTHORS.ID))
           .from(AUTHORS)
           .fetch()
           .forEach(author -> {

            System.out.println();
            System.out.println("Author " + author.get(AUTHORS.FIRST_NAME) + " " + author.get(AUTHORS.LAST_NAME) + " wrote: ");
            author.value3().forEach(book -> {
                System.out.println(book.getTitle());
            });
        });
    }
}
