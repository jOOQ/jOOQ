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
import static org.jooq.example.db.h2.Tables.BOOK;

import java.sql.Connection;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.academy.tools.Tools;
import org.jooq.conf.Settings;
import org.jooq.example.db.h2.tables.records.BookRecord;
import org.jooq.exception.DataChangedException;
import org.jooq.impl.DSL;

import org.junit.Test;

public class Example_2_2_OptimisticLocking {

    @Test
    public void run() throws SQLException {
        Connection connection = connection();
        DSLContext dsl = DSL.using(connection, new Settings().withExecuteWithOptimisticLocking(true));

        try {
            Tools.title("Applying optimistic locking");

            BookRecord book1 = dsl.selectFrom(BOOK).where(BOOK.ID.eq(1)).fetchOne();
            BookRecord book2 = dsl.selectFrom(BOOK).where(BOOK.ID.eq(1)).fetchOne();

            book1.setTitle("New Title");
            book1.store();

            book2.setTitle("Another Title");
            book2.store();
        }

        catch (DataChangedException expected) {
            expected.printStackTrace();
        }

        // Don't store the changes
        finally {
            connection.rollback();
        }
    }
}
