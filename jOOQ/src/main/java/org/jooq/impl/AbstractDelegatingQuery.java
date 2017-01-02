/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import org.jooq.AttachableInternal;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Param;
import org.jooq.Query;
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
    public final void accept(Context<?> context) {
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
    public final void detach() {
        delegate.detach();
    }

    @Override
    public final int execute() {
        return delegate.execute();
    }



    @Override
    public final CompletionStage<Integer> executeAsync() {
        return delegate.executeAsync();
    }

    @Override
    public final CompletionStage<Integer> executeAsync(Executor executor) {
        return delegate.executeAsync(executor);
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
