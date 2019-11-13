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

import static org.jooq.Clause.CREATE_SCHEMA;
import static org.jooq.Clause.CREATE_SCHEMA_NAME;
// ...
// ...
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
// ...
// ...
// ...
// ...
import static org.jooq.impl.Keywords.K_CREATE;
import static org.jooq.impl.Keywords.K_DATABASE;
import static org.jooq.impl.Keywords.K_EXEC;
import static org.jooq.impl.Keywords.K_IF_NOT_EXISTS;
import static org.jooq.impl.Keywords.K_SCHEMA;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateSchemaFinalStep;
// ...
import org.jooq.SQLDialect;
import org.jooq.Schema;

/**
 * @author Lukas Eder
 */
final class CreateSchemaImpl extends AbstractRowCountQuery implements

    // Cascading interface implementations for CREATE SCHEMA behaviour
    CreateSchemaFinalStep {


    /**
     * Generated UID
     */
    private static final long                serialVersionUID           = 8904572826501186329L;
    private static final Clause[]            CLAUSES                    = { CREATE_SCHEMA };
    private static final Set<SQLDialect>     NO_SUPPORT_IF_NOT_EXISTS   = SQLDialect.supportedBy(DERBY, FIREBIRD);






    private final Schema                     schema;
    private final boolean                    ifNotExists;

    CreateSchemaImpl(Configuration configuration, Schema schema, boolean ifNotExists) {
        super(configuration);

        this.schema = schema;
        this.ifNotExists = ifNotExists;
    }

    final Schema  $schema()      { return schema; }
    final boolean $ifNotExists() { return ifNotExists; }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.beginTryCatch(ctx, DDLStatementType.CREATE_SCHEMA);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.CREATE_SCHEMA);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {













            accept1(ctx);
    }

    private final void accept1(Context<?> ctx) {
        ctx.start(CREATE_SCHEMA_NAME)
           .visit(K_CREATE);






            ctx.sql(' ').visit(K_SCHEMA);

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.sql(' ').visit(K_IF_NOT_EXISTS);

        ctx.sql(' ').visit(schema)
           .end(CREATE_SCHEMA_NAME);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
