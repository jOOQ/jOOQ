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

import java.sql.PreparedStatement;

import org.jooq.RenderContext.CastMode;
import org.jooq.conf.ParamType;
import org.jooq.conf.RenderKeywordStyle;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;

/**
 * A context type that is used for rendering SQL or for binding.
 *
 * @author Lukas Eder
 * @see BindContext
 * @see RenderContext
 */
public interface Context<C extends Context<C>> extends Scope {

    // ------------------------------------------------------------------------
    // Methods specifying the scope of the SQL being rendered
    // ------------------------------------------------------------------------

    /**
     * Visit a <code>QueryPart</code> in the current <code>Context</code>.
     * <p>
     * This method is called by certain <code>QueryPart</code> implementations
     * to recursively visit component <code>QueryPart</code>s.
     *
     * @param part The component <code>QueryPart</code>
     * @throws DataAccessException If something went wrong while visiting the
     *             component <code>QueryPart</code>, e.g. when binding a
     *             variable
     */
    C visit(QueryPart part) throws DataAccessException;

    /**
     * TODO [#2667]
     *
     * Properties of these methods:
     * - A clause is always started / ended, even if it isn't rendered or if it's empty!
     */
    C start(Clause clause);
    C end(Clause clause);

    /**
     * Whether the current context is rendering a SQL field declaration (e.g. a
     * {@link Field} in the <code>SELECT</code> clause of the query).
     */
    boolean declareFields();

    /**
     * Set the new context value for {@link #declareFields()}.
     */
    C declareFields(boolean declareFields);

    /**
     * Whether the current context is rendering a SQL table declaration (e.g. a
     * {@link Table} in the <code>FROM</code> or <code>JOIN</code> clause of the
     * query).
     */
    boolean declareTables();

    /**
     * Set the new context value for {@link #declareTables()}.
     */
    C declareTables(boolean declareTables);

    /**
     * Whether the current context is rendering a SQL alias declarations in
     * {@link #declareTables()} or {@link #declareFields()} sections.
     */
    boolean declareAliases();

    /**
     * Whether the current context is rendering a SQL alias declarations in
     * {@link #declareTables()} or {@link #declareFields()} sections.
     */
    C declareAliases(boolean declareTables);

    /**
     * Whether the current context is rendering a SQL window declaration (e.g. a
     * {@link WindowDefinition} in the <code>WINDOW</code> clause of the query).
     */
    boolean declareWindows();

    /**
     * Set the new context value for {@link #declareWindows()}.
     */
    C declareWindows(boolean declareWindows);

    /**
     * Whether the current context is rendering a common table expression (e.g.
     * a {@link CommonTableExpression} in the <code>WITH</code> clause of the
     * query).
     */
    boolean declareCTE();

    /**
     * Set the new context value for {@link #declareCTE()}.
     */
    C declareCTE(boolean declareCTE);

    /**
     * Whether the current context is rendering a sub-query (nested query).
     */
    boolean subquery();

    /**
     * Set the new context value for {@link #subquery()}.
     */
    C subquery(boolean subquery);

    /**
     * whether the current context is rendering a string literal.
     */
    boolean stringLiteral();

    /**
     * Set the new context value for {@link #stringLiteral()}.
     */
    C stringLiteral(boolean stringLiteral);

    /**
     * Get the next bind index. This increments an internal counter. This is
     * relevant for two use-cases:
     * <ul>
     * <li>When binding variables to a {@link PreparedStatement}. Client code
     * must assure that calling {@link #nextIndex()} is followed by setting a
     * bind value to {@link BindContext#statement()}</li>
     * <li>When rendering unnamed bind variables with
     * {@link RenderContext#paramType()} being to <code>NAMED</code></li>
     * </ul>
     */
    int nextIndex();

    /**
     * Peek the next bind index. This won't increment the internal counter,
     * unlike {@link #nextIndex()}.
     */
    int peekIndex();

    // ------------------------------------------------------------------------
    // Methods used for variable binding
    // ------------------------------------------------------------------------

    /**
     * Retrieve the context's underlying {@link PreparedStatement} if available,
     * or <code>null</code> if this traversal does not operate on a
     * <code>PreparedStatement</code>.
     */
    PreparedStatement statement();

    /**
     * Bind a value using a specific type. This will also increment the internal
     * counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     */
    BindContext bindValue(Object value, Field<?> field) throws DataAccessException;

    // ------------------------------------------------------------------------
    // Methods used for SQL rendering
    // ------------------------------------------------------------------------

    /**
     * Peek the next alias that will be generated by {@link #nextAlias()}.
     */
    String peekAlias();

    /**
     * Return a new alias that is unique for the scope of one query. These
     * aliases are sometimes needed when unaliased projections are defined in
     * subqueries, which can lead to syntax errors.
     */
    String nextAlias();

    /**
     * Render the context's underlying SQL statement.
     */
    String render();

    /**
     * Render a query part in a new context derived from this one. The rendered
     * SQL will not be appended to this context.
     */
    String render(QueryPart part);

