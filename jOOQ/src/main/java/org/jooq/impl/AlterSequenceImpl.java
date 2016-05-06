/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static java.util.Arrays.asList;
import static org.jooq.Clause.ALTER_SEQUENCE;
import static org.jooq.Clause.ALTER_SEQUENCE_RESTART;
import static org.jooq.Clause.ALTER_SEQUENCE_SEQUENCE;
import static org.jooq.SQLDialect.CUBRID;
// ...
// ...

import org.jooq.AlterSequenceFinalStep;
import org.jooq.AlterSequenceRestartStep;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Sequence;

/**
 * @author Lukas Eder
 */
class AlterSequenceImpl<T extends Number> extends AbstractQuery implements

    // Cascading interface implementations for AlterSequence behaviour
    AlterSequenceRestartStep<T>,
    AlterSequenceFinalStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { ALTER_SEQUENCE };

    private final Sequence<T>     sequence;
    private T                     restartWith;

    AlterSequenceImpl(Configuration configuration, Sequence<T> sequence) {
        super(configuration);

        this.sequence = sequence;
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

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.start(ALTER_SEQUENCE_SEQUENCE)
           .keyword("alter")
           .sql(' ')
           .keyword(ctx.family() == CUBRID ? "serial" : "sequence")
           .sql(' ').visit(sequence)
           .end(ALTER_SEQUENCE_SEQUENCE)
           .start(ALTER_SEQUENCE_RESTART);

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

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
