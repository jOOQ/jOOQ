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

import static java.util.Arrays.asList;
import static java.util.Collections.synchronizedMap;
// ...
// ...
// ...
import static org.jooq.conf.DiagnosticsConnection.OFF;
import static org.jooq.conf.ParamType.FORCE_INDEXED;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.noCondition;
import static org.jooq.impl.QOM.commutativeCheck;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import org.jooq.AggregateFunction;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Parser;
// ...
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.QueryPart;
import org.jooq.RenderContext;
// ...
import org.jooq.Select;
import org.jooq.TransactionContext;
import org.jooq.conf.Settings;
import org.jooq.impl.QOM.CompareCondition;
import org.jooq.impl.QOM.Concat;
import org.jooq.impl.QOM.Eq;
import org.jooq.impl.QOM.In;
import org.jooq.impl.QOM.InList;
import org.jooq.impl.QOM.IsDistinctFrom;
import org.jooq.impl.QOM.Mod;
import org.jooq.impl.QOM.NotInList;
import org.jooq.impl.QOM.UCommutativeOperator;
import org.jooq.tools.jdbc.DefaultConnection;

/**
 * @author Lukas Eder
 */
final class DiagnosticsConnection extends DefaultConnection {

    // TODO: Make these configurable
    static final int                LRU_SIZE_GLOBAL = 50000;
    static final int                LRU_SIZE_LOCAL  = 500;
    static final int                DUP_SIZE        = 500;

    final Configuration             configuration;
    final Configuration             configurationTranformPatterns;
    final RenderContext             normalisingRenderer;
    final Parser                    parser;
    final DiagnosticsListeners      listeners;
    final boolean                   release;

    DiagnosticsConnection(Configuration configuration) {
        this(configuration, null);
    }

    DiagnosticsConnection(Configuration configuration, Connection c) {
        super(c != null ? c : configuration.connectionProvider().acquire());

        this.release = c == null;

        // [#7527] The Settings.diagnosticsPattern flag overrides the Settings.transformPatterns flag.
        this.configuration = configuration;
        this.configurationTranformPatterns = configuration.deriveSettings(s -> s.withTransformPatterns(true));
        this.normalisingRenderer = configuration.deriveSettings(s -> s

            // Forcing all inline parameters to be indexed helps find opportunities to use bind variables
            .withParamType(FORCE_INDEXED)

            // Padding IN lists shows duplicates that arise from arbitrary-length dynamic IN lists
            .withInListPadding(true)
            .withInListPadBase(16)
        ).dsl().renderContext();
        this.parser = configuration.dsl().parser();
        this.listeners = DiagnosticsListeners.get(configuration);
    }

