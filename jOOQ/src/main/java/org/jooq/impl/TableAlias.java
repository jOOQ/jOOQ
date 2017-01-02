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
 */

package org.jooq.impl;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Record;
import org.jooq.Row;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;

/**
 * @author Lukas Eder
 */
final class TableAlias<R extends Record> extends AbstractTable<R> {

    private static final long serialVersionUID = -8417114874567698325L;

    final Alias<Table<R>>     alias;
    final Fields<R>           aliasedFields;

    TableAlias(Table<R> table, String alias) {
        this(table, alias, null, false);
    }

    TableAlias(Table<R> table, String alias, boolean wrapInParentheses) {
        this(table, alias, null, wrapInParentheses);
    }

    TableAlias(Table<R> table, String alias, String[] fieldAliases) {
        this(table, alias, fieldAliases, false);
    }

    TableAlias(Table<R> table, String alias, String[] fieldAliases, boolean wrapInParentheses) {
        super(alias, table.getSchema());

        this.alias = new Alias<Table<R>>(table, alias, fieldAliases, wrapInParentheses);
        this.aliasedFields = init(fieldAliases);
    }

    /**
     * Register fields for this table alias
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private final Fields<R> init(String[] fieldAliases) {
        List<Field<?>> result = new ArrayList<Field<?>>();
        Row row = this.alias.wrapped().fieldsRow();
        int size = row.size();

        for (int i = 0; i < size; i++) {
            Field<?> field = row.field(i);
            String name = field.getName();

            if (fieldAliases != null && fieldAliases.length > i) {
                name = fieldAliases[i];
            }

            result.add(new TableFieldImpl(name, field.getDataType(), this, field.getComment(), field.getBinding()));
        }

        return new Fields<R>(result);
    }

    /**
     * Get the aliased table wrapped by this table
     */
    Table<R> getAliasedTable() {
        if (alias != null) {
            return alias.wrapped();
        }

        return null;
    }

    @Override
    public final Identity<R, ?> getIdentity() {
        return alias.wrapped().getIdentity();
    }

    @Override
    public final UniqueKey<R> getPrimaryKey() {
        return alias.wrapped().getPrimaryKey();
    }

    @Override
    public final List<UniqueKey<R>> getKeys() {
        return alias.wrapped().getKeys();
    }

    @Override
    public final List<ForeignKey<R, ?>> getReferences() {
        return alias.wrapped().getReferences();
    }

    @Override
    public final TableField<R, ?> getRecordVersion() {
        return alias.wrapped().getRecordVersion();
    }

    @Override
    public final TableField<R, ?> getRecordTimestamp() {
        return alias.wrapped().getRecordTimestamp();
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(alias);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final Table<R> as(String as) {
        return alias.wrapped().as(as);
    }

    @Override
    public final Table<R> as(String as, String... fieldAliases) {
        return alias.wrapped().as(as, fieldAliases);
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    @Override
    final Fields<R> fields0() {
        return aliasedFields;
    }

    @Override
    public Class<? extends R> getRecordType() {
        return alias.wrapped().getRecordType();
    }
}
