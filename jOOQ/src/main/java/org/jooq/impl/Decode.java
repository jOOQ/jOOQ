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

// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.Names.N_DECODE;
import static org.jooq.impl.Names.N_DECODE_ORACLE;
import static org.jooq.impl.Names.N_MAP;
import static org.jooq.impl.Tools.EMPTY_FIELD;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
// ...
import org.jooq.QueryPart;
// ...
import org.jooq.SQLDialect;
// ...

/**
 * @author Lukas Eder
 */
final class Decode<V, T>
extends
    AbstractCaseSimple<V, T, Decode<V, T>>
implements
    QOM.Decode<V, T>
{
    private static final Set<SQLDialect> EMULATE_DECODE_ORACLE = SQLDialect.supportedBy(MARIADB);
    private static final Set<SQLDialect> EMULATE_DISTINCT      = SQLDialect.supportedUntil(CUBRID, DERBY, FIREBIRD, HSQLDB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB);





    @SuppressWarnings("unchecked")
    Decode(Field<V> field, Field<V> search, Field<T> result, Field<?>[] more) {
        super(N_DECODE, field, result.getDataType());

        when(search, result);
        if (more.length > 1)
            for (int i = 0; i + 1 < more.length; i += 2)
                when((Field<V>) more[i], (Field<T>) more[i + 1]);

        if (more.length % 2 != 0)
            else_((Field<T>) more[more.length - 1]);
    }

    Decode(Field<V> field, DataType<T> type) {
        super(N_DECODE, field, type);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    final void accept0(Context<?> ctx) {
        if (EMULATE_DISTINCT.contains(ctx.dialect())) {
            ctx.visit(Tools.derivedTableIf(ctx, when.size() > 1, value, f -> {
                CaseSearched<T> c = new CaseSearched<>(getDataType());
                when.forEach(t -> c.when(f.isNotDistinctFrom(t.$1()), t.$2()));

                if (else_ == null)
                    return c;
                else
                    return c.else_(else_);
            }));
        }






        else if (EMULATE_DECODE_ORACLE.contains(ctx.dialect()))
            ctx.visit(function(N_DECODE_ORACLE, getDataType(), args()));
        else
            ctx.visit(function(N_DECODE, getDataType(), args()));
    }

    final Field<?>[] args() {
        List<Field<?>> result = new ArrayList<>();

        result.add(value);
        when.forEach(t -> {
            result.add(t.$1());
            result.add(t.$2());
        });

        if (else_ != null)
            result.add(else_);

        return result.toArray(EMPTY_FIELD);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    final Decode<V, T> construct(Field<V> v, DataType<T> t) {
        return new Decode<>(v, t);
    }














}
