/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
