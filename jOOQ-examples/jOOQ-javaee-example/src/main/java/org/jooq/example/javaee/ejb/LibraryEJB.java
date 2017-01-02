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
package org.jooq.example.javaee.ejb;

import static org.jooq.SQLDialect.H2;
import static org.jooq.example.db.h2.Tables.AUTHOR;
import static org.jooq.example.db.h2.Tables.BOOK;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import org.jooq.Result;
import org.jooq.SortField;
import org.jooq.UpdatableRecord;
import org.jooq.example.db.h2.tables.records.AuthorRecord;
import org.jooq.example.db.h2.tables.records.BookRecord;
import org.jooq.impl.DSL;

/**
 * A session bean that uses the configured {@link DataSource} to interact with
 * the embedded H2 database.
 *
 * @author Lukas Eder
 */
@Stateless
public class LibraryEJB {

    @Resource(lookup="java:jboss/datasources/jooq-javaee-example")
    private DataSource ds;

    public Result<AuthorRecord> fetchAuthors(SortField<?> sort) {
        return DSL.using(ds, H2)
                  .selectFrom(AUTHOR)
                  .orderBy(sort)
                  .fetch();
    }

    public Result<BookRecord> fetchBooks(SortField<?> sort) {
        return DSL.using(ds, H2)
                  .selectFrom(BOOK)
                  .orderBy(sort)
                  .fetch();
    }

    public void store(UpdatableRecord<?> record) {
        DSL.using(ds, H2).attach(record);
        record.store();
    }

    public void delete(UpdatableRecord<?> record) {
        DSL.using(ds, H2).attach(record);
        record.delete();
    }

}