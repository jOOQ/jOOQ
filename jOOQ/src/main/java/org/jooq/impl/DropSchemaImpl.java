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

import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.impl.*;

import java.util.*;

/**
 * The <code>DROP SCHEMA IF EXISTS</code> statement.
 */
@SuppressWarnings({ "unused" })
final class DropSchemaImpl
extends
    AbstractRowCountQuery
implements
    DropSchemaStep,
    DropSchemaFinalStep
{
    
    private static final long serialVersionUID = 1L;

    private final Schema  schema;
    private final boolean ifExists;
    private       Boolean cascade;
    
    DropSchemaImpl(
        Configuration configuration,
        Schema schema,
        boolean ifExists
    ) {
        this(
            configuration,
            schema,
            ifExists,
            null
        );
    }
    
    DropSchemaImpl(
        Configuration configuration,
        Schema schema,
        boolean ifExists,
        Boolean cascade
    ) {
        super(configuration);

        this.schema = schema;
        this.ifExists = ifExists;
        this.cascade = cascade;
    }

    final Schema  $schema()   { return schema; }
    final boolean $ifExists() { return ifExists; }
    final Boolean $cascade()  { return cascade; }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------
    
    @Override
    public final DropSchemaImpl cascade() {
        this.cascade = true;
        return this;
    }

    @Override
    public final DropSchemaImpl restrict() {
        this.cascade = false;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]        CLAUSES                    = { Clause.DROP_SCHEMA };
    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS       = SQLDialect.supportedBy(DERBY, FIREBIRD);
    private static final Set<SQLDialect> REQUIRES_RESTRICT          = SQLDialect.supportedBy(DERBY);






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
        ctx.start(Clause.DROP_SCHEMA_SCHEMA)
           .visit(K_DROP);






            ctx.sql(' ').visit(K_SCHEMA);

        if (ifExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(schema);

        if (cascade)
            ctx.sql(' ').visit(K_CASCADE);
        else if (REQUIRES_RESTRICT.contains(ctx.family()))
            ctx.sql(' ').visit(K_RESTRICT);

        ctx.end(Clause.DROP_SCHEMA_SCHEMA);
    }


    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }


}
