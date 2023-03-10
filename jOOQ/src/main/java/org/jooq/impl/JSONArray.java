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
 * The <code>JSON ARRAY</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class JSONArray<T>
extends
    AbstractField<T>
implements
    QOM.JSONArray<T>,
    JSONArrayNullStep<T>,
    JSONArrayReturningStep<T>
{

    final DataType<T>                           type;
    final QueryPartListView<? extends Field<?>> fields;
          JSONOnNull                            onNull;
          DataType<?>                           returning;

    JSONArray(
        DataType<T> type,
        Collection<? extends Field<?>> fields
    ) {
        this(
            type,
            fields,
            null,
            null
        );
    }

    JSONArray(
        DataType<T> type,
        Collection<? extends Field<?>> fields,
        JSONOnNull onNull,
        DataType<?> returning
    ) {
        super(
            N_JSON_ARRAY,
            type
        );

        this.type = type;
        this.fields = new QueryPartList<>(fields);
        this.onNull = onNull;
        this.returning = returning;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final JSONArray<T> nullOnNull() {
        this.onNull = JSONOnNull.NULL_ON_NULL;
        return this;
    }

    @Override
    public final JSONArray<T> absentOnNull() {
        this.onNull = JSONOnNull.ABSENT_ON_NULL;
        return this;
    }

    @Override
    public final JSONArray<T> returning(DataType<?> returning) {
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
    public void accept(Context<?> ctx) {
        QueryPartCollectionView<Field<?>> mapped = QueryPartCollectionView.wrap((Collection<Field<?>>) fields).map(JSONEntryImpl.jsonCastMapper(ctx));

        switch (ctx.family()) {


            case POSTGRES:
            case YUGABYTEDB:
                if (onNull == JSONOnNull.ABSENT_ON_NULL && !mapped.isEmpty()) {
                    Row1[] rows = map(fields, f -> row(f), Row1[]::new);
                    Table<?> t = values(rows).as("t", "a");
                    Field<?> a = t.field("a");
                    ctx.visit(DSL.field(
                        select((Field<?>) (getDataType() == JSON ? jsonArrayAgg(a) : jsonbArrayAgg(a)))
                        .from(t)
                        .where(a.isNotNull())
                    ));
                }
                else
                    ctx.visit(getDataType() == JSON ? N_JSON_BUILD_ARRAY : N_JSONB_BUILD_ARRAY).sql('(').visit(mapped).sql(')');

                break;











































            default: {
                JSONNull jsonNull;
                JSONReturning jsonReturning;

                // Workaround for https://github.com/h2database/h2database/issues/2496
                if (fields.isEmpty() && ctx.family() == H2)
                    jsonNull = new JSONNull(JSONOnNull.NULL_ON_NULL);
                else if (fields.isEmpty() && JSONNull.NO_SUPPORT_NULL_ON_EMPTY.contains(ctx.dialect()))
                    jsonNull = new JSONNull(null);
                else
                    jsonNull = new JSONNull(onNull);







                jsonReturning = new JSONReturning(returning);

                Field<T> jsonArray = CustomField.of(N_JSON_ARRAY, getDataType(), c ->
                    c.visit(N_JSON_ARRAY).sql('(').visit(QueryPartListView.wrap(mapped, jsonNull, jsonReturning).separator("")).sql(')')
                );

                switch (ctx.family()) {











                    default:
                        ctx.visit(jsonArray);
                        break;
                }
                break;
            }
        }
    }


















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final DataType<T> $arg1() {
        return type;
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $arg2() {
        return QOM.unmodifiable(fields);
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
    public final QOM.JSONArray<T> $arg1(DataType<T> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3(), $arg4());
    }

    @Override
    public final QOM.JSONArray<T> $arg2(UnmodifiableList<? extends Field<?>> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3(), $arg4());
    }

    @Override
    public final QOM.JSONArray<T> $arg3(JSONOnNull newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue, $arg4());
    }

    @Override
    public final QOM.JSONArray<T> $arg4(DataType<?> newValue) {
        return $constructor().apply($arg1(), $arg2(), $arg3(), newValue);
    }

    @Override
    public final Function4<? super DataType<T>, ? super Collection<? extends Field<?>>, ? super JSONOnNull, ? super DataType<?>, ? extends QOM.JSONArray<T>> $constructor() {
        return (a1, a2, a3, a4) -> new JSONArray(a1, (Collection<? extends Field<?>>) a2, a3, a4);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONArray<?> o) {
            return
                StringUtils.equals($type(), o.$type()) &&
                StringUtils.equals($fields(), o.$fields()) &&
                StringUtils.equals($onNull(), o.$onNull()) &&
                StringUtils.equals($returning(), o.$returning())
            ;
        }
        else
            return super.equals(that);
    }
}
