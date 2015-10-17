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

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.selectZero;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.zero;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.util.List;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

public class AliasTests<
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

    public AliasTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testAliasingSimple() throws Exception {
        Table<B> b = TBook().as("b");
        Field<Integer> b_ID = b.field(TBook_ID());

        List<Integer> ids = create().select(b_ID).from(b).orderBy(b_ID).fetch(b_ID);
        assertEquals(4, ids.size());
        assertEquals(BOOK_IDS, ids);

        Result<Record> books = create().select().from(b).orderBy(b_ID).fetch();
        assertEquals(4, books.size());
        assertEquals(BOOK_IDS, books.getValues(b_ID));
    }

    public void testDerivedColumnListsWithAmbiguousColumnNames() throws Exception {

        // [#3156] If derived column lists are emulated on derived tables that contain
        // ambiguous column names, jOOQ must make sure that this ambiguity doesn't cause any issues
        Result<Record> result =
        create().select()
                .from(
                    table(
                        select(TBook_ID(), TAuthor_ID())
                        .from(TBook())
                        .join(TAuthor()).on(TBook_AUTHOR_ID().eq(TAuthor_ID()))
                    ).as("t", "a", "b"))
                .orderBy(field(name("a")))
                .fetch();

        assertEquals(4, result.size());
        assertEquals(BOOK_IDS, result.getValues("a"));
        assertEquals(BOOK_AUTHOR_IDS, result.getValues("b"));
    }

    @SuppressWarnings("unchecked")

    public void testAliasingTablesAndFields() throws Exception {
        Table<B2S> b2s = TBookToBookStore().as("b2s", "b2s_1", "b2s_2", "b2s_3");
        Field<String> b2s1 = (Field<String>) b2s.field(0);
        Field<Integer> b2s2 = (Field<Integer>) b2s.field(1);
        Field<Integer> b2s3 = (Field<Integer>) b2s.field(2);

        assertEquals("b2s", b2s.getName());
        assertEquals("b2s_1", b2s1.getName());
        assertEquals("b2s_2", b2s2.getName());
        assertEquals("b2s_3", b2s3.getName());

        assertEquals("b2s_1", b2s.field("b2s_1").getName());
        assertEquals("b2s_2", b2s.field("b2s_2").getName());
        assertEquals("b2s_3", b2s.field("b2s_3").getName());

        // TODO: What happens with typed records?
        // B2S record1 = create().selectFrom(b2s).where(b2s3.eq(2)).fetchOne();
        // Record record1 = create().select().from(b2s).where(b2s3.eq(2)).fetchOne();
        Record record1 = create()
            .select(b2s1, b2s2, b2s3)
            .from(b2s)
            .where(b2s3.eq(2))
            .fetchOne();

        assertEquals("Ex Libris", record1.getValue("b2s_1"));
        assertEquals("Ex Libris", record1.getValue(b2s1));
        assertEquals(3, record1.getValue("b2s_2"));
        assertEquals(3, (int) record1.getValue(b2s2));
        assertEquals(2, record1.getValue("b2s_3"));
        assertEquals(2, (int) record1.getValue(b2s3));
    }

    public void testAliasingSelectAndFields() throws Exception {
        Record r1 = create().select().from(table(selectOne()).as("t", "v")).fetchOne();
        assertEquals("v", r1.field(0).getName());
        assertEquals("v", r1.field("v").getName());
        assertEquals(1, r1.getValue(0));
        assertEquals(1, r1.getValue("v"));
        assertEquals(1, r1.getValue(r1.field(0)));

        Record r2 = null;

        switch (dialect().family()) {
            /* [pro] */
            // No CROSS JOINs are supported in MS Access. Also other JOIN types have some restrictions.
            case ACCESS: {
                r2 = create()
                    .select()
                    .from(
                        table(select(one().as("one"))).as("t1", "v1"),
                        table(select(two().as("two"), zero().as("zero"))).as("t2", "v2a", "v2b"),
                        table(select(val((Integer) null).as("one"))).as("t3", "v3"))
                    .fetchOne();
                break;
            }
            /* [/pro] */

            default: {
                r2 = create()
                    .select()
                    .from(table(selectOne()).as("t1", "v1"))
                    .crossJoin(table(select(two(), zero())).as("t2", "v2a", "v2b"))
                    .leftOuterJoin(table(selectOne()).as("t3", "v3"))
                    .on("1 = 0")
                    .fetchOne();

                break;
            }
        }
        assertEquals("v1", r2.field(0).getName());
        assertEquals("v2a", r2.field(1).getName());
        assertEquals("v2b", r2.field(2).getName());
        assertEquals("v3", r2.field(3).getName());
        assertEquals("v1", r2.field("v1").getName());
        assertEquals("v2a", r2.field("v2a").getName());
        assertEquals("v2b", r2.field("v2b").getName());
        assertEquals("v3", r2.field("v3").getName());

        assertEquals(1, r2.getValue(0));
        assertEquals(1, r2.getValue("v1"));
        assertEquals(2, r2.getValue(1));
        assertEquals(2, r2.getValue("v2a"));
        assertEquals(0, r2.getValue(2));
        assertEquals(0, r2.getValue("v2b"));
        assertNull(r2.getValue(3));
        assertNull(r2.getValue("v3"));
    }

    public void testAliasingJoins() throws Exception {
        Record r1 = create()
            .select()
            .from(table(selectOne())
                .crossJoin(table(selectZero())).as("t", "a", "b"))
            .fetchOne();

        assertEquals("a", r1.field(0).getName());
        assertEquals("b", r1.field(1).getName());
        assertEquals("a", r1.field("a").getName());
        assertEquals("b", r1.field("b").getName());
        assertEquals(1, r1.getValue(0));
        assertEquals(0, r1.getValue(1));
        assertEquals(1, r1.getValue("a"));
        assertEquals(0, r1.getValue("b"));

    }

    public void testAliasingDelete() throws Exception {
        switch (dialect().family()) {
            /* [pro] */
            case SQLSERVER:
            /* [/pro] */
            case SQLITE:
                log.info("SKIPPING", "Aliasing DELETE tests");
                return;
        }

        jOOQAbstractTest.reset = false;
        Table<B2S> b = TBookToBookStore().as("b");

        assertEquals(2, create().delete(b).where(b.field(TBookToBookStore_BOOK_ID()).eq(1)).execute());
        assertEquals(4, create().fetchCount(selectFrom(TBookToBookStore())));

        assertEquals(4, create().delete(b).execute());
        assertEquals(0, create().fetchCount(selectFrom(TBookToBookStore())));
    }
}
