/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Cursors allow for lazy, sequential access to an underlying JDBC
 * {@link ResultSet}. Unlike {@link Result}, data can only be accessed
 * sequentially, using an {@link Iterator}, or the cursor's {@link #hasNext()}
 * and {@link #fetch()} methods.
 * <p>
 * Client code must close this {@link Cursor} in order to close the underlying
 * {@link PreparedStatement} and {@link ResultSet}
 *
 * @param <R> The cursor's record type
 * @author Lukas Eder
 */
public interface Cursor<R extends Record> extends FieldProvider, Iterable<R> {

    /**
     * Check whether this cursor has a next record
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     */
    boolean hasNext() throws SQLException;

    /**
     * Fetch all remaining records as a result.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     */
    Result<R> fetch() throws SQLException;

    /**
     * Fetch the next couple of records from the cursor.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     *
     * @param number The number of records to fetch. If this is <code>0</code>
     *            or negative an empty list is returned, the cursor is
     *            untouched. If this is greater than the number of remaining
     *            records, then all remaining records are returned.
     */
    Result<R> fetch(int number) throws SQLException;

    /**
     * Fetch the next record from the cursor
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     *
     * @return The next record from the cursor, or <code>null</code> if there is
     *         no next record.
     */
    R fetchOne() throws SQLException;

    /**
     * Fetch all remaining records as a result.
     * <p>
     * This will conveniently close the <code>Cursor</code>, after the last
     * <code>Record</code> was fetched.
     *
     * @deprecated - 1.6.8 [#849] - Use {@link #fetch()} instead
     */
    @Deprecated
    Result<R> fetchResult() throws SQLException;

    /**
     * Fetch some records as a result
     * <p>
     * This will conveniently close the <code>Cursor</code>, if the last
     * <code>Record</code> was fetched.
     *
     * @param number The number of records to fetch. If this is <code>0</code>
     *            or negative an empty result is returned, the cursor is
     *            untouched. If this is greater than the number of remaining
     *            records, then all remaining records are returned.
     *
     * @deprecated - 1.6.8 [#849] - Use {@link #fetch(int)} instead
     */
    @Deprecated
    Result<R> fetchResult(int number) throws SQLException;

    /**
     * Fetch results into a custom handler callback
     *
     * @param handler The handler callback
     * @return Convenience result, returning the parameter handler itself
     */
    RecordHandler<R> fetchInto(RecordHandler<R> handler) throws SQLException;

    /**
     * Explicitly close the underlying {@link PreparedStatement} and
     * {@link ResultSet}
     * <p>
     * If you fetch all records from the underlying {@link ResultSet}, jOOQ
     * <code>Cursor</code> implementations will close themselves for you.
     * Calling <code>close()</code> again will have no effect.
     */
    void close() throws SQLException;

    /**
     * Check whether this <code>Cursor</code> has been explicitly or
     * "conveniently" closed.
     * <p>
     * Explicit closing can be achieved by calling {@link #close()} from client
     * code. "Convenient" closing is done by any of the other methods, when the
     * last record was fetched.
     */
    boolean isClosed();
}
