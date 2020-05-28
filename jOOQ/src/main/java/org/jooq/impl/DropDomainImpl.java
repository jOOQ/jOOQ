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
 * The <code>DROP DOMAIN IF EXISTS</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class DropDomainImpl
extends
    AbstractRowCountQuery
implements
    DropDomainCascadeStep,
    DropDomainFinalStep
{

    private static final long serialVersionUID = 1L;

    private final Domain<?> domain;
    private final boolean   dropDomainIfExists;
    private       Boolean   cascade;
    
    DropDomainImpl(
        Configuration configuration,
        Domain domain,
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
        Domain domain,
        boolean dropDomainIfExists,
        Boolean cascade
    ) {
        super(configuration);

        this.domain = domain;
        this.dropDomainIfExists = dropDomainIfExists;
        this.cascade = cascade;
    }

    final Domain<?> $domain()             { return domain; }
    final boolean   $dropDomainIfExists() { return dropDomainIfExists; }
    final Boolean   $cascade()            { return cascade; }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------
    
    @Override
    public final DropDomainImpl cascade() {
        this.cascade = true;
        return this;
    }

    @Override
    public final DropDomainImpl restrict() {
        this.cascade = false;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(K_DROP).sql(' ').visit(K_DOMAIN);
                break;
        }

        if (dropDomainIfExists)
            ctx.sql(' ').visit(K_IF_EXISTS);

        ctx.sql(' ').visit(domain);

        if (cascade != null)
            if (cascade)
                ctx.sql(' ').visit(K_CASCADE);
            else
                ctx.sql(' ').visit(K_RESTRICT);
    }


}
