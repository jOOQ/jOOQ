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

// ...
import static org.jooq.SQLDialect.IGNITE;
import static org.jooq.SQLDialect.TRINO;
import static org.jooq.impl.Keywords.K_CHECK;

import java.util.Set;

import org.jooq.Condition;
import org.jooq.ConstraintEnforcementStep;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.QueryPart;
// ...
import org.jooq.SQLDialect;
import org.jooq.Table;
// ...
import org.jooq.impl.QOM.Check;

/**
 * @author Lukas Eder
 */
final class CheckConstraintImpl
extends
    AbstractConstraint
implements
    QOM.Check
{
    static final Set<SQLDialect> NO_SUPPORT_CHECK = SQLDialect.supportedBy(IGNITE, TRINO);

    final Condition              condition;

    CheckConstraintImpl(Name name, Condition condition) {
        this(name, condition, true);
    }

    private CheckConstraintImpl(Name name, Condition condition, boolean enforced) {
        super(name, enforced);

        this.condition = condition;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept0(Context<?> ctx) {
        ctx.visit(K_CHECK)
           .sql(" (").qualify(false, c -> c.visit(condition)).sql(')');
    }

    @Override
    final boolean supported(Context<?> ctx, Table<?> onTable) {
        return !NO_SUPPORT_CHECK.contains(ctx.dialect());
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Condition $condition() {
        return condition;
    }

    @Override
    public final Check $condition(Condition newCondition) {
        return new CheckConstraintImpl($name(), newCondition, $enforced());
    }

    @Override
    public final Check $name(Name newName) {
        return new CheckConstraintImpl(newName, $condition(), $enforced());
    }

    @Override
    public final Check $enforced(boolean newEnforced) {
        return new CheckConstraintImpl($name(), $condition(), newEnforced);
    }


















}
