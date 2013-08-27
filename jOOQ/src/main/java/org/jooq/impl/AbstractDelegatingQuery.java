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

import java.util.List;
import java.util.Map;

import org.jooq.AttachableInternal;
import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.RenderContext;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
abstract class AbstractDelegatingQuery<Q extends Query> extends AbstractQueryPart implements Query {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 6710523592699040547L;
    private final Q           delegate;

    AbstractDelegatingQuery(Q delegate) {
        this.delegate = delegate;
    }

    @Override
    public final Configuration configuration() {
        if (delegate instanceof AttachableInternal) {
            return ((AttachableInternal) delegate).configuration();
        }

        return super.configuration();
    }

    @Override
    public final List<Object> getBindValues() {
        return delegate.getBindValues();
    }

    @Override
    public final Map<String, Param<?>> getParams() {
        return delegate.getParams();
    }

    @Override
    public final Param<?> getParam(String name) {
        return delegate.getParam(name);
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.visit(delegate);
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(delegate);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {

        // Delegate queries don't emit clauses themselves.
        return null;
    }

    @Override
    public final String getSQL() {
        return delegate.getSQL();
    }

    @Override
    @Deprecated
    public final String getSQL(boolean inline) {
        return delegate.getSQL(inline);
    }

    @Override
    public final String getSQL(ParamType paramType) {
        return delegate.getSQL(paramType);
    }

    @Override
    public final void attach(Configuration configuration) {
        delegate.attach(configuration);
    }

    @Override
    public final int execute() {
        return delegate.execute();
    }

    @Override
    public final boolean isExecutable() {
        return delegate.isExecutable();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q bind(String param, Object value) {
        return (Q) delegate.bind(param, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q bind(int index, Object value) {
        return (Q) delegate.bind(index, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q queryTimeout(int timeout) {
        return (Q) delegate.queryTimeout(timeout);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Q keepStatement(boolean keepStatement) {
        return (Q) delegate.keepStatement(keepStatement);
    }

    @Override
    public final void close() {
        delegate.close();
    }

    @Override
    public final void cancel() {
        delegate.cancel();
    }

    final Q getDelegate() {
        return delegate;
    }
}
