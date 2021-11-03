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

import static java.util.Collections.emptyList;
import static org.jooq.conf.SettingsTools.getParamType;
import static org.jooq.impl.CacheType.CACHE_PARSING_CONNECTION;
import static org.jooq.impl.Tools.EMPTY_PARAM;
import static org.jooq.impl.Tools.map;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DetachedException;
import org.jooq.impl.DefaultRenderContext.Rendered;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.DefaultConnection;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class ParsingConnection extends DefaultConnection {

    private static final JooqLogger log = JooqLogger.getLogger(ParsingConnection.class);

    final Configuration             configuration;

    ParsingConnection(Configuration configuration) {
        super(configuration.connectionProvider().acquire());

        if (getDelegate() == null)
            if (configuration.connectionFactory() instanceof NoConnectionFactory)
                throw new DetachedException("ConnectionProvider did not provide a JDBC Connection");
            else
                throw new DetachedException("Attempt to use a ParsingConnection (JDBC) when only an R2BDC ConnectionFactory was configured. Using ParsingConnectionFactory instead.");

        this.configuration = configuration;
    }

    static final class CacheValue {
        final String                      output;
        final int                         bindSize;
        final Map<Integer, List<Integer>> bindMapping;

        CacheValue(Configuration configuration, String input, Param<?>[] bindValues) {
            DSLContext ctx = configuration.dsl();
            DefaultRenderContext render = (DefaultRenderContext) ctx.renderContext();
            render.paramType(configuration.settings().getParamType()).visit(ctx.parser().parseQuery(input, (Object[]) bindValues));

            output = render.render();
            bindSize = render.bindValues().size();
            bindMapping = new HashMap<>();

            // TODO: We shouldn't rely on identity for these reasons:
            // - Copies are possible
            // - Wrappings are possible
            // - Conversions are possible
            // Ideally, we should be able to maintain and extract the map directly in the DefaultRenderContext
            // TODO: If anything goes wrong (probably because of the above), the cache must be invalid, and we must re-parse and re-render the query every time
            for (int i = 0; i < bindValues.length; i++)
                for (int j = 0; j < render.bindValues().size(); j++)
                    if (bindValues[i] == render.bindValues().get(j))
                        bindMapping.computeIfAbsent(i, x -> new ArrayList<>()).add(j);
        }

        Rendered rendered(Param<?>... bindValues) {
            Param<?>[] binds = new Param[bindSize];

            for (int i = 0; i < bindValues.length; i++)
                for (int mapped : bindMapping.getOrDefault(i, emptyList()))
                    binds[mapped] = bindValues[i];

            return new Rendered(output, new QueryPartList<>(binds), 0);
        }

        @Override
        public String toString() {
            return output;
        }
    }

    static final Rendered translate(Configuration configuration, String sql, Param<?>... bindValues) {
        log.debug("Translating from", sql);
        Rendered result = null;

        Supplier<CacheValue> miss = () -> {
            log.debug("Translation cache miss", sql);
            return new CacheValue(configuration, sql, bindValues);
        };

        Settings settings = configuration.settings();
        if (CACHE_PARSING_CONNECTION.category.predicate.test(settings) && bindValues.length > 0) {
            switch (getParamType(settings)) {
                case INLINED:
                case NAMED_OR_INLINED:
                    result = miss.get().rendered(bindValues);
                    break;
            }
        }

        if (result == null)
            result = Cache.run(
                configuration,
                miss,
                CACHE_PARSING_CONNECTION,
                () -> Cache.key(sql, map(nonNull(bindValues), f -> f.getDataType()))
            ).rendered(bindValues);

        log.debug("Translating to", result.sql);
        return result;
    }

    private static Param<?>[] nonNull(Param<?>[] bindValues) {
        for (int i = 0; i < bindValues.length; i++)
            if (bindValues[i] == null)
                throw new DataAccessException("Bind value at position " + i + " not set");

        return bindValues;
    }

    @Override
    public final Statement createStatement() throws SQLException {
        return new ParsingStatement(this, getDelegate().createStatement());
    }

    @Override
    public final Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return new ParsingStatement(this, getDelegate().createStatement(resultSetType, resultSetConcurrency));
    }

    @Override
    public final Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new ParsingStatement(this, getDelegate().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    private final ThrowingFunction<List<List<Param<?>>>, PreparedStatement, SQLException> prepareAndBind(
        String sql,
        ThrowingFunction<String, PreparedStatement, SQLException> prepare
    ) {
        return p -> {
            int size = p.size();
            Rendered rendered = size == 0 ? translate(configuration, sql) : translate(configuration, sql, p.get(0).toArray(EMPTY_PARAM));
            PreparedStatement s = prepare.apply(rendered.sql);

            for (int i = 0; i < size; i++) {

                // TODO: Can we avoid re-parsing and re-generating the SQL and mapping bind values only?
                if (i > 0)
                    rendered = translate(configuration, sql, p.get(i).toArray(EMPTY_PARAM));

                new DefaultBindContext(configuration, s).visit(rendered.bindValues);

                // TODO: Find a less hacky way to signal that we're batching. Currently:
                // - ArrayList<Arraylist<Param<?>>> = batching
                // - SingletonList<ArrayList<Param<?>>> = not batching
                if (size > 1 || p instanceof ArrayList)
                    s.addBatch();
            }

            return s;
        };
    }

    @Override
    public final PreparedStatement prepareStatement(String sql) throws SQLException {
        return new ParsingStatement(this, prepareAndBind(sql, s -> getDelegate().prepareStatement(s)));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return new ParsingStatement(this, prepareAndBind(sql, s -> getDelegate().prepareStatement(s, resultSetType, resultSetConcurrency)));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new ParsingStatement(this, prepareAndBind(sql, s -> getDelegate().prepareStatement(s, resultSetType, resultSetConcurrency, resultSetHoldability)));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return new ParsingStatement(this, prepareAndBind(sql, s -> getDelegate().prepareStatement(s, autoGeneratedKeys)));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return new ParsingStatement(this, prepareAndBind(sql, s -> getDelegate().prepareStatement(s, columnIndexes)));
    }

    @Override
    public final PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return new ParsingStatement(this, prepareAndBind(sql, s -> getDelegate().prepareStatement(s, columnNames)));
    }

    @Override
    public final CallableStatement prepareCall(String sql) throws SQLException {
        return new ParsingStatement(this, prepareAndBind(sql, s -> getDelegate().prepareCall(s)));
    }

    @Override
    public final CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return new ParsingStatement(this, prepareAndBind(sql, s -> getDelegate().prepareCall(s, resultSetType, resultSetConcurrency)));
    }

    @Override
    public final CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new ParsingStatement(this, prepareAndBind(sql, s -> getDelegate().prepareCall(s, resultSetType, resultSetConcurrency, resultSetHoldability)));
    }

    @Override
    public final void close() throws SQLException {
        configuration.connectionProvider().release(getDelegate());
    }
}
