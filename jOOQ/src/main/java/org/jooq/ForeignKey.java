/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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

import org.jooq.exception.DataAccessException;

/**
 * A <code>ForeignKey</code> is an object referencing a {@link UniqueKey}. It
 * represents a <code>FOREIGN KEY</code> relationship between two tables.
 *
 * @param <R> The <code>FOREIGN KEY</code>'s owner table record
 * @param <O> The referenced <code>KEY</code>'s owner table record
 * @author Lukas Eder
 */
public interface ForeignKey<R extends Record, O extends Record> extends Key<R> {

    /**
     * The referenced <code>Key</code>
     */
    UniqueKey<O> getKey();

    /**
     * Fetch a parent record of a given record through this foreign key
     * <p>
     * This returns a parent record referenced by a given record through this
     * foreign key. If no parent record was found, this returns
     * <code>null</code>
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see TableRecord#fetchParent(ForeignKey)
     */
    O fetchParent(R record) throws DataAccessException;

    /**
     * Fetch parent records of a given set of record through this foreign key
     * <p>
     * This returns parent records referenced by any record in a given set of
     * records through this foreign key.
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see TableRecord#fetchParent(ForeignKey)
     */
    Result<O> fetchParents(R... records) throws DataAccessException;

    /**
     * Fetch parent records of a given set of record through this foreign key
     * <p>
     * This returns parent records referenced by any record in a given set of
     * records through this foreign key.
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see TableRecord#fetchParent(ForeignKey)
     */
    Result<O> fetchParents(Collection<? extends R> records) throws DataAccessException;

    /**
     * Fetch child records of a given record through this foreign key
     * <p>
     * This returns childs record referencing a given record through this
     * foreign key
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdatableRecord#fetchChild(ForeignKey)
     * @see UpdatableRecord#fetchChildren(ForeignKey)
     */
    Result<R> fetchChildren(O record) throws DataAccessException;

    /**
     * Fetch child records of a given set of records through this foreign key
     * <p>
     * This returns childs record referencing any record in a given set of
     * records through this foreign key
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdatableRecord#fetchChild(ForeignKey)
     * @see UpdatableRecord#fetchChildren(ForeignKey)
     */
    Result<R> fetchChildren(O... records) throws DataAccessException;

    /**
     * Fetch child records of a given set of records through this foreign key
     * <p>
     * This returns childs record referencing any record in a given set of
     * records through this foreign key
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdatableRecord#fetchChild(ForeignKey)
     * @see UpdatableRecord#fetchChildren(ForeignKey)
     */
    Result<R> fetchChildren(Collection<? extends O> records) throws DataAccessException;
}
