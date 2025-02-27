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
import org.jooq.impl.QOM.SampleMethod;
import org.jooq.impl.QOM.SampleSizeType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;



/**
 * The <code>TABLESAMPLE BERNOULLI</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class TableSample<R extends Record>
extends
    AbstractDelegatingTable<R>
implements
    QOM.TableSample<R>,
    TableSampleRowsStep<R>,
    TableSampleRepeatableStep<R>
{

    final Table<R>                table;
    final Field<? extends Number> size;
    final SampleMethod            method;
          SampleSizeType          sizeType;
          Field<? extends Number> seed;

    TableSample(
        Table<R> table,
        Field<? extends Number> size,
        SampleMethod method
    ) {
        this(
            table,
            size,
            method,
            null,
            null
        );
    }

    TableSample(
        Table<R> table,
        Field<? extends Number> size,
        SampleMethod method,
        SampleSizeType sizeType,
        Field<? extends Number> seed
    ) {
        super(
            (AbstractTable<R>) table
        );

        this.table = table;
        this.size = nullSafeNotNull(size, INTEGER);
        this.method = method;
        this.sizeType = sizeType;
        this.seed = seed;
    }

    @Override
    final <R extends Record> TableSample<R> construct(AbstractTable<R> newDelegate) {
        return new TableSample<>(newDelegate, size, method, sizeType, seed);
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final TableSample<R> rows() {
        this.sizeType = SampleSizeType.ROWS;
        return this;
    }

    @Override
    public final TableSample<R> percent() {
        this.sizeType = SampleSizeType.PERCENT;
        return this;
    }

    @Override
    public final TableSample<R> repeatable(int seed) {
        return repeatable(Tools.field(seed));
    }

    @Override
    public final TableSample<R> repeatable(Field<? extends Number> seed) {
        this.seed = seed;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(table).sql(' ');

        switch (ctx.family()) {






            case DUCKDB:
                ctx.visit(K_USING).sql(' ').visit(K_SAMPLE).sql(' ');
                break;

            default:
                ctx.visit(K_TABLESAMPLE).sql(' ');
                break;
        }

        switch (ctx.family()) {


















            case DUCKDB:
                if (method == SampleMethod.BERNOULLI)
                    ctx.visit(method.keyword).sql(' ');

                break;

            default:
                ctx.visit(method.keyword).sql(' ');
                break;
        }

        switch (ctx.family()) {




            case POSTGRES:
            case TRINO:
                ctx.sql('(').visit(size).sql(')');
                break;












            default:
                ctx.sql('(').visit(size);

                if (sizeType != null)
                    ctx.sql(' ').visit(sizeType.keyword);

                ctx.sql(')');
                break;
        }

        if (seed != null) {
            switch (ctx.family()) {

                    ctx.sql(' ').visit(K_SEED).sql(" (").visit(seed).sql(')');
                    break;

                default:
                    ctx.sql(' ').visit(K_REPEATABLE).sql(" (").visit(seed).sql(')');
                    break;
            }
        }
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<R> $arg1() {
        return table;
    }

    @Override
    public final Field<? extends Number> $arg2() {
        return size;
    }

    @Override
    public final SampleMethod $arg3() {
        return method;
    }

    @Override
    public final SampleSizeType $arg4() {
        return sizeType;
    }

    @Override
    public final Field<? extends Number> $arg5() {
        return seed;
    }

    @Override
    public final QOM.TableSample<R> $arg1(Table<R> newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3(), $arg4(), $arg5());
    }

    @Override
    public final QOM.TableSample<R> $arg2(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3(), $arg4(), $arg5());
    }

    @Override
    public final QOM.TableSample<R> $arg3(SampleMethod newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue, $arg4(), $arg5());
    }

    @Override
    public final QOM.TableSample<R> $arg4(SampleSizeType newValue) {
        return $constructor().apply($arg1(), $arg2(), $arg3(), newValue, $arg5());
    }

    @Override
    public final QOM.TableSample<R> $arg5(Field<? extends Number> newValue) {
        return $constructor().apply($arg1(), $arg2(), $arg3(), $arg4(), newValue);
    }

    @Override
    public final Function5<? super Table<R>, ? super Field<? extends Number>, ? super SampleMethod, ? super SampleSizeType, ? super Field<? extends Number>, ? extends QOM.TableSample<R>> $constructor() {
        return (a1, a2, a3, a4, a5) -> new TableSample<>(a1, a2, a3, a4, a5);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.TableSample<?> o) {
            return
                StringUtils.equals($table(), o.$table()) &&
                StringUtils.equals($size(), o.$size()) &&
                StringUtils.equals($method(), o.$method()) &&
                StringUtils.equals($sizeType(), o.$sizeType()) &&
                StringUtils.equals($seed(), o.$seed())
            ;
        }
        else
            return super.equals(that);
    }
}
