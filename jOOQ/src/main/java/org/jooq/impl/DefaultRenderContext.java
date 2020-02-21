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
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.SettingsTools.renderLocale;
import static org.jooq.impl.Identifiers.QUOTES;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER_ESCAPED;
import static org.jooq.impl.Identifiers.QUOTE_START_DELIMITER;
import static org.jooq.impl.Keywords.K_WITH;
import static org.jooq.impl.ScopeMarkers.AFTER_LAST_TOP_LEVEL_CTE;
import static org.jooq.impl.ScopeMarkers.BEFORE_FIRST_TOP_LEVEL_CTE;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_COUNT_BIND_VALUES;
import static org.jooq.impl.Tools.DataKey.DATA_TOP_LEVEL_CTE;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.conf.RenderFormatting;
import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.Tools.DataKey;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
class DefaultRenderContext extends AbstractContext<RenderContext> implements RenderContext {

    private static final JooqLogger       log                = JooqLogger.getLogger(DefaultRenderContext.class);

    private static final Pattern          IDENTIFIER_PATTERN = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");
    private static final Pattern          NEWLINE            = Pattern.compile("[\\n\\r]");
    private static final Set<String>      SQLITE_KEYWORDS;

    final StringBuilder                   sql;
    private final QueryPartList<Param<?>> bindValues;
    private int                           params;
    private int                           alias;
    private int                           indent;
    private Deque<Integer>                indentLock;
    private boolean                       separator;
    private boolean                       newline;
    private int                           skipUpdateCounts;

    // [#1632] Cached values from Settings
    RenderKeywordCase                     cachedRenderKeywordCase;
    RenderNameCase                        cachedRenderNameCase;
    RenderQuotedNames                     cachedRenderQuotedNames;
    boolean                               cachedRenderFormatted;

    // [#6525] Cached values from Settings.renderFormatting
    String                                cachedIndentation;
    int                                   cachedIndentWidth;
    String                                cachedNewline;
    int                                   cachedPrintMargin;

    DefaultRenderContext(Configuration configuration) {
        super(configuration, null);

        Settings settings = configuration.settings();

        this.sql = new StringBuilder();
        this.bindValues = new QueryPartList<>();
        this.cachedRenderKeywordCase = SettingsTools.getRenderKeywordCase(settings);
        this.cachedRenderFormatted = Boolean.TRUE.equals(settings.isRenderFormatted());
        this.cachedRenderNameCase = SettingsTools.getRenderNameCase(settings);
        this.cachedRenderQuotedNames = SettingsTools.getRenderQuotedNames(settings);

        RenderFormatting formatting = settings.getRenderFormatting();
        if (formatting == null)
            formatting = new RenderFormatting();

        this.cachedNewline = formatting.getNewline() == null ? "\n" : formatting.getNewline();
        this.cachedIndentation = formatting.getIndentation() == null ? "  " : formatting.getIndentation();
        this.cachedIndentWidth = cachedIndentation.length();
        this.cachedPrintMargin = formatting.getPrintMargin() == null ? 80 : formatting.getPrintMargin();
    }

    DefaultRenderContext(RenderContext context) {
        this(context.configuration());

        paramType(context.paramType());
        qualifyCatalog(context.qualifyCatalog());
        qualifySchema(context.qualifySchema());
        quote(context.quote());
        castMode(context.castMode());
        data().putAll(context.data());

        declareCTE = context.declareCTE();
        declareWindows = context.declareWindows();
        declareFields = context.declareFields();
        declareTables = context.declareTables();
        declareAliases = context.declareAliases();
    }

    // ------------------------------------------------------------------------
    // BindContext API
    // ------------------------------------------------------------------------

    @Override
    public final BindContext bindValue(Object value, Field<?> field) throws DataAccessException {
        throw new UnsupportedOperationException();
    }

    final QueryPartList<Param<?>> bindValues() {
        return bindValues;
    }

