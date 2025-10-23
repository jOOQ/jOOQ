/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.impl;

import static java.util.Collections.emptyList;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.Keywords.K_DENSE_RANK;
import static org.jooq.impl.Keywords.K_DISTINCT;
import static org.jooq.impl.Keywords.K_FILTER;
import static org.jooq.impl.Keywords.K_FIRST;
import static org.jooq.impl.Keywords.K_KEEP;
import static org.jooq.impl.Keywords.K_LAST;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_ORDER_BY;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.Keywords.K_WITHIN_GROUP;
import static org.jooq.impl.Names.N_COUNT;
import static org.jooq.impl.Names.N_COUNTIF;
import static org.jooq.impl.Names.N_COUNT_IF;
import static org.jooq.impl.QueryPartCollectionView.wrap;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.NUMERIC;
import static org.jooq.impl.Tools.EMPTY_QUERYPART;
import static org.jooq.impl.Tools.camelCase;
import static org.jooq.impl.Tools.isEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.jooq.AggregateFilterStep;
import org.jooq.AggregateFunction;
import org.jooq.ArrayAggOrderByStep;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.OptionallyOrderedAggregateFunction;
import org.jooq.OrderField;
import org.jooq.OrderedAggregateFunction;
// ...
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.SortField;
// ...
import org.jooq.WindowBeforeOverStep;
import org.jooq.impl.QOM.FrameUnits;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
abstract class AbstractAggregateFunction<T, Q extends QOM.AggregateFunction<T, Q>>
extends
    AbstractWindowFunction<T, Q>
