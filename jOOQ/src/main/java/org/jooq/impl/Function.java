/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
            context.visit(arguments.get(0));
            context.visit(withinGroupOrderBy);

            if (arguments.size() > 1) {
                context.visit(arguments.get(1));
            }
        }
        else {
            context.visit(arguments)
                   .visit(keepDenseRankOrderBy)
                   .visit(withinGroupOrderBy)
                   .visit(partitionBy)
                   .visit(orderBy);
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
                   .visit(arguments.get(1))
                   .sql(", ");
        }

        context.visit(arguments.get(0));

        if (arguments.size() > 1) {
            context.sql(")"); // CONCAT
        }

        context.sql(")"); // XMLTEXT

        if (!withinGroupOrderBy.isEmpty()) {
            context.sql(" ").keyword("order by").sql(" ")
                   .visit(withinGroupOrderBy);
        }

        context.sql(")"); // XMLAGG
        context.sql(" ").keyword("as").sql(" ");
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
            context.keyword("distinct").sql(" ");
        }

        // The explicit cast is needed in Postgres
        context.visit(((Field<?>) arguments.get(0)).cast(String.class));

        if (arguments.size() > 1) {
            context.sql(", ");
            context.visit(arguments.get(1));
        }
        else {
            context.sql(", ''");
        }

        if (!withinGroupOrderBy.isEmpty()) {
            context.sql(" ").keyword("order by").sql(" ")
                   .visit(withinGroupOrderBy);
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
            context.keyword("distinct").sql(" ");
        }

        context.visit(arguments.get(0));

        if (!withinGroupOrderBy.isEmpty()) {
            context.sql(" ").keyword("order by").sql(" ")
                   .visit(withinGroupOrderBy);
        }

        if (arguments.size() > 1) {
            context.sql(" ").keyword("separator").sql(" ")
                   .visit(arguments.get(1));
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
        context.sql(" ").keyword("over").sql(" (");
        if (!partitionBy.isEmpty()) {

            // Ignore PARTITION BY 1 clause. These databases erroneously map the
            // 1 literal onto the column index
            if (partitionByOne && asList(CUBRID, SYBASE).contains(context.configuration().dialect())) {
            }
            else {
                context.sql(glue)
                       .keyword("partition by").sql(" ")
                       .visit(partitionBy);

                glue = " ";
            }
        }

        if (!orderBy.isEmpty()) {
            context.sql(glue)
                   .keyword("order by").sql(" ")
                   .visit(orderBy);

            glue = " ";
        }

        if (rowsStart != null) {
            context.sql(glue);
            context.keyword("rows").sql(" ");

            if (rowsEnd != null) {
                context.keyword("between").sql(" ");
                toSQLRows(context, rowsStart);

                context.sql(" ").keyword("and").sql(" ");
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
            context.sql(" ").keyword("keep")
                   .sql(" (").keyword("dense_rank")
                   .sql(" ").keyword(first ? "first" : "last")
                   .sql(" ").keyword("order by")
                   .sql(" ").visit(keepDenseRankOrderBy)
                   .sql(")");
        }
    }

    /**
     * Render <code>WITHIN GROUP (ORDER BY ..)</code> clause
     */
    private final void toSQLWithinGroupClause(RenderContext context) {
        if (!withinGroupOrderBy.isEmpty()) {
            context.sql(" ").keyword("within group")
                   .sql(" (").keyword("order by")
                   .sql(" ").visit(withinGroupOrderBy)
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
            context.keyword("distinct").sql(" ");
        }

        if (!arguments.isEmpty()) {
            context.visit(arguments);
        }

        if (ignoreNulls) {
            if (context.configuration().dialect() == SQLDialect.DB2) {
                context.sql(", 'IGNORE NULLS'");
            }
            else {
                context.sql(" ").keyword("ignore nulls");
            }
        }
        else if (respectNulls) {
            if (context.configuration().dialect() == SQLDialect.DB2) {
                context.sql(", 'RESPECT NULLS'");
            }
            else {
                context.sql(" ").keyword("respect nulls");
            }
        }

        context.sql(")");
    }

    private final void toSQLFunctionName(RenderContext ctx) {
        if (name != null) {
            ctx.visit(name);
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
            context.sql(" ").keyword("preceding");
        }
        else if (rows > 0) {
            context.sql(rows);
            context.sql(" ").keyword("following");
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
