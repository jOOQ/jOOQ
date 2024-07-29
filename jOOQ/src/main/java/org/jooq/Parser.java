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

import org.jooq.impl.ParserException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A SQL parser.
 *
 * @author Lukas Eder
 */
public interface Parser {

    /**
     * Parse a SQL string into a set of {@link Queries}.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed into a set
     *             of {@link Queries}.
     */
    @NotNull
    @Support
    @PlainSQL
    Queries parse(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables into a set of {@link Queries}.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed into a set
     *             of {@link Queries}.
     */
    @NotNull
    @Support
    @PlainSQL
    Queries parse(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string into a {@link Query}.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Query}.
     */
    @Nullable
    @Support
    @PlainSQL
    Query parseQuery(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables into a {@link Query}.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Query}.
     */
    @Nullable
    @Support
    @PlainSQL
    Query parseQuery(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string into a procedural {@link Statement}.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed into a
     *             procedural {@link Statement}.
     */
    @Nullable
    @Support
    @PlainSQL
    Statement parseStatement(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables into a procedural
     * {@link Statement}.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed into a
     *             procedural {@link Statement}.
     */
    @Nullable
    @Support
    @PlainSQL
    Statement parseStatement(String sql, Object... bindings) throws ParserException;

































    /**
     * Parse a SQL string into a {@link ResultQuery}.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link ResultQuery}.
     */
    @Nullable
    @Support
    @PlainSQL
    ResultQuery<?> parseResultQuery(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables into a {@link ResultQuery}.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link ResultQuery}.
     */
    @Nullable
    @Support
    @PlainSQL
    ResultQuery<?> parseResultQuery(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string into a {@link Select} statement.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Select} statement.
     */
    @Nullable
    @Support
    @PlainSQL
    Select<?> parseSelect(String sql) throws ParserException;

    /**
     * Parse a SQL string into a {@link Select} statement.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Select} statement.
     */
    @Nullable
    @Support
    @PlainSQL
    Select<?> parseSelect(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string into a {@link Table}.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Table}.
     */
    @NotNull
    @Support
    @PlainSQL
    Table<?> parseTable(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables into a {@link Table}.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Table}.
     */
    @NotNull
    @Support
    @PlainSQL
    Table<?> parseTable(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string into a {@link Field}.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Field}.
     */
    @NotNull
    @Support
    @PlainSQL
    Field<?> parseField(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables into a {@link Field}.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Field}.
     */
    @NotNull
    @Support
    @PlainSQL
    Field<?> parseField(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string into a {@link Row}.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Row}.
     */
    @NotNull
    @Support
    @PlainSQL
    Row parseRow(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables into a {@link Row}.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Row}.
     */
    @NotNull
    @Support
    @PlainSQL
    Row parseRow(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string into a {@link Condition}.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Condition}.
     */
    @NotNull
    @Support
    @PlainSQL
    Condition parseCondition(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables into a {@link Condition}.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed into a
     *             {@link Condition}.
     */
    @NotNull
    @Support
    @PlainSQL
    Condition parseCondition(String sql, Object... bindings) throws ParserException;

    /**
     * Parse a SQL string into a name {@link Name}.
     *
     * @param sql The SQL string
     * @throws ParserException If the SQL string could not be parsed into a name
     *             {@link Name}.
     */
    @NotNull
    @Support
    @PlainSQL
    Name parseName(String sql) throws ParserException;

    /**
     * Parse a SQL string with bind variables into a name {@link Name}.
     *
     * @param sql The SQL string
     * @param bindings The bind variables
     * @throws ParserException If the SQL string could not be parsed into a name
     *             {@link Name}.
     */
    @NotNull
    @Support
    @PlainSQL
    Name parseName(String sql, Object... bindings) throws ParserException;
}
