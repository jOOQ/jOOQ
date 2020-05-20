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
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.Keywords.K_ALTER;
import static org.jooq.impl.Keywords.K_DATABASE;
import static org.jooq.impl.Keywords.K_IF_EXISTS;
import static org.jooq.impl.Keywords.K_RENAME;
import static org.jooq.impl.Keywords.K_RENAME_TO;
import static org.jooq.impl.Keywords.K_TO;

import java.util.Set;

import org.jooq.AlterDatabaseFinalStep;
import org.jooq.AlterDatabaseStep;
import org.jooq.Catalog;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Name;
// ...
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class AlterDatabaseImpl extends AbstractRowCountQuery implements

    // Cascading interface implementations for ALTER DATABASE behaviour
    AlterDatabaseStep,
    AlterDatabaseFinalStep {

    /**
     * Generated UID
     */
    private static final long            serialVersionUID      = 8904572826501186329L;







    private final Catalog                database;
    private final boolean                ifExists;
    private Catalog                      renameTo;

    AlterDatabaseImpl(Configuration configuration, Catalog database) {
        this(configuration, database, false);
    }

    AlterDatabaseImpl(Configuration configuration, Catalog database, boolean ifExists) {
        super(configuration);

        this.database = database;
        this.ifExists = ifExists;
    }

    final Catalog $database() { return database; }
    final boolean $ifExists() { return ifExists; }
    final Catalog $renameTo() { return renameTo; }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final AlterDatabaseImpl renameTo(Catalog newName) {
        this.renameTo = newName;
        return this;
    }

    @Override
    public final AlterDatabaseImpl renameTo(Name newName) {
        return renameTo(DSL.catalog(newName));
    }

    @Override
    public final AlterDatabaseImpl renameTo(String newName) {
        return renameTo(name(newName));
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------








    @Override
    public final void accept(Context<?> ctx) {








            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        boolean supportRename = false;

        if (supportRename)
            ctx.visit(K_RENAME);
        else
            ctx.visit(K_ALTER);

        ctx.sql(' ').visit(K_DATABASE);

        if (ifExists)



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
