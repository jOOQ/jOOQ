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
 * A listing of clauses that emit events {@link VisitListener}.
 * <p>
 * <h3>Disclaimer</h3> This SPI is still <strong>experimental</strong>! Some SPI
 * elements and/or behavioural elements may change in future minor releases.
 */
public enum Clause {

    // -------------------------------------------------------------------------
    // Clauses used in a any type of statement to model constraint references
    // -------------------------------------------------------------------------

    CONSTRAINT,

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
    // Clauses used in a any type of statement to model sequence references
    // -------------------------------------------------------------------------

    SEQUENCE,
    SEQUENCE_REFERENCE,

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
    TABLE_JOIN_CROSS_APPLY,
    TABLE_JOIN_OUTER_APPLY,
    TABLE_JOIN_SEMI_LEFT,
    TABLE_JOIN_ANTI_LEFT,
    TABLE_JOIN_STRAIGHT,
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
     * {@link #SELECT_SELECT} clause, wrapping another {@link #FIELD}.
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
    FIELD_FUNCTION,

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
    // The WITH clause that is used in all sorts of statements
    // -------------------------------------------------------------------------

    /**
     * A <code>WITH</code> clause preceding all sorts of DML statements.
     */
    WITH,

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
     * A <code>INTERSECT ALL</code> set operation.
     * <p>
     * This clause surrounds two or more subselects (see {@link #SELECT})
     * concatenating them using a <code>INTERSECT ALL</code> set operation.
     */
    SELECT_INTERSECT_ALL,

    /**
     * A <code>EXCEPT</code> set operation.
     * <p>
     * This clause surrounds two or more subselects (see {@link #SELECT})
     * concatenating them using a <code>EXCEPT</code> set operation.
     */
    SELECT_EXCEPT,

    /**
     * A <code>EXCEPT ALL</code> set operation.
     * <p>
     * This clause surrounds two or more subselects (see {@link #SELECT})
     * concatenating them using a <code>EXCEPT ALL</code> set operation.
     */
    SELECT_EXCEPT_ALL,

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
     * A <code>INTO</code> clause within a {@link #SELECT} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>INTO</code> keyword</li>
     * <li>the table reference</li>
     * </ul>
     * <p>
     * See {@link #TABLE} and related clauses for possible table references.
     *
     * @see #TABLE
     */
    SELECT_INTO,

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
    SELECT_WINDOW,
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
     * <li>a {@link #FIELD} receiving the assignment</li>
     * <li>an assigment operator</li>
     * <li>a {@link #FIELD} being assigned</li>
     * </ul>
     */
    UPDATE_SET_ASSIGNMENT,

    /**
     * A vendor-specific <code>FROM</code> clause within an {@link #UPDATE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>FROM</code> keyword</li>
     * <li>the table reference list</li>
     * </ul>
     * <p>
     * See {@link #TABLE} and related clauses for possible table references.
     */
    UPDATE_FROM,

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

    /**
     * A complete <code>DELETE</code> statement.
     */
    DELETE,

    /**
     * A <code>DELETE</code> clause within an {@link #DELETE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>DELETE FROM</code> keywords</li>
     * <li>the table that is being deleted</li>
     * </ul>
     */
    DELETE_DELETE,

    /**
     * A <code>WHERE</code> clause within an {@link #DELETE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>WHERE</code> keyword</li>
     * <li>a {@link #CONDITION}</li>
     * </ul>
     */
    DELETE_WHERE,

    /**
     * A <code>RETURNING</code> clause within an {@link #DELETE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RETURNING</code> keyword</li>
     * <li>several {@link #FIELD} clauses</li>
     * </ul>
     */
    DELETE_RETURNING,

    // -------------------------------------------------------------------------
    // Clauses that are used in an MERGE statement
    // -------------------------------------------------------------------------

    /**
     * A complete <code>MERGE</code> statement.
     */
    MERGE,

    /**
     * A <code>MERGE INTO</code> clause within an {@link #MERGE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>MERGE INTO</code> keywords</li>
     * <li>the table that is being merged</li>
     * </ul>
     */
    MERGE_MERGE_INTO,

    /**
     * A <code>USING</code> clause within a {@link #MERGE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>USING</code> keyword</li>
     * <li>a {@link #TABLE}</li>
     * </ul>
     */
    MERGE_USING,

    /**
     * An <code>ON</code> clause within a {@link #MERGE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>ON</code> keyword</li>
     * <li>a {@link #CONDITION}</li>
     * </ul>
     */
    MERGE_ON,

    /**
     * A <code>WHEN MATCHED THEN UPDATE</code> clause within a {@link #MERGE}
     * statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>WHEN MATCHED THEN UPDATE</code> keywords</li>
     * <li>a {@link #MERGE_SET} clause</li>
     * <li>a {@link #MERGE_WHERE} clause</li>
     * <li>a {@link #MERGE_DELETE_WHERE} clause</li>
     * </ul>
     */
    MERGE_WHEN_MATCHED_THEN_UPDATE,

    /**
     * A <code>SET</code> clause within a
     * {@link #MERGE_WHEN_MATCHED_THEN_UPDATE} clause within an {@link #MERGE}
     * statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>SET</code> keyword</li>
     * <li>several {@link #MERGE_SET_ASSIGNMENT} clauses</li>
     * </ul>
     */
    MERGE_SET,

    /**
     * An assigment within a {@link #MERGE_SET} clause within an {@link #MERGE}
     * statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>a {@link #FIELD} receiving the assignment</li>
     * <li>an assigment operator</li>
     * <li>a {@link #FIELD} being assigned</li>
     * </ul>
     */
    MERGE_SET_ASSIGNMENT,

    /**
     * A <code>WHERE</code> clause within a
     * {@link #MERGE_WHEN_MATCHED_THEN_UPDATE} clause within a
     * {@link #MERGE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>WHERE</code> keyword</li>
     * <li>a {@link #CONDITION}</li>
     * </ul>
     */
    MERGE_WHERE,

    /**
     * A <code>DELETE_WHERE</code> clause within a
     * {@link #MERGE_WHEN_MATCHED_THEN_UPDATE} clause within a {@link #MERGE}
     * statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>DELETE WHERE</code> keyword</li>
     * <li>a {@link #CONDITION}</li>
     * </ul>
     */
    MERGE_DELETE_WHERE,

    /**
     * A <code>WHEN NOT MATCHED THEN INSERT</code> clause within a
     * {@link #MERGE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>WHEN NOT MATCHED THEN INSERT</code> keywords</li>
     * <li>several {@link #FIELD} clauses</li>
     * </ul>
     */
    MERGE_WHEN_NOT_MATCHED_THEN_INSERT,

    /**
     * A <code>VALUES</code> clause within a {@link #MERGE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>VALUES</code> keyword</li>
     * <li>several {@link #FIELD_ROW} clauses</li>
     * </ul>
     */
    MERGE_VALUES,

    // -------------------------------------------------------------------------
    // Clauses that are used in an TRUNCATE statement
    // -------------------------------------------------------------------------

    /**
     * A complete <code>TRUNCATE</code> statement.
     */
    TRUNCATE,

    /**
     * A <code>TRUNCATE</code> clause within an {@link #TRUNCATE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>TRUNCATE TABLE</code> keywords</li>
     * <li>the table that is being truncated</li>
     * </ul>
     */
    TRUNCATE_TRUNCATE,

    // -------------------------------------------------------------------------
    // Clauses that are used in an ALTER statement
    // -------------------------------------------------------------------------

    /**
     * A complete <code>CREATE TABLE</code> statement.
     */
    CREATE_TABLE,

    /**
     * A view name clause within a {@link #CREATE_TABLE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>The table name</li>
     * <li>The (optional) column names</li>
     * </ul>
     */
    CREATE_TABLE_NAME,

    /**
     * An <code>AS</code> clause within a {@link #CREATE_TABLE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>The <code>AS</code> keyword</li>
     * <li>The select code</li>
     * </ul>
     */
    CREATE_TABLE_AS,

    /**
     * A column list within a {@link #CREATE_TABLE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>The column list</li>
     * </ul>
     */
    CREATE_TABLE_COLUMNS,

    /**
     * A constraint list within a {@link #CREATE_TABLE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>The constraint list</li>
     * </ul>
     */
    CREATE_TABLE_CONSTRAINTS,

    /**
     * A complete <code>CREATE SCHEMA</code> statement.
     */
    CREATE_SCHEMA,

    /**
     * A view name clause within a {@link #CREATE_SCHEMA} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>The schema name</li>
     * </ul>
     */
    CREATE_SCHEMA_NAME,

    /**
     * A complete <code>CREATE VIEW</code> statement.
     */
    CREATE_VIEW,

    /**
     * A view name clause within a {@link #CREATE_VIEW} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>The view name</li>
     * <li>The (optional) column names</li>
     * </ul>
     */
    CREATE_VIEW_NAME,

    /**
     * An <code>AS</code> clause within a {@link #CREATE_VIEW} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>The <code>AS</code> keyword</li>
     * <li>The view code</li>
     * </ul>
     */
    CREATE_VIEW_AS,

    /**
     * A complete <code>CREATE INDEX</code> statement.
     */
    CREATE_INDEX,

    /**
     * A complete <code>CREATE SEQUENCE</code> statement.
     */
    CREATE_SEQUENCE,

    /**
     * A <code>SEQUENCE</code> clause within a {@link #CREATE_SEQUENCE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>CREATE SEQUENCE</code> keywords</li>
     * <li>the sequence that is being created</li>
     * </ul>
     */
    CREATE_SEQUENCE_SEQUENCE,

    /**
     * A complete <code>ALTER SEQUENCE</code> statement.
     */
    ALTER_SEQUENCE,

    /**
     * A <code>SEQUENCE</code> clause within an {@link #ALTER_SEQUENCE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>ALTER SEQUENCE</code> keywords</li>
     * <li>the sequence that is being altered</li>
     * </ul>
     */
    ALTER_SEQUENCE_SEQUENCE,

    /**
     * A <code>RESTART</code> clause within an {@link #ALTER_SEQUENCE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RESTART</code> keyword</li>
     * <li>the <code>WITH</code> keyword and the new sequence value, if applicable.</li>
     * </ul>
     */
    ALTER_SEQUENCE_RESTART,

    /**
     * A <code>RENAME</code> clause within an {@link #ALTER_SEQUENCE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RENAME TO</code> keywords</li>
     * <li>the new sequence name</li>
     * </ul>
     */
    ALTER_SEQUENCE_RENAME,

    /**
     * A complete <code>ALTER TABLE</code> statement.
     */
    ALTER_TABLE,

    /**
     * A <code>TABLE</code> clause within an {@link #ALTER_TABLE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>ALTER TABLE</code> keywords</li>
     * <li>the table that is being altered</li>
     * </ul>
     */
    ALTER_TABLE_TABLE,

    /**
     * A <code>RENAME TO</code> clause within an {@link #ALTER_TABLE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RENAME TO</code> keywords</li>
     * <li>the new table name</li>
     * </ul>
     */
    ALTER_TABLE_RENAME,

    /**
     * A <code>RENAME COLUMN</code> clause within an {@link #ALTER_TABLE}
     * statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RENAME COLUMN</code> keywords</li>
     * <li>the old column name</li>
     * <li>the <code>TO</code> keyword</li>
     * <li>the new column name</li>
     * </ul>
     */
    ALTER_TABLE_RENAME_COLUMN,

    /**
     * A <code>RENAME CONSTRAINT</code> clause within an {@link #ALTER_TABLE}
     * statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RENAME CONSTRAINT</code> keywords</li>
     * <li>the old column name</li>
     * <li>the <code>TO</code> keyword</li>
     * <li>the new column name</li>
     * </ul>
     */
    ALTER_TABLE_RENAME_CONSTRAINT,

    /**
     * A <code>ADD</code> clause within an {@link #ALTER_TABLE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>ADD</code> keywords</li>
     * <li>the column that is being added</li>
     * </ul>
     */
    ALTER_TABLE_ADD,

    /**
     * A <code>ALTER</code> clause within an {@link #ALTER_TABLE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>ALTER</code> keywords</li>
     * <li>the column that is being altered</li>
     * </ul>
     */
    ALTER_TABLE_ALTER,

    /**
     * A <code>ALTER DEFAULT</code> clause within an {@link #ALTER_TABLE}
     * statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the default expression that is being altered</li>
     * </ul>
     */
    ALTER_TABLE_ALTER_DEFAULT,

    /**
     * A <code>DROP</code> clause within an {@link #ALTER_TABLE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>DROP</code> keywords</li>
     * <li>the column that is being dropped</li>
     * </ul>
     */
    ALTER_TABLE_DROP,

    /**
     * A complete <code>ALTER SCHEMA</code> statement.
     */
    ALTER_SCHEMA,

    /**
     * A <code>SCHEMA</code> clause within an {@link #ALTER_SCHEMA} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>ALTER SCHEMA</code> keywords</li>
     * <li>the schema that is being altered</li>
     * </ul>
     */
    ALTER_SCHEMA_SCHEMA,

    /**
     * A <code>RENAME TO</code> clause within an {@link #ALTER_SCHEMA} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RENAME TO</code> keywords</li>
     * <li>the new schema name</li>
     * </ul>
     */
    ALTER_SCHEMA_RENAME,

    /**
     * A complete <code>ALTER VIEW</code> statement.
     */
    ALTER_VIEW,

    /**
     * A <code>TABLE</code> clause within an {@link #ALTER_VIEW} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>ALTER VIEW</code> keywords</li>
     * <li>the view that is being altered</li>
     * </ul>
     */
    ALTER_VIEW_VIEW,

    /**
     * A <code>RENAME TO</code> clause within an {@link #ALTER_VIEW} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RENAME TO</code> keywords</li>
     * <li>the new view name</li>
     * </ul>
     */
    ALTER_VIEW_RENAME,

    /**
     * A complete <code>ALTER INDEX</code> statement.
     */
    ALTER_INDEX,

    /**
     * An <code>INDEX</code> clause within an {@link #ALTER_INDEX} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>ALTER INDEX</code> keywords</li>
     * <li>the index that is being altered</li>
     * </ul>
     */
    ALTER_INDEX_INDEX,

    /**
     * A <code>RENAME TO</code> clause within an {@link #ALTER_INDEX} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>RENAME TO</code> keywords</li>
     * <li>the new index name</li>
     * </ul>
     */
    ALTER_INDEX_RENAME,

    /**
     * A complete <code>DROP SCHEMA</code> statement.
     */
    DROP_SCHEMA,

    /**
     * A <code>SCHEMA</code> clause within an {@link #DROP_SCHEMA} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>DROP SCHEMA</code> keywords</li>
     * <li>the schema that is being dropped</li>
     * </ul>
     */
    DROP_SCHEMA_SCHEMA,

    /**
     * A complete <code>DROP VIEW</code> statement.
     */
    DROP_VIEW,

    /**
     * A <code>VIEW</code> clause within an {@link #DROP_VIEW} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>DROP VIEW</code> keywords</li>
     * <li>the view that is being dropped</li>
     * </ul>
     */
    DROP_VIEW_TABLE,

    /**
     * A complete <code>DROP TABLE</code> statement.
     */
    DROP_TABLE,

    /**
     * A <code>TABLE</code> clause within an {@link #DROP_TABLE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>DROP TABLE</code> keywords</li>
     * <li>the table that is being dropped</li>
     * </ul>
     */
    DROP_TABLE_TABLE,

    /**
     * A complete <code>DROP INDEX</code> statement.
     */
    DROP_INDEX,

    /**
     * A complete <code>DROP SEQUENCE</code> statement.
     */
    DROP_SEQUENCE,

    /**
     * A <code>SEQUENCE</code> clause within a {@link #DROP_SEQUENCE} statement.
     * <p>
     * This clause surrounds
     * <ul>
     * <li>the <code>DROP SEQUENCE</code> keywords</li>
     * <li>the sequence that is being dropped</li>
     * </ul>
     */
    DROP_SEQUENCE_SEQUENCE,

    // -------------------------------------------------------------------------
    // Other clauses
    // -------------------------------------------------------------------------

    /**
     * A plain SQL template clause.
     */
    TEMPLATE,

    /**
     * A custom {@link QueryPart} clause.
     */
    CUSTOM
}
