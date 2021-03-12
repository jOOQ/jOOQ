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

import static org.jooq.impl.Tools.EMPTY_PARAM;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.Parser;
import org.jooq.impl.DefaultRenderContext.Rendered;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.DefaultConnection;

/**
 * @author Lukas Eder
 */
final class ParsingConnection extends DefaultConnection {

    private static final JooqLogger log = JooqLogger.getLogger(ParsingConnection.class);

    final Configuration             configuration;
    final DSLContext                ctx;
    final Parser                    parser;

    ParsingConnection(Configuration configuration) {
        super(configuration.connectionProvider().acquire());

        this.configuration = configuration;
        this.ctx = DSL.using(configuration);
        this.parser = ctx.parser();
    }

    final Rendered translate(String sql, Param<?>... bindValues) {
        log.debug("Translating from", sql);
        DefaultRenderContext render = (DefaultRenderContext) ctx.renderContext();
        Rendered result = new Rendered(
            render.visit(parser.parseQuery(sql, (Object[]) bindValues)).render(),
            render.bindValues(),
            render.skipUpdateCounts()
        );
        log.debug("Translating to", result.sql);
        return result;
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
            Rendered rendered = size == 0 ? translate(sql) : translate(sql, p.get(0).toArray(EMPTY_PARAM));
            PreparedStatement s = prepare.apply(rendered.sql);

            for (int i = 0; i < size; i++) {

                // TODO: Can we avoid re-parsing and re-generating the SQL and mapping bind values only?
                if (i > 0)
                    rendered = translate(sql, p.get(i).toArray(EMPTY_PARAM));

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
