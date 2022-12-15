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

import org.jooq.Context;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.QOM.UTransient;

/**
 * A delegating table that un-{@link AutoAlias}-es a wrapped table.
 * <p>
 * When emulating the derived column list feature, the auto-aliasing feature
 * would re-alias the table again and again, leading to a
 * {@link StackOverflowError}. This helps prevent it.
 */
final class NoAutoAlias<R extends Record>
extends
    AbstractDelegatingTable<R>
implements
    UTransient
{

    NoAutoAlias(AbstractTable<R> delegate) {
        super(delegate);
    }

    static final <R extends Record> Table<R> noAutoAlias(Table<R> table) {
        return table instanceof AutoAlias ? new NoAutoAlias<>((AbstractTable<R>) table) : table;
    }

    @Override
    final <O extends Record> NoAutoAlias<O> construct(AbstractTable<O> newDelegate) {
        return new NoAutoAlias<>(newDelegate);
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate);
    }
}
