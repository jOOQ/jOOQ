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
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>ALTER DATABASE</code> statement.
 */
@SuppressWarnings({ "hiding", "unused" })
final class AlterDatabaseImpl
extends
    AbstractRowCountQuery
implements
    AlterDatabaseStep,
    AlterDatabaseFinalStep
{

    private static final long serialVersionUID = 1L;

    private final Catalog database;
    private final boolean alterDatabaseIfExists;
    private       Catalog renameTo;

    AlterDatabaseImpl(
        Configuration configuration,
        Catalog database,
        boolean alterDatabaseIfExists
    ) {
        this(
            configuration,
            database,
            alterDatabaseIfExists,
            null
        );
    }

    AlterDatabaseImpl(
        Configuration configuration,
        Catalog database,
        boolean alterDatabaseIfExists,
        Catalog renameTo
    ) {
        super(configuration);

        this.database = database;
        this.alterDatabaseIfExists = alterDatabaseIfExists;
        this.renameTo = renameTo;
    }

    final Catalog $database()              { return database; }
    final boolean $alterDatabaseIfExists() { return alterDatabaseIfExists; }
    final Catalog $renameTo()              { return renameTo; }

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



    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS    = SQLDialect.supportedBy(POSTGRES);





    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (alterDatabaseIfExists && !supportsIfExists(ctx)) {
            Tools.beginTryCatch(ctx, DDLStatementType.ALTER_DATABASE);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.ALTER_DATABASE);
        }
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

        if (alterDatabaseIfExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(database);

        if (renameTo != null) {
            boolean qualify = ctx.qualify();

            ctx.sql(' ')
               .qualify(false)
               .visit(supportRename ? K_TO : K_RENAME_TO).sql(' ').visit(renameTo)
               .qualify(qualify);
        }
    }


}
