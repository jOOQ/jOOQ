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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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
 * The <code>JSONB KEYS</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class JSONBKeys
extends
    AbstractField<JSONB>
implements
    QOM.JSONBKeys
{

    static final Set<SQLDialect> NO_SUPPORT_PATH_QUERY = SQLDialect.supportedUntil();

    final Field<JSONB> field;

    JSONBKeys(
        Field<JSONB> field
    ) {
        super(
            N_JSONB_KEYS,
            allNotNull(JSONB, field)
        );

        this.field = nullSafeNotNull(field, JSONB);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {


            case POSTGRES:
            case YUGABYTEDB:
                return false;









            case SQLITE:
                return false;

            default:
                return false;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {








            case POSTGRES:
            case YUGABYTEDB: {
                if (NO_SUPPORT_PATH_QUERY.contains(ctx.dialect()))
                    ctx.visit(DSL.field(select(DSL.coalesce(jsonArrayAgg(DSL.field(unquotedName("j"))), jsonArray())).from("json_object_keys({0}) as j(j)", field)));
                else
                    ctx.visit(function(N_JSONB_PATH_QUERY_ARRAY, getDataType(), field, inline("$.keyvalue().key")));
                break;
            }













            case SQLITE:
                ctx.visit(DSL.field(select(jsonbArrayAgg(DSL.field(name("key")))).from("json_each({0})", field)));
                break;

            default:
                ctx.visit(function(N_JSON_KEYS, JSONB, field));
                break;
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<JSONB> $arg1() {
        return field;
    }

    @Override
    public final QOM.JSONBKeys $arg1(Field<JSONB> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<JSONB>, ? extends QOM.JSONBKeys> $constructor() {
        return (a1) -> new JSONBKeys(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONBKeys o) {
            return
                Objects.equals($field(), o.$field())
            ;
        }
        else
            return super.equals(that);
    }
}
