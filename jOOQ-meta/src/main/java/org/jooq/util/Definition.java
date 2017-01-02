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

package org.jooq.util;

import java.util.List;

import org.jooq.Name;

/**
 * A general interface defining any database object, such as tables, views,
 * stored procedures, etc.
 *
 * @author Lukas Eder
 */
public interface Definition {

    /**
     * @return A reference to the Database context
     */
    Database getDatabase();

    /**
     * @return The catalog of this object.
     */
    CatalogDefinition getCatalog();

    /**
     * @return The schema of this object or <code>null</code> if this object is
     *         a {@link CatalogDefinition}.
     */
    SchemaDefinition getSchema();

    /**
     * @return The name of this object, e.g. [my_table]. This corresponds to
     *         {@link #getInputName()}
     */
    String getName();

    /**
     * @return The name of this object, e.g. [my_table], as defined in the
     *         source database.
     */
    String getInputName();

    /**
     * @return The name of this object, e.g. [my_table], as defined for the
     *         target database. This may differ from the input name if schema /
     *         table rewriting is applied.
     */
    String getOutputName();

    /**
     * @return The comment of this object
     */
    String getComment();

    /**
     * @return A path of definitions for this definition, e.g.
     *         <code>[schema].[package].[routine].[parameter]</code>
     */
    List<Definition> getDefinitionPath();

    /**
     * @return A qualified name for this object (corresponding to
     *         {@link #getName()})
     */
    String getQualifiedName();

    /**
     * @return A qualified name for this object (corresponding to
     *         {@link #getInputName()})
     */
    String getQualifiedInputName();

    /**
     * @return A qualified name for this object (corresponding to
     *         {@link #getOutputName()})
     */
    String getQualifiedOutputName();

    /**
     * @return A qualified name for this object (corresponding to
     *         {@link #getName()})
     */
    Name getQualifiedNamePart();

    /**
     * @return A qualified name for this object (corresponding to
     *         {@link #getInputName()})
     */
    Name getQualifiedInputNamePart();

    /**
     * @return A qualified name for this object (corresponding to
     *         {@link #getOutputName()})
     */
    Name getQualifiedOutputNamePart();

    /**
     * @return The overload suffix if applicable
     */
    String getOverload();
}
