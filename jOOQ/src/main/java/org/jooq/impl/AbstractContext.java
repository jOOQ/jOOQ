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
package org.jooq.impl;

import static java.lang.Boolean.TRUE;
import static org.jooq.JoinType.JOIN;
import static org.jooq.JoinType.LEFT_OUTER_JOIN;
import static org.jooq.conf.InvocationOrder.REVERSE;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.JoinTable.onKey0;
import static org.jooq.impl.Tools.DATAKEY_RESET_IN_SUBQUERY_SCOPE;
import static org.jooq.impl.Tools.EMPTY_CLAUSE;
import static org.jooq.impl.Tools.EMPTY_QUERYPART;
import static org.jooq.impl.Tools.lazy;
import static org.jooq.impl.Tools.traverseJoins;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_NESTED_SET_OPERATIONS;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_OMIT_CLAUSE_EVENT_EMISSION;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_RENDER_IMPLICIT_JOIN;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_UNALIAS_ALIASED_EXPRESSIONS;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_UNQUALIFY_LOCAL_SCOPE;
import static org.jooq.impl.Tools.SimpleDataKey.DATA_OVERRIDE_ALIASES_IN_ORDER_BY;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.ConverterContext;
import org.jooq.DSLContext;
import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
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
import org.jooq.exception.DataAccessException;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.Tools.DataKey;


/**
 * @author Lukas Eder
 */
@SuppressWarnings("unchecked")
abstract class AbstractContext<C extends Context<C>> extends AbstractScope implements Context<C> {

    final ExecuteContext                           ctx;
    final PreparedStatement                        stmt;

    boolean                                        declareFields;
    boolean                                        declareTables;
    boolean                                        declareAliases;
    boolean                                        declareWindows;
    boolean                                        declareCTE;




    int                                            subquery;
    BitSet                                         subqueryScopedNestedSetOperations;
    boolean                                        predicandSubquery;
    boolean                                        derivedTableSubquery;
    boolean                                        setOperationSubquery;
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
    boolean                                        qualify                     = true;
    boolean                                        qualifySchema               = true;
    boolean                                        qualifyCatalog              = true;
    QueryPart                                      topLevel;
    QueryPart                                      topLevelForLanguageContext;

    // [#11711] Enforcing scientific notation
    private transient DecimalFormat                doubleFormat;
    private transient DecimalFormat                floatFormat;

    AbstractContext(Configuration configuration, ExecuteContext ctx, PreparedStatement stmt) {
        super(configuration);

        this.ctx = ctx;
        this.stmt = stmt;

        VisitListenerProvider[] providers = configuration.visitListenerProviders();

        // [#6758] Avoid this allocation if unneeded
        VisitListener[] visitListeners = providers.length > 0
            ? new VisitListener[providers.length]
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
        this.scopeStack = new ScopeStack<QueryPart, ScopeStackElement>((k, v) -> {
            if (k == DataKeyScopeStackPart.INSTANCE)
                return new DataKeyScopeStackElement(k, v);
            else if (k == ScopeDefinerScopeStackPart.INSTANCE)
                return new ScopeDefinerScopeStackElement(k, v);
            else
                return new DefaultScopeStackElement(k, v);
        });

        if (TRUE.equals(configuration.settings().isEmulateNestedRecordProjectionsUsingMultisetEmulation())) {
            data(DATA_MULTISET_CONTENT, true);

            if (ctx != null)
                ctx.data(DATA_MULTISET_CONTENT, true);
        }
    }

    // ------------------------------------------------------------------------
    // ExecuteScope API
    // ------------------------------------------------------------------------

    @Override
    public final ConverterContext converterContext() {
        return ctx != null ? ctx.converterContext() : Tools.converterContext(configuration());
    }

    @Override
    public final ExecuteContext executeContext() {
        return ctx;
    }

    // ------------------------------------------------------------------------
    // VisitListener API
    // ------------------------------------------------------------------------

