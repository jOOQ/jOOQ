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

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JoinType;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.TableAsofJoinStep;
import org.jooq.TableLike;

/**
 * [#20146] A base type for <code>ASOF JOIN</code> and
 * <code>LEFT ASOF JOIN</code>.
 *
 * @author Lukas Eder
 */
abstract class AsofJoinTable<J extends AsofJoinTable<J>>
extends
    JoinTable<J>
implements
    TableAsofJoinStep<Record>
{

    AsofJoinTable(TableLike<?> lhs, TableLike<?> rhs, JoinType type) {
        super(lhs, rhs, type, null);
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final J match(Condition c) {
        match = c;
        return (J) this;
    }

    @Override
    public final J match(Field<Boolean> c) {
        return match(DSL.condition(c));
    }

    @Override
    public final J match(SQL sql) {
        return match(DSL.condition(sql));
    }

    @Override
    public final J match(String sql) {
        return match(DSL.condition(sql));
    }

    @Override
    public final J match(String sql, Object... bindings) {
        return match(DSL.condition(sql, bindings));
    }

    @Override
    public final J match(String sql, QueryPart... parts) {
        return match(DSL.condition(sql, parts));
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    public final Condition $match() {
        return match;
    }

    public final J $match(Condition m) {
        return construct($table1(), $partitionBy1(), $partitionBy2(), $table2(), $on(), $using(), $hint(), m);
    }
}
