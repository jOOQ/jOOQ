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

import static org.jooq.SQLDialect.CUBRID;
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...
// ...

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * This type is used for the {@link Merge}'s DSL API.
 * <p>
 * Example: <pre><code>
 * DSLContext create = DSL.using(configuration);
 *
 * create.mergeInto(table)
 *       .using(select)
 *       .on(condition)
 *       .whenMatchedThenUpdate()
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .whenNotMatchedThenInsert(field1, field2)
 *       .values(value1, value2)
 *       .execute();
 * </code></pre>
 *
 * @author Lukas Eder
 */
public interface MergeNotMatchedValuesStepN<R extends Record> {

    /**
     * Set <code>VALUES</code> for <code>INSERT</code> in the <code>MERGE</code>
     * statement's <code>WHEN NOT MATCHED THEN INSERT</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    MergeNotMatchedWhereStep<R> values(Object... values);

    /**
     * Set <code>VALUES</code> for <code>INSERT</code> in the <code>MERGE</code>
     * statement's <code>WHEN NOT MATCHED THEN INSERT</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    MergeNotMatchedWhereStep<R> values(Field<?>... values);

    /**
     * Set <code>VALUES</code> for <code>INSERT</code> in the <code>MERGE</code>
     * statement's <code>WHEN NOT MATCHED THEN INSERT</code> clause.
     */
    @NotNull @CheckReturnValue
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES })
    MergeNotMatchedWhereStep<R> values(Collection<?> values);
}
