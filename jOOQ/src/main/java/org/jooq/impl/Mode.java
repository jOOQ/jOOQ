/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.mode;
import static org.jooq.impl.Names.N_MODE;
import static org.jooq.impl.Names.N_STATS_MODE;

import java.util.Set;

import org.jooq.AggregateFunction;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.SQLDialect;

/**
 * @author Lukas Eder
 */
final class Mode<T> extends AbstractAggregateFunction<T> implements QOM.Mode<T> {
    private static final Set<SQLDialect> EMULATE_AS_ORDERED_SET_AGG = SQLDialect.supportedBy(H2, POSTGRES, YUGABYTEDB);

    Mode(Field<T> arg) {
        super(false, N_MODE, arg.getDataType(), arg);
    }

    @Override
    public void accept(Context<?> ctx) {
        if (EMULATE_AS_ORDERED_SET_AGG.contains(ctx.dialect()))
            ctx.visit(mode().withinGroupOrderBy(arguments.get(0)));






        else
            super.accept(ctx);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> $arg1() {
        return (Field<T>) getArguments().get(0);
    }

    @Override
    public final Function1<? super Field<T>, ? extends AggregateFunction<T>> $constructor() {
        return f -> new Mode<>(f);
    }
}
