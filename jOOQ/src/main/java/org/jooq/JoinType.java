/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
