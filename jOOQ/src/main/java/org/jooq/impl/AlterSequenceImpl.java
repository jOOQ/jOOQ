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

import static org.jooq.Clause.ALTER_SEQUENCE;
import static org.jooq.Clause.ALTER_SEQUENCE_RENAME;
import static org.jooq.Clause.ALTER_SEQUENCE_RESTART;
import static org.jooq.Clause.ALTER_SEQUENCE_SEQUENCE;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
// ...
// ...
import static org.jooq.impl.Keywords.K_ALTER;
import static org.jooq.impl.Keywords.K_ALTER_TABLE;
import static org.jooq.impl.Keywords.K_CACHE;
import static org.jooq.impl.Keywords.K_CYCLE;
import static org.jooq.impl.Keywords.K_IF_EXISTS;
import static org.jooq.impl.Keywords.K_INCREMENT_BY;
import static org.jooq.impl.Keywords.K_MAXVALUE;
import static org.jooq.impl.Keywords.K_MINVALUE;
import static org.jooq.impl.Keywords.K_NO;
import static org.jooq.impl.Keywords.K_RENAME;
import static org.jooq.impl.Keywords.K_RENAME_SEQUENCE;
import static org.jooq.impl.Keywords.K_RENAME_TO;
import static org.jooq.impl.Keywords.K_RESTART;
import static org.jooq.impl.Keywords.K_RESTART_WITH;
import static org.jooq.impl.Keywords.K_SEQUENCE;
import static org.jooq.impl.Keywords.K_SERIAL;
import static org.jooq.impl.Keywords.K_START_WITH;
import static org.jooq.impl.Keywords.K_TO;
import static org.jooq.impl.Tools.beginTryCatch;
import static org.jooq.impl.Tools.endTryCatch;

import java.util.Set;

import org.jooq.AlterSequenceFinalStep;
import org.jooq.AlterSequenceStep;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
// ...
import org.jooq.SQLDialect;
import org.jooq.Sequence;

/**
 * @author Lukas Eder
 */
