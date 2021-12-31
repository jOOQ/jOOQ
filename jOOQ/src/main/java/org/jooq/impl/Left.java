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
 * The <code>LEFT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class Left
extends
    AbstractField<String>
implements
    QOM.Left
{

    final Field<String>           string;
    final Field<? extends Number> length;

    Left(
        Field<String> string,
        Field<? extends Number> length
    ) {
        super(
            N_LEFT,
            allNotNull(VARCHAR, string, length)
        );

        this.string = nullSafeNotNull(string, VARCHAR);
        this.length = nullSafeNotNull(length, INTEGER);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {







            case DERBY:
            case SQLITE:
                ctx.visit(DSL.substring(string, inline(1), length));
                break;

            default:
                ctx.visit(function(N_LEFT, getDataType(), string, length));
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $string() {
        return string;
    }

    @Override
    public final Field<? extends Number> $length() {
        return length;
    }

    @Override
    public final QOM.Left $string(Field<String> newValue) {
        return constructor().apply(newValue, $length());
    }

    @Override
    public final QOM.Left $length(Field<? extends Number> newValue) {
        return constructor().apply($string(), newValue);
    }

    public final Function2<? super Field<String>, ? super Field<? extends Number>, ? extends QOM.Left> constructor() {
        return (a1, a2) -> new Left(a1, a2);
    }
























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.Left) { QOM.Left o = (QOM.Left) that;
            return
                StringUtils.equals($string(), o.$string()) &&
                StringUtils.equals($length(), o.$length())
            ;
        }
        else
            return super.equals(that);
    }
}
