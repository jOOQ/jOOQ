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
 * The <code>GRANT</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class GrantImpl
extends
    AbstractDDLQuery
implements
    QOM.Grant,
    GrantOnStep,
    GrantToStep,
    GrantWithGrantOptionStep,
    GrantFinalStep
{

    final QueryPartListView<? extends Privilege> privileges;
          Table<?>                               on;
          Role                                   to;
          boolean                                toPublic;
          boolean                                withGrantOption;

    GrantImpl(
        Configuration configuration,
        Collection<? extends Privilege> privileges
    ) {
        this(
            configuration,
            privileges,
            null,
            null,
            false,
            false
        );
    }

    GrantImpl(
        Configuration configuration,
        Collection<? extends Privilege> privileges,
        Table<?> on,
        Role to,
        boolean toPublic,
        boolean withGrantOption
    ) {
        super(configuration);

        this.privileges = new QueryPartList<>(privileges);
        this.on = on;
        this.to = to;
        this.toPublic = toPublic;
        this.withGrantOption = withGrantOption;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final GrantImpl on(String on) {
        return on(DSL.table(DSL.name(on)));
    }

    @Override
    public final GrantImpl on(Name on) {
        return on(DSL.table(on));
    }

    @Override
    public final GrantImpl on(Table<?> on) {
        this.on = on;
        return this;
    }

    @Override
    public final GrantImpl to(User to) {
        return to(DSL.role(to.getQualifiedName()));
    }

    @Override
    public final GrantImpl to(Role to) {
        this.to = to;
        return this;
    }

    @Override
    public final GrantImpl toPublic() {
        this.toPublic = true;
        return this;
    }

    @Override
    public final GrantImpl withGrantOption() {
        this.withGrantOption = true;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[] CLAUSE = { Clause.GRANT };

    @Override
    public final void accept(Context<?> ctx) {
        ctx.start(Clause.GRANT_PRIVILEGE)
           .visit(K_GRANT).sql(' ')
           .visit(QueryPartCollectionView.wrap(privileges))
           .end(Clause.GRANT_PRIVILEGE).sql(' ')
           .start(Clause.GRANT_ON)
           .visit(K_ON).sql(' ')
           .visit(on)
           .end(Clause.GRANT_ON).sql(' ')
           .start(Clause.GRANT_TO)
           .visit(K_TO).sql(' ');

        if (to != null)
            ctx.visit(to);
        else if (toPublic)
            ctx.visit(K_PUBLIC);

        if (withGrantOption)
            ctx.sql(' ')
               .visit(K_WITH_GRANT_OPTION);

        ctx.end(Clause.GRANT_TO);
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
    public final Table<?> $on() {
        return on;
    }

    @Override
    public final Role $to() {
        return to;
    }

    @Override
    public final boolean $toPublic() {
        return toPublic;
    }

    @Override
    public final boolean $withGrantOption() {
        return withGrantOption;
    }

    @Override
    public final QOM.Grant $privileges(UnmodifiableList<? extends Privilege> newValue) {
        return constructor().apply(newValue, $on(), $to(), $toPublic(), $withGrantOption());
    }

    @Override
    public final QOM.Grant $on(Table<?> newValue) {
        return constructor().apply($privileges(), newValue, $to(), $toPublic(), $withGrantOption());
    }

    @Override
    public final QOM.Grant $to(Role newValue) {
        return constructor().apply($privileges(), $on(), newValue, $toPublic(), $withGrantOption());
    }

    @Override
    public final QOM.Grant $toPublic(boolean newValue) {
        return constructor().apply($privileges(), $on(), $to(), newValue, $withGrantOption());
    }

    @Override
    public final QOM.Grant $withGrantOption(boolean newValue) {
        return constructor().apply($privileges(), $on(), $to(), $toPublic(), newValue);
    }

    public final Function5<? super UnmodifiableList<? extends Privilege>, ? super Table<?>, ? super Role, ? super Boolean, ? super Boolean, ? extends QOM.Grant> constructor() {
        return (a1, a2, a3, a4, a5) -> new GrantImpl(configuration(), (Collection<? extends Privilege>) a1, a2, a3, a4, a5);
    }



























}
