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

/**
 * @author Lukas Eder
 * @deprecated - [#2303] This is experimental functionality.
 */
@Deprecated
public interface Parser {

    /**
     * Parse a SQL string to a set of queries.
     *
     * @deprecated - [#2303] This is experimental functionality.
     */
    @Deprecated
    Queries parse(String sql);

    /**
     * Parse a SQL string to a query.
     *
     * @deprecated - [#2303] This is experimental functionality.
     */
    @Deprecated
    Query parseQuery(String sql);

    /**
     * Parse a SQL string to a table.
     *
     * @deprecated - [#2303] This is experimental functionality.
     */
    @Deprecated
    Table<?> parseTable(String sql);

    /**
     * Parse a SQL string to a field.
     *
     * @deprecated - [#2303] This is experimental functionality.
     */
    @Deprecated
    Field<?> parseField(String sql);

    /**
     * Parse a SQL string to a condition.
     *
     * @deprecated - [#2303] This is experimental functionality.
     */
    @Deprecated
    Condition parseCondition(String sql);

    /**
     * Parse a SQL string to a name.
     *
     * @deprecated - [#2303] This is experimental functionality.
     */
    @Deprecated
    Name parseName(String sql);
}
