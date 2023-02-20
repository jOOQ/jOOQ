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

// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.impl.AbstractRowAsField.forceMultisetContent;
import static org.jooq.impl.DSL.case_;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.inlined;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.toChar;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.Keywords.K_FORMAT;
import static org.jooq.impl.Keywords.K_JSON;
import static org.jooq.impl.Keywords.K_KEY;
import static org.jooq.impl.Keywords.K_VALUE;
import static org.jooq.impl.Names.N_HEX;
import static org.jooq.impl.Names.N_JSON;
import static org.jooq.impl.Names.N_JSON_EXTRACT;
import static org.jooq.impl.Names.N_JSON_MERGE;
import static org.jooq.impl.Names.N_JSON_MERGE_PRESERVE;
import static org.jooq.impl.Names.N_JSON_QUERY;
import static org.jooq.impl.Names.N_PARSE_JSON;
import static org.jooq.impl.Names.N_RAWTOHEX;
import static org.jooq.impl.Names.N_TO_VARIANT;
import static org.jooq.impl.SQLDataType.BIT;
import static org.jooq.impl.SQLDataType.BOOLEAN;
import static org.jooq.impl.SQLDataType.JSON;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.combine;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.impl.Tools.isScalarSubquery;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSON;
import org.jooq.JSONEntry;
import org.jooq.JSONEntryValueStep;
import org.jooq.Param;
// ...
import org.jooq.QueryPart;
import org.jooq.Record1;
// ...
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Select;
// ...
import org.jooq.conf.NestedCollectionEmulation;



/**
 * The JSON object entry.
 *
 * @author Lukas Eder
 */
final class JSONEntryImpl<T> extends AbstractQueryPart implements JSONEntry<T>, JSONEntryValueStep {

    static final Set<SQLDialect> SUPPORT_JSON_MERGE_PRESERVE = SQLDialect.supportedBy(MARIADB, MYSQL);

    private final Field<String>  key;
    private final Field<T>       value;

    JSONEntryImpl(Field<String> key) {
        this(key, null);
    }

    JSONEntryImpl(Field<String> key, Field<T> value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public final Field<String> key() {
        return key;
    }

    @Override
    public final Field<T> value() {
        return value;
    }

    @Override
    public final <X> JSONEntry<X> value(X newValue) {
        return value(Tools.field(newValue));
    }

    @Override
    public final <X> JSONEntry<X> value(Field<X> newValue) {
        return new JSONEntryImpl<>(key, newValue);
    }

    @Override
    public final <X> JSONEntry<X> value(Select<? extends Record1<X>> newValue) {
        return value(DSL.field(newValue));
    }

    @Override
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {























            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case YUGABYTEDB:
                ctx.visit(key).sql(", ").visit(jsonCast(ctx, value));
                break;

            default:
                ctx.visit(K_KEY).sql(' ').visit(key).sql(' ').visit(K_VALUE).sql(' ').visit(jsonCast(ctx, value));
                break;
        }
    }

    static final Function<? super Field<?>, ? extends Field<?>> jsonCastMapper(final Context<?> ctx) {
        return field -> jsonCast(ctx, field);
    }

    static final Field<?> jsonCast(Context<?> ctx, Field<?> field) {
        return jsonCast(ctx, field, false);
    }

