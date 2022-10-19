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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inlined;
import static org.jooq.impl.JSONValue.Behaviour.DEFAULT;
import static org.jooq.impl.JSONValue.Behaviour.ERROR;
import static org.jooq.impl.JSONValue.Behaviour.NULL;
import static org.jooq.impl.Keywords.K_EMPTY;
import static org.jooq.impl.Keywords.K_ERROR;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Names.N_JSONB_PATH_QUERY_FIRST;
import static org.jooq.impl.Names.N_JSON_EXTRACT;
import static org.jooq.impl.Names.N_JSON_VALUE;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.Tools.castIfNeeded;
import static org.jooq.impl.Tools.isSimple;

import java.util.Set;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONValueDefaultStep;
import org.jooq.JSONValueOnStep;
import org.jooq.Keyword;
// ...
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.UNotYetImplemented;


/**
 * The JSON value constructor.
 *
 * @author Lukas Eder
 */
final class JSONValue<J>
extends AbstractField<J>
implements
    JSONValueOnStep<J>,
    JSONValueDefaultStep<J>,
    UNotYetImplemented {





    private final Field<?>       json;
    private final Field<String>  path;
    private final DataType<?>    returning;
    private final Behaviour      onError;
    private final Field<?>       onErrorDefault;
    private final Behaviour      onEmpty;
    private final Field<?>       onEmptyDefault;
    private final Field<?>       default_;

    JSONValue(DataType<J> type, Field<?> json, Field<String> path, DataType<?> returning) {
        this(type, json, path, returning, null, null, null, null, null);
    }

    private JSONValue(
        DataType<J> type,
        Field<?> json,
        Field<String> path,
        DataType<?> returning,
        Behaviour onError,
        Field<?> onErrorDefault,
        Behaviour onEmpty,
        Field<?> onEmptyDefault,
        Field<?> default_
    ) {
        super(N_JSON_VALUE, type);

        this.json = json;
        this.path = path;
        this.returning = returning;
        this.onError = onError;
        this.onErrorDefault = onErrorDefault;
        this.onEmpty = onEmpty;
        this.onEmptyDefault = onEmptyDefault;
        this.default_ = default_;

    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------








































    @Override
    public final JSONValue<J> returning(DataType<?> r) {
        return new JSONValue<>(getDataType(), json, path, r, onError, onErrorDefault, onEmpty, onEmptyDefault, null);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

            case MYSQL:
            case SQLITE:
                ctx.visit(function(N_JSON_EXTRACT, json.getDataType(), json, path));
                break;


            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(function(N_JSONB_PATH_QUERY_FIRST, json.getDataType(), castIfNeeded(json, JSONB), DSL.field("cast({0} as jsonpath)", path)));
                break;

            default: {
                boolean format = !isSimple(ctx, json, path);

                ctx.visit(N_JSON_VALUE).sql('(');

                if (format)
                    ctx.sqlIndentStart();

                ctx.visit(json).sql(",");

                if (format)
                    ctx.formatSeparator();
                else
                    ctx.sql(' ');






                ctx.visit(path);











                if (returning != null) {
                    JSONReturning r = new JSONReturning(returning);

                    if (r.rendersContent(ctx)) {
                        if (format)
                            ctx.formatNewLine();

                        ctx.separatorRequired(true).visit(r);
                    }
                }

                if (format)
                    ctx.sqlIndentEnd();

                ctx.sql(')');
                break;
            }
        }
    }






























    enum Behaviour {
        ERROR, NULL, DEFAULT;

        final Keyword keyword;

        Behaviour() {
            this.keyword = DSL.keyword(name().toLowerCase());
        }
    }
}
