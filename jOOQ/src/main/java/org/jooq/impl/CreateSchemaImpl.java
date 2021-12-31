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
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>CREATE SCHEMA</code> statement.
 */
@SuppressWarnings({ "unused" })
final class CreateSchemaImpl
extends
    AbstractDDLQuery
implements
    QOM.CreateSchema,
    CreateSchemaFinalStep
{

    final Schema  schema;
    final boolean ifNotExists;

    CreateSchemaImpl(
        Configuration configuration,
        Schema schema,
        boolean ifNotExists
    ) {
        super(configuration);

        this.schema = schema;
        this.ifNotExists = ifNotExists;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]            CLAUSES                    = { Clause.CREATE_SCHEMA };
    private static final Set<SQLDialect>     NO_SUPPORT_IF_NOT_EXISTS   = SQLDialect.supportedBy(DERBY, FIREBIRD);






    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {














        accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx))
            tryCatch(ctx, DDLStatementType.CREATE_SCHEMA, c -> accept1(c));
        else
            accept1(ctx);
    }

    private final void accept1(Context<?> ctx) {













            accept2(ctx);
    }

    private final void accept2(Context<?> ctx) {
        ctx.start(Clause.CREATE_SCHEMA_NAME)
           .visit(K_CREATE);






            ctx.sql(' ').visit(K_SCHEMA);

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.sql(' ').visit(K_IF_NOT_EXISTS);

        ctx.sql(' ').visit(schema)
           .end(Clause.CREATE_SCHEMA_NAME);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Schema $schema() {
        return schema;
    }

    @Override
    public final boolean $ifNotExists() {
        return ifNotExists;
    }

    @Override
    public final QOM.CreateSchema $schema(Schema newValue) {
        return constructor().apply(newValue, $ifNotExists());
    }

    @Override
    public final QOM.CreateSchema $ifNotExists(boolean newValue) {
        return constructor().apply($schema(), newValue);
    }

    public final Function2<? super Schema, ? super Boolean, ? extends QOM.CreateSchema> constructor() {
        return (a1, a2) -> new CreateSchemaImpl(configuration(), a1, a2);
    }






















}
