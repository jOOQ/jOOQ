/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
 *
 *
 *
 */
package org.jooq.example;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.jooq.example.db.oracle.sp.Queues.NEW_AUTHOR_AQ;
// ...
// ...
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jooq.example.db.oracle.sp.udt.records.AuthorTRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
// ...
// ...
// ...
// ...

import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class OracleAQExamples extends Utils {

    // Generate 10 authors
    static final List<AuthorTRecord> authors =
        range(0, 10).mapToObj(i -> new AuthorTRecord(i, "F" + i, "L1" + i, null)).collect(toList());

    @Test
    public void testAQSimple() throws Exception {
        dsl.transaction(c -> {

            // Enqueue all authors
            authors.stream().forEach(a -> {
                DBMS_AQ.enqueue(dsl.configuration(), NEW_AUTHOR_AQ, a);
            });

            // Dequeue them again
            authors.stream().forEach(a -> {
                assertEquals(a, DBMS_AQ.dequeue(dsl.configuration(), NEW_AUTHOR_AQ));
            });
        });
    }

    @Test
    public void testAQOptions() throws Exception {
        dsl.transaction(c -> {
            MESSAGE_PROPERTIES_T props = new MESSAGE_PROPERTIES_T();
            ENQUEUE_OPTIONS_T enq = new ENQUEUE_OPTIONS_T().visibility(IMMEDIATE);

            // Enqueue two authors
            DBMS_AQ.enqueue(c, NEW_AUTHOR_AQ, authors.get(0), enq, props);
            DBMS_AQ.enqueue(c, NEW_AUTHOR_AQ, authors.get(1), enq, props);

            // Dequeue them again
            DEQUEUE_OPTIONS_T deq = new DEQUEUE_OPTIONS_T().wait(NO_WAIT);

            assertEquals(authors.get(0), DBMS_AQ.dequeue(c, NEW_AUTHOR_AQ, deq, props));
            assertEquals(authors.get(1), DBMS_AQ.dequeue(c, NEW_AUTHOR_AQ, deq, props));

            // The queue is empty, this should fail
            assertThrows(DataAccessException.class, () -> {
                DBMS_AQ.dequeue(c, NEW_AUTHOR_AQ, deq, props);
            });
        });
    }

    @Test
    public void testAQTransactions() throws Exception {
        dsl.transaction(c1 -> {

            // Enqueue an author
            DBMS_AQ.enqueue(c1, NEW_AUTHOR_AQ, authors.get(0));

            // This nested transaction is rolled back to its savepoint
            assertThrows(RuntimeException.class, () -> {
                DSL.using(c1).transaction(c2 -> {
                    DBMS_AQ.enqueue(c2, NEW_AUTHOR_AQ, authors.get(1));
                    throw new RuntimeException();
                });
            });

            // Dequeue the first author
            MESSAGE_PROPERTIES_T props = new MESSAGE_PROPERTIES_T();
            DEQUEUE_OPTIONS_T deq = new DEQUEUE_OPTIONS_T().wait(NO_WAIT);
            assertEquals(authors.get(0), DBMS_AQ.dequeue(c1, NEW_AUTHOR_AQ, deq, props));

            // The queue is empty (due to the rollback), this should fail
            assertThrows(DataAccessException.class, () -> {
                DBMS_AQ.dequeue(c1, NEW_AUTHOR_AQ, deq, props);
            });
        });
    }
}
