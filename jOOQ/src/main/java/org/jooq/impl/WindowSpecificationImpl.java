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
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.WindowSpecificationImpl.FrameUnits.RANGE;
import static org.jooq.impl.WindowSpecificationImpl.FrameUnits.ROWS;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.WindowSpecificationFinalStep;
import org.jooq.WindowSpecificationOrderByStep;
import org.jooq.WindowSpecificationPartitionByStep;
import org.jooq.WindowSpecificationRowsAndStep;

/**
 * @author Lukas Eder
 */
final class WindowSpecificationImpl extends AbstractQueryPart implements

    // Cascading interface implementations for window specification behaviour
    WindowSpecificationPartitionByStep,
    WindowSpecificationRowsAndStep
    {

    /**
     * Generated UID
     */
    private static final long             serialVersionUID = 2996016924769376361L;

    private final QueryPartList<Field<?>> partitionBy;
    private final SortFieldList           orderBy;
    private Integer                       frameStart;
    private Integer                       frameEnd;
    private FrameUnits                    frameUnits;
    private boolean                       partitionByOne;

    WindowSpecificationImpl() {
        this.partitionBy = new QueryPartList<Field<?>>();
        this.orderBy = new SortFieldList();
    }

    @Override
    public final void accept(Context<?> ctx) {
        String glue = "";

        if (!partitionBy.isEmpty()) {

            // Ignore PARTITION BY 1 clause. These databases erroneously map the
            // 1 literal onto the column index (CUBRID, Sybase), or do not support
            // constant expressions in the PARTITION BY clause (HANA)
            if (partitionByOne && asList(CUBRID).contains(ctx.configuration().dialect())) {
            }
            else {
                ctx.sql(glue)
                       .keyword("partition by").sql(' ')
                       .visit(partitionBy);

                glue = " ";
            }
        }

        if (!orderBy.isEmpty()) {
            ctx.sql(glue)
                   .keyword("order by").sql(' ')
                   .visit(orderBy);

            glue = " ";
        }

        if (frameStart != null) {
            ctx.sql(glue);
            ctx.keyword(frameUnits.keyword).sql(' ');

            if (frameEnd != null) {
                ctx.keyword("between").sql(' ');
                toSQLRows(ctx, frameStart);

                ctx.sql(' ').keyword("and").sql(' ');
                toSQLRows(ctx, frameEnd);
            }
            else {
                toSQLRows(ctx, frameStart);
            }

            glue = " ";
        }
    }

    private final void toSQLRows(Context<?> ctx, Integer rows) {
        if (rows == Integer.MIN_VALUE) {
            ctx.keyword("unbounded preceding");
        }
        else if (rows == Integer.MAX_VALUE) {
            ctx.keyword("unbounded following");
        }
        else if (rows < 0) {
            ctx.sql(-rows);
            ctx.sql(' ').keyword("preceding");
        }
        else if (rows > 0) {
            ctx.sql(rows);
            ctx.sql(' ').keyword("following");
        }
        else {
            ctx.keyword("current row");
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final WindowSpecificationPartitionByStep partitionBy(Field<?>... fields) {
        return partitionBy(Arrays.asList(fields));
    }

    @Override
    public final WindowSpecificationPartitionByStep partitionBy(Collection<? extends Field<?>> fields) {
        partitionBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowSpecificationOrderByStep partitionByOne() {
        partitionByOne = true;
        partitionBy.add(one());
        return null;
    }

    @Override
    public final WindowSpecificationOrderByStep orderBy(Field<?>... fields) {
        orderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowSpecificationOrderByStep orderBy(SortField<?>... fields) {
        return orderBy(Arrays.asList(fields));
    }

    @Override
    public final WindowSpecificationOrderByStep orderBy(Collection<? extends SortField<?>> fields) {
        orderBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rowsUnboundedPreceding() {
        frameUnits = ROWS;
        frameStart = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rowsPreceding(int number) {
        frameUnits = ROWS;
        frameStart = -number;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rowsCurrentRow() {
        frameUnits = ROWS;
        frameStart = 0;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rowsUnboundedFollowing() {
        frameUnits = ROWS;
        frameStart = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rowsFollowing(int number) {
        frameUnits = ROWS;
        frameStart = number;
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenUnboundedPreceding() {
        rowsUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenPreceding(int number) {
        rowsPreceding(number);
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenCurrentRow() {
        rowsCurrentRow();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenUnboundedFollowing() {
        rowsUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenFollowing(int number) {
        rowsFollowing(number);
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rangeUnboundedPreceding() {
        frameUnits = RANGE;
        frameStart = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rangePreceding(int number) {
        frameUnits = RANGE;
        frameStart = -number;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rangeCurrentRow() {
        frameUnits = RANGE;
        frameStart = 0;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rangeUnboundedFollowing() {
        frameUnits = RANGE;
        frameStart = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep rangeFollowing(int number) {
        frameUnits = RANGE;
        frameStart = number;
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rangeBetweenUnboundedPreceding() {
        rangeUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rangeBetweenPreceding(int number) {
        rangePreceding(number);
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rangeBetweenCurrentRow() {
        rangeCurrentRow();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rangeBetweenUnboundedFollowing() {
        rangeUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep rangeBetweenFollowing(int number) {
        rangeFollowing(number);
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep andUnboundedPreceding() {
        frameEnd = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep andPreceding(int number) {
        frameEnd = -number;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep andCurrentRow() {
        frameEnd = 0;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep andUnboundedFollowing() {
        frameEnd = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep andFollowing(int number) {
        frameEnd = number;
        return this;
    }

    enum FrameUnits {
        ROWS("rows"),
        RANGE("range");

        private final String keyword;

        private FrameUnits(String keyword) {
            this.keyword = keyword;
        }
    }
}