    // ------------------------------------------------------------------------
    // RenderContext API
    // ------------------------------------------------------------------------

    @Override
    void scopeMarkStart0(QueryPart part) {
        ScopeStackElement e = scopeStack.getOrCreate(part);
        e.positions = new int[] { sql.length(), -1 };
        e.indent = indent;
    }

    @Override
    void scopeMarkEnd0(QueryPart part) {
        ScopeStackElement e = scopeStack.getOrCreate(part);
        e.positions[1] = sql.length();
    }

    @Override
    public RenderContext scopeRegister(QueryPart part) {
        if (scopeStack.inScope()) {
            if (part instanceof TableImpl) {
                Table<?> root = (Table<?>) part;
                Table<?> child = root;
                List<Table<?>> tables = new ArrayList<>();

                while (root instanceof TableImpl && (child = ((TableImpl<?>) root).child) != null) {
                    tables.add(root);
                    root = child;
                }

                ScopeStackElement e = scopeStack.getOrCreate(root);
                if (e.joinNode == null)
                    e.joinNode = new JoinNode(root);

                JoinNode childNode = e.joinNode;
                for (int i = tables.size() - 1; i >= 0; i--) {
                    Table<?> t = tables.get(i);
                    ForeignKey<?, ?> k = ((TableImpl<?>) t).childPath;

                    JoinNode next = childNode.children.get(k);
                    if (next == null) {
                        next = new JoinNode(t);
                        childNode.children.put(k, next);
                    }

                    childNode = next;
                }
            }
        }

        return this;
    }

    @Override
    void scopeEnd0() {

        // TODO: Think about a more appropriate location for this logic, rather
        // than the generic DefaultRenderContext, which shouldn't know anything
        // about the individual query parts that it is rendering.
        TopLevelCte cte = null;
        ScopeStackElement beforeFirstCte = null;
        ScopeStackElement afterLastCte = null;

        if (subqueryLevel() == 0
                && (cte = (TopLevelCte) data(DATA_TOP_LEVEL_CTE)) != null
                && !cte.isEmpty()) {
            beforeFirstCte = scopeStack.get(BEFORE_FIRST_TOP_LEVEL_CTE);
            afterLastCte = scopeStack.get(AFTER_LAST_TOP_LEVEL_CTE);
        }

        outer:
        for (ScopeStackElement e1 : scopeStack) {
            String replaced = null;

            if (subqueryLevel() != e1.scopeLevel) {
                continue outer;
            }
            else if (e1.positions == null) {
                continue outer;
            }
            else if (e1 == beforeFirstCte) {
                boolean single = cte != null && cte.size() == 1;
                RenderContext render = configuration.dsl().renderContext();

                // There is no WITH clause
                if (afterLastCte != null && e1.positions[0] == afterLastCte.positions[0])
                    render.visit(K_WITH);

                if (single)
                    render.formatIndentStart()
                          .formatSeparator();
                else
                    render.sql(' ');

                render.declareCTE(true).visit(cte).declareCTE(false);

                if (single)
                    render.formatIndentEnd();

                replaced = render.render();
            }
            else if (e1.joinNode == null) {
                continue outer;
            }
            else if (!e1.joinNode.children.isEmpty()) {
                replaced = configuration
                    .dsl()
                    .renderContext()
                    .declareTables(true)
                    .sql('(')
                    .formatIndentStart(e1.indent)
                    .formatIndentStart()
                    .formatNewLine()
                    .visit(e1.joinNode.joinTree())
                    .formatNewLine()
                    .sql(')')
                    .render();
            }

            if (replaced != null) {
                sql.replace(e1.positions[0], e1.positions[1], replaced);
                int shift = replaced.length() - (e1.positions[1] - e1.positions[0]);

                inner:
                for (ScopeStackElement e2 : scopeStack) {
                    if (e2.positions == null)
                        continue inner;

                    if (e2.positions[0] > e1.positions[0]) {
                        e2.positions[0] = e2.positions[0] + shift;
                        e2.positions[1] = e2.positions[1] + shift;
                    }
                }
            }
        }
    }

