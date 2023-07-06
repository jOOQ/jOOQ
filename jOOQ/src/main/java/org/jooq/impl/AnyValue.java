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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
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
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>ANY VALUE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class AnyValue<T>
extends
    AbstractAggregateFunction<T>
implements
    QOM.AnyValue<T>
{

    AnyValue(
        Field<T> field
    ) {
        super(
            false,
            N_ANY_VALUE,
            Tools.nullSafeDataType(field),
            nullSafeNotNull(field, (DataType) OTHER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    void acceptFunctionName(Context<?> ctx) {
        switch (ctx.family()) {













            case CUBRID:
            case DERBY:
            case FIREBIRD:

            case HSQLDB:
            case IGNITE:
            case MARIADB:
            case POSTGRES:
            case SQLITE:
            case YUGABYTEDB:
                ctx.visit(N_MIN);
                break;

            case TRINO:
                ctx.visit(N_ARBITRARY);
                break;





























            default:
                super.acceptFunctionName(ctx);
                break;
        }
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> $field() {
        return (Field<T>) getArguments().get(0);
    }

    @Override
    public final QOM.AnyValue<T> $field(Field<T> newValue) {
        return $constructor().apply(newValue);
    }

    public final Function1<? super Field<T>, ? extends QOM.AnyValue<T>> $constructor() {
        return (a1) -> new AnyValue<>(a1);
    }























    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.AnyValue<?> o) {
            return
                StringUtils.equals($field(), o.$field())
            ;
        }
        else
            return super.equals(that);
    }
}
