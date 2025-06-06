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
import java.util.Set;



/**
 * The <code>ARRAY ANY MATCH</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ArrayAnyMatch<T>
extends
    AbstractCondition
implements
    QOM.ArrayAnyMatch<T>
{

    final Field<T[]>                   array;
    final Lambda1<Field<T>, Condition> predicate;

    ArrayAnyMatch(
        Field<T[]> array,
        Lambda1<Field<T>, Condition> predicate
    ) {

        this.array = nullSafeNotNull(array, ((DataType) OTHER).array());
        this.predicate = predicate;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {



            case DUCKDB:
            case H2:
            case HSQLDB:
            case POSTGRES:
            case YUGABYTEDB:
                return false;






            case CLICKHOUSE:
                return false;

            case TRINO:
                return true;

            default:
                return true;
        }
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {









            case DUCKDB:
            case H2:
            case HSQLDB:
            case POSTGRES:
            case YUGABYTEDB:
                ctx.visit(ifNotNull(array, exists(
                    select(one())
                    .from(unnest(array).as(N_T, predicate.$arg1().getUnqualifiedName()))
                    .where(predicate.$result())
                )));
                break;







            case CLICKHOUSE:
                ctx.visit(DSL.function(N_arrayExists, getDataType(), DSL.field("{0}", OTHER, predicate), array));
                break;

            case TRINO:
                ctx.visit(function(N_ANY_MATCH, BOOLEAN, array, DSL.field("{0}", OTHER, predicate)));
                break;

            default:
                ctx.visit(function(N_ARRAY_ANY_MATCH, BOOLEAN, array, DSL.field("{0}", OTHER, predicate)));
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T[]> $arg1() {
        return array;
    }

    @Override
    public final Lambda1<Field<T>, Condition> $arg2() {
        return predicate;
    }

    @Override
    public final QOM.ArrayAnyMatch<T> $arg1(Field<T[]> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.ArrayAnyMatch<T> $arg2(Lambda1<Field<T>, Condition> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<T[]>, ? super Lambda1<Field<T>, Condition>, ? extends QOM.ArrayAnyMatch<T>> $constructor() {
        return (a1, a2) -> new ArrayAnyMatch<>(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ArrayAnyMatch<?> o) {
            return
                StringUtils.equals($array(), o.$array()) &&
                StringUtils.equals($predicate(), o.$predicate())
            ;
        }
        else
            return super.equals(that);
    }
}
