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
 * The <code>ALTER VIEW</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class AlterViewImpl
extends
    AbstractDDLQuery
implements
    AlterViewStep,
    AlterViewFinalStep
{

    private final Table<?> view;
    private final boolean  alterViewIfExists;
    private       Comment  comment;
    private       Table<?> renameTo;

    AlterViewImpl(
        Configuration configuration,
        Table<?> view,
        boolean alterViewIfExists
    ) {
        this(
            configuration,
            view,
            alterViewIfExists,
            null,
            null
        );
    }

    AlterViewImpl(
        Configuration configuration,
        Table<?> view,
        boolean alterViewIfExists,
        Comment comment,
        Table<?> renameTo
    ) {
        super(configuration);

        this.view = view;
        this.alterViewIfExists = alterViewIfExists;
        this.comment = comment;
        this.renameTo = renameTo;
    }

    final Table<?> $view()              { return view; }
    final boolean  $alterViewIfExists() { return alterViewIfExists; }
    final Comment  $comment()           { return comment; }
    final Table<?> $renameTo()          { return renameTo; }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final AlterViewImpl comment(String comment) {
        return comment(DSL.comment(comment));
    }

    @Override
    public final AlterViewImpl comment(Comment comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public final AlterViewImpl renameTo(String renameTo) {
        return renameTo(DSL.table(DSL.name(renameTo)));
    }

    @Override
    public final AlterViewImpl renameTo(Name renameTo) {
        return renameTo(DSL.table(renameTo));
    }

    @Override
    public final AlterViewImpl renameTo(Table<?> renameTo) {
        this.renameTo = renameTo;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]        CLAUSES           = { Clause.ALTER_VIEW };
    private static final Set<SQLDialect> SUPPORT_IF_EXISTS = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD);

    private final boolean supportsIfExists(Context<?> ctx) {
        return !SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (alterViewIfExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.ALTER_VIEW, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        if (comment != null) {
            ctx.visit(commentOnView(view).is(comment));
            return;
        }




















        accept1(ctx);
    }

















































    private final void accept1(Context<?> ctx) {
        ctx.start(Clause.ALTER_VIEW_VIEW)
           .visit(K_ALTER).sql(' ')
           .visit(ctx.family() == HSQLDB ? K_TABLE : K_VIEW);

        if (alterViewIfExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(view)
           .end(Clause.ALTER_VIEW_VIEW)
           .formatIndentStart()
           .formatSeparator();

        if (renameTo != null)
            ctx.start(Clause.ALTER_VIEW_RENAME)
               .visit(K_RENAME_TO).sql(' ')
               .qualify(false, c -> c.visit(renameTo))
               .end(Clause.ALTER_VIEW_RENAME);

        ctx.formatIndentEnd();
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }


}
