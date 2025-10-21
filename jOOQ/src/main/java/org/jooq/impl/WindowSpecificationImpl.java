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

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.H2;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Keywords.K_AND;
import static org.jooq.impl.Keywords.K_BETWEEN;
import static org.jooq.impl.Keywords.K_CURRENT_ROW;
import static org.jooq.impl.Keywords.K_EXCLUDE;
import static org.jooq.impl.Keywords.K_FOLLOWING;
import static org.jooq.impl.Keywords.K_ORDER_BY;
import static org.jooq.impl.Keywords.K_PARTITION_BY;
import static org.jooq.impl.Keywords.K_PRECEDING;
import static org.jooq.impl.Keywords.K_UNBOUNDED_FOLLOWING;
import static org.jooq.impl.Keywords.K_UNBOUNDED_PRECEDING;
import static org.jooq.impl.QOM.FrameExclude.CURRENT_ROW;
import static org.jooq.impl.QOM.FrameExclude.GROUP;
import static org.jooq.impl.QOM.FrameExclude.NO_OTHERS;
import static org.jooq.impl.QOM.FrameExclude.TIES;
import static org.jooq.impl.QOM.FrameUnits.GROUPS;
import static org.jooq.impl.QOM.FrameUnits.RANGE;
import static org.jooq.impl.QOM.FrameUnits.ROWS;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.EMPTY_SORTFIELD;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.impl.Tools.ExtendedDataKey.DATA_WINDOW_FUNCTION;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.GroupField;
import org.jooq.OrderField;
// ...
import org.jooq.QueryPart;
// ...
import org.jooq.SQLDialect;
import org.jooq.SortField;
// ...
import org.jooq.WindowSpecification;
import org.jooq.WindowSpecificationExcludeStep;
import org.jooq.WindowSpecificationFinalStep;
import org.jooq.WindowSpecificationOrderByStep;
import org.jooq.WindowSpecificationPartitionByStep;
import org.jooq.WindowSpecificationRowsAndStep;
import org.jooq.conf.RenderImplicitWindowRange;
import org.jooq.impl.QOM.FrameExclude;
import org.jooq.impl.QOM.FrameUnits;
import org.jooq.impl.QOM.UnmodifiableList;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class WindowSpecificationImpl
extends AbstractQueryPart
implements

    // Cascading interface implementations for window specification behaviour
    WindowSpecificationPartitionByStep,
    WindowSpecificationRowsAndStep,
    WindowSpecificationExcludeStep
{

    private static final Set<SQLDialect> REQUIRES_DEFAULT_FRAME_IN_LEAD_LAG_ORDER_BY = SQLDialect.supportedBy(CLICKHOUSE);
    private static final Set<SQLDialect> REQUIRES_ORDER_BY_IN_LEAD_LAG               = SQLDialect.supportedBy(H2, MARIADB, TRINO);
    private static final Set<SQLDialect> REQUIRES_ORDER_BY_IN_NTILE                  = SQLDialect.supportedBy(CLICKHOUSE, H2);
    private static final Set<SQLDialect> REQUIRES_ORDER_BY_IN_RANK_DENSE_RANK        = SQLDialect.supportedBy(H2, MARIADB);
    private static final Set<SQLDialect> REQUIRES_ORDER_BY_IN_PERCENT_RANK_CUME_DIST = SQLDialect.supportedBy(MARIADB);








    private final WindowDefinitionImpl   windowDefinition;
    private final GroupFieldList         partitionBy;
    private final SortFieldList          orderBy;
    private Integer                      frameStart;
    private Integer                      frameEnd;
    private FrameUnits                   frameUnits;
    private FrameExclude                 exclude;

    WindowSpecificationImpl() {
        this(null);
    }

    WindowSpecificationImpl(WindowDefinitionImpl windowDefinition) {
        this.windowDefinition = windowDefinition;
        this.partitionBy = new GroupFieldList();
        this.orderBy = new SortFieldList();
    }

    WindowSpecificationImpl copy() {
        return copy(c -> {});
    }

    WindowSpecificationImpl copy(Consumer<? super WindowSpecificationImpl> consumer) {
        WindowSpecificationImpl copy = new WindowSpecificationImpl(this.windowDefinition);
        copy.partitionBy.addAll(this.partitionBy);
        copy.orderBy.addAll(this.orderBy);
        copy.frameStart = this.frameStart;
        copy.frameEnd = this.frameEnd;
        copy.frameUnits = this.frameUnits;
        copy.exclude = this.exclude;
        consumer.accept(copy);
        return copy;
    }

    @Override
    public final void accept(Context<?> ctx) {







        SortFieldList o = orderBy;

        // [#8414] [#8593] [#11021] [#11851] Some RDBMS require ORDER BY in some window functions
        AbstractWindowFunction<?, ?> w = (AbstractWindowFunction<?, ?>) ctx.data(DATA_WINDOW_FUNCTION);

        if (o.isEmpty()) {
            boolean ordered =
                    w instanceof Ntile && REQUIRES_ORDER_BY_IN_NTILE.contains(ctx.dialect())
                 || w instanceof Lead && REQUIRES_ORDER_BY_IN_LEAD_LAG.contains(ctx.dialect())
                 || w instanceof Lag && REQUIRES_ORDER_BY_IN_LEAD_LAG.contains(ctx.dialect())
                 || w instanceof Rank && REQUIRES_ORDER_BY_IN_RANK_DENSE_RANK.contains(ctx.dialect())
                 || w instanceof DenseRank && REQUIRES_ORDER_BY_IN_RANK_DENSE_RANK.contains(ctx.dialect())
                 || w instanceof PercentRank && REQUIRES_ORDER_BY_IN_PERCENT_RANK_CUME_DIST.contains(ctx.dialect())
                 || w instanceof CumeDist && REQUIRES_ORDER_BY_IN_PERCENT_RANK_CUME_DIST.contains(ctx.dialect())






            ;

            if (ordered) {
                Field<Integer> constant;

                switch (ctx.family()) {







                    default:
                        constant = field(select(one())); break;
                }

                o = new SortFieldList();
                o.add(constant.sortDefault());
            }
        }

        boolean requiresDefaultFrame =
              w instanceof Lead && REQUIRES_DEFAULT_FRAME_IN_LEAD_LAG_ORDER_BY.contains(ctx.dialect())
           || w instanceof Lag && REQUIRES_DEFAULT_FRAME_IN_LEAD_LAG_ORDER_BY.contains(ctx.dialect())
        ;

        boolean hasWindowDefinitions = windowDefinition != null;
        boolean hasPartitionBy = !partitionBy.isEmpty();
        boolean hasOrderBy = !o.isEmpty();
        boolean hasFrame = frameStart != null
            || hasOrderBy && requiresDefaultFrame





        ;

        int clauses = 0;

        if (hasWindowDefinitions)
            clauses++;
        if (hasPartitionBy)
            clauses++;
        if (hasOrderBy)
            clauses++;
        if (hasFrame)
            clauses++;

        boolean indent = clauses > 1;

        if (indent)
            ctx.formatIndentStart()
               .formatNewLine();

        if (windowDefinition != null)
            ctx.declareWindows(false, c -> c.visit(windowDefinition));

        if (hasPartitionBy) {
            if (hasWindowDefinitions)
                ctx.formatSeparator();

            ctx.visit(K_PARTITION_BY).separatorRequired(true)
               .visit(partitionBy);
        }

        if (hasOrderBy) {
            if (hasWindowDefinitions || hasPartitionBy)
                ctx.formatSeparator();

            ctx.visit(K_ORDER_BY).separatorRequired(true)
               .visit(o);
        }

        if (hasFrame) {
            if (hasWindowDefinitions || hasPartitionBy || hasOrderBy)
                ctx.formatSeparator();

            FrameUnits u = frameUnits;
            Integer s = frameStart;
            Integer e = frameEnd;

            if (s == null) {
                if (requiresDefaultFrame) {
                    u = FrameUnits.RANGE;
                    s = Integer.MIN_VALUE;
                    e = Integer.MAX_VALUE;
                }































            }

            ctx.visit(u.keyword).sql(' ');

            if (e != null) {
                ctx.visit(K_BETWEEN).sql(' ');
                toSQLRows(ctx, s);

                ctx.sql(' ').visit(K_AND).sql(' ');
                toSQLRows(ctx, e);
            }
            else {
                toSQLRows(ctx, s);
            }

            if (exclude != null)
                ctx.sql(' ').visit(K_EXCLUDE).sql(' ').visit(exclude.keyword);
        }

        if (indent)
            ctx.formatIndentEnd()
               .formatNewLine();
    }






























































    private final void toSQLRows(Context<?> ctx, Integer rows) {
        if (rows == Integer.MIN_VALUE)
            ctx.visit(K_UNBOUNDED_PRECEDING);
        else if (rows == Integer.MAX_VALUE)
            ctx.visit(K_UNBOUNDED_FOLLOWING);
        else if (rows < 0)
            ctx.sql(-rows).sql(' ').visit(K_PRECEDING);
        else if (rows > 0)
            ctx.sql(rows).sql(' ').visit(K_FOLLOWING);
        else
            ctx.visit(K_CURRENT_ROW);
    }

    @Override
    public final WindowSpecificationPartitionByStep partitionBy(GroupField... fields) {
        return partitionBy(Arrays.asList(fields));
    }

    @Override
    public final WindowSpecificationPartitionByStep partitionBy(Collection<? extends GroupField> fields) {
        partitionBy.addAll(fields);
        return this;
    }

    @Override
    public final WindowSpecificationOrderByStep orderBy(OrderField<?>... fields) {
        return orderBy(Arrays.asList(fields));
    }

    @Override
    public final WindowSpecificationOrderByStep orderBy(Collection<? extends OrderField<?>> fields) {
        orderBy.addAll(Tools.sortFields(fields));
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep rowsUnboundedPreceding() {
        frameUnits = ROWS;
        frameStart = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep rowsPreceding(int number) {
        frameUnits = ROWS;
        frameStart = -number;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep rowsCurrentRow() {
        frameUnits = ROWS;
        frameStart = 0;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep rowsUnboundedFollowing() {
        frameUnits = ROWS;
        frameStart = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep rowsFollowing(int number) {
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
    public final WindowSpecificationExcludeStep rangeUnboundedPreceding() {
        frameUnits = RANGE;
        frameStart = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep rangePreceding(int number) {
        frameUnits = RANGE;
        frameStart = -number;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep rangeCurrentRow() {
        frameUnits = RANGE;
        frameStart = 0;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep rangeUnboundedFollowing() {
        frameUnits = RANGE;
        frameStart = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep rangeFollowing(int number) {
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
    public final WindowSpecificationExcludeStep groupsUnboundedPreceding() {
        frameUnits = GROUPS;
        frameStart = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep groupsPreceding(int number) {
        frameUnits = GROUPS;
        frameStart = -number;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep groupsCurrentRow() {
        frameUnits = GROUPS;
        frameStart = 0;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep groupsUnboundedFollowing() {
        frameUnits = GROUPS;
        frameStart = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep groupsFollowing(int number) {
        frameUnits = GROUPS;
        frameStart = number;
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep groupsBetweenUnboundedPreceding() {
        groupsUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep groupsBetweenPreceding(int number) {
        groupsPreceding(number);
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep groupsBetweenCurrentRow() {
        groupsCurrentRow();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep groupsBetweenUnboundedFollowing() {
        groupsUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowSpecificationRowsAndStep groupsBetweenFollowing(int number) {
        groupsFollowing(number);
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep andUnboundedPreceding() {
        frameEnd = Integer.MIN_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep andPreceding(int number) {
        frameEnd = -number;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep andCurrentRow() {
        frameEnd = 0;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep andUnboundedFollowing() {
        frameEnd = Integer.MAX_VALUE;
        return this;
    }

    @Override
    public final WindowSpecificationExcludeStep andFollowing(int number) {
        frameEnd = number;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep excludeCurrentRow() {
        exclude = CURRENT_ROW;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep excludeGroup() {
        exclude = GROUP;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep excludeTies() {
        exclude = TIES;
        return this;
    }

    @Override
    public final WindowSpecificationFinalStep excludeNoOthers() {
        exclude = NO_OTHERS;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final WindowDefinitionImpl $windowDefinition() {
        return windowDefinition;
    }

    @Override
    public final UnmodifiableList<? extends GroupField> $partitionBy() {
        return QOM.unmodifiable(partitionBy);
    }

    @Override
    public final WindowSpecification $partitionBy(Collection<? extends GroupField> newPartitionBy) {
        if (newPartitionBy == partitionBy)
            return this;
        else
            return copy(c -> {
                c.partitionBy.clear();
                c.partitionBy.addAll(newPartitionBy);
            });
    }

    @Override
    public final UnmodifiableList<? extends SortField<?>> $orderBy() {
        return QOM.unmodifiable(orderBy);
    }

    @Override
    public final WindowSpecification $orderBy(Collection<? extends SortField<?>> newOrderBy) {
        if (newOrderBy == orderBy)
            return this;
        else
            return copy(c -> {
                c.orderBy.clear();
                c.orderBy.addAll(newOrderBy);
            });
    }

    @Override
    public final FrameUnits $frameUnits() {
        return frameUnits;
    }

    @Override
    public final WindowSpecification $frameUnits(FrameUnits newFrameUnits) {
        if (newFrameUnits == frameUnits)
            return this;
        else
            return copy(c -> {
                c.frameUnits = newFrameUnits;
            });
    }

    @Override
    public final Integer $frameStart() {
        return frameStart;
    }

    @Override
    public final WindowSpecification $frameStart(Integer newFrameStart) {
        if (newFrameStart == frameStart)
            return this;
        else
            return copy(c -> {
                c.frameStart = newFrameStart;
            });
    }

    @Override
    public final Integer $frameEnd() {
        return frameEnd;
    }

    @Override
    public final WindowSpecification $frameEnd(Integer newFrameEnd) {
        if (newFrameEnd == frameEnd)
            return this;
        else
            return copy(c -> {
                c.frameEnd = newFrameEnd;
            });
    }

    @Override
    public final FrameExclude $exclude() {
        return exclude;
    }

    @Override
    public final WindowSpecification $exclude(FrameExclude newExclude) {
        if (newExclude == exclude)
            return this;
        else
            return copy(c -> {
                c.exclude = newExclude;
            });
    }



























}
