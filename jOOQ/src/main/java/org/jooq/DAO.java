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
import java.util.List;

import org.jooq.exception.DataAccessException;

/**
 * A generic DAO interface for a pojo and a primary key type.
 * <p>
 * This type is implemented by generated DAO classes to provide a common API for
 * common actions on POJOs
 *
 * @author Lukas Eder
 * @param <R> The generic record type
 * @param <P> The generic POJO type
 * @param <T> The generic primary key type
 */
public interface DAO<R extends TableRecord<R>, P, T> {

    /**
     * Expose the configuration in whose context this <code>DAO</code> is
     * operating.
     *
     * @return the <code>DAO</code>'s underlying <code>Configuration</code>
     */
    Configuration configuration();

    /**
     * Performs an <code>INSERT</code> statement for a given POJO
     *
     * @param object The POJO to be inserted
     * @throws DataAccessException if something went wrong executing the query
     */
    void insert(P object) throws DataAccessException;

    /**
     * Performs a batch <code>INSERT</code> statement for a given set of POJOs
     *
     * @param objects The POJOs to be inserted
     * @throws DataAccessException if something went wrong executing the query
     * @see #insert(Collection)
     */
    void insert(P... objects) throws DataAccessException;

    /**
     * Performs a batch <code>INSERT</code> statement for a given set of POJOs
     *
     * @param objects The POJOs to be inserted
     * @throws DataAccessException if something went wrong executing the query
     * @see #insert(Object...)
     */
    void insert(Collection<P> objects) throws DataAccessException;

    /**
     * Performs an <code>UPDATE</code> statement for a given POJO
     *
     * @param object The POJO to be updated
     * @throws DataAccessException if something went wrong executing the query
     */
    void update(P object) throws DataAccessException;

    /**
     * Performs a batch <code>UPDATE</code> statement for a given set of POJOs
     *
     * @param objects The POJOs to be updated
     * @throws DataAccessException if something went wrong executing the query
     * @see #update(Collection)
     */
    void update(P... objects) throws DataAccessException;

    /**
     * Performs a batch <code>UPDATE</code> statement for a given set of POJOs
     *
     * @param objects The POJOs to be updated
     * @throws DataAccessException if something went wrong executing the query
     * @see #update(Object...)
     */
    void update(Collection<P> objects) throws DataAccessException;

    /**
     * Performs a <code>DELETE</code> statement for a given set of POJOs
     *
     * @param objects The POJOs to be deleted
     * @throws DataAccessException if something went wrong executing the query
     * @see #delete(Collection)
     */
    void delete(P... objects) throws DataAccessException;

    /**
     * Performs a <code>DELETE</code> statement for a given set of POJOs
     *
     * @param objects The POJOs to be deleted
     * @throws DataAccessException if something went wrong executing the query
     * @see #delete(Object...)
     */
    void delete(Collection<P> objects) throws DataAccessException;

    /**
     * Performs a <code>DELETE</code> statement for a given set of IDs
     *
     * @param ids The IDs to be deleted
     * @throws DataAccessException if something went wrong executing the query
     * @see #delete(Collection)
     */
    void deleteById(T... ids) throws DataAccessException;

    /**
     * Performs a <code>DELETE</code> statement for a given set of IDs
     *
     * @param ids The IDs to be deleted
     * @throws DataAccessException if something went wrong executing the query
     * @see #delete(Object...)
     */
    void deleteById(Collection<T> ids) throws DataAccessException;

    /**
     * Checks if a given POJO exists
     *
     * @param object The POJO whose existence is checked
     * @return Whether the POJO already exists
     * @throws DataAccessException if something went wrong executing the query
     */
    boolean exists(P object) throws DataAccessException;

    /**
     * Checks if a given ID exists
     *
     * @param id The ID whose existence is checked
     * @return Whether the ID already exists
     * @throws DataAccessException if something went wrong executing the query
     */
    boolean existsById(T id) throws DataAccessException;

    /**
     * Count all records of the underlying table.
     *
     * @return The number of records of the underlying table
     * @throws DataAccessException if something went wrong executing the query
     */
    long count() throws DataAccessException;

    /**
     * Find all records of the underlying table.
     *
     * @return All records of the underlying table
     * @throws DataAccessException if something went wrong executing the query
     */
    List<P> findAll() throws DataAccessException;

    /**
     * Find a record of the underlying table by ID.
     *
     * @param id The ID of a record in the underlying table
     * @return A record of the underlying table given its ID, or
     *         <code>null</code> if no record was found.
     * @throws DataAccessException if something went wrong executing the query
     */
    P findById(T id) throws DataAccessException;

    /**
     * Find records by a given field and a set of values.
     *
     * @param field The field to compare values against
     * @param values The accepted values
     * @return A list of records fulfilling <code>field IN (values)</code>
     * @throws DataAccessException if something went wrong executing the query
     */
    <Z> List<P> fetch(Field<Z> field, Z... values) throws DataAccessException;

    /**
     * Find a unique record by a given field and a value.
     *
     * @param field The field to compare value against
     * @param value The accepted value
     * @return A record fulfilling <code>field = value</code>, or
     *         <code>null</code>
     * @throws DataAccessException This exception is thrown
     *             <ul>
     *             <li>if something went wrong executing the query</li>
     *             <li>if the query returned more than one value</li>
     *             </ul>
     */
    <Z> P fetchOne(Field<Z> field, Z value) throws DataAccessException;

    /**
     * Get the underlying table
     */
    Table<R> getTable();

    /**
     * Get the underlying POJO type
     */
    Class<P> getType();
}
