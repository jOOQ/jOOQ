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
import java.util.Arrays;

import org.jooq.Record;
import org.jooq.academy.tools.Tools;
import org.jooq.impl.DSL;

import org.junit.Test;

public class Example_3_2_ResultSets {

    @Test
    public void run() {
        Connection connection = connection();

        Tools.title("Using jOOQ Results in foreach loops");
        for (Record record : DSL.using(connection)
                                .select()
                                .from(AUTHOR)
                                .fetch()) {
            System.out.println(record);
        }

        Tools.title("Using jOOQ Results with Java 8 streams");
        DSL.using(connection)
           .select()
           .from(AUTHOR)
           .fetch()
           .stream()
           .flatMap(record -> Arrays.stream(record.intoArray()))
           .forEach(System.out::println);
    }
}
