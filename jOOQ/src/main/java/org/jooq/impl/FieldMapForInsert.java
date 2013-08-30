/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
