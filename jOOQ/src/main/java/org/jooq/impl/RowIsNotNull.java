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

import static org.jooq.impl.Keywords.K_IS_NOT_NULL;
import static org.jooq.impl.Tools.allNotNull;

import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Function1;
import org.jooq.Row;

/**
 * @author Lukas Eder
 */
final class RowIsNotNull extends AbstractCondition implements QOM.RowIsNotNull {

    final Row row;

    RowIsNotNull(Row row) {
        this.row = row;
    }

    @Override
    final boolean isNullable() {
        return false;
    }

    @Override
    public final void accept(Context<?> ctx) {






        if (RowIsNull.EMULATE_NULL_ROW.contains(ctx.dialect()))
            ctx.visit(allNotNull(row.fields()));
        else
            acceptStandard(ctx);
    }

    private final void acceptStandard(Context<?> ctx) {
        ctx.visit(row);

        switch (ctx.family()) {






            default:
                ctx.sql(' ')
                   .visit(K_IS_NOT_NULL);
                break;
        }
    }

    @Override // Avoid AbstractCondition implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Row $arg1() {
        return row;
    }

    @Override
    public final Function1<? super Row, ? extends Condition> constructor() {
        return r -> new RowIsNotNull(r);
    }
}
