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

import org.jooq.impl.ParserException;

/**
 * This is experimental functionality.
 * <p>
 * While the parser API will probably not change between versions, the
 * functionality itself may be subject to change in future releases.
 *
 * @author Lukas Eder
 */
public interface Parser {

    /**
     * Parse a SQL string to a set of queries.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Queries parse(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables to a set of queries.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Queries parse(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string to a query.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Query parseQuery(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables to a query.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Query parseQuery(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string to a result query.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    ResultQuery<?> parseResultQuery(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables to a result query.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    ResultQuery<?> parseResultQuery(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string to a select statement.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Select<?> parseSelect(String sql) throws ParserException;

    /**
     * Parse a SQL string to a select statement.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Select<?> parseSelect(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string to a table.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Table<?> parseTable(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables to a table.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Table<?> parseTable(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string to a field.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Field<?> parseField(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables to a field.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Field<?> parseField(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string to a row.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Row parseRow(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables to a row.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Row parseRow(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string to a condition.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Condition parseCondition(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables to a condition.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Condition parseCondition(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string to a name.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Name parseName(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables to a name.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed.
     */
    @Support
    @PlainSQL
    Name parseName(String sql, Object... bindings) throws ParserException;
}
