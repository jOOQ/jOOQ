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

import java.util.List;
import java.util.Map;

import org.jooq.AttachableInternal;
import org.jooq.BindContext;
import org.jooq.Configuration;
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
        context.sql(delegate);
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(delegate);
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
