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
import org.jooq.impl.QOM.JSONQueryBehavior;
import org.jooq.impl.QOM.JSONQueryBehavior;
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
 * The <code>JSON QUERY</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class JSONQuery<T>
extends
    AbstractField<T>
implements
    QOM.JSONQuery<T>,
    JSONQueryOnStep<T>,
    JSONQueryReturningStep<T>
{

    final DataType<T>       type;
    final Field<T>          json;
    final Field<String>     path;
          JSONQueryBehavior onEmpty;
          JSONQueryBehavior onError;
          DataType<?>       returning;

    JSONQuery(
        DataType<T> type,
        Field<T> json,
        Field<String> path
    ) {
        this(
            type,
            json,
            path,
            null,
            null,
            null
        );
    }

    JSONQuery(
        DataType<T> type,
        Field<T> json,
        Field<String> path,
        JSONQueryBehavior onEmpty,
        JSONQueryBehavior onError,
        DataType<?> returning
    ) {
        super(
            N_JSON_QUERY,
            type
        );

        this.type = type;
        this.json = nullSafeNotNull(json, ((DataType) OTHER));
        this.path = nullSafeNotNull(path, VARCHAR);
        this.onEmpty = onEmpty;
        this.onError = onError;
        this.returning = returning;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final JSONQuery<T> errorOnEmpty() {
        this.onEmpty = JSONQueryBehavior.ERROR;
        return this;
    }

    @Override
    public final JSONQuery<T> errorOnError() {
        this.onError = JSONQueryBehavior.ERROR;
        return this;
    }

    @Override
    public final JSONQuery<T> nullOnEmpty() {
        this.onEmpty = JSONQueryBehavior.NULL;
        return this;
    }

    @Override
    public final JSONQuery<T> nullOnError() {
        this.onError = JSONQueryBehavior.NULL;
        return this;
    }

    @Override
    public final JSONQuery<T> returning(DataType<?> returning) {
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








    @SuppressWarnings("unchecked")
    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

            case DUCKDB:
            case MARIADB:
            case MYSQL:
                ctx.visit(function(N_JSON_EXTRACT, json.getDataType(), json, path));
                break;

            case SQLITE:







                ctx.sql('(').visit(json).sql("->").visit(path).sql(')');

                break;


            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(function(N_JSONB_PATH_QUERY_FIRST, json.getDataType(), castIfNeeded(json, JSONB), DSL.field("cast({0} as jsonpath)", path)));
                break;

            case CLICKHOUSE:
                ctx.visit(function(systemName("JSON_QUERY"), getDataType(), json, path));
                break;












            default:
                acceptDefault(ctx);
                break;
        }
    }

    private final void acceptDefault(Context<?> ctx) {
        boolean format = !isSimple(ctx, json, path);

        ctx.visit(N_JSON_QUERY).sql('(');

        if (format)
            ctx.sqlIndentStart();

        ctx.visit(json).sql(",");

        if (format)
            ctx.formatSeparator();
        else
            ctx.sql(' ');






        ctx.visit(path);











        if (returning != null) {
            JSONReturning r = new JSONReturning(returning);

            if (r.rendersContent(ctx)) {
                if (format)
                    ctx.formatNewLine();

                ctx.separatorRequired(true).visit(r);
            }
        }

        if (format)
            ctx.sqlIndentEnd();

        ctx.sql(')');
    }








































    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final DataType<T> $arg1() {
        return type;
    }

    @Override
    public final Field<T> $arg2() {
        return json;
    }

    @Override
    public final Field<String> $arg3() {
        return path;
    }

    @Override
    public final JSONQueryBehavior $arg4() {
        return onEmpty;
    }

    @Override
    public final JSONQueryBehavior $arg5() {
        return onError;
    }

    @Override
    public final DataType<?> $arg6() {
        return returning;
    }

    @Override
    public final QOM.JSONQuery<T> $arg1(DataType<T> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3(), $arg4(), $arg5(), $arg6());
    }

    @Override
    public final QOM.JSONQuery<T> $arg2(Field<T> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3(), $arg4(), $arg5(), $arg6());
    }

    @Override
    public final QOM.JSONQuery<T> $arg3(Field<String> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue, $arg4(), $arg5(), $arg6());
    }

    @Override
    public final QOM.JSONQuery<T> $arg4(JSONQueryBehavior newValue) {
        return $constructor().apply($arg1(), $arg2(), $arg3(), newValue, $arg5(), $arg6());
    }

    @Override
    public final QOM.JSONQuery<T> $arg5(JSONQueryBehavior newValue) {
        return $constructor().apply($arg1(), $arg2(), $arg3(), $arg4(), newValue, $arg6());
    }

    @Override
    public final QOM.JSONQuery<T> $arg6(DataType<?> newValue) {
        return $constructor().apply($arg1(), $arg2(), $arg3(), $arg4(), $arg5(), newValue);
    }

    @Override
    public final Function6<? super DataType<T>, ? super Field<T>, ? super Field<String>, ? super JSONQueryBehavior, ? super JSONQueryBehavior, ? super DataType<?>, ? extends QOM.JSONQuery<T>> $constructor() {
        return (a1, a2, a3, a4, a5, a6) -> new JSONQuery(a1, a2, a3, a4, a5, a6);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONQuery<?> o) {
            return
                Objects.equals($type(), o.$type()) &&
                Objects.equals($json(), o.$json()) &&
                Objects.equals($path(), o.$path()) &&
                Objects.equals($onEmpty(), o.$onEmpty()) &&
                Objects.equals($onError(), o.$onError()) &&
                Objects.equals($returning(), o.$returning())
            ;
        }
        else
            return super.equals(that);
    }
}
