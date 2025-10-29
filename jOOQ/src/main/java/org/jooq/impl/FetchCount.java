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

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.countLarge;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Tools.anyMatch;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.Table;
import org.jooq.impl.QOM.UEmpty;

/**
 * @author Lukas Eder
 */
final class FetchCount<T> extends AbstractResultQuery<Record1<T>> implements UEmpty {

    private final Field<?>[]  count;
    private final Select<?>   query;
    private final DataType<T> type;

    FetchCount(Configuration configuration, Select<?> query, DataType<T> type) {
        super(configuration);

        this.count = new Field<?>[] {
            type.getFromType() == Long.class
            ? countLarge().as("c")
            : count().as("c")
        };
        this.query = query;
        this.type = type;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#17425] Without native support for nested records, the projected aliases will be off
        //          There's likely a more generic bug, related to AliasedSelect in general, not just to
        //          FetchCount, where this fix should be moved, instead
        AliasedSelect<?> s = new AliasedSelect<>(query, true, true, false);

        ctx.visit(select(count).from(
            RowAsField.NO_NATIVE_SUPPORT.contains(ctx.dialect()) && anyMatch(query.getSelect(), f -> f.getDataType().isRecord())
            ? s.as("t", Tools.EMPTY_STRING)
            : s.as("t")
        ));
    }

    @Override
    final Table<? extends Record1<T>> getTable0() {
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Class<? extends Record1<T>> getRecordType0() {
        return (Class) RecordImpl1.class;
    }

    @Override
    public final Field<?>[] getFields() {
        return count;
    }
}
