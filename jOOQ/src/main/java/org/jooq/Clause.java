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
 * TODO [#2667]
 */
public enum Clause {

    /**
     * A placeholder clause for cases where the behaviour was not yet specified.
     * This will not go public, and is meant to be a placeholder during
     * development.
     */
    @Deprecated
    DUMMY,

    // -------------------------------------------------------------------------
    // Clauses used in a any type of statement to model catalog references
    // -------------------------------------------------------------------------

    CATALOG,
    CATALOG_REFERENCE,

    // -------------------------------------------------------------------------
    // Clauses used in a any type of statement to model schema references
    // -------------------------------------------------------------------------

    SCHEMA,
    SCHEMA_REFERENCE,

    // -------------------------------------------------------------------------
    // Clauses used in a any type of statement to model table references
    // -------------------------------------------------------------------------

    /**
     * A complete table reference.
     * <p>
     * This clause surrounds a complete table reference as it can be encountered
     * in
     * <ul>
     * <li> {@link #SELECT_FROM}</li>
     * <li> {@link #INSERT_INSERT_INTO}</li>
     * <li> {@link #UPDATE_UPDATE}</li>
     * <li> {@link #DELETE_DELETE}</li>
     * <li> {@link #MERGE_MERGE_INTO}</li>
     * <li> {@link #TRUNCATE_TRUNCATE}</li>
     * </ul>
     */
    TABLE,

    TABLE_ALIAS,
    TABLE_REFERENCE,

    /**
     *
     */
    TABLE_JOIN,
    TABLE_JOIN_INNER,
    TABLE_JOIN_CROSS,
    TABLE_JOIN_NATURAL,
    TABLE_JOIN_OUTER_LEFT,
    TABLE_JOIN_OUTER_RIGHT,
    TABLE_JOIN_OUTER_FULL,
    TABLE_JOIN_NATURAL_OUTER_LEFT,
    TABLE_JOIN_NATURAL_OUTER_RIGHT,
    TABLE_JOIN_ON,
    TABLE_JOIN_USING,
    TABLE_JOIN_PARTITION_BY,

    TABLE_FLASHBACK,
    TABLE_PIVOT,

    // -------------------------------------------------------------------------
    // Clauses used in a any type of statement to model column references
    // -------------------------------------------------------------------------

    FIELD,
    FIELD_ALIAS,
    FIELD_REFERENCE,
    FIELD_VALUE,
    FIELD_CASE,
    FIELD_ROW,

    // -------------------------------------------------------------------------
    // Clauses used in a any type of statement to model condition references
    // -------------------------------------------------------------------------

    /**
     * A condition expression.
     */
    CONDITION,

    /**
     * A <code>NULL</code> condition.
     * <p>
     * This clause surrounds a {@link #FIELD}.
     */
    CONDITION_IS_NULL,

    /**
     * A <code>NOT NULL</code> condition.
     * <p>
     * This clause surrounds a {@link #FIELD}.
     */
    CONDITION_IS_NOT_NULL,

    // TODO: Should operators be distinguished?
    // - LIKE predicate
    // - Subselect predicates
    // - RVE predicates
    // - Quantified predicates
    CONDITION_COMPARISON,

    /**
     * A <code>BEWEEN</code> condition.
     * <p>
     * This clause surrounds three {@link #FIELD} clauses.
     */
    CONDITION_BETWEEN,

    /**
     * A <code>BEWEEN SYMMETRIC</code> condition.
     * <p>
     * This clause surrounds three {@link #FIELD} clauses.
     */
    CONDITION_BETWEEN_SYMMETRIC,

    /**
     * A <code>NOT BEWEEN</code> condition.
     * <p>
     * This clause surrounds three {@link #FIELD} clauses.
     */
    CONDITION_NOT_BETWEEN,

    /**
     * A <code>NOT BEWEEN SYMMETRIC</code> condition.
     * <p>
     * This clause surrounds three {@link #FIELD} clauses.
     */
    CONDITION_NOT_BETWEEN_SYMMETRIC,

    /**
     * An <code>OVERLAPS</code> condition.
     * <p>
     * This clause surrounds two {@link #FIELD} clauses.
     */
    CONDITION_OVERLAPS,

    /**
     * A combined condition using <code>AND</code>.
     * <p>
     * This clause surrounds several {@link #CONDITION} clauses.
     */
    CONDITION_AND,

    /**
     * A combined condition using <code>OR</code>.
     * <p>
     * This clause surrounds several {@link #CONDITION} clauses.
     */
    CONDITION_OR,

    /**
     * A <code>NOT</code> condition.
     * <p>
     * This clause surrounds a {@link #CONDITION} clause.
     */
    CONDITION_NOT,

    /**
     * An <code>IN</code> condition.
     * <p>
     * This clause surrounds two or more {@link #FIELD} clauses.
     */
    CONDITION_IN,

    /**
     * A <code>NOT IN</code> condition.
     * <p>
     * This clause surrounds two or more {@link #FIELD} clauses.
     */
    CONDITION_NOT_IN,

    /**
     * An <code>EXISTS</code> condition.
     * <p>
     * This clause surrounds a {@link #SELECT} clause.
     */
    CONDITION_EXISTS,

    /**
     * A <code>NOT EXISTS</code> condition.
     * <p>
     * This clause surrounds a {@link #SELECT} clause.
     */
    CONDITION_NOT_EXISTS,

    // -------------------------------------------------------------------------
    // Clauses that are used in a SELECT statement
    // -------------------------------------------------------------------------

    /**
     * A complete <code>SELECT</code> statement or a subselect.
     * <p>
     * This clause surrounds a complete <code>SELECT</code> statement, a
     * subselect, or a set operation, such as
     * <ul>
     * <li> {@link #SELECT_UNION}</li>
     * <li> {@link #SELECT_UNION_ALL}</li>
     * <li> {@link #SELECT_INTERSECT}</li>
     * <li> {@link #SELECT_EXCEPT}</li>
     * </ul>
     */
    SELECT,

    /**
     * A <code>UNION</code> set operation.
     * <p>
     * This clause surrounds two or more subselects (see {@link #SELECT})
     * concatenating them using a <code>UNION</code> set operation.
     */
    SELECT_UNION,

    /**
     * A <code>UNION ALL</code> set operation.
     * <p>
     * This clause surrounds two or more subselects (see {@link #SELECT})
     * concatenating them using a <code>UNION ALL</code> set operation.
     */
    SELECT_UNION_ALL,

    /**
     * A <code>INTERSECT</code> set operation.
     * <p>
     * This clause surrounds two or more subselects (see {@link #SELECT})
     * concatenating them using a <code>INTERSECT</code> set operation.
     */
    SELECT_INTERSECT,

    /**
     * A <code>EXCEPT</code> set operation.
     * <p>
     * This clause surrounds two or more subselects (see {@link #SELECT})
     * concatenating them using a <code>EXCEPT</code> set operation.
     */
    SELECT_EXCEPT,

    /**
     * A <code>SELECT</code> clause within a {@link #SELECT} statement or
     * subselect.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>SELECT</code> keyword</li>
     * <li>Oracle style hints</li>
     * <li>the T-SQL style <code>TOP .. START AT</code> clause</li>
     * <li>the select field list</li>
     * </ul>
     */
    SELECT_SELECT,

    /**
     * A <code>FROM</code> clause within a {@link #SELECT} statement or
     * subselect.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>FROM</code> keyword</li>
     * <li>the table reference list</li>
     * </ul>
     * <p>
     * See {@link #TABLE} and related clauses for possible table references.
     *
     * @see #TABLE
     */
    SELECT_FROM,
    SELECT_WHERE,
    SELECT_START_WITH,
    SELECT_CONNECT_BY,
    SELECT_GROUP_BY,
    SELECT_HAVING,
    SELECT_ORDER_BY,



    INSERT,
    INSERT_INSERT_INTO,
    INSERT_RETURNING,



    // -------------------------------------------------------------------------
    // Clauses that are used in an UPDATE statement
    // -------------------------------------------------------------------------

    /**
     * A complete <code>UPDATE</code> statement.
     */
    UPDATE,

    /**
     * An <code>UPDATE</code> clause within an {@link #UPDATE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>UPDATE</code> keyword</li>
     * <li>the table that is being updated</li>
     * </ul>
     */
    UPDATE_UPDATE,

    /**
     * A <code>SET</code> clause within an {@link #UPDATE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>SET</code> keyword</li>
     * <li>one or several assignments: {@link #UPDATE_SET_ASSIGNMENT}</li>
     * </ul>
     */
    UPDATE_SET,

    /**
     * An assigment within a {@link #UPDATE_SET} clause within an
     * {@link #UPDATE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>a column</li>
     * <li>an assigment operator</li>
     * <li>a value being assigned</li>
     * </ul>
     */
    UPDATE_SET_ASSIGNMENT,

    /**
     * A <code>WHERE</code> clause within an {@link #UPDATE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>WHERE</code> keyword</li>
     * <li>a {@link #CONDITION}</li>
     * </ul>
     */
    UPDATE_WHERE,

    /**
     * A <code>RETURNING</code> clause within an {@link #UPDATE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RETURNING</code> keyword</li>
     * <li>several {@link #FIELD} clauses</li>
     * </ul>
     */
    UPDATE_RETURNING,


    DELETE,
    DELETE_DELETE,
    DELETE_WHERE,


    MERGE,
    MERGE_MERGE_INTO,



    TRUNCATE,
    TRUNCATE_TRUNCATE,
}