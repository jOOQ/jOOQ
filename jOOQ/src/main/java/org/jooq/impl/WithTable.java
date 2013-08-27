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
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class WithTable<R extends Record> extends AbstractTable<R> {

    /**
     * Generated UID
     */
    private static final long      serialVersionUID = -3905775637768497535L;

    private final AbstractTable<R> delegate;
    private final String           hint;

    WithTable(AbstractTable<R> delegate, String hint) {
        super(delegate.getName(), delegate.getSchema());

        this.delegate = delegate;
        this.hint = hint;
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.visit(delegate)
               .sql(" ").keyword("with")
               .sql(" (").sql(hint)
               .sql(")");
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(delegate);
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return delegate.getRecordType();
    }

    @Override
    public final Table<R> as(String alias) {
        return new WithTable<R>(new TableAlias<R>(delegate, alias), hint);
    }

    @Override
    public final Table<R> as(String alias, String... fieldAliases) {
        return new WithTable<R>(new TableAlias<R>(delegate, alias, fieldAliases), hint);
    }

    @Override
    final Fields<R> fields0() {
        return delegate.fields0();
    }
}
