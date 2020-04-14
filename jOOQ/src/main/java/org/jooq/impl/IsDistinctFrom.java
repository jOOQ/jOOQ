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

import static org.jooq.Comparator.IS_DISTINCT_FROM;
import static org.jooq.Comparator.IS_NOT_DISTINCT_FROM;
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.exists;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.isEmbeddable;

import java.util.Set;

import org.jooq.Comparator;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPartInternal;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class IsDistinctFrom<T> extends AbstractCondition {


    /**
     * Generated UID
     */
    private static final long            serialVersionUID            = 4568269684824736461L;
    private static final Set<SQLDialect> EMULATE_DISTINCT_PREDICATE  = SQLDialect.supportedUntil(CUBRID, DERBY);
    private static final Set<SQLDialect> SUPPORT_DISTINCT_WITH_ARROW = SQLDialect.supportedBy(MARIADB, MYSQL);

    private final Field<T>               lhs;
    private final Field<T>               rhs;
    private final Comparator             comparator;

    IsDistinctFrom(Field<T> lhs, Field<T> rhs, Comparator comparator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.comparator = comparator;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (isEmbeddable(lhs) && isEmbeddable(rhs))
            ctx.visit(row(embeddedFields(lhs)).compare(comparator, row(embeddedFields(rhs))));

        // [#3511]         These dialects need to emulate the IS DISTINCT FROM predicate,
        //                 optimally using INTERSECT...
        // [#7222] [#7224] Make sure the columns are aliased
        else if (EMULATE_DISTINCT_PREDICATE.contains(ctx.dialect()))
            ctx.visit(comparator == IS_DISTINCT_FROM
                ? (QueryPartInternal) notExists(select(lhs.as("x")).intersect(select(rhs.as("x"))))
                : (QueryPartInternal) exists(select(lhs.as("x")).intersect(select(rhs.as("x")))));

        // MySQL knows the <=> operator
        else if (SUPPORT_DISTINCT_WITH_ARROW.contains(ctx.dialect())) {
            ctx.visit(comparator == IS_DISTINCT_FROM
                ? condition("{not}({0} <=> {1})", lhs, rhs)
                : condition("{0} <=> {1}", lhs, rhs));
        }

        // SQLite knows the IS / IS NOT predicate
        else if (SQLITE == ctx.family())
            ctx.visit(comparator == IS_DISTINCT_FROM
                ? condition("{0} {is not} {1}", lhs, rhs)
                : condition("{0} {is} {1}", lhs, rhs));










        else
            ctx.visit(new CompareCondition(lhs, rhs, comparator));
    }
}
