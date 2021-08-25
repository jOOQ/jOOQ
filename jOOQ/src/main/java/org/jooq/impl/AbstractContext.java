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
import static org.jooq.JoinType.JOIN;
import static org.jooq.JoinType.LEFT_OUTER_JOIN;
// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.conf.InvocationOrder.REVERSE;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.impl.Tools.EMPTY_CLAUSE;
import static org.jooq.impl.Tools.EMPTY_QUERYPART;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_NESTED_SET_OPERATIONS;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_OMIT_CLAUSE_EVENT_EMISSION;

import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DSLContext;
import org.jooq.ForeignKey;
import org.jooq.JoinType;
import org.jooq.LanguageContext;
// ...
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.RenderContext;
import org.jooq.RenderContext.CastMode;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.VisitContext;
import org.jooq.VisitListener;
import org.jooq.VisitListenerProvider;
import org.jooq.conf.ParamCastMode;
import org.jooq.conf.ParamType;
import org.jooq.conf.RenderImplicitJoinType;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.conf.StatementType;
import org.jooq.tools.StringUtils;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
abstract class AbstractContext<C extends Context<C>> extends AbstractScope implements Context<C> {






    final PreparedStatement                        stmt;

    boolean                                        declareFields;
    boolean                                        declareTables;
    boolean                                        declareAliases;
    boolean                                        declareWindows;
    boolean                                        declareCTE;




    int                                            subquery;
    BitSet                                         subqueryScopedNestedSetOperations;
    int                                            stringLiteral;
    String                                         stringLiteralEscapedApos    = "'";
    int                                            index;
    int                                            scopeMarking;
    final ScopeStack<QueryPart, ScopeStackElement> scopeStack;
    int                                            skipUpdateCounts;

    // [#2665] VisitListener API
    private final VisitListener[]                  visitListenersStart;
    private final VisitListener[]                  visitListenersEnd;
    private final Deque<Clause>                    visitClauses;
    private final DefaultVisitContext              visitContext;
    private final Deque<QueryPart>                 visitParts;

    // [#2694] Unified RenderContext and BindContext traversal
    final ParamType                                forcedParamType;
    final boolean                                  castModeOverride;
    CastMode                                       castMode;
    LanguageContext                                languageContext;
    ParamType                                      paramType                   = ParamType.INDEXED;
    boolean                                        quote                       = true;
    boolean                                        qualifySchema               = true;
    boolean                                        qualifyCatalog              = true;

    // [#11711] Enforcing scientific notation
    private transient DecimalFormat                doubleFormat;
    private transient DecimalFormat                floatFormat;

    AbstractContext(Configuration configuration, PreparedStatement stmt) {
        super(configuration);
        this.stmt = stmt;

        VisitListenerProvider[] providers = configuration.visitListenerProviders();

        // [#2080] [#3935] Currently, the InternalVisitListener is not used everywhere
        boolean useInternalVisitListener =
            false



            ;

        // [#6758] Avoid this allocation if unneeded
        VisitListener[] visitListeners = providers.length > 0 || useInternalVisitListener
            ? new VisitListener[providers.length + (useInternalVisitListener ? 1 : 0)]
            : null;

        if (visitListeners != null) {
            for (int i = 0; i < providers.length; i++)
                visitListeners[i] = providers[i].provide();






            this.visitContext = new DefaultVisitContext();
            this.visitParts = new ArrayDeque<>();
            this.visitClauses = new ArrayDeque<>();

            this.visitListenersStart = configuration.settings().getVisitListenerStartInvocationOrder() != REVERSE
                ? visitListeners
                : Tools.reverse(visitListeners.clone());
            this.visitListenersEnd = configuration.settings().getVisitListenerEndInvocationOrder() != REVERSE
                ? visitListeners
                : Tools.reverse(visitListeners.clone());
        }
        else {
            this.visitContext = null;
            this.visitParts = null;
            this.visitClauses = null;
            this.visitListenersStart = null;
            this.visitListenersEnd = null;
        }

        this.forcedParamType = SettingsTools.getStatementType(settings()) == StatementType.STATIC_STATEMENT
            ? ParamType.INLINED
            : SettingsTools.getParamType(settings()) == ParamType.FORCE_INDEXED
            ? ParamType.INDEXED
            : null;

        ParamCastMode m = settings().getParamCastMode();
        this.castModeOverride =
              m != ParamCastMode.DEFAULT && m != null;
        this.castMode =
              m == ParamCastMode.ALWAYS
            ? CastMode.ALWAYS
            : m == ParamCastMode.NEVER
            ? CastMode.NEVER
            : CastMode.DEFAULT;
        this.languageContext = LanguageContext.QUERY;
        this.scopeStack = new ScopeStack<QueryPart, ScopeStackElement>(ScopeStackElement::new);
    }

