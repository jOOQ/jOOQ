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
 * The <code>CREATE DOMAIN IF NOT EXISTS</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class CreateDomainImpl<T>
extends
    AbstractRowCountQuery
implements
    CreateDomainAsStep,
    CreateDomainDefaultStep<T>,
    CreateDomainConstraintStep,
    CreateDomainFinalStep
{
    
    private static final long serialVersionUID = 1L;

    private final Domain<?>                        domain;
    private final boolean                          ifNotExists;
    private final DataType<T>                      dataType;
    private final Field<T>                         default_;
    private final Collection<? extends Constraint> constraints;
    
    CreateDomainImpl(
        Configuration configuration,
        Domain domain,
        boolean ifNotExists
    ) {
        this(
            configuration,
            domain,
            ifNotExists,
            null,
            null,
            null
        );
    }
    
    CreateDomainImpl(
        Configuration configuration,
        Domain domain,
        boolean ifNotExists,
        DataType dataType,
        Field default_,
        Collection constraints
    ) {
        super(configuration);

        this.domain = domain;
        this.ifNotExists = ifNotExists;
        this.dataType = dataType;
        this.default_ = default_;
        this.constraints = constraints;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------
    
    @Override
    public final <T> CreateDomainImpl<T> as(Class<T> dataType) {
        return as(DefaultDataType.getDataType(null, dataType));
    }

    @Override
    public final <T> CreateDomainImpl<T> as(DataType<T> dataType) {
        return new CreateDomainImpl<>(
            configuration(),
            this.domain,
            this.ifNotExists,
            dataType,
            this.default_,
            this.constraints
        );
    }

    @Override
    public final CreateDomainImpl<T> default_(T default_) {
        return default_(Tools.field(default_));
    }

    @Override
    public final CreateDomainImpl<T> default_(Field<T> default_) {
        return new CreateDomainImpl<>(
            configuration(),
            this.domain,
            this.ifNotExists,
            this.dataType,
            default_,
            this.constraints
        );
    }

    @Override
    public final CreateDomainImpl<T> constraints(Constraint... constraints) {
        return constraints(Arrays.asList(constraints));
    }

    @Override
    public final CreateDomainImpl<T> constraints(Collection<? extends Constraint> constraints) {
        return new CreateDomainImpl<>(
            configuration(),
            this.domain,
            this.ifNotExists,
            this.dataType,
            this.default_,
            constraints
        );
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS = SQLDialect.supportedBy(POSTGRES);

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.beginTryCatch(ctx, DDLStatementType.CREATE_DOMAIN);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.CREATE_DOMAIN);
        }
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(K_CREATE).sql(' ').visit(K_DOMAIN);

                if (ifNotExists && supportsIfNotExists(ctx))
                    ctx.sql(' ').visit(K_IF_NOT_EXISTS);

                ctx.sql(' ').visit(domain).sql(' ').visit(K_AS).sql(' ');
        }

        Tools.toSQLDDLTypeDeclaration(ctx, dataType);
        if (default_ != null)
            ctx.formatSeparator().visit(K_DEFAULT).sql(' ').visit(default_);

        if (constraints != null)
            for (Constraint constraint : constraints)
                ctx.formatSeparator().visit(constraint);
    }


}
