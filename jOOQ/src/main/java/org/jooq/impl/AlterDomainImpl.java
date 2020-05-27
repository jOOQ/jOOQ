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
 * The <code>ALTER DOMAIN IF EXISTS</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class AlterDomainImpl<T>
extends
    AbstractRowCountQuery
implements
    AlterDomainStep<T>,
    AlterDomainDropConstraintCascadeStep,
    AlterDomainRenameConstraintStep,
    AlterDomainFinalStep
{
    
    private static final long serialVersionUID = 1L;

    private final Domain<T> domain;
    private final boolean ifExists;
    private final Constraint addConstraint;
    private final boolean dropDefault;
    private final boolean setNotNull;
    private final boolean dropNotNull;
    private final Constraint dropConstraint;
    private final boolean dropConstraintIfExists;
    private final Domain<?> renameTo;
    private final Constraint renameConstraint;
    private final boolean renameConstraintIfExists;
    private final Field<T> setDefault;
    private final Boolean cascade;
    private final Constraint renameConstraintTo;
    
    AlterDomainImpl(
        Configuration configuration,
        Domain domain,
        boolean ifExists
    ) {
        this(
            configuration,
            domain,
            ifExists,
            null,
            false,
            false,
            false,
            null,
            false,
            null,
            null,
            false,
            null,
            null,
            null
        );
    }
    
    AlterDomainImpl(
        Configuration configuration,
        Domain domain,
        boolean ifExists,
        Constraint addConstraint,
        boolean dropDefault,
        boolean setNotNull,
        boolean dropNotNull,
        Constraint dropConstraint,
        boolean dropConstraintIfExists,
        Domain renameTo,
        Constraint renameConstraint,
        boolean renameConstraintIfExists,
        Field setDefault,
        Boolean cascade,
        Constraint renameConstraintTo
    ) {
        super(configuration);

        this.domain = domain;
        this.ifExists = ifExists;
        this.addConstraint = addConstraint;
        this.dropDefault = dropDefault;
        this.setNotNull = setNotNull;
        this.dropNotNull = dropNotNull;
        this.dropConstraint = dropConstraint;
        this.dropConstraintIfExists = dropConstraintIfExists;
        this.renameTo = renameTo;
        this.renameConstraint = renameConstraint;
        this.renameConstraintIfExists = renameConstraintIfExists;
        this.setDefault = setDefault;
        this.cascade = cascade;
        this.renameConstraintTo = renameConstraintTo;
    }

    // -------------------------------------------------------------------------
    // XXX DSL API
    // -------------------------------------------------------------------------

    @Override
    public final AlterDomainImpl<T> add(Constraint addConstraint) {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            addConstraint,
            this.dropDefault,
            this.setNotNull,
            this.dropNotNull,
            this.dropConstraint,
            this.dropConstraintIfExists,
            this.renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            this.setDefault,
            this.cascade,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> dropDefault() {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            true,
            this.setNotNull,
            this.dropNotNull,
            this.dropConstraint,
            this.dropConstraintIfExists,
            this.renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            this.setDefault,
            this.cascade,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> setNotNull() {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            true,
            this.dropNotNull,
            this.dropConstraint,
            this.dropConstraintIfExists,
            this.renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            this.setDefault,
            this.cascade,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> dropNotNull() {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            this.setNotNull,
            true,
            this.dropConstraint,
            this.dropConstraintIfExists,
            this.renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            this.setDefault,
            this.cascade,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> dropConstraint(String dropConstraint) {
        return dropConstraint(DSL.constraint(dropConstraint));
    }

    @Override
    public final AlterDomainImpl<T> dropConstraint(Name dropConstraint) {
        return dropConstraint(DSL.constraint(dropConstraint));
    }

    @Override
    public final AlterDomainImpl<T> dropConstraint(Constraint dropConstraint) {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            this.setNotNull,
            this.dropNotNull,
            dropConstraint,
            false,
            this.renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            this.setDefault,
            this.cascade,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> dropConstraintIfExists(String dropConstraint) {
        return dropConstraintIfExists(DSL.constraint(dropConstraint));
    }

    @Override
    public final AlterDomainImpl<T> dropConstraintIfExists(Name dropConstraint) {
        return dropConstraintIfExists(DSL.constraint(dropConstraint));
    }

    @Override
    public final AlterDomainImpl<T> dropConstraintIfExists(Constraint dropConstraint) {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            this.setNotNull,
            this.dropNotNull,
            dropConstraint,
            true,
            this.renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            this.setDefault,
            this.cascade,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> renameTo(String renameTo) {
        return renameTo(DSL.domain(renameTo));
    }

    @Override
    public final AlterDomainImpl<T> renameTo(Name renameTo) {
        return renameTo(DSL.domain(renameTo));
    }

    @Override
    public final AlterDomainImpl<T> renameTo(Domain<?> renameTo) {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            this.setNotNull,
            this.dropNotNull,
            this.dropConstraint,
            this.dropConstraintIfExists,
            renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            this.setDefault,
            this.cascade,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> renameConstraint(String renameConstraint) {
        return renameConstraint(DSL.constraint(renameConstraint));
    }

    @Override
    public final AlterDomainImpl<T> renameConstraint(Name renameConstraint) {
        return renameConstraint(DSL.constraint(renameConstraint));
    }

    @Override
    public final AlterDomainImpl<T> renameConstraint(Constraint renameConstraint) {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            this.setNotNull,
            this.dropNotNull,
            this.dropConstraint,
            this.dropConstraintIfExists,
            this.renameTo,
            renameConstraint,
            false,
            this.setDefault,
            this.cascade,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> renameConstraintIfExists(String renameConstraint) {
        return renameConstraintIfExists(DSL.constraint(renameConstraint));
    }

    @Override
    public final AlterDomainImpl<T> renameConstraintIfExists(Name renameConstraint) {
        return renameConstraintIfExists(DSL.constraint(renameConstraint));
    }

    @Override
    public final AlterDomainImpl<T> renameConstraintIfExists(Constraint renameConstraint) {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            this.setNotNull,
            this.dropNotNull,
            this.dropConstraint,
            this.dropConstraintIfExists,
            this.renameTo,
            renameConstraint,
            true,
            this.setDefault,
            this.cascade,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> setDefault(T setDefault) {
        return setDefault(Tools.field(setDefault));
    }

    @Override
    public final AlterDomainImpl<T> setDefault(Field<T> setDefault) {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            this.setNotNull,
            this.dropNotNull,
            this.dropConstraint,
            this.dropConstraintIfExists,
            this.renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            setDefault,
            this.cascade,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> cascade() {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            this.setNotNull,
            this.dropNotNull,
            this.dropConstraint,
            this.dropConstraintIfExists,
            this.renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            this.setDefault,
            true,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> restrict() {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            this.setNotNull,
            this.dropNotNull,
            this.dropConstraint,
            this.dropConstraintIfExists,
            this.renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            this.setDefault,
            false,
            this.renameConstraintTo
        );
    }

    @Override
    public final AlterDomainImpl<T> to(String renameConstraintTo) {
        return to(DSL.constraint(renameConstraintTo));
    }

    @Override
    public final AlterDomainImpl<T> to(Name renameConstraintTo) {
        return to(DSL.constraint(renameConstraintTo));
    }

    @Override
    public final AlterDomainImpl<T> to(Constraint renameConstraintTo) {
        return new AlterDomainImpl<>(
            configuration(),
            this.domain,
            this.ifExists,
            this.addConstraint,
            this.dropDefault,
            this.setNotNull,
            this.dropNotNull,
            this.dropConstraint,
            this.dropConstraintIfExists,
            this.renameTo,
            this.renameConstraint,
            this.renameConstraintIfExists,
            this.setDefault,
            this.cascade,
            renameConstraintTo
        );
    }

    // -------------------------------------------------------------------------
    // XXX QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_RENAME_CONSTRAINT_IF_EXISTS = SQLDialect.supportedBy(POSTGRES);

    private final boolean supportsRenameConstraintIfExists(Context<?> ctx) {
        return !NO_SUPPORT_RENAME_CONSTRAINT_IF_EXISTS.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (renameConstraintIfExists && !supportsRenameConstraintIfExists(ctx)) {
            Tools.beginTryCatch(ctx, DDLStatementType.ALTER_DOMAIN);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.ALTER_DOMAIN);
        }
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        Object previous = ctx.data(DATA_CONSTRAINT_REFERENCE);

        ctx.visit(K_ALTER).sql(' ').visit(K_DOMAIN).sql(' ');

        if (ifExists)
            ctx.visit(K_IF_EXISTS).sql(' ');

        ctx.visit(domain).sql(' ');

        if (addConstraint != null) {
            ctx.visit(K_ADD).sql(' ').visit(addConstraint);
        }
        else if (dropConstraint != null) {
            ctx.visit(K_DROP_CONSTRAINT).sql(' ');

            if (dropConstraintIfExists)
                ctx.visit(K_IF_EXISTS).sql(' ');

            ctx.data(DATA_CONSTRAINT_REFERENCE, true);
            ctx.visit(dropConstraint);
            ctx.data(DATA_CONSTRAINT_REFERENCE, previous);

            if (cascade != null)
                if (cascade)
                    ctx.sql(' ').visit(K_CASCADE);
                else
                    ctx.sql(' ').visit(K_RESTRICT);
        }
        else if (renameTo != null) {
            ctx.data(DATA_CONSTRAINT_REFERENCE, true);
            ctx.visit(K_RENAME_TO).sql(' ').visit(renameTo);
            ctx.data(DATA_CONSTRAINT_REFERENCE, previous);
        }
        else if (renameConstraint != null) {
            ctx.data(DATA_CONSTRAINT_REFERENCE, true);
            ctx.visit(K_RENAME_CONSTRAINT).sql(' ');

            if (renameConstraintIfExists && supportsRenameConstraintIfExists(ctx))
                ctx.visit(K_IF_EXISTS).sql(' ');

            ctx.visit(renameConstraint).sql(' ').visit(K_TO).sql(' ').visit(renameConstraintTo);
            ctx.data(DATA_CONSTRAINT_REFERENCE, previous);
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


}
