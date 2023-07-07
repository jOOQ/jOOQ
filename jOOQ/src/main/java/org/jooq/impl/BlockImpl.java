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

// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.conf.ParamType.INLINED;
// ...
// ...
// ...
import static org.jooq.impl.Keywords.K_ALIAS;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_ATOMIC;
import static org.jooq.impl.Keywords.K_BEGIN;
import static org.jooq.impl.Keywords.K_CALL;
import static org.jooq.impl.Keywords.K_CREATE;
import static org.jooq.impl.Keywords.K_DO;
import static org.jooq.impl.Keywords.K_DROP;
import static org.jooq.impl.Keywords.K_END;
import static org.jooq.impl.Keywords.K_EXECUTE;
import static org.jooq.impl.Keywords.K_EXECUTE_BLOCK;
import static org.jooq.impl.Keywords.K_EXECUTE_IMMEDIATE;
import static org.jooq.impl.Keywords.K_EXECUTE_STATEMENT;
import static org.jooq.impl.Keywords.K_IF;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Keywords.K_THEN;
import static org.jooq.impl.Keywords.K_TRUE;
import static org.jooq.impl.Tools.collect;
import static org.jooq.impl.Tools.decrement;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.increment;
import static org.jooq.impl.Tools.toplevel;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_FORCE_STATIC_STATEMENT;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_BLOCK_NESTING;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jooq.Block;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DDLQuery;
import org.jooq.Function1;
import org.jooq.Keyword;
import org.jooq.LanguageContext;
import org.jooq.Name;
// ...
import org.jooq.Query;
import org.jooq.QueryPart;
// ...
import org.jooq.SQLDialect;
import org.jooq.Statement;
// ...
// ...
import org.jooq.conf.ParamType;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.impl.ScopeMarker.ScopeContent;
import org.jooq.impl.Tools.ExtendedDataKey;

/**
 * @author Lukas Eder
 */
final class BlockImpl extends AbstractRowCountQuery implements Block {

