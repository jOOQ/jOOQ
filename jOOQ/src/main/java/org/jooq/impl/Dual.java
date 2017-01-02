/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
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
final class Dual extends AbstractTable<Record> {

    private static final long          serialVersionUID = -7492790780048090156L;
    private static final Table<Record> FORCED_DUAL      = select(new Field[] { inline("X").as("DUMMY") }).asTable("DUAL");




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
            switch (ctx.family()) {






                case H2:
                case POSTGRES:
                case SQLITE:
                    break;

                case FIREBIRD:
                    ctx.literal("RDB$DATABASE");
                    break;

                case HSQLDB:
                    ctx.sql('(').sql(DUAL_HSQLDB).sql(") as dual");
                    break;

                case CUBRID:
                    ctx.literal("db_root");
                    break;

                // These dialects don't have a DUAL table. But emulation is needed
                // for queries like SELECT 1 WHERE 1 = 1



























                case DERBY:
                    ctx.literal("SYSIBM")
                       .sql('.')
                       .literal("SYSDUMMY1");
                    break;

                case MARIADB:
                case MYSQL:




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
