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
package org.jooq.example;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.jooq.example.db.oracle.sp.Queues.NEW_AUTHOR_AQ;
// ...
// ...
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletionException;

import org.jooq.example.db.oracle.sp.udt.records.AuthorTRecord;
import org.jooq.example.db.oracle.sp.udt.records.BookTRecord;
import org.jooq.example.db.oracle.sp.udt.records.BooksTRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
// ...
// ...
// ...
// ...

import org.junit.Before;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class OracleAQExamples extends Utils {

    // Generate 10 authors
    static final List<AuthorTRecord> authors =
        range(0, 10).mapToObj(i -> new AuthorTRecord(i, "F" + i, "L1" + i,
                        i == 0 ? null : range(1, i).mapToObj(j -> new BookTRecord(j, "T" + j, "DE")).collect(
                            () -> new BooksTRecord(),
                            (l, b) -> l.add(b),
                            (l1, l2) -> l1.addAll(l2)
                        )))
                    .collect(toList());

    @Before
    public void setup() throws Exception {
        try {

            // Dequeue them again
            DEQUEUE_OPTIONS_T deq = new DEQUEUE_OPTIONS_T().wait(NO_WAIT);

            // Empty the queue in case failing tests leave messages in the queue
            for (int i = 0; i < 99; i++)
                DBMS_AQ.dequeue(dsl.configuration(), NEW_AUTHOR_AQ, deq);
        }
        catch (Exception ignore) {}
    }

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
    public void testAQStream() throws Exception {
        dsl.transaction(c -> {

            // Enqueue all authors
            authors.stream().forEach(a -> {
                DBMS_AQ.enqueue(dsl.configuration(), NEW_AUTHOR_AQ, a);
            });

            // Dequeue some of them again
            List<AuthorTRecord> l1 =
            DBMS_AQ.dequeueStream(dsl.configuration(), NEW_AUTHOR_AQ)
                   .limit(2)
                   .collect(toList());

            assertEquals(authors.subList(0, 2), l1);


            List<AuthorTRecord> l2 =
            DBMS_AQ.dequeueStream(dsl.configuration(), NEW_AUTHOR_AQ)
                   .limit(authors.size() - 2)
                   .collect(toList());

            assertEquals(authors.subList(2, authors.size()), l2);
        });
    }

    @Test
    public void testAQIterable() throws Exception {
        dsl.transaction(c -> {

            // Enqueue all authors
            authors.stream().forEach(a -> {
                DBMS_AQ.enqueue(dsl.configuration(), NEW_AUTHOR_AQ, a);
            });

            // Dequeue them again
            int i = 0;
            for (AuthorTRecord author : DBMS_AQ.dequeueIterable(dsl.configuration(), NEW_AUTHOR_AQ)) {
                assertEquals(authors.get(i++), author);

                if (i == authors.size())
                    break;
            }
        });
    }

    @Test
    public void testAQAsync() throws Exception {
        dsl.transaction(c -> {

            // Enqueue all authors
            authors.stream().forEach(a -> {
                DBMS_AQ.enqueue(dsl.configuration(), NEW_AUTHOR_AQ, a);
            });

            DEQUEUE_OPTIONS_T options = new DEQUEUE_OPTIONS_T().wait(NO_WAIT);

            // Dequeue some of them again
            assertNull(
            DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ)
                   .thenCompose(a -> {
                       assertEquals(authors.get(0), a);
                       return DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ, options);
                   })
                   .thenCompose(a -> {
                       assertEquals(authors.get(1), a);
                       return DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ, options);
                   })
                   .thenCompose(a -> {
                       assertEquals(authors.get(2), a);
                       return DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ, options);
                   })
                   .thenCompose(a -> {
                       assertEquals(authors.get(3), a);
                       return DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ, options);
                   })
                   .thenCompose(a -> {
                       assertEquals(authors.get(4), a);
                       return DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ, options);
                   })
                   .thenCompose(a -> {
                       assertEquals(authors.get(5), a);
                       return DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ, options);
                   })
                   .thenCompose(a -> {
                       assertEquals(authors.get(6), a);
                       return DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ, options);
                   })
                   .thenCompose(a -> {
                       assertEquals(authors.get(7), a);
                       return DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ, options);
                   })
                   .thenCompose(a -> {
                       assertEquals(authors.get(8), a);
                       return DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ, options);
                   })
                   .thenCompose(a -> {
                       assertEquals(authors.get(9), a);
                       return DBMS_AQ.dequeueAsync(dsl.configuration(), NEW_AUTHOR_AQ, options);
                   })
                   .toCompletableFuture()
                   .exceptionally(t -> {
                       assertTrue(t instanceof CompletionException);
                       assertTrue(t.getCause() instanceof DataAccessException);
                       assertTrue(t.getCause().getCause() instanceof SQLException);
                       assertTrue(t.getCause().getCause().getMessage().contains("ORA-25228"));
                       return null;
                   })
                   .join());
        });
    }

    @Test
    public void testAQWait() throws Exception {
        dsl.transaction(c -> {
            long time = System.nanoTime();

            try {
                DBMS_AQ.dequeue(c, NEW_AUTHOR_AQ, new DEQUEUE_OPTIONS_T().wait(2));
                fail();
            }
            catch (DataAccessException expected) {}

            // "close enough"
            assertTrue(System.nanoTime() - time > 1_900_000_000L);
        });
    }

    @Test
    public void testAQOptions() throws Exception {
        dsl.transaction(c -> {
            MESSAGE_PROPERTIES_T props = new MESSAGE_PROPERTIES_T();
            ENQUEUE_OPTIONS_T enq = new ENQUEUE_OPTIONS_T().visibility(IMMEDIATE);

            // Enqueue two authors
            DBMS_AQ.enqueue(c, NEW_AUTHOR_AQ, authors.get(0), enq,
                props.correlation("A")
                     .delay(BigDecimal.ZERO)
            );
            DBMS_AQ.enqueue(c, NEW_AUTHOR_AQ, authors.get(1), enq,
                props.correlation("B")
                     .delay(BigDecimal.ZERO)
            );

            // Dequeue them again
            DEQUEUE_OPTIONS_T deq = new DEQUEUE_OPTIONS_T();

            assertEquals(authors.get(0), DBMS_AQ.dequeue(c, NEW_AUTHOR_AQ, deq.wait(NO_WAIT), props));
            assertEquals(0, (int) props.attempts);
            assertEquals("A", props.correlation);
            assertEquals(BigDecimal.ZERO, props.delay);

            assertEquals(authors.get(1), DBMS_AQ.dequeue(c, NEW_AUTHOR_AQ, deq.wait(NO_WAIT), props));
            assertEquals(0, (int) props.attempts);
            assertEquals("B", props.correlation);
            assertEquals(BigDecimal.ZERO, props.delay);

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
