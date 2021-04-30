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
 *
 *
 *
 */
package org.jooq.impl;

import static org.jooq.impl.ScopeMarker.TOP_LEVEL_CTE;

import java.util.function.Consumer;

import org.jooq.CommonTableExpression;
import org.jooq.Context;

/**
 * A list of {@link CommonTableExpression} query parts.
 *
 * @author Lukas Eder
 */
final class CommonTableExpressionList extends QueryPartList<CommonTableExpression<?>> {

    @Override
    public void accept(Context<?> ctx) {
        markTopLevelCteAndAccept(ctx, c -> super.accept(c));
    }

    static void markTopLevelCteAndAccept(Context<?> ctx, Consumer<? super Context<?>> consumer) {
        if (ctx.subqueryLevel() == 0)
            ctx.scopeMarkStart(TOP_LEVEL_CTE.beforeFirst)
               .scopeMarkEnd(TOP_LEVEL_CTE.beforeFirst);

        consumer.accept(ctx);

        if (ctx.subqueryLevel() == 0)
            ctx.scopeMarkStart(TOP_LEVEL_CTE.afterLast)
               .scopeMarkEnd(TOP_LEVEL_CTE.afterLast);
    }

    @Override
    public final boolean declaresCTE() {
        return true;
    }
}
