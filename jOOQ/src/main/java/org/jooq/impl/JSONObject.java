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

import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.DSL.values;
import static org.jooq.impl.Keywords.K_AUTO;
import static org.jooq.impl.Keywords.K_FOR;
import static org.jooq.impl.Keywords.K_JSON;
import static org.jooq.impl.Keywords.K_JSON_OBJECT;
import static org.jooq.impl.Keywords.K_WITHOUT_ARRAY_WRAPPER;

import java.util.Collection;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONEntry;
import org.jooq.Name;


/**
 * The JSON array constructor.
 *
 * @author Lukas Eder
 */
final class JSONObject<J> extends AbstractField<J> {

    /**
     * Generated UID
     */
    private static final long                 serialVersionUID = 1772007627336725780L;
    private final QueryPartList<JSONEntry<?>> args;

    JSONObject(DataType<J> type, Collection<? extends JSONEntry<?>> args) {
        super(DSL.name("json_array"), type);

        this.args = new QueryPartList<>(args);
    }

    @Override
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case POSTGRES:
                ctx.visit(unquotedName("json_build_object")).sql('(').visit(args).sql(')');
                break;

































            default:
                ctx.visit(K_JSON_OBJECT).sql('(').visit(args).sql(')');
                break;
        }
    }
}
