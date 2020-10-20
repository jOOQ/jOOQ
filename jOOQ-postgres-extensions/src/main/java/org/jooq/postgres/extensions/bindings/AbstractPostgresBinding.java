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
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;

import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.impl.AbstractBinding;

/**
 * A common base class for bindings in this module.
 *
 * @author Lukas Eder
 */
public abstract class AbstractPostgresBinding<T, U> extends AbstractBinding<T, U> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 689952685043626697L;

    /**
     * Provide the data type name for casts.
     * <p>
     * Most PostgreSQL vendor specific data types need to be cast explicitly,
     * e.g. <code>?::hstore</code>. Implementations should provide this cast
     * type, e.g. <code>hstore</code>.
     */
    protected String castType() {
        return null;
    }

    @Override
    protected void sqlInline(BindingSQLContext<U> ctx) throws SQLException {
        super.sqlInline(ctx);

        String castType = castType();
        if (castType != null)
            ctx.render().sql("::").sql(castType);
    }

    @Override
    protected void sqlBind(BindingSQLContext<U> ctx) throws SQLException {
        super.sqlBind(ctx);

        String castType = castType();
        if (castType != null)
            ctx.render().sql("::").sql(castType);
    }

    // -------------------------------------------------------------------------
    // pgjdbc does not support these yet
    // -------------------------------------------------------------------------

    @Override
    public void set(final BindingSetSQLOutputContext<U> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void get(final BindingGetSQLInputContext<U> ctx) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }
}
