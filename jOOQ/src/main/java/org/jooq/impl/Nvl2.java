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
 * The <code>NVL2</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Nvl2<T>
extends
    AbstractField<T>
implements
    QOM.Nvl2<T>
{

    final Field<?> value;
    final Field<T> ifNotNull;
    final Field<T> ifNull;

    Nvl2(
        Field<?> value,
        Field<T> ifNotNull,
        Field<T> ifNull
    ) {
        super(
            N_NVL2,
            nvl2NotNull((DataType) dataType(ifNotNull), value, ifNotNull, ifNull)
        );

        this.value = nullSafeNotNull(value, OTHER);
        this.ifNotNull = nullSafeNotNull(ifNotNull, (DataType) OTHER);
        this.ifNull = nullSafeNotNull(ifNull, (DataType) OTHER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {












            case CLICKHOUSE:
            case CUBRID:
            case DERBY:
            case DUCKDB:
            case FIREBIRD:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
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











            case MARIADB: {





                ctx.visit(function(N_NVL2, getDataType(), value, ifNotNull, ifNull));
                break;
            }











            case CLICKHOUSE:
            case CUBRID:
            case DERBY:
            case DUCKDB:
            case FIREBIRD:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case TRINO:
            case YUGABYTEDB:
                ctx.visit(DSL.when(value.isNotNull(), ifNotNull).otherwise(ifNull));
                break;

            default:
                ctx.visit(function(N_NVL2, getDataType(), value, ifNotNull, ifNull));
                break;
        }
    }















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $arg1() {
        return value;
    }

    @Override
    public final Field<T> $arg2() {
        return ifNotNull;
    }

    @Override
    public final Field<T> $arg3() {
        return ifNull;
    }

    @Override
    public final QOM.Nvl2<T> $arg1(Field<?> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.Nvl2<T> $arg2(Field<T> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.Nvl2<T> $arg3(Field<T> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<?>, ? super Field<T>, ? super Field<T>, ? extends QOM.Nvl2<T>> $constructor() {
        return (a1, a2, a3) -> new Nvl2<>(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Nvl2<?> o) {
            return
                Objects.equals($value(), o.$value()) &&
                Objects.equals($ifNotNull(), o.$ifNotNull()) &&
                Objects.equals($ifNull(), o.$ifNull())
            ;
        }
        else
            return super.equals(that);
    }
}

