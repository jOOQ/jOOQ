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
 * The <code>SHA1</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Sha1
extends
    AbstractField<String>
implements
    QOM.Sha1
{

    final Field<String> string;

    Sha1(
        Field<String> string
    ) {
        super(
            N_SHA1,
            allNotNull(VARCHAR, string)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------










    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {




































            // [#20565] PostgreSQL doesn't support SHA1 natively, only via the pgcrypto extension
            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(function(N_ENCODE, getDataType(), function(N_DIGEST, VARBINARY, string, inline("sha1")), inline("hex")));
                break;

            case CLICKHOUSE:
                ctx.visit(function(N_LOWER, getDataType(), function(N_HEX, getDataType(), function(systemName("SHA1"), VARBINARY, string))));
                break;

            case H2:
                ctx.visit(function(N_LOWER, getDataType(), function(N_RAWTOHEX, getDataType(), function(N_HASH, VARBINARY, inline("SHA-1"), string))));
                break;

            case TRINO:
                ctx.visit(function(N_LOWER, getDataType(), function(N_TO_HEX, getDataType(), function(N_SHA1, VARBINARY, function(N_TO_UTF8, VARBINARY, string)))));
                break;

            default:
                ctx.visit(function(N_SHA1, getDataType(), string));
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
    public final QOM.Sha1 $arg1(Field<String> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<String>, ? extends QOM.Sha1> $constructor() {
        return (a1) -> new Sha1(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Sha1 o) {
            return
                Objects.equals($string(), o.$string())
            ;
        }
        else
            return super.equals(that);
    }
}

