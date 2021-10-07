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

// ...
import static org.jooq.impl.Names.N_NEG;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.Param;
import org.jooq.conf.TransformUnneededArithmeticExpressions;

/**
 * @author Lukas Eder
 */
final class Neg<T> extends AbstractTransformable<T> implements QOM.Neg<T> {

    private final Field<T> field;
    private final boolean  internal;

    Neg(Field<T> field, boolean internal) {
        super(N_NEG, field.getDataType());

        this.field = field;
        this.internal = internal;
    }

    @Override
    public final Field<?> transform(TransformUnneededArithmeticExpressions transform) {


















        return this;
    }

    @Override
    public final void accept0(Context<?> ctx) {






        ctx.sql("-(")
           .visit(field)
           .sql(')');
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return field;
    }

    @Override
    public final Function1<? super Field<T>, ? extends Field<T>> constructor() {
        return f -> new Neg<>(f, internal);
    }
}
