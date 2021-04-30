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
 * The <code>CREATE DOMAIN</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class CreateDomainImpl<T>
extends
    AbstractDDLQuery
implements
    CreateDomainAsStep,
    CreateDomainDefaultStep<T>,
    CreateDomainConstraintStep,
    CreateDomainFinalStep
{

    private final Domain<?>                        domain;
    private final boolean                          createDomainIfNotExists;
    private       DataType<T>                      dataType;
    private       Field<T>                         default_;
    private       Collection<? extends Constraint> constraints;

    CreateDomainImpl(
        Configuration configuration,
        Domain<?> domain,
        boolean createDomainIfNotExists
    ) {
        this(
            configuration,
            domain,
            createDomainIfNotExists,
            null,
            null,
            null
        );
    }

    CreateDomainImpl(
        Configuration configuration,
        Domain<?> domain,
        boolean createDomainIfNotExists,
        DataType<T> dataType,
        Field<T> default_,
        Collection<? extends Constraint> constraints
    ) {
        super(configuration);

        this.domain = domain;
        this.createDomainIfNotExists = createDomainIfNotExists;
        this.dataType = dataType;
        this.default_ = default_;
        this.constraints = constraints;
    }

    final Domain<?>                        $domain()                  { return domain; }
    final boolean                          $createDomainIfNotExists() { return createDomainIfNotExists; }
    final DataType<T>                      $dataType()                { return dataType; }
    final Field<T>                         $default_()                { return default_; }
    final Collection<? extends Constraint> $constraints()             { return constraints; }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final <T> CreateDomainImpl<T> as(Class<T> dataType) {
        return as(DefaultDataType.getDataType(null, dataType));
    }

    @Override
    public final <T> CreateDomainImpl<T> as(DataType<T> dataType) {
        this.dataType = (DataType) dataType;
        return (CreateDomainImpl) this;
    }

    @Override
    public final CreateDomainImpl<T> default_(T default_) {
        return default_(Tools.field(default_));
    }

    @Override
    public final CreateDomainImpl<T> default_(Field<T> default_) {
        this.default_ = default_;
        return this;
    }

    @Override
    public final CreateDomainImpl<T> constraints(Constraint... constraints) {
        return constraints(Arrays.asList(constraints));
    }

    @Override
    public final CreateDomainImpl<T> constraints(Collection<? extends Constraint> constraints) {
        this.constraints = constraints;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS = SQLDialect.supportedBy(FIREBIRD, POSTGRES);

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (createDomainIfNotExists && !supportsIfNotExists(ctx))
            tryCatch(ctx, DDLStatementType.CREATE_DOMAIN, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.visit(K_CREATE).sql(' ').visit(K_DOMAIN);

                if (createDomainIfNotExists && supportsIfNotExists(ctx))
                    ctx.sql(' ').visit(K_IF_NOT_EXISTS);

                ctx.sql(' ').visit(domain).sql(' ').visit(K_AS).sql(' ');
        }

        Tools.toSQLDDLTypeDeclaration(ctx, dataType);
        if (default_ != null)
            ctx.formatSeparator().visit(K_DEFAULT).sql(' ').visit(default_);

        if (constraints != null)
            if (ctx.family() == FIREBIRD)
                ctx.formatSeparator().visit(DSL.check(DSL.and(Tools.map(constraints, c -> ((ConstraintImpl) c).$check()))));
            else
                for (Constraint constraint : constraints)
                    ctx.formatSeparator().visit(constraint);
    }


}
