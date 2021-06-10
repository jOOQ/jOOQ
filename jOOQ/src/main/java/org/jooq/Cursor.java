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
package org.jooq;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;
import org.jooq.impl.DefaultRecordMapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Cursors allow for lazy, sequential access to an underlying JDBC
 * {@link ResultSet}. Unlike {@link Result}, data can only be accessed
 * sequentially, using an {@link Iterator}, or the cursor's {@link #hasNext()}
 * and {@link #fetch()} methods.
 * <p>
 * Client code must close this {@link Cursor} in order to close the underlying
 * {@link PreparedStatement} and {@link ResultSet}
 * <p>
 * The cursor can be consumed in two ways:
 * <ul>
 * <li>Record by record: Such methods can be recognised by the term "Next" in
 * the method name, e.g. {@link #fetchNext(int)}.</li>
 * <li>Completely in one go: Such methods do not have the term "Next" in their
 * method names, e.g. {@link #fetch()}.</li>
 * </ul>
 * <p>
 * Note: Unlike usual implementations of {@link Iterable}, a <code>Cursor</code>
 * can only provide one {@link Iterator}!
 *
 * @param <R> The cursor's record type
 * @author Lukas Eder
 */
public interface Cursor<R extends Record> extends Fields, Iterable<R>, Formattable, AutoCloseable {

    /**
     * Get this cursor's row type.
     */
    @NotNull
    RecordType<R> recordType();

    /**
     * Check whether this cursor has a next record.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    boolean hasNext() throws DataAccessException;

    /**
     * Fetch all remaining records as a result.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     * <p>
     * The result and its contained records are attached to the original
     * {@link Configuration} by default. Use {@link Settings#isAttachRecords()}
     * to override this behaviour.
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @NotNull
    Result<R> fetch() throws DataAccessException;

    /**
     * @deprecated - 3.10 - [#6363] - Use {@link #fetchNext(int)} instead.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "3.10")
    Result<R> fetch(int number) throws DataAccessException;

    /**
     * Fetch the next couple of records from the cursor.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     * <p>
     * The result and its contained records are attached to the original
     * {@link Configuration} by default. Use {@link Settings#isAttachRecords()}
     * to override this behaviour.
     *
     * @param number The number of records to fetch. If this is <code>0</code>
     *            or negative an empty list is returned, the cursor is
     *            untouched. If this is greater than the number of remaining
     *            records, then all remaining records are returned.
     * @throws DataAccessException if something went wrong executing the query
     */
    @NotNull
    Result<R> fetchNext(int number) throws DataAccessException;

    /**
     * Fetch results into a custom handler callback.
     * <p>
     * The resulting records are attached to the original {@link Configuration}
     * by default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @param handler The handler callback
     * @return Convenience result, returning the parameter handler itself
     * @throws DataAccessException if something went wrong executing the query
     * @deprecated - 3.15.0 - [#11902] - Use {@link Iterable#forEach(Consumer)}
     *             based methods, instead.
     */
    @Deprecated(forRemoval = true, since = "3.15")
    @NotNull
    <H extends RecordHandler<? super R>> H fetchInto(H handler) throws DataAccessException;

    /**
     * Fetch results into a custom mapper callback.
     *
     * @param mapper The mapper callback
     * @return The custom mapped records
     * @throws DataAccessException if something went wrong executing the query
     */
    @NotNull
    <E> List<E> fetch(RecordMapper<? super R, E> mapper) throws DataAccessException;

    /**
     * Map resulting records onto a custom type.
     * <p>
     * This is the same as calling <code>fetch().into(type)</code>. See
     * {@link Record#into(Class)} for more details
     *
     * @param <E> The generic entity type.
     * @param type The entity type.
     * @see Record#into(Class)
     * @see Result#into(Class)
     * @throws DataAccessException if something went wrong executing the query
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    @NotNull
    <E> List<E> fetchInto(Class<? extends E> type) throws DataAccessException, MappingException;

    /**
     * Map resulting records onto a custom record.
     * <p>
     * This is the same as calling <code>fetch().into(table)</code>. See
     * {@link Record#into(Class)} for more details
     * <p>
     * The result and its contained records are attached to the original
     * {@link Configuration} by default. Use {@link Settings#isAttachRecords()}
     * to override this behaviour.
     *
     * @param <Z> The generic table record type.
     * @param table The table type.
     * @see Record#into(Class)
     * @see Result#into(Class)
     * @throws DataAccessException if something went wrong executing the query
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     */
    @NotNull
    <Z extends Record> Result<Z> fetchInto(Table<Z> table) throws DataAccessException, MappingException;

    /**
     * @deprecated - 3.10 - [#6363] - Use {@link #fetchNext()} instead.
     */
    @Nullable
    @Deprecated(forRemoval = true, since = "3.10")
    R fetchOne() throws DataAccessException;

    /**
     * @deprecated - 3.10 - [#6363] - Use {@link #fetchNextInto(RecordHandler)} instead.
     * @deprecated - 3.15.0 - [#11902] - Use {@link Iterable#forEach(Consumer)}
     *             based methods, instead.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "3.10")
    <H extends RecordHandler<? super R>> H fetchOneInto(H handler) throws DataAccessException;

    /**
     * @deprecated - 3.10 - [#6363] - Use {@link #fetchNext(RecordMapper)} instead.
     */
    @Nullable
    @Deprecated(forRemoval = true, since = "3.10")
    <E> E fetchOne(RecordMapper<? super R, E> mapper) throws DataAccessException;

    /**
     * @deprecated - 3.10 - [#6363] - Use {@link #fetchNextInto(Table)} instead.
     */
    @Nullable
    @Deprecated(forRemoval = true, since = "3.10")
    <Z extends Record> Z fetchOneInto(Table<Z> table) throws DataAccessException, MappingException;

    /**
     * Fetch the next record from the cursor.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     * <p>
     * The resulting record is attached to the original {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @return The next record from the cursor, or <code>null</code> if there is
     *         no next record.
     * @throws DataAccessException if something went wrong executing the query
     */
    @Nullable
    R fetchNext() throws DataAccessException;

    /**
     * Fetch the next record into a custom handler callback.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     * <p>
     * The resulting record is attached to the original {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @param handler The handler callback
     * @return Convenience result, returning the parameter handler itself
     * @throws DataAccessException if something went wrong executing the query
     * @deprecated - 3.15.0 - [#11902] - Use {@link Iterable#forEach(Consumer)}
     *             based methods, instead.
     */
    @Deprecated(forRemoval = true, since = "3.15")
    @NotNull
    <H extends RecordHandler<? super R>> H fetchNextInto(H handler) throws DataAccessException;

    /**
     * @deprecated - 3.10 - [#6363] - Use {@link #fetchNextInto(Class)} instead.
     */
    @Nullable
    @Deprecated(forRemoval = true, since = "3.10")
    <E> E fetchOneInto(Class<? extends E> type) throws DataAccessException, MappingException;

    /**
     * Map the next resulting record onto a custom type.
     * <p>
     * This is the same as calling <code>fetchOne().into(type)</code>. See
     * {@link Record#into(Class)} for more details
     *
     * @param <E> The generic entity type.
     * @param type The entity type.
     * @see Record#into(Class)
     * @see Result#into(Class)
     * @throws DataAccessException if something went wrong executing the query
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    @Nullable
    <E> E fetchNextInto(Class<? extends E> type) throws DataAccessException, MappingException;

    /**
     * Fetch the next record into a custom mapper callback.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     *
     * @param mapper The mapper callback
     * @return The custom mapped record
     * @throws DataAccessException if something went wrong executing the query
     */
    @Nullable
    <E> E fetchNext(RecordMapper<? super R, E> mapper) throws DataAccessException;

    /**
     * Map the next resulting record onto a custom record.
     * <p>
     * This is the same as calling <code>fetchOne().into(table)</code>. See
     * {@link Record#into(Class)} for more details
     * <p>
     * The resulting record is attached to the original {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @param <Z> The generic table record type.
     * @param table The table type.
     * @see Record#into(Class)
     * @see Result#into(Class)
     * @throws DataAccessException if something went wrong executing the query
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     */
    @Nullable
    <Z extends Record> Z fetchNextInto(Table<Z> table) throws DataAccessException, MappingException;

    /**
     * @deprecated - 3.10 - [#6363] - Use {@link #fetchNextOptional()} instead.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "3.10")
    Optional<R> fetchOptional() throws DataAccessException;

    /**
     * @deprecated - 3.10 - [#6363] - Use {@link #fetchNextOptionalInto(Class)} instead.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "3.10")
    <E> Optional<E> fetchOptionalInto(Class<? extends E> type) throws DataAccessException, MappingException;

    /**
     * @deprecated - 3.10 - [#6363] - Use {@link #fetchNextOptional(RecordMapper)} instead.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "3.10")
    <E> Optional<E> fetchOptional(RecordMapper<? super R, E> mapper) throws DataAccessException;

    /**
     * @deprecated - 3.10 - [#6363] - Use {@link #fetchNextOptionalInto(Table)} instead.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "3.10")
    <Z extends Record> Optional<Z> fetchOptionalInto(Table<Z> table) throws DataAccessException, MappingException;

    /**
     * Fetch the next record from the cursor.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     * <p>
     * The resulting record is attached to the original {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @return The next record from the cursor
     * @throws DataAccessException if something went wrong executing the query
     */
    @NotNull
    Optional<R> fetchNextOptional() throws DataAccessException;

    /**
     * Map the next resulting record onto a custom type.
     * <p>
     * This is the same as calling <code>fetchOne().into(type)</code>. See
     * {@link Record#into(Class)} for more details
     *
     * @param <E> The generic entity type.
     * @param type The entity type.
     * @see Record#into(Class)
     * @see Result#into(Class)
     * @throws DataAccessException if something went wrong executing the query
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     * @see DefaultRecordMapper
     */
    @NotNull
    <E> Optional<E> fetchNextOptionalInto(Class<? extends E> type) throws DataAccessException, MappingException;

    /**
     * Fetch the next record into a custom mapper callback.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     *
     * @param mapper The mapper callback
     * @return The custom mapped record
     * @throws DataAccessException if something went wrong executing the query
     */
    @NotNull
    <E> Optional<E> fetchNextOptional(RecordMapper<? super R, E> mapper) throws DataAccessException;

    /**
     * Map the next resulting record onto a custom record.
     * <p>
     * This is the same as calling <code>fetchOne().into(table)</code>. See
     * {@link Record#into(Class)} for more details
     * <p>
     * The resulting record is attached to the original {@link Configuration} by
     * default. Use {@link Settings#isAttachRecords()} to override this
     * behaviour.
     *
     * @param <Z> The generic table record type.
     * @param table The table type.
     * @see Record#into(Class)
     * @see Result#into(Class)
     * @throws DataAccessException if something went wrong executing the query
     * @throws MappingException wrapping any reflection or data type conversion
     *             exception that might have occurred while mapping records
     */
    @NotNull
    <Z extends Record> Optional<Z> fetchNextOptionalInto(Table<Z> table) throws DataAccessException, MappingException;

    /**
     * Turn this <code>Cursor</code> into a {@link Stream}.
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @NotNull
    Stream<R> stream() throws DataAccessException;

    /**
     * Reduce the execution results of this query using a {@link Collector}.
     * <p>
     * This works in the same way as calling the following code:
     *
     * <pre>
     * <code>
     * cursor.stream().collect(collector);
     * </code>
     * </pre>
     *
     * @param collector The collector that collects all records and accumulates
     *            them into a result type.
     * @return The result of the collection.
     * @throws DataAccessException if something went wrong executing the query
     */
    <X, A> X collect(Collector<? super R, A, X> collector) throws DataAccessException;

    /**
     * Explicitly close the underlying {@link PreparedStatement} and
     * {@link ResultSet}.
     * <p>
     * If you fetch all records from the underlying {@link ResultSet}, jOOQ
     * <code>Cursor</code> implementations will close themselves for you.
     * Calling <code>close()</code> again will have no effect.
     *
     * @throws DataAccessException if something went wrong executing the query
     */
    @Override
    void close() throws DataAccessException;

    /**
     * Check whether this <code>Cursor</code> has been explicitly or
     * "conveniently" closed.
     * <p>
     * Explicit closing can be achieved by calling {@link #close()} from client
     * code. "Convenient" closing is done by any of the other methods, when the
     * last record was fetched.
     */
    boolean isClosed();

    /**
     * Get the <code>Cursor</code>'s underlying {@link ResultSet}.
     * <p>
     * This will return a {@link ResultSet} wrapping the JDBC driver's
     * <code>ResultSet</code>. Closing this <code>ResultSet</code> may close the
     * producing {@link Statement} or {@link PreparedStatement}, depending on
     * your setting for {@link ResultQuery#keepStatement(boolean)}.
     * <p>
     * Modifying this <code>ResultSet</code> will affect this
     * <code>Cursor</code>.
     *
     * @return The underlying <code>ResultSet</code>. May be <code>null</code>,
     *         for instance when the <code>Cursor</code> is closed.
     */
    @Nullable
    ResultSet resultSet();
}
