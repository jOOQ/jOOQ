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
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.Clause.DROP_INDEX;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DropIndexFinalStep;
import org.jooq.DropIndexOnStep;
import org.jooq.Name;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class DropIndexImpl extends AbstractQuery implements

    // Cascading interface implementations for DROP INDEX behaviour
    DropIndexOnStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { DROP_INDEX };

    private final Name    index;
    private final boolean ifExists;
    private Table<?>      on;

    DropIndexImpl(Configuration configuration, Name index) {
        this(configuration, index, false);
    }

    DropIndexImpl(Configuration configuration, Name index, boolean ifExists) {
        super(configuration);

        this.index = index;
        this.ifExists = ifExists;
    }

    // ------------------------------------------------------------------------
    // XXX: DropIndex API
    // ------------------------------------------------------------------------

    @Override
    public final DropIndexFinalStep on(Table<?> table) {
        this.on = table;
        return this;
    }

    @Override
    public final DropIndexFinalStep on(String tableName) {
        return on(name(tableName));
    }

    @Override
    public final DropIndexFinalStep on(Name tableName) {
        return on(table(tableName));
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfExists(Context<?> ctx) {
        return !asList(CUBRID, DERBY, FIREBIRD).contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx)) {
            Tools.executeImmediateIfExistsBegin(ctx, DDLStatementType.DROP_INDEX, index);
            accept0(ctx);
            Tools.executeImmediateIfExistsEnd(ctx, DDLStatementType.DROP_INDEX, index);
        }
        else {
            accept0(ctx);
        }
    }

    private void accept0(Context<?> ctx) {
        ctx.keyword("drop index").sql(' ');

        if (ifExists && supportsIfExists(ctx))
            ctx.keyword("if exists").sql(' ');

        ctx.visit(index);

        if (on != null)
            ctx.sql(' ').keyword("on").sql(' ').visit(on);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
