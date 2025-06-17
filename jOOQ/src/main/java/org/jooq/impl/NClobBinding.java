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

import static org.jooq.impl.ClobBinding.NO_SUPPORT_LOBS;
import static org.jooq.impl.ClobBinding.NO_SUPPORT_NULL_LOBS;
import static org.jooq.impl.DefaultBinding.InternalBinding.NO_SUPPORT_NVARCHAR;
import static org.jooq.impl.DefaultExecuteContext.localConnection;
import static org.jooq.impl.DefaultExecuteContext.localTargetConnection;
import static org.jooq.impl.Tools.asInt;

import java.sql.NClob;
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
import org.jooq.Converter;
import org.jooq.Converters;
import org.jooq.ResourceManagingScope;
import org.jooq.conf.ParamType;
import org.jooq.tools.jdbc.JDBCUtils;

// ...

/**
 * A binding that takes binary values but binds them as {@link NClob} to at the
 * JDBC level.
 * <p>
 * This is useful for workarounds for bugs in Oracle, like ORA-01461: can bind a
 * LONG value only for insert into a LONG column (see [#4091])
 *
 * @author Lukas Eder
 */
public class NClobBinding implements Binding<String, String> {

    final ClobBinding clobBinding = new ClobBinding();

    @Override
    public final Converter<String, String> converter() {
        return Converters.identity(String.class);
    }

    @Override
    public final void sql(BindingSQLContext<String> ctx) throws SQLException {
        if (ctx.render().paramType() == ParamType.INLINED)
            ctx.render().visit(DSL.inline(ctx.convert(converter()).value(), SQLDataType.NCLOB));
        else
            ctx.render().sql(ctx.variable());
    }

    @Override
    public final void register(BindingRegisterContext<String> ctx) throws SQLException {
        if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect()))
            clobBinding.register(ctx);
        else if (!NO_SUPPORT_LOBS.contains(ctx.dialect()))
            ctx.statement().registerOutParameter(ctx.index(), Types.NCLOB);
        else
            ctx.statement().registerOutParameter(ctx.index(), Types.NVARCHAR);
    }

    @Override
    public final void set(BindingSetStatementContext<String> ctx) throws SQLException {
        if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect())) {
            clobBinding.set(ctx);
        }
        else if (!NO_SUPPORT_LOBS.contains(ctx.dialect())) {
            NClob clob = newNClob(ctx, ctx.value());

            // [#14067] Workaround for Firebird bug https://github.com/FirebirdSQL/jaybird/issues/712
            if (clob == null && NO_SUPPORT_NULL_LOBS.contains(ctx.dialect()))
                ctx.statement().setNull(ctx.index(), Types.NCLOB);
            else
                ctx.statement().setNClob(ctx.index(), clob);
        }
        else
            ctx.statement().setNString(ctx.index(), ctx.value());
    }

    @Override
    public final void set(BindingSetSQLOutputContext<String> ctx) throws SQLException {
        if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect()))
            clobBinding.set(ctx);
        else if (!NO_SUPPORT_LOBS.contains(ctx.dialect()))
            ctx.output().writeNClob(newNClob(ctx, ctx.value()));
        else
            ctx.output().writeNString(ctx.value());
    }

    @Override
    public final void get(BindingGetResultSetContext<String> ctx) throws SQLException {
        if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect())) {
            clobBinding.get(ctx);
        }
        else if (!NO_SUPPORT_LOBS.contains(ctx.dialect())) {
            NClob clob = ctx.resultSet().getNClob(ctx.index());

            try {
                ctx.value(clob == null ? null : clob.getSubString(1, asInt(clob.length())));
            }
            finally {
                JDBCUtils.safeFree(clob);
            }
        }
        else
            ctx.value(ctx.resultSet().getNString(ctx.index()));
    }

    @Override
    public final void get(BindingGetStatementContext<String> ctx) throws SQLException {
        if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect())) {
            clobBinding.get(ctx);
        }
        else if (!NO_SUPPORT_LOBS.contains(ctx.dialect())) {
            NClob clob = ctx.statement().getNClob(ctx.index());

            try {
                ctx.value(clob == null ? null : clob.getSubString(1, asInt(clob.length())));
            }
            finally {
                JDBCUtils.safeFree(clob);
            }
        }
        else
            ctx.value(ctx.statement().getNString(ctx.index()));
    }

    @Override
    public final void get(BindingGetSQLInputContext<String> ctx) throws SQLException {
        if (NO_SUPPORT_NVARCHAR.contains(ctx.dialect())) {
            clobBinding.get(ctx);
        }
        else if (!NO_SUPPORT_LOBS.contains(ctx.dialect())) {
            NClob clob = ctx.input().readNClob();

            try {
                ctx.value(clob == null ? null : clob.getSubString(1, asInt(clob.length())));
            }
            finally {
                JDBCUtils.safeFree(clob);
            }
        }
        else
            ctx.value(ctx.input().readNString());
    }

    static final NClob newNClob(ResourceManagingScope scope, String string) throws SQLException {
        if (string == null)
            return null;

        NClob clob;

        switch (scope.dialect()) {











            default: {
                clob = localConnection().createNClob();
                break;
            }
        }

        scope.autoFree(clob);
        clob.setString(1, string);
        return clob;
    }
}
