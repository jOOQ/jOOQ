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

import java.math.BigDecimal;


/**
 * The <code>RADIANS</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class Radians
extends
    AbstractField<BigDecimal>
implements
    QOM.Radians
{

    final Field<? extends Number> degrees;

    Radians(
        Field<? extends Number> degrees
    ) {
        super(
            N_RADIANS,
            allNotNull(NUMERIC, degrees)
        );

        this.degrees = nullSafeNotNull(degrees, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {






            case FIREBIRD:
                return false;

            case SQLITE:
                return false;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {












            case FIREBIRD:
                ctx.visit(idiv(
                    imul(
                        castIfNeeded(degrees, BigDecimal.class),
                        pi()
                    ),
                    inline(180)
                ));
                break;

            case SQLITE: {











                ctx.visit(function(N_RADIANS, getDataType(), degrees));
                break;
            }

            default:
                ctx.visit(function(N_RADIANS, getDataType(), degrees));
                break;
        }
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<? extends Number> $arg1() {
        return degrees;
    }

    @Override
    public final QOM.Radians $arg1(Field<? extends Number> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<? extends Number>, ? extends QOM.Radians> $constructor() {
        return (a1) -> new Radians(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Radians o) {
            return
                Objects.equals($degrees(), o.$degrees())
            ;
        }
        else
            return super.equals(that);
    }
}
