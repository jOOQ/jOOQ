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

import java.util.Objects;

import org.jooq.Check;
import org.jooq.Condition;
import org.jooq.Constraint;
import org.jooq.ConstraintEnforcementStep;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class CheckImpl<R extends Record> extends AbstractNamed implements Check<R> {

    final Table<R>            table;
    final Condition           condition;
    final boolean             enforced;

    CheckImpl(Table<R> table, Condition condition, boolean enforced) {
        this(table, null, condition, enforced);
    }

    CheckImpl(Table<R> table, Name name, Condition condition, boolean enforced) {
        super(name, null);

        this.table = table;
        this.condition = condition;
        this.enforced = enforced;
    }

    @Override
    public final Table<R> getTable() {
        return table;
    }

    @Override
    public final Condition condition() {
        return condition;
    }

    @Override
    public final boolean enforced() {
        return enforced;
    }

    private final Constraint enforced(ConstraintEnforcementStep key) {
        return enforced() ? key : key.notEnforced();
    }

    @Override
    public final Constraint constraint() {
        return enforced(DSL.constraint(getName()).check(condition));
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(getUnqualifiedName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(getQualifiedName(), condition, table);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        CheckImpl<?> other = (CheckImpl<?>) obj;
        return Objects.equals(getQualifiedName(), other.getQualifiedName())
            && Objects.equals(condition, other.condition)
            && Objects.equals(table, other.table);
    }

    @Override
    public String toString() {
        return constraint().toString();
    }
}
