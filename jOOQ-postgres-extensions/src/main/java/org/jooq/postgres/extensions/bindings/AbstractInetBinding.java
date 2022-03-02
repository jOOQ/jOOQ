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
package org.jooq.postgres.extensions.bindings;

import java.sql.SQLException;
import java.sql.Types;

import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.postgres.extensions.types.AbstractInet;

/**
 * A binding for the PostgreSQL <code>inet</code> or <code>cidr</code> data
 * type.
 *
 * @author Lukas Eder
 */
abstract class AbstractInetBinding<U extends AbstractInet> extends AbstractPostgresBinding<Object, U> {

    @Override
    public void register(final BindingRegisterContext<U> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void set(final BindingSetStatementContext<U> ctx) throws SQLException {
        Object value = ctx.convert(converter()).value();

        ctx.statement().setString(ctx.index(), value == null ? null : "" + value);
    }

    @Override
    public void get(final BindingGetResultSetContext<U> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()));
    }

    @Override
    public void get(final BindingGetStatementContext<U> ctx) throws SQLException {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()));
    }
}
