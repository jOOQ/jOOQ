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

import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.JSONNullClause.ABSENT_ON_NULL;
import static org.jooq.impl.JSONNullClause.NULL_ON_NULL;
import static org.jooq.impl.JSONObject.acceptJSONNullClause;
import static org.jooq.impl.Names.N_JSONB_AGG;
import static org.jooq.impl.Names.N_JSON_AGG;
import static org.jooq.impl.Names.N_JSON_ARRAYAGG;
import static org.jooq.impl.SQLDataType.JSON;

import java.util.Collection;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONArrayAggOrderByStep;
import org.jooq.OrderField;


/**
 * The JSON array constructor.
 *
 * @author Lukas Eder
 */
final class JSONArrayAgg<J>
extends AbstractAggregateFunction<J>
implements JSONArrayAggOrderByStep<J> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 1772007627336725780L;

    private JSONNullClause    nullClause;

    JSONArrayAgg(DataType<J> type, Field<?> arg) {
        super(false, N_JSON_ARRAYAGG, type, arg);
    }

    @Override
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case MARIADB:
            case MYSQL:

                // Workaround for https://jira.mariadb.org/browse/MDEV-21914
                if (!Tools.isEmpty(withinGroupOrderBy))
                    ctx.visit(DSL.concat(inline('['), groupConcat(arguments.get(0)).orderBy(withinGroupOrderBy), inline(']')));

                // Workaround for https://jira.mariadb.org/browse/MDEV-21912
                else
                    ctx.visit(DSL.concat(inline('['), groupConcat(arguments.get(0)), inline(']')));

                break;






            case POSTGRES:
                ctx.visit(getDataType() == JSON ? N_JSON_AGG : N_JSONB_AGG).sql('(');
                ctx.visit(arguments.get(0));
                acceptOrderBy(ctx);
                ctx.sql(')');

                // TODO: What about a user-defined filter clause?
                if (nullClause == ABSENT_ON_NULL)
                    acceptFilterClause(ctx, arguments.get(0).isNotNull());

                break;

            default:
                acceptStandard(ctx);
                break;
        }
    }

    private final void acceptStandard(Context<?> ctx) {
        ctx.visit(N_JSON_ARRAYAGG).sql('(');
        ctx.visit(arguments.get(0));
        acceptOrderBy(ctx);
        acceptJSONNullClause(ctx, nullClause);
        ctx.sql(')');
    }

    @Override
    public final JSONArrayAgg<J> nullOnNull() {
        nullClause = NULL_ON_NULL;
        return this;
    }

    @Override
    public final JSONArrayAgg<J> absentOnNull() {
        nullClause = ABSENT_ON_NULL;
        return this;
    }

    @Override
    public final JSONArrayAgg<J> orderBy(OrderField<?>... fields) {
        return (JSONArrayAgg<J>) super.orderBy(fields);
    }

    @Override
    public final JSONArrayAgg<J> orderBy(Collection<? extends OrderField<?>> fields) {
        return (JSONArrayAgg<J>) super.orderBy(fields);
    }
}
