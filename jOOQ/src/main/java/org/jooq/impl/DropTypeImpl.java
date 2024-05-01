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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.QOM.Cascade;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * The <code>DROP TYPE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class DropTypeImpl
extends
    AbstractDDLQuery
implements
    QOM.DropType,
    DropTypeStep,
    DropTypeFinalStep
{

    final QueryPartListView<? extends Type<?>> types;
    final boolean                              ifExists;
          Cascade                              cascade;

    DropTypeImpl(
        Configuration configuration,
        Collection<? extends Type<?>> types,
        boolean ifExists
    ) {
        this(
            configuration,
            types,
            ifExists,
            null
        );
    }

    DropTypeImpl(
        Configuration configuration,
        Collection<? extends Type<?>> types,
        boolean ifExists,
        Cascade cascade
    ) {
        super(configuration);

        this.types = new QueryPartList<>(types);
        this.ifExists = ifExists;
        this.cascade = cascade;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final DropTypeImpl cascade() {
        this.cascade = Cascade.CASCADE;
        return this;
    }

    @Override
    public final DropTypeImpl restrict() {
        this.cascade = Cascade.RESTRICT;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supportedUntil();

    @Override
    public final void accept(Context<?> ctx) {











        accept0(ctx);
    }

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    private final void accept0(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.DROP_TYPE, c -> accept1(c));
        else
            accept1(ctx);
    }

    private final void accept1(Context<?> ctx) {
        ctx.visit(K_DROP).sql(' ');






        ctx.visit(K_TYPE);

        if (ifExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(types);

        if (cascade == Cascade.CASCADE)
            ctx.sql(' ').visit(K_CASCADE);
        else if (cascade == Cascade.RESTRICT)
            ctx.sql(' ').visit(K_RESTRICT);





    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final QOM.UnmodifiableList<? extends Type<?>> $types() {
        return QOM.unmodifiable(types);
    }

    @Override
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final Cascade $cascade() {
        return cascade;
    }

    @Override
    public final QOM.DropType $types(Collection<? extends Type<?>> newValue) {
        return $constructor().apply(newValue, $ifExists(), $cascade());
    }

    @Override
    public final QOM.DropType $ifExists(boolean newValue) {
        return $constructor().apply($types(), newValue, $cascade());
    }

    @Override
    public final QOM.DropType $cascade(Cascade newValue) {
        return $constructor().apply($types(), $ifExists(), newValue);
    }

    public final Function3<? super Collection<? extends Type<?>>, ? super Boolean, ? super Cascade, ? extends QOM.DropType> $constructor() {
        return (a1, a2, a3) -> new DropTypeImpl(configuration(), (Collection<? extends Type<?>>) a1, a2, a3);
    }























}
