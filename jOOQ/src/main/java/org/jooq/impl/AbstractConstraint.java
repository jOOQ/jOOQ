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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.jooq.SQLDialect.*;
import static org.jooq.impl.Keywords.K_CONSTRAINT;
import static org.jooq.impl.Keywords.K_DEFERRABLE;
import static org.jooq.impl.Keywords.K_DEFERRED;
import static org.jooq.impl.Keywords.K_DISABLE;
import static org.jooq.impl.Keywords.K_DISABLED;
import static org.jooq.impl.Keywords.K_ENABLE;
import static org.jooq.impl.Keywords.K_ENABLED;
import static org.jooq.impl.Keywords.K_ENFORCED;
import static org.jooq.impl.Keywords.K_IMMEDIATE;
import static org.jooq.impl.Keywords.K_INITIALLY;
import static org.jooq.impl.Keywords.K_NOT;
import static org.jooq.impl.Keywords.K_VALID;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_CONSTRAINT_REFERENCE;

import java.util.Set;

import org.jooq.ConstraintEnforcementStep;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
abstract class AbstractConstraint
extends
    AbstractNamed
implements
    ConstraintEnforcementStep
{
    static final Set<SQLDialect> NO_SUPPORT_NAMED          = SQLDialect.supportedBy();
    static final Set<SQLDialect> NO_SUPPORT_NAMED_PK       = SQLDialect.supportedBy(CLICKHOUSE);
    static final Set<SQLDialect> NO_SUPPORT_PK_ENFORCEMENT = SQLDialect.supportedBy(TRINO);
    static final Set<SQLDialect> NO_SUPPORT_UK_ENFORCEMENT = SQLDialect.supportedBy();
    static final Set<SQLDialect> NO_SUPPORT_FK_ENFORCEMENT = SQLDialect.supportedBy(CLICKHOUSE, IGNITE, TRINO);
    static final Set<SQLDialect> NO_SUPPORT_PK_DEFERRING   = SQLDialect.supportedBy(SQLITE, YUGABYTEDB);
    static final Set<SQLDialect> NO_SUPPORT_UK_DEFERRING   = SQLDialect.supportedBy(SQLITE);
    static final Set<SQLDialect> NO_SUPPORT_CHK_DEFERRING  = SQLDialect.supportedBy(POSTGRES, SQLITE, YUGABYTEDB);
    static final Set<SQLDialect> NO_SUPPORT_ADD_FK         = SQLDialect.supportedBy(SQLITE);

    boolean                      enforced                  = true;
    boolean                      deferrable;
    boolean                      initiallyDeferred;

    AbstractConstraint() {
        this(null);
    }

    AbstractConstraint(Name name) {
        this(name, false, false, false);
    }

    AbstractConstraint(Name name, boolean enforced, boolean deferrable, boolean initiallyDeferred) {
        super(name, null);

        this.enforced = enforced;
        this.deferrable = deferrable;
        this.initiallyDeferred = initiallyDeferred;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        boolean named = !getQualifiedName().equals(AbstractName.NO_NAME);

        if (named && TRUE.equals(ctx.data(DATA_CONSTRAINT_REFERENCE))) {
            ctx.visit(getQualifiedName());
        }
        else {
            if (named
                    && !NO_SUPPORT_NAMED.contains(ctx.dialect())
                    && (!(this instanceof PrimaryKeyConstraintImpl) || !NO_SUPPORT_NAMED_PK.contains(ctx.dialect())))
                ctx.visit(K_CONSTRAINT).sql(' ')
                   .visit(getUnqualifiedName()).sql(' ');

            accept0(ctx);









            if (!enforced)
                acceptEnforced(ctx, enforced);

            else if (this instanceof PrimaryKeyConstraintImpl && NO_SUPPORT_PK_ENFORCEMENT.contains(ctx.dialect())
                ||   this instanceof UniqueConstraintImpl && NO_SUPPORT_UK_ENFORCEMENT.contains(ctx.dialect())
                ||   this instanceof ForeignKeyConstraintImpl && NO_SUPPORT_FK_ENFORCEMENT.contains(ctx.dialect()))
                acceptEnforced(ctx, false);

            if (deferrable)
                acceptCharacteristic(ctx, deferrable, initiallyDeferred);
        }
    }

    abstract void accept0(Context<?> ctx);

    static void acceptEnforced(Context<?> ctx, boolean enforced) {
        switch (ctx.family()) {



























            default:
                if (enforced)
                    ctx.sql(' ').visit(K_ENFORCED);
                else
                    ctx.sql(' ').visit(K_NOT).sql(' ').visit(K_ENFORCED);

                break;
        }
    }

    static void acceptCharacteristic(Context<?> ctx, Boolean deferrable, Boolean initiallyDeferred) {
        if (deferrable != null) {
            switch (ctx.family()) {










                default:
                    if (deferrable)
                        ctx.sql(' ').visit(K_DEFERRABLE);
                    else
                        ctx.sql(' ').visit(K_NOT).sql(' ').visit(K_DEFERRABLE);

                    if (initiallyDeferred != null) {
                        if (initiallyDeferred)
                            ctx.sql(' ').visit(K_INITIALLY).sql(' ').visit(K_DEFERRED);
                        else
                            ctx.sql(' ').visit(K_INITIALLY).sql(' ').visit(K_IMMEDIATE);
                    }

                    break;
            }
        }
    }

    abstract boolean supported(Context<?> ctx, Table<?> onTable);

    // ------------------------------------------------------------------------
    // XXX: Constraint API
    // ------------------------------------------------------------------------

    @Override
    public final AbstractConstraint enforced() {
        this.enforced = true;
        return this;
    }

    @Override
    public final AbstractConstraint notEnforced() {
        this.enforced = false;
        return this;
    }

    @Override
    public final AbstractConstraint deferrable() {
        this.deferrable = true;
        return this;
    }

    @Override
    public final AbstractConstraint notDeferrable() {
        this.deferrable = false;
        return this;
    }

    @Override
    public final AbstractConstraint initiallyDeferred() {
        this.initiallyDeferred = true;
        return this;
    }

    @Override
    public final AbstractConstraint initiallyImmediate() {
        this.initiallyDeferred = false;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    public final boolean $enforced() {
        return enforced;
    }

    public final boolean $deferrable() {
        return deferrable;
    }

    public final boolean $initiallyDeferred() {
        return initiallyDeferred;
    }
}
