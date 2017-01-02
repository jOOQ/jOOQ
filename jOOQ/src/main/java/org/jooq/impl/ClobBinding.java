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

import static org.jooq.tools.reflect.Reflect.on;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Configuration;
import org.jooq.Converter;
import org.jooq.Converters;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * A binding that takes binary values but binds them as {@link Clob} to at the
 * JDBC level.
 * <p>
 * This is useful for workarounds for bugs in Oracle, like ORA-01461: can bind a
 * LONG value only for insert into a LONG column (see [#4091])
 *
 * @author Lukas Eder
 */
public class ClobBinding implements Binding<String, String> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 358789452467943117L;

    @Override
    public final Converter<String, String> converter() {
        return Converters.identity(String.class);
    }

    @Override
    public final void sql(BindingSQLContext<String> ctx) throws SQLException {
        ctx.render().sql("?");
    }

    @Override
    public final void register(BindingRegisterContext<String> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.CLOB);
    }

    @Override
    public final void set(BindingSetStatementContext<String> ctx) throws SQLException {
        Clob clob = newClob(ctx.configuration(), ctx.value());
        DefaultExecuteContext.register(clob);
        ctx.statement().setClob(ctx.index(), clob);
    }

    @Override
    public final void set(BindingSetSQLOutputContext<String> ctx) throws SQLException {
        Clob clob = newClob(ctx.configuration(), ctx.value());
        DefaultExecuteContext.register(clob);
        ctx.output().writeClob(clob);
    }

    @Override
    public final void get(BindingGetResultSetContext<String> ctx) throws SQLException {
        Clob clob = ctx.resultSet().getClob(ctx.index());

        try {
            ctx.value(clob == null ? null : clob.getSubString(1, (int) clob.length()));
        }
        finally {
            JDBCUtils.safeFree(clob);
        }
    }

    @Override
    public final void get(BindingGetStatementContext<String> ctx) throws SQLException {
        Clob clob = ctx.statement().getClob(ctx.index());

        try {
            ctx.value(clob == null ? null : clob.getSubString(1, (int) clob.length()));
        }
        finally {
            JDBCUtils.safeFree(clob);
        }
    }

    @Override
    public final void get(BindingGetSQLInputContext<String> ctx) throws SQLException {
        Clob clob = ctx.input().readClob();

        try {
            ctx.value(clob == null ? null : clob.getSubString(1, (int) clob.length()));
        }
        finally {
            JDBCUtils.safeFree(clob);
        }
    }

    private final Clob newClob(Configuration configuration, String string) throws SQLException {
        Connection c = configuration.connectionProvider().acquire();

        try {
            Clob clob = null;

            switch (configuration.family()) {











                default: {
                    clob = c.createClob();
                    break;
                }
            }

            clob.setString(1, string);
            return clob;
        }
        finally {
            configuration.connectionProvider().release(c);
        }
    }
}
