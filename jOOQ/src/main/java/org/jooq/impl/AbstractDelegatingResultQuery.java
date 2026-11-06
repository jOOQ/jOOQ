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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Flow;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.CloseableResultQuery;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Cursor;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Fields;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.Record7;
import org.jooq.Record8;
import org.jooq.Record9;
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
import org.jooq.Record20;
import org.jooq.Record21;
import org.jooq.Record22;
import org.jooq.RecordHandler;
import org.jooq.RecordMapper;
import org.jooq.Result;
import org.jooq.Results;
import org.jooq.Row;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
abstract class AbstractDelegatingResultQuery<R extends Record, Q extends ResultQueryTrait<R>>
extends
    AbstractDelegatingQuery<R, Q>
implements
    ResultQueryTrait<R>
{
    AbstractDelegatingResultQuery(Q delegate) {
        super(delegate);
    }

    // -------------------------------------------------------------------------
    // XXX: ResultQuery API, delegating to the delegate query
    // -------------------------------------------------------------------------

    // [#xxxxx] All ResultQuery methods are implemented explicitly here, even
    // where an interface default method exists, because this greatly speeds up
    // compilation of SelectImpl: javac's Check.checkAllDefined() looks up
    // implementations of abstract methods among class members via a cache.
    // Abstract methods that are implemented only by interface default methods
    // fall back to the uncached Types.interfaceCandidates() lookup, which scans
    // the entire supertype closure, once per path through the supertype graph
    // (SelectImpl reaches ResultQuery via 35 paths and Fields via 70).

    @Override
    public final CloseableResultQuery<R> maxRows(int rows) {
        return getDelegate().maxRows(rows);
    }

    @Override
    public final CloseableResultQuery<R> fetchSize(int rows) {
        return getDelegate().fetchSize(rows);
    }

    @Override
    public final CloseableResultQuery<R> resultSetConcurrency(int resultSetConcurrency) {
        return getDelegate().resultSetConcurrency(resultSetConcurrency);
    }

    @Override
    public final CloseableResultQuery<R> resultSetType(int resultSetType) {
        return getDelegate().resultSetType(resultSetType);
    }

    @Override
    public final CloseableResultQuery<R> resultSetHoldability(int resultSetHoldability) {
        return getDelegate().resultSetHoldability(resultSetHoldability);
    }

    @Override
    public final Class<? extends R> getRecordType() {
        return getDelegate().getRecordType();
    }

    @Override
    public final Result<R> getResult() {
        return getDelegate().getResult();
    }

    @Override
    public final Result<R> fetch() {
        return getDelegate().fetch();
    }



    @Override
    public final void subscribe(Flow.Subscriber<? super R> subscriber) {
        getDelegate().subscribe(subscriber);
    }



    @Override
    public final void subscribe(org.reactivestreams.Subscriber<? super R> subscriber) {
        getDelegate().subscribe(subscriber);
    }

    @Override
    public final Cursor<R> fetchLazy() {
        return getDelegate().fetchLazy();
    }

    @Override
    public final Cursor<R> fetchLazyNonAutoClosing() {
        return getDelegate().fetchLazyNonAutoClosing();
    }

    @Override
    public final Results fetchMany() {
        return getDelegate().fetchMany();
    }

    @Override
    public final Row fieldsRow() {
        return getDelegate().fieldsRow();
    }

    @Override
    public final <X extends Record> CloseableResultQuery<X> coerce(Table<X> table) {
        return getDelegate().coerce(table);
    }

    @Override
    public final CloseableResultQuery<Record> coerce(Collection<? extends Field<?>> fields) {
        return getDelegate().coerce(fields);
    }

    @Override
    public final Field<?>[] getFields(ThrowingSupplier<? extends ResultSetMetaData, SQLException> rs) throws SQLException {
        return getDelegate().getFields(rs);
    }

    @Override
    public final Field<?>[] getFields() {
        return getDelegate().getFields();
    }


    @Override
    public final CloseableResultQuery<Record> coerce(Field<?>... fields) {
        return getDelegate().coerce(fields);
    }



    @Override
    public final <T1> CloseableResultQuery<Record1<T1>> coerce(Field<T1> field1) {
        return getDelegate().coerce(field1);
    }

    @Override
    public final <T1, T2> CloseableResultQuery<Record2<T1, T2>> coerce(Field<T1> field1, Field<T2> field2) {
        return getDelegate().coerce(field1, field2);
    }

    @Override
    public final <T1, T2, T3> CloseableResultQuery<Record3<T1, T2, T3>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return getDelegate().coerce(field1, field2, field3);
    }

    @Override
    public final <T1, T2, T3, T4> CloseableResultQuery<Record4<T1, T2, T3, T4>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return getDelegate().coerce(field1, field2, field3, field4);
    }

    @Override
    public final <T1, T2, T3, T4, T5> CloseableResultQuery<Record5<T1, T2, T3, T4, T5>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return getDelegate().coerce(field1, field2, field3, field4, field5);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6> CloseableResultQuery<Record6<T1, T2, T3, T4, T5, T6>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7> CloseableResultQuery<Record7<T1, T2, T3, T4, T5, T6, T7>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8> CloseableResultQuery<Record8<T1, T2, T3, T4, T5, T6, T7, T8>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> CloseableResultQuery<Record9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> CloseableResultQuery<Record10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> CloseableResultQuery<Record11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> CloseableResultQuery<Record12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> CloseableResultQuery<Record13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> CloseableResultQuery<Record14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> CloseableResultQuery<Record15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> CloseableResultQuery<Record16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> CloseableResultQuery<Record17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> CloseableResultQuery<Record18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> CloseableResultQuery<Record19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> CloseableResultQuery<Record20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> CloseableResultQuery<Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21);
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> CloseableResultQuery<Record22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> coerce(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return getDelegate().coerce(field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22);
    }



    @Override
    public final ResultSet fetchResultSet() {
        return getDelegate().fetchResultSet();
    }

    @Override
    public final Iterator<R> iterator() {
        return getDelegate().iterator();
    }

    @Override
    public final CompletionStage<Result<R>> fetchAsync() {
        return getDelegate().fetchAsync();
    }

    @Override
    public final CompletionStage<Result<R>> fetchAsync(Executor executor) {
        return getDelegate().fetchAsync(executor);
    }

    @Override
    public final Stream<R> fetchStream() {
        return getDelegate().fetchStream();
    }

    @Override
    public final <E> Stream<E> fetchStreamInto(Class<? extends E> type) {
        return getDelegate().fetchStreamInto(type);
    }

    @Override
    public final <Z extends Record> Stream<Z> fetchStreamInto(Table<Z> table) {
        return getDelegate().fetchStreamInto(table);
    }

    @Override
    public final Stream<R> stream() {
        return getDelegate().stream();
    }

    @Override
    public final <X, A> X collect(Collector<? super R, A, X> collector) {
        return getDelegate().collect(collector);
    }

    @Override
    public final <T> List<T> fetch(Field<T> field) {
        return getDelegate().fetch(field);
    }

    @Override
    public final <U> List<U> fetch(Field<?> field, Class<? extends U> type) {
        return getDelegate().fetch(field, type);
    }

    @Override
    public final <T, U> List<U> fetch(Field<T> field, Converter<? super T, ? extends U> converter) {
        return getDelegate().fetch(field, converter);
    }

    @Override
    public final List<?> fetch(int fieldIndex) {
        return getDelegate().fetch(fieldIndex);
    }

    @Override
    public final <U> List<U> fetch(int fieldIndex, Class<? extends U> type) {
        return getDelegate().fetch(fieldIndex, type);
    }

    @Override
    public final <U> List<U> fetch(int fieldIndex, Converter<?, ? extends U> converter) {
        return getDelegate().fetch(fieldIndex, converter);
    }

    @Override
    public final List<?> fetch(String fieldName) {
        return getDelegate().fetch(fieldName);
    }

    @Override
    public final <U> List<U> fetch(String fieldName, Class<? extends U> type) {
        return getDelegate().fetch(fieldName, type);
    }

    @Override
    public final <U> List<U> fetch(String fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetch(fieldName, converter);
    }

    @Override
    public final List<?> fetch(Name fieldName) {
        return getDelegate().fetch(fieldName);
    }

    @Override
    public final <U> List<U> fetch(Name fieldName, Class<? extends U> type) {
        return getDelegate().fetch(fieldName, type);
    }

    @Override
    public final <U> List<U> fetch(Name fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetch(fieldName, converter);
    }

    @Override
    public final <T> T fetchOne(Field<T> field) {
        return getDelegate().fetchOne(field);
    }

    @Override
    public final <U> U fetchOne(Field<?> field, Class<? extends U> type) {
        return getDelegate().fetchOne(field, type);
    }

    @Override
    public final <T, U> U fetchOne(Field<T> field, Converter<? super T, ? extends U> converter) {
        return getDelegate().fetchOne(field, converter);
    }

    @Override
    public final Object fetchOne(int fieldIndex) {
        return getDelegate().fetchOne(fieldIndex);
    }

    @Override
    public final <U> U fetchOne(int fieldIndex, Class<? extends U> type) {
        return getDelegate().fetchOne(fieldIndex, type);
    }

    @Override
    public final <U> U fetchOne(int fieldIndex, Converter<?, ? extends U> converter) {
        return getDelegate().fetchOne(fieldIndex, converter);
    }

    @Override
    public final Object fetchOne(String fieldName) {
        return getDelegate().fetchOne(fieldName);
    }

    @Override
    public final <U> U fetchOne(String fieldName, Class<? extends U> type) {
        return getDelegate().fetchOne(fieldName, type);
    }

    @Override
    public final <U> U fetchOne(String fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchOne(fieldName, converter);
    }

    @Override
    public final Object fetchOne(Name fieldName) {
        return getDelegate().fetchOne(fieldName);
    }

    @Override
    public final <U> U fetchOne(Name fieldName, Class<? extends U> type) {
        return getDelegate().fetchOne(fieldName, type);
    }

    @Override
    public final <U> U fetchOne(Name fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchOne(fieldName, converter);
    }

    @Override
    public final R fetchOne() {
        return getDelegate().fetchOne();
    }

    @Override
    public final <E> E fetchOne(RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchOne(mapper);
    }

    @Override
    public final Map<String, Object> fetchOneMap() {
        return getDelegate().fetchOneMap();
    }

    @Override
    public final Object @Nullable [] fetchOneArray() {
        return getDelegate().fetchOneArray();
    }

    @Override
    public final <E> E fetchOneInto(Class<? extends E> type) {
        return getDelegate().fetchOneInto(type);
    }

    @Override
    public final <Z extends Record> Z fetchOneInto(Table<Z> table) {
        return getDelegate().fetchOneInto(table);
    }

    @Override
    public final <T> T fetchSingle(Field<T> field) {
        return getDelegate().fetchSingle(field);
    }

    @Override
    public final <U> U fetchSingle(Field<?> field, Class<? extends U> type) {
        return getDelegate().fetchSingle(field, type);
    }

    @Override
    public final <T, U> U fetchSingle(Field<T> field, Converter<? super T, ? extends U> converter) {
        return getDelegate().fetchSingle(field, converter);
    }

    @Override
    public final Object fetchSingle(int fieldIndex) {
        return getDelegate().fetchSingle(fieldIndex);
    }

    @Override
    public final <U> U fetchSingle(int fieldIndex, Class<? extends U> type) {
        return getDelegate().fetchSingle(fieldIndex, type);
    }

    @Override
    public final <U> U fetchSingle(int fieldIndex, Converter<?, ? extends U> converter) {
        return getDelegate().fetchSingle(fieldIndex, converter);
    }

    @Override
    public final Object fetchSingle(String fieldName) {
        return getDelegate().fetchSingle(fieldName);
    }

    @Override
    public final <U> U fetchSingle(String fieldName, Class<? extends U> type) {
        return getDelegate().fetchSingle(fieldName, type);
    }

    @Override
    public final <U> U fetchSingle(String fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchSingle(fieldName, converter);
    }

    @Override
    public final Object fetchSingle(Name fieldName) {
        return getDelegate().fetchSingle(fieldName);
    }

    @Override
    public final <U> U fetchSingle(Name fieldName, Class<? extends U> type) {
        return getDelegate().fetchSingle(fieldName, type);
    }

    @Override
    public final <U> U fetchSingle(Name fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchSingle(fieldName, converter);
    }

    @Override
    public final R fetchSingle() {
        return getDelegate().fetchSingle();
    }

    @Override
    public final <E> E fetchSingle(RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchSingle(mapper);
    }

    @Override
    public final Map<String, Object> fetchSingleMap() {
        return getDelegate().fetchSingleMap();
    }

    @Override
    public final Object @NotNull [] fetchSingleArray() {
        return getDelegate().fetchSingleArray();
    }

    @Override
    public final <E> E fetchSingleInto(Class<? extends E> type) {
        return getDelegate().fetchSingleInto(type);
    }

    @Override
    public final <Z extends Record> Z fetchSingleInto(Table<Z> table) {
        return getDelegate().fetchSingleInto(table);
    }

    @Override
    public final <T> Optional<T> fetchOptional(Field<T> field) {
        return getDelegate().fetchOptional(field);
    }

    @Override
    public final <U> Optional<U> fetchOptional(Field<?> field, Class<? extends U> type) {
        return getDelegate().fetchOptional(field, type);
    }

    @Override
    public final <T, U> Optional<U> fetchOptional(Field<T> field, Converter<? super T, ? extends U> converter) {
        return getDelegate().fetchOptional(field, converter);
    }

    @Override
    public final Optional<?> fetchOptional(int fieldIndex) {
        return getDelegate().fetchOptional(fieldIndex);
    }

    @Override
    public final <U> Optional<U> fetchOptional(int fieldIndex, Class<? extends U> type) {
        return getDelegate().fetchOptional(fieldIndex, type);
    }

    @Override
    public final <U> Optional<U> fetchOptional(int fieldIndex, Converter<?, ? extends U> converter) {
        return getDelegate().fetchOptional(fieldIndex, converter);
    }

    @Override
    public final Optional<?> fetchOptional(String fieldName) {
        return getDelegate().fetchOptional(fieldName);
    }

    @Override
    public final <U> Optional<U> fetchOptional(String fieldName, Class<? extends U> type) {
        return getDelegate().fetchOptional(fieldName, type);
    }

    @Override
    public final <U> Optional<U> fetchOptional(String fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchOptional(fieldName, converter);
    }

    @Override
    public final Optional<?> fetchOptional(Name fieldName) {
        return getDelegate().fetchOptional(fieldName);
    }

    @Override
    public final <U> Optional<U> fetchOptional(Name fieldName, Class<? extends U> type) {
        return getDelegate().fetchOptional(fieldName, type);
    }

    @Override
    public final <U> Optional<U> fetchOptional(Name fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchOptional(fieldName, converter);
    }

    @Override
    public final Optional<R> fetchOptional() {
        return getDelegate().fetchOptional();
    }

    @Override
    public final <E> Optional<E> fetchOptional(RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchOptional(mapper);
    }

    @Override
    public final Optional<Map<String, Object>> fetchOptionalMap() {
        return getDelegate().fetchOptionalMap();
    }

    @Override
    public final Optional<Object[]> fetchOptionalArray() {
        return getDelegate().fetchOptionalArray();
    }

    @Override
    public final <E> Optional<E> fetchOptionalInto(Class<? extends E> type) {
        return getDelegate().fetchOptionalInto(type);
    }

    @Override
    public final <Z extends Record> Optional<Z> fetchOptionalInto(Table<Z> table) {
        return getDelegate().fetchOptionalInto(table);
    }

    @Override
    public final <T> T fetchAny(Field<T> field) {
        return getDelegate().fetchAny(field);
    }

    @Override
    public final <U> U fetchAny(Field<?> field, Class<? extends U> type) {
        return getDelegate().fetchAny(field, type);
    }

    @Override
    public final <T, U> U fetchAny(Field<T> field, Converter<? super T, ? extends U> converter) {
        return getDelegate().fetchAny(field, converter);
    }

    @Override
    public final Object fetchAny(int fieldIndex) {
        return getDelegate().fetchAny(fieldIndex);
    }

    @Override
    public final <U> U fetchAny(int fieldIndex, Class<? extends U> type) {
        return getDelegate().fetchAny(fieldIndex, type);
    }

    @Override
    public final <U> U fetchAny(int fieldIndex, Converter<?, ? extends U> converter) {
        return getDelegate().fetchAny(fieldIndex, converter);
    }

    @Override
    public final Object fetchAny(String fieldName) {
        return getDelegate().fetchAny(fieldName);
    }

    @Override
    public final <U> U fetchAny(String fieldName, Class<? extends U> type) {
        return getDelegate().fetchAny(fieldName, type);
    }

    @Override
    public final <U> U fetchAny(String fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchAny(fieldName, converter);
    }

    @Override
    public final Object fetchAny(Name fieldName) {
        return getDelegate().fetchAny(fieldName);
    }

    @Override
    public final <U> U fetchAny(Name fieldName, Class<? extends U> type) {
        return getDelegate().fetchAny(fieldName, type);
    }

    @Override
    public final <U> U fetchAny(Name fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchAny(fieldName, converter);
    }

    @Override
    public final R fetchAny() {
        return getDelegate().fetchAny();
    }

    @Override
    public final <E> E fetchAny(RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchAny(mapper);
    }

    @Override
    public final Map<String, Object> fetchAnyMap() {
        return getDelegate().fetchAnyMap();
    }

    @Override
    public final Object @Nullable [] fetchAnyArray() {
        return getDelegate().fetchAnyArray();
    }

    @Override
    public final <E> E fetchAnyInto(Class<? extends E> type) {
        return getDelegate().fetchAnyInto(type);
    }

    @Override
    public final <Z extends Record> Z fetchAnyInto(Table<Z> table) {
        return getDelegate().fetchAnyInto(table);
    }

    @Override
    public final <K> Map<K, R> fetchMap(Field<K> key) {
        return getDelegate().fetchMap(key);
    }

    @Override
    public final Map<?, R> fetchMap(int keyFieldIndex) {
        return getDelegate().fetchMap(keyFieldIndex);
    }

    @Override
    public final Map<?, R> fetchMap(String keyFieldName) {
        return getDelegate().fetchMap(keyFieldName);
    }

    @Override
    public final Map<?, R> fetchMap(Name keyFieldName) {
        return getDelegate().fetchMap(keyFieldName);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Field<K> key, Field<V> value) {
        return getDelegate().fetchMap(key, value);
    }

    @Override
    public final Map<?, ?> fetchMap(int keyFieldIndex, int valueFieldIndex) {
        return getDelegate().fetchMap(keyFieldIndex, valueFieldIndex);
    }

    @Override
    public final Map<?, ?> fetchMap(String keyFieldName, String valueFieldName) {
        return getDelegate().fetchMap(keyFieldName, valueFieldName);
    }

    @Override
    public final Map<?, ?> fetchMap(Name keyFieldName, Name valueFieldName) {
        return getDelegate().fetchMap(keyFieldName, valueFieldName);
    }

    @Override
    public final <K, E> Map<K, E> fetchMap(Field<K> key, Class<? extends E> type) {
        return getDelegate().fetchMap(key, type);
    }

    @Override
    public final <E> Map<?, E> fetchMap(int keyFieldIndex, Class<? extends E> type) {
        return getDelegate().fetchMap(keyFieldIndex, type);
    }

    @Override
    public final <E> Map<?, E> fetchMap(String keyFieldName, Class<? extends E> type) {
        return getDelegate().fetchMap(keyFieldName, type);
    }

    @Override
    public final <E> Map<?, E> fetchMap(Name keyFieldName, Class<? extends E> type) {
        return getDelegate().fetchMap(keyFieldName, type);
    }

    @Override
    public final <K, E> Map<K, E> fetchMap(Field<K> key, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(key, mapper);
    }

    @Override
    public final <E> Map<?, E> fetchMap(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(keyFieldIndex, mapper);
    }

    @Override
    public final <E> Map<?, E> fetchMap(String keyFieldName, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(keyFieldName, mapper);
    }

    @Override
    public final <E> Map<?, E> fetchMap(Name keyFieldName, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(keyFieldName, mapper);
    }

    @Override
    public final Map<Record, R> fetchMap(Field<?>[] keys) {
        return getDelegate().fetchMap(keys);
    }

    @Override
    public final Map<Record, R> fetchMap(int[] keyFieldIndexes) {
        return getDelegate().fetchMap(keyFieldIndexes);
    }

    @Override
    public final Map<Record, R> fetchMap(String[] keyFieldNames) {
        return getDelegate().fetchMap(keyFieldNames);
    }

    @Override
    public final Map<Record, R> fetchMap(Name[] keyFieldNames) {
        return getDelegate().fetchMap(keyFieldNames);
    }

    @Override
    public final Map<Record, Record> fetchMap(Field<?>[] keys, Field<?>[] values) {
        return getDelegate().fetchMap(keys, values);
    }

    @Override
    public final Map<Record, Record> fetchMap(int[] keyFieldIndexes, int[] valueFieldIndexes) {
        return getDelegate().fetchMap(keyFieldIndexes, valueFieldIndexes);
    }

    @Override
    public final Map<Record, Record> fetchMap(String[] keyFieldNames, String[] valueFieldNames) {
        return getDelegate().fetchMap(keyFieldNames, valueFieldNames);
    }

    @Override
    public final Map<Record, Record> fetchMap(Name[] keyFieldNames, Name[] valueFieldNames) {
        return getDelegate().fetchMap(keyFieldNames, valueFieldNames);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Field<?>[] keys, Class<? extends E> type) {
        return getDelegate().fetchMap(keys, type);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(int[] keyFieldIndexes, Class<? extends E> type) {
        return getDelegate().fetchMap(keyFieldIndexes, type);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(String[] keyFieldNames, Class<? extends E> type) {
        return getDelegate().fetchMap(keyFieldNames, type);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Name[] keyFieldNames, Class<? extends E> type) {
        return getDelegate().fetchMap(keyFieldNames, type);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(keys, mapper);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(keyFieldIndexes, mapper);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(String[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(keyFieldNames, mapper);
    }

    @Override
    public final <E> Map<List<?>, E> fetchMap(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(keyFieldNames, mapper);
    }

    @Override
    public final <K> Map<K, R> fetchMap(Class<? extends K> keyType) {
        return getDelegate().fetchMap(keyType);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Class<? extends K> keyType, Class<? extends V> valueType) {
        return getDelegate().fetchMap(keyType, valueType);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper) {
        return getDelegate().fetchMap(keyType, valueMapper);
    }

    @Override
    public final <K> Map<K, R> fetchMap(RecordMapper<? super R, K> keyMapper) {
        return getDelegate().fetchMap(keyMapper);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(RecordMapper<? super R, K> keyMapper, Class<V> valueType) {
        return getDelegate().fetchMap(keyMapper, valueType);
    }

    @Override
    public final <K, V> Map<K, V> fetchMap(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper) {
        return getDelegate().fetchMap(keyMapper, valueMapper);
    }

    @Override
    public final <S extends Record> Map<S, R> fetchMap(Table<S> table) {
        return getDelegate().fetchMap(table);
    }

    @Override
    public final <S extends Record, T extends Record> Map<S, T> fetchMap(Table<S> keyTable, Table<T> valueTable) {
        return getDelegate().fetchMap(keyTable, valueTable);
    }

    @Override
    public final <E, S extends Record> Map<S, E> fetchMap(Table<S> table, Class<? extends E> type) {
        return getDelegate().fetchMap(table, type);
    }

    @Override
    public final <E, S extends Record> Map<S, E> fetchMap(Table<S> table, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchMap(table, mapper);
    }

    @Override
    public final List<Map<String, Object>> fetchMaps() {
        return getDelegate().fetchMaps();
    }

    @Override
    public final <K> Map<K, Result<R>> fetchGroups(Field<K> key) {
        return getDelegate().fetchGroups(key);
    }

    @Override
    public final Map<?, Result<R>> fetchGroups(int keyFieldIndex) {
        return getDelegate().fetchGroups(keyFieldIndex);
    }

    @Override
    public final Map<?, Result<R>> fetchGroups(String keyFieldName) {
        return getDelegate().fetchGroups(keyFieldName);
    }

    @Override
    public final Map<?, Result<R>> fetchGroups(Name keyFieldName) {
        return getDelegate().fetchGroups(keyFieldName);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(Field<K> key, Field<V> value) {
        return getDelegate().fetchGroups(key, value);
    }

    @Override
    public final Map<?, List<?>> fetchGroups(int keyFieldIndex, int valueFieldIndex) {
        return getDelegate().fetchGroups(keyFieldIndex, valueFieldIndex);
    }

    @Override
    public final Map<?, List<?>> fetchGroups(String keyFieldName, String valueFieldName) {
        return getDelegate().fetchGroups(keyFieldName, valueFieldName);
    }

    @Override
    public final Map<?, List<?>> fetchGroups(Name keyFieldName, Name valueFieldName) {
        return getDelegate().fetchGroups(keyFieldName, valueFieldName);
    }

    @Override
    public final <K, E> Map<K, List<E>> fetchGroups(Field<K> key, Class<? extends E> type) {
        return getDelegate().fetchGroups(key, type);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(int keyFieldIndex, Class<? extends E> type) {
        return getDelegate().fetchGroups(keyFieldIndex, type);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(String keyFieldName, Class<? extends E> type) {
        return getDelegate().fetchGroups(keyFieldName, type);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(Name keyFieldName, Class<? extends E> type) {
        return getDelegate().fetchGroups(keyFieldName, type);
    }

    @Override
    public final <K, E> Map<K, List<E>> fetchGroups(Field<K> key, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(key, mapper);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(int keyFieldIndex, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(keyFieldIndex, mapper);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(String keyFieldName, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(keyFieldName, mapper);
    }

    @Override
    public final <E> Map<?, List<E>> fetchGroups(Name keyFieldName, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(keyFieldName, mapper);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(Field<?>[] keys) {
        return getDelegate().fetchGroups(keys);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(int[] keyFieldIndexes) {
        return getDelegate().fetchGroups(keyFieldIndexes);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(String[] keyFieldNames) {
        return getDelegate().fetchGroups(keyFieldNames);
    }

    @Override
    public final Map<Record, Result<R>> fetchGroups(Name[] keyFieldNames) {
        return getDelegate().fetchGroups(keyFieldNames);
    }

    @Override
    public final Map<Record, Result<Record>> fetchGroups(Field<?>[] keys, Field<?>[] values) {
        return getDelegate().fetchGroups(keys, values);
    }

    @Override
    public final Map<Record, Result<Record>> fetchGroups(int[] keyFieldIndexes, int[] valueFieldIndexes) {
        return getDelegate().fetchGroups(keyFieldIndexes, valueFieldIndexes);
    }

    @Override
    public final Map<Record, Result<Record>> fetchGroups(String[] keyFieldNames, String[] valueFieldNames) {
        return getDelegate().fetchGroups(keyFieldNames, valueFieldNames);
    }

    @Override
    public final Map<Record, Result<Record>> fetchGroups(Name[] keyFieldNames, Name[] valueFieldNames) {
        return getDelegate().fetchGroups(keyFieldNames, valueFieldNames);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Field<?>[] keys, Class<? extends E> type) {
        return getDelegate().fetchGroups(keys, type);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(int[] keyFieldIndexes, Class<? extends E> type) {
        return getDelegate().fetchGroups(keyFieldIndexes, type);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(String[] keyFieldNames, Class<? extends E> type) {
        return getDelegate().fetchGroups(keyFieldNames, type);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Name[] keyFieldNames, Class<? extends E> type) {
        return getDelegate().fetchGroups(keyFieldNames, type);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(int[] keyFieldIndexes, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(keyFieldIndexes, mapper);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(String[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(keyFieldNames, mapper);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Name[] keyFieldNames, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(keyFieldNames, mapper);
    }

    @Override
    public final <E> Map<Record, List<E>> fetchGroups(Field<?>[] keys, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(keys, mapper);
    }

    @Override
    public final <K> Map<K, Result<R>> fetchGroups(Class<? extends K> keyType) {
        return getDelegate().fetchGroups(keyType);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(Class<? extends K> keyType, Class<? extends V> valueType) {
        return getDelegate().fetchGroups(keyType, valueType);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(Class<? extends K> keyType, RecordMapper<? super R, V> valueMapper) {
        return getDelegate().fetchGroups(keyType, valueMapper);
    }

    @Override
    public final <K> Map<K, Result<R>> fetchGroups(RecordMapper<? super R, K> keyMapper) {
        return getDelegate().fetchGroups(keyMapper);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(RecordMapper<? super R, K> keyMapper, Class<V> valueType) {
        return getDelegate().fetchGroups(keyMapper, valueType);
    }

    @Override
    public final <K, V> Map<K, List<V>> fetchGroups(RecordMapper<? super R, K> keyMapper, RecordMapper<? super R, V> valueMapper) {
        return getDelegate().fetchGroups(keyMapper, valueMapper);
    }

    @Override
    public final <S extends Record> Map<S, Result<R>> fetchGroups(Table<S> table) {
        return getDelegate().fetchGroups(table);
    }

    @Override
    public final <S extends Record, T extends Record> Map<S, Result<T>> fetchGroups(Table<S> keyTable, Table<T> valueTable) {
        return getDelegate().fetchGroups(keyTable, valueTable);
    }

    @Override
    public final <E, S extends Record> Map<S, List<E>> fetchGroups(Table<S> table, Class<? extends E> type) {
        return getDelegate().fetchGroups(table, type);
    }

    @Override
    public final <E, S extends Record> Map<S, List<E>> fetchGroups(Table<S> table, RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchGroups(table, mapper);
    }

    @Override
    public final Object /* @NotNull */ [] /* @NotNull */ [] fetchArrays() {
        return getDelegate().fetchArrays();
    }

    @Override
    public final R @NotNull [] fetchArray() {
        return getDelegate().fetchArray();
    }

    @Override
    public final Object @NotNull [] fetchArray(int fieldIndex) {
        return getDelegate().fetchArray(fieldIndex);
    }

    @Override
    public final <U> U @NotNull [] fetchArray(int fieldIndex, Class<? extends U> type) {
        return getDelegate().fetchArray(fieldIndex, type);
    }

    @Override
    public final <U> U @NotNull [] fetchArray(int fieldIndex, Converter<?, ? extends U> converter) {
        return getDelegate().fetchArray(fieldIndex, converter);
    }

    @Override
    public final Object @NotNull [] fetchArray(String fieldName) {
        return getDelegate().fetchArray(fieldName);
    }

    @Override
    public final <U> U @NotNull [] fetchArray(String fieldName, Class<? extends U> type) {
        return getDelegate().fetchArray(fieldName, type);
    }

    @Override
    public final <U> U @NotNull [] fetchArray(String fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchArray(fieldName, converter);
    }

    @Override
    public final Object @NotNull [] fetchArray(Name fieldName) {
        return getDelegate().fetchArray(fieldName);
    }

    @Override
    public final <U> U @NotNull [] fetchArray(Name fieldName, Class<? extends U> type) {
        return getDelegate().fetchArray(fieldName, type);
    }

    @Override
    public final <U> U @NotNull [] fetchArray(Name fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchArray(fieldName, converter);
    }

    @Override
    public final <T> T @NotNull [] fetchArray(Field<T> field) {
        return getDelegate().fetchArray(field);
    }

    @Override
    public final <U> U @NotNull [] fetchArray(Field<?> field, Class<? extends U> type) {
        return getDelegate().fetchArray(field, type);
    }

    @Override
    public final <T, U> U @NotNull [] fetchArray(Field<T> field, Converter<? super T, ? extends U> converter) {
        return getDelegate().fetchArray(field, converter);
    }

    @Override
    public final <E> Set<E> fetchSet(RecordMapper<? super R, E> mapper) {
        return getDelegate().fetchSet(mapper);
    }

    @Override
    public final Set<?> fetchSet(int fieldIndex) {
        return getDelegate().fetchSet(fieldIndex);
    }

    @Override
    public final <U> Set<U> fetchSet(int fieldIndex, Class<? extends U> type) {
        return getDelegate().fetchSet(fieldIndex, type);
    }

    @Override
    public final <U> Set<U> fetchSet(int fieldIndex, Converter<?, ? extends U> converter) {
        return getDelegate().fetchSet(fieldIndex, converter);
    }

    @Override
    public final Set<?> fetchSet(String fieldName) {
        return getDelegate().fetchSet(fieldName);
    }

    @Override
    public final <U> Set<U> fetchSet(String fieldName, Class<? extends U> type) {
        return getDelegate().fetchSet(fieldName, type);
    }

    @Override
    public final <U> Set<U> fetchSet(String fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchSet(fieldName, converter);
    }

    @Override
    public final Set<?> fetchSet(Name fieldName) {
        return getDelegate().fetchSet(fieldName);
    }

    @Override
    public final <U> Set<U> fetchSet(Name fieldName, Class<? extends U> type) {
        return getDelegate().fetchSet(fieldName, type);
    }

    @Override
    public final <U> Set<U> fetchSet(Name fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().fetchSet(fieldName, converter);
    }

    @Override
    public final <T> Set<T> fetchSet(Field<T> field) {
        return getDelegate().fetchSet(field);
    }

    @Override
    public final <U> Set<U> fetchSet(Field<?> field, Class<? extends U> type) {
        return getDelegate().fetchSet(field, type);
    }

    @Override
    public final <T, U> Set<U> fetchSet(Field<T> field, Converter<? super T, ? extends U> converter) {
        return getDelegate().fetchSet(field, converter);
    }

    @Override
    public final <U> List<U> fetchInto(Class<? extends U> type) {
        return getDelegate().fetchInto(type);
    }

    @Override
    public final <Z extends Record> Result<Z> fetchInto(Table<Z> table) {
        return getDelegate().fetchInto(table);
    }

    @Override
    public final <H extends RecordHandler<? super R>> H fetchInto(H handler) {
        return getDelegate().fetchInto(handler);
    }

    @Override
    public final void forEach(Consumer<? super R> action) {
        getDelegate().forEach(action);
    }

    @Override
    public final <E> List<E> fetch(RecordMapper<? super R, E> mapper) {
        return getDelegate().fetch(mapper);
    }

    @Override
    public final boolean hasLimit1() {
        return getDelegate().hasLimit1();
    }

    @Override
    public final RecordMapper<R, ?> mapper(int fieldIndex) {
        return getDelegate().mapper(fieldIndex);
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(int fieldIndex, Configuration configuration, Class<? extends U> type) {
        return getDelegate().mapper(fieldIndex, configuration, type);
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(int fieldIndex, Converter<?, ? extends U> converter) {
        return getDelegate().mapper(fieldIndex, converter);
    }

    @Override
    public final RecordMapper<R, Record> mapper(int[] fieldIndexes) {
        return getDelegate().mapper(fieldIndexes);
    }

    @Override
    public final RecordMapper<R, ?> mapper(String fieldName) {
        return getDelegate().mapper(fieldName);
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(String fieldName, Configuration configuration, Class<? extends U> type) {
        return getDelegate().mapper(fieldName, configuration, type);
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(String fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().mapper(fieldName, converter);
    }

    @Override
    public final RecordMapper<R, Record> mapper(String[] fieldNames) {
        return getDelegate().mapper(fieldNames);
    }

    @Override
    public final RecordMapper<R, ?> mapper(Name fieldName) {
        return getDelegate().mapper(fieldName);
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(Name fieldName, Configuration configuration, Class<? extends U> type) {
        return getDelegate().mapper(fieldName, configuration, type);
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(Name fieldName, Converter<?, ? extends U> converter) {
        return getDelegate().mapper(fieldName, converter);
    }

    @Override
    public final RecordMapper<R, Record> mapper(Name[] fieldNames) {
        return getDelegate().mapper(fieldNames);
    }

    @Override
    public final <T> RecordMapper<R, T> mapper(Field<T> field) {
        return getDelegate().mapper(field);
    }

    @Override
    public final <U> RecordMapper<R, U> mapper(Field<?> field, Configuration configuration, Class<? extends U> type) {
        return getDelegate().mapper(field, configuration, type);
    }

    @Override
    public final <T, U> RecordMapper<R, U> mapper(Field<T> field, Converter<? super T, ? extends U> converter) {
        return getDelegate().mapper(field, converter);
    }

    @Override
    public final RecordMapper<R, Record> mapper(Field<?>[] fields) {
        return getDelegate().mapper(fields);
    }

    @Override
    public final <S extends Record> RecordMapper<R, S> mapper(Table<S> table) {
        return getDelegate().mapper(table);
    }

    @Override
    public final <E> RecordMapper<R, E> mapper(Configuration configuration, Class<? extends E> type) {
        return getDelegate().mapper(configuration, type);
    }

    @Override
    public final CloseableResultQuery<R> intern(Field<?>... fields) {
        return getDelegate().intern(fields);
    }

    @Override
    public final CloseableResultQuery<R> intern(int... fieldIndexes) {
        return getDelegate().intern(fieldIndexes);
    }

    @Override
    public final CloseableResultQuery<R> intern(Name... fieldNames) {
        return getDelegate().intern(fieldNames);
    }

    @Override
    public final CloseableResultQuery<R> intern(String... fieldNames) {
        return getDelegate().intern(fieldNames);
    }

    @Override
    public final Field<?>[] fields() {
        return getDelegate().fields();
    }

    @Override
    public final Stream<Field<?>> fieldStream() {
        return getDelegate().fieldStream();
    }

    @Override
    public final <T> Field<T> field(Field<T> field) {
        return getDelegate().field(field);
    }

    @Override
    @Deprecated
    public final Field<?> field(String name) {
        return getDelegate().field(name);
    }

    @Override
    @Deprecated
    public final <T> Field<T> field(String name, Class<T> type) {
        return getDelegate().field(name, type);
    }

    @Override
    @Deprecated
    public final <T> Field<T> field(String name, DataType<T> dataType) {
        return getDelegate().field(name, dataType);
    }

    @Override
    @Deprecated
    public final Field<?> field(Name name) {
        return getDelegate().field(name);
    }

    @Override
    @Deprecated
    public final <T> Field<T> field(Name name, Class<T> type) {
        return getDelegate().field(name, type);
    }

    @Override
    @Deprecated
    public final <T> Field<T> field(Name name, DataType<T> dataType) {
        return getDelegate().field(name, dataType);
    }

    @Override
    public final Field<?> field(int index) {
        return getDelegate().field(index);
    }

    @Override
    public final <T> Field<T> field(int index, Class<T> type) {
        return getDelegate().field(index, type);
    }

    @Override
    public final <T> Field<T> field(int index, DataType<T> dataType) {
        return getDelegate().field(index, dataType);
    }

    @Override
    public final Field<?>[] fields(Field<?>... fields) {
        return getDelegate().fields(fields);
    }

    @Override
    public final Field<?>[] fields(String... names) {
        return getDelegate().fields(names);
    }

    @Override
    public final Field<?>[] fields(Name... names) {
        return getDelegate().fields(names);
    }

    @Override
    public final Field<?>[] fields(int... indexes) {
        return getDelegate().fields(indexes);
    }

    @Override
    public final int indexOf(Field<?> field) {
        return getDelegate().indexOf(field);
    }

    @Override
    public final int indexOf(String name) {
        return getDelegate().indexOf(name);
    }

    @Override
    public final int indexOf(Name name) {
        return getDelegate().indexOf(name);
    }

    @Override
    public final Class<?>[] types() {
        return getDelegate().types();
    }

    @Override
    public final Class<?> type(int index) {
        return getDelegate().type(index);
    }

    @Override
    public final Class<?> type(String name) {
        return getDelegate().type(name);
    }

    @Override
    public final Class<?> type(Name name) {
        return getDelegate().type(name);
    }

    @Override
    public final DataType<?>[] dataTypes() {
        return getDelegate().dataTypes();
    }

    @Override
    public final DataType<?> dataType(int index) {
        return getDelegate().dataType(index);
    }

    @Override
    public final DataType<?> dataType(String name) {
        return getDelegate().dataType(name);
    }

    @Override
    public final DataType<?> dataType(Name name) {
        return getDelegate().dataType(name);
    }
}
