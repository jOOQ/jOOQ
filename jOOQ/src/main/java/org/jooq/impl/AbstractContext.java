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

import static java.util.Arrays.asList;
// ...
// ...
// ...
// ...
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.impl.Tools.EMPTY_CLAUSE;
import static org.jooq.impl.Tools.EMPTY_QUERYPART;
import static org.jooq.impl.Tools.DataKey.DATA_OMIT_CLAUSE_EVENT_EMISSION;

import java.sql.PreparedStatement;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.RenderContext.CastMode;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.VisitContext;
import org.jooq.VisitListener;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.ParamType;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.conf.StatementType;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
abstract class AbstractContext<C extends Context<C>> extends AbstractScope implements Context<C> {

    final PreparedStatement           stmt;

    boolean                           declareFields;
    boolean                           declareTables;
    boolean                           declareAliases;
    boolean                           declareWindows;
    boolean                           declareCTE;
    boolean                           subquery;
    int                               stringLiteral;
    String                            stringLiteralEscapedApos = "'";
    int                               index;

    // [#2665] VisitListener API
    final VisitListener[]             visitListeners;
    private final Deque<Clause>       visitClauses;
    private final DefaultVisitContext visitContext;
    private final Deque<QueryPart>    visitParts;

    // [#2694] Unified RenderContext and BindContext traversal
    final ParamType                   forcedParamType;
    ParamType                         paramType                = ParamType.INDEXED;
    boolean                           qualifySchema            = true;
    boolean                           qualifyCatalog           = true;
    CastMode                          castMode                 = CastMode.DEFAULT;

    AbstractContext(Configuration configuration, PreparedStatement stmt) {
        super(configuration);
        this.stmt = stmt;

        VisitListenerProvider[] providers = configuration.visitListenerProviders();
        boolean userInternalVisitListener =
            false





            ;

        this.visitListeners = new VisitListener[providers.length + (userInternalVisitListener ? 1 : 0)];

        for (int i = 0; i < providers.length; i++)
            this.visitListeners[i] = providers[i].provide();






        if (this.visitListeners.length > 0) {
            this.visitContext = new DefaultVisitContext();
            this.visitParts = new ArrayDeque<QueryPart>();
            this.visitClauses = new ArrayDeque<Clause>();
        }
        else {
            this.visitContext = null;
            this.visitParts = null;
            this.visitClauses = null;
        }

        forcedParamType = SettingsTools.getStatementType(settings()) == StatementType.STATIC_STATEMENT
            ? ParamType.INLINED
            : null;
    }

    // ------------------------------------------------------------------------
    // VisitListener API
    // ------------------------------------------------------------------------

    @Override
    public final C visit(QueryPart part) {
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
                visit0(original);
            else
                visit0(replacement);

            end(replacement);

            // Issue end clause events
            // -----------------------------------------------------------------
            if (clauses != null)
                for (int i = clauses.length - 1; i >= 0; i--)
                    end(clauses[i]);
        }

        return (C) this;
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
        if (part instanceof QueryPartInternal && data(DATA_OMIT_CLAUSE_EVENT_EMISSION) == null)
            return ((QueryPartInternal) part).clauses(this);

