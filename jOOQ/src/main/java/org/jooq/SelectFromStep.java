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
 */
package org.jooq;

import java.util.Collection;

import org.jooq.impl.DSL;

/**
 * This type is used for the {@link Select}'s DSL API when selecting generic
 * {@link Record} types.
 * <p>
 * Example: <code><pre>
 * -- get all authors' first and last names, and the number
 * -- of books they've written in German, if they have written
 * -- more than five books in German in the last three years
 * -- (from 2011), and sort those authors by last names
 * -- limiting results to the second and third row
 *
 *   SELECT T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME, COUNT(*)
 *     FROM T_AUTHOR
 *     JOIN T_BOOK ON T_AUTHOR.ID = T_BOOK.AUTHOR_ID
 *    WHERE T_BOOK.LANGUAGE = 'DE'
 *      AND T_BOOK.PUBLISHED > '2008-01-01'
 * GROUP BY T_AUTHOR.FIRST_NAME, T_AUTHOR.LAST_NAME
 *   HAVING COUNT(*) > 5
 * ORDER BY T_AUTHOR.LAST_NAME ASC NULLS FIRST
 *    LIMIT 2
 *   OFFSET 1
 *      FOR UPDATE
 *       OF FIRST_NAME, LAST_NAME
 *       NO WAIT
 * </pre></code> Its equivalent in jOOQ <code><pre>
 * create.select(TAuthor.FIRST_NAME, TAuthor.LAST_NAME, create.count())
 *       .from(T_AUTHOR)
 *       .join(T_BOOK).on(TBook.AUTHOR_ID.equal(TAuthor.ID))
 *       .where(TBook.LANGUAGE.equal("DE"))
 *       .and(TBook.PUBLISHED.greaterThan(parseDate('2008-01-01')))
 *       .groupBy(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .having(create.count().greaterThan(5))
 *       .orderBy(TAuthor.LAST_NAME.asc().nullsFirst())
 *       .limit(2)
 *       .offset(1)
 *       .forUpdate()
 *       .of(TAuthor.FIRST_NAME, TAuthor.LAST_NAME)
 *       .noWait();
 * </pre></code> Refer to the manual for more details
 *
 * @author Lukas Eder
 */
public interface SelectFromStep<R extends Record> extends SelectWhereStep<R> {

    /**
     * Add a <code>FROM</code> clause to the query.
     */
    @Support
    SelectJoinStep<R> from(TableLike<?> table);

    /**
     * Add a <code>FROM</code> clause to the query.
     */
    @Support
    SelectJoinStep<R> from(TableLike<?>... tables);

    /**
     * Add a <code>FROM</code> clause to the query.
     */
    @Support
    SelectJoinStep<R> from(Collection<? extends TableLike<?>> tables);

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
    @Support
    @PlainSQL
    SelectJoinStep<R> from(SQL sql);

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
    @Support
    @PlainSQL
    SelectJoinStep<R> from(String sql);

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
    @Support
    @PlainSQL
    SelectJoinStep<R> from(String sql, Object... bindings);

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
    @Support
    @PlainSQL
    SelectJoinStep<R> from(String sql, QueryPart... parts);

    /**
     * Add a <code>FROM</code> clause to the query.
     *
     * @see DSL#table(Name)
     */
    @Support
    SelectJoinStep<R> from(Name name);

    /**
     * Add an Oracle-style hint to the preceding select clause.
     * <p>
     * Example: <code><pre>
     * DSLContext create = DSL.using(configuration);
     *
     * create.select(field1, field2)
     *       .hint("/*+ALL_ROWS&#42;/")
     *       .from(table1)
     *       .fetch();
     * </pre></code>
     * <p>
     * You can also use this clause for any other database, that accepts hints
     * or options at the same syntactic location, e.g. for MySQL's
     * <code>SQL_CALC_FOUND_ROWS</code> option: <code><pre>
     * create.select(field1, field2)
     *       .hint("SQL_CALC_FOUND_ROWS")
     *       .from(table1)
     *       .fetch();
     * </pre></code>
     * <p>
     * The outcome of such a query is this: <code><pre>
     * SELECT [hint] field1, field2 FROM table1
     * </pre></code>
     * <p>
     * For SQL Server style table hints, see {@link Table#with(String)}
     *
     * @see Table#with(String)
     * @see SelectQuery#addHint(String)
     */
    @Support
    SelectFromStep<R> hint(String hint);
}
