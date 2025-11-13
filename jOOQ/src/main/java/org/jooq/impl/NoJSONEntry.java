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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.SQLDataType.OTHER;
import static org.jooq.impl.SQLDataType.VARCHAR;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONEntry;
import org.jooq.JSONEntryValueStep;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.QOM.UNoQueryPart;



/**
 * The JSON object entry.
 *
 * @author Lukas Eder
 */
final class NoJSONEntry<T>
extends
    AbstractQueryPart
implements
    JSONEntry<T>,
    JSONEntryValueStep,
    UEmpty,
    UNoQueryPart
{

    static final NoJSONEntry<?> INSTANCE = new NoJSONEntry<>(OTHER);
    final DataType<T>           type;

    NoJSONEntry(DataType<T> type) {
        this.type = type;
    }

    @Override
    public final Field<String> key() {
        return inline(null, VARCHAR);
    }

    @Override
    public final Field<T> value() {
        return inline(null, type);
    }

    @Override
    public final <X> JSONEntry<X> value(X newValue) {
        return value(Tools.field(newValue));
    }

    @Override
    public final <X> JSONEntry<X> value(Field<X> newValue) {
        return DSL.noJsonEntry(newValue);
    }

    @Override
    public final <X> JSONEntry<X> value(Select<? extends Record1<X>> newValue) {
        return value(DSL.field(newValue));
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(DSL.key(key()).value(value()));
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<String> $key() {
        return key();
    }

    @Override
    public final Field<?> $value() {
        return value();
    }
}
