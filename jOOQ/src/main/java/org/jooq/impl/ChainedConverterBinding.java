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

import java.sql.SQLException;

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

/**
 * A binding that chains a new converter to an existing binding / converter.
 *
 * @author Lukas Eder
 */
class ChainedConverterBinding<T, U1, U2> implements Binding<T, U2> {

    private final Binding<T, U1>    delegate;
    private final Converter<U1, U2> suffix;
    private final Converter<T, U2>  chained;

    ChainedConverterBinding(Binding<T, U1> delegate, Converter<U1, U2> converter) {
        this.delegate = delegate;
        this.suffix = converter;
        this.chained = Converters.of(delegate.converter(), converter);
    }

    @Override
    public Converter<T, U2> converter() {
        return chained;
    }

    @Override
    public void sql(BindingSQLContext<U2> ctx) throws SQLException {
        delegate.sql(ctx.convert(suffix));
    }

    @Override
    public void register(BindingRegisterContext<U2> ctx) throws SQLException {
        delegate.register(ctx.convert(suffix));
    }

    @Override
    public void set(BindingSetStatementContext<U2> ctx) throws SQLException {
        delegate.set(ctx.convert(suffix));
    }

    @Override
    public void set(BindingSetSQLOutputContext<U2> ctx) throws SQLException {
        delegate.set(ctx.convert(suffix));
    }

    @Override
    public void get(BindingGetResultSetContext<U2> ctx) throws SQLException {
        delegate.get(ctx.convert(suffix));
    }

    @Override
    public void get(BindingGetStatementContext<U2> ctx) throws SQLException {
        delegate.get(ctx.convert(suffix));
    }

    @Override
    public void get(BindingGetSQLInputContext<U2> ctx) throws SQLException {
        delegate.get(ctx.convert(suffix));
    }
}