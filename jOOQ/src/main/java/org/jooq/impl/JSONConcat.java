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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>JSON CONCAT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class JSONConcat
extends
    AbstractField<JSON>
implements
    QOM.JSONConcat
{

    final Field<JSON> json1;
    final Field<JSON> json2;

    JSONConcat(
        Field<JSON> json1,
        Field<JSON> json2
    ) {
        super(
            N_JSON_CONCAT,
            allNotNull(JSON, json1, json2)
        );

        this.json1 = nullSafeNotNull(json1, JSON);
        this.json2 = nullSafeNotNull(json2, JSON);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {







            case MARIADB:
            case MYSQL:
                ctx.visit(when(
                    function(N_JSON_TYPE, VARCHAR, json1).eq(inline("OBJECT")).and(function(N_JSON_TYPE, VARCHAR, json2).eq(inline("OBJECT"))),
                    function(N_JSON_MERGE_PATCH, getDataType(), json1, json2)
                ).else_(function(N_JSON_MERGE_PRESERVE, getDataType(), json1, json2)));
                break;

            case SQLITE:
                ctx.visit(DSL.field(
                    "case "
                  + "when {0} is null or {1} is null then null "
                  + "when json_type({0}) = 'object' and json_type({1}) = 'object' then ("
                  +   "select json_group_object(k, case when t in ('true', 'false') then json(t) when t in ('object', 'array') then json(v) else v end) "
                  +   "from ("
                  +     "select key k, type t, value v from json_each({0}) where key not in (select key from json_each({1})) "
                  +     "union all "
                  +     "select key, type, value from json_each({1})"
                  +   ") j"
                  + ") "
                  + "else ("
                  +   "select json_group_array(case when t in ('true', 'false') then json(t) when t in ('object', 'array') then json(v) else v end) "
                  +   "from ("
                  +     "select 0 g, key k, type t, value v from json_each(case when json_type({0}) = 'array' then {0} else json_array(json({0})) end) "
                  +     "union all "
                  +     "select 1, key, type, value from json_each(case when json_type({1}) = 'array' then {1} else json_array(json({1})) end) "
                  +     "order by g, k"
                  +   ") j"
                  + ") "
                  + "end",
                    getDataType(), json1, json2
                ));
                break;

            case DUCKDB:
                ctx.visit(DSL.field(
                    "case "
                  + "when {0} is null or {1} is null then null "
                  + "when json_type({0}) = 'OBJECT' and json_type({1}) = 'OBJECT' then json_merge_patch({0}, {1}) "
                  + "else to_json(list_concat("
                  +   "cast(case when json_type({0}) = 'ARRAY' then {0} else json_array({0}) end as json[]), "
                  +   "cast(case when json_type({1}) = 'ARRAY' then {1} else json_array({1}) end as json[])"
                  + ")) "
                  + "end",
                    getDataType(), json1, json2
                ));
                break;

            case TRINO:
                ctx.visit(DSL.field(
                    "case "
                  + "when {0} is null or {1} is null then null "
                  + "when json_array_length({0}) is null and not is_json_scalar({0}) and json_array_length({1}) is null and not is_json_scalar({1}) "
                  +   "then cast(map_concat(cast({0} as map(varchar, json)), cast({1} as map(varchar, json))) as json) "
                  + "else cast(concat("
                  +   "case when json_array_length({0}) is null then array[{0}] else cast({0} as array(json)) end, "
                  +   "case when json_array_length({1}) is null then array[{1}] else cast({1} as array(json)) end"
                  + ") as json) "
                  + "end",
                    getDataType(), json1, json2
                ));
                break;

            default:
                ctx.visit(jsonbConcat(json1.cast(JSONB), json2.cast(JSONB)).cast(JSON));
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<JSON> $arg1() {
        return json1;
    }

    @Override
    public final Field<JSON> $arg2() {
        return json2;
    }

    @Override
    public final QOM.JSONConcat $arg1(Field<JSON> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.JSONConcat $arg2(Field<JSON> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<JSON>, ? super Field<JSON>, ? extends QOM.JSONConcat> $constructor() {
        return (a1, a2) -> new JSONConcat(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONConcat o) {
            return
                Objects.equals($json1(), o.$json1()) &&
                Objects.equals($json2(), o.$json2())
            ;
        }
        else
            return super.equals(that);
    }
}

