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

import static java.util.Collections.emptySet;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.jooq.Fields;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.ResultQuery;

/**
 * An array collector that delays the instantiation of the array type until it
 * is known.
 * <p>
 * {@link Records#intoArray(Object[])} and similar collectors require an eager
 * specification of the resulting array type. In some cases, this array type
 * cannot be known until a query is executed (e.g in the case of a plain SQL
 * {@link ResultQuery}). In that case, we rely
 * {@link ResultQuery#collect(Collector)} to inject a {@link Fields}
 * implementation prior to running a collection.
 *
 * @author Lukas Eder
 */
final class DelayedArrayCollector<R extends Record, E> implements Collector<R, List<E>, E[]> {

    private final Function<Fields, E[]>          array;
    private final Collector<R, List<E>, List<E>> delegate;
    Fields                                       fields;

    @SuppressWarnings("unchecked")
    DelayedArrayCollector(Function<Fields, E[]> array, Function<R, E> mapper) {
        this.array = array;
        this.delegate = (Collector<R, List<E>, List<E>>) Records.intoList(mapper);
    }

    static final <F extends Fields> F patch(Collector<?, ?, ?> collector, F fields) {
        if (collector instanceof DelayedArrayCollector)
            ((DelayedArrayCollector<?, ?>) collector).fields = fields;

        return fields;
    }

    @Override
    public final Supplier<List<E>> supplier() {
        return delegate.supplier();
    }

    @Override
    public final BiConsumer<List<E>, R> accumulator() {
        return delegate.accumulator();
    }

    @Override
    public final BinaryOperator<List<E>> combiner() {
        return delegate.combiner();
    }

    @Override
    public final Function<List<E>, E[]> finisher() {
        return delegate.finisher().andThen(l -> l.toArray(array.apply(fields)));
    }

    @Override
    public final Set<Characteristics> characteristics() {
        return emptySet();
    }
}
