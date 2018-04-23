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
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
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
import static org.jooq.impl.DSL.select;

import java.util.EnumSet;

import org.jooq.Clause;
import org.jooq.Comparator;
import org.jooq.Configuration;
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
    private static final long                serialVersionUID            = 4568269684824736461L;
    private static final EnumSet<SQLDialect> EMULATE_DISTINCT_PREDICATE  = EnumSet.of(CUBRID, DERBY);
    private static final EnumSet<SQLDialect> SUPPORT_DISTINCT_WITH_ARROW = EnumSet.of(MARIADB, MYSQL);

    private final Field<T>                   lhs;
    private final Field<T>                   rhs;
    private final Comparator                 comparator;

    private transient QueryPartInternal      mySQLCondition;
    private transient QueryPartInternal      sqliteCondition;
    private transient QueryPartInternal      compareCondition;

    IsDistinctFrom(Field<T> lhs, Field<T> rhs, Comparator comparator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.comparator = comparator;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate(ctx.configuration()));
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    /**
     * Get a delegate <code>CompareCondition</code>, in case the context
     * {@link SQLDialect} natively supports the <code>IS DISTINCT FROM</code>
     * clause.
     */
    private final QueryPartInternal delegate(Configuration configuration) {

        // [#3511]         These dialects need to emulate the IS DISTINCT FROM predicate,
        //                 optimally using INTERSECT...
        // [#7222] [#7224] Make sure the columns are aliased
        if (EMULATE_DISTINCT_PREDICATE.contains(configuration.family())) {
            return (comparator == IS_DISTINCT_FROM)
                ? (QueryPartInternal) notExists(select(lhs.as("x")).intersect(select(rhs.as("x"))))
                : (QueryPartInternal) exists(select(lhs.as("x")).intersect(select(rhs.as("x"))));
        }

        // MySQL knows the <=> operator
        else if (SUPPORT_DISTINCT_WITH_ARROW.contains(configuration.family())) {
            if (mySQLCondition == null)
                mySQLCondition = (QueryPartInternal) ((comparator == IS_DISTINCT_FROM)
                    ? condition("{not}({0} <=> {1})", lhs, rhs)
                    : condition("{0} <=> {1}", lhs, rhs));

            return mySQLCondition;
        }

        // SQLite knows the IS / IS NOT predicate
        else if (SQLITE == configuration.family()) {
            if (sqliteCondition == null)
                sqliteCondition = (QueryPartInternal) ((comparator == IS_DISTINCT_FROM)
                    ? condition("{0} {is not} {1}", lhs, rhs)
                    : condition("{0} {is} {1}", lhs, rhs));

            return sqliteCondition;
        }















        // These dialects natively support the IS DISTINCT FROM predicate:
        // H2, Postgres
        else {
            if (compareCondition == null)
                compareCondition = new CompareCondition(lhs, rhs, comparator);

            return compareCondition;
        }
    }
}
