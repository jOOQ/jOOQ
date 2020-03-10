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
 *
 *
 *
 */

package org.jooq.impl;

// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.mode;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.percentileCont;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.DSL.zero;
import static org.jooq.impl.Keywords.F_CONCAT;
import static org.jooq.impl.Keywords.F_SUBSTR;
import static org.jooq.impl.Keywords.F_XMLAGG;
import static org.jooq.impl.Keywords.F_XMLSERIALIZE;
import static org.jooq.impl.Keywords.F_XMLTEXT;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_DENSE_RANK;
import static org.jooq.impl.Keywords.K_DISTINCT;
import static org.jooq.impl.Keywords.K_FILTER;
import static org.jooq.impl.Keywords.K_FIRST;
import static org.jooq.impl.Keywords.K_KEEP;
import static org.jooq.impl.Keywords.K_LAST;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_ORDER_BY;
import static org.jooq.impl.Keywords.K_SEPARATOR;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.Keywords.K_WITHIN_GROUP;
import static org.jooq.impl.SQLDataType.NUMERIC;
import static org.jooq.impl.Term.ARRAY_AGG;
import static org.jooq.impl.Term.LIST_AGG;
import static org.jooq.impl.Term.MEDIAN;
import static org.jooq.impl.Term.MODE;
import static org.jooq.impl.Term.PRODUCT;
import static org.jooq.impl.Tools.castIfNeeded;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.jooq.AggregateFilterStep;
import org.jooq.AggregateFunction;
import org.jooq.ArrayAggOrderByStep;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.OrderedAggregateFunction;
// ...
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.SQLDialect;
import org.jooq.WindowBeforeOverStep;
// ...

/**
 * A field that handles built-in functions, aggregate functions, and window
 * functions.
 *
 * @author Lukas Eder
 */
