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
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL_8_0;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
// ...

/**
 * A derived column list.
 * <p>
 * Thist type models a table name and an optional "derived column list", which
 * can be used to name both tables and columns in one go, e.g. when aliasing a
 * derived table or a {@link CommonTableExpression}.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * <code><pre>
 * // Assuming import static org.jooq.impl.DSL.*;
 *
 * Table<?> t = name("t").fields("v").as(select(one()));
 * //           ^^^^^^^^^^^^^^^^^^^^^ -- DerivedColumnList
 *
 * using(configuration)
 *    .select()
 *    .from(t)
 *    .fetch();
 * </pre></code>
 * <p>
 * Instances can be created using {@link Name#fields(String...)} and overloads.
 *
 * @author Lukas Eder
 */
public interface DerivedColumnList19 extends QueryPart {

    /**
     * Specify a subselect to refer to by the <code>DerivedColumnList</code> to
     * form a common table expression.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES, SQLITE })
    <R extends Record19<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> CommonTableExpression<R> as(Select<R> select);

}
