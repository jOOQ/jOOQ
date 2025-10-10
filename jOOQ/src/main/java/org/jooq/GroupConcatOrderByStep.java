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
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DUCKDB;
// ...
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...

import java.util.Collection;

import org.jooq.impl.DSL;


/**
 * MySQL's <code>GROUP_CONCAT</code> function.
 *
 * @author Lukas Eder
 * @see DSL#listAgg(Field)
 */
public interface GroupConcatOrderByStep extends GroupConcatSeparatorStep {

    /**
     * Add an <code>ORDER BY</code> clause to the function.
     */
    @NotNull
    @Support({ CUBRID, DUCKDB, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    GroupConcatSeparatorStep orderBy(OrderField<?>... fields);

    /**
     * Add an <code>ORDER BY</code> clause to the function.
     */
    @NotNull
    @Support({ CUBRID, DUCKDB, H2, HSQLDB, MARIADB, MYSQL, POSTGRES })
    GroupConcatSeparatorStep orderBy(Collection<? extends OrderField<?>> fields);
}
