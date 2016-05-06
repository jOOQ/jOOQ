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

import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...

/**
 * This type is used for the {@link Merge}'s DSL API.
 * <p>
 * Example: <code><pre>
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
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface MergeUsingStep<R extends Record> extends MergeKeyStepN<R> {

    /**
     * Add the <code>USING</code> clause to the SQL standard <code>MERGE</code>
     * statement
     */
    @Support({ CUBRID, HSQLDB })
    MergeOnStep<R> using(TableLike<?> table);

    /**
     * Add a dummy <code>USING</code> clause to the SQL standard
     * <code>MERGE</code> statement
     * <p>
     * This results in <code>USING(SELECT 1 FROM DUAL)</code> for most RDBMS, or
     * in <code>USING(SELECT 1) AS [dummy_table(dummy_field)]</code> in SQL
     * Server, where derived tables need to be aliased.
     */
    @Support({ CUBRID, HSQLDB })
    MergeOnStep<R> usingDual();
}
