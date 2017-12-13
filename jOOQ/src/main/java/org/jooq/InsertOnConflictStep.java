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

import static org.jooq.SQLDialect.POSTGRES_9_5;

/**
 * This type is used for the {@link Insert}'s DSL API
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.insertInto(table, field1, field2)
 *       .values(value1, value2)
 *       .values(value3, value4)
 *       .onConflict(field1, collation1, opclass1)
 *       .addOnConflict(field2, collation2, opclass2)
 *       .addOnConflict(field3, collation3, opclass3)
 *       .set(field1, value1)
 *       .set(field2, value2)
 *       .execute();
 * </pre></code>
 *
 * @author Timur Shaidullin
 */
public interface InsertOnConflictStep<R extends Record> {
    /**
     * Add a conflict target to <code>ON CONFLICT</code> clause
     * to this query
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictTargetMoreStep<R> addOnConflict(OnConflict onConflict);

    /**
     * Add an column name or index expression to <code>ON CONFLICT</code> clause
     * to this query.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictTargetMoreStep<R> addOnConflict(Field<?> field);

    /**
     * Add an column name or index expression, collation to <code>ON CONFLICT</code> clause
     * to this query.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictTargetMoreStep<R> addOnConflict(Field<?> field, Name collation);

    /**
     * Add an column name or index expression, opclass to <code>ON CONFLICT</code> clause
     * to this query.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictTargetMoreStep<R> addOnConflict(Field<?> field, Keyword opclass);


    /**
     * Add an column name or index expression, collation, opclass to <code>ON CONFLICT</code> clause
     * to this query.
     */
    @Support({ POSTGRES_9_5 })
    InsertOnConflictTargetMoreStep<R> addOnConflict(Field<?> field, Name collation, Keyword opclass);
}
