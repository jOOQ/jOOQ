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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
 * The <code>ALTER DATABASE</code> statement.
 */
@SuppressWarnings({ "hiding", "unused" })
final class AlterDatabaseImpl
extends
    AbstractDDLQuery
implements
    QOM.AlterDatabase,
    AlterDatabaseStep,
    AlterDatabaseFinalStep
{

    final Catalog database;
    final boolean ifExists;
          Catalog renameTo;

    AlterDatabaseImpl(
        Configuration configuration,
        Catalog database,
        boolean ifExists
    ) {
        this(
            configuration,
            database,
            ifExists,
            null
        );
    }

    AlterDatabaseImpl(
        Configuration configuration,
        Catalog database,
        boolean ifExists,
        Catalog renameTo
    ) {
        super(configuration);

        this.database = database;
        this.ifExists = ifExists;
        this.renameTo = renameTo;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final AlterDatabaseImpl renameTo(String renameTo) {
        return renameTo(DSL.catalog(DSL.name(renameTo)));
    }

    @Override
    public final AlterDatabaseImpl renameTo(Name renameTo) {
        return renameTo(DSL.catalog(renameTo));
    }

    @Override
    public final AlterDatabaseImpl renameTo(Catalog renameTo) {
        this.renameTo = renameTo;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS    = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);





    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.ALTER_DATABASE, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        boolean supportRename = false;

        if (supportRename)
            ctx.visit(K_RENAME);
        else
            ctx.visit(K_ALTER);

        ctx.sql(' ').visit(K_DATABASE);

        if (ifExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(database);

        if (renameTo != null)
            ctx.sql(' ').visit(supportRename ? K_TO : K_RENAME_TO).sql(' ')
               .qualify(false, c -> c.visit(renameTo));
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
    public final Catalog $renameTo() {
        return renameTo;
    }

    @Override
    public final QOM.AlterDatabase $database(Catalog newValue) {
        return $constructor().apply(newValue, $ifExists(), $renameTo());
    }

    @Override
    public final QOM.AlterDatabase $ifExists(boolean newValue) {
        return $constructor().apply($database(), newValue, $renameTo());
    }

    @Override
    public final QOM.AlterDatabase $renameTo(Catalog newValue) {
        return $constructor().apply($database(), $ifExists(), newValue);
    }

    public final Function3<? super Catalog, ? super Boolean, ? super Catalog, ? extends QOM.AlterDatabase> $constructor() {
        return (a1, a2, a3) -> new AlterDatabaseImpl(configuration(), a1, a2, a3);
    }
























}
