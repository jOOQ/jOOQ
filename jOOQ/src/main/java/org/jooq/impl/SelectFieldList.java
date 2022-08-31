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

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.SelectFieldOrAsterisk;

/**
 * @author Lukas Eder
 */
final class SelectFieldList<F extends SelectFieldOrAsterisk> extends QueryPartList<F> {

    SelectFieldList() {
        super();
    }

    SelectFieldList(Iterable<? extends F> wrappedList) {
        super(wrappedList);
    }

    SelectFieldList(F[] wrappedList) {
        super(wrappedList);
    }

    @Override
    public final boolean rendersContent(Context<?> ctx) {
        return true;
    }

    @Override
    protected final void toSQLEmptyList(Context<?> ctx) {
        ctx.visit(AsteriskImpl.INSTANCE);
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
        F alternative;

        if (ctx.declareFields() && part instanceof AutoAlias && (alternative = ((AutoAlias<F>) part).autoAlias(ctx)) != null)
            super.acceptElement(ctx, alternative);
        else
            super.acceptElement(ctx, part);
    }
}
