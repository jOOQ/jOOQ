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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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

package org.jooq.meta;

/**
 * An interface defining a column of an embeddable type.
 *
 * @author Lukas Eder
 */
public interface EmbeddableColumnDefinition extends TypedElementDefinition<EmbeddableDefinition>, PositionedDefinition {

    /**
     * The referencing column definition.
     */
    ColumnDefinition getReferencingColumn();

    /**
     * The data type definition of the embeddable.
     * <p>
     * This may be different from the data type definition of the
     * {@link #getReferencingColumn()}, depending on what
     * {@link #merge(EmbeddableColumnDefinition)} produced as a result e.g. of
     * merging multiple distinct nullabilities.
     */
    @Override
    DataTypeDefinition getType();

    /**
     * The {@link PositionedDefinition#getPosition()} of
     * {@link #getReferencingColumn()} within its defining table.
     * <p>
     * [#17434] While in most RDBMS, this value is 1-based and a consecutive
     * ordinal, there may be cases where a positional value is skipped, either
     * by what's reported by the RDBMS's dictionary views, or because the
     * definition is filtered by jOOQ-meta. Users shouldn't rely on the
     * positions reflecting the actually generated column/attribute/etc.
     * position.
     */
    int getReferencingColumnPosition();

    /**
     * Merge type information of another referencing
     * {@link EmbeddableColumnDefinition} into this
     * {@link EmbeddableColumnDefinition}.
     * <p>
     * Multiple referencing column definitions can be used to define an
     * embeddable column definition. The types and constraints on these
     * alternative references may differ. For example, one column may be
     * nullable, others may not be nullable.
     */
    void merge(EmbeddableColumnDefinition other);

}
