/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.test.all.testcases;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.update;

import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

/**
 * This test suite checks jOOQ's Java 8 interaction readiness
 *
 * @author Lukas Eder
 */
public class StreamsTest<
    A    extends UpdatableRecord<A> & Record6<Integer, String, String, Date, Integer, ?>,
    AP,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S> & Record1<String>,
    B2S  extends UpdatableRecord<B2S> & Record3<String, Integer, Integer>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L> & Record2<String, String>,
    X    extends TableRecord<X>,
    DATE extends UpdatableRecord<DATE>,
    BOOL extends UpdatableRecord<BOOL>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    UU   extends UpdatableRecord<UU>,
    CS   extends UpdatableRecord<CS>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> {

    public StreamsTest(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testStreamsCollectRecords() {

        // Collect the AUTHOR / BOOK relationship grouping books by author
        // The result is collected into jOOQ Records

        Map<Record2<String, String>, List<Record2<Integer, String>>> booksByAuthor =
        create().select(
                    TBook_ID(),
                    TBook_TITLE(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME())
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                .orderBy(TBook_ID())
                .fetch()
                .stream()
                .collect(groupingBy(
                    r -> r.into(TAuthor_FIRST_NAME(), TAuthor_LAST_NAME()),
                    LinkedHashMap::new,
                    mapping(
                        r -> r.into(TBook_ID(), TBook_TITLE()),
                        toList()
                    )
                ));

        assertEquals(2, booksByAuthor.size());
        List<Entry<Record2<String, String>, List<Record2<Integer, String>>>> entries = booksByAuthor.entrySet().stream().collect(toList());

        assertEquals(AUTHOR_FIRST_NAMES.get(0), entries.get(0).getKey().getValue(TAuthor_FIRST_NAME()));
        assertEquals(AUTHOR_FIRST_NAMES.get(1), entries.get(1).getKey().getValue(TAuthor_FIRST_NAME()));
        assertEquals(AUTHOR_LAST_NAMES.get(0), entries.get(0).getKey().getValue(TAuthor_LAST_NAME()));
        assertEquals(AUTHOR_LAST_NAMES.get(1), entries.get(1).getKey().getValue(TAuthor_LAST_NAME()));

        assertEquals(2, entries.get(0).getValue().size());
        assertEquals(2, entries.get(1).getValue().size());

        assertEquals(BOOK_IDS.get(0), entries.get(0).getValue().get(0).getValue(TBook_ID()));
        assertEquals(BOOK_IDS.get(1), entries.get(0).getValue().get(1).getValue(TBook_ID()));
        assertEquals(BOOK_IDS.get(2), entries.get(1).getValue().get(0).getValue(TBook_ID()));
        assertEquals(BOOK_IDS.get(3), entries.get(1).getValue().get(1).getValue(TBook_ID()));

        assertEquals(BOOK_TITLES.get(0), entries.get(0).getValue().get(0).getValue(TBook_TITLE()));
        assertEquals(BOOK_TITLES.get(1), entries.get(0).getValue().get(1).getValue(TBook_TITLE()));
        assertEquals(BOOK_TITLES.get(2), entries.get(1).getValue().get(0).getValue(TBook_TITLE()));
        assertEquals(BOOK_TITLES.get(3), entries.get(1).getValue().get(1).getValue(TBook_TITLE()));
    }

    public void testStreamsCollectPOJOs() {

        // Collect the AUTHOR / BOOK relationship grouping books by author
        // The result is collected into POJOs

        Map<Author, List<Book>> booksByAuthor =
        create().select(
                    TBook_ID().as("book_id"),
                    TBook_TITLE(),
                    TAuthor_FIRST_NAME(),
                    TAuthor_LAST_NAME())
                .from(TBook())
                .join(TAuthor()).on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                .orderBy(TBook_ID())
                .fetch()
                .stream()
                .collect(groupingBy(
                    r -> r.into(Author.class),
                    LinkedHashMap::new,
                    mapping(
                        r -> r.into(Book.class),
                        toList()
                    )
                ));

        assertEquals(2, booksByAuthor.size());
        List<Entry<Author, List<Book>>> entries = booksByAuthor.entrySet().stream().collect(toList());

        assertEquals(AUTHOR_FIRST_NAMES.get(0), entries.get(0).getKey().firstName);
        assertEquals(AUTHOR_FIRST_NAMES.get(1), entries.get(1).getKey().firstName);
        assertEquals(AUTHOR_LAST_NAMES.get(0), entries.get(0).getKey().lastName);
        assertEquals(AUTHOR_LAST_NAMES.get(1), entries.get(1).getKey().lastName);

        assertEquals(2, entries.get(0).getValue().size());
        assertEquals(2, entries.get(1).getValue().size());

        assertEquals((int) BOOK_IDS.get(0), entries.get(0).getValue().get(0).bookId);
        assertEquals((int) BOOK_IDS.get(1), entries.get(0).getValue().get(1).bookId);
        assertEquals((int) BOOK_IDS.get(2), entries.get(1).getValue().get(0).bookId);
        assertEquals((int) BOOK_IDS.get(3), entries.get(1).getValue().get(1).bookId);

        assertEquals(BOOK_TITLES.get(0), entries.get(0).getValue().get(0).title);
        assertEquals(BOOK_TITLES.get(1), entries.get(0).getValue().get(1).title);
        assertEquals(BOOK_TITLES.get(2), entries.get(1).getValue().get(0).title);
        assertEquals(BOOK_TITLES.get(3), entries.get(1).getValue().get(1).title);
    }

    static class Book {
        public int bookId;
        public String title;

        @Override
        public String toString() {
            return "Book [bookId=" + bookId + ", title=" + title + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + bookId;
            result = prime * result + ((title == null) ? 0 : title.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Book other = (Book) obj;
            if (bookId != other.bookId)
                return false;
            if (title == null) {
                if (other.title != null)
                    return false;
            }
            else if (!title.equals(other.title))
                return false;
            return true;
        }
    }

    static class Author {
        public String firstName;
        public String lastName;

        @Override
        public String toString() {
            return "Author [firstName=" + firstName + ", lastName=" + lastName + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
            result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Author other = (Author) obj;
            if (firstName == null) {
                if (other.firstName != null)
                    return false;
            }
            else if (!firstName.equals(other.firstName))
                return false;
            if (lastName == null) {
                if (other.lastName != null)
                    return false;
            }
            else if (!lastName.equals(other.lastName))
                return false;
            return true;
        }
    }

    public void testStreamsReduceResultsIntoBatch() {
        jOOQAbstractTest.reset = false;

        int[] result =
        create().selectFrom(TBook())
                .where(TBook_ID().in(2, 3))
                .orderBy(TBook_ID())
                .fetch()
                .stream()
                .map(book -> { book.setValue(TBook_TITLE(), book.getValue(TBook_TITLE()).toUpperCase()); return book; })
                .reduce(
                    create().batch(update(TBook()).set(TBook_TITLE(), (String) null).where(TBook_ID().eq((Integer) null))),
                    (batch, book) -> batch.bind(book.getValue(TBook_TITLE()), book.getValue(TBook_ID())),
                    (b1, b2) -> b1
                )
                .execute();

        assertEquals(2, result.length);
        assertEquals(
            asList(
                BOOK_TITLES.get(0),
                BOOK_TITLES.get(1).toUpperCase(),
                BOOK_TITLES.get(2).toUpperCase(),
                BOOK_TITLES.get(3)),
            create().fetchValues(select(TBook_TITLE()).from(TBook()).orderBy(TBook_ID()))
        );
    }
}
