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

import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * @author Lukas Eder
 */
class FunctionTable<R extends Record> extends AbstractTable<R> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 2380426377794577041L;

    private final Field<?>       function;

    FunctionTable(Field<?> function) {
        super("function_table");

        this.function = function;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class<? extends R> getRecordType() {
        return (Class<? extends R>) RecordImpl.class;
    }

    @Override
    public final Table<R> as(String as) {
        return new TableAlias<R>(new FunctionTable<R>(function), as);
    }

    @Override
    public final Table<R> as(String as, String... fieldAliases) {
        return new TableAlias<R>(new FunctionTable<R>(function), as, fieldAliases);
    }

    @Override
    public final void toSQL(RenderContext context) {
        switch (context.configuration().dialect()) {
            case HSQLDB: {
                context.keyword("table(").visit(function).sql(")");
                break;
            }

            default:
                throw new SQLDialectNotSupportedException("FUNCTION TABLE is not supported for " + context.configuration().dialect());
        }
    }

    @Override
    public final void bind(BindContext context) {
        switch (context.configuration().dialect()) {
            case HSQLDB:
                context.visit(function);
                break;

            default:
                throw new SQLDialectNotSupportedException("FUNCTION TABLE is not supported for " + context.configuration().dialect());
        }
    }

    @Override
    final Fields<R> fields0() {
        return new Fields<R>();
    }
}
