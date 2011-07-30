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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Table;

class DescribeQuery<R extends Record> extends AbstractQuery {

    private static final long       serialVersionUID = -2639180702290896997L;
    private static final JooqLogger log              = JooqLogger.getLogger(DescribeQuery.class);

    private final Table<R>          table;

    DescribeQuery(Configuration configuration, Table<R> table) {
        super(configuration);

        this.table = table;
    }

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(table);
    }

    @Override
    protected final int execute(Configuration configuration, PreparedStatement statement) throws SQLException {
        ResultSet rs = null;

        try {
            rs = statement.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            MetaDataFieldProvider provider = new MetaDataFieldProvider(configuration, meta);

            for (Field<?> field : provider.getFields()) {
                addField(field.getName(), field.getDataType());
            }

            if (log.isDebugEnabled()) {
                log.debug("Describing table", table + " with fields " + table.getFields());
            }
        }
        finally {
            JooqUtil.safeClose(rs);
        }

        return 0;
    }

    private final <T> void addField(String fieldName, DataType<T> fieldType) {

        // Instanciating a TableFieldImpl will add the field to [this]
        new TableFieldImpl<R, T>(fieldName, fieldType, table);
    }

    @Override
    public final void toSQL(RenderContext context) {
        Limit limit = new Limit();
        limit.setNumberOfRows(1);

        context.sql("select ");

        // TODO [#21] Refactor this and correctly implement Limit for Sybase
        if (context.getDialect() == SQLDialect.SYBASE ||
            context.getDialect() == SQLDialect.SQLSERVER) {
            context.inline(true)
                   .sql(limit)
                   .inline(false);
        }

        context.sql(" * from ");
        context.sql(table);
        context.sql(" ");

        // nothing to do here, top clause added at start of statement
        if (context.getDialect() == SQLDialect.SYBASE ||
            context.getDialect() == SQLDialect.SQLSERVER) {
        }

        // Oracle can filter by rownum
        else if (context.getDialect() == SQLDialect.ORACLE) {
            context.sql("where rownum < 1");
        }

        // Most RDBMS support a limit clause at the end
        else {
            context.inline(true)
                   .sql(limit)
                   .inline(false);
        }
    }

    @Override
    public final void bind(BindContext context) throws SQLException {
        context.bind(table);
    }
}
