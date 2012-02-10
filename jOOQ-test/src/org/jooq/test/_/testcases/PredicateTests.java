/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.impl.Factory.castNull;
import static org.jooq.impl.Factory.concat;
import static org.jooq.impl.Factory.count;
import static org.jooq.impl.Factory.escape;
import static org.jooq.impl.Factory.lower;
import static org.jooq.impl.Factory.trueCondition;
import static org.jooq.impl.Factory.upper;
import static org.jooq.impl.Factory.val;

import java.util.Arrays;
import java.util.Collections;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

public class PredicateTests<
    A    extends UpdatableRecord<A>,
    B    extends UpdatableRecord<B>,
    S    extends UpdatableRecord<S>,
    B2S  extends UpdatableRecord<B2S>,
    BS   extends UpdatableRecord<BS>,
    L    extends TableRecord<L>,
    X    extends TableRecord<X>,
    D    extends UpdatableRecord<D>,
    T    extends UpdatableRecord<T>,
    U    extends TableRecord<U>,
    I    extends TableRecord<I>,
    IPK  extends UpdatableRecord<IPK>,
    T658 extends TableRecord<T658>,
    T725 extends UpdatableRecord<T725>,
    T639 extends UpdatableRecord<T639>,
    T785 extends TableRecord<T785>>
