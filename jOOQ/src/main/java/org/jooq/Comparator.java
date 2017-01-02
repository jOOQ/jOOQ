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

/**
 * A comparator to be used in conditions to form comparison predicates.
 * <p>
 * Comparison operators listed here can be used to compare {@link Field} or
 * {@link Row} values with other {@link Field} or {@link Row} values, or with
 * {@link Select} or {@link QuantifiedSelect} values.
 * <p>
 * The following flags indicate whether the comparator can be used as:
 * <ul>
 * <li>a quantified comparison operator: {@link #supportsQuantifier()}. Example:
 * <code>X = ANY (A, B, C)</code></li>
 * <li>a subselect comparison operator: {@link #supportsSubselect()}. Example:
 * <code>X = (SELECT A)</code></li>
 * </ul>
 *
 * @author Lukas Eder
 */
public enum Comparator {

    @Support
    IN("in", false, true),

    @Support
    NOT_IN("not in", false, true),

    @Support
    EQUALS("=", true, true),

    @Support
    NOT_EQUALS("<>", true, true),

    @Support
    LESS("<", true, true),

    @Support
    LESS_OR_EQUAL("<=", true, true),

    @Support
    GREATER(">", true, true),

    @Support
    GREATER_OR_EQUAL(">=", true, true),

    @Support
    IS_DISTINCT_FROM("is distinct from", false, false),

    @Support
    IS_NOT_DISTINCT_FROM("is not distinct from", false, false),

    @Support
    LIKE("like", false, false),

    @Support
    NOT_LIKE("not like", false, false),

    @Support
    LIKE_IGNORE_CASE("ilike", false, false),

    @Support
    NOT_LIKE_IGNORE_CASE("not ilike", false, false),

    ;

    private final String  sql;
    private final boolean supportsQuantifier;
    private final boolean supportsSubselect;

    private Comparator(String sql, boolean supportsQuantifier, boolean supportsSubselect) {
        this.sql = sql;
        this.supportsQuantifier = supportsQuantifier;
        this.supportsSubselect = supportsSubselect;
    }

    /**
     * A SQL rendition of this comparator.
     */
    public String toSQL() {
        return sql;
    }

    /**
     * Whether this comparator supports quantifiers on the right-hand side.
     */
    public boolean supportsQuantifier() {
        return supportsQuantifier;
    }

    /**
     * Whether this comparator supports subselects on the right-hand side.
     */
    public boolean supportsSubselect() {
        return supportsSubselect;
    }
}
