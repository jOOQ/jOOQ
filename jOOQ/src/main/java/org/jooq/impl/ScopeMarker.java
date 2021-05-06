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

// ...
// ...
// ...
import static org.jooq.impl.Keywords.K_DECLARE;
import static org.jooq.impl.Tools.increment;
import static org.jooq.impl.Tools.DataKey.DATA_TOP_LEVEL_CTE;
import static org.jooq.impl.WithImpl.acceptWithRecursive;

import org.jooq.Clause;
import org.jooq.Context;
// ...
import org.jooq.QueryPartInternal;
import org.jooq.Statement;
import org.jooq.impl.AbstractContext.ScopeStackElement;
import org.jooq.impl.Tools.DataExtendedKey;
import org.jooq.impl.Tools.DataKey;

/**
 * A set of markers for use with the {@link ScopeStack}.
 */
enum ScopeMarker {
























































    TOP_LEVEL_CTE(
        true,
        DATA_TOP_LEVEL_CTE,
        (ctx, beforeFirst, afterLast, object) -> {
            TopLevelCte cte = (TopLevelCte) object;
            boolean single = cte.size() == 1;
            boolean noWith = afterLast != null && beforeFirst.positions[0] == afterLast.positions[0];

            if (noWith) {
                acceptWithRecursive(ctx, cte.recursive);

                if (single)
                    ctx.formatIndentStart()
                       .formatSeparator();
                else
                    ctx.sql(' ');
            }

            // [#11587] Recurse to allow for deeply nested CTE
            ctx.scopeStart().data(DATA_TOP_LEVEL_CTE, new TopLevelCte());
            ctx.declareCTE(true).visit(cte).declareCTE(false);
            ctx.scopeEnd();

            if (noWith) {
                if (single)
                    ctx.formatIndentEnd();
            }

            // Top level CTE are inserted before all other CTEs
            else if (!Tools.isRendersSeparator(cte))
                ctx.sql(',');

            ctx.formatSeparator().sql("");
        }
    );

    final ReplacementRenderer renderer;
    final boolean             topLevelOnly;
    final Object              key;
    final Marker              beforeFirst;
    final Marker              afterLast;

    private ScopeMarker(boolean topLevelOnly, Object key, ReplacementRenderer renderer) {
        this.renderer = renderer;
        this.topLevelOnly = topLevelOnly;
        this.key = key;
        this.beforeFirst = new Marker(name() + "_BEFORE");
        this.afterLast = new Marker(name() + "_AFTER");
    }

    @FunctionalInterface
    interface ReplacementRenderer {
        void render(
            DefaultRenderContext ctx,
            ScopeStackElement beforeFirst,
            ScopeStackElement afterLast,
            ScopeContent content
        );
    }

    interface ScopeContent {
        boolean isEmpty();
    }

    static class Marker implements QueryPartInternal {
        private final String marker;

        Marker(String marker) {
            this.marker = marker;
        }

        @Override
        public final boolean rendersContent(Context<?> ctx) {
            return false;
        }

        @Override
        public final void accept(Context<?> ctx) {}

        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return null;
        }

        @Override
        public final boolean declaresFields() {
            return false;
        }

        @Override
        public final boolean declaresTables() {
            return false;
        }

        @Override
        public final boolean declaresWindows() {
            return false;
        }

        @Override
        public final boolean declaresCTE() {
            return false;
        }











        @Override
        public final boolean generatesCast() {
            return false;
        }

        @Override
        public String toString() {
            return marker;
        }
    }
}
