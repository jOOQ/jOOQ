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
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...
// ...

import java.util.Collection;

import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;


/**
 * The step in the <code>ALTER TABLE</code> statement where the action can be
 * decided.
 * <p>
 * <h3>Referencing <code>XYZ*Step</code> types directly from client code</h3>
 * <p>
 * It is usually not recommended to reference any <code>XYZ*Step</code> types
 * directly from client code, or assign them to local variables. When writing
 * dynamic SQL, creating a statement's components dynamically, and passing them
 * to the DSL API statically is usually a better choice. See the manual's
 * section about dynamic SQL for details: <a href=
 * "https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql">https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql</a>.
 * <p>
 * Drawbacks of referencing the <code>XYZ*Step</code> types directly:
 * <ul>
 * <li>They're operating on mutable implementations (as of jOOQ 3.x)</li>
 * <li>They're less composable and not easy to get right when dynamic SQL gets
 * complex</li>
 * <li>They're less readable</li>
 * <li>They might have binary incompatible changes between minor releases</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface AlterTableStep {

    /**
     * Specify a comment for a table using MySQL's syntax.
     *
     * @see DSL#commentOnTable(Table)
     * @see DSLContext#commentOnTable(Table)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableFinalStep comment(String comment);

    /**
     * Specify a comment for a table using MySQL's syntax.
     *
     * @see DSL#commentOnTable(Table)
     * @see DSLContext#commentOnTable(Table)
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableFinalStep comment(Comment comment);

    /**
     * Add a <code>RENAME TO</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    AlterTableFinalStep renameTo(Table<?> newName);

    /**
     * Add a <code>RENAME TO</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    AlterTableFinalStep renameTo(Name newName);

    /**
     * Add a <code>RENAME TO</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    AlterTableFinalStep renameTo(String newName);

    /**
     * Add a <code>RENAME COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    AlterTableRenameColumnToStep renameColumn(Field<?> oldName);

    /**
     * Add a <code>RENAME COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    AlterTableRenameColumnToStep renameColumn(Name oldName);

    /**
     * Add a <code>RENAME COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    AlterTableRenameColumnToStep renameColumn(String oldName);

    /**
     * Add a <code>RENAME INDEX</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableRenameIndexToStep renameIndex(Name oldName);

    /**
     * Add a <code>RENAME INDEX</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableRenameIndexToStep renameIndex(Index oldName);

    /**
     * Add a <code>RENAME INDEX</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableRenameIndexToStep renameIndex(String oldName);

    /**
     * Add a <code>RENAME CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    AlterTableRenameConstraintToStep renameConstraint(Constraint oldName);

    /**
     * Add a <code>RENAME CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    AlterTableRenameConstraintToStep renameConstraint(Name oldName);

    /**
     * Add a <code>RENAME CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, H2, HSQLDB, POSTGRES })
    AlterTableRenameConstraintToStep renameConstraint(String oldName);

    /**
     * Add an <code>ALTER CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #alterConstraint(Constraint)}.
     */
    @NotNull @CheckReturnValue
    @Support({ MYSQL })
    AlterTableAlterConstraintStep alter(Constraint constraint);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #alterColumn(Field)}.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    <T> AlterTableAlterStep<T> alter(Field<T> field);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #alterColumn(Name)}
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableAlterStep<Object> alter(Name field);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #alterColumn(String)}
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableAlterStep<Object> alter(String field);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    <T> AlterTableAlterStep<T> alterColumn(Field<T> field);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableAlterStep<Object> alterColumn(Name field);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableAlterStep<Object> alterColumn(String field);

    /**
     * Add an <code>ALTER CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ MYSQL })
    AlterTableAlterConstraintStep alterConstraint(Constraint constraint);

    /**
     * Add an <code>ALTER CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ MYSQL })
    AlterTableAlterConstraintStep alterConstraint(Name constraint);

    /**
     * Add an <code>ALTER CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ MYSQL })
    AlterTableAlterConstraintStep alterConstraint(String constraint);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumn(Field)}.
     */
    @NotNull @CheckReturnValue
    @Support
    AlterTableAddStep add(Field<?> field);

    /**
     * Add an <code>ADD</code> clause with multiple columns or constraints to
     * the <code>ALTER TABLE</code> statement.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableAddStep add(FieldOrConstraint... fields);

    /**
     * Add an <code>ADD</code> clause with multiple columns or constraints to
     * the <code>ALTER TABLE</code> statement.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableAddStep add(Collection<? extends FieldOrConstraint> fields);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumn(Field, DataType)}.
     */
    @NotNull @CheckReturnValue
    @Support
    <T> AlterTableAddStep add(Field<T> field, DataType<T> type);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumn(Name, DataType)}.
     */
    @NotNull @CheckReturnValue
    @Support
    AlterTableAddStep add(Name field, DataType<?> type);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumn(String, DataType)}.
     */
    @NotNull @CheckReturnValue
    @Support
    AlterTableAddStep add(String field, DataType<?> type);

    /**
     * Add an <code>ADD COLUMN IF NOT EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumnIfNotExists(Field)}.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableAddStep addIfNotExists(Field<?> field);

    /**
     * Add an <code>ADD COLUMN IF NOT EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumnIfNotExists(Field, DataType)}.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    <T> AlterTableAddStep addIfNotExists(Field<T> field, DataType<T> type);

    /**
     * Add an <code>ADD COLUMN IF NOT EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumnIfNotExists(Name, DataType)}.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableAddStep addIfNotExists(Name field, DataType<?> type);

    /**
     * Add an <code>ADD COLUMN IF NOT EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumnIfNotExists(String, DataType)}.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableAddStep addIfNotExists(String field, DataType<?> type);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support
    AlterTableAddStep addColumn(Field<?> field);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support
    <T> AlterTableAddStep addColumn(Field<T> field, DataType<T> type);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support
    AlterTableAddStep addColumn(Name field, DataType<?> type);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support
    AlterTableAddStep addColumn(String field, DataType<?> type);

    /**
     * Add an <code>ADD COLUMN IF NOT EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableAddStep addColumnIfNotExists(Field<?> field);

    /**
     * Add an <code>ADD COLUMN IF NOT EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    <T> AlterTableAddStep addColumnIfNotExists(Field<T> field, DataType<T> type);

    /**
     * Add an <code>ADD COLUMN IF NOT EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableAddStep addColumnIfNotExists(Name field, DataType<?> type);

    /**
     * Add an <code>ADD COLUMN IF NOT EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableAddStep addColumnIfNotExists(String field, DataType<?> type);

    /**
     * Add an <code>ADD CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableUsingIndexStep add(Constraint constraint);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #dropColumn(Field)}.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep drop(Field<?> field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #dropColumn(Name)}.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep drop(Name field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #dropColumn(String)}.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep drop(String field);

    /**
     * Add an <code>DROP COLUMN IF EXISTS</code> clause to the
     * <code>ALTER TABLE</code> statement.
     * <p>
     * This is an alias for {@link #dropColumnIfExists(Field)}.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableDropStep dropIfExists(Field<?> field);

    /**
     * Add an <code>DROP COLUMN IF EXISTS</code> clause to the
     * <code>ALTER TABLE</code> statement.
     * <p>
     * This is an alias for {@link #dropColumnIfExists(Name)}.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableDropStep dropIfExists(Name field);

    /**
     * Add an <code>DROP COLUMN IF EXISTS</code> clause to the
     * <code>ALTER TABLE</code> statement.
     * <p>
     * This is an alias for {@link #dropColumnIfExists(String)}.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableDropStep dropIfExists(String field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropColumn(Field<?> field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropColumn(Name field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropColumn(String field);

    /**
     * Add an <code>DROP COLUMN IF EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableDropStep dropColumnIfExists(Field<?> field);

    /**
     * Add an <code>DROP COLUMN IF EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableDropStep dropColumnIfExists(Name field);

    /**
     * Add an <code>DROP COLUMN IF EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, IGNITE, MARIADB, POSTGRES })
    AlterTableDropStep dropColumnIfExists(String field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #dropColumns(Collection)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep drop(Field<?>... fields);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #dropColumns(Collection)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep drop(Name... fields);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #dropColumns(Collection)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep drop(String... fields);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropColumns(Field<?>... fields);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropColumns(Name... fields);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropColumns(String... fields);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #dropColumns(Collection)}.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep drop(Collection<? extends Field<?>> fields);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ FIREBIRD, H2, IGNITE, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropColumns(Collection<? extends Field<?>> fields);

    /**
     * Add a <code>DROP CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    AlterTableDropStep drop(Constraint constraint);

    /**
     * Add a <code>DROP CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     *
     * @see DSL#constraint(String)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    AlterTableDropStep dropConstraint(Constraint constraint);

    /**
     * Add a <code>DROP CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     *
     * @see DSL#constraint(String)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    AlterTableDropStep dropConstraint(Name constraint);

    /**
     * Add a <code>DROP CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     *
     * @see DSL#constraint(String)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    AlterTableDropStep dropConstraint(String constraint);

    /**
     * Add a <code>DROP CONSTRAINT IF EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    AlterTableDropStep dropIfExists(Constraint constraint);

    /**
     * Add a <code>DROP CONSTRAINT IF EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     *
     * @see DSL#constraint(String)
     */
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    AlterTableDropStep dropConstraintIfExists(Constraint constraint);

    /**
     * Add a <code>DROP CONSTRAINT IF EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     *
     * @see DSL#constraint(String)
     */
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    AlterTableDropStep dropConstraintIfExists(Name constraint);

    /**
     * Add a <code>DROP CONSTRAINT IF EXISTS</code> clause to the <code>ALTER TABLE</code>
     * statement.
     *
     * @see DSL#constraint(String)
     */
    @NotNull @CheckReturnValue
    @Support({ H2, POSTGRES })
    AlterTableDropStep dropConstraintIfExists(String constraint);

    /**
     * Add a <code>DROP PRIMARY KEY</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ DERBY, H2, HSQLDB, MARIADB, MYSQL })
    AlterTableDropStep dropPrimaryKey();

    /**
     * Add a <code>DROP PRIMARY KEY</code> clause to the
     * <code>ALTER TABLE</code> statement.
     * <p>
     * Dialect families derived from MySQL do not know named constraints, in
     * case of which this clause simply generates <code>DROP PRIMARY KEY</code>
     * as in {@link #dropPrimaryKey()}. In other dialect families, this produces
     * a <code>DROP CONSTRAINT [name]</code> clause, as in
     * {@link #dropConstraint(Constraint)}.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropPrimaryKey(Constraint constraint);

    /**
     * Add a <code>DROP PRIMARY KEY</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * Dialect families derived from MySQL do not know named constraints, in
     * case of which this clause simply generates <code>DROP PRIMARY KEY</code>
     * as in {@link #dropPrimaryKey()}. In other dialect families, this produces
     * a <code>DROP CONSTRAINT [name]</code> clause, as in
     * {@link #dropConstraint(Name)}.
     *
     * @see DSL#constraint(Name)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropPrimaryKey(Name constraint);

    /**
     * Add a <code>DROP PRIMARY KEY</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * Dialect families derived from MySQL do not know named constraints, in
     * case of which this clause simply generates <code>DROP PRIMARY KEY</code>
     * as in {@link #dropPrimaryKey()}. In other dialect families, this produces
     * a <code>DROP CONSTRAINT [name]</code> clause, as in
     * {@link #dropConstraint(String)}.
     *
     * @see DSL#constraint(String)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropPrimaryKey(String constraint);

    /**
     * Add a <code>DROP UNIQUE</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * Some dialects (e.g. {@link SQLDialect#COCKROACHDB}) may not be able to
     * drop constraints by name. If users specify the constraint type
     * <em>and</em> the name, however, then the syntax can be emulated, e.g.
     * using <code>DROP INDEX .. CASCADE</code>.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropUnique(Constraint constraint);

    /**
     * Add a <code>DROP UNIQUE</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * Some dialects (e.g. {@link SQLDialect#COCKROACHDB}) may not be able to
     * drop constraints by name. If users specify the constraint type
     * <em>and</em> the name, however, then the syntax can be emulated, e.g.
     * using <code>DROP INDEX .. CASCADE</code>.
     *
     * @see DSL#constraint(Name)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropUnique(Name constraint);

    /**
     * Add a <code>DROP UNIQUE</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * Some dialects (e.g. {@link SQLDialect#COCKROACHDB}) may not be able to
     * drop constraints by name. If users specify the constraint type
     * <em>and</em> the name, however, then the syntax can be emulated, e.g.
     * using <code>DROP INDEX .. CASCADE</code>.
     *
     * @see DSL#constraint(String)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropUnique(String constraint);

    /**
     * Add a <code>DROP FOREIGN KEY</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropForeignKey(Constraint constraint);

    /**
     * Add a <code>DROP FOREIGN KEY</code> clause to the <code>ALTER TABLE</code>
     * statement.
     *
     * @see DSL#constraint(Name)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropForeignKey(Name constraint);

    /**
     * Add a <code>DROP FOREIGN KEY</code> clause to the <code>ALTER TABLE</code>
     * statement.
     *
     * @see DSL#constraint(String)
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropForeignKey(String constraint);
}
