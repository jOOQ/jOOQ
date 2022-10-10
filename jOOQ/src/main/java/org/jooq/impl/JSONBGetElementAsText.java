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
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>JSONB GET ELEMENT AS TEXT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class JSONBGetElementAsText
extends
    AbstractField<String>
implements
    QOM.JSONBGetElementAsText
{

    final Field<JSONB>   field;
    final Field<Integer> index;

    JSONBGetElementAsText(
        Field<JSONB> field,
        Field<Integer> index
    ) {
        super(
            N_JSONB_GET_ELEMENT_AS_TEXT,
            allNotNull(VARCHAR, field, index)
        );

        this.field = nullSafeNotNull(field, JSONB);
        this.index = nullSafeNotNull(index, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {





























            case MYSQL:
                ctx.visit(function(N_JSON_UNQUOTE, JSONB, DSL.nullif(function(N_JSON_EXTRACT, JSONB, field, inline("$[").concat(index).concat(inline("]"))), inline("null").cast(JSONB))));
                break;

            case MARIADB:
                ctx.visit(function(N_JSON_UNQUOTE, JSONB, DSL.nullif(function(N_JSON_EXTRACT, JSONB, field, inline("$[").concat(index).concat(inline("]"))).cast(VARCHAR), inline("null"))));
                break;

            case SQLITE:
                ctx.visit(function(N_JSON_EXTRACT, JSONB, field, inline("$[").concat(index).concat(inline("]"))));
                break;

            default:
                ctx.sql('(').visit(field).sql("->>").visit(index).sql(')');
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<JSONB> $field() {
        return field;
    }

    @Override
    public final Field<Integer> $index() {
        return index;
    }

    @Override
    public final QOM.JSONBGetElementAsText $field(Field<JSONB> newValue) {
        return $constructor().apply(newValue, $index());
    }

    @Override
    public final QOM.JSONBGetElementAsText $index(Field<Integer> newValue) {
        return $constructor().apply($field(), newValue);
    }

    public final Function2<? super Field<JSONB>, ? super Field<Integer>, ? extends QOM.JSONBGetElementAsText> $constructor() {
        return (a1, a2) -> new JSONBGetElementAsText(a1, a2);
    }
























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONBGetElementAsText o) {
            return
                StringUtils.equals($field(), o.$field()) &&
                StringUtils.equals($index(), o.$index())
            ;
        }
        else
            return super.equals(that);
    }
}
