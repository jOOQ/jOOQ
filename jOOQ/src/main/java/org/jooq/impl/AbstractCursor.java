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

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jooq.Configuration;
import org.jooq.Cursor;
import org.jooq.Record;
import org.jooq.RecordHandler;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
abstract class AbstractCursor<R extends Record> extends AbstractResult<R> implements Cursor<R> {

    AbstractCursor(Configuration configuration, AbstractRow<R> row) {
        super(configuration, row);
    }

    @Override
    public final Stream<R> stream() {
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                iterator(),
                Spliterator.ORDERED | Spliterator.NONNULL
            ),
            false
        ).onClose(() -> close());
    }

    @Override
    public final <X, A> X collect(Collector<? super R, A, X> collector) {
        return stream().collect(collector);
    }

    @Override
    public final boolean hasNext() {
        return iterator().hasNext();
    }

    @Override
    public final Result<R> fetch() {
        return fetch(Integer.MAX_VALUE);
    }

    @Override
    @Deprecated
    public final R fetchOne() {
        return fetchNext();
    }

    @Override
    @Deprecated
    public final <E> E fetchOne(RecordMapper<? super R, E> mapper) {
        return fetchNext(mapper);
    }

    @Override
    @Deprecated
    public final <H extends RecordHandler<? super R>> H fetchOneInto(H handler) {
        return fetchNextInto(handler);
    }

    @Override
    @Deprecated
    public final <Z extends Record> Z fetchOneInto(Table<Z> table) {
        return fetchNextInto(table);
    }

    @Override
    @Deprecated
    public final <E> E fetchOneInto(Class<? extends E> type) {
        return fetchNextInto(type);
    }

    @Override
    public final R fetchNext() {
        Result<R> result = fetch(1);

        if (result.size() == 1)
            return result.get(0);

        return null;
    }

    @Override
    @Deprecated
    public final Optional<R> fetchOptional() {
        return fetchNextOptional();
    }

    @Override
    @Deprecated
    public final <E> Optional<E> fetchOptional(RecordMapper<? super R, E> mapper) {
        return fetchNextOptional(mapper);
    }

    @Override
    @Deprecated
    public final <E> Optional<E> fetchOptionalInto(Class<? extends E> type) {
        return fetchNextOptionalInto(type);
    }

    @Override
    @Deprecated
    public final <Z extends Record> Optional<Z> fetchOptionalInto(Table<Z> table) {
        return fetchNextOptionalInto(table);
    }

    @Override
    public final Optional<R> fetchNextOptional() {
        return Optional.ofNullable(fetchNext());
    }

    @Override
    public final <E> Optional<E> fetchNextOptional(RecordMapper<? super R, E> mapper) {
        return Optional.ofNullable(fetchNext(mapper));
    }

    @Override
    public final <E> Optional<E> fetchNextOptionalInto(Class<? extends E> type) {
        return Optional.ofNullable(fetchNextInto(type));
    }

    @Override
    public final <Z extends Record> Optional<Z> fetchNextOptionalInto(Table<Z> table) {
        return Optional.ofNullable(fetchNextInto(table));
    }

    @Override
    public final Result<R> fetch(int number) {
        return fetchNext(number);
    }

    @Override
    public final <H extends RecordHandler<? super R>> H fetchNextInto(H handler) {
        handler.next(fetchNext());
        return handler;
    }

    @Override
    public final <H extends RecordHandler<? super R>> H fetchInto(H handler) {
        forEach(handler);
        return handler;
    }

    @Override
    public final <E> E fetchNext(RecordMapper<? super R, E> mapper) {
        R record = fetchNext();
        return record == null ? null : mapper.map(record);
    }

    @Override
    public final <E> List<E> fetch(RecordMapper<? super R, E> mapper) {
        return fetch().map(mapper);
    }

    @Override
    public final <E> E fetchNextInto(Class<? extends E> clazz) {
        R record = fetchNext();
        return record == null ? null : record.into(clazz);
    }

    @Override
    public final <E> List<E> fetchInto(Class<? extends E> clazz) {
        return fetch().into(clazz);
    }

    @Override
    public final <Z extends Record> Z fetchNextInto(Table<Z> table) {
        return fetchNext().into(table);
    }

    @Override
    public final <Z extends Record> Result<Z> fetchInto(Table<Z> table) {
        return fetch().into(table);
    }
}
