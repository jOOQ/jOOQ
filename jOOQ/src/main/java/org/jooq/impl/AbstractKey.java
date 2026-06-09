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

import static org.jooq.impl.Tools.anyMatch;

import java.util.Arrays;
import java.util.List;

import org.jooq.Constraint;
import org.jooq.ConstraintEnforcementStep;
import org.jooq.Context;
import org.jooq.ForeignKey;
import org.jooq.Key;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.QOM.ConstraintCharacteristic;
import org.jooq.impl.QOM.ConstraintCheckTime;
import org.jooq.impl.QOM.UEmpty;

/**
 * Common base class for <code>Key</code>'s
 *
 * @author Lukas Eder
 */
abstract class AbstractKey<R extends Record> extends AbstractNamed implements Key<R>, UEmpty {

    final Table<R>                 table;
    final TableField<R, ?>[]       fields;
    final boolean                  enforced;
    final ConstraintCharacteristic characteristic;
    final ConstraintCheckTime      checkTime;

    AbstractKey(
        Table<R> table,
        TableField<R, ?>[] fields,
        boolean enforced,
        ConstraintCharacteristic characteristic,
        ConstraintCheckTime checkTime
    ) {
        this(table, null, fields, enforced, characteristic, checkTime);
    }

    AbstractKey(
        Table<R> table,
        Name name,
        TableField<R, ?>[] fields,
        boolean enforced,
        ConstraintCharacteristic characteristic,
        ConstraintCheckTime checkTime
    ) {
        super(qualify(table, name), null);

        this.table = table;
        this.fields = fields;
        this.enforced = enforced;
        this.characteristic = characteristic;
        this.checkTime = checkTime;
    }

    @Override
    public final Table<R> getTable() {
        return table;
    }

    @Override
    public final List<TableField<R, ?>> getFields() {
        return Arrays.asList(fields);
    }

    @Override
    public final TableField<R, ?>[] getFieldsArray() {
        return fields;
    }

    @Override
    public final boolean nullable() {
        return anyMatch(fields, f -> f.getDataType().nullable());
    }

    @Override
    public final boolean enforced() {
        return enforced;
    }

    @Override
    public final ConstraintCharacteristic characteristic() {
        return characteristic;
    }

    @Override
    public final ConstraintCheckTime checkTime() {
        return checkTime;
    }

    private final Constraint enforced(ConstraintEnforcementStep key) {
        if (!enforced())
            key = key.notEnforced();

        if (characteristic() == ConstraintCharacteristic.DEFERRABLE)
            key = key.deferrable();
        else if (characteristic() == ConstraintCharacteristic.NOT_DEFERRABLE)
            key = key.notDeferrable();

        if (checkTime() == ConstraintCheckTime.INITIALLY_DEFERRED)
            key = key.initiallyDeferred();
        else if (checkTime() == ConstraintCheckTime.INITIALLY_IMMEDIATE)
            key = key.initiallyImmediate();

        return key;
    }

    @Override
    public final Constraint constraint() {
        return enforced(constraint0());
    }

    abstract ConstraintEnforcementStep constraint0();

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(getUnqualifiedName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getQualifiedName().hashCode();
        result = prime * result + ((table == null) ? 0 : table.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AbstractKey))
            return false;
        if (this instanceof ForeignKey) {
            if (!(obj instanceof ForeignKey))
                return false;
        }
        else if (this instanceof UniqueKey) {
            if (!(obj instanceof UniqueKey))
                return false;
        }
        AbstractKey<?> other = (AbstractKey<?>) obj;
        if (!getQualifiedName().equals(other.getQualifiedName()))
            return false;
        if (table == null) {
            if (other.table != null)
                return false;
        }
        else if (!table.equals(other.table))
            return false;
        else if (!Arrays.equals(fields, other.fields))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return constraint().toString();
    }
}
