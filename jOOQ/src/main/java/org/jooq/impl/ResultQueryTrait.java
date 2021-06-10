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

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.jooq.Records.intoArray;
import static org.jooq.Records.intoGroups;
import static org.jooq.Records.intoList;
import static org.jooq.Records.intoMap;
import static org.jooq.Records.intoResultGroups;
import static org.jooq.Records.intoSet;
import static org.jooq.conf.SettingsTools.fetchIntermediateResult;
import static org.jooq.impl.DelayedArrayCollector.patch;
import static org.jooq.impl.Tools.blocking;
import static org.jooq.impl.Tools.indexOrFail;
import static org.jooq.tools.jdbc.JDBCUtils.safeClose;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Cursor;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.QueryPartInternal;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Record11;
import org.jooq.Record12;
import org.jooq.Record13;
import org.jooq.Record14;
import org.jooq.Record15;
import org.jooq.Record16;
import org.jooq.Record17;
import org.jooq.Record18;
import org.jooq.Record19;
import org.jooq.Record2;
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
import org.jooq.RecordHandler;
import org.jooq.RecordMapper;
import org.jooq.Records;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.Results;
import org.jooq.Row;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.R2DBC.BlockingRecordSubscription;
import org.jooq.impl.R2DBC.QuerySubscription;
import org.jooq.impl.R2DBC.ResultSubscriber;
import org.jooq.tools.jdbc.JDBCUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reactivestreams.Subscriber;

import io.r2dbc.spi.ConnectionFactory;

/**
 * All the common fetch logic of a {@link ResultQuery}.
 *
 * @author Lukas Eder
 */
interface ResultQueryTrait<R extends Record> extends QueryPartInternal, ResultQuery<R>, Mappable<R>, FieldsTrait {

