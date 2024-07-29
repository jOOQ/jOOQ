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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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

import java.util.List;

import org.jooq.ConstraintEnforcementStep;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.QOM.UTransient;

/**
 * @author Lukas Eder
 */
class InverseReferenceImpl<R extends Record, O extends Record>
extends
    AbstractKey<R>
implements
    InverseForeignKey<R, O>,
    UTransient
{

    private final ForeignKey<O, R> foreignKey;

    InverseReferenceImpl(ForeignKey<O, R> foreignKey) {
        super(foreignKey.getKey().getTable(), foreignKey.getQualifiedName(), foreignKey.getKeyFieldsArray(), foreignKey.enforced());

        this.foreignKey = foreignKey;
    }

    @Override
    public final ForeignKey<O, R> getForeignKey() {
        return foreignKey;
    }

    @Override
    public final List<TableField<O, ?>> getForeignKeyFields() {
        return foreignKey.getFields();
    }

    @Override
    public final TableField<O, ?>[] getForeignKeyFieldsArray() {
        return foreignKey.getFieldsArray();
    }

    @Override
    ConstraintEnforcementStep constraint0() {
        return ((ReferenceImpl<O, R>) foreignKey).constraint0();
    }
}
