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

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class DerivedTable<R extends Record> extends AbstractTable<R> {

    private static final long serialVersionUID = 6272398035926615668L;

    private final Select<R>   query;

    DerivedTable(Select<R> query) {
        super("select");

        this.query = query;
    }

    final Select<R> query() {
        return query;
    }

    @Override
    public final Table<R> as(String alias) {
        return new TableAlias<R>(this, alias, true);
    }

    @Override
    public final Table<R> as(String alias, String... fieldAliases) {
        return new TableAlias<R>(this, alias, fieldAliases, true);
    }

    @Override
    final Fields<R> fields0() {
        return new Fields<R>(query.getSelect());
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return query.getRecordType();
    }

    @Override
    public final void accept(Context<?> ctx) {
        boolean subquery = ctx.subquery();

        ctx.subquery(true)
           .formatIndentStart()
           .formatNewLine()
           .visit(query)
           .formatIndentEnd()
           .formatNewLine()
           .subquery(subquery);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
