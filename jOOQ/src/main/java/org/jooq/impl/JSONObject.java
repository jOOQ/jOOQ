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

import org.jetbrains.annotations.*;


import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.DSL.values;
import static org.jooq.impl.JSONNull.JSONNullType.ABSENT_ON_NULL;
import static org.jooq.impl.JSONNull.JSONNullType.NULL_ON_NULL;
import static org.jooq.impl.Keywords.K_JSON_OBJECT;
import static org.jooq.impl.Names.N_JSON_MERGE;
import static org.jooq.impl.Names.N_JSON_OBJECT;
import static org.jooq.impl.Names.N_T;
import static org.jooq.impl.QueryPartListView.wrap;

import java.util.Collection;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectNullStep;
import org.jooq.Name;
// ...
import org.jooq.impl.JSONNull.JSONNullType;


/**
 * The JSON array constructor.
 *
 * @author Lukas Eder
 */
final class JSONObject<J> extends AbstractField<J> implements JSONObjectNullStep<J> {

    /**
     * Generated UID
     */
    private static final long                 serialVersionUID          = 1772007627336725780L;

    private final QueryPartList<JSONEntry<?>> args;
    private final JSONNullType                nullType;

    JSONObject(DataType<J> type, Collection<? extends JSONEntry<?>> args) {
        this(type, args, null);
    }

    JSONObject(DataType<J> type, Collection<? extends JSONEntry<?>> args, JSONNullType nullType) {
        super(N_JSON_OBJECT, type);

        this.args = new QueryPartList<>(args);
        this.nullType = nullType;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final JSONObject<J> nullOnNull() {
        return new JSONObject<>(getDataType(), args, NULL_ON_NULL);
    }

    @Override
    public final JSONObject<J> absentOnNull() {
        return new JSONObject<>(getDataType(), args, ABSENT_ON_NULL);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {





            case POSTGRES:
                if (nullType == ABSENT_ON_NULL)
                    ctx.visit(unquotedName(getDataType().getType() == JSONB.class ? "jsonb_strip_nulls" : "json_strip_nulls")).sql('(');

                ctx.visit(unquotedName(getDataType().getType() == JSONB.class ? "jsonb_build_object" : "json_build_object")).sql('(').visit(args).sql(')');

                if (nullType == ABSENT_ON_NULL)
                    ctx.sql(')');

                break;









































            case MARIADB:

                // Workaround for https://jira.mariadb.org/browse/MDEV-13701
                if (args.size() > 1) {
                    ctx.visit(N_JSON_MERGE).sql('(').visit(inline("{}"))
                       .formatIndentStart();

                    for (JSONEntry<?> entry : args)
                        ctx.sql(',').formatSeparator().visit(jsonObject(entry));

                    ctx.formatIndentEnd()
                       .formatNewLine()
                       .sql(')');
                }
                else
                    acceptStandard(ctx);

                break;

            default:
                acceptStandard(ctx);
                break;
        }
    }

    private final void acceptStandard(Context<?> ctx) {
        JSONNull jsonNull;

        // Workaround for https://github.com/h2database/h2database/issues/2496
        if (args.isEmpty() && ctx.family() == H2)
            jsonNull = new JSONNull(NULL_ON_NULL);





        else
            jsonNull = new JSONNull(nullType);

        ctx.visit(K_JSON_OBJECT).sql('(').visit(wrap(args, jsonNull).separator("")).sql(')');
    }
}
