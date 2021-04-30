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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>TRUNCATE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class TruncateImpl<R extends Record>
extends
    AbstractDDLQuery
implements
    TruncateIdentityStep<R>,
    TruncateCascadeStep<R>,
    TruncateFinalStep<R>,
    Truncate<R>
{

    private final Table<R> table;
    private       Boolean  restartIdentity;
    private       Cascade  cascade;

    TruncateImpl(
        Configuration configuration,
        Table<R> table
    ) {
        this(
            configuration,
            table,
            null,
            null
        );
    }

    TruncateImpl(
        Configuration configuration,
        Table<R> table,
        Boolean restartIdentity,
        Cascade cascade
    ) {
        super(configuration);

        this.table = table;
        this.restartIdentity = restartIdentity;
        this.cascade = cascade;
    }

    final Table<R> $table()           { return table; }
    final Boolean  $restartIdentity() { return restartIdentity; }
    final Cascade  $cascade()         { return cascade; }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final TruncateImpl<R> restartIdentity() {
        this.restartIdentity = true;
        return this;
    }

    @Override
    public final TruncateImpl<R> continueIdentity() {
        this.restartIdentity = false;
        return this;
    }

    @Override
    public final TruncateImpl<R> cascade() {
        this.cascade = Cascade.CASCADE;
        return this;
    }

    @Override
    public final TruncateImpl<R> restrict() {
        this.cascade = Cascade.RESTRICT;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[] CLAUSES = { Clause.TRUNCATE };

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {

            // These dialects don't implement the TRUNCATE statement



            case FIREBIRD:
            case IGNITE:
            case SQLITE: {
                ctx.visit(delete(table));
                break;
            }

            // All other dialects do
            default: {
                ctx.start(Clause.TRUNCATE_TRUNCATE)
                   .visit(K_TRUNCATE).sql(' ').visit(K_TABLE).sql(' ')
                   .visit(table);






                if (restartIdentity != null)
                    ctx.formatSeparator()
                       .visit(restartIdentity ? K_RESTART_IDENTITY : K_CONTINUE_IDENTITY);

                if (cascade != null)









                        ctx.formatSeparator()
                           .visit(cascade == Cascade.CASCADE ? K_CASCADE : K_RESTRICT);

                ctx.end(Clause.TRUNCATE_TRUNCATE);
                break;
            }
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }


}
