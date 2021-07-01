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

import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.function.Consumer;

import org.jooq.RenderContext.CastMode;
import org.jooq.conf.ParamType;
import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @NotNull
    C visit(QueryPart part) throws DataAccessException;

    /**
     * TODO [#2667]
     *
     * Properties of these methods:
     * - A clause is always started / ended, even if it isn't rendered or if it's empty!
     */
    @NotNull
    C start(Clause clause);

    @NotNull
    C end(Clause clause);

    /**
     * Set a data value for a key for the scope of a {@link Consumer}.
     */
    @NotNull
    C data(Object key, Object value, Consumer<? super C> consumer);

    /**
     * Whether the current context is rendering a SQL field declaration (e.g. a
     * {@link Field} in the <code>SELECT</code> clause of the query).
     */
    boolean declareFields();

    /**
     * Set the new context value for {@link #declareFields()}.
     */
    @NotNull
    C declareFields(boolean declareFields);

    /**
     * Set the new context value for {@link #declareFields()} for the scope of a
     * {@link Consumer}.
     */
    @NotNull
    C declareFields(boolean declareFields, Consumer<? super C> consumer);

    /**
     * Whether the current context is rendering a SQL table declaration (e.g. a
     * {@link Table} in the <code>FROM</code> or <code>JOIN</code> clause of the
     * query).
     */
    boolean declareTables();

    /**
     * Set the new context value for {@link #declareTables()}.
     */
    @NotNull
    C declareTables(boolean declareTables);

    /**
     * Set the new context value for {@link #declareTables()} for the scope of a
     * {@link Consumer}.
     */
    @NotNull
    C declareTables(boolean declareTables, Consumer<? super C> consumer);

    /**
     * Whether the current context is rendering a SQL alias declarations in
     * {@link #declareTables()} or {@link #declareFields()} sections.
     */
    boolean declareAliases();

    /**
     * Whether the current context is rendering a SQL alias declarations in
     * {@link #declareTables()} or {@link #declareFields()} sections.
     */
    @NotNull
    C declareAliases(boolean declareTables);

    /**
     * Whether the current context is rendering a SQL alias declarations in
     * {@link #declareTables()} or {@link #declareFields()} sections for the
     * scope of a {@link Consumer}.
     */
    @NotNull
    C declareAliases(boolean declareTables, Consumer<? super C> consumer);

    /**
     * Whether the current context is rendering a SQL window declaration (e.g. a
     * {@link WindowDefinition} in the <code>WINDOW</code> clause of the query).
     */
    boolean declareWindows();

    /**
     * Set the new context value for {@link #declareWindows()}.
     */
    @NotNull
    C declareWindows(boolean declareWindows);

    /**
     * Set the new context value for {@link #declareWindows()} for the scope of
     * a {@link Consumer}.
     */
    @NotNull
    C declareWindows(boolean declareWindows, Consumer<? super C> consumer);



























    /**
     * Whether the current context is rendering a common table expression (e.g.
     * a {@link CommonTableExpression} in the <code>WITH</code> clause of the
     * query).
     */
    boolean declareCTE();

    /**
     * Set the new context value for {@link #declareCTE()}.
     */
    @NotNull
    C declareCTE(boolean declareCTE);

    /**
     * Set the new context value for {@link #declareCTE()} for the scope of a
     * {@link Consumer}.
     */
    @NotNull
    C declareCTE(boolean declareCTE, Consumer<? super C> consumer);

    /**
     * Whether the current context is rendering a sub-query (nested query).
     */
    boolean subquery();

    /**
     * Set the new context value for {@link #subquery()}.
     */
    @NotNull
    C subquery(boolean subquery);

    /**
     * Which level of subqueries we're currently in, starting with 0 for the top
     * level query.
     */
    int subqueryLevel();

    /**
     * Which level of scopes we're currently in, starting with 0 for the top
     * scope.
     */
    int scopeLevel();

    /**
     * Start a new scope.
     */
    @NotNull
    C scopeStart();

    /**
     * Mark the beginning of a scoped query part.
     */
    @NotNull
    C scopeMarkStart(QueryPart part);

    /**
     * Register a "special" query part in the scope, reusing the object from a
     * higher scope, if available.
     */
    @NotNull
    C scopeRegister(QueryPart part);

    /**
     * Combine {@link #scopeRegister(QueryPart, boolean)},
     * {@link #scopeMarkStart(QueryPart)} and {@link #scopeMarkEnd(QueryPart)}.
     */
    @NotNull
    C scopeRegisterAndMark(QueryPart part, boolean forceNew);

    /**
     * Register a "special" query part in the scope, allowing to force
     * registering the object in the new scope, if a higher scope already has
     * the object.
     * <p>
     * [#10992] This is necessary to allow for hiding identifiers from nested
     * scopes.
     */
    @NotNull
    C scopeRegister(QueryPart part, boolean forceNew);

    /**
     * Register a "special" query part in the scope, allowing to force
     * registering the object in the new scope, if a higher scope already has
     * the object, as well as providing a mapped part to map the original part
     * to.
     * <p>
     * [#10716] When wrapping parts of a query in a derived table, additional
     * table mappings may be needed.
     */
    @NotNull
    C scopeRegister(QueryPart part, boolean forceNew, QueryPart mapped);

    /**
     * Retrieve the registered mapping for a query part in the current scope, or
     * <code>null</code> if no such mapping exists.
     */
    @Nullable
    QueryPart scopeMapping(QueryPart part);

    /**
     * Mark the end of a scoped query part.
     */
    @NotNull
    C scopeMarkEnd(QueryPart part);

    /**
     * End a previous SELECT scope.
     */
    @NotNull
    C scopeEnd();

    /**
     * whether the current context is rendering a string literal.
     */
    boolean stringLiteral();

    /**
     * Set the new context value for {@link #stringLiteral()}.
     */
    @NotNull
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

    /**
     * Skip an additional update count produced by this query.
     */
    @NotNull
    C skipUpdateCount();

    /**
     * Skip a number of additional update counts produced by this query.
     */
    @NotNull
    C skipUpdateCounts(int skip);

    /**
     * The number of update counts to be skipped by this query.
     */
    int skipUpdateCounts();

    // ------------------------------------------------------------------------
    // Methods used for variable binding
    // ------------------------------------------------------------------------

    /**
     * Retrieve the context's underlying {@link PreparedStatement} if available,
     * or <code>null</code> if this traversal does not operate on a
     * <code>PreparedStatement</code>.
     */
    @Nullable
    PreparedStatement statement();

    /**
     * Bind a value using a specific type. This will also increment the internal
     * counter.
     *
     * @throws DataAccessException If something went wrong while binding a
     *             variable
     */
    @NotNull
    BindContext bindValue(Object value, Field<?> field) throws DataAccessException;

    // ------------------------------------------------------------------------
    // Methods used for SQL rendering
    // ------------------------------------------------------------------------

    /**
     * Peek the next alias that will be generated by {@link #nextAlias()}.
     */
    @NotNull
    String peekAlias();

    /**
     * Return a new alias that is unique for the scope of one query. These
     * aliases are sometimes needed when unaliased projections are defined in
     * subqueries, which can lead to syntax errors.
     */
    @NotNull
    String nextAlias();

    /**
     * Render the context's underlying SQL statement.
     */
    @NotNull
    String render();

    /**
     * Render a query part in a new context derived from this one. The rendered
     * SQL will not be appended to this context.
     */
    @NotNull
    String render(QueryPart part);

    /**
     * Append a SQL keyword to the context's contained {@link StringBuilder}.
     * <p>
     * Use this to have your SQL keyword rendered in {@link RenderKeywordCase}.
     *
     * @deprecated - 3.10.0 - [#4990] - Use {@link DSL#keyword(String)} instead.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "3.10")
    C keyword(String keyword);

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     */
    @NotNull
    C sql(String sql);

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     * <p>
     * Set <code>literal = true</code> to indicate that the
     * <code>RenderContext</code> shall not format the argument SQL.
     */
    @NotNull
    C sql(String sql, boolean literal);

    /**
     * Append some SQL to the context's contained {@link StringBuilder},
     * followed by the usual calls to {@link #formatIndentStart()} and
     * {@link #formatNewLine()}.
     */
    @NotNull
    C sqlIndentStart(String sql);

    /**
     * Append some SQL to the context's contained {@link StringBuilder} preceded
     * by the usual calls to {@link #formatIndentEnd()} and
     * {@link #formatNewLine()}.
     */
    @NotNull
    C sqlIndentEnd(String sql);

    /**
     * Append some SQL to the context's contained {@link StringBuilder},
     * followed by the usual calls to {@link #formatIndentStart()} and
     * {@link #formatNewLine()}.
     */
    @NotNull
    C sqlIndentStart();

    /**
     * Append some SQL to the context's contained {@link StringBuilder} preceded
     * by the usual calls to {@link #formatIndentEnd()} and
     * {@link #formatNewLine()}.
     */
    @NotNull
    C sqlIndentEnd();

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     */
    @NotNull
    C sql(char sql);

    /**
     * Append some SQL to the context's contained {@link StringBuilder},
     * followed by the usual calls to {@link #formatIndentStart()} and
     * {@link #formatNewLine()}.
     */
    @NotNull
    C sqlIndentStart(char sql);

    /**
     * Append some SQL to the context's contained {@link StringBuilder} preceded
     * by the usual calls to {@link #formatIndentEnd()} and
     * {@link #formatNewLine()}.
     */
    @NotNull
    C sqlIndentEnd(char sql);

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     */
    @NotNull
    C sql(int sql);

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     */
    @NotNull
    C sql(long sql);

    /**
     * A formatter to produce scientific notation for {@link Float} types.
     */
    DecimalFormat floatFormat();

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     */
    @NotNull
    C sql(float sql);

    /**
     * A formatter to produce scientific notation for {@link Double} types.
     */
    DecimalFormat doubleFormat();

    /**
     * Append some SQL to the context's contained {@link StringBuilder}.
     */
    @NotNull
    C sql(double sql);

    /**
     * Override the value of {@link Settings#isRenderFormatted()}.
     */
    @NotNull
    C format(boolean format);

    /**
     * The value of {@link Settings#isRenderFormatted()}.
     */
    boolean format();

    /**
     * Render a new line character (only if {@link Settings#isRenderFormatted()}
     * is set to <code>true</code>).
     */
    @NotNull
    C formatNewLine();

    /**
     * Render a new line character (only if {@link Settings#isRenderFormatted()}
     * is set to <code>true</code>, and the {@link #formatPrintMargin(int)} has
     * been exceeded).
     */
    @NotNull
    C formatNewLineAfterPrintMargin();

    /**
     * Render a new line character (only if {@link Settings#isRenderFormatted()}
     * is set to <code>true</code>), or a whitespace separator character
     * otherwise.
     */
    @NotNull
    C formatSeparator();

    /**
     * Specify that a separator will be required before the next
     * {@link #visit(QueryPart)} call, but leave the decision whether to
     * generate a {@link #formatSeparator()} or just a whitespace to that next
     * {@link QueryPart}.
     */
    @NotNull
    C separatorRequired(boolean separatorRequired);

    /**
     * Whether some sort of separator is required before rendering the next
     * {@link QueryPart}.
     */
    boolean separatorRequired();

    /**
     * Start indenting subsequent SQL by one level (two characters), if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     * <p>
     * This is the same as calling {@link #formatIndentStart(int)} with a
     * parameter of <code>2</code>
     */
    @NotNull
    C formatIndentStart();

    /**
     * Start indenting subsequent SQL by a number of characters, if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     */
    @NotNull
    C formatIndentStart(int indent);

    /**
     * Start indenting subsequent SQL at the same level as the current line, if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     *
     * @deprecated - [#10317] - 3.14.0 - Do not reuse this method. It will be
     *             removed without replacement.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
    C formatIndentLockStart();

    /**
     * Stop indenting subsequent SQL by one level (two characters), if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     * <p>
     * This is the same as calling {@link #formatIndentEnd(int)} with a
     * parameter of <code>2</code>
     */
    @NotNull
    C formatIndentEnd();

    /**
     * Stop indenting subsequent SQL by a number of characters, if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     */
    @NotNull
    C formatIndentEnd(int indent);

    /**
     * Stop indenting subsequent SQL at the same level as the current line, if
     * {@link Settings#isRenderFormatted()} is set to <code>true</code>.
     *
     * @deprecated - [#10317] - 3.14.0 - Do not reuse this method. It will be
     *             removed without replacement.
     */
    @Deprecated(forRemoval = true, since = "3.14")
    @NotNull
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
    @NotNull
    C formatPrintMargin(int margin);

    /**
     * Append some literal to the context's contained {@link StringBuilder}.
     *
     * @deprecated - 3.10.0 - [#4990] - Use any of {@link DSL#name(String)},
     *             {@link DSL#quotedName(String)} or
     *             {@link DSL#unquotedName(String)} instead.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "3.10")
    C literal(String literal);

    /**
     * Whether {@link Name} parts (and {@link #literal(String)}) should be quoted.
     */
    boolean quote();

    /**
     * Set the new context value for {@link #quote()}.
     */
    @NotNull
    C quote(boolean quote);

    /**
     * Set the new context value for {@link #quote()} for the scope of a
     * {@link Consumer}.
     */
    @NotNull
    C quote(boolean quote, Consumer<? super C> consumer);

    /**
     * Whether query parts should render qualified names or not.
     */
    boolean qualify();

    /**
     * Set the new context value for {@link #qualify()}.
     * <p>
     * This is the same as {@link #qualifySchema(boolean)}.
     */
    @NotNull
    C qualify(boolean qualify);

    /**
     * Set the new context value for {@link #qualify()} for the scope of a
     * {@link Consumer}.
     */
    @NotNull
    C qualify(boolean qualify, Consumer<? super C> consumer);

    /**
     * Whether query parts should render qualified names or not.
     * <p>
     * This is the same as {@link #qualifySchema()}.
     */
    boolean qualifySchema();

    /**
     * Set the new context value for {@link #qualifySchema()}.
     */
    @NotNull
    C qualifySchema(boolean qualifySchema);

    /**
     * Set the new context value for {@link #qualifySchema()} for the scope of a
     * {@link Consumer}.
     */
    @NotNull
    C qualifySchema(boolean qualifySchema, Consumer<? super C> consumer);

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
    @NotNull
    C qualifyCatalog(boolean qualifyCatalog);

    /**
     * Set the new context value for {@link #qualifyCatalog()} for the scope of
     * a {@link Consumer}.
     * <p>
     * The catalog can only be qualified when {@link #qualifySchema()} is
     * <code>true</code> as well.
     */
    @NotNull
    C qualifyCatalog(boolean qualifyCatalog, Consumer<? super C> consumer);

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
    @NotNull
    ParamType paramType();

    /**
     * Set the new context value for {@link #paramType()}.
     */
    @NotNull
    C paramType(ParamType paramType);

    /**
     * Visit a query part with a given value for {@link #paramType()}.
     */
    @NotNull
    C visit(QueryPart part, ParamType paramType);

    /**
     * Set the new context value for {@link #paramType()}, if a condition is
     * true.
     */
    @NotNull
    C paramTypeIf(ParamType paramType, boolean condition);

    /**
     * Set the new context value for {@link #paramType()} for the scope of a
     * {@link Consumer}.
     */
    @NotNull
    C paramType(ParamType paramType, Consumer<? super C> runnable);

    /**
     * Set the new context value for {@link #paramType()} for the scope of a
     * {@link Consumer}, if a condition is true.
     */
    @NotNull
    C paramTypeIf(ParamType paramType, boolean condition, Consumer<? super C> runnable);

    /**
     * The current language context.
     */
    @NotNull
    LanguageContext languageContext();

    /**
     * Set the new language context for {@link #languageContext()}
     */
    @NotNull
    C languageContext(LanguageContext languageContext);

    /**
     * Set the new language context for {@link #languageContext()} for the scope
     * of a {@link Consumer}.
     */
    @NotNull
    C languageContext(LanguageContext languageContext, Consumer<? super C> consumer);

    /**
     * Set the new language context for {@link #languageContext()}, if a
     * condition is true.
     */
    @NotNull
    C languageContextIf(LanguageContext languageContext, boolean condition);

    /**
     * The currently applied cast mode for bind values.
     */
    @NotNull
    CastMode castMode();

    /**
     * Set the new cast mode for {@link #castMode()}.
     */
    @NotNull
    C castMode(CastMode mode);

    /**
     * Set the new cast mode for {@link #castMode()} for the scope of a
     * {@link Consumer}.
     */
    @NotNull
    C castMode(CastMode mode, Consumer<? super C> consumer);

    /**
     * Set the new cast mode for {@link #castMode()}, if a condition is true.
     */
    @NotNull
    C castModeIf(CastMode mode, boolean condition);
}
