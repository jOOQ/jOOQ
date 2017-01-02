/*
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
package org.jooq.academy.section3;

import static org.jooq.academy.tools.Tools.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jooq.Query;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.academy.tools.Tools;
import org.jooq.impl.DSL;

import org.junit.Test;

public class Example_3_4_StatementsAndResults {

    @Test
    public void run() throws SQLException {
        Connection connection = connection();

        Tools.title("If you don't know whether a result set is produced with JDBC");
        try (PreparedStatement stmt = connection.prepareStatement("SELECT FIRST_NAME FROM AUTHOR")) {
            boolean moreResults = stmt.execute();

            do {
                if (moreResults) {
                    try (ResultSet rs = stmt.getResultSet()) {
                        while (rs.next()) {
                            System.out.println(rs.getString(1));
                        }
                    }
                }
                else {
                    System.out.println(stmt.getUpdateCount());
                }
            } while ((moreResults = stmt.getMoreResults()) || stmt.getUpdateCount() != -1);
        }

        Tools.title("You always know whether a result set is produced with jOOQ, because of the type");
        Query q1 = DSL.using(connection).query("UPDATE AUTHOR SET LAST_NAME = LAST_NAME");
        System.out.println(
            q1.execute()
        );

        ResultQuery<Record> q2 = DSL.using(connection).resultQuery("SELECT * FROM AUTHOR");
        System.out.println(
            q2.fetch()
        );
    }
}
