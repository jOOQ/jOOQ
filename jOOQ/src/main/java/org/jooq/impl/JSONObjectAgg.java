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

import static org.jooq.impl.JSONNullClause.ABSENT_ON_NULL;
import static org.jooq.impl.JSONNullClause.NULL_ON_NULL;
import static org.jooq.impl.JSONObject.acceptJSONNullClause;
import static org.jooq.impl.Names.N_JSONB_OBJECT_AGG;
import static org.jooq.impl.Names.N_JSON_OBJECTAGG;
import static org.jooq.impl.Names.N_JSON_OBJECT_AGG;
import static org.jooq.impl.SQLDataType.JSON;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectAggNullStep;


/**
 * The JSON object constructor.
 *
 * @author Lukas Eder
 */
final class JSONObjectAgg<J>
extends AbstractAggregateFunction<J>
implements JSONObjectAggNullStep<J> {

    /**
     * Generated UID
     */
    private static final long  serialVersionUID = 1772007627336725780L;

    private final JSONEntry<?> entry;
    private JSONNullClause     nullClause;

    JSONObjectAgg(DataType<J> type, JSONEntry<?> entry) {
        super(false, N_JSON_OBJECTAGG, type, entry.key(), entry.value());

        this.entry = entry;
    }

    @Override
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {





            case POSTGRES:

                ctx.visit(getDataType() == JSON ? N_JSON_OBJECT_AGG : N_JSONB_OBJECT_AGG).sql('(');
                ctx.visit(entry);
                ctx.sql(')');

                // TODO: What about a user-defined filter clause?
                if (nullClause == ABSENT_ON_NULL)
                    acceptFilterClause(ctx, entry.value().isNotNull());

                break;

            default:
                ctx.visit(N_JSON_OBJECTAGG).sql('(');
                ctx.visit(entry);
                acceptJSONNullClause(ctx, nullClause);
                ctx.sql(')');
                break;
        }
    }

    @Override
    public final JSONObjectAgg<J> nullOnNull() {
        nullClause = NULL_ON_NULL;
        return this;
    }

    @Override
    public final JSONObjectAgg<J> absentOnNull() {
        nullClause = ABSENT_ON_NULL;
        return this;
    }
}
