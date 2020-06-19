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

import org.jooq.Context;
import org.jooq.Keyword;
import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.SettingsTools;

/**
 * A default {@link Keyword} implementation.
 *
 * @author Lukas Eder
 */
final class KeywordImpl extends AbstractQueryPart implements Keyword {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 9137269798087732005L;

    private final String      asIs;

    private       String      lower;
    private       String      upper;
    private       String      pascal;

    KeywordImpl(String keyword) {
        this.asIs = keyword;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ctx.separatorRequired())
            ctx.sql(' ');

        ctx.sql(render(ctx), true);
    }

    private String render(Context<?> ctx) {
        RenderKeywordCase style = SettingsTools.getRenderKeywordCase(ctx.settings());

        switch (style) {
            case AS_IS:  return asIs;
            case LOWER:  return lower == null  ? lower  = asIs.toLowerCase() : lower;
            case UPPER:  return upper == null  ? upper  = asIs.toUpperCase() : upper;
            case PASCAL: return pascal == null ? pascal = pascal(asIs)       : pascal;
            default:
                throw new UnsupportedOperationException("Unsupported style: " + style);
        }
    }

    private static final String pascal(String keyword) {
        if (keyword.isEmpty())
            return keyword;
        else if (keyword.indexOf(' ') >= 0) {
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
            return Character.toUpperCase(keyword.charAt(0)) + keyword.substring(1).toLowerCase();
    }
}
