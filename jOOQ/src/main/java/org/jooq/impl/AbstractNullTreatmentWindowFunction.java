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

import static org.jooq.impl.Keywords.K_IGNORE_NULLS;
import static org.jooq.impl.Keywords.K_RESPECT_NULLS;

import java.util.function.Function;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Name;
// ...
import org.jooq.WindowIgnoreNullsStep;
import org.jooq.WindowOverStep;
import org.jooq.impl.QOM.NullTreatment;

/**
 * @author Lukas Eder
 */
abstract class AbstractNullTreatmentWindowFunction<T, Q extends AbstractNullTreatmentWindowFunction<T, Q>>
extends
    AbstractWindowFunction<T, Q>
implements
    WindowIgnoreNullsStep<T>
{

    NullTreatment nullTreatment;

    AbstractNullTreatmentWindowFunction(Name name, DataType<T> type) {
        super(name, type);
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------


















    final void acceptNullTreatmentAsArgumentKeywords(Context<?> ctx) {
        switch (ctx.family()) {



            case DUCKDB:
                if (nullTreatment == NullTreatment.IGNORE_NULLS)
                    ctx.sql(' ').visit(K_IGNORE_NULLS);

                break;
        }
    }

    final void acceptNullTreatment(Context<?> ctx) {
        switch (ctx.family()) {














            case DUCKDB:
                break;

            default:
                acceptNullTreatmentStandard(ctx);
                break;
        }
    }

    final void acceptNullTreatmentStandard(Context<?> ctx) {
        switch (ctx.family()) {


            case DUCKDB:
                break;

            default:
                if (nullTreatment == NullTreatment.IGNORE_NULLS)
                    ctx.sql(' ').visit(K_IGNORE_NULLS);
                else if (nullTreatment == NullTreatment.RESPECT_NULLS)
                    ctx.sql(' ').visit(K_RESPECT_NULLS);

                break;
        }
    }

    @Override
    public final WindowOverStep<T> ignoreNulls() {
        nullTreatment = NullTreatment.IGNORE_NULLS;
        return this;
    }

    @Override
    public final WindowOverStep<T> respectNulls() {
        nullTreatment = NullTreatment.RESPECT_NULLS;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    public final NullTreatment $nullTreatment() {
        return nullTreatment;
    }

    @SuppressWarnings("unchecked")
    public final Q $nullTreatment(NullTreatment n) {
        if (n == nullTreatment)
            return (Q) this;
        else
            return copy(c -> {
                c.nullTreatment = n;
            });
    }

    @Override
    final Q copy1(Function<Q, Q> function) {
        return function.apply(copy2(copy1()));
    }

    final Function<Q, Q> copy1() {
        return c -> {
            c.nullTreatment = nullTreatment;
            return c;
        };
    }

    abstract Q copy2(Function<Q, Q> function);
}
