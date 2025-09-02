/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>DROP VIEW</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class DropViewImpl
extends
    AbstractDDLQuery
implements
    QOM.DropView,
    DropViewFinalStep
{

    final Table<?> view;
    final boolean  materialized;
    final boolean  ifExists;

    DropViewImpl(
        Configuration configuration,
        Table<?> view,
        boolean materialized,
        boolean ifExists
    ) {
        super(configuration);

        this.view = view;
        this.materialized = materialized;
        this.ifExists = ifExists;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]        CLAUSES              = { Clause.DROP_VIEW };
    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supportedUntil(DERBY, FIREBIRD);

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.DROP_VIEW, c -> accept0(c));
        else
            accept0(ctx);
    }

    private void accept0(Context<?> ctx) {
        ctx.start(Clause.DROP_VIEW_TABLE)
           .visit(K_DROP).sql(' ');

        if (materialized) {
            switch (ctx.family()) {










                default:
                    ctx.visit(K_MATERIALIZED).sql(' ').visit(K_VIEW).sql(' ');
                    break;
            }
        }
        else
            ctx.visit(K_VIEW).sql(' ');

        if (ifExists && supportsIfExists(ctx))
            ctx.visit(K_IF_EXISTS).sql(' ');






        ctx.visit(view);

        ctx.end(Clause.DROP_VIEW_TABLE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<?> $view() {
        return view;
    }

    @Override
    public final boolean $materialized() {
        return materialized;
    }

    @Override
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final QOM.DropView $view(Table<?> newValue) {
        return $constructor().apply(newValue, $materialized(), $ifExists());
    }

    @Override
    public final QOM.DropView $materialized(boolean newValue) {
        return $constructor().apply($view(), newValue, $ifExists());
    }

    @Override
    public final QOM.DropView $ifExists(boolean newValue) {
        return $constructor().apply($view(), $materialized(), newValue);
    }

    public final Function3<? super Table<?>, ? super Boolean, ? super Boolean, ? extends QOM.DropView> $constructor() {
        return (a1, a2, a3) -> new DropViewImpl(configuration(), a1, a2, a3);
    }























}
