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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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

import static org.jooq.impl.Tools.CONFIG;
import static org.jooq.impl.Tools.CONFIG_FORMATTED;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

import org.jooq.Attachable;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DSLContext;
// ...
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.tools.JooqLogger;

/**
 * @author Lukas Eder
 */
abstract class AbstractQueryPart implements QueryPartInternal {

    // -------------------------------------------------------------------------
    // [#1544] The deprecated Attachable and Attachable internal API
    // -------------------------------------------------------------------------

    Configuration configuration() {
        return CONFIG.get();
    }

    // -------------------------------------------------------------------------
    // Deprecated API
    // -------------------------------------------------------------------------

    /**
     * @deprecated - 3.11.0 - [#8179] - This functionality will be removed in
     *             the future.
     */
    @Deprecated
    @Override
    public Clause[] clauses(Context<?> ctx) {
        return null;
    }

    // -------------------------------------------------------------------------
    // The QueryPart and QueryPart internal API
    // -------------------------------------------------------------------------

    /**
     * Subclasses may override this
     */
    @Override
    public boolean rendersContent(Context<?> ctx) {
        return true;
    }

    /**
     * Subclasses may override this
     */
    @Override
    public boolean declaresFields() {
        return false;
    }

    /**
     * Subclasses may override this
     */
    @Override
    public boolean declaresTables() {
        return false;
    }

    /**
     * Subclasses may override this
     */
    @Override
    public boolean declaresWindows() {
        return false;
    }

    /**
     * Subclasses may override this
     */
    @Override
    public boolean declaresCTE() {
        return false;
    }














    /**
     * Subclasses may override this
     */
    @Override
    public boolean generatesCast() {
        return false;
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        // This is a working default implementation. It should be overridden by
        // concrete subclasses, to improve performance
        if (that instanceof QueryPart q) {

            // [#10635] The two QueryParts may have different Settings attached.
            Configuration c1 = Tools.configuration(configuration());
            Configuration c2 = that instanceof AbstractQueryPart a ? Tools.configuration(a.configuration()) : c1;

            // [#18931] Avoid unnecessary DSLContext allocations (e.g. in hot loops)
            DSLContext dsl1 = c1.dsl();
            DSLContext dsl2 = c2 == c1 ? dsl1 : c2.dsl();

            String sql1 = dsl1.renderInlined(this);
            String sql2 = dsl2.renderInlined(q);

            return sql1.equals(sql2);
        }

        return false;
    }

    @Override
    public int hashCode() {
        // This is a working default implementation. It should be overridden by
        // concrete subclasses, to improve performance

        return create().renderInlined(this).hashCode();
    }

    @Override
    public String toString() {
        try {

            // [#8355] Subtypes may have null configuration
            Configuration configuration = Tools.configuration(configuration());

            // [#19059] Avoid Configuration::derive and its internal allocations if we can, in case this is called in a hot loop.
            if (configuration instanceof InternalConfiguration)
                return create(CONFIG_FORMATTED.get()).renderInlined(this);
            else
                return create(configuration.deriveSettings(s -> s.withRenderFormatted(true))).renderInlined(this);
        }
        catch (SQLDialectNotSupportedException e) {
            return "[ ... " + e.getMessage() + " ... ]";
        }
    }

    // -------------------------------------------------------------------------
    // Internal convenience methods
    // -------------------------------------------------------------------------

    /**
     * Internal convenience method
     *
     * @deprecated - 3.11.0 - [#6722] - Use {@link Attachable#configuration()}
     *             and {@link Configuration#dsl()} instead.
     */
    @Deprecated
    protected final DSLContext create() {
        return create(configuration());
    }

    /**
     * Internal convenience method
     *
     * @deprecated - 3.11.0 - [#6722] - Use {@link Attachable#configuration()}
     *             and {@link Configuration#dsl()} instead.
     */
    @Deprecated
    protected final DSLContext create(Configuration configuration) {
        return DSL.using(configuration);
    }

    /**
     * Internal convenience method
     *
     * @deprecated - 3.11.0 - [#6722] - Use {@link Attachable#configuration()}
     *             and {@link Configuration#dsl()} instead.
     */
    @Deprecated
    protected final DSLContext create(Context<?> ctx) {
        return DSL.using(ctx.configuration());
    }

    /**
     * Internal convenience method
     */
    protected final DataAccessException translate(String sql, SQLException e) {
        return Tools.translate(sql, e);
    }
}
