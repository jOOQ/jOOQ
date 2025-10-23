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

// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.DUCKDB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Keywords.K_OVER;
import static org.jooq.impl.SelectQueryImpl.NO_SUPPORT_WINDOW_CLAUSE;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_WINDOW_DEFINITIONS;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.GroupField;
import org.jooq.Name;
import org.jooq.OrderField;
// ...
import org.jooq.QueryPart;
import org.jooq.SQLDialect;
// ...
import org.jooq.WindowDefinition;
import org.jooq.WindowExcludeStep;
import org.jooq.WindowFinalStep;
import org.jooq.WindowOrderByStep;
import org.jooq.WindowOverStep;
import org.jooq.WindowPartitionByStep;
import org.jooq.WindowRowsAndStep;
import org.jooq.WindowRowsStep;
import org.jooq.WindowSpecification;
import org.jooq.impl.Tools.ExtendedDataKey;

import org.jetbrains.annotations.NotNull;


/**
 * @author Lukas Eder
 */
abstract class AbstractWindowFunction<T, Q extends QOM.WindowFunction<T, Q>>
extends
    AbstractField<T>
implements
    WindowOverStep<T>,
    WindowPartitionByStep<T>,
    WindowRowsStep<T>,
    WindowRowsAndStep<T>,
    WindowExcludeStep<T>,
    QOM.WindowFunction<T, Q>,
    ScopeMappable
{
    private static final Set<SQLDialect> SUPPORT_NO_PARENS_WINDOW_REFERENCE = SQLDialect.supportedBy(CLICKHOUSE, DUCKDB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB);

    // Other attributes
    WindowSpecificationImpl              windowSpecification;
    WindowDefinitionImpl                 windowDefinition;
    Name                                 windowName;

    AbstractWindowFunction(Name name, DataType<T> type) {
        super(name, type);
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------













    @SuppressWarnings("unchecked")
    final QueryPart window(Context<?> ctx) {
        if (windowSpecification != null)
            return CustomQueryPart.of(c -> c.sql('(').visit(windowSpecification).sql(')'));

        // [#3727] Referenced WindowDefinitions that contain a frame clause
        // shouldn't be referenced from within parentheses (in MySQL and PostgreSQL)
        if (windowDefinition != null)
            if (SUPPORT_NO_PARENS_WINDOW_REFERENCE.contains(ctx.dialect()) && !NO_SUPPORT_WINDOW_CLAUSE.contains(ctx.dialect()))
                return windowDefinition;
            else
                return CustomQueryPart.of(c -> c.sql('(').visit(windowDefinition).sql(')'));

        // [#531] Inline window specifications if the WINDOW clause is not supported
        if (windowName != null) {
            if (!NO_SUPPORT_WINDOW_CLAUSE.contains(ctx.dialect()))
                return windowName;

            QueryPartList<WindowDefinition> windows = (QueryPartList<WindowDefinition>) ctx.data(DATA_WINDOW_DEFINITIONS);

            if (windows != null) {
                for (WindowDefinition window : windows)
                    if (((WindowDefinitionImpl) window).getName().equals(windowName))
                        return CustomQueryPart.of(c -> c.sql('(').visit(window).sql(')'));
            }

            // [#3162] If a window specification is missing from the query's WINDOW clause,
            // jOOQ should just render the window name regardless of the SQL dialect
            else
                return windowName;
        }

        return null;
    }

    final boolean isWindow() {
        return windowSpecification != null || windowDefinition != null || windowName != null;
    }

    final boolean isOrderedWindow(Context<?> ctx) {

        if (windowSpecification != null)
            return isOrderedWindowSpecification(windowSpecification);
        else if (windowDefinition != null && windowDefinition.$windowSpecification() != null)
            return isOrderedWindowSpecification(windowDefinition.$windowSpecification());

        // TODO: Resolve window names
        else
            return false;
    }

    private static final boolean isOrderedWindowSpecification(WindowSpecification s) {
        return !s.$orderBy().isEmpty()
            || s.$frameUnits() != null
            || s.$exclude() != null;
    }

    final void acceptOverClause(Context<?> ctx) {
        QueryPart window = window(ctx);

        // Render this clause only if needed
        if (window == null)
            return;

        switch (ctx.family()) {












            default:
                ctx.sql(' ')
                   .visit(K_OVER)
                   .sql(' ');

                ctx.data(ExtendedDataKey.DATA_WINDOW_FUNCTION, this, c -> c.visit(window));
                break;
        }
    }

    // -------------------------------------------------------------------------
    // XXX Window function fluent API methods
    // -------------------------------------------------------------------------

    @Override
    public final WindowPartitionByStep<T> over() {
        windowSpecification = new WindowSpecificationImpl();
        return this;
    }

    @Override
    public final WindowFinalStep<T> over(WindowSpecification specification) {
        this.windowSpecification = specification instanceof WindowSpecificationImpl w
            ? w
            : new WindowSpecificationImpl((WindowDefinitionImpl) specification);

        return this;
    }

    @Override
    public final WindowFinalStep<T> over(WindowDefinition definition) {
        this.windowDefinition = (WindowDefinitionImpl) definition;
        return this;
    }

    @Override
    public final WindowFinalStep<T> over(String n) {
        return over(name(n));
    }

    @Override
    public final WindowFinalStep<T> over(Name n) {
        this.windowName = n;
        return this;
    }

    @Override
    public final WindowOrderByStep<T> partitionBy(GroupField... fields) {
        windowSpecification.partitionBy(fields);
        return this;
    }

    @Override
    public final WindowOrderByStep<T> partitionBy(Collection<? extends GroupField> fields) {
        windowSpecification.partitionBy(fields);
        return this;
    }

    @Override
    public /* non-final */ AbstractWindowFunction<T, Q> orderBy(OrderField<?>... fields) {
        windowSpecification.orderBy(fields);
        return this;
    }

    @Override
    public /* non-final */ AbstractWindowFunction<T, Q> orderBy(Collection<? extends OrderField<?>> fields) {
        windowSpecification.orderBy(fields);
        return this;
    }

    @Override
    public final WindowExcludeStep<T> rowsUnboundedPreceding() {
        windowSpecification.rowsUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> rowsPreceding(int number) {
        windowSpecification.rowsPreceding(number);
        return this;
    }

    @Override
    public final WindowExcludeStep<T> rowsCurrentRow() {
        windowSpecification.rowsCurrentRow();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> rowsUnboundedFollowing() {
        windowSpecification.rowsUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> rowsFollowing(int number) {
        windowSpecification.rowsFollowing(number);
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rowsBetweenUnboundedPreceding() {
        windowSpecification.rowsBetweenUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rowsBetweenPreceding(int number) {
        windowSpecification.rowsBetweenPreceding(number);
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rowsBetweenCurrentRow() {
        windowSpecification.rowsBetweenCurrentRow();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rowsBetweenUnboundedFollowing() {
        windowSpecification.rowsBetweenUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rowsBetweenFollowing(int number) {
        windowSpecification.rowsBetweenFollowing(number);
        return this;
    }

    @Override
    public final WindowExcludeStep<T> rangeUnboundedPreceding() {
        windowSpecification.rangeUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> rangePreceding(int number) {
        windowSpecification.rangePreceding(number);
        return this;
    }

    @Override
    public final WindowExcludeStep<T> rangeCurrentRow() {
        windowSpecification.rangeCurrentRow();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> rangeUnboundedFollowing() {
        windowSpecification.rangeUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> rangeFollowing(int number) {
        windowSpecification.rangeFollowing(number);
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rangeBetweenUnboundedPreceding() {
        windowSpecification.rangeBetweenUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rangeBetweenPreceding(int number) {
        windowSpecification.rangeBetweenPreceding(number);
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rangeBetweenCurrentRow() {
        windowSpecification.rangeBetweenCurrentRow();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rangeBetweenUnboundedFollowing() {
        windowSpecification.rangeBetweenUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> rangeBetweenFollowing(int number) {
        windowSpecification.rangeBetweenFollowing(number);
        return this;
    }

    @Override
    public final WindowExcludeStep<T> groupsUnboundedPreceding() {
        windowSpecification.groupsUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> groupsPreceding(int number) {
        windowSpecification.groupsPreceding(number);
        return this;
    }

    @Override
    public final WindowExcludeStep<T> groupsCurrentRow() {
        windowSpecification.groupsCurrentRow();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> groupsUnboundedFollowing() {
        windowSpecification.groupsUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> groupsFollowing(int number) {
        windowSpecification.groupsFollowing(number);
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> groupsBetweenUnboundedPreceding() {
        windowSpecification.groupsBetweenUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> groupsBetweenPreceding(int number) {
        windowSpecification.groupsBetweenPreceding(number);
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> groupsBetweenCurrentRow() {
        windowSpecification.groupsBetweenCurrentRow();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> groupsBetweenUnboundedFollowing() {
        windowSpecification.groupsBetweenUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowRowsAndStep<T> groupsBetweenFollowing(int number) {
        windowSpecification.groupsBetweenFollowing(number);
        return this;
    }

    @Override
    public final WindowExcludeStep<T> andUnboundedPreceding() {
        windowSpecification.andUnboundedPreceding();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> andPreceding(int number) {
        windowSpecification.andPreceding(number);
        return this;
    }

    @Override
    public final WindowExcludeStep<T> andCurrentRow() {
        windowSpecification.andCurrentRow();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> andUnboundedFollowing() {
        windowSpecification.andUnboundedFollowing();
        return this;
    }

    @Override
    public final WindowExcludeStep<T> andFollowing(int number) {
        windowSpecification.andFollowing(number);
        return this;
    }

    @Override
    public final WindowFinalStep<T> excludeCurrentRow() {
        windowSpecification.excludeCurrentRow();
        return this;
    }

    @Override
    public final WindowFinalStep<T> excludeGroup() {
        windowSpecification.excludeGroup();
        return this;
    }

    @Override
    public final WindowFinalStep<T> excludeTies() {
        windowSpecification.excludeTies();
        return this;
    }

    @Override
    public final WindowFinalStep<T> excludeNoOthers() {
        windowSpecification.excludeNoOthers();
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    final Q copy(Consumer<? super Q> consumer) {
        Q copy = copyWindowFunction(copyWindowSpecification());
        consumer.accept(copy);
        return copy;
    }

    final Function<? super Q, ? extends Q> copyWindowSpecification() {
        return c -> {
            AbstractWindowFunction<?, ?> q = (AbstractWindowFunction<?, ?>) c;

            // [#19255] TODO: Copy also definitions and specifications
            q.windowSpecification = windowSpecification;
            q.windowDefinition = windowDefinition;
            q.windowName = windowName;

            return c;
        };
    }

    abstract Q copyWindowFunction(Function<? super Q, ? extends Q> function);

    @Override
    public final WindowSpecification $windowSpecification() {
        return windowSpecification;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q $windowSpecification(WindowSpecification s) {
        if (s == windowSpecification)
            return (Q) this;
        else
            return copy(c -> {
                AbstractWindowFunction<?, ?> q = (AbstractWindowFunction<?, ?>) c;

                q.windowDefinition = null;
                q.windowSpecification = (WindowSpecificationImpl) s;
                q.windowName = null;
            });
    }

    @Override
    public final WindowDefinition $windowDefinition() {
        return windowDefinition;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q $windowDefinition(WindowDefinition d) {
        if (d == windowDefinition)
            return (Q) this;
        else
            return copy(c -> {
                AbstractWindowFunction<?, ?> q = (AbstractWindowFunction<?, ?>) c;

                q.windowDefinition = (WindowDefinitionImpl) d;
                q.windowSpecification = null;
                q.windowName = null;
            });
    }

    @Override
    public final Name $windowName() {
        return windowName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q $windowName(Name n) {
        if (n == windowName)
            return (Q) this;
        else
            return copy(c -> {
                AbstractWindowFunction<?, ?> q = (AbstractWindowFunction<?, ?>) c;

                q.windowDefinition = null;
                q.windowSpecification = null;
                q.windowName = n;
            });
    }













}
