/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
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
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.val;
import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectQuery;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

public class UnionTests<
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

    public UnionTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testUnionAndOrderBy() throws Exception {
        Field<String> x = val("A").as("x");

        // Simple ORDER BY following UNION
        // -------------------------------
        assertEquals(
            asList("A", "B", "C"),
            create().select(x)
                    .union(select(val("B")))
                    .union(select(val("C")))
                    .orderBy(x)
                    .fetch(x)
        );

        assertEquals(
            asList("C", "B", "A"),
            create().select(x)
                    .union(select(val("B")))
                    .union(select(val("C")))
                    .orderBy(x.desc())
                    .fetch(x)
        );

        // ORDER BY with LIMIT
        // -------------------
        assertEquals(
            asList("C", "B"),
            create().select(x)
                    .union(select(val("B")))
                    .union(select(val("C")))
                    .orderBy(x.desc())
                    .limit(2)
                    .fetch(x)
        );

        assertEquals(
            asList("B", "A"),
            create().select(x)
                    .union(select(val("B")))
                    .union(select(val("C")))
                    .orderBy(x.desc())
                    .limit(2)
                    .offset(1)
                    .fetch(x)
        );

        // Different SET operators
        // -----------------------
        assertEquals(
            asList("A", "B", "B"),
            create().select(x)
                    .union(select(val("B")))
                    .unionAll(select(val("B")))
                    .orderBy(x)
                    .fetch(x)
        );

        assertEquals(
            asList("A", "B"),
            create().select(x)
                    .unionAll(select(val("B")))
                    .union(select(val("B")))
                    .orderBy(x)
                    .fetch(x)
        );

        assertEquals(
            asList("A", "B", "B"),
            create().select(x)
                    .unionAll(select(val("B")))
                    .union(select(val("B")))
                    .unionAll(select(val("B")))
                    .orderBy(x)
                    .fetch(x)
        );
    }

    public void testUnionExceptIntersectAndOrderBy() throws Exception {
        assumeFamilyNotIn(FIREBIRD);

        Field<String> x = val("A").as("x");

        // Different SET operators
        // -----------------------
        assertEquals(
            asList("A"),
            create().select(x)
                    .union(select(val("B")))
                    .union(select(val("C")))
                    .intersect(select(val("A")))
                    .orderBy(x)
                    .fetch(x)
        );

        assertEquals(
            asList("A", "B"),
            create().select(x)
                    .union(select(val("B")))
                    .union(select(val("C")))
                    .intersect(select(val("A"))
                        .union(select(val("B"))))
                    .orderBy(x)
                    .fetch(x)
        );

        assertEquals(
            asList("C"),
            create().select(x)
                    .union(select(val("B")))
                    .union(select(val("C")))
                    .except(select(val("A"))
                        .union(select(val("B"))))
                    .orderBy(x)
                    .fetch(x)
        );

        assertEquals(
            asList("B"),
            create().select(x)
                    .union(select(val("B")))
                    .union(select(val("C")))
                    .except(select(val("A"))
                        .union(select(val("D"))))
                    .intersect(select(val("A"))
                        .union(select(val("B"))))
                    .orderBy(x)
                    .fetch(x)
        );

    }

    public void testCombinedSelectQuery() throws Exception {
        SelectQuery<B> q1 = create().selectQuery(TBook());
        SelectQuery<B> q2 = create().selectQuery(TBook());

        q1.addConditions(TBook_AUTHOR_ID().equal(1));
        q2.addConditions(TBook_TITLE().equal("Brida"));

        // Use union all because of clob's
        Select<?> union = q1.unionAll(q2);
        int rows = union.execute();
        assertEquals(3, rows);

        // Use union all because of clob's
        rows = create().selectDistinct(union.field(TBook_AUTHOR_ID()), TAuthor_FIRST_NAME())
            .from(union)
            .join(TAuthor())
            .on(union.field(TBook_AUTHOR_ID()).equal(TAuthor_ID()))
            .orderBy(TAuthor_FIRST_NAME())
            .execute();

        assertEquals(2, rows);
    }

    public void testComplexUnions() throws Exception {
        Select<Record1<String>> s1 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(1));
        Select<Record1<String>> s2 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(2));
        Select<Record1<String>> s3 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(3));
        Select<Record1<String>> s4 = create().select(TBook_TITLE()).from(TBook()).where(TBook_ID().equal(4));

        Result<Record> result = create().select().from(s1.union(s2).union(s3).union(s4)).fetch();
        assertEquals(4, result.size());

        result = create().select().from(s1.union(s2).union(s3.union(s4))).fetch();
        assertEquals(4, result.size());

        assertEquals(4, create().selectFrom(s1.union(
                            create().selectFrom(s2.unionAll(
                                create().selectFrom(s3.union(s4).asTable())
                            ).asTable())
                        ).asTable())
                                    .fetch().size());

        // [#289] Handle bad syntax scenario provided by user Gunther
        Select<Record1<Integer>> q = create().select(val(2008).as("y"));
        for (int year = 2009; year <= 2011; year++) {
            q = q.union(create().select(val(year).as("y")));
        }

        assertEquals(4, q.execute());
    }

    public void testIntersectAndExcept() throws Exception {
        assumeFamilyNotIn(FIREBIRD);

        // [#3507] Not all dialects support INTERSECT and EXCEPT
        Result<Record1<Integer>> r1 =
        create().select(TBook_ID())
                .from(TBook())
                .where(TBook_ID().le(3))
                .intersect(
                 select(TBook_ID())
                .from(TBook())
                .where(TBook_ID().ge(3)))
                .fetch();

        assertEquals(1, r1.size());
        assertEquals(3, (int) r1.get(0).getValue(TBook_ID()));

        Result<Record1<Integer>> r2 =
        create().select(TBook_ID())
                .from(TBook())
                .where(TBook_ID().le(3))
                .except(
                 select(TBook_ID())
                .from(TBook())
                .where(TBook_ID().le(2)))
                .fetch();

        assertEquals(1, r2.size());
        assertEquals(3, (int) r2.get(0).getValue(TBook_ID()));


    }
}
