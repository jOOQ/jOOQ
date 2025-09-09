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
import org.jooq.impl.QOM.Cascade;
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
 * The <code>DROP INDEX</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class DropIndexImpl
extends
    AbstractDDLQuery
implements
    QOM.DropIndex,
    DropIndexOnStep,
    DropIndexCascadeStep,
    DropIndexFinalStep
{

    final Index    index;
    final boolean  ifExists;
          Table<?> on;
          Cascade  cascade;

    DropIndexImpl(
        Configuration configuration,
        Index index,
        boolean ifExists
    ) {
        this(
            configuration,
            index,
            ifExists,
            null,
            null
        );
    }

    DropIndexImpl(
        Configuration configuration,
        Index index,
        boolean ifExists,
        Table<?> on,
        Cascade cascade
    ) {
        super(configuration);

        this.index = index;
        this.ifExists = ifExists;
        this.on = on;
        this.cascade = cascade;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final DropIndexImpl on(String on) {
        return on(DSL.table(DSL.name(on)));
    }

    @Override
    public final DropIndexImpl on(Name on) {
        return on(DSL.table(on));
    }

    @Override
    public final DropIndexImpl on(Table<?> on) {
        this.on = on;
        return this;
    }

    @Override
    public final DropIndexImpl cascade() {
        this.cascade = Cascade.CASCADE;
        return this;
    }

    @Override
    public final DropIndexImpl restrict() {
        this.cascade = Cascade.RESTRICT;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]        CLAUSES              = { Clause.DROP_INDEX };
    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD, MYSQL);
    private static final Set<SQLDialect> REQUIRES_ON          = SQLDialect.supportedBy(CLICKHOUSE, MARIADB, MYSQL);

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.DROP_INDEX, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        switch (ctx.family()) {
            case CLICKHOUSE:
                acceptClickhouse(ctx);
                break;
            default:
                acceptDefault(ctx);
                break;
        }
    }

    private final void acceptDefault(Context<?> ctx) {
        ctx.visit(K_DROP).sql(' ').visit(K_INDEX).sql(' ');

        if (ifExists && supportsIfExists(ctx))
            ctx.visit(K_IF_EXISTS).sql(' ');






        ctx.visit(index);

        if (REQUIRES_ON.contains(ctx.dialect()))
            if (on != null)
                ctx.sql(' ').visit(K_ON).sql(' ').visit(on);
            else if (index.getTable() != null)
                ctx.sql(' ').visit(K_ON).sql(' ').visit(index.getTable());

        acceptCascade(ctx, cascade);
    }

    private final void acceptClickhouse(Context<?> ctx) {
        ctx.visit(K_ALTER_TABLE)
           .sql(' ')
           .visit(on != null ? on : index.getTable())
           .sql(' ')
           .visit(K_DROP).sql(' ')
           .visit(K_INDEX).sql(' ');

        if (ifExists && supportsIfExists(ctx))
            ctx.visit(K_IF_EXISTS).sql(' ');

        if (index != null)
            ctx.qualify(false, c -> c.visit(index))
               .sql(' ');
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Index $index() {
        return index;
    }

    @Override
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final Table<?> $on() {
        return on;
    }

    @Override
    public final Cascade $cascade() {
        return cascade;
    }

    @Override
    public final QOM.DropIndex $index(Index newValue) {
        return $constructor().apply(newValue, $ifExists(), $on(), $cascade());
    }

    @Override
    public final QOM.DropIndex $ifExists(boolean newValue) {
        return $constructor().apply($index(), newValue, $on(), $cascade());
    }

    @Override
    public final QOM.DropIndex $on(Table<?> newValue) {
        return $constructor().apply($index(), $ifExists(), newValue, $cascade());
    }

    @Override
    public final QOM.DropIndex $cascade(Cascade newValue) {
        return $constructor().apply($index(), $ifExists(), $on(), newValue);
    }

    public final Function4<? super Index, ? super Boolean, ? super Table<?>, ? super Cascade, ? extends QOM.DropIndex> $constructor() {
        return (a1, a2, a3, a4) -> new DropIndexImpl(configuration(), a1, a2, a3, a4);
    }

























}
