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
 * The <code>BOOL OR</code> statement.
 */
@SuppressWarnings({ "unused" })
final class BoolOr
extends
    AbstractAggregateFunction<Boolean, BoolOr>
implements
    QOM.BoolOr
{

    BoolOr(
        Condition condition
    ) {
        super(
            false,
            N_BOOL_OR,
            BOOLEAN,
            DSL.field(condition)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> EMULATE  = SQLDialect.supportedBy(CLICKHOUSE, CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, SQLITE);

    @Override
    final void acceptFunctionName(Context<?> ctx) {
        switch (ctx.family()) {
















            default:
                super.acceptFunctionName(ctx);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    final Condition condition() {
        return DSL.condition((Field<Boolean>) getArguments().get(0));
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (EMULATE.contains(ctx.dialect()))
            ctx.visit(DSL.field(fo(DSL.max(DSL.when(condition(), one()).otherwise(zero()))).eq(one())));
        else
            super.accept(ctx);
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Condition $condition() {
        return DSL.condition((Field<Boolean>) getArgument(0));
    }

    @Override
    public final QOM.BoolOr $condition(Condition newValue) {
        return $constructor().apply(newValue);
    }

    public final Function1<? super Condition, ? extends QOM.BoolOr> $constructor() {
        return (a1) -> new BoolOr(a1);
    }

    @Override
    final BoolOr copy2(Function<BoolOr, BoolOr> function) {
        return function.apply((BoolOr) $constructor().apply($condition()));
    }























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BoolOr o) {
            return
                Objects.equals($condition(), o.$condition())
            ;
        }
        else
            return super.equals(that);
    }
}
