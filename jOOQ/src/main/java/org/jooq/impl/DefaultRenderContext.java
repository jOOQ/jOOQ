/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
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
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.impl.Utils.DATA_COUNT_BIND_VALUES;
import static org.jooq.impl.Utils.DATA_OMIT_CLAUSE_EVENT_EMISSION;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Constants;
import org.jooq.Context;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.VisitContext;
import org.jooq.VisitListener;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.ParamType;
import org.jooq.conf.RenderKeywordStyle;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.exception.ControlFlowSignal;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
class DefaultRenderContext extends AbstractContext<RenderContext> implements RenderContext {

    private static final JooqLogger   log                = JooqLogger.getLogger(DefaultRenderContext.class);

    private static final Pattern      IDENTIFIER_PATTERN = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");
    private static final Pattern      NEWLINE            = Pattern.compile("[\\n\\r]");
    private static final Set<String>  SQLITE_KEYWORDS;

    private final StringBuilder       sql;
    private ParamType                 paramType;
    private int                       params;
    private boolean                   qualify            = true;
    private int                       alias;
    private CastMode                  castMode           = CastMode.DEFAULT;
    private SQLDialect[]              castDialects;
    private int                       indent;
    private Stack<Integer>            indentLock         = new Stack<Integer>();
    private int                       printMargin        = 80;

    // [#1632] Cached values from Settings
    private RenderKeywordStyle        cachedRenderKeywordStyle;
    private RenderNameStyle           cachedRenderNameStyle;
    private boolean                   cachedRenderFormatted;

    // [#2665] VisitListener API
    final VisitListener[]             visitListeners;
    private final DefaultVisitContext visitContext;
    private final Deque<Clause>       visitClauses;
    private final Deque<QueryPart>    visitParts;

