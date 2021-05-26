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

import static org.jooq.Clause.CONDITION;
import static org.jooq.Clause.CONDITION_OVERLAPS;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.impl.Internal.iadd;
import static org.jooq.impl.Keywords.K_OVERLAPS;
import static org.jooq.impl.Tools.castIfNeeded;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class RowOverlapsCondition<T1, T2> extends AbstractCondition {
    private static final Set<SQLDialect> EMULATE_NON_STANDARD_OVERLAPS = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD, H2, MARIADB, MYSQL, SQLITE);
    private static final Set<SQLDialect> EMULATE_INTERVAL_OVERLAPS     = SQLDialect.supportedBy(HSQLDB);

    private final Row2<T1, T2>           left;
    private final Row2<T1, T2>           right;

    @SuppressWarnings("unchecked")
    RowOverlapsCondition(Row2<T1, T2> left, Row2<T1, T2> right) {
        this.left = (Row2<T1, T2>) ((AbstractRow<?>) left).convertTo(right);
        this.right = (Row2<T1, T2>) ((AbstractRow<?>) right).convertTo(left);
    }

    @Override
    public final void accept(Context<?> ctx) {
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
        if (!standardOverlaps || EMULATE_NON_STANDARD_OVERLAPS.contains(ctx.dialect())) {

            // Interval OVERLAPS predicates need some additional arithmetic
            if (intervalOverlaps)
                ctx.visit(right1.le(iadd(left1, left2)).and(left1.le(iadd(right1, right2))));

            // All other OVERLAPS predicates can be emulated simply
            else
                ctx.visit(right1.le(castIfNeeded(left2, right1)).and(left1.le(castIfNeeded(right2, left1))));
        }

        // These dialects seem to have trouble with INTERVAL OVERLAPS predicates
        else if (intervalOverlaps && EMULATE_INTERVAL_OVERLAPS.contains(ctx.dialect()))
            ctx.visit(right1.le(iadd(left1, left2)).and(left1.le(iadd(right1, right2))));






        // Everyone else can handle OVERLAPS
        else
            ctx.sql('(').visit(left)
               .sql(' ').visit(K_OVERLAPS)
               .sql(' ').visit(right)
               .sql(')');
    }

    @Override // Avoid AbstractCondition implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}