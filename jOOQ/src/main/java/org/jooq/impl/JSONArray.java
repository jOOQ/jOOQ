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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>JSON ARRAY</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class JSONArray<T>
extends
    AbstractField<T>
implements
    JSONArrayNullStep<T>,
    JSONArrayReturningStep<T>
{

    private final DataType<T>                    type;
    private final Collection<? extends Field<?>> fields;
    private       JSONOnNull                     onNull;
    private       DataType<?>                    returning;

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
        this.fields = fields;
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
    public void accept(Context<?> ctx) {
        QueryPartCollectionView<Field<?>> mapped = QueryPartCollectionView.wrap((Collection<Field<?>>) fields).map(JSONEntryImpl.jsonCastMapper(ctx));

        switch (ctx.family()) {


            case POSTGRES:
                if (onNull == JSONOnNull.ABSENT_ON_NULL) {
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
                JSONReturning jsonReturning = new JSONReturning(returning);

                // Workaround for https://github.com/h2database/h2database/issues/2496
                if (ctx.family() == H2 && fields.isEmpty())
                    jsonNull = new JSONNull(JSONOnNull.NULL_ON_NULL);
                else
                    jsonNull = new JSONNull(onNull);

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
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof JSONArray) {
            return
                StringUtils.equals(type, ((JSONArray) that).type) &&
                StringUtils.equals(fields, ((JSONArray) that).fields) &&
                StringUtils.equals(onNull, ((JSONArray) that).onNull) &&
                StringUtils.equals(returning, ((JSONArray) that).returning)
            ;
        }
        else
            return super.equals(that);
    }
}