    final int peekSkipUpdateCounts() {
        return skipUpdateCounts;
    }

    final void incrementSkipUpdateCounts() {
        skipUpdateCounts++;
    }

    @Override
    public final String peekAlias() {
        return "alias_" + (alias + 1);
    }

    @Override
    public final String nextAlias() {
        return "alias_" + (++alias);
    }

    @Override
    public final String render() {
        String prepend = null; 
        String result = sql.toString();
        return prepend == null ? result : prepend + result;
    }

    @Override
    public final String render(QueryPart part) {
        return new DefaultRenderContext(this).visit(part).render();
    }

    @Override
    public final RenderContext keyword(String keyword) {
        return visit(DSL.keyword(keyword));
    }

    @Override
    public final RenderContext sql(String s) {
        return sql(s, s == null || !cachedRenderFormatted);
    }

    @Override
    public final RenderContext sql(String s, boolean literal) {
        if (!literal)
            s = NEWLINE.matcher(s).replaceAll("$0" + indentation());

        if (stringLiteral())
            s = StringUtils.replace(s, "'", stringLiteralEscapedApos);

        sql.append(s);
        separator = false;
        newline = false;
        return this;

    }

    @Override
    public final RenderContext sql(char c) {
        sql.append(c);

        if (c == '\'' && stringLiteral())
            sql.append(c);

        separator = false;
        newline = false;
        return this;
    }

    @Override
    public final RenderContext sql(int i) {
        sql.append(i);
        separator = false;
        newline = false;
        return this;
    }

    @Override
    public final RenderContext sql(long l) {
        sql.append(l);
        separator = false;
        newline = false;
        return this;
    }

    @Override
    public final RenderContext sql(float f) {
        sql.append(f);
        separator = false;
        newline = false;
        return this;
    }

    @Override
    public final RenderContext sql(double d) {
        sql.append(d);
        separator = false;
        newline = false;
        return this;
    }

    @Override
    public final RenderContext formatNewLine() {
        if (cachedRenderFormatted) {
            sql(cachedNewline, true);
            sql(indentation(), true);

            newline = true;
        }

        return this;
    }

    @Override
    public final RenderContext formatNewLineAfterPrintMargin() {
        if (cachedRenderFormatted && cachedPrintMargin > 0)
            if (sql.length() - sql.lastIndexOf(cachedNewline) > cachedPrintMargin)
                formatNewLine();

        return this;
    }

    private final String indentation() {
        return StringUtils.leftPad("", indent, cachedIndentation);
    }

    @Override
    public final RenderContext format(boolean format) {
        cachedRenderFormatted = format;
        return this;
    }

    @Override
    public final boolean format() {
        return cachedRenderFormatted;
    }

    @Override
    public final RenderContext formatSeparator() {
        if (!separator && !newline) {
            if (cachedRenderFormatted)
                formatNewLine();
            else
                sql(" ", true);

            separator = true;
        }

        return this;
    }

    @Override
    public final RenderContext formatIndentStart() {
        return formatIndentStart(cachedIndentWidth);
    }

    @Override
    public final RenderContext formatIndentEnd() {
        return formatIndentEnd(cachedIndentWidth);
    }

    @Override
    public final RenderContext formatIndentStart(int i) {
        if (cachedRenderFormatted) {
            indent += i;

            // [#9193] If we've already generated the separator (and indentation)
            if (newline)
                sql.append(cachedIndentation);
        }

        return this;
    }

    @Override
    public final RenderContext formatIndentEnd(int i) {
        if (cachedRenderFormatted)
            indent -= i;

        return this;
    }

    private final Deque<Integer> indentLock() {
        if (indentLock == null)
            indentLock = new ArrayDeque<>();

        return indentLock;
    }

