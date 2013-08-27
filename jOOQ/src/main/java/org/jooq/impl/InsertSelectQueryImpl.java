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

import static org.jooq.Clause.INSERT;
import static org.jooq.Clause.INSERT_INSERT_INTO;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE;
import static org.jooq.Clause.INSERT_RETURNING;
import static org.jooq.Clause.INSERT_SELECT;
import static org.jooq.impl.Utils.visitAll;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class InsertSelectQueryImpl<R extends Record> extends AbstractQuery implements Insert<R> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1540775270159018516L;
    private static final Clause[] CLAUSES          = { INSERT };

    private final Table<?>        into;
    private final Field<?>[]      fields;
    private final Select<?>       select;

    InsertSelectQueryImpl(Configuration configuration, Table<?> into, Field<?>[] fields, Select<?> select) {
        super(configuration);

        this.into = into;
        this.fields = (fields == null || fields.length == 0) ? into.fields() : fields;
        this.select = select;
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.start(INSERT_INSERT_INTO)
               .keyword("insert into")
               .sql(" ")
               .visit(into)
               .sql(" (");

        // [#989] Avoid qualifying fields in INSERT field declaration
        boolean qualify = context.qualify();
        context.qualify(false);

        String separator = "";
        for (Field<?> field : fields) {
            context.sql(separator)
                   .visit(field);

            separator = ", ";
        }

        context.qualify(qualify);
        context.sql(")")
               .end(INSERT_INSERT_INTO)
               .formatSeparator()
               .start(INSERT_SELECT)
               .visit(select)
               .end(INSERT_SELECT)
               .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
               .end(INSERT_ON_DUPLICATE_KEY_UPDATE)
               .start(INSERT_RETURNING)
               .end(INSERT_RETURNING);
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(into);
        visitAll(context, fields);
        context.visit(select);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
