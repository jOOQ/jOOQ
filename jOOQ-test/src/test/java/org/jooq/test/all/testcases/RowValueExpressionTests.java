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
import static org.jooq.SQLDialect.HANA;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.REDSHIFT;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.currentDate;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.zero;

import java.sql.Date;
import java.util.Collections;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Result;
import org.jooq.Row1;
import org.jooq.Row2;
import org.jooq.Row3;
import org.jooq.Row4;
import org.jooq.Row5;
import org.jooq.Row6;
import org.jooq.Row7;
import org.jooq.Row8;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;
import org.jooq.types.DayToSecond;

public class RowValueExpressionTests<
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

    public RowValueExpressionTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    @SuppressWarnings("unchecked")

    public void testRowValueExpressionConditions() throws Exception {

        // Simple equality tests
        assertEquals(1, (int)
        create().selectOne()
                .where(row(1, 2, 3).equal(1, 2, 3))
                .and(row(1, 2, 3).ne(3, 2, 1))
                .fetchOne(0, Integer.class));

        // Equality with various types
        assertEquals(1, (int)
        create().selectOne()
                .where(row(1, "a", Date.valueOf("2013-01-01")).eq(1, "a", Date.valueOf("2013-01-01")))
                .fetchOne(0, Integer.class));

        // IN-condition tests
        assertEquals(1, (int)
        create().selectOne()
                .where(row(1, 2, 3).in(row(3, 3, 3), row(2, 3, 1), row(1, 2, 3)))
                .and(row(1, 2, 3).notIn(row(3, 3, 3), row(2, 3, 1)))
                .fetchOne(0, Integer.class));

        // Tuples with actual data
        assertEquals(asList(1, 2),
        create().select(TBook_ID())
                .from(TBook())
                .where(row(TBook_ID()).equal(1))
                .or(row(TBook_ID(), inline(2)).equal(2, 2))
                .or(row(TBook_ID(), inline(2), val(3)).equal(1, 2, 3))
                .or(row("1", "2", "3", "4").equal(TBook_TITLE(), TBook_TITLE(), TBook_TITLE(), TBook_TITLE()))
                .or(row(TBook_ID(), TBook_ID(), TBook_ID(), TBook_ID(), TBook_ID()).notEqual(row(TBook_ID(), TBook_ID(), TBook_ID(), TBook_ID(), TBook_ID())))
                .or(row(1, 2, "3", 4, 5, 6).eq(1, 2, "3", 4, 5, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7).eq(1, 2, "3", 4, 5, 6, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8).eq(1, 2, "3", 4, 5, 6, 7, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9).eq(1, 2, "3", 4, 5, 6, 7, 8, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 0))
                .or(row(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23).eq(1, 2, "3", 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 0))
                .orderBy(TBook_ID())
                .fetch(TBook_ID()));
    }

    public void testRowValueExpressionBetweenConditions() throws Exception {
        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1).between(row(0), row(2)))
                .and(row(1).between(0).and(2))
                .and(row(1).betweenSymmetric(row(2), row(0)))
                .and(row(1).betweenSymmetric(2).and(0))
                .and(row(1).notBetween(row(2), row(4)))
                .and(row(1).notBetween(2).and(4))
                .and(row(1).notBetweenSymmetric(row(3), row(5)))
                .and(row(1).notBetweenSymmetric(3).and(5))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1).between(row(1, 0), row(1, 2)))
                .and(row(1, 1).between(1, 0).and(1, 2))
                .and(row(1, 1).betweenSymmetric(row(1, 2), row(1, 0)))
                .and(row(1, 1).betweenSymmetric(1, 2).and(1, 0))
                .and(row(1, 1).notBetween(row(1, 2), row(1, 0)))
                .and(row(1, 1).notBetween(1, 2).and(1, 0))
                .and(row(1, 1).notBetweenSymmetric(row(1, 3), row(1, 5)))
                .and(row(1, 1).notBetweenSymmetric(1, 3).and(1, 5))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
            .where(trueCondition())
            .and(row(1, 1, 1).between(row(1, 1, 0), row(1, 1, 2)))
            .and(row(1, 1, 1).between(1, 1, 0).and(1, 1, 2))
            .and(row(1, 1, 1).betweenSymmetric(row(1, 1, 2), row(1, 1, 0)))
            .and(row(1, 1, 1).betweenSymmetric(1, 1, 2).and(1, 1, 0))
            .and(row(1, 1, 1).notBetween(row(1, 1, 2), row(1, 1, 0)))
            .and(row(1, 1, 1).notBetween(1, 1, 2).and(1, 1, 0))
            .and(row(1, 1, 1).notBetweenSymmetric(row(1, 1, 3), row(1, 1, 5)))
            .and(row(1, 1, 1).notBetweenSymmetric(1, 1, 3).and(1, 1, 5))
            .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
            .where(trueCondition())
            .and(row(1, 1, 1, 1).between(row(1, 1, 1, 0), row(1, 1, 1, 2)))
            .and(row(1, 1, 1, 1).between(1, 1, 1, 0).and(1, 1, 1, 2))
            .and(row(1, 1, 1, 1).betweenSymmetric(row(1, 1, 1, 2), row(1, 1, 1, 0)))
            .and(row(1, 1, 1, 1).betweenSymmetric(1, 1, 1, 2).and(1, 1, 1, 0))
            .and(row(1, 1, 1, 1).notBetween(row(1, 1, 1, 2), row(1, 1, 1, 0)))
            .and(row(1, 1, 1, 1).notBetween(1, 1, 1, 2).and(1, 1, 1, 0))
            .and(row(1, 1, 1, 1).notBetweenSymmetric(row(1, 1, 1, 3), row(1, 1, 1, 5)))
            .and(row(1, 1, 1, 1).notBetweenSymmetric(1, 1, 1, 3).and(1, 1, 1, 5))
            .fetchOne(0, Integer.class));
    }

    public void testRowValueExpressionOrderingConditions() throws Exception {
        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1).lt(2))
                .and(row(1).le(1))
                .and(row(1).gt(0))
                .and(row(1).ge(1))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1).lt(1, 2))
                .and(row(1, 1).le(1, 2))
                .and(row(1, 1).gt(1, 0))
                .and(row(1, 1).ge(1, 0))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1).lt(1, 1, 2))
                .and(row(1, 1, 1).le(1, 1, 2))
                .and(row(1, 1, 1).gt(1, 1, 0))
                .and(row(1, 1, 1).ge(1, 1, 0))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1, 1).lt(1, 1, 1, 2))
                .and(row(1, 1, 1, 1).le(1, 1, 1, 2))
                .and(row(1, 1, 1, 1).gt(1, 1, 1, 0))
                .and(row(1, 1, 1, 1).ge(1, 1, 1, 0))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1, 1, 1).lt(1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1).le(1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1).gt(1, 1, 1, 1, 0))
                .and(row(1, 1, 1, 1, 1).ge(1, 1, 1, 1, 0))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1, 1, 1, 1).lt(1, 1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1, 1).le(1, 1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1, 1).gt(1, 1, 1, 1, 1, 0))
                .and(row(1, 1, 1, 1, 1, 1).ge(1, 1, 1, 1, 1, 0))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1, 1, 1, 1, 1).lt(1, 1, 1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1, 1, 1).le(1, 1, 1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1, 1, 1).gt(1, 1, 1, 1, 1, 1, 0))
                .and(row(1, 1, 1, 1, 1, 1, 1).ge(1, 1, 1, 1, 1, 1, 0))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1, 1, 1, 1, 1, 1).lt(1, 1, 1, 1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1, 1, 1, 1).le(1, 1, 1, 1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1, 1, 1, 1).gt(1, 1, 1, 1, 1, 1, 1, 0))
                .and(row(1, 1, 1, 1, 1, 1, 1, 1).ge(1, 1, 1, 1, 1, 1, 1, 0))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1, 1, 1, 1, 1, 1, 1).lt(1, 1, 1, 1, 1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1, 1, 1, 1, 1).le(1, 1, 1, 1, 1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1, 1, 1, 1, 1).gt(1, 1, 1, 1, 1, 1, 1, 1, 0))
                .and(row(1, 1, 1, 1, 1, 1, 1, 1, 1).ge(1, 1, 1, 1, 1, 1, 1, 1, 0))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1, 1, 1, 1, 1, 1, 1, 1).lt(1, 1, 1, 1, 1, 1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1, 1, 1, 1, 1, 1).le(1, 1, 1, 1, 1, 1, 1, 1, 1, 2))
                .and(row(1, 1, 1, 1, 1, 1, 1, 1, 1, 1).gt(1, 1, 1, 1, 1, 1, 1, 1, 1, 0))
                .and(row(1, 1, 1, 1, 1, 1, 1, 1, 1, 1).ge(1, 1, 1, 1, 1, 1, 1, 1, 1, 0))
                .fetchOne(0, Integer.class));
    }

    public void testRowValueExpressionOrderingSubselects() throws Exception {
        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1).lt(select(inline(2))))
                .and(row(1).le(select(inline(1))))
                .and(row(1).gt(select(inline(0))))
                .and(row(1).ge(select(inline(1))))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1).lt(select(inline(1), inline(2))))
                .and(row(1, 1).le(select(inline(1), inline(2))))
                .and(row(1, 1).gt(select(inline(1), inline(0))))
                .and(row(1, 1).ge(select(inline(1), inline(0))))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1).lt(select(inline(1), inline(1), inline(2))))
                .and(row(1, 1, 1).le(select(inline(1), inline(1), inline(2))))
                .and(row(1, 1, 1).gt(select(inline(1), inline(1), inline(0))))
                .and(row(1, 1, 1).ge(select(inline(1), inline(1), inline(0))))
                .fetchOne(0, Integer.class));
    }

    public void testRowValueExpressionQuantifiedComparisonPredicates_EQ_NE() throws Exception {
        Field<Integer> _1 = inline(1);
        Field<Integer> _2 = inline(2);

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1).eq(any(  select(_1).unionAll(select(_2))  )))
                .and(row(1).eq(all(  select(_1).unionAll(select(_1))  )))
                .and(row(1).ne(any(  select(_1).unionAll(select(_2))  )))
                .and(row(1).ne(all(  select(_2).unionAll(select(_2))  )))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1).eq(any(  select(_1, _1).unionAll(select(_1, _2))  )))
                .and(row(1, 1).eq(all(  select(_1, _1).unionAll(select(_1, _1))  )))
                .and(row(1, 1).ne(any(  select(_1, _1).unionAll(select(_1, _2))  )))
                .and(row(1, 1).ne(all(  select(_1, _2).unionAll(select(_2, _2))  )))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1).eq(any(  select(_1, _1, _1).unionAll(select(_1, _1, _2))  )))
                .and(row(1, 1, 1).eq(all(  select(_1, _1, _1).unionAll(select(_1, _1, _1))  )))
                .and(row(1, 1, 1).ne(any(  select(_1, _1, _1).unionAll(select(_1, _1, _2))  )))
                .and(row(1, 1, 1).ne(all(  select(_1, _1, _2).unionAll(select(_2, _2, _2))  )))
                .fetchOne(0, Integer.class));
    }


    public void testRowValueExpressionQuantifiedComparisonPredicates_LE_LT_GE_GT() throws Exception {
        // [#3505] TODO Emulate this
        assumeFamilyNotIn(HANA, ORACLE, REDSHIFT);

        Field<Integer> _0 = inline(0);
        Field<Integer> _1 = inline(1);
        Field<Integer> _2 = inline(2);

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1).eq(any(  select(_1).unionAll(select(_2))  )))
                .and(row(1).eq(all(  select(_1).unionAll(select(_1))  )))
                .and(row(1).ne(any(  select(_1).unionAll(select(_2))  )))
                .and(row(1).ne(all(  select(_2).unionAll(select(_2))  )))

                .and(row(1).lt(any(  select(_1).unionAll(select(_2))  )))
                .and(row(1).lt(all(  select(_2).unionAll(select(_2))  )))
                .and(row(1).le(any(  select(_1).unionAll(select(_1))  )))
                .and(row(1).le(all(  select(_1).unionAll(select(_2))  )))

                .and(row(1).gt(any(  select(_0).unionAll(select(_1))  )))
                .and(row(1).gt(all(  select(_0).unionAll(select(_0))  )))
                .and(row(1).ge(any(  select(_1).unionAll(select(_2))  )))
                .and(row(1).ge(all(  select(_1).unionAll(select(_1))  )))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1).eq(any(  select(_1, _1).unionAll(select(_1, _2))  )))
                .and(row(1, 1).eq(all(  select(_1, _1).unionAll(select(_1, _1))  )))
                .and(row(1, 1).ne(any(  select(_1, _1).unionAll(select(_1, _2))  )))
                .and(row(1, 1).ne(all(  select(_1, _2).unionAll(select(_2, _2))  )))

                .and(row(1, 1).lt(any(  select(_1, _2).unionAll(select(_1, _1))  )))
                .and(row(1, 1).lt(all(  select(_1, _2).unionAll(select(_2, _2))  )))
                .and(row(1, 1).le(any(  select(_1, _0).unionAll(select(_2, _2))  )))
                .and(row(1, 1).le(all(  select(_1, _2).unionAll(select(_2, _2))  )))

                .and(row(1, 1).gt(any(  select(_1, _0).unionAll(select(_2, _2))  )))
                .and(row(1, 1).gt(all(  select(_1, _0).unionAll(select(_0, _0))  )))
                .and(row(1, 1).ge(any(  select(_1, _1).unionAll(select(_2, _2))  )))
                .and(row(1, 1).ge(all(  select(_1, _0).unionAll(select(_1, _1))  )))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(trueCondition())
                .and(row(1, 1, 1).eq(any(  select(_1, _1, _1).unionAll(select(_1, _1, _2))  )))
                .and(row(1, 1, 1).eq(all(  select(_1, _1, _1).unionAll(select(_1, _1, _1))  )))
                .and(row(1, 1, 1).ne(any(  select(_1, _1, _1).unionAll(select(_1, _1, _2))  )))
                .and(row(1, 1, 1).ne(all(  select(_1, _1, _2).unionAll(select(_2, _2, _2))  )))

                .and(row(1, 1, 1).lt(any(  select(_1, _1, _2).unionAll(select(_1, _1, _1))  )))
                .and(row(1, 1, 1).lt(all(  select(_1, _1, _2).unionAll(select(_2, _2, _2))  )))
                .and(row(1, 1, 1).le(any(  select(_1, _1, _2).unionAll(select(_2, _2, _2))  )))
                .and(row(1, 1, 1).le(all(  select(_1, _1, _2).unionAll(select(_2, _2, _2))  )))

                .and(row(1, 1, 1).gt(any(  select(_1, _1, _0).unionAll(select(_2, _2, _2))  )))
                .and(row(1, 1, 1).gt(all(  select(_1, _1, _0).unionAll(select(_0, _0, _0))  )))
                .and(row(1, 1, 1).ge(any(  select(_1, _1, _0).unionAll(select(_2, _2, _2))  )))
                .and(row(1, 1, 1).ge(all(  select(_1, _1, _0).unionAll(select(_1, _1, _1))  )))
                .fetchOne(0, Integer.class));
    }

    public void testRowValueExpressionInConditions() throws Exception {
        assertEquals(1, (int)
        create().selectOne()
                .where(row(1, 2, 3).in(select(val(1), val(2), val(3))))
                .and(row(3, "2").notIn(
                    select(val(2), val("3")).union(
                    select(val(4), val("3")))))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().selectOne()
                .where(row(1, 2, 3).in(select(val(1), val(2), val(3))))
                .and(row(1, 2, 3).notIn(select(val(3), val(2), val(1))))
                .and(row(1, 2, "3").equal(select(val(1), val(2), val("3"))))
                .and(row(1, "2", 3).notEqual(select(val(1), val("4"), val(3))))
                .fetchOne(0, Integer.class));

        assertEquals(1, (int)
        create().select(TBook_ID())
                .from(TBook())
                .where(row(TBook_ID(), TBook_AUTHOR_ID()).in(select(val(1), val(1))))
                .fetchOne(0, Integer.class));

        assertEquals(asList(1, 2),
        create().select(TBook_ID())
                .from(TBook())
                .where(row(TBook_ID(), TBook_AUTHOR_ID()).in(
                    select(val(1), val(1))
                    .unionAll(
                    select(val(2), val(1)))
                    .unionAll(
                    select(TBook_ID(), TBook_ID()).from(TBook()))))
                .orderBy(TBook_ID())
                .fetch(0, Integer.class));
    }

    public void testRowValueExpressionInConditionsWithEmptyList() throws Exception {
        assertEquals(
            Collections.emptyList(),
            create().select(TBook_ID())
                    .from(TBook())
                    .where(row(TBook_ID(), TBook_AUTHOR_ID()).in(Collections.emptyList()))
                    .orderBy(TBook_ID())
                    .fetch(0, Integer.class)
        );

        assertEquals(
            BOOK_IDS,
            create().select(TBook_ID())
                    .from(TBook())
                    .where(row(TBook_ID(), TBook_AUTHOR_ID()).notIn(Collections.emptyList()))
                    .orderBy(TBook_ID())
                    .fetch(0, Integer.class)
        );
    }

    public void testRowValueExpressionNULLPredicate() throws Exception {
        assertEquals(4, (int)
        create().selectCount()
                .from(TBook())
                .where(row(TBook_CO_AUTHOR_ID(), TBook_CO_AUTHOR_ID()).isNull())
                .and(row(TBook_ID(), TBook_AUTHOR_ID()).isNotNull())
                .fetchOne(0, Integer.class));

    }

    public void testRowValueExpressionOverlapsCondition() throws Exception {
        // 1903-06-25
        // 1947-08-24

        long now = System.currentTimeMillis();
        long day = 1000L * 60 * 60 * 24;

        // SQL standard (DATE, DATE) OVERLAPS (DATE, DATE) predicate
        // ---------------------------------------------------------
        assertEquals(2, (int)
        create().selectCount()
                .from(TAuthor())
                .where(row(TAuthor_DATE_OF_BIRTH(), currentDate())
                    .overlaps(new Date(now - day), new Date(now + day)))
                .fetchOne(0, Integer.class));

        // SQL standard (DATE, INTERVAL) OVERLAPS (DATE, INTERVAL) predicate
        // -----------------------------------------------------------------
        /* [pro] */
        if (asList(INGRES).contains(dialect())) {
            log.info("SKIPPING", "Ingres INTERVAL OVERLAPS tests");
        }
        else
        /* [/pro] */
        {
            assertEquals(1, (int)
            create().selectOne()
                    .where(row(new Date(now), new DayToSecond(3))
                        .overlaps(new Date(now + day), new DayToSecond(3)))
                    .fetchOne(0, Integer.class));

            // jOOQ should recognise these as a (DATE, INTERVAL) tuple
            assertEquals(1, (int)
            create().selectOne()
                    .where(row(new Date(now), 3)
                        .overlaps(new Date(now + day), 3))
                    .fetchOne(0, Integer.class));
        }

        // jOOQ's convenience for letting arbitrary data types "overlap"
        // -------------------------------------------------------------
        assertEquals(1, (int)
        create().selectOne()
                .where(row(1, 3).overlaps(2, 4))
                .and(row(1, 4).overlaps(2, 3))
                .and(row(1, 4).overlaps(3, 2))
                .and(not(row(1, 2).overlaps(3, 4)))
                .fetchOne(0, Integer.class));
    }

    public void testRowValueExpressionRecords() {

        // All record types should be assignment-compatible to the general-purpose
        // record type, both at compile-time and at run-time
        Record record;

        // Type-safe record types
        Record1<Integer> r1;
        Record2<Integer, String> r2;
        Record3<Integer, String, Integer> r3;
        Record4<Integer, String, Integer, String> r4;
        Record5<Integer, String, Integer, String, Integer> r5;
        Record6<Integer, String, Integer, String, Integer, String> r6;
        Record7<Integer, String, Integer, String, Integer, String, Integer> r7;
        Record8<Integer, String, Integer, String, Integer, String, Integer, String> r8;
        Record r9;

        record = r1 = create().fetchOne(select(val(1)));
        record = r2 = create().fetchOne(select(val(1), val("2")));
        record = r3 = create().fetchOne(select(val(1), val("2"), val(3)));
        record = r4 = create().fetchOne(select(val(1), val("2"), val(3), val("4")));
        record = r5 = create().fetchOne(select(val(1), val("2"), val(3), val("4"), val(5)));
        record = r6 = create().fetchOne(select(val(1), val("2"), val(3), val("4"), val(5), val("6")));
        record = r7 = create().fetchOne(select(val(1), val("2"), val(3), val("4"), val(5), val("6"), val(7)));
        record = r8 = create().fetchOne(select(val(1), val("2"), val(3), val("4"), val(5), val("6"), val(7), val("8")));
        record = r9 = create().fetchOne(select(val(1), val("2"), val(3), val("4"), val(5), val("6"), val(7), val("8"), val(9)));

        assertEquals(record, r9);

        // Checking value[N]() and field[N]() methods
        // ------------------------------------------

        // Checking value1(), field1()
        assertEquals(1, (int) r1.value1()); assertEquals(val(1), r1.field1());
        assertEquals(1, (int) r2.value1()); assertEquals(val(1), r2.field1());
        assertEquals(1, (int) r3.value1()); assertEquals(val(1), r3.field1());
        assertEquals(1, (int) r4.value1()); assertEquals(val(1), r4.field1());
        assertEquals(1, (int) r5.value1()); assertEquals(val(1), r5.field1());
        assertEquals(1, (int) r6.value1()); assertEquals(val(1), r6.field1());
        assertEquals(1, (int) r7.value1()); assertEquals(val(1), r7.field1());
        assertEquals(1, (int) r8.value1()); assertEquals(val(1), r8.field1());

        // Checking value2(), field2()
        assertEquals("2", r2.value2()); assertEquals(val("2"), r2.field2());
        assertEquals("2", r3.value2()); assertEquals(val("2"), r3.field2());
        assertEquals("2", r4.value2()); assertEquals(val("2"), r4.field2());
        assertEquals("2", r5.value2()); assertEquals(val("2"), r5.field2());
        assertEquals("2", r6.value2()); assertEquals(val("2"), r6.field2());
        assertEquals("2", r7.value2()); assertEquals(val("2"), r7.field2());
        assertEquals("2", r8.value2()); assertEquals(val("2"), r8.field2());

        // Checking value3(), field3()
        assertEquals(3, (int) r3.value3()); assertEquals(val(3), r3.field3());
        assertEquals(3, (int) r4.value3()); assertEquals(val(3), r4.field3());
        assertEquals(3, (int) r5.value3()); assertEquals(val(3), r5.field3());
        assertEquals(3, (int) r6.value3()); assertEquals(val(3), r6.field3());
        assertEquals(3, (int) r7.value3()); assertEquals(val(3), r7.field3());
        assertEquals(3, (int) r8.value3()); assertEquals(val(3), r8.field3());

        // Checking value4(), field4()
        assertEquals("4", r4.value4()); assertEquals(val("4"), r4.field4());
        assertEquals("4", r5.value4()); assertEquals(val("4"), r5.field4());
        assertEquals("4", r6.value4()); assertEquals(val("4"), r6.field4());
        assertEquals("4", r7.value4()); assertEquals(val("4"), r7.field4());
        assertEquals("4", r8.value4()); assertEquals(val("4"), r8.field4());

        // Checking value5(), field5()
        assertEquals(5, (int) r5.value5()); assertEquals(val(5), r5.field5());
        assertEquals(5, (int) r6.value5()); assertEquals(val(5), r6.field5());
        assertEquals(5, (int) r7.value5()); assertEquals(val(5), r7.field5());
        assertEquals(5, (int) r8.value5()); assertEquals(val(5), r8.field5());

        // Checking value6(), field6()
        assertEquals("6", r6.value6()); assertEquals(val("6"), r6.field6());
        assertEquals("6", r7.value6()); assertEquals(val("6"), r7.field6());
        assertEquals("6", r8.value6()); assertEquals(val("6"), r8.field6());

        // Checking value7(), field7()
        assertEquals(7, (int) r7.value7()); assertEquals(val(7), r7.field7());
        assertEquals(7, (int) r8.value7()); assertEquals(val(7), r8.field7());

        // Checking value8(), field8()
        assertEquals("8", r8.value8()); assertEquals(val("8"), r8.field8());

        // Checking fieldsRow() and valuesRow() methods
        // --------------------------------------------
        Row1<Integer> row1;
        Row2<Integer, String> row2;
        Row3<Integer, String, Integer> row3;
        Row4<Integer, String, Integer, String> row4;
        Row5<Integer, String, Integer, String, Integer> row5;
        Row6<Integer, String, Integer, String, Integer, String> row6;
        Row7<Integer, String, Integer, String, Integer, String, Integer> row7;
        Row8<Integer, String, Integer, String, Integer, String, Integer, String> row8;

        for (int i = 0; i < 2; i++) {

            // In the first run, consider the row value expression's fields...
            if (i == 0) {
                row1 = r1.fieldsRow();
                row2 = r2.fieldsRow();
                row3 = r3.fieldsRow();
                row4 = r4.fieldsRow();
                row5 = r5.fieldsRow();
                row6 = r6.fieldsRow();
                row7 = r7.fieldsRow();
                row8 = r8.fieldsRow();
            }

            // ... in this test-case, they should coincide with the values
            else {
                row1 = r1.valuesRow();
                row2 = r2.valuesRow();
                row3 = r3.valuesRow();
                row4 = r4.valuesRow();
                row5 = r5.valuesRow();
                row6 = r6.valuesRow();
                row7 = r7.valuesRow();
                row8 = r8.valuesRow();
            }

            // Checking field1()
            assertEquals(val(1), row1.field1());
            assertEquals(val(1), row2.field1());
            assertEquals(val(1), row3.field1());
            assertEquals(val(1), row4.field1());
            assertEquals(val(1), row5.field1());
            assertEquals(val(1), row6.field1());
            assertEquals(val(1), row7.field1());
            assertEquals(val(1), row8.field1());

            // Checking field2()
            assertEquals(val("2"), row2.field2());
            assertEquals(val("2"), row3.field2());
            assertEquals(val("2"), row4.field2());
            assertEquals(val("2"), row5.field2());
            assertEquals(val("2"), row6.field2());
            assertEquals(val("2"), row7.field2());
            assertEquals(val("2"), row8.field2());

            // Checking field3()
            assertEquals(val(3), row3.field3());
            assertEquals(val(3), row4.field3());
            assertEquals(val(3), row5.field3());
            assertEquals(val(3), row6.field3());
            assertEquals(val(3), row7.field3());
            assertEquals(val(3), row8.field3());

            // Checking field4()
            assertEquals(val("4"), row4.field4());
            assertEquals(val("4"), row5.field4());
            assertEquals(val("4"), row6.field4());
            assertEquals(val("4"), row7.field4());
            assertEquals(val("4"), row8.field4());

            // Checking field5()
            assertEquals(val(5), row5.field5());
            assertEquals(val(5), row6.field5());
            assertEquals(val(5), row7.field5());
            assertEquals(val(5), row8.field5());

            // Checking field6()
            assertEquals(val("6"), row6.field6());
            assertEquals(val("6"), row7.field6());
            assertEquals(val("6"), row8.field6());

            // Checking field7()
            assertEquals(val(7), row7.field7());
            assertEquals(val(7), row8.field7());

            // Checking field8()
            assertEquals(val("8"), row8.field8());
        }
    }

    public void testRowValueExpressionTableRecords() throws Exception {

        // [#1918] Generated records now also implement Record[N] interfaces
        // Check for assignment-compatibility
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Record6<Integer, String, String, Date, Integer, Object> author = (Record6) getAuthor(1);

        // Check field[N]() methods
        assertEquals(TAuthor_ID(), author.field1());
        assertEquals(TAuthor_FIRST_NAME(), author.field2());
        assertEquals(TAuthor_LAST_NAME(), author.field3());
        assertEquals(TAuthor_DATE_OF_BIRTH(), author.field4());
        assertEquals(TAuthor_YEAR_OF_BIRTH(), author.field5());
        // ignore field6(), which is possibly a UDT

        // Check value[N]() methods
        assertEquals(1, (int) author.value1());
        assertEquals("George", author.value2());
        assertEquals("Orwell", author.value3());
        assertEquals(Date.valueOf("1903-06-25"), author.value4());
        assertEquals(1903, (int) author.value5());
        // ignore field6(), which is possibly a UDT

        // Check if the a record can be re-selected using row-value expression predicates
        // Note, can't use author, because it contains a NULL value in the address field
        B2S b2s = create().selectFrom(TBookToBookStore()).where(TBookToBookStore_BOOK_ID().eq(2)).fetchOne();
        assertEquals(b2s,
        create().fetchOne(
            select(
                b2s.field1(),
                b2s.field2(),
                b2s.field3())
            .from(TBookToBookStore())
            .where(b2s.fieldsRow().eq(b2s.valuesRow()))

            // Add a dummy union, as a compile-time type-check
            .union(
            select(
                inline("abc"),
                inline(1),
                inline(1))
            .where(inline(1).eq(inline(0))))

            // Another dummy union as a compile-time type-check
            .union(create()
            .selectFrom(TBookToBookStore())
            .where(one().eq(zero())))
        ));
    }

    public void testRowValueExpressionValuesConstructor() throws Exception {
        // [#915] TODO: How to properly alias things in order to be able to select them?

//        create().select()
//                .from(Factory.values(row(1, 2), row(3, 4)))
//                .fetch();
    }

    public void testRowValueExpressionInSelectClause() throws Exception {
        Field<Integer> authorId = TAuthor_ID().as("author_id");
        Field<Integer> bookId = TBook_ID().as("book_id");

        Result<Record4<
            Integer,
            Record2<String, String>,
            Integer,
            Record2<String, Integer>
        >> result =
        create().select(
                    authorId,
                    field(row(TAuthor_FIRST_NAME(), TAuthor_LAST_NAME())).as("row1"),
                    bookId,
                    field(row(TBook_TITLE(), TBook_LANGUAGE_ID())).as("row2")
                )
                .from(TAuthor())
                .join(TBook())
                .on(TAuthor_ID().eq(TBook_AUTHOR_ID()))
                .union(
                 select(
                    val(1),
                    field(row("A", "B")),
                    val(2),
                    field(row("C", 3))
                )
                .where("1 = 0"))
                .orderBy(1)
                .fetch();

        assertEquals(4, result.fields().length);
        assertEquals(BOOK_AUTHOR_IDS, result.getValues(authorId));
        assertEquals(BOOK_FIRST_NAMES, result.map(r -> r.value2().value1()));
        assertEquals(BOOK_LAST_NAMES, result.map(r -> r.value2().value2()));
        assertEquals(BOOK_IDS, result.getValues(bookId));
        assertEquals(BOOK_TITLES, result.map(r -> r.value4().value1()));
        assertEquals(BOOK_LANGUAGES, result.map(r -> r.value4().value2()));
    }
}
