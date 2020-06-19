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

import static org.jooq.impl.ScopeMarkers.AFTER_LAST_TOP_LEVEL_CTE;
import static org.jooq.impl.ScopeMarkers.BEFORE_FIRST_TOP_LEVEL_CTE;

import org.jooq.CommonTableExpression;
import org.jooq.Context;

/**
 * A list of {@link CommonTableExpression} query parts.
 *
 * @author Lukas Eder
 */
final class CommonTableExpressionList extends QueryPartList<CommonTableExpression<?>> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 4284724883554582081L;

    @Override
    public void accept(Context<?> ctx) {
        if (ctx.subqueryLevel() == 0)
            ctx.scopeMarkStart(BEFORE_FIRST_TOP_LEVEL_CTE)
               .scopeMarkEnd(BEFORE_FIRST_TOP_LEVEL_CTE);

        super.accept(ctx);

        if (ctx.subqueryLevel() == 0)
            ctx.scopeMarkStart(AFTER_LAST_TOP_LEVEL_CTE)
               .scopeMarkEnd(AFTER_LAST_TOP_LEVEL_CTE);
    }

    @Override
    public final boolean declaresCTE() {
        return true;
    }
}
