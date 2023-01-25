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

import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.systemName;
import static org.jooq.impl.Keywords.K_MILLISECOND;
import static org.jooq.impl.Names.N_DATEDIFF;
import static org.jooq.impl.Names.N_DAYS;
import static org.jooq.impl.Names.N_NANO100_BETWEEN;
import static org.jooq.impl.Names.N_STRFTIME;
import static org.jooq.impl.Names.N_TIMESTAMPDIFF;
import static org.jooq.impl.Names.N_TIMESTAMP_DIFF;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.SQLDataType.INTERVALDAYTOSECOND;
import static org.jooq.impl.Tools.castIfNeeded;

import java.sql.Timestamp;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function2;
import org.jooq.types.DayToSecond;

/**
 * @author Lukas Eder
 */
final class TimestampDiff<T> extends AbstractField<DayToSecond> implements QOM.TimestampDiff<T> {

    private final Field<T> timestamp1;
    private final Field<T> timestamp2;

    TimestampDiff(Field<T> timestamp1, Field<T> timestamp2) {
        super(N_TIMESTAMPDIFF, INTERVALDAYTOSECOND);

        this.timestamp1 = timestamp1;
        this.timestamp2 = timestamp2;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {


















































            case POSTGRES:
            case YUGABYTEDB:

                // [#4481] Parentheses are important in case this expression is
                //         placed in the context of other arithmetic
                ctx.sql('(').visit(timestamp1).sql(" - ").visit(timestamp2).sql(')');
                break;

            // CUBRID's datetime operations operate on a millisecond level
            case CUBRID:
                ctx.visit(timestamp1.sub(timestamp2));
                break;

            case DERBY:
                ctx.sql("1000 * {fn ").visit(N_TIMESTAMPDIFF).sql('(').visit(keyword("sql_tsi_second")).sql(", ").visit(timestamp2).sql(", ").visit(timestamp1).sql(") }");
                break;


            case FIREBIRD:
                ctx.visit(N_DATEDIFF).sql('(').visit(K_MILLISECOND).sql(", ").visit(timestamp2).sql(", ").visit(timestamp1).sql(')');
                break;

            case H2:
            case HSQLDB:
                ctx.visit(N_DATEDIFF).sql("('ms', ").visit(timestamp2).sql(", ").visit(timestamp1).sql(')');
                break;

            // MySQL's datetime operations operate on a microsecond level


            case MARIADB:
            case MYSQL:
                ctx.visit(N_TIMESTAMPDIFF).sql('(').visit(keyword("microsecond")).sql(", ").visit(timestamp2).sql(", ").visit(timestamp1).sql(") / 1000");
                break;

            case SQLITE:
                ctx.sql('(').visit(N_STRFTIME).sql("('%s', ").visit(timestamp1).sql(") - ").visit(N_STRFTIME).sql("('%s', ").visit(timestamp2).sql(")) * 1000");
                break;

            default:
                // Default implementation for equals() and hashCode()
                ctx.visit(castIfNeeded(timestamp1.sub(timestamp2), DayToSecond.class));
                break;
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return timestamp1;
    }

    @Override
    public final Field<T> $arg2() {
        return timestamp2;
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<T>, ? extends QOM.TimestampDiff<T>> $constructor() {
        return (t1, t2) -> new TimestampDiff<>(t1, t2);
    }
}