    DefaultRenderContext(Configuration configuration) {
        super(configuration);

        Settings settings = configuration.settings();

        this.sql = new StringBuilder();
        this.cachedRenderKeywordStyle = settings.getRenderKeywordStyle();
        this.cachedRenderFormatted = Boolean.TRUE.equals(settings.isRenderFormatted());
        this.cachedRenderNameStyle = settings.getRenderNameStyle();

        VisitListenerProvider[] providers = configuration.visitListenerProviders();

        this.visitListeners = new VisitListener[providers.length];
        this.visitContext = new DefaultVisitContext();
        this.visitClauses = new ArrayDeque<Clause>();
        this.visitParts = new ArrayDeque<QueryPart>();

        for (int i = 0; i < providers.length; i++) {
            this.visitListeners[i] = providers[i].provide();
        }
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
    // VisitListener API
    // ------------------------------------------------------------------------

    @Override
    public final RenderContext start(Clause clause) {
        if (clause != null) {
            visitClauses.addLast(clause);

            for (VisitListener listener : visitListeners) {
                listener.clauseStart(visitContext);
            }
        }

        return this;
    }

    @Override
    public final RenderContext end(Clause clause) {
        if (clause != null) {
            for (VisitListener listener : visitListeners) {
                listener.clauseEnd(visitContext);
            }

            if (visitClauses.removeLast() != clause)
                throw new IllegalStateException("Mismatch between visited clauses!");
        }

        return this;
    }

    @Override
    public final RenderContext visit(QueryPart part) {
        if (part != null) {

            // Issue start clause events
            // -----------------------------------------------------------------
            Clause[] clauses = visitListeners.length > 0 ? clause(part) : null;
            if (clauses != null)
                for (int i = 0; i < clauses.length; i++)
                    start(clauses[i]);

            // Perform the actual visiting, or recurse into the replacement
            // -----------------------------------------------------------------
            QueryPart original = part;
            QueryPart replacement = start(part);

            if (original == replacement)
                super.visit(original);
            else
                visit(replacement);

            end(replacement);

            // Issue end clause events
            // -----------------------------------------------------------------
            if (clauses != null)
                for (int i = clauses.length - 1; i >= 0; i--)
                    end(clauses[i]);
        }

        return this;
    }

    private final QueryPart start(QueryPart part) {
        visitParts.addLast(part);

        for (VisitListener listener : visitListeners) {
            listener.visitStart(visitContext);
        }

        return visitParts.peekLast();
    }

    private final void end(QueryPart part) {
        for (VisitListener listener : visitListeners) {
            listener.visitEnd(visitContext);
        }

        if (visitParts.removeLast() != part)
            throw new RuntimeException("Mismatch between visited query parts");
    }

    /**
     * Emit a clause from a query part being visited.
     * <p>
     * This method returns a clause to emit as a surrounding event before /
     * after visiting a query part. This is needed for all reusable query parts,
     * whose clause type is ambiguous at the container site. An example:
     * <p>
     * <code><pre>SELECT * FROM [A CROSS JOIN B]</pre></code>
     * <p>
     * The type of the above <code>JoinTable</code> modelling
     * <code>A CROSS JOIN B</code> is not known to the surrounding
     * <code>SELECT</code> statement, which only knows {@link Table} types. The
     * {@link Clause#TABLE_JOIN} event that is required to be emitted around the
     * {@link Context#visit(QueryPart)} event has to be issued here in
     * <code>AbstractContext</code>.
     */
    private final Clause[] clause(QueryPart part) {
        if (part instanceof QueryPartInternal && data(DATA_OMIT_CLAUSE_EVENT_EMISSION) == null) {
            return ((QueryPartInternal) part).clauses(this);
        }

        return null;
    }

    /**
     * A {@link VisitContext} is always in the scope of the current
     * {@link RenderContext}.
     */
    private class DefaultVisitContext implements VisitContext {

        @Override
        public final Map<Object, Object> data() {
            return DefaultRenderContext.this.data();
        }

        @Override
        public final Object data(Object key) {
            return DefaultRenderContext.this.data(key);
        }

        @Override
        public final Object data(Object key, Object value) {
            return DefaultRenderContext.this.data(key, value);
        }

        @Override
        public final Configuration configuration() {
            return DefaultRenderContext.this.configuration();
        }

        @Override
        public final Clause clause() {
            return visitClauses.peekLast();
        }

        @Override
        public final Clause[] clauses() {
            return visitClauses.toArray(new Clause[visitClauses.size()]);
        }

        @Override
        public final QueryPart queryPart() {
            return visitParts.peekLast();
        }

        @Override
        public final void queryPart(QueryPart part) {
            visitParts.pollLast();
            visitParts.addLast(part);
        }

        @Override
        public final QueryPart[] queryParts() {
            return visitParts.toArray(new QueryPart[visitParts.size()]);
        }

        @Override
        public final Context<?> context() {
            return DefaultRenderContext.this;
        }

        @Override
        public final RenderContext renderContext() {
            return DefaultRenderContext.this;
        }

        @Override
        public final BindContext bindContext() {
            throw new UnsupportedOperationException("QueryPart traversal listening is currently only supported for RenderContext");
        }
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
        RenderContext local = new DefaultRenderContext(this).visit(part);

        /* [pro] */ /* [trial] */

        // Please do not remove or circumvent the below logic
        // --------------------------------------------------
        // This generated SQL comment is a friendly reminder that you are using
        // a commercial version of jOOQ with a free 30 days trial license. We do
        // not want to bother our honest customers with annoying license
        // verification. So, if you want to remove the below notice, consider
        // purchasing a license from http://www.jooq.org/download

        if (part instanceof Select)
            local.sql(" -- SQL rendered with a free trial version of jOOQ " + Constants.FULL_VERSION);

        /* [/trial] */ /* [/pro] */

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

    @Override
    public final RenderContext formatIndentLockStart() {
        if (cachedRenderFormatted) {
            indentLock.push(indent);
            String[] lines = sql.toString().split("[\\n\\r]");
            indent = lines[lines.length - 1].length();
        }

        return this;
    }

    @Override
    public final RenderContext formatIndentLockEnd() {
        if (cachedRenderFormatted) {
            indent = indentLock.pop();
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

        // Quoting is needed when explicitly requested...
        boolean needsQuote =
            (RenderNameStyle.QUOTED == cachedRenderNameStyle

        // [#2367] ... but in SQLite, quoting "normal" literals is generally
        // asking for trouble, as SQLite bends the rules here, see
        // http://www.sqlite.org/lang_keywords.html for details ...
            && configuration.dialect() != SQLDialect.SQLITE)

        ||

        // [#2367] ... yet, do quote when an identifier is a SQLite keyword
            (configuration.dialect() == SQLDialect.SQLITE
            && SQLITE_KEYWORDS.contains(literal.toUpperCase()))

        ||

        // [#1982] ... yet, do quote when an identifier contains special characters
            (!IDENTIFIER_PATTERN.matcher(literal).matches());

        if (RenderNameStyle.LOWER == cachedRenderNameStyle) {
            literal = literal.toLowerCase();
        }
        else if (RenderNameStyle.UPPER == cachedRenderNameStyle) {
            literal = literal.toUpperCase();
        }

        if (!needsQuote) {
            sql(literal);
        }
        else {
            switch (configuration.dialect().family()) {

                // MySQL supports backticks and double quotes
                case MARIADB:
                case MYSQL:
                    sql("`").sql(StringUtils.replace(literal, "`", "``")).sql("`");
                    break;

                /* [pro] */
                // T-SQL databases use brackets
                case ASE:
                case SQLSERVER:
                case SYBASE:
                    sql("[").sql(StringUtils.replace(literal, "]", "]]")).sql("]");
                    break;

                /* [/pro] */
                // Most dialects implement the SQL standard, using double quotes
                /* [pro] */
                case DB2:
                case INGRES:
                case ORACLE:
                /* [/pro] */
                case CUBRID:
                case DERBY:
                case FIREBIRD:
                case H2:
                case HSQLDB:
                case POSTGRES:
                case SQLITE:
                default:
                    sql('"').sql(StringUtils.replace(literal, "\"", "\"\"")).sql('"');
                    break;
            }
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
        internal.toSQL(this);
    }

    private final void checkForceInline(QueryPart part) throws ForceInlineSignal {
        if (paramType == INLINED)
            return;

        if (part instanceof Param) {
            if (((Param<?>) part).isInline())
                return;

            switch (configuration().dialect().family()) {
                /* [pro] */
                case ASE:
                    checkForceInline(2000);
                    return;

                case INGRES:
                    checkForceInline(1024);
                    return;

                case SQLSERVER:
                    checkForceInline(2100);
                    return;

                /* [/pro] */
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
    public final boolean inline() {
        return paramType == INLINED;
    }

    @Override
    @Deprecated
    public final RenderContext inline(boolean i) {
        this.paramType = i ? INLINED : INDEXED;
        return this;
    }

    @Override
    public final ParamType paramType() {
        return paramType;
    }

    @Override
    public final RenderContext paramType(ParamType p) {
        paramType = p;
        return this;
    }

    @Override
    public final boolean qualify() {
        return qualify;
    }

    @Override
    public final RenderContext qualify(boolean q) {
        this.qualify = q;
        return this;
    }

    @Override
    public final boolean namedParams() {
        return paramType == NAMED;
    }

    @Override
    @Deprecated
    public final RenderContext namedParams(boolean r) {
        this.paramType = r ? NAMED : INDEXED;
        return this;
    }

    @Override
    public final CastMode castMode() {
        return castMode;
    }

    @Override
    public final RenderContext castMode(CastMode mode) {
        this.castMode = mode;
        this.castDialects = null;
        return this;
    }

    @Override
    public final Boolean cast() {
        switch (castMode) {
            case ALWAYS:
                return true;
            case NEVER:
                return false;
            case SOME:
                return asList(castDialects).contains(configuration.dialect());
        }

        return null;
    }

    @Override
    public final RenderContext castModeSome(SQLDialect... dialects) {
        this.castMode = CastMode.SOME;
        this.castDialects = dialects;
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
