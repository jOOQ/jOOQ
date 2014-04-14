/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import static org.jooq.Clause.INSERT;
import static org.jooq.Clause.INSERT_INSERT_INTO;
import static org.jooq.Clause.INSERT_ON_DUPLICATE_KEY_UPDATE;
import static org.jooq.Clause.INSERT_RETURNING;
import static org.jooq.Clause.INSERT_SELECT;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Insert;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class InsertSelectQueryImpl<R extends Record> extends AbstractQuery implements Insert<R> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = -1540775270159018516L;
    private static final Clause[] CLAUSES          = { INSERT };

    private final Table<?>        into;
    private final Field<?>[]      fields;
    private final Select<?>       select;

    InsertSelectQueryImpl(Configuration configuration, Table<?> into, Field<?>[] fields, Select<?> select) {
        super(configuration);

        this.into = into;
        this.fields = (fields == null || fields.length == 0) ? into.fields() : fields;
        this.select = select;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.start(INSERT_INSERT_INTO)
           .keyword("insert into")
           .sql(" ")
           .visit(into)
           .sql(" (");

        // [#989] Avoid qualifying fields in INSERT field declaration
        boolean qualify = ctx.qualify();
        ctx.qualify(false);

        String separator = "";
        for (Field<?> field : fields) {
            ctx.sql(separator)
               .visit(field);

            separator = ", ";
        }

        ctx.qualify(qualify);
        ctx.sql(")")
           .end(INSERT_INSERT_INTO)
           .formatSeparator()
           .start(INSERT_SELECT)
           .visit(select)
           .end(INSERT_SELECT)
           .start(INSERT_ON_DUPLICATE_KEY_UPDATE)
           .end(INSERT_ON_DUPLICATE_KEY_UPDATE)
           .start(INSERT_RETURNING)
           .end(INSERT_RETURNING);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
