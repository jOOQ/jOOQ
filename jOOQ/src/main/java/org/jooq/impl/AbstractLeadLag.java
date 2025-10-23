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

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.inlined;
import static org.jooq.impl.Names.N_COALESCE;
import static org.jooq.impl.Names.N_lagInFrame;
import static org.jooq.impl.Names.N_leadInFrame;

import java.util.function.Function;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QueryPart;
// ...
// ...
import org.jooq.impl.QOM.NullTreatmentWindowFunction;
import org.jooq.impl.QOM.WindowFunction;


/**
 * @author Lukas Eder
 */
abstract class AbstractLeadLag<T, Q extends NullTreatmentWindowFunction<T, Q>>
extends
    AbstractNullTreatmentWindowFunction<T, Q>
{

    private final Field<T>       field;
    private final Field<Integer> offset;
    private final Field<T>       defaultValue;

    AbstractLeadLag(Name name, Field<T> field, Field<Integer> offset, Field<T> defaultValue) {
        super(name, field.getDataType().null_());

        this.field = field;
        this.offset = offset;
        this.defaultValue = defaultValue;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (defaultValue == null) {
            accept0(ctx);
        }
        else {
            switch (ctx.family()) {













                default:
                    accept0(ctx);
                    break;
            }
        }
    }

    private final void accept0(Context<?> ctx) {
        switch (ctx.family()) {







            default:
                switch (ctx.family()) {
                    case CLICKHOUSE:
                        if (this instanceof Lead)
                            ctx.visit(N_leadInFrame);
                        else
                            ctx.visit(N_lagInFrame);

                        break;

                    default:
                        ctx.visit(getUnqualifiedName());
                        break;
                }

                ctx.sql('(').visit(field);

                if (offset != null) {
                    switch (ctx.family()) {










                        default:
                            ctx.sql(", ").visit(offset);
                            break;
                    }
                }

                if (defaultValue != null) {
                    switch (ctx.family()) {












                        default:
                            ctx.sql(", ").visit(defaultValue);
                            break;
                    }
                }






                ctx.sql(')');
                acceptNullTreatment(ctx);
                break;
        }

        acceptOverClause(ctx);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    public final Field<T> $field() {
        return field;
    }

    @SuppressWarnings("unchecked")
    public final Q $field(Field<T> newField) {
        if (newField == field)
            return (Q) this;
        else
            return copyAggregateSpecification().apply(constructor(newField, offset, defaultValue));
    }

    public final Field<Integer> $offset() {
        return offset;
    }

    public final Q $offset(Field<Integer> newOffset) {
        if (newOffset == offset)
            return (Q) this;
        else
            return copyAggregateSpecification().apply(constructor(field, newOffset, defaultValue));
    }

    public final Field<T> $defaultValue() {
        return defaultValue;
    }

    public final Q $defaultValue(Field<T> newDefaultValue) {
        if (newDefaultValue == defaultValue)
            return (Q) this;
        else
            return copyAggregateSpecification().apply(constructor(field, offset, newDefaultValue));
    }

    @Override
    final Q copyAggregateFunction(Function<? super Q, ? extends Q> function) {
        return function.apply(constructor(field, offset, defaultValue));
    }

    abstract Q constructor(Field<T> f, Field<Integer> o, Field<T> def);




















}
