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
 * The <code>SHA2</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Sha2
extends
    AbstractField<String>
implements
    QOM.Sha2
{

    final Field<String> string;
    final int           bitLength;

    Sha2(
        Field<String> string,
        int bitLength
    ) {
        super(
            N_SHA2,
            allNotNull(VARCHAR, string)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.bitLength = bitLength;
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



























            case DUCKDB:
                ctx.visit(function(systemName("sha" + bitLength), getDataType(), string));
                break;


            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(function(N_ENCODE, getDataType(), function(systemName("sha" + bitLength), VARBINARY, function(N_CONVERT_TO, VARBINARY, string, inline("UTF8"))), inline("hex")));
                break;

            case CLICKHOUSE:
                ctx.visit(function(N_LOWER, getDataType(), function(N_HEX, getDataType(), function(systemName("SHA" + bitLength), VARBINARY, string))));
                break;

            case H2:
                ctx.visit(function(N_LOWER, getDataType(), function(N_RAWTOHEX, getDataType(), function(N_HASH, VARBINARY, inline("SHA-" + bitLength), string))));
                break;

            case TRINO:
                ctx.visit(function(N_LOWER, getDataType(), function(N_TO_HEX, getDataType(), function(systemName("sha" + bitLength), VARBINARY, function(N_TO_UTF8, VARBINARY, string)))));
                break;

            default:
                ctx.visit(function(N_SHA2, getDataType(), string, inline(bitLength)));
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $arg1() {
        return string;
    }

    @Override
    public final Integer $arg2() {
        return bitLength;
    }

    @Override
    public final QOM.Sha2 $arg1(Field<String> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.Sha2 $arg2(Integer newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<String>, ? super Integer, ? extends QOM.Sha2> $constructor() {
        return (a1, a2) -> new Sha2(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Sha2 o) {
            return
                Objects.equals($string(), o.$string()) &&
                Objects.equals($bitLength(), o.$bitLength())
            ;
        }
        else
            return super.equals(that);
    }
}

