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

// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...

import java.util.Collection;

import org.jooq.impl.DSL;

/**
 * This type is used for the {@link Update}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.update(table)
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .where(field1.greaterThan(100))
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
public interface UpdateFromStep<R extends Record> extends UpdateWhereStep<R> {

    /**
     * Add a <code>FROM</code> clause to the query.
     */
    @Support({ POSTGRES })
    UpdateWhereStep<R> from(TableLike<?> table);

    /**
     * Add a <code>FROM</code> clause to the query.
     */
    @Support({ POSTGRES })
    UpdateWhereStep<R> from(TableLike<?>... table);

    /**
     * Add a <code>FROM</code> clause to the query.
     */
    @Support({ POSTGRES })
    UpdateWhereStep<R> from(Collection<? extends TableLike<?>> tables);

    /**
     * Add a <code>FROM</code> clause to the query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(SQL)
     * @see SQL
     */
    @Support({ POSTGRES })
    @PlainSQL
    UpdateWhereStep<R> from(SQL sql);

    /**
     * Add a <code>FROM</code> clause to the query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String)
     * @see SQL
     */
    @Support({ POSTGRES })
    @PlainSQL
    UpdateWhereStep<R> from(String sql);

    /**
     * Add a <code>FROM</code> clause to the query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, Object...)
     * @see DSL#sql(String, Object...)
     * @see SQL
     */
    @Support({ POSTGRES })
    @PlainSQL
    UpdateWhereStep<R> from(String sql, Object... bindings);

    /**
     * Add a <code>FROM</code> clause to the query.
     * <p>
     * <b>NOTE</b>: When inserting plain SQL into jOOQ objects, you must
     * guarantee syntax integrity. You may also create the possibility of
     * malicious SQL injection. Be sure to properly use bind variables and/or
     * escape literals when concatenated into SQL clauses!
     *
     * @see DSL#table(String, QueryPart...)
     * @see DSL#sql(String, QueryPart...)
     * @see SQL
     */
    @Support({ POSTGRES })
    @PlainSQL
    UpdateWhereStep<R> from(String sql, QueryPart... parts);

    /**
     * Add a <code>FROM</code> clause to the query.
     *
     * @see DSL#table(Name)
     */
    @Support({ POSTGRES })
    UpdateWhereStep<R> from(Name name);
}