    @Override
    default ResultQuery<Record> coerce(Field<?>... fields) {
        return coerce(Arrays.asList(fields));
    }



    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1> ResultQuery<Record1<T1>> coerce(Field<T1> field1) {
        return (ResultQuery) coerce(new Field[] { field1 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2> ResultQuery<Record2<T1, T2>> coerce(Field<T1> field1, Field<T2> field2) {
        return (ResultQuery) coerce(new Field[] { field1, field2 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3> ResultQuery<Record3<T1, T2, T3>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4> ResultQuery<Record4<T1, T2, T3, T4>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5> ResultQuery<Record5<T1, T2, T3, T4, T5>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6> ResultQuery<Record6<T1, T2, T3, T4, T5, T6>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7> ResultQuery<Record7<T1, T2, T3, T4, T5, T6, T7>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8> ResultQuery<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9> ResultQuery<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> ResultQuery<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> ResultQuery<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> ResultQuery<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> ResultQuery<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> ResultQuery<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ResultQuery<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ResultQuery<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ResultQuery<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ResultQuery<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ResultQuery<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ResultQuery<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ResultQuery<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ResultQuery<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return (ResultQuery) coerce(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



    @Override
    default Cursor<R> fetchLazy() throws DataAccessException {
        return new ResultAsCursor<R>(fetch());
    }

    @Override
    default Results fetchMany() throws DataAccessException {
        throw new DataAccessException("Attempt to call fetchMany() on " + getClass());
    }

    default Cursor<R> fetchLazyNonAutoClosing() {
        return fetchLazy();
    }

    @Override
    default ResultSet fetchResultSet() {
        if (fetchIntermediateResult(Tools.configuration(this)))
            return fetch().intoResultSet();
        else
            return fetchLazy().resultSet();
    }

    @Override
    default Iterator<R> iterator() {
        return fetch().iterator();
    }

    @Override
    default CompletionStage<Result<R>> fetchAsync() {
        return fetchAsync(Tools.configuration(this).executorProvider().provide());
    }

    @Override
    default CompletionStage<Result<R>> fetchAsync(Executor executor) {
        return ExecutorProviderCompletionStage.of(CompletableFuture.supplyAsync(blocking(this::fetch), executor), () -> executor);
    }

    @Override
    default Stream<R> fetchStream() {
        if (fetchIntermediateResult(Tools.configuration(this)))
            return fetch().stream();

        // [#11895] Don't use the Stream.of(1).flatMap(i -> fetchLazy().stream())
        //          trick, because flatMap() will consume the entire result set
        AtomicReference<Cursor<R>> r = new AtomicReference<>();

        // [#11895] Don't use the Stream.of(1).flatMap(i -> fetchLazy().stream())
        //          trick, because flatMap() will consume the entire result set
        return StreamSupport.stream(
            () -> {
                Cursor<R> c = fetchLazy();
                r.set(c);
                return c.spliterator();
            },
            Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED,
            false
        ).onClose(() -> {
            safeClose(r.get());
        });
    }

    @Override
    default <E> Stream<E> fetchStreamInto(Class<? extends E> type) {
        return fetchStream().map(mapper(Tools.configuration(this), type));
    }

    @Override
    default <Z extends Record> Stream<Z> fetchStreamInto(Table<Z> table) {
        return fetchStream().map(mapper(table));
    }

    @Override
    default Stream<R> stream() {
        return fetchStream();
    }

    @Override
    default <X, A> X collect(Collector<? super R, A, X> collector) {
        if (fetchIntermediateResult(Tools.configuration(this)))
            return patch(collector, fetch()).collect(collector);

        try (Cursor<R> c = fetchLazyNonAutoClosing()) {
            return patch(collector, c).collect(collector);
        }
    }

    @Override
    default void subscribe(Subscriber<? super R> subscriber) {
        ConnectionFactory cf = configuration().connectionFactory();

        if (!(cf instanceof NoConnectionFactory))
            subscriber.onSubscribe(new QuerySubscription<>(this, subscriber, ResultSubscriber::new));
        else
            subscriber.onSubscribe(new BlockingRecordSubscription<>(this, subscriber));
    }

    @Override
    default <T> List<T> fetch(Field<T> field) {
        return collect(intoList(mapper(field)));
    }

    @Override
    default <U> List<U> fetch(Field<?> field, Class<? extends U> type) {
        return collect(intoList(mapper(field, Tools.configuration(this), type)));
    }

    @Override
    default <T, U> List<U> fetch(Field<T> field, Converter<? super T, ? extends U> converter) {
        return collect(intoList(mapper(field, converter)));
    }

    @Override
    default List<?> fetch(int fieldIndex) {
        return collect(intoList(mapper(fieldIndex)));
    }

    @Override
    default <U> List<U> fetch(int fieldIndex, Class<? extends U> type) {
        return collect(intoList(mapper(fieldIndex, Tools.configuration(this), type)));
    }

    @Override
    default <U> List<U> fetch(int fieldIndex, Converter<?, ? extends U> converter) {
        return collect(intoList(mapper(fieldIndex, converter)));
    }

    @Override
    default List<?> fetch(String fieldName) {
        return collect(intoList(mapper(fieldName)));
    }

    @Override
    default <U> List<U> fetch(String fieldName, Class<? extends U> type) {
        return collect(intoList(mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    default <U> List<U> fetch(String fieldName, Converter<?, ? extends U> converter) {
        return collect(intoList(mapper(fieldName, converter)));
    }

    @Override
    default List<?> fetch(Name fieldName) {
        return collect(intoList(mapper(fieldName)));
    }

    @Override
    default <U> List<U> fetch(Name fieldName, Class<? extends U> type) {
        return collect(intoList(mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    default <U> List<U> fetch(Name fieldName, Converter<?, ? extends U> converter) {
        return collect(intoList(mapper(fieldName, converter)));
    }

    @Override
    default <T> T fetchOne(Field<T> field) {
        R record = fetchOne();
        return record == null ? null : record.get(field);
    }

    @Override
    default <U> U fetchOne(Field<?> field, Class<? extends U> type) {
        R record = fetchOne();
        return record == null ? null : record.get(field, type);
    }

    @Override
    default <T, U> U fetchOne(Field<T> field, Converter<? super T, ? extends U> converter) {
        R record = fetchOne();
        return record == null ? null : record.get(field, converter);
    }

    @Override
    default Object fetchOne(int fieldIndex) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldIndex);
    }

    @Override
    default <U> U fetchOne(int fieldIndex, Class<? extends U> type) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldIndex, type);
    }

    @Override
    default <U> U fetchOne(int fieldIndex, Converter<?, ? extends U> converter) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldIndex, converter);
    }

    @Override
    default Object fetchOne(String fieldName) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    default <U> U fetchOne(String fieldName, Class<? extends U> type) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName, type);
    }

    @Override
    default <U> U fetchOne(String fieldName, Converter<?, ? extends U> converter) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName, converter);
    }

    @Override
    default Object fetchOne(Name fieldName) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    default <U> U fetchOne(Name fieldName, Class<? extends U> type) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName, type);
    }

    @Override
    default <U> U fetchOne(Name fieldName, Converter<?, ? extends U> converter) {
        R record = fetchOne();
        return record == null ? null : record.get(fieldName, converter);
    }

    @Override
    default R fetchOne() {
        return Tools.fetchOne(fetchLazyNonAutoClosing(), hasLimit1());
    }

    @Override
    default <E> E fetchOne(RecordMapper<? super R, E> mapper) {
        R record = fetchOne();
        return record == null ? null : mapper.map(record);
    }

    @Override
    default Map<String, Object> fetchOneMap() {
        R record = fetchOne();
        return record == null ? null : record.intoMap();
    }

    @Override
    default Object[] fetchOneArray() {
        R record = fetchOne();
        return record == null ? null : record.intoArray();
    }

    @Override
    default <E> E fetchOneInto(Class<? extends E> type) {
        R record = fetchOne();
        return record == null ? null : record.into(type);
    }

    @Override
    default <Z extends Record> Z fetchOneInto(Table<Z> table) {
        R record = fetchOne();
        return record == null ? null : record.into(table);
    }

    @Override
    default <T> T fetchSingle(Field<T> field) {
        return fetchSingle().get(field);
    }

    @Override
    default <U> U fetchSingle(Field<?> field, Class<? extends U> type) {
        return fetchSingle().get(field, type);
    }

    @Override
    default <T, U> U fetchSingle(Field<T> field, Converter<? super T, ? extends U> converter) {
        return fetchSingle().get(field, converter);
    }

    @Override
    default Object fetchSingle(int fieldIndex) {
        return fetchSingle().get(fieldIndex);
    }

    @Override
    default <U> U fetchSingle(int fieldIndex, Class<? extends U> type) {
        return fetchSingle().get(fieldIndex, type);
    }

    @Override
    default <U> U fetchSingle(int fieldIndex, Converter<?, ? extends U> converter) {
        return fetchSingle().get(fieldIndex, converter);
    }

    @Override
    default Object fetchSingle(String fieldName) {
        return fetchSingle().get(fieldName);
    }

    @Override
    default <U> U fetchSingle(String fieldName, Class<? extends U> type) {
        return fetchSingle().get(fieldName, type);
    }

    @Override
    default <U> U fetchSingle(String fieldName, Converter<?, ? extends U> converter) {
        return fetchSingle().get(fieldName, converter);
    }

    @Override
    default Object fetchSingle(Name fieldName) {
        return fetchSingle().get(fieldName);
    }

    @Override
    default <U> U fetchSingle(Name fieldName, Class<? extends U> type) {
        return fetchSingle().get(fieldName, type);
    }

    @Override
    default <U> U fetchSingle(Name fieldName, Converter<?, ? extends U> converter) {
        return fetchSingle().get(fieldName, converter);
    }

    @Override
    default R fetchSingle() {
        return Tools.fetchSingle(fetchLazyNonAutoClosing(), hasLimit1());
    }

    @Override
    default <E> E fetchSingle(RecordMapper<? super R, E> mapper) {
        return mapper.map(fetchSingle());
    }

    @Override
    default Map<String, Object> fetchSingleMap() {
        return fetchSingle().intoMap();
    }

    @Override
    default Object[] fetchSingleArray() {
        return fetchSingle().intoArray();
    }

    @Override
    default <E> E fetchSingleInto(Class<? extends E> type) {
        return fetchSingle().into(type);
    }

    @Override
    default <Z extends Record> Z fetchSingleInto(Table<Z> table) {
        return fetchSingle().into(table);
    }

    @Override
    default <T> Optional<T> fetchOptional(Field<T> field) {
        return Optional.ofNullable(fetchOne(field));
    }

    @Override
    default <U> Optional<U> fetchOptional(Field<?> field, Class<? extends U> type) {
        return Optional.ofNullable(fetchOne(field, type));
    }

    @Override
    default <T, U> Optional<U> fetchOptional(Field<T> field, Converter<? super T, ? extends U> converter) {
        return Optional.ofNullable(fetchOne(field, converter));
    }

    @Override
    default Optional<?> fetchOptional(int fieldIndex) {
        return Optional.ofNullable(fetchOne(fieldIndex));
    }

    @Override
    default <U> Optional<U> fetchOptional(int fieldIndex, Class<? extends U> type) {
        return Optional.ofNullable(fetchOne(fieldIndex, type));
    }

    @Override
    default <U> Optional<U> fetchOptional(int fieldIndex, Converter<?, ? extends U> converter) {
        return Optional.ofNullable(fetchOne(fieldIndex, converter));
    }

    @Override
    default Optional<?> fetchOptional(String fieldName) {
        return Optional.ofNullable(fetchOne(fieldName));
    }

    @Override
    default <U> Optional<U> fetchOptional(String fieldName, Class<? extends U> type) {
        return Optional.ofNullable(fetchOne(fieldName, type));
    }

    @Override
    default <U> Optional<U> fetchOptional(String fieldName, Converter<?, ? extends U> converter) {
        return Optional.ofNullable(fetchOne(fieldName, converter));
    }

    @Override
    default Optional<?> fetchOptional(Name fieldName) {
        return Optional.ofNullable(fetchOne(fieldName));
    }

    @Override
    default <U> Optional<U> fetchOptional(Name fieldName, Class<? extends U> type) {
        return Optional.ofNullable(fetchOne(fieldName, type));
    }

    @Override
    default <U> Optional<U> fetchOptional(Name fieldName, Converter<?, ? extends U> converter) {
        return Optional.ofNullable(fetchOne(fieldName, converter));
    }

    @Override
    default Optional<R> fetchOptional() {
        return Optional.ofNullable(fetchOne());
    }

    @Override
    default <E> Optional<E> fetchOptional(RecordMapper<? super R, E> mapper) {
        return Optional.ofNullable(fetchOne(mapper));
    }

    @Override
    default Optional<Map<String, Object>> fetchOptionalMap() {
        return Optional.ofNullable(fetchOneMap());
    }

    @Override
    default Optional<Object[]> fetchOptionalArray() {
        return Optional.ofNullable(fetchOneArray());
    }

    @Override
    default <E> Optional<E> fetchOptionalInto(Class<? extends E> type) {
        return Optional.ofNullable(fetchOneInto(type));
    }

    @Override
    default <Z extends Record> Optional<Z> fetchOptionalInto(Table<Z> table) {
        return Optional.ofNullable(fetchOneInto(table));
    }

    @Override
    default <T> T fetchAny(Field<T> field) {
        R record = fetchAny();
        return record == null ? null : record.get(field);
    }

    @Override
    default <U> U fetchAny(Field<?> field, Class<? extends U> type) {
        R record = fetchAny();
        return record == null ? null : record.get(field, type);
    }

    @Override
    default <T, U> U fetchAny(Field<T> field, Converter<? super T, ? extends U> converter) {
        R record = fetchAny();
        return record == null ? null : record.get(field, converter);
    }

    @Override
    default Object fetchAny(int fieldIndex) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldIndex);
    }

    @Override
    default <U> U fetchAny(int fieldIndex, Class<? extends U> type) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldIndex, type);
    }

    @Override
    default <U> U fetchAny(int fieldIndex, Converter<?, ? extends U> converter) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldIndex, converter);
    }

    @Override
    default Object fetchAny(String fieldName) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    default <U> U fetchAny(String fieldName, Class<? extends U> type) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName, type);
    }

    @Override
    default <U> U fetchAny(String fieldName, Converter<?, ? extends U> converter) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName, converter);
    }

    @Override
    default Object fetchAny(Name fieldName) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName);
    }

    @Override
    default <U> U fetchAny(Name fieldName, Class<? extends U> type) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName, type);
    }

    @Override
    default <U> U fetchAny(Name fieldName, Converter<?, ? extends U> converter) {
        R record = fetchAny();
        return record == null ? null : record.get(fieldName, converter);
    }

    @Override
    default R fetchAny() {
        try (Cursor<R> c = fetchLazyNonAutoClosing()) {
            return c.fetchNext();
        }
    }

    @Override
    default <E> E fetchAny(RecordMapper<? super R, E> mapper) {
        R record = fetchAny();
        return record == null ? null : mapper.map(record);
    }

    @Override
    default Map<String, Object> fetchAnyMap() {
        R record = fetchAny();
        return record == null ? null : record.intoMap();
    }

    @Override
    default Object[] fetchAnyArray() {
        R record = fetchAny();
        return record == null ? null : record.intoArray();
    }

    @Override
    default <E> E fetchAnyInto(Class<? extends E> type) {
        R record = fetchAny();
        return record == null ? null : record.into(type);
    }

    @Override
    default <Z extends Record> Z fetchAnyInto(Table<Z> table) {
        R record = fetchAny();
        return record == null ? null : record.into(table);
    }

    @Override
    default <K> Map<K, R> fetchMap(Field<K> key) {
        return collect(intoMap(mapper(key)));
    }

    @Override
    default Map<?, R> fetchMap(int keyFieldIndex) {
        return collect(intoMap(mapper(keyFieldIndex)));
    }

    @Override
    default Map<?, R> fetchMap(String keyFieldName) {
        return collect(intoMap(mapper(keyFieldName)));
    }

    @Override
    default Map<?, R> fetchMap(Name keyFieldName) {
        return collect(intoMap(mapper(keyFieldName)));
    }

    @Override
    default <K, V> Map<K, V> fetchMap(Field<K> key, Field<V> value) {
        return collect(intoMap(mapper(key), mapper(value)));
    }

    @Override
    default Map<?, ?> fetchMap(int keyFieldIndex, int valueFieldIndex) {
        return collect(intoMap(mapper(keyFieldIndex), mapper(valueFieldIndex)));
    }

    @Override
    default Map<?, ?> fetchMap(String keyFieldName, String valueFieldName) {
        return collect(intoMap(mapper(keyFieldName), mapper(valueFieldName)));
    }

    @Override
    default Map<?, ?> fetchMap(Name keyFieldName, Name valueFieldName) {
        return collect(intoMap(mapper(keyFieldName), mapper(valueFieldName)));
    }

    @Override
    default <K, E> Map<K, E> fetchMap(Field<K> key, Class<? extends E> type) {
        return collect(intoMap(mapper(key), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<?, E> fetchMap(int keyFieldIndex, Class<? extends E> type) {
        return collect(intoMap(mapper(keyFieldIndex), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<?, E> fetchMap(String keyFieldName, Class<? extends E> type) {
        return collect(intoMap(mapper(keyFieldName), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<?, E> fetchMap(Name keyFieldName, Class<? extends E> type) {
        return collect(intoMap(mapper(keyFieldName), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <K, E> Map<K, E> fetchMap(Field<K> key, RecordMapper<? super R, E> mapper) {
        return collect(intoMap(mapper(key), mapper));
    }

    @Override
    default <E> Map<?, E> fetchMap(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        return collect(intoMap(mapper(keyFieldIndex), mapper));
    }

    @Override
    default <E> Map<?, E> fetchMap(String keyFieldName, RecordMapper<? super R, E> mapper) {
        return collect(intoMap(mapper(keyFieldName), mapper));
    }

    @Override
    default <E> Map<?, E> fetchMap(Name keyFieldName, RecordMapper<? super R, E> mapper) {
        return collect(intoMap(mapper(keyFieldName), mapper));
    }

    @Override
    default Map<Record, R> fetchMap(Field<?>[] keys) {
        return collect(intoMap(mapper(keys)));
    }

    @Override
    default Map<Record, R> fetchMap(int[] keyFieldIndexes) {
        return collect(intoMap(mapper(keyFieldIndexes)));
    }

    @Override
    default Map<Record, R> fetchMap(String[] keyFieldNames) {
        return collect(intoMap(mapper(keyFieldNames)));
    }

    @Override
    default Map<Record, R> fetchMap(Name[] keyFieldNames) {
        return collect(intoMap(mapper(keyFieldNames)));
    }

    @Override
    default Map<Record, Record> fetchMap(Field<?>[] keys, Field<?>[] values) {
        return collect(intoMap(mapper(keys), mapper(values)));
    }

    @Override
    default Map<Record, Record> fetchMap(int[] keyFieldIndexes, int[] valueFieldIndexes) {
        return collect(intoMap(mapper(keyFieldIndexes), mapper(valueFieldIndexes)));
    }

    @Override
    default Map<Record, Record> fetchMap(String[] keyFieldNames, String[] valueFieldNames) {
        return collect(intoMap(mapper(keyFieldNames), mapper(valueFieldNames)));
    }

    @Override
    default Map<Record, Record> fetchMap(Name[] keyFieldNames, Name[] valueFieldNames) {
        return collect(intoMap(mapper(keyFieldNames), mapper(valueFieldNames)));
    }

    @Override
    default <E> Map<List<?>, E> fetchMap(Field<?>[] keys, Class<? extends E> type) {
        return collect(intoMap(mapper(keys).andThen(Record::intoList), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<List<?>, E> fetchMap(int[] keyFieldIndexes, Class<? extends E> type) {
        return collect(intoMap(mapper(keyFieldIndexes).andThen(Record::intoList), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<List<?>, E> fetchMap(String[] keyFieldNames, Class<? extends E> type) {
        return collect(intoMap(mapper(keyFieldNames).andThen(Record::intoList), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<List<?>, E> fetchMap(Name[] keyFieldNames, Class<? extends E> type) {
        return collect(intoMap(mapper(keyFieldNames).andThen(Record::intoList), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<List<?>, E> fetchMap(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        return collect(intoMap(mapper(keys).andThen(Record::intoList), mapper));
    }

    @Override
    default <E> Map<List<?>, E> fetchMap(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) {
        return collect(intoMap(mapper(keyFieldIndexes).andThen(Record::intoList), mapper));
    }

    @Override
    default <E> Map<List<?>, E> fetchMap(String[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return collect(intoMap(mapper(keyFieldNames).andThen(Record::intoList), mapper));
    }

    @Override
    default <E> Map<List<?>, E> fetchMap(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return collect(intoMap(mapper(keyFieldNames).andThen(Record::intoList), mapper));
    }

    @Override
    default <K> Map<K, R> fetchMap(Class<? extends K> keyType) {
        return collect(intoMap(mapper(Tools.configuration(this), keyType)));
    }

    @Override
    default <K, V> Map<K, V> fetchMap(Class<? extends K> keyType, Class<? extends V> valueType) {
        return collect(intoMap(mapper(Tools.configuration(this), keyType), mapper(Tools.configuration(this), valueType)));
    }

    @Override
    default <K, V> Map<K, V> fetchMap(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper) {
        return collect(intoMap(mapper(Tools.configuration(this), keyType), valueMapper));
    }

    @Override
    default <K> Map<K, R> fetchMap(RecordMapper<? super R, K> keyMapper) {
        return collect(intoMap(keyMapper));
    }

    @Override
    default <K, V> Map<K, V> fetchMap(RecordMapper<? super R, K> keyMapper, Class<V> valueType) {
        return collect(intoMap(keyMapper, mapper(Tools.configuration(this), valueType)));
    }

    @Override
    default <K, V> Map<K, V> fetchMap(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper) {
        return collect(intoMap(keyMapper, valueMapper));
    }

    @Override
    default <S extends Record> Map<S, R> fetchMap(Table<S> table) {
        return collect(intoMap(mapper(table)));
    }

    @Override
    default <S extends Record, T extends Record> Map<S, T> fetchMap(Table<S> keyTable, Table<T> valueTable) {
        return collect(intoMap(mapper(keyTable), mapper(valueTable)));
    }

    @Override
    default <E, S extends Record> Map<S, E> fetchMap(Table<S> table, Class<? extends E> type) {
        return collect(intoMap(mapper(table), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E, S extends Record> Map<S, E> fetchMap(Table<S> table, RecordMapper<? super R, E> mapper) {
        return collect(intoMap(mapper(table), mapper));
    }

    @Override
    default List<Map<String, Object>> fetchMaps() {
        if (fetchIntermediateResult(Tools.configuration(this))) {
            return fetch().intoMaps();
        }
        else try (Cursor<R> c = fetchLazy()) {
            return c.stream().collect(ArrayList::new, (l, r) -> l.add(r.intoMap()), ArrayList::addAll);
        }
    }

    @Override
    default <K> Map<K, Result<R>> fetchGroups(Field<K> key) {
        return collect(intoResultGroups(mapper(key)));
    }

    @Override
    default Map<?, Result<R>> fetchGroups(int keyFieldIndex) {
        return collect(intoResultGroups(mapper(keyFieldIndex)));
    }

    @Override
    default Map<?, Result<R>> fetchGroups(String keyFieldName) {
        return collect(intoResultGroups(mapper(keyFieldName)));
    }

    @Override
    default Map<?, Result<R>> fetchGroups(Name keyFieldName) {
        return collect(intoResultGroups(mapper(keyFieldName)));
    }

    @Override
    default <K, V> Map<K, List<V>> fetchGroups(Field<K> key, Field<V> value) {
        return collect(intoGroups(mapper(key), mapper(value)));
    }

    @Override
    default Map<?, List<?>> fetchGroups(int keyFieldIndex, int valueFieldIndex) {
        return (Map) collect(intoGroups(mapper(keyFieldIndex), mapper(valueFieldIndex)));
    }

    @Override
    default Map<?, List<?>> fetchGroups(String keyFieldName, String valueFieldName) {
        return (Map) collect(intoGroups(mapper(keyFieldName), mapper(valueFieldName)));
    }

    @Override
    default Map<?, List<?>> fetchGroups(Name keyFieldName, Name valueFieldName) {
        return (Map) collect(intoGroups(mapper(keyFieldName), mapper(valueFieldName)));
    }

    @Override
    default <K, E> Map<K, List<E>> fetchGroups(Field<K> key, Class<? extends E> type) {
        return collect(intoGroups(mapper(key), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<?, List<E>> fetchGroups(int keyFieldIndex, Class<? extends E> type) {
        return collect(intoGroups(mapper(keyFieldIndex), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<?, List<E>> fetchGroups(String keyFieldName, Class<? extends E> type) {
        return collect(intoGroups(mapper(keyFieldName), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<?, List<E>> fetchGroups(Name keyFieldName, Class<? extends E> type) {
        return collect(intoGroups(mapper(keyFieldName), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <K, E> Map<K, List<E>> fetchGroups(Field<K> key, RecordMapper<? super R, E> mapper) {
        return collect(intoGroups(mapper(key), mapper));
    }

    @Override
    default <E> Map<?, List<E>> fetchGroups(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        return collect(intoGroups(mapper(keyFieldIndex), mapper));
    }

    @Override
    default <E> Map<?, List<E>> fetchGroups(String keyFieldName, RecordMapper<? super R, E> mapper) {
        return collect(intoGroups(mapper(keyFieldName), mapper));
    }

    @Override
    default <E> Map<?, List<E>> fetchGroups(Name keyFieldName, RecordMapper<? super R, E> mapper) {
        return collect(intoGroups(mapper(keyFieldName), mapper));
    }

    @Override
    default Map<Record, Result<R>> fetchGroups(Field<?>[] keys) {
        return collect(intoResultGroups(mapper(keys)));
    }

    @Override
    default Map<Record, Result<R>> fetchGroups(int[] keyFieldIndexes) {
        return collect(intoResultGroups(mapper(keyFieldIndexes)));
    }

    @Override
    default Map<Record, Result<R>> fetchGroups(String[] keyFieldNames) {
        return collect(intoResultGroups(mapper(keyFieldNames)));
    }

    @Override
    default Map<Record, Result<R>> fetchGroups(Name[] keyFieldNames) {
        return collect(intoResultGroups(mapper(keyFieldNames)));
    }

    @Override
    default Map<Record, Result<Record>> fetchGroups(Field<?>[] keys, Field<?>[] values) {
        return collect(intoResultGroups(mapper(keys), mapper(values)));
    }

    @Override
    default Map<Record, Result<Record>> fetchGroups(int[] keyFieldIndexes, int[] valueFieldIndexes) {
        return collect(intoResultGroups(mapper(keyFieldIndexes), mapper(valueFieldIndexes)));
    }

    @Override
    default Map<Record, Result<Record>> fetchGroups(String[] keyFieldNames, String[] valueFieldNames) {
        return collect(intoResultGroups(mapper(keyFieldNames), mapper(valueFieldNames)));
    }

    @Override
    default Map<Record, Result<Record>> fetchGroups(Name[] keyFieldNames, Name[] valueFieldNames) {
        return collect(intoResultGroups(mapper(keyFieldNames), mapper(valueFieldNames)));
    }

    @Override
    default <E> Map<Record, List<E>> fetchGroups(Field<?>[] keys, Class<? extends E> type) {
        return collect(intoGroups(mapper(keys), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<Record, List<E>> fetchGroups(int[] keyFieldIndexes, Class<? extends E> type) {
        return collect(intoGroups(mapper(keyFieldIndexes), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<Record, List<E>> fetchGroups(String[] keyFieldNames, Class<? extends E> type) {
        return collect(intoGroups(mapper(keyFieldNames), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<Record, List<E>> fetchGroups(Name[] keyFieldNames, Class<? extends E> type) {
        return collect(intoGroups(mapper(keyFieldNames), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E> Map<Record, List<E>> fetchGroups(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) {
        return collect(intoGroups(mapper(keyFieldIndexes), mapper));
    }

    @Override
    default <E> Map<Record, List<E>> fetchGroups(String[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return collect(intoGroups(mapper(keyFieldNames), mapper));
    }

    @Override
    default <E> Map<Record, List<E>> fetchGroups(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return collect(intoGroups(mapper(keyFieldNames), mapper));
    }

    @Override
    default <E> Map<Record, List<E>> fetchGroups(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        return collect(intoGroups(mapper(keys), mapper));
    }

    @Override
    default <K> Map<K, Result<R>> fetchGroups(Class<? extends K> keyType) {
        return collect(intoResultGroups(mapper(Tools.configuration(this), keyType)));
    }

    @Override
    default <K, V> Map<K, List<V>> fetchGroups(Class<? extends K> keyType, Class<? extends V> valueType) {
        return collect(intoGroups(mapper(Tools.configuration(this), keyType), mapper(Tools.configuration(this), valueType)));
    }

    @Override
    default <K, V> Map<K, List<V>> fetchGroups(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper) {
        return collect(intoGroups(mapper(Tools.configuration(this), keyType), valueMapper));
    }

    @Override
    default <K> Map<K, Result<R>> fetchGroups(RecordMapper<? super R, K> keyMapper) {
        return collect(intoResultGroups(keyMapper));
    }

    @Override
    default <K, V> Map<K, List<V>> fetchGroups(RecordMapper<? super R, K> keyMapper, Class<V> valueType) {
        return collect(intoGroups(keyMapper, mapper(Tools.configuration(this), valueType)));
    }

    @Override
    default <K, V> Map<K, List<V>> fetchGroups(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper) {
        return collect(intoGroups(keyMapper, valueMapper));
    }

    @Override
    default <S extends Record> Map<S, Result<R>> fetchGroups(Table<S> table) {
        return collect(intoResultGroups(mapper(table)));
    }

    @Override
    default <S extends Record, T extends Record> Map<S, Result<T>> fetchGroups(Table<S> keyTable, Table<T> valueTable) {
        return collect(intoResultGroups(mapper(keyTable), mapper(valueTable)));
    }

    @Override
    default <E, S extends Record> Map<S, List<E>> fetchGroups(Table<S> table, Class<? extends E> type) {
        return collect(intoGroups(mapper(table), mapper(Tools.configuration(this), type)));
    }

    @Override
    default <E, S extends Record> Map<S, List<E>> fetchGroups(Table<S> table, RecordMapper<? super R, E> mapper) {
        return collect(intoGroups(mapper(table), mapper));
    }

    @Override
    default Object[][] fetchArrays() {
        return collect(intoArray(new Object[0][], R::intoArray));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    default R[] fetchArray() {
        // [#9288] TODO: Create a delayed Collector that can delay the array type lookup until it's available
        Result<R> r = fetch();

        if (r.isNotEmpty())
            return r.toArray((R[]) Array.newInstance(r.get(0).getClass(), r.size()));

        Class<? extends R> recordType;

        // TODO [#3185] Pull up getRecordType()
        if (this instanceof AbstractResultQuery)
            recordType = ((AbstractResultQuery<R>) this).getRecordType();
        else if (this instanceof SelectImpl)
            recordType = ((SelectImpl) this).getRecordType();
        else
            throw new DataAccessException("Attempt to call fetchArray() on " + getClass());

        return r.toArray((R[]) Array.newInstance(recordType, r.size()));
    }

    @SuppressWarnings("unchecked")
    @Override
    default Object[] fetchArray(int fieldIndex) {
        return collect(new DelayedArrayCollector<>(
            fields -> (Object[]) Array.newInstance(fields.field(indexOrFail(fields, fieldIndex)).getType(), 0),
            (RecordMapper<R, Object>) mapper(fieldIndex)
        ));
    }

    @Override
    default <U> U[] fetchArray(int fieldIndex, Class<? extends U> type) {
        return collect(Records.intoArray(type, mapper(fieldIndex, Tools.configuration(this), type)));
    }

    @Override
    default <U> U[] fetchArray(int fieldIndex, Converter<?, ? extends U> converter) {
        return collect(Records.intoArray(converter.toType(), mapper(fieldIndex, converter)));
    }

    @SuppressWarnings("unchecked")
    @Override
    default Object[] fetchArray(String fieldName) {
        return collect(new DelayedArrayCollector<>(
            fields -> (Object[]) Array.newInstance(fields.field(indexOrFail(fields, fieldName)).getType(), 0),
            (RecordMapper<R, Object>) mapper(fieldName)
        ));
    }

    @Override
    default <U> U[] fetchArray(String fieldName, Class<? extends U> type) {
        return collect(Records.intoArray(type, mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    default <U> U[] fetchArray(String fieldName, Converter<?, ? extends U> converter) {
        return collect(Records.intoArray(converter.toType(), mapper(fieldName, converter)));
    }

    @SuppressWarnings("unchecked")
    @Override
    default Object[] fetchArray(Name fieldName) {
        return collect(new DelayedArrayCollector<>(
            fields -> (Object[]) Array.newInstance(fields.field(indexOrFail(fields, fieldName)).getType(), 0),
            (RecordMapper<R, Object>) mapper(fieldName)
        ));
    }

    @Override
    default <U> U[] fetchArray(Name fieldName, Class<? extends U> type) {
        return collect(Records.intoArray(type, mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    default <U> U[] fetchArray(Name fieldName, Converter<?, ? extends U> converter) {
        return collect(Records.intoArray(converter.toType(), mapper(fieldName, converter)));
    }

    @Override
    default <T> T[] fetchArray(Field<T> field) {
        return collect(Records.intoArray(field.getType(), mapper(field)));
    }

    @Override
    default <U> U[] fetchArray(Field<?> field, Class<? extends U> type) {
        return collect(Records.intoArray(type, mapper(field, Tools.configuration(this), type)));
    }

    @Override
    default <T, U> U[] fetchArray(Field<T> field, Converter<? super T, ? extends U> converter) {
        return collect(Records.intoArray(converter.toType(), mapper(field, converter)));
    }

    @Override
    default <E> Set<E> fetchSet(RecordMapper<? super R, E> mapper) {
        return collect(intoSet(mapper));
    }

    @Override
    default Set<?> fetchSet(int fieldIndex) {
        return collect(intoSet(mapper(fieldIndex)));
    }

    @Override
    default <U> Set<U> fetchSet(int fieldIndex, Class<? extends U> type) {
        return collect(intoSet(mapper(fieldIndex, Tools.configuration(this), type)));
    }

    @Override
    default <U> Set<U> fetchSet(int fieldIndex, Converter<?, ? extends U> converter) {
        return collect(intoSet(mapper(fieldIndex, converter)));
    }

    @Override
    default Set<?> fetchSet(String fieldName) {
        return collect(intoSet(mapper(fieldName)));
    }

    @Override
    default <U> Set<U> fetchSet(String fieldName, Class<? extends U> type) {
        return collect(intoSet(mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    default <U> Set<U> fetchSet(String fieldName, Converter<?, ? extends U> converter) {
        return collect(intoSet(mapper(fieldName, converter)));
    }

    @Override
    default Set<?> fetchSet(Name fieldName) {
        return collect(intoSet(mapper(fieldName)));
    }

    @Override
    default <U> Set<U> fetchSet(Name fieldName, Class<? extends U> type) {
        return collect(intoSet(mapper(fieldName, Tools.configuration(this), type)));
    }

    @Override
    default <U> Set<U> fetchSet(Name fieldName, Converter<?, ? extends U> converter) {
        return collect(intoSet(mapper(fieldName, converter)));
    }

    @Override
    default <T> Set<T> fetchSet(Field<T> field) {
        return collect(intoSet(mapper(field)));
    }

    @Override
    default <U> Set<U> fetchSet(Field<?> field, Class<? extends U> type) {
        return collect(intoSet(mapper(field, Tools.configuration(this), type)));
    }

    @Override
    default <T, U> Set<U> fetchSet(Field<T> field, Converter<? super T, ? extends U> converter) {
        return collect(intoSet(mapper(field, converter)));
    }
    @Override
    default <U> List<U> fetchInto(Class<? extends U> type) {
        return collect(intoList(mapper(Tools.configuration(this), type)));
    }

    @Override
    default <Z extends Record> Result<Z> fetchInto(Table<Z> table) {
        if (fetchIntermediateResult(Tools.configuration(this))) {
            return fetch().into(table);
        }
        else try (Cursor<R> c = fetchLazy()) {
            return c.fetchInto(table);
        }
    }

    @Override
    default <H extends RecordHandler<? super R>> H fetchInto(H handler) {
        forEach(handler);
        return handler;
    }

    @Override
    default void forEach(Consumer<? super R> action) {
        if (fetchIntermediateResult(Tools.configuration(this))) {
            fetch().forEach(action);
        }
        else try (Cursor<R> c = fetchLazy()) {
            c.forEach(action);
        }
    }

    @Override
    default <E> List<E> fetch(RecordMapper<? super R, E> mapper) {
        return collect(mapping(mapper, toList()));
    }

    default boolean hasLimit1() {
        if (this instanceof Select) {
            SelectQueryImpl<?> s = Tools.selectQueryImpl((Select<?>) this);

            if (s != null) {
                Limit l = s.getLimit();
                return !l.withTies() && !l.percent() && l.limitOne();
            }
        }

        return false;
    }

    @Override
    default RecordMapper<R, ?> mapper(int fieldIndex) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldIndex));
    }

    @Override
    default <U> RecordMapper<R, U> mapper(int fieldIndex, Configuration configuration, Class<? extends U> type) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldIndex, configuration, type));
    }

    @Override
    default <U> RecordMapper<R, U> mapper(int fieldIndex, Converter<?, ? extends U> converter) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldIndex, converter));
    }

    @Override
    default RecordMapper<R, Record> mapper(int[] fieldIndexes) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldIndexes));
    }

    @Override
    default RecordMapper<R, ?> mapper(String fieldName) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldName));
    }

    @Override
    default <U> RecordMapper<R, U> mapper(String fieldName, Configuration configuration, Class<? extends U> type) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldName, configuration, type));
    }

    @Override
    default <U> RecordMapper<R, U> mapper(String fieldName, Converter<?, ? extends U> converter) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldName, converter));
    }

    @Override
    default RecordMapper<R, Record> mapper(String[] fieldNames) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldNames));
    }

    @Override
    default RecordMapper<R, ?> mapper(Name fieldName) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldName));
    }

    @Override
    default <U> RecordMapper<R, U> mapper(Name fieldName, Configuration configuration, Class<? extends U> type) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldName, configuration, type));
    }

    @Override
    default <U> RecordMapper<R, U> mapper(Name fieldName, Converter<?, ? extends U> converter) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldName, converter));
    }

    @Override
    default RecordMapper<R, Record> mapper(Name[] fieldNames) {
        return new DelayedRecordMapper<>(t -> t.mapper(fieldNames));
    }

    @Override
    default <T> RecordMapper<R, T> mapper(Field<T> field) {
        return new DelayedRecordMapper<>(t -> t.mapper(field));
    }

    @Override
    default <U> RecordMapper<R, U> mapper(Field<?> field, Configuration configuration, Class<? extends U> type) {
        return new DelayedRecordMapper<>(t -> t.mapper(field, configuration, type));
    }

    @Override
    default <T, U> RecordMapper<R, U> mapper(Field<T> field, Converter<? super T, ? extends U> converter) {
        return new DelayedRecordMapper<>(t -> t.mapper(field, converter));
    }

    @Override
    default RecordMapper<R, Record> mapper(Field<?>[] fields) {
        return new DelayedRecordMapper<>(t -> t.mapper(fields));
    }

    @Override
    default <S extends Record> RecordMapper<R, S> mapper(Table<S> table) {
        return new DelayedRecordMapper<>(t -> t.mapper(table));
    }

    @Override
    default <E> RecordMapper<R, E> mapper(Configuration configuration, Class<? extends E> type) {
        return new DelayedRecordMapper<>(t -> t.mapper(configuration, type));
    }

    // -------------------------------------------------------------------------
    // XXX: Fields API
    // -------------------------------------------------------------------------

    /**
     * Get a list of fields provided a result set.
     *
     * @throws SQLException If something goes wrong when accessing
     *             {@link ResultSetMetaData}.
     */
    default Field<?>[] getFields(ResultSetMetaData rs) throws SQLException {
        return getFields();
    }

    /**
     * Get a list of fields if we don't have a result set.
     */
    Field<?>[] getFields();

    @Override
    default Row fieldsRow() {
        return Tools.row0(getFields());
    }
}
