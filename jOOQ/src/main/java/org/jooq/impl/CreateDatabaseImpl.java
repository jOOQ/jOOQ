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
import org.jooq.Traverser;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>CREATE DATABASE</code> statement.
 */
@SuppressWarnings({ "unused" })
final class CreateDatabaseImpl
extends
    AbstractDDLQuery
implements
    QOM.CreateDatabase,
    CreateDatabaseFinalStep
{

    final Catalog database;
    final boolean ifNotExists;

    CreateDatabaseImpl(
        Configuration configuration,
        Catalog database,
        boolean ifNotExists
    ) {
        super(configuration);

        this.database = database;
        this.ifNotExists = ifNotExists;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS = SQLDialect.supportedBy(DERBY, FIREBIRD, POSTGRES, YUGABYTE);

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx))
            tryCatch(ctx, DDLStatementType.CREATE_DATABASE, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        ctx.visit(K_CREATE).sql(' ').visit(K_DATABASE);

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.sql(' ').visit(K_IF_NOT_EXISTS);

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
    public final boolean $ifNotExists() {
        return ifNotExists;
    }

    @Override
    public final QOM.CreateDatabase $database(Catalog newValue) {
        return constructor().apply(newValue, $ifNotExists());
    }

    @Override
    public final QOM.CreateDatabase $ifNotExists(boolean newValue) {
        return constructor().apply($database(), newValue);
    }

    public final Function2<? super Catalog, ? super Boolean, ? extends QOM.CreateDatabase> constructor() {
        return (a1, a2) -> new CreateDatabaseImpl(configuration(), a1, a2);
    }

    @Override
    public final QueryPart $replace(
        Predicate<? super QueryPart> recurse,
        Function1<? super QueryPart, ? extends QueryPart> replacement
    ) {
        return QOM.replace(
            this,
            $database(),
            $ifNotExists(),
            constructor()::apply,
            recurse,
            replacement
        );
    }

    @Override
    public final <R> R $traverse(Traverser<?, R> traverser) {
        return QOM.traverse(traverser, this,
            $database()
        );
    }
}