    @Override
    public final RenderContext formatIndentLockStart() {
        if (cachedRenderFormatted) {
            indentLock().push(indent);
            String[] lines = NEWLINE.split(sql);
            indent = lines[lines.length - 1].length();
        }

        return this;
    }

    @Override
    public final RenderContext formatIndentLockEnd() {
        if (cachedRenderFormatted)
            indent = indentLock().pop();

        return this;
    }

    @Override
    public final RenderContext formatPrintMargin(int margin) {
        cachedPrintMargin = margin;
        return this;
    }

    @Override
    public final RenderContext literal(String literal) {
        // Literal usually originates from NamedQueryPart.getName(). This could
        // be null for CustomTable et al.
        if (literal == null)
            return this;

        SQLDialect family = family();

        // Quoting is needed when explicitly requested...
        boolean needsQuote =

            // [#2367] ... but in SQLite, quoting "normal" literals is generally
            // asking for trouble, as SQLite bends the rules here, see
            // http://www.sqlite.org/lang_keywords.html for details ...
            (family != SQLITE && quote())

        ||

            // [#2367] ... yet, do quote when an identifier is a SQLite keyword
            (family == SQLITE && SQLITE_KEYWORDS.contains(literal.toUpperCase(renderLocale(configuration().settings()))))

        ||

            // [#1982] [#3360] ... yet, do quote when an identifier contains special characters
            (family == SQLITE && !IDENTIFIER_PATTERN.matcher(literal).matches());

        if (RenderNameCase.LOWER == cachedRenderNameCase ||
            RenderNameCase.LOWER_IF_UNQUOTED == cachedRenderNameCase && !quote())
            literal = literal.toLowerCase(renderLocale(configuration().settings()));
        else if (RenderNameCase.UPPER == cachedRenderNameCase ||
                 RenderNameCase.UPPER_IF_UNQUOTED == cachedRenderNameCase && !quote())
            literal = literal.toUpperCase(renderLocale(configuration().settings()));

        if (needsQuote) {
            char[][][] quotes = QUOTES.get(family);

            char start = quotes[QUOTE_START_DELIMITER][0][0];
            char end = quotes[QUOTE_END_DELIMITER][0][0];

            sql(start);

            // [#4922] This micro optimisation does seem to have a significant
            //         effect as the replace call can be avoided in almost all
            //         situations
            if (literal.indexOf(end) > -1)
                sql(StringUtils.replace(literal, new String(quotes[QUOTE_END_DELIMITER][0]), new String(quotes[QUOTE_END_DELIMITER_ESCAPED][0])), true);
            else
                sql(literal, true);

            sql(end);
        }
        else {
            sql(literal, true);
        }

        return this;
    }

    @Override
    @Deprecated
    public final RenderContext sql(QueryPart part) {
        return visit(part);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected final void visit0(QueryPartInternal internal) {
        int before = bindValues.size();
        internal.accept(this);
        int after = bindValues.size();

        // [#4650] In PostgreSQL, UDTConstants are always inlined as ROW(?, ?)
        //         as the PostgreSQL JDBC driver doesn't support SQLData. This
        //         means that the above internal.accept(this) call has already
        //         collected the bind variable. The same is true if custom data
        //         type bindings use Context.visit(Param), in case of which we
        //         must not collect the current Param
        if (after == before && paramType != INLINED && internal instanceof Param) {
            Param<?> param = (Param<?>) internal;

            if (!param.isInline()) {
                bindValues.add(param);

                Integer threshold = settings().getInlineThreshold();
                if (threshold != null && threshold > 0) {
                    checkForceInline(threshold);
                }
                else {
                    switch (family()) {




























                        // [#5701] Tests were conducted with PostgreSQL 9.5 and pgjdbc 9.4.1209
                        case POSTGRES:
                            checkForceInline(32767);
                            break;

                        case SQLITE:
                            checkForceInline(999);
                            break;

                        default:
                            break;
                    }
                }
            }
        }
    }

    private final void checkForceInline(int max) throws ForceInlineSignal {
        if (bindValues.size() > max)
            if (TRUE.equals(data(DATA_COUNT_BIND_VALUES)))
                throw new ForceInlineSignal();
    }

    @Override
    @Deprecated
    public final boolean inline() {
        return paramType == INLINED;
    }

    @Override
    @Deprecated
    public final boolean namedParams() {
        return paramType == NAMED;
    }

    @Override
    @Deprecated
    public final RenderContext inline(boolean i) {
        this.paramType = i ? INLINED : INDEXED;
        return this;
    }

    @Override
    @Deprecated
    public final RenderContext namedParams(boolean r) {
        this.paramType = r ? NAMED : INDEXED;
        return this;
    }

    // ------------------------------------------------------------------------
    // Object API
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("rendering    [");
        sb.append(render());
        sb.append("]\n");
        sb.append("parameters   [");
        sb.append(paramType);
        sb.append("]\n");

        toString(sb);
        return sb.toString();
    }

