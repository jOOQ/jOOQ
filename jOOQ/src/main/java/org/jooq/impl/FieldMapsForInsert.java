/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
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
    private static final long serialVersionUID = -6227074228534414225L;

    private final List<FieldMapForInsert> insertMaps;

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
            context.sql(insertMaps.get(0));
        }

        // True SQL92 multi-record inserts aren't always supported
        else {
            switch (context.configuration().dialect()) {

                // Some dialects don't support multi-record inserts
                case ASE:
                case FIREBIRD:
                case INGRES:
                case ORACLE:
                case SQLITE:
                    toSQLInsertSelect(context);
                    break;

                default:
                    toSQL92Values(context);
                    break;
            }
        }
    }

    private void toSQLInsertSelect(RenderContext context) {
        insertMaps.get(0).toSQLReferenceKeys(context);
        context.sql(" ");

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

        context.sql(select);
    }

    private void toSQL92Values(RenderContext context) {
        context.sql(insertMaps.get(0));

        int i = 0;
        for (FieldMapForInsert map : insertMaps) {
            if (map != null && i > 0) {
                context.sql(", ");
                map.toSQLReferenceValues(context);
            }

            i++;
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(insertMaps);
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
