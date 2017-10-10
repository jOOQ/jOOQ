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
import static org.jooq.impl.Keywords.K_CREATE;
import static org.jooq.impl.Keywords.K_IF_NOT_EXISTS;
import static org.jooq.impl.Keywords.K_SEQUENCE;
import static org.jooq.impl.Keywords.K_SERIAL;
import static org.jooq.impl.Keywords.K_START_WITH;

import java.util.EnumSet;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateSequenceFinalStep;
import org.jooq.SQLDialect;
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
    private static final long                serialVersionUID         = 8904572826501186329L;
    private static final Clause[]            CLAUSES                  = { CREATE_SEQUENCE };
    private static final EnumSet<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS = EnumSet.of(DERBY, FIREBIRD);
    private static final EnumSet<SQLDialect> REQUIRES_START_WITH      = EnumSet.of(DERBY);

    private final Sequence<?>                sequence;
    private final boolean                    ifNotExists;

    CreateSequenceImpl(Configuration configuration, Sequence<?> sequence, boolean ifNotExists) {
        super(configuration);

        this.sequence = sequence;
        this.ifNotExists = ifNotExists;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.beginTryCatch(ctx, DDLStatementType.CREATE_SEQUENCE);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.CREATE_SEQUENCE);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        ctx.start(CREATE_SEQUENCE_SEQUENCE)
           .visit(K_CREATE)
           .sql(' ')
           .visit(ctx.family() == CUBRID ? K_SERIAL : K_SEQUENCE)
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.visit(K_IF_NOT_EXISTS)
               .sql(' ');

        ctx.visit(sequence);

        // Some databases default to sequences starting with MIN_VALUE
        if (REQUIRES_START_WITH.contains(ctx.family()))
            ctx.sql(' ').visit(K_START_WITH).sql(" 1");

        ctx.end(CREATE_SEQUENCE_SEQUENCE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
