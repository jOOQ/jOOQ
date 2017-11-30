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

import static org.jooq.Clause.GRANT;
import static org.jooq.Clause.GRANT_ON;
import static org.jooq.Clause.GRANT_PRIVILEGE;
import static org.jooq.Clause.GRANT_TO;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Keywords.K_GRANT;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_TO;

import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.GrantFinalStep;
import org.jooq.GrantOnStep;
import org.jooq.GrantToStep;
import org.jooq.Name;
import org.jooq.Privilege;
import org.jooq.Role;
import org.jooq.Table;
import org.jooq.User;

/**
 * Grant privilege or privileges on a table to user or role.
 * @author Timur Shaidullin
 */
final class GrantImpl extends AbstractQuery implements

    // Cascading interface implementations for Select behaviour
    GrantOnStep,
    GrantToStep,
    GrantFinalStep {

    /**
     * Generated UID
     */
    private static final long                     serialVersionUID = -6509384254822040545L;
    private static final Clause[]                 CLAUSE           = { GRANT };
    private final Collection<? extends Privilege> privileges;
    private Role                                  role;
    private Table<?>                              table;
    private User                                  user;

    GrantImpl(Configuration configuration, Collection<? extends Privilege> privileges) {
        super(configuration);

        this.privileges = privileges;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.start(GRANT_PRIVILEGE)
           .visit(K_GRANT).sql(' ');

        String separator = "";
        for (Privilege privilege : privileges) {
            ctx.sql(separator)
               .visit(privilege);

            separator = ", ";
        }

        ctx.end(GRANT_PRIVILEGE).sql(' ')
           .start(GRANT_ON)
           .visit(K_ON).sql(' ')
           .visit(table)
           .end(GRANT_ON).sql(' ')
           .start(GRANT_TO)
           .visit(K_TO).sql(' ');

        if (user != null)
            ctx.visit(user);
        else if (role != null)
            ctx.visit(role);

        ctx.end(GRANT_TO);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSE;
    }

    // ------------------------------------------------------------------------
    // XXX: GrantImpl API
    // ------------------------------------------------------------------------

    @Override
    public final GrantImpl on(Table<?> t) {
        this.table = t;
        return this;
    }

    @Override
    public final GrantImpl on(Name t) {
        return on(table(t));
    }

    @Override
    public final GrantImpl on(String t) {
        return on(table(t));
    }

    @Override
    public final GrantImpl to(User u) {
        this.user = u;
        return this;
    }

    @Override
    public final GrantImpl to(Role r) {
        this.role = r;
        return this;
    }
}
