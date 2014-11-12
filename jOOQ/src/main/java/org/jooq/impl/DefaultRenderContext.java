/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
// ...
// ...
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
import static org.jooq.impl.Utils.DATA_COUNT_BIND_VALUES;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
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
import org.jooq.Select;
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

    private static final JooqLogger  log                = JooqLogger.getLogger(DefaultRenderContext.class);

    private static final Pattern     IDENTIFIER_PATTERN = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");
    private static final Pattern     NEWLINE            = Pattern.compile("[\\n\\r]");
    private static final Set<String> SQLITE_KEYWORDS;

    private final StringBuilder      sql;
    private int                      params;
    private int                      alias;
    private int                      indent;
    private Stack<Integer>           indentLock;
    private int                      printMargin = 80;

    // [#1632] Cached values from Settings
    RenderKeywordStyle               cachedRenderKeywordStyle;
    RenderNameStyle                  cachedRenderNameStyle;
    boolean                          cachedRenderFormatted;

    DefaultRenderContext(Configuration configuration) {
        super(configuration, null);

        Settings settings = configuration.settings();

        this.sql = new StringBuilder();
        this.cachedRenderKeywordStyle = settings.getRenderKeywordStyle();
        this.cachedRenderFormatted = Boolean.TRUE.equals(settings.isRenderFormatted());
        this.cachedRenderNameStyle = settings.getRenderNameStyle();
    }

    DefaultRenderContext(RenderContext context) {
        this(context.configuration());

        paramType(context.paramType());
        qualify(context.qualify());
        castMode(context.castMode());
        declareFields(context.declareFields());
        declareTables(context.declareTables());
        data().putAll(context.data());
    }

    // ------------------------------------------------------------------------
    // BindContext API
    // ------------------------------------------------------------------------

    @Override
    public final BindContext bindValue(Object value, Field<?> field) throws DataAccessException {
        throw new UnsupportedOperationException();
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

    /* [pro] xx xx xxxxxxx xx
    xxxxxxx xxxxxx xxxxx xxxx xxxx
    xxxxxxx xxxxxx xxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxx

    xxxxxx x
        xxxxxx x x xxxxxxxxxxxxx
        xxxxxxxxxxxxxxxxxxxxxxxx x x xxxxx

        xxx x
            x x xxx xxxxxxxxxxxxxxxxxxxxxxxxx
                xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xxxxx

            xxxxxx xxx x xxxxx
            xxxxxx xxxxxxxx x xxx xxxxxxxxxx xxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxx
            xxx x xxx xxxxxxxxxxxxxxxxx

            xxxxxxxxxxxxxxxxxxxxxxx x x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            xxxxxxxxxxxxxxxxxxxxxxx x x xxxxxxxxxxxxxxx

            xx xxxxxxxxxx x
                 x x xxxxxxxxxxx
            x
        x
        xxxxx xxxxxxxxxx xxxxxxx xx
        xxxxxxx x
            xx xx xx xxxxx x
                xxx x
                    xxxxxxxxxx
                x
                xxxxx xxxxxxxxxx xxxxxxx xx
            x
        x

        xx xxxxxx xxxxxxx xxxx xxxxxxxxxx
        xxx x xxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xxx x xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    x

    xx xxxxxxxx xx xx [/pro] */

    @Override
    public final String render(QueryPart part) {
        RenderContext local = new DefaultRenderContext(this).visit(part);

        /* [pro] xx xx xxxxxxx xx

        xx xxxxxx xx xxx xxxxxx xx xxxxxxxxxx xxx xxxxx xxxxx
        xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        xx xxxx xxxxxxxxx xxx xxxxxxx xx x xxxxxxxx xxxxxxxx xxxx xxx xxx xxxxx
        xx x xxxxxxxxxx xxxxxxx xx xxxx xxxx x xxxx xx xxxx xxxxx xxxxxxxx xx xx
        xx xxx xxxx xx xxxxxx xxx xxxxxx xxxxxxxxx xxxx xxxxxxxx xxxxxxx
        xx xxxxxxxxxxxxx xxx xx xxx xxxx xx xxxxxx xxx xxxxx xxxxxxx xxxxxxxx
        xx xxxxxxxxxx x xxxxxxx xxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxx

        xx xxxxx xxxxxxxxxx xxxxxxx x

            xx xx xxxxx xx xxxxxx xx xxxxxx xxxxxx xxxxxx xxx xxx xxxxxxxx
            xx xxxxxxx xx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
            x
            xxxx xx xxxxxxxxxxxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx x
                xxxxxxxxxxx xx xxx xxxxxxxx xxxx x xxxx xxxxx xxxxxxx xx xxxx x x xxxxxxxxxxxxxxxxxxxxxx x x xxxxx
            x
            xxxx x
                xxxxxxxxxxx xx xxx xxxxxxxx xxxx x xxxx xxxxx xxxxxxx xx xxxx x x xxxxxxxxxxxxxxxxxxxxxxxx
            x
        x

        xx xxxxxxxxxxxxxxxxxxxxxx x xxxxxxx x
            xxxxx xxx xxxxxxxxxxxxxxxxxxxxx xxxx xxxxxxxx x xxxxxx xxxxxxx xxxx xxx xxxx xxxxx xxxxxxxx xxxxxx xxxxxxxx xxxxxxxxx xx x xxxxxxxxxx xxxxxxx xx xxxxxxx xxxxxxxxxxxxxxxxxxxxxx xx xxx xxxx xx xxx xxxx xxxxxxx xxxx xxxx xxxx xxxxxxxxx
        x

        xx xxxx x xxxxxxxxxxxxxxxxxxxxxxxxxxx x
            xxxxx xxx xxxxxxxxxxxxxxxxxxxxxx xx xxx xxxxx xxxxxx xxx xxxxx xxxx xxxx xxxx xxxxxx xxxxxxxx xxxxxxxxx xx x xxxxxxxxxx xxxxxxx xx xxxxxxx xxxxxxxxxxxxxxxxxxxxxx xx xxx xxxx xx xxxxxx xxxx xxxx xxxxxxxxx
        x

        xx xxxxxxxx xx xx [/pro] */

        return local.render();
    }

    @Override
    public final RenderContext keyword(String keyword) {
        if (RenderKeywordStyle.UPPER == cachedRenderKeywordStyle) {
            return sql(keyword.toUpperCase());
        }
        else {
            return sql(keyword.toLowerCase());
        }
    }

    @Override
    public final RenderContext sql(String s) {
        return sql(s, s == null || !cachedRenderFormatted);
    }

    @Override
    public final RenderContext sql(String s, boolean literal) {
        if (literal) {
            sql.append(s);
        }
        else {
            sql.append(NEWLINE.matcher(s).replaceAll("$0" + indentation()));
        }

        return this;

    }

    @Override
    public final RenderContext sql(char c) {
        sql.append(c);
        return this;
    }

    @Override
    public final RenderContext sql(int i) {
        sql.append(i);
        return this;
    }

    @Override
    public final RenderContext formatNewLine() {
        if (cachedRenderFormatted) {
            sql.append("\n");
            sql.append(indentation());
        }

        return this;
    }

    @Override
    public final RenderContext formatNewLineAfterPrintMargin() {
        if (cachedRenderFormatted && printMargin > 0) {
            if (sql.length() - sql.lastIndexOf("\n") > printMargin) {
                formatNewLine();
            }
        }

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
        if (cachedRenderFormatted) {
            formatNewLine();
        }
        else {
            sql.append(" ");
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
        if (cachedRenderFormatted) {
            indent += i;
        }

        return this;
    }

    @Override
    public final RenderContext formatIndentEnd(int i) {
        if (cachedRenderFormatted) {
            indent -= i;
        }

        return this;
    }

    private Stack<Integer> indentLock() {
        if (indentLock == null) {
            indentLock = new Stack<Integer>();
        }

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
        if (cachedRenderFormatted) {
            indent = indentLock().pop();
        }

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
        if (literal == null) {
            return this;
        }

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
            if (LOWER == cachedRenderNameStyle) {
                literal = literal.toLowerCase();
            }
            else if (UPPER == cachedRenderNameStyle) {
                literal = literal.toUpperCase();
            }

            sql(literal);
        }
        else {
            String[][] quotes = QUOTES.get(family);

            sql(quotes[QUOTE_START_DELIMITER][0]);
            sql(StringUtils.replace(literal, quotes[QUOTE_END_DELIMITER][0], quotes[QUOTE_END_DELIMITER_ESCAPED][0]));
            sql(quotes[QUOTE_END_DELIMITER][0]);
        }

        return this;
    }

    @Override
    @Deprecated
    public final RenderContext sql(QueryPart part) {
        return visit(part);
    }

    @Override
    protected final void visit0(QueryPartInternal internal) {
        checkForceInline(internal);
        internal.accept(this);
    }

    private final void checkForceInline(QueryPart part) throws ForceInlineSignal {
        if (paramType == INLINED)
            return;

        if (part instanceof Param) {
            if (((Param<?>) part).isInline())
                return;

            switch (configuration().dialect().family()) {
                /* [pro] xx
                xxxx xxxxxxx

                    xx xxxxxxxxxxx xxxx xxxxx xxxx xxx xxxxx xxxxx xx xx xxx xxxx xxxxxxxxx xxx xx xxxxxx
                    xx xxxxx xxxxxxx xxxx xxxxxx xx xxxx xx xxx xxxx xxxx
                    xxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxx

                xxxx xxxx
                    xxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxx

                xxxx xxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxx

                xxxx xxxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxx
                    xxxxxxx

                xx [/pro] */
                case SQLITE:
                    checkForceInline(999);
                    return;

                default:
                    return;
            }
        }
    }

    private final void checkForceInline(int max) throws ForceInlineSignal {
        if (Boolean.TRUE.equals(data(DATA_COUNT_BIND_VALUES)))
            if (++params > max)
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
        JooqLogger l = JooqLogger.getLogger(Constants.class);
        String message;

        message = "Thank you for using jOOQ " + Constants.FULL_VERSION;

        /* [pro] xx
        xxxxxxx x xxxxxx xxx xxx xxxxx xxx xx xxx xxxx xxxx x x xxxxxxxxxxxxxxxxxxxxxx x x xxxxx xxxxxxxxx
        xx [/pro] */


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
