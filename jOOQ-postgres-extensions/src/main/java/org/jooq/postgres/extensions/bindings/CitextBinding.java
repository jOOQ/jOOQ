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
import org.jooq.Converter;
import org.jooq.postgres.extensions.converters.CitextConverter;

/**
 * A binding for the PostgreSQL <code>citext</code> data type.
 *
 * @author Lukas Eder
 */
public class CitextBinding extends AbstractPostgresBinding<Object, String> {

    private static final Converter<Object, String> CONVERTER = new CitextConverter();

    @Override
    public Converter<Object, String> converter() {
        return CONVERTER;
    }

    @Override
    protected String castType() {
        return "citext";
    }

    @Override
    public void register(final BindingRegisterContext<String> ctx) throws SQLException {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR);
    }

    @Override
    public void set(final BindingSetStatementContext<String> ctx) throws SQLException {
        ctx.statement().setString(ctx.index(), ctx.value());
    }


    @Override
    public void get(final BindingGetResultSetContext<String> ctx) throws SQLException {
        ctx.value(ctx.resultSet().getString(ctx.index()));
    }

    @Override
    public void get(final BindingGetStatementContext<String> ctx) throws SQLException {
        ctx.value(ctx.statement().getString(ctx.index()));
    }
}
