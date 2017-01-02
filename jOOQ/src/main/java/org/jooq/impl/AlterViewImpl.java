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
import static org.jooq.Clause.ALTER_VIEW;
import static org.jooq.Clause.ALTER_VIEW_RENAME;
import static org.jooq.Clause.ALTER_VIEW_VIEW;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.impl.DSL.name;

import org.jooq.AlterViewFinalStep;
import org.jooq.AlterViewStep;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class AlterViewImpl extends AbstractQuery implements

    // Cascading interface implementations for ALTER VIEW behaviour
    AlterViewStep,
    AlterViewFinalStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { ALTER_VIEW };

    private final Table<?>        view;
    private final boolean         ifExists;
    private Table<?>              renameTo;

    AlterViewImpl(Configuration configuration, Table<?> view) {
        this(configuration, view, false);
    }

    AlterViewImpl(Configuration configuration, Table<?> view, boolean ifExists) {
        super(configuration);

        this.view = view;
        this.ifExists = ifExists;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final AlterViewImpl renameTo(Table<?> newName) {
        this.renameTo = newName;
        return this;
    }

    @Override
    public final AlterViewImpl renameTo(Name newName) {
        return renameTo(DSL.table(newName));
    }

    @Override
    public final AlterViewImpl renameTo(String newName) {
        return renameTo(name(newName));
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
            Tools.executeImmediateIfExistsBegin(ctx, DDLStatementType.ALTER_VIEW, view);
            accept0(ctx);
            Tools.executeImmediateIfExistsEnd(ctx, DDLStatementType.ALTER_VIEW, view);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        switch (ctx.family()) {










            default:
                accept1(ctx);
                break;
        }

    }





















    private final void accept1(Context<?> ctx) {
        ctx.start(ALTER_VIEW_VIEW)
           .keyword("alter").sql(' ')
           .keyword(ctx.family() == HSQLDB ? "table" : "view");

        if (ifExists && supportsIfExists(ctx))
            ctx.sql(' ').keyword("if exists");

        ctx.sql(' ').visit(view)
           .end(ALTER_VIEW_VIEW)
           .formatIndentStart()
           .formatSeparator();

        if (renameTo != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_VIEW_RENAME)
               .qualify(false)
               .keyword("rename to").sql(' ').visit(renameTo)
               .qualify(qualify)
               .end(ALTER_VIEW_RENAME);
        }

        ctx.formatIndentEnd();
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
