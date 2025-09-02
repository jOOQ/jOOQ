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
 * The <code>SUBSTRING</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Substring
extends
    AbstractField<String>
implements
    QOM.Substring
{

    final Field<String>           string;
    final Field<? extends Number> startingPosition;
    final Field<? extends Number> length;

    Substring(
        Field<String> string,
        Field<? extends Number> startingPosition
    ) {
        super(
            N_SUBSTRING,
            allNotNull(VARCHAR, string, startingPosition)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.startingPosition = nullSafeNotNull(startingPosition, INTEGER);
        this.length = null;
    }

    Substring(
        Field<String> string,
        Field<? extends Number> startingPosition,
        Field<? extends Number> length
    ) {
        super(
            N_SUBSTRING,
            allNotNull(VARCHAR, string, startingPosition, length)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.startingPosition = nullSafeNotNull(startingPosition, INTEGER);
        this.length = nullSafeNotNull(length, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        Name functionName = N_SUBSTRING;

        switch (ctx.family()) {






            // [#430] These databases use SQL standard syntax

            case FIREBIRD: {
                if (length == null)
                    ctx.visit(N_SUBSTRING).sql('(').visit(string).sql(' ').visit(K_FROM).sql(' ').visit(startingPosition).sql(')');
                else
                    ctx.visit(N_SUBSTRING).sql('(').visit(string).sql(' ').visit(K_FROM).sql(' ').visit(startingPosition).sql(' ').visit(K_FOR).sql(' ').visit(length).sql(')');

                return;
            }

































            case DERBY:
            case SQLITE:
                functionName = N_SUBSTR;
                break;
        }

        if (length == null)
            ctx.visit(function(functionName, getDataType(), string, startingPosition));
        else
            ctx.visit(function(functionName, getDataType(), string, startingPosition, length));
    }

















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $arg1() {
        return string;
    }

    @Override
    public final Field<? extends Number> $arg2() {
        return startingPosition;
    }

    @Override
    public final Field<? extends Number> $arg3() {
        return length;
    }

    @Override
    public final QOM.Substring $arg1(Field<String> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.Substring $arg2(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.Substring $arg3(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Field<String>, ? super Field<? extends Number>, ? super Field<? extends Number>, ? extends QOM.Substring> $constructor() {
        return (a1, a2, a3) -> new Substring(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Substring o) {
            return
                Objects.equals($string(), o.$string()) &&
                Objects.equals($startingPosition(), o.$startingPosition()) &&
                Objects.equals($length(), o.$length())
            ;
        }
        else
            return super.equals(that);
    }
}
