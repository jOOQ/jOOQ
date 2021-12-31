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
 * The <code>ALTER DOMAIN</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class AlterDomainImpl<T>
extends
    AbstractDDLQuery
implements
    QOM.AlterDomain<T>,
    AlterDomainStep<T>,
    AlterDomainDropConstraintCascadeStep,
    AlterDomainRenameConstraintStep,
    AlterDomainFinalStep
{

    final Domain<T>  domain;
    final boolean    ifExists;
          Constraint addConstraint;
          Constraint dropConstraint;
          boolean    dropConstraintIfExists;
          Domain<?>  renameTo;
          Constraint renameConstraint;
          boolean    renameConstraintIfExists;
          Field<T>   setDefault;
          boolean    dropDefault;
          boolean    setNotNull;
          boolean    dropNotNull;
          Cascade    cascade;
          Constraint renameConstraintTo;

    AlterDomainImpl(
        Configuration configuration,
        Domain<T> domain,
        boolean ifExists
    ) {
        this(
            configuration,
            domain,
            ifExists,
            null,
            null,
            false,
            null,
            null,
            false,
            null,
            false,
            false,
            false,
            null,
            null
        );
    }

    AlterDomainImpl(
        Configuration configuration,
        Domain<T> domain,
        boolean ifExists,
        Constraint addConstraint,
        Constraint dropConstraint,
        boolean dropConstraintIfExists,
        Domain<?> renameTo,
        Constraint renameConstraint,
        boolean renameConstraintIfExists,
        Field<T> setDefault,
        boolean dropDefault,
        boolean setNotNull,
        boolean dropNotNull,
        Cascade cascade,
        Constraint renameConstraintTo
    ) {
        super(configuration);

        this.domain = domain;
        this.ifExists = ifExists;
        this.addConstraint = addConstraint;
        this.dropConstraint = dropConstraint;
        this.dropConstraintIfExists = dropConstraintIfExists;
        this.renameTo = renameTo;
        this.renameConstraint = renameConstraint;
        this.renameConstraintIfExists = renameConstraintIfExists;
        this.setDefault = setDefault;
        this.dropDefault = dropDefault;
        this.setNotNull = setNotNull;
        this.dropNotNull = dropNotNull;
        this.cascade = cascade;
        this.renameConstraintTo = renameConstraintTo;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final AlterDomainImpl<T> add(Constraint addConstraint) {
        this.addConstraint = addConstraint;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> dropConstraint(String dropConstraint) {
        return dropConstraint(DSL.constraint(DSL.name(dropConstraint)));
    }

    @Override
    public final AlterDomainImpl<T> dropConstraint(Name dropConstraint) {
        return dropConstraint(DSL.constraint(dropConstraint));
    }

    @Override
    public final AlterDomainImpl<T> dropConstraint(Constraint dropConstraint) {
        this.dropConstraint = dropConstraint;
        this.dropConstraintIfExists = false;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> dropConstraintIfExists(String dropConstraint) {
        return dropConstraintIfExists(DSL.constraint(DSL.name(dropConstraint)));
    }

    @Override
    public final AlterDomainImpl<T> dropConstraintIfExists(Name dropConstraint) {
        return dropConstraintIfExists(DSL.constraint(dropConstraint));
    }

    @Override
    public final AlterDomainImpl<T> dropConstraintIfExists(Constraint dropConstraint) {
        this.dropConstraint = dropConstraint;
        this.dropConstraintIfExists = true;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> renameTo(String renameTo) {
        return renameTo(DSL.domain(DSL.name(renameTo)));
    }

    @Override
    public final AlterDomainImpl<T> renameTo(Name renameTo) {
        return renameTo(DSL.domain(renameTo));
    }

    @Override
    public final AlterDomainImpl<T> renameTo(Domain<?> renameTo) {
        this.renameTo = renameTo;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> renameConstraint(String renameConstraint) {
        return renameConstraint(DSL.constraint(DSL.name(renameConstraint)));
    }

    @Override
    public final AlterDomainImpl<T> renameConstraint(Name renameConstraint) {
        return renameConstraint(DSL.constraint(renameConstraint));
    }

    @Override
    public final AlterDomainImpl<T> renameConstraint(Constraint renameConstraint) {
        this.renameConstraint = renameConstraint;
        this.renameConstraintIfExists = false;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> renameConstraintIfExists(String renameConstraint) {
        return renameConstraintIfExists(DSL.constraint(DSL.name(renameConstraint)));
    }

    @Override
    public final AlterDomainImpl<T> renameConstraintIfExists(Name renameConstraint) {
        return renameConstraintIfExists(DSL.constraint(renameConstraint));
    }

    @Override
    public final AlterDomainImpl<T> renameConstraintIfExists(Constraint renameConstraint) {
        this.renameConstraint = renameConstraint;
        this.renameConstraintIfExists = true;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> setDefault(T setDefault) {
        return setDefault(Tools.field(setDefault));
    }

    @Override
    public final AlterDomainImpl<T> setDefault(Field<T> setDefault) {
        this.setDefault = setDefault;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> dropDefault() {
        this.dropDefault = true;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> setNotNull() {
        this.setNotNull = true;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> dropNotNull() {
        this.dropNotNull = true;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> cascade() {
        this.cascade = Cascade.CASCADE;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> restrict() {
        this.cascade = Cascade.RESTRICT;
        return this;
    }

    @Override
    public final AlterDomainImpl<T> to(String renameConstraintTo) {
        return to(DSL.constraint(DSL.name(renameConstraintTo)));
    }

    @Override
    public final AlterDomainImpl<T> to(Name renameConstraintTo) {
        return to(DSL.constraint(renameConstraintTo));
    }

    @Override
    public final AlterDomainImpl<T> to(Constraint renameConstraintTo) {
        this.renameConstraintTo = renameConstraintTo;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_RENAME_CONSTRAINT_IF_EXISTS = SQLDialect.supportedBy(FIREBIRD, POSTGRES);
    private static final Set<SQLDialect> NO_SUPPORT_DROP_CONSTRAINT_IF_EXISTS   = SQLDialect.supportedBy(FIREBIRD);

    private final boolean supportsRenameConstraintIfExists(Context<?> ctx) {
        return !NO_SUPPORT_RENAME_CONSTRAINT_IF_EXISTS.contains(ctx.dialect());
    }

    private final boolean supportsDropConstraintIfExists(Context<?> ctx) {
        return !NO_SUPPORT_DROP_CONSTRAINT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (renameConstraintIfExists && !supportsRenameConstraintIfExists(ctx) ||
            dropConstraintIfExists && !supportsDropConstraintIfExists(ctx))
            Tools.tryCatch(ctx, DDLStatementType.ALTER_DOMAIN, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        ctx.visit(K_ALTER).sql(' ').visit(K_DOMAIN).sql(' ');

        if (ifExists)
            ctx.visit(K_IF_EXISTS).sql(' ');

        ctx.visit(domain).sql(' ');

        if (addConstraint != null) {
            if (ctx.family() == FIREBIRD)
                ctx.visit(K_ADD).sql(' ').visit(DSL.check(((ConstraintImpl) addConstraint).$check()));
            else
                ctx.visit(K_ADD).sql(' ').visit(addConstraint);
        }
        else if (dropConstraint != null) {
            ctx.visit(K_DROP_CONSTRAINT);

            if (dropConstraintIfExists && supportsDropConstraintIfExists(ctx))
                ctx.sql(' ').visit(K_IF_EXISTS);

            if (ctx.family() != FIREBIRD) {
                ctx.sql(' ').data(DATA_CONSTRAINT_REFERENCE, true, c -> c.visit(dropConstraint));
                acceptCascade(ctx, cascade);
            }
        }
        else if (renameTo != null) {
            ctx.visit(ctx.family() == FIREBIRD ? K_TO : K_RENAME_TO).sql(' ').data(DATA_CONSTRAINT_REFERENCE, true, c -> c.visit(renameTo));
        }
        else if (renameConstraint != null) {
            ctx.visit(K_RENAME_CONSTRAINT).sql(' ').data(DATA_CONSTRAINT_REFERENCE, true, c -> {
                if (renameConstraintIfExists && supportsRenameConstraintIfExists(c))
                    c.visit(K_IF_EXISTS).sql(' ');

                c.visit(renameConstraint).sql(' ').visit(K_TO).sql(' ').visit(renameConstraintTo);
            });
        }
        else if (setDefault != null) {
            ctx.visit(K_SET_DEFAULT).sql(' ').visit(setDefault);
        }
        else if (dropDefault) {
            ctx.visit(K_DROP_DEFAULT);
        }
        else if (setNotNull) {
            ctx.visit(K_SET_NOT_NULL);
        }
        else if (dropNotNull) {
            ctx.visit(K_DROP_NOT_NULL);
        }
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Domain<T> $domain() {
        return domain;
    }

    @Override
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final Constraint $addConstraint() {
        return addConstraint;
    }

    @Override
    public final Constraint $dropConstraint() {
        return dropConstraint;
    }

    @Override
    public final boolean $dropConstraintIfExists() {
        return dropConstraintIfExists;
    }

    @Override
    public final Domain<?> $renameTo() {
        return renameTo;
    }

    @Override
    public final Constraint $renameConstraint() {
        return renameConstraint;
    }

    @Override
    public final boolean $renameConstraintIfExists() {
        return renameConstraintIfExists;
    }

    @Override
    public final Field<T> $setDefault() {
        return setDefault;
    }

    @Override
    public final boolean $dropDefault() {
        return dropDefault;
    }

    @Override
    public final boolean $setNotNull() {
        return setNotNull;
    }

    @Override
    public final boolean $dropNotNull() {
        return dropNotNull;
    }

    @Override
    public final Cascade $cascade() {
        return cascade;
    }

    @Override
    public final Constraint $renameConstraintTo() {
        return renameConstraintTo;
    }

    @Override
    public final QOM.AlterDomain<T> $domain(Domain<T> newValue) {
        return constructor().apply(newValue, $ifExists(), $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), $renameTo(), $renameConstraint(), $renameConstraintIfExists(), $setDefault(), $dropDefault(), $setNotNull(), $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $ifExists(boolean newValue) {
        return constructor().apply($domain(), newValue, $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), $renameTo(), $renameConstraint(), $renameConstraintIfExists(), $setDefault(), $dropDefault(), $setNotNull(), $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $addConstraint(Constraint newValue) {
        return constructor().apply($domain(), $ifExists(), newValue, $dropConstraint(), $dropConstraintIfExists(), $renameTo(), $renameConstraint(), $renameConstraintIfExists(), $setDefault(), $dropDefault(), $setNotNull(), $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $dropConstraint(Constraint newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), newValue, $dropConstraintIfExists(), $renameTo(), $renameConstraint(), $renameConstraintIfExists(), $setDefault(), $dropDefault(), $setNotNull(), $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $dropConstraintIfExists(boolean newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), $dropConstraint(), newValue, $renameTo(), $renameConstraint(), $renameConstraintIfExists(), $setDefault(), $dropDefault(), $setNotNull(), $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $renameTo(Domain<?> newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), newValue, $renameConstraint(), $renameConstraintIfExists(), $setDefault(), $dropDefault(), $setNotNull(), $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $renameConstraint(Constraint newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), $renameTo(), newValue, $renameConstraintIfExists(), $setDefault(), $dropDefault(), $setNotNull(), $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $renameConstraintIfExists(boolean newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), $renameTo(), $renameConstraint(), newValue, $setDefault(), $dropDefault(), $setNotNull(), $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $setDefault(Field<T> newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), $renameTo(), $renameConstraint(), $renameConstraintIfExists(), newValue, $dropDefault(), $setNotNull(), $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $dropDefault(boolean newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), $renameTo(), $renameConstraint(), $renameConstraintIfExists(), $setDefault(), newValue, $setNotNull(), $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $setNotNull(boolean newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), $renameTo(), $renameConstraint(), $renameConstraintIfExists(), $setDefault(), $dropDefault(), newValue, $dropNotNull(), $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $dropNotNull(boolean newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), $renameTo(), $renameConstraint(), $renameConstraintIfExists(), $setDefault(), $dropDefault(), $setNotNull(), newValue, $cascade(), $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $cascade(Cascade newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), $renameTo(), $renameConstraint(), $renameConstraintIfExists(), $setDefault(), $dropDefault(), $setNotNull(), $dropNotNull(), newValue, $renameConstraintTo());
    }

    @Override
    public final QOM.AlterDomain<T> $renameConstraintTo(Constraint newValue) {
        return constructor().apply($domain(), $ifExists(), $addConstraint(), $dropConstraint(), $dropConstraintIfExists(), $renameTo(), $renameConstraint(), $renameConstraintIfExists(), $setDefault(), $dropDefault(), $setNotNull(), $dropNotNull(), $cascade(), newValue);
    }

    public final Function14<? super Domain<T>, ? super Boolean, ? super Constraint, ? super Constraint, ? super Boolean, ? super Domain<?>, ? super Constraint, ? super Boolean, ? super Field<T>, ? super Boolean, ? super Boolean, ? super Boolean, ? super Cascade, ? super Constraint, ? extends QOM.AlterDomain<T>> constructor() {
        return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) -> new AlterDomainImpl(configuration(), a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
    }








































}
