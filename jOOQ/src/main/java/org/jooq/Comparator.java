/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
