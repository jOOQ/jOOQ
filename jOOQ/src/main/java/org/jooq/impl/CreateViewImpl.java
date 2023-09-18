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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>CREATE VIEW</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class CreateViewImpl<R extends Record>
extends
    AbstractDDLQuery
implements
    QOM.CreateView<R>,
    CreateViewAsStep<R>,
    CreateViewFinalStep
{

    final Table<?>                              view;
    final QueryPartListView<? extends Field<?>> fields;
    final boolean                               orReplace;
    final boolean                               materialized;
    final boolean                               ifNotExists;
          ResultQuery<? extends R>              query;

    CreateViewImpl(
        Configuration configuration,
        Table<?> view,
        Collection<? extends Field<?>> fields,
        boolean orReplace,
        boolean materialized,
        boolean ifNotExists
    ) {
        this(
            configuration,
            view,
            fields,
            orReplace,
            materialized,
            ifNotExists,
            null
        );
    }

    CreateViewImpl(
        Configuration configuration,
        Table<?> view,
        Collection<? extends Field<?>> fields,
        boolean orReplace,
        boolean materialized,
        boolean ifNotExists,
        ResultQuery<? extends R> query
    ) {
        super(configuration);

        this.view = view;
        this.fields = new QueryPartList<>(fields);
        this.orReplace = orReplace;
        this.materialized = materialized;
        this.ifNotExists = ifNotExists;
        this.query = query;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final CreateViewImpl<R> as(ResultQuery<? extends R> query) {
        this.query = query;
        return this;
    }

    @Override
    public final CreateViewImpl<R> as(String query, QueryPart... parts) {
        return as((ResultQuery<R>) DSL.resultQuery(query, parts));
    }

    @Override
    public final CreateViewImpl<R> as(String query, Object... bindings) {
        return as((ResultQuery<R>) DSL.resultQuery(query, bindings));
    }

    @Override
    public final CreateViewImpl<R> as(String query) {
        return as((ResultQuery<R>) DSL.resultQuery(query));
    }

    @Override
    public final CreateViewImpl<R> as(SQL query) {
        return as((ResultQuery<R>) DSL.resultQuery(query));
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]        CLAUSES                        = { Clause.CREATE_VIEW };
    private static final Set<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS       = SQLDialect.supportedUntil(DERBY, FIREBIRD, MYSQL, POSTGRES, YUGABYTEDB);
    private static final Set<SQLDialect> NO_SUPPORT_COLUMN_RENAME       = SQLDialect.supportedBy(TRINO);
    private static final Set<SQLDialect> NO_SUPPORT_COLUMN_RENAME_MVIEW = SQLDialect.supportedBy(H2);
    private transient Select<?>          parsed;

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx))
            tryCatch(ctx, DDLStatementType.CREATE_VIEW, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        switch (ctx.family()) {










            default:
                acceptDefault(ctx);
                break;
        }
    }



































    private final void acceptDefault(Context<?> ctx) {
        List<? extends Field<?>> f = fields;

        // [#2059] [#11485] Some dialects don't support column aliases at the view level
        boolean rename = f != null && f.size() > 0;
        boolean renameSupported =
            materialized && !NO_SUPPORT_COLUMN_RENAME_MVIEW.contains(ctx.dialect())
        || !materialized && !NO_SUPPORT_COLUMN_RENAME.contains(ctx.dialect());
        boolean replaceSupported = false ;









        ctx.start(Clause.CREATE_VIEW_NAME)
           .visit(replaceSupported && orReplace ? K_REPLACE : K_CREATE);

        if (orReplace && !replaceSupported) {
            ctx.sql(' ').visit(K_OR);

            switch (ctx.family()) {


                case FIREBIRD:
                    ctx.sql(' ').visit(K_ALTER);
                    break;

                default:
                    ctx.sql(' ').visit(K_REPLACE);
                    break;
            }
        }

        if (materialized)
            ctx.sql(' ').visit(K_MATERIALIZED);

        ctx.sql(' ').visit(K_VIEW)
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.visit(K_IF_NOT_EXISTS)
               .sql(' ');






        ctx.visit(view);

        if (rename && renameSupported)
            ctx.sql('(').visit(QueryPartListView.wrap(f).qualify(false)).sql(')');

        ctx.end(Clause.CREATE_VIEW_NAME)
           .formatSeparator()
           .visit(K_AS)
           .formatSeparator()
           .start(Clause.CREATE_VIEW_AS)
           // [#4806] CREATE VIEW doesn't accept parameters in most databases
           .visit(
               rename && !renameSupported
             ? selectFrom(parsed().asTable(name("t"), map(f, Field::getUnqualifiedName, Name[]::new)))
             : query,
               ParamType.INLINED
           )
           .end(Clause.CREATE_VIEW_AS);
    }

    final Select<?> parsed() {
        if (parsed != null)
            return parsed;

        if (query instanceof Select s)
            return parsed = s;

        DSLContext dsl = configuration().dsl();
        return dsl.parser().parseSelect(dsl.renderInlined(query));
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
    public final UnmodifiableList<? extends Field<?>> $fields() {
        return QOM.unmodifiable(fields);
    }

    @Override
    public final boolean $orReplace() {
        return orReplace;
    }

    @Override
    public final boolean $materialized() {
        return materialized;
    }

    @Override
    public final boolean $ifNotExists() {
        return ifNotExists;
    }

    @Override
    public final ResultQuery<? extends R> $query() {
        return query;
    }

    @Override
    public final QOM.CreateView<R> $view(Table<?> newValue) {
        return $constructor().apply(newValue, $fields(), $orReplace(), $materialized(), $ifNotExists(), $query());
    }

    @Override
    public final QOM.CreateView<R> $fields(Collection<? extends Field<?>> newValue) {
        return $constructor().apply($view(), newValue, $orReplace(), $materialized(), $ifNotExists(), $query());
    }

    @Override
    public final QOM.CreateView<R> $orReplace(boolean newValue) {
        return $constructor().apply($view(), $fields(), newValue, $materialized(), $ifNotExists(), $query());
    }

    @Override
    public final QOM.CreateView<R> $materialized(boolean newValue) {
        return $constructor().apply($view(), $fields(), $orReplace(), newValue, $ifNotExists(), $query());
    }

    @Override
    public final QOM.CreateView<R> $ifNotExists(boolean newValue) {
        return $constructor().apply($view(), $fields(), $orReplace(), $materialized(), newValue, $query());
    }

    @Override
    public final QOM.CreateView<R> $query(ResultQuery<? extends R> newValue) {
        return $constructor().apply($view(), $fields(), $orReplace(), $materialized(), $ifNotExists(), newValue);
    }

    public final Function6<? super Table<?>, ? super Collection<? extends Field<?>>, ? super Boolean, ? super Boolean, ? super Boolean, ? super ResultQuery<? extends R>, ? extends QOM.CreateView<R>> $constructor() {
        return (a1, a2, a3, a4, a5, a6) -> new CreateViewImpl(configuration(), a1, (Collection<? extends Field<?>>) a2, a3, a4, a5, a6);
    }




























}
