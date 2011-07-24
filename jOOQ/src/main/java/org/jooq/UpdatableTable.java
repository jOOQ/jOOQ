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

import java.util.List;

/**
 * A common interface for tables whose records can be stored back to the
 * database again.
 *
 * @param <R> The record type of the table
 * @see UpdatableRecord
 * @author Lukas Eder
 */
public interface UpdatableTable<R extends Record> extends Updatable<R>, Table<R> {

    /**
     * Retrieve the table's <code>IDENTITY</code> information, if available.
     * <p>
     * With SQL:2003, the concept of <code>IDENTITY</code> columns was
     * introduced in most RDBMS. These are special kinds of columns that have
     * auto-increment functionality when <code>INSERT</code> statements are
     * performed. An <code>IDENTITY</code> column must be part of the
     * <code>PRIMARY KEY</code> or of a <code>UNIQUE KEY</code> in the table.
     * There can only be at most one <code>IDENTITY</code> column.
     * <p>
     * Note: Unfortunately, this is not supported in the Oracle dialect, where
     * identities simulated by triggers cannot be formally detected.
     *
     * @return The table's <code>IDENTITY</code> information, or
     *         <code>null</code>, if no such information is available.
     */
    Identity<R, ? extends Number> getIdentity();

    /**
     * Retrieve the table's main unique key. If there exists a
     * <code>PRIMARY KEY</code> in the table, the <code>PRIMARY KEY</code> is
     * returned. Otherwise, the most suitable <code>UNIQUE KEY</code> is
     * returned.
     *
     * @return The main key. This is never <code>null</code> because
     *         {@link UpdatableTable}'s always have at least one key.
     */
    UniqueKey<R> getMainKey();

    /**
     * Retrieve all of the table's unique keys.
     *
     * @return All keys. This is never <code>null</code> or empty, because
     *         {@link UpdatableTable}'s always have at least one key.
     */
    List<UniqueKey<R>> getKeys();

    /**
     * Get a list of <code>FOREIGN KEY</code>'s of a specific table, referencing
     * a this table.
     *
     * @param <O> The other table's record type
     * @param other The other table of the foreign key relationship
     * @return Some other table's <code>FOREIGN KEY</code>'s towards an this
     *         table. This is never <code>null</code>.
     */
    <O extends Record> List<ForeignKey<O, R>> getReferencesFrom(Table<O> other);

}
