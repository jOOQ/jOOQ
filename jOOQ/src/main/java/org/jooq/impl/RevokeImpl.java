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
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>REVOKE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class RevokeImpl
extends
    AbstractDDLQuery
implements
    QOM.Revoke,
    RevokeOnStep,
    RevokeFromStep,
    RevokeFinalStep
{

    final QueryPartListView<? extends Privilege> privileges;
    final boolean                                grantOptionFor;
          Table<?>                               on;
          Role                                   from;
          boolean                                fromPublic;

    RevokeImpl(
        Configuration configuration,
        Collection<? extends Privilege> privileges,
        boolean grantOptionFor
    ) {
        this(
            configuration,
            privileges,
            grantOptionFor,
            null,
            null,
            false
        );
    }

    RevokeImpl(
        Configuration configuration,
        Collection<? extends Privilege> privileges,
        boolean grantOptionFor,
        Table<?> on,
        Role from,
        boolean fromPublic
    ) {
        super(configuration);

        this.privileges = new QueryPartList<>(privileges);
        this.grantOptionFor = grantOptionFor;
        this.on = on;
        this.from = from;
        this.fromPublic = fromPublic;
    }

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

        if (grantOptionFor)
            ctx.visit(K_GRANT_OPTION_FOR)
               .sql(' ');

        ctx.visit(privileges)
           .end(Clause.REVOKE_PRIVILEGE).sql(' ')
           .start(Clause.REVOKE_ON)
           .visit(K_ON).sql(' ')
           .visit(on)
           .end(Clause.REVOKE_ON).sql(' ')
           .start(Clause.REVOKE_FROM)
           .visit(K_FROM).sql(' ');

        if (from != null)
            ctx.visit(from);
        else if (fromPublic)
            ctx.visit(K_PUBLIC);

        if (ctx.family() == HSQLDB)
            ctx.sql(' ').visit(K_RESTRICT);

        ctx.end(Clause.REVOKE_FROM);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSE;
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UnmodifiableList<? extends Privilege> $privileges() {
        return QOM.unmodifiable(privileges);
    }

    @Override
    public final boolean $grantOptionFor() {
        return grantOptionFor;
    }

    @Override
    public final Table<?> $on() {
        return on;
    }

    @Override
    public final Role $from() {
        return from;
    }

    @Override
    public final boolean $fromPublic() {
        return fromPublic;
    }

    @Override
    public final QOM.Revoke $privileges(UnmodifiableList<? extends Privilege> newValue) {
        return constructor().apply(newValue, $grantOptionFor(), $on(), $from(), $fromPublic());
    }

    @Override
    public final QOM.Revoke $grantOptionFor(boolean newValue) {
        return constructor().apply($privileges(), newValue, $on(), $from(), $fromPublic());
    }

    @Override
    public final QOM.Revoke $on(Table<?> newValue) {
        return constructor().apply($privileges(), $grantOptionFor(), newValue, $from(), $fromPublic());
    }

    @Override
    public final QOM.Revoke $from(Role newValue) {
        return constructor().apply($privileges(), $grantOptionFor(), $on(), newValue, $fromPublic());
    }

    @Override
    public final QOM.Revoke $fromPublic(boolean newValue) {
        return constructor().apply($privileges(), $grantOptionFor(), $on(), $from(), newValue);
    }

    public final Function5<? super UnmodifiableList<? extends Privilege>, ? super Boolean, ? super Table<?>, ? super Role, ? super Boolean, ? extends QOM.Revoke> constructor() {
        return (a1, a2, a3, a4, a5) -> new RevokeImpl(configuration(), (Collection<? extends Privilege>) a1, a2, a3, a4, a5);
    }



























}
