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
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.RenderContext;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
class SelectQueryAsField<T> extends AbstractField<T> {

    private static final long serialVersionUID = 3463144434073231750L;

    private final Select<?>   query;

    SelectQueryAsField(Select<?> query, DataType<T> type) {
        super("select", type);

        this.query = query;
    }

    @Override
    public final Field<T> as(String alias) {
        return new FieldAlias<T>(this, alias);
    }

    @Override
    public final void bind(BindContext context) {

        // If this is already a subquery, proceed
        if (context.subquery()) {
            context.bind(query);
        }
        else {
            context.subquery(true)
                   .bind(query)
                   .subquery(false);
        }
    }

    @Override
    public final void toSQL(RenderContext context) {

        // If this is already a subquery, proceed
        if (context.subquery()) {
            context.sql("(")
                   .formatIndentStart()
                   .formatNewLine()
                   .sql(query)
                   .formatIndentEnd()
                   .formatNewLine()
                   .sql(")");
        }
        else {
            context.sql("(")
                   .subquery(true)
                   .formatIndentStart()
                   .formatNewLine()
                   .sql(query)
                   .formatIndentEnd()
                   .formatNewLine()
                   .subquery(false)
                   .sql(")");
        }
    }
}
