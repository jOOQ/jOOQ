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

    @Override
    public final Converter<String, String> converter() {
        return Converters.identity(String.class);
    }

    @Override
    public final void sql(BindingSQLContext<String> ctx) throws SQLException {
        ctx.render().visit(DSL.val(ctx.value(), SQLDataType.NCLOB));
    }

    @Override
    public final void register(BindingRegisterContext<String> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.NCLOB);
    }

    @Override
    public final void set(BindingSetStatementContext<String> ctx) throws SQLException {
        ctx.statement().setClob(ctx.index(), newNClob(ctx, ctx.value()));
    }

    @Override
    public final void set(BindingSetSQLOutputContext<String> ctx) throws SQLException {
        ctx.output().writeClob(newNClob(ctx, ctx.value()));
    }

    @Override
    public final void get(BindingGetResultSetContext<String> ctx) throws SQLException {
        NClob clob = ctx.resultSet().getNClob(ctx.index());

        try {
            ctx.value(clob == null ? null : clob.getSubString(1, asInt(clob.length())));
        }
        finally {
            JDBCUtils.safeFree(clob);
        }
    }

    @Override
    public final void get(BindingGetStatementContext<String> ctx) throws SQLException {
        NClob clob = ctx.statement().getNClob(ctx.index());

        try {
            ctx.value(clob == null ? null : clob.getSubString(1, asInt(clob.length())));
        }
        finally {
            JDBCUtils.safeFree(clob);
        }
    }

    @Override
    public final void get(BindingGetSQLInputContext<String> ctx) throws SQLException {
        NClob clob = ctx.input().readNClob();

        try {
            ctx.value(clob == null ? null : clob.getSubString(1, asInt(clob.length())));
        }
        finally {
            JDBCUtils.safeFree(clob);
        }
    }

    static final NClob newNClob(ResourceManagingScope scope, String string) throws SQLException {
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
