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

import static org.jooq.impl.DSL.choose;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.when;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.nullSafeDataType;

import org.jooq.CaseValueStep;
import org.jooq.CaseWhenStep;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function2;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
final class Choose<T> extends AbstractField<T> implements QOM.Choose<T> {

    private Field<Integer> index;
    private Field<T>[]     values;

    Choose(Field<Integer> index, Field<T>[] values) {
        this(index, values, nullSafeDataType(values));
    }

    Choose(Field<Integer> index, Field<T>[] values, DataType<T> type) {
        super(N_CHOOSE, type);

        this.index = index;
        this.values = values;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (values.length == 0) {
            ctx.visit(inline(null, getDataType()));
            return;
        }

        switch (ctx.family()) {


















            case CLICKHOUSE:
            case CUBRID:
            case DERBY:
            case DUCKDB:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case IGNITE:
            case POSTGRES:
            case SQLITE:
            case TRINO:
            case YUGABYTEDB: {
                CaseValueStep<Integer> s = choose(index);
                CaseWhenStep<Integer, T> when = null;

                for (int i = 0; i < values.length; i++)
                    when = when == null
                        ? s.when(inline(i + 1), values[i])
                        : when.when(inline(i + 1), values[i]);

                ctx.visit(when);
                break;
            }










            case MARIADB:
            case MYSQL: {
                ctx.visit(function(N_ELT, getDataType(), Tools.combine(index, values)));
                break;
            }

            default: {
                ctx.visit(function(N_CHOOSE, getDataType(), Tools.combine(index, values)));
                break;
            }
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<Integer> $arg1() {
        return index;
    }

    @Override
    public final UnmodifiableList<? extends Field<T>> $arg2() {
        return QOM.unmodifiable(values);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Function2<? super Field<Integer>, ? super UnmodifiableList<? extends Field<T>>, ? extends QOM.Choose<T>> $constructor() {
        return (i, v) -> v.isEmpty()
            ? new Choose<T>(i, (Field<T>[]) EMPTY_FIELD, getDataType())
            : new Choose<T>(i, (Field<T>[]) v.toArray(EMPTY_FIELD));
    }
}
