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
    // Clauses used in a any type of statement to model package references
    // -------------------------------------------------------------------------

    PACKAGE,
    PACKAGE_REFERENCE,

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
     * A table expression.
     * <p>
     * This clause surrounds an actual table expression as it can be encountered
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

    /**
     * A table alias declaration.
     * <p>
     * This clause surrounds a table alias declaration, for instance within the
     * {@link #SELECT_FROM} clause, or within a {@link #TABLE_JOIN} clause,
     * wrapping another {@link #TABLE}.
     * <p>
     * Referenced table aliases emit {@link #TABLE_REFERENCE} clauses.
     */
    TABLE_ALIAS,

    /**
     * A physical or aliased table reference.
     * <p>
     * This is a terminal clause used to reference physical or aliased tables.
     */
    TABLE_REFERENCE,
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

    /**
     * A <code>VALUES</code> table constructor
     * <p>
     * This clause surrounds a
     * <ul>
     * <li>a <code>VALUES</code> keyword</li>
     * <li>a table constructor with several {@link #FIELD_ROW} value expressions
     * </li>
     * </ul>
     */
    TABLE_VALUES,
    TABLE_FLASHBACK,
    TABLE_PIVOT,

    // -------------------------------------------------------------------------
    // Clauses used in a any type of statement to model column references
    // -------------------------------------------------------------------------

    /**
     * A field expression.
     * <p>
     * This clause surrounds an actual field expression as it can be encountered
     * in various other clauses, such as for instance {@link #SELECT_SELECT}.
     */
    FIELD,

    /**
     * A field alias declaration.
     * <p>
     * This clause surrounds a field alias declaration, for instance within the
     * {@link #SELECT_SELECT} clause,
     * wrapping another {@link #FIELD}.
     * <p>
     * Referenced field aliases emit {@link #FIELD_REFERENCE} clauses.
     */
    FIELD_ALIAS,

    /**
     * A physical or aliased field reference.
     * <p>
     * This is a terminal clause used to reference physical or aliased fields.
     */
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

    /**
     * A <code>WHERE</code> clause within a {@link #SELECT} statement or
     * subselect.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>WHERE</code> keyword</li>
     * <li>a {@link #CONDITION}</li>
     * </ul>
     * <p>
     * See {@link #CONDITION} and related clauses for possible conditions
     *
     * @see #CONDITION
     */
    SELECT_WHERE,

    /**
     * A <code>START WITH</code> clause within a {@link #SELECT} statement or
     * subselect.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>START WITH</code> keywords</li>
     * <li>a {@link #CONDITION}</li>
     * </ul>
     * <p>
     * See {@link #CONDITION} and related clauses for possible conditions
     *
     * @see #CONDITION
     */
    SELECT_START_WITH,

    /**
     * A <code>CONNECT BY</code> clause within a {@link #SELECT} statement or
     * subselect.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>CONNECT BY</code> keywords</li>
     * <li>a {@link #CONDITION}</li>
     * </ul>
     * <p>
     * See {@link #CONDITION} and related clauses for possible conditions
     *
     * @see #CONDITION
     */
    SELECT_CONNECT_BY,
    SELECT_GROUP_BY,

    /**
     * A <code>HAVING</code> clause within a {@link #SELECT} statement or
     * subselect.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>HAVING</code> keyword</li>
     * <li>a {@link #CONDITION}</li>
     * </ul>
     * <p>
     * See {@link #CONDITION} and related clauses for possible conditions
     *
     * @see #CONDITION
     */
    SELECT_HAVING,
    SELECT_ORDER_BY,


    // -------------------------------------------------------------------------
    // Clauses that are used in an INSERT statement
    // -------------------------------------------------------------------------

    /**
     * A complete <code>INSERT</code> statement.
     */
    INSERT,

    /**
     * The <code>INSERT INTO</code> clause within an {@link #INSERT} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>INSERT INTO</code> keywords</li>
     * <li>the table that is being inserted</li>
     * </ul>
     */
    INSERT_INSERT_INTO,

    /**
     * The <code>VALUES</code> clause within an {@link #INSERT} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>VALUES</code> keyword</li>
     * <li>several {@link #FIELD_ROW} clauses</li>
     * </ul>
     */
    INSERT_VALUES,

    /**
     * The <code>SELECT</code> clause within an {@link #INSERT} statement.
     * <p>
     * This clause surrounds a {@link #SELECT} clause.
     */
    INSERT_SELECT,

    /**
     * The <code>ON DUPLICATE KEY UPDATE</code> clause within an {@link #INSERT}
     * statement.
     * <p>
     * This clause surrounds several
     * {@link #INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT} clauses.
     * <ul>
     * <li>the <code>ON DUPLICATE KEY UPDATE</code> keywords</li>
     * <li>several {@link #INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT} clauses</li>
     * </ul>
     */
    INSERT_ON_DUPLICATE_KEY_UPDATE,

    /**
     * The <code>ON DUPLICATE KEY UPDATE</code> clause within an {@link #INSERT}
     * statement.
     * <p>
     * This clause surrounds two {@link #FIELD} clauses.
     */
    INSERT_ON_DUPLICATE_KEY_UPDATE_ASSIGNMENT,

    /**
     * The <code>RETURNING</code> clause within an {@link #INSERT} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RETURNING</code> keyword</li>
     * <li>several {@link #FIELD} clauses</li>
     * </ul>
     */
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

    // -------------------------------------------------------------------------
    // Clauses that are used in an DELETE statement
    // -------------------------------------------------------------------------

    DELETE,
    DELETE_DELETE,
    DELETE_WHERE,

    // -------------------------------------------------------------------------
    // Clauses that are used in an MERGE statement
    // -------------------------------------------------------------------------


    MERGE,
    MERGE_MERGE_INTO,
    MERGE_WHEN_MATCHED_THEN_UPDATE_SET,
    MERGE_WHEN_MATCHED_THEN_UPDATE_SET_ASSIGNMENT,

    // -------------------------------------------------------------------------
    // Clauses that are used in an TRUNCATE statement
    // -------------------------------------------------------------------------

    TRUNCATE,
    TRUNCATE_TRUNCATE,
}
