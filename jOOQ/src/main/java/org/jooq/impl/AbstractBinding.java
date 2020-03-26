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

import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.Keywords.K_NULL;

import java.sql.SQLData;
import java.sql.SQLException;

import org.jooq.Binding;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.conf.ParamType;

/**
 * A convenient base implementation for custom bindings, simplifies the
 * implementation.
 * <p>
 * The simplification is provided by offering empty implementations for rarely
 * used methods, such as those for stored procedure OUT parameter support:
 * <p>
 * <ul>
 * <li>{@link #register(BindingRegisterContext)} to register the OUT parameter.</li>
 * <li>{@link #get(BindingGetStatementContext)} to read the OUT parameter.</li>
 * </ul>
 * <p>
 * or {@link SQLData} serialisation support (e.g. for Oracle user defined types):
 * <p>
 * <ul>
 * <li>{@link #set(BindingSetSQLOutputContext)} to serialise a user defined type.</li>
 * <li>{@link #get(BindingGetSQLInputContext)} to read a user defined type.</li>
 * </ul>
 * <p>
 *
 * @author Lukas Eder
 */
@SuppressWarnings("unused")
public abstract class AbstractBinding<T, U> implements Binding<T, U> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 2172557061592118398L;

    /**
     * A convenient base implementation that handles the {@link ParamType}
     * setting and delegates to {@link #sqlInline(BindingSQLContext)} or
     * {@link #sqlBind(BindingSQLContext)} respectively.
     */
    @Override
    public void sql(BindingSQLContext<U> ctx) throws SQLException {
        if (ctx.render().paramType() == INLINED)
            if (ctx.value() == null)
                ctx.render().visit(K_NULL);
            else
                sqlInline(ctx);
        else
            sqlBind(ctx);
    }

    /**
     * Generate the SQL string for inline values.
     * <p>
     * The default implementation generates a string literal representation of
     * the user defined value. Custom implementations may want to override this.
     */
    protected void sqlInline(BindingSQLContext<U> ctx) throws SQLException {
        ctx.render().visit(DSL.inline("" + ctx.value()));
    }

    /**
     * Generate the SQL string for a bind variable placeholder.
     * <p>
     * In almost all cases, this can just be a "?" string. In some cases, the
     * placeholder needs to be cast to a vendor specific data type, such as in
     * PostgreSQL, "?::tsvector".
     */
    protected void sqlBind(BindingSQLContext<U> ctx) throws SQLException {
        ctx.render().sql(ctx.variable());
    }

    // -------------------------------------------------------------------------
    // Empty base implementations for stored procedure OUT parameter support:
    // -------------------------------------------------------------------------

    @Override
    public void register(BindingRegisterContext<U> ctx) throws SQLException {}

    @Override
    public void get(BindingGetStatementContext<U> ctx) throws SQLException {}

    // -------------------------------------------------------------------------
    // Empty base implementations for SQLData serialisation support:
    // -------------------------------------------------------------------------

    @Override
    public void set(BindingSetSQLOutputContext<U> ctx) throws SQLException {}

    @Override
    public void get(BindingGetSQLInputContext<U> ctx) throws SQLException {}
}
