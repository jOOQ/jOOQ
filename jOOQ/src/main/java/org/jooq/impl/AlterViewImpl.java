/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>ALTER VIEW</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class AlterViewImpl
extends
    AbstractDDLQuery
implements
    QOM.AlterView,
    AlterViewStep,
    AlterViewFinalStep
{

    final Table<?>                              view;
    final QueryPartListView<? extends Field<?>> fields;
    final boolean                               materialized;
    final boolean                               ifExists;
          Comment                               comment;
          Table<?>                              renameTo;
          Select<?>                             as;

    AlterViewImpl(
        Configuration configuration,
        Table<?> view,
        Collection<? extends Field<?>> fields,
        boolean materialized,
        boolean ifExists
    ) {
        this(
            configuration,
            view,
            fields,
            materialized,
            ifExists,
            null,
            null,
            null
        );
    }

    AlterViewImpl(
        Configuration configuration,
        Table<?> view,
        boolean materialized,
        boolean ifExists
    ) {
        this(
            configuration,
            view,
            null,
            materialized,
            ifExists
        );
    }

    AlterViewImpl(
        Configuration configuration,
        Table<?> view,
        Collection<? extends Field<?>> fields,
        boolean materialized,
        boolean ifExists,
        Comment comment,
        Table<?> renameTo,
        Select<?> as
    ) {
        super(configuration);

        this.view = view;
        this.fields = new QueryPartList<>(fields);
        this.materialized = materialized;
        this.ifExists = ifExists;
        this.comment = comment;
        this.renameTo = renameTo;
        this.as = as;
    }

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

    @Override
    public final AlterViewImpl as(Select<?> as) {
        this.as = as;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]        CLAUSES                     = { Clause.ALTER_VIEW };
    private static final Set<SQLDialect> NO_SUPPORT_RENAME_IF_EXISTS = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD);
    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS        = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD);
    private static final Set<SQLDialect> SUPPORT_ALTER_TABLE_RENAME  = SQLDialect.supportedBy(HSQLDB, YUGABYTEDB);

    private final boolean supportsIfExists(Context<?> ctx) {
        if (renameTo != null)
            return !NO_SUPPORT_RENAME_IF_EXISTS.contains(ctx.dialect());
        else
            return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.ALTER_VIEW, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        if (as != null) {
            switch (ctx.family()) {



















                case CUBRID:
                case DERBY:
                case FIREBIRD:
                case H2:
                case HSQLDB:
                case IGNITE:
                case MARIADB:
                case MYSQL:
                case POSTGRES:
                case SQLITE:
                case YUGABYTEDB:
                    if (materialized)
                        ctx.visit(begin(dropMaterializedView(view), createMaterializedView(view, fields.toArray(Tools.EMPTY_FIELD)).as(as)));
                    else
                        ctx.visit(begin(dropView(view), createView(view, fields.toArray(Tools.EMPTY_FIELD)).as(as)));

                    break;

                default:
                    ctx.visit(K_ALTER).sql(' ');

                    if (materialized)
                        ctx.visit(K_MATERIALIZED).sql(' ');

                    ctx.visit(K_VIEW).sql(' ').visit(view);

                    if (!fields.isEmpty())
                        ctx.sql(" (").visit(QueryPartCollectionView.wrap(fields).qualify(false)).sql(')');

                    ctx.formatSeparator().visit(K_AS).formatSeparator().visit(as);
                    break;
            }

            return;
        }

        if (comment != null) {
            ctx.visit((materialized ? commentOnMaterializedView(view) : commentOnView(view)).is(comment));
            return;
        }




















        accept1(ctx);
    }

















































    private final void accept1(Context<?> ctx) {
        ctx.start(Clause.ALTER_VIEW_VIEW)
           .visit(K_ALTER).sql(' ');

        if (SUPPORT_ALTER_TABLE_RENAME.contains(ctx.dialect()))
            ctx.visit(K_TABLE).sql(' ');
        else if (materialized)
            ctx.visit(K_MATERIALIZED).sql(' ').visit(K_VIEW).sql(' ');
        else
            ctx.visit(K_VIEW).sql(' ');

        if (ifExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(view).sql(' ')
           .end(Clause.ALTER_VIEW_VIEW);

        if (renameTo != null)
            ctx.start(Clause.ALTER_VIEW_RENAME)
               .visit(K_RENAME_TO).sql(' ')
               .qualify(false, c -> c.visit(renameTo))
               .end(Clause.ALTER_VIEW_RENAME);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<?> $view() {
        return view;
    }

    @Override
    public final QOM.UnmodifiableList<? extends Field<?>> $fields() {
        return QOM.unmodifiable(fields);
    }

    @Override
    public final boolean $materialized() {
        return materialized;
    }

    @Override
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final Comment $comment() {
        return comment;
    }

    @Override
    public final Table<?> $renameTo() {
        return renameTo;
    }

    @Override
    public final Select<?> $as() {
        return as;
    }

    @Override
    public final QOM.AlterView $view(Table<?> newValue) {
        return $constructor().apply(newValue, $fields(), $materialized(), $ifExists(), $comment(), $renameTo(), $as());
    }

    @Override
    public final QOM.AlterView $fields(Collection<? extends Field<?>> newValue) {
        return $constructor().apply($view(), newValue, $materialized(), $ifExists(), $comment(), $renameTo(), $as());
    }

    @Override
    public final QOM.AlterView $materialized(boolean newValue) {
        return $constructor().apply($view(), $fields(), newValue, $ifExists(), $comment(), $renameTo(), $as());
    }

    @Override
    public final QOM.AlterView $ifExists(boolean newValue) {
        return $constructor().apply($view(), $fields(), $materialized(), newValue, $comment(), $renameTo(), $as());
    }

    @Override
    public final QOM.AlterView $comment(Comment newValue) {
        return $constructor().apply($view(), $fields(), $materialized(), $ifExists(), newValue, $renameTo(), $as());
    }

    @Override
    public final QOM.AlterView $renameTo(Table<?> newValue) {
        return $constructor().apply($view(), $fields(), $materialized(), $ifExists(), $comment(), newValue, $as());
    }

    @Override
    public final QOM.AlterView $as(Select<?> newValue) {
        return $constructor().apply($view(), $fields(), $materialized(), $ifExists(), $comment(), $renameTo(), newValue);
    }

    public final Function7<? super Table<?>, ? super Collection<? extends Field<?>>, ? super Boolean, ? super Boolean, ? super Comment, ? super Table<?>, ? super Select<?>, ? extends QOM.AlterView> $constructor() {
        return (a1, a2, a3, a4, a5, a6, a7) -> new AlterViewImpl(configuration(), a1, (Collection<? extends Field<?>>) a2, a3, a4, a5, a6, a7);
    }































}
