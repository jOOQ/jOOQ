/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
 *
 *
 *
 */
package org.jooq.impl;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QualifiedAsterisk;
import org.jooq.Query;
import org.jooq.Table;
import org.jooq.impl.QOM.UProxy;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * A {@link Field} that acts as another field, allowing for the proxied field to
 * be replaced.
 *
 * @author Lukas Eder
 */
final class QualifiedAsteriskProxy
extends
    AbstractQueryPart
implements
    QualifiedAsterisk,
    UProxy<QualifiedAsterisk>,
    ScopeMappable
{

    /**
     * The resolved expression after a successful meta lookup.
     */
    private QualifiedAsteriskImpl delegate;

    /**
     * The position in the parsed SQL string where this qualified asterisk proxy
     * was encountered.
     */
    private final int             position;

    /**
     * The scope owner that produced this qualified asterisk proxy.
     */
    Query                         scopeOwner;

    /**
     * Whether this qualified asterisk proxy could be resolved at some scope
     * level.
     */
    boolean                       resolved;

    QualifiedAsteriskProxy(QualifiedAsteriskImpl delegate, int position) {
        this.delegate = delegate;
        this.position = position;
    }

    final int position() {
        return position;
    }

    final void delegate(QualifiedAsteriskImpl newDelegate) {
        resolve();
        this.delegate = newDelegate;
    }

    final QualifiedAsteriskProxy resolve() {
        this.resolved = true;
        this.scopeOwner = null;

        return this;
    }

    final void scopeOwner(Query query) {
        if (!resolved && scopeOwner == null)
            scopeOwner = query;
    }

    @Override
    public final int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public final boolean equals(Object that) {
        return delegate.equals(that);
    }

    @Override
    public final boolean declaresFields() {
        return delegate.declaresFields();
    }

    @Override
    public final boolean declaresTables() {
        return delegate.declaresTables();
    }

    @Override
    public final boolean declaresWindows() {
        return delegate.declaresWindows();
    }

    @Override
    public final boolean declaresCTE() {
        return delegate.declaresCTE();
    }

    @Override
    public final boolean generatesCast() {
        return delegate.generatesCast();
    }

    @Override
    public final boolean rendersContent(Context<?> ctx) {
        return delegate.rendersContent(ctx);
    }

    @Override
    public final void accept(Context<?> ctx) {
        delegate.accept(ctx);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return delegate.clauses(ctx);
    }

    @Override
    public final String toString() {
        return delegate.toString();
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final Table<?> qualifier() {
        return delegate.qualifier();
    }

    @Override
    public final QualifiedAsterisk except(String... fieldNames) {
        return new QualifiedAsteriskProxy((QualifiedAsteriskImpl) delegate.except(fieldNames), position);
    }

    @Override
    public final QualifiedAsterisk except(Name... fieldNames) {
        return new QualifiedAsteriskProxy((QualifiedAsteriskImpl) delegate.except(fieldNames), position);
    }

    @Override
    public final QualifiedAsterisk except(Field<?>... fields) {
        return new QualifiedAsteriskProxy((QualifiedAsteriskImpl) delegate.except(fields), position);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final QualifiedAsterisk $delegate() {
        return delegate;
    }

    @Override
    public final Table<?> $table() {
        return delegate.$table();
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $except() {
        return delegate.$except();
    }
}