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

package org.jooq.meta;

import java.util.List;

/**
 * The definition of an embeddable type.
 *
 * @author Lukas Eder
 */
public interface EmbeddableDefinition extends TableElementDefinition {

    /**
     * The table defining the embeddable (same as {@link #getTable()}).
     */
    TableDefinition getDefiningTable();

    /**
     * The referencing comment of this embeddable, if it differs from the
     * defining name ({@link #getComment()}).
     */
    String getReferencingComment();

    /**
     * The referencing name of this embeddable, if it differs from the defining
     * name ({@link #getName()}).
     */
    String getReferencingName();

    /**
     * The referencing input name of this embeddable, if it differs from the
     * defining name ({@link #getInputName()}).
     */
    String getReferencingInputName();

    /**
     * The referencing output name of this embeddable, if it differs from the
     * defining name ({@link #getOutputName()}).
     */
    String getReferencingOutputName();

    /**
     * The table referencing the embeddable.
     */
    TableDefinition getReferencingTable();

    /**
     * All defining columns in the type, table or view.
     */
    List<EmbeddableColumnDefinition> getColumns();

    /**
     * Get a defining column in this type by its name.
     */
    EmbeddableColumnDefinition getColumn(String columnName);

    /**
     * Get a defining column in this type by its name.
     */
    EmbeddableColumnDefinition getColumn(String columnName, boolean ignoreCase);

    /**
     * Get a defining column in this type by its index (starting at 0).
     */
    EmbeddableColumnDefinition getColumn(int columnIndex);

    /**
     * Get a referencing column in this type by its referencing name.
     */
    EmbeddableColumnDefinition getReferencingColumn(String columnName);

    /**
     * Get a referencing column in this type by its referencing name.
     */
    EmbeddableColumnDefinition getReferencingColumn(String columnName, boolean ignoreCase);

    /**
     * Whether this embeddable replaces the fields it represents.
     */
    boolean replacesFields();

}
