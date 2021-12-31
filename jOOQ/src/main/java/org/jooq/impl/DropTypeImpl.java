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

import static org.jooq.impl.Keywords.K_CASCADE;
import static org.jooq.impl.Keywords.K_DROP;
import static org.jooq.impl.Keywords.K_IF_EXISTS;
import static org.jooq.impl.Keywords.K_RESTRICT;
import static org.jooq.impl.Keywords.K_TYPE;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DropTypeFinalStep;
import org.jooq.DropTypeStep;
import org.jooq.Field;
import org.jooq.Function1;
import org.jooq.Name;
import org.jooq.impl.QOM.Cascade;
import org.jooq.impl.QOM.DropType;
import org.jooq.impl.QOM.UnmodifiableList;
import org.jooq.QueryPart;
// ...
// ...

/**
 * @author Lukas Eder
 */
final class DropTypeImpl
extends
    AbstractDDLQuery
implements
    DropTypeStep,
    DropType
{

    private final QueryPartList<Name> type;
    private final boolean             ifExists;
    private Cascade                   cascade;

    DropTypeImpl(Configuration configuration, Collection<?> type, boolean ifExists) {
        super(configuration);

        this.type = new QueryPartList<>(Tools.names(type));
        this.ifExists = ifExists;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final DropTypeFinalStep cascade() {
        this.cascade = Cascade.CASCADE;
        return this;
    }

    @Override
    public final DropTypeFinalStep restrict() {
        this.cascade = Cascade.RESTRICT;
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(K_DROP).sql(' ').visit(K_TYPE);

        if (ifExists)
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(type);

        if (cascade == Cascade.CASCADE)
            ctx.sql(' ').visit(K_CASCADE);
        else if (cascade == Cascade.RESTRICT)
            ctx.sql(' ').visit(K_RESTRICT);
    }
    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UnmodifiableList<? extends Name> $names() {
        return QOM.unmodifiable(type);
    }

    @Override
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final Cascade $cascade() {
        return cascade;
    }


















}
