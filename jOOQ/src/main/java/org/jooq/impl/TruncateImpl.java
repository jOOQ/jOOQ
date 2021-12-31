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
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>TRUNCATE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class TruncateImpl<R extends Record>
extends
    AbstractDDLQuery
implements
    QOM.Truncate<R>,
    TruncateIdentityStep<R>,
    TruncateCascadeStep<R>,
    TruncateFinalStep<R>,
    org.jooq.Truncate<R>
{

    final Table<R>              table;
          IdentityRestartOption restartIdentity;
          Cascade               cascade;

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
        IdentityRestartOption restartIdentity,
        Cascade cascade
    ) {
        super(configuration);

        this.table = table;
        this.restartIdentity = restartIdentity;
        this.cascade = cascade;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final TruncateImpl<R> restartIdentity() {
        this.restartIdentity = IdentityRestartOption.RESTART_IDENTITY;
        return this;
    }

    @Override
    public final TruncateImpl<R> continueIdentity() {
        this.restartIdentity = IdentityRestartOption.CONTINUE_IDENTITY;
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
                       .visit(restartIdentity.keyword);

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



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<R> $table() {
        return table;
    }

    @Override
    public final IdentityRestartOption $restartIdentity() {
        return restartIdentity;
    }

    @Override
    public final Cascade $cascade() {
        return cascade;
    }

    @Override
    public final QOM.Truncate<R> $table(Table<R> newValue) {
        return constructor().apply(newValue, $restartIdentity(), $cascade());
    }

    @Override
    public final QOM.Truncate<R> $restartIdentity(IdentityRestartOption newValue) {
        return constructor().apply($table(), newValue, $cascade());
    }

    @Override
    public final QOM.Truncate<R> $cascade(Cascade newValue) {
        return constructor().apply($table(), $restartIdentity(), newValue);
    }

    public final Function3<? super Table<R>, ? super IdentityRestartOption, ? super Cascade, ? extends QOM.Truncate<R>> constructor() {
        return (a1, a2, a3) -> new TruncateImpl(configuration(), a1, a2, a3);
    }























}
