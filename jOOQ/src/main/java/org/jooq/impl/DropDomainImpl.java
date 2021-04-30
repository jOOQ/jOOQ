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
 * The <code>DROP DOMAIN</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class DropDomainImpl
extends
    AbstractDDLQuery
implements
    DropDomainCascadeStep,
    DropDomainFinalStep
{

    private final Domain<?> domain;
    private final boolean   dropDomainIfExists;
    private       Cascade   cascade;

    DropDomainImpl(
        Configuration configuration,
        Domain<?> domain,
        boolean dropDomainIfExists
    ) {
        this(
            configuration,
            domain,
            dropDomainIfExists,
            null
        );
    }

    DropDomainImpl(
        Configuration configuration,
        Domain<?> domain,
        boolean dropDomainIfExists,
        Cascade cascade
    ) {
        super(configuration);

        this.domain = domain;
        this.dropDomainIfExists = dropDomainIfExists;
        this.cascade = cascade;
    }

    final Domain<?> $domain()             { return domain; }
    final boolean   $dropDomainIfExists() { return dropDomainIfExists; }
    final Cascade   $cascade()            { return cascade; }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final DropDomainImpl cascade() {
        this.cascade = Cascade.CASCADE;
        return this;
    }

    @Override
    public final DropDomainImpl restrict() {
        this.cascade = Cascade.RESTRICT;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supportedBy(FIREBIRD);

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (dropDomainIfExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.DROP_DOMAIN, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(K_DROP).sql(' ').visit(K_DOMAIN);
                break;
        }

        if (dropDomainIfExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(domain);
        acceptCascade(ctx, cascade);
    }


}