    /**
     * Append a SQL keyword to the context's contained {@link StringBuilder}.
     * <p>
     * Use this to have your SQL keyword rendered in {@link RenderKeywordStyle}.
     */
    C keyword(String keyword);

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     */
    C sql(String sql);

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     * <p>
     * Set <code>literal = true</code> to indicate that the
     * <code>RenderContext</code> shall not format the argument SQL.
     */
    C sql(String sql, boolean literal);

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     */
    C sql(char sql);

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     */
    C sql(int sql);

    /**
     * Override the value of {@link Settings#isRenderFormatted()}.
     */
    C format(boolean format);

    /**
     * The value of {@link Settings#isRenderFormatted()}.
     */
    boolean format();

    /**
     * Render a new line character (only if {@link Settings#isRenderFormatted()}
     * is set to <code>true</code>).
     */
    C formatNewLine();

    /**
     * Render a new line character (only if {@link Settings#isRenderFormatted()}
     * is set to <code>true</code>, and the {@link #formatPrintMargin(int)} has
     * been exceeded).
     */
    C formatNewLineAfterPrintMargin();

    /**
     * Render a new line character (only if {@link Settings#isRenderFormatted()}
     * is set to <code>true</code>), or a whitespace separator character
     * otherwise.
     */
    C formatSeparator();

    /**
     * Start indenting subsequent SQL by one level (two characters), if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     * <p>
     * This is the same as calling {@link #formatIndentStart(int)} with a
     * parameter of <code>2</code>
     */
    C formatIndentStart();

    /**
     * Start indenting subsequent SQL by a number of characters, if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     */
    C formatIndentStart(int indent);

    /**
     * Start indenting subsequent SQL at the same level as the current line, if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     */
    C formatIndentLockStart();

    /**
     * Stop indenting subsequent SQL by one level (two characters), if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     * <p>
     * This is the same as calling {@link #formatIndentEnd(int)} with a
     * parameter of <code>2</code>
     */
    C formatIndentEnd();

    /**
     * Stop indenting subsequent SQL by a number of characters, if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     */
    C formatIndentEnd(int indent);

    /**
     * Stop indenting subsequent SQL at the same level as the current line, if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     */
    C formatIndentLockEnd();

    /**
     * Set a print margin that will be applied to formatted SQL, if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     * <p>
     * The default print margin is <code>80</code>. Setting this to zero or a
     * negative value means that no print margin will be applied.
     * <p>
     * The print margin is applied to any of these <code>QueryParts</code>:
     * <ul>
     * <li> {@link Field#in(Field...)} and related expressions</li>
     */
    C formatPrintMargin(int margin);

    /**
     * Append some (quoted) literal to the context's contained
     * {@link StringBuilder}.
     */
    C literal(String literal);

    /**
     * Whether query parts should render qualified names or not.
     */
    boolean qualify();

    /**
     * Set the new context value for {@link #qualify()}.
     * <p>
     * This is the same as {@link #qualifySchema(boolean)}.
     */
    C qualify(boolean qualify);

    /**
     * Whether query parts should render qualified names or not.
     * <p>
     * This is the same as {@link #qualifySchema()}.
     */
    boolean qualifySchema();

    /**
     * Set the new context value for {@link #qualifySchema()}.
     */
    C qualifySchema(boolean qualifySchema);

    /**
     * Whether query parts should render qualified names or not.
     * <p>
     * The catalog can only be qualified when {@link #qualifySchema()} is
     * <code>true</code> as well.
     */
    boolean qualifyCatalog();

    /**
     * Set the new context value for {@link #qualifyCatalog()}.
     * <p>
     * The catalog can only be qualified when {@link #qualifySchema()} is
     * <code>true</code> as well.
     */
    C qualifyCatalog(boolean qualifyCatalog);

    /**
     * Specify, how bind values should be rendered.
     * <p>
     * <ul>
     * <li>As {@link ParamType#INDEXED} parameters: <br/>
     * <code>&#160; ?, ?, ?</code></li>
     * <li>As {@link ParamType#NAMED} parameters: <br/>
     * <code>&#160; :1, :2, :custom_name</code></li>
     * <li>As {@link ParamType#INLINED} parameters: <br/>
     * <code>&#160; 1, 'A', null</code></li>
     * </ul>
     */
    ParamType paramType();

    /**
     * Set the new context value for {@link #paramType()}.
     */
    C paramType(ParamType paramType);

    /**
     * The currently applied cast mode for bind values.
     */
    CastMode castMode();

    /**
     * Set the new cast mode for {@link #castMode()}.
     */
    C castMode(CastMode mode);

    /**
     * Whether casting must be applied. The result follows this logic:
     * <table border="1">
     * <tr>
     * <th>CastMode</th>
     * <th>result</th>
     * </tr>
     * <tr>
     * <td><code>ALWAYS</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>NEVER</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>SOME</code></td>
     * <td><code>true</code> or <code>false</code> depending on the dialect</td>
     * </tr>
     * <tr>
     * <td><code>DEFAULT</code></td>
     * <td><code>null</code></td>
     * </tr>
     * </table>
     *
     * @deprecated - [#3703] - 3.5.0 - Do not use this any longer
     */
    @Deprecated
    Boolean cast();

    /**
     * Set the new cast mode to {@link CastMode#SOME} for a list of dialects.
     *
     * @deprecated - [#3703] - 3.5.0 - Do not use this any longer
     */
    @Deprecated
    C castModeSome(SQLDialect... dialects);
}
