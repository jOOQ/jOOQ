/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
     * Expose the {@link RecordMapper} that is used internally by this
     * <code>DAO</code> to map from records of type <code>R</code> to POJOs of
     * type <code>P</code>.
     *
     * @return the <code>DAO</code>'s underlying <code>RecordMapper</code>
     */
    RecordMapper<R, P> mapper();

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
