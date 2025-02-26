/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import org.jetbrains.annotations.*;


// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;

/**
 * The step in the <code>ALTER TABLE</code> DSL used to <code>ALTER</code>
 * columns.
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
public interface AlterTableAlterStep<T> {

    /**
     * Specify a new column <code>DEFAULT</code>.
     * <p>
     * This is an alias for {@link #default_(Object)}.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    AlterTableFinalStep defaultValue(T literal);

    /**
     * Specify a new column <code>DEFAULT</code>.
     * <p>
     * This is an alias for {@link #default_(Field)}.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    AlterTableFinalStep defaultValue(Field<T> expression);

    /**
     * Specify a new column <code>DEFAULT</code>.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    AlterTableFinalStep default_(T literal);

    /**
     * Specify a new column <code>DEFAULT</code>.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    AlterTableFinalStep default_(Field<T> expression);

    /**
     * Specify a new column <code>DEFAULT</code>.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    AlterTableFinalStep setDefault(T literal);

    /**
     * Specify a new column <code>DEFAULT</code>.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    AlterTableFinalStep setDefault(Field<T> expression);

    /**
     * Drop the column <code>DEFAULT</code>.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    AlterTableFinalStep dropDefault();

    /**
     * Specify a new column data type.
     * <p>
     * This adds or removes <code>NOT NULL</code> constraints on the column if
     * {@link DataType#nullable()} is specified explicitly (not all databases
     * support this).
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, TRINO, YUGABYTEDB })
    AlterTableFinalStep set(DataType<?> type);

    /**
     * Make the column <code>NOT NULL</code>.
     */
    @NotNull @CheckReturnValue
    @Support({ DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    AlterTableFinalStep setNotNull();

    /**
     * Make the column nullable.
     */
    @NotNull @CheckReturnValue
    @Support({ DERBY, DUCKDB, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, YUGABYTEDB })
    AlterTableFinalStep dropNotNull();
}
