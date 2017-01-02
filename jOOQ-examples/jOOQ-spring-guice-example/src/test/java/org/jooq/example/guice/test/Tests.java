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
