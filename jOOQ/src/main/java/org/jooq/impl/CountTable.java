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

import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.jooq.Context;
import org.jooq.Function1;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.impl.QOM.MCountTable;
import org.jooq.impl.QOM.MQueryPart;
import org.jooq.impl.QOM.MTable;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class CountTable extends AbstractAggregateFunction<Integer> implements MCountTable {

    private final Table<?> table;

    CountTable(Table<?> table, boolean distinct) {
        super(distinct, "count", SQLDataType.INTEGER, DSL.field(DSL.name(table.getName())));

        this.table = table;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {





            case POSTGRES:
            case YUGABYTE: {
                super.accept(ctx);
                break;
            }

            default: {
                UniqueKey<?> pk = table.getPrimaryKey();

                if (pk != null)
                    ctx.visit(new DefaultAggregateFunction<>(distinct, "count", SQLDataType.INTEGER, table.fields(pk.getFieldsArray())));
                else
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
    public final <R> R traverse(
        R init,
        Predicate<? super R> abort,
        Predicate<? super MQueryPart> recurse,
        BiFunction<? super R, ? super MQueryPart, ? extends R> accumulate
    ) {
        return super.traverse(
            QOM.traverse(
                init, abort, recurse, accumulate, this,
                $table()
            ), abort, recurse, accumulate
        );
    }

    @Override
    public final MQueryPart replace(
        Predicate<? super MQueryPart> recurse,
        Function1<? super MQueryPart, ? extends MQueryPart> replacement
    ) {
        return QOM.replace(
            this,
            $table(),
            distinct,
            CountTable::new,
            recurse,
            replacement
        );
    }
}
