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

import org.jooq.impl.DSL;

/**
 * A sequence.
 * <p>
 * Instances can be created using {@link DSL#sequence(Name)} and overloads.
 *
 * @author Lukas Eder
 */
public interface Sequence<T extends Number> extends Named {

    /**
     * Get the sequence catalog.
     */
    Catalog getCatalog();

    /**
     * Get the sequence schema
     */
    Schema getSchema();

    /**
     * Get the sequence data type
     */
    DataType<T> getDataType();

    /**
     * Get the start value for this sequence
     */
    Field<T> getStartWith();

    /**
     * Get the increment for this sequence
     */
    Field<T> getIncrementBy();

    /**
     * Get the minimum value for this sequence
     */
    Field<T> getMinValue();

    /**
     * Get the maximum value for this sequence
     */
    Field<T> getMaxValue();

    /**
     * Returns {@code true} if this sequence cycles when it reaches {@link #getMaxValue()}
     */
    boolean getCycle();

    /**
     * Get the number of sequence values to cache for this sequence
     */
    Field<T> getCache();

    /**
     * Get the current value of this sequence
     */
    @Support({ CUBRID, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    Field<T> currval();

    /**
     * Increment the sequence and get the next value
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, MARIADB, POSTGRES })
    Field<T> nextval();
}