    static final Field<?> jsonCast(Context<?> ctx, Field<?> field, boolean castJSONTypes) {
        DataType<?> type = field.getDataType();

        switch (ctx.family()) {

            // [#10769] [#12141] Some dialects don't support auto conversions from X to JSON
            case H2:
                if (isType(type, UUID.class))
                    return field.cast(VARCHAR(36));

                // This is fixed, but not in 2.1.210 yet:
                // https://github.com/h2database/h2database/issues/3439
                else if (type.isEnum())
                    return field.cast(VARCHAR);
                else if (type.isTemporal())
                    return field.cast(VARCHAR);

                // [#12134] No auto conversion available yet
                else if (type.isBinary())
                    return function(N_RAWTOHEX, VARCHAR, field);

                break;

            // [#11025] These don't have boolean support outside of JSON
            case MARIADB:
            case MYSQL:
                // [#10323] [#13089] An explicit CAST is needed for BIT(1) types,
                //                   which jOOQ interprets as BOOLEAN, but which are
                //                   serialised as binary by MySQL
                if (type.getSQLDataType() == BIT)
                    return field.cast(BOOLEAN);
                else if (isType(type, Boolean.class))
                    return inlined(field);
                else if (castJSONTypes && type.isJSON())
                    if (ctx.family() == MYSQL)
                        return field.cast(field.getDataType());
                    else
                        return function(N_JSON_EXTRACT, field.getDataType(), field, inline("$"));

                break;

            case SQLITE:
                if (isType(type, Boolean.class))
                    return function(N_JSON, SQLDataType.JSON, booleanCase(field));
                else if (type.isBinary())
                    return when(field.isNotNull(), function(N_HEX, VARCHAR, field));
                else if (castJSONTypes && type.isJSON())
                    return function(N_JSON, SQLDataType.JSON, field);

                break;










































            case POSTGRES:
            case YUGABYTEDB:
                if (field instanceof Param)
                    if (field.getType() != Object.class)
                        return field.cast(field.getDataType());
                    else
                        return field.cast(VARCHAR);
                else
                    return field;
        }

        return field;
    }














    @SuppressWarnings("unchecked")
    private static Field<String> booleanCase(Field<?> field) {
        return case_((Field<Boolean>) field).when(inline(true), inline("true")).when(inline(false), inline("false"));
    }

    static final <T> Field<T> unescapeNestedJSON(Context<?> ctx, Field<T> value) {

        // [#12086] Avoid escaping nested JSON
        // [#12168] Yet another MariaDB JSON un-escaping workaround https://jira.mariadb.org/browse/MDEV-26134
        // [#12549] Using JSON_MERGE_PRESERVE doesn't work here, as we might not know the user content
        if (isJSON(ctx, value.getDataType())) {
            switch (ctx.family()) {







                case MARIADB:
                case MYSQL:
                case SQLITE:
                    return function(
                        N_JSON_EXTRACT,
                        value.getDataType(),
                        value,
                        inline("$")
                    );
            }
        }

        return value;
    }

    static final boolean isType(DataType<?> t, Class<?> type) {
        return ConvertedDataType.delegate(t).getType() == type;
    }

    static final boolean isJSON(Context<?> ctx, DataType<?> type) {
        DataType<?> t = ConvertedDataType.delegate(type);

        return t.isJSON()
            || t.isEmbeddable() && forceMultisetContent(ctx, () -> t.getRow().size() > 1) && emulateMultisetWithJSON(ctx)
            || t.isRecord() && forceMultisetContent(ctx, () -> t.getRow().size() > 1) && emulateMultisetWithJSON(ctx)
            || t.isMultiset() && emulateMultisetWithJSON(ctx);
    }

    private static final boolean emulateMultisetWithJSON(Scope scope) {
        return emulateMultiset(scope.configuration()) == NestedCollectionEmulation.JSON
            || emulateMultiset(scope.configuration()) == NestedCollectionEmulation.JSONB;
    }

    static final Field<?> booleanValAsVarchar(Field<?> field) {
        return field instanceof Val<?> v ? v.convertTo0(VARCHAR) : booleanCase(field);
    }

    static final Field<?> jsonMerge(Scope scope, String empty, Field<?>... fields) {
        return function(
            SUPPORT_JSON_MERGE_PRESERVE.contains(scope.dialect()) ? N_JSON_MERGE_PRESERVE : N_JSON_MERGE,
            fields[0].getDataType(),
            combine(inline(empty), fields)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $key() {
        return key;
    }

    @Override
    public final Field<?> $value() {
        return value;
    }














}
