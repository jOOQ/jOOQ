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

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;

import java.util.Map;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class FieldMapForUpdate extends AbstractQueryPartMap<Field<?>, Field<?>> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6139709404698673799L;

    private final Clause      assignmentClause;

    FieldMapForUpdate(Clause assignmentClause) {
        this.assignmentClause = assignmentClause;
    }

    @Override
    public final void toSQL(RenderContext context) {
        if (size() > 0) {
            String separator = "";

            // [#989] Some dialects do not support qualified column references
            // in the UPDATE statement's SET clause

            // [#2055] Other dialects require qualified column references to
            // disambiguated columns in queries like
            // UPDATE t1 JOIN t2 .. SET t1.val = ..., t2.val = ...
            boolean restoreQualify = context.qualify();
            boolean supportsQualify = asList(POSTGRES, SQLITE).contains(context.configuration().dialect()) ? false : restoreQualify;

            for (Entry<Field<?>, Field<?>> entry : entrySet()) {
                context.sql(separator);

                if (!"".equals(separator)) {
                    context.formatNewLine();
                }

                context.start(assignmentClause)
                       .qualify(supportsQualify)
                       .visit(entry.getKey())
                       .qualify(restoreQualify)
                       .sql(" = ")
                       .visit(entry.getValue())
                       .end(assignmentClause);

                separator = ", ";
            }
        }
        else {
            context.sql("[ no fields are updated ]");
        }
    }

    @Override
    public final void bind(BindContext context) {
        for (Entry<Field<?>, Field<?>> entry : entrySet()) {
            context.visit(entry.getKey());
            context.visit(entry.getValue());
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    final void set(Map<? extends Field<?>, ?> map) {
        for (Entry<? extends Field<?>, ?> entry : map.entrySet()) {
            Field<?> field = entry.getKey();
            Object value = entry.getValue();

            put(entry.getKey(), Utils.field(value, field));
        }
    }
}
