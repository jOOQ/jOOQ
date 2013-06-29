/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jooq.impl;

import java.util.ArrayList;
import java.util.List;

import org.jooq.BindContext;
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
        context.sql(alias);
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(alias);
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
