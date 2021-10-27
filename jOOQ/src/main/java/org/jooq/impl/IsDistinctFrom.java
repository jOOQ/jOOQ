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
 * The <code>IS DISTINCT FROM</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class IsDistinctFrom<T>
extends
    AbstractCondition
implements
    QOM.IsDistinctFrom<T>
{

    final Field<T> arg1;
    final Field<T> arg2;

    IsDistinctFrom(
        Field<T> arg1,
        Field<T> arg2
    ) {

        this.arg1 = nullableIf(true, Tools.nullSafe(arg1, arg2.getDataType()));
        this.arg2 = nullableIf(true, Tools.nullSafe(arg2, arg1.getDataType()));
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    static final Set<SQLDialect> EMULATE_DISTINCT_PREDICATE  = SQLDialect.supportedUntil(CUBRID, DERBY);
    static final Set<SQLDialect> SUPPORT_DISTINCT_WITH_ARROW = SQLDialect.supportedBy(MARIADB, MYSQL);





    @Override
    final boolean isNullable() {
        return false;
    }

    @Override
    public final void accept(Context<?> ctx) {





        if (arg1.getDataType().isEmbeddable() && arg2.getDataType().isEmbeddable())
            ctx.visit(row(embeddedFields(arg1)).isDistinctFrom(row(embeddedFields(arg2))));






        // [#3511]         These dialects need to emulate the IS DISTINCT FROM predicate,
        //                 optimally using INTERSECT...
        // [#7222] [#7224] Make sure the columns are aliased
        else if (EMULATE_DISTINCT_PREDICATE.contains(ctx.dialect()))
            ctx.visit(notExists(select(arg1.as("x")).intersect(select(arg2.as("x")))));

        // MySQL knows the <=> operator
        else if (SUPPORT_DISTINCT_WITH_ARROW.contains(ctx.dialect()))
            ctx.visit(condition("{not}({0} <=> {1})", arg1, arg2));

        // SQLite knows the IS / IS NOT predicate
        else if (SQLITE == ctx.family())
            ctx.visit(condition("{0} {is not} {1}", arg1, arg2));

        else
            ctx.visit(arg1).sql(' ').visit(K_IS).sql(' ').visit(K_DISTINCT).sql(' ').visit(K_FROM).sql(' ').visit(arg2);
    }












    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T> $arg1() {
        return arg1;
    }

    @Override
    public final Field<T> $arg2() {
        return arg2;
    }

    @Override
    public final QOM.IsDistinctFrom<T> $arg1(Field<T> newValue) {
        return constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.IsDistinctFrom<T> $arg2(Field<T> newValue) {
        return constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T>, ? super Field<T>, ? extends QOM.IsDistinctFrom<T>> constructor() {
        return (a1, a2) -> new IsDistinctFrom<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.IsDistinctFrom) { QOM.IsDistinctFrom<?> o = (QOM.IsDistinctFrom<?>) that;
            return
                StringUtils.equals($arg1(), o.$arg1()) &&
                StringUtils.equals($arg2(), o.$arg2())
            ;
        }
        else
            return super.equals(that);
    }
}
