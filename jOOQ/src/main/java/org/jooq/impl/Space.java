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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>SPACE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Space
extends
    AbstractField<String>
implements
    QOM.Space
{

    final Field<? extends Number> count;

    Space(
        Field<? extends Number> count
    ) {
        super(
            N_SPACE,
            allNotNull(VARCHAR, count)
        );

        this.count = nullSafeNotNull(count, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {






            case FIREBIRD:
            case SQLITE:
            case TRINO:
                return false;






            case DERBY:
            case DUCKDB:
            case HSQLDB:
            case POSTGRES:
            case YUGABYTEDB:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {












            case FIREBIRD:
            case SQLITE:
            case TRINO: {
                // [#10135] Avoid REPEAT() emulation that is too complicated for SPACE(N)
                ctx.visit(DSL.rpad(DSL.inline(' '), count));
                break;
            }






            case DERBY:
            case DUCKDB:
            case HSQLDB:
            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(DSL.repeat(DSL.inline(" "), count));
                break;

            default:
                ctx.visit(function(N_SPACE, getDataType(), count));
                break;
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<? extends Number> $arg1() {
        return count;
    }

    @Override
    public final QOM.Space $arg1(Field<? extends Number> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<? extends Number>, ? extends QOM.Space> $constructor() {
        return (a1) -> new Space(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Space o) {
            return
                Objects.equals($count(), o.$count())
            ;
        }
        else
            return super.equals(that);
    }
}
