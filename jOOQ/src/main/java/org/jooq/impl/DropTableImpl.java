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

import static org.jooq.Clause.DROP_TABLE;
import static org.jooq.Clause.DROP_TABLE_TABLE;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
// ...
import static org.jooq.impl.Cascade.CASCADE;
import static org.jooq.impl.Cascade.RESTRICT;
import static org.jooq.impl.Keywords.K_CASCADE;
import static org.jooq.impl.Keywords.K_DROP;
import static org.jooq.impl.Keywords.K_DROP_TABLE;
import static org.jooq.impl.Keywords.K_IF_EXISTS;
import static org.jooq.impl.Keywords.K_RESTRICT;
import static org.jooq.impl.Keywords.K_TABLE;
import static org.jooq.impl.Keywords.K_TEMPORARY;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DropTableStep;
import org.jooq.SQLDialect;
import org.jooq.Table;


/**
 * @author Lukas Eder
 */
final class DropTableImpl extends AbstractRowCountQuery implements

    // Cascading interface implementations for DROP TABLE behaviour
    DropTableStep {

    /**
     * Generated UID
     */
    private static final long            serialVersionUID     = 8904572826501186329L;
    private static final Clause[]        CLAUSES              = { DROP_TABLE };
    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supported(DERBY, FIREBIRD);
    private static final Set<SQLDialect> TEMPORARY_SEMANTIC   = SQLDialect.supported(MYSQL);

    private final Table<?>               table;
    private final boolean                temporary;
    private final boolean                ifExists;
    private Cascade                      cascade;

    DropTableImpl(Configuration configuration, Table<?> table) {
        this(configuration, table, false, false);
    }

    DropTableImpl(Configuration configuration, Table<?> table, boolean ifExists) {
        this(configuration, table, ifExists, false);
    }

    DropTableImpl(Configuration configuration, Table<?> table, boolean ifExists, boolean temporary) {
        super(configuration);

        this.table = table;
        this.ifExists = ifExists;
        this.temporary = temporary;
    }

    final Table<?> $table()    { return table; }
    final boolean  $ifExists() { return ifExists; }
    final Cascade  $cascade()  { return cascade; }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final DropTableImpl cascade() {
        cascade = CASCADE;
        return this;
    }

    @Override
    public final DropTableImpl restrict() {
        cascade = RESTRICT;
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
            Tools.beginTryCatch(ctx, DDLStatementType.DROP_TABLE);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.DROP_TABLE);
        }
        else {
            accept0(ctx);
        }
    }

    private void accept0(Context<?> ctx) {
        ctx.start(DROP_TABLE_TABLE);

        // [#6371] [#9019] While many dialects do not require this keyword, in
        //                 some dialects (e.g. MySQL), there is a semantic
        //                 difference, e.g. with respect to transactions.
        if (temporary && TEMPORARY_SEMANTIC.contains(ctx.family()))
            ctx.visit(K_DROP).sql(' ').visit(K_TEMPORARY).sql(' ').visit(K_TABLE).sql(' ');
        else
            ctx.visit(K_DROP_TABLE).sql(' ');


        if (ifExists && supportsIfExists(ctx))
            ctx.visit(K_IF_EXISTS).sql(' ');

        ctx.visit(table);

        if (cascade == CASCADE)
            ctx.sql(' ').visit(K_CASCADE);
        else if (cascade == RESTRICT)
            ctx.sql(' ').visit(K_RESTRICT);

        ctx.end(DROP_TABLE_TABLE);
    }


    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
