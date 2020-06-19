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
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Names.N_GROUP_CONCAT;

import java.util.Arrays;
import java.util.Collection;

import org.jooq.AggregateFunction;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.GroupConcatOrderByStep;
import org.jooq.OrderField;

/**
 * @author Lukas Eder
 */
final class GroupConcat
extends AbstractAggregateFunction<String>
implements GroupConcatOrderByStep {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -6884415527559632960L;

    private final Field<?>      field;
    private final SortFieldList orderBy;
    private String              separator;

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
            result = new ListAgg(distinct, field, inline(","));
        else
            result = new ListAgg(distinct, field, inline(separator));







        if (orderBy.isEmpty())
            ctx.visit(result);
        else
            ctx.visit(result.withinGroupOrderBy(orderBy));
    }

    @Override
    public final AggregateFunction<String> separator(String s) {
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
}
