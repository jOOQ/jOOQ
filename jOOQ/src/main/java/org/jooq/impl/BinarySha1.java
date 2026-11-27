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
 * The <code>BINARY SHA1</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BinarySha1
extends
    AbstractField<byte[]>
implements
    QOM.BinarySha1
{

    final Field<byte[]> bytes;

    BinarySha1(
        Field<byte[]> bytes
    ) {
        super(
            N_BINARY_SHA1,
            allNotNull(VARBINARY, bytes)
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
            case DUCKDB:
            case H2:
            case POSTGRES:
            case TRINO:
            case YUGABYTEDB:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
































            case POSTGRES:
            case YUGABYTEDB: {
                // [jOOQ/jOOQ#20565] PostgreSQL doesn't support SHA1 natively, only via the pgcrypto extension
                ctx.visit(function(N_ENCODE, getDataType(), function(N_DIGEST, VARBINARY, bytes, inline("sha1")), inline("hex")));
                break;
            }

            case CLICKHOUSE:
                ctx.visit(function(N_LOWER, getDataType(), function(N_HEX, getDataType(), function(systemName("SHA1"), VARBINARY, bytes))));
                break;

            case DUCKDB: {
                // [jOOQ/jOOQ#20565] DuckDB's SHA functions return VARCHAR, which the JDBC driver can't read as byte[]
                ctx.visit(function(N_ENCODE, getDataType(), function(N_SHA1, VARCHAR, bytes)));
                break;
            }

            case H2:
                ctx.visit(function(N_LOWER, getDataType(), function(N_RAWTOHEX, getDataType(), function(N_HASH, VARBINARY, inline("SHA-1"), bytes))));
                break;

            case TRINO:
                ctx.visit(function(N_LOWER, getDataType(), function(N_TO_HEX, getDataType(), function(N_SHA1, VARBINARY, bytes))));
                break;

            default:
                ctx.visit(function(N_SHA1, getDataType(), bytes));
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
    public final QOM.BinarySha1 $arg1(Field<byte[]> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<byte[]>, ? extends QOM.BinarySha1> $constructor() {
        return (a1) -> new BinarySha1(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BinarySha1 o) {
            return
                Objects.equals($bytes(), o.$bytes())
            ;
        }
        else
            return super.equals(that);
    }
}

