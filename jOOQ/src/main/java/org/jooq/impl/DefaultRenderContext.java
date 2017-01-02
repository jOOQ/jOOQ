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
package org.jooq.impl;

import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.RenderNameStyle.LOWER;
import static org.jooq.conf.RenderNameStyle.QUOTED;
import static org.jooq.conf.RenderNameStyle.UPPER;
import static org.jooq.impl.Identifiers.QUOTES;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER;
import static org.jooq.impl.Identifiers.QUOTE_END_DELIMITER_ESCAPED;
import static org.jooq.impl.Identifiers.QUOTE_START_DELIMITER;
import static org.jooq.impl.Tools.DataKey.DATA_COUNT_BIND_VALUES;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderKeywordStyle;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.exception.DataAccessException;
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

    private final StringBuilder           sql;
    private final QueryPartList<Param<?>> bindValues;
    private int                           params;
    private int                           alias;
    private int                           indent;
    private Deque<Integer>                indentLock;
    private int                           printMargin        = 80;
    private boolean                       separator;
    private boolean                       newline;

    // [#1632] Cached values from Settings
    RenderKeywordStyle                    cachedRenderKeywordStyle;
    RenderNameStyle                       cachedRenderNameStyle;
    boolean                               cachedRenderFormatted;

    DefaultRenderContext(Configuration configuration) {
        super(configuration, null);

        Settings settings = configuration.settings();

        this.sql = new StringBuilder();
        this.bindValues = new QueryPartList<Param<?>>();
        this.cachedRenderKeywordStyle = settings.getRenderKeywordStyle();
        this.cachedRenderFormatted = Boolean.TRUE.equals(settings.isRenderFormatted());
        this.cachedRenderNameStyle = settings.getRenderNameStyle();
    }

    DefaultRenderContext(RenderContext context) {
        this(context.configuration());

        paramType(context.paramType());
        qualifyCatalog(context.qualifyCatalog());
        qualifySchema(context.qualifySchema());
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
    public final String peekAlias() {
        return "alias_" + (alias + 1);
    }

    @Override
    public final String nextAlias() {
        return "alias_" + (++alias);
    }

    @Override
    public final String render() {
        return sql.toString();
    }

    @Override
    public final String render(QueryPart part) {
        return new DefaultRenderContext(this).visit(part).render();
    }

    @Override
    public final RenderContext keyword(String keyword) {
        if (RenderKeywordStyle.UPPER == cachedRenderKeywordStyle)
            return sql(keyword.toUpperCase(), true);
        else if (RenderKeywordStyle.LOWER == cachedRenderKeywordStyle)
            return sql(keyword.toLowerCase(), true);
        else
            return sql(keyword, true);
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
    public final RenderContext formatNewLine() {
        if (cachedRenderFormatted) {
            sql("\n", true);
            sql(indentation(), true);

            newline = true;
        }

        return this;
    }

    @Override
    public final RenderContext formatNewLineAfterPrintMargin() {
        if (cachedRenderFormatted && printMargin > 0)
            if (sql.length() - sql.lastIndexOf("\n") > printMargin)
                formatNewLine();

        return this;
    }

    private final String indentation() {
        return StringUtils.leftPad("", indent, " ");
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
        return formatIndentStart(2);
    }

    @Override
    public final RenderContext formatIndentEnd() {
        return formatIndentEnd(2);
    }

    @Override
    public final RenderContext formatIndentStart(int i) {
        if (cachedRenderFormatted)
            indent += i;

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
            indentLock = new ArrayDeque<Integer>();

        return indentLock;
    }

    @Override
    public final RenderContext formatIndentLockStart() {
        if (cachedRenderFormatted) {
            indentLock().push(indent);
            String[] lines = sql.toString().split("[\\n\\r]");
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
        printMargin = margin;
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
            (family != SQLITE && QUOTED == cachedRenderNameStyle)

        ||

            // [#2367] ... yet, do quote when an identifier is a SQLite keyword
            (family == SQLITE && SQLITE_KEYWORDS.contains(literal.toUpperCase()))

        ||

            // [#1982] [#3360] ... yet, do quote when an identifier contains special characters
            (family == SQLITE && !IDENTIFIER_PATTERN.matcher(literal).matches());

        if (!needsQuote) {
            if (LOWER == cachedRenderNameStyle)
                literal = literal.toLowerCase();
            else if (UPPER == cachedRenderNameStyle)
                literal = literal.toUpperCase();

            sql(literal, true);
        }
        else {
            String[][] quotes = QUOTES.get(family);

            char start = quotes[QUOTE_START_DELIMITER][0].charAt(0);
            char end = quotes[QUOTE_END_DELIMITER][0].charAt(0);

            sql(start);

            // [#4922] This micro optimisation does seem to have a significant
            //         effect as the replace call can be avoided in almost all
            //         situations
            if (literal.indexOf(end) > -1)
                sql(StringUtils.replace(literal, quotes[QUOTE_END_DELIMITER][0], quotes[QUOTE_END_DELIMITER_ESCAPED][0]), true);
            else
                sql(literal, true);

            sql(end);
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

    private final void checkForceInline(int max) throws ForceInlineSignal {
        if (bindValues.size() > max)
            if (Boolean.TRUE.equals(data(DATA_COUNT_BIND_VALUES)))
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
        SQLITE_KEYWORDS = new HashSet<String>();

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
            "DROP",
            "EACH",
            "ELSE",
            "END",
            "ESCAPE",
            "EXCEPT",
            "EXCLUSIVE",
            "EXISTS",
            "EXPLAIN",
            "FAIL",
            "FOR",
            "FOREIGN",
            "FROM",
            "FULL",
            "GLOB",
            "GROUP",
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
            "NOTNULL",
            "NULL",
            "OF",
            "OFFSET",
            "ON",
            "OR",
            "ORDER",
            "OUTER",
            "PLAN",
            "PRAGMA",
            "PRIMARY",
            "QUERY",
            "RAISE",
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
            "SAVEPOINT",
            "SELECT",
            "SET",
            "TABLE",
            "TEMP",
            "TEMPORARY",
            "THEN",
            "TO",
            "TRANSACTION",
            "TRIGGER",
            "UNION",
            "UNIQUE",
            "UPDATE",
            "USING",
            "VACUUM",
            "VALUES",
            "VIEW",
            "VIRTUAL",
            "WHEN",
            "WHERE"
        ));

        /* [trial] */

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
        /* [/trial] */
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
