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
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Schema;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class Dual extends AbstractTable<Record> {

    private static final long serialVersionUID = -7492790780048090156L;

    Dual() {
        super("dual", (Schema) null);
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final Table<Record> as(String alias) {
        return new TableAlias<Record>(this, alias);
    }

    @Override
    public final Table<Record> as(String alias, String... fieldAliases) {
        return new TableAlias<Record>(this, alias, fieldAliases);
    }

    @Override
    public final void toSQL(RenderContext context) {
        switch (context.configuration().dialect().family()) {
            case ASE:
            case POSTGRES:
            case SQLITE:
            case SQLSERVER:
                break;

            case FIREBIRD:
                context.literal("RDB$DATABASE");
                break;

            case HSQLDB:
                context.literal("INFORMATION_SCHEMA")
                       .sql(".")
                       .literal("SYSTEM_USERS");
                break;

            case CUBRID:
                context.literal("db_root");
                break;

            // These dialects don't have a DUAL table. But simulation is needed
            // for queries like SELECT 1 WHERE 1 = 1
            case INGRES:
                context.keyword("(select 1 as dual) as dual");
                break;

            case DB2:
                context.literal("SYSIBM")
                       .sql(".")
                       .literal("DUAL");
                break;

            case DERBY:
                context.literal("SYSIBM")
                       .sql(".")
                       .literal("SYSDUMMY1");
                break;

            case SYBASE:
                context.literal("SYS")
                       .sql(".")
                       .literal("DUMMY");
                break;

            default:
                context.keyword("dual");
                break;
        }
    }

    @Override
    public final void bind(BindContext context) {}

    @Override
    final Fields<Record> fields0() {
        return new Fields<Record>();
    }
}
