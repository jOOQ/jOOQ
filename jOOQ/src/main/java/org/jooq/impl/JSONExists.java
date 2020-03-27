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

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.JSONExists.Behaviour.ERROR;
import static org.jooq.impl.JSONExists.Behaviour.FALSE;
import static org.jooq.impl.JSONExists.Behaviour.TRUE;
import static org.jooq.impl.JSONExists.Behaviour.UNKNOWN;
import static org.jooq.impl.JSONValue.NO_SUPPORT_PATH_BINDS;
import static org.jooq.impl.Keywords.K_ERROR;
import static org.jooq.impl.Keywords.K_JSON_EXISTS;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Names.N_JSONB_PATH_EXISTS;
import static org.jooq.impl.Names.N_JSON_CONTAINS_PATH;
import static org.jooq.impl.SQLDataType.JSONB;
import static org.jooq.impl.Tools.castIfNeeded;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.JSONExistsOnStep;
import org.jooq.Keyword;
import org.jooq.conf.ParamType;


/**
 * The JSON value constructor.
 *
 * @author Lukas Eder
 */
final class JSONExists extends AbstractCondition implements JSONExistsOnStep {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = 1772007627336725780L;

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

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {



            case MYSQL:
                ctx.visit(N_JSON_CONTAINS_PATH).sql('(').visit(json).sql(", 'one', ").visit(path).sql(')');
                break;

            case POSTGRES:
                ctx.visit(N_JSONB_PATH_EXISTS).sql('(').visit(castIfNeeded(json, JSONB)).sql(", ").visit(path).sql("::jsonpath)");
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
