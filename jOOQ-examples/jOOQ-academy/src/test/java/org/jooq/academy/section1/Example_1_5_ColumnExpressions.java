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
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.val;

import org.jooq.DSLContext;
import org.jooq.academy.tools.Tools;
import org.jooq.impl.DSL;

import org.junit.Test;

public class Example_1_5_ColumnExpressions {

    @Test
    public void run() {
        DSLContext dsl = DSL.using(connection());

        Tools.title("CONCAT() function with prefix notation");
        Tools.print(
            dsl.select(concat(AUTHOR.FIRST_NAME, val(" "), AUTHOR.LAST_NAME))
               .from(AUTHOR)
               .orderBy(AUTHOR.ID)
               .fetch()
        );

        Tools.title("CONCAT() function with infix notation");
        Tools.print(
            dsl.select(AUTHOR.FIRST_NAME.concat(" ").concat(AUTHOR.LAST_NAME))
               .from(AUTHOR)
               .orderBy(AUTHOR.ID)
               .fetch()
        );

    }
}
