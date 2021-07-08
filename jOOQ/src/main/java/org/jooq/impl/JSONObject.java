/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: http://www.jooq.org/licenses
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

import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.key;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.DSL.values;
import static org.jooq.impl.Names.N_JSON_MERGE;
import static org.jooq.impl.Names.N_JSON_MERGE_PRESERVE;
import static org.jooq.impl.Names.N_JSON_OBJECT;
import static org.jooq.impl.Names.N_T;
import static org.jooq.impl.Tools.map;

import java.util.Collection;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.JSONEntry;
import org.jooq.JSONObjectNullStep;
import org.jooq.JSONObjectReturningStep;
import org.jooq.Name;
// ...
// ...
import org.jooq.tools.StringUtils;


/**
 * The <code>JSON OBJECT</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class JSONObject<T>
extends
    AbstractField<T>
implements
    JSONObjectNullStep<T>,
    JSONObjectReturningStep<T>
{

    private final DataType<T>                        type;
    private final Collection<? extends JSONEntry<?>> entries;
    private       JSONOnNull                         onNull;
    private       DataType<?>                        returning;

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
        this.entries = entries;
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
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {


            case POSTGRES:
                if (onNull == JSONOnNull.ABSENT_ON_NULL)
                    ctx.visit(unquotedName(getDataType().getType() == JSONB.class ? "jsonb_strip_nulls" : "json_strip_nulls")).sql('(');

                ctx.visit(unquotedName(getDataType().getType() == JSONB.class ? "jsonb_build_object" : "json_build_object")).sql('(').visit(QueryPartCollectionView.wrap(entries)).sql(')');

                if (onNull == JSONOnNull.ABSENT_ON_NULL)
                    ctx.sql(')');

                break;


































            case MARIADB: {
                JSONEntry<?> first;
                Name name = JSONArrayAgg.SUPPORT_JSON_MERGE_PRESERVE.contains(ctx.dialect()) ? N_JSON_MERGE_PRESERVE : N_JSON_MERGE;

                // Workaround for https://jira.mariadb.org/browse/MDEV-13701
                if (entries.size() > 1) {
                    ctx.visit(name).sql('(').visit(inline("{}"))
                       .formatIndentStart();

                    for (JSONEntry<?> entry : entries)
                        ctx.sql(',').formatSeparator().visit(jsonObject(entry));

                    ctx.formatIndentEnd()
                       .formatNewLine()
                       .sql(')');
                }
                else if (!entries.isEmpty() && isJSONArray((first = entries.iterator().next()).value())) {
                    ctx.visit(jsonObject(
                        key(first.key()).value(DSL.field("{0}({1}, {2})", getDataType(), name, inline("[]"), first.value()))
                    ));
                }
                else
                    acceptStandard(ctx);

                break;
            }

            default:
                acceptStandard(ctx);
                break;
        }
    }

    private static final boolean isJSONArray(Field<?> field) {
        return field instanceof JSONArray
            || field instanceof JSONArrayAgg
            || field instanceof ScalarSubquery && isJSONArray(((ScalarSubquery<?>) field).query.getSelect().get(0));
    }












    private final void acceptStandard(Context<?> ctx) {
        JSONNull jsonNull;
        JSONReturning jsonReturning = new JSONReturning(returning);

        // Workaround for https://github.com/h2database/h2database/issues/2496
        if (entries.isEmpty() && ctx.family() == H2)
            jsonNull = new JSONNull(JSONOnNull.NULL_ON_NULL);





        else
            jsonNull = new JSONNull(onNull);

        ctx.visit(N_JSON_OBJECT).sql('(').visit(QueryPartListView.wrap(QueryPartCollectionView.wrap(entries), jsonNull, jsonReturning).separator("")).sql(')');
    }



    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof JSONObject) {
            return
                StringUtils.equals(type, ((JSONObject) that).type) &&
                StringUtils.equals(entries, ((JSONObject) that).entries) &&
                StringUtils.equals(onNull, ((JSONObject) that).onNull) &&
                StringUtils.equals(returning, ((JSONObject) that).returning)
            ;
        }
        else
            return super.equals(that);
    }
}
