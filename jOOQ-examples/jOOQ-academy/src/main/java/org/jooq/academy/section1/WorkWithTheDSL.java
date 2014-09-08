/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.academy.section1;

import static org.jooq.example.db.h2.Tables.AUTHOR;
import static org.jooq.impl.DSL.select;

import org.jooq.academy.tools.Tools;
import org.jooq.impl.DSL;

public class WorkWithTheDSL {

    public static void main(String[] args) {
        example1_createAndPrintASimpleQuery();
        example2_executeASimpleQuery();
    }

    static void example1_createAndPrintASimpleQuery() {

        // This creates a simple query without executing it
        // By default, a Query's toString() method will print the SQL string to the console
        Tools.run("Create a simple query without executing it", () ->
             select(AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
            .from(AUTHOR)
            .orderBy(AUTHOR.ID)
        );
    }

    static void example2_executeASimpleQuery() {

        // All we need to
        Tools.run("Selecting FIRST_NAME and LAST_NAME from the AUTHOR table", () ->
            DSL.using(Tools.connection())
               .select(AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
               .from(AUTHOR)
               .orderBy(AUTHOR.ID)
               .fetch()
        );
    }

}
