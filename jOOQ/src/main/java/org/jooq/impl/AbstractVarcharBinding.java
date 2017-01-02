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

import static org.jooq.tools.Convert.convert;

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

/**
 * @author Lukas Eder
 */
abstract class AbstractVarcharBinding<T> implements Binding<Object, T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2153155338260706262L;

    @Override
    public final void sql(BindingSQLContext<T> ctx) throws SQLException {
        ctx.render().visit(DSL.val(ctx.convert(converter()).value()));
    }

    @Override
    public final void register(BindingRegisterContext<T> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public final void set(BindingSetStatementContext<T> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), convert(ctx.convert(converter()).value(), String.class));
    }

    @Override
    public final void get(BindingGetResultSetContext<T> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    @Override
    public final void get(BindingGetStatementContext<T> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }

    @Override
    public final void set(BindingSetSQLOutputContext<T> ctx) throws SQLException {
        ctx.output().writeString(convert(ctx.convert(converter()).value(), String.class));
    }

    @Override
    public final void get(BindingGetSQLInputContext<T> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.input().readString());
    }
}
