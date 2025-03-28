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
import java.util.Set;



/**
 * The <code>STARTS WITH</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class StartsWith<T>
extends
    AbstractCondition
implements
    QOM.StartsWith<T>
{

    final Field<T> string;
    final Field<T> prefix;

    StartsWith(
        Field<T> string,
        Field<T> prefix
    ) {

        this.string = nullableIf(false, Tools.nullSafe(string, prefix.getDataType()));
        this.prefix = nullableIf(false, Tools.nullSafe(prefix, string.getDataType()));
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {

















            case CUBRID:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case IGNITE:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case YUGABYTEDB:
                return false;



            case DERBY:
                return false;






            case DUCKDB:
            case TRINO:
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























            case CUBRID:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case IGNITE:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case YUGABYTEDB: {
                acceptLike(ctx);
                break;
            }



            case DERBY: {
                acceptPosition(ctx);
                break;
            }







            case DUCKDB:
            case TRINO:
                ctx.visit(function(N_STARTS_WITH, BOOLEAN, string, prefix));
                break;

            case CLICKHOUSE:
                ctx.visit(function(N_startsWith, BOOLEAN, string, prefix));
                break;

            default:
                ctx.visit(function(N_STARTS_WITH, BOOLEAN, string, prefix));
                break;
        }
    }










    private final void acceptLike(Context<?> ctx) {
        ctx.visit(string.like(DSL.concat(Tools.escapeForLike(prefix, ctx.configuration()), inline("%")), Tools.ESCAPE));
    }

    private final void acceptPosition(Context<?> ctx) {
        ctx.visit(DSL.position(Like.requiresStringCast(string), Like.requiresStringCast(prefix)).eq(inline(1)));
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return string;
    }

    @Override
    public final Field<T> $arg2() {
        return prefix;
    }

    @Override
    public final QOM.StartsWith<T> $arg1(Field<T> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.StartsWith<T> $arg2(Field<T> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<T>, ? extends QOM.StartsWith<T>> $constructor() {
        return (a1, a2) -> new StartsWith<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.StartsWith<?> o) {
            return
                StringUtils.equals($string(), o.$string()) &&
                StringUtils.equals($prefix(), o.$prefix())
            ;
        }
        else
            return super.equals(that);
    }
}
