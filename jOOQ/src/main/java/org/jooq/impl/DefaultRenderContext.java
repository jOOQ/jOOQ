/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.impl.Utils.DATA_COUNT_BIND_VALUES;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import org.jooq.Configuration;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
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

    private static final JooqLogger  log                = JooqLogger.getLogger(DefaultRenderContext.class);

    private static final Pattern     IDENTIFIER_PATTERN = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");
    private static final Pattern     NEWLINE            = Pattern.compile("[\\n\\r]");
    private static final Set<String> SQLITE_KEYWORDS;

    private final StringBuilder      sql;
    private ParamType            paramType;
    private int                      params;
    private boolean                  qualify            = true;
    private int                      alias;
    private CastMode                 castMode           = CastMode.DEFAULT;
    private SQLDialect[]             castDialects;
    private int                      indent;
    private Stack<Integer>           indentLock         = new Stack<Integer>();
    private int                      printMargin        = 80;

    // [#1632] Cached values from Settings
    private RenderKeywordStyle       cachedRenderKeywordStyle;
    private RenderNameStyle          cachedRenderNameStyle;
    private boolean                  cachedRenderFormatted;

    DefaultRenderContext(Configuration configuration) {
        super(configuration);

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
        return new DefaultRenderContext(this).sql(part).render();
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

                // T-SQL databases use brackets
                case ASE:
                case SQLSERVER:
                case SYBASE:
                    sql("[").sql(StringUtils.replace(literal, "]", "]]")).sql("]");
                    break;

                // Most dialects implement the SQL standard, using double quotes
                case SQLITE:
                case CUBRID:
                case DB2:
                case DERBY:
                case FIREBIRD:
                case H2:
                case HSQLDB:
                case INGRES:
                case ORACLE:
                case POSTGRES:
                default:
                    sql('"').sql(StringUtils.replace(literal, "\"", "\"\"")).sql('"');
                    break;
            }
        }

        return this;
    }

    @Override
    public final RenderContext sql(QueryPart part) {
        if (part != null) {
            checkForceInline(part);
            QueryPartInternal internal = (QueryPartInternal) part;

            // If this is supposed to be a declaration section and the part
            // isn't able to declare anything, then disable declaration temporarily

            // We're declaring fields, but "part" does not declare fields
            if (declareFields() && !internal.declaresFields()) {
                declareFields(false);
                internal.toSQL(this);
                declareFields(true);
            }

            // We're declaring tables, but "part" does not declare tables
            else if (declareTables() && !internal.declaresTables()) {
                declareTables(false);
                internal.toSQL(this);
                declareTables(true);
            }

            // We're not declaring, or "part" can declare
            else {
                internal.toSQL(this);
            }
        }

        return this;
    }

    private final void checkForceInline(QueryPart part) throws ForceInlineSignal {
        if (paramType == INLINED)
            return;

        if (part instanceof Param) {
            if (((Param<?>) part).isInline())
                return;

            switch (configuration().dialect().family()) {
                case ASE:
                    checkForceInline(2000);
                    return;

                case INGRES:
                    checkForceInline(1024);
                    return;

                case SQLITE:
                    checkForceInline(999);
                    return;

                case SQLSERVER:
                    checkForceInline(2100);
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
