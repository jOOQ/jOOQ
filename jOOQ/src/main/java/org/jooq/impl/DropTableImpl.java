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
 * The <code>DROP TABLE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class DropTableImpl
extends
    AbstractDDLQuery
implements
    QOM.DropTable,
    DropTableStep,
    DropTableFinalStep
{

    final boolean  temporary;
    final Table<?> table;
    final boolean  ifExists;
          Cascade  cascade;

    DropTableImpl(
        Configuration configuration,
        boolean temporary,
        Table<?> table,
        boolean ifExists
    ) {
        this(
            configuration,
            temporary,
            table,
            ifExists,
            null
        );
    }

    DropTableImpl(
        Configuration configuration,
        boolean temporary,
        Table<?> table,
        boolean ifExists,
        Cascade cascade
    ) {
        super(configuration);

        this.temporary = temporary;
        this.table = table;
        this.ifExists = ifExists;
        this.cascade = cascade;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final DropTableImpl cascade() {
        this.cascade = Cascade.CASCADE;
        return this;
    }

    @Override
    public final DropTableImpl restrict() {
        this.cascade = Cascade.RESTRICT;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]        CLAUSES              = { Clause.DROP_TABLE };
    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supportedUntil(DERBY, FIREBIRD);
    private static final Set<SQLDialect> TEMPORARY_SEMANTIC   = SQLDialect.supportedBy(MARIADB, MYSQL);

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.DROP_TABLE, c -> accept0(c));
        else
            accept0(ctx);
    }

    private void accept0(Context<?> ctx) {
        ctx.start(Clause.DROP_TABLE_TABLE);

        ctx.visit(K_DROP).sql(' ');

        // [#6371] [#9019] While many dialects do not require this keyword, in
        //                 some dialects (e.g. MySQL), there is a semantic
        //                 difference, e.g. with respect to transactions.
        if (temporary && TEMPORARY_SEMANTIC.contains(ctx.dialect()))
            ctx.visit(K_TEMPORARY).sql(' ');

        ctx.visit(K_TABLE).sql(' ');

        if (ifExists && supportsIfExists(ctx))
            ctx.visit(K_IF_EXISTS).sql(' ');

        ctx.visit(table);
        acceptCascade(ctx);
        ctx.end(Clause.DROP_TABLE_TABLE);
    }

    private final void acceptCascade(Context<?> ctx) {
        switch (ctx.family()) {









            default:
                acceptCascade(ctx, cascade);
                break;
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final boolean $temporary() {
        return temporary;
    }

    @Override
    public final Table<?> $table() {
        return table;
    }

    @Override
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final Cascade $cascade() {
        return cascade;
    }

    @Override
    public final QOM.DropTable $temporary(boolean newValue) {
        return $constructor().apply(newValue, $table(), $ifExists(), $cascade());
    }

    @Override
    public final QOM.DropTable $table(Table<?> newValue) {
        return $constructor().apply($temporary(), newValue, $ifExists(), $cascade());
    }

    @Override
    public final QOM.DropTable $ifExists(boolean newValue) {
        return $constructor().apply($temporary(), $table(), newValue, $cascade());
    }

    @Override
    public final QOM.DropTable $cascade(Cascade newValue) {
        return $constructor().apply($temporary(), $table(), $ifExists(), newValue);
    }

    public final Function4<? super Boolean, ? super Table<?>, ? super Boolean, ? super Cascade, ? extends QOM.DropTable> $constructor() {
        return (a1, a2, a3, a4) -> new DropTableImpl(configuration(), a1, a2, a3, a4);
    }
























}
