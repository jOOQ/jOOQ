/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.examples;

import static org.jooq.impl.Factory.table;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.examples.oracle.sys.packages.DbmsXplan;
import org.jooq.util.oracle.OracleFactory;

public class DBMS_XPLAN {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "TEST", "TEST");

        OracleFactory ora = new OracleFactory(connection);
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
