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
 * The <code>BIT NOR AGG</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BitNorAgg<T extends Number>
extends
    AbstractAggregateFunction<T>
implements
    QOM.BitNorAgg<T>
{

    BitNorAgg(
        Field<T> value
    ) {
        super(
            false,
            N_BIT_NOR_AGG,
            Tools.nullSafeDataType(value),
            nullSafeNotNull(value, INTEGER)
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    public static final Set<SQLDialect> NO_SUPPORT_NATIVE = SQLDialect.supportedUntil(CLICKHOUSE, CUBRID, DUCKDB, FIREBIRD, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB);

    @Override
    public final void accept(Context<?> ctx) {
        if (NO_SUPPORT_NATIVE.contains(ctx.dialect()))
            ctx.visit(DSL.bitNot(fo(bitOrAgg((Field) getArguments().get(0)))));
        else
            super.accept(ctx);
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Field<T> $value() {
        return (Field<T>) getArgument(0);
    }

    @Override
    public final QOM.BitNorAgg<T> $value(Field<T> newValue) {
        return $constructor().apply(newValue);
    }

    public final Function1<? super Field<T>, ? extends QOM.BitNorAgg<T>> $constructor() {
        return (a1) -> new BitNorAgg<>(a1);
    }















    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BitNorAgg<?> o) {
            return
                Objects.equals($value(), o.$value())
            ;
        }
        else
            return super.equals(that);
    }
}
