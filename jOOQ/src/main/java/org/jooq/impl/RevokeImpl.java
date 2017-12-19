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

import static org.jooq.Clause.REVOKE;
import static org.jooq.Clause.REVOKE_FROM;
import static org.jooq.Clause.REVOKE_ON;
import static org.jooq.Clause.REVOKE_PRIVILEGE;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Keywords.K_FROM;
import static org.jooq.impl.Keywords.K_GRANT_OPTION_FOR;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_PUBLIC;
import static org.jooq.impl.Keywords.K_REVOKE;

import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Privilege;
import org.jooq.RevokeFinalStep;
import org.jooq.RevokeFromStep;
import org.jooq.RevokeOnStep;
import org.jooq.Role;
import org.jooq.Table;
import org.jooq.User;

/**
 * Revoke privilege or privileges on a table from user or role.
 *
 * @author Timur Shaidullin
 */
final class RevokeImpl extends AbstractQuery implements

    // Cascading interface implementations for Select behaviour
    RevokeOnStep,
    RevokeFromStep,
    RevokeFinalStep {

    /**
     * Generated UID
     */
    private static final long                     serialVersionUID = -5777612075774539326L;
    private static final Clause[]                 CLAUSE           = { REVOKE };
    private final Collection<? extends Privilege> privileges;
    private Role                                  role;
    private Table<?>                              table;
    private User                                  user;
    private final boolean                         grantOptionFor;

    RevokeImpl(Configuration configuration, Collection<? extends Privilege> privileges, boolean grantOptionFor) {
        super(configuration);
        this.privileges = privileges;
        this.grantOptionFor = grantOptionFor;
    }

    RevokeImpl(Configuration configuration, Collection<? extends Privilege> privileges) {
        this(configuration, privileges, false);
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.start(REVOKE_PRIVILEGE)
           .visit(K_REVOKE).sql(' ');

        if (grantOptionFor)
            ctx.visit(K_GRANT_OPTION_FOR)
               .sql(' ');

        String separator = "";
        for (Privilege privilege : privileges) {
            ctx.sql(separator)
               .visit(privilege);

            separator = ", ";
        }

        ctx.end(REVOKE_PRIVILEGE).sql(' ')
           .start(REVOKE_ON)
           .visit(K_ON).sql(' ')
           .visit(table)
           .end(REVOKE_ON).sql(' ')
           .start(REVOKE_FROM)
           .visit(K_FROM).sql(' ');

        if (user != null)
            ctx.visit(user);
        else if (role != null)
            ctx.visit(role);
        else
            ctx.visit(K_PUBLIC);

        ctx.end(REVOKE_FROM);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSE;
    }

    // ------------------------------------------------------------------------
    // XXX: RevokeImpl API
    // ------------------------------------------------------------------------

    @Override
    public final RevokeImpl on(Table<?> t) {
        this.table = t;
        return this;
    }

    @Override
    public final RevokeImpl on(Name t) {
        return on(table(t));
    }

    @Override
    public final RevokeImpl on(String t) {
        return on(table(t));
    }

    @Override
    public final RevokeImpl from(User u) {
        this.user = u;
        return this;
    }

    @Override
    public final RevokeImpl from(Role r) {
        this.role = r;
        return this;
    }

    @Override
    public final RevokeImpl fromPublic() {
        return this;
    }
}