    @Override
    public final Statement createStatement() throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().createStatement());
    }

    @Override
    public final Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().createStatement(resultSetType, resultSetConcurrency));
    }

    @Override
    public final Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().prepareStatement(parse(sql)), sql);
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().prepareStatement(parse(sql), resultSetType, resultSetConcurrency), sql);
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().prepareStatement(parse(sql), resultSetType, resultSetConcurrency, resultSetHoldability), sql);
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().prepareStatement(parse(sql), autoGeneratedKeys), sql);
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().prepareStatement(parse(sql), columnIndexes), sql);
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().prepareStatement(parse(sql), columnNames), sql);
    }

    @Override
    public final CallableStatement prepareCall(String sql) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().prepareCall(parse(sql)), sql);
    }

    @Override
    public final CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().prepareCall(parse(sql), resultSetType, resultSetConcurrency), sql);
    }

    @Override
    public final CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new DiagnosticsStatement(this, getDelegate().prepareCall(parse(sql), resultSetType, resultSetConcurrency, resultSetHoldability), sql);
    }

    @Override
    public final void close() throws SQLException {
        if (release) {
            repeatedSql().clear();
            consecutiveAgg().clear();

            configuration.connectionProvider().release(getDelegate());
        }
    }

    final boolean checkPattern(Predicate<? super Settings> test) {
        return DiagnosticsListeners.checkPattern(configuration.settings(), test);
    }

    final boolean check(Predicate<? super Settings> test) {
        return DiagnosticsListeners.check(configuration.settings(), test);
    }

    final Map<String, Set<String>> duplicateSql() {
        return duplicateSql0(configuration);
    }

    @SuppressWarnings("unchecked")
    static final Map<String, Set<String>> duplicateSql0(Configuration configuration) {
        return (Map<String, Set<String>>) configuration.data().computeIfAbsent(
            "org.jooq.diagnostics.duplicate-sql",
            k -> synchronizedMap(new LRU<>(LRU_SIZE_GLOBAL))
        );
    }

    final Map<String, List<String>> repeatedSql() {
        return repeatedSql0(configuration);
    }

    static final Map<String, List<String>> repeatedSql0(Configuration configuration) {
        return repetition0(configuration, "org.jooq.diagnostics.repeated-sql");
    }

    final Map<String, List<String>> consecutiveAgg() {
        return consecutiveAgg0(configuration);
    }

    static final Map<String, List<String>> consecutiveAgg0(Configuration configuration) {
        return repetition0(configuration, "org.jooq.diagnostics.consecutive-agg");
    }

    @SuppressWarnings("unchecked")
    private static final Map<String, List<String>> repetition0(Configuration configuration, String cacheKey) {
        return (Map<String, List<String>>) repetitionData(configuration).computeIfAbsent(
            cacheKey,
            k -> synchronizedMap(new LRU<>(LRU_SIZE_LOCAL))
        );
    }

    private static final Map<Object, Object> repetitionData(Configuration configuration) {
        TransactionContext trx = (TransactionContext) configuration.data(DefaultTransactionContext.DATA_KEY);
        return trx != null ? trx.data() : configuration.data();
    }

    final boolean disabled() {
        return configuration.settings().getDiagnosticsConnection() == OFF;
    }

    final String parse(String sql) {

        // [#7398] Don't do anything if the feature is turned OFF
        if (disabled())
            return sql;

        Queries queries = null;
        Queries transformed = null;
        String normalised;

        try {

            // [#14137] TODO: Avoid unnecessary work, depending on the Settings
            transformed = queries = parser.parse(sql);




            normalised = normalisingRenderer.render(transformed);
        }
        catch (ParserException exception) {
            normalised = sql;
            listeners.exception(new DefaultDiagnosticsContext(
                configuration,
                "Query could not be parsed.", sql, exception
            ));
        }

        try {
            if (check(Settings::isDiagnosticsDuplicateStatements)) {
                Set<String> duplicates = duplicates(duplicateSql(), sql, normalised);

                if (duplicates != null)
                    listeners.duplicateStatements(new DefaultDiagnosticsContext(
                        configuration,
                        "Duplicate statements encountered.",
                        sql, normalised, duplicates, null, queries, transformed, null
                    ));
            }

            if (check(Settings::isDiagnosticsRepeatedStatements)) {
                List<String> repetitions = repetitions(repeatedSql(), sql, normalised);

                if (repetitions != null)
                    listeners.repeatedStatements(new DefaultDiagnosticsContext(
                        configuration,
                        "Repeated statements encountered.",
                        sql, normalised, null, repetitions, queries, transformed, null
                    ));
            }

            if (queries != null) {









































































            }
        }
        catch (Error e) {
            throw e;
        }
        catch (Throwable exception) {
            listeners.exception(new DefaultDiagnosticsContext(
                configuration,
                "An unexpected exception has occurred. See exception for details.",
                sql, normalised, null, null, queries, transformed, exception
            ));
        }

        return sql;
    }


























































    private final Set<String> duplicates(Map<String, Set<String>> map, String sql, String normalised) {
        synchronized (map) {
            Set<String> v = map.computeIfAbsent(normalised, k -> new HashSet<>());

            if (v.size() >= DUP_SIZE || (v.add(sql) && v.size() > 1))
                return v;
            else
                return null;
        }
    }

    private final List<String> repetitions(Map<String, List<String>> map, String sql, String normalised) {
        List<String> v = map.computeIfAbsent(normalised, k -> new ArrayList<>());

        if (v.size() >= DUP_SIZE || (v.add(sql) && v.size() > 1))
            return v;
        else
            return null;
    }

    // See https://stackoverflow.com/a/1953516/521799
    static final class LRU<V> extends LinkedHashMap<String, V> {
        private final int size;

        LRU(int size) {
            super(size + 1, 1.0f, true);
            this.size = size;
        }

        @Override
        protected boolean removeEldestEntry(Entry<String, V> eldest) {
            return size() > size;
        }
    }
}
