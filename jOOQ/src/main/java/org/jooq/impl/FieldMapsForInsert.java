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

import static org.jooq.Clause.INSERT_SELECT;
import static org.jooq.Clause.INSERT_VALUES;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Record;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class FieldMapsForInsert extends AbstractQueryPart {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID      = -6227074228534414225L;

    final List<FieldMapForInsert> insertMaps;

    FieldMapsForInsert() {
        insertMaps = new ArrayList<FieldMapForInsert>();
        insertMaps.add(null);
    }

    // -------------------------------------------------------------------------
    // The QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        if (!isExecutable()) {
            ctx.formatSeparator()
               .start(INSERT_VALUES)
               .keyword("default values")
               .end(INSERT_VALUES);
        }

        // Single record inserts can use the standard syntax in any dialect
        else if (insertMaps.size() == 1 || insertMaps.get(1) == null) {
            ctx.formatSeparator()
               .start(INSERT_VALUES)
               .keyword("values")
               .sql(' ')
               .visit(insertMaps.get(0))
               .end(INSERT_VALUES);
        }

        // True SQL92 multi-record inserts aren't always supported
        else {
            switch (ctx.family()) {

                // Some dialects don't support multi-record inserts
























                case FIREBIRD:
                case SQLITE: {
                    ctx.formatSeparator()
                       .start(INSERT_SELECT);
                    ctx.visit(insertSelect(ctx));
                    ctx.end(INSERT_SELECT);

                    break;
                }

                default: {
                    ctx.formatSeparator()
                       .start(INSERT_VALUES)
                       .keyword("values")
                       .sql(' ');
                    toSQL92Values(ctx);
                    ctx.end(INSERT_VALUES);

                    break;
                }
            }
        }
    }

    private final Select<Record> insertSelect(Context<?> context) {
        Select<Record> select = null;

        for (FieldMapForInsert map : insertMaps) {
            if (map != null) {
                Select<Record> iteration = DSL.using(context.configuration()).select(map.values());

                if (select == null) {
                    select = iteration;
                }
                else {
                    select = select.unionAll(iteration);
                }
            }
        }

        return select;
    }

    private final void toSQL92Values(Context<?> context) {
        context.visit(insertMaps.get(0));

        int i = 0;
        for (FieldMapForInsert map : insertMaps) {
            if (map != null && i > 0) {
                context.sql(", ");
                context.visit(map);
            }

            i++;
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    // -------------------------------------------------------------------------
    // The FieldMapsForInsert API
    // -------------------------------------------------------------------------

    final boolean isExecutable() {
        return !insertMaps.isEmpty() && insertMaps.get(0) != null;
    }

    public final FieldMapForInsert getMap() {
        if (insertMaps.get(index()) == null) {
            insertMaps.set(index(), new FieldMapForInsert());
        }

        return insertMaps.get(index());
    }

    public final void newRecord() {
        if (insertMaps.get(index()) != null) {
            insertMaps.add(null);
        }
    }

    private final int index() {
        return insertMaps.size() - 1;
    }
}
