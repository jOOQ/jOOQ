/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static org.jooq.impl.DSL.table;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QuantifiedSelect;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
class QuantifiedSelectImpl<R extends Record> extends AbstractQueryPart implements QuantifiedSelect<R> {

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

        // If this is already a subquery, proceed
        if (ctx.subquery()) {
            ctx.keyword(quantifier.toSQL())
               .sql(" (")
               .formatIndentStart()
               .formatNewLine()
               .visit(delegate(ctx.configuration()))
               .formatIndentEnd()
               .formatNewLine()
               .sql(")");
        }
        else {
            ctx.keyword(quantifier.toSQL())
               .sql(" (")
               .subquery(true)
               .formatIndentStart()
               .formatNewLine()
               .visit(delegate(ctx.configuration()))
               .formatIndentEnd()
               .formatNewLine()
               .subquery(false)
               .sql(")");
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return delegate(ctx.configuration()).clauses(ctx);
    }

    private final QueryPartInternal delegate(Configuration ctx) {
        if (query != null) {
            return (QueryPartInternal) query;
        }
        else {
            switch (ctx.dialect()) {

                // [#869] Postgres supports this syntax natively
                case POSTGRES: {
                    return (QueryPartInternal) array;
                }

                // [#869] H2 and HSQLDB can simulate this syntax by unnesting
                // the array in a subselect
                case H2:
                case HSQLDB:

                // [#1048] All other dialects simulate unnesting of arrays using
                // UNION ALL-connected subselects
                default: {
                    return (QueryPartInternal) create(ctx).select().from(table(array));
                }
            }
        }
    }
}
