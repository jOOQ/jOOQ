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
 * The <code>STARTS WITH IGNORE CASE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class StartsWithIgnoreCase<T>
extends
    AbstractCondition
implements
    QOM.StartsWithIgnoreCase<T>
{

    final Field<T> string;
    final Field<T> prefix;

    StartsWithIgnoreCase(
        Field<T> string,
        Field<T> prefix
    ) {

        this.string = nullableIf(false, Tools.nullSafe(string, prefix.getDataType()));
        this.prefix = nullableIf(false, Tools.nullSafe(prefix, string.getDataType()));
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(string.likeIgnoreCase(DSL.concat(Tools.escapeForLike(prefix, ctx.configuration()), inline("%")), Tools.ESCAPE));
                break;
        }
    }










    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return string;
    }

    @Override
    public final Field<T> $arg2() {
        return prefix;
    }

    @Override
    public final QOM.StartsWithIgnoreCase<T> $arg1(Field<T> newValue) {
        return constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.StartsWithIgnoreCase<T> $arg2(Field<T> newValue) {
        return constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<T>, ? extends QOM.StartsWithIgnoreCase<T>> constructor() {
        return (a1, a2) -> new StartsWithIgnoreCase<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.StartsWithIgnoreCase) { QOM.StartsWithIgnoreCase<?> o = (QOM.StartsWithIgnoreCase<?>) that;
            return
                StringUtils.equals($string(), o.$string()) &&
                StringUtils.equals($prefix(), o.$prefix())
            ;
        }
        else
            return super.equals(that);
    }
}
