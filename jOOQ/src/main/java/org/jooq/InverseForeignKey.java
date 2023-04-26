/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An <code>InverseForeignKey</code> is an inverse {@link ForeignKey}. It
 * represents an inverse <code>FOREIGN KEY</code> relationship between two
 * tables.
 * <p>
 * Instances of this type cannot be created directly. They are available from
 * generated code.
 *
 * @param <PARENT> The referenced <code>KEY</code>'s owner table record
 * @param <CHILD> The <code>FOREIGN KEY</code>'s owner table record
 * @author Lukas Eder
 */
public interface InverseForeignKey<PARENT extends Record, CHILD extends Record> extends Key<PARENT> {

    /**
     * The referenced <code>UniqueKey</code>.
     */
    @NotNull
    ForeignKey<CHILD, PARENT> getForeignKey();

    /**
     * The fields that make up the referenced <code>UniqueKey</code>.
     * <p>
     * This returns the order in which the fields of {@link #getKey()} are
     * referenced, which is usually the same as the fields of
     * {@link UniqueKey#getFields()}, but not necessarily so.
     */
    @NotNull
    List<TableField<CHILD, ?>> getForeignKeyFields();

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
    TableField<CHILD, ?> @NotNull [] getForeignKeyFieldsArray();
}
