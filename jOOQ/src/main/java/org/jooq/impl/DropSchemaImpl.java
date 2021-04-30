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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>DROP SCHEMA</code> statement.
 */
@SuppressWarnings({ "unused" })
final class DropSchemaImpl
extends
    AbstractDDLQuery
implements
    DropSchemaStep,
    DropSchemaFinalStep
{

    private final Schema  schema;
    private final boolean dropSchemaIfExists;
    private       Cascade cascade;

    DropSchemaImpl(
        Configuration configuration,
        Schema schema,
        boolean dropSchemaIfExists
    ) {
        this(
            configuration,
            schema,
            dropSchemaIfExists,
            null
        );
    }

    DropSchemaImpl(
        Configuration configuration,
        Schema schema,
        boolean dropSchemaIfExists,
        Cascade cascade
    ) {
        super(configuration);

        this.schema = schema;
        this.dropSchemaIfExists = dropSchemaIfExists;
        this.cascade = cascade;
    }

    final Schema  $schema()             { return schema; }
    final boolean $dropSchemaIfExists() { return dropSchemaIfExists; }
    final Cascade $cascade()            { return cascade; }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final DropSchemaImpl cascade() {
        this.cascade = Cascade.CASCADE;
        return this;
    }

    @Override
    public final DropSchemaImpl restrict() {
        this.cascade = Cascade.RESTRICT;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]        CLAUSES                    = { Clause.DROP_SCHEMA };
    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS       = SQLDialect.supportedBy(DERBY, FIREBIRD);
    private static final Set<SQLDialect> REQUIRES_RESTRICT          = SQLDialect.supportedBy(DERBY);






    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {














        accept0(ctx);
    }

    private void accept0(Context<?> ctx) {
        if (dropSchemaIfExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.DROP_SCHEMA, c -> accept1(c));
        else
            accept1(ctx);
    }

    private void accept1(Context<?> ctx) {
        ctx.start(Clause.DROP_SCHEMA_SCHEMA)
           .visit(K_DROP);






            ctx.sql(' ').visit(K_SCHEMA);

        if (dropSchemaIfExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(schema);

        if (cascade == Cascade.CASCADE)
            ctx.sql(' ').visit(K_CASCADE);
        else if (cascade == Cascade.RESTRICT || REQUIRES_RESTRICT.contains(ctx.dialect()))
            ctx.sql(' ').visit(K_RESTRICT);

        ctx.end(Clause.DROP_SCHEMA_SCHEMA);
    }


    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }


}
