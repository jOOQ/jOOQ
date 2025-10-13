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

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.jsonGetAttribute;
import static org.jooq.impl.DSL.systemName;
import static org.jooq.impl.JSONExists.Behaviour.ERROR;
import static org.jooq.impl.JSONExists.Behaviour.FALSE;
import static org.jooq.impl.JSONExists.Behaviour.TRUE;
import static org.jooq.impl.JSONExists.Behaviour.UNKNOWN;
import static org.jooq.impl.Keywords.K_ERROR;
import static org.jooq.impl.Keywords.K_JSON_EXISTS;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Names.N_JSONB_PATH_EXISTS;
import static org.jooq.impl.Names.N_JSON_CONTAINS_PATH;
import static org.jooq.impl.Names.N_JSON_QUERY;
import static org.jooq.impl.Names.N_JSON_TYPE;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.Tools.castIfNeeded;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.JSONExistsOnStep;
import org.jooq.Keyword;
import org.jooq.conf.ParamType;
import org.jooq.impl.QOM.UNotYetImplemented;


/**
 * The JSON value constructor.
 *
 * @author Lukas Eder
 */
final class JSONExists extends AbstractCondition implements JSONExistsOnStep, UNotYetImplemented {

    private final Field<?>      json;
    private final Field<String> path;





    JSONExists(Field<?> json, Field<String> path) {
        this(json, path, null);
    }

    private JSONExists(
        Field<?> json,
        Field<String> path,
        Behaviour onError
    ) {

        this.json = json;
        this.path = path;




    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

























    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {








            case MYSQL:
                ctx.visit(N_JSON_CONTAINS_PATH).sql('(').visit(json).sql(", 'one', ").visit(path).sql(')');
                break;

            case SQLITE:
                ctx.visit(function(N_JSON_TYPE, SQLDataType.JSON, json, path).isNotNull());
                break;

            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(N_JSONB_PATH_EXISTS).sql('(')
                   .visit(castIfNeeded(json, JSONB)).sql(", ");
                Cast.renderCast(ctx, c -> c.visit(path), c -> c.visit(Names.N_JSONPATH));
                ctx.sql(')');
                break;

            case CLICKHOUSE:
                ctx.visit(function(systemName("JSON_EXISTS"), getDataType(), json, path));
                break;

            case DUCKDB:
                ctx.visit(jsonGetAttribute((Field) json, path).isNotNull());
                break;

            default:
                ctx.visit(K_JSON_EXISTS).sql('(').visit(json).sql(", ");






                ctx.visit(path);






                ctx.sql(')');
                break;
        }
    }

    enum Behaviour {
        ERROR, TRUE, FALSE, UNKNOWN;

        final Keyword keyword;

        Behaviour() {
            this.keyword = DSL.keyword(name().toLowerCase());
        }
    }
}
