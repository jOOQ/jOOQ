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
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
// ...
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.Tools.DataKey.DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.QuantifiedSelect;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class QuantifiedSelectImpl<R extends Record> extends AbstractQueryPart implements QuantifiedSelect<R> {

    /**
     * Generated UID
     */
    private static final long               serialVersionUID = -1224570388944748450L;

    private final Quantifier                quantifier;
    private final Select<R>                 query;
    private final Field<? extends Object[]> array;

    QuantifiedSelectImpl(Quantifier quantifier, Select<R> query) {
        this.quantifier = quantifier;
        this.query = query;
        this.array = null;
    }

    QuantifiedSelectImpl(Quantifier quantifier, Field<? extends Object[]> array) {
        this.quantifier = quantifier;
        this.query = null;
        this.array = array;
    }

    @Override
    public final void accept(Context<?> ctx) {
        Object data = ctx.data(DATA_ROW_VALUE_EXPRESSION_PREDICATE_SUBQUERY);
        boolean extraParentheses = data != null && asList().contains(ctx.family());

        // If this is already a subquery, proceed
        if (ctx.subquery()) {
            ctx.keyword(quantifier.toSQL())
               .sql(extraParentheses ? " ((" : " (")
               .formatIndentStart()
               .formatNewLine()
               .visit(delegate(ctx.configuration()))
               .formatIndentEnd()
               .formatNewLine()
               .sql(extraParentheses ? "))" : ")");
        }
        else {
            ctx.keyword(quantifier.toSQL())
            .sql(extraParentheses ? " ((" : " (")
               .subquery(true)
               .formatIndentStart()
               .formatNewLine()
               .visit(delegate(ctx.configuration()))
               .formatIndentEnd()
               .formatNewLine()
               .subquery(false)
               .sql(extraParentheses ? "))" : ")");
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    private final QueryPartInternal delegate(Configuration ctx) {
        if (query != null) {
            return (QueryPartInternal) query;
        }
        else {
            switch (ctx.family()) {

                // [#869] Postgres supports this syntax natively
                case POSTGRES: {
                    return (QueryPartInternal) array;
                }

                // [#869] H2 and HSQLDB can emulate this syntax by unnesting
                // the array in a subselect
                case H2:
                case HSQLDB:
                    return (QueryPartInternal) create(ctx).select().from(table(array));

                // [#1048] All other dialects emulate unnesting of arrays using
                // UNION ALL-connected subselects
                default: {

                    // The Informix database has an interesting bug when quantified comparison predicates
                    // use nested derived tables with UNION ALL
                    if (array instanceof Param) {
                        Object[] values = ((Param<? extends Object[]>) array).getValue();

                        Select<Record1<Object>> select = null;
                        for (Object value : values) {
                            if (select == null)
                                select = select(val(value));
                            else
                                select = select.unionAll(select(val(value)));
                        }

                        return (QueryPartInternal) select;
                    }
                    else {
                        return (QueryPartInternal) select().from(table(array));
                    }
                }
            }
        }
    }
}
