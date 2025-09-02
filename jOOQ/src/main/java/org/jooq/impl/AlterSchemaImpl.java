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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>ALTER SCHEMA</code> statement.
 */
@SuppressWarnings({ "hiding", "unused" })
final class AlterSchemaImpl
extends
    AbstractDDLQuery
implements
    QOM.AlterSchema,
    AlterSchemaStep,
    AlterSchemaFinalStep
{

    final Schema  schema;
    final boolean ifExists;
          Schema  renameTo;

    AlterSchemaImpl(
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

    AlterSchemaImpl(
        Configuration configuration,
        Schema schema,
        boolean ifExists,
        Schema renameTo
    ) {
        super(configuration);

        this.schema = schema;
        this.ifExists = ifExists;
        this.renameTo = renameTo;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final AlterSchemaImpl renameTo(String renameTo) {
        return renameTo(DSL.schema(DSL.name(renameTo)));
    }

    @Override
    public final AlterSchemaImpl renameTo(Name renameTo) {
        return renameTo(DSL.schema(renameTo));
    }

    @Override
    public final AlterSchemaImpl renameTo(Schema renameTo) {
        this.renameTo = renameTo;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[] CLAUSES                 = { Clause.ALTER_SCHEMA };
    static final Set<SQLDialect>  SUPPORT_RENAME_DATABASE = SQLDialect.supportedBy(CLICKHOUSE);












    @Override
    public final void accept(Context<?> ctx) {





        accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        ctx.start(Clause.ALTER_SCHEMA_SCHEMA);

        boolean supportRename = false;

        if (supportRename = SUPPORT_RENAME_DATABASE.contains(ctx.dialect()))
            ctx.visit(K_RENAME).sql(' ').visit(K_DATABASE);




        else
            ctx.visit(K_ALTER_SCHEMA);

        if (ifExists)



                ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(schema)
           .end(Clause.ALTER_SCHEMA_SCHEMA).sql(' ');

        if (renameTo != null)
            ctx.start(Clause.ALTER_SCHEMA_RENAME)
               .visit(supportRename ? K_TO : K_RENAME_TO).sql(' ')
               .qualify(false, c -> c.visit(renameTo))
               .end(Clause.ALTER_SCHEMA_RENAME);
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
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final Schema $renameTo() {
        return renameTo;
    }

    @Override
    public final QOM.AlterSchema $schema(Schema newValue) {
        return $constructor().apply(newValue, $ifExists(), $renameTo());
    }

    @Override
    public final QOM.AlterSchema $ifExists(boolean newValue) {
        return $constructor().apply($schema(), newValue, $renameTo());
    }

    @Override
    public final QOM.AlterSchema $renameTo(Schema newValue) {
        return $constructor().apply($schema(), $ifExists(), newValue);
    }

    public final Function3<? super Schema, ? super Boolean, ? super Schema, ? extends QOM.AlterSchema> $constructor() {
        return (a1, a2, a3) -> new AlterSchemaImpl(configuration(), a1, a2, a3);
    }
























}
