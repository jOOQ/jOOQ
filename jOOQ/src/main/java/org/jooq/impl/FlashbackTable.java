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

import static org.jooq.Clause.DUMMY;
import static org.jooq.impl.DSL.keyword;
import static org.jooq.impl.DSL.val;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;
import org.jooq.VersionsBetweenAndStep;

/**
 * A flashback query clause implementation.
 *
 * @author Lukas Eder
 */
class FlashbackTable<R extends Record, T>
extends AbstractTable<R>
implements VersionsBetweenAndStep<R, T> {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -7918219502110473521L;

    private final Table<R>      table;
    private final QueryPart     type;
    private final Field<?>      asOf;
    private final QueryPart     minvalue;
    private QueryPart           maxvalue;

    FlashbackTable(Table<R> table, Field<?> asOf, Field<?> minvalue, FlashbackType type) {
        super("flashbackquery");

        this.table = table;
        this.asOf = asOf;
        this.minvalue = minvalue != null ? minvalue : keyword("minvalue");
        this.type = type == FlashbackType.SCN ? keyword("scn") : keyword("timestamp");
    }

    // ------------------------------------------------------------------------
    // XXX: Flashback API
    // ------------------------------------------------------------------------

    @Override
    public final Table<R> and(T value) {
        return and(val(value));
    }

    @Override
    public final Table<R> and(Field<? extends T> field) {
        maxvalue = field;
        return this;
    }

    @Override
    public final Table<R> andMaxvalue() {
        maxvalue = keyword("maxvalue");
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: Table API
    // ------------------------------------------------------------------------

    @Override
    public final Class<? extends R> getRecordType() {
        return table.getRecordType();
    }

    @Override
    public final void toSQL(RenderContext context) {
        context.visit(table);

        if (asOf != null) {
            context.sql(" ")
                   .keyword("as of")
                   .sql(" ")
                   .visit(type)
                   .sql(" ")
                   .visit(asOf);
        }
        else {
            context.sql(" ")
                   .keyword("versions between")
                   .sql(" ")
                   .visit(type)
                   .sql(" ")
                   .visit(minvalue)
                   .sql(" ")
                   .keyword("and")
                   .sql(" ")
                   .visit(maxvalue);
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(table);

        if (asOf != null) {
            context.visit(asOf);
        }
        else {
            context.visit(minvalue).visit(maxvalue);
        }
    }

    @Override
    public final Clause clause() {
        return DUMMY;
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    @Override
    public final Table<R> as(String alias) {
        return new TableAlias<R>(this, alias, true);
    }

    @Override
    public final Table<R> as(String alias, String... fieldAliases) {
        return new TableAlias<R>(this, alias, fieldAliases, true);
    }

    @Override
    final Fields<R> fields0() {
        return new Fields<R>(table.fields());
    }

    /**
     * The flashback query clause type
     */
    enum FlashbackType {
        SCN, TIMESTAMP;
    }
}
