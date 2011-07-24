/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
public class TableImpl<R extends Record> extends AbstractTable<R> {

    private static final long serialVersionUID = 261033315221985068L;
    private final FieldList   fields;

    /**
     * @deprecated - 1.6.1 [#453] - Regenerate your schema
     */
    @Deprecated
    public TableImpl(SQLDialect dialect, String name) {
        this(dialect, name, (Schema) null);
    }

    /**
     * @deprecated - 1.6.1 [#453] - Regenerate your schema
     */
    @Deprecated
    public TableImpl(SQLDialect dialect, String name, Schema schema) {
        this(Factory.getStaticFactory(dialect), name, schema);
    }

    /**
     * @deprecated - 1.6.1 [#453] - Regenerate your schema
     */
    @SuppressWarnings("unused")
    @Deprecated
    public TableImpl(Configuration configuration, String name, Schema schema) {
        this(name, schema);
    }

    public TableImpl(String name) {
        this(name, null);
    }

    public TableImpl(String name, Schema schema) {
        super(name, schema);

        this.fields = new FieldList();
    }

    @Override
    public final List<Attachable> getAttachables0() {
        return getAttachables(fields);
    }

    @Override
    public final int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException {
        return initialIndex;
    }

    @Override
    protected final FieldList getFieldList() {
        return fields;
    }

    @Override
    public final String toSQLReference(Configuration configuration, boolean inlineParameters) {
        StringBuilder sb = new StringBuilder();

        if (getMappedSchema(configuration, getSchema()) != null) {
            sb.append(internal(getMappedSchema(configuration, getSchema())).toSQLReference(configuration, inlineParameters));
            sb.append(".");
        }

        sb.append(JooqUtil.toSQLLiteral(configuration, getMappedTable(configuration, this).getName()));

        return sb.toString();
    }

    @Override
    public final Table<R> as(String alias) {
        return new TableAlias<R>(this, alias);
    }

    /**
     * Subclasses must override this method if they use the generic type
     * parameter <R> for other types than {@link Record}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends R> getRecordType() {
        return (Class<? extends R>) RecordImpl.class;
    }
}
