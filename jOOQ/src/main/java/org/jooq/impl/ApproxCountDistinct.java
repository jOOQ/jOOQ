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

import java.util.function.Function;


/**
 * The <code>APPROX COUNT DISTINCT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class ApproxCountDistinct
extends
    AbstractAggregateFunction<Integer, QOM.ApproxCountDistinct>
implements
    QOM.ApproxCountDistinct
{

    ApproxCountDistinct(
        Field<?> field
    ) {
        super(
            false,
            N_APPROX_COUNT_DISTINCT,
            INTEGER,
            nullSafeNotNull(field, OTHER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        super.accept(ctx);
    }

    @Override
    final boolean requiresDistinct(Context<?> ctx) {
        switch (ctx.family()) {













            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case IGNITE:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case YUGABYTEDB:
                return true;

            default:
                return false;
        }
    }

    @Override
    final void acceptFunctionName(Context<?> ctx) {
        switch (ctx.family()) {
            case CLICKHOUSE:
                ctx.visit(N_uniq);
                break;












            case TRINO:
                ctx.visit(N_APPROX_DISTINCT);
                break;













            case CUBRID:
            case DERBY:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case IGNITE:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case YUGABYTEDB:
                ctx.visit(N_COUNT);
                break;

            default:
                super.acceptFunctionName(ctx);
                break;
        }
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $field() {
        return getArgument(0);
    }

    @Override
    public final QOM.ApproxCountDistinct $field(Field<?> newValue) {
        return copyAggregateSpecification().apply($constructor().apply(newValue));
    }

    public final Function1<? super Field<?>, ? extends QOM.ApproxCountDistinct> $constructor() {
        return (a1) -> new ApproxCountDistinct(a1);
    }

    @Override
    final QOM.ApproxCountDistinct copyAggregateFunction(Function<? super QOM.ApproxCountDistinct, ? extends QOM.ApproxCountDistinct> function) {
        return function.apply($constructor().apply($field()));
    }






















    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ApproxCountDistinct o) {
            return
                Objects.equals($field(), o.$field())
            ;
        }
        else
            return super.equals(that);
    }
}
