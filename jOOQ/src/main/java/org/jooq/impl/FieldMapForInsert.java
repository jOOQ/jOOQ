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

import static org.jooq.Clause.FIELD_ROW;
import static org.jooq.impl.Utils.visitAll;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class FieldMapForInsert extends AbstractQueryPartMap<Field<?>, Field<?>> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -2192833491610583485L;
    private static final Clause[] CLAUSES          = { FIELD_ROW };

    FieldMapForInsert() {
    }

    @Override
    public final void toSQL(RenderContext context) {
        boolean indent = (size() > 1);

        context.sql("(");

        if (indent) {
            context.formatIndentStart();
        }

        String separator = "";
        for (Field<?> field : values()) {
            context.sql(separator);

            if (indent) {
                context.formatNewLine();
            }

            context.visit(field);
            separator = ", ";
        }

        if (indent) {
            context.formatIndentEnd()
                   .formatNewLine();
        }

        context.sql(")");
    }

    final void toSQLReferenceKeys(RenderContext context) {
        boolean indent = (size() > 1);

        context.sql("(");

        if (indent) {
            context.formatIndentStart();
        }

        // [#989] Avoid qualifying fields in INSERT field declaration
        boolean qualify = context.qualify();
        context.qualify(false);

        String separator = "";
        for (Field<?> field : keySet()) {
            context.sql(separator);

            if (indent) {
                context.formatNewLine();
            }

            context.visit(field);
            separator = ", ";
        }

        context.qualify(qualify);

        if (indent) {
            context.formatIndentEnd()
                   .formatNewLine();
        }

        context.sql(")");
    }

    @Override
    public final void bind(BindContext context) {
        visitAll(context, keySet());
        visitAll(context, values());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    final void putFields(Collection<? extends Field<?>> fields) {
        for (Field<?> field : fields) {
            put(field, null);
        }
    }

    final void putValues(Collection<? extends Field<?>> values) {
        if (values.size() != size()) {
            throw new IllegalArgumentException("The number of values must match the number of fields: " + this);
        }

        Iterator<? extends Field<?>> it = values.iterator();
        for (Entry<Field<?>, Field<?>> entry : entrySet()) {
            entry.setValue(it.next());
        }
    }

    final void set(Map<? extends Field<?>, ?> map) {
        for (Entry<? extends Field<?>, ?> entry : map.entrySet()) {
            Field<?> field = entry.getKey();
            Object value = entry.getValue();

            put(entry.getKey(), Utils.field(value, field));
        }
    }
}
