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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.impl.ClobBinding.NO_SUPPORT_NULL_LOBS;
import static org.jooq.impl.DefaultExecuteContext.localConnection;
import static org.jooq.impl.DefaultExecuteContext.localTargetConnection;
import static org.jooq.impl.Tools.asInt;

import java.sql.Blob;
import java.sql.Connection;
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
import org.jooq.conf.ParamType;
import org.jooq.impl.R2DBC.R2DBCPreparedStatement;
import org.jooq.impl.R2DBC.R2DBCResultSet;
import org.jooq.tools.jdbc.JDBCUtils;

// ...

/**
 * A binding that takes binary values but binds them as {@link Blob} to at the
 * JDBC level.
 * <p>
 * This is useful for workarounds for bugs in Oracle, like ORA-01461: can bind a
 * LONG value only for insert into a LONG column (see [#4091])
 *
 * @author Lukas Eder
 */
public class BlobBinding implements Binding<byte[], byte[]> {

    // See also https://github.com/pgjdbc/pgjdbc/issues/458
    static final Set<SQLDialect> NO_SUPPORT_LOBS      = SQLDialect.supportedBy(POSTGRES, SQLITE, TRINO, YUGABYTEDB);

    @Override
    public final Converter<byte[], byte[]> converter() {
        return Converters.identity(byte[].class);
    }

    @Override
    public final void sql(BindingSQLContext<byte[]> ctx) throws SQLException {
        if (ctx.render().paramType() == ParamType.INLINED)
            ctx.render().visit(DSL.inline(ctx.convert(converter()).value(), SQLDataType.BLOB));
        else
            ctx.render().sql(ctx.variable());
    }

    @Override
    public final void register(BindingRegisterContext<byte[]> ctx) throws SQLException {
        if (!NO_SUPPORT_LOBS.contains(ctx.dialect()))
            ctx.statement().registerOutParameter(ctx.index(), Types.BLOB);
        else
            ctx.statement().registerOutParameter(ctx.index(), Types.BINARY);
    }

    @Override
    public final void set(BindingSetStatementContext<byte[]> ctx) throws SQLException {

        // [#12964] We don't support the whole Blob API for R2DBC yet.
        if (ctx.statement() instanceof R2DBCPreparedStatement) {
            ctx.statement().setBytes(ctx.index(), ctx.value());
        }
        else if (!NO_SUPPORT_LOBS.contains(ctx.dialect())) {
            Blob blob = newBlob(ctx, ctx.value(), ctx.statement().getConnection());

            // [#14067] Workaround for Firebird bug https://github.com/FirebirdSQL/jaybird/issues/712
            if (blob == null && NO_SUPPORT_NULL_LOBS.contains(ctx.dialect()))
                ctx.statement().setNull(ctx.index(), Types.BLOB);
            else
                ctx.statement().setBlob(ctx.index(), blob);
        }
        else
            ctx.statement().setBytes(ctx.index(), ctx.value());
    }

    @Override
    public final void set(BindingSetSQLOutputContext<byte[]> ctx) throws SQLException {
        if (!NO_SUPPORT_LOBS.contains(ctx.dialect()))
            ctx.output().writeBlob(newBlob(ctx, ctx.value(), null));
        else
            ctx.output().writeBytes(ctx.value());
    }

    @Override
    public final void get(BindingGetResultSetContext<byte[]> ctx) throws SQLException {

        // [#12964] We don't support the whole Blob API for R2DBC yet.
        if (ctx.resultSet() instanceof R2DBCResultSet) {
            ctx.value(ctx.resultSet().getBytes(ctx.index()));
        }
        else if (!NO_SUPPORT_LOBS.contains(ctx.dialect())) {
            Blob blob = ctx.resultSet().getBlob(ctx.index());

            try {
                ctx.value(blob == null ? null : blob.getBytes(1, asInt(blob.length())));
            }
            finally {
                JDBCUtils.safeFree(blob);
            }
        }
        else
            ctx.value(ctx.resultSet().getBytes(ctx.index()));
    }

    @Override
    public final void get(BindingGetStatementContext<byte[]> ctx) throws SQLException {
        if (!NO_SUPPORT_LOBS.contains(ctx.dialect())) {
            Blob blob = ctx.statement().getBlob(ctx.index());

            try {
                ctx.value(blob == null ? null : blob.getBytes(1, asInt(blob.length())));
            }
            finally {
                JDBCUtils.safeFree(blob);
            }
        }
        else
            ctx.value(ctx.statement().getBytes(ctx.index()));
    }

    @Override
    public final void get(BindingGetSQLInputContext<byte[]> ctx) throws SQLException {
        if (!NO_SUPPORT_LOBS.contains(ctx.dialect())) {
            Blob blob = ctx.input().readBlob();

            try {
                ctx.value(blob == null ? null : blob.getBytes(1, asInt(blob.length())));
            }
            finally {
                JDBCUtils.safeFree(blob);
            }
        }
        else
            ctx.value(ctx.input().readBytes());
    }

    static final Blob newBlob(ResourceManagingScope scope, byte[] bytes, Connection connection) throws SQLException {
        if (bytes == null)
            return null;

        Blob blob;

        switch (scope.dialect()) {











            default: {
                blob = (connection != null ? connection : localConnection()).createBlob();
                break;
            }
        }

        scope.autoFree(blob);
        blob.setBytes(1, bytes);
        return blob;
    }
}
