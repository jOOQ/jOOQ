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
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.QOM.Quantifier.ANY;
import static org.jooq.impl.SubqueryCharacteristics.PREDICAND;
import static org.jooq.impl.Tools.visitSubquery;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function2;
import org.jooq.Param;
import org.jooq.QuantifiedSelect;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.impl.QOM.Quantifier;
import org.jooq.impl.QOM.UNotYetImplemented;
import org.jooq.impl.Tools.BooleanDataKey;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class QuantifiedArray<T>
extends
    AbstractQueryPart
implements
    QuantifiedSelect<Record1<T>>,
    QOM.QuantifiedArray<T> {

    final Quantifier quantifier;
    final Field<T[]> array;

    QuantifiedArray(Quantifier quantifier, Field<T[]> array) {
        this.quantifier = quantifier;
        this.array = array;
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
                visitSubquery(ctx, delegate(ctx), PREDICAND, false);
                ctx.sql(extraParentheses ? "))" : ")");
                break;
        }
    }

    private final QueryPart delegate(Context<?> ctx) {
        if (array instanceof QOM.Array<T> a) {
            switch (ctx.family()) {

                // [#869] Postgres supports this syntax natively


                case POSTGRES:
                case YUGABYTEDB:
                    return array;

                default: {
                    Select<Record1<T>> select = null;

                    for (Field<T> value : a.$elements())
                        if (select == null)
                            select = select(value);
                        else
                            select = select.unionAll(select(value));

                    return select;
                }
            }
        }
        else {
            switch (ctx.family()) {

                // [#869] Postgres supports this syntax natively


                case POSTGRES:
                case YUGABYTEDB:
                    return array;

                // [#869] H2 and HSQLDB can emulate this syntax by unnesting
                // the array in a subselect
                case H2:
                case HSQLDB:
                    return select().from(table(array));

                // [#1048] All other dialects emulate unnesting of arrays using
                // UNION ALL-connected subselects
                default: {

                    // The Informix database has an interesting bug when quantified comparison predicates
                    // use nested derived tables with UNION ALL
                    if (array instanceof Param) {
                        Object[] values0 = ((Param<? extends Object[]>) array).getValue();

                        Select<Record1<Object>> select = null;
                        for (Object value : values0)
                            if (select == null)
                                select = select(val(value));
                            else
                                select = select.unionAll(select(val(value)));

                        return select;
                    }
                    else
                        return select().from(table(array));
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Function2<? super Quantifier, ? super Field<T[]>, ? extends QuantifiedArray<T>> $constructor() {
        return (q, a) -> new QuantifiedArray<>(q, a);
    }

    @Override
    public final Quantifier $arg1() {
        return quantifier;
    }

    @Override
    public final Field<T[]> $arg2() {
        return array;
    }
}
