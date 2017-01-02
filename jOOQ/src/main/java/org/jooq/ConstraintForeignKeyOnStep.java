/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...


/**
 * The step in the {@link Constraint} construction DSL API that allows for
 * adding <code>ON DELETE</code> and <code>ON UPDATE</code> clauses.
 *
 * @author Lukas Eder
 */
public interface ConstraintForeignKeyOnStep extends ConstraintFinalStep {

    /**
     * Add an <code>ON DELETE NO ACTION</code> clause to the
     * <code>FOREIGN KEY</code> constraint.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    ConstraintFinalStep onDeleteNoAction();

    /**
     * Add an <code>ON DELETE RESTRICT</code> clause to the
     * <code>FOREIGN KEY</code> constraint.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    ConstraintFinalStep onDeleteRestrict();

    /**
     * Add an <code>ON DELETE CASCADE</code> clause to the
     * <code>FOREIGN KEY</code> constraint.
     */
    @Support
    ConstraintFinalStep onDeleteCascade();

    /**
     * Add an <code>ON DELETE SET NULL</code> clause to the
     * <code>FOREIGN KEY</code> constraint.
     */
    @Support
    ConstraintFinalStep onDeleteSetNull();

    /**
     * Add an <code>ON DELETE SET DEFAULT</code> clause to the
     * <code>FOREIGN KEY</code> constraint.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    ConstraintFinalStep onDeleteSetDefault();

    /**
     * Add an <code>ON UPDATE NO ACTION</code> clause to the
     * <code>FOREIGN KEY</code> constraint.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    ConstraintFinalStep onUpdateNoAction();

    /**
     * Add an <code>ON UPDATE RESTRICT</code> clause to the
     * <code>FOREIGN KEY</code> constraint.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    ConstraintFinalStep onUpdateRestrict();

    /**
     * Add an <code>ON UPDATE CASCADE</code> clause to the
     * <code>FOREIGN KEY</code> constraint.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    ConstraintFinalStep onUpdateCascade();

    /**
     * Add an <code>ON UPDATE SET NULL</code> clause to the
     * <code>FOREIGN KEY</code> constraint.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    ConstraintFinalStep onUpdateSetNull();

    /**
     * Add an <code>ON UPDATE SET DEFAULT</code> clause to the
     * <code>FOREIGN KEY</code> constraint.
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, MYSQL, POSTGRES, SQLITE })
    ConstraintFinalStep onUpdateSetDefault();

}
