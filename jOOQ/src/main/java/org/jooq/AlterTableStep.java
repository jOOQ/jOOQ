/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...

import org.jooq.impl.DSL;


/**
 * The step in the <code>ALTER TABLE</code> statement where the action can be
 * decided.
 *
 * @author Lukas Eder
 */
public interface AlterTableStep {

    /**
     * Add a <code>RENAME TO</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableFinalStep renameTo(Table<?> newName);

    /**
     * Add a <code>RENAME TO</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableFinalStep renameTo(Name newName);

    /**
     * Add a <code>RENAME TO</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableFinalStep renameTo(String newName);

    /**
     * Add a <code>RENAME COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableRenameColumnToStep renameColumn(Field<?> oldName);

    /**
     * Add a <code>RENAME COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableRenameColumnToStep renameColumn(Name oldName);

    /**
     * Add a <code>RENAME COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableRenameColumnToStep renameColumn(String oldName);

    /**
     * Add a <code>RENAME CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableRenameConstraintToStep renameConstraint(Constraint oldName);

    /**
     * Add a <code>RENAME CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableRenameConstraintToStep renameConstraint(Name oldName);

    /**
     * Add a <code>RENAME CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableRenameConstraintToStep renameConstraint(String oldName);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #alterColumn(Field)}.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    <T> AlterTableAlterStep<T> alter(Field<T> field);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #alterColumn(Name)}
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableAlterStep<Object> alter(Name field);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #alterColumn(String)}
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableAlterStep<Object> alter(String field);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    <T> AlterTableAlterStep<T> alterColumn(Field<T> field);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableAlterStep<Object> alterColumn(Name field);

    /**
     * Add an <code>ALTER COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableAlterStep<Object> alterColumn(String field);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumn(Field, DataType)}.
     */
    @Support
    <T> AlterTableFinalStep add(Field<T> field, DataType<T> type);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumn(Name, DataType)}.
     */
    @Support
    AlterTableFinalStep add(Name field, DataType<?> type);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #addColumn(String, DataType)}.
     */
    @Support
    AlterTableFinalStep add(String field, DataType<?> type);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    <T> AlterTableFinalStep addColumn(Field<T> field, DataType<T> type);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableFinalStep addColumn(Name field, DataType<?> type);

    /**
     * Add an <code>ADD COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support
    AlterTableFinalStep addColumn(String field, DataType<?> type);

    /**
     * Add an <code>ADD CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableUsingIndexStep add(Constraint constraint);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #dropColumn(Field)}.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep drop(Field<?> field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #dropColumn(Name)}.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep drop(Name field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     * <p>
     * This is an alias for {@link #dropColumn(String)}.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep drop(String field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropColumn(Field<?> field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropColumn(Name field);

    /**
     * Add an <code>DROP COLUMN</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableDropStep dropColumn(String field);

    /**
     * Add a <code>DROP CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableFinalStep drop(Constraint constraint);

    /**
     * Add a <code>DROP CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     *
     * @see DSL#constraint(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableFinalStep dropConstraint(Name constraint);
    /**
     * Add a <code>DROP CONSTRAINT</code> clause to the <code>ALTER TABLE</code>
     * statement.
     *
     * @see DSL#constraint(String)
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    AlterTableFinalStep dropConstraint(String constraint);
}
