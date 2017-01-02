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
package org.jooq.example.guice;

import static org.jooq.example.db.h2.Tables.AUTHOR;

import java.util.List;

import javax.sql.DataSource;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.example.db.h2.tables.daos.AuthorDao;
import org.jooq.example.db.h2.tables.daos.BookDao;
import org.jooq.example.db.h2.tables.pojos.Author;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;

/**
 * A fully transactional service class offering various API methods for data
 * manipulation.
 *
 * @author Lukas Eder
 */
@Transactional
public class Service {

    final DataSource ds;
    final Configuration configuration;
    final AuthorDao authors;
    final BookDao books;

    @Inject
    public Service(DataSource ds) {
        this.ds = ds;
        this.configuration = new DefaultConfiguration()
            .set(new SpringConnectionProvider(ds))
            .set(SQLDialect.H2)
            .set(new DefaultExecuteListenerProvider(new ExceptionTranslator(ds)));

        this.authors = new AuthorDao(configuration);
        this.books = new BookDao(configuration);
    }

    public List<Author> getAuthors() {
        return authors.findAll();
    }

    public Author getAuthor(int id) {
        return authors.fetchOneById(id);
    }

    public void deleteAuthor(int id) {
        authors.deleteById(id);
    }

    public int mergeNames(Author author) {
        return
        DSL.using(configuration)
           .mergeInto(AUTHOR, AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
           .key(AUTHOR.ID)
           .values(author.getId(), author.getFirstName(), author.getLastName())
           .execute();
    }

    /**
     * A utility method to allow for nested transactions.
     * <p>
     * All code wrapped by the <code>runnable</code> argument is automatically
     * executed in a transaction.
     */
    public void transactional(Runnable runnable) {
        runnable.run();
    }
}
