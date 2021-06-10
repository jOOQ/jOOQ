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

// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.POSTGRES;

import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Support
    IN("in", false, true, false),

    @NotNull
    @Support
    NOT_IN("not in", false, true, false),

    @NotNull
    @Support
    EQUALS("=", true, true, false),

    @NotNull
    @Support
    NOT_EQUALS("<>", true, true, false),

    @NotNull
    @Support
    LESS("<", true, true, false),

    @NotNull
    @Support
    LESS_OR_EQUAL("<=", true, true, false),

    @NotNull
    @Support
    GREATER(">", true, true, false),

    @NotNull
    @Support
    GREATER_OR_EQUAL(">=", true, true, false),

    @NotNull
    @Support
    IS_DISTINCT_FROM("is distinct from", false, false, true),

    @NotNull
    @Support
    IS_NOT_DISTINCT_FROM("is not distinct from", false, false, true),

    @NotNull
    @Support
    LIKE("like", false, false, false),

    @NotNull
    @Support
    NOT_LIKE("not like", false, false, false),

    @NotNull
    @Support({ FIREBIRD, POSTGRES })
    SIMILAR_TO("similar to", false, false, false),

    @NotNull
    @Support({ FIREBIRD, POSTGRES })
    NOT_SIMILAR_TO("not similar to", false, false, false),

    @NotNull
    @Support
    LIKE_IGNORE_CASE("ilike", false, false, false),

    @NotNull
    @Support
    NOT_LIKE_IGNORE_CASE("not ilike", false, false, false),

    ;

    private final String  sql;
    private final Keyword keyword;
    private final boolean supportsQuantifier;
    private final boolean supportsSubselect;
    private final boolean supportsNulls;

    private Comparator(String sql, boolean supportsQuantifier, boolean supportsSubselect, boolean supportsNulls) {
        this.sql = sql;
        this.keyword = DSL.keyword(sql);
        this.supportsQuantifier = supportsQuantifier;
        this.supportsSubselect = supportsSubselect;
        this.supportsNulls = supportsNulls;
    }

    /**
     * A SQL rendition of this comparator.
     */
    public String toSQL() {
        return sql;
    }

    /**
     * A keyword rendition of this comparator.
     */
    public Keyword toKeyword() {
        return keyword;
    }

    /**
     * Get the inverse comparator such that <code>A [op] B</code> and
     * <code>NOT(A [inverse op] B)</code>.
     */
    public Comparator inverse() {
        switch (this) {
            case EQUALS:               return NOT_EQUALS;
            case GREATER:              return LESS_OR_EQUAL;
            case GREATER_OR_EQUAL:     return LESS;
            case IN:                   return NOT_IN;
            case IS_DISTINCT_FROM:     return IS_NOT_DISTINCT_FROM;
            case IS_NOT_DISTINCT_FROM: return IS_DISTINCT_FROM;
            case LESS:                 return GREATER_OR_EQUAL;
            case LESS_OR_EQUAL:        return GREATER;
            case LIKE:                 return NOT_LIKE;
            case LIKE_IGNORE_CASE:     return NOT_LIKE_IGNORE_CASE;
            case NOT_EQUALS:           return EQUALS;
            case NOT_IN:               return IN;
            case NOT_LIKE:             return LIKE;
            case NOT_LIKE_IGNORE_CASE: return LIKE_IGNORE_CASE;
            case NOT_SIMILAR_TO:       return SIMILAR_TO;
            case SIMILAR_TO:           return NOT_SIMILAR_TO;
            default:                   throw new IllegalStateException();
        }
    }

    /**
     * Get the mirrored comparator such that <code>A [op] B</code> and
     * <code>B [mirrored op] A</code>, or <code>null</code> if the comparator
     * cannot be mirrored.
     */
    public Comparator mirror() {
        switch (this) {
            case EQUALS:               return EQUALS;
            case GREATER:              return LESS;
            case GREATER_OR_EQUAL:     return LESS_OR_EQUAL;
            case IS_DISTINCT_FROM:     return IS_DISTINCT_FROM;
            case IS_NOT_DISTINCT_FROM: return IS_NOT_DISTINCT_FROM;
            case LESS:                 return GREATER;
            case LESS_OR_EQUAL:        return GREATER_OR_EQUAL;
            case NOT_EQUALS:           return NOT_EQUALS;
            default:                   return null;
        }
    }

    /**
     * Whether this comparator supports quantifiers on the right-hand side.
     *
     * @deprecated - 3.14.0 - [#9911] - This method is no longer supported.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    public final boolean supportsNulls() {
        return supportsNulls;
    }

    /**
     * Whether this comparator supports quantifiers on the right-hand side.
     *
     * @deprecated - 3.14.0 - [#9911] - This method is no longer supported.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    public boolean supportsQuantifier() {
        return supportsQuantifier;
    }

    /**
     * Whether this comparator supports subselects on the right-hand side.
     *
     * @deprecated - 3.14.0 - [#9911] - This method is no longer supported.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    public boolean supportsSubselect() {
        return supportsSubselect;
    }
}
