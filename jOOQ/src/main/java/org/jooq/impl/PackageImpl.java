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

import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Package;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.tools.StringUtils;

/**
 * A default implementation for packages (containers of stored procedures and
 * functions)
 * <p>
 * Currently, this is only supported for the {@link SQLDialect#ORACLE} dialect.
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class PackageImpl extends AbstractQueryPart implements Package {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 7466890004995197675L;

    private final Schema          schema;
    private final String          name;

    public PackageImpl(String name, Schema schema) {
        this.schema = schema;
        this.name = name;
    }

    @Override
    public final Catalog getCatalog() {
        return getSchema() == null ? null : getSchema().getCatalog();
    }

    @Override
    public final Schema getSchema() {
        return schema;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.literal(getName());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {

        // [#1938] This is a much more efficient hashCode() implementation
        // compared to that of standard QueryParts
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        // [#1626] PackageImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof PackageImpl) {
            return
                StringUtils.equals(schema, ((PackageImpl) that).getSchema()) &&
                StringUtils.equals(name, ((PackageImpl) that).name);
        }

        return super.equals(that);
    }
}
