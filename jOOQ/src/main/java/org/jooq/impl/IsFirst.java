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

import static org.jooq.impl.DSL.rank;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.Names.N_IS_FIRST;
import static org.jooq.impl.SQLDataType.BOOLEAN;

import java.util.function.Function;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
// ...
// ...

/**
 * @author Lukas Eder
 */
final class IsFirst
extends
    AbstractWindowFunction<Boolean, QOM.IsFirst>
implements
    QOM.IsFirst
{

    final Field<Integer> count;

    IsFirst(Field<Integer> count) {
        super(N_IS_FIRST, BOOLEAN.notNull());

        this.count = count;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

























            case CLICKHOUSE:
            case CUBRID:
            case DUCKDB:
            case FIREBIRD:
            case H2:
            case MARIADB:
            case MYSQL:
            case POSTGRES:
            case SQLITE:
            case TRINO:
            case YUGABYTEDB:
                ctx.visit(DSL.field(o(rowNumber()).le(count)));
                break;

            default:
                ctx.visit(N_IS_FIRST).sql("()");
                acceptOverClause(ctx);
                break;
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<Integer> $count() {
        return count;
    }

    @Override
    public final QOM.IsFirst $count(Field<Integer> count) {
        return copyWindowSpecification().apply(new IsFirst(count));
    }

    @Override
    final QOM.IsFirst copyWindowFunction(Function<? super QOM.IsFirst, ? extends QOM.IsFirst> function) {
        return function.apply(new IsFirst(count));
    }



















}
