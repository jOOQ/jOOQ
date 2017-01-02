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

import org.jooq.Context;
import org.jooq.QueryPart;

final class SQLCondition extends AbstractCondition {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7661748411414898501L;

    private final QueryPart   delegate;

    SQLCondition(QueryPart delegate) {
        this.delegate = delegate;
    }

    // ------------------------------------------------------------------------
    // QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        // We have no control over the plain SQL content, hence we MUST put it
        // in parentheses to ensure correct semantics

        ctx.sql('(');
        ctx.visit(delegate);
        ctx.sql(')');
    }
}
