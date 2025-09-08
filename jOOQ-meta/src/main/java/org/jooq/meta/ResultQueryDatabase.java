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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import java.math.BigDecimal;
import java.util.List;

import org.jooq.DataType;
import org.jooq.Meta;
import org.jooq.Record12;
import org.jooq.Record14;
import org.jooq.Record15;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Record6;
import org.jooq.ResultQuery;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

/**
 * An interface for all {@link AbstractDatabase} implementations that can
 * produce {@link ResultQuery} objects to query meta data.
 * <p>
 * These queries will be used to generate some internal queries in the core
 * library's {@link Meta} API. The return types of the various methods are
 * subject to change and should not be relied upon.
 *
 * @author Lukas Eder
 */
@Internal
public interface ResultQueryDatabase extends Database {

    /**
     * A query that produces primary keys for a set of input schemas.
     * <p>
     * The resulting columns are:
     * <ol>
     * <li>Catalog name</li>
     * <li>Schema name</li>
     * <li>Table name</li>
     * <li>Constraint name</li>
     * <li>Column name</li>
     * <li>Column sequence</li>
     * </ol>
     *
     * @return The query or <code>null</code> if this implementation doesn't support the query.
     */
    @Internal
    @Nullable
    ResultQuery<Record6<String, String, String, String, String, Integer>> primaryKeys(List<String> schemas);

    /**
     * A query that produces (non-primary) unique keys for a set of input
     * schemas.
     * <p>
     * The resulting columns are:
     * <ol>
     * <li>Catalog name</li>
     * <li>Schema name</li>
     * <li>Table name</li>
     * <li>Constraint name</li>
     * <li>Column name</li>
     * <li>Column sequence</li>
     * </ol>
     *
     * @return The query or <code>null</code> if this implementation doesn't support the query.
     */
    @Internal
    @Nullable
    ResultQuery<Record6<String, String, String, String, String, Integer>> uniqueKeys(List<String> schemas);

    /**
     * A query that produces sequences for a set of input schemas.
     * <p>
     * The resulting columns are:
     * <ol>
     * <li>Catalog name</li>
     * <li>Schema name</li>
     * <li>Sequence name</li>
     * <li>Data type name</li>
     * <li>Data type precision</li>
     * <li>Data type scale</li>
     * <li>Start value</li>
     * <li>Increment</li>
     * <li>Min value</li>
     * <li>Max value</li>
     * <li>Cycle</li>
     * <li>Cache</li>
     * </ol>
     *
     * @return The query or <code>null</code> if this implementation doesn't support the query.
     */
    @Internal
    @Nullable
    ResultQuery<Record12<String, String, String, String, Integer, Integer, Long, Long, BigDecimal, BigDecimal, Boolean, Long>> sequences(List<String> schemas);

    /**
     * A query that produces enum types and their literals for a set of input schemas.
     * <p>
     * The resulting columns are:
     * <ol>
     * <li>Catalog name</li>
     * <li>Schema name</li>
     * <li>Column name (if applicable, e.g. in MySQL style RDBMS)</li>
     * <li>Enum type name (if applicable, e.g. in PostgreSQL style RDBMS)</li>
     * <li>Literal value</li>
     * <li>Literal position</li>
     * </ol>
     *
     * @return The query or <code>null</code> if this implementation doesn't support the query.
     */
    @Internal
    @Nullable
    ResultQuery<Record6<String, String, String, String, String, Integer>> enums(List<String> schemas);

    /**
     * A query that produces source code for a set of input schemas.
     * <p>
     * The resulting columns are:
     * <ol>
     * <li>Catalog name</li>
     * <li>Schema name</li>
     * <li>Object name (e.g. table, view, function, package)</li>
     * <li>Source</li>
     * </ol>
     *
     * @return The query or <code>null</code> if this implementation doesn't support the query.
     */
    @Internal
    @Nullable
    ResultQuery<Record4<String, String, String, String>> sources(List<String> schemas);

    /**
     * A query that produces comments for a set of input schemas.
     * <p>
     * The resulting columns are:
     * <ol>
     * <li>Catalog name</li>
     * <li>Schema name</li>
     * <li>Object name (e.g. table, view, function, package)</li>
     * <li>Object sub name (e.g. column, package routine)</li>
     * <li>Comment</li>
     * </ol>
     *
     * @return The query or <code>null</code> if this implementation doesn't support the query.
     */
    @Internal
    @Nullable
    ResultQuery<Record5<String, String, String, String, String>> comments(List<String> schemas);

    /**
     * A query that produces UDT attributes for a set of input schemas.
     * <p>
     * The resulting columns are:
     * <ol>
     * <li>Catalog name</li>
     * <li>Schema name</li>
     * <li>Package name</li>
     * <li>Type name</li>
     * <li>Attribute name</li>
     * <li>Attribute position</li>
     * <li>Data type name</li>
     * <li>Data type precision</li>
     * <li>Data type scale</li>
     * <li>Data type nullability</li>
     * <li>Data type default</li>
     * <li>UDT catalog name</li>
     * <li>UDT schema name</li>
     * <li>UDT package name</li>
     * <li>UDT name</li>
     * </ol>
     *
     * @return The query or <code>null</code> if this implementation doesn't support the query.
     */
    @Internal
    @Nullable
    ResultQuery<Record15<String, String, String, String, String, Integer, String, Integer, Integer, Boolean, String, String, String, String, String>> attributes(List<String> schemas);



















































    /**
     * A query that produces generator expressions for computed columns for a
     * set of input schemas.
     * <p>
     * The resulting columns are:
     * <ol>
     * <li>Catalog name</li>
     * <li>Schema name</li>
     * <li>Table name</li>
     * <li>Column name</li>
     * <li>Generator expression as in {@link DataType#generatedAlwaysAs()}</li>
     * <li>Generation option as in {@link DataType#generationOption()}</li>
     * </ol>
     *
     * @return The query or <code>null</code> if this implementation doesn't
     *         support the query.
     */
    @Internal
    @Nullable
    ResultQuery<Record6<String, String, String, String, String, String>> generators(List<String> schemas);

}
