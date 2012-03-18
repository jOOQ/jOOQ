/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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

import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class TableAlias<R extends Record> extends AbstractTable<R> {

    private static final long                 serialVersionUID = -8417114874567698325L;

    private final AliasProviderImpl<Table<R>> aliasProvider;
    private FieldList                         aliasedFields;

    TableAlias(Table<R> table, String alias) {
        this(table, alias, false);
    }

    TableAlias(Table<R> table, String alias, boolean wrapInParentheses) {
        super(alias, table.getSchema());

        this.aliasProvider = new AliasProviderImpl<Table<R>>(table, alias, wrapInParentheses);
    }

    @Override
    public final List<ForeignKey<R, ?>> getReferences() {
        return aliasProvider.getAliasProvider().getReferences();
    }

    @Override
    public final List<Attachable> getAttachables0() {
        return getAttachables(aliasProvider, aliasedFields);
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.sql(aliasProvider);
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(aliasProvider);
    }

    @Override
    public final Table<R> as(String alias) {
        return aliasProvider.as(alias);
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    @Override
    protected FieldList getFieldList() {
        if (aliasedFields == null) {
            aliasedFields = new FieldList();

            for (Field<?> field : aliasProvider.getAliasProvider().getFields()) {
                registerTableField(field);
            }
        }

        return aliasedFields;
    }

    /**
     * Register a field for this table alias
     */
    private <T> void registerTableField(Field<T> field) {
        aliasedFields.add(new TableFieldImpl<R, T>(field.getName(), field.getDataType(), this));
    }

    @Override
    public Class<? extends R> getRecordType() {
        return aliasProvider.getAliasProvider().getRecordType();
    }
}
