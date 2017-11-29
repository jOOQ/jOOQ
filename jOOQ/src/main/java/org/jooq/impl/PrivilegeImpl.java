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

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Keyword;
import org.jooq.Privilege;

import static org.jooq.Clause.PRIVILEGE;

/**
 * @author Timur Shaidullin
 */
final class PrivilegeImpl extends AbstractQueryPart implements Privilege {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -3106268610481536038L;
    private static final Clause[] CLAUSES          = { PRIVILEGE };
    private final Keyword         privilege;

    PrivilegeImpl(Keyword privilege) {
        this.privilege = privilege;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public void accept(Context<?> ctx) {
        ctx.visit(privilege);
    }

    @Override
    public Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
