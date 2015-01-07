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
import static org.jooq.impl.DSL.count;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

public class RecordMapperTests<
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
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>,
    CASE extends UpdatableRecord<CASE>>
extends BaseTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> {

    public RecordMapperTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testFetchIntoRecordHandler() throws Exception {

        // Test a simple query with typed records
        // --------------------------------------
        final Queue<Integer> ids = new LinkedList<Integer>();
        final Queue<String> titles = new LinkedList<String>();

        ids.addAll(BOOK_IDS);
        titles.addAll(BOOK_TITLES);

        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetchInto(record -> {
                    assertEquals(ids.poll(), record.getValue(TBook_ID()));
                    assertEquals(titles.poll(), record.getValue(TBook_TITLE()));
                });

        assertTrue(ids.isEmpty());
        assertTrue(titles.isEmpty());

        // Test lazy fetching
        // --------------------------------------
        ids.addAll(BOOK_IDS);
        titles.addAll(BOOK_TITLES);

        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetchLazy()
                .fetchInto(record -> {
                    assertEquals(ids.poll(), record.getValue(TBook_ID()));
                    assertEquals(titles.poll(), record.getValue(TBook_TITLE()));
                });

        assertTrue(ids.isEmpty());
        assertTrue(titles.isEmpty());

        // Test a generic query with any records
        // -------------------------------------
        final Queue<Integer> authorIDs = new LinkedList<Integer>();
        final Queue<Integer> count = new LinkedList<Integer>();

        authorIDs.addAll(Arrays.asList(1, 2));
        count.addAll(Arrays.asList(2, 2));

        create().select(TBook_AUTHOR_ID(), count())
                .from(TBook())
                .groupBy(TBook_AUTHOR_ID())
                .orderBy(TBook_AUTHOR_ID())
                .fetchInto(record -> {
                    assertEquals(authorIDs.poll(), record.getValue(TBook_AUTHOR_ID()));
                    assertEquals(count.poll(), record.getValue(count()));
                });
    }

    public void testFetchIntoRecordMapper() throws Exception {
        assertEquals(BOOK_IDS,
        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetch(record -> record.getValue(TBook_ID())));

        assertEquals(BOOK_TITLES,
        create().selectFrom(TBook())
                .orderBy(TBook_ID())
                .fetch(record -> record.getValue(TBook_TITLE())));
    }

    @SuppressWarnings("serial")
    static class NoRecordMapperAvailableException extends RuntimeException {}

    @SuppressWarnings("unchecked")
    public void testFetchIntoWithRecordMapperProvider() throws Exception {
        DSLContext create = create(create().configuration().derive(
            new RecordMapperProvider() {
                @Override
                public <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> rowType, Class<? extends E> type) {
                    if (type == Integer.class) {
                        return record -> (E) record.getValue(TBook_ID());
                    }

                    if (type == String.class && rowType.field(TBook_TITLE()) != null) {
                        return record -> (E) record.getValue(TBook_TITLE());
                    }

                    throw new NoRecordMapperAvailableException();
                }
            }
        ));

        assertEquals(BOOK_IDS,
        create.selectFrom(TBook())
              .orderBy(TBook_ID())
              .fetchInto(Integer.class));

        assertEquals(BOOK_TITLES,
        create.selectFrom(TBook())
              .orderBy(TBook_ID())
              .fetchInto(String.class));

        try {
            create.selectFrom(TBook())
                  .fetchInto(Object.class);
            fail();
        }
        catch (NoRecordMapperAvailableException expected) {}

        try {
            create.select(TBook_ID())
                  .from(TBook())
                  .fetchInto(String.class);
            fail();
        }
        catch (NoRecordMapperAvailableException expected) {}
    }

    public void testFetchGroupsMapper() throws Exception {
        RecordMapper<Record, String> bookIdMapper = record -> record.getValue(TBook_ID(), String.class);
        RecordMapper<Record, String> authorIdMapper = record -> record.getValue(TBook_AUTHOR_ID(), String.class);

        Map<Integer, List<String>> groups1 =
        create().select(TBook_AUTHOR_ID(), TBook_ID())
                .from(TBook())
                .orderBy(TBook_AUTHOR_ID(), TBook_ID())
                .fetchGroups(TBook_AUTHOR_ID(), bookIdMapper);

        assertEquals(asList(1, 2), new ArrayList<Integer>(groups1.keySet()));
        assertEquals(asList("1", "2"), groups1.get(1));
        assertEquals(asList("3", "4"), groups1.get(2));

        Map<Record, List<String>> groups2 =
        create().select(TBook_AUTHOR_ID(), TBook_ID())
                .from(TBook())
                .orderBy(TBook_AUTHOR_ID(), TBook_ID())
                .fetchGroups(new Field[] {
                    TBook_ID(),
                    TBook_AUTHOR_ID()
                }, bookIdMapper);

        assertEquals(4, groups2.size());
        assertEquals(1, (int) new ArrayList<Record>(groups2.keySet()).get(0).getValue(TBook_ID()));
        assertEquals(2, (int) new ArrayList<Record>(groups2.keySet()).get(1).getValue(TBook_ID()));
        assertEquals(3, (int) new ArrayList<Record>(groups2.keySet()).get(2).getValue(TBook_ID()));
        assertEquals(4, (int) new ArrayList<Record>(groups2.keySet()).get(3).getValue(TBook_ID()));
        assertEquals(1, (int) new ArrayList<Record>(groups2.keySet()).get(0).getValue(TBook_AUTHOR_ID()));
        assertEquals(1, (int) new ArrayList<Record>(groups2.keySet()).get(1).getValue(TBook_AUTHOR_ID()));
        assertEquals(2, (int) new ArrayList<Record>(groups2.keySet()).get(2).getValue(TBook_AUTHOR_ID()));
        assertEquals(2, (int) new ArrayList<Record>(groups2.keySet()).get(3).getValue(TBook_AUTHOR_ID()));
        assertEquals("1", new ArrayList<List<String>>(groups2.values()).get(0).get(0));
        assertEquals("2", new ArrayList<List<String>>(groups2.values()).get(1).get(0));
        assertEquals("3", new ArrayList<List<String>>(groups2.values()).get(2).get(0));
        assertEquals("4", new ArrayList<List<String>>(groups2.values()).get(3).get(0));
        assertEquals(1, new ArrayList<List<String>>(groups2.values()).get(0).size());
        assertEquals(1, new ArrayList<List<String>>(groups2.values()).get(1).size());
        assertEquals(1, new ArrayList<List<String>>(groups2.values()).get(2).size());
        assertEquals(1, new ArrayList<List<String>>(groups2.values()).get(3).size());


        Map<Integer, String> maps1 =
        create().select(TBook_AUTHOR_ID(), TBook_ID())
                .from(TBook())
                .orderBy(TBook_AUTHOR_ID(), TBook_ID())
                .fetchMap(TBook_ID(), authorIdMapper);

        assertEquals(asList(1, 2, 3, 4), new ArrayList<Integer>(maps1.keySet()));
        assertEquals(asList("1", "1", "2", "2"), new ArrayList<String>(maps1.values()));

        Map<List<?>, String> maps2 =
        create().select(TBook_AUTHOR_ID(), TBook_ID())
                .from(TBook())
                .orderBy(TBook_AUTHOR_ID(), TBook_ID())
                .fetchMap(new Field[] {
                    TBook_ID(),
                    TBook_AUTHOR_ID()
                }, bookIdMapper);

        assertEquals(4, maps2.size());
        assertEquals(1, new ArrayList<List<?>>(maps2.keySet()).get(0).get(0));
        assertEquals(2, new ArrayList<List<?>>(maps2.keySet()).get(1).get(0));
        assertEquals(3, new ArrayList<List<?>>(maps2.keySet()).get(2).get(0));
        assertEquals(4, new ArrayList<List<?>>(maps2.keySet()).get(3).get(0));
        assertEquals(1, new ArrayList<List<?>>(maps2.keySet()).get(0).get(1));
        assertEquals(1, new ArrayList<List<?>>(maps2.keySet()).get(1).get(1));
        assertEquals(2, new ArrayList<List<?>>(maps2.keySet()).get(2).get(1));
        assertEquals(2, new ArrayList<List<?>>(maps2.keySet()).get(3).get(1));
        assertEquals("1", new ArrayList<String>(maps2.values()).get(0));
        assertEquals("2", new ArrayList<String>(maps2.values()).get(1));
        assertEquals("3", new ArrayList<String>(maps2.values()).get(2));
        assertEquals("4", new ArrayList<String>(maps2.values()).get(3));
    }
}
