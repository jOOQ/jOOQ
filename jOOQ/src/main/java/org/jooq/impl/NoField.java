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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.Names.N_NULL;
import static org.jooq.impl.SQLDataType.OTHER;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.jooq.impl.QOM.NullOrdering;
import org.jooq.impl.QOM.UEmptyField;

/**
 * @author Lukas Eder
 */
final class NoField<T>
extends
    AbstractField<T>
implements
    SortField<T>,
    UEmptyField<T>
{

    static final NoField<?> INSTANCE = new NoField<>(OTHER);

    NoField(DataType<T> type) {
        super(N_NULL, type);
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(inline(null, getDataType()));
    }

    // -------------------------------------------------------------------------
    // XXX: SortField API
    // -------------------------------------------------------------------------

    @Override
    public final SortOrder getOrder() {
        return SortOrder.DEFAULT;
    }

    @Override
    public final Field<T> $field() {
        return this;
    }

    @Override
    public final SortOrder $sortOrder() {
        return SortOrder.DEFAULT;
    }

    @Override
    public final NullOrdering $nullOrdering() {
        return null;
    }
}
