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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static org.jooq.impl.Keywords.K_FORCE_INDEX;

import org.jooq.Context;
import org.jooq.Keyword;
import org.jooq.Name;
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.Table;
// ...

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class HintedTable<R extends Record>
extends
    AbstractDelegatingTable<R>
implements
    QOM.HintedTable<R>
{

    private final HintType            hintType;
    private final QueryPartList<Name> arguments;

    HintedTable(AbstractTable<R> delegate, HintType hintType, String... arguments) {
        this(delegate, hintType, new QueryPartList<>(Tools.names(arguments)));
    }

    HintedTable(AbstractTable<R> delegate, HintType hintType, QueryPartList<Name> arguments) {
        super(delegate);

        this.hintType = hintType;
        this.arguments = arguments;
    }

    enum HintType {
        FORCE_INDEX("force index"),
        FORCE_INDEX_FOR_GROUP_BY("force index for group by"),
        FORCE_INDEX_FOR_JOIN("force index for join"),
        FORCE_INDEX_FOR_ORDER_BY("force index for order by"),
        IGNORE_INDEX("ignore index"),
        IGNORE_INDEX_FOR_GROUP_BY("ignore index for group by"),
        IGNORE_INDEX_FOR_JOIN("ignore index for join"),
        IGNORE_INDEX_FOR_ORDER_BY("ignore index for order by"),
        USE_INDEX("use index"),
        USE_INDEX_FOR_GROUP_BY("use index for group by"),
        USE_INDEX_FOR_JOIN("use index for join"),
        USE_INDEX_FOR_ORDER_BY("use index for order by");

        final Keyword keywords;

        private HintType(String keywords) {
            this.keywords = DSL.keyword(keywords);
        }
    }

    @Override
    final <O extends Record> HintedTable<O> construct(AbstractTable<O> newDelegate) {
        return new HintedTable<>(newDelegate, hintType, arguments);
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {










            default:
                acceptDefault(ctx);
                break;
        }
    }

    private final void acceptDefault(Context<?> ctx) {
        ctx.visit(delegate)
            .sql(' ').visit(hintType.keywords)
            .sql(" (").visit(arguments)
            .sql(')');
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------


    @Override
    public final Table<R> $table() {
        return delegate;
    }

    @Override
    public final <O extends Record> HintedTable<O> $table(Table<O> newTable) {
        return construct((AbstractTable<O>) newTable);
    }


















}
