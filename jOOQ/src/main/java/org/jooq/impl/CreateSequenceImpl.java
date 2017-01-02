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
import static org.jooq.Clause.CREATE_SEQUENCE;
import static org.jooq.Clause.CREATE_SEQUENCE_SEQUENCE;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateSequenceFinalStep;
import org.jooq.Sequence;

/**
 * @author Lukas Eder
 */
final class CreateSequenceImpl extends AbstractQuery implements

    // Cascading interface implementations for CREATE SEQUENCE behaviour
    CreateSequenceFinalStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { CREATE_SEQUENCE };

    private final Sequence<?>     sequence;
    private final boolean         ifNotExists;

    CreateSequenceImpl(Configuration configuration, Sequence<?> sequence, boolean ifNotExists) {
        super(configuration);

        this.sequence = sequence;
        this.ifNotExists = ifNotExists;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !asList(DERBY, FIREBIRD).contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.executeImmediateBegin(ctx, DDLStatementType.CREATE_SEQUENCE);
            accept0(ctx);
            Tools.executeImmediateEnd(ctx, DDLStatementType.CREATE_SEQUENCE);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        ctx.start(CREATE_SEQUENCE_SEQUENCE)
           .keyword("create")
           .sql(' ')
           .keyword(ctx.family() == CUBRID ? "serial" : "sequence")
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.keyword("if not exists")
               .sql(' ');

        ctx.visit(sequence);

        // Some databases default to sequences starting with MIN_VALUE
        if (asList(DERBY).contains(ctx.family()))
            ctx.sql(' ').keyword("start with").sql(" 1");

        ctx.end(CREATE_SEQUENCE_SEQUENCE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
