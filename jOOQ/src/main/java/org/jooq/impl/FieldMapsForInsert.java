/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.Clause.INSERT_SELECT;
import static org.jooq.Clause.INSERT_VALUES;
import static org.jooq.impl.Utils.visitAll;

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
class FieldMapsForInsert extends AbstractQueryPart {

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
    public final void toSQL(RenderContext context) {
        if (!isExecutable()) {
            context.sql("[ no fields are inserted ]");
        }

        // Single record inserts can use the standard syntax in any dialect
        else if (insertMaps.size() == 1 || insertMaps.get(1) == null) {
            context.formatSeparator()
                   .start(INSERT_VALUES)
                   .keyword("values")
                   .sql(" ")
                   .visit(insertMaps.get(0))
                   .end(INSERT_VALUES);
        }

        // True SQL92 multi-record inserts aren't always supported
        else {
            switch (context.configuration().dialect().family()) {

                // Some dialects don't support multi-record inserts
                case ASE:
                case FIREBIRD:
                case INGRES:
                case ORACLE:
                case SQLITE:
                    context.start(INSERT_SELECT);
                    toSQLInsertSelect(context);
                    context.end(INSERT_SELECT);

                    break;

                default:
                    context.formatSeparator()
                           .start(INSERT_VALUES)
                           .keyword("values")
                           .sql(" ");
                    toSQL92Values(context);
                    context.end(INSERT_VALUES);

                    break;
            }
        }
    }

    private void toSQLInsertSelect(RenderContext context) {
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

        context.visit(select);
    }

    private void toSQL92Values(RenderContext context) {
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
    public final void bind(BindContext context) {
        visitAll(context, insertMaps);
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
