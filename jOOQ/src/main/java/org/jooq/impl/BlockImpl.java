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
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_ATOMIC;
import static org.jooq.impl.Keywords.K_BEGIN;
import static org.jooq.impl.Keywords.K_DO;
import static org.jooq.impl.Keywords.K_END;
import static org.jooq.impl.Keywords.K_EXECUTE_BLOCK;
import static org.jooq.impl.Keywords.K_EXECUTE_IMMEDIATE;
import static org.jooq.impl.Keywords.K_EXECUTE_STATEMENT;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Tools.decrement;
import static org.jooq.impl.Tools.increment;
import static org.jooq.impl.Tools.DataKey.DATA_BLOCK_NESTING;
import static org.jooq.impl.Tools.DataKey.DATA_FORCE_STATIC_STATEMENT;

import java.util.Collection;
import java.util.EnumSet;

import org.jooq.Block;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DDLQuery;
import org.jooq.SQLDialect;
import org.jooq.Statement;

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

    BlockImpl(Configuration configuration, Collection<? extends Statement> statements) {
        super(configuration);

        this.statements = statements;
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

















            default: {
                accept0(ctx);
                break;
            }
        }
    }

    private final void accept0(Context<?> ctx) {
        ctx.visit(K_BEGIN);

        if (ctx.family() == MARIADB)
            ctx.sql(' ').visit(K_NOT).sql(' ').visit(K_ATOMIC);

        ctx.formatIndentStart();

        if (statements.isEmpty()) {
            switch (ctx.family()) {










                case FIREBIRD:
                case MARIADB:
                default:
                    break;
            }
        }
        else {
            statementLoop:
            for (Statement s : statements) {
                if (s instanceof NullStatement && !SUPPORTS_NULL_STATEMENT.contains(ctx.family()))
                    continue statementLoop;

                ctx.formatSeparator();









                ctx.visit(s);






                if (!(s instanceof Block))
                    ctx.sql(';');
            }
        }

        ctx.formatIndentEnd()
           .formatSeparator()
           .visit(K_END);

        switch (ctx.family()) {
            case FIREBIRD:
                break;






            default:
                ctx.sql(';');
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
