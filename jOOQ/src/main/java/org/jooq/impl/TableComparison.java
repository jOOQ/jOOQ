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

import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.sql;

import org.jooq.Comparator;
import org.jooq.Context;
import org.jooq.Record;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class TableComparison<R extends Record> extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7033304130029726093L;
    private final Table<R>    lhs;
    private final Table<R>    rhs;
    private final Comparator  comparator;

    TableComparison(Table<R> lhs, Table<R> rhs, Comparator comparator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.comparator = comparator;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case POSTGRES: {
                ctx.visit(DSL.condition("{0} {1} {2}", lhs, sql(comparator.toSQL()), rhs));
                break;
            }

            default: {
                ctx.visit(row(lhs.fields()).compare(comparator, row(rhs.fields())));
                break;
            }
        }
    }
}
