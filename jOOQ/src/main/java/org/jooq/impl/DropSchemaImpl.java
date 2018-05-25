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

import static org.jooq.Clause.DROP_SCHEMA;
import static org.jooq.Clause.DROP_SCHEMA_SCHEMA;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
// ...
import static org.jooq.impl.Keywords.K_CASCADE;
import static org.jooq.impl.Keywords.K_DROP_SCHEMA;
import static org.jooq.impl.Keywords.K_IF_EXISTS;
import static org.jooq.impl.Keywords.K_RESTRICT;

import java.util.EnumSet;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DropSchemaFinalStep;
import org.jooq.DropSchemaStep;
import org.jooq.SQLDialect;
import org.jooq.Schema;


/**
 * @author Lukas Eder
 */
final class DropSchemaImpl extends AbstractQuery implements

    // Cascading interface implementations for DROP VIEW behaviour
    DropSchemaStep {

    /**
     * Generated UID
     */
    private static final long                serialVersionUID     = 8904572826501186329L;
    private static final Clause[]            CLAUSES              = { DROP_SCHEMA };
    private static final EnumSet<SQLDialect> NO_SUPPORT_IF_EXISTS = EnumSet.of(DERBY, FIREBIRD);
    private static final EnumSet<SQLDialect> REQUIRES_RESTRICT    = EnumSet.of(DERBY);

    private final Schema                     schema;
    private final boolean                    ifExists;
    private boolean                          cascade;

    DropSchemaImpl(Configuration configuration, Schema schema) {
        this(configuration, schema, false);
    }

    DropSchemaImpl(Configuration configuration, Schema schema, boolean ifExists) {
        super(configuration);

        this.schema = schema;
        this.ifExists = ifExists;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final DropSchemaFinalStep cascade() {
        cascade = true;
        return this;
    }

    @Override
    public final DropSchemaFinalStep restrict() {
        cascade = false;
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx)) {
            Tools.beginTryCatch(ctx, DDLStatementType.DROP_SCHEMA);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.DROP_SCHEMA);
        }
        else {
            accept0(ctx);
        }
    }

    private void accept0(Context<?> ctx) {
        ctx.start(DROP_SCHEMA_SCHEMA)
           .visit(K_DROP_SCHEMA);

        if (ifExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(schema);

        if (cascade)
            ctx.sql(' ').visit(K_CASCADE);
        else if (REQUIRES_RESTRICT.contains(ctx.family()))
            ctx.sql(' ').visit(K_RESTRICT);

        ctx.end(DROP_SCHEMA_SCHEMA);
    }


    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
