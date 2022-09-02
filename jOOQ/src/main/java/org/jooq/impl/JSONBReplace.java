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
 * The <code>JSONB REPLACE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class JSONBReplace
extends
    AbstractField<JSONB>
implements
    QOM.JSONBReplace
{

    final Field<JSONB>  field;
    final Field<String> path;
    final Field<?>      value;

    JSONBReplace(
        Field<JSONB> field,
        Field<String> path,
        Field<?> value
    ) {
        super(
            N_JSONB_REPLACE,
            allNotNull(JSONB, field, path, value)
        );

        this.field = nullSafeNotNull(field, JSONB);
        this.path = nullSafeNotNull(path, VARCHAR);
        this.value = nullSafeNoConvertValNotNull(value, VARCHAR);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {


















            default:
                ctx.visit(function(N_JSON_REPLACE, JSONB, field, path, JSONEntryImpl.jsonCast(ctx, value, true)));
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
    public final Field<String> $path() {
        return path;
    }

    @Override
    public final Field<?> $value() {
        return value;
    }

    @Override
    public final QOM.JSONBReplace $field(Field<JSONB> newValue) {
        return $constructor().apply(newValue, $path(), $value());
    }

    @Override
    public final QOM.JSONBReplace $path(Field<String> newValue) {
        return $constructor().apply($field(), newValue, $value());
    }

    @Override
    public final QOM.JSONBReplace $value(Field<?> newValue) {
        return $constructor().apply($field(), $path(), newValue);
    }

    public final Function3<? super Field<JSONB>, ? super Field<String>, ? super Field<?>, ? extends QOM.JSONBReplace> $constructor() {
        return (a1, a2, a3) -> new JSONBReplace(a1, a2, a3);
    }


























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONBReplace o) {
            return
                StringUtils.equals($field(), o.$field()) &&
                StringUtils.equals($path(), o.$path()) &&
                StringUtils.equals($value(), o.$value())
            ;
        }
        else
            return super.equals(that);
    }
}
