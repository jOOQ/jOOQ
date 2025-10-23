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

import static org.jooq.impl.Names.N_LAST_VALUE;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.QueryPart;
// ...
// ...

/**
 * @author Lukas Eder
 */
final class LastValue<T>
extends
    AbstractNullTreatmentWindowFunction<T, LastValue<T>>
implements
    QOM.LastValue<T>
{

    final Field<T> field;

    LastValue(Field<T> field) {
        super(N_LAST_VALUE, field.getDataType().null_());

        this.field = field;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {







            default:
                ctx.visit(N_LAST_VALUE).sql('(').visit(field);






                ctx.sql(')');
                acceptNullTreatment(ctx);
                break;
        }

        acceptOverClause(ctx);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $field() {
        return field;
    }

    @Override
    public final LastValue<T> $field(Field<T> newField) {
        if (newField == field)
            return this;
        else
            return copyAggregateSpecification().apply(new LastValue<>(newField));
    }

    @Override
    final LastValue<T> copyAggregateFunction(Function<? super LastValue<T>, ? extends LastValue<T>> function) {
        return function.apply(new LastValue<>(field));
    }



















}
