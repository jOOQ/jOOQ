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

// ...
// ...
// ...
import static org.jooq.SQLDialect.DUCKDB;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Names.N_LISTAGG;
import static org.jooq.impl.Names.N_STRING_AGG;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.SQLDataType.VARBINARY;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.util.Set;
import java.util.function.Function;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function2;
import org.jooq.QueryPart;
// ...
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class BinaryListAgg
extends
    AbstractAggregateFunction<byte[], QOM.BinaryListAgg>
implements
    QOM.BinaryListAgg
{

    static final Set<SQLDialect> SUPPORT_STRING_AGG           = SQLDialect.supportedBy(DUCKDB, POSTGRES);
    static final Field<byte[]>   DEFAULT_SEPARATOR            = DSL.inline(",".getBytes());

    BinaryListAgg(boolean distinct, Field<?> arg) {
        super(distinct, N_LISTAGG, VARBINARY, arg);
    }

    BinaryListAgg(boolean distinct, Field<?> arg, Field<byte[]> separator) {
        super(distinct, N_LISTAGG, VARBINARY, arg, separator);
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        if (emulateWindowAggregateOrderBy(ctx)) {
            acceptWindowAggregateOrderByEmulation(ctx);
        }
        else if (SUPPORT_STRING_AGG.contains(ctx.dialect())) {
            acceptStringAgg(ctx);
            acceptFilterClause(ctx);
            acceptOverClause(ctx);
        }
        else
            super.accept(ctx);
    }

    @Override
    final void acceptFunctionName(Context<?> ctx) {





        super.acceptFunctionName(ctx);
    }

    @Override
    final boolean applyFilterToArgument(Context<?> ctx, Field<?> arg, int i) {
        return i == 0;
    }

    /**
     * [#1275] <code>LIST_AGG</code> emulation for Postgres, Sybase
     */
    private final void acceptStringAgg(Context<?> ctx) {
        ctx.visit(N_STRING_AGG);
        ctx.sql('(');

        // The explicit cast is needed in Postgres
        QueryPartListView<Field<?>> args = wrap(
              arguments.get(0).getDataType().isBinary()
            ? arguments.get(0)
            : arguments.get(0).getDataType().isString()
            ? arguments.get(0).cast(VARBINARY)
            : arguments.get(0).cast(VARCHAR).cast(VARBINARY)
        );
        acceptArguments1(ctx, args);

        if (arguments.size() > 1)
            ctx.sql(", ").visit(arguments.get(1));
        else
            ctx.sql(", ").visit(inline("".getBytes()));

        acceptOrderBy(ctx);
        ctx.sql(')');
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<?> $arg1() {
        return getArgument(0);
    }

    @Override
    public final QOM.BinaryListAgg $arg1(Field<?> newArg1) {
        return copyAggregateSpecification().apply($constructor().apply(newArg1, $arg2()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Field<byte[]> $arg2() {
        return (Field<byte[]>) getArgument(1);
    }

    @Override
    public final QOM.BinaryListAgg $arg2(Field<byte[]> newArg2) {
        return copyAggregateSpecification().apply($constructor().apply($arg1(), newArg2));
    }

    @Override
    public final Function2<? super Field<?>, ? super Field<byte[]>, ? extends QOM.BinaryListAgg> $constructor() {
        return (a1, a2) -> new BinaryListAgg(distinct, a1, a2);
    }

    @Override
    final QOM.BinaryListAgg copyAggregateFunction(Function<? super QOM.BinaryListAgg, ? extends QOM.BinaryListAgg> function) {
        return function.apply($constructor().apply($arg1(), $arg2()));
    }

























}
