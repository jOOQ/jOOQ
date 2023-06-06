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

// ...

import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.JoinType;
import org.jooq.Name;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Select;
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableLike;
import org.jooq.TableOnConditionStep;
import org.jooq.TableOnStep;
import org.jooq.TableOptionalOnStep;
import org.jooq.TableOuterJoinStep;
import org.jooq.TablePartitionByStep;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.QOM.UTransient;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lukas Eder
 */
final class NoTableJoin
extends
    AbstractJoinTable<NoTableJoin>
implements
    UTransient
{
    final Table<?> table;

    NoTableJoin(Table<?> table) {
        super(table.getOptions(), table.getQualifiedName());

        this.table = table;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(table);
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return table.getRecordType();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    final FieldsImpl<Record> fields0() {
        return ((AbstractTable) table).fields0();
    }

    // ------------------------------------------------------------------------
    // Table API
    // ------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Override
    public final Table<Record> as(Name alias) {
        return (Table<Record>) table.as(alias);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Table<Record> as(Name alias, Name... fieldAliases) {
        return (Table<Record>) table.as(alias, fieldAliases);
    }

    // ------------------------------------------------------------------------
    // XXX: JOIN API
    // ------------------------------------------------------------------------

    @Override
    public final TableOnConditionStep<Record> onKey() {
        return this;
    }

    @Override
    public final TableOnConditionStep<Record> onKey(TableField<?, ?>... keyFields) {
        return this;
    }

    @Override
    public final TableOnConditionStep<Record> onKey(ForeignKey<?, ?> key) {
        return this;
    }

    @Override
    final NoTableJoin partitionBy0(Collection<? extends Field<?>> fields) {
        return this;
    }

    @Override
    public final NoTableJoin on(Condition conditions) {
        return this;
    }

    @Override
    public final NoTableJoin on(Condition... conditions) {
        return this;
    }

    @Override
    public final NoTableJoin using(Collection<? extends Field<?>> fields) {
        return this;
    }

    @Override
    public final NoTableJoin and(Condition c) {
        return this;
    }

    @Override
    public final NoTableJoin or(Condition c) {
        return this;
    }
}
