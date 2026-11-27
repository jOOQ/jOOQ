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
 * The <code>TO BASE64</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ToBase64
extends
    AbstractField<String>
implements
    QOM.ToBase64
{

    final Field<byte[]> bytes;

    ToBase64(
        Field<byte[]> bytes
    ) {
        super(
            N_TO_BASE64,
            allNotNull(VARCHAR, bytes)
        );

        this.bytes = nullSafeNotNull(bytes, VARBINARY);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {



            case CLICKHOUSE:
            case FIREBIRD:
                return true;






            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case YUGABYTEDB:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {








            case MARIADB:
            case MYSQL:
                ctx.visit(DSL.replace(function(N_TO_BASE64, getDataType(), bytes), inline("\n")));
                break;



            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(DSL.replace(function(N_ENCODE, getDataType(), bytes, inline("base64")), inline("\n")));
                break;

            case CLICKHOUSE:
                ctx.visit(function(N_base64Encode, getDataType(), bytes));
                break;









            case FIREBIRD:
                ctx.visit(function(N_BASE64_ENCODE, getDataType(), bytes));
                break;







            default:
                ctx.visit(function(N_TO_BASE64, getDataType(), bytes));
                break;
        }
    }



















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<byte[]> $arg1() {
        return bytes;
    }

    @Override
    public final QOM.ToBase64 $arg1(Field<byte[]> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<byte[]>, ? extends QOM.ToBase64> $constructor() {
        return (a1) -> new ToBase64(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ToBase64 o) {
            return
                Objects.equals($bytes(), o.$bytes())
            ;
        }
        else
            return super.equals(that);
    }
}

