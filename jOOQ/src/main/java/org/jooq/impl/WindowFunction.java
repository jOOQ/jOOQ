/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import static org.jooq.impl.Factory.one;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.SortField;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowPartitionByStep;
import org.jooq.WindowRowsAndStep;
import org.jooq.WindowRowsStep;

/**
 * Implementation object for window function DSL API
 *
 * @author Lukas Eder
 */
class WindowFunction<T> extends AbstractField<T>
implements

    // Cascading interface implementations for window function behaviour
    WindowIgnoreNullsStep<T>,
    WindowPartitionByStep<T>,
    WindowRowsStep<T>,
    WindowRowsAndStep<T>
    {

    /**
     * Generated UID
     */
    private static final long      serialVersionUID = 5505202722420252635L;

    private final Term             term;

    private final QueryPartList<?> arguments;
    private final FieldList        partitionBy;
    private final SortFieldList    orderBy;

    private boolean                partitionByOne;
    private boolean                ignoreNulls;
    private boolean                respectNulls;
    private Integer                rowsStart;
    private Integer                rowsEnd;

    public WindowFunction(String name, DataType<T> type, QueryPart... arguments) {
        super(name, type);

        this.partitionBy = new FieldList();
        this.orderBy = new SortFieldList();
        this.arguments = new QueryPartList<QueryPart>(Arrays.asList(arguments));
        this.term = null;
    }

    public WindowFunction(Term term, DataType<T> type, QueryPart... arguments) {
        super(term.name().toLowerCase(), type);

        this.partitionBy = new FieldList();
        this.orderBy = new SortFieldList();
        this.arguments = new QueryPartList<QueryPart>(Arrays.asList(arguments));
        this.term = term;
    }

    // -------------------------------------------------------------------------
    // Field API
    // -------------------------------------------------------------------------

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(arguments, partitionBy, orderBy);
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.sql(getFNName(context.getDialect()));
        context.sql("(");

        if (!arguments.isEmpty()) {
            context.sql(arguments);
        }

        if (ignoreNulls) {
            if (context.getDialect() == SQLDialect.DB2) {
                context.sql(", 'IGNORE NULLS'");
            }
            else {
                context.sql(" ignore nulls");
            }
        }
        else if (respectNulls) {
            if (context.getDialect() == SQLDialect.DB2) {
                context.sql(", 'RESPECT NULLS'");
            }
            else {
                context.sql(" respect nulls");
            }
        }

        context.sql(") over (");
        String glue = "";

        if (!partitionBy.isEmpty()) {
            if (partitionByOne && context.getDialect() == SQLDialect.SYBASE) {
                // Ignore partition clause. Sybase does not support this construct
            }
            else {
                context.sql(glue)
                       .sql("partition by ")
                       .sql(partitionBy);

                glue = " ";
            }
        }

        if (!orderBy.isEmpty()) {
            context.sql(glue)
                   .sql("order by ");

            switch (context.getDialect()) {

                // SQL Server and Sybase don't allow for fully qualified fields
                // in the ORDER BY clause of an analytic expression
                case SQLSERVER: // No break
                case SYBASE: {
                    for (SortField<?> f : orderBy) {
                        SortFieldImpl<?> field = (SortFieldImpl<?>) f;
                        field.toSQLInAnalyticClause(context);
                    }

                    break;
                }

                default: {
                    context.sql(orderBy);
                    break;
                }
            }

            glue = " ";
        }

        if (rowsStart != null) {
            context.sql(glue);
            context.sql("rows ");

            if (rowsEnd != null) {
                context.sql("between ");
                toSQLRows(context, rowsStart);
                context.sql(" and ");
                toSQLRows(context, rowsEnd);
            }
            else {
                toSQLRows(context, rowsStart);
            }

            glue = " ";
        }

        context.sql(")");
    }

    private final String getFNName(SQLDialect dialect) {
        if (term != null) {
            return term.translate(dialect);
        }
        else {
            return getName();
        }
    }

    private final void toSQLRows(RenderContext context, Integer rows) {
        if (rows == Integer.MIN_VALUE) {
            context.sql("unbounded preceding");
        }
        else if (rows == Integer.MAX_VALUE) {
            context.sql("unbounded following");
        }
        else if (rows < 0) {
            context.sql(-rows);
            context.sql(" preceding");
        }
        else if (rows > 0) {
            context.sql(rows);
            context.sql(" following");
        }
        else {
            context.sql("current row");
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.bind((QueryPart) arguments)
               .bind((QueryPart) partitionBy)
               .bind((QueryPart) orderBy);
    }

    @Override
    public final boolean isNullLiteral() {
        return false;
    }

    // -------------------------------------------------------------------------
    // Window function API
    // -------------------------------------------------------------------------

    @Override
    public final WindowFunction<T> ignoreNulls() {
        ignoreNulls = true;
        respectNulls = false;
        return this;
    }

    @Override
    public final WindowFunction<T> respectNulls() {
        ignoreNulls = false;
        respectNulls = true;
        return this;
    }

    @Override
    public final WindowFunction<T> over() {
        return this;
    }

    @Override
    public final WindowFunction<T> partitionBy(Field<?>... fields) {
        partitionBy.addAll(Arrays.asList(fields));
        return this;
    }

    @Override
    public final WindowFunction<T> partitionByOne() {
        partitionByOne = true;
        partitionBy.add(one());
        return this;
    }

    @Override
    public final WindowFunction<T> orderBy(Field<?>... fields) {
        orderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowFunction<T> orderBy(SortField<?>... fields) {
        orderBy.addAll(Arrays.asList(fields));
        return this;
    }

    @Override
    public final WindowFunction<T> orderBy(Collection<SortField<?>> fields) {
        orderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowFunction<T> rowsUnboundedPreceding() {
        rowsStart = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowFunction<T> rowsPreceding(int number) {
        rowsStart = -number;
        return this;
    }

    @Override
    public final WindowFunction<T> rowsCurrentRow() {
        rowsStart = 0;
        return this;
    }

    @Override
    public final WindowFunction<T> rowsUnboundedFollowing() {
        rowsStart = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowFunction<T> rowsFollowing(int number) {
        rowsStart = number;
        return this;
    }

    @Override
    public final WindowFunction<T> rowsBetweenUnboundedPreceding() {
        rowsUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowFunction<T> rowsBetweenPreceding(int number) {
        rowsPreceding(number);
        return this;
    }

    @Override
    public final WindowFunction<T> rowsBetweenCurrentRow() {
        rowsCurrentRow();
        return this;
    }

    @Override
    public final WindowFunction<T> rowsBetweenUnboundedFollowing() {
        rowsUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowFunction<T> rowsBetweenFollowing(int number) {
        rowsFollowing(number);
        return this;
    }

    @Override
    public final WindowFunction<T> andUnboundedPreceding() {
        rowsEnd = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowFunction<T> andPreceding(int number) {
        rowsEnd = -number;
        return this;
    }

    @Override
    public final WindowFunction<T> andCurrentRow() {
        rowsEnd = 0;
        return this;
    }

    @Override
    public final WindowFunction<T> andUnboundedFollowing() {
        rowsEnd = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowFunction<T> andFollowing(int number) {
        rowsEnd = number;
        return this;
    }
}
