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
package org.jooq.test._.testcases;

import static java.util.Arrays.asList;
import static org.jooq.Clause.CUSTOM;
import static org.jooq.Clause.DELETE;
import static org.jooq.Clause.DELETE_DELETE;
import static org.jooq.Clause.DELETE_WHERE;
import static org.jooq.Clause.INSERT;
import static org.jooq.Clause.INSERT_INSERT_INTO;
import static org.jooq.Clause.SELECT;
import static org.jooq.Clause.SELECT_FROM;
import static org.jooq.Clause.SELECT_WHERE;
import static org.jooq.Clause.TABLE_ALIAS;
import static org.jooq.Clause.UPDATE;
import static org.jooq.Clause.UPDATE_UPDATE;
import static org.jooq.Clause.UPDATE_WHERE;
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.queryPart;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.trueCondition;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record6;
import org.jooq.RenderContext;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.VisitContext;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.CustomQueryPart;
import org.jooq.impl.DefaultVisitListener;
import org.jooq.test.BaseTest;
import org.jooq.test.jOOQAbstractTest;

import org.junit.Test;

@SuppressWarnings("serial")
public class VisitListenerTests<
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

    public VisitListenerTests(jOOQAbstractTest<A, AP, B, S, B2S, BS, L, X, DATE, BOOL, D, T, U, UU, I, IPK, T725, T639, T785, CASE> delegate) {
        super(delegate);
    }

    public void testVisitListenerOnSELECT() throws Exception {

        // No join with author table
        Result<?> result1 =
        create(new OnlyAuthorIDEqual1())
            .select(TBook_ID())
            .from(TBook())
            .orderBy(TBook_ID())
            .fetch();

        assertEquals(2, result1.size());
        assertEquals(asList(1, 2), result1.getValues(TBook_ID()));

        // Additional predicates
        Result<?> result2 =
        create(new OnlyAuthorIDEqual1())
            .select(TBook_ID())
            .from(TBook())
            .where(TBook_ID().in(BOOK_IDS))
            .orderBy(TBook_ID())
            .fetch();

        assertEquals(2, result2.size());
        assertEquals(asList(1, 2), result2.getValues(TBook_ID()));

        // Join with author table
        Result<?> result3 =
        create(new OnlyAuthorIDEqual1())
            .select(TBook_ID())
            .from(TBook().join(TAuthor())
                         .on(TBook_AUTHOR_ID().eq(TAuthor_ID())))
            .orderBy(TBook_ID())
            .fetch();

        assertEquals(2, result3.size());
        assertEquals(asList(1, 2), result3.getValues(TBook_ID()));

        // Create a union of authors
        Result<?> result4 =
        create(new OnlyAuthorIDEqual1())
            .select(TAuthor_ID())
            .from(TAuthor())
            .where(TAuthor_ID().eq(1))
            .union(
             select(TAuthor_ID())
            .from(TAuthor())
            .where(TAuthor_ID().ne(1)))
            .union(
             select(TAuthor_ID())
            .from(TAuthor()))
            .fetch();

        assertEquals(1, result4.size());
        assertEquals(1, (int) result4.getValue(0, TAuthor_ID()));

        // Use nested selects
        Result<?> result5 =
        create(new OnlyAuthorIDEqual1())
            .select(inline(1).as("value"))
            .where(inline(2).in(select(TAuthor_ID()).from(TAuthor()).where(TAuthor_ID().eq(2))))
            .union(
             select(inline(2))
            .whereExists(selectOne().from(TAuthor()).where(TAuthor_ID().eq(2))))
            .union(
             select(inline(3))
            .from(selectFrom(TAuthor()).where(TAuthor_ID().eq(2))))
            .fetch();

        assertEquals(0, result5.size());

        // Use aliased tables
        Table<B> b = TBook().as("b");
        Table<A> a1 = TAuthor().as("a1");
        Table<A> a2 = TAuthor().as("a2");

        Result<?> result6 =
        create(new OnlyAuthorIDEqual1())
            .select(b.field(TBook_ID()))
            .from(b.join(a1)
                   .on(b.field(TBook_AUTHOR_ID()).eq(a1.field(TAuthor_ID())))
                   .leftOuterJoin(a2)
                   .on(b.field(TBook_CO_AUTHOR_ID()).eq(a2.field(TAuthor_ID())))
            )
            .orderBy(b.field(TBook_ID()))
            .fetch();

        assertEquals(2, result6.size());
        assertEquals(asList(1, 2), result6.getValues(TBook_ID()));

        // Count books from a subselect
        assertEquals(2, create(new OnlyAuthorIDEqual1())
            .fetchCount(selectOne().from(TBook()))
        );
    }

    public void testVisitListenerOnDML() throws Exception {
        jOOQAbstractTest.reset = false;

        // Can only update 2 out of 4 books
        assertEquals(2,
        create(new OnlyAuthorIDEqual1())
            .update(TBook())
            .set(TBook_TITLE(), "changed")
            .execute());

        assertEquals(2, create().fetchCount(
             selectOne()
            .from(TBook())
            .where(TBook_TITLE().eq("changed"))
        ));

        // Can only delete 2 out of 4 books
        assertEquals(2,
        create(new OnlyAuthorIDEqual1())
            .delete(TBook())
            .execute());

        assertEquals(0, create().fetchCount(
            selectOne()
           .from(TBook())
           .where(TBook_TITLE().eq("changed"))
        ));

        /* [pro] xx
        xx xxxxxxxxxxxxxxxxxxx xx xxxxxxx x

            xx xxxxxx xxxxxx xxxxx xxx xxxxxxxxx x x
            xxx x
                xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxx xx
                    xxxxxxxxxxxxxxxxxxxxxxx xx
                    xxxxxxxxxxxxxxxxxxx xxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxx xx
                    xxxxxxxxxxx
                xxxxxxx
            x
            xxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxx x
                xxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            x

            xx xxx xxxxxx xxxxx xxx xxxxxxxxx x x
            xxxxxxxxxxxxxxx
            xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxxxxxx
                xxxxxxxxxxxxxxxx xx
                xxxxxxxxxxxxxxxxxxxxxxx xx
                xxxxxxxxxxxxxxxxxxx xxxxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx
                xxxxxxxxxxxxxxxxxxxxxxxxx xx
                xxxxxxxxxxxx
            xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

            xx xxxxxx xxxxxx xxx xxx xxxxxxx
            xxx x
                xxxxxxxxxx xxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxx xx
                    xxxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxx
                    xxxxxxxxxxx
                xxxxxxx
            x
            xxxxx xxxxxxxxxxxxxxxxxxxx xxxxxxxxx x
                xxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            x
        x
        xx [/pro] */
    }

    public void testVisitListenerFailOnMissingWhere() {
        jOOQAbstractTest.reset = false;

        assertEquals(4,
        create(new FailOnMissingWhere())
            .update(TBook())
            .set(TBook_TITLE(), "changed")
            .where("1 = 1")
            .execute());

        try {
            create(new FailOnMissingWhere())
                .update(TBook())
                .set(TBook_TITLE(), "changed")
                .execute();

            fail();
        }
        catch (MissingWhereException expected) {}
    }

    // -------------------------------------------------------------------------
    // Utilities
    // -------------------------------------------------------------------------

    /**
     * A key object to be used with {@link VisitContext#data()}
     */
    private enum DataKey {

        /**
         * The current nesting level.
         * <p>
         * <ul>
         * <li>Top-level {@link Clause#SELECT} = 1</li>
         * <li>Next-level {@link Clause#SELECT} = 2</li>
         * <li>... etc</li>
         * </ul>
         */
        NESTING_LEVEL,

        /**
         * Whether the current nesting level has predicates (i.e. a
         * {@link Clause#SELECT_WHERE} is present).
         * <p>
         * This is needed to decide whether to render <code>WHERE</code> or
         * <code>AND</code> in order to append more predicates.
         */
        SUBSELECT_HAS_PREDICATES,

        /**
         * The additional predicates that are supposed to be added to the
         * {@link Clause#SELECT_WHERE}.
         */
        SUBSELECT_CONDITIONS
    }

    private class NestingVisitListener extends DefaultVisitListener {

        /**
         * Extract the nesting level from the current {@link VisitContext}.
         */
        final int nestingLevel(VisitContext context) {
            return nestingLevel(context, 0);
        }

        /**
         * Increase or decrease the nesting level from the current
         * {@link VisitContext}.
         * <p>
         * Possible values for <code>increase</code> are:
         * <ul>
         * <li><code>-1</code>: Decrease the level by one (leaving a subselect)</li>
         * <li><code>0</code>: Leaving the level untouched</li>
         * <li><code>1</code>: Increase the level by one (entering a subselect)</li>
         * </ul>
         */
        int nestingLevel(VisitContext context, int increase) {
            Integer level = (Integer) context.data(DataKey.NESTING_LEVEL);
            if (level == null) {
                level = 0;
            }

            // Clean up the level that we're about to leave.
            if (increase == -1) {
                anyPredicatesMap(context).remove(level);
                subselectConditionMap(context).remove(level);
            }

            level += increase;
            context.data(DataKey.NESTING_LEVEL, level);

            // Initialise the new level that we're about to enter.
            if (increase == 1) {
                subselectConditionMap(context).put(level, new ArrayList<Condition>());
                anyPredicatesMap(context).put(level, false);
            }

            return level;
        }

        @Override
        public void clauseStart(VisitContext context) {

            // Operating on RenderContext only, as we're using inline values
            if (context.renderContext() == null)
                return;

            // Enter a new SELECT clause / nested select, or DML statement
            if (context.clause() == SELECT ||
                context.clause() == UPDATE ||
                context.clause() == DELETE ||
                context.clause() == INSERT) {
                nestingLevel(context, 1);
            }
        }

        @Override
        public void clauseEnd(VisitContext context) {

            // Operating on RenderContext only, as we're using inline values
            if (context.renderContext() == null)
                return;

            // Leave a SELECT clause / nested select, or DML statement
            if (context.clause() == SELECT ||
                context.clause() == UPDATE ||
                context.clause() == DELETE ||
                context.clause() == INSERT) {
                nestingLevel(context, -1);
            }
        }



        @Override
        public void visitEnd(VisitContext context) {

            // Operating on RenderContext only, as we're using inline values
            if (context.renderContext() == null)
                return;

            // Check if we're rendering any condition within the WHERE clause
            // In this case, we can be sure that jOOQ will render a WHERE keyword
            if (context.queryPart() instanceof Condition) {
                List<Clause> clauses = subselectClauses(context);

                if (clauses.contains(SELECT_WHERE) ||
                    clauses.contains(UPDATE_WHERE) ||
                    clauses.contains(DELETE_WHERE)) {
                    anyPredicates(context, true);
                }
            }
        }

        /**
         * Lazy-initialise the per-level list of predicates to be added
         * subselects.
         */
        @SuppressWarnings("unchecked")
        Map<Integer, List<Condition>> subselectConditionMap(VisitContext context) {
            Map<Integer, List<Condition>> data = (Map<Integer, List<Condition>>) context.data(DataKey.SUBSELECT_CONDITIONS);
            if (data == null) {
                data = new HashMap<Integer, List<Condition>>();
                context.data(DataKey.SUBSELECT_CONDITIONS, data);
            }
            return data;
        }

        /**
         * Access the list of predicates to be added to the current subselect
         * level.
         */
        List<Condition> subselectConditions(VisitContext context) {
            return subselectConditionMap(context).get(nestingLevel(context));
        }

        /**
         * Lazy-initialise the per-level map for "has predicates" flags.
         */
        @SuppressWarnings("unchecked")
        Map<Integer, Boolean> anyPredicatesMap(VisitContext context) {
            Map<Integer, Boolean> data = (Map<Integer, Boolean>) context.data(DataKey.SUBSELECT_HAS_PREDICATES);
            if (data == null) {
                data = new HashMap<Integer, Boolean>();
                context.data(DataKey.SUBSELECT_HAS_PREDICATES, data);
            }
            return data;
        }

        /**
         * Check whether the current subselect level already has predicates.
         */
        boolean anyPredicates(VisitContext context) {
            return anyPredicatesMap(context).get(nestingLevel(context));
        }

        /**
         * Indicate whether the current subselect level already has predicates.
         */
        void anyPredicates(VisitContext context, boolean value) {
            anyPredicatesMap(context).put(nestingLevel(context), value);
        }

        /**
         * Retrieve all clauses for the current subselect level, starting with
         * the last {@link Clause#SELECT}.
         */
        List<Clause> subselectClauses(VisitContext context) {
            List<Clause> result = asList(context.clauses());
            int index = result.lastIndexOf(SELECT);

            if (index > 0)
                return result.subList(index, result.size() - 1);
            else
                return result;
        }
    }

    private class FailOnMissingWhere extends NestingVisitListener {

        @Override
        public void clauseEnd(VisitContext context) {

            // Operating on RenderContext only, as we're using inline values
            if (context.renderContext() == null)
                return;

            if (context.clause() == SELECT_WHERE ||
                context.clause() == UPDATE_WHERE ||
                context.clause() == DELETE_WHERE) {

                if (!anyPredicates(context)) {
                    throw new MissingWhereException();
                }
            }
        }
    }

    private static class MissingWhereException extends RuntimeException {

    }

    /**
     * This sample visit listener restricts <code>T_AUTHOR.ID = 1</code> and
     * <code>T_BOOK.AUTHOR_ID
     * = 1</code>. It can do so in
     * <ul>
     * <li>Top-level selects</li>
     * <li>Subselects (from set operations, such as unions)</li>
     * <li>Nested selects</li>
     * <li>Derived tables</li>
     * <li>Aliased tables</li>
     * </ul>
     */
    private class OnlyAuthorIDEqual1 extends NestingVisitListener {

        @Override
        public void clauseEnd(VisitContext context) {

            // Operating on RenderContext only, as we're using inline values
            if (context.renderContext() == null)
                return;

            // Append all collected predicates to the WHERE clause if any
            if (context.clause() == SELECT_WHERE ||
                context.clause() == UPDATE_WHERE ||
                context.clause() == DELETE_WHERE) {
                List<Condition> conditions = subselectConditions(context);

                if (conditions.size() > 0) {
                    context.renderContext()
                           .formatSeparator()
                           .keyword(anyPredicates(context) ? "and" : "where")
                           .sql(" ");

                    Condition condition = trueCondition();
                    for (Condition c : conditions)
                        condition = condition.and(c);

                    // Force bind variable inlining
                    ParamType previous = context.renderContext().paramType();
                    context.renderContext()
                           .paramType(ParamType.INLINED)
                           .visit(condition)
                           .paramType(previous);
                }
            }

            super.clauseEnd(context);
        }

        @Override
        public void visitStart(VisitContext context) {

            // Operating on RenderContext only, as we're using inline values
            if (context.renderContext() == null)
                return;

            /* [pro] xx
            xx xxx xxxxxx xxxxx xxxxxxx xx xxxxxx xxxxxxxxxxx xx xxxxxxxxxx
            xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxx x
                xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxx xxxxxxxxxxxxxxxxxx xxx
                xxxxxxxxxxxxxxxxxxxxxxxxx xxxxxxxxxx xxxxxxxxxxxxx xxx
            x
            xx [/pro] */
        }

        @Override
        public void visitEnd(VisitContext context) {

            // Operating on RenderContext only, as we're using inline values
            if (context.renderContext() == null)
                return;

            // Push conditions for BOOK and AUTHOR tables, if applicable
            pushConditions(context, TBook(), TBook_AUTHOR_ID(), 1);
            pushConditions(context, TAuthor(), TAuthor_ID(), 1);

            super.visitEnd(context);
        }

        /* [pro] xx
        xxxxxxx xxx xxxx xxxxxxxxxxxxxxxxx
                xxxxx xxxxxxxxxxxx xxxxxxxx
                xxxxx xxxxxxxx xxxxxx
                xxxxx xxxxxxxx xxxxxx
                xxxxx xxxx xxxxxxx
        x
            xx xxxxxxxxxxxxxxxxxxxx xx xxxxxx x

                xx xxx xxxxxx x xxx xxxxxx xxxx xxxxxx
                xxxxxxxxxxxx xxxxxxx x xxxxxxxxxxxxxxxxxxxxxxxxxx
                xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

                    xx xxx xxxxx xxxxxxxxxx
                    xx xxxxxxxxxxxxxxxxxxxxxxxxxx x

                    xx xxx xxxxx xxxxxxx xxx xxxxx xx xx xxxxxxxxxx
                    xx xxxx xxxx x xxxxx xxxxxx xxxxxx
                    xxxxxxxxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxx x
                        xxxxxxxxx
                        xxxxxx xxxx xxxxxxxxxxxxxxxxxxx xxxx x
                            xxxxxxxxx xxxxxxxx x xxxxxxxxxxxxxxxx
                            xxxxxxxxxxxxxxxxxxxxxx
                               xxxxxxxxxxxxxxxxx
                                   xxxxxxxx x xxxx xxx xxxxx xxx xxxx xxxxx xxxxxxxxx
                                   xxxxxx
                                   xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                xx
                               xxxxxxxxxxxxxxxxxxxxx
                        x
                    xxx
                x
            x
        x

        xx [/pro] */
        private <E> void pushConditions(VisitContext context, Table<?> table, Field<E> field, E... values) {

            // Check if we're visiting the given table
            if (context.queryPart() == table) {
                List<Clause> clauses = subselectClauses(context);

                // ... and if we're in the context of the current subselect's
                // FROM clause
                if (clauses.contains(SELECT_FROM) ||
                    clauses.contains(UPDATE_UPDATE) ||
                    clauses.contains(DELETE_DELETE)) {

                    // If we're declaring a TABLE_ALIAS... (e.g. "T_BOOK" as "b")
                    if (clauses.contains(TABLE_ALIAS)) {
                        QueryPart[] parts = context.queryParts();

                        // ... move up the QueryPart visit path to find the
                        // defining aliased table, and extract the aliased
                        // field from it. (i.e. the "b" reference)
                        for (int i = parts.length - 2; i >= 0; i--) {
                            if (parts[i] instanceof Table) {
                                field = ((Table<?>) parts[i]).field(field);
                                break;
                            }
                        }
                    }

                    // Push a condition for the field of the (potentially aliased) table
                    subselectConditions(context).add(field.in(values).or(field.isNull()));
                }
            }
        }
    }
}