extends BaseTest<A, B, S, B2S, BS, L, X, D, T, U, I, IPK, T658, T725, T639, T785> {

    public PredicateTests(jOOQAbstractTest<A, B, S, B2S, BS, L, X, D, T, U, I, IPK, T658, T725, T639, T785> delegate) {
        super(delegate);
    }

    @Test
    public void testIsTrue() throws Exception {
        assertEquals(0, create().select().where(val(null).isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("asdf").isTrue()).fetch().size());

        assertEquals(0, create().select().where(val(0).isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("false").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("n").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("no").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("0").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("disabled").isTrue()).fetch().size());
        assertEquals(0, create().select().where(val("off").isTrue()).fetch().size());

        assertEquals(1, create().select().where(val(1).isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("true").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("y").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("yes").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("1").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("enabled").isTrue()).fetch().size());
        assertEquals(1, create().select().where(val("on").isTrue()).fetch().size());

        assertEquals(0, create().select().where(val("asdf").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val(null).isFalse()).fetch().size());

        assertEquals(1, create().select().where(val(0).isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("false").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("n").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("no").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("0").isFalse()).fetch().size());

        assertEquals(1, create().select().where(val("disabled").isFalse()).fetch().size());
        assertEquals(1, create().select().where(val("off").isFalse()).fetch().size());

        assertEquals(0, create().select().where(val(1).isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("true").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("y").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("yes").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("1").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("enabled").isFalse()).fetch().size());
        assertEquals(0, create().select().where(val("on").isFalse()).fetch().size());

        // The below code throws an exception on Ingres when run once. When run
        // twice, the DB crashes... This seems to be a driver / database bug
        if (getDialect() != SQLDialect.INGRES) {
            assertEquals(0, create().select().where(val(false).isTrue()).fetch().size());
            assertEquals(1, create().select().where(val(false).isFalse()).fetch().size());
            assertEquals(1, create().select().where(val(true).isTrue()).fetch().size());
            assertEquals(0, create().select().where(val(true).isFalse()).fetch().size());
        }
    }

    @Test
    public void testLike() throws Exception {
        jOOQAbstractTest.reset = false;

        Field<String> notLike = TBook_PUBLISHED_IN().cast(String.class);

        // DB2 doesn't support this syntax
        if (getDialect() == DB2) {
            notLike = val("bbb");
        }

        Result<B> books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().like("%a%"))
                .and(TBook_TITLE().notLike(notLike))
                .fetch();

        assertEquals(3, books.size());

        assertEquals(1,
        create().insertInto(TBook())
                .set(TBook_ID(), 5)
                .set(TBook_AUTHOR_ID(), 2)
                .set(TBook_PUBLISHED_IN(), 2012)
                .set((Field<Integer>) TBook_LANGUAGE_ID(), 1)
                .set(TBook_TITLE(), "About percentages (%) and underscores (_), a critical review")
                .execute());

        // [#1072] Add checks for ESCAPE syntax
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().like("%(!%)%", '!'))
                .and(TBook_TITLE().like("%(#_)%", '#'))
                .and(TBook_TITLE().notLike("%(!%)%", '#'))
                .and(TBook_TITLE().notLike("%(#_)%", '!'))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(5, (int) books.get(0).getValue(TBook_ID()));

        // DERBY doesn't know any REPLACE function, hence only test those
        // conditions that do not use REPLACE internally
        boolean derby = getDialect() == DERBY;

        // [#1131] DB2 doesn't like concat in LIKE expressions very much
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().like(concat("19", "84")))
                .and(TBook_TITLE().like(upper(concat("198", "4"))))
                .and(TBook_TITLE().like(lower(concat("1", "984"))))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(1, (int) books.get(0).getValue(TBook_ID()));

        // [#1106] Add checks for Factory.escape() function
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().like(concat("%", escape("(%)", '!'), "%"), '!'))
                .and(derby ? trueCondition() :
                     TBook_TITLE().like(concat(val("%"), escape(val("(_)"), '#'), val("%")), '#'))
                .and(TBook_TITLE().notLike(concat("%", escape("(!%)", '#'), "%"), '#'))
                .and(derby ? trueCondition() :
                     TBook_TITLE().notLike(concat(val("%"), escape(val("(#_)"), '!'), val("%")), '!'))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(5, (int) books.get(0).getValue(TBook_ID()));

        // [#1089] Add checks for convenience methods
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().contains("%"))
                .and(derby ? trueCondition() :
                     TBook_TITLE().contains(val("(_")))
                .and(TBook_TITLE().startsWith("About"))
                .and(derby ? trueCondition() :
                     TBook_TITLE().startsWith(val("Abo")))
                .and(TBook_TITLE().endsWith("review"))
                .and(derby ? trueCondition() :
                     TBook_TITLE().endsWith(val("review")))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(5, (int) books.get(0).getValue(TBook_ID()));
    }

    @Test
    public void testLargeINCondition() throws Exception {
        Field<Integer> count = count();
        assertEquals(1, (int) create().select(count)
                                      .from(TBook())
                                      .where(TBook_ID().in(Collections.nCopies(999, 1)))
                                      .fetchOne(count));

        switch (getDialect()) {
            case SQLITE:
                log.info("SKIPPING", "SQLite can't handle more than 999 variables");
                break;

            default:
                assertEquals(1, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().in(Collections.nCopies(1000, 1)))
                    .fetchOne(count));

                assertEquals(1, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().in(Collections.nCopies(1001, 1)))
                    .fetchOne(count));

                // SQL Server's is at 2100...
                // Sybase ASE's is at 2000...
                assertEquals(1, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().in(Collections.nCopies(1950, 1)))
                    .fetchOne(count));

                assertEquals(3, (int) create().select(count)
                    .from(TBook())
                    .where(TBook_ID().notIn(Collections.nCopies(1950, 1)))
                    .fetchOne(count));

                break;
        }
    }

    @Test
    public void testConditionalSelect() throws Exception {
        Condition c = trueCondition();

        assertEquals(4, create().selectFrom(TBook()).where(c).execute());

        c = c.and(TBook_PUBLISHED_IN().greaterThan(1945));
        assertEquals(3, create().selectFrom(TBook()).where(c).execute());

        c = c.not();
        assertEquals(1, create().selectFrom(TBook()).where(c).execute());

        c = c.or(TBook_AUTHOR_ID().equal(
            create().select(TAuthor_ID()).from(TAuthor()).where(TAuthor_FIRST_NAME().equal("Paulo"))));
        assertEquals(3, create().selectFrom(TBook()).where(c).execute());
    }

    @Test
    public void testConditions() throws Exception {
        // The BETWEEN clause
        assertEquals(Arrays.asList(2, 3), create().select()
            .from(TBook())
            .where(TBook_ID().between(2, 3))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        assertEquals(Arrays.asList(3, 4), create().select()
            .from(TBook())
            .where(val(3).between(TBook_AUTHOR_ID(), TBook_ID()))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        // [#1074] The NOT BETWEEN clause
        assertEquals(Arrays.asList(1, 4), create().select()
            .from(TBook())
            .where(TBook_ID().notBetween(2, 3))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 2), create().select()
            .from(TBook())
            .where(val(3).notBetween(TBook_AUTHOR_ID(), TBook_ID()))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        // The IN clause
        // [#502] empty set checks
        assertEquals(Arrays.asList(), create().select()
            .from(TBook())
            .where(TBook_ID().in(new Integer[0]))
            .fetch(TBook_ID()));
        assertEquals(BOOK_IDS, create().select()
            .from(TBook())
            .where(TBook_ID().notIn(new Integer[0]))
            .orderBy(TBook_ID())
            .fetch(TBook_ID()));

        // The IN clause
        // [#1073] NULL checks
        assertEquals(
        asList(1),
        create().select(TBook_ID())
                .from(TBook())
                .where(TBook_ID().in(val(1), castNull(Integer.class)))
                .fetch(TBook_ID()));

        // [#1073] Some dialects incorrectly handle NULL in NOT IN predicates
        if (asList(ASE, HSQLDB, MYSQL).contains(getDialect())) {
            assertEquals(
            asList(2, 3, 4),
            create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().notIn(val(1), castNull(Integer.class)))
                    .orderBy(TBook_ID())
                    .fetch(TBook_ID()));
        }
        else {
            assertEquals(
            asList(),
            create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().notIn(val(1), castNull(Integer.class)))
                    .fetch(TBook_ID()));
        }

        assertEquals(Arrays.asList(1, 2), create().select()
            .from(TBook())
            .where(TBook_ID().in(1, 2))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        assertEquals(Arrays.asList(2, 3, 4), create().select()
            .from(TBook())
            .where(val(2).in(TBook_ID(), TBook_AUTHOR_ID()))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        // = { ALL | ANY | SOME }
        switch (getDialect()) {
            case SQLITE:
                log.info("SKIPPING", "= { ALL | ANY | SOME } tests");
                break;

            default: {

                // Testing = ALL(subquery)
                assertEquals(Arrays.asList(1), create().select()
                    .from(TBook())
                    .where(TBook_ID().equalAll(create().selectOne()))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(), create().select()
                    .from(TBook())
                    .where(TBook_ID().equalAll(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2))))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Testing = ANY(subquery)
                assertEquals(Arrays.asList(1), create().select()
                    .from(TBook())
                    .where(TBook_ID().equalAny(create().selectOne()))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(1, 2), create().select()
                    .from(TBook())
                    .where(TBook_ID().equalAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2))))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Testing = ALL(array)
                assertEquals(Arrays.asList(1), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equalAll(1))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equalAll(1, 2))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Testing = ANY(array)
                assertEquals(Arrays.asList(1), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equalAny(1))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(1, 2), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equalAny(1, 2))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Inducing the above to work the same way as all other operators
                // Check all operators in a single query
                assertEquals(Arrays.asList(3), create()
                    .select()
                    .from(TBook())
                    .where(TBook_ID().equal(create().select(val(3))))
                    .and(TBook_ID().equalAll(create().select(val(3))))
                    .and(TBook_ID().equalAll(3, 3))
                    .and(TBook_ID().equalAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(3, 4))))
                    .and(TBook_ID().equalAny(3, 4))
                    .and(TBook_ID().notEqual(create().select(val(1))))
                    .and(TBook_ID().notEqualAll(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().notEqualAll(1, 4, 4))
                    .and(TBook_ID().notEqualAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().notEqualAny(1, 4, 4))
                    .and(TBook_ID().greaterOrEqual(create().select(val(1))))
                    .and(TBook_ID().greaterOrEqualAll(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2))))
                    .and(TBook_ID().greaterOrEqualAll(1, 2))
                    .and(TBook_ID().greaterOrEqualAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().greaterOrEqualAny(1, 4))
                    .and(TBook_ID().greaterThan(create().select(val(1))))
                    .and(TBook_ID().greaterThanAll(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2))))
                    .and(TBook_ID().greaterThanAll(1, 2))
                    .and(TBook_ID().greaterThanAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().greaterThanAny(1, 4))
                    .and(TBook_ID().lessOrEqual(create().select(val(3))))
                    .and(TBook_ID().lessOrEqualAll(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(3, 4))))
                    .and(TBook_ID().lessOrEqualAll(3, 4))
                    .and(TBook_ID().lessOrEqualAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().lessOrEqualAny(1, 4))
                    .and(TBook_ID().lessThan(create().select(val(4))))
                    .and(TBook_ID().lessThanAll(create().select(val(4))))
                    .and(TBook_ID().lessThanAll(4, 5))
                    .and(TBook_ID().lessThanAny(create().select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4))))
                    .and(TBook_ID().lessThanAny(1, 4))
                    .fetch(TBook_ID()));

                break;
            }
        }
    }
}
