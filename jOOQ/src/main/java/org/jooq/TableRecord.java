/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import org.jooq.exception.DataAccessException;

/**
 * A record originating from a single table
 *
 * @param <R> The record type
 * @author Lukas Eder
 */
public interface TableRecord<R extends TableRecord<R>> extends Record {

    /**
     * The table from which this record was read
     */
    Table<R> getTable();

    /**
     * Store this record back to the database.
     * <p>
     * Depending on the state of the provided keys' value, an
     * <code>INSERT</code> or an <code>UPDATE</code> statement is executed.
     * <p>
     * <ul>
     * <li>If this record was created by client code, an <code>INSERT</code>
     * statement is executed</li>
     * <li>If this record was loaded by jOOQ, but the provided keys' value was
     * changed, an <code>INSERT</code> statement is executed. jOOQ expects that
     * primary key values will never change due to the principle of
     * normalisation in RDBMS. So if client code changes primary key values,
     * this is interpreted by jOOQ as client code wanting to duplicate this
     * record.</li>
     * <li>If this record was loaded by jOOQ, and the provided keys' value was
     * not changed, an <code>UPDATE</code> statement is executed.</li>
     * </ul>
     * <p>
     * In either statement, only those fields are inserted/updated, which had
     * been explicitly set by client code, in order to allow for
     * <code>DEFAULT</code> values to be applied by the underlying RDBMS. If no
     * fields were modified, neither an <code>UPDATE</code> nor an
     * <code>INSERT</code> will be executed.
     * <p>
     * Possible statements are
     * <ul>
     * <li>
     * <code><pre>
     * INSERT INTO [table] ([modified fields, including keys])
     * VALUES ([modified values, including keys])</pre></code></li>
     * <li>
     * <code><pre>
     * UPDATE [table]
     * SET [modified fields = modified values, excluding keys]
     * WHERE [key fields = key values]</pre></code></li>
     * </ul>
     *
     * @param keys The key fields used for deciding whether to execute an
     *            <code>INSERT</code> or <code>UPDATE</code> statement. If an
     *            <code>UPDATE</code> statement is executed, they are also the
     *            key fields for the <code>UPDATE</code> statement's
     *            <code>WHERE</code> clause.
     * @return The number of stored records.
     * @throws DataAccessException if something went wrong executing the query
     */
    int storeUsing(TableField<R, ?>... keys) throws DataAccessException;

    /**
     * Deletes this record from the database, based on the value of the provided
     * keys.
     * <p>
     * The executed statement is <code><pre>
     * DELETE FROM [table]
     * WHERE [key fields = key values]</pre></code>
     *
     * @param keys The key fields for the <code>DELETE</code> statement's
     *            <code>WHERE</code> clause.
     * @return The number of deleted records.
     * @throws DataAccessException if something went wrong executing the query
     */
    int deleteUsing(TableField<R, ?>... keys) throws DataAccessException;

    /**
     * Refresh this record from the database, based on the value of the provided
     * keys.
     * <p>
     * The executed statement is <code><pre>
     * SELECT * FROM [table]
     * WHERE [key fields = key values]</pre></code>
     *
     * @param keys The key fields for the <code>SELECT</code> statement's
     *            <code>WHERE</code> clause.
     * @throws DataAccessException This exception is thrown if
     *             <ul>
     *             <li>something went wrong executing the query</li> <li>the
     *             record does not exist anymore in the database</li> <li>the
     *             provided keys return several records.</li>
     *             </ul>
     */
    void refreshUsing(TableField<R, ?>... keys) throws DataAccessException;
}
