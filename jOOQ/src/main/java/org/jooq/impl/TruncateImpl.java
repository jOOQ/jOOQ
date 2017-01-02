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

import static org.jooq.Clause.TRUNCATE;
import static org.jooq.Clause.TRUNCATE_TRUNCATE;
// ...

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TruncateCascadeStep;
import org.jooq.TruncateFinalStep;
import org.jooq.TruncateIdentityStep;

/**
 * @author Lukas Eder
 */
final class TruncateImpl<R extends Record> extends AbstractQuery implements

    // Cascading interface implementations for Truncate behaviour
    TruncateIdentityStep<R> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { TRUNCATE };

    private final Table<R>    table;
    private Boolean           cascade;
    private Boolean           restartIdentity;

    public TruncateImpl(Configuration configuration, Table<R> table) {
        super(configuration);

        this.table = table;
    }

    @Override
    public final TruncateFinalStep<R> cascade() {
        cascade = true;
        return this;
    }

    @Override
    public final TruncateFinalStep<R> restrict() {
        cascade = false;
        return this;
    }

    @Override
    public final TruncateCascadeStep<R> restartIdentity() {
        restartIdentity = true;
        return this;
    }

    @Override
    public final TruncateCascadeStep<R> continueIdentity() {
        restartIdentity = false;
        return this;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

            // These dialects don't implement the TRUNCATE statement




            case FIREBIRD:
            case SQLITE: {
                ctx.visit(create(ctx).delete(table));
                break;
            }

            // All other dialects do
            default: {
                ctx.start(TRUNCATE_TRUNCATE)
                   .keyword("truncate table").sql(' ')
                   .visit(table);






                if (restartIdentity != null)
                    ctx.formatSeparator()
                       .keyword(restartIdentity ? "restart identity" : "continue identity");

                if (cascade != null)









                        ctx.formatSeparator()
                           .keyword(cascade ? "cascade" : "restrict");

                ctx.end(TRUNCATE_TRUNCATE);
                break;
            }
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
