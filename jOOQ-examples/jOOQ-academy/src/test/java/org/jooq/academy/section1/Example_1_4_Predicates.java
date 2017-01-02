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
import static org.jooq.example.db.h2.Tables.BOOK;
import static org.jooq.impl.DSL.select;

import org.jooq.DSLContext;
import org.jooq.academy.tools.Tools;
import org.jooq.impl.DSL;

import org.junit.Test;

public class Example_1_4_Predicates {

    @Test
    public void run() {
        DSLContext dsl = DSL.using(connection());

        Tools.title("Combine predicates using AND");
        Tools.print(
            dsl.select()
               .from(BOOK)
               .where(BOOK.TITLE.like("%a%").and(BOOK.AUTHOR_ID.eq(1)))
               .fetch()
        );

        /*
        Tools.title("Wrong types in comparison predicate");
        Tools.print(
            dsl.select()
               .from(BOOK)
               .where(BOOK.ID.eq("abc"))
               .fetch()
        );
        */

        Tools.title("Use an IN-predicate");
        Tools.print(
            dsl.select()
               .from(AUTHOR)
               .where(AUTHOR.ID.in(select(BOOK.AUTHOR_ID).from(BOOK)))
               .fetch()
        );

        /*
        Tools.title("Wrong type of columns in subquery");
        Tools.print(
            dsl.select()
               .from(AUTHOR)
               .where(AUTHOR.ID.in(select(BOOK.TITLE).from(BOOK)))
               .fetch()
        );
        */

        /*
        Tools.title("Wrong number of columns in subquery");
        Tools.print(
            dsl.select()
               .from(AUTHOR)
               .where(AUTHOR.ID.in(select(BOOK.AUTHOR_ID, BOOK.TITLE).from(BOOK)))
               .fetch()
        );
        */
    }
}
