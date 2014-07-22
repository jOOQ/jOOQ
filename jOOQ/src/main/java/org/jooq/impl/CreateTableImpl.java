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

import static java.util.Arrays.asList;
import static org.jooq.Clause.CREATE_TABLE;
import static org.jooq.Clause.CREATE_TABLE_AS;
import static org.jooq.Clause.CREATE_TABLE_NAME;
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.Utils.DATA_SELECT_INTO_TABLE;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateTableAsStep;
import org.jooq.CreateTableFinalStep;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class CreateTableImpl<R extends Record> extends AbstractQuery implements

    // Cascading interface implementations for CREATE TABLE behaviour
    CreateTableAsStep<R>,
    CreateTableFinalStep {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8904572826501186329L;

    private final Table<?>    table;
    private Select<?>         select;

    CreateTableImpl(Configuration configuration, Table<?> table) {
        super(configuration);

        this.table = table;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final CreateTableFinalStep as(Select<? extends R> s) {
        this.select = s;
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        /* [pro] */
        if (asList(ACCESS, ASE, SQLSERVER, SYBASE).contains(ctx.configuration().dialect().family())) {
            acceptSelectInto(ctx);
        }
        else
        /* [/pro] */
        {
            acceptNative(ctx);
        }
    }

    private final void acceptNative(Context<?> ctx) {
        ctx.start(CREATE_TABLE)
           .start(CREATE_TABLE_NAME)
           .keyword("create table")
           .sql(" ")
           .visit(table)
           .end(CREATE_TABLE_NAME)
           .formatSeparator()
           .keyword("as")
           .formatSeparator()
           .start(CREATE_TABLE_AS)
           .visit(select)
           .end(CREATE_TABLE_AS)
           .end(CREATE_TABLE);
    }

    private final void acceptSelectInto(Context<?> ctx) {
        ctx.data(DATA_SELECT_INTO_TABLE, table);
        ctx.visit(select);
        ctx.data().remove(DATA_SELECT_INTO_TABLE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }
}
