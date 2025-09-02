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
 * The <code>JSON OBJECT</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class JSONObject<T>
extends
    AbstractField<T>
implements
    QOM.JSONObject<T>,
    JSONObjectNullStep<T>,
    JSONObjectReturningStep<T>
{

    final DataType<T>                               type;
    final QueryPartListView<? extends JSONEntry<?>> entries;
          JSONOnNull                                onNull;
          DataType<?>                               returning;

    JSONObject(
        DataType<T> type,
        Collection<? extends JSONEntry<?>> entries
    ) {
        this(
            type,
            entries,
            null,
            null
        );
    }

    JSONObject(
        DataType<T> type,
        Collection<? extends JSONEntry<?>> entries,
        JSONOnNull onNull,
        DataType<?> returning
    ) {
        super(
            N_JSON_OBJECT,
            type
        );

        this.type = type;
        this.entries = new QueryPartList<>(entries);
        this.onNull = onNull;
        this.returning = returning;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final JSONObject<T> nullOnNull() {
        this.onNull = JSONOnNull.NULL_ON_NULL;
        return this;
    }

    @Override
    public final JSONObject<T> absentOnNull() {
        this.onNull = JSONOnNull.ABSENT_ON_NULL;
        return this;
    }

    @Override
    public final JSONObject<T> returning(DataType<?> returning) {
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
            case YUGABYTEDB:
                if (onNull == JSONOnNull.ABSENT_ON_NULL)
                    ctx.visit(unquotedName(getDataType().getType() == JSONB.class ? "jsonb_strip_nulls" : "json_strip_nulls")).sql('(');

                ctx.visit(unquotedName(getDataType().getType() == JSONB.class ? "jsonb_build_object" : "json_build_object")).sql('(').visit(QueryPartCollectionView.wrap(entries)).sql(')');

                if (onNull == JSONOnNull.ABSENT_ON_NULL)
                    ctx.sql(')');

                break;













































            case MARIADB: {
                JSONEntry<?> first;

                // Workaround for https://jira.mariadb.org/browse/MDEV-13701
                if (entries.size() > 1) {
                    ctx.visit(JSONEntryImpl.jsonMerge(ctx, "{}", Tools.map(entries, e -> jsonObject(e), Field[]::new)));
                }
                else if (!entries.isEmpty() && isJSONArray((first = entries.iterator().next()).value())) {
                    ctx.visit(jsonObject(
                        key(first.key()).value(JSONEntryImpl.jsonMerge(ctx, "[]", first.value()))
                    ));
                }
                else
                    acceptStandard(ctx);

                break;
            }

            case MYSQL: {

                // [#13249] ABSENT ON NULL emulation using JSON_TABLE
                if (onNull == JSONOnNull.ABSENT_ON_NULL) {
                    Field<String> k = DSL.field(name("jt", "k"), VARCHAR);
                    Field<JSON> o = DSL.field(name("j", "o"), JSON);

                    ctx.visit(DSL.field(
                        select(DSL.coalesce(
                            DSL.jsonObjectAgg(k, DSL.function(N_JSON_EXTRACT, JSON, o, DSL.concat(inline("$.\""), k, inline("\"")))),
                            DSL.jsonObject()))
                        .from(
                            select(CustomField.of("o", JSON, c -> acceptStandard(c)).as(o)).asTable("j"),
                            jsonTable(function(N_JSON_KEYS, JSON, o), inline("$[*]"))
                                .column("k", VARCHAR).path("$")
                                .asTable("jt"))
                        .where(DSL.function(N_JSON_EXTRACT, JSON, o, DSL.concat(inline("$.\""), k, inline("\""))).ne(DSL.inline("null").cast(JSON)))
                    ));
                }
                else
                    acceptStandard(ctx);

                break;
            }

            case TRINO: {
                // [#11485] While JSON_OBJECT is supported in Trino, it seems there are a few show stopping bugs, including:
                // https://github.com/trinodb/trino/issues/16522
                // https://github.com/trinodb/trino/issues/16523
                // https://github.com/trinodb/trino/issues/16525

                ctx.visit(function(N_MAP_FROM_ENTRIES, JSON,
                    absentOnNullIf(
                        () -> onNull == JSONOnNull.ABSENT_ON_NULL,
                        e -> DSL.field("{0}[2]", e.getDataType(), e),
                        array(map(entries, e -> function(N_ROW, JSON, e.key(), JSONEntryImpl.jsonCast(ctx, e.value()).cast(JSON))))
                    )
                ).cast(JSON));
                break;
            }

            default:
                acceptStandard(ctx);
                break;
        }
    }

    static final Field<?> absentOnNullIf(
        Function0<Boolean> test,
        Function1<Field<?>, Field<?>> e,
        Field<?> array
    ) {
        if (test.get())
            return function(N_FILTER, array.getDataType(),
                array,
                DSL.field("e -> {0}", BOOLEAN, e.apply(DSL.field(raw("e"), array.getDataType())).isNotNull())
            );
        else
            return array;
    }

    private static final boolean isJSONArray(Field<?> field) {
        return field instanceof JSONArray
            || field instanceof JSONArrayAgg
            || field instanceof ScalarSubquery && isJSONArray(((ScalarSubquery<?>) field).query.getSelect().get(0));
    }

    private final void acceptStandard(Context<?> ctx) {
        JSONNull jsonNull;
        JSONReturning jsonReturning;

        // Workaround for https://github.com/h2database/h2database/issues/2496
        if (entries.isEmpty() && ctx.family() == H2)
            jsonNull = new JSONNull(JSONOnNull.NULL_ON_NULL);

        // Some dialects support the JSONNull clause only for non-empty JSON_OBJECT
        // E.g. https://trino.io/docs/current/functions/json.html#json-object
        else if (entries.isEmpty() && JSONNull.NO_SUPPORT_NULL_ON_EMPTY.contains(ctx.dialect()))
            jsonNull = new JSONNull(null);
        else
            jsonNull = new JSONNull(onNull);







        jsonReturning = new JSONReturning(returning);

        ctx.visit(N_JSON_OBJECT).sql('(').visit(QueryPartListView.wrap(QueryPartCollectionView.wrap(entries), jsonNull, jsonReturning).separator("")).sql(')');
    }


















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final DataType<T> $arg1() {
        return type;
    }

    @Override
    public final QOM.UnmodifiableList<? extends JSONEntry<?>> $arg2() {
        return QOM.unmodifiable(entries);
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
    public final QOM.JSONObject<T> $arg1(DataType<T> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3(), $arg4());
    }

    @Override
    public final QOM.JSONObject<T> $arg2(QOM.UnmodifiableList<? extends JSONEntry<?>> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3(), $arg4());
    }

    @Override
    public final QOM.JSONObject<T> $arg3(JSONOnNull newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue, $arg4());
    }

    @Override
    public final QOM.JSONObject<T> $arg4(DataType<?> newValue) {
        return $constructor().apply($arg1(), $arg2(), $arg3(), newValue);
    }

    @Override
    public final Function4<? super DataType<T>, ? super Collection<? extends JSONEntry<?>>, ? super JSONOnNull, ? super DataType<?>, ? extends QOM.JSONObject<T>> $constructor() {
        return (a1, a2, a3, a4) -> new JSONObject(a1, (Collection<? extends JSONEntry<?>>) a2, a3, a4);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.JSONObject<?> o) {
            return
                Objects.equals($type(), o.$type()) &&
                Objects.equals($entries(), o.$entries()) &&
                Objects.equals($onNull(), o.$onNull()) &&
                Objects.equals($returning(), o.$returning())
            ;
        }
        else
            return super.equals(that);
    }
}