        return null;
    }

    @Override
    public final C start(Clause clause) {
        if (clause != null && visitClauses != null) {
            visitClauses.addLast(clause);

            for (VisitListener listener : visitListeners)
                listener.clauseStart(visitContext);
        }

        return (C) this;
    }

    @Override
    public final C end(Clause clause) {
        if (clause != null && visitClauses != null) {
            for (VisitListener listener : visitListeners)
                listener.clauseEnd(visitContext);

            if (visitClauses.removeLast() != clause)
                throw new IllegalStateException("Mismatch between visited clauses!");
        }

        return (C) this;
    }

    private final QueryPart start(QueryPart part) {
        if (visitParts != null) {
            visitParts.addLast(part);

            for (VisitListener listener : visitListeners)
                listener.visitStart(visitContext);

            return visitParts.peekLast();
        }
        else {
            return part;
        }
    }

    private final void end(QueryPart part) {
        if (visitParts != null) {
            for (VisitListener listener : visitListeners)
                listener.visitEnd(visitContext);

            if (visitParts.removeLast() != part)
                throw new RuntimeException("Mismatch between visited query parts");
        }
    }

    /**
     * A {@link VisitContext} is always in the scope of the current
     * {@link RenderContext}.
     */
    private class DefaultVisitContext implements VisitContext {

        @Override
        public final Map<Object, Object> data() {
            return AbstractContext.this.data();
        }

        @Override
        public final Object data(Object key) {
            return AbstractContext.this.data(key);
        }

        @Override
        public final Object data(Object key, Object value) {
            return AbstractContext.this.data(key, value);
        }

        @Override
        public final Configuration configuration() {
            return AbstractContext.this.configuration();
        }

        @Override
        public final Settings settings() {
            return Tools.settings(configuration());
        }

        @Override
        public final SQLDialect dialect() {
            return Tools.configuration(configuration()).dialect();
        }

        @Override
        public final SQLDialect family() {
            return dialect().family();
        }

        @Override
        public final Clause clause() {
            return visitClauses.peekLast();
        }

        @Override
        public final Clause[] clauses() {
            return visitClauses.toArray(EMPTY_CLAUSE);
        }

        @Override
        public final int clausesLength() {
            return visitClauses.size();
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
            return visitParts.toArray(EMPTY_QUERYPART);
        }

        @Override
        public final int queryPartsLength() {
            return visitParts.size();
        }

        @Override
        public final Context<?> context() {
            return AbstractContext.this;
        }

        @Override
        public final RenderContext renderContext() {
            return context() instanceof RenderContext ? (RenderContext) context() : null;
        }

        @Override
        public final BindContext bindContext() {
            return context() instanceof BindContext ? (BindContext) context() : null;
        }
    }

    // ------------------------------------------------------------------------
    // XXX Context API
    // ------------------------------------------------------------------------

    private final C visit0(QueryPart part) {
        if (part != null) {
            QueryPartInternal internal = (QueryPartInternal) part;

            // If this is supposed to be a declaration section and the part isn't
            // able to declare anything, then disable declaration temporarily

            // We're declaring fields, but "part" does not declare fields
            if (declareFields() && !internal.declaresFields()) {
                boolean aliases = declareAliases();
                declareFields(false);
                visit0(internal);
                declareFields(true);
                declareAliases(aliases);
            }

            // We're declaring tables, but "part" does not declare tables
            else if (declareTables() && !internal.declaresTables()) {
                boolean aliases = declareAliases();
                declareTables(false);
                visit0(internal);
                declareTables(true);
                declareAliases(aliases);
            }

            // We're declaring windows, but "part" does not declare windows
            else if (declareWindows() && !internal.declaresWindows()) {
                declareWindows(false);
                visit0(internal);
                declareWindows(true);
            }

            // We're declaring cte, but "part" does not declare cte
            else if (declareCTE() && !internal.declaresCTE()) {
                declareCTE(false);
                visit0(internal);
                declareCTE(true);
            }

            else if (castMode() != CastMode.DEFAULT && !internal.generatesCast()) {
                CastMode previous = castMode();

                castMode(CastMode.DEFAULT);
                visit0(internal);
                castMode(previous);
            }

            // We're not declaring, or "part" can declare
            else {
                visit0(internal);
            }
        }

        return (C) this;
    }

    protected abstract void visit0(QueryPartInternal internal);

    @Override
    public final boolean declareFields() {
        return declareFields;
    }

    @Override
    public final C declareFields(boolean d) {
        this.declareFields = d;
        declareAliases(d);
        return (C) this;
    }

    @Override
    public final boolean declareTables() {
        return declareTables;
    }

    @Override
    public final C declareTables(boolean d) {
        this.declareTables = d;
        declareAliases(d);
        return (C) this;
    }

    @Override
    public final boolean declareAliases() {
        return declareAliases;
    }

    @Override
    public final C declareAliases(boolean d) {
        this.declareAliases = d;
        return (C) this;
    }

    @Override
    public final boolean declareWindows() {
        return declareWindows;
    }

    @Override
    public final C declareWindows(boolean d) {
        this.declareWindows = d;
        return (C) this;
    }

    @Override
    public final boolean declareCTE() {
        return declareCTE;
    }

    @Override
    public final C declareCTE(boolean d) {
        this.declareCTE = d;
        return (C) this;
    }

    @Override
    public final boolean subquery() {
        return subquery;
    }

    @Override
    public final C subquery(boolean s) {
        this.subquery = s;
        return (C) this;
    }

    @Override
    public final boolean stringLiteral() {
        return stringLiteral > 0;
    }

    @Override
    public final C stringLiteral(boolean s) {
        if (s) {
            stringLiteral++;
            stringLiteralEscapedApos = stringLiteralEscapedApos + stringLiteralEscapedApos;
        }
        else {
            stringLiteral--;
            stringLiteralEscapedApos = stringLiteralEscapedApos.substring(0, stringLiteralEscapedApos.length() / 2);
        }

        return (C) this;
    }

    @Override
    public final int nextIndex() {
        return ++index;
    }

    @Override
    public final int peekIndex() {
        return index + 1;
    }

    // ------------------------------------------------------------------------
    // XXX RenderContext API
    // ------------------------------------------------------------------------

    @Override
    public final ParamType paramType() {
        return forcedParamType != null ? forcedParamType : paramType;
    }

    @Override
    public final C paramType(ParamType p) {
        paramType = (p == null ? INDEXED : p);
        return (C) this;
    }

    @Override
    public final boolean qualify() {
        return qualifySchema();
    }

    @Override
    public final C qualify(boolean q) {
        return qualifySchema(q);
    }

    @Override
    public final boolean qualifySchema() {
        return qualifySchema;
    }

    @Override
    public final C qualifySchema(boolean q) {
        this.qualifySchema = q;
        return (C) this;
    }

    @Override
    public final boolean qualifyCatalog() {
        return qualifyCatalog;
    }

    @Override
    public final C qualifyCatalog(boolean q) {
        this.qualifyCatalog = q;
        return (C) this;
    }

    @Override
    public final CastMode castMode() {
        return castMode;
    }

    @Override
    public final C castMode(CastMode mode) {
        this.castMode = mode;
        return (C) this;
    }

    @Override
    @Deprecated
    public final Boolean cast() {
        switch (castMode) {
            case ALWAYS:
                return true;
            case NEVER:
                return false;
        }

        return null;
    }

    @Override
    @Deprecated
    public final C castModeSome(SQLDialect... dialects) {
        return (C) this;
    }

    // ------------------------------------------------------------------------
    // XXX BindContext API
    // ------------------------------------------------------------------------

    @Override
    public final PreparedStatement statement() {
        return stmt;
    }

    // ------------------------------------------------------------------------
    // XXX Object API
    // ------------------------------------------------------------------------

    void toString(StringBuilder sb) {
        sb.append(  "bind index   [");
        sb.append(index);
        sb.append("]");
        sb.append("\ndeclaring    [");

        if (declareFields) {
            sb.append("fields");

            if (declareAliases)
                sb.append(" and aliases");
        }
        else if (declareTables) {
            sb.append("tables");

            if (declareAliases)
                sb.append(" and aliases");
        }
        else if (declareWindows) {
            sb.append("windows");
        }
        else if (declareCTE) {
            sb.append("cte");
        }
        else {
            sb.append("-");
        }

        sb.append("]\nsubquery     [");
        sb.append(subquery);
        sb.append("]");
    }
}
