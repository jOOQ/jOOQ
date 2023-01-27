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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static java.lang.Boolean.TRUE;
// ...
import static org.jooq.impl.QOM.Quantifier.ANY;
import static org.jooq.impl.SubqueryCharacteristics.PREDICAND;
import static org.jooq.impl.Tools.visitSubquery;

import org.jooq.Context;
import org.jooq.Function2;
import org.jooq.QuantifiedSelect;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.impl.QOM.Quantifier;
import org.jooq.impl.Tools.BooleanDataKey;

/**
 * @author Lukas Eder
 */
final class QuantifiedSelectImpl<R extends Record>
extends
    AbstractQueryPart
implements
    QuantifiedSelect<R>,
    QOM.QuantifiedSelect<R>
{

    final Quantifier quantifier;
    final Select<R>  query;

    QuantifiedSelectImpl(Quantifier quantifier, Select<R> query) {
        this.quantifier = quantifier;
        this.query = query;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        boolean extraParentheses = false ;

        switch (ctx.family()) {








            default:
                ctx.visit(quantifier.keyword);
                ctx.sql(extraParentheses ? " ((" : " (");
                visitSubquery(ctx, query, PREDICAND, false);
                ctx.sql(extraParentheses ? "))" : ")");
                break;
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Function2<? super Quantifier, ? super Select<R>, ? extends QOM.QuantifiedSelect<R>> $constructor() {
        return (q, s) -> new QuantifiedSelectImpl<>(q, s);
    }

    @Override
    public final Quantifier $arg1() {
        return quantifier;
    }

    @Override
    public final Select<R> $arg2() {
        return query;
    }
}
