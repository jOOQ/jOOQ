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

import static org.jooq.impl.Tools.isInlineVal1;
import static org.jooq.impl.Tools.unalias;
import static org.jooq.impl.Tools.visitAutoAliased;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.SelectFieldOrAsterisk;

/**
 * @author Lukas Eder
 */
final class SelectFieldList<F extends SelectFieldOrAsterisk> extends QueryPartList<F> {

    SelectFieldList() {
        this(true);
    }

    SelectFieldList(Iterable<? extends F> wrappedList) {
        this(true, wrappedList);
    }

    SelectFieldList(F[] wrappedList) {
        this(true, wrappedList);
    }

    SelectFieldList(boolean allowNoQueryParts) {
        super(allowNoQueryParts);
    }

    SelectFieldList(boolean allowNoQueryParts, Iterable<? extends F> wrappedList) {
        super(allowNoQueryParts, wrappedList);
    }

    SelectFieldList(boolean allowNoQueryParts, F[] wrappedList) {
        super(allowNoQueryParts, wrappedList);
    }

    @Override
    public final boolean rendersContent(Context<?> ctx) {
        return true;
    }

    @Override
    protected final void toSQLEmptyList(Context<?> ctx) {
        ctx.visit(AsteriskImpl.INSTANCE.get());
    }

    @Override
    public final boolean declaresFields() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void acceptElement(Context<?> ctx, F part) {

        // [#4727] Various SelectFieldList references containing Table<?> cannot
        //         resolve the instance in time for the rendering, e.g. RETURNING
        if (part instanceof AbstractTable<?> t)
            acceptElement0(ctx, (F) t.tf());
        else if (part instanceof AbstractRow<?> r)
            acceptElement0(ctx, (F) r.rf());
        else
            acceptElement0(ctx, part);
    }

    @SuppressWarnings("unchecked")
    private void acceptElement0(Context<?> ctx, F part) {
        if (ctx.declareFields() && part instanceof Field<?> f) {
            part = (F) project(ctx, f);
        }

        visitAutoAliased(ctx, part, Context::declareFields, (c, t) -> super.acceptElement(c, t));
    }

    static final <T> Field<T> project(Context<?> ctx, Field<T> field) {
        switch (ctx.family()) {

            case HSQLDB:
            case POSTGRES:
            case YUGABYTEDB: {

                // [#16367] The NULL literal defaults to type TEXT in PostgreSQL.
                //          if we know the data type, we should cast it explicitly
                if (ctx.subquery()) {
                    Field<T> f = unalias(field);

                    if (isInlineVal1(ctx, f, v -> v == null)
                        && !f.getDataType().isOther()
                        && !f.getDataType().isString()
                    ) {
                        Field<T> cast = f.cast(f.getDataType());
                        return f == field ? cast : cast.as(field);
                    }
                }

                break;
            }



















        }

        return field;
    }
}
