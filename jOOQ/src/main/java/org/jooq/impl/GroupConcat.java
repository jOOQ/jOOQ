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
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Names.N_GROUP_CONCAT;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import org.jooq.AggregateFunction;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.GroupConcatOrderByStep;
import org.jooq.OrderField;
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * @author Lukas Eder
 */
final class GroupConcat
extends
    AbstractAggregateFunction<String, GroupConcat>
implements
    GroupConcatOrderByStep,
    UNotYetImplemented
{

    final Set<SQLDialect>       REQUIRE_WITHIN_GROUP = SQLDialect.supportedBy(TRINO);

    private final Field<?>      field;
    private final SortFieldList orderBy;
    private Field<String>       separator;

    GroupConcat(Field<?> field) {
        this(field, false);
    }

    GroupConcat(Field<?> field, boolean distinct) {
        super(distinct, N_GROUP_CONCAT, SQLDataType.VARCHAR, field);

        this.field = field;
        this.orderBy = new SortFieldList();
    }

    @Override
    public final void accept(Context<?> ctx) {
        ListAgg result;

        if (separator == null)
            result = new ListAgg(distinct, field, ListAgg.DEFAULT_SEPARATOR);
        else
            result = new ListAgg(distinct, field, separator);

        if (!orderBy.isEmpty())
            result.withinGroupOrderBy(orderBy);

        // [#3045] [#11485] Dialects with mandatory WITHIN GROUP clause
        else if (REQUIRE_WITHIN_GROUP.contains(ctx.dialect()))
            result.withinGroupOrderBy(orderBy);

        ctx.visit(fo(result));
    }

    @Override
    public final AggregateFunction<String> separator(String s) {
        return separator(inline(s));
    }

    @Override
    public final AggregateFunction<String> separator(Field<String> s) {
        this.separator = s;
        return this;
    }

    @Override
    public final GroupConcat orderBy(OrderField<?>... fields) {
        return orderBy(Arrays.asList(fields));
    }

    @Override
    public final GroupConcat orderBy(Collection<? extends OrderField<?>> fields) {
        orderBy.addAll(Tools.sortFields(fields));
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    final GroupConcat copy2(Function<GroupConcat, GroupConcat> function) {
        return function.apply((GroupConcat) new GroupConcat(field, distinct).separator(separator));
    }
}