    private static final Set<SQLDialect>  SUPPORTS_NULL_STATEMENT            = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);




















    final Collection<? extends Statement> statements;
    final boolean                         alwaysWrapInBeginEnd;

    BlockImpl(Configuration configuration, Collection<? extends Statement> statements, boolean alwaysWrapInBeginEnd) {
        super(configuration);

        // [#14153] Remove NullStatement as it is UTransient
        this.statements = collect(filter(statements, s -> !(s instanceof NullStatement)));
        this.alwaysWrapInBeginEnd = alwaysWrapInBeginEnd;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case FIREBIRD: {
                if (increment(ctx.data(), DATA_BLOCK_NESTING)) {
                    ctx.paramType(INLINED)
                       .visit(K_EXECUTE_BLOCK).sql(' ').visit(K_AS);

                    ctx.data(DATA_FORCE_STATIC_STATEMENT, true);
                    scopeDeclarations(ctx.formatIndentStart(),
                        c -> accept0(c.formatIndentEnd().formatSeparator())
                    );
                }
                else
                    accept0(ctx);

                decrement(ctx.data(), DATA_BLOCK_NESTING);
                break;
            }

            case POSTGRES:
            case YUGABYTEDB: {
                bodyAsString(ctx, K_DO, c -> accept0(c));
                break;
            }

            case H2: {
                String name = randomName();

                if (increment(ctx.data(), DATA_BLOCK_NESTING)) {
                    ctx.paramType(INLINED)
                       .visit(K_CREATE).sql(' ')
                       .visit(K_ALIAS).sql(' ').sql(name).sql(' ')
                       .visit(K_AS).sql(" $$")
                       .formatIndentStart()
                       .formatSeparator()
                       .sql("void x(Connection c) throws SQLException ");

                    ctx.data(DATA_FORCE_STATIC_STATEMENT, true);
                }

                accept0(ctx);

                if (decrement(ctx.data(), DATA_BLOCK_NESTING))
                    ctx.formatIndentEnd()
                       .formatSeparator()
                       .sql("$$;")
                       .formatSeparator()
                       .visit(K_CALL).sql(' ').sql(name).sql("();")
                       .formatSeparator()
                       .visit(K_DROP).sql(' ').visit(K_ALIAS).sql(' ').sql(name).sql(';');

                break;
            }















            case MYSQL: {












                accept0(ctx);
                break;
            }






            case HSQLDB:
            case MARIADB:
            default: {
                increment(ctx.data(), DATA_BLOCK_NESTING);
                accept0(ctx);
                decrement(ctx.data(), DATA_BLOCK_NESTING);
                break;
            }
        }
    }














































    static final void scopeDeclarations(Context<?> ctx, Consumer<? super Context<?>> runnable) {
        if (!ctx.configuration().commercial()) {
            runnable.accept(ctx);
            return;
        }















    }

    static final void bodyAsString(Context<?> ctx, Keyword keyword, Consumer<? super Context<?>> runnable) {
        ParamType previous = ctx.paramType();

        if (increment(ctx.data(), DATA_BLOCK_NESTING)) {
            ctx.paramType(INLINED);

            if (keyword != null)
                ctx.visit(keyword).sql(' ');

            ctx.sql('$').sql(ctx.settings().getRenderDollarQuotedStringToken()).sql('$')
               .formatSeparator()
               .data(DATA_FORCE_STATIC_STATEMENT, true);
        }

        runnable.accept(ctx);

        if (decrement(ctx.data(), DATA_BLOCK_NESTING))
            ctx.formatSeparator()
               .sql('$').sql(ctx.settings().getRenderDollarQuotedStringToken()).sql('$')
               .paramType(previous);
    }

    private static final String randomName() {
        return "block_" + System.currentTimeMillis() + "_" + (long) (10000000L * Math.random());
    }

    private final void accept0(Context<?> ctx) {
        boolean wrapInBeginEnd =
               alwaysWrapInBeginEnd



        ;

        if (wrapInBeginEnd) {
            boolean topLevel = ctx.scopeLevel() == -1;
            LanguageContext language = ctx.languageContext();

            if (topLevel && language == LanguageContext.QUERY)
                ctx.languageContext(LanguageContext.BLOCK);





























            {
                begin(ctx, topLevel);
                scopeDeclarations(ctx, c -> accept1(c));
                end(ctx, topLevel);
            }

            if (topLevel && language == LanguageContext.QUERY)
                ctx.languageContext(language);
        }
        else
            accept1(ctx);
    }

    private final void accept1(Context<?> ctx) {
        if (statements.isEmpty()) {

            // TODO: Replace this switch by SUPPORTS_NULL_STATEMENT usage
            switch (ctx.family()) {









                default:
                    break;
            }
        }
        else {

            statementLoop:
            for (Statement s : statements) {
                if (s instanceof NullStatement && !SUPPORTS_NULL_STATEMENT.contains(ctx.dialect()))
                    continue statementLoop;

                if (s instanceof Query && !(s instanceof Block))
                    ctx.languageContext(LanguageContext.QUERY, s, c -> accept2(c, s));
                else
                    accept2(ctx, s);
            }
        }
    }

    private static final void accept2(Context<?> ctx, Statement s) {
        ctx.formatSeparator();
        int position = ctx instanceof DefaultRenderContext d ? d.sql.length() : 0;


















































            ctx.visit(s);






        // [#11374] [#11367] TODO Improve this clunky semi colon decision logic
        if (position < (ctx instanceof DefaultRenderContext d ? d.sql.length() : 0))
            semicolonAfterStatement(ctx, s);
    }

    private static final void semicolonAfterStatement(Context<?> ctx, Statement s) {
        if (s instanceof Block)
            return;






















            ctx.sql(';');
    }



















    private static final void begin(Context<?> ctx, boolean topLevel) {
        if (ctx.family() == H2)
            ctx.sql('{');





        else
            ctx.visit(K_BEGIN);

        if (ctx.family() == MARIADB && toplevel(ctx.data(), DATA_BLOCK_NESTING))
            ctx.sql(' ').visit(K_NOT).sql(' ').visit(K_ATOMIC);
        else if (ctx.family() == HSQLDB)
            ctx.sql(' ').visit(K_ATOMIC);

        ctx.formatIndentStart();
    }

    private static final void end(Context<?> ctx, boolean topLevel) {
        ctx.formatIndentEnd()
           .formatSeparator();

        if (ctx.family() == H2)
            ctx.sql('}');





        else
            ctx.visit(K_END);

        switch (ctx.family()) {

            case H2:
            case FIREBIRD:
                break;









            default:
                ctx.sql(';');
        }
    }

    static final String  STATEMENT_VARIABLES = "org.jooq.impl.BlockImpl.statement-variables";













    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UnmodifiableList<? extends Statement> $statements() {
        return QOM.unmodifiable(statements);
    }














}
