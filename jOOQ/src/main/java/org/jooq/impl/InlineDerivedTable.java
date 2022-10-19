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

import static org.jooq.impl.DSL.selectFrom;

import org.jooq.Condition;
import org.jooq.QueryPart;
import org.jooq.Record;
// ...
import org.jooq.Table;
// ...

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class InlineDerivedTable<R extends Record> extends DerivedTable<R> {

    final Table<R>  table;
    final Condition condition;

    InlineDerivedTable(Table<R> table, Condition condition) {
        super(selectFrom(table).where(condition), table.getUnqualifiedName());

        this.table = table;
        this.condition = condition;
    }

    @Override
    final FieldsImpl<R> fields0() {
        return new FieldsImpl<>(table.as(table).fields());
    }
























}
