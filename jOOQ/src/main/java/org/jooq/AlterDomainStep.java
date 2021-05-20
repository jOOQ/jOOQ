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

import static org.jooq.SQLDialect.*;

import java.util.*;

import org.jetbrains.annotations.*;

/**
 * A step in the construction of the <code>ALTER DOMAIN</code> statement.
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
 */
@SuppressWarnings({ "unused" })
public interface AlterDomainStep<T> {

    /**
     * Add the <code>ADD</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainFinalStep add(Constraint addConstraint);

    /**
     * Add the <code>DROP CONSTRAINT</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainDropConstraintCascadeStep dropConstraint(@Stringly.Name String dropConstraint);

    /**
     * Add the <code>DROP CONSTRAINT</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainDropConstraintCascadeStep dropConstraint(Name dropConstraint);

    /**
     * Add the <code>DROP CONSTRAINT</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainDropConstraintCascadeStep dropConstraint(Constraint dropConstraint);

    /**
     * Add the <code>DROP CONSTRAINT IF EXISTS</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainDropConstraintCascadeStep dropConstraintIfExists(@Stringly.Name String dropConstraint);

    /**
     * Add the <code>DROP CONSTRAINT IF EXISTS</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainDropConstraintCascadeStep dropConstraintIfExists(Name dropConstraint);

    /**
     * Add the <code>DROP CONSTRAINT IF EXISTS</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainDropConstraintCascadeStep dropConstraintIfExists(Constraint dropConstraint);

    /**
     * Add the <code>RENAME TO</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainFinalStep renameTo(@Stringly.Name String renameTo);

    /**
     * Add the <code>RENAME TO</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainFinalStep renameTo(Name renameTo);

    /**
     * Add the <code>RENAME TO</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainFinalStep renameTo(Domain<?> renameTo);

    /**
     * Add the <code>RENAME CONSTRAINT</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainRenameConstraintStep renameConstraint(@Stringly.Name String renameConstraint);

    /**
     * Add the <code>RENAME CONSTRAINT</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainRenameConstraintStep renameConstraint(Name renameConstraint);

    /**
     * Add the <code>RENAME CONSTRAINT</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainRenameConstraintStep renameConstraint(Constraint renameConstraint);

    /**
     * Add the <code>RENAME CONSTRAINT IF EXISTS</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainRenameConstraintStep renameConstraintIfExists(@Stringly.Name String renameConstraint);

    /**
     * Add the <code>RENAME CONSTRAINT IF EXISTS</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainRenameConstraintStep renameConstraintIfExists(Name renameConstraint);

    /**
     * Add the <code>RENAME CONSTRAINT IF EXISTS</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainRenameConstraintStep renameConstraintIfExists(Constraint renameConstraint);

    /**
     * Add the <code>SET DEFAULT</code> clause to the <code>ALTER DOMAIN</code> statement.
     *
     * @param setDefault is wrapped as {@link org.jooq.impl.DSL#val(Object)}.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainFinalStep setDefault(T setDefault);

    /**
     * Add the <code>SET DEFAULT</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainFinalStep setDefault(Field<T> setDefault);

    /**
     * Add the <code>DROP DEFAULT</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ FIREBIRD, HSQLDB, POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainFinalStep dropDefault();

    /**
     * Add the <code>SET NOT NULL</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainFinalStep setNotNull();

    /**
     * Add the <code>DROP NOT NULL</code> clause to the <code>ALTER DOMAIN</code> statement.
     */
    @Support({ POSTGRES })
    @NotNull @CheckReturnValue
    AlterDomainFinalStep dropNotNull();
}
