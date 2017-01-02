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
import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_OVERLAPS;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.QueryPartInternal;
import org.jooq.Row2;

/**
 * @author Lukas Eder
 */
final class RowOverlapsCondition<T1, T2> extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 85887551884667824L;
    private static final Clause[] CLAUSES          = { CONDITION, CONDITION_OVERLAPS };

    private final Row2<T1, T2>    left;
    private final Row2<T1, T2>    right;

    RowOverlapsCondition(Row2<T1, T2> left, Row2<T1, T2> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate(ctx.configuration()));
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    private final QueryPartInternal delegate(Configuration configuration) {
        Field<T1> left1 = left.field1();
        Field<T2> left2 = left.field2();
        Field<T1> right1 = right.field1();
        Field<T2> right2 = right.field2();

        DataType<?> type0 = left1.getDataType();
        DataType<?> type1 = left2.getDataType();

        // The SQL standard only knows temporal OVERLAPS predicates:
        // (DATE, DATE)     OVERLAPS (DATE, DATE)
        // (DATE, INTERVAL) OVERLAPS (DATE, INTERVAL)
        boolean standardOverlaps = type0.isDateTime() && type1.isTemporal();
        boolean intervalOverlaps = type0.isDateTime() && (type1.isInterval() || type1.isNumeric());

        // The non-standard OVERLAPS predicate is always emulated
        if (!standardOverlaps || asList(CUBRID, DERBY, FIREBIRD, H2, MARIADB, MYSQL, SQLITE).contains(configuration.family())) {

            // Interval OVERLAPS predicates need some additional arithmetic
            if (intervalOverlaps) {
                return (QueryPartInternal)
                       right1.le(left1.add(left2)).and(
                       left1.le(right1.add(right2)));
            }

            // All other OVERLAPS predicates can be emulated simply
            else {
                return (QueryPartInternal)
                       right1.le(left2.cast(right1)).and(
                       left1.le(right2.cast(left1)));
            }
        }

        // These dialects seem to have trouble with INTERVAL OVERLAPS predicates
        else if (intervalOverlaps && asList(HSQLDB).contains(configuration.dialect())) {
                return (QueryPartInternal)
                        right1.le(left1.add(left2)).and(
                        left1.le(right1.add(right2)));
        }

        // Everyone else can handle OVERLAPS (Postgres, Oracle)
        else {
            return new Native();
        }
    }

    private class Native extends AbstractCondition {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -1552476981094856727L;

        @Override
        public final void accept(Context<?> ctx) {
            ctx.sql('(').visit(left)
               .sql(' ').keyword("overlaps")
               .sql(' ').visit(right)
               .sql(')');
        }

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }
    }
}