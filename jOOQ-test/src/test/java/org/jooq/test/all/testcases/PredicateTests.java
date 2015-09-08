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
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.INGRES;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.REDSHIFT;
import static org.jooq.conf.StatementType.STATIC_STATEMENT;
import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.any;
import static org.jooq.impl.DSL.castNull;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.escape;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lower;
import static org.jooq.impl.DSL.not;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.trueCondition;
import static org.jooq.impl.DSL.two;
import static org.jooq.impl.DSL.upper;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.DSL.zero;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

public class PredicateTests<
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

    public PredicateTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, CS, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

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
        if (true/* [pro] */ && dialect() != SQLDialect.INGRES/* [/pro] */) {
            assertEquals(0, create().select().where(val(false).isTrue()).fetch().size());
            assertEquals(1, create().select().where(val(false).isFalse()).fetch().size());
            assertEquals(1, create().select().where(val(true).isTrue()).fetch().size());
            assertEquals(0, create().select().where(val(true).isFalse()).fetch().size());
        }
    }

    public void testIsDistinctFrom() throws Exception {
        assertEquals(0, create().select().where(val(null, Integer.class).isDistinctFrom((Integer) null)).fetch().size());
        assertEquals(1, create().select().where(val(1).isDistinctFrom((Integer) null)).fetch().size());
        assertEquals(1, create().select().where(val(null, Integer.class).isDistinctFrom(1)).fetch().size());
        assertEquals(0, create().select().where(val(1).isDistinctFrom(1)).fetch().size());

        assertEquals(1, create().select().where(val(null, Integer.class).isNotDistinctFrom((Integer) null)).fetch().size());
        assertEquals(0, create().select().where(val(1).isNotDistinctFrom((Integer) null)).fetch().size());
        assertEquals(0, create().select().where(val(null, Integer.class).isNotDistinctFrom(1)).fetch().size());
        assertEquals(1, create().select().where(val(1).isNotDistinctFrom(1)).fetch().size());
    }

    public void testLike() throws Exception {
        jOOQAbstractTest.reset = false;

        Field<String> notLike = TBook_PUBLISHED_IN().cast(String.class);

        /* [pro] */
        // DB2 doesn't support this syntax
        if (dialect() == DB2) {
            notLike = val("bbb");
        }

        /* [/pro] */
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
                .set(TBook_LANGUAGE_ID(), 1)
                .set(TBook_TITLE(), "About percentages (%) and underscores (_), a critical review! Check exclamation marks, too!")
                .execute());

        // [#1072] Add checks for ESCAPE syntax
        // ------------------------------------
        // [#2818] TODO: Re-enable this test for MS Access
        if (!asList(ACCESS).contains(dialect().family())) {
            books =
            create().selectFrom(TBook())
                    .where(TBook_TITLE().like("%(!%)%", '!'))
                    .and(TBook_TITLE().like("%(#_)%", '#'))
                    .and(TBook_TITLE().notLike("%(!%)%", '#'))
                    .and(TBook_TITLE().notLike("%(#_)%", '!'))
                    .fetch();

            assertEquals(1, books.size());
            assertEquals(5, (int) books.get(0).getValue(TBook_ID()));
        }

        else {
            log.info("SKIPPING", "Most LIKE tests");
            return;
        }

        // DERBY doesn't know any REPLACE function, hence only test those
        // conditions that do not use REPLACE internally
        boolean derby = dialect() == DERBY;

        // [#1131] DB2 doesn't like concat in LIKE expressions very much
        // -------------------------------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().like(concat("19", "84")))
                .and(TBook_TITLE().like(upper(concat("198", "4"))))
                .and(TBook_TITLE().like(lower(concat("1", "984"))))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(1, (int) books.get(0).getValue(TBook_ID()));

        // [#1106] Add checks for Factory.escape() function
        // ------------------------------------------------
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
        // ------------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().contains("%"))
                .and(TBook_TITLE().contains("review!"))
                .and(derby ? trueCondition() :
                     TBook_TITLE().contains(val("(_")))

                .and(TBook_TITLE().startsWith("About"))
                .and(derby ? trueCondition() :
                     TBook_TITLE().startsWith(val("Abo")))

                .and(TBook_TITLE().endsWith("too!"))
                .and(derby ? trueCondition() :
                     TBook_TITLE().endsWith(val("too!")))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(5, (int) books.get(0).getValue(TBook_ID()));

        // [#1423] Add checks for the ILIKE operator
        // -----------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_TITLE().likeIgnoreCase("%IM%"))
                .and(TBook_TITLE().notLikeIgnoreCase("%o%"))
                .fetch();

        assertEquals(1, books.size());
        assertEquals(asList(2), books.getValues(TBook_ID()));
    }

    public void testLikeWithNumbers() throws Exception {

        // [#1159] Add checks for matching numbers with LIKE
        // -------------------------------------------------
        Result<B> books =
        create().selectFrom(TBook())
                .where(TBook_PUBLISHED_IN().like("194%"))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(2, books.size());
        assertEquals(asList(1, 2), books.getValues(TBook_ID()));

        books =
        create().selectFrom(TBook())
                .where(TBook_PUBLISHED_IN().like("%9%"))
                .and(TBook_PUBLISHED_IN().notLike("%8%"))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(2, books.size());
        assertEquals(asList(2, 4), books.getValues(TBook_ID()));

        // [#1160] Add checks for convenience methods using numbers
        // --------------------------------------------------------
        books =
        create().selectFrom(TBook())
                .where(TBook_PUBLISHED_IN().contains(9))
                .and(TBook_PUBLISHED_IN().endsWith(88))
                .and(TBook_PUBLISHED_IN().startsWith(1))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(1, books.size());
        assertEquals(asList(3), books.getValues(TBook_ID()));
    }

    public void testLikeRegex() throws Exception {
        switch (dialect().family()) {
            /* [pro] */
            case ACCESS:
            case ASE:
            case DB2:
            case HANA:
            case INFORMIX:
            case INGRES:
            case SQLSERVER:
            /* [/pro] */
            case DERBY:
            case FIREBIRD:
            case SQLITE:
                log.info("SKIPPING", "REGEX tests");
                return;
        }

        Result<B> result =
        create().selectFrom(TBook())
                .where(TBook_CONTENT_TEXT().likeRegex(".*conscious.*"))
                .or(TBook_TITLE().notLikeRegex(".*m.*"))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(2, result.size());
        assertEquals(1, (int) result.get(0).getValue(TBook_ID()));
        assertEquals(4, (int) result.get(1).getValue(TBook_ID()));
    }

    public void testLargeINCondition() throws Exception {

        // This seems to take forever in Redshift. Let's just skip...
        assumeFamilyNotIn(REDSHIFT);

        Field<Integer> count = count();
        assertEquals(1, (int) create().select(count)
                                      .from(TBook())
                                      .where(TBook_ID().in(Collections.nCopies(999, 1)))
                                      .fetchOne(count));

        // Oracle needs splitting of IN(..) expressions when there are
        // more than 1000 elements
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
            .where(TBook_ID().in(Collections.nCopies(2500, 1)))
            .fetchOne(count));

        assertEquals(3, (int) create().select(count)
            .from(TBook())
            .where(TBook_ID().notIn(Collections.nCopies(2500, 1)))
            .fetchOne(count));

        // [#1520] Any database should be able to handle lots of inlined
        // variables, including SQL Server and Sybase ASE
        assertEquals(3, (int) create(new Settings().withStatementType(STATIC_STATEMENT))
            .select(count)
            .from(TBook())
            .where(TBook_ID().notIn(Collections.nCopies(2500, 1)))
            .fetchOne(count));

        // [#1515] Check correct splitting of NOT IN
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(Collections.nCopies(1000, 1));
        list.addAll(Collections.nCopies(1000, 2));

        assertEquals(2, (int) create().select(count)
            .from(TBook())
            .where(TBook_ID().notIn(list))
            .fetchOne(count));
    }

    public void testLargeINConditionWithExecuteListener() throws Exception {

        // This seems to take forever in Redshift. Let's just skip...
        assumeFamilyNotIn(REDSHIFT);

        NoControlFlowSignals listener = new NoControlFlowSignals();

        // [#3427] Internally, jOOQ uses org.jooq.exception.ControlFlowSignal to abort rendering of bind values
        // This "exception" must not escape to client ExecuteListeners
        assertEquals(3, (int) create(listener).select(count())
            .from(TBook())
            .where(TBook_ID().notIn(Collections.nCopies(2500, 1)))
            .fetchOne(count()));

        assertNull(listener.e1);
        assertNull(listener.e2);
    }

    @SuppressWarnings("serial")
    static class NoControlFlowSignals extends DefaultExecuteListener {

        RuntimeException e1;
        SQLException e2;

        @Override
        public void exception(ExecuteContext ctx) {
            super.exception(ctx);

            e1 = ctx.exception();
            e2 = ctx.sqlException();
        }
    }

    public void testConditionalSelect() throws Exception {
        Condition c = trueCondition();

        assertEquals(4, create().selectFrom(TBook()).where(c).execute());

        c = c.and(TBook_PUBLISHED_IN().greaterThan(1945));
        assertEquals(3, create().selectFrom(TBook()).where(c).execute());

        c = c.not();
        assertEquals(1, create().selectFrom(TBook()).where(c).execute());

        c = c.or(TBook_AUTHOR_ID().equal(
            select(TAuthor_ID()).from(TAuthor()).where(TAuthor_FIRST_NAME().equal("Paulo"))));
        assertEquals(3, create().selectFrom(TBook()).where(c).execute());
    }

    public void testBetweenConditions() throws Exception {

        // The BETWEEN clause
        assertEquals(Arrays.asList(2, 3), create().select()
            .from(TBook())
            .where(TBook_ID().between(2, 3))
            .and(TBook_ID().betweenSymmetric(2, 3))
            .and(TBook_ID().betweenSymmetric(3, 2))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        assertEquals(Arrays.asList(3, 4), create().select()
            .from(TBook())
            .where(val(3).between(TBook_AUTHOR_ID(), TBook_ID()))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        // [#1074] The NOT BETWEEN clause
        assertEquals(Arrays.asList(1, 4), create().select()
            .from(TBook())
            .where(TBook_ID().notBetween(2, 3))
            .and(TBook_ID().notBetweenSymmetric(2, 3))
            .and(TBook_ID().notBetweenSymmetric(3, 2))
            .orderBy(TBook_ID()).fetch(TBook_ID()));

        assertEquals(Arrays.asList(1, 2), create().select()
            .from(TBook())
            .where(val(3).notBetween(TBook_AUTHOR_ID(), TBook_ID()))
            .orderBy(TBook_ID()).fetch(TBook_ID()));
    }

    public void testConditions() throws Exception {

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
        // Ingres doesn't correctly implement NULL semantics in IN predicates
        if (!asList(INGRES).contains(dialect().family())) {
            assertEquals(
            asList(1),
            create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().in(val(1), castNull(Integer.class)))
                    .fetch(TBook_ID()));
        }

        // [#1073] Some dialects incorrectly handle NULL in NOT IN predicates
        /* [pro] */
        if (asList(ACCESS, ASE).contains(dialect())) {
            assertEquals(
            asList(2, 3, 4),
            create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().notIn(val(1), castNull(Integer.class)))
                    .orderBy(TBook_ID())
                    .fetch(TBook_ID()));
        }
        else
        /* [/pro] */
        {
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
    }

    public void testInPredicateWithResult() throws Exception {
        Result<Record1<Integer>> r1 = create()
            .select(one())
            .union(
             select(two()))
            .fetch();

        assertEquals(2, create()
            .selectFrom(TBook())
            .where(TBook_ID().in(r1))
            .fetch()
            .size()
        );

        Result<Record2<Integer, Integer>> r2 = create()
            .select(one(), one())
            .union(
             select(two(), one()))
            .fetch();

        assertEquals(2, create()
            .selectFrom(TBook())
            .where(row(TBook_ID(), TBook_AUTHOR_ID()).in(r2))
            .fetch()
            .size()
        );
    }

    public void testInPredicateWithPlainSQL() throws Exception {

        // [#3086] This issue cannot seem to be reproduced again...?
        Result<Record1<Integer>> result =
        create().select(TBook_ID())
                .from(TBook())
                .where(field(TBook_ID().getName()).in(1, 2))
                .or(field(TBook_TITLE().getName()).in(null, null))
                .orderBy(TBook_ID())
                .fetch();

        assertEquals(2, result.size());
        assertEquals(asList(1, 2), result.getValues(TBook_ID()));
    }

    public void testInPredicateWithSubselectAndLimitOffset() throws Exception {

        // [#2335] [#3195] Subqueries must not render additional columns when emulating
        // LIMIT .. OFFSET

        // This query is not yet supported in these databases
        assumeFamilyNotIn(MARIADB, MYSQL);

        Result<Record1<Integer>> result =
        create().select(TBook_ID())
                .from(TBook())
                .where(TBook_AUTHOR_ID().in(
                    select(TAuthor_ID())
                    .from(TAuthor())
                    .orderBy(TAuthor_ID())
                    .limit(1)
                ))
                .orderBy(TBook_ID())
                .limit(1)
                .fetch();

        assertEquals(1, result.size());
        assertEquals(1, (int) result.get(0).value1());
    }

    public void testConditionsAsFields() throws Exception {
        Record3<Boolean, Boolean, Boolean> record = create().select(
            field(one().eq(zero())),
            field(one().eq(1)),
            field(field(one().eq(1)).eq(true))
        ).fetchOne();

        assertEquals(false, record.value1());
        assertEquals(true, record.value2());
        assertEquals(true, record.value2());
    }

    public void testFieldsAsConditions() throws Exception {
        Param<Boolean> t = val(true);
        Param<Boolean> f = val(false);

        assertEquals(1, create().selectOne().where(condition(t)).fetchOne(0));
        assertNull(create().selectOne().where(condition(f)).fetchOne());
        assertEquals(1, create().selectOne().where(t).fetchOne(0));
        assertNull(create().selectOne().where(f).fetchOne());
        assertEquals(1, create().selectOne().where(trueCondition().andNot(f)).fetchOne(0));
        assertNull(create().selectOne().where(trueCondition().andNot(t)).fetchOne());
    }

    public void testNotField() throws Exception {
        Record record = create().select(
            not(true),
            not(false),
            not((Boolean) null),
            field(not(condition(true))),
            field(not(condition(false))),
            field(not(condition((Boolean) null))),
            field(not(condition(val(true)))),
            field(not(condition(val(false)))),
            field(not(condition(val(null, Boolean.class))))
        )
        .fetchOne();

        assertEquals(false, record.getValue(0));
        assertEquals(true, record.getValue(1));
        assertEquals(null, record.getValue(2));
        assertEquals(false, record.getValue(3));
        assertEquals(true, record.getValue(4));
        assertEquals(null, record.getValue(5));
        assertEquals(false, record.getValue(6));
        assertEquals(true, record.getValue(7));
        assertEquals(null, record.getValue(8));
    }

    public void testQuantifiedPredicates() throws Exception {

        // = { ALL | ANY | SOME }
        switch (dialect()) {
            /* [pro] */
            case INGRES: // Ingres supports these syntaxes but has internal errors...
            /* [/pro] */
            case SQLITE:
                log.info("SKIPPING", "= { ALL | ANY | SOME } tests");
                break;

            default: {

                // Testing = ALL(subquery)
                assertEquals(Arrays.asList(1), create().select()
                    .from(TBook())
                    .where(TBook_ID().equal(all(selectOne())))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(), create().select()
                    .from(TBook())
                    .where(TBook_ID().equal(all(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2)))))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Testing = ANY(subquery)
                assertEquals(Arrays.asList(1), create().select()
                    .from(TBook())
                    .where(TBook_ID().equal(any(selectOne())))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(1, 2), create().select()
                    .from(TBook())
                    .where(TBook_ID().equal(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2)))))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Testing = ALL(array)
                assertEquals(Arrays.asList(1), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equal(all(1)))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equal(all(1, 2)))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Testing = ANY(array)
                assertEquals(Arrays.asList(1), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equal(any(1)))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));
                assertEquals(Arrays.asList(1, 2), create().select(TBook_ID())
                    .from(TBook())
                    .where(TBook_ID().equal(any(1, 2)))
                    .orderBy(TBook_ID()).fetch(TBook_ID()));

                // Inducing the above to work the same way as all other operators
                // Check all operators in a single query
                assertEquals(Arrays.asList(3), create()
                    .select()
                    .from(TBook())
                    .where(TBook_ID().equal(select(val(3))))
                    .and(TBook_ID().equal(all(select(val(3)))))
                    .and(TBook_ID().equal(all(3, 3)))
                    .and(TBook_ID().equal(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(3, 4)))))
                    .and(TBook_ID().equal(any(3, 4)))
                    .and(TBook_ID().notEqual(select(val(1))))
                    .and(TBook_ID().notEqual(all(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().notEqual(all(1, 4, 4)))
                    .and(TBook_ID().notEqual(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().notEqual(any(1, 4, 4)))
                    .and(TBook_ID().greaterOrEqual(select(val(1))))
                    .and(TBook_ID().greaterOrEqual(all(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2)))))
                    .and(TBook_ID().greaterOrEqual(all(1, 2)))
                    .and(TBook_ID().greaterOrEqual(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().greaterOrEqual(any(1, 4)))
                    .and(TBook_ID().greaterThan(select(val(1))))
                    .and(TBook_ID().greaterThan(all(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 2)))))
                    .and(TBook_ID().greaterThan(all(1, 2)))
                    .and(TBook_ID().greaterThan(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().greaterThan(any(1, 4)))
                    .and(TBook_ID().lessOrEqual(select(val(3))))
                    .and(TBook_ID().lessOrEqual(all(select(TBook_ID()).from(TBook()).where(TBook_ID().in(3, 4)))))
                    .and(TBook_ID().lessOrEqual(all(3, 4)))
                    .and(TBook_ID().lessOrEqual(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().lessOrEqual(any(1, 4)))
                    .and(TBook_ID().lessThan(select(val(4))))
                    .and(TBook_ID().lessThan(all(select(val(4)))))
                    .and(TBook_ID().lessThan(all(4, 5)))
                    .and(TBook_ID().lessThan(any(select(TBook_ID()).from(TBook()).where(TBook_ID().in(1, 4)))))
                    .and(TBook_ID().lessThan(any(1, 4)))
                    .fetch(TBook_ID()));

                break;
            }
        }
    }

    public void testIgnoreCase() {
        A author =
        create().selectFrom(TAuthor())
                .where(TAuthor_FIRST_NAME().equalIgnoreCase(TAuthor_FIRST_NAME()))
                .and(upper(TAuthor_FIRST_NAME()).equalIgnoreCase(lower(TAuthor_FIRST_NAME())))
                .and(TAuthor_FIRST_NAME().equalIgnoreCase("george"))
                .and(TAuthor_FIRST_NAME().equalIgnoreCase("geORge"))
                .and(TAuthor_FIRST_NAME().equalIgnoreCase("GEORGE"))
                .and(TAuthor_FIRST_NAME().notEqualIgnoreCase("paulo"))
                .fetchOne();

        assertEquals("George", author.getValue(TAuthor_FIRST_NAME()));
    }

    public void testIgnoreCaseForLongStrings() {
        jOOQAbstractTest.reset = false;

        A a = create().newRecord(TAuthor());
        a.setValue(TAuthor_ID(), 3);
        a.setValue(TAuthor_LAST_NAME(), "ABCDEFGHIJ1234567890ABCDEFGHIJ1234567890");
        assertEquals(1, a.store());

        // [#2712] SQL Server VARCHAR types default to length 30. If used in a
        // CAST(x AS VARCHAR), this may unexpectedly truncate strings.
        assertEquals(3, (int)
        create().select(TAuthor_ID())
                .from(TAuthor())
                .where(TAuthor_LAST_NAME().equalIgnoreCase("abcdefghij1234567890abcdefghij1234567890"))
                .fetchOne(TAuthor_ID()));
    }

    public void testBigDecimalPredicates() {

        // [#2902] The Xerial (SQLite) driver binds BigDecimal values as String, internally
        // This can lead to problems with some predicates

        Record2<Integer, BigDecimal> record =
        create().select(TBook_AUTHOR_ID(), sum(TBook_ID()))
                .from(TBook())
                .groupBy(TBook_AUTHOR_ID())
                .having(sum(TBook_ID()).ge(new BigDecimal("4")))
                .fetchOne();

        assertNotNull(record);
        assertEquals(2, record.value1().intValue());
        assertEquals(7, record.value2().intValue());
    }
}