implements
    OptionallyOrderedAggregateFunction<T>,
    ArrayAggOrderByStep<T>,
    QOM.AggregateFunction<T, Q>
{





    static final Set<SQLDialect>      NO_SUPPORT_FILTER                 = SQLDialect.supportedUntil(CUBRID, DERBY, IGNITE, MARIADB, MYSQL);
    static final Set<SQLDialect>      NO_SUPPORT_WINDOW_FILTER          = SQLDialect.supportedBy(TRINO);
    static final Set<SQLDialect>      REQUIRE_DISTINCT_RVE              = SQLDialect.supportedBy(DUCKDB, H2, POSTGRES);
    static final Set<SQLDialect>      EMULATE_WINDOW_AGGREGATE_ORDER_BY = SQLDialect.supportedBy(POSTGRES, SQLITE, YUGABYTEDB);

    static final Lazy<Field<Integer>> ASTERISK                          = Lazy.of(() -> DSL.field(DSL.raw("*"), Integer.class));

    // Other attributes
    final QueryPartList<Field<?>>     arguments;
    final boolean                     distinct;
    final ConditionProviderImpl       filter;

    // Other attributes
    SortFieldList                     withinGroupOrderBy;
    SortFieldList                     keepDenseRankOrderBy;
    boolean                           first;


    AbstractAggregateFunction(String name, DataType<T> type, Field<?>... arguments) {
        this(false, name, type, arguments);
    }

    AbstractAggregateFunction(Name name, DataType<T> type, Field<?>... arguments) {
        this(false, name, type, arguments);
    }

    AbstractAggregateFunction(boolean distinct, String name, DataType<T> type, Field<?>... arguments) {
        this(distinct, DSL.unquotedName(name), type, arguments);
    }

    AbstractAggregateFunction(boolean distinct, Name name, DataType<T> type, Field<?>... arguments) {
        this(distinct, name, type, Arrays.asList(arguments));
    }

    AbstractAggregateFunction(boolean distinct, String name, DataType<T> type, Collection<? extends Field<?>> arguments) {
        this(distinct, DSL.unquotedName(name), type, arguments);
    }

    AbstractAggregateFunction(boolean distinct, Name name, DataType<T> type, Collection<? extends Field<?>> arguments) {
        super(name, type);

        this.distinct = distinct;
        this.arguments = new QueryPartList<>(arguments);
        this.filter = new ConditionProviderImpl();
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public /* final */ void accept(Context<?> ctx) {
        toSQLArguments(ctx);
        acceptKeepDenseRankOrderByClause(ctx);
        acceptWithinGroupClause(ctx);
        acceptFilterClause(ctx);
        acceptOverClause(ctx);
    }

    /**
     * Render <code>KEEP (DENSE_RANK [FIRST | LAST] ORDER BY {…})</code> clause
     */
    final void acceptKeepDenseRankOrderByClause(Context<?> ctx) {
        if (!Tools.isEmpty(keepDenseRankOrderBy)) {

            switch (ctx.family()) {






                default:
                    ctx.sql(' ').visit(K_KEEP)
                       .sql(" (").visit(K_DENSE_RANK)
                       .sql(' ').visit(first ? K_FIRST : K_LAST)
                       .sql(' ').visit(K_ORDER_BY)
                       .sql(' ').visit(keepDenseRankOrderBy)
                       .sql(')');
                    break;
            }
        }
    }

    /**
     * Render <code>WITHIN GROUP (ORDER BY …)</code> clause
     */
    final void acceptWithinGroupClause(Context<?> ctx) {
        if (withinGroupOrderBy != null) {
            switch (ctx.family()) {






                default:





                    ctx.sql(' ').visit(K_WITHIN_GROUP)
                       .sql(" (").visit(K_ORDER_BY).sql(' ');

                    if (withinGroupOrderBy.isEmpty())
                        ctx.visit(K_NULL);
                    else if (filter.hasWhere() && !supportsFilter(ctx) && applyFilterToWithinGroup(ctx))
                        ctx.visit(wrap(withinGroupOrderBy).map((arg, i) -> DSL.when(filter, arg.$field()).sort(arg.$sortOrder())));
                    else
                        ctx.visit(withinGroupOrderBy);

                    ctx.sql(')');
                    break;
            }
        }
    }

    /**
     * Render function arguments and argument modifiers
     */
    final void toSQLArguments(Context<?> ctx) {
        acceptFunctionName(ctx);
        ctx.sql('(');
        acceptArguments0(ctx);
        ctx.sql(')');
    }

    /* non-final */ void acceptFunctionName(Context<?> ctx) {



















        AbstractFunction.acceptFunctionName(ctx, true, getQualifiedName());
    }

    final void acceptArguments0(Context<?> ctx) {





        acceptArguments1(ctx, arguments);
    }

    final void acceptArguments1(Context<?> ctx, QueryPartCollectionView<Field<?>> args) {
        boolean parens = false;
        if (distinct) {
            ctx.visit(K_DISTINCT).sql(' ');

            // [#2883][#9109] PostgreSQL and H2 can use the DISTINCT keyword with formal row value expressions.
            // [#13415] ListAgg is a special case, where the second argument is the separator
            if (parens |= (args.size() > 1 && REQUIRE_DISTINCT_RVE.contains(ctx.dialect()) && !(this instanceof ListAgg) && !(this instanceof BinaryListAgg)))
                ctx.sql('(');
        }

        acceptArguments2(ctx, args);

        if (parens)
            ctx.sql(')');
    }

    final void acceptArguments2(Context<?> ctx, QueryPartCollectionView<Field<?>> args) {
        acceptArguments3(ctx, args, f -> applyMap(ctx, f));
    }

    final void acceptArguments3(Context<?> ctx, QueryPartCollectionView<Field<?>> args, Function<? super Field<?>, ? extends Field<?>> fun) {
        if (args.isEmpty() && this instanceof Count)

            // [#7539] Work around https://github.com/ClickHouse/ClickHouse/issues/61004
            if (ctx.family() == CLICKHOUSE && filter.hasWhere())
                args = QueryPartListView.wrap();
            else
                args = QueryPartListView.wrap(ASTERISK.get());

        if (!filter.hasWhere() || supportsFilter(ctx))
            ctx.visit(wrap(args).map(fun));




        else
            ctx.visit(wrap(args).map((arg, i) -> applyFilterToArgument(ctx, arg, i) ? DSL.when(filter, arg == ASTERISK.get() ? one() : arg) : arg).map(fun));
    }

    /* non-final */ Field<?> applyMap(Context<?> ctx, Field<?> arg) {
        return arg;
    }

    /* non-final */ boolean applyFilterToArgument(Context<?> ctx, Field<?> arg, int i) {
        return true;
    }

    /* non-final */ boolean applyFilterToWithinGroup(Context<?> ctx) {
        return false;
    }

    final boolean emulateWindowAggregateOrderBy(Context<?> ctx) {
        return EMULATE_WINDOW_AGGREGATE_ORDER_BY.contains(ctx.dialect())
            && isWindow()
            && !isOrderedWindow(ctx)
            && !isEmpty(withinGroupOrderBy);
    }

    final void acceptWindowAggregateOrderByEmulation(Context<?> ctx) {

        // [#19255] TODO: Make sure this works also with WindowDefinition and Window name
        ctx.visit(
            $withinGroupOrderBy(emptyList()).$windowSpecification(
                $windowSpecification()
                    .$orderBy(withinGroupOrderBy)
                    .$frameStart(Integer.MIN_VALUE)
                    .$frameEnd(Integer.MAX_VALUE)
                    .$frameUnits(FrameUnits.RANGE)
            )
        );
    }













    final void acceptFilterClause(Context<?> ctx) {
        if (filter.hasWhere())
            acceptFilterClause(ctx, filter);
    }

    final void acceptFilterClause(Context<?> ctx, Condition f) {
        switch (ctx.family()) {






            default:
                if (supportsFilter(ctx))
                    ctx.sql(' ')
                       .visit(K_FILTER)
                       .sql(" (")
                       .visit(K_WHERE)
                       .sql(' ')
                       .visit(f)
                       .sql(')');
                break;
        }
    }

    /* non-final */ boolean supportsFilter(Context<?> ctx) {
        return !(
             NO_SUPPORT_FILTER.contains(ctx.dialect())
          || NO_SUPPORT_WINDOW_FILTER.contains(ctx.dialect()) && isWindow()
        );
    }

    final void acceptOrderBy(Context<?> ctx) {
        acceptOrderBy(ctx, withinGroupOrderBy);
    }

    static final void acceptOrderBy(Context<?> ctx, SortFieldList orderBy) {
        if (!Tools.isEmpty(orderBy)) {
            switch (ctx.family()) {






                default:
                    ctx.sql(' ').visit(K_ORDER_BY).sql(' ').visit(orderBy);
                    break;
            }
        }
    }

    // -------------------------------------------------------------------------
    // XXX Aggregate function API
    // -------------------------------------------------------------------------

    final Field<?> getArgument(int index) {
        return index < arguments.size() ? arguments.get(index) : null;
    }

    final QueryPartList<Field<?>> getArguments() {
        return arguments;
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(Condition c) {
        filter.addConditions(c);
        return this;
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(Condition... conditions) {
        return filterWhere(DSL.and(conditions));
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(Collection<? extends Condition> conditions) {
        return filterWhere(DSL.and(conditions));
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(Field<Boolean> field) {
        return filterWhere(condition(field));
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(SQL sql) {
        return filterWhere(condition(sql));
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(String sql) {
        return filterWhere(condition(sql));
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(String sql, Object... bindings) {
        return filterWhere(condition(sql, bindings));
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(String sql, QueryPart... parts) {
        return filterWhere(condition(sql, parts));
    }


    @Override
    public final AggregateFunction<T> withinGroupOrderBy(OrderField<?>... fields) {
        return withinGroupOrderBy(Arrays.asList(fields));
    }

    @Override
    public final AggregateFunction<T> withinGroupOrderBy(Collection<? extends OrderField<?>> fields) {
        if (withinGroupOrderBy == null)
            withinGroupOrderBy = new SortFieldList();

        withinGroupOrderBy.addAll(Tools.sortFields(fields));
        return this;
    }





































    @SuppressWarnings("unchecked")
    @Override
    public /* non-final */ AbstractAggregateFunction<T, Q> orderBy(OrderField<?>... fields) {
        if (windowSpecification != null)
            super.orderBy(fields);
        else
            withinGroupOrderBy(fields);

        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public /* non-final */ AbstractAggregateFunction<T, Q> orderBy(Collection<? extends OrderField<?>> fields) {
        if (windowSpecification != null)
            windowSpecification.orderBy(fields);
        else
            withinGroupOrderBy(fields);

        return this;
    }

    final Condition f(Condition c) {
        return filter.hasWhere() ? filter.and(c) : c;
    }

    @SuppressWarnings("unchecked")
    final <U> Field<U> fon(AggregateFunction<U> function) {
        return DSL.nullif(fo(function), (Field<U>) zero());
    }

    /**
     * Apply this aggregate function's <code>ORDER BY</code>,
     * <code>FILTER</code> and <code>OVER</code> clauses to an argument
     * aggregate function.
     */
    final <U> Field<U> ofo(AbstractAggregateFunction<U, ?> function) {
        return fo(isEmpty(withinGroupOrderBy) ? function : function.orderBy(withinGroupOrderBy));
    }

    /**
     * Apply this aggregate function's <code>FILTER</code> and <code>OVER</code>
     * clauses to an argument aggregate function.
     */
    final <U> Field<U> fo(AggregateFilterStep<U> function) {
        return o(filter.hasWhere() ? function.filterWhere(filter) : function);
    }

    /**
     * Apply this aggregate function's <code>FILTER</code> and <code>OVER</code>
     * clauses to an argument aggregate function.
     */
    final <U> Field<U> fo(AggregateFilterStep<U> function, Condition condition) {
        return o(function.filterWhere(f(condition)));
    }

    /**
     * Apply this aggregate function's <code>FILTER</code> and <code>OVER</code>
     * clauses to an argument aggregate function.
     */
    final <U> Field<U> o(WindowBeforeOverStep<U> function) {
        if (windowSpecification != null)
            return function.over(windowSpecification);
        else if (windowDefinition != null)
            return function.over(windowDefinition);
        else if (windowName != null)
            return function.over(windowName);
        else
            return function;
    }

    /**
     * Type safe <code>NVL2(y, x, null)</code> for statistical function
     * emulations.
     */
    final <U extends Number> Field<U> x(Field<U> x, Field<? extends Number> y) {
        return DSL.nvl2(y, x, DSL.inline(null, x.getDataType()));
    }

    /**
     * Type safe <code>NVL2(x, y, null)</code> for statistical function
     * emulations.
     */
    final <U extends Number> Field<U> y(Field<? extends Number> x, Field<U> y) {
        return DSL.nvl2(x, y, DSL.inline(null, y.getDataType()));
    }

    /**
     * The data type to use in casts when emulating statistical functions.
     */
    final DataType<? extends Number> d(Context<?> ctx) {
        switch (ctx.family()) {

            // [#11547] These families default to NUMERIC(*, 0) when a scale is
            //          not provided explicitly, hence resort to using floats
            case DERBY:
            case FIREBIRD:
            case HSQLDB:
            case SQLITE:
            case TRINO:
                return DOUBLE;

            default:
                return NUMERIC;
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    final Q copyWindowFunction(Function<? super Q, ? extends Q> function) {
        return function.apply(copyAggregateFunction(copyAggregateSpecification()));
    }

    final Function<? super Q, ? extends Q> copyAggregateSpecification() {
        return copyWindowSpecification().andThen(c -> {
            AbstractAggregateFunction<?, ?> q = (AbstractAggregateFunction<?, ?>) c;

            // [#19255] Arguments can be expected to have already been copied by the copyAggregateFunction() utility, in subclasses.
            if (filter.hasWhere())
                q.filter.addConditions(filter.getWhere());
            if (!isEmpty(withinGroupOrderBy))
                q.withinGroupOrderBy = new SortFieldList(withinGroupOrderBy);
            if (!isEmpty(keepDenseRankOrderBy))
                q.keepDenseRankOrderBy = new SortFieldList(keepDenseRankOrderBy);
            q.first = first;

            return c;
        });
    }

    abstract Q copyAggregateFunction(Function<? super Q, ? extends Q> function);

    public final boolean $distinct() {
        return distinct;
    }

    @Override
    public final Condition $filterWhere() {
        return filter.getWhereOrNull();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Q $filterWhere(Condition condition) {
        if ($filterWhere() == condition)
            return (Q) this;
        else
            return copy(c -> {
                AbstractAggregateFunction<?, ?> q = (AbstractAggregateFunction<?, ?>) c;

                q.filter.setWhere(condition);
            });
    }

    // [#19255] [#19260] TODO: Push down to new subclass, perhaps?

    public final UnmodifiableList<? extends SortField<?>> $withinGroupOrderBy() {
        return QOM.unmodifiable(withinGroupOrderBy == null ? QueryPartList.emptyList() : withinGroupOrderBy);
    }

    @SuppressWarnings("unchecked")
    public final Q $withinGroupOrderBy(Collection<? extends SortField<?>> newOrderBy) {
        if (withinGroupOrderBy == newOrderBy)
            return (Q) this;
        else
            return copy(c -> {
                AbstractAggregateFunction<?, ?> q = (AbstractAggregateFunction<?, ?>) c;

                q.withinGroupOrderBy = null;

                if (!isEmpty(newOrderBy))
                    q.withinGroupOrderBy = new SortFieldList(newOrderBy);
            });
    }





















}
