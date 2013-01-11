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

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class InsertSelectQueryImpl<R extends Record> extends AbstractQuery implements Insert<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -1540775270159018516L;

    private final Table<?>    into;
    private final Field<?>[]  fields;
    private final Select<?>   select;

    InsertSelectQueryImpl(Configuration configuration, Table<?> into, Field<?>[] fields, Select<?> select) {
        super(configuration);

        this.into = into;
        this.fields = (fields == null || fields.length == 0) ? into.fields() : fields;
        this.select = select;
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.keyword("insert into ")
               .sql(into)
               .sql(" (");

        String separator = "";
        for (Field<?> field : fields) {
            context.sql(separator)
                   .literal(field.getName());

            separator = ", ";
        }

        context.sql(")")
               .formatSeparator()
               .sql(select);
    }

    @Override
    public final void bind(BindContext context) {
        context.bind(into);
        context.bind(fields);
        context.bind(select);
    }
}
