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

// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.impl.Keywords.K_CREATE;
import static org.jooq.impl.Keywords.K_DATABASE;
import static org.jooq.impl.Keywords.K_IF_NOT_EXISTS;

import java.util.Set;

import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateDatabaseFinalStep;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class CreateDatabaseImpl extends AbstractRowCountQuery implements

    // Cascading interface implementations for CREATE DATABASE behaviour
    CreateDatabaseFinalStep {


    /**
     * Generated UID
     */
    private static final long            serialVersionUID           = 8904572826501186329L;
    private static final Set<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS   = SQLDialect.supportedBy(DERBY, FIREBIRD, POSTGRES);

    private final Catalog                database;
    private final boolean                ifNotExists;

    CreateDatabaseImpl(Configuration configuration, Catalog database, boolean ifNotExists) {
        super(configuration);

        this.database = database;
        this.ifNotExists = ifNotExists;
    }

    final Catalog $database()    { return database; }
    final boolean $ifNotExists() { return ifNotExists; }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.beginTryCatch(ctx, DDLStatementType.CREATE_DATABASE);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.CREATE_DATABASE);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        ctx.visit(K_CREATE).sql(' ').visit(K_DATABASE);

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.sql(' ').visit(K_IF_NOT_EXISTS);

        ctx.sql(' ').visit(database);
    }
}
