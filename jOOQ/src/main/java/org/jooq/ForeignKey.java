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
