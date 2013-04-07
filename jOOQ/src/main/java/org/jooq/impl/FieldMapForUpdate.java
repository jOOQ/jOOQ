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

import static java.util.Arrays.asList;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;

import java.util.Map;

import org.jooq.BindContext;
import org.jooq.Field;
import org.jooq.RenderContext;

/**
 * @author Lukas Eder
 */
class FieldMapForUpdate extends AbstractQueryPartMap<Field<?>, Field<?>> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -6139709404698673799L;

    FieldMapForUpdate() {
    }

    @Override
    public final void toSQL(RenderContext context) {
        if (size() > 0) {
            String separator = "";

            // [#989] Some dialects do not support qualified column references
            // in the UPDATE statement's SET clause

            // [#2055] Other dialects require qualified column references to
            // disambiguated columns in queries like
            // UPDATE t1 JOIN t2 .. SET t1.val = ..., t2.val = ...
            boolean restoreQualify = context.qualify();
            boolean supportsQualify = asList(POSTGRES, SQLITE).contains(context.configuration().dialect()) ? false : restoreQualify;

            for (Entry<Field<?>, Field<?>> entry : entrySet()) {
                context.sql(separator);

                if (!"".equals(separator)) {
                    context.formatNewLine();
                }

                context.qualify(supportsQualify)
                       .sql(entry.getKey())
                       .qualify(restoreQualify)
                       .sql(" = ")
                       .sql(entry.getValue());

                separator = ", ";
            }
        }
        else {
            context.sql("[ no fields are updated ]");
        }
    }

    @Override
    public final void bind(BindContext context) {
        for (Entry<Field<?>, Field<?>> entry : entrySet()) {
            context.bind(entry.getKey());
            context.bind(entry.getValue());
        }
    }

    final void set(Map<? extends Field<?>, ?> map) {
        for (Entry<? extends Field<?>, ?> entry : map.entrySet()) {
            Field<?> field = entry.getKey();
            Object value = entry.getValue();

            put(entry.getKey(), Utils.field(value, field));
        }
    }
}
