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
package org.jooq.academy.section2;

import static org.jooq.academy.tools.Tools.connection;
import static org.jooq.example.db.h2.Tables.AUTHOR;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.academy.tools.Tools;
import org.jooq.example.db.h2.tables.records.AuthorRecord;
import org.jooq.impl.DSL;

import org.junit.Test;

public class Example_2_1_ActiveRecords {

    @Test
    public void run() throws SQLException {
        Connection connection = connection();
        DSLContext dsl = DSL.using(connection);

        AuthorRecord author;

        try {
            Tools.title("Loading and changing active records");
            author = dsl.selectFrom(AUTHOR).where(AUTHOR.ID.eq(1)).fetchOne();
            author.setDateOfBirth(Date.valueOf("2000-01-01"));
            author.store();
            Tools.print(author);


            Tools.title("Creating a new active record");
            author = dsl.newRecord(AUTHOR);
            author.setId(3);
            author.setFirstName("Alfred");
            author.setLastName("Hitchcock");
            author.store();
            Tools.print(author);


            Tools.title("Refreshing an active record");
            author = dsl.newRecord(AUTHOR);
            author.setId(3);
            author.refresh();
            Tools.print(author);


            Tools.title("Updating an active record");
            author.setDateOfBirth(Date.valueOf("1899-08-13"));
            author.store();
            Tools.print(author);


            Tools.title("Deleting an active record");
            author.delete();
            Tools.print(dsl.selectFrom(AUTHOR).fetch());

        }

        // Don't store the changes
        finally {
            connection.rollback();
        }
    }
}
