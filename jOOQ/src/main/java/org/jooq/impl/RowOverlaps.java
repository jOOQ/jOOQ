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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CLICKHOUSE;
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
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
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.impl.Keywords.K_OVERLAPS;
import static org.jooq.impl.Tools.castIfNeeded;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function2;
import org.jooq.Row;
import org.jooq.Row2;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class RowOverlaps<T1, T2> extends AbstractCondition implements QOM.RowOverlaps {

    private static final Set<SQLDialect> EMULATE_NON_STANDARD_OVERLAPS = SQLDialect.supportedUntil(CLICKHOUSE, CUBRID, DERBY, DUCKDB, FIREBIRD, H2, MARIADB, MYSQL, SQLITE, TRINO);
    private static final Set<SQLDialect> EMULATE_INTERVAL_OVERLAPS     = SQLDialect.supportedBy(CLICKHOUSE, DUCKDB, HSQLDB, TRINO);

    private final Row2<T1, T2>           left;
    private final Row2<T1, T2>           right;

    @SuppressWarnings("unchecked")
    RowOverlaps(Row2<T1, T2> left, Row2<T1, T2> right) {
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
                ctx.visit(right1.le(left1.plus(left2)).and(left1.le(right1.plus(right2))));

            // All other OVERLAPS predicates can be emulated simply
            else
                ctx.visit(right1.le(castIfNeeded(left2, right1)).and(left1.le(castIfNeeded(right2, left1))));
        }

        // These dialects seem to have trouble with INTERVAL OVERLAPS predicates
        else if (intervalOverlaps && EMULATE_INTERVAL_OVERLAPS.contains(ctx.dialect()))
            ctx.visit(right1.le(left1.plus(left2)).and(left1.le(right1.plus(right2))));






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

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Row $arg1() {
        return left;
    }

    @Override
    public final Row $arg2() {
        return right;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Function2<? super Row, ? super Row, ? extends QOM.RowOverlaps> $constructor() {
        return (r1, r2) -> new RowOverlaps<>((Row2<T1, T2>) r1, (Row2<T1, T2>) r2);
    }
}