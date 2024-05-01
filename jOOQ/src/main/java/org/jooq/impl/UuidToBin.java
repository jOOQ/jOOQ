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
import java.util.function.*;
import java.util.UUID;


/**
 * The <code>UUID TO BIN</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class UuidToBin
extends
    AbstractField<byte[]>
implements
    QOM.UuidToBin
{

    final Field<UUID> uuid;

    UuidToBin(
        Field<UUID> uuid
    ) {
        super(
            N_UUID_TO_BIN,
            allNotNull(VARBINARY, uuid)
        );

        this.uuid = nullSafeNotNull(uuid, UUID);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {
            case CLICKHOUSE:
                return false;



            case H2:
            case HSQLDB:
                return false;


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






            case CLICKHOUSE:
                ctx.visit(function(N_UUIDStringToNum, BINARY(16), DSL.cast(uuid, VARCHAR)));
                break;



            case H2:
            case HSQLDB:
                ctx.visit(DSL.cast(uuid, BINARY(16)));
                break;


            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(function(N_DECODE, UUID, DSL.replace(DSL.cast(uuid, VARCHAR), inline("-"), inline("")), inline("hex")));
                break;

            default:
                ctx.visit(function(N_UUID_TO_BIN, getDataType(), uuid));
                break;
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<UUID> $arg1() {
        return uuid;
    }

    @Override
    public final QOM.UuidToBin $arg1(Field<UUID> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<UUID>, ? extends QOM.UuidToBin> $constructor() {
        return (a1) -> new UuidToBin(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.UuidToBin o) {
            return
                StringUtils.equals($uuid(), o.$uuid())
            ;
        }
        else
            return super.equals(that);
    }
}
