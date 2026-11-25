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

import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JoinType;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.impl.QOM.JoinHint;

/**
 * @author Lukas Eder
 */
final class AsofJoin
extends
    AsofJoinTable<AsofJoin>
implements
    QOM.AsofJoin<Record>
{

    AsofJoin(TableLike<?> lhs, TableLike<?> rhs) {
        super(lhs, rhs, JoinType.ASOF_JOIN);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    AsofJoin construct(
        Table<?> table1,
        Collection<? extends Field<?>> partitionBy1,
        Collection<? extends Field<?>> partitionBy2,
        Table<?> table2,
        Condition o,
        Collection<? extends Field<?>> u,
        JoinHint h
    ) {
        return construct(table1, partitionBy1, partitionBy2, table2, o, u, h, match);
    }

    @Override
    AsofJoin construct(
        Table<?> table1,
        Collection<? extends Field<?>> partitionBy1,
        Collection<? extends Field<?>> partitionBy2,
        Table<?> table2,
        Condition o,
        Collection<? extends Field<?>> u,
        JoinHint h,
        Condition m
    ) {
        AsofJoin result = new AsofJoin(table1, table2).matchCondition(m);

        // [#20146] The ON clause is optional, and an empty one must not be
        //          rendered as ON TRUE
        if (o instanceof ConditionProviderImpl c)
            o = c.getWhereOrNull();

        if (o != null)
            return result.on(o);
        else if (u != null)
            return result.using(u);
        else
            return result;
    }
}
