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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>REVOKE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class RevokeImpl
extends
    AbstractDDLQuery
implements
    RevokeOnStep,
    RevokeFromStep,
    RevokeFinalStep
{

    private final Collection<? extends Privilege> privileges;
    private final boolean                         revokeGrantOptionFor;
    private       Table<?>                        on;
    private       Role                            from;
    private       Boolean                         fromPublic;

    RevokeImpl(
        Configuration configuration,
        Collection<? extends Privilege> privileges,
        boolean revokeGrantOptionFor
    ) {
        this(
            configuration,
            privileges,
            revokeGrantOptionFor,
            null,
            null,
            null
        );
    }

    RevokeImpl(
        Configuration configuration,
        Collection<? extends Privilege> privileges,
        boolean revokeGrantOptionFor,
        Table<?> on,
        Role from,
        Boolean fromPublic
    ) {
        super(configuration);

        this.privileges = privileges;
        this.revokeGrantOptionFor = revokeGrantOptionFor;
        this.on = on;
        this.from = from;
        this.fromPublic = fromPublic;
    }

    final Collection<? extends Privilege> $privileges()           { return privileges; }
    final boolean                         $revokeGrantOptionFor() { return revokeGrantOptionFor; }
    final Table<?>                        $on()                   { return on; }
    final Role                            $from()                 { return from; }
    final Boolean                         $fromPublic()           { return fromPublic; }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final RevokeImpl on(String on) {
        return on(DSL.table(DSL.name(on)));
    }

    @Override
    public final RevokeImpl on(Name on) {
        return on(DSL.table(on));
    }

    @Override
    public final RevokeImpl on(Table<?> on) {
        this.on = on;
        return this;
    }

    @Override
    public final RevokeImpl from(User from) {
        return from(DSL.role(from.getQualifiedName()));
    }

    @Override
    public final RevokeImpl from(Role from) {
        this.from = from;
        return this;
    }

    @Override
    public final RevokeImpl fromPublic() {
        this.fromPublic = true;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[] CLAUSE = { Clause.REVOKE };

    @Override
    public final void accept(Context<?> ctx) {
        ctx.start(Clause.REVOKE_PRIVILEGE)
           .visit(K_REVOKE).sql(' ');

        if (revokeGrantOptionFor)
            ctx.visit(K_GRANT_OPTION_FOR)
               .sql(' ');

        ctx.visit(QueryPartCollectionView.wrap(privileges))
           .end(Clause.REVOKE_PRIVILEGE).sql(' ')
           .start(Clause.REVOKE_ON)
           .visit(K_ON).sql(' ')
           .visit(on)
           .end(Clause.REVOKE_ON).sql(' ')
           .start(Clause.REVOKE_FROM)
           .visit(K_FROM).sql(' ');

        if (from != null)
            ctx.visit(from);
        else if (Boolean.TRUE.equals(fromPublic))
            ctx.visit(K_PUBLIC);

        if (ctx.family() == HSQLDB)
            ctx.sql(' ').visit(K_RESTRICT);

        ctx.end(Clause.REVOKE_FROM);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSE;
    }


}