    @Override
    public final C visit(Condition part) {

        // [#16310] The MULTISET content flag needs to be reset for any non
        //          multiset projection context. For example, in any Condition.
        //          While Conditions can be projected, they're projecting a BOOLEAN,
        //          not nested records or collections
        if (TRUE.equals(data(DATA_MULTISET_CONTENT))) {
            try {
                data(DATA_MULTISET_CONTENT, false);
                visit((QueryPart) part);
            }
            finally {
                data(DATA_MULTISET_CONTENT, true);
            }
        }
        else
            visit((QueryPart) part);

        return (C) this;
    }

    @Override
    public final C visit(Field<?> part) {
        return part instanceof Condition c ? visit((QueryPart) DSL.field(c)) : visit((QueryPart) part);
    }

    @Override
    public final C visit(QueryPart part) {
        if (part != null) {
            if (topLevel == null) {
                topLevel = topLevelForLanguageContext = part;

                // [#14155] Apply transformation only if it hasn't been applied
                //          already, from some ExecuteContext.
                if (executeContext() == null
                        && TRUE.equals(settings().isTransformPatterns())
                        && configuration().requireCommercial(() -> "SQL transformations are a commercial only feature. Please consider upgrading to the jOOQ Professional Edition or jOOQ Enterprise Edition.")) {



                }
            }

            // [#16928] Apply type specific replacements that can't be implemented in individual types,
            //          and for which an internal VisitListener is overkill
            part = typeSpecificReplacements(part);

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

    static final record AliasOverride(List<Field<?>> originalFields, List<Field<?>> aliasedFields) {}

    private final QueryPart typeSpecificReplacements(QueryPart part) {
        if (!declareFields() && part instanceof Field<?> f) {

            // [#2080] Override the actual alias in case a synthetic alias is generated
            // in the SELECT clause
            AliasOverride override = (AliasOverride) data(DATA_OVERRIDE_ALIASES_IN_ORDER_BY);

            // Don't combine the effects of DATA_OVERRIDE_ALIASES_IN_ORDER_BY with DATA_UNALIAS_ALIASES_IN_ORDER_BY
            if (override != null && !TRUE.equals(data(DATA_UNALIAS_ALIASED_EXPRESSIONS))) {

                // [#16946] Ignore qualification of field if unambiguous
                int i = new FieldsImpl<>(override.originalFields()).indexOf(f);

                if (i >= 0 && i < override.aliasedFields().size())
                    return field(name(override.aliasedFields().get(i).getName()), f.getDataType());
            }
        }

        return part;
    }

    @Override
    public final C visitSubquery(QueryPart part) {
        Tools.visitSubquery(this, part);
        return (C) this;
    }

    protected abstract void visit0(QueryPartInternal internal);

    @Override
    public final C data(Object key, Object value, Consumer<? super C> consumer) {
        Object previous = data(key);

        try {
            if (value == null)
                data().remove(key);
            else
                data(key, value);

            consumer.accept((C) this);
        }
        finally {
            if (previous == null)
                data().remove(key);
            else
                data(key, previous);
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
     * <pre><code>SELECT * FROM [A CROSS JOIN B]</code></pre>
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
        public final Instant creationTime() {
            return AbstractContext.this.creationTime();
        }

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
            return AbstractContext.this.settings();
        }

        @Override
        public final SQLDialect dialect() {
            return AbstractContext.this.dialect();
        }

        @Override
        public final SQLDialect family() {
            return AbstractContext.this.family();
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
            return context() instanceof RenderContext c ? c : null;
        }

        @Override
        public final BindContext bindContext() {
            return context() instanceof BindContext c ? c : null;
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
        boolean previous = declareFields();

        try {
            declareFields(f);
            consumer.accept((C) this);
        }
        finally {
            declareFields(previous);
        }

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
    public C declareTables(boolean f, Consumer<? super C> consumer) {
        boolean previous = declareTables();

        try {
        	declareTables(f);
            consumer.accept((C) this);
        }
        finally {
            declareTables(previous);
        }

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
    public C declareAliases(boolean f, Consumer<? super C> consumer) {
        boolean previous = declareAliases();

        try {
        	declareAliases(f);
            consumer.accept((C) this);
        }
        finally {
            declareAliases(previous);
        }

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
    public C declareWindows(boolean f, Consumer<? super C> consumer) {
        boolean previous = declareWindows();

        try {
            declareWindows(f);
            consumer.accept((C) this);
        }
        finally {
            declareWindows(previous);
        }

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
    public C declareCTE(boolean f, Consumer<? super C> consumer) {
        boolean previous = declareCTE();

        try {
            declareCTE(f);
            consumer.accept((C) this);
        }
        finally {
            declareCTE(previous);
        }

        return (C) this;
    }

    @Override
    public final int scopeLevel() {
        return scopeStack.scopeLevel();
    }

    @Override
    public final QueryPart topLevel() {
        return topLevel;
    }

    @Override
    public final C topLevel(QueryPart t) {
        topLevel = t;
        return (C) this;
    }

    @Override
    public final QueryPart topLevelForLanguageContext() {
        return topLevelForLanguageContext;
    }

    @Override
    public final C topLevelForLanguageContext(QueryPart t) {
        topLevelForLanguageContext = t;
        return (C) this;
    }

    @Override
    public final int subqueryLevel() {
        return subquery;
    }

    @Override
    public final boolean predicandSubquery() {
        return predicandSubquery;
    }

    @Override
    public final C predicandSubquery(boolean s) {
        predicandSubquery = s;
        return (C) this;
    }

    @Override
    public final boolean derivedTableSubquery() {
        return derivedTableSubquery;
    }

    @Override
    public final C derivedTableSubquery(boolean s) {
        derivedTableSubquery = s;
        return (C) this;
    }

    @Override
    public final boolean setOperationSubquery() {
        return setOperationSubquery;
    }

    @Override
    public final C setOperationSubquery(boolean s) {
        setOperationSubquery = s;
        return (C) this;
    }

    @Override
    public final boolean subquery() {
        return subquery > 0;
    }

    final C subquery0(boolean s, boolean setOperation, QueryPart part) {
        setOperationSubquery(setOperation);

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

            scopeStart(part);
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
        return subquery(s, null);
    }

    @Override
    public final C subquery(boolean s, QueryPart part) {
        return subquery0(s, false, part);
    }

    @Override
    public final C scopeStart() {
        return scopeStart(null);
    }

    @Override
    public final C scopeStart(QueryPart part) {
        scopeStack.scopeStart();
        if (part != null)
            ((ScopeDefinerScopeStackElement) scopeStack.getOrCreate(ScopeDefinerScopeStackPart.INSTANCE)).scopeDefiner = part;
        scopeStart0();
        resetDataKeys((DataKeyScopeStackElement) scopeStack.getOrCreate(DataKeyScopeStackPart.INSTANCE));

        return (C) this;
    }

    @Override
    public final QueryPart scopePart() {
        ScopeDefinerScopeStackElement e = ((ScopeDefinerScopeStackElement) scopeStack.get(ScopeDefinerScopeStackPart.INSTANCE));
        return e != null ? e.scopeDefiner : null;
    }

    @Override
    public /* non-final */ C scopeHide(QueryPart part) {
        return (C) this;
    }

    @Override
    public /* non-final */ C scopeShow(QueryPart part) {
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
    public final <Q extends QueryPart> Iterable<Q> scopeParts(Class<? extends Q> type) {
        return (Iterable<Q>) scopeStack.keyIterable(k -> type.isInstance(k));
    }

    @Override
    public final <Q extends QueryPart> Iterable<Q> currentScopeParts(Class<? extends Q> type) {
        return (Iterable<Q>) scopeStack.keyIterableAtScopeLevel(k -> type.isInstance(k));
    }

    @Override
    public final boolean inScope(QueryPart part) {
        return scopeStack.get(part) != null;
    }

    @Override
    public final boolean inCurrentScope(QueryPart part) {
        return scopeStack.getCurrentScope(part) != null;
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
        restoreDataKeys((DataKeyScopeStackElement) scopeStack.getOrCreate(DataKeyScopeStackPart.INSTANCE));

        scopeEnd0();
        scopeStack.scopeEnd();

        return (C) this;
    }

    void scopeStart0() {}
    void scopeMarkStart0(@SuppressWarnings("unused") QueryPart part) {}
    void scopeMarkEnd0(@SuppressWarnings("unused") QueryPart part) {}
    void scopeEnd0() {}

    final void resetDataKeys(DataKeyScopeStackElement e) {
        for (int i = 0; i < DATAKEY_RESET_IN_SUBQUERY_SCOPE.length; i++) {
            DataKey key = DATAKEY_RESET_IN_SUBQUERY_SCOPE[i];

            if (subqueryLevel() >= key.resetThreshold() && data().containsKey(key))
                (e.restoreDataKeys = lazy(e.restoreDataKeys, i + 1)).set(i, data().put(key, key.resetValue()));
        }
    }

    final void restoreDataKeys(DataKeyScopeStackElement e) {
        for (int i = 0; i < DATAKEY_RESET_IN_SUBQUERY_SCOPE.length; i++) {
            DataKey key = DATAKEY_RESET_IN_SUBQUERY_SCOPE[i];

            if (subqueryLevel() >= key.resetThreshold() && e.restoreDataKeys != null && i < e.restoreDataKeys.size())
                data().put(key, e.restoreDataKeys.get(i));
        }
    }

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
        return skipUpdateCounts + (ctx != null ? ctx.skipUpdateCounts() : 0);
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
        ParamType previous = paramType();

        try {
            paramType(p);
            runnable.accept((C) this);
        }
        finally {
            paramType(previous);
        }

        return (C) this;
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
        boolean previous = quote();

        try {
            quote(q);
            consumer.accept((C) this);
        }
        finally {
            quote(previous);
        }

        return (C) this;
    }

    @Override
    public final boolean qualify() {
        return data(DATA_UNQUALIFY_LOCAL_SCOPE) == null ? qualify : false;
    }

    @Override
    public final C qualify(boolean q) {
        this.qualify = q;
        return (C) this;
    }

    @Override
    public final C qualify(boolean q, Consumer<? super C> consumer) {
        boolean previous = qualify();

        try {
            qualify(q);
            consumer.accept((C) this);
        }
        finally {
            qualify(previous);
        }

        return (C) this;
    }

    @Override
    public final boolean qualifySchema() {
        return qualify && qualifySchema;
    }

    @Override
    public final C qualifySchema(boolean q) {
        this.qualifySchema = q;
        return (C) this;
    }

    @Override
    public final C qualifySchema(boolean q, Consumer<? super C> consumer) {
        boolean previous = qualifySchema();

        try {
            qualifySchema(q);
            consumer.accept((C) this);
        }
        finally {
            qualifySchema(previous);
        }

        return (C) this;
    }

    @Override
    public final boolean qualifyCatalog() {
        return qualify && qualifyCatalog;
    }

    @Override
    public final C qualifyCatalog(boolean q) {
        this.qualifyCatalog = q;
        return (C) this;
    }

    @Override
    public final C qualifyCatalog(boolean q, Consumer<? super C> consumer) {
        boolean previous = qualifyCatalog();

        try {
            qualifyCatalog(q);
            consumer.accept((C) this);
        }
        finally {
            qualifyCatalog(previous);
        }

        return (C) this;
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
        LanguageContext previous = languageContext();

        try {
            languageContext(context);
            consumer.accept((C) this);
        }
        finally {
            languageContext(previous);
        }

        return (C) this;
    }

    @Override
    public final C languageContext(LanguageContext context, QueryPart newTopLevelForLanguageContext, Consumer<? super C> consumer) {
        return languageContext(context, c -> {
            QueryPart previous = topLevelForLanguageContext();

            try {
                topLevelForLanguageContext(newTopLevelForLanguageContext);
                consumer.accept((C) this);
            }
            finally {
                topLevelForLanguageContext(previous);
            }
        });
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
        CastMode previous = castMode();

        try {
            castMode(mode);
            consumer.accept((C) this);
        }
        finally {
            castMode(previous);
        }

        return (C) this;
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
        final Context<?>                             ctx;
        final Table<?>                               table;
        final Map<ForeignKey<?, ?>, JoinNode>        pathsToOne;
        final Map<InverseForeignKey<?, ?>, JoinNode> pathsToMany;
        int                                          references;

        JoinNode(Context<?> ctx, Table<?> table) {
            this.ctx = ctx;
            this.table = table;
            this.pathsToOne = new LinkedHashMap<>();
            this.pathsToMany = new LinkedHashMap<>();
        }

        static final JoinNode create(
            Context<?> ctx,
            JoinNode result,
            Table<?> root,
            List<TableImpl<?>> tables
        ) {
            if (!tables.isEmpty()) {
                if (result == null)
                    result = new JoinNode(ctx, root);

                JoinNode node = result;
                for (int i = tables.size() - 1; i >= 0; i--) {
                    TableImpl<?> t = tables.get(i);

                    if (t.childPath != null)
                        node = node.pathsToOne.computeIfAbsent(t.childPath, k -> new JoinNode(ctx, t));
                    else
                        node = node.pathsToMany.computeIfAbsent(t.parentPath, k -> new JoinNode(ctx, t));

                    if (i == 0)
                        node.references++;
                }
            }

            return result;
        }

        Table<?> joinTree() {
            return joinTree(null);
        }

        Table<?> joinTree(JoinType joinType) {
            Table<?> result = table;
















            if (ctx.data(DATA_RENDER_IMPLICIT_JOIN) != null && TableImpl.path(result) != null)
                result = Tools.unwrap(result).as(result);

            for (Entry<ForeignKey<?, ?>, JoinNode> e : pathsToOne.entrySet()) {
                Table<?> t = e.getValue().joinTree(joinType);

                // [#14992] Eliminate to-one -> to-many hops if there are no projection references
                if (skippable(e.getKey(), e.getValue()))

                    // [#14992] TODO: Currently, skippable JoinNodes have no outgoing to-one
                    //          relationships, but that might change in the future.
                    result = appendToManyPaths(result, e.getValue(), joinType);
                else
                    result = result
                        .join(t, joinType != null ? joinType : joinType(t, e.getKey().nullable() ? LEFT_OUTER_JOIN : JOIN))
                        .on(onKey0(e.getKey(), result, t));
            }

            return appendToManyPaths(result, this, joinType);
        }

        private static final Table<?> appendToManyPaths(Table<?> result, JoinNode node, JoinType joinType) {
            for (Entry<InverseForeignKey<?, ?>, JoinNode> e : node.pathsToMany.entrySet()) {
                Table<?> t = e.getValue().joinTree();

                result = result
                    .join(t, joinType != null ? joinType : node.joinToManyType(t))
                    .on(onKey0(e.getKey().getForeignKey(), t, result));
            }

            return result;
        }

        private final boolean skippable(ForeignKey<?, ?> fk, JoinNode node) {
            if (node.references == 0) {

                // [#14992] TODO: Do this for to-one paths as well, if that exists?
                if (!node.pathsToOne.isEmpty())
                    return false;

                for (Entry<InverseForeignKey<?, ?>, JoinNode> path : node.pathsToMany.entrySet()) {
                    if (!fk.getKeyFields().equals(path.getKey().getFields()))
                        return false;
                }

                return true;
            }

            return false;
        }

        private final JoinType joinType(Table<?> t, JoinType onDefault) {
            RenderImplicitJoinType type = defaultIfNull(ctx.settings().getRenderImplicitJoinType(), RenderImplicitJoinType.DEFAULT);

            switch (type) {

                // SCALAR_SUBQUERY is handled elsewhere
                case INNER_JOIN:
                    return JOIN;
                case LEFT_JOIN:
                    return LEFT_OUTER_JOIN;
                case THROW:

                    // [#15755] Throw exceptions only if the to-many join is done to a table
                    //          that isn't in any explicit scope
                    if (!allInScope(t))
                        throw new DataAccessException("Implicit to-one JOIN of " + ctx.dsl().renderContext().declareTables(true).render(table) + " isn't supported with Settings.renderImplicitJoinType = " + type + ". Either change Settings value, or use explicit path join, see https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/explicit-path-join/");
                    else
                        return LEFT_OUTER_JOIN;

                case DEFAULT:
                default:
                    return onDefault;
            }
        }

        private final JoinType joinToManyType(Table<?> t) {
            RenderImplicitJoinType type = defaultIfNull(ctx.settings().getRenderImplicitJoinToManyType(), RenderImplicitJoinType.DEFAULT);

            switch (type) {

                // SCALAR_SUBQUERY is handled elsewhere
                case INNER_JOIN:
                    return JOIN;
                case LEFT_JOIN:
                    return LEFT_OUTER_JOIN;
                case DEFAULT:
                case THROW:
                default:

                    // [#15755] Throw exceptions only if the to-many join is done to a table
                    //          that isn't in any explicit scope
                    if (!allInScope(t))
                        throw new DataAccessException("Implicit to-many JOIN of " + ctx.dsl().renderContext().declareTables(true).render(table) + " isn't supported with Settings.renderImplicitJoinToManyType = " + type + ". Either change Settings value, or use explicit path join, see https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/explicit-path-join/");
                    else
                        return LEFT_OUTER_JOIN;
            }
        }

        private final boolean allInScope(Table<?> t) {
            if (t instanceof TableImpl<?> ti)
                return ctx.inScope(t);
            else if (t instanceof JoinTable<?> j)
                return traverseJoins(j, true, b -> !b, (b, x) -> b && ctx.inScope(x));
            else
                return false;
        }

        final boolean hasJoinPaths() {
            return !pathsToOne.isEmpty() || !pathsToMany.isEmpty();
        }

        @Override
        public String toString() {
            return joinTree().toString();
        }
    }

    abstract static class ScopeStackElement {
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
    }

    static abstract class AbstractScopeStackPart extends AbstractQueryPart implements UEmpty {

        @Override
        public final void accept(Context<?> ctx) {}

        @Override
        public boolean equals(Object that) {
            return this == that;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName();
        }
    }

    static final class DataKeyScopeStackPart extends AbstractScopeStackPart {
        static final DataKeyScopeStackPart INSTANCE = new DataKeyScopeStackPart();
        private DataKeyScopeStackPart() {}
    }

    static final class ScopeDefinerScopeStackPart extends AbstractScopeStackPart {
        static final ScopeDefinerScopeStackPart INSTANCE = new ScopeDefinerScopeStackPart();
        private ScopeDefinerScopeStackPart() {}
    }

    static final class DataKeyScopeStackElement extends ScopeStackElement {
        List<Object> restoreDataKeys;

        DataKeyScopeStackElement(QueryPart part, int scopeLevel) {
            super(part, scopeLevel);
        }

        @Override
        public String toString() {
            return "RestoreDataKeys [" + restoreDataKeys + "]";
        }
    }

    static final class ScopeDefinerScopeStackElement extends ScopeStackElement {
        QueryPart scopeDefiner;

        ScopeDefinerScopeStackElement(QueryPart part, int scopeLevel) {
            super(part, scopeLevel);
        }

        @Override
        public String toString() {
            return "" + scopeDefiner;
        }
    }

    static final class DefaultScopeStackElement extends ScopeStackElement {
        DefaultScopeStackElement(QueryPart part, int scopeLevel) {
            super(part, scopeLevel);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (positions != null)
                sb.append(Arrays.toString(positions));

            sb.append(part);

            if (mapped != null)
                sb.append(" (" + mapped + ")");

            return sb.toString();
        }
    }
}
