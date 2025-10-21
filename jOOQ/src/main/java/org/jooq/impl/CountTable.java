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

import java.util.function.Function;

import org.jooq.Context;
import org.jooq.QueryPart;
// ...
import org.jooq.Table;
// ...
import org.jooq.UniqueKey;

/**
 * @author Lukas Eder
 */
final class CountTable
extends
    AbstractAggregateFunction<Integer, CountTable>
implements
    QOM.CountTable
{

    private final Table<?> table;

    CountTable(Table<?> table, boolean distinct) {
        super(distinct, "count", SQLDataType.INTEGER, DSL.field(DSL.name(table.getName())));

        this.table = table;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {




















            case CLICKHOUSE:
            case CUBRID:
            case DERBY:
            case DUCKDB:
            case FIREBIRD:
            case H2:
            case HSQLDB:
            case IGNITE:
            case MARIADB:
            case MYSQL:
            case SQLITE:
            case TRINO: {
                UniqueKey<?> pk = table.getPrimaryKey();

                if (pk != null)
                    ctx.visit(new DefaultAggregateFunction<>(distinct, "count", SQLDataType.INTEGER, table.fields(pk.getFieldsArray())));
                else
                    super.accept(ctx);

                break;
            }

            default: {
                super.accept(ctx);
                break;
            }
        }
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<?> $table() {
        return table;
    }

    @Override
    final CountTable copy2(Function<CountTable, CountTable> function) {
        return function.apply(new CountTable(table, distinct));
    }
























}
