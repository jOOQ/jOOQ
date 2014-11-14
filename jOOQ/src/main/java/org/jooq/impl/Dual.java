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

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.select;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
class Dual extends AbstractTable<Record> {

    private static final long          serialVersionUID = -7492790780048090156L;
    private static final Table<Record> FORCED_DUAL      = select(new Field[] { inline("X").as("DUMMY") }).asTable("DUAL");
    /* [pro] xx
    xxxxxx xxxxx xxxxxx                xxxxxxxxxxx      x xxxxxxx xxxxxxxx xxxx xxxx xxxxxxxxxxxxxxx
    xxxxxx xxxxx xxxxxx                xxxxxxxxxxxxx    x xxxxxxx x xx xxxx xxxx xxxxxxxxx xxxxx xxxxx x xxx
    xx [/pro] */
    static final String                DUAL_HSQLDB      = "select 1 as dual from information_schema.system_users limit 1";

    private final boolean              force;

    Dual() {
        this(false);
    }

    Dual(boolean force) {
        super("dual", (Schema) null);

        this.force = force;
    }

    @Override
    public final Class<? extends Record> getRecordType() {
        return RecordImpl.class;
    }

    @Override
    public final Table<Record> as(String alias) {
        if (force) {
            return FORCED_DUAL.as(alias);
        }
        else {
            return new TableAlias<Record>(this, alias);
        }
    }

    @Override
    public final Table<Record> as(String alias, String... fieldAliases) {
        if (force) {
            return FORCED_DUAL.as(alias, fieldAliases);
        }
        else {
            return new TableAlias<Record>(this, alias, fieldAliases);
        }
    }

    @Override
    public boolean declaresTables() {
        return true;
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (force) {
            ctx.visit(FORCED_DUAL);
        }
        else {
            switch (ctx.configuration().dialect().family()) {
                /* [pro] xx
                xxxx xxxx
                xxxx xxxxxxxxxx
                xx [/pro] */
                case POSTGRES:
                case SQLITE:
                    break;

                case FIREBIRD:
                    ctx.literal("RDB$DATABASE");
                    break;

                case HSQLDB:
                    ctx.sql("(").sql(DUAL_HSQLDB).sql(") as dual");
                    break;

                case CUBRID:
                    ctx.literal("db_root");
                    break;

                // These dialects don't have a DUAL table. But simulation is needed
                // for queries like SELECT 1 WHERE 1 = 1
                /* [pro] xx
                xxxx xxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxx
                    xxxxxx

                xxxx xxxx
                    xxxxxxxxxxxxxxxxxxxxx
                       xxxxxxxxx
                       xxxxxxxxxxxxxxxxx
                    xxxxxx

                xxxx xxxxxxxxx
                    xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx xx xxxxxxx
                    xxxxxx

                xxxx xxxxxxx
                    xxxxxxxxxxxxxxxxxxxx x xx xxxxx xx xxxxxxx
                    xxxxxx

                xxxx xxxxxxx
                    xxxxxxxxxxxxxxxxxx
                       xxxxxxxxx
                       xxxxxxxxxxxxxxxxxx
                    xxxxxx

                xx [/pro] */
                case DERBY:
                    ctx.literal("SYSIBM")
                       .sql(".")
                       .literal("SYSDUMMY1");
                    break;

                default:
                    ctx.keyword("dual");
                    break;
            }
        }
    }

    @Override
    final Fields<Record> fields0() {
        return new Fields<Record>();
    }
}
