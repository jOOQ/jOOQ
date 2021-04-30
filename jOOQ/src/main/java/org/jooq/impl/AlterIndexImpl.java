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
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>ALTER INDEX</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class AlterIndexImpl
extends
    AbstractDDLQuery
implements
    AlterIndexOnStep,
    AlterIndexStep,
    AlterIndexFinalStep
{

    private final Index    index;
    private final boolean  alterIndexIfExists;
    private       Table<?> on;
    private       Index    renameTo;

    AlterIndexImpl(
        Configuration configuration,
        Index index,
        boolean alterIndexIfExists
    ) {
        this(
            configuration,
            index,
            alterIndexIfExists,
            null,
            null
        );
    }

    AlterIndexImpl(
        Configuration configuration,
        Index index,
        boolean alterIndexIfExists,
        Table<?> on,
        Index renameTo
    ) {
        super(configuration);

        this.index = index;
        this.alterIndexIfExists = alterIndexIfExists;
        this.on = on;
        this.renameTo = renameTo;
    }

    final Index    $index()              { return index; }
    final boolean  $alterIndexIfExists() { return alterIndexIfExists; }
    final Table<?> $on()                 { return on; }
    final Index    $renameTo()           { return renameTo; }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final AlterIndexImpl on(String on) {
        return on(DSL.table(DSL.name(on)));
    }

    @Override
    public final AlterIndexImpl on(Name on) {
        return on(DSL.table(on));
    }

    @Override
    public final AlterIndexImpl on(Table<?> on) {
        this.on = on;
        return this;
    }

    @Override
    public final AlterIndexImpl renameTo(String renameTo) {
        return renameTo(DSL.index(DSL.name(renameTo)));
    }

    @Override
    public final AlterIndexImpl renameTo(Name renameTo) {
        return renameTo(DSL.index(renameTo));
    }

    @Override
    public final AlterIndexImpl renameTo(Index renameTo) {
        this.renameTo = renameTo;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]            CLAUSES              = { Clause.ALTER_INDEX };
    private static final Set<SQLDialect>     NO_SUPPORT_IF_EXISTS = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD);
    private static final Set<SQLDialect>     SUPPORT_RENAME_INDEX = SQLDialect.supportedBy(DERBY);

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (alterIndexIfExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.ALTER_INDEX, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        boolean renameIndex = SUPPORT_RENAME_INDEX.contains(ctx.dialect());

        switch (ctx.family()) {


            case MARIADB:
            case MYSQL: {
                ctx.visit(K_ALTER_TABLE).sql(' ')
                   .visit(on).sql(' ')
                   .visit(K_RENAME_INDEX).sql(' ')
                   .qualify(false, c -> c.visit(index)).sql(' ')
                   .visit(K_TO).sql(' ')
                   .qualify(false, c -> c.visit(renameTo));

                break;
            }




























            default: {
                ctx.start(Clause.ALTER_INDEX_INDEX)
                   .visit(renameIndex ? K_RENAME_INDEX : K_ALTER_INDEX);

                if (alterIndexIfExists && supportsIfExists(ctx))
                    ctx.sql(' ').visit(K_IF_EXISTS);

                ctx.sql(' ');

                if (on != null)
                    ctx.visit(on).sql('.').visit(index.getUnqualifiedName());
                else
                    ctx.visit(index);

                ctx.end(Clause.ALTER_INDEX_INDEX)
                   .formatIndentStart()
                   .formatSeparator();

                if (renameTo != null)
                    ctx.start(Clause.ALTER_INDEX_RENAME)
                       .visit(renameIndex ? K_TO : K_RENAME_TO).sql(' ')
                       .qualify(false, c -> c.visit(renameTo))
                       .end(Clause.ALTER_INDEX_RENAME);

                ctx.formatIndentEnd();
                break;
            }
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }


}
