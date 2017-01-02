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

// ...
import static org.jooq.impl.DSL.val;

import java.sql.SQLException;
import java.sql.Timestamp;

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
import org.jooq.RenderContext;
import org.jooq.SQLDialect;

/**
 * A binding that implements the date-as-timestamp semantics of the jOOQ code
 * generator.
 *
 * @author Lukas Eder
 * @see <a
 *      href="https://github.com/jOOQ/jOOQ/issues/3369">https://github.com/jOOQ/jOOQ/issues/3369</a>
 */
public class DateAsTimestampBinding implements Binding<Timestamp, Timestamp> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -7730882831126647188L;

    private final Converter<Timestamp, Timestamp> converter;
    private final DefaultBinding<Timestamp, Timestamp> delegate;

    public DateAsTimestampBinding() {
        this.converter = Converters.identity(Timestamp.class);
        this.delegate = new DefaultBinding<Timestamp, Timestamp>(converter);
    }

    @Override
    public final Converter<Timestamp, Timestamp> converter() {
        return converter;
    }

    @Override
    public final void sql(BindingSQLContext<Timestamp> ctx) throws SQLException {















        delegate.sql(ctx);
    }

    @Override
    public final void register(BindingRegisterContext<Timestamp> ctx) throws SQLException {
        delegate.register(ctx);
    }

    @Override
    public final void set(BindingSetStatementContext<Timestamp> ctx) throws SQLException {
        delegate.set(ctx);
    }

    @Override
    public final void set(BindingSetSQLOutputContext<Timestamp> ctx) throws SQLException {
        delegate.set(ctx);
    }

    @Override
    public final void get(BindingGetResultSetContext<Timestamp> ctx) throws SQLException {
        delegate.get(ctx);
    }

    @Override
    public final void get(BindingGetStatementContext<Timestamp> ctx) throws SQLException {
        delegate.get(ctx);
    }

    @Override
    public final void get(BindingGetSQLInputContext<Timestamp> ctx) throws SQLException {
        delegate.get(ctx);
    }
}
