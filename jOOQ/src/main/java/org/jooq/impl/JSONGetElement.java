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
 * The <code>JSON GET ELEMENT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class JSONGetElement
extends
    AbstractField<JSON>
implements
    QOM.JSONGetElement
{

    final Field<JSON>    field;
    final Field<Integer> index;

    JSONGetElement(
        Field<JSON> field,
        Field<Integer> index
    ) {
        super(
            N_JSON_GET_ELEMENT,
            allNotNull(JSON, field, index)
        );

        this.field = nullSafeNotNull(field, JSON);
        this.index = nullSafeNotNull(index, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {



















            case MARIADB:
            case MYSQL:
            case TRINO:
                return false;

            case CLICKHOUSE:
                return false;

            default:
                return false;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






































            case MARIADB:
            case MYSQL:
            case TRINO:
                ctx.visit(function(N_JSON_EXTRACT, JSON, field, inline("$[").concat(index).concat(inline("]"))));
                break;

            case CLICKHOUSE:
                ctx.visit(function(N_JSONExtractRaw, JSON, field, iadd(index, one())));
                break;

            default:
                ctx.sql('(').visit(field).sql("->").visit(index).sql(')');
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<JSON> $arg1() {
        return field;
    }

    @Override
    public final Field<Integer> $arg2() {
        return index;
    }

    @Override
    public final QOM.JSONGetElement $arg1(Field<JSON> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.JSONGetElement $arg2(Field<Integer> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<JSON>, ? super Field<Integer>, ? extends QOM.JSONGetElement> $constructor() {
        return (a1, a2) -> new JSONGetElement(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONGetElement o) {
            return
                Objects.equals($field(), o.$field()) &&
                Objects.equals($index(), o.$index())
            ;
        }
        else
            return super.equals(that);
    }
}
