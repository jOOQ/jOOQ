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

import static org.jooq.impl.DSL.NULL;
import static org.jooq.impl.QOM.tuple;

import java.util.List;
import java.util.Map;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Function3;
import org.jooq.Name;
import org.jooq.impl.QOM.Tuple2;
import org.jooq.impl.QOM.UnmodifiableList;

/**
 * @author Lukas Eder
 */
abstract class AbstractCaseSimple<V, T, CS extends AbstractCaseSimple<V, T, CS>>
extends
    AbstractField<T>
{

    final Field<V>                         value;
    final List<Tuple2<Field<V>, Field<T>>> when;
    Field<T>                               else_;

    AbstractCaseSimple(Name name, Field<V> value, Field<V> compareValue, Field<T> result) {
        this(name, value, result.getDataType());

        when(compareValue, result);
    }

    AbstractCaseSimple(Name name, Field<V> value, Map<? extends Field<V>, ? extends Field<T>> map) {
        this(name, value, dataType(map));

        mapFields(map);
    }

    AbstractCaseSimple(Name name, Field<V> value, DataType<T> type) {
        super(name, type);

        this.value = value;
        this.when = new QueryPartList<>();
    }

    @SuppressWarnings("unchecked")
    private static final <T> DataType<T> dataType(Map<? extends Field<?>, ? extends Field<T>> map) {
        if (map.isEmpty())
            return (DataType<T>) SQLDataType.OTHER;
        else
            return map.entrySet().iterator().next().getValue().getDataType();
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    // @Override
    public final CS when(V compareValue, T result) {
        return when(Tools.field(compareValue, value), Tools.field(result));
    }

    // @Override
    public final CS when(V compareValue, Field<T> result) {
        return when(Tools.field(compareValue, value), result);
    }

    // @Override
    public final CS when(Field<V> compareValue, T result) {
        return when(compareValue, Tools.field(result));
    }

    // @Override
    public final CS when(Field<V> compareValue, Field<T> result) {
        when.add(tuple(compareValue, result));

        return (CS) this;
    }

    // @Override
    @SuppressWarnings("unchecked")
    public final CS mapValues(Map<V, T> values) {
        values.forEach((k, v) -> when(k, v));
        return (CS) this;
    }

    // @Override
    @SuppressWarnings("unchecked")
    public final CS mapFields(Map<? extends Field<V>, ? extends Field<T>> fields) {
        fields.forEach((k, v) -> when(k, v));
        return (CS) this;
    }

    // @Override
    public final Field<T> else_(T result) {
        return else_(Tools.field(result));
    }

    // @Override
    public final Field<T> else_(Field<T> result) {
        this.else_ = result;

        return this;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (when.isEmpty())
            if (else_ != null)
                ctx.visit(else_);
            else
                ctx.visit(NULL(getDataType()));
        else
            accept0(ctx);
    }

    abstract void accept0(Context<?> ctx);

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    abstract CS construct(Field<V> v, DataType<T> t);

    // @Override
    public final Function3<? super Field<V>, ? super UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>>, ? super Field<T>, ? extends CS> $constructor() {
        return (v, w, e) -> {
            CS r = construct(v, getDataType());
            w.forEach(t -> r.when(t.$1(), t.$2()));
            r.else_(e);
            return r;
        };
    }

    // @Override
    public final Field<V> $arg1() {
        return value;
    }

    // @Override
    public final CS $arg1(Field<V> newArg1) {
        return $constructor().apply(newArg1, $arg2(), $arg3());
    }

    // @Override
    public final UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>> $arg2() {
        return QOM.unmodifiable(when);
    }

    // @Override
    public final CS $arg2(UnmodifiableList<? extends Tuple2<Field<V>, Field<T>>> w) {
        return $constructor().apply($arg1(), w, $arg3());
    }

    // @Override
    public final Field<T> $arg3() {
        return else_;
    }

    // @Override
    public final CS $arg3(Field<T> e) {
        return $constructor().apply($arg1(), $arg2(), e);
    }
}
