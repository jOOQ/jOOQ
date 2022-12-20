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

import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.Names.N_COLUMN_VALUE;
import static org.jooq.impl.Tools.componentDataType;
import static org.jooq.impl.Tools.visitSubquery;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.impl.QOM.UTransient;

/**
 * Essentially, this is the same as <code>ArrayTable</code>, except that it
 * emulates unnested arrays using <code>UNION ALL</code>
 *
 * @author Lukas Eder
 */
final class ArrayTableEmulation
extends
    AbstractQueryPart
implements
    UTransient
{

    private final Object[]      array;
    private final DataType<?>   type;
    private final Name          fieldAlias;

    private transient Select<?> table;

    ArrayTableEmulation(Object[] array, Name[] fieldAliases) {
        if (Tools.isEmpty(fieldAliases))
            this.fieldAlias = N_COLUMN_VALUE;
        else if (fieldAliases.length == 1)
            this.fieldAlias = fieldAliases[0];
        else
            throw new IllegalArgumentException("Array table simulations can only have a single field alias");

        this.array = array;
        this.type = componentDataType(array);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        visitSubquery(ctx, table(), SubqueryCharacteristics.DERIVED_TABLE, true);
    }

    private final Select<?> table() {
        if (table == null) {
            Select<Record> select = null;

            for (Object element : array) {

                // [#1081] Be sure to get the correct cast type also for null
                Field<?> val = DSL.val(element, type);
                Select<Record> subselect = DSL.select(val.as(fieldAlias)).select();

                if (select == null)
                    select = subselect;
                else
                    select = select.unionAll(subselect);
            }

            // Empty arrays should result in empty tables
            if (select == null)
                select = DSL.select(one().as(fieldAlias)).select().where(falseCondition());

            table = select;
        }

        return table;
    }
}
