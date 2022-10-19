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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>CREATE DOMAIN</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class CreateDomainImpl<T>
extends
    AbstractDDLQuery
implements
    QOM.CreateDomain<T>,
    CreateDomainAsStep,
    CreateDomainDefaultStep<T>,
    CreateDomainConstraintStep,
    CreateDomainFinalStep
{

    final Domain<?>                               domain;
    final boolean                                 ifNotExists;
          DataType<T>                             dataType;
          Field<T>                                default_;
          QueryPartListView<? extends Constraint> constraints;

    CreateDomainImpl(
        Configuration configuration,
        Domain<?> domain,
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
        Domain<?> domain,
        boolean ifNotExists,
        DataType<T> dataType,
        Field<T> default_,
        Collection<? extends Constraint> constraints
    ) {
        super(configuration);

        this.domain = domain;
        this.ifNotExists = ifNotExists;
        this.dataType = dataType;
        this.default_ = default_;
        this.constraints = new QueryPartList<>(constraints);
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
        this.constraints = new QueryPartList<>(constraints);
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS = SQLDialect.supportedBy(FIREBIRD, POSTGRES, YUGABYTEDB);

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx))
            tryCatch(ctx, DDLStatementType.CREATE_DOMAIN, c -> accept0(c));
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

        if (!Tools.isEmpty(constraints))
            if (ctx.family() == FIREBIRD)
                ctx.formatSeparator().visit(DSL.check(DSL.and(Tools.map(constraints, c -> ((ConstraintImpl) c).$check()))));
            else
                for (Constraint constraint : constraints)
                    ctx.formatSeparator().visit(constraint);
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Domain<?> $domain() {
        return domain;
    }

    @Override
    public final boolean $ifNotExists() {
        return ifNotExists;
    }

    @Override
    public final DataType<T> $dataType() {
        return dataType;
    }

    @Override
    public final Field<T> $default_() {
        return default_;
    }

    @Override
    public final UnmodifiableList<? extends Constraint> $constraints() {
        return QOM.unmodifiable(constraints);
    }

    @Override
    public final QOM.CreateDomain<T> $domain(Domain<?> newValue) {
        return $constructor().apply(newValue, $ifNotExists(), $dataType(), $default_(), $constraints());
    }

    @Override
    public final QOM.CreateDomain<T> $ifNotExists(boolean newValue) {
        return $constructor().apply($domain(), newValue, $dataType(), $default_(), $constraints());
    }

    @Override
    public final QOM.CreateDomain<T> $dataType(DataType<T> newValue) {
        return $constructor().apply($domain(), $ifNotExists(), newValue, $default_(), $constraints());
    }

    @Override
    public final QOM.CreateDomain<T> $default_(Field<T> newValue) {
        return $constructor().apply($domain(), $ifNotExists(), $dataType(), newValue, $constraints());
    }

    @Override
    public final QOM.CreateDomain<T> $constraints(Collection<? extends Constraint> newValue) {
        return $constructor().apply($domain(), $ifNotExists(), $dataType(), $default_(), newValue);
    }

    public final Function5<? super Domain<?>, ? super Boolean, ? super DataType<T>, ? super Field<T>, ? super Collection<? extends Constraint>, ? extends QOM.CreateDomain<T>> $constructor() {
        return (a1, a2, a3, a4, a5) -> new CreateDomainImpl(configuration(), a1, a2, a3, a4, (Collection<? extends Constraint>) a5);
    }




























}
