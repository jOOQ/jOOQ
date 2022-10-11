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

import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.impl.DefaultExecuteContext.localConnection;
import static org.jooq.impl.DefaultExecuteContext.localTargetConnection;
import static org.jooq.impl.Tools.asInt;

import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Set;

import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Converter;
import org.jooq.Converters;
import org.jooq.ResourceManagingScope;
import org.jooq.SQLDialect;
import org.jooq.Scope;
import org.jooq.Source;
import org.jooq.conf.ParamType;
import org.jooq.tools.jdbc.JDBCUtils;

// ...

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

    static final Set<SQLDialect> NO_SUPPORT_NULL_LOBS = SQLDialect.supportedBy(FIREBIRD, HSQLDB);

    @Override
    public final Converter<String, String> converter() {
        return Converters.identity(String.class);
    }

    @Override
    public final void sql(BindingSQLContext<String> ctx) throws SQLException {
        if (ctx.render().paramType() == ParamType.INLINED)
            ctx.render().visit(DSL.inline(ctx.convert(converter()).value(), SQLDataType.CLOB));
        else
            ctx.render().sql(ctx.variable());
    }

    @Override
    public final void register(BindingRegisterContext<String> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.CLOB);
    }

    @Override
    public final void set(BindingSetStatementContext<String> ctx) throws SQLException {
        Clob clob = newClob(ctx, ctx.value());

        // [#14067] Workaround for Firebird bug https://github.com/FirebirdSQL/jaybird/issues/712
        if (clob == null && NO_SUPPORT_NULL_LOBS.contains(ctx.dialect()))
            ctx.statement().setNull(ctx.index(), Types.CLOB);
        else
            ctx.statement().setClob(ctx.index(), clob);
    }

    @Override
    public final void set(BindingSetSQLOutputContext<String> ctx) throws SQLException {
        ctx.output().writeClob(newClob(ctx, ctx.value()));
    }

    @Override
    public final void get(BindingGetResultSetContext<String> ctx) throws SQLException {
        Clob clob = ctx.resultSet().getClob(ctx.index());

        try {
            ctx.value(clob == null ? null : read(ctx, clob));
        }
        finally {
            JDBCUtils.safeFree(clob);
        }
    }

    @Override
    public final void get(BindingGetStatementContext<String> ctx) throws SQLException {
        Clob clob = ctx.statement().getClob(ctx.index());

        try {
            ctx.value(clob == null ? null : read(ctx, clob));
        }
        finally {
            JDBCUtils.safeFree(clob);
        }
    }

    @Override
    public final void get(BindingGetSQLInputContext<String> ctx) throws SQLException {
        Clob clob = ctx.input().readClob();

        try {
            ctx.value(clob == null ? null : read(ctx, clob));
        }
        finally {
            JDBCUtils.safeFree(clob);
        }
    }

    static final String read(Scope ctx, Clob clob) throws SQLException {
        switch (ctx.family()) {

            // [#13144] Cannot call Clob::length in Firebird
            case FIREBIRD:

            // [#13217] The Clob may be non-null, yet the data is still null in SQLite
            case SQLITE: {
                Reader r = clob.getCharacterStream();
                return r == null ? null : Source.of(r).readString();
            }

            default: {
                return clob.getSubString(1, asInt(clob.length()));
            }
        }
    }

    static final Clob newClob(ResourceManagingScope scope, String string) throws SQLException {
        if (string == null)
            return null;

        Clob clob;

        switch (scope.dialect()) {











            default: {
                clob = localConnection().createClob();
                break;
            }
        }

        scope.autoFree(clob);
        clob.setString(1, string);
        return clob;
    }
}
