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

import static org.jooq.Clause.TABLE_VALUES;
import static org.jooq.impl.Utils.visitAll;

import org.jooq.BindContext;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Row;
import org.jooq.Select;
import org.jooq.Support;
import org.jooq.Table;

/**
 * An implementation for the <code>VALUES(...)</code> table constructor
 *
 * @author Lukas Eder
 */
class Values<R extends Record> extends AbstractTable<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -637982217747670311L;

    private final Row[]       rows;

    Values(Row[] rows) {
        super("values");

        this.rows = rows;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends R> getRecordType() {
        return (Class<? extends R>) RecordImpl.class;
    }

    @Override
    @Support
    public final Table<R> as(String alias) {
        return new TableAlias<R>(this, alias, true);
    }

    @Override
    public final Table<R> as(String alias, String... fieldAliases) {
        return new TableAlias<R>(this, alias, fieldAliases, true);
    }

    @Override
    public final void toSQL(RenderContext context) {
        switch (context.configuration().dialect().family()) {

            // [#915] Simulate VALUES(..) with SELECT .. UNION ALL SELECT ..
            // for those dialects that do not support a VALUES() constructor
            case FIREBIRD:
            case MARIADB:
            case MYSQL:
            case ORACLE:
            case SQLITE:
            case SYBASE:

            // [#1801] H2 knows a native VALUES(..) constructor, but doesn't
            // have any means to rename it using derived column lists
            case H2: {
                Select<Record> selects = null;
                boolean subquery = context.subquery();

                for (Row row : rows) {
                    Select<Record> select = create().select(row.fields());

                    if (selects == null) {
                        selects = select;
                    }
                    else {
                        selects = selects.unionAll(select);
                    }
                }

                context.formatIndentStart()
                       .formatNewLine()
                       .subquery(true)
                       .visit(selects)
                       .subquery(subquery)
                       .formatIndentEnd()
                       .formatNewLine();
                break;
            }

            // [#915] Native support of VALUES(..)
            case CUBRID:
            case DERBY:
            case HSQLDB:
            case POSTGRES:
            case SQLSERVER:

            // TODO to be verified
            case ASE:
            case DB2:
            case INGRES:
            default: {
                context.start(TABLE_VALUES)
                       .keyword("values")
                       .formatIndentLockStart();

                boolean firstRow = true;
                for (Row row : rows) {
                    if (!firstRow) {
                        context.sql(",").formatSeparator();
                    }

                    context.visit(row);
                    firstRow = false;
                }

                context.formatIndentLockEnd()
                       .end(TABLE_VALUES);
                break;
            }
        }
    }

    @Override
    public final void bind(BindContext context) {
        visitAll(context, rows);
    }

    @Override
    final Fields<R> fields0() {
        return new Fields<R>(rows[0].fields());
    }
}
