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

import static org.jooq.impl.Names.N_FIRST_VALUE;

import java.util.function.Function;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
// ...
// ...

/**
 * @author Lukas Eder
 */
final class FirstValue<T>
extends
    AbstractNullTreatmentWindowFunction<T, QOM.FirstValue<T>>
implements
    QOM.FirstValue<T>
{

    final Field<T> field;

    FirstValue(Field<T> field) {
        super(N_FIRST_VALUE, field.getDataType().null_());

        this.field = field;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {







            default:
                ctx.visit(N_FIRST_VALUE).sql('(').visit(field);






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
    public final QOM.FirstValue<T> $field(Field<T> newField) {
        if (newField == field)
            return this;
        else
            return copyAggregateSpecification().apply(new FirstValue<>(newField));
    }

    @Override
    final QOM.FirstValue<T> copyAggregateFunction(Function<? super QOM.FirstValue<T>, ? extends QOM.FirstValue<T>> function) {
        return function.apply(new FirstValue<>(field));
    }



















}
