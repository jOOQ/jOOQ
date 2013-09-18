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
 * and Maintenance Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.examples;

/* [pro] */

import static org.jooq.impl.DSL.table;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.examples.oracle.sys.packages.DbmsXplan;
import org.jooq.impl.DSL;

public class DBMS_XPLAN {

    public static void main(String[] args) throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "TEST", "TEST");

        DSLContext ora = DSL.using(connection, SQLDialect.ORACLE);
        ora.fetch("select * from t_book b join t_author a on b.author_id = a.id");

        // TODO [#1113] This doesn't work yet
//        System.out.println("Standalone call:");
//        System.out.println("----------------");
//        for (DbmsXplanTypeRecord record : DbmsXplan.displayCursor(ora, null, null, "ALLSTATS LAST").get()) {
//            System.out.println(record.getPlanTableOutput());
//        }

        // [#1114] Unnesting TABLE of OBJECT
        System.out.println("Unnested table:");
        System.out.println("---------------");
        for (String row : ora.select()
                             .from(table(DbmsXplan.displayCursor(null, null, "ALLSTATS LAST")))
                             .fetch(0, String.class)) {

            System.out.println(row);
        }
    }
}

/* [/pro] */