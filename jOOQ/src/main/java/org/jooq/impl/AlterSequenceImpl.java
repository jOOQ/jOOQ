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
// ...
// ...
// ...

import org.jooq.AlterSequenceFinalStep;
import org.jooq.AlterSequenceStep;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Sequence;

/**
 * @author Lukas Eder
 */
final class AlterSequenceImpl<T extends Number> extends AbstractQuery implements

    // Cascading interface implementations for AlterSequence behaviour
    AlterSequenceStep<T>,
    AlterSequenceFinalStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { ALTER_SEQUENCE };

    private final Sequence<T>     sequence;
    private final boolean         ifExists;
    private T                     restartWith;
    private Sequence<?>           renameTo;

    AlterSequenceImpl(Configuration configuration, Sequence<T> sequence) {
        this(configuration, sequence, false);
    }

    AlterSequenceImpl(Configuration configuration, Sequence<T> sequence, boolean ifExists) {
        super(configuration);

        this.sequence = sequence;
        this.ifExists = ifExists;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final AlterSequenceFinalStep restart() {
        return this;
    }

    @Override
    public final AlterSequenceFinalStep restartWith(T value) {
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

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfExists(Context<?> ctx) {
        return !asList(CUBRID, DERBY, FIREBIRD).contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx)) {
            Tools.executeImmediateIfExistsBegin(ctx, DDLStatementType.ALTER_SEQUENCE, sequence);
            accept0(ctx);
            Tools.executeImmediateIfExistsEnd(ctx, DDLStatementType.ALTER_SEQUENCE, sequence);
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
        ctx.start(ALTER_SEQUENCE_SEQUENCE)
           .keyword("alter")
           .sql(' ')
           .keyword(ctx.family() == CUBRID ? "serial" : "sequence");

        if (ifExists && supportsIfExists(ctx))
            ctx.sql(' ').keyword("if exists");

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
               .sql(' ').keyword("rename to")
               .sql(' ')
               .qualify(false)
               .visit(renameTo)
               .qualify(qualify)
               .end(ALTER_SEQUENCE_RENAME);
        }
        else {
            ctx.start(ALTER_SEQUENCE_RESTART);

            T with = restartWith;
            if (with == null) {






                    ctx.sql(' ').keyword("restart");
            }
            else {
                if (ctx.family() == CUBRID)
                    ctx.sql(' ').keyword("start with")
                       .sql(' ').sql(with.toString());
                else
                    ctx.sql(' ').keyword("restart with")
                       .sql(' ').sql(with.toString());
            }

            ctx.end(ALTER_SEQUENCE_RESTART);
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
