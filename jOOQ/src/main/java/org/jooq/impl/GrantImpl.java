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
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.GrantFirstStep;
import org.jooq.GrantStepOn;
import org.jooq.GrantStepTo;
import org.jooq.Privilege;
import org.jooq.Query;
import org.jooq.Role;
import org.jooq.Table;
import org.jooq.User;

import java.util.Collection;
import java.util.Collections;

import static org.jooq.Clause.GRANT;
import static org.jooq.Clause.GRANT_PRIVILEGE;
import static org.jooq.impl.Keywords.K_GRANT;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_TO;

/**
 * Grant privilege or privileges on a table to user or role.
 * @author Timur Shaidullin
 */
final class GrantImpl extends AbstractQuery implements
    GrantFirstStep,
    GrantStepOn,
    GrantStepTo,
    Query {

    /**
     * Generated UID
     */
    private static final long               serialVersionUID = -6509384254822040545L;
    private Clause[]                        CLAUSE           = { GRANT };
    private Collection<? extends Privilege> privileges;
    private Role                            role;
    private Table<?>                        table;
    private User                            user;

    GrantImpl(Configuration configuration) {
        super(configuration);
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public void accept(Context<?> ctx) {
        ctx.start(GRANT_PRIVILEGE)
            .visit(K_GRANT).sql(' ');

        Privilege[] arrayOfPrivileges = privileges.toArray(Tools.EMPTY_PRIVILEGE);

        for (int i = 0; i < arrayOfPrivileges.length; i++) {
            ctx.visit(arrayOfPrivileges[i]);

            if (i != (arrayOfPrivileges.length - 1)) {
                ctx.sql(',');
            }

            ctx.sql(' ');
        }

        ctx.visit(K_ON).sql(' ')
            .visit(table).sql(' ')
            .visit(K_TO).sql(' ');

        if (user != null) {
            ctx.visit(user);
        } else if (role != null) {
            ctx.visit(role);
        }

        ctx.end(GRANT_PRIVILEGE).sql(';');
    }

    @Override
    public Clause[] clauses(Context<?> ctx) {
        return CLAUSE;
    }

    // ------------------------------------------------------------------------
    // XXX: GrantImpl API
    // ------------------------------------------------------------------------

    @Override
    public GrantStepOn grant(Privilege privilege) {
        this.privileges = Collections.singletonList(privilege);
        return this;
    }

    @Override
    public GrantStepOn grant(Collection<? extends Privilege> privileges) {
        this.privileges = privileges;
        return this;
    }

    @Override
    public GrantStepTo on(Table<?> table) {
        this.table = table;
        return this;
    }

    @Override
    public GrantStepTo on(String table) {
        this.table = DSL.table(table);
        return this;
    }

    @Override
    public Query to(User user) {
        this.user = user;
        return this;
    }

    @Override
    public Query to(Role role) {
        this.role = role;
        return this;
    }
}
