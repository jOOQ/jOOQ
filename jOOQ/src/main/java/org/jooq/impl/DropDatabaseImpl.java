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
import java.util.Set;



/**
 * The <code>DROP DATABASE</code> statement.
 */
@SuppressWarnings({ "unused" })
final class DropDatabaseImpl
extends
    AbstractDDLQuery
implements
    QOM.DropDatabase,
    DropDatabaseFinalStep
{

    final Catalog database;
    final boolean ifExists;

    DropDatabaseImpl(
        Configuration configuration,
        Catalog database,
        boolean ifExists
    ) {
        super(configuration);

        this.database = database;
        this.ifExists = ifExists;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supportedUntil(DERBY, FIREBIRD);

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.DROP_DATABASE, c -> accept0(c));
        else
            accept0(ctx);
    }

    private void accept0(Context<?> ctx) {
        ctx.visit(K_DROP).sql(' ');

        switch (ctx.family()) {






            default:
                ctx.visit(K_DATABASE);
                break;
        }

        if (ifExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(database);





    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Catalog $database() {
        return database;
    }

    @Override
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final QOM.DropDatabase $database(Catalog newValue) {
        return $constructor().apply(newValue, $ifExists());
    }

    @Override
    public final QOM.DropDatabase $ifExists(boolean newValue) {
        return $constructor().apply($database(), newValue);
    }

    public final Function2<? super Catalog, ? super Boolean, ? extends QOM.DropDatabase> $constructor() {
        return (a1, a2) -> new DropDatabaseImpl(configuration(), a1, a2);
    }






















}
