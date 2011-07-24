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

import java.sql.SQLException;

/**
 * A common interface for records that can be stored back to the database again.
 * <p>
 * Any {@link Record} can be {@link Updatable}, if
 * <p>
 * <ol>
 * <li>it represents a record from a table or view - a {@link TableRecord}</li>
 * <li>its underlying table or view has a "main unique key", i.e. a primary key
 * or at least one unique key</li>
 * </ol>
 * <p>
 * The "main unique key" is used by jOOQ to perform the various operations that
 * can be performed on an <code>UpdatableRecord</code>:
 * <p>
 * <ul>
 * <li> {@link #delete()} : Deleting the record</li>
 * <li> {@link #refresh()} : Refreshing the records attributes (or loading it for
 * the first time)</li>
 * <li> {@link #store()} : Storing the record to the database. This executes
 * either an <code>INSERT</code> or an <code>UPDATE</code> statement</li>
 * </ul>
 * <p>
 * <code>UpdatableRecords</code> are {@link Attachable}, which means that they
 * hold an underlying {@link Configuration} that they can be detached from. They
 * can also be instanciated without any underlying {@link Configuration}, in
 * case of which they have to be attached first, in order to be refreshed,
 * stored, or deleted.
 *
 * @param <R> The record type
 * @author Lukas Eder
 */
public interface UpdatableRecord<R extends Record> extends Updatable<R>, TableRecord<R> {

    /**
     * The table from which this record was read
     */
    @Override
    UpdatableTable<R> getTable();

    /**
     * Store this record back to the database.
     * <p>
     * Depending on the state of the primary key's or main unique key's value,
     * an <code>INSERT</code> or an <code>UPDATE</code> statement is executed.
     * <p>
     * <ul>
     * <li>If this record was created by client code, an <code>INSERT</code>
     * statement is executed</li>
     * <li>If this record was loaded by jOOQ, but the primary key value was
     * changed, an <code>INSERT</code> statement is executed. jOOQ expects that
     * primary key values will never change due to the principle of
     * normalisation in RDBMS. So if client code changes primary key values,
     * this is interpreted by jOOQ as client code wanting to duplicate this
     * record.</li>
     * <li>If this record was loaded by jOOQ, and the primary key value was not
     * changed, an <code>UPDATE</code> statement is executed.</li>
     * </ul>
     * <p>
     * In either statement, only those fields are inserted/updated, which had
     * been explicitly set by client code, in order to allow for
     * <code>DEFAULT</code> values to be applied by the underlying RDBMS. If no
     * fields were modified, neither an <code>UPDATE</code> nor an
     * <code>INSERT</code> will be executed.
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws SQLException
     */
    int store() throws SQLException;

    /**
     * Deletes this record from the database, based on the value of the primary
     * key or main unique key.
     *
     * @return <code>1</code> if the record was deleted from the database.
     *         <code>0</code> if deletion was not necessary.
     * @throws SQLException
     */
    int delete() throws SQLException;

    /**
     * Refresh this record from the database, based on the value of the primary
     * key or main unique key.
     *
     * @throws SQLException - If there is an underlying {@link SQLException} or
     *             if the record does not exist anymore in the database.
     */
    void refresh() throws SQLException;

    /**
     * Duplicate this record (in memory) and reset all fields from the primary
     * key or main unique key, such that a subsequent call to {@link #store()}
     * will result in an <code>INSERT</code> statement.
     *
     * @return A new record, distinct from <code>this</code> record.
     */
    R copy();
}
