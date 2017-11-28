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

import org.jooq.*;

import java.util.Collection;
import java.util.Collections;

import static org.jooq.Clause.GRANT;
import static org.jooq.impl.Keywords.K_GRANT;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_TO;

/**
 * @author Timur Shaidullin
 */
final class GrantImpl<R extends Record> extends AbstractQuery implements Grant<R> {

    /**
     * Generated UID
     */
    private static final long               serialVersionUID = -6509384254822040545L;
    private Clause[]                        CLAUSE = { GRANT };
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
        ctx.start(GRANT)
            .visit(K_GRANT).sql(' ');

        Privilege[] arrayOfPrivileges = privileges.toArray(new Privilege[privileges.size()]);

        for (int i = 0; i < arrayOfPrivileges.length; i++) {
            ctx.visit(arrayOfPrivileges[i]);

            if (i != arrayOfPrivileges.length - 1) {
                ctx.sql(",");
            }

            ctx.sql(' ');
        }

        ctx.visit(K_ON).sql(' ')
            .visit(table).sql(' ')
            .visit(K_TO).sql(' ');

        if (user != null) {
            ctx.visit(user);
        }

        if (role != null) {
            if (user != null) {
                ctx.sql(", "); //todo check
            }

            ctx.visit(role);
        }
    }

    @Override
    public Clause[] clauses(Context<?> ctx) {
        return CLAUSE;
    }

    // ------------------------------------------------------------------------
    // XXX: Grant API
    // ------------------------------------------------------------------------

    @Override
    public GrantImpl<R> grant(Privilege privilege) {
        this.privileges = Collections.singletonList(privilege);
        return this;
    }

    @Override
    public GrantImpl<R> grant(Collection<? extends Privilege> privileges) {
        this.privileges = privileges;
        return this;
    }

    @Override
    public GrantImpl<R> on(Table<?> table) {
        this.table = table;
        return this;
    }

    @Override
    public GrantImpl<R> to(User user) {
        this.user = user;
        return this;
    }

    @Override
    public GrantImpl<R> to(Role role) {
        this.role = role;
        return this;
    }
}
