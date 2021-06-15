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

import static org.jooq.impl.Keywords.K_DENSE_RANK;
import static org.jooq.impl.Keywords.K_FILTER;
import static org.jooq.impl.Keywords.K_FIRST;
import static org.jooq.impl.Keywords.K_KEEP;
import static org.jooq.impl.Keywords.K_LAST;
import static org.jooq.impl.Keywords.K_NULL;
import static org.jooq.impl.Keywords.K_ORDER_BY;
import static org.jooq.impl.Keywords.K_WHERE;
import static org.jooq.impl.Keywords.K_WITHIN_GROUP;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;

/**
 * A field that handles built-in functions, aggregate functions, and window
 * functions.
 *
 * @author Lukas Eder
 */
class DefaultAggregateFunction<T> extends AbstractAggregateFunction<T> {

    // -------------------------------------------------------------------------
    // XXX Constructors
    // -------------------------------------------------------------------------

    DefaultAggregateFunction(String name, DataType<T> type, Field<?>... arguments) {
        this(false, name, type, arguments);
    }

    DefaultAggregateFunction(Name name, DataType<T> type, Field<?>... arguments) {
        this(false, name, type, arguments);
    }

    DefaultAggregateFunction(boolean distinct, String name, DataType<T> type, Field<?>... arguments) {
        this(distinct, DSL.unquotedName(name), type, arguments);
    }

    DefaultAggregateFunction(boolean distinct, Name name, DataType<T> type, Field<?>... arguments) {
        super(distinct, name, type, arguments);
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public /* final */ void accept(Context<?> ctx) {
        toSQLArguments(ctx);
        acceptKeepDenseRankOrderByClause(ctx);
        acceptWithinGroupClause(ctx);
        acceptFilterClause(ctx);
        acceptOverClause(ctx);
    }

    /**
     * Render <code>KEEP (DENSE_RANK [FIRST | LAST] ORDER BY {...})</code> clause
     */
    private final void acceptKeepDenseRankOrderByClause(Context<?> ctx) {
        if (!Tools.isEmpty(keepDenseRankOrderBy)) {

            switch (ctx.family()) {






                default:
                    ctx.sql(' ').visit(K_KEEP)
                       .sql(" (").visit(K_DENSE_RANK)
                       .sql(' ').visit(first ? K_FIRST : K_LAST)
                       .sql(' ').visit(K_ORDER_BY)
                       .sql(' ').visit(keepDenseRankOrderBy)
                       .sql(')');
                    break;
            }
        }
    }

    /**
     * Render <code>WITHIN GROUP (ORDER BY ..)</code> clause
     */
    final void acceptWithinGroupClause(Context<?> ctx) {
        if (withinGroupOrderBy != null) {
            switch (ctx.family()) {






                default:
                    ctx.sql(' ').visit(K_WITHIN_GROUP)
                       .sql(" (").visit(K_ORDER_BY).sql(' ');

                    if (withinGroupOrderBy.isEmpty())
                        ctx.visit(K_NULL);
                    else
                        ctx.visit(withinGroupOrderBy);

                    ctx.sql(')');
                    break;
            }
        }
    }

    /**
     * Render function arguments and argument modifiers
     */
    private final void toSQLArguments(Context<?> ctx) {
        acceptFunctionName(ctx);
        ctx.sql('(');
        acceptArguments0(ctx);
        ctx.sql(')');
    }
}
