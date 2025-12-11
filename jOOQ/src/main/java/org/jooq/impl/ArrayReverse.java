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
 * The <code>ARRAY REVERSE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class ArrayReverse<T>
extends
    AbstractField<T[]>
implements
    QOM.ArrayReverse<T>
{

    final Field<T[]> array;

    ArrayReverse(
        Field<T[]> array
    ) {
        super(
            N_ARRAY_REVERSE,
            allNotNull((DataType) dataType(((DataType) OTHER).array(), array, false), array)
        );

        this.array = nullSafeNotNull(array, ((DataType) OTHER).array());
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {
        switch (ctx.family()) {
            case POSTGRES:
                return false;





            case DUCKDB:
            case H2:
            case HSQLDB:
            case YUGABYTEDB:
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






            case POSTGRES: {





                ctx.visit(function(N_ARRAY_REVERSE, getDataType(), array));
                break;
            }





            case DUCKDB:
            case H2:
            case HSQLDB:
            case YUGABYTEDB:
                ctx.visit(arrayAggEmulation(ctx));
                break;

            case TRINO:
                ctx.visit(function(N_REVERSE, getDataType(), array));
                break;

            default:
                ctx.visit(function(N_ARRAY_REVERSE, getDataType(), array));
                break;
        }
    }












    private final Field<?> arrayAggEmulation(Context<?> ctx) {
        return DSL
            .when(array.isNull(), array)
            .when(cardinality(array).eq(0), array)
            .else_(
                DSL.select(arrayAgg((Field<T>) DSL.field(N_E)).orderBy(DSL.field(N_O).desc()))
                .from(unnest(array).withOrdinality().as(N_T, N_E, N_O))
            );
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<T[]> $arg1() {
        return array;
    }

    @Override
    public final QOM.ArrayReverse<T> $arg1(Field<T[]> newValue) {
        return $constructor().apply(newValue);
    }

    @Override
    public final Function1<? super Field<T[]>, ? extends QOM.ArrayReverse<T>> $constructor() {
        return (a1) -> new ArrayReverse<>(a1);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.ArrayReverse<?> o) {
            return
                Objects.equals($array(), o.$array())
            ;
        }
        else
            return super.equals(that);
    }
}