final class AlterSequenceImpl<T extends Number> extends AbstractRowCountQuery implements

    // Cascading interface implementations for AlterSequence behaviour
    AlterSequenceStep<T> {

    /**
     * Generated UID
     */
    private static final long            serialVersionUID     = 8904572826501186329L;
    private static final Clause[]        CLAUSES              = { ALTER_SEQUENCE };
    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supported(CUBRID, DERBY, FIREBIRD);
    private static final Set<SQLDialect> NO_SEPARATOR         = SQLDialect.supported(CUBRID, MARIADB);
    private static final Set<SQLDialect> NO_SUPPORT_CACHE     = SQLDialect.supported(DERBY, FIREBIRD, HSQLDB);





    private final Sequence<T>            sequence;
    private final boolean                ifExists;
    private Sequence<?>                  renameTo;
    private boolean                      restart;
    private Field<?>                     restartWith;
    private Field<? extends Number>      startWith;
    private Field<? extends Number>      incrementBy;
    private Field<? extends Number>      minvalue;
    private boolean                      noMinvalue;
    private Field<? extends Number>      maxvalue;
    private boolean                      noMaxvalue;
    private Boolean                      cycle;
    private Field<? extends Number>      cache;
    private boolean                      noCache;

    AlterSequenceImpl(Configuration configuration, Sequence<T> sequence) {
        this(configuration, sequence, false);
    }

    AlterSequenceImpl(Configuration configuration, Sequence<T> sequence, boolean ifExists) {
        super(configuration);

        this.sequence = sequence;
        this.ifExists = ifExists;
    }

    final Sequence<?> $sequence() { return sequence; }
    final boolean     $ifExists() { return ifExists; }
    final Sequence<?> $renameTo() { return renameTo; }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final AlterSequenceStep<T> restart() {
        restart = true;
        restartWith = null;
        return this;
    }

    @Override
    public final AlterSequenceStep<T> restartWith(T value) {
        return restartWith(Tools.field(value, sequence.getDataType()));
    }

    @Override
    public final AlterSequenceStep<T> restartWith(Field<? extends T> value) {
        restart = false;
        restartWith = value;
        return this;
    }

    @Override
    public final AlterSequenceFinalStep renameTo(Sequence<?> newName) {
        renameTo = newName;
        return this;
    }

    @Override
    public final AlterSequenceFinalStep renameTo(Name newName) {
        return renameTo(DSL.sequence(newName));
    }

    @Override
    public final AlterSequenceFinalStep renameTo(String newName) {
        return renameTo(DSL.name(newName));
    }

    @Override
    public AlterSequenceStep<T> startWith(Number constant) {
        return startWith(Tools.field(constant, sequence.getDataType()));
    }

    @Override
    public AlterSequenceStep<T> startWith(Field<? extends Number> constant) {
        startWith = constant;
        return this;
    }

    @Override
    public AlterSequenceStep<T> incrementBy(Number constant) {
        return incrementBy(Tools.field(constant, sequence.getDataType()));
    }

    @Override
    public AlterSequenceStep<T> incrementBy(Field<? extends Number> constant) {
        incrementBy = constant;
        return this;
    }

    @Override
    public AlterSequenceStep<T> minvalue(Number constant) {
        return minvalue(Tools.field(constant, sequence.getDataType()));
    }

    @Override
    public AlterSequenceStep<T> minvalue(Field<? extends Number> constant) {
        minvalue = constant;
        noMinvalue = false;
        return this;
    }

    @Override
    public AlterSequenceStep<T> noMinvalue() {
        minvalue = null;
        noMinvalue = true;
        return this;
    }

    @Override
    public AlterSequenceStep<T> maxvalue(Number constant) {
        return maxvalue(Tools.field(constant, sequence.getDataType()));
    }

    @Override
    public AlterSequenceStep<T> maxvalue(Field<? extends Number> constant) {
        maxvalue = constant;
        noMaxvalue = false;
        return this;
    }

    @Override
    public AlterSequenceStep<T> noMaxvalue() {
        maxvalue = null;
        noMaxvalue = true;
        return this;
    }

    @Override
    public AlterSequenceStep<T> cycle() {
        cycle = true;
        return this;
    }

    @Override
    public AlterSequenceStep<T> noCycle() {
        cycle = false;
        return this;
    }

    @Override
    public AlterSequenceStep<T> cache(Number constant) {
        return cache(Tools.field(constant, sequence.getDataType()));
    }

    @Override
    public AlterSequenceStep<T> cache(Field<? extends Number> constant) {
        cache = constant;
        noCache = false;
        return this;
    }

    @Override
    public AlterSequenceStep<T> noCache() {
        cache = null;
        noCache = true;
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
            beginTryCatch(ctx, DDLStatementType.ALTER_SEQUENCE);
            accept0(ctx);
            endTryCatch(ctx, DDLStatementType.ALTER_SEQUENCE);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        switch (ctx.family()) {











            case MARIADB:
                if (renameTo != null)
                    acceptRenameTable(ctx);
                else
                    accept1(ctx);

                break;

            default:
                accept1(ctx);
                break;
        }
    }

    private final void acceptRenameTable(Context<?> ctx) {
        boolean qualify = ctx.qualify();

        ctx.start(ALTER_SEQUENCE_SEQUENCE)
           .start(ALTER_SEQUENCE_RENAME)
           .visit(K_ALTER_TABLE)
           .sql(' ')
           .visit(sequence)
           .sql(' ')
           .visit(K_RENAME_TO)
           .sql(' ')
           .qualify(false)
           .visit(renameTo)
           .qualify(qualify)
           .end(ALTER_SEQUENCE_RENAME)
           .end(ALTER_SEQUENCE_SEQUENCE);
    }






















    private final void accept1(Context<?> ctx) {
        ctx.start(ALTER_SEQUENCE_SEQUENCE)
           .visit(K_ALTER)
           .sql(' ')
           .visit(ctx.family() == CUBRID ? K_SERIAL : K_SEQUENCE);

        if (ifExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        switch (ctx.family()) {












            default: {
                ctx.sql(' ').visit(sequence);
                break;
            }
        }

        ctx.end(ALTER_SEQUENCE_SEQUENCE);

        if (renameTo != null) {
            boolean qualify = ctx.qualify();

            ctx.start(ALTER_SEQUENCE_RENAME)
               .sql(' ').visit(K_RENAME_TO)
               .sql(' ')
               .qualify(false)
               .visit(renameTo)
               .qualify(qualify)
               .end(ALTER_SEQUENCE_RENAME);
        }
        else {
            ctx.start(ALTER_SEQUENCE_RESTART);

            String noSeparator = NO_SEPARATOR.contains(ctx.family()) ? "" : " ";

            if (incrementBy != null) {
                ctx.sql(' ').visit(K_INCREMENT_BY)
                   .sql(' ').visit(incrementBy);
            }

            if (minvalue != null)
                ctx.sql(' ').visit(K_MINVALUE).sql(' ').visit(minvalue);
            else if (noMinvalue)
                ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_MINVALUE);

            if (maxvalue != null)
                ctx.sql(' ').visit(K_MAXVALUE).sql(' ').visit(maxvalue);
            else if (noMaxvalue)
                ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_MAXVALUE);

            if (startWith != null) {
                ctx.sql(' ').visit(K_START_WITH)
                   .sql(' ').visit(startWith);
            }

            if (restart) {





                    ctx.sql(' ').visit(K_RESTART);
            }
            else if (restartWith != null) {
                if (ctx.family() == CUBRID)
                    ctx.sql(' ').visit(K_START_WITH)
                       .sql(' ').visit(restartWith);
                else
                    ctx.sql(' ').visit(K_RESTART_WITH)
                       .sql(' ').visit(restartWith);
            }

            if (!NO_SUPPORT_CACHE.contains(ctx.family()))
                if (cache != null)
                    ctx.sql(' ').visit(K_CACHE).sql(' ').visit(cache);
                else if (noCache)
                    // TODO: Postgres requires CACHE 1 here
                    ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_CACHE);

            if (Boolean.TRUE.equals(cycle))
                ctx.sql(' ').visit(K_CYCLE);
            else if (Boolean.FALSE.equals(cycle))
                ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_CYCLE);

            ctx.end(ALTER_SEQUENCE_RESTART);
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

}
