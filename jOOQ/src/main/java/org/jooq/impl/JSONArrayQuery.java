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
import org.jooq.impl.QOM.JSONOnNull;
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
 * The <code>JSON ARRAY</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class JSONArrayQuery<T>
extends
    AbstractField<T>
implements
    QOM.JSONArrayQuery<T>,
    JSONArrayQueryNullStep<T>,
    JSONArrayQueryReturningStep<T>
{

    final DataType<T>                  type;
    final Select<? extends Record1<?>> select;
          JSONOnNull                   onNull;
          DataType<?>                  returning;

    JSONArrayQuery(
        DataType<T> type,
        Select<? extends Record1<?>> select
    ) {
        this(
            type,
            select,
            null,
            null
        );
    }

    JSONArrayQuery(
        DataType<T> type,
        Select<? extends Record1<?>> select,
        JSONOnNull onNull,
        DataType<?> returning
    ) {
        super(
            N_JSON_ARRAY,
            type
        );

        this.type = type;
        this.select = select;
        this.onNull = onNull;
        this.returning = returning;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final JSONArrayQuery<T> nullOnNull() {
        this.onNull = JSONOnNull.NULL_ON_NULL;
        return this;
    }

    @Override
    public final JSONArrayQuery<T> absentOnNull() {
        this.onNull = JSONOnNull.ABSENT_ON_NULL;
        return this;
    }

    @Override
    public final JSONArrayQuery<T> returning(DataType<?> returning) {
        this.returning = returning;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean isNullable() {
        return false;
    }



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {










            case POSTGRES:
            case YUGABYTEDB: {
                ctx.visit(N_COALESCE).sqlIndentStart('(');
                acceptStandard(ctx);
                ctx.sql(',').formatSeparator()
                   .visit(empty())
                   .sqlIndentEnd(')');
                break;
            }




            case DUCKDB:
            case MARIADB:
            case MYSQL:
            case SQLITE:
            case TRINO: {
                acceptJSONArrayAgg(ctx);
                break;
            }

            default: {
                acceptStandard(ctx);
                break;
            }
        }
    }

    private final boolean json() {
        return getDataType().getFromType() == JSON.class;
    }

    private final Field<?> empty() {
        return inline(json() ? org.jooq.JSON.json("[]") : org.jooq.JSONB.jsonb("[]"));
    }

    private final Field<?> jsonArrayAgg(Field<?> arg) {
        return json() ? DSL.jsonArrayAgg(arg) : DSL.jsonbArrayAgg(arg);
    }

    private final void acceptJSONArrayAgg(Context<?> ctx) {
        List<Field<?>> s = select.getSelect();
        DataType<?> t = s.size() <= 1 ? s.get(0).getDataType() : SQLDataType.OTHER;
        Field<?> f = DSL.field(select(jsonArrayAgg(DSL.field(N_A, t))).from(select.asTable(N_T, N_A)));

        switch (ctx.family()) {


            case DUCKDB:
            case MARIADB:
            case MYSQL:
            case TRINO:
                ctx.visit(DSL.nvl(f, empty()));
                break;

            default:
                ctx.visit(f);
                break;
        }
    }

    private final void acceptStandard(Context<?> ctx) {
        ctx.visit(N_JSON_ARRAY).sqlIndentStart('(');

        switch (ctx.family()) {
            case H2: {
                // Work around this limitation: https://github.com/h2database/h2database/issues/4325
                SelectQueryImpl<Record> s = Tools.selectQueryImpl(select);
                if (s != null && s.hasUnions())
                    ctx.visit(selectFrom(select.asTable(N_T)));
                else
                    ctx.visit(select);

                break;
            }

            default: {
                ctx.visit(select);
                break;
            }
        }

        switch (ctx.family()) {


            case POSTGRES:
            case YUGABYTEDB: {
                ctx.formatSeparator().visit(K_RETURNING).sql(' ');

                if (returning == null)
                    ctx.visit(getDataType());
                else
                    ctx.visit(returning);

                break;
            }












            default: {
                if (returning != null)
                    ctx.formatSeparator().visit(K_RETURNING).sql(' ').visit(returning);

                break;
            }
        }
        ctx.sqlIndentEnd(')');
    }


















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final DataType<T> $arg1() {
        return type;
    }

    @Override
    public final Select<? extends Record1<?>> $arg2() {
        return select;
    }

    @Override
    public final JSONOnNull $arg3() {
        return onNull;
    }

    @Override
    public final DataType<?> $arg4() {
        return returning;
    }

    @Override
    public final QOM.JSONArrayQuery<T> $arg1(DataType<T> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3(), $arg4());
    }

    @Override
    public final QOM.JSONArrayQuery<T> $arg2(Select<? extends Record1<?>> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3(), $arg4());
    }

    @Override
    public final QOM.JSONArrayQuery<T> $arg3(JSONOnNull newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue, $arg4());
    }

    @Override
    public final QOM.JSONArrayQuery<T> $arg4(DataType<?> newValue) {
        return $constructor().apply($arg1(), $arg2(), $arg3(), newValue);
    }

    @Override
    public final Function4<? super DataType<T>, ? super Select<? extends Record1<?>>, ? super JSONOnNull, ? super DataType<?>, ? extends QOM.JSONArrayQuery<T>> $constructor() {
        return (a1, a2, a3, a4) -> new JSONArrayQuery<>(a1, a2, a3, a4);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONArrayQuery<?> o) {
            return
                Objects.equals($type(), o.$type()) &&
                Objects.equals($select(), o.$select()) &&
                Objects.equals($onNull(), o.$onNull()) &&
                Objects.equals($returning(), o.$returning())
            ;
        }
        else
            return super.equals(that);
    }
}
