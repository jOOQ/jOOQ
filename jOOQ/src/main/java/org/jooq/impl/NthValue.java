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

// ...
import static org.jooq.impl.Keywords.K_FIRST;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_LAST;
import static org.jooq.impl.Names.N_NTH_VALUE;

import java.util.function.Function;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
// ...
// ...
import org.jooq.WindowFromFirstLastStep;
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.impl.QOM.FromFirstOrLast;

/**
 * @author Lukas Eder
 */
final class NthValue<T>
extends
    AbstractNullTreatmentWindowFunction<T, NthValue<T>>
implements
    WindowFromFirstLastStep<T>,
    QOM.NthValue<T> {

    final Field<T>       field;
    final Field<Integer> offset;
    FromFirstOrLast      fromFirstOrLast;

    NthValue(Field<T> field, Field<Integer> offset) {
        super(N_NTH_VALUE, field.getDataType().null_());

        this.field = field;
        this.offset = offset;
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {








            default:
                ctx.visit(N_NTH_VALUE).sql('(').visit(field).sql(", ").visit(offset);





                ctx.sql(')');




                acceptFromFirstOrLast(ctx);
                acceptNullTreatmentStandard(ctx);






                break;
        }

        acceptOverClause(ctx);
    }

    final void acceptFromFirstOrLast(Context<?> ctx) {
        switch (ctx.family()) {










            default:
                if (fromFirstOrLast == FromFirstOrLast.FROM_LAST)
                    ctx.sql(' ').visit(K_FROM).sql(' ').visit(K_LAST);
                else if (fromFirstOrLast == FromFirstOrLast.FROM_FIRST)
                    ctx.sql(' ').visit(K_FROM).sql(' ').visit(K_FIRST);

                break;
        }
    }

    @Override
    public final WindowIgnoreNullsStep<T> fromFirst() {
        fromFirstOrLast = FromFirstOrLast.FROM_FIRST;
        return this;
    }

    @Override
    public final WindowIgnoreNullsStep<T> fromLast() {
        fromFirstOrLast = FromFirstOrLast.FROM_LAST;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $field() {
        return field;
    }

    @Override
    public final NthValue<T> $field(Field<T> newField) {
        if (newField == field)
            return this;
        else
            return copyWindowSpecification().apply(new NthValue<>(newField, offset));
    }

    @Override
    public final Field<Integer> $offset() {
        return offset;
    }

    @Override
    public final NthValue<T> $offset(Field<Integer> newOffset) {
        if (newOffset == offset)
            return this;
        else
            return copyWindowSpecification().apply(new NthValue<>(field, newOffset));
    }

    @Override
    public final FromFirstOrLast $fromFirstOrLast() {
        return fromFirstOrLast;
    }

    @Override
    public final NthValue<T> $fromFirstOrLast(FromFirstOrLast f) {
        if (f == fromFirstOrLast)
            return this;
        else
            return copy(c -> {
                c.fromFirstOrLast = f;
            });
    }

    @Override
    final NthValue<T> copyAggregateFunction(Function<? super NthValue<T>, ? extends NthValue<T>> function) {
        NthValue<T> result = new NthValue<>(field, offset);
        result.fromFirstOrLast = fromFirstOrLast;
        return function.apply(result);
    }



















}
