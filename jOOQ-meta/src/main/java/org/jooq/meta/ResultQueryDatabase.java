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

import org.jooq.Internal;
import org.jooq.Meta;
import org.jooq.Record6;
import org.jooq.ResultQuery;

/**
 * An interface for all {@link AbstractDatabase} implementations that can
 * produce {@link ResultQuery} objects to query meta data.
 * <p>
 * These queries will be used to generate some queries in the core library's
 * {@link Meta} API.
 *
 * @author Lukas Eder
 */
@Internal
public interface ResultQueryDatabase {

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
     */
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
     */
    ResultQuery<Record6<String, String, String, String, String, Integer>> uniqueKeys(List<String> schemas);
}
