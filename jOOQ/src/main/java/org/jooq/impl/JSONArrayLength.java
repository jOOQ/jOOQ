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
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>JSON ARRAY LENGTH</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class JSONArrayLength
extends
    AbstractField<Integer>
implements
    QOM.JSONArrayLength
{

    final Field<JSON> field;

    JSONArrayLength(
        Field<JSON> field
    ) {
        super(
            N_JSON_ARRAY_LENGTH,
            allNotNull(INTEGER, field)
        );

        this.field = nullSafeNotNull(field, JSON);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {








            case MARIADB:
            case MYSQL:
                return true;

            case CLICKHOUSE:
                return true;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
















            case MARIADB:
            case MYSQL:
                ctx.visit(function(N_JSON_LENGTH, getDataType(), field));
                break;

            case CLICKHOUSE:
                ctx.visit(function(N_JSONArrayLength, getDataType(), field));
                break;

            default:
                ctx.visit(function(N_JSON_ARRAY_LENGTH, getDataType(), field));
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
    public final QOM.JSONArrayLength $arg1(Field<JSON> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<JSON>, ? extends QOM.JSONArrayLength> $constructor() {
        return (a1) -> new JSONArrayLength(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONArrayLength o) {
            return
                StringUtils.equals($field(), o.$field())
            ;
        }
        else
            return super.equals(that);
    }
}
