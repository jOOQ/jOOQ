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

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Keyword;
import org.jooq.conf.RenderKeywordStyle;

/**
 * A default {@link Keyword} implementation.
 *
 * @author Lukas Eder
 */
public class KeywordImpl extends AbstractQueryPart implements Keyword {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 9137269798087732005L;

    private final String      asIs;
    private final String      upper;
    private final String      lower;
    private final String      pascal;

    KeywordImpl(String keyword) {
        this.asIs = keyword;
        this.upper = keyword.toUpperCase();
        this.lower = keyword.toLowerCase();
        this.pascal = keyword.length() > 0 ? pascal(keyword) : keyword;
    }

    private static final String pascal(String keyword) {
        if (keyword.contains(" ")) {
            StringBuilder sb = new StringBuilder();

            int prev = 0;
            int next = 0;

            do {
                next = keyword.indexOf(' ', prev);

                if (prev > 0)
                    sb.append(' ');

                sb.append(Character.toUpperCase(keyword.charAt(prev)));
                sb.append(keyword.substring(prev + 1, next == -1 ? keyword.length() : next).toLowerCase());

                prev = next + 1;
            }
            while (next != -1);

            return sb.toString();
        }
        else
            return keyword.substring(0, 1).toUpperCase() + keyword.substring(1, keyword.length()).toLowerCase();
    }

    @Override
    public final void accept(Context<?> ctx) {
        RenderKeywordStyle style = ctx.settings().getRenderKeywordStyle();

        if (RenderKeywordStyle.AS_IS == style)
            ctx.sql(asIs, true);
        else if (RenderKeywordStyle.UPPER == style)
            ctx.sql(upper, true);
        else if (RenderKeywordStyle.PASCAL == style)
            ctx.sql(pascal, true);
        else
            ctx.sql(lower, true);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
