/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.examples;

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
