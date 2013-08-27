/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */

package org.jooq.impl;

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Row;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class TableAlias<R extends Record> extends AbstractTable<R> {

    private static final long     serialVersionUID = -8417114874567698325L;

    private final Alias<Table<R>> alias;
    private final Fields<R>       aliasedFields;

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

            result.add(new TableFieldImpl(name, field.getDataType(), this));
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
    public final List<ForeignKey<R, ?>> getReferences() {
        return alias.wrapped().getReferences();
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.visit(alias);
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(alias);
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
