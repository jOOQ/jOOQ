/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.Term.LIST_AGG;
import static org.jooq.impl.Term.ROW_NUMBER;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.AggregateFunction;
import org.jooq.BindContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.OrderedAggregateFunction;
import org.jooq.QueryPart;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.SortField;
import org.jooq.WindowBeforeOverStep;
import org.jooq.WindowFinalStep;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowOrderByStep;
import org.jooq.WindowOverStep;
import org.jooq.WindowPartitionByStep;
import org.jooq.WindowRowsAndStep;
import org.jooq.WindowRowsStep;
import org.jooq.util.db2.DB2DataType;

/**
 * A field that handles built-in functions, aggregate functions, and window
 * functions.
 *
 * @author Lukas Eder
 */
class Function<T> extends AbstractField<T> implements

    // Cascading interface implementations for aggregate function behaviour
    OrderedAggregateFunction<T>,
    AggregateFunction<T>,
    WindowBeforeOverStep<T>,

    // and for window function behaviour
    WindowIgnoreNullsStep<T>,
    WindowPartitionByStep<T>,
    WindowRowsStep<T>,
    WindowRowsAndStep<T>
    {

    private static final long              serialVersionUID = 347252741712134044L;

    // Mutually exclusive attributes: super.getName(), this.name, this.term
    private final Name                     name;
    private final Term                     term;

    // Other attributes
    private final QueryPartList<QueryPart> arguments;
    private final boolean                  distinct;
    private final SortFieldList            withinGroupOrderBy;
    private final SortFieldList            keepDenseRankOrderBy;
    private final QueryPartList<Field<?>>  partitionBy;
    private final SortFieldList            orderBy;

    private boolean                        first;
    private boolean                        over;
    private boolean                        partitionByOne;
    private boolean                        ignoreNulls;
    private boolean                        respectNulls;
    private Integer                        rowsStart;
    private Integer                        rowsEnd;

    // -------------------------------------------------------------------------
    // XXX Constructors
    // -------------------------------------------------------------------------

    Function(String name, DataType<T> type, QueryPart... arguments) {
        this(name, false, type, arguments);
    }

    Function(Term term, DataType<T> type, QueryPart... arguments) {
        this(term, false, type, arguments);
    }

    Function(Name name, DataType<T> type, QueryPart... arguments) {
        this(name, false, type, arguments);
    }

    Function(String name, boolean distinct, DataType<T> type, QueryPart... arguments) {
        super(name, type);

        this.term = null;
        this.name = null;
        this.distinct = distinct;
        this.arguments = new QueryPartList<QueryPart>(arguments);
        this.keepDenseRankOrderBy = new SortFieldList();
        this.withinGroupOrderBy = new SortFieldList();
        this.partitionBy = new QueryPartList<Field<?>>();
        this.orderBy = new SortFieldList();
    }

    Function(Term term, boolean distinct, DataType<T> type, QueryPart... arguments) {
        super(term.name().toLowerCase(), type);

        this.term = term;
        this.name = null;
        this.distinct = distinct;
        this.arguments = new QueryPartList<QueryPart>(arguments);
        this.keepDenseRankOrderBy = new SortFieldList();
        this.withinGroupOrderBy = new SortFieldList();
        this.partitionBy = new QueryPartList<Field<?>>();
        this.orderBy = new SortFieldList();
    }

    Function(Name name, boolean distinct, DataType<T> type, QueryPart... arguments) {
        super(last(name.getName()), type);

        this.term = null;
        this.name = name;
        this.distinct = distinct;
        this.arguments = new QueryPartList<QueryPart>(arguments);
        this.keepDenseRankOrderBy = new SortFieldList();
        this.withinGroupOrderBy = new SortFieldList();
        this.partitionBy = new QueryPartList<Field<?>>();
        this.orderBy = new SortFieldList();
    }

    private static String last(String... strings) {
        if (strings != null && strings.length > 0) {
            return strings[strings.length - 1];
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void bind(BindContext context) {

        if (term == LIST_AGG && asList(CUBRID, H2, HSQLDB, MARIADB, MYSQL).contains(context.configuration().dialect())) {
            context.bind(arguments.get(0));
            context.bind((QueryPart) withinGroupOrderBy);

            if (arguments.size() > 1) {
                context.bind(arguments.get(1));
            }
        }
        else {
            context.bind((QueryPart) arguments)
                   .bind((QueryPart) keepDenseRankOrderBy)
                   .bind((QueryPart) withinGroupOrderBy)
                   .bind((QueryPart) partitionBy)
                   .bind((QueryPart) orderBy);
        }
    }

    @Override
    public final void toSQL(RenderContext context) {
        if (term == LIST_AGG && asList(CUBRID, H2, HSQLDB, MARIADB, MYSQL).contains(context.configuration().dialect())) {
            toSQLGroupConcat(context);
        }
        else if (term == LIST_AGG && asList(POSTGRES, SYBASE).contains(context.configuration().dialect())) {
            toSQLStringAgg(context);
        }
        else if (term == LIST_AGG && asList(DB2).contains(context.configuration().dialect())) {
            toSQLXMLAGG(context);
        }
        else {
            toSQLArguments(context);
            toSQLKeepDenseRankOrderByClause(context);
            toSQLWithinGroupClause(context);
            toSQLOverClause(context);
        }
    }

    /**
     * [#1276] <code>LIST_AGG</code> simulation for DB2
     */
    private void toSQLXMLAGG(RenderContext context) {

        // This is a complete view of what the below SQL will render
        // substr(xmlserialize(xmlagg(xmltext(concat(', ', title)) order by id) as varchar(1024)), 3)
        if (arguments.size() > 1) {
            context.keyword("substr(");
        }

        context.keyword("xmlserialize(xmlagg(xmltext(");

        if (arguments.size() > 1) {
            context.keyword("concat(")
                   .sql(arguments.get(1))
                   .sql(", ");
        }

        context.sql(arguments.get(0));

        if (arguments.size() > 1) {
            context.sql(")"); // CONCAT
        }

        context.sql(")"); // XMLTEXT

        if (!withinGroupOrderBy.isEmpty()) {
            context.keyword(" order by ")
                   .sql(withinGroupOrderBy);
        }

        context.sql(")"); // XMLAGG
        context.keyword(" as ");
        context.sql(DB2DataType.VARCHAR.getCastTypeName());
        context.sql(")"); // XMLSERIALIZE

        if (arguments.size() > 1) {
            context.sql(", ");

            // The separator is of this form: [', '].
            // The example has length 4
            context.sql(arguments.get(1).toString().length() - 1);
            context.sql(")"); // SUBSTR
        }
    }

    /**
     * [#1275] <code>LIST_AGG</code> simulation for Postgres, Sybase
     */
    private void toSQLStringAgg(RenderContext context) {
        toSQLFunctionName(context);
        context.sql("(");

        if (distinct) {
            context.keyword("distinct ");
        }

        // The explicit cast is needed in Postgres
        context.sql(((Field<?>) arguments.get(0)).cast(String.class));

        if (arguments.size() > 1) {
            context.sql(", ");
            context.sql(arguments.get(1));
        }
        else {
            context.sql(", ''");
        }

        if (!withinGroupOrderBy.isEmpty()) {
            context.keyword(" order by ")
                   .sql(withinGroupOrderBy);
        }

        context.sql(")");
        toSQLOverClause(context);
    }

    /**
     * [#1273] <code>LIST_AGG</code> simulation for MySQL and CUBRID
     */
    private final void toSQLGroupConcat(RenderContext context) {
        toSQLFunctionName(context);
        context.sql("(");

        if (distinct) {
            context.keyword("distinct ");
        }

        context.sql(arguments.get(0));

        if (!withinGroupOrderBy.isEmpty()) {
            context.keyword(" order by ")
                   .sql(withinGroupOrderBy);
        }

        if (arguments.size() > 1) {
            context.keyword(" separator ")
                   .sql(arguments.get(1));
        }

        context.sql(")");
    }

    private final void toSQLOverClause(RenderContext context) {

        // Render this clause only if needed
        if (!over) {
            return;
        }

        // [#1524] Don't render this clause where it is not supported
        if (over && term == ROW_NUMBER && context.configuration().dialect() == HSQLDB) {
            return;
        }

        String glue = "";
        context.keyword(" over (");
        if (!partitionBy.isEmpty()) {

            // Ignore PARTITION BY 1 clause. These databases erroneously map the
            // 1 literal onto the column index
            if (partitionByOne && asList(CUBRID, SYBASE).contains(context.configuration().dialect())) {
            }
            else {
                context.sql(glue)
                       .keyword("partition by ")
                       .sql(partitionBy);

                glue = " ";
            }
        }

        if (!orderBy.isEmpty()) {
            context.sql(glue)
                   .keyword("order by ")
                   .sql(orderBy);

            glue = " ";
        }

        if (rowsStart != null) {
            context.sql(glue);
            context.keyword("rows ");

            if (rowsEnd != null) {
                context.keyword("between ");
                toSQLRows(context, rowsStart);

                context.keyword(" and ");
                toSQLRows(context, rowsEnd);
            }
            else {
                toSQLRows(context, rowsStart);
            }

            glue = " ";
        }

        context.sql(")");
    }

    /**
     * Render <code>KEEP (DENSE_RANK [FIRST | LAST] ORDER BY {...})</code> clause
     */
    private void toSQLKeepDenseRankOrderByClause(RenderContext context) {
        if (!keepDenseRankOrderBy.isEmpty()) {
            context.keyword(" keep (dense_rank ")
                   .keyword(first ? "first" : "last")
                   .keyword(" order by ")
                   .sql(keepDenseRankOrderBy)
                   .sql(")");
        }
    }

    /**
     * Render <code>WITHIN GROUP (ORDER BY ..)</code> clause
     */
    private final void toSQLWithinGroupClause(RenderContext context) {
        if (!withinGroupOrderBy.isEmpty()) {
            context.keyword(" within group (order by ")
                   .sql(withinGroupOrderBy)
                   .sql(")");
        }
    }

    /**
     * Render function arguments and argument modifiers
     */
    private final void toSQLArguments(RenderContext context) {
        toSQLFunctionName(context);
        context.sql("(");

        if (distinct) {
            context.keyword("distinct ");
        }

        if (!arguments.isEmpty()) {
            context.sql(arguments);
        }

        if (ignoreNulls) {
            if (context.configuration().dialect() == SQLDialect.DB2) {
                context.sql(", 'IGNORE NULLS'");
            }
            else {
                context.keyword(" ignore nulls");
            }
        }
        else if (respectNulls) {
            if (context.configuration().dialect() == SQLDialect.DB2) {
                context.sql(", 'RESPECT NULLS'");
            }
            else {
                context.keyword(" respect nulls");
            }
        }

        context.sql(")");
    }

    private final void toSQLFunctionName(RenderContext ctx) {
        if (name != null) {
            ctx.sql(name);
        }
        else if (term != null) {
            ctx.sql(term.translate(ctx.configuration().dialect()));
        }
        else {
            ctx.sql(getName());
        }
    }

    private final void toSQLRows(RenderContext context, Integer rows) {
        if (rows == Integer.MIN_VALUE) {
            context.keyword("unbounded preceding");
        }
        else if (rows == Integer.MAX_VALUE) {
            context.keyword("unbounded following");
        }
        else if (rows < 0) {
            context.sql(-rows);
            context.keyword(" preceding");
        }
        else if (rows > 0) {
            context.sql(rows);
            context.keyword(" following");
        }
        else {
            context.keyword("current row");
        }
    }

    // -------------------------------------------------------------------------
    // XXX aggregate and window function fluent API methods
    // -------------------------------------------------------------------------

    final QueryPartList<QueryPart> getArguments() {
        return arguments;
    }

    @Override
    public final AggregateFunction<T> withinGroupOrderBy(Field<?>... fields) {
        withinGroupOrderBy.addAll(fields);
        return this;
    }

    @Override
    public final AggregateFunction<T> withinGroupOrderBy(SortField<?>... fields) {
        withinGroupOrderBy.addAll(Arrays.asList(fields));
        return this;
    }

    @Override
    public final AggregateFunction<T> withinGroupOrderBy(Collection<SortField<?>> fields) {
        withinGroupOrderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowBeforeOverStep<T> keepDenseRankFirstOrderBy(Field<?>... fields) {
        first = true;
        keepDenseRankOrderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowBeforeOverStep<T> keepDenseRankFirstOrderBy(SortField<?>... fields) {
        return keepDenseRankFirstOrderBy(Arrays.asList(fields));
    }

    @Override
    public final WindowBeforeOverStep<T> keepDenseRankFirstOrderBy(Collection<SortField<?>> fields) {
        first = true;
        keepDenseRankOrderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowBeforeOverStep<T> keepDenseRankLastOrderBy(Field<?>... fields) {
        keepDenseRankOrderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowBeforeOverStep<T> keepDenseRankLastOrderBy(SortField<?>... fields) {
        return keepDenseRankLastOrderBy(Arrays.asList(fields));
    }

    @Override
    public final WindowBeforeOverStep<T> keepDenseRankLastOrderBy(Collection<SortField<?>> fields) {
        keepDenseRankOrderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowPartitionByStep<T> over() {
        over = true;
        return this;
    }

    @Override
    public final WindowOverStep<T> ignoreNulls() {
        ignoreNulls = true;
        respectNulls = false;
        return this;
    }

    @Override
    public final WindowOverStep<T> respectNulls() {
        ignoreNulls = false;
        respectNulls = true;
        return this;
    }

    @Override
    public final WindowOrderByStep<T> partitionBy(Field<?>... fields) {
        partitionBy.addAll(Arrays.asList(fields));
        return this;
    }

    @Override
    public final WindowOrderByStep<T> partitionByOne() {
        partitionByOne = true;
        partitionBy.add(one());
        return this;
    }

    @Override
    public final WindowRowsStep<T> orderBy(Field<?>... fields) {
        orderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowRowsStep<T> orderBy(SortField<?>... fields) {
        orderBy.addAll(Arrays.asList(fields));
        return this;
    }

    @Override
    public final WindowRowsStep<T> orderBy(Collection<SortField<?>> fields) {
        orderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowFinalStep<T> rowsUnboundedPreceding() {
        rowsStart = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowFinalStep<T> rowsPreceding(int number) {
        rowsStart = -number;
        return this;
    }

    @Override
    public final WindowFinalStep<T> rowsCurrentRow() {
        rowsStart = 0;
        return this;
    }

    @Override
    public final WindowFinalStep<T> rowsUnboundedFollowing() {
        rowsStart = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowFinalStep<T> rowsFollowing(int number) {
        rowsStart = number;
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rowsBetweenUnboundedPreceding() {
        rowsUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rowsBetweenPreceding(int number) {
        rowsPreceding(number);
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rowsBetweenCurrentRow() {
        rowsCurrentRow();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rowsBetweenUnboundedFollowing() {
        rowsUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rowsBetweenFollowing(int number) {
        rowsFollowing(number);
        return this;
    }

    @Override
    public final WindowFinalStep<T> andUnboundedPreceding() {
        rowsEnd = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowFinalStep<T> andPreceding(int number) {
        rowsEnd = -number;
        return this;
    }

    @Override
    public final WindowFinalStep<T> andCurrentRow() {
        rowsEnd = 0;
        return this;
    }

    @Override
    public final WindowFinalStep<T> andUnboundedFollowing() {
        rowsEnd = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowFinalStep<T> andFollowing(int number) {
        rowsEnd = number;
        return this;
    }
}
