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
 * A step in the construction of the <code>CREATE SEQUENCE</code> statement.
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
public interface CreateSequenceFlagsStep extends CreateSequenceFinalStep {

    /**
     * Add the <code>START WITH</code> clause to the <code>CREATE SEQUENCE</code> statement.
     *
     * @param startWith is wrapped as {@link org.jooq.impl.DSL#val(Object)}.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep startWith(Number startWith);

    /**
     * Add the <code>START WITH</code> clause to the <code>CREATE SEQUENCE</code> statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep startWith(Field<? extends Number> startWith);

    /**
     * Add the <code>INCREMENT BY</code> clause to the <code>CREATE SEQUENCE</code> statement.
     *
     * @param incrementBy is wrapped as {@link org.jooq.impl.DSL#val(Object)}.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep incrementBy(Number incrementBy);

    /**
     * Add the <code>INCREMENT BY</code> clause to the <code>CREATE SEQUENCE</code> statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep incrementBy(Field<? extends Number> incrementBy);

    /**
     * Add the <code>MINVALUE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     *
     * @param minvalue is wrapped as {@link org.jooq.impl.DSL#val(Object)}.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep minvalue(Number minvalue);

    /**
     * Add the <code>MINVALUE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep minvalue(Field<? extends Number> minvalue);

    /**
     * Add the <code>NO MINVALUE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep noMinvalue();

    /**
     * Add the <code>MAXVALUE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     *
     * @param maxvalue is wrapped as {@link org.jooq.impl.DSL#val(Object)}.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep maxvalue(Number maxvalue);

    /**
     * Add the <code>MAXVALUE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep maxvalue(Field<? extends Number> maxvalue);

    /**
     * Add the <code>NO MAXVALUE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep noMaxvalue();

    /**
     * Add the <code>CYCLE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     */
    @Support({ CUBRID, DERBY, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep cycle();

    /**
     * Add the <code>NO CYCLE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep noCycle();

    /**
     * Add the <code>CACHE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     *
     * @param cache is wrapped as {@link org.jooq.impl.DSL#val(Object)}.
     */
    @Support({ CUBRID, H2, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep cache(Number cache);

    /**
     * Add the <code>CACHE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     */
    @Support({ CUBRID, H2, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep cache(Field<? extends Number> cache);

    /**
     * Add the <code>NO CACHE</code> clause to the <code>CREATE SEQUENCE</code> statement.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    @NotNull @CheckReturnValue
    CreateSequenceFlagsStep noCache();
}
