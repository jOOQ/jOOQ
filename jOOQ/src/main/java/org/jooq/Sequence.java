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
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...

import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A sequence.
 * <p>
 * Instances can be created using {@link DSL#sequence(Name)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Sequence<T extends Number> extends Qualified, Typed<T> {

    /**
     * Get the start value for this sequence or <code>null</code>, if no such
     * value is specified.
     */
    @Nullable
    Field<T> getStartWith();

    /**
     * Get the increment for this sequence or <code>null</code>, if no such
     * value is specified.
     */
    @Nullable
    Field<T> getIncrementBy();

    /**
     * Get the minimum value for this sequence or <code>null</code>, if no such
     * value is specified.
     */
    @Nullable
    Field<T> getMinvalue();

    /**
     * Get the maximum value for this sequence or <code>null</code>, if no such
     * value is specified.
     */
    @Nullable
    Field<T> getMaxvalue();

    /**
     * Returns {@code true} if this sequence cycles to {@link #getMinvalue()}
     * when it reaches {@link #getMaxvalue()}.
     */
    boolean getCycle();

    /**
     * Get the number of sequence values to cache for this sequence or
     * <code>null</code>, if no such value is specified.
     */
    @Nullable
    Field<T> getCache();

    /**
     * An expression to get the current value of this sequence.
     */
    @NotNull
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    Field<T> currval();

    /**
     * An expression to increment the sequence and get the next value.
     */
    @NotNull
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    Field<T> nextval();

    /**
     * An expression to increment the sequence and get the next values.
     * <p>
     * This is done using {@link DSL#generateSeries(int, int)}.
     */
    @NotNull
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    Select<Record1<T>> nextvals(int size);
}