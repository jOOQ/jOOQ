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

import static java.util.Arrays.asList;
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.Cast.renderCastIf;
import static org.jooq.impl.Keywords.K_ARRAY;
import static org.jooq.impl.Keywords.K_INT;
import static org.jooq.impl.Names.N_ARRAY;
import static org.jooq.impl.Tools.ExtendedDataKey.DATA_EMPTY_ARRAY_BASE_TYPE;

import java.util.Collection;
import java.util.Set;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext.CastMode;
// ...
import org.jooq.SQLDialect;
// ...
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
final class Array<T>
extends
    AbstractField<T[]>
implements
    QOM.Array<T>
{

    static final Set<SQLDialect> REQUIRES_CAST              = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect> NO_SUPPORT_SQUARE_BRACKETS = SQLDialect.supportedBy(CLICKHOUSE);

    final FieldsImpl<Record>     fields;

    Array(Collection<? extends Field<T>> fields) {
        super(N_ARRAY, type(fields));

        this.fields = new FieldsImpl<>(fields);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean isNullable() {
        return false;
    }

    @Override
    public boolean generatesCast() {
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static <T> DataType<T[]> type(Collection<? extends Field<T>> fields) {
        if (fields == null || fields.isEmpty())
            return (DataType) SQLDataType.OTHER.getArrayDataType();
        else
            return fields.iterator().next().getDataType().getArrayDataType();
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                renderCastIf(ctx,
                    c -> {
                        if (NO_SUPPORT_SQUARE_BRACKETS.contains(ctx.dialect()))
                            ctx.visit(K_ARRAY).sql('(').visit(fields).sql(')');
                        else
                            ctx.visit(K_ARRAY).sql('[').visit(fields).sql(']');
                    },
                    c -> {
                        DataType<?> type = (DataType<?>) c.data(DATA_EMPTY_ARRAY_BASE_TYPE);

                        if (type != null && !type.isOther())
                            c.sql(type.getCastTypeName(ctx.configuration())).sql("[]");
                        else
                            c.visit(K_INT).sql("[]");
                    },
                    () -> fields.fields.length == 0 && REQUIRES_CAST.contains(ctx.dialect()) && ctx.castMode() != CastMode.NEVER
                );

                break;
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UnmodifiableList<? extends Field<T>> $elements() {
        return (UnmodifiableList) QOM.unmodifiable(fields.fields);
    }














}
