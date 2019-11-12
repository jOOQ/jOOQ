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

import static org.jooq.Clause.DROP_INDEX;
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
// ...
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Keywords.K_CASCADE;
import static org.jooq.impl.Keywords.K_DROP_INDEX;
import static org.jooq.impl.Keywords.K_IF_EXISTS;
import static org.jooq.impl.Keywords.K_ON;
import static org.jooq.impl.Keywords.K_RESTRICT;
import static org.jooq.impl.Tools.beginTryCatch;
import static org.jooq.impl.Tools.endTryCatch;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DropIndexOnStep;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class DropIndexImpl extends AbstractRowCountQuery implements

    // Cascading interface implementations for DROP INDEX behaviour
    DropIndexOnStep {

    /**
     * Generated UID
     */
    private static final long            serialVersionUID     = 8904572826501186329L;
    private static final Clause[]        CLAUSES              = { DROP_INDEX };
    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supported(CUBRID, DERBY, FIREBIRD);
    private static final Set<SQLDialect> REQUIRES_ON          = SQLDialect.supported(MARIADB, MYSQL);

    private final Index                  index;
    private final boolean                ifExists;
    private Table<?>                     on;
    private Boolean                      cascade;

    DropIndexImpl(Configuration configuration, Index index) {
        this(configuration, index, false);
    }

    DropIndexImpl(Configuration configuration, Index index, boolean ifExists) {
        super(configuration);

        this.index = index;
        this.ifExists = ifExists;
        this.on = index.getTable();
    }

    final Index    $index()    { return index; }
    final boolean  $ifExists() { return ifExists; }
    final Table<?> $on()       { return on; }

    // ------------------------------------------------------------------------
    // XXX: DropIndex API
    // ------------------------------------------------------------------------

    @Override
    public final DropIndexImpl on(Table<?> table) {
        this.on = table;
        return this;
    }

    @Override
    public final DropIndexImpl on(String tableName) {
        return on(name(tableName));
    }

    @Override
    public final DropIndexImpl on(Name tableName) {
        return on(table(tableName));
    }

    @Override
    public final DropIndexImpl cascade() {
        cascade = true;
        return this;
    }

    @Override
    public final DropIndexImpl restrict() {
        cascade = false;
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx)) {
            beginTryCatch(ctx, DDLStatementType.DROP_INDEX);
            accept0(ctx);
            endTryCatch(ctx, DDLStatementType.DROP_INDEX);
        }
        else {
            accept0(ctx);
        }
    }

    private void accept0(Context<?> ctx) {
        ctx.visit(K_DROP_INDEX).sql(' ');

        if (ifExists && supportsIfExists(ctx))
            ctx.visit(K_IF_EXISTS).sql(' ');






        ctx.visit(index);

        if (on != null && REQUIRES_ON.contains(ctx.family()))
            ctx.sql(' ').visit(K_ON).sql(' ').visit(on);

        if (cascade != null)
            ctx.visit(cascade ? K_CASCADE : K_RESTRICT);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