    // ------------------------------------------------------------------------
    // Static initialisation
    // ------------------------------------------------------------------------

    static {
        SQLITE_KEYWORDS = new HashSet<>();

        // [#2367] Taken from http://www.sqlite.org/lang_keywords.html
        SQLITE_KEYWORDS.addAll(Arrays.asList(
            "ABORT",
            "ACTION",
            "ADD",
            "AFTER",
            "ALL",
            "ALTER",
            "ANALYZE",
            "AND",
            "AS",
            "ASC",
            "ATTACH",
            "AUTOINCREMENT",
            "BEFORE",
            "BEGIN",
            "BETWEEN",
            "BY",
            "CASCADE",
            "CASE",
            "CAST",
            "CHECK",
            "COLLATE",
            "COLUMN",
            "COMMIT",
            "CONFLICT",
            "CONSTRAINT",
            "CREATE",
            "CROSS",
            "CURRENT",
            "CURRENT_DATE",
            "CURRENT_TIME",
            "CURRENT_TIMESTAMP",
            "DATABASE",
            "DEFAULT",
            "DEFERRABLE",
            "DEFERRED",
            "DELETE",
            "DESC",
            "DETACH",
            "DISTINCT",
            "DO",
            "DROP",
            "EACH",
            "ELSE",
            "END",
            "ESCAPE",
            "EXCEPT",
            "EXCLUDE",
            "EXCLUSIVE",
            "EXISTS",
            "EXPLAIN",
            "FAIL",
            "FILTER",
            "FOLLOWING",
            "FOR",
            "FOREIGN",
            "FROM",
            "FULL",
            "GLOB",
            "GROUP",
            "GROUPS",
            "HAVING",
            "IF",
            "IGNORE",
            "IMMEDIATE",
            "IN",
            "INDEX",
            "INDEXED",
            "INITIALLY",
            "INNER",
            "INSERT",
            "INSTEAD",
            "INTERSECT",
            "INTO",
            "IS",
            "ISNULL",
            "JOIN",
            "KEY",
            "LEFT",
            "LIKE",
            "LIMIT",
            "MATCH",
            "NATURAL",
            "NO",
            "NOT",
            "NOTHING",
            "NOTNULL",
            "NULL",
            "OF",
            "OFFSET",
            "ON",
            "OR",
            "ORDER",
            "OTHERS",
            "OUTER",
            "OVER",
            "PARTITION",
            "PLAN",
            "PRAGMA",
            "PRECEDING",
            "PRIMARY",
            "QUERY",
            "RAISE",
            "RANGE",
            "RECURSIVE",
            "REFERENCES",
            "REGEXP",
            "REINDEX",
            "RELEASE",
            "RENAME",
            "REPLACE",
            "RESTRICT",
            "RIGHT",
            "ROLLBACK",
            "ROW",
            "ROWS",
            "SAVEPOINT",
            "SELECT",
            "SET",
            "TABLE",
            "TEMP",
            "TEMPORARY",
            "THEN",
            "TIES",
            "TO",
            "TRANSACTION",
            "TRIGGER",
            "UNBOUNDED",
            "UNION",
            "UNIQUE",
            "UPDATE",
            "USING",
            "VACUUM",
            "VALUES",
            "VIEW",
            "VIRTUAL",
            "WHEN",
            "WHERE",
            "WINDOW",
            "WITH",
            "WITHOUT"
        ));



        /*
         * So, you've found the piece of logic that displays our beautifully-crafted ASCII-art logo that
         * we display in the log files to our jOOQ Open Source Edition and jOOQ Free Trial Edition users
         *
         * You probably came here to see if you can somehow turn it off, e.g. because you found this
         * page here:
         *
         *   http://stackoverflow.com/q/28272284/521799
         *
         * And yes! You can:
         *
         *   a) turn off your logger for org.jooq.Constants
         *   b) set the -Dorg.jooq.no-logo=true property
         *   c) simply patch this file in your sources and rebuild jOOQ
         *   d) buy a commercial license
         *
         * Hint: While a) - c) work, d) is the right answer :-)
         *
         * But before you do any of a) - c), consider this. We give away this awesome software for free,
         * and we'd love to continue giving it away for free, so all we would like to ask you is to
         * continue to show your love and our brand to everyone involved in your software simply in the
         * log files when you load jOOQ. Please don't remove our logo.
         *
         * Thank you very much! If you absolutely must remove this logo, and can live with the guilt
         * and shame, below is the system property that deactivates it.
         *
         * Cheers from the jOOQ Team.
         *
         * --------------------------------------------------
         * DEAR USER, PLEASE READ THE ABOVE BEFORE PROCEEDING
         */
        if (!Boolean.getBoolean("org.jooq.no-logo")) {
            JooqLogger l = JooqLogger.getLogger(Constants.class);
            String message;

            message = "Thank you for using jOOQ " + Constants.FULL_VERSION;






            l.info("\n                                      " +
                   "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
                   "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
                   "\n@@@@@@@@@@@@@@@@  @@        @@@@@@@@@@" +
                   "\n@@@@@@@@@@@@@@@@@@@@        @@@@@@@@@@" +
                   "\n@@@@@@@@@@@@@@@@  @@  @@    @@@@@@@@@@" +
                   "\n@@@@@@@@@@  @@@@  @@  @@    @@@@@@@@@@" +
                   "\n@@@@@@@@@@        @@        @@@@@@@@@@" +
                   "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
                   "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
                   "\n@@@@@@@@@@        @@        @@@@@@@@@@" +
                   "\n@@@@@@@@@@    @@  @@  @@@@  @@@@@@@@@@" +
                   "\n@@@@@@@@@@    @@  @@  @@@@  @@@@@@@@@@" +
                   "\n@@@@@@@@@@        @@  @  @  @@@@@@@@@@" +
                   "\n@@@@@@@@@@        @@        @@@@@@@@@@" +
                   "\n@@@@@@@@@@@@@@@@@@@@@@@  @@@@@@@@@@@@@" +
                   "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
                   "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  " + message +
                   "\n                                      ");
        }

    }

    /**
     * A query execution interception signal.
     * <p>
     * This exception is used as a signal for jOOQ's internals to abort query
     * execution, and return generated SQL back to batch execution.
     */
    class ForceInlineSignal extends ControlFlowSignal {

        /**
         * Generated UID
         */
        private static final long serialVersionUID = -9131368742983295195L;

        public ForceInlineSignal() {
            if (log.isDebugEnabled())
                log.debug("Re-render query", "Forcing bind variable inlining as " + configuration().dialect() + " does not support " + params + " bind variables (or more) in a single query");
        }
    }
}
