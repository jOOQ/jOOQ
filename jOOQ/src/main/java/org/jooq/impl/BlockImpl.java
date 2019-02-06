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
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.Keywords.K_ALIAS;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_ATOMIC;
import static org.jooq.impl.Keywords.K_BEGIN;
import static org.jooq.impl.Keywords.K_CALL;
import static org.jooq.impl.Keywords.K_CREATE;
import static org.jooq.impl.Keywords.K_DECLARE;
import static org.jooq.impl.Keywords.K_DO;
import static org.jooq.impl.Keywords.K_DROP;
import static org.jooq.impl.Keywords.K_END;
import static org.jooq.impl.Keywords.K_EXECUTE_BLOCK;
import static org.jooq.impl.Keywords.K_EXECUTE_IMMEDIATE;
import static org.jooq.impl.Keywords.K_EXECUTE_STATEMENT;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Tools.decrement;
import static org.jooq.impl.Tools.increment;
import static org.jooq.impl.Tools.toplevel;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_FORCE_STATIC_STATEMENT;
import static org.jooq.impl.Tools.DataKey.DATA_BLOCK_NESTING;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.jooq.Block;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DDLQuery;
// ...
import org.jooq.Field;
// ...
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.Statement;
// ...

/**
 * @author Lukas Eder
 */
final class BlockImpl extends AbstractQuery implements Block {

    /**
     * Generated UID
     */
    private static final long                     serialVersionUID                  = 6881305779639901498L;
    private static final EnumSet<SQLDialect>      REQUIRES_EXECUTE_IMMEDIATE_ON_DDL = EnumSet.of(FIREBIRD);
    private static final EnumSet<SQLDialect>      SUPPORTS_NULL_STATEMENT           = EnumSet.of(POSTGRES);









    private final Collection<? extends Statement> statements;
    private final boolean                         alwaysWrapInBeginEnd;

    BlockImpl(Configuration configuration, Collection<? extends Statement> statements, boolean alwaysWrapInBeginEnd) {
        super(configuration);

        this.statements = statements;
        this.alwaysWrapInBeginEnd = alwaysWrapInBeginEnd;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case FIREBIRD: {
                if (increment(ctx.data(), DATA_BLOCK_NESTING)) {
                    ctx.paramType(INLINED)
                       .visit(K_EXECUTE_BLOCK).sql(' ').visit(K_AS).sql(' ')
                       .formatSeparator();

                    ctx.data(DATA_FORCE_STATIC_STATEMENT, true);
                }

                accept0(ctx);

                decrement(ctx.data(), DATA_BLOCK_NESTING);
                break;
            }
//            case MARIADB: {
//                if (increment(ctx.data(), DATA_BLOCK_NESTING)) {
//                    ctx/*.paramType(INLINED)
//                       */.visit(K_BEGIN).sql(' ').visit(K_NOT).sql(' ').visit(K_ATOMIC)
//                       .formatIndentStart();
//
//                    // ctx.data(DATA_FORCE_STATIC_STATEMENT, true);
//                }
//
//                accept0(ctx);
//
//                decrement(ctx.data(), DATA_BLOCK_NESTING);
//                break;
//            }



            case POSTGRES: {
                if (increment(ctx.data(), DATA_BLOCK_NESTING)) {
                    ctx.paramType(INLINED)
                       .visit(K_DO).sql(" $$")
                       .formatSeparator();

                    ctx.data(DATA_FORCE_STATIC_STATEMENT, true);
                }

                accept0(ctx);

                if (decrement(ctx.data(), DATA_BLOCK_NESTING))
                    ctx.formatSeparator()
                       .sql("$$");

                break;
            }

            case H2: {
                String name = "block_" + System.currentTimeMillis() + "_" + (long) (10000000L * Math.random());

                // TODO: create a non-ambiguous name
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







            default: {
                increment(ctx.data(), DATA_BLOCK_NESTING);
                accept0(ctx);
                decrement(ctx.data(), DATA_BLOCK_NESTING);
                break;
            }
        }
    }

    private final void accept0(Context<?> ctx) {
        boolean wrapInBeginEnd =
               alwaysWrapInBeginEnd



        ;

        acceptDeclarations(ctx, new ArrayList<Statement>(statements), wrapInBeginEnd);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static final void acceptDeclarations(Context<?> ctx, List<Statement> statements, boolean wrapInBeginEnd) {


















































        acceptNonDeclarations(ctx, statements, wrapInBeginEnd);





    }

    private static final void semicolonAfterStatement(Context<?> ctx, Statement s) {
        if (s instanceof Block)
            return;

















            ctx.sql(';');
    }











    private static final void acceptNonDeclarations(Context<?> ctx, List<Statement> statements, boolean wrapInBeginEnd) {
        if (wrapInBeginEnd) {
            if (ctx.family() == H2)
                ctx.sql('{');
            else
                ctx.visit(K_BEGIN);

            if (ctx.family() == MARIADB)
                ctx.sql(' ').visit(K_NOT).sql(' ').visit(K_ATOMIC);

            ctx.formatIndentStart();
        }

        if (statements.isEmpty()) {
            switch (ctx.family()) {











                case H2:
                case FIREBIRD:
                case MARIADB:
                default:
                    break;
            }
        }
        else {
            statementLoop:
            for (int i = 0; i < statements.size(); i++) {
                Statement s = statements.get(i);










                if (s instanceof NullStatement && !SUPPORTS_NULL_STATEMENT.contains(ctx.family()))
                    continue statementLoop;

                ctx.formatSeparator();









                if (ctx.family() == H2 && s instanceof Query && !(s instanceof Block)) {
                    ArrayList<Variable<?>> list = new ArrayList<Variable<?>>();

                    ctx.data(STATEMENT_VARIABLES, list);
                    ctx.sql("try (PreparedStatement s = c.prepareStatement(")
                       .formatIndentStart()
                       .formatNewLine()
                       .sql('"')
                       .sql(ctx.render(s).replace("\"", "\\\"").replace("\n", "\\n\" +\n\""))
                       .sql('"')
                       .formatIndentEnd()
                       .formatNewLine()
                       .sql(")) {")
                       .formatIndentStart()
                       .formatSeparator();

                    for (int j = 0; j < list.size(); j++)
                        ctx.sql("s.setObject(" + (j + 1) + ", ")
                           .sql(list.get(j).getName())
                           .sql(");")
                           .formatSeparator();

                    ctx.sql("s.execute();")
                       .formatIndentEnd()
                       .formatSeparator()
                       .sql('}');

                    ctx.data().remove(STATEMENT_VARIABLES);
                }
                else
                    ctx.visit(s);






                semicolonAfterStatement(ctx, s);
            }
        }

        if (wrapInBeginEnd)
            end(ctx);
    }

    private static final void end(Context<?> ctx) {
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












}
