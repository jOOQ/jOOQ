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
package org.jooq.example.guice.test;

import static com.google.inject.Guice.createInjector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jooq.example.db.h2.tables.pojos.Author;
import org.jooq.example.guice.Module;
import org.jooq.example.guice.Service;

import org.junit.Test;
import org.springframework.dao.DataAccessException;

/**
 * @author Lukas Eder
 */
public class Tests {

    Service service = createInjector(new Module()).getInstance(Service.class);

    @Test
    public void testSize() {
        assertEquals(2, service.getAuthors().size());
    }

    @Test
    public void testName() {
        Author author = service.getAuthor(1);
        assertEquals("George", author.getFirstName());
        assertEquals("Orwell", author.getLastName());
    }

    @Test
    public void testTransaction() {
        try {
            service.transactional(new Runnable() {
                @Override
                public void run() {

                    // This should work normally
                    Author author = service.getAuthor(1);
                    author.setFirstName("John");
                    author.setLastName("Smith");
                    assertEquals(1, service.mergeNames(author));

                    author = service.getAuthor(1);
                    assertEquals("John", author.getFirstName());
                    assertEquals("Smith", author.getLastName());

                    // But this shouldn't work. Authors have books, and there is no cascade delete
                    service.deleteAuthor(1);
                    fail();
                }
            });

            fail();
        }
        catch (DataAccessException expected) {}

        // The database should be at its original state
        testSize();
        testName();
    }
}
