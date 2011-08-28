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

import java.util.Collection;
import java.util.Map;

/**
 * A query for data insertion
 *
 * @param <R> The record type of the table being inserted into
 * @author Lukas Eder
 */
public interface InsertQuery<R extends TableRecord<R>> extends StoreQuery<R>, Insert {

    /**
     * Adds a new Record to the insert statement for multi-record inserts
     * <p>
     * Calling this method will cause subsequent calls to
     * {@link #addValue(Field, Object)} (and similar) to fill the next record.
     * <p>
     * If this call is not followed by {@link #addValue(Field, Object)} calls,
     * then this call has no effect.
     * <p>
     * If this call is done on a fresh insert statement (without any values
     * yet), then this call has no effect either.
     */
    void newRecord();

    /**
     * Short for calling <code>
     * newRecord();
     * setRecord(record);
     * </code>
     *
     * @param record The record to add to this insert statement.
     */
    void addRecord(R record);

    /**
     * Whether a <code>ON DUPLICATE KEY UPDATE</code> clause should be added to
     * this <code>INSERT</code> statement.
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    void onDuplicateKeyUpdate(boolean flag);

    /**
     * Add a value to the <code>ON DUPLICATE KEY UPDATE</code> clause of this
     * <code>INSERT</code> statement, where this is supported.
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    void addValueForUpdate(Field<?> field, Object value);

    /**
     * Add a value to the <code>ON DUPLICATE KEY UPDATE</code> clause of this
     * <code>INSERT</code> statement, where this is supported.
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    void addValueForUpdate(Field<?> field, Field<?> value);

    /**
     * Add multiple values to the <code>ON DUPLICATE KEY UPDATE</code> clause of
     * this <code>INSERT</code> statement, where this is supported.
     * <p>
     * Please assure that key/value pairs have matching <code>&lt;T&gt;</code>
     * types. Values can either be of type <code>&lt;T&gt;</code> or
     * <code>Field&lt;T&gt;</code>
     *
     * @see InsertOnDuplicateStep#onDuplicateKeyUpdate()
     */
    void addValuesForUpdate(Map<? extends Field<?>, ?> map);

    /**
     * Configure the <code>INSERT</code> statement to return all fields in
     * <code>R</code>.
     *
     * @see #getReturned()
     */
    void setReturning();

    /**
     * Configure the <code>INSERT</code> statement to return the generated
     * identity value.
     *
     * @param identity The table's identity
     * @see #getReturned()
     */
    void setReturning(Identity<R, ? extends Number> identity);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     *
     * @param fields Fields to be returned
     * @see #getReturned()
     */
    void setReturning(Field<?>... fields);

    /**
     * Configure the <code>INSERT</code> statement to return a list of fields in
     * <code>R</code>.
     *
     * @param fields Fields to be returned
     * @see #getReturned()
     */
    void setReturning(Collection<? extends Field<?>> fields);

    /**
     * The record holding returned values as specified by any of the
     * {@link #setReturning()} methods.
     * <p>
     * This implemented differently for every dialect:
     * <ul>
     * <li>Postgres has native support for <code>INSERT .. RETURNING</code>
     * clauses</li>
     * <li>HSQLDB, Oracle, and DB2 JDBC drivers allow for retrieving any table
     * column as "generated key" in one statement</li>
     * <li>Derby, H2, MySQL, SQL Server only allow for retrieving IDENTITY
     * column values as "generated key". If other fields are requested, a second
     * statement is issued. Client code must assure transactional integrity
     * between the two statements.</li>
     * <li>Ingres support will be added with #808</li>
     * <li>Sybase support will be added with #809</li>
     * <li>SQLite support will be added with #810</li>
     * </ul>
     */
    R getReturned();
}
