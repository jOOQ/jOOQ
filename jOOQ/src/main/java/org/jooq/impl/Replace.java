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
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>REPLACE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Replace
extends
    AbstractField<String>
implements
    QOM.Replace
{

    final Field<String> string;
    final Field<String> search;
    final Field<String> replace;

    Replace(
        Field<String> string,
        Field<String> search
    ) {
        super(
            N_REPLACE,
            allNotNull(VARCHAR, string, search)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.search = nullSafeNotNull(search, VARCHAR);
        this.replace = null;
    }

    Replace(
        Field<String> string,
        Field<String> search,
        Field<String> replace
    ) {
        super(
            N_REPLACE,
            allNotNull(VARCHAR, string, search, replace)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.search = nullSafeNotNull(search, VARCHAR);
        this.replace = nullSafeNotNull(replace, VARCHAR);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {

        // [#861] Most dialects don't ship with a two-argument replace function:
        switch (ctx.family()) {
































            case FIREBIRD:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case YUGABYTEDB:
                if (replace == null)
                    ctx.visit(function(N_REPLACE, VARCHAR, string, search, inline("")));
                else
                    ctx.visit(function(N_REPLACE, VARCHAR, string, search, replace));

                return;

            default:
                if (replace == null)
                    ctx.visit(function(N_REPLACE, VARCHAR, string, search));
                else
                    ctx.visit(function(N_REPLACE, VARCHAR, string, search, replace));

                return;
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
    public final Field<String> $arg2() {
        return search;
    }

    @Override
    public final Field<String> $arg3() {
        return replace;
    }

    @Override
    public final QOM.Replace $arg1(Field<String> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.Replace $arg2(Field<String> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.Replace $arg3(Field<String> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<String>, ? super Field<String>, ? super Field<String>, ? extends QOM.Replace> $constructor() {
        return (a1, a2, a3) -> new Replace(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Replace o) {
            return
                StringUtils.equals($string(), o.$string()) &&
                StringUtils.equals($search(), o.$search()) &&
                StringUtils.equals($replace(), o.$replace())
            ;
        }
        else
            return super.equals(that);
    }
}