    // ------------------------------------------------------------------------
    // VisitListener API
    // ------------------------------------------------------------------------

    @Override
    public final C visit(QueryPart part) {
        if (part != null) {

            // Issue start clause events
            // -----------------------------------------------------------------
            Clause[] clauses = Tools.isNotEmpty(visitListenersStart) ? clause(part) : null;
            if (clauses != null)
                for (int i = 0; i < clauses.length; i++)
                    start(clauses[i]);

            // Perform the actual visiting, or recurse into the replacement
            // -----------------------------------------------------------------
            QueryPart replacement = start(part);

            if (replacement != null) {
                QueryPartInternal internal = (QueryPartInternal) scopeMapping(replacement);

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












                else if (!castModeOverride && castMode() != CastMode.DEFAULT && !internal.generatesCast()) {
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

            end(replacement);

            // Issue end clause events
            // -----------------------------------------------------------------
            if (clauses != null)
                for (int i = clauses.length - 1; i >= 0; i--)
                    end(clauses[i]);
        }

        return (C) this;
    }

    protected abstract void visit0(QueryPartInternal internal);

    private final C toggle(boolean b, BooleanSupplier get, BooleanConsumer set, Consumer<? super C> consumer) {
        boolean previous = get.getAsBoolean();

        try {
            set.accept(b);
            consumer.accept((C) this);
        }
        finally {
            set.accept(previous);
        }

        return (C) this;
    }

    private final <T> C toggle(T t, Supplier<T> get, Consumer<T> set, Consumer<? super C> consumer) {
        T previous = get.get();

        try {
            set.accept(t);
            consumer.accept((C) this);
        }
        finally {
            set.accept(previous);
        }

        return (C) this;
    }

    @Override
    public final C data(Object key, Object value, Consumer<? super C> consumer) {
        return toggle(
            value,
            () -> data(key),
            v -> {
                if (v == null)
                    data().remove(key);
                else
                    data(key, v);
            },
            consumer
        );
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
        if (part instanceof QueryPartInternal && !TRUE.equals(data(DATA_OMIT_CLAUSE_EVENT_EMISSION)))
            return ((QueryPartInternal) part).clauses(this);

        return null;
    }

    @Override
    public final C start(Clause clause) {
        if (clause != null && visitClauses != null) {
            visitClauses.addLast(clause);

            for (VisitListener listener : visitListenersStart)
                listener.clauseStart(visitContext);
        }

        return (C) this;
    }

    @Override
    public final C end(Clause clause) {
        if (clause != null && visitClauses != null) {
            for (VisitListener listener : visitListenersEnd)
                listener.clauseEnd(visitContext);

            if (visitClauses.removeLast() != clause)
                throw new IllegalStateException("Mismatch between visited clauses!");
        }

        return (C) this;
    }

    private final QueryPart start(QueryPart part) {
        if (visitParts != null) {
            visitParts.addLast(part);

            for (VisitListener listener : visitListenersStart)
                listener.visitStart(visitContext);

            return visitParts.peekLast();
        }
        else {
            return part;
        }
    }

    private final void end(QueryPart part) {
        if (visitParts != null) {
            for (VisitListener listener : visitListenersEnd)
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
        public final DSLContext dsl() {
            return AbstractContext.this.dsl();
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
    public C declareFields(boolean f, Consumer<? super C> consumer) {
        return toggle(f, this::declareFields, this::declareFields, consumer);
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
    public C declareTables(boolean f, Consumer<? super C> consumer) {
        return toggle(f, this::declareTables, this::declareTables, consumer);
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
    public C declareAliases(boolean f, Consumer<? super C> consumer) {
        return toggle(f, this::declareAliases, this::declareAliases, consumer);
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
    public C declareWindows(boolean f, Consumer<? super C> consumer) {
        return toggle(f, this::declareWindows, this::declareWindows, consumer);
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
    public C declareCTE(boolean f, Consumer<? super C> consumer) {
        return toggle(f, this::declareCTE, this::declareCTE, consumer);
    }

    @Override
    public final int scopeLevel() {
        return scopeStack.scopeLevel();
    }

    @Override
    public final int subqueryLevel() {
        return subquery;
    }

    @Override
    public final boolean subquery() {
        return subquery > 0;
    }

    final C subquery0(boolean s, boolean setOperation) {
        if (s) {
            subquery++;

            // [#7222] If present the nested set operations flag needs to be reset whenever we're
            //         entering a subquery (and restored when leaving it).
            // [#2791] This works differently from the scope marking mechanism, which wraps a
            // [#7152] naming scope for aliases and other object names.
            // [#9961] Do this only for scalar subqueries and derived tables, not for set operation subqueries
            if (!setOperation && TRUE.equals(data(DATA_NESTED_SET_OPERATIONS))) {
                data().remove(DATA_NESTED_SET_OPERATIONS);

                if (subqueryScopedNestedSetOperations == null)
                    subqueryScopedNestedSetOperations = new BitSet();

                subqueryScopedNestedSetOperations.set(subquery);
            }

            scopeStart();
        }
        else {
            scopeEnd();

            if (!setOperation)
                if (subqueryScopedNestedSetOperations != null &&
                    subqueryScopedNestedSetOperations.get(subquery))
                    data(DATA_NESTED_SET_OPERATIONS, true);
                else
                    data().remove(DATA_NESTED_SET_OPERATIONS);

            subquery--;
        }

        return (C) this;
    }

    @Override
    public final C subquery(boolean s) {
        return subquery0(s, false);
    }

    @Override
    public final C scopeStart() {
        scopeStack.scopeStart();
        scopeStart0();

        return (C) this;
    }

    @Override
    public final C scopeRegister(QueryPart part) {
        return scopeRegister(part, false);
    }

    @Override
    public final C scopeRegister(QueryPart part, boolean forceNew) {
        return scopeRegister(part, forceNew, null);
    }

    @Override
    public /* non-final */ C scopeRegister(QueryPart part, boolean forceNew, QueryPart mapped) {
        return (C) this;
    }

    @Override
    public /* non-final */ QueryPart scopeMapping(QueryPart part) {
        return part;
    }

    @Override
    public final C scopeRegisterAndMark(QueryPart part, boolean forceNew) {
        return scopeRegister(part, forceNew).scopeMarkStart(part).scopeMarkEnd(part);
    }

    @Override
    public final C scopeMarkStart(QueryPart part) {
        if (scopeStack.inScope() && scopeMarking++ == 0)
            scopeMarkStart0(part);

        return (C) this;
    }

    @Override
    public final C scopeMarkEnd(QueryPart part) {
        if (scopeStack.inScope() && --scopeMarking == 0)
            scopeMarkEnd0(part);

        return (C) this;
    }

    @Override
    public final C scopeEnd() {
        scopeEnd0();
        scopeStack.scopeEnd();

        return (C) this;
    }

    void scopeStart0() {}
    void scopeMarkStart0(@SuppressWarnings("unused") QueryPart part) {}
    void scopeMarkEnd0(@SuppressWarnings("unused") QueryPart part) {}
    void scopeEnd0() {}

    String applyNameCase(String literal) {
        return literal;
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

    @Override
    public final int skipUpdateCounts() {
        return skipUpdateCounts;
    }

    @Override
    public final C skipUpdateCount() {
        return skipUpdateCounts(1);
    }

    @Override
    public final C skipUpdateCounts(int skip) {
        skipUpdateCounts += skip;
        return (C) this;
    }

    // ------------------------------------------------------------------------
    // XXX RenderContext API
    // ------------------------------------------------------------------------

    @Override
    public final DecimalFormat floatFormat() {
        if (floatFormat == null)
            floatFormat = new DecimalFormat("0.#######E0", DecimalFormatSymbols.getInstance(Locale.US));

        return floatFormat;
    }

    @Override
    public final DecimalFormat doubleFormat() {
        if (doubleFormat == null)
            doubleFormat = new DecimalFormat("0.################E0", DecimalFormatSymbols.getInstance(Locale.US));

        return doubleFormat;
    }

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
    public final C visit(QueryPart part, ParamType p) {
        return paramType(p, c -> c.visit(part));
    }

    @Override
    public final C paramTypeIf(ParamType p, boolean condition) {
        if (condition)
            paramType(p);

        return (C) this;
    }

    @Override
    public final C paramType(ParamType p, Consumer<? super C> runnable) {
        return toggle(p, this::paramType, this::paramType, runnable);
    }

    @Override
    public final C paramTypeIf(ParamType p, boolean condition, Consumer<? super C> runnable) {
        if (condition)
            paramType(p, runnable);
        else
            runnable.accept((C) this);

        return (C) this;
    }

    @Override
    public final boolean quote() {
        return quote;
    }

    @Override
    public final C quote(boolean q) {
        this.quote = q;
        return (C) this;
    }

    @Override
    public final C quote(boolean q, Consumer<? super C> consumer) {
        return toggle(q, this::quote, this::quote, consumer);
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
    public final C qualify(boolean q, Consumer<? super C> consumer) {
        return toggle(q, this::qualify, this::qualify, consumer);
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
    public final C qualifySchema(boolean q, Consumer<? super C> consumer) {
        return toggle(q, this::qualifySchema, this::qualifySchema, consumer);
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
    public final C qualifyCatalog(boolean q, Consumer<? super C> consumer) {
        return toggle(q, this::qualifyCatalog, this::qualifyCatalog, consumer);
    }

    @Override
    public final LanguageContext languageContext() {
        return languageContext;
    }

    @Override
    public final C languageContext(LanguageContext context) {
        this.languageContext = context;
        return (C) this;
    }

    @Override
    public final C languageContext(LanguageContext context, Consumer<? super C> consumer) {
        return toggle(context, this::languageContext, this::languageContext, consumer);
    }

    @Override
    public final C languageContextIf(LanguageContext context, boolean condition) {
        if (condition)
            languageContext(context);

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
    public final C castMode(CastMode mode, Consumer<? super C> consumer) {
        return toggle(mode, this::castMode, this::castMode, consumer);
    }

    @Override
    public final C castModeIf(CastMode mode, boolean condition) {
        if (condition)
            castMode(mode);

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
        sb.append("\ncast mode    [");
        sb.append(castMode);
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

    static class JoinNode {
        final Configuration                   configuration;
        final Table<?>                        table;
        final Map<ForeignKey<?, ?>, JoinNode> children;

        JoinNode(Configuration configuration, Table<?> table) {
            this.configuration = configuration;
            this.table = table;
            this.children = new LinkedHashMap<>();
        }

        public Table<?> joinTree() {
            Table<?> result = table;

            for (Entry<ForeignKey<?, ?>, JoinNode> e : children.entrySet()) {
                JoinType type;

                switch (StringUtils.defaultIfNull(Tools.settings(configuration).getRenderImplicitJoinType(),
                    RenderImplicitJoinType.DEFAULT)) {
                    case INNER_JOIN:
                        type = JOIN;
                        break;
                    case LEFT_JOIN:
                        type = LEFT_OUTER_JOIN;
                        break;
                    case DEFAULT:
                    default:
                        type = e.getKey().nullable() ? LEFT_OUTER_JOIN : JOIN;
                        break;
                }

                result = result.join(e.getValue().joinTree(), type).onKey(e.getKey());
            }

            return result;
        }

        @Override
        public String toString() {
            return joinTree().toString();
        }
    }

    static class ScopeStackElement {
        final int       scopeLevel;
        final QueryPart part;
        QueryPart       mapped;
        int[]           positions;
        int             bindIndex;
        int             indent;
        JoinNode        joinNode;

        ScopeStackElement(QueryPart part, int scopeLevel) {
            this.part = part;
            this.mapped = part;
            this.scopeLevel = scopeLevel;
        }

        @Override
        public String toString() {
            return (positions != null ? Arrays.toString(positions) + ": " : "")
                 + part
                 + (mapped != null ? " (" + mapped + ")" : "");
        }
    }
}
