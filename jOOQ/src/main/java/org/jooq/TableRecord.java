/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq;

import java.util.Collection;

import org.jooq.exception.DataAccessException;

/**
 * A record originating from a single table
 *
 * @param <R> The record type
 * @author Lukas Eder
 */
public interface TableRecord<R extends TableRecord<R>> extends Record {

    /**
     * The table from which this record was read.
     */
    Table<R> getTable();

    /**
     * {@inheritDoc}
     */
    @Override
    R original();

    /**
     * Store this record to the database using an <code>INSERT</code>
     * statement.
     * <p>
     * If you want to enforce statement execution, regardless if the values in
     * this record were changed, you can explicitly set the changed flags for
     * all values with {@link #changed(boolean)} or for single values with
     * {@link #changed(Field, boolean)}, prior to insertion.
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     */
    int insert() throws DataAccessException;

    /**
     * Store parts of this record to the database using an <code>INSERT</code>
     * statement.
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @see #insert()
     */
    int insert(Field<?>... fields) throws DataAccessException;

    /**
     * Store parts of this record to the database using an <code>INSERT</code>
     * statement.
     *
     * @return <code>1</code> if the record was stored to the database. <code>0
     *         </code> if storing was not necessary.
     * @throws DataAccessException if something went wrong executing the query
     * @see #insert()
     */
    int insert(Collection<? extends Field<?>> fields) throws DataAccessException;

    /**
     * Fetch a parent record of this record, given a foreign key.
     * <p>
     * This returns a parent record referenced by this record through a given
     * foreign key. If no parent record was found, this returns
     * <code>null</code>
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see ForeignKey#fetchParent(Record)
     * @see ForeignKey#fetchParents(java.util.Collection)
     * @see ForeignKey#fetchParents(Record...)
     */
    <O extends UpdatableRecord<O>> O fetchParent(ForeignKey<R, O> key) throws DataAccessException;

    /**
     * {@inheritDoc}
     */
    @Override
    <T> R with(Field<T> field, T value);

    /**
     * {@inheritDoc}
     */
    @Override
    <T, U> R with(Field<T> field, U value, Converter<? extends T, ? super U> converter);
}
