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
 * The <code>SET</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class SetCommand
extends
    AbstractDDLQuery
implements
    QOM.SetCommand
{

    final Name     name;
    final Param<?> value;
    final boolean  local;

    SetCommand(
        Configuration configuration,
        Name name,
        Param<?> value,
        boolean local
    ) {
        super(configuration);

        this.name = name;
        this.value = value;
        this.local = local;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_BIND_VALUES = SQLDialect.supportedBy(POSTGRES);

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(K_SET);

        if (local)
            ctx.sql(' ').visit(K_LOCAL);

        ctx.sql(' ').visit(name).sql(" = ").paramTypeIf(ParamType.INLINED, NO_SUPPORT_BIND_VALUES.contains(ctx.dialect()), c -> c.visit(value));
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Name $name() {
        return name;
    }

    @Override
    public final Param<?> $value() {
        return value;
    }

    @Override
    public final boolean $local() {
        return local;
    }

    @Override
    public final QOM.SetCommand $name(Name newValue) {
        return constructor().apply(newValue, $value(), $local());
    }

    @Override
    public final QOM.SetCommand $value(Param<?> newValue) {
        return constructor().apply($name(), newValue, $local());
    }

    @Override
    public final QOM.SetCommand $local(boolean newValue) {
        return constructor().apply($name(), $value(), newValue);
    }

    public final Function3<? super Name, ? super Param<?>, ? super Boolean, ? extends QOM.SetCommand> constructor() {
        return (a1, a2, a3) -> new SetCommand(configuration(), a1, a2, a3);
    }
























}
