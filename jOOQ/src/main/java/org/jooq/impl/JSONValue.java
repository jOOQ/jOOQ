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

import static org.jooq.impl.JSONValue.Behaviour.DEFAULT;
import static org.jooq.impl.JSONValue.Behaviour.ERROR;
import static org.jooq.impl.JSONValue.Behaviour.NULL;
import static org.jooq.impl.Keywords.K_EMPTY;
import static org.jooq.impl.Keywords.K_ERROR;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Names.N_JSON_VALUE;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONValueDefaultStep;
import org.jooq.JSONValueOnStep;
import org.jooq.Keyword;


/**
 * The JSON value constructor.
 *
 * @author Lukas Eder
 */
final class JSONValue<J>
extends AbstractField<J>
implements
    JSONValueOnStep<J>,
    JSONValueDefaultStep<J> {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = 1772007627336725780L;

    private final Field<?>      json;
    private final Field<String> path;











    JSONValue(DataType<J> type, Field<?> json, Field<String> path) {
        this(type, json, path, null, null, null, null, null);
    }

    private JSONValue(
        DataType<J> type,
        Field<?> json,
        Field<String> path,
        Behaviour onError,
        Field<?> onErrorDefault,
        Behaviour onEmpty,
        Field<?> onEmptyDefault,
        Field<?> default_
    ) {
        super(N_JSON_VALUE, type);

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
        ctx.visit(N_JSON_VALUE).sql('(').visit(json).sql(", ").visit(path);











        ctx.sql(')');
    }



















    enum Behaviour {
        ERROR, NULL, DEFAULT;

        final Keyword keyword;

        Behaviour() {
            this.keyword = DSL.keyword(name().toLowerCase());
        }
    }
}
