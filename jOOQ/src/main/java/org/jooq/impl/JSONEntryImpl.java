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

import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.Keywords.K_FORMAT;
import static org.jooq.impl.Keywords.K_JSON;
import static org.jooq.impl.Keywords.K_KEY;
import static org.jooq.impl.Keywords.K_VALUE;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.util.UUID;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONEntry;


/**
 * The JSON object entry.
 *
 * @author Lukas Eder
 */
final class JSONEntryImpl<T> extends AbstractQueryPart implements JSONEntry<T> {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = 6734093632906565848L;
    private final Field<String> key;
    private final Field<T>      value;

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
    public void accept(Context<?> ctx) {
        switch (ctx.family()) {



















            case MARIADB:
            case MYSQL:
            case POSTGRES:
                ctx.visit(key).sql(", ").visit(value);
                break;

            default:
                ctx.visit(K_KEY).sql(' ').visit(key).sql(' ').visit(K_VALUE).sql(' ').visit(jsonCast(ctx, value));
                break;
        }
    }

    static final F1<Field<?>, Field<?>> jsonCastMapper(final Context<?> ctx) {
        return new F1<Field<?>, Field<?>>() {
            @Override
            public Field<?> apply(Field<?> field) {
                return jsonCast(ctx, field);
            }
        };
    }

    static final Field<?> jsonCast(Context<?> ctx, Field<?> field) {
        switch (ctx.family()) {

            // [#10769] Some dialects don't support auto conversions from X to JSON
            case H2: {
                DataType<?> type = field.getDataType();

                if (type.getType() == UUID.class)
                    return field.cast(VARCHAR(36));
            }
        }

        return field;
    }
}
