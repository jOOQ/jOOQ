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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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
 * The <code>POSITION</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Position
extends
    AbstractField<Integer>
implements
    QOM.Position
{

    final Field<String>           in;
    final Field<String>           search;
    final Field<? extends Number> startIndex;

    Position(
        Field<String> in,
        Field<String> search
    ) {
        super(
            N_POSITION,
            allNotNull(INTEGER, in, search)
        );

        this.in = nullSafeNotNull(in, VARCHAR);
        this.search = nullSafeNotNull(search, VARCHAR);
        this.startIndex = null;
    }

    Position(
        Field<String> in,
        Field<String> search,
        Field<? extends Number> startIndex
    ) {
        super(
            N_POSITION,
            allNotNull(INTEGER, in, search, startIndex)
        );

        this.in = nullSafeNotNull(in, VARCHAR);
        this.search = nullSafeNotNull(search, VARCHAR);
        this.startIndex = nullSafeNotNull(startIndex, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {








        if (startIndex != null) {
            switch (ctx.family()) {

                case DERBY:
                case H2:
                    ctx.visit(N_LOCATE).sql('(').visit(search).sql(", ").visit(in).sql(", ").visit(startIndex).sql(')');
                    break;





















                default:
                    ctx.visit(
                        DSL.case_(DSL.position(DSL.substring(in, startIndex), search))
                           .when(inline(0), inline(0))
                           .else_(iadd(DSL.position(DSL.substring(in, startIndex), search), isub(startIndex, one())))
                    );
                    break;
            }
        }
        else {
            switch (ctx.family()) {

                case DERBY:
                    ctx.visit(N_LOCATE).sql('(').visit(search).sql(", ").visit(in).sql(')');
                    break;


















                case SQLITE:
                    ctx.visit(N_INSTR).sql('(').visit(in).sql(", ").visit(search).sql(')');
                    break;

                default:
                    ctx.visit(N_POSITION).sql('(').visit(search).sql(' ').visit(K_IN).sql(' ').visit(in).sql(')');
                    break;
            }
        }
    }

















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $arg1() {
        return in;
    }

    @Override
    public final Field<String> $arg2() {
        return search;
    }

    @Override
    public final Field<? extends Number> $arg3() {
        return startIndex;
    }

    @Override
    public final QOM.Position $arg1(Field<String> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.Position $arg2(Field<String> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.Position $arg3(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<String>, ? super Field<String>, ? super Field<? extends Number>, ? extends QOM.Position> $constructor() {
        return (a1, a2, a3) -> new Position(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Position o) {
            return
                Objects.equals($in(), o.$in()) &&
                Objects.equals($search(), o.$search()) &&
                Objects.equals($startIndex(), o.$startIndex())
            ;
        }
        else
            return super.equals(that);
    }
}
