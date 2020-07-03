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
 *
 *
 *
 */
package org.jooq;

import java.util.Collection;
import java.util.List;

import org.jooq.exception.DataAccessException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A <code>ForeignKey</code> is an object referencing a {@link UniqueKey}. It
 * represents a <code>FOREIGN KEY</code> relationship between two tables.
 * <p>
 * Instances of this type cannot be created directly. They are available from
 * generated code.
 *
 * @param <R> The <code>FOREIGN KEY</code>'s owner table record
 * @param <O> The referenced <code>KEY</code>'s owner table record
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
public interface ForeignKey<R extends Record, O extends Record> extends Key<R> {

    /**
     * The referenced <code>UniqueKey</code>.
     */
    @NotNull
    UniqueKey<O> getKey();

    /**
     * The fields that make up the referenced <code>UniqueKey</code>.
     * <p>
     * This returns the order in which the fields of {@link #getKey()} are
     * referenced, which is usually the same as the fields of
     * {@link UniqueKey#getFields()}, but not necessarily so.
     */
    @NotNull
    List<TableField<O, ?>> getKeyFields();

    /**
     * The fields that make up the referenced <code>UniqueKey</code>.
     * <p>
     * This returns the order in which the fields of {@link #getKey()} are
     * referenced, which is usually the same as the fields of
     * {@link UniqueKey#getFieldsArray()}, but not necessarily so.
     *
     * @see #getKeyFields()
     */
    @NotNull
    TableField<O, ?>[] getKeyFieldsArray();

    /**
     * Fetch a parent record of a given record through this foreign key
     * <p>
     * This returns a parent record referenced by a given record through this
     * foreign key, as if fetching from {@link #parent(Record)}. If no parent
     * record was found, this returns <code>null</code>
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see TableRecord#fetchParent(ForeignKey)
     */
    @Nullable
    O fetchParent(R record) throws DataAccessException;

    /**
     * Fetch parent records of a given set of record through this foreign key
     * <p>
     * This returns parent records referenced by any record in a given set of
     * records through this foreign key, as if fetching from
     * {@link #parents(Record...)}.
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see TableRecord#fetchParent(ForeignKey)
     */
    @NotNull
    Result<O> fetchParents(R... records) throws DataAccessException;

    /**
     * Fetch parent records of a given set of record through this foreign key
     * <p>
     * This returns parent records referenced by any record in a given set of
     * records through this foreign key, as if fetching from
     * {@link #parents(Collection)}.
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see TableRecord#fetchParent(ForeignKey)
     */
    @NotNull
    Result<O> fetchParents(Collection<? extends R> records) throws DataAccessException;

    /**
     * Fetch child records of a given record through this foreign key
     * <p>
     * This returns childs record referencing a given record through this
     * foreign key, as if fetching from {@link #children(Record)}.
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdatableRecord#fetchChild(ForeignKey)
     * @see UpdatableRecord#fetchChildren(ForeignKey)
     */
    @NotNull
    Result<R> fetchChildren(O record) throws DataAccessException;

    /**
     * Fetch child records of a given set of records through this foreign key
     * <p>
     * This returns childs record referencing any record in a given set of
     * records through this foreign key, as if fetching from
     * {@link #children(Record...)}.
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdatableRecord#fetchChild(ForeignKey)
     * @see UpdatableRecord#fetchChildren(ForeignKey)
     */
    @NotNull
    Result<R> fetchChildren(O... records) throws DataAccessException;

    /**
     * Fetch child records of a given set of records through this foreign key
     * <p>
     * This returns childs record referencing any record in a given set of
     * records through this foreign key, as if fetching from
     * {@link #children(Collection)}.
     *
     * @throws DataAccessException if something went wrong executing the query
     * @see UpdatableRecord#fetchChild(ForeignKey)
     * @see UpdatableRecord#fetchChildren(ForeignKey)
     */
    @NotNull
    Result<R> fetchChildren(Collection<? extends O> records) throws DataAccessException;

    /**
     * Get a table expression representing the parent of a record, given this
     * foreign key.
     */
    @NotNull
    Table<O> parent(R record);

    /**
     * Get a table expression representing the parents of a record, given this
     * foreign key.
     */
    @NotNull
    Table<O> parents(R... records);

    /**
     * Get a table expression representing the parents of a record, given this
     * foreign key.
     */
    @NotNull
    Table<O> parents(Collection<? extends R> records);

    /**
     * Get a table expression representing the children of a record, given this
     * foreign key.
     */
    @NotNull
    Table<R> children(O record);

    /**
     * Get a table expression representing the children of a record, given this
     * foreign key.
     */
    @NotNull
    Table<R> children(O... records);

    /**
     * Get a table expression representing the children of a record, given this
     * foreign key.
     */
    @NotNull
    Table<R> children(Collection<? extends O> records);
}
