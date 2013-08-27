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
 * The type of join
 *
 * @author Lukas Eder
 */
public enum JoinType {

    /**
     * <code>INNER JOIN</code> two tables
     */
    @Support
    JOIN("join"),

    /**
     * <code>CROSS JOIN</code> two tables
     */
    @Support
    CROSS_JOIN("cross join"),

    /**
     * <code>LEFT OUTER JOIN</code> two tables
     */
    @Support
    LEFT_OUTER_JOIN("left outer join"),

    /**
     * <code>RIGHT OUTER JOIN</code> two tables
     */
    @Support
    RIGHT_OUTER_JOIN("right outer join"),

    /**
     * <code>FULL OUTER JOIN</code> two tables
     */
    @Support
    FULL_OUTER_JOIN("full outer join"),

    /**
     * <code>NATURAL INNER JOIN</code> two tables
     */
    @Support
    NATURAL_JOIN("natural join"),

    /**
     * <code>NATURAL LEFT OUTER JOIN</code> two tables
     */
    @Support
    NATURAL_LEFT_OUTER_JOIN("natural left outer join"),

    /**
     * <code>NATURAL RIGHT OUTER JOIN</code> two tables
     */
    @Support
    NATURAL_RIGHT_OUTER_JOIN("natural right outer join"),

    ;

    private final String sql;

    private JoinType(String sql) {
        this.sql = sql;
    }

    public final String toSQL() {
        return sql;
    }
}
