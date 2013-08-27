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

import static org.jooq.Clause.TABLE;
import static org.jooq.Clause.TABLE_REFERENCE;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;

/**
 * A <code>QualifiedTable</code> is a {@link Table} that always renders a table
 * name or alias as a literal using {@link RenderContext#literal(String)}
 *
 * @author Lukas Eder
 */
class QualifiedTable extends AbstractTable<Record> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 6937002867156868761L;
    private static final Clause[] CLAUSES          = { TABLE, TABLE_REFERENCE };

    private final String[]    sql;

    QualifiedTable(String... sql) {
        super(sql[sql.length - 1]);

        this.sql = sql;
    }

    // ------------------------------------------------------------------------
    // Table API
    // ------------------------------------------------------------------------

    @Override
    public final void toSQL(RenderContext context) {
        String separator = "";
        for (String string : sql) {
            context.sql(separator);
            context.literal(string);

            separator = ".";
        }
    }

    @Override
    public final void bind(BindContext context) {}

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final Table<Record> as(String alias) {
        return new TableAlias<Record>(this, alias);
    }

    @Override
    public final Table<Record> as(String alias, String... fieldAliases) {
        return new TableAlias<Record>(this, alias, fieldAliases);
    }

    @Override
    final Fields<Record> fields0() {
        return new Fields<Record>();
    }
}