class DefaultAggregateFunction<T> extends AbstractWindowFunction<T> implements

    // Cascading interface implementations for aggregate function behaviour
    OrderedAggregateFunction<T>,
    ArrayAggOrderByStep<T>,
    AggregateFunction<T> {


    private static final long             serialVersionUID             = 347252741712134044L;
    private static final Set<SQLDialect>  SUPPORT_ARRAY_AGG            = SQLDialect.supportedBy(HSQLDB, POSTGRES);
    private static final Set<SQLDialect>  SUPPORT_GROUP_CONCAT         = SQLDialect.supportedBy(CUBRID, H2, HSQLDB, MARIADB, MYSQL, SQLITE);
    private static final Set<SQLDialect>  SUPPORT_STRING_AGG           = SQLDialect.supportedBy(POSTGRES);
    private static final Set<SQLDialect>  SUPPORT_FILTER               = SQLDialect.supportedBy(H2, HSQLDB, POSTGRES, SQLITE);
    private static final Set<SQLDialect>  SUPPORT_DISTINCT_RVE         = SQLDialect.supportedBy(H2, POSTGRES);





    static final Field<Integer>           ASTERISK                     = DSL.field("*", Integer.class);

    // Mutually exclusive attributes: super.getName(), this.term
    private final Term                    term;

    // Other attributes
    private final QueryPartList<Field<?>> arguments;
    private final boolean                 distinct;
    private SortFieldList                 withinGroupOrderBy;
    private SortFieldList                 keepDenseRankOrderBy;
    private Condition                     filter;
    private boolean                       first;

    // -------------------------------------------------------------------------
    // XXX Constructors
    // -------------------------------------------------------------------------

    DefaultAggregateFunction(String name, DataType<T> type, Field<?>... arguments) {
        this(name, false, type, arguments);
    }

    DefaultAggregateFunction(Term term, DataType<T> type, Field<?>... arguments) {
        this(term, false, type, arguments);
    }

    DefaultAggregateFunction(String name, boolean distinct, DataType<T> type, Field<?>... arguments) {
        super(DSL.name(name), type);

        this.term = null;
        this.distinct = distinct;
        this.arguments = new QueryPartList<>(arguments);
    }

    DefaultAggregateFunction(Term term, boolean distinct, DataType<T> type, Field<?>... arguments) {
        super(term.toName(), type);

        this.term = term;
        this.distinct = distinct;
        this.arguments = new QueryPartList<>(arguments);
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public /* final */ void accept(Context<?> ctx) {
        if (term == ARRAY_AGG && SUPPORT_ARRAY_AGG.contains(ctx.dialect())) {
            toSQLGroupConcat(ctx);
            toSQLFilterClause(ctx);
            acceptOverClause(ctx);
        }
        else if (term == LIST_AGG && SUPPORT_GROUP_CONCAT.contains(ctx.dialect())) {
            toSQLGroupConcat(ctx);
        }
        else if (term == LIST_AGG && SUPPORT_STRING_AGG  .contains(ctx.dialect())) {
            toSQLStringAgg(ctx);
            toSQLFilterClause(ctx);
            acceptOverClause(ctx);
        }





        else if (term == MODE && ( ctx.family() == H2 || ctx.family() == POSTGRES)) {
            ctx.visit(mode().withinGroupOrderBy(DSL.field("{0}", arguments.get(0))));
        }
        else if (term == MEDIAN && ( ctx.family() == POSTGRES)) {
            Field<?>[] fields = new Field[arguments.size()];
            for (int i = 0; i < fields.length; i++)
                fields[i] = DSL.field("{0}", arguments.get(i));

            ctx.visit(percentileCont(new BigDecimal("0.5")).withinGroupOrderBy(fields));
        }
        else if (term == PRODUCT) {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            final Field<Integer> f = (Field) DSL.field("{0}", arguments.get(0).getDataType(), arguments.get(0));
            final Field<Integer> negatives = DSL.when(f.lt(zero()), inline(-1));

            @SuppressWarnings("serial")
            Field<BigDecimal> negativesSum = new CustomField<BigDecimal>("sum", NUMERIC) {
                @Override
                public void accept(Context<?> c) {
                    c.visit(distinct
                        ? DSL.sumDistinct(negatives)
                        : DSL.sum(negatives));

                    toSQLFilterClause(c);
                    acceptOverClause(c);
                }
            };

            @SuppressWarnings("serial")
            Field<BigDecimal> zerosSum = new CustomField<BigDecimal>("sum", NUMERIC) {
                @Override
                public void accept(Context<?> c) {
                    c.visit(DSL.sum(choose(f).when(zero(), one())));

                    toSQLFilterClause(c);
                    acceptOverClause(c);
                }
            };

            @SuppressWarnings("serial")
            Field<BigDecimal> logarithmsSum = new CustomField<BigDecimal>("sum", NUMERIC) {
                @Override
                public void accept(Context<?> c) {
                    Field<Integer> abs = DSL.abs(DSL.nullif(f, zero()));
                    Field<BigDecimal> ln =





                        DSL.ln(abs);

                    c.visit(distinct
                        ? DSL.sumDistinct(ln)
                        : DSL.sum(ln));

                    toSQLFilterClause(c);
                    acceptOverClause(c);
                }
            };

            ctx.visit(
                when(zerosSum.gt(inline(BigDecimal.ZERO)), zero())
               .when(negativesSum.mod(inline(2)).lt(inline(BigDecimal.ZERO)), inline(-1))
               .otherwise(one()).mul(DSL.exp(logarithmsSum))
            );
        }
        else {
            toSQLArguments(ctx);
            toSQLKeepDenseRankOrderByClause(ctx);
            toSQLWithinGroupClause(ctx);
            toSQLFilterClause(ctx);
            acceptOverClause(ctx);
        }
    }















































    /**
     * [#1275] <code>LIST_AGG</code> emulation for Postgres, Sybase
     */
    final void toSQLStringAgg(Context<?> ctx) {
        toSQLFunctionName(ctx);
        ctx.sql('(');

        if (distinct)
            ctx.visit(K_DISTINCT).sql(' ');

        // The explicit cast is needed in Postgres
        ctx.visit(castIfNeeded((Field<?>) arguments.get(0), String.class));

        if (arguments.size() > 1)
            ctx.sql(", ").visit(arguments.get(1));
        else
            ctx.sql(", ''");

        if (!Tools.isEmpty(withinGroupOrderBy))
            ctx.sql(' ').visit(K_ORDER_BY).sql(' ')
               .visit(withinGroupOrderBy);

        ctx.sql(')');
    }

    /**
     * [#1273] <code>LIST_AGG</code> emulation for MySQL
     */
    final void toSQLGroupConcat(Context<?> ctx) {
        toSQLFunctionName(ctx);
        ctx.sql('(');
        toSQLArguments1(ctx, new QueryPartList<>(Arrays.asList(arguments.get(0))));

        if (!Tools.isEmpty(withinGroupOrderBy))
            ctx.sql(' ').visit(K_ORDER_BY).sql(' ')
               .visit(withinGroupOrderBy);

        if (arguments.size() > 1)
            if (ctx.family() == SQLITE)
                ctx.sql(", ").visit(arguments.get(1));
            else
                ctx.sql(' ').visit(K_SEPARATOR).sql(' ')
                   .visit(arguments.get(1));

        ctx.sql(')');
    }

    final void toSQLFilterClause(Context<?> ctx) {
        if (filter != null && SUPPORT_FILTER.contains(ctx.dialect()))
            ctx.sql(' ')
               .visit(K_FILTER)
               .sql(" (")
               .visit(K_WHERE)
               .sql(' ')
               .visit(filter)
               .sql(')');
    }

    /**
     * Render <code>KEEP (DENSE_RANK [FIRST | LAST] ORDER BY {...})</code> clause
     */
    final void toSQLKeepDenseRankOrderByClause(Context<?> ctx) {
        if (!Tools.isEmpty(keepDenseRankOrderBy)) {
            ctx.sql(' ').visit(K_KEEP)
               .sql(" (").visit(K_DENSE_RANK)
               .sql(' ').visit(first ? K_FIRST : K_LAST)
               .sql(' ').visit(K_ORDER_BY)
               .sql(' ').visit(keepDenseRankOrderBy)
               .sql(')');
        }
    }

    /**
     * Render <code>WITHIN GROUP (ORDER BY ..)</code> clause
     */
    final void toSQLWithinGroupClause(Context<?> ctx) {
        if (withinGroupOrderBy != null) {
            ctx.sql(' ').visit(K_WITHIN_GROUP)
               .sql(" (").visit(K_ORDER_BY).sql(' ');

            if (withinGroupOrderBy.isEmpty())
                ctx.visit(K_NULL);
            else
                ctx.visit(withinGroupOrderBy);

            ctx.sql(')');
        }
    }

    /**
     * Render function arguments and argument modifiers
     */
    final void toSQLArguments(Context<?> ctx) {
        toSQLFunctionName(ctx);
        ctx.sql('(');
        toSQLArguments0(ctx);
        ctx.sql(')');
    }

    final void toSQLArguments0(Context<?> ctx) {
        toSQLArguments1(ctx, arguments);
    }

    final void toSQLArguments1(Context<?> ctx, QueryPartList<Field<?>> args) {
        if (distinct) {
            ctx.visit(K_DISTINCT).sql(' ');

            // [#2883][#9109] PostgreSQL and H2 can use the DISTINCT keyword with formal row value expressions.
            if (args.size() > 1 && SUPPORT_DISTINCT_RVE.contains(ctx.family()))
                ctx.sql('(');
        }

        if (!args.isEmpty()) {
            if (filter == null || SUPPORT_FILTER.contains(ctx.dialect())) {
                ctx.visit(args);
            }
            else {
                QueryPartList<Field<?>> expressions = new QueryPartList<>();

                for (Field<?> argument : args)
                    expressions.add(DSL.when(filter, argument == ASTERISK ? one() : argument));

                ctx.visit(expressions);
            }
        }

        if (distinct)
            if (args.size() > 1 && SUPPORT_DISTINCT_RVE.contains(ctx.family()))
                ctx.sql(')');
    }

    final void toSQLFunctionName(Context<?> ctx) {
        if (term != null)
            ctx.sql(term.translate(ctx.dialect()));
        else
            ctx.sql(getName());
    }

    // -------------------------------------------------------------------------
    // XXX aggregate and window function fluent API methods
    // -------------------------------------------------------------------------

    final QueryPartList<Field<?>> getArguments() {
        return arguments;
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





































    @Override
    public final WindowBeforeOverStep<T> filterWhere(Condition c) {
        filter = c;
        return this;
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(Condition... conditions) {
        return filterWhere(Arrays.asList(conditions));
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(Collection<? extends Condition> conditions) {
        ConditionProviderImpl c = new ConditionProviderImpl();
        c.addConditions(conditions);
        return filterWhere(c);
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(Field<Boolean> field) {
        return filterWhere(condition(field));
    }

    @Override
    public final WindowBeforeOverStep<T> filterWhere(Boolean field) {
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
    public final DefaultAggregateFunction<T> orderBy(OrderField<?>... fields) {
        if (windowSpecification != null)
            windowSpecification.orderBy(fields);
        else
            withinGroupOrderBy(fields);

        return this;
    }

    @Override
    public final DefaultAggregateFunction<T> orderBy(Collection<? extends OrderField<?>> fields) {
        if (windowSpecification != null)
            windowSpecification.orderBy(fields);
        else
            withinGroupOrderBy(fields);

        return this;
    }
}
