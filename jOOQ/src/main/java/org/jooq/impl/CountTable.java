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

import org.jooq.Context;
import org.jooq.Table;
import org.jooq.UniqueKey;

/**
 * @author Lukas Eder
 */
final class CountTable extends Function<Integer> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7292087943334025737L;

    private final Table<?>    table;
    private final boolean     distinct;

    CountTable(Table<?> table, boolean distinct) {
        super("count", distinct, SQLDataType.INTEGER, DSL.field("{0}", DSL.name(table.getName())));

        this.table = table;
        this.distinct = distinct;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case POSTGRES: {
                super.accept(ctx);
                break;
            }

            default: {
                UniqueKey<?> pk = table.getPrimaryKey();

                if (pk != null)
                    ctx.visit(new Function<Integer>("count", distinct, SQLDataType.INTEGER, table.fields(pk.getFieldsArray())));
                else
                    super.accept(ctx);

                break;
            }
        }
    }
}